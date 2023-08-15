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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ict.gson.utils.AdapterFactory;
import org.ict.model.wot.core.ActionAffordance;
import org.ict.model.wot.core.PropertyAffordance;
import org.ict.model.wot.core.Thing;
import org.ict.model.wot.hypermedia.Form;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * An application that illustrates the deserialization processing of WoT
 * descriptions.
 * 
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2021-12-20
 */
public class WoTD_T {
	/* The logger instance */
	private static final Logger LOGGER = LogManager.getFormatterLogger(WoTD_T.class);

	/* We are using GSON */
	private static Gson gsonThing = AdapterFactory.getGsonWithDefaultWotTypeAdapters(true);

	/* The things map with WoT things linked with the file name */
	private static Map<String, Thing> things = new HashMap<>();

	/* The map to extract the WoT atType tags links with the href */
	private static Map<String, Object> typeMap = new HashMap<>();

	/* The map to extract the WoT properties linked with the href */
	private static Map<String, PropertyAffordance> propertyMap = new HashMap<>();

	/* The map to extract the WoT actions linked with the href */
	private static Map<String, ActionAffordance> actionMap = new HashMap<>();

	/* The folder path with WoT descriptions */
	private static String thingFolderPath;

	public static void main(String[] args) {
		try {
			switch (args.length) {
			case 1:
				/* Sets the folder path */
				thingFolderPath = args[0];
				LOGGER.info("Path: %s", thingFolderPath);
				break;
			default:
				LOGGER.info("\nArgs.length '%d', " + "required 1 parameter (folder path) !!!\n", args.length);
				/* Shutdown the application */
				System.exit(1);
			}
			/* Creates things from WoT JSON files */
			things = getAllFiles(new File(thingFolderPath), things);
			LOGGER.info("Things {}", things);

			/* Creates the maps */
			createMapsFromWoT(things);

			/* Logging ! */
			LOGGER.info("Thing size: %d", things.size());

			LOGGER.info("Thing: %s",
					things.get("https://localhost:8080/service/things/GAB_EnergyMeterWE1_chall_sensor_PowerTot"));

			LOGGER.info("PropertiesMap Size: %d", propertyMap.size());

			LOGGER.info("Property Form: %s",
					propertyMap.get("https://gateway/things/GAB_EnergyMeterWE1_chall_sensor_PowerTot/properties/power")
							.getForms());

			LOGGER.info("ActionsMap Size: %d", actionMap.size());
			LOGGER.info("TypeMap Size: %d, %s", typeMap.size(), typeMap.toString());
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	private static Map<String, Thing> getAllFiles(File curDir, Map<String, Thing> things)
			throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Map<String, Thing> thingMap = things;
		File[] filesList = curDir.listFiles();
		for (File f : filesList) {
			if (f.isDirectory())
				thingMap.putAll(getAllFiles(f, thingMap));
			if (f.isFile()) {
				if (f.getName().contains(".json")) {
					Thing thing = gsonThing.fromJson(new FileReader(f), Thing.class);
					thingMap.put(thing.getId().toString(), thing);
				}
			}
		}
		return thingMap;
	}

	private static void createMapsFromWoT(Map<String, Thing> things) {
		for (String key : things.keySet()) {
			for (Map.Entry<String, PropertyAffordance> entry : things.get(key).getProperties().entrySet()) {
				String hrefProperty = null;
				for (Form form : entry.getValue().getForms()) {
					hrefProperty = form.getHref().toString();
				}
				typeMap.put(hrefProperty, entry.getValue().getAtType());
				propertyMap.put(hrefProperty, entry.getValue());
			}

			try {
				for (Map.Entry<String, ActionAffordance> entry : things.get(key).getActions().entrySet()) {
					String hrefProperty = null;
					for (Form form : entry.getValue().getForms()) {
						hrefProperty = form.getHref().toString();
					}
					typeMap.put(hrefProperty, entry.getValue().getAtType());
					actionMap.put(hrefProperty, entry.getValue());
				}
			} catch (Exception e) {
				if (e instanceof NullPointerException)
					LOGGER.info("No action found ...");
				else
					LOGGER.error("", e);
			}
		}
	}
}
