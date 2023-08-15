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
package org.ict.kura.internal.driver.knx;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.net.InetSocketAddress;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.eclipse.kura.KuraException;
import org.eclipse.kura.channel.ChannelRecord;
import org.eclipse.kura.configuration.ConfigurableComponent;
import org.eclipse.kura.driver.Driver;
import org.eclipse.kura.driver.PreparedRead;
import org.ict.kura.asset.creator.thing.util.ThingPreparedRead;
import org.ict.kura.driver.thing.ThingChannelListener;
import org.ict.kura.driver.thing.ThingDriver;
import org.ict.kura.driver.thing.Util;
import org.ict.kura.internal.driver.knx.util.KnxBindingConfig;
import org.ict.kura.internal.driver.knx.util.Tool;
import org.ict.kura.internal.driver.knx.util.dpt.Dpt;
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
import com.google.gson.JsonParser;

import tuwien.auto.calimero.DataUnitBuilder;
import tuwien.auto.calimero.DetachEvent;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.KNXException;
import tuwien.auto.calimero.KNXIllegalArgumentException;
import tuwien.auto.calimero.KNXTimeoutException;
import tuwien.auto.calimero.dptxlator.DPTXlator;
import tuwien.auto.calimero.link.KNXNetworkLink;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.medium.TPSettings;
import tuwien.auto.calimero.process.ProcessCommunicator;
import tuwien.auto.calimero.process.ProcessCommunicatorImpl;
import tuwien.auto.calimero.process.ProcessEvent;
import tuwien.auto.calimero.process.ProcessListener;

/**
 * The KNX driver implementation based on a WoT description and using the
 * semantic software layer {@link ThingDriver}.
 * 
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2023-07-21
 */
/* Annotation to point a {@link KnxConfig} class */
@Designate(ocd = KnxConfig.class, factory = true)
/* Annotation to create the component.xml from source code */
@Component(name = "org.ict.kura.driver.knx", immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE, property = "service.pid=org.ict.kura.driver.knx", service = {
		Driver.class, ConfigurableComponent.class })
public class KnxDriver extends ThingDriver<KnxBindingConfig> implements Driver, ConfigurableComponent, ProcessListener {
	/* The logger instance */
	private static final Logger LOGGER = LoggerFactory.getLogger(KnxDriver.class);

	/* The unique driver app id - in this case the driver package name */
	private static final String APP_ID = "org.ict.kura.driver.knx";

	/* The driver keepalived topic */
	private static final String KEEPALIVED_TOPIC = "drivers/keepalived";

	/* The driver keepalived topic */
	private static final String THINGS_TOPIC = "things/";

	/* The driver properties defined by the user in the KURA web admin interface */
	@SuppressWarnings("unused")
	private Map<String, Object> properties;

	/* The driver options */
	private KnxDriverOptions options;

	/*
	 * The {@link KnxBindingConfig} sorted by the KNX group address. The key is the
	 * KNX group address. The value (T) is the {@link KnxBindingConfig}.
	 */
	private Map<String, KnxBindingConfig> knxGroupAddressBindingConfigurations = new HashMap<>();

	/* The KNX net IP communication channel */
	private KNXNetworkLink knxNetworkLink;

	/* The KNX process communicator to read / write group addresses */
	private ProcessCommunicator processCommunicator;

	/*
	 * The scheduled executor service to start the initial KNX reading process - try
	 * to get the actual group address status values for initial setup.
	 */
	private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

	/* The thread to manage the reconnect behavior */
	private ScheduledFuture<?> reconnectHandle;

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
	 *                   KURA web admin interface
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
		/* Creates a thing list based on the given folder and thing files */
		List<Thing> things = new ArrayList<>();

		try {
			LOGGER.info("doUpdate...");

			/* We need here the thin provider service */
			requireNonNull(getThingProvider(), "The thing provider service must not be null !");

			/* Gets the actual options (configuration) */
			this.options = new KnxDriverOptions(properties);

			/*
			 * If updated==false, then we are in bundle state activate, in this case all
			 * resources must be created
			 */
			LOGGER.info("Bundle is in activate mode ...");

			// ************** (1) Section: Loads the thing description ****************

			LOGGER.info("Thing folder path: {}", options.getThingFolderLocation());

			if (options.getThingFolderLocation().isEmpty())
				throw new IllegalArgumentException("Invalid location folder has been defined for thing descriptions !");

			File file = new File(this.options.getThingFolderLocation());

			if (file.isDirectory()) {
				// Gets things from folder !
				things = Util.getThingsFromFolder(file, new HashMap<String, Thing>()).entrySet().stream()
						.map(Map.Entry::getValue).collect(Collectors.toList());
			} else {
				throw new IllegalArgumentException("Invalid file type is not a directory !");
			}

			/* Iterates over the thing map and creates assets with channels */
			for (Thing thing : things) {
				try {
					/*
					 * SOLUTION INFO: Creates kura asset with channels from the given thing via the
					 * OSGi event admin. In this case, a thing description is available in the event
					 * admin. The {@link ThingProviderImpl#handleEvent(Event) received the thing
					 * descriptions and creates asset with channels. Other services such as the
					 * thing directory service can also receive and process thing descriptions.
					 */
					Map<String, Object> eventAdminProperties = new HashMap<>();

					// Create a thin key referenced to the thing description
					eventAdminProperties.put("thing", thing);

					// We need a reference to the driver pid
					eventAdminProperties.put("driverPid", options.getDriverServicePID());

					// We need a topic where we publish the thing description
					String topic = THINGS_TOPIC + thing.getTitle();
					LOGGER.info("Send thing to EventAdmin with Topic: " + topic);

					// Gets the {@link EventAdmin} instance and publish the thing description
					getEA().sendEvent(new Event(topic, eventAdminProperties));
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}

			// **************** (2) Section: Initializes the KNX network adapter ***********

			// Try to connect the KNX network
			connectKnxNetwork(this);
		} catch (Exception e) {
			LOGGER.error("doUpdate ...{}", e);
		} finally {
			// ************* (4) Section: Start the KNX network reconnect task ************
			/* Starts always the reconnect task, also a exception was thrown */
			runReconnectTask(this);
			LOGGER.info("Starts the reconnectTask with delay {} msec", this.options.getReconnectTimeout());
		}
	}

	/**
	 * Deactivates all resources
	 */
	private void doDeactivate() {
		LOGGER.info("...doDeactivate");

		/*
		 * Closes only the local {@link ProcessCommunicator}, all other resources will
		 * be removed by the {@link KnxDriver}
		 */
		if (this.processCommunicator != null)
			this.processCommunicator.close();

		/* Closes the KNX network link if not null */
		if (knxNetworkLink != null)
			knxNetworkLink.close();

		/* Stops the KNX scan process if active */
		if (scheduledExecutorService != null)
			scheduledExecutorService.shutdownNow();
	}

	/*
	 * This annotation adds the {@link EventAdmin} service methods to the
	 * OSGI-INF/org.ict.kura.driver.xml from source code
	 */
	@Reference(name = "EventAdmin", service = EventAdmin.class, cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, bind = "setEventAdmin", unbind = "unsetEventAdmin")

	/**
	 * This methods receives the {@link EventAdmin} from OSGI
	 * 
	 * @param eventAdmin the OSGI {@link EventAdmin} service to send events
	 */
	public final void setEventAdmin(EventAdmin eventAdmin) {
		try {
			this.setEA(eventAdmin);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		}
	}

	/**
	 * Removes the {@link EventAdmin} - sets the instance equal null.
	 * 
	 * @param eventAdmin the OSGI {@link EventAdmin} service to send events
	 */
	public final void unsetEventAdmin(EventAdmin eventAdmin) {
		this.setEA(null);
	}

	/*
	 * This annotation adds the {@link EventAdmin} service methods to the
	 * OSGI-INF/org.ict.kura.driver.xml from source code
	 */
	@Reference(name = "AssetProvider", service = ThingProvider.class, cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, bind = "setAssetProvider", unbind = "unsetAssetProvider")

	/**
	 * This method receives the {@link ThingProvider} from OSGI
	 * 
	 * @param assetProvider the OSGI {@link ThingProvider} service to handle KURA
	 *                      assets and channels
	 */
	public final void setAssetProvider(ThingProvider assetProvider) {
		this.setThingProvider(assetProvider);
	}

	/**
	 * Removes the {@link ThingProvider} - sets the instance equal null.
	 * 
	 * @param assetProvider the OSGI {@link ThingProvider} service to handle KURA
	 *                      assets and channels
	 */
	public final void unsetAssetProvider(ThingProvider thingProvider) {
		this.setThingProvider(null);
	}

	@Override
	/**
	 * Creates a {@link PreparedRead} instance contains the {@link ChannelRecord}
	 * list.
	 */
	public final PreparedRead prepareRead(List<ChannelRecord> channelRecords) {
		LOGGER.info("...prepareRead");
		requireNonNull(channelRecords, "The channelRecords list must not be null");

		/* Creates the {@link PreparedRead} instance */
		try (ThingPreparedRead preparedRead = new ThingPreparedRead(this, channelRecords)) {
			LOGGER.debug("Prepared read: {}", preparedRead);
			return preparedRead;
		}
	}

	@Override
	/**
	 * Here we do nothing. In this method we can e.g. implement a real connection to
	 * other physical gateways, sensors and actuators.
	 */
	public final void connect() throws ConnectionException {
		LOGGER.info("...connect");
	}

	@Override
	/**
	 * Here we do nothing. In this method we can e.g. implement a real disconnection
	 * to other physical gateways, sensors and actuators.
	 */
	public final void disconnect() throws ConnectionException {
		LOGGER.info("...disconnect");
	}

	// **************** Section: Binding ***********

	@Override
	public Optional<JsonObject> doRead(KnxBindingConfig knxBindingConfig) {
		try {
			LOGGER.info("...doRead group address {}", knxBindingConfig.getGroupAddress());

			// ################################################################################################
			/*
			 * Reads the initial value from the KNX bus, this method here only triggers the
			 * asynchronous method {@link ProcessListener#groupReadResponse(ProcessEvent)}.
			 * Since all values are forwarded as JSON String, the request for the data type
			 * String is made here for all group addresses - all data types are directly
			 * converted to String by the calimero api.
			 */
			String ret = processCommunicator.readString(new GroupAddress(knxBindingConfig.getGroupAddress()));
			LOGGER.info("...doRead return value {}", ret);
			// ################################################################################################
		} catch (Exception ex) {
			if (ex instanceof KNXIllegalArgumentException)
				LOGGER.warn(
						"Knx read request expects only the type string as response for all datapoint types, group address: {}, cause {}",
						knxBindingConfig.getGroupAddress(), ex.getMessage());
			else if (ex instanceof KNXTimeoutException)
				LOGGER.warn("A Knx read access to the given sensor datapoint {} is not supported, cause {}",
						knxBindingConfig.getGroupAddress(), ex.getMessage());
			else
				LOGGER.error("doRead ...{}", ex);
		}
		/*
		 * We return here an empty optional value, because the method {@link
		 * ProcessCommunicator.readString(GroupAddress)} is asynchronously
		 */
		return Optional.empty();
	}

	@Override
	public void doWrite(KnxBindingConfig knxBindingConfig, ActionAffordance actionAffordance, JsonObject jsonValue) {
		try {
			Objects.requireNonNull(knxBindingConfig, "The knxBindingConfig is null !");
			Objects.requireNonNull(actionAffordance, "The actionAffordance is null !");
			Objects.requireNonNull(jsonValue, "The jsonValue is null !");

			LOGGER.info("...doWrite jsonValue {}, with group address {}", jsonValue,
					knxBindingConfig.getGroupAddress());

			// ################################################################################################
			/* Gets a DPT translator object */
			Dpt dpt = (Dpt) knxBindingConfig.getAdditionalProperties().get(knxBindingConfig.getDptID());
			/* Prepares the DPT translator object with the current value */
			DPTXlator dptXlator = dpt.to(knxBindingConfig, actionAffordance, jsonValue);

			LOGGER.info("...writing the value {}, to the KNX group address {} !", dptXlator.getValue(),
					knxBindingConfig.getGroupAddress());
			/*
			 * Writes the value into the KNX network via the binding group address DPT
			 * translator object
			 */
			this.processCommunicator.write(new GroupAddress(knxBindingConfig.getGroupAddress()), dptXlator);
			// ################################################################################################
		} catch (Exception ex) {
			LOGGER.error("doWrite ... {}", ex);
		}
	}

	// **************** Section: KNX ProcessListener call backs ***********

	@Override
	public void groupWrite(final ProcessEvent processEvent) {
		LOGGER.info("...groupWrite ProcessEvent {}", processEvent);

		try {
			LOGGER.info(LocalTime.now() + " " + processEvent.getSourceAddr() + "->" + processEvent.getDestination()
					+ " " + "write.ind" + ": " + DataUnitBuilder.toHex(processEvent.getASDU(), ""));

			/* Looks for the binding */
			final KnxBindingConfig knxBindingConfig = knxGroupAddressBindingConfigurations
					.get(processEvent.getDestination().toString());

			/* If not found, throw an exception */
			if (knxBindingConfig == null) {
				LOGGER.warn("KnxBindingConfig NOT found/configured for this group address {} !",
						processEvent.getDestination().toString());
				return;
			}

			// ################################################################################################
			LOGGER.info("getUpdateBindingConfigurations().size(): {}", getUpdateBindingConfigurations().size());
			getUpdateBindingConfigurations()
					.forEach((k, v) -> LOGGER.info(("getUpdateBindingConfigurations(): " + k + ":" + v)));
			/*
			 * Searches the group address in the local map and than in the map {@link
			 * update(T, kcl)} to get the kcl for the channel name
			 */
			ThingChannelListener thingChannelListenerImpl = getUpdateBindingConfigurations().get(knxBindingConfig);

			/*
			 * If not found, throw an exception. If an knxBindingConfig exists but not a
			 * ThingChannelListenerImpl, the actual event is an action and we do not
			 * supporting a telemetry path for actions, only for properties.
			 */
			if (thingChannelListenerImpl == null) {
				LOGGER.debug(
						"No ThingChannelListenerImpl for this processEvent/knxBindingConfig '%s' found, the actual event is an action and we do not supporting a telemetry path for actions, only for properties !",
						processEvent.toString());
				return;
			}

			/* Creates a JSON payload ... */
			Dpt dpt = (Dpt) knxBindingConfig.getAdditionalProperties().get(knxBindingConfig.getDptID());
			LOGGER.info("knxBindingConfig.getDptID(): {}, dpt {}", knxBindingConfig.getDptID(), dpt);
			final String jsonValue = dpt.from(knxBindingConfig, thingChannelListenerImpl, processEvent);

			LOGGER.info("Update {} with payload {}", thingChannelListenerImpl.getPropertyName(), jsonValue);

			/* Updates the value in KURA */
			thingChannelListenerImpl.doUpdate(jsonValue);
		} catch (Exception e) {
			LOGGER.error("groupWrite ...", e);
		}
	}

	@Override
	public void groupReadRequest(final ProcessEvent processEvent) {
		LOGGER.info("...groupReadRequest ...NOT IMPLEMENTED, group address {}", processEvent.getDestination());
	}

	@Override
	public void groupReadResponse(final ProcessEvent processEvent) {
		try {
			LOGGER.info("...groupReadResponse from group address {}", processEvent.getDestination());
			/* Looks for the binding */
			final KnxBindingConfig knxBindingConfig = this.knxGroupAddressBindingConfigurations
					.get(processEvent.getDestination().toString());
			/* Gets the DPT xlator */
			Dpt dpt = (Dpt) knxBindingConfig.getAdditionalProperties().get(knxBindingConfig.getDptID());
			/* Gets the thing channel listener */
			final ThingChannelListener thingChannelListenerImpl = getUpdateBindingConfigurations()
					.get(knxBindingConfig);
			/* Converts the KNX value to JSON string */
			final String jsonValue = dpt.from(knxBindingConfig, thingChannelListenerImpl, processEvent);
			LOGGER.info("State of group address {} is {}", processEvent.getDestination(), jsonValue);
			/* Fires the current value into the Kura system */
			getUpdateBindingConfigurations().get(knxBindingConfig).doUpdate(jsonValue);
		} catch (Exception ex) {
			if (ex instanceof NullPointerException)
				LOGGER.warn("KNX group address NOT found/configured {}", processEvent.getDestination().toString());
			else
				LOGGER.error("groupReadResponse ...", ex);
		}
	}

	@Override
	public void detached(final DetachEvent detachedEvent) {
		LOGGER.info("...detached, DetachEvent {}", detachedEvent.toString());
	}

	@Override
	public KnxBindingConfig createBinding(String jsonBinding) {
		try {
			LOGGER.info("...createBinding: jsonBinding {}", jsonBinding);

			/* Creates a JSON Object from jsonValue */
			JsonObject jsonObject = new JsonParser().parse(jsonBinding).getAsJsonObject();

			/* Creates a technology binding configuration from json object */
			KnxBindingConfig knxBindingConfig = new Gson().fromJson(jsonObject.get("knx").toString(),
					KnxBindingConfig.class);
			knxBindingConfig.setAdditionalProperty(knxBindingConfig.getDptID(),
					Tool.createDptXlator(knxBindingConfig.getDptID()));
			LOGGER.info("Created knxBindingConfig {}", knxBindingConfig);

			/*
			 * Saves the technology binding configuration by the group address - we need
			 * this in the method {@link #groupWrite()}
			 */
			knxGroupAddressBindingConfigurations.put(knxBindingConfig.getGroupAddress(), knxBindingConfig);

			/*
			 * Returns a technology binding configuration, which links with the
			 * corresponding {@link KuraChannelListener} - the {@link KuraDriver} stores
			 * this in the binding configuration map.
			 */
			return knxBindingConfig;
		} catch (Exception e) {
			LOGGER.error("createBinding ...", e);
			throw new NullPointerException(
					"Could not create knxBindingConfig from the given jsonBinding: " + jsonBinding);
		}
	}

	/**
	 * The task scans the actual status values with the given KNX group addresses
	 * and updates the values in the KURA framework with the {@link #doRead()}
	 * methods.
	 */
	public class KnxNetworkScanTask implements Runnable {
		private final Logger LOGGER = LoggerFactory.getLogger(KnxNetworkScanTask.class);

		/* The technology / KURA binding */
		final private Map<KnxBindingConfig, ThingChannelListener> updateBindingConfigurations;

		/**
		 * Constructor
		 * 
		 * @param updateBindingConfigurations the technology / KURA binding
		 *                                    configuration
		 */
		public KnxNetworkScanTask(Map<KnxBindingConfig, ThingChannelListener> updateBindingConfigurations) {
			this.updateBindingConfigurations = updateBindingConfigurations;
		}

		@Override
		public void run() {
			LOGGER.info("... initial KNX scan task started ...");
			/*
			 * Iterates over the updateBindingConfigurations. The key is the {@link
			 * KnxBindingConfig} object we need here.
			 */
			for (KnxBindingConfig key : updateBindingConfigurations.keySet()) {
				try {
					LOGGER.info("Scanning: reads the KNX payload {}", key.toString());
					/*
					 * Reads the actual status value from the KNX network. Starts the KNX read
					 * process with the given key ({@link KnxBindingConfig})
					 */
					doRead(key);
				} catch (Exception e) {
					LOGGER.error("KnxNetworkScanTask run ...", e);
				}
			}
			LOGGER.info("Initial knx scan task is ready - SHUTDOWN !");
		}
	}

	/**
	 * Starts the reconnect thread DIRECTLY and EVERY RECONNECT TIMEOUT.
	 */
	private void runReconnectTask(KnxDriver knxDriver) {
		if (this.reconnectHandle != null) {
			this.reconnectHandle.cancel(true);
		}

		/* The keepalived thread */
		this.reconnectHandle = this.scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					// Special case, the knxNetworkLink could be null. if we stop the application
					// and restart the application immediately, it is possible that the KNX Gateway
					// not accept the new connection in the doUpdate method, so the knxNetworkLink
					// is null.
					if (knxDriver.knxNetworkLink == null) {
						LOGGER.info("The knxNetworkLink is null, try to create a new knxNetworkLink");

						// Try to connect the KNX network again
						connectKnxNetwork(knxDriver);
					} else if (knxDriver.knxNetworkLink.isOpen() == true) {
						// Connection status TRUE
						LOGGER.info("The knxNetworkLink is connected: {}", knxDriver.knxNetworkLink.isOpen());

						/* Sends keepalived connected status false */
						Tool.sendKeepalived(knxDriver.options.getDriverServicePID(), KEEPALIVED_TOPIC, true,
								knxDriver.getEA());
					} else if (knxDriver.knxNetworkLink.isOpen() == false) {
						// Connection status FALSE, network not reachable or the KNX Gateway not accept
						// the connection
						LOGGER.info("The knxNetworkLink is NOT connected: {}, try reconnect ...",
								knxDriver.knxNetworkLink.isOpen());

						// Try to connect the KNX network again
						connectKnxNetwork(knxDriver);
					} else
						LOGGER.info("runReconnectTask => UNKNOWN case!");

				} catch (Exception e) {
					LOGGER.error("runReconnectHandle run ...", e);
					LOGGER.warn("Send keepalived OFFLINE, connection could not be reconnected !" + e);
					Tool.sendKeepalived(knxDriver.options.getDriverServicePID(), KEEPALIVED_TOPIC, false,
							knxDriver.getEA());
				}

			}
			// 0 = thread starts immediately, getReconnectTimeout() = thread will
			// be started again after timeout in milliseconds
		}, 0, options.getReconnectTimeout(), TimeUnit.MILLISECONDS);
	}

	/**
	 * Connect the KNX network
	 * 
	 * @param knxDriver the KNX driver
	 */
	public void connectKnxNetwork(KnxDriver knxDriver) throws InterruptedException, KNXException {
		LOGGER.info("Try to connect the KNX network");
		/* Create the local KNX net IP address */
		final InetSocketAddress remote = new InetSocketAddress(knxDriver.options.getGatewayIp(),
				knxDriver.options.getGatewayPort());

		/* Creates the local IP address. */
		final InetSocketAddress local = new InetSocketAddress(0);

		/* Creates the KNX UDP network link to the gateway */
		knxDriver.knxNetworkLink = KNXNetworkLinkIP.newTunnelingLink(local, remote, false, TPSettings.TP1);
		/*
		 * Starts the KNX monitoring listener to receive all group address status values
		 * from the KNX network/gateway.
		 */
		knxDriver.processCommunicator = new ProcessCommunicatorImpl(knxDriver.knxNetworkLink);
		knxDriver.processCommunicator.addProcessListener(knxDriver);

		LOGGER.info("The started knxNetworkLink {}", knxDriver.knxNetworkLink.toString());
		/* Sends keepalived connected status true */
		Tool.sendKeepalived(knxDriver.options.getDriverServicePID(), KEEPALIVED_TOPIC, true, knxDriver.getEA());

		/*
		 * Start also the scan process with delay, wait until all channel listeners have
		 * been started.
		 */
		knxDriver.scheduledExecutorService.schedule(new KnxNetworkScanTask(getUpdateBindingConfigurations()),
				knxDriver.options.getScanDelay(), TimeUnit.MILLISECONDS);
		LOGGER.info("Starts the KnxNetworkReadTask with delay {} msec", knxDriver.options.getScanDelay());
	}
}
