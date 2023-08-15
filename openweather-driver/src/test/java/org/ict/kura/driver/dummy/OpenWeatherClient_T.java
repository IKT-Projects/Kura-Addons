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
package org.ict.kura.driver.dummy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.ict.kura.internal.driver.openweather.client.OpenWeatherClient;
import org.ict.kura.internal.driver.openweather.util.Tool;

import com.google.gson.JsonElement;

public class OpenWeatherClient_T {
	static List<String> list = new ArrayList<String>();

	public static void main(String[] args) {
		OpenWeatherClient client = new OpenWeatherClient("Dortmund", "de", "7df8c19b9799805abc69546513e471c7");
		// oldJsonNode response = client.getWeather();

		JsonElement element = client.getWeather();
//		System.out.println(client.getValueByKey("temp", element));
		System.out.println(Tool.getValueByKey("humidity", element));
//		check("lon", element);
//		System.out.println(searchJson("lon", element));
//		System.out.println(check2("temp", element));

	}

	@SuppressWarnings("unused")
	private static JsonElement getValueByKey(String findKey, JsonElement jsonElement) {
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

	public static String searchJson(String key, JsonElement jsonElement) {

		String value = null;

		// If input is an array, iterate through each element
		if (jsonElement.isJsonArray()) {
			for (JsonElement jsonElement1 : jsonElement.getAsJsonArray()) {
				value = searchJson(key, jsonElement1);
				if (value != null) {
					return value;
				}
			}
		} else {
			// If input is object, iterate through the keys
			if (jsonElement.isJsonObject()) {
				Set<Entry<String, JsonElement>> entrySet = jsonElement.getAsJsonObject().entrySet();
				for (Entry<String, JsonElement> entry : entrySet) {

					// If key corresponds to the
					String key1 = entry.getKey();
					if (key1.equals(key)) {
						value = entry.getValue().toString();
						return value;
					}

					// Use the entry as input, recursively
					value = searchJson(key, entry.getValue());
					if (value != null) {
						return value;
					}
				}
			}

			// If input is element, check whether it corresponds to the key
			else {
				if (jsonElement.toString().equals(key)) {
					value = jsonElement.toString();
					return value;
				}
			}
		}
		return value;
	}

}
