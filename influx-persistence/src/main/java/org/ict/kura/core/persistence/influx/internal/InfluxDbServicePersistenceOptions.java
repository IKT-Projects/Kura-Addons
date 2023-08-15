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

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class {@link InfluxDbServicePersistenceOptions} is responsible to provide
 * all the required configurable options for the
 * {@link InfluxDbServicePersistence}.<br/>
 * <br/>
 *
 * The different properties to configure a InfluxDbServicePersistence are as
 * follows:
 * <ul>
 * <li>store</li>
 * </ul>
 * 
 * @author ICT M. Biskup
 * @author ICT M. Kuller
 * @version 2020-10-14
 */
public class InfluxDbServicePersistenceOptions {
	/** The Logger instance. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(InfluxDbServicePersistenceOptions.class);
	/**
	 * The memory flag to write channel events into the influx database.
	 */
	private static final String COMMON_ENABLE = "common.enable";


	/** The properties as associated */
	private final Map<String, Object> properties;

	/**
	 * Instantiates a new InfluxDbServicePersistence options.
	 *
	 * @param properties the properties
	 * @throws NullPointerException if any of the arguments is null
	 */
	InfluxDbServicePersistenceOptions(final Map<String, Object> properties) {
		requireNonNull(properties, "Properties cannot be null");

		this.properties = properties;
	}

	/**
	 * Returns the InfluxDbServicePersistence enable flag
	 *
	 * @return the InfluxDbServicePersistence enable flag
	 */
	Boolean getCommonEnable() {
		Boolean enable = null;
		final Object flag = this.properties.get(COMMON_ENABLE);
		if (nonNull(flag) && flag instanceof Boolean) {
			enable = (Boolean) flag;
		}
		return enable;
	}


	@Override
	public String toString() {
		return "InfluxDbServicePersistenceOptions [properties=" + properties + ", getCommonEnable()="
				+ getCommonEnable() +  "]";
	}
}
