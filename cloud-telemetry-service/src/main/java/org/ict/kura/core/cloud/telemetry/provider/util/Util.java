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

import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Utility class.s
 * 
 * @author MK
 * @version 2023-05-25
 */
public class Util {
	/** To create an instance is not possible! */
	private Util() {
	}

	/**
	 * This method extracts the WoT properties with the corresponding values from
	 * the WoT payload and stores them in new {@link JsonObject}. Only primitive
	 * JSON data types are supported (boolean, string, float) - all other JSON data
	 * types are ignored and not written.
	 * 
	 * @param value the WoT payload
	 * @return a new {@link JsonObject} only with values without the time stamp
	 */
	public static JsonObject getValuesFromJson(JsonObject value) {
		JsonObject payload = new JsonObject();

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
						payload.add(key, p);
					} else if (p.isString()) {
						payload.add(key, p);
					} else if (p.isNumber()) {
						payload.add(key, p);
					}
				}
			}
		}
		return payload;
	}
}
