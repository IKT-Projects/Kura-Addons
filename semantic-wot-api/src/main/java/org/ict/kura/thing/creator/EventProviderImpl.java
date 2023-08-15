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
package org.ict.kura.thing.creator;

import static org.ict.kura.asset.creator.thing.util.PropertyData.time;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;

import org.ict.kura.asset.creator.thing.util.IoTSchemaUris;
import org.ict.model.wot.constant.Op;
import org.ict.model.wot.core.EventAffordance;
import org.ict.model.wot.dataschema.BooleanSchema;
import org.ict.model.wot.dataschema.DataSchema;
import org.ict.model.wot.dataschema.ObjectSchema;
import org.ict.model.wot.hypermedia.Form;

public enum EventProviderImpl implements CreateableEvent {
	CO2Alert() {
		@Override
		public EventAffordance create(String baseHref, String id) throws Exception {
			var time = time();
			var data = BooleanSchema.builder().title("C02Alert")
					.description("CO2 alert. true = CO2 concentration to high, False = CO2 concentration okay")
					.atType(Arrays.asList(IoTSchemaUris.CARBONDIOXIDECONCENTRATIONDATA)).modified(time).build();
			var map = new HashMap<String, DataSchema>();
			map.put("time", time);
			map.put("co2-alert", data);
			var objSchema = ObjectSchema.builder().title("C02Alert")
					.description("CO2 alert schema containing time of event and alert state(boolean)")
					.atType(Arrays.asList(IoTSchemaUris.CARBONDIOXIDECONCENTRATIONDATA)).properties(map)
					.required(Arrays.asList("time", "co2-alert")).readOnly(true).build();
			var event = EventAffordance.builder().title("C02 alert")
					.description("CO2 alert triggered when CO2 concentration is to high.")
					.atType(Arrays.asList(IoTSchemaUris.CARBONDIOXIDECONCENTRATION)).data(objSchema)
					.forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
							.href(URI.create(baseHref + "/events/" + id)).contentType("application/json").build()))
					.build();
			event.setName("co2-alert");
			return event;
		}
	},
	HighTemperatureAlert() {
		@Override
		public EventAffordance create(String baseHref, String id) throws Exception {
			var time = time();
			var data = BooleanSchema.builder().title("HighTemperatureAlert")
					.description("High temperature alert. true = temperature to high, False = temperature okay")
					.atType(Arrays.asList(IoTSchemaUris.TEMPERATUREDATA)).modified(time).build();
			var map = new HashMap<String, DataSchema>();
			map.put("time", time);
			map.put("temperature-high-alert", data);
			var objSchema = ObjectSchema.builder().title("HighTemperatureAlert")
					.description("Temperature alert schema containing time of event and alert state(boolean)")
					.atType(Arrays.asList(IoTSchemaUris.TEMPERATUREDATA)).properties(map)
					.required(Arrays.asList("time", "temperature-high-alert")).readOnly(true).build();
			var event = EventAffordance.builder().title("Temperature alert")
					.description("Temperature alert triggered when temperature is to high.")
					.atType(Arrays.asList(IoTSchemaUris.TEMPERATURE)).data(objSchema)
					.forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
							.href(URI.create(baseHref + "/events/" + id)).contentType("application/json").build()))
					.build();
			event.setName("temperature-high-alert");
			return event;
		}
	},
	LowTemperatureAlert() {
		@Override
		public EventAffordance create(String baseHref, String id) throws Exception {
			var time = time();
			var data = BooleanSchema.builder().title("LowTemperatureAlert")
					.description("Low temperature alert. true = temperature to low, False = temperature okay")
					.atType(Arrays.asList(IoTSchemaUris.TEMPERATUREDATA)).modified(time).build();
			var map = new HashMap<String, DataSchema>();
			map.put("time", time);
			map.put("temperature-low-alert", data);
			var objSchema = ObjectSchema.builder().title("LowTemperatureAlert")
					.description("Temperature alert schema containing time of event and alert state(boolean)")
					.atType(Arrays.asList(IoTSchemaUris.TEMPERATUREDATA)).properties(map)
					.required(Arrays.asList("time", "temperature-low-alert")).readOnly(true).build();
			var event = EventAffordance.builder().title("Temperature alert")
					.description("Temperature alert triggered when temperature is to low.")
					.atType(Arrays.asList(IoTSchemaUris.TEMPERATURE)).data(objSchema)
					.forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
							.href(URI.create(baseHref + "/events/" + id)).contentType("application/json").build()))
					.build();
			event.setName("temperature-low-alert");
			return event;
		}
	};
}
