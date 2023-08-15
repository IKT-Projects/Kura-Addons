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
package org.ict.kura.driver.knx;

import static tuwien.auto.calimero.dptxlator.DPTXlator2ByteFloat.DPT_TEMPERATURE;
import static tuwien.auto.calimero.dptxlator.DPTXlatorBoolean.DPT_SWITCH;

import java.util.Arrays;

import static tuwien.auto.calimero.dptxlator.DPTXlatorBoolean.DPT_BOOL;

import tuwien.auto.calimero.DataUnitBuilder;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.KNXException;
import tuwien.auto.calimero.KNXFormatException;
import tuwien.auto.calimero.datapoint.DatapointMap;
import tuwien.auto.calimero.datapoint.DatapointModel;
import tuwien.auto.calimero.datapoint.StateDP;
import tuwien.auto.calimero.dptxlator.DPTXlator;
import tuwien.auto.calimero.dptxlator.DPTXlator2ByteFloat;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;
import tuwien.auto.calimero.dptxlator.TranslatorTypes;

/**
 * Shows several ways of translating KNX datapoint type using Calimero DPT
 * translators.
 */
public class DptTranslation {
	public static void main(final String[] args) throws KNXException {
		// our knx data (DPT 9.001) we want to translate to a java temperature value
		final byte[] data = new byte[] { 0xc, (byte) 0xe2 };

		// Approach 1: manually create a DPT translator
		manualTranslation(data);

		// Approach 2: request DPT translator using factory method and DPT
		createUsingDpt(data);

		// Approach 3: use a datapoint model with a datapoint configuration
		useDatapointModel(data);

		final DPTXlator ts = new DPTXlatorBoolean(DPT_SWITCH);
		System.out.println("switch is " + ts.getValue() + " (" + ts.getNumericValue() + ") " + ts.toString());
		System.out.println("switch all values " + ts.getAllValues().length + " " + Arrays.toString(ts.getAllValues()));
		ts.setValue("on");
		System.out.println("switch all values " + ts.getAllValues().length + " " + ts.getItems() + " "
				+ Arrays.toString(ts.getAllValues()));
		System.out.println("switch low/upper value " + DPT_SWITCH.getLowerValue() + "" + DPT_SWITCH.getUpperValue());
		ts.getType().getUpperValue();

		final DPTXlator t = new DPTXlatorBoolean(DPT_BOOL);
		System.out.println("boolean is " + t.getValue() + " (" + t.getNumericValue() + ") " + t.toString());
	}

	private static void manualTranslation(final byte[] data) throws KNXFormatException {
		// DPT translator 9.001 for knx temperature datapoint
		final DPTXlator t = new DPTXlator2ByteFloat(DPT_TEMPERATURE);
		System.out
				.println("temperature all values " + t.getAllValues().length + " " + Arrays.toString(t.getAllValues()));

		// translate knx data to java value
		t.setData(data);
		final double temperature = t.getNumericValue();
		final String formatted = t.getValue();
		System.out.println("temperature is " + formatted + " (" + temperature + ")");

		// set temperature value of -4 degree celsius (physical unit can be omitted)
		t.setValue("-4 \u00b0C");
		// get KNX translated data
		System.out.println(t.getValue() + " translated to knx data: 0x" + DataUnitBuilder.toHex(t.getData(), ""));
	}

	private static void createUsingDpt(final byte[] data) throws KNXException, KNXFormatException {
		final DPTXlator t = TranslatorTypes.createTranslator(DPT_TEMPERATURE);
		t.setData(data);
		System.out.println("temperature is " + t.getValue() + " (" + t.getNumericValue() + ")");
	}

	private static void useDatapointModel(final byte[] data) throws KNXFormatException, KNXException {
		// we use a map of state-based datapoints
		final DatapointModel<StateDP> datapoints = new DatapointMap<>();
		// add the required datapoints
		final GroupAddress temperature = new GroupAddress("0/0/1");
		datapoints.add(new StateDP(temperature, "my temperature", 0, DPT_TEMPERATURE.getID()));

		// now we can create a translator for datapoints (if the requested translator is
		// supported by Calimero)
		if (datapoints.contains(temperature)) {
			final String dpt = datapoints.get(temperature).getDPT();
			final DPTXlator t = TranslatorTypes.createTranslator(0, dpt);
			t.setData(data);
			System.out.println("temperature is " + t.getValue() + " (" + t.getNumericValue() + ")");
		}
	}
}