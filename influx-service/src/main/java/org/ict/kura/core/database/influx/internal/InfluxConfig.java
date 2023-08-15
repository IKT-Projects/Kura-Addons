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
package org.ict.kura.core.database.influx.internal;



import org.osgi.service.metatype.annotations.*;

@ObjectClassDefinition(
		id = "org.ict.kura.core.database.influx.InfluxDbService",
		name = "InfluxDbService",
		description = "Configuration parameters | Influx DB",
		localization = "en_us",
		icon = @Icon(resource = "influx.png", size = 32))
@interface InfluxConfig {
	
	@AttributeDefinition(name = "Enable database connection",
			type = AttributeType.BOOLEAN,
			required = true,
			defaultValue = "false",
			description = "Enable Influx DB")
	String database_connection_enable();
	
	@AttributeDefinition(
            name = "Database address",
            type = AttributeType.STRING,
            required = true,
            defaultValue = "172.18.0.3",
            description = "Host Address for InfluxDb."
    )
    String database_connection_ip();
	
	@AttributeDefinition(
            name = "Database port",
            type = AttributeType.INTEGER,
            required = true,
            defaultValue = "8086",
            description = "Port Address for InfluxDb."
    )
    String database_connection_port();
	
	@AttributeDefinition(
            name = "Token",
            type = AttributeType.STRING,
            required = false,
            description = "Token."
    )
    String database_token();
	
	@AttributeDefinition(
            name = "Org",
            type = AttributeType.STRING,
            required = false,
            description = "Org."
    )
    String database_org();
	
	@AttributeDefinition(
            name = "Bucket",
            type = AttributeType.STRING,
            required = true,
            defaultValue = "myInfluxDB",
            description = "Bucket Name."
    )
    String database_bucket();
	
	@AttributeDefinition(
            name = "Database retention policy",
            type = AttributeType.STRING,
            required = true,
            defaultValue = "autogen",
            description = "Retention Policy."
    )
    String databse_retention_policy();


}

