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
package org.ict.kura.internal.driver.avm.things.util;

import static org.ict.kura.internal.driver.avm.things.util.PropertyData.onOff;
import static org.ict.kura.internal.driver.avm.things.util.PropertyData.time;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.temperaturePropertyObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.ict.model.wot.constant.Op;
import org.ict.model.wot.core.ActionAffordance;
import org.ict.model.wot.core.PropertyAffordance;
import org.ict.model.wot.dataschema.ArraySchema;
import org.ict.model.wot.dataschema.DataSchema;
import org.ict.model.wot.dataschema.IntegerSchema;
import org.ict.model.wot.dataschema.ObjectSchema;
import org.ict.model.wot.hypermedia.Form;

public class WoTUtils {

	public static PropertyAffordance createTemperaturPropertie(String href, String propertyId) throws MalformedURLException {
		PropertyAffordance temperature = PropertyAffordance.builder()
				.ds(temperaturePropertyObject("temperature"))
				.forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
						.href(URI.create(href + "/properties/" + propertyId)).contentType("application/json")
						.subprotocol("https").build()))
				.build();
		temperature.setAtType(Arrays.asList(URI.create("iot:Temperature"), URI.create("iot:AirTemperature")));
		return temperature;
	}

	public static PropertyAffordance createTemperaturHistoriePropertie(String href, String propertyId) throws MalformedURLException {
		Map<String, DataSchema> uriVariables = new HashMap<String, DataSchema>();
		IntegerSchema start = IntegerSchema.builder().description("The Start Time").build();
		IntegerSchema stop = IntegerSchema.builder().description("The Start Time").build();
		uriVariables.put("start", start);
		uriVariables.put("stop", stop);
		
		 PropertyAffordance temperature2 = PropertyAffordance.builder().ds(temperaturePropertyObject("temperature"))

				 

	                .build();
	        ArraySchema tempArray = ArraySchema.builder().items(Arrays.asList(temperature2)).maxItems(100000l).build();

	 

	        PropertyAffordance temperatureHistory = PropertyAffordance.builder().ds(tempArray).uriVariables(uriVariables)
	                .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
	                        .href(URI.create("https://iktsystems.goip.de:443/parameters/" + propertyId))
	                        .contentType("application/json").subprotocol("https").build()))
	                .build();
	        temperatureHistory.setAtType(Arrays.asList(URI.create("iot:Temperature"), URI.create("iot:AirTemperature")));
		
		return temperatureHistory;
	}

	public static ActionAffordance createSwitchAction(String href, String propertyId) {
		final DataSchema time = time();
		ActionAffordance action = ActionAffordance.builder().title("On/Off")
				.description("The state to be set (true/false)")
				.atType(Arrays.asList(URI.create("iot:TurnOff"), URI.create("iot:TurnOn")))
				.input(ObjectSchema.builder().title("On/Off")
						.atType(Arrays.asList(URI.create("iot:TurnOff"), URI.create("iot:TurnOn"))).writeOnly(true)
						.properties(new HashMap<String, DataSchema>() {
							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;

							{
								put("onOff", onOff(time));
							}
						}).build())
				.output(ObjectSchema.builder().title("On/Off").properties(new HashMap<String, DataSchema>() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					{
						put("onOff", onOff(time));
						put("time", time);
					}
				}).readOnly(true).build()).build();
		return action;
	}

}
