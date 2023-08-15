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
package org.ict.kura.util;

/**
 * This class contains useful constants.
 * 
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2021-05-31
 */

public enum Constants {
	/*
	 * The {@link EventAdmin} base topic name. All drivers uses this base name to
	 * publish new property values (telemetry) via the {@link EventAdmin}.
	 */
	EVENT_ADMIN_BASE_TOPIC_NAME("telemetry/things/"),
	/*
	 * The {@link EventAdmin} property tag topic name. All drivers uses this
	 * property tag name to publish new property values (telemetry) via the {@link
	 * EventAdmin}.
	 */
	EVENT_ADMIN_PROPERTY_TAG_TOPIC_NAME("/properties/");

	private final String value;

	private Constants(String v) {
		this.value = v;
	}

	public String value() {
		return this.value;
	}

	public static Constants fromValue(String v) {
		for (Constants c : Constants.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
}
