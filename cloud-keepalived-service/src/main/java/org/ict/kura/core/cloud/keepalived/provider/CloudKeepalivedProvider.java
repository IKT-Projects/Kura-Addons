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
package org.ict.kura.core.cloud.keepalived.provider;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.kura.asset.AssetService;
import org.eclipse.kura.cloudconnection.message.KuraMessage;
import org.eclipse.kura.cloudconnection.publisher.CloudPublisher;
import org.eclipse.kura.configuration.ConfigurableComponent;
import org.eclipse.kura.data.DataService;
import org.ict.kura.core.cloud.keepalived.provider.util.PayloadContext;
import org.ict.kura.core.cloud.keepalived.provider.util.PayloadStrategyTb;
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
import org.osgi.service.event.EventHandler;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The cloud keepalived service sends a keepalived message for each asset to a
 * MQTT broker using the {@link DataService}.
 * 
 * The {@link DataService} endpoint (MQTT broker) can be changed at runtime via
 * the web admin - cloud service!
 * 
 * @author ICT M. Biskup
 * @author ICT M. Kuller
 * @version 2023-06-29
 * 
 * @see <a href=
 *      "https://eclipse.github.io/kura/cloud-api/2-user-guide.html">Configure a
 *      Kura Cloud Connection</a>
 */

@Designate(ocd = CloudKeepalivedProviderConfig.class, factory = true)
@Component(immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE, service = { EventHandler.class,
		ConfigurableComponent.class }, name = "org.ict.kura.core.cloud.keepalived.provider.CloudKeepalivedProvider", property = {
				"event.topics=drivers/keepalived",
				"service.pid=org.ict.kura.core.cloud.keepalived.provider.CloudKeepalivedProvider" })
public class CloudKeepalivedProvider implements ConfigurableComponent, EventHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(CloudKeepalivedProvider.class);
	private static final String APP_ID = "org.ict.kura.core.cloud.keepalived.provider.CloudKeepalivedProvider";

	/* The thread cached pool to manage multiple state changes */
	private ScheduledExecutorService scheduledExecutorService;
	private ScheduledFuture<?> keepalivedHandle;

	/* The service options */
	private CloudKeepalivedProviderOptions options;

	/* The asset service */
	private AssetService assetService;

	/* Map with the driver connection status */
	private SortedMap<String, Boolean> driverConnectionStatus = Collections
			.synchronizedSortedMap(new TreeMap<String, Boolean>());

	/*
	 * The kura {@link CloudPublisher} - for this you must configure a new Cloud
	 * Connection.
	 */
	private CloudPublisher cloudPublisher;

	/* Saves the strategy to convert the payload; */
	private PayloadContext payloadContext = new PayloadContext();

	@Activate
	protected void activate(ComponentContext componentContext, Map<String, Object> properties) {
		LOGGER.info("Bundle " + APP_ID + " has started!");
		/*
		 * Saves the component context - we use this to get the bundle context
		 * (EventHandler)
		 */
		/* Updates the configuration */
		doUpdate(properties);
	}

	@Deactivate
	protected void deactivate(ComponentContext componentContext) {
		LOGGER.info("Bundle " + APP_ID + " has stopped!");
		/* Closes all resources */
		doDeactivate();
	}

	@Modified
	public void updated(Map<String, Object> properties) {
		/* Deactivates all components */
		doDeactivate();

		/* Updates all components with new configuration */
		doUpdate(properties);
	}

	/**
	 * Deactivates all resources means unregisters all channel listener - bundle
	 * shutdown.
	 */
	private void doDeactivate() {
		try {
			LOGGER.info("Bundle " + APP_ID + " do deactivate!");

			/* Shutdown the thread cache pool immediately */
			if (this.scheduledExecutorService != null)
				this.scheduledExecutorService.shutdownNow();

		} catch (Throwable T) {
			LOGGER.error(T.getMessage());
		}
	}

	/**
	 * Updates all resources.
	 * 
	 * @param properties the configuration parameters {@link #updated(Map<String,
	 *                   Object> properties)}
	 */
	private void doUpdate(Map<String, Object> properties) {
		/* Saves the properties into the options object */
		this.options = new CloudKeepalivedProviderOptions(properties);

		LOGGER.info("Properties {}", this.options.toString());

		LOGGER.info("Configured strategy {}", this.options.getStrategy());
		/* Sets the keepalived strategy */
		switch (this.options.getStrategy()) {
		case "thingsBoard":
			payloadContext.setStrategy(new PayloadStrategyTb());
			break;
		default:
			LOGGER.info("INVALID strategy configuration, working with the default strategy ThingsBoard");
		}

		if (!this.options.getCommonEnable()) {
			/* Stops the service */
			LOGGER.info("Disables the service {}", APP_ID);

			/* Shutdown the thread cache pool immediately */
			if (this.scheduledExecutorService != null)
				this.scheduledExecutorService.shutdown();
		} else {
			/* Starts the service */
			LOGGER.info("Enables the service {}", APP_ID);

			/* Starts the thread cache pool immediately */
			this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

			/* Starts the scheduled keepalived thread */
			doKeepalivedUpdate();
		}
	}

	@Reference(name = "CloudPublisher", service = CloudPublisher.class, cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC, unbind = "unsetCloudPublisher", bind = "setCloudPublisher")
	public void setCloudPublisher(CloudPublisher cloudPublisher) {
		this.cloudPublisher = cloudPublisher;
	}

	public void unsetCloudPublisher(CloudPublisher cloudPublisher) {
		this.cloudPublisher = null;
	}

	@Reference(name = "AssetService", service = AssetService.class, cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "unsetAssetService", bind = "setAssetService")
	protected void setAssetService(AssetService assetService) {
		this.assetService = assetService;
	}

	protected void unsetAssetService(AssetService assetService) {
		this.assetService = null;
	}

	@Override
	public void handleEvent(Event event) {
		LOGGER.info("Receiving the event on topic: " + event.getTopic() + ", driverServicePid: "
				+ event.getProperty("driverServicePid") + ", connection status: " + event.getProperty("connected"));
		driverConnectionStatus.put(String.valueOf(event.getProperty("driverServicePid")),
				(Boolean) event.getProperty("connected"));
		driverConnectionStatus.forEach((key, value) -> {
			LOGGER.info("Key - " + key + ", Value - " + value);
		});
	}

	/**
	 * Starts the keepalived thread DIRECTLY EVERY KEEPALIVED TIMEOUT.
	 */
	private void doKeepalivedUpdate() {
		if (this.keepalivedHandle != null) {
			this.keepalivedHandle.cancel(true);
		}

		/* The keepalived thread */
		this.keepalivedHandle = this.scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				// Creates and sends the keepalived messages for each asset
				assetService.listAssets().forEach(e -> {
					try {
						LOGGER.info("Keepalived for AssetPid: " + assetService.getAssetPid(e) + " and DriverPid: "
								+ assetService.getAsset(assetService.getAssetPid(e)).getAssetConfiguration()
										.getDriverPid());

						// Extracts the connected status
						Boolean connected = (Boolean) driverConnectionStatus.get(assetService
								.getAsset(assetService.getAssetPid(e)).getAssetConfiguration().getDriverPid());

						// Check if the key was found in the map
						if (connected == null) {
							// The driver is not connected with devices, so we do nothing
							LOGGER.info("Keepalived NOT possible for AssetPid: " + assetService.getAssetPid(e)
									+ " because the DriverPid: " + assetService.getAsset(assetService.getAssetPid(e))
											.getAssetConfiguration().getDriverPid()
									+ " was not found in the driverConnectionStatus map.");
						}
						// Check if the driver is connected
						else if ((Boolean) driverConnectionStatus.get(assetService.getAsset(assetService.getAssetPid(e))
								.getAssetConfiguration().getDriverPid()) == true) {
							// Creates the message
							KuraMessage message = payloadContext.create(assetService.getAssetPid(e));
							LOGGER.info("Send keepalived payload: " + new String(message.getPayload().getBody()));
							// Calls the given strategy and publishes the keepalived message to the
							// configured broker
							cloudPublisher.publish(message);
						} else {
							// The driver is not connected with devices, so we do nothing
							LOGGER.info("Keepalived NOT possible for AssetPid: " + assetService.getAssetPid(e)
									+ " because the DriverPid: " + assetService.getAsset(assetService.getAssetPid(e))
											.getAssetConfiguration().getDriverPid()
									+ " is not connected.");
						}
					} catch (Throwable t) {
						LOGGER.error("", t);
					}
				});
			}
			// 0 = thread starts immediately, options.getKeepalivedTimeout() = thread will
			// be started again after timeout in milliseconds
		}, 0, options.getKeepalivedTimeout(), TimeUnit.MILLISECONDS);
	}
}