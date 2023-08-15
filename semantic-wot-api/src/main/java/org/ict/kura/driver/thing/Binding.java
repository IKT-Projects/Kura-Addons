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

import java.util.Optional;

import org.ict.model.wot.core.ActionAffordance;

import com.google.gson.JsonObject;

/**
 * This is the binding interface, which have to implement by the technology. The
 * KURA framework calls this technology specific methods.
 * 
 * @author IKT M. Kuller
 * @author IKT M. Biskup
 * @version 2022-12-02
 */
public interface Binding<T> {

	// @formatter:off
	/**
	 * Reads the value from the technology and updates the new value in the Kura
	 * environment via the {@link ThingChannelListener#update}.
	 * 
	 * we need the JSON schema here: public void doRead(T
	 * technologyBindingConfiguration, String payloadSchema);
	 * 
	 * @param technologyBindingConfiguration the technology configuration
	 * @return the return value is optional, depending on the technology used. We
	 *         distinguish 3 cases: 
	 *         
	 *         1) Synchronous reading of the state value - the
	 *         read value is returned directly. 
	 *         
	 *         2) Asynchronous reading of the state
	 *         value - the read value is returned via an asynchronous channel,
	 *         cannot be returned directly in this method. 
	 *         
	 *         3) An active read is not possible, no value can be returned
	 */
	// @formatter:on
	public Optional<JsonObject> doRead(T technologyBindingConfiguration);

	/**
	 * Writes the value from KURA to the technology.
	 * 
	 * @param technologyBindingConfiguration the technology configuration
	 * @param actionAffordance               the {@link ActionAffordance} with
	 *                                       semantic informations about the action
	 *                                       and payload
	 * @param jsonValue                      the value in {@link JsonObject} format
	 */
	public void doWrite(T technologyBindingConfiguration, ActionAffordance actionAffordance, JsonObject jsonValue);
}
