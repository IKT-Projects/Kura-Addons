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
package org.ict.kura.internal.driver.knx;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.Map;

/**
 * The KNX configuration options.
 * 
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2020-12-17
 */
public class KnxDriverOptions {

	/* The folder of the thing configuration files */
	private final static String THING_FOLDER_LOCATION = "common.thing.folder";

	/* The IP address of the KNX Gateway */
	private final static String NETWORK_GATEWAY_IP = "network.gateway.ip";

	/* The port of the KNX Gateway */
	private final static String NETWORK_GATEWAY_PORT = "network.gateway.port";

	/* The KNX read scan delay in milliseconds */
	private final static String NETWORK_SCAN_DELAY = "network.scan.delay";

	// Reconnect timeout
	private static final String RECONNECT_TIMEOUT = "reconnect.timeout";

	/*
	 * The driver service pid
	 */
	private static final String KURA_SERVICE_PID = "kura.service.pid";

	/* The properties as associated */
	private final Map<String, Object> properties;

	/**
	 * Instantiates a new OPCUA options.
	 *
	 * @param properties the properties
	 * @throws NullPointerException if any of the arguments is null
	 */
	KnxDriverOptions(final Map<String, Object> properties) {
		requireNonNull(properties, "Properties cannot be null");

		this.properties = properties;
	}

	/**
	 * Returns the thing folder path
	 *
	 * @return the thing folder path
	 */
	String getThingFolderLocation() {
		String value = "";
		final Object obj = this.properties.get(THING_FOLDER_LOCATION);
		if (nonNull(obj) && obj instanceof String) {
			value = (String) obj;
		}
		return value;
	}

	/**
	 * Returns the KNX net IP address
	 *
	 * @return the KNX net IP address
	 */
	String getGatewayIp() {
		String value = "";
		final Object obj = this.properties.get(NETWORK_GATEWAY_IP);
		if (nonNull(obj) && obj instanceof String) {
			value = (String) obj;
		}
		return value;
	}

	/**
	 * Returns the KNX net port
	 *
	 * @return the KNX net port
	 */
	Integer getGatewayPort() {
		Integer value = 0;
		final Object obj = this.properties.get(NETWORK_GATEWAY_PORT);
		if (nonNull(obj) && obj instanceof Integer) {
			value = (Integer) obj;
		}
		return value;
	}

	/**
	 * Returns the KNX net scan delay
	 *
	 * @return the KNX net scan delay
	 */
	Integer getScanDelay() {
		Integer value = 0;
		final Object obj = this.properties.get(NETWORK_SCAN_DELAY);
		if (nonNull(obj) && obj instanceof Integer) {
			value = (Integer) obj;
		}
		return value;
	}

	/**
	 * Returns the kura service pid
	 *
	 * @return the pid
	 */
	String getDriverServicePID() {
		String value = "";
		final Object obj = this.properties.get(KURA_SERVICE_PID);
		if (nonNull(obj) && obj instanceof String) {
			value = (String) obj;
		}
		return value;
	}

	/**
	 * Returns the reconnect timeout
	 *
	 * @return the reconnect timeout
	 */
	Long getReconnectTimeout() {
		Long timeout = null;
		final Object flag = this.properties.get(RECONNECT_TIMEOUT);
		if (nonNull(flag) && flag instanceof Long) {
			timeout = (Long) flag;
		}
		return timeout;
	}

	@Override
	public String toString() {
		return "KnxDriverOptions [properties=" + properties + ", getThingFolderLocation()=" + getThingFolderLocation()
				+ ", getGatewayIp()=" + getGatewayIp() + ", getGatewayPort()=" + getGatewayPort() + ", getScanDelay()="
				+ getScanDelay() + ", getDriverServicePID()=" + getDriverServicePID() + ", getReconnectTimeout()="
				+ getReconnectTimeout() + "]";
	}
}