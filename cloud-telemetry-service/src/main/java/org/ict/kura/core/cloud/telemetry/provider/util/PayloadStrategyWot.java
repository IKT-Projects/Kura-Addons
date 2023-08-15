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
package org.ict.kura.core.cloud.telemetry.provider.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.kura.cloudconnection.message.KuraMessage;
import org.eclipse.kura.message.KuraPayload;
import org.osgi.service.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

/**
 * Payload strategy class for WoT Thing Descriptions
 * 
 * @author MK
 * @version 2023-05-25
 */
public class PayloadStrategyWot implements PayloadStrategyIf {
	private static final Logger LOGGER = LoggerFactory.getLogger(PayloadStrategyWot.class);

	public KuraMessage convertMessage(Event event) {
		LOGGER.info("Strategy WoT converts the given event {}", event.toString());

		// Extracts the thing name
		String thingName = (String) event.getProperty("thingName");
		// Extracts the thing property name
		String propertyName = (String) event.getProperty("propertyName");
		/* Gets the value object from the event {timestamp: xx, temperature: xx} */
		JsonObject value = (JsonObject) event.getProperty("value");

		/* Create a {@link KuraPayload} */
		KuraPayload payload = new KuraPayload();

		/* Sets the value (wot payload) into the body */
		payload.setBody(value.toString().getBytes());

		/* Create an options map for the {@link KuraMessage} */
		Map<String, Object> map = new HashMap<>();
		map.put("thingName", thingName);
		map.put("propertyName", propertyName);

		/* Creates the {@link KuraMessage} */
		return new KuraMessage(payload, map);
	}
}
