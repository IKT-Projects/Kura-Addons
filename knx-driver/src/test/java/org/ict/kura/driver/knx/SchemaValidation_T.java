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

import org.everit.json.schema.loader.SchemaLoader;
import org.ict.gson.utils.AdapterFactory;
import org.ict.model.wot.core.Thing;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class SchemaValidation_T {
	/* The logger instance */
	private static final Logger LOGGER = LoggerFactory.getLogger(SchemaValidation_T.class);

	public static void main(String[] args) {
		try {
			LOGGER.info("start...");

			/* The gson object to convert json string to {@link Thing} */
			Gson gsonThing = AdapterFactory.getGsonWithDefaultWotTypeAdapters(true);

			/* Creates a thing object from the thing description file */
			Thing thing = gsonThing.fromJson(new FileReader("conf/GAB_WorkroomLight.json"), Thing.class);

			LOGGER.info("Thing: {}", thing.getActions().get("onOff").getInput().toString());

			/* Validates the schema of the value */
			SchemaLoader.load(new JSONObject(gsonThing.toJson(thing.getActions().get("onOff").getInput())))
					.validate(new JSONObject("{\"onOff\": true}"));
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}
}
