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
package org.ict.kura.internal.driver.knx.util.dpt;

import org.ict.kura.driver.thing.ThingChannelListener;
import org.ict.kura.internal.driver.knx.util.KnxBindingConfig;
import org.ict.model.wot.core.ActionAffordance;

import com.google.gson.JsonObject;

import tuwien.auto.calimero.KNXFormatException;
import tuwien.auto.calimero.dptxlator.DPTXlator;
import tuwien.auto.calimero.process.ProcessEvent;

public abstract class Dpt {

	/* The dpt translator */
	private DPTXlator dptXlator;

	/**
	 * Creates a JSON payload from the given KNX {@link ProcessEvent}.
	 * 
	 * @param knxBindingConfig         the KNX binding configuration with the given
	 *                                 DPT Id
	 * @param thingChannelListenerImpl the thing channel listener we need here to
	 *                                 get the channel name, corresponding with the
	 *                                 action name
	 * @param processEvent             the KNX {@link ProcessEvent} object
	 * @return the JSON payload contains the channel (action) name and value
	 * @throws KNXFormatException
	 */
	public abstract String from(KnxBindingConfig knxBindingConfig, ThingChannelListener thingChannelListenerImpl,
			ProcessEvent processEvent) throws KNXFormatException;

	/**
	 * Creates a KNX payload {@link DPTXlator} object with the given JSON payload.
	 * 
	 * @param knxBindingConfig the KNX binding configuration with the given DPT Id
	 * @param actionAffordance the action affordance we need here to get the action
	 *                         name we have to extract from the JSON value
	 * @param jsonValue        the JSON value contains the action name and value
	 * @return the DPT translator object
	 * @throws KNXFormatException
	 */
	public abstract DPTXlator to(KnxBindingConfig knxBindingConfig, ActionAffordance actionAffordance,
			JsonObject jsonValue) throws KNXFormatException;

	public DPTXlator getDptXlator() {
		return dptXlator;
	}

	public void setDptXlator(DPTXlator dptXlator) {
		this.dptXlator = dptXlator;
	}
}
