/*
 * Copyright © 2023 Institut fuer Kommunikationstechnik - FH-Dortmund (codebase.ikt@fh-dortmund.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ict.kura.driver.multisensor.simulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.ict.gson.utils.AdapterFactory;
import org.ict.kura.internal.driver.mqtt.MultisensorDriver;
import org.ict.kura.internal.driver.multisensor.mdns.MdnsService;
import org.ict.kura.internal.driver.multisensor.mdns.MdnsServiceFactory;
import org.ict.model.wot.core.ActionAffordance;
import org.ict.model.wot.core.PropertyAffordance;
import org.ict.model.wot.core.Thing;
import org.ict.model.wot.hypermedia.Form;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * The multisensor simulation.
 * 
 * @author ICT N. Karaoglan
 * @author ICT M. Biskup
 * @author ICT M. Kuller
 * @version 2021-06-18
 */
public class MultisensorSimulator {
	/* log4j logger reference for class Start_T */
	private static final Logger LOGGER = LogManager.getFormatterLogger(MultisensorSimulator.class);

	/** Properties parameters */
	private static String PROP_FILE;
	private static Properties propFile;

	/* The threaDocumentsd cached pool to manage multiple state changes */
	private static ExecutorService executorService;

	/* Register service */
	private static MdnsService mdnsServiceR;

	/* DNS service type e.g. for HTTP */
	private static String serviceType;

	/* DNS symbolic service name */
	private static String serviceName;

	/* IP under which the service is available */
	private static String serviceIp;

	/* Port under which the service is available */
	private static int servicePort;

	/* MQTT client */
	private static MqttClient mqttClient;

	/* MQTT connection options */
	@SuppressWarnings("unused")
	private static MqttConnectOptions mqttConnectOptions;

	/* MQTT memory persistence */
	private static MemoryPersistence memoryPersistence = new MemoryPersistence();

	/* MQTT qos */
	private static int MQTT_QOS = 2;

	/* MQTT ip */
	private static String mqttIp;

	/* MQTT port */
	private static int mqttPort;

	/* WoT file */
	// private static String wotFile;

	/* Thing description object */
	private static Thing thing = null;

	/* Gson for json */
	private static Gson gson;

	/* JAVA, S. 290: Reading Strings */
	final private static BufferedReader brConsole = new BufferedReader(new InputStreamReader(System.in));

	/* List of created command task */
	private static ArrayList<CommandTask> commandTasks = new ArrayList<>();

	/*
	 * Only for the return-Code of the function Dialog to control the application
	 * from the command line.
	 */
	@SuppressWarnings("unused")
	private static String ret;

	/**
	 * DIALOG: Displays some informations to use System.out and waiting in blocking
	 * mode of input from System.in.
	 * 
	 * @param s The display info.
	 * @return The user defined input.
	 */
	public static String Dialog(String s) {
		LOGGER.info(s);
		try {
			return brConsole.readLine();
		} catch (Throwable T) {
			return null;
		}
	} // End of DIALOG

	public static void main(String[] args) {
		try {
			/* Trace info */
			LOGGER.info("Application '%s'", MultisensorDriver.class);

			/* Checks the number of arguments */
			if (args.length != 1) {
				LOGGER.info("Number of arguments: %d', expect 1 parameter: ... the configuration file", args.length);
				throw new IllegalArgumentException("Number of arguments: args.length != 1, not allowed!");
			}

			/** Initializes the property file from argument */
			PROP_FILE = args[0];
			File file = new File(PROP_FILE);

			/* Checks if the property file exists */
			if (file.exists() == false)
				throw new IllegalArgumentException(String.format("BAD_PARAM: PROP_FILE does not exists"));

			/* Creates a new property file object */
			propFile = new Properties();

			/* Reads the property file parameters */
			propFile.load(new FileInputStream(PROP_FILE));

			LOGGER.info("Loaded configuration file '%s'", propFile.toString());

			/* Initializes the WoT file from argument */
			// wotFile = propFile.getProperty("wot_file");
			File wotFile = new File(propFile.getProperty("wot_file"));

			/* Checks if the WoT file exists */
			if (wotFile.exists() == false)
				throw new IllegalArgumentException(String.format("BAD_PARAM: WotFile does not exists"));

			/* Creates the thing from file */
			thing = createThingFromJsonFile(propFile.getProperty("wot_file"));

			LOGGER.info("Loaded thing description '%s'", thing.toString());

			/* Initializes the service type from argument */
			serviceType = propFile.getProperty("mdns_service_type");

			/* Initializes the service name from argument */
			serviceName = thing.getTitle();

			/* Initializes the ip from argument */
			serviceIp = propFile.getProperty("mdns_service_ip");

			/* Initializes the port from argument */
			servicePort = Integer.parseInt(propFile.getProperty("mdns_service_port"));

			/* Initializes the mqtt server ip from argument */
			mqttIp = propFile.getProperty("mqtt_server_ip");

			/* Initializes the mqtt server port from argument */
			mqttPort = Integer.parseInt(propFile.getProperty("mqtt_server_port"));

			LOGGER.info("Starts the JMDNS register service ...");

			/* Creates a service description */
			Map<String, String> serviceDescription = new HashMap<String, String>();
			serviceDescription.put("MAC", thing.getId().toString());

			LOGGER.info("serviceType '%s', serviceName '%s', servicePort '%d', serviceIp '%s' ", serviceType,
					serviceName, servicePort, InetAddress.getByName(serviceIp));

			/* Starts the register service */
			mdnsServiceR = MdnsServiceFactory.createRegister(serviceType, serviceName, serviceDescription, servicePort,
					InetAddress.getByName(serviceIp));

			LOGGER.info("dnsServicR: " + mdnsServiceR.toString());

			LOGGER.info(String.format("MQTT endpoint: tcp://%s:%d", mqttIp, mqttPort));
			/* Creates a MQTT client */
			MqttClient mqttClient = new MqttClient(String.format("tcp://%s:%d", mqttIp, mqttPort),
					thing.getId().toString(), memoryPersistence);

			/* Creates MQTT connection options */
			MqttConnectOptions connOpts = new MqttConnectOptions();

			/* Creates a new clean MQTT session */
			connOpts.setCleanSession(true);
			connOpts.setUserName(propFile.getProperty("mqtt_username"));
			connOpts.setPassword(propFile.getProperty("mqtt_password").toCharArray());

			/* Connects the MQTT server */
			mqttClient.connect(connOpts);

			/* Gets the file content as byte array */
			MqttMessage message = new MqttMessage(Files.readAllBytes(Paths.get(propFile.getProperty("wot_file"))));

			/*
			 * Publishes the message with wot description to the given topic, true =
			 * retained message
			 */
			mqttClient.publish(thing.getId().toString(), message.getPayload(), MQTT_QOS, true);

			/* Starts the tcom.google.gson.Gson;hread cache pool immediately */
			executorService = Executors.newCachedThreadPool();

			/* Parses the WoT file */
			createTasks(thing, mqttClient, MQTT_QOS);

			LOGGER.info("%d CommandTasks started", commandTasks.size());

			/** Waits for user input from command line */
			ret = Dialog(String.format(
					"%tT: Exit program - press terminate button in Eclipse IDE, press key 'return' or press key 'q', continue program - press any other key",
					Calendar.getInstance()));

		} catch (Throwable T) {
			/** Trace error */
			LOGGER.catching(T);
		} finally {
			try {
				/* Closes the command tasks */
				for (int i = 0; i < commandTasks.size(); i++)
					commandTasks.get(i).close();

				/* Closes the register service */
				if (mdnsServiceR != null) {
					mdnsServiceR.getJmdns().unregisterAllServices();
					mdnsServiceR.close();
				}

				/* Shutdown the thread cache pool immediately */
				if (executorService != null)
					executorService.shutdownNow();

				/* Closes the mqtt service */
				if (mqttClient != null)
					mqttClient.close();
			} catch (Throwable T) {
				/** Trace error */
				LOGGER.catching(T);
			}
			/** Trace info */
			LOGGER.info("Application shutdown !");
		}
	}

	private static Thing createThingFromJsonFile(String filePath) {
		/* The thing object */
		Thing thing = null;

		try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
			/* Creates a json object */
			String content = lines.collect(Collectors.joining(System.lineSeparator()));
			gson = AdapterFactory.getGsonWithDefaultWotTypeAdapters(true);
			JsonObject jsonObject = gson.fromJson(content, JsonObject.class);

			/* Creates a thing from json object */
			thing = gson.fromJson(jsonObject, Thing.class);
			LOGGER.debug(gson.toJson(thing));
		} catch (Throwable t) {
			LOGGER.error("", t);
		}
		return thing;
	}

	/**
	 * Extracts the hrefs from the thing properties, which are used as mqtt topics.
	 * 
	 * @param thing the web of thing object
	 * @return the map with property names and hrefs
	 */
	private static void createTasks(Thing thing, MqttClient mqttClient, int mqttQos) {
		/* Property, action form hrefs */
		String propertyFormHref = null, actionFormHref = null;
		/* A property corresponds with an action */
		boolean pairActionProperty = false;

		try {
			LOGGER.info("Try to get the PropertyAffordance list ...");
			/* Gets the href from the properties */
			for (PropertyAffordance property : nullGuard(thing.getPropertiesRDF())) {
				/* Extracts the property form href */
				for (Form form : nullGuard(property.getForms())) {
					propertyFormHref = form.getHref().toString();
				}
				LOGGER.info("Try to get the corresponding ActionAffordance list ...");
				for (ActionAffordance action : nullGuard(thing.getActionsRDF())) {
					/* If true the action corresponds with a property */
					if (action.getName().equals(property.getName()) == true) {
						/* Extracts the action form href */
						for (Form form : nullGuard(action.getForms())) {
							actionFormHref = form.getHref().toString();
						}
						/* Creates a new command task */
						CommandTask commandTask = new CommandTask(mqttClient, property.getName(), propertyFormHref,
								action.getName(), actionFormHref, mqttQos);
						commandTasks.add(commandTask);
						LOGGER.info(
								"Command task started prop name: %s, prop form href: %s, act name: %s, act form href: %s",
								property.getName(), propertyFormHref, action.getName(), actionFormHref);
						pairActionProperty = true;
						break;
					}
				}
				/* If true the property corresponds with an action */
				if (pairActionProperty == true) {
					pairActionProperty = false;
					break;
				}
				/*
				 * If false the property not corresponded with an action number of messages
				 * from propfile. Try/catch e.g. the thing description property/action name not
				 * corresponds with the MultisensorSimulation configuration.
				 */
				try {
					LOGGER.info(
							"Executor services submitted with property.getName(): %s, property.getName()_task_delay: %s, property.getName()_task_delay: %s",
							property.getName(), property.getName() + "_task_delay",
							property.getName() + "_task_message_number");
					executorService.submit(new TelemetryTask(mqttClient, property.getName(), propertyFormHref, MQTT_QOS,
							Integer.parseInt(propFile.getProperty(property.getName() + "_task_delay")),
							Integer.parseInt(propFile.getProperty(property.getName() + "_task_message_number"))));

					LOGGER.info("Telemetry task started prop name: %s, prop form href: %s", property.getName(),
							propertyFormHref);
				} catch (Throwable t) {
					LOGGER.error("", t);
				}
			}
		} catch (Throwable t) {
			LOGGER.error("", t);
		}
	}

	/**
	 * The helper class to check if the list is null, so create an empty list.
	 * 
	 * @param list the list to be checked
	 * @return if null return an empty list otherwise the given list
	 * @version 2020-11-23
	 */
	private static <T> List<T> nullGuard(List<T> list) {
		if (list == null) {
			LOGGER.info("List is null!");
			return Collections.emptyList();
		} else {
			return list;
		}
	}
}


