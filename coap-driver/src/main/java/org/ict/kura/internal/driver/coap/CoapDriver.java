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
package org.ict.kura.internal.driver.coap;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.ws.rs.BadRequestException;

import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.eclipse.kura.KuraException;
import org.eclipse.kura.channel.ChannelRecord;
import org.eclipse.kura.configuration.ConfigurableComponent;
import org.eclipse.kura.driver.Driver;
import org.eclipse.kura.driver.PreparedRead;
import org.ict.gson.utils.AdapterFactory;
import org.ict.kura.asset.creator.thing.util.ThingPreparedRead;
import org.ict.kura.driver.thing.ThingDriver;
import org.ict.kura.internal.driver.coap.client.CustomCoapClient;
import org.ict.kura.internal.driver.coap.client.ObserveHandler;
import org.ict.kura.internal.driver.coap.mdns.MdnsService;
import org.ict.kura.internal.driver.coap.mdns.MdnsServiceFactory;
import org.ict.kura.internal.driver.coap.util.CoapBindingConfig;
import org.ict.kura.internal.driver.coap.util.Util;
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
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import javassist.NotFoundException;

//formatter:off
/**
 * This is a CoAP specific driver implementation to communicate with CoAP server
 * endpoints. Supports reading, writing and observing resources.
 * 
 * 
 * @author IKT B. Helgers
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2022-12-16
 */
//formatter:on

/* Annotation to point to a {@link CoapConfig} class */
@Designate(ocd = CoapConfig.class, factory = true)
/* Annotation to create the component.xml from source code */
@Component(name = "org.ict.kura.driver.coap", immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE, property = "service.pid=org.ict.kura.driver.coap")
public class CoapDriver extends ThingDriver<CoapBindingConfig>
		implements Driver, ConfigurableComponent, ServiceListener {

	/* The coap driver logger instance */
	private static final Logger LOGGER = LoggerFactory.getLogger(CoapDriver.class);

	/* The uniqe driver app id - in this case driver package name */
	private static final String APP_ID = "org.ict.kura.driver.coap";

	/* The driver keepalived topic */
	private static final String THINGS_TOPIC = "things/";

	/*
	 * The global Gson instance used to deserialize object string representations
	 */
	private static final Gson gson = AdapterFactory.getGsonWithDefaultWotTypeAdapters(true);

	/*
	 * The instances of {@link CustomCoapClient} used to read and write data from/to
	 * CoAP endpoints. The key is the thing id, linked with the instance.
	 */
	private Map<String, CustomCoapClient> customCoapClients = new HashMap<>();

	/* The {@link MdnsService} to discover services */
	private MdnsService mdnsService = null;

	/* The executor service to handle the MDNS discovery start phase */
	private ScheduledExecutorService scheduledExecutorService;

	/* MDNS discovery start delay in milli seconds */
	private int MDNS_DISCOVERY_START_DELAY = 100;

	/* Service information of the {@link MdnsService} discover */
	private TreeMap<String, ServiceInfo> serviceInfos = new TreeMap<>();

	/* The driver options */
	private CoapDriverOptions options = null;

	/**
	 * Binding method which starts the bundle, see component.xml, is called by the
	 * OSGi framework
	 * 
	 * @throws KuraException
	 */
	@Activate
	protected void activate(ComponentContext componentContext, Map<String, Object> properties) {
		/* Updates the configuration */
		updated(properties);
		LOGGER.info("Bundle " + APP_ID + " has started with config!");
	}

	/**
	 * Handles the configuration updates.
	 * 
	 * @param properties the configuration parameters, which are configured via the
	 *                   kura admin web interface
	 */
	@Modified
	public void updated(Map<String, Object> properties) {
		/* Deactivates all components */
		doDeactivate();

		/* Updates all components with new configuration */
		doUpdate(properties);
		LOGGER.info("Bundle " + APP_ID + " has updated!");
	}

	/**
	 * Reads configuration map and starts instance of </br>
	 * CustomKuraClient and CustomKuraMapper to create kura devices and channels.
	 * 
	 * @param properties configuration parameters
	 * @throws IllegalArgumentException if kura configuration is empty
	 */
	private void doUpdate(Map<String, Object> properties) {
		Objects.requireNonNull(properties, "Empty kura configuration can't start framework!");

		try {
			LOGGER.info("...doUpdate()");

			/** The instance of CoapDriverOptions */
			options = new CoapDriverOptions(properties);

			/*
			 * If AutoCreateConfig is equals false, we do nothing, otherwise we are looking
			 * for CoAP servers via a defined configuration or general in the network via
			 * MDNS !
			 */
			if (options.getAutoCreateConfig() == true) {
				/* Checks if target ip is defined, otherwise uses the mdns discover if empty */
				if (options.getConnectionServerAddresses() != null) {
					LOGGER.info("Connecting defined CoAP servers with addresses ...... "
							+ options.getConnectionServerAddresses());

					// Iterates over the server address list
					for (int i = 0; i < options.getConnectionServerAddresses().length; i++) {
						/** Starts instance of CustomCoapClient */
						CustomCoapClient client = new CustomCoapClient(
								options.getConnectionServerAddresses()[i].split(":")[0],
								Integer.valueOf(options.getConnectionServerAddresses()[i].split(":")[1]),
								options.getConnectionTimeOut());

						LOGGER.info("Started CoapClient with connection to server: " + client.getURI()
								+ " with timeOut of " + options.getConnectionTimeOut() + "ms");

						/* Request thing map and return filled map thing string */
						Thing thing = requireNonNull(client.requestThingResource(),
								"Could not request the thing resource (thing description) from server: "
										+ client.getURI());

						/*
						 * Puts the instance into the customCoapClients map. The key is the ID/Name from
						 * the thing href, see {@link Util.extractThingIdFromThingHref()}.
						 */
						LOGGER.debug(thing.getId().toString());
						customCoapClients.put(Util.extractThingIdFromThingHref(thing.getId().toString()), client);

						LOGGER.info("Creating assets and channels to thing by name: " + thing.getId());

						/* Create Asset and Channels for the thing */
						sendThingEvent(thing);
					}
				} else {
					LOGGER.info("Looking for CoAP servers via MDNS ...... ");
					/** Activates the MDNS service */
					LOGGER.info("Starts the MDNS discovery task with {} delay", MDNS_DISCOVERY_START_DELAY);

					/** Create the single thread executor service */
					scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

					/** Schedule the mdns discovery task with delay in milliseconds */
					scheduledExecutorService.schedule(mdnsDiscoveryTask, MDNS_DISCOVERY_START_DELAY,
							TimeUnit.MILLISECONDS);
				}
			} else {
				LOGGER.info("Service not activated...");
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	/**
	 * Closes all resources
	 * 
	 * @throws Exception
	 */
	private void doDeactivate() {
		try {
			/* Deletes all assets and channels */
			getThingProvider().deleteAssetsWithChannels(options.getDriverServicePID());

			/* Cancel all active observe relations */
			customCoapClients.forEach((k, v) -> {
				try {
					if (v != null) {
						v.cancelAllObserveRelations();
						v.close();
					}
				} catch (Exception e) {
					LOGGER.error("" + e.getMessage());
				}
			});
			/* Removes all entries */
			customCoapClients.clear();

			/* Closes the ExecutorService */
			if (scheduledExecutorService != null) {
				scheduledExecutorService.shutdownNow();
			} else {
				LOGGER.info("ExecutorService is null");
			}

			/* Closes the MDNS service */
			if (mdnsService != null) {
				mdnsService.close();
			} else {
				LOGGER.info("MDNSService is null");
			}
		} catch (Exception e) {
			LOGGER.error("" + e.getMessage());
		}
	}

	/**
	 * Binding function which to shutdown the bundle, see component.xml, is called
	 * by the OSGi framework
	 * 
	 * @param componentContext The OSGi component informations of this bundle - in
	 *                         this case we do nothing there.
	 */
	@Deactivate
	protected void deactivate(ComponentContext componentContext) {
		/* Closes all resources */
		doDeactivate();

		LOGGER.info("Bundle " + APP_ID + " has stopped!");
	}

	@Override
	/**
	 * Here we do nothing. In this method we ca e.g. implement a real connection to
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

	/*
	 * This annotation adds the event admin service methods to the
	 * OSGI-INF/org.ict.kura.driver.coap.xml from source code
	 */
	@Reference(name = "EventAdmin", service = EventAdmin.class, cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, bind = "setEventAdmin", unbind = "unsetEventAdmin")

	/**
	 * This metods receives the {@link EventAdmin} from OSGI
	 * 
	 * @param eventAdmin the OSGI EventAdmin service to send events
	 */
	public void setEventAdmin(EventAdmin eventAdmin) {
		this.setEA(eventAdmin);
	}

	/**
	 * Removes the {@link EventAdmin} - sets the instance qual null.
	 * 
	 * @param eventAdmin the OSGI EventAdmin service to send events
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
	 * This metods receives the {@link EventAdmin} from OSGI
	 * 
	 * @param eventAdmin the OSGI {@link EventAdmin} service to send events
	 */
	public void setAssetProvider(ThingProvider assetProvider) {
		this.setThingProvider(assetProvider);
	}

	/**
	 * Removes the {@link EventAdmin} - sets the instance equal null.
	 * 
	 * @param eventAdmin the OSGI {@link EventAdmin} service to send events
	 */
	public void unsetAssetProvider(ThingProvider thingProvider) {
		this.setThingProvider(null);
	}

	@Override
	/*
	 * Creates a {@link PreparedRead} instance contains the {@link ChannelRecord}
	 * list.
	 */
	public PreparedRead prepareRead(List<ChannelRecord> channelRecords) {
		LOGGER.info("...prepareRead");
		Objects.requireNonNull(channelRecords, "Channel Record list cannot be null");

		/* Creates the {@link PreparedRead} instance */
		try (ThingPreparedRead preparedRead = new ThingPreparedRead(this, channelRecords)) {
			LOGGER.info(" " + preparedRead);
			return preparedRead;
		}
	}

	@Override
	public Optional<JsonObject> doRead(CoapBindingConfig coapBindingConfig) {
		try {
			LOGGER.info("...doRead");

			// Extracts the thing id from the form element href
			String thingId = Util.extractThingIdFromFormHref(coapBindingConfig.getHref());

			// Exists a client with the given key
			if (!customCoapClients.containsKey(thingId))
				throw new NotFoundException("Key: {} not found in the customCoapClients map!");

			// Gets the client instance from customCoapClients !!
			CustomCoapClient client = customCoapClients.get(thingId);

			// Sets the uri to request
			client.setURI(coapBindingConfig.getHref());

			LOGGER.debug("Connect client to resource: " + client.getURI());

			/* Build request and wait for response */
			CoapResponse coapResponse = client.get(MediaTypeRegistry.APPLICATION_JSON);

			/* Checks the response status */
			if (coapResponse.isSuccess() && !coapResponse.getResponseText().isEmpty()) {
				LOGGER.debug("Received payload from request: " + coapResponse.getResponseText());

				/* Deserializes the String response to a JsonObject */
				JsonObject payload = gson.fromJson(coapResponse.getResponseText(), JsonObject.class);

				LOGGER.info("Updated CoapBindingConfig " + coapBindingConfig + " with new state " + payload.toString());

				// Returns the received payload
				return Optional.of(payload);
			} else {
				LOGGER.warn("Bad Response: " + Utils.prettyPrint(coapResponse));
			}

		} catch (Exception e) {
			if (e instanceof JsonSyntaxException) {
				LOGGER.error(
						"Unable to parse response payload to JsonObject.Please check data of CoapBindingConfig by path "
								+ coapBindingConfig.getHref());
			}
			LOGGER.info("Error: " + e.getMessage());
		}
		// Returns an empty payload, if no payload is present
		return Optional.empty();
	}

	@Override
	public void doWrite(CoapBindingConfig coapBindingConfig, ActionAffordance action, JsonObject jsonValue) {
		CoapResponse coapResponse = null;
		try {
			LOGGER.info("...doWrite");

			// Extracts the form href (only one form is supported)
			String formHref = action.getForms().get(0).getHref().toString();

			// Extracts the thing id from the form element href
			String thingId = Util.extractThingIdFromFormHref(formHref);

			// Exists a client with the given key
			if (!customCoapClients.containsKey(thingId))
				throw new NotFoundException("Key: {} not found in the customCoapClients map!");

			// Gets the client instance from customCoapClients !!
			CustomCoapClient client = customCoapClients.get(thingId);

			// Sets the uri to request
			client.setURI(coapBindingConfig.getHref());

			LOGGER.debug("Trying to change state of actuator to: " + jsonValue.toString());

			/* Builds request manually */
			Request request = new Request(Code.PUT, Type.CON);
			request.getOptions().setAccept(MediaTypeRegistry.APPLICATION_JSON);
			request.setPayload(jsonValue.toString());
			LOGGER.debug("Build put request to write new state: " + Utils.prettyPrint(request));

			/* Sends request and expects response timely */
			try {
				coapResponse = client.advanced(request);
				if (!coapResponse.isSuccess())
					throw new BadRequestException("Bad request due to malformed payload!");
			} catch (ConnectorException | IOException e) {
				e.printStackTrace();
			}

			LOGGER.debug("Received response: " + Utils.prettyPrint(coapResponse));

			/* Fires the current value into the Kura system */
			getUpdateBindingConfigurations().get(coapBindingConfig)
					.doUpdate(gson.fromJson(coapResponse.getResponseText(), JsonObject.class));
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	@Override
	/**
	 * Create instance of CoapBindingConfig for each Property
	 */
	public CoapBindingConfig createBinding(String jsonBinding) {
		try {
			/* Creates a JSON Object from jsonValue */
			JsonObject jsonObject = gson.fromJson(jsonBinding, JsonObject.class);

			LOGGER.info("...createBinding: jsonBinding {}", jsonObject.get("href"));

			/* Creates a technology binding configuration from json object */
			CoapBindingConfig coapBindingConfig = new CoapBindingConfig();
			coapBindingConfig.setHref(jsonObject.get("href").getAsString());

			LOGGER.info("Created coapBindingConfig {}", coapBindingConfig);
			LOGGER.debug("Creating binding to resource: " + coapBindingConfig.getHref());

			// Extracts the thing id from the form element href, see {@link
			// Util.extractThingIdFromFormHref()}.
			String thingId = Util.extractThingIdFromFormHref(coapBindingConfig.getHref());

			// Exists a client with the given key
			if (!customCoapClients.containsKey(thingId))
				throw new NoSuchElementException("Key: {} not found in the customCoapClients map!");

			// Gets the client instance from customCoapClients !!
			CustomCoapClient client = customCoapClients.get(thingId);
			// Sets the uri to request
			client.setURI(coapBindingConfig.getHref());
			LOGGER.debug("Connected client to uri: " + client.getURI());

			/* Create instance of ObserveHandler */
			ObserveHandler obsHandler = new ObserveHandler(coapBindingConfig, this);

			/* Send observe request to resource using created observe handler */
			client.observeResource(coapBindingConfig.getHref(), obsHandler, MediaTypeRegistry.APPLICATION_JSON);
			LOGGER.info("Established observe relation to server resource " + coapBindingConfig.getHref());

			return coapBindingConfig;
		} catch (Exception e) {
			LOGGER.error("", e);
			throw e;
		}
	}

	/*************************************
	 * THE MDNS SERVICE CALLBACK METHODS
	 *************************************/

	/*
	 * The MDNS discovery task. In start phase it is possible, that the MDNS service
	 * discovery is faster than the initialization of the {@link DataService}. So,
	 * here we are waiting empirically for
	 */
	public Runnable mdnsDiscoveryTask = () -> {
		try {
			LOGGER.info("MDNS discovery task started");

			/* Creates the MDNS service name + type */
			String mdnsServiceNameType = this.options.getMdnsServiceName() + this.options.getMdnsServiceType();
			LOGGER.info("mdnsServiceNameType: {}", mdnsServiceNameType);

			/* Starts the MDNS discovery service to find multisensors */
			this.mdnsService = MdnsServiceFactory.createDiscovery(mdnsServiceNameType, this,
					InetAddress.getLocalHost());
			LOGGER.info("dnsServiceID: " + mdnsService.toString());

		} catch (Throwable e) {
			LOGGER.error("", e);
		}
		LOGGER.info("MDNS discovery task SHUTDOWN !");
	};

	@Override
	public void serviceAdded(ServiceEvent event) {
		LOGGER.info("DNS service added with scheme 'coap': " + event.getInfo().getQualifiedName());
	}

	@Override
	public void serviceRemoved(ServiceEvent event) {
		LOGGER.info("DNS service removed: " + event.getInfo().getQualifiedName());
		// Removes only the service info, the client instance remains, see method {@link
		// #serviceResolved(ServiceEvent)}
		serviceInfos.remove(event.getInfo().getQualifiedName());
	}

	@Override
	public void serviceResolved(ServiceEvent event) {
		try {
			/* Full list of data properties */
			LOGGER.info("Data of event: " + event.getInfo());

			// Extracts the thing id
			String thingId = Util.extractThingIdFromThingHref(event.getInfo().getPropertyString("td"));

			/* Checks if a coap client instance exists */
			if (customCoapClients.containsKey(thingId)) {
				/* Puts the discovered MDNS service into the internal service info map */
				serviceInfos.put(event.getInfo().getQualifiedName(), event.getInfo());

				// Step 1
				// - Gets the client instance
				CustomCoapClient client = customCoapClients.get(thingId);
				// Extract IP and PORT
				String ipPort = Util.extractIpPortFromThingHref(event.getInfo().getPropertyString("td"));
				// Sets the ip endpoint
				client.setServerIp(ipPort.split(":")[0]);
				// Sets the port endpoint
				client.setServerPort(Integer.valueOf(ipPort.split(":")[1]));

				// Step 2
				/* Requests the thing description */
				Thing thing = requireNonNull(client.requestThingResource(),
						"Could not request the thing resource (thing description) from server: " + client.getURI());

				/*
				 * Simply overwrites the asset and channels for this thing. This also triggers
				 * the method {@link #createBinding(String)} and also creates new observe
				 * relations.
				 */
				sendThingEvent(thing);
			} else {
				/* Puts the discovered MDNS service into the internal service info map */
				serviceInfos.put(event.getInfo().getQualifiedName(), event.getInfo());

				/*
				 * Creates a new client instance with IP and PORT from the MDNS service info.
				 */
				LOGGER.info("Coap MDNS Info, try to extract ip:port from url {}",
						event.getInfo().getPropertyString("td"));
				String ipPort = Util.extractIpPortFromThingHref(event.getInfo().getPropertyString("td"));
				LOGGER.info("Coap MDNS Info: {}", ipPort);
				CustomCoapClient client = new CustomCoapClient(ipPort.split(":")[0],
						Integer.valueOf(ipPort.split(":")[1]), options.getConnectionTimeOut());

				LOGGER.info("Started CoapClient with connection to server: " + client.getURI() + " with timeOut of "
						+ options.getConnectionTimeOut() + "ms");

				/* Request thing map and return filled map thing string */
				Thing thing = requireNonNull(client.requestThingResource(),
						"Could not request the thing resource (thing description) from server: " + client.getURI());

				/*
				 * Puts the instance into the customCoapClients map. The key is the ID/Name from
				 * the thing href, see {@link Util.extractThingIdFromThingHref()}.
				 */
				customCoapClients.put(Util.extractThingIdFromThingHref(thing.getId().toString()), client);

				LOGGER.info("Creating assets and channels to thing by name: " + thing.getAtIdRDF());

				/* Create Asset and Channels for the thing */
				sendThingEvent(thing);

				LOGGER.info("DNS service resolved: {} with URL {}", event.getInfo().getQualifiedName(),
						event.getInfo().getPropertyString("td"));
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

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
}