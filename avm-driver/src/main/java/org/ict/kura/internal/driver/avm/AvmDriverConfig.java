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
package org.ict.kura.internal.driver.avm;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
		id = "org.ict.kura.driver.avm",
		name = "AvmDriver",
		description = "Configuration parameters | AVM Driver",
		localization = "en_us")
@interface AvmDriverConfig {
	
	@AttributeDefinition(
			name = "FritzBox Ip address", 
			type = AttributeType.STRING, 
			required = true, 
			defaultValue = "localhost", 
			description = "Host Address to FritzBox.")
	String fritzbox_ip();
	
	@AttributeDefinition(
			name = "Fritzbox Smart Home Username", 
			type = AttributeType.STRING, 
			required = true, 
			description = "Fritzbox Smart Home Username.")
	String fritzbox_username();

	@AttributeDefinition(
			name = "Fritzbox Smart Home Password", 
			type = AttributeType.PASSWORD, 
			required = true, 
			description = "Fritzbox Smart Home Password.")
	String fritzbox_password();
	
	@AttributeDefinition(
			name = "Poll interval", 
			type = AttributeType.INTEGER, 
			required = true, 
			defaultValue = "60", 
			description = "Pollinterval in s")
	String fritzbox_pollinterval();

}
