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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.ict.kura.core.database.influx.InfluxDbService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * The persistence task stores all channel state changes into the influx
 * database. The database layout is based on the WoT descriptions with thing
 * name, property name, time (stamp), the payload properties (influx fields) and
 * values.
 * 
 * @author ICT M. Biskup
 * @author ICT M. Kuller
 * @version 2022-01-14
 */
public class InfluxDbServicePersistenceTask implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(InfluxDbServicePersistenceTask.class);

	/* The event from the {@link EventAdmin} */
	final private Event event;
	/* The influx service instance is needed to store the channel state changes */
	final private InfluxDbService influxDbService;

	/**
	 * Constructor.
	 * 
	 * @param event           the event from the {@link EventAdmin}
	 * @param influxDbService the influx service instance
	 */
	public InfluxDbServicePersistenceTask(Event event, InfluxDbService influxDbService) {
		this.event = event;
		this.influxDbService = influxDbService;
	}

	@Override
	public void run() {
		try {
			LOGGER.debug("Save channel state change in influx database with ... thingName: {}, propertyName: {}",
					event.getProperty("thingName"), event.getProperty("propertyName"));

			/* Gets the WoT payload */
			JsonObject value = (JsonObject) event.getProperty("value");

			LOGGER.debug("WoT payload {}", value);

			/* Extracts the timestamp from the WoT payload */
			long timestamp = value.get("time").getAsLong();

			/*
			 * Extracts the the WoT payload properties (influx fields) - ignore the data
			 * types JsonArray, JsonObject, JsonNull.
			 */
			Map<String, Object> fields = getValuesFromJson(value);

			LOGGER.info("WoT payload properties (influx fields) {}", fields);

			/*
			 * Empty fields are possible as we ignore JsonArray, JsonObject, JsonNull - only
			 * data points with valid data types are written to the database.
			 */
			if (!fields.isEmpty()) {
				influxDbService.save(event.getProperty("thingName").toString(),
						event.getProperty("propertyName").toString(), timestamp, fields);
				LOGGER.debug("The payload is written to the database asynchronously: {}", value);
			} else {
				LOGGER.debug("The payload was ignored - does not correspond to the specified layout: {}", value);
			}
		} catch (Throwable t) {
			LOGGER.error("", t);
		}
	}

	/**
	 * This method extracts the WoT properties with the corresponding values from
	 * the WoT payload and stores them in a map. Only primitive JSON data types are
	 * supported (boolean, string, float) - all other JSON data types are ignored
	 * and not written to the database.
	 * 
	 * @param value the WoT payload
	 * @return the map with WoT properties (influx fields) and the corresponds
	 *         values
	 */
	public Map<String, Object> getValuesFromJson(JsonObject value) {
		Map<String, Object> fields = new HashMap<>();

		Set<Entry<String, JsonElement>> entries = value.entrySet();
		for (Entry<String, JsonElement> entry : entries) {
			String key = entry.getKey();
			if (!key.equals("time")) {
				JsonElement ele = entry.getValue();
				if (ele.isJsonObject() || ele.isJsonArray() || ele.isJsonNull())
					continue;
				if (ele.isJsonPrimitive()) {
					JsonPrimitive p = ele.getAsJsonPrimitive();
					if (p.isBoolean()) {
						fields.put(key, p.getAsBoolean());
					} else if (p.isString()) {
						fields.put(key, p.getAsString());
					} else if (p.isNumber()) {
						fields.put(key, p.getAsFloat());
					}
				}
			}
		}
		return fields;
	}
}
