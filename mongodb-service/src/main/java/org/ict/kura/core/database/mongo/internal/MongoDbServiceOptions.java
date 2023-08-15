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

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.Map;

import org.eclipse.kura.configuration.Password;
import org.eclipse.kura.crypto.CryptoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoDbServiceOptions {

	private static final String DATABASE_CONNECTION_ENABLE = "database.connection.enable";

	private static final String DATABASE_CONNECTION_IP = "database.connection.ip";

	private static final String DATABASE_CONNECTION_PORT = "database.connection.port";

	private static final String DATABASE_USERNAME = "database.username";

	private static final String DATABASE_PASSWORD = "database.password";

	private static final String DATABASE_NAME = "database.name";

	/** The Logger instance. */
	private static final Logger LOGGER = LoggerFactory.getLogger(MongoDbServiceOptions.class);

	/** The Crypto Service dependency. */
	private final CryptoService cryptoService;

	/** The properties as associated */
	private final Map<String, Object> properties;

	MongoDbServiceOptions(Map<String, Object> properties, CryptoService cryptoService) {
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

	String getDatabaseUsername() {
		String username = "";
		final Object obj = this.properties.get(DATABASE_USERNAME);
		if (nonNull(obj) && obj instanceof String) {
			username = (String) obj;
		}
		return username;
	}

	String getDatabasePassword() {
		String password = "";
		Password decryptedPassword;
		final Object obj = this.properties.get(DATABASE_PASSWORD);
		if (nonNull(obj) && obj instanceof String) {
			try {
				decryptedPassword = new Password(this.cryptoService.decryptAes(obj.toString().toCharArray()));
				password = new String(decryptedPassword.getPassword());
			} catch (final Throwable e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		return password;
	}

	String getDatabaseName() {
		String name = "";
		final Object obj = this.properties.get(DATABASE_NAME);
		if (nonNull(obj) && obj instanceof String) {
			name = (String) obj;
		}
		return name;
	}

}
