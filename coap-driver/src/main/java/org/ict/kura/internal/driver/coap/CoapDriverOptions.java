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
package org.ict.kura.internal.driver.coap;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.Map;

import org.ict.kura.internal.driver.coap.client.CustomCoapClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class {@link CoapDriverOptions} is responsible to provide all the
 * required configurable options for the {@link CoapDriver}.<br/>
 * <br/>
 * 
 * The different properties to configure a CoapDriver are as follows:
 * <ul>
 * <li>connectionIp</li>
 * <li>connectionPort</li>
 * <li>connectionTimeOut</li>
 * <li>pid</li>
 * </ul>
 * 
 * @author IKT B. Helgers
 * @author IKT M. Biskup
 */
public class CoapDriverOptions {

	/** The Logger instance. */
	private static final Logger LOGGER = LoggerFactory.getLogger(CoapDriverOptions.class);

	/**
	 * The autoCreateConfig flag to create a configuration automatically.
	 */
	private static final String AUTO_CREATE_CONFIG = "common.enable";

	/** DNS service type e.g. for TCP (._tcp.local.) */
	private static final String MDNS_SERVICE_TYPE = "mdns.service.type";

	/** DNS symbolic service name (_multisensor) */
	private static final String MDNS_SERVICE_NAME = "mdns.service.name";

	/** The driver service pid */
	private static final String KURA_SERVICE_PID = "kura.service.pid";

	/** The stored key for the connection server address, ip and port */
	private static final String CONNECTION_SERVER_ADDRESSES = "coap.connection.server.addresses";

	/** The stored key for the connection timeOut */
	private static final String CONNECTION_TIMEOUT = "coap.connection.timeOut";

	/** The properties as associated */
	private final Map<String, Object> properties;

	/**
	 * Instantiates a new OPCUA options.
	 * 
	 * @param properties the properties
	 * @throws NullPointerException if any of the arguments is null
	 */
	CoapDriverOptions(final Map<String, Object> properties) {
		requireNonNull(properties, "Properties can not be null");

		this.properties = properties;
	}

	/**
	 * Returns the autoCreateConfig flag
	 *
	 * @return the flag
	 */
	Boolean getAutoCreateConfig() {
		Boolean value = null;
		final Object obj = this.properties.get(AUTO_CREATE_CONFIG);
		if (nonNull(obj) && obj instanceof Boolean) {
			value = (Boolean) obj;
		}
		return value;
	}

	/**
	 * Returns the MDNS service type
	 *
	 * @return the MDNS service type
	 */
	String getMdnsServiceType() {
		String value = "";
		final Object obj = this.properties.get(MDNS_SERVICE_TYPE);
		if (nonNull(obj) && obj instanceof String) {
			value = (String) obj;
		}
		return value;
	}

	/**
	 * Returns the MDNS service name
	 *
	 * @return the MDNS service name
	 */
	String getMdnsServiceName() {
		String value = "";
		final Object obj = this.properties.get(MDNS_SERVICE_NAME);
		if (nonNull(obj) && obj instanceof String) {
			value = (String) obj;
		}
		return value;
	}

	/**
	 * Returns the CoAP server address for {@link CustomCoapClient}
	 * 
	 * @return target address of format 'xxx.xxx.xxx.xxx:xxxxx' or null is possible
	 */
	String[] getConnectionServerAddresses() {
		String[] connectionServerAddress = null;
		final Object address = this.properties.get(CONNECTION_SERVER_ADDRESSES);
		if (address instanceof String) {
			String tmp = (String) address;
			if (tmp.length() == 0)
				return connectionServerAddress;
			connectionServerAddress = tmp.split(";");
			LOGGER.info("CoAP server addresses: " + tmp);
		}
		return connectionServerAddress;
	}

	/**
	 * Returns communication timeOut used by instance of {@link CustomCoapClient}
	 * 
	 * @return timeOut duration in millis
	 */
	int getConnectionTimeOut() {
		int connectionTimeOut = 0;
		final Object timeOut = this.properties.get(CONNECTION_TIMEOUT);
		if (nonNull(timeOut) && timeOut instanceof Integer) {
			connectionTimeOut = (int) timeOut;
		}
		return connectionTimeOut;
	}

	/**
	 * Returns service pid of driver
	 * 
	 * @return service pid
	 */
	String getDriverServicePID() {
		String pid = "";
		final Object value = this.properties.get(KURA_SERVICE_PID);
		if (nonNull(value) && value instanceof String) {
			pid = (String) value;
		}
		return pid;
	}

}