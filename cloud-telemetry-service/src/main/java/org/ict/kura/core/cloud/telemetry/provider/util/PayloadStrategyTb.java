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
package org.ict.kura.core.cloud.telemetry.provider.util;

import org.eclipse.kura.cloudconnection.message.KuraMessage;
import org.eclipse.kura.message.KuraPayload;
import org.osgi.service.event.Event;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * <pre>
 * This strategy implements the ThingsBoard MQTT 
 * telemetry upload API.
 * 
 * This convert strategy converts the Kura payload:
 * {
 * 		"time": 1483228800000,
 * 		"temperature": 20.1
 * } 
 * into the ThingsBoard payload:
 * {
 * 		"Device A":[
 * 			{
 * 				"ts": 1483228800000,
 * 				"values": {
 * 					"temperature": 20.1,
 * 				}
 * 			}
 * 		]
 * }
 * 
 * Topic (publish) : v1/gateway/telemetry
 * </pre>
 * 
 * @author MK
 * @version 2023-06-23
 * @see https://thingsboard.io/docs/reference/gateway-mqtt-api/
 */
public class PayloadStrategyTb implements PayloadStrategyIf {

	public KuraMessage convertMessage(Event event) {
		JsonObject root = new JsonObject();
		JsonArray device = new JsonArray();
		JsonObject property = new JsonObject();

		JsonObject value = (JsonObject) event.getProperty("value");
		property.addProperty("ts", value.get("time").getAsLong());
		property.add("values", Util.getValuesFromJson(value));
		device.add(property);
		root.add((String) event.getProperty("thingName"), device);

		/* Create a {@link KuraPayload} */
		KuraPayload payload = new KuraPayload();

		payload.setBody(root.toString().getBytes());

		/* Creates the {@link KuraMessage} */
		return new KuraMessage(payload);
	}
}
