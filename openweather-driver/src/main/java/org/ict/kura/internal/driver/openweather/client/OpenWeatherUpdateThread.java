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
package org.ict.kura.internal.driver.openweather.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.ict.kura.driver.thing.ThingChannelListener;
import org.ict.kura.internal.driver.openweather.util.OpenWeatherBindingConfig;
import org.ict.kura.internal.driver.openweather.util.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;

/**
 * The open weather update thread.
 * 
 * This class starts a thread to update the weather data with the given poll
 * interval.
 * 
 * @author M. Biskup
 * @author M. Kuller
 * @version 2021-10-20
 */
public class OpenWeatherUpdateThread implements Runnable, Closeable {
	/* The open weather update thread logger instance */
	private static final Logger LOGGER = LoggerFactory.getLogger(OpenWeatherUpdateThread.class);

	/* The open weather client instance */
	private OpenWeatherClient openWeatherClient;

	/* The map of technology bindings */
	private Map<String, OpenWeatherBindingConfig> openWeatherBindingConfigs;

	/* The {@link ThingChannelListener} map */
	Map<String, ThingChannelListener> thingChannelListeners;

	/* The poll interval */
	private int pollInterval;

	/* the Thread instance */
	private Thread runner;

	/* The payload format */
	final String payloadFormat = "{\"time\" : %d, \"%s\": %s}";

	/**
	 * Constructor of {@link OpenWeatherUpdateThread}
	 * 
	 * @param openWeatherClient     the open weather client instance
	 * @param thingChannelListeners binding configuration from thing driver
	 * @param pollinterval          the polling interval
	 */
	public OpenWeatherUpdateThread(OpenWeatherClient openWeatherClient,
			Map<String, OpenWeatherBindingConfig> openWeatherBindingConfigs,
			Map<String, ThingChannelListener> thingChannelListeners, int pollInterval) {
		LOGGER.info("constructor...");
		Objects.requireNonNull(openWeatherClient, "INVALID - the openWeatherClient is null !");
		Objects.requireNonNull(openWeatherBindingConfigs, "INVALID - the openWeatherBindingConfigs is null !");
		Objects.requireNonNull(thingChannelListeners, "INVALID - the thingChannelListeners is null !");
		this.openWeatherClient = openWeatherClient;
		this.openWeatherBindingConfigs = openWeatherBindingConfigs;
		this.thingChannelListeners = thingChannelListeners;
		this.pollInterval = pollInterval;
		/* Creates a thread instance */
		this.runner = new Thread(this);
		/* Starts the module as a thread */
		this.runner.start();
	}

	/**
	 * Thread method that polls the current weather data.
	 */
	public void run() {
		String threadName = Thread.currentThread().getName();
		LOGGER.info("Start thread {}", threadName);
		try {
			/* Empirically first sleep */
			TimeUnit.SECONDS.sleep(5);

			/* Thread is running while it is not interrupted */
			while (!Thread.interrupted()) {
				/* Gets the weather informations */
				JsonElement response = openWeatherClient.getWeather();

				/*
				 * Iterates over {@link openWeatherBindingConfigs} and gets the open weather
				 * property names
				 */
				LOGGER.info("OpenWeatherBindingConfigs size: {}", openWeatherBindingConfigs.size());

				for (Map.Entry<String, OpenWeatherBindingConfig> entry : openWeatherBindingConfigs.entrySet()) {
					LOGGER.info("Key : " + entry.getKey() + " Value : " + entry.getValue());

					/*
					 * Gets thing listener from the property binding configurations - the key is the
					 * href
					 */
					ThingChannelListener tcl = thingChannelListeners.get(entry.getKey());
					LOGGER.info("Content of thingChannelListeners: key {}, value {}", entry.getKey(), tcl.toString());

					/* Extracts the value with the given name from the OpenWeathermap response */

					JsonElement value = Tool.getValueByKey(entry.getValue().getName(), response);

					LOGGER.info("Key : " + entry.getKey() + ", runtime value : " + value);

					/* Creates an actual time stamp */
					long timeStamp = System.currentTimeMillis();

					/* Prepares the payload */

					String payload = String.format(payloadFormat, timeStamp,
//							tcl.getPropertyName().equals("temp") ? "temperature" : tcl.getPropertyName(), value);
							tcl.getPropertyName(), value);
					LOGGER.info("payload: {}", payload);

					/* Fires the new payload */
					tcl.doUpdate(payload);
				}
				/* Sleep for seconds */
				TimeUnit.SECONDS.sleep(pollInterval);
			}
		} catch (Throwable t) {
			LOGGER.error("", t);
		}
	}

	@Override
	public void close() throws IOException {
		LOGGER.info("Stops the thread");
		if (runner != null)
			runner.interrupt();
	}
}
