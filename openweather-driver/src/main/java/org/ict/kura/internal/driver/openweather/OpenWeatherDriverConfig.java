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

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * The driver configuration.
 * 
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2023-01-13
 */

/*
 * The metatype xml file is created on the basis of these annotations, which is
 * used to create a configuration page in Kura Web Ui. This enables the
 * configuration of the bundle via the UI.
 */
@ObjectClassDefinition(id = "org.ict.kura.driver.openweather", name = "NAME Driver", description = "Name Driver", localization = "en_us")

@interface OpenWeatherDriverConfig {
	@AttributeDefinition(name = "Api-Key", type = AttributeType.STRING, required = true, defaultValue = "7df8c19b9799805abc69546513e471c7", description = "Api Key for OpenWeatherMap")
	String weather_client_apiKey();

	@AttributeDefinition(name = "City name", type = AttributeType.STRING, required = true, defaultValue = "Dortmund", description = "Name of the city for weather data")
	String weather_cityName();

	@AttributeDefinition(name = "State code", type = AttributeType.STRING, required = true, defaultValue = "de", description = "State cide e.g. 'de' for weather data")
	String weather_stateCode();

	@AttributeDefinition(name = "Poll interval", type = AttributeType.INTEGER, required = true, defaultValue = "60", description = "Pollinterval in s")
	String weather_client_pollinterval();

	@AttributeDefinition(name = "Thing folder", type = AttributeType.STRING, required = true, defaultValue = "", description = "Thing files with the sensor and actuator configurations")
	String thing_folder();
}
