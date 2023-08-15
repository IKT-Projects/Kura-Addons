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
package org.ict.kura.core.cloud.telemetry.provider;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

//@formatter:off
@ObjectClassDefinition(
		id = "org.ict.kura.core.cloud.telemetry.provider.CloudTelemetryProvider",
		name = "CloudServicePersistence",
		description = "Configuration parameters | Cloud Telemetry Service",
		localization = "en_us")

@interface CloudTelemetryProviderConfig {
	@AttributeDefinition(name = "Enable", 
			type = AttributeType.BOOLEAN, 
			required = true, 
			defaultValue = "true", 
			description = "Enables the service")
	String common_enable();
	
	@AttributeDefinition(name = "Publisher Target Filter",
			type = AttributeType.STRING,
			required = true,
			cardinality = 0,
			defaultValue = "(kura.service.pid=changeme)",
			description = "The pid of the Data Service used to publish telemetry messages to the cloud platform.")
	String CloudPublisher_target();
	
	@AttributeDefinition(name = "The convert strategy",
			type = AttributeType.STRING,
			required = true,
			cardinality = 0,
			options = {@Option(label = "ThingsBoard", value = "thingsBoard"), 
					   @Option(label = "WoT", value = "wot")},
			defaultValue = "wot",
			description = "Two strategies are supported: WoT payload ({'time': 1001, \" + \"'temperature': 20.1}) and Thingsboard payload ({\"Device A\":[{\"ts\":1001,\"values\":{\"temperature\":20.1}}]})")
	String strategy();
// @formatter:on
}
