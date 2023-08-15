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
package org.ict.kura.driver.thing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.Map;

import org.eclipse.kura.channel.ChannelFlag;
import org.eclipse.kura.channel.ChannelRecord;
import org.eclipse.kura.channel.ChannelStatus;
import org.ict.gson.utils.AdapterFactory;
import org.ict.model.wot.core.Thing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * Gets thing description from folder, directory, ...
 * 
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2021-04-20
 */
public class Util {
	public static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

	/* Singleton instance - eager loading */
	private static Util instance = new Util();

	/* To create an instance - its not possible */
	private Util() {
		/* Exists only to defeat instantiation */
	}

	/*
	 * Supports only ONE (and the same) instance for all users
	 */
	public static Util getInstance() {
		/** Eager loading */
		return instance;
	}

	/**
	 * This method reads recursive all thing descriptions from a folder structure
	 * 
	 * @param currrentFolder the current (root) folder in which the search is
	 *                       started
	 * @param things         the thing map (IN/OUT)
	 * @return the thing map with all found things
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 * @throws FileNotFoundException
	 */
	public static Map<String, Thing> getThingsFromFolder(File currrentFolder, Map<String, Thing> things)
			throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		LOGGER.info("... getThingsFromFolder, currentFolder {}", currrentFolder);

		/* The gson object to convert json string to {@link Thing} */
		Gson gsonThing = AdapterFactory.getGsonWithDefaultWotTypeAdapters(true);

		/* For recursive search we need temporary thing map */
		Map<String, Thing> thingsTmp = things;

		/* Gets all (sub) directories and files */
		File[] filesList = currrentFolder.listFiles();

		/* Iterates over the file list */
		for (File f : filesList) {
			/* The file list entry is of type directory */
			if (f.isDirectory())
				/*
				 * Recursive call with the argument - the actual thing map. The file is of type
				 * folder, so we call this method again to search for more files in the sub
				 * folder.
				 */
				thingsTmp.putAll(getThingsFromFolder(f, thingsTmp));
			/* The file list entry is of type file with extension .json */
			if (f.isFile() && f.getName().contains(".json")) {
				/* Creates a thing object from the thing description file */
				try {
					LOGGER.info("Thing string: {}", new String(new String(Files.readAllBytes(f.toPath()))));
				} catch (Exception e) {
					LOGGER.error("", e);
				}
				Thing thing = gsonThing.fromJson(new FileReader(f), Thing.class);
				/* Puts the thing into the thing map */
				thingsTmp.put(thing.getId().toString(), thing);
			}
		}
		/* Returns the thing map */
		return thingsTmp;
	}

	/**
	 * Creates a failure record for the given {@link ChannelRecord}.
	 * 
	 * @param record             the {@link ChannelRecord}
	 * @param writeFailedMessage the failure message text
	 */
	public static final void setFailureRecord(ChannelRecord record, String writeFailedMessage) {
		record.setChannelStatus(new ChannelStatus(ChannelFlag.FAILURE, writeFailedMessage, null));
		record.setTimestamp(System.currentTimeMillis());
		LOGGER.warn(writeFailedMessage);
	}
}
