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
package org.ict.kura.internal.driver.mqtt;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class supports the configuration parameters, which can be defined by the
 * kura web admin ui. The layout with the default parameters are defined in the
 * class {@link MultisensorDriverConfig}
 * 
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2021-02-04
 */
public class MultisensorDriverOptions {
	/* The Logger instance. */
	@SuppressWarnings("unused")
	private static final Logger Logger = LoggerFactory.getLogger(MultisensorDriverOptions.class);
	/*
	 * The autoCreateConfig flag to create a configuration automatically.
	 */
	private static final String AUTO_CREATE_CONFIG = "common.enable";

	private static final String MDNS_SERVICE_ENABLE = "mdns.enable";

	/* DNS service type e.g. for TCP (._tcp.local.) */
	private static final String MDNS_SERVICE_TYPE = "mdns.service.type";

	/* DNS symbolic service name (_multisensor) */
	private static final String MDNS_SERVICE_NAME = "mdns.service.name";

	private static final String DESCRIPTION_MQTT_TOPIC = "description.mqtt.topic";

	/* The driver service pid */
	private static final String KURA_SERVICE_PID = "kura.service.pid";

	/* The properties as associated */
	private final Map<String, Object> properties;

	/**
	 * Instantiates a new multisensor options.
	 *
	 * @param properties the properties
	 * @throws NullPointerException if any of the arguments is null
	 */
	MultisensorDriverOptions(final Map<String, Object> properties) {
		requireNonNull(properties, "Properties cannot be null");

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
	 * Returns the MDNS enable flag
	 *
	 * @return the flag
	 */
	Boolean getMdnsEnable() {
		Boolean value = null;
		final Object obj = this.properties.get(MDNS_SERVICE_ENABLE);
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
	 * Returns the description mqtt topics
	 *
	 * @return the description mqtt topics
	 */
	String getDescriptionMqttTopic() {
		String value = "";
		final Object obj = this.properties.get(DESCRIPTION_MQTT_TOPIC);
		if (nonNull(obj) && obj instanceof String) {
			value = (String) obj;
		}
		return value;
	}

	/**
	 * Returns the kura service pid
	 *
	 * @return the kura service pid
	 */
	String getDriverServicePID() {
		String value = "";
		final Object obj = this.properties.get(KURA_SERVICE_PID);
		if (nonNull(obj) && obj instanceof String) {
			value = (String) obj;
		}
		return value;
	}

	@Override
	public String toString() {
		return "MultisensorDriverOptions [properties=" + properties + ", getAutoCreateConfig()=" + getAutoCreateConfig()
				+ ", getMdnsServiceType()=" + getMdnsServiceType() + ", getMdnsServiceName()=" + getMdnsServiceName()
				+ ", getDriverServicePID()=" + getDriverServicePID() + "]";
	}
}