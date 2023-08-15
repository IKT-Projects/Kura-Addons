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
package org.ict.kura.core.cloud.keepalived.provider.util;

import org.eclipse.kura.cloudconnection.message.KuraMessage;
import org.eclipse.kura.message.KuraPayload;

import com.google.gson.JsonObject;

/**
 * <pre>
 * This strategy implements the ThingsBoard MQTT 
 * keepalived message.
 * 
 * Message: {"device":"AssetPid A"}
 * 
 * Topic (publish) : v1/gateway/connect
 * </pre>
 * 
 * @author MK
 * @version 2023-06-29
 * @see https://thingsboard.io/docs/reference/gateway-mqtt-api/
 */
public class PayloadStrategyTb implements PayloadStrategyIf {

	public KuraMessage createMessage(String asset) {
		JsonObject message = new JsonObject();
		message.addProperty("device", asset);

		/* Create a {@link KuraPayload} */
		KuraPayload payload = new KuraPayload();

		payload.setBody(message.toString().getBytes());

		/* Creates the {@link KuraMessage} */
		return new KuraMessage(payload);
	}
}
