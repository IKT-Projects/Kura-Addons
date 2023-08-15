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
package org.ict.kura.driver.knx;

import java.io.FileReader;

import org.ict.kura.internal.driver.knx.util.KnxBindingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Binding_T {
	/* The logger instance */
	private static final Logger LOGGER = LoggerFactory.getLogger(Binding_T.class);

	public static void main(String[] args) {
		// Read from File to String
		JsonObject jsonObject = new JsonObject();

		try {
			JsonParser parser = new JsonParser();
			JsonElement jsonElement = parser.parse(new FileReader("backup/binding/binding.json"));
			jsonObject = jsonElement.getAsJsonObject();
			LOGGER.info("jo {}", jsonObject);
			LOGGER.info("jo binding {}", jsonObject.get("binding"));
			LOGGER.info("jo knx {}", jsonObject.get("knx"));
			KnxBindingConfig kbc = new Gson().fromJson(jsonObject.get("knx").getAsJsonArray().get(0).toString(),
					KnxBindingConfig.class);
			LOGGER.info("kbc {}", kbc);
		} catch (Exception ex) {

		}
	}
}
