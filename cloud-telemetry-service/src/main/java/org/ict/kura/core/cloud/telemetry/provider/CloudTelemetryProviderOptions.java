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
package org.ict.kura.core.cloud.telemetry.provider;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class {@link CloudTelemetryProviderOptions} is responsible to provide all
 * the required configurable options for the
 * {@link CloudTelemetryProvider}.<br/>
 * <br/>
 *
 * @author ICT M. Biskup
 * @author ICT M. Kuller
 * @version 2023-05-25
 */
public class CloudTelemetryProviderOptions {
	/** The Logger instance. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(CloudTelemetryProviderOptions.class);

	// Enable or disable the service..
	private static final String COMMON_ENABLE = "common.enable";

	// Enable or disable the service..
	private static final String STRATEGY = "strategy";

	/** The properties as associated */
	private final Map<String, Object> properties;

	/**
	 * Instantiates a new InfluxDbServicePersistence options.
	 *
	 * @param properties the properties
	 * @throws NullPointerException if any of the arguments is null
	 */
	CloudTelemetryProviderOptions(final Map<String, Object> properties) {
		requireNonNull(properties, "Properties cannot be null");
		this.properties = properties;
	}

	/**
	 * Returns the enable flag
	 *
	 * @return the enable flag
	 */
	Boolean getCommonEnable() {
		Boolean enable = null;
		final Object flag = this.properties.get(COMMON_ENABLE);
		if (nonNull(flag) && flag instanceof Boolean) {
			enable = (Boolean) flag;
		}
		return enable;
	}

	/**
	 * Returns the convert strategy
	 *
	 * @return the convert strategy
	 */
	String getStrategy() {
		String startegy = null;
		final Object flag = this.properties.get(STRATEGY);
		if (nonNull(flag) && flag instanceof String) {
			startegy = (String) flag;
		}
		return startegy;
	}

	@Override
	public String toString() {
		return "CloudTelemetryProviderOptions [properties=" + properties + ", getCommonEnable()=" + getCommonEnable()
				+ ", getStrategy()=" + getStrategy() + "]";
	}
}
