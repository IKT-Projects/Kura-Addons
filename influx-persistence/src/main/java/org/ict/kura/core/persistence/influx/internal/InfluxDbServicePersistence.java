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
package org.ict.kura.core.persistence.influx.internal;

/*-
 * #%L
 * org.ict.kura.core.persistence.influx.provider
 * %%
 * Copyright (C) 2023 ICT - FH-Dortmund
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.kura.configuration.ConfigurableComponent;
import org.ict.kura.core.database.influx.InfluxDbService;
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
 * The influx database service persistence captures all channel state changes
 * using the {@link EventHandler} of the {@link EventAdmin} service and stores
 * them in the influx database using the influx database service
 * {@link InfluxDbService}.
 * 
 * @author ICT M. Biskup
 * @author ICT M. Kuller
 * @version 2020-10-12
 */

@Designate(ocd = InfluxDbServicePersistenceConfig.class, factory = true)
@Component(immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE, service = { EventHandler.class,
		ConfigurableComponent.class }, property = { "event.topics=telemetry/*",
				"service.pid=org.ict.kura.persistence.influx.InfluxDbServicePersistence" }, name = "org.ict.kura.persistence.influx.InfluxDbServicePersistence")
public class InfluxDbServicePersistence implements ConfigurableComponent, EventHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(InfluxDbServicePersistence.class);
	private static final String APP_ID = "org.ict.kura.persistence.influx.InfluxDbServicePersistence";

	/* The influx service instance */
	private InfluxDbService influxDbService;
	/* The thread cached pool to manage multiple state changes */
	private ExecutorService executorService;
	/* The driver options */
	private InfluxDbServicePersistenceOptions options;

	@Activate
	protected void activate(ComponentContext componentContext, Map<String, Object> properties) {
		LOGGER.info("Bundle " + APP_ID + " has started!");

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
		this.options = new InfluxDbServicePersistenceOptions(properties);

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
	}

	/*
	 * This annotation adds the influx database service to the
	 * OSGI-INF/org.ict.kura.service.influx.persistence.InfluxDbServicePersistence.
	 * xml from source code.
	 */
	@Reference(name = "InfluxDbService", service = InfluxDbService.class, cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC, unbind = "unsetInfluxService", bind = "setInfluxService")
	/**
	 * This methods receives the {@link InfluxDbService} from kura OSGi.
	 * 
	 * @param InfluxDbService the OSGi influx service to store values into the
	 *                        influx database
	 */
	public void setInfluxService(InfluxDbService influxDbService) {
		LOGGER.info("... setInfluxService");
		this.influxDbService = influxDbService;
		LOGGER.info("InfluxDbService database ping: {}", influxDbService.ping());
	}

	/**
	 * Removes the {@link InfluxDbService} - sets the local instance equal null.
	 * 
	 * @param influxDbService the influx database service
	 */
	public void unsetInfluxService(InfluxDbService influxDbService) {
		LOGGER.info("... unsetInfluxService");
		this.influxDbService = null;
	}

	@Override
	public void handleEvent(Event event) {
		/*
		 * Call the {@link InfluxDbServicePersistenceTask} to process the event
		 * asynchronously.
		 */
		LOGGER.info("Receiving the event: {}", event.getTopic());

		/* Starts the persistence task to store data into the database asynchronously */
		executorService.submit(new InfluxDbServicePersistenceTask(event, influxDbService));
	}
}
