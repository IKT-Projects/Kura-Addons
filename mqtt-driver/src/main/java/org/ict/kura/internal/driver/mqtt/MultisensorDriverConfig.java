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
package org.ict.kura.internal.driver.mqtt;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * The multisensor configuration
 * 
 * With this annotations the metatype xml file in the folder OSGI-INF is
 * created, which is used to build a configuration page in the kura web ui. This
 * allows the bundle to be configured via the UI.
 * 
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2020-11-23
 */
//@formatter:off
@ObjectClassDefinition(
		id = "org.ict.kura.driver.multisensor",
		name = "MultisensorDriver",
		description = "Configuration parameters | Multisensor Driver",
		localization = "en_us")

@interface MultisensorDriverConfig {
	@AttributeDefinition(
			name = "Enables the MDNS service",
			type = AttributeType.BOOLEAN,
			required = true,
			defaultValue = "true",
			description = "Avtivate / deactivate the MDNS service")
	String mdns_enable();

	@AttributeDefinition(
			name = "MDNS service type",
			type = AttributeType.STRING,
			required = true,
			defaultValue = "_tcp.local.",
			description = "The MDNS discover service type, e.g. _tcp.local.")
	String mdns_service_type();

	@AttributeDefinition(
			name = "MDNS service name",
			type = AttributeType.STRING,
			required = true,
			defaultValue = "_multisensor.",
			description = "The MDNS discover service name, e.g. the multisenssor _multisensor., the room controller _instahubX or in general _wot.")
	String mdns_service_name();

	@AttributeDefinition(
			name = "Description topic (MQTT)",
			type = AttributeType.STRING,
			required = false,
			defaultValue = "",
			description = "An alternative to the dynamic search of thing descriptions using mdns. Here the topics can be configured directly. Use the following syntax: <topic1>;<topic2>;<topicN>")
	String description_mqtt_topic();

	@AttributeDefinition(
			name = "Removes all assets and channels",
			type = AttributeType.BOOLEAN,
			required = true,
			defaultValue = "false",
			description = "Use this flag first, if you want to remove the driver")
	String common_enable();

	@AttributeDefinition(
			name = "DataService Target Filter",
			type = AttributeType.STRING, required = true,
			cardinality = 0, 
			defaultValue = "(kura.service.pid=changeme)",
			description = "Specifies an OSGi target filter, the pid of the Data Service used to publish messages to the cloud platform.")
	String DataService_target();
//@formatter:on
}
