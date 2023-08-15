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
package org.ict.kura.driver.thing;

import com.google.gson.JsonObject;

/**
 * This is the update interface, which have to implement by the
 * {@link ThingChannelListener}. This interface updates the state changes of
 * a sensor/actuator (technology) in the KURA environment.
 * 
 * @author IKT M. Kuller
 * @version 2020-03-18
 */
public interface Update {
	/**
	 * Updates the state changes of a sensor/actuator (technology) in the KURA
	 * environment.
	 * 
	 * @param jsonValue the value in {@link String} JSON format
	 */
	void doUpdate(String jsonValue);

	/**
	 * Updates the state changes of a sensor/actuator (technology) in the KURA
	 * environment.
	 * 
	 * @param jsonValue the value in {link JsonObject} format
	 */
	void doUpdate(JsonObject jsonValue);
}
