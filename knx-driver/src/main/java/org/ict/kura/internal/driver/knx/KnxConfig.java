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
package org.ict.kura.internal.driver.knx;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * The KNX configuration.
 * 
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2021-02-17
 */
//@formatter:off 
@ObjectClassDefinition(
		id = "org.ict.kura.driver.knx",
		name = "KnxDriver",
		description = "Configuration parameters | KNX Driver",
		localization = "en_us")

@interface KnxConfig {
	@AttributeDefinition(
			name = "Thing folder",
			type = AttributeType.STRING,
			required = true,
			defaultValue = "<path to the folder>/<folder name>",
			description = "Thing files with the sensor and actuator configurations generated from the ets. Subfolders are also searched for wot descriptions.")
	String common_thing_folder();

	@AttributeDefinition(
			name = "KNX net IP gateway address",
			type = AttributeType.STRING,
			required = true,
			defaultValue = "localhost",
			description = "The IP address of the KNX net IP gateway")
	String network_gateway_ip();

	@AttributeDefinition(
			name = "KNX net IP gateway port",
			type = AttributeType.INTEGER,
			required = true,
			defaultValue = "3671",
			description = "The port of the KNX net IP gateway")
	String network_gateway_port();

	@AttributeDefinition(
			name = "KNX read scan delay",
			type = AttributeType.INTEGER,
			required = true,
			defaultValue = "5000",
			description = "The KNX read scan delay starts with delay in milliseconds and is started once in the initialization phase and after successful reconnect")
	String network_scan_delay();
	
	@AttributeDefinition(name = "KNX reconnect delay",
			type = AttributeType.LONG,
			required = true,
			cardinality = 0,
			defaultValue = "10000",
			description = "The KNX reconnect delay in milliseconds")
	String reconnect_timeout();

//@formatter:on
}
