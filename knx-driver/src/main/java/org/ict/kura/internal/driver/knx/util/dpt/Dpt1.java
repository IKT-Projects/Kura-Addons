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
import org.ict.model.wot.dataschema.ObjectSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import tuwien.auto.calimero.KNXFormatException;
import tuwien.auto.calimero.dptxlator.DPTXlator;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;
import tuwien.auto.calimero.process.ProcessEvent;

public class Dpt1 extends Dpt {
	/* The logger instance */
	private static final Logger LOGGER = LoggerFactory.getLogger(Dpt1.class);

	public Dpt1(DPTXlatorBoolean dptXlatorBoolean) throws KNXFormatException {
		this.setDptXlator(dptXlatorBoolean);
		LOGGER.info("DPT {} created with XLator {}", this.getClass().getName(),
				this.getDptXlator().getType().toString());
	}

	@Override
	public String from(KnxBindingConfig knxBindingConfig, ThingChannelListener thingChannelListenerImpl,
			ProcessEvent processEvent) throws KNXFormatException {

		/* Translates the KNX value to a java value */
		this.getDptXlator().setData(processEvent.getASDU());

		/*
		 * Creates a JSON payload, JSON payload schema based on the
		 * PropertyAddordance schema !!!)
		 */
		JsonObject jsonObject = new JsonObject();
		/* Adds the new time stamp */
		jsonObject.addProperty("time", System.currentTimeMillis());
		/*
		 * Adds the new value property, no conversion necessary (require B1 Value, DPT
		 * 1.xxx), we are using here the string type of the DptXlator, this is the
		 * encoding.
		 */
		jsonObject.addProperty(thingChannelListenerImpl.getPropertyName(), this.getDptXlator().getValue());
		/* Returns the JSON object as String */
		return jsonObject.toString();
	}

	@Override
	public DPTXlator to(KnxBindingConfig knxBindingConfig, ActionAffordance actionAffordance, JsonObject jsonValue)
			throws KNXFormatException {
		/* Gets the input action schema */
		ObjectSchema os = (ObjectSchema) actionAffordance.getInput();

		/* We need the action input property names here ... */
		String[] actionInputPropertyNames = os.getPropertiesMap().keySet().toArray(new String[0]);

		/*
		 * Extracts the value from the JSON object - boolean has only one action input
		 * property name
		 */
		String value = jsonValue.get(actionInputPropertyNames[0]).getAsString();
		/* Sets the value */
		this.getDptXlator().setValue(value);

		return this.getDptXlator();
	}

	@Override
	public String toString() {
		return "Dpt1 [getDptXlator()=" + getDptXlator() + "]";
	}
}
