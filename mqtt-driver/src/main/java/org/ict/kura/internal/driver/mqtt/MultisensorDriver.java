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
package org.ict.kura.internal.driver.mqtt;

import static java.util.Objects.requireNonNull;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import org.eclipse.kura.KuraException;
import org.eclipse.kura.channel.ChannelRecord;
import org.eclipse.kura.configuration.ConfigurableComponent;
import org.eclipse.kura.data.DataService;
import org.eclipse.kura.data.listener.DataServiceListener;
import org.eclipse.kura.driver.Driver;
import org.eclipse.kura.driver.PreparedRead;
import org.ict.gson.utils.AdapterFactory;
import org.ict.kura.asset.creator.thing.util.ThingPreparedRead;
import org.ict.kura.driver.thing.ThingChannelListener;
import org.ict.kura.driver.thing.ThingDriver;
import org.ict.kura.internal.driver.mqtt.util.MultisensorBindingConfig;
import org.ict.kura.internal.driver.multisensor.mdns.MdnsService;
import org.ict.kura.internal.driver.multisensor.mdns.MdnsServiceFactory;
import org.ict.kura.thing.creator.ThingProvider;
import org.ict.model.wot.core.ActionAffordance;
import org.ict.model.wot.core.Thing;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The multisensor driver implementation based on a WoT description.
 * 
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2020-12-17
 */
/* Annotation to point a {@link MultisensorDriverConfig} class */
@Designate(ocd = MultisensorDriverConfig.class, factory = true)
/* Annotation to create the component.xml from source code */
@Component(name = "org.ict.kura.driver.multisensor", immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE, property = "service.pid=org.ict.kura.driver.multisensor")
public class MultisensorDriver extends ThingDriver<MultisensorBindingConfig>
		implements Driver, ConfigurableComponent, DataServiceListener, ServiceListener {
	/* The logger instance */
	private static final Logger LOGGER = LoggerFactory.getLogger(MultisensorDriver.class);

	/* The unique driver app id - in this case the driver package name */
	private static final String APP_ID = "org.ict.kura.driver.multisensor";

	/* The driver keepalived topic */
	private static final String THINGS_TOPIC = "things/";

	/* The gson object to convert json string to {@link Thing} */
	private static final Gson gsonThing = AdapterFactory.getGsonWithDefaultWotTypeAdapters(true);

	private static final JsonParser parser = new JsonParser();

	/* The driver properties defined by the user in the Kura web admin interface */
	@SuppressWarnings("unused")
	private Map<String, Object> properties;

	/* The driver options */
	private MultisensorDriverOptions options;

	/*
	 * The {@link MultisensorBindingConfig} sorted by the href. The key is the form
	 * href. The value (T) is the {@link MultisensorBindingConfig}.
	 */
	private Map<String, MultisensorBindingConfig> multisensorHrefBindingConfigurations = new HashMap<>();

	/* List of discovered things (Ids) by {@link MdnsService} */
	private Set<String> thingIds = new HashSet<String>();

	/* List of mqtt topics */
	private List<String> descriptionMqttTopics = new ArrayList<String>();

	/*
	 * The Kura {@link DataService} - for this you must configure a new Cloud
	 * Connection.
	 */
	private DataService dataService;

	/* The {@link MdnsService} to discover services */
	private MdnsService mdnsService;

	/* The executor service to handle the MDNS discovery start phase */
	private ScheduledExecutorService scheduledExecutorService;

	/* MDNS discovery start delay in milli seconds */
	private int MDNS_DISCOVERY_START_DELAY = 2000;

	/* Service information of the {@link MdnsService} discover */
	private TreeMap<String, ServiceInfo> serviceInfos = new TreeMap<>();

	/**
	 * Binding method which starts the bundle, see component.xml, is called by the
	 * OSGi framework
	 * 
	 * @throws KuraException
	 */
	@Activate
	protected void activate(ComponentContext componentContext, Map<String, Object> properties) {
		LOGGER.info("Bundle " + APP_ID + " has started with config!");

		/* Updates the configuration */
		doUpdate(properties);
	}

	/**
	 * Binding method to shutdown the bundle, see component.xml, is called by the
	 * OSGi framework.
	 * 
	 * @param componentContext The OSGi component informations of this bundle - in
	 *                         this case we do nothing there.
	 */
	@Deactivate
	protected void deactivate(ComponentContext componentContext) {
		LOGGER.info("Bundle " + APP_ID + " has stopped!");

		/* Closes all resources */
		doDeactivate();
	}

	/**
	 * Handles the configuration updates.
	 * 
	 * @param properties the configuration parameters, which are configured via the
	 *                   kura admin web interface
	 */
	@Modified
	public void updated(Map<String, Object> properties) {
		LOGGER.info("Bundle " + APP_ID + " has updated!");
		/*
		 * Deactivates all configurations, because it isn't able to modify an existing
		 * configuration
		 */
		doDeactivate();

		/* Updates all components with new configuration */
		doUpdate(properties);
	}

	/**
	 * Updates all resources.
	 * 
	 * @param properties the configuration parameters {@link #updated(Map<String,
	 *                   Object> properties)}
	 */
	private void doUpdate(Map<String, Object> properties) {
		LOGGER.info("doUpdate...");

		/*
		 * Creates a new options instance - for easy access to the bundle configuration
		 * parameters
		 */
		this.options = new MultisensorDriverOptions(properties);
		LOGGER.info("Options content: {}", options.toString());

		/*
		 * If autoCreateConfig is enabled, then a kura configuration (with assets and
		 * channels) is created, in this a discovery MDNS instance will be started.
		 */
		if (options.getAutoCreateConfig() == false) {
			try {
				/* Activates the MDNS service */
				if (options.getMdnsEnable() == true) {
					LOGGER.info("Starts the MDNS discovery task with {} delay, status of the dataService {}",
							MDNS_DISCOVERY_START_DELAY, this.dataService);

					/* Creates the executor service */
					scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
					/* Starts the MDNS discovery instance with delay */
					scheduledExecutorService.schedule(mdnsDiscoveryTask, MDNS_DISCOVERY_START_DELAY,
							TimeUnit.MILLISECONDS);
				}

				LOGGER.info("Starts the description MQTT topic parsing");
				/* Parses the topics */
				descriptionMqttTopics = Collections.list(new StringTokenizer(options.getDescriptionMqttTopic(), ";"))
						.stream().map(token -> (String) token).collect(Collectors.toList());
				LOGGER.info("Alternative additional description MQTT topis {}", descriptionMqttTopics.toString());

				/*
				 * Subscribes to the topic, if the MQTT connection is active. If the MQTT
				 * connection is not active the subscribe process is started in the method {link
				 * #onConnectionEstablished}.
				 */
				if (this.dataService != null && descriptionMqttTopics.size() != 0) {
					LOGGER.info(
							"The MQTT connection is established, subscribing to the alternative additional description MQTT topics");
					for (String descriptionMqttTopic : descriptionMqttTopics) {
						this.dataService.subscribe(descriptionMqttTopic, 1);
					}
				}
			} catch (Throwable e) {
				LOGGER.error("", e);
			}
		}
	}

	/**
	 * Deactivates all resources, means deletes all assets and devices - bundle
	 * shutdown.
	 * 
	 * @param updated false if bundle is in activate mode, true if bundle is in
	 *                updated mode
	 */
	private void doDeactivate() {
		LOGGER.info("doDeactivate...");

		try {
			/* Closes the ExecutorService */
			if (scheduledExecutorService != null) {
				scheduledExecutorService.shutdownNow();
			} else {
				LOGGER.info("ExecutorService is null");
			}

			/* Closes the MDNS service */
			if (mdnsService != null) {
				mdnsService.close();
				mdnsService = null;
			} else {
				LOGGER.info("MDNSService is null");
			}

			/* Unsubscribes all listeners from the MQTT topics */
			thingIds.stream().forEach(topic -> {
				try {
					dataService.unsubscribe(topic);
				} catch (Exception e) {
					LOGGER.error("" + e);
				}
			});
			/* Clears the internal thing id list */
			thingIds.clear();

			/* Deletes all assets and channels */
			getThingProvider().deleteAssetsWithChannels(options.getDriverServicePID());
		} catch (Exception e) {
			LOGGER.error("" + e);
		}
	}

	/*
	 * This annotation adds the event admin service methods to the
	 * OSGI-INF/org.ict.kura.driver.multisensor.xml from source code
	 */
	@Reference(name = "DataService", service = DataService.class, cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC, policyOption = ReferencePolicyOption.GREEDY, unbind = "unsetDataService", bind = "setDataService")

	/**
	 * This methods receives the {@link DataService} reference from OSGI
	 * 
	 * @param dataService the Kura {@link DataService} to send events to a MQTT
	 *                    topic
	 */
	protected void setDataService(DataService dataService) {
		LOGGER.info("... setDataService");
		this.dataService = dataService;
		this.dataService.addDataServiceListener(this);
	}

	/**
	 * Removes the {@link DataService} - sets the instance equal null.
	 * 
	 * @param dataService the Kura {@link DataService} to send events
	 */
	protected void unsetDataService(DataService dataService) {
		LOGGER.info("... unsetDataService");
		if (this.dataService == dataService) {
			this.dataService.removeDataServiceListener(this);
			this.dataService = null;
		}
	}

	/*
	 * This annotation adds the {@link EventAdmin} service methods to the
	 * OSGI-INF/org.ict.kura.driver.multisensor.xml from source code
	 */
	@Reference(name = "EventAdmin", service = EventAdmin.class, cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, bind = "setEventAdmin", unbind = "unsetEventAdmin")

	/**
	 * This methods receives the {@link EventAdmin} from OSGI
	 * 
	 * @param eventAdmin the OSGI {@link EventAdmin} service to send events
	 */
	public void setEventAdmin(EventAdmin eventAdmin) {
		try {
			this.setEA(eventAdmin);
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	/**
	 * Removes the {@link EventAdmin} - sets the instance equal null.
	 * 
	 * @param eventAdmin the OSGI {@link EventAdmin} service to send events
	 */
	public void unsetEventAdmin(EventAdmin eventAdmin) {
		this.setEA(null);
	}

	/*
	 * This annotation adds the {@link EventAdmin} service methods to the
	 * OSGI-INF/org.ict.kura.driver.multisensor.xml from source code
	 */
	@Reference(name = "AssetProvider", service = ThingProvider.class, cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, bind = "setAssetProvider", unbind = "unsetAssetProvider")

	/**
	 * This method receives the {@link ThingProvider} from OSGI
	 * 
	 * @param assetProvider the OSGI {@link ThingProvider} service to handle KURA
	 *                      assets and channels
	 */
	public void setAssetProvider(ThingProvider assetProvider) {
		this.setThingProvider(assetProvider);
	}

	/**
	 * Removes the {@link ThingProvider} - sets the instance equal null.
	 * 
	 * @param assetProvider the OSGI {@link ThingProvider} service to handle KURA
	 *                      assets and channels
	 */
	public void unsetAssetProvider(ThingProvider thingProvider) {
		this.setThingProvider(null);
	}

	@Override
	/**
	 * Here we do nothing. In this method we can e.g. implement a real connection to
	 * other physical gateways, sensors and actuators.
	 */
	public void connect() throws ConnectionException {
		LOGGER.info("...connect");
	}

	@Override
	/**
	 * Here we do nothing. In this method we can e.g. implement a real disconnection
	 * to other physical gateways, sensors and actuators.
	 */
	public void disconnect() throws ConnectionException {
		LOGGER.info("...disconnect");
	}

	@Override
	/**
	 * Creates a {@link PreparedRead} instance contains the {@link ChannelRecord}
	 * list.
	 */
	public PreparedRead prepareRead(List<ChannelRecord> channelRecords) {
		LOGGER.info("...prepareRead");
		requireNonNull(channelRecords, "The channelRecords list must not be null");

		/* Creates the {@link PreparedRead} instance */
		try (ThingPreparedRead preparedRead = new ThingPreparedRead(this, channelRecords)) {
			preparedRead.getChannelRecords().stream()
					.forEach(o -> LOGGER.info("Prepare Read for chanel {}", o.getChannelName()));
			return preparedRead;
		}
	}

	@Override
	public MultisensorBindingConfig createBinding(String jsonBinding) {

		/* Creates a JSON Object from jsonValue */
		JsonObject jsonObject = new JsonParser().parse(jsonBinding).getAsJsonObject();

		LOGGER.info("...createBinding: jsonBinding {}", jsonObject.get("href"));

		/* Creates a technology binding configuration from json object */
		final MultisensorBindingConfig multisensorBindingConfig = new MultisensorBindingConfig();
		multisensorBindingConfig.setHref(jsonObject.get("href").getAsString());
		// multisensorBindingConfig.setControlPacketValue(jsonObject.get("mqv:controlPacketValue").getAsString());

		LOGGER.info("Created multisensorBindingConfig {}", multisensorBindingConfig);

		/*
		 * Saves the technology binding configuration by the group address - we need
		 * this in the method {@link #groupWrite()}
		 */
		multisensorHrefBindingConfigurations.put(multisensorBindingConfig.getHref(), multisensorBindingConfig);

		try {
			/* Subscribes to the MQTT topic with the given form href */
			this.dataService.subscribe(multisensorBindingConfig.getHref(), 1);
		} catch (Exception e) {
			LOGGER.error("", e);
		}

		/* Returns a technology binding configuration */
		return multisensorBindingConfig;
	}

	// **************** Section: Binding ***********

	@Override
	public Optional<JsonObject> doRead(MultisensorBindingConfig multisensorBindingConfig) {
		try {
			LOGGER.info("...doRead ... here we do nothing. The Multisensor driver receives the "
					+ "last current sensor/actuator status automatically via MQTT subscribe (callback).");
			return Optional.empty();
		} catch (Exception ex) {
			LOGGER.error("", ex);
			return Optional.empty();
		}
	}

	@Override
	public void doWrite(MultisensorBindingConfig multisensorBindingConfig, ActionAffordance actionAffordance,
			JsonObject jsonValue) {
		try {
			LOGGER.info("...doWrite jsonValue {}", jsonValue);

			// ################################################################################################

			LOGGER.info("...writing the value {}, to the href {} !", jsonValue, multisensorBindingConfig.getHref());

			/*
			 * Sends the new value to the MQTT topic action directly - here with the data
			 * service. After the actuator receives the command, it sends the new status
			 * back to the property topic, which is received with the thing channel listener
			 * and updates the status value in the thing channel listener and in the Kura
			 * framework. See {@link ThingChannelListener} .
			 */
			dataService.publish(multisensorBindingConfig.getHref(), jsonValue.toString().getBytes(), 1, true, 1);
			// ################################################################################################
		} catch (Exception ex) {
			LOGGER.error("", ex);
		}
	}

	/*************************************
	 * THE DATA SERVICE CALLBACK METHODS
	 *************************************/

	@Override
	public void onConnectionEstablished() {
		try {
			LOGGER.info("On connection established: ");

			LOGGER.info("Number of multisensorHrefBindingConfigurations: {}",
					multisensorHrefBindingConfigurations.size());
			/* Subscribes to the topics after reconnect ! */
			for (String key : multisensorHrefBindingConfigurations.keySet()) {
				/* Subscribes to the MQTT topic with the given form href */
				this.dataService.subscribe(key, 1);
			}

			LOGGER.info("Number of descriptionMqttTopics: {}", descriptionMqttTopics.size());
			/* Subscribes to the topics after first connect and reconnect ! */
			for (String descriptionMqttTopic : descriptionMqttTopics) {
				this.dataService.subscribe(descriptionMqttTopic, 1);
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	@Override
	public void onDisconnecting() {
		LOGGER.info("On disconnected: ");
	}

	@Override
	public void onDisconnected() {
		LOGGER.info("On connected: ");
	}

	@Override
	public void onConnectionLost(Throwable cause) {
		LOGGER.info("On connection lost: ");
	}

	@Override
	public void onMessageArrived(String topic, byte[] payload, int qos, boolean retained) {
		LOGGER.info("On message arrived from topic {} with payload {}", topic, new String(payload));

		/* Is this a property payload (if) or a thing description (else) */
		if (multisensorHrefBindingConfigurations.containsKey(topic)) {
			/*
			 * Receives a payload from a property (channel) topic and updates this in the
			 * corresponding thing driver channel listener
			 */

			/* Gets the multisensor binding configuration with the given topic (key) */
			MultisensorBindingConfig multisensorBindingConfig = multisensorHrefBindingConfigurations.get(topic);

			/*
			 * Gets the method {@link ThingChannelListenerImpl#update} and update the new
			 * value in the KURA environment}
			 */
			ThingChannelListener listener = getUpdateBindingConfigurations().get(multisensorBindingConfig);
			if (listener.getPropertyName().equals("vibration")) {
				vibrationconverter(listener, new String(payload));
			} else {
				listener.doUpdate(new String(payload));
			}
		} else {
			/*
			 * Receives a payload from a thing (channel) topic - the web of thing
			 * description and creates a thing instance from the web of description.
			 */
			Thing thing = gsonThing.fromJson(new String(payload), Thing.class);

			LOGGER.info("Try to create new assets and channel for thing id: {}", thing.getAtIdRDF());
			LOGGER.info("Try to create new assets and channel for thing id: {}", thing.getAtId());
			LOGGER.info("Try to create new assets and channel for thing id: {}", thing.getDescription());

			try {
				/* Create Asset and Channels for the thing */
				sendThingEvent(thing);
			} catch (Exception e) {
				LOGGER.error("", e);
			}
		}
	}

	@Override
	public void onMessagePublished(int messageId, String topic) {
		LOGGER.info("On message published: ");
	}

	@Override
	public void onMessageConfirmed(int messageId, String topic) {
		LOGGER.info("On message confirmed: ");
	}

	/*************************************
	 * THE MDNS SERVICE CALLBACK METHODS
	 *************************************/

	@Override
	public void serviceAdded(ServiceEvent event) {
		LOGGER.info("DNS service added: " + event.getInfo());
	}

	@Override
	public void serviceRemoved(ServiceEvent event) {
		LOGGER.info("DNS service removed: " + event.getInfo());
		/* Deletes from service info */
		serviceInfos.remove(event.getInfo().getURLs()[0]);
	}

	@Override
	public void serviceResolved(ServiceEvent event) {
		LOGGER.info("DNS service resolved: {} with URL {}", event.getInfo(), event.getInfo().clone().getURLs());

		/* Puts the discovered MDNS service into the internal service info map */
		serviceInfos.put(event.getInfo().clone().getURLs()[0], event.getInfo());
		/* Extracts the unique device id (MAC) from the MDNS service configuration */
		String thingId = event.getInfo().getPropertyString("MAC");

		/* Special implementation if the MDNS MAC Tag contains only the MAC address */
		if (thingId.contains("thing") == false)
			/* Adds the "things" semantic */
			thingId = "things/" + thingId;

		LOGGER.info("Discovered the thing with id: " + thingId);
		/*
		 * Adds the discovered thing id into the internal thing id list - if the id does
		 * not exists.
		 */
		thingIds.add(thingId);
		try {
			/*
			 * Subscribes to the thing id MQTT topic to receive the thing description of the
			 * thing.
			 */
			this.dataService.subscribe(thingId, 1);
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		LOGGER.info("Number of actual discovered things: " + thingIds.size());
	}

	/*
	 * The MDNS discovery task. In start phase it is possible, that the MDNS service
	 * discovery is faster than the initialization of the {@link DataService}. So,
	 * here we are waiting empirically for
	 */
	public Runnable mdnsDiscoveryTask = () -> {
		try {
			LOGGER.info("MDNS discovery task started");

			/* Waits for the {@link DataService} and that the MQTT server is connected */
			for (;;) {
				/*
				 * The {@link DataService} is null or the MQTT server is not connected - the ||
				 * or operator checks, if {@link DataService} is null - the MQTT connection is
				 * not checked at all - this prevents a NULL pointer exception, when we query
				 * the MQTT connect status.
				 */
				if (this.dataService == null || this.dataService.isConnected() == false) {
					LOGGER.info("Status of the dataService: {}, MQTT connection status: {}", this.dataService,
							this.dataService == null ? "not connected" : this.dataService.isConnected());
					/* Waiting empirically ... */
					Thread.sleep(MDNS_DISCOVERY_START_DELAY);
					continue;
				} else {
					/* The {@link DataService} is not null and the MQTT server is connected */
					LOGGER.info("Status of the dataService {}, MQTT status {}", this.dataService,
							this.dataService.isConnected());
					break;
				}
			}
			LOGGER.info("MDNS discovery service started");

			/* Creates the MDNS service name + type */
			String mdnsServiceNameType = this.options.getMdnsServiceName() + this.options.getMdnsServiceType();
			LOGGER.info("mdnsServiceNameType: {}", mdnsServiceNameType);

			/* Starts the MDNS discovery service to find multisensors */
			this.mdnsService = MdnsServiceFactory.createDiscovery(mdnsServiceNameType, this,
					InetAddress.getLocalHost());
			LOGGER.info("dnsServicD: " + mdnsService.toString());
		} catch (Throwable e) {
			LOGGER.error("", e);
		}
		LOGGER.info("MDNS discovery task SHUTDOWN !");
	};

	/**
	 * Distributes the new thing with the {@link EventAdmin} to create asset and
	 * channels and to save the thing in the thing description directory.
	 * 
	 * @param thing the generated thing
	 */
	public void sendThingEvent(Thing thing) {
		Map<String, Object> eventAdminProperties = new HashMap<>();

		// Create a thin key referenced to the thing description
		eventAdminProperties.put("thing", thing);

		// We need a reference to the driver pid
		eventAdminProperties.put("driverPid", options.getDriverServicePID());

		// We need a topic where we can publish the thing description
		String topic = THINGS_TOPIC + thing.getTitle();
		LOGGER.info("Send thing to EventAdmin with Topic: " + topic);

		// Gets the {@link EventAdmin} instance and publish the thing description
		getEA().sendEvent(new Event(topic, eventAdminProperties));
	}

	public void vibrationconverter(ThingChannelListener listener, String payload) {
		JsonElement value = parser.parse(payload);
		long time = 1;
		if (value.isJsonObject()) {
			Set<Entry<String, JsonElement>> entries = value.getAsJsonObject().entrySet();
			for (Entry<String, JsonElement> entry : entries) {
				String key = entry.getKey();
				if (key.equals("time")) {
					time = entry.getValue().getAsLong();
				}
			}
			for (Entry<String, JsonElement> entry : entries) {
				String key = entry.getKey();
				if (key.equals("vibration")) {
					if (entry.getValue().isJsonArray()) {
						for (int i = 0; i < entry.getValue().getAsJsonArray().size(); i = i + 6) {
							JsonObject newValue = new JsonObject();
							newValue.addProperty("time", time);
							Set<Entry<String, JsonElement>> values = entry.getValue().getAsJsonArray().get(i)
									.getAsJsonObject().entrySet();
							for (Entry<String, JsonElement> v : values) {
								newValue.addProperty(v.getKey(), v.getValue().getAsNumber());
							}
//							System.out.println(newValue);
							listener.doUpdate(newValue);
							time += 1;
						}
					}
				}
			}
		}
	}
}
