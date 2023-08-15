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
package org.ict.kura.core.persistence.influx.internal;

/*-
 * #%L
 * org.ict.kura.core.persistence.influx.provider
 * %%
 * Copyright (C) 2023 ICT - FH-Dortmund
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

//@formatter:off
@ObjectClassDefinition(
		id = "org.ict.kura.persistence.influx.InfluxDbServicePersistence",
		name = "InfluxDbServicePersistence",
		description = "Configuration parameters | Influx DB Persistence Service",
		localization = "en_us")

@interface InfluxDbServicePersistenceConfig {
	@AttributeDefinition(name = "Enable", 
			type = AttributeType.BOOLEAN, 
			required = true, 
			defaultValue = "true", 
			description = "Enables the service")
	String common_enable();
	
	@AttributeDefinition(name = "InfluxDB Target Filter", 
			type = AttributeType.STRING, 
			required = true, 
			cardinality = 0, 
			defaultValue = "(kura.service.pid=changeme)", 
			description = "Specifies, as an OSGi target filter, the pid of the InfluxDB Service used to publish messages to the cloud platform.")
	String InfluxDbService_target();

// @formatter:on
}
