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
package org.ict.kura.internal.driver.openweather.util;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * This class implements required helper methods.
 * 
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2021-10-19
 */
public class Tool {
	/**
	 * Creates a random value.
	 * 
	 * @param min the min value
	 * @param max the max value
	 * @return the random value
	 */
	public static int getRandomNumberInRange(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

	/**
	 * Extracts the json sub element from the given json element corresponding with
	 * the key
	 * 
	 * @param findKey     the value name
	 * @param jsonElement the json element received from the open weather API
	 * @return returns the sub json element corresponding to the key
	 */
	public static JsonElement getValueByKey(String findKey, JsonElement jsonElement) {
		JsonElement value = null;

		if (jsonElement.isJsonArray()) {
			for (JsonElement element : jsonElement.getAsJsonArray()) {
				value = getValueByKey(findKey, element);
				if (value != null) {
					return value;
				}
			}
		} else {
			if (jsonElement.isJsonObject()) {
				Set<Map.Entry<String, JsonElement>> entrySet = jsonElement.getAsJsonObject().entrySet();
				for (Map.Entry<String, JsonElement> entry : entrySet) {
					if (entry.getKey().equals(findKey)) {
						value = entry.getValue();
						return value;
					}
					value = getValueByKey(findKey, entry.getValue());
					if (value != null) {
						return value;
					}
				}
			} else {
				if (jsonElement.toString().equals(findKey)) {
					value = jsonElement;
					if (value != null) {
						return value;
					}
				}
			}
		}
		return value;
	}

	/**
	 * Searches the key (value name) in the json element and returns the current
	 * value.
	 * 
	 * @param findKey     the value name
	 * @param jsonElement the json element received from the open weather API
	 * @return the value as object, but converted to a correct data type.
	 */
	public static Object getObjectByKey(String findKey, JsonElement jsonElement) {
		/*
		 * Searches the key in the json element and returns the sub json element
		 * corresponding to the key
		 */
		JsonElement element = getValueByKey(findKey, jsonElement);

		/* Gets the json primitive data type */
		JsonPrimitive p = element.getAsJsonPrimitive();

		/*
		 * Checks the json primitive data type and creates a new value object with the
		 * given data type und the value.
		 */
		if (p.isBoolean()) {
			/* Returns the value in boolean format */
			return Boolean.valueOf(p.getAsBoolean());
		} else if (p.isString()) {
			/* Returns the value in string format */
			return new String(p.getAsString());
		} else if (p.isNumber()) {
			/* Returns the value in double format */
			return Double.valueOf(p.getAsDouble());
		} else {
			/* Returns the value in string format, if everything else does not apply */
			return new String(p.getAsString());
		}
	}
}
