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
package org.ict.kura.core.cloud.telemetry.provider;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.kura.cloudconnection.publisher.CloudPublisher;
import org.eclipse.kura.configuration.ConfigurableComponent;
import org.eclipse.kura.data.DataService;
import org.ict.kura.core.cloud.telemetry.provider.util.PayloadContext;
import org.ict.kura.core.cloud.telemetry.provider.util.PayloadStrategyTb;
import org.ict.kura.core.cloud.telemetry.provider.util.PayloadStrategyWot;
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
import org.osgi.service.event.EventHandler;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The cloud telemetry service captures all channel state changes using the
 * {@link EventHandler} of the {@link EventAdmin} service and redirects them to
 * a mqtt broker using the {@link DataService}.
 * 
 * The {@link DataService} endpoint (MQTT broker) can be changed at runtime via
 * the web admin - cloud service!
 * 
 * @author ICT M. Biskup
 * @author ICT M. Kuller
 * @version 2023-05-15
 */

@Designate(ocd = CloudTelemetryProviderConfig.class, factory = true)
@Component(immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE, service = { EventHandler.class,
		ConfigurableComponent.class }, name = "org.ict.kura.core.cloud.telemetry.provider.CloudTelemetryProvider", property = {
				"event.topics=telemetry/*",
				"service.pid=org.ict.kura.core.cloud.telemetry.provider.CloudTelemetryProvider" })
public class CloudTelemetryProvider implements ConfigurableComponent, EventHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(CloudTelemetryProvider.class);
	private static final String APP_ID = "org.ict.kura.core.cloud.telemetry.provider.CloudTelemetryProvider";

	/* The thread cached pool to manage multiple state changes */
	private ExecutorService executorService;
	/* The service options */
	private CloudTelemetryProviderOptions options;
	/*
	 * The kura {@link CloudPublisher} - for this you must configure a new Cloud
	 * Connection.
	 */
	private CloudPublisher cloudPublisher;

	/* Saves the strategy to convert the payload; */
	private PayloadContext payloadContext = new PayloadContext();

//	@Activate
//	protected void activate(ComponentContext componentContext) {
//		LOGGER.info("Bundle " + APP_ID + " has started without configuration!");
//	}

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
			if (this.executorService != null)
				this.executorService.shutdownNow();

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
		this.options = new CloudTelemetryProviderOptions(properties);

		LOGGER.info("Properties {}", this.options.toString());

		if (!this.options.getCommonEnable()) {
			/* Stops the service */
			LOGGER.info("Disables the service {}", APP_ID);

		} else {
			/* Starts the service */
			LOGGER.info("Enables the service {}", APP_ID);

			/* Starts the thread cache pool immediately */
			executorService = Executors.newCachedThreadPool();
		}

		/* Sets the payload convert strategy */
		switch (this.options.getStrategy()) {
		case "wot":
			payloadContext.setStrategy(new PayloadStrategyWot());
			break;
		case "thingsBoard":
			payloadContext.setStrategy(new PayloadStrategyTb());
			break;
		default:
			LOGGER.info("INVALID convert strategy configuration, working with the default strategy WoT");
		}
	}

	@Reference(name = "CloudPublisher", service = CloudPublisher.class, cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC, unbind = "unsetCloudPublisher", bind = "setCloudPublisher")
	public void setCloudPublisher(CloudPublisher cloudPublisher) {
		this.cloudPublisher = cloudPublisher;
	}

	public void unsetCloudPublisher(CloudPublisher cloudPublisher) {
		this.cloudPublisher = null;
	}

	@Override
	public void handleEvent(Event event) {
		/*
		 * Call the {@link CloudServicePersistenceTask} to process the event
		 * asynchronously.
		 */
		LOGGER.info("Receiving the event: {}", event.getTopic());

		/* If there is no {@link CloudPublisher} available, do nothing. */
		if (this.cloudPublisher == null) {
			LOGGER.info("No data service selected. Cannot publish!");
			return;
		}
		/* Starts the cloud task to redirects the data asynchronously */
		executorService.submit(new CloudTelemetryProviderTask(event, cloudPublisher, payloadContext));
	}

}