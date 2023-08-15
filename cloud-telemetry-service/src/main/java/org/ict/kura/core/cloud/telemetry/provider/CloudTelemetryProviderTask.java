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

import org.eclipse.kura.cloudconnection.message.KuraMessage;
import org.eclipse.kura.cloudconnection.publisher.CloudPublisher;
import org.ict.kura.core.cloud.telemetry.provider.util.PayloadContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * The cloud service task redirects all channel state changes to a MQTT broker.
 * 
 * @author ICT M. Biskup
 * @author ICT M. Kuller
 * @version 2020-10-21
 */
public class CloudTelemetryProviderTask implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(CloudTelemetryProviderTask.class);

	/* The event from the {@link EventAdmin} */
	final private Event event;

	/*
	 * The kura {@link CloudPublisher} - for this you must configure a new Cloud
	 * Connection.
	 */
	private CloudPublisher cloudPublisher;

	/* Saves the strategy to convert the payload */
	private PayloadContext payloadContext;

	/* A gson builder to create a json formatted payload. */
	@SuppressWarnings("unused")
	final private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * Constructor.
	 * 
	 * @param event       the event from the {@link EventAdmin}
	 * @param dataService the data service instance
	 */
	public CloudTelemetryProviderTask(Event event, CloudPublisher cloudPublisher, PayloadContext payloadContext) {
		this.event = event;
		this.cloudPublisher = cloudPublisher;
		this.payloadContext = payloadContext;
	}

	@Override
	public void run() {
		try {
			LOGGER.info("Send channel state change of the event {}, execute the {}", event.toString(),
					payloadContext.toString());
			// Converts the message
			KuraMessage message = payloadContext.convert(event);
			LOGGER.info("Payload: "+ new String(message.getPayload().getBody()));
			// Calls the given strategy and publishes the message to the configured broker
			cloudPublisher.publish(message);
		} catch (Throwable t) {
			LOGGER.error("", t);
		}
	}
}
