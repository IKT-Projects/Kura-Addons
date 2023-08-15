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
package org.ict.kura.internal.driver.openweather;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenWeatherDriverOptions {
	/** The Logger instance. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(OpenWeatherDriver.class);

	/**
	 * The api key to get weather data
	 */
	private static final String WEATHER_CLIENT_APIKEY = "weather.client.apiKey";

	/**
	 * Name of the city from which weather data is requested
	 */
	private static final String WEATHER_CITY_NAME = "weather.cityName";

	/**
	 * Name of the state code from which weather data is requested
	 */
	private static final String WEATHER_STATE_CODE = "weather.stateCode";

	/**
	 * The poll interval, how often weather data is queried.
	 */
	private static final String WEATHER_CLIENT_POLLINTERVAL = "weather.client.pollinterval";

	/* The thing folder location */
	private final static String THING_FOLDER_LOC = "thing.folder";

	/**
	 * The driver service pid
	 */
	private static final String KURA_SERVICE_PID = "kura.service.pid";

	/** The properties as associated */
	private final Map<String, Object> properties;

	public OpenWeatherDriverOptions(final Map<String, Object> properties) {
		requireNonNull(properties, "Properties cannot be null");
		this.properties = properties;
	}

	/**
	 * Returns the OpenWeatherDriver api key
	 * 
	 * @return the OpenWeatherDriver api key
	 */
	String getApiKey() {
		String key = "";
		final Object value = this.properties.get(WEATHER_CLIENT_APIKEY);
		if (nonNull(value) && value instanceof String) {
			key = (String) value;
		}
		return key;
	}

	/**
	 * Returns the OpenWeatherDriver city name
	 * 
	 * @return the OpenWeatherDriver city name
	 */
	String getCityName() {
		String cityName = "";
		final Object value = this.properties.get(WEATHER_CITY_NAME);
		if (nonNull(value) && value instanceof String) {
			cityName = (String) value;
		}
		return cityName;
	}

	/**
	 * Returns the OpenWeatherDriver state code
	 * 
	 * @return the OpenWeatherDriver state code
	 */
	String getStateCode() {
		String stateCode = "";
		final Object value = this.properties.get(WEATHER_STATE_CODE);
		if (nonNull(value) && value instanceof String) {
			stateCode = (String) value;
		}
		return stateCode;
	}

	/**
	 * Returns the OpenWeatherDriver poll interval
	 * 
	 * @return the OpenWeatherDriver poll interval
	 */
	int getPollinterval() {
		int interval = 0;
		final Object number = this.properties.get(WEATHER_CLIENT_POLLINTERVAL);
		if (nonNull(number) && number instanceof Integer) {
			interval = (Integer) number;
		}
		return interval;
	}

	/**
	 * Returns the thing folder path
	 *
	 * @return the thing folder path
	 */
	String getThingFolderLocation() {
		String value = "";
		final Object obj = this.properties.get(THING_FOLDER_LOC);
		if (nonNull(obj) && obj instanceof String) {
			value = (String) obj;
		}
		return value;
	}

	/**
	 * Returns the OpenWeatherDriver service pid
	 * 
	 * @return the OpenWeatherDriver service pid
	 */
	String getDriverServicePID() {
		String pid = "";
		final Object value = this.properties.get(KURA_SERVICE_PID);
		if (nonNull(value) && value instanceof String) {
			pid = (String) value;
		}
		return pid;
	}

}
