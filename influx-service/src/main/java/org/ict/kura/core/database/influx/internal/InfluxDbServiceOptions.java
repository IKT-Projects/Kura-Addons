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

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.Map;

import org.eclipse.kura.crypto.CryptoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfluxDbServiceOptions {

	private static final String DATABASE_CONNECTION_ENABLE = "database.connection.enable";

	private static final String DATABASE_CONNECTION_IP = "database.connection.ip";

	private static final String DATABASE_CONNECTION_PORT = "database.connection.port";

	private static final String DATABASE_TOKEN = "database.token";
	private static final String DATABASE_ORG = "database.org";
	private static final String DATABASE_BUCKET = "database.bucket";
	private static final String DATABASE_RETENTION_POLICY = "databse.retention.policy";

	/** The Logger instance. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(InfluxDbServiceOptions.class);

	/** The Crypto Service dependency. */
	@SuppressWarnings("unused")
	private final CryptoService cryptoService;

	/** The properties as associated */
	private final Map<String, Object> properties;

	InfluxDbServiceOptions(Map<String, Object> properties, CryptoService cryptoService) {
		requireNonNull(properties, "Properties cannot be null");
		requireNonNull(cryptoService, "Crypto Service cannot be null");

		this.properties = properties;
		this.cryptoService = cryptoService;
	}

	boolean getEnableDatabaseConnection() {
		boolean enable = false;
		final Object flag = this.properties.get(DATABASE_CONNECTION_ENABLE);
		if (nonNull(flag) && flag instanceof Boolean) {
			enable = (Boolean) flag;
		}
		return enable;
	}

	String getDataBaseConnectionIp() {
		String ip = "";
		final Object obj = this.properties.get(DATABASE_CONNECTION_IP);
		if (nonNull(obj) && obj instanceof String) {
			ip = (String) obj;
		}
		return ip;
	}

	int getDataBaseConnectionPort() {
		int port = 0;
		final Object obj = this.properties.get(DATABASE_CONNECTION_PORT);
		if (nonNull(obj) && obj instanceof Integer) {
			port = (Integer) obj;
		}
		return port;
	}

	char[] getDatabaseToken() {
		String username = "";
		final Object obj = this.properties.get(DATABASE_TOKEN);
		if (nonNull(obj) && obj instanceof String) {
			username = (String) obj;
		}
		return username.toCharArray();
	}

	String getDatabaseOrg() {
		String name = "";
		final Object obj = this.properties.get(DATABASE_ORG);
		if (nonNull(obj) && obj instanceof String) {
			name = (String) obj;
		}
		return name;
	}

	String getDatabaseBucket() {
		String name = "";
		final Object obj = this.properties.get(DATABASE_BUCKET);
		if (nonNull(obj) && obj instanceof String) {
			name = (String) obj;
		}
		return name;
	}

	String getRetentionPolicy() {
		String policy = "";
		final Object obj = this.properties.get(DATABASE_RETENTION_POLICY);
		if (nonNull(obj) && obj instanceof String) {
			policy = (String) obj;
		}
		return policy;
	}



}
