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
package org.ict.kura.core.database.mongo.internal;

import org.osgi.service.metatype.annotations.*;

@ObjectClassDefinition(
		id = "org.ict.kura.core.database.mongo.MongoDbService", 
		name = "MongoDbService", 
		description = "Configuration parameters | Mongo DB",
		localization = "en_us",
		icon = @Icon(resource = "mongo.png", size = 32))
	

@interface MongoConfig {
	@AttributeDefinition(
			name = "Enable database connection", 
			type = AttributeType.BOOLEAN, 
			required = true, 
			defaultValue = "false", 
			description = "Enable Influx DB")
	String database_connection_enable();

	@AttributeDefinition(
			name = "Database address", 
			type = AttributeType.STRING, 
			required = true, 
			defaultValue = "localhost", 
			description = "Host Address for InfluxDb.")
	String database_connection_ip();

	@AttributeDefinition(
			name = "Database port", 
			type = AttributeType.INTEGER, 
			required = true, 
			defaultValue = "27017", 
			description = "Port Address for InfluxDb.")
	String database_connection_port();

	@AttributeDefinition(
			name = "Username", 
			type = AttributeType.STRING, 
			required = false, 
			description = "Username.")
	String database_username();

	@AttributeDefinition(
			name = "Password", 
			type = AttributeType.STRING, 
			required = false, 
			description = "Password.")
	String database_password();

	@AttributeDefinition(
			name = "Database name", 
			type = AttributeType.STRING, 
			required = true, 
			defaultValue = "myMongoDB", 
			description = "Database Name.")
	String database_name();

}
