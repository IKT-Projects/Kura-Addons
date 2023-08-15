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

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.Map;

import org.eclipse.kura.configuration.Password;
import org.eclipse.kura.crypto.CryptoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AvmDriverOptions {

	private static final Logger LOGGER = LoggerFactory.getLogger(AvmDriverOptions.class);

	/**
	 * The driver service pid
	 */
	private static final String KURA_SERVICE_PID = "kura.service.pid";

	/**
	 * The poll interval, how often fritzbox data is queried.
	 */
	private static final String FRITZBOX_POLLINTERVAL = "fritzbox.pollinterval";

	/**
	 * The IP adress of the fritz box
	 */
	private static final String FRITZBOX_IP = "fritzbox.ip";

	/**
	 * The username for the fritzbox smart home user
	 */
	private static final String FRITZBOX_USERNAME = "fritzbox.username";

	/**
	 * The password for the fritzbox smart home user
	 */
	private static final String FRITZBOX_PASSWORD = "fritzbox.password";

	/** The Crypto Service dependency. */
	private final CryptoService cryptoService;

	/** The properties as associated */
	private final Map<String, Object> properties;

	/**
	 * Constructor for the avm options
	 * 
	 * @param properties    the map properties
	 * @param cryptoService the crypto service for password encryption
	 */
	AvmDriverOptions(Map<String, Object> properties, CryptoService cryptoService) {
		requireNonNull(properties, "Properties cannot be null");
		requireNonNull(cryptoService, "Crypto Service cannot be null");

		this.properties = properties;
		this.cryptoService = cryptoService;
	}

	/**
	 * Returns the AVM ip adress
	 * 
	 * @return String fritzbox ip
	 */
	String getFritzboxIp() {
		String ip = "";
		final Object obj = this.properties.get(FRITZBOX_IP);
		if (nonNull(obj) && obj instanceof String) {
			ip = (String) obj;
		}
		return ip;
	}

	/**
	 * Returns the AVM username
	 * 
	 * @return String fritzbox username
	 */
	String getFritzboxUsername() {
		String username = "";
		final Object obj = this.properties.get(FRITZBOX_USERNAME);
		if (nonNull(obj) && obj instanceof String) {
			username = (String) obj;
		}
		return username;
	}

	/**
	 * Returns the AVM password
	 * 
	 * @return String fritzbox password
	 */
	String getFritzboxPassword() {
		String password = "";
		Password decryptedPassword;
		final Object obj = this.properties.get(FRITZBOX_PASSWORD);
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

	/**
	 * Returns the AVM poll interval
	 * 
	 * @return the AVM poll interval
	 */
	int getPollinterval() {
		int interval = 0;
		final Object number = this.properties.get(FRITZBOX_POLLINTERVAL);
		if (nonNull(number) && number instanceof Integer) {
			interval = (Integer) number;
		}
		return interval;
	}

	/**
	 * Returns the OpenWeaterDriver service pid
	 * 
	 * @return the OpenWeaterDriver service pid
	 */
	String getDriverServicePID() {
		String pid = "";
		final Object value = this.properties.get(KURA_SERVICE_PID);
		if (nonNull(value) && value instanceof String) {
			pid = (String) value;
		}
		return pid;
	}

	public String toString(boolean all) {
		return "AvmDriverOptions [getFritzboxIp()=" + getFritzboxIp() + ", getFritzboxUsername()="
				+ getFritzboxUsername() + ", getFritzboxPassword()=" + getFritzboxPassword() + ", getPollinterval()="
				+ getPollinterval() + ", getDriverServicePID()=" + getDriverServicePID() + "]";
	}

	@Override
	public String toString() {
		return "AvmDriverOptions [properties=" + properties + "]";
	}

}
