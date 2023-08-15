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
package $package;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleDriverOptions {
	/** The Logger instance. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleDriverOptions.class);

	/**
	 * The driver service pid
	 */
	private static final String KURA_SERVICE_PID = "kura.service.pid";
	
	private static final String EXAMPLE_VALUE = "example.value";

	/** The properties as associated */
	private final Map<String, Object> properties;

	public ExampleOptions(final Map<String, Object> properties) {
		requireNonNull(properties, "Properties cannot be null");
		this.properties = properties;
	}

	/**
	 * Returns the ExampleDriver service pid
	 * 
	 * @return the ExampleDriver service pid
	 */
	String getDriverServicePID() {
		String pid = "";
		final Object value = this.properties.get(KURA_SERVICE_PID);
		if (nonNull(value) && value instanceof String) {
			pid = (String) value;
		}
		return pid;
	}
	
	String getDriverServicePID() {
		String string = "";
		final Object value = this.properties.get(EXAMPLE_VALUE);
		if (nonNull(value) && value instanceof String) {
			string = (String) value;
		}
		return string;
	}

}
