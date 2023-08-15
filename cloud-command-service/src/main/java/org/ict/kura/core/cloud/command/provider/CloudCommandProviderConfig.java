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
package org.ict.kura.core.cloud.command.provider;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

@ObjectClassDefinition(id = "org.ict.kura.core.cloud.command.provider.CloudCommandProvider", name = "CloudCommandProvider", description = "Configuration parameters | Cloud Command Service", localization = "en_us")

@interface CloudCommandProviderConfig {

	@AttributeDefinition(name = "Subscriber Target Filter", type = AttributeType.STRING, required = true, cardinality = 0, defaultValue = "(kura.service.pid=changeme)", description = "The pid of the Subscriber Service used to receive command messages from the cloud platform.")
	String CloudSubscriber_target();

	@AttributeDefinition(name = "Publisher Target Filter",
			type = AttributeType.STRING,
			required = true,
			cardinality = 0,
			defaultValue = "(kura.service.pid=changeme)",
			description = "The pid of the Data Service used to publish command response messages to the cloud platform.")
	String CloudPublisher_target();

	@AttributeDefinition(name = "The RPC convert strategy", type = AttributeType.STRING, required = true, cardinality = 0, options = {
			@Option(label = "ThingsBoard", value = "thingsBoard"),
			@Option(label = "WoT", value = "wot") }, defaultValue = "wot", description = "Two RPC strategies are supported: WoT payload and Thingsboard")
	String strategy();
}
