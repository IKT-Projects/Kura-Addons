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
package org.ict.kura.core.thing.creator.impl;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.Map;

public class ThingCreatorHrefOptions {
	/**
	 * The api key to get weather data
	 */
	private static final String BASE_HREF = "base.href";

	/** The properties as associated */
	private final Map<String, Object> properties;

	public ThingCreatorHrefOptions(Map<String, Object> properties) {
		requireNonNull(properties, "Properties cannot be null");
		this.properties = properties;
	}

	/**
	 * Returns the OpenWeatherDriver api key
	 * 
	 * @return the OpenWeatherDriver api key
	 */
	public String getBaseHref() {
		String key = "";
		final Object value = this.properties.get(BASE_HREF);
		if (nonNull(value) && value instanceof String) {
			key = (String) value;
		}
		return key;
	}

	@Override
	public String toString() {
		return "ThingCreatorOptions [BaseHref()=" + getBaseHref() + "]";
	}
	
	

}
