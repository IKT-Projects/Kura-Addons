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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

/**
 * This is the OpenWeather client implementation. We are using here the unirest
 * http client to connect the OpenWeather API.
 * 
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2020-10-22
 */
public class OpenWeatherClient {
	/* The open weather client logger instance */
	private static final Logger LOGGER = LoggerFactory.getLogger(OpenWeatherClient.class);

	/*
	 * REST call URL template: api.openweathermap.org/data/2.5/weather?q={city
	 * name},{state code}&appid={API key}
	 */
	private static final String URL_1 = "https://api.openweathermap.org/data/2.5/weather?q=%s,%s&units=metric&appid=%s";

	/* The API Key to register with the OpenWeather platform */
	private String apiKey;

	/* The city name to get weather data eg. Dortmund */
	private String cityName;

	/* The state code of city eg. de */
	private String stateCode;

	/**
	 * The {@link OpenWeatherClient} constructor
	 * 
	 * @param cityName  the name of the city for which weather data should be
	 *                  fetched.
	 * @param stateCode the state code of city name
	 * @param apiKey    the API key of user
	 */
	public OpenWeatherClient(String cityName, String stateCode, String apiKey) {
		this.cityName = cityName;
		this.stateCode = stateCode;
		this.apiKey = apiKey;
	}

	/**
	 * HTTP REST request/response to get the current weather data.
	 * 
	 * @return {@link JsonElement} contains the current weather data in json format
	 */
	public JsonElement getWeather() {
		/* Create uri with given informations */
		String uri = String.format(URL_1, this.cityName, this.stateCode, this.apiKey);
		LOGGER.info("Get OpenWeather: {}",uri);

		/* Create a response object and execute the REST call */
		HttpResponse<JsonNode> response = Unirest.get(uri).header("Content-Type", "application/json").asJson();

		/* Get status code of response */
		LOGGER.info("Status code: '{}'", response.getStatus());
		if (response.getStatus() != 200)
			throw new NullPointerException("HTTP Error: " + response.getStatus());
		LOGGER.info(response.getBody().toString());

		/* Return response body formatted in {@link JsonElement} */
		return new JsonParser().parse(response.getBody().toString());
	}
}
