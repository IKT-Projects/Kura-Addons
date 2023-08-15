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
package org.ict.kura.core.cloud.command.provider.util;

import java.util.Arrays;
import java.util.Optional;

import org.eclipse.kura.KuraException;
import org.eclipse.kura.asset.AssetService;
import org.eclipse.kura.channel.Channel;
import org.eclipse.kura.channel.ChannelRecord;
import org.eclipse.kura.cloudconnection.message.KuraMessage;
import org.eclipse.kura.type.TypedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * This strategy implements the WoT MQTT server-side RPC API.
 * 
 * Receives a WoT payload from the MQTT broker and redirects the payload
 * directly to the corresponding driver-asset-channel without waiting for a
 * response.
 * 
 * <pre>
 * Received Request:
 * 
 * Payload example: {"onOff": true}
 * 
 * Topic (subscribe): things/# 
 * Topic (receive):   things/$thing-name/actions/$action-name
 * 					  $thing-name is the asset	
 * 					  $action-name is the channel	
 * 
 * Send response:
 * 
 * Payload example: {"time": 1690356551064, "onOff": true}
 * 
 * Topic (publish): things/$thing-name/properties/$property-name
 * </pre>
 * 
 * @author MK
 * @version 2023-07-26
 */
public class CommandStrategyWotOneWay implements CommandStrategyIf {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommandStrategyWotOneWay.class);
	
	private JsonParser parser = new JsonParser();
	

	private static Gson gson = new Gson();
	

	public void convertAndRedirectMessage(KuraMessage message, AssetService assetService) throws KuraException {
		LOGGER.info("Command strategy WoT (one way) converts and redirects the given message {}", message.toString());

		String topic = (String) message.getProperties().get("topic");
		byte[] payload = message.getPayload().getBody();
		LOGGER.info("Message arrived on topic:{} with body:{}", topic, new String(payload));

		String thingName = topic.substring(topic.lastIndexOf("things/"), topic.indexOf("/actions"));
		thingName = thingName.substring(thingName.indexOf("/") + 1);
		String actionName = topic.substring(topic.indexOf("actions"));
		actionName = actionName.substring(actionName.indexOf("/") + 1);
		LOGGER.info("ThingName:{} ActionName:{}", thingName, actionName);

		
		JsonObject valueObj = parser.parse(new String(payload)).getAsJsonObject();
	
		String value = gson.toJson(valueObj);

		Channel channel = assetService.getAsset(thingName).getAssetConfiguration().getAssetChannels().get(actionName);
		Optional<TypedValue<?>> typedValue = Util.getTypedValue(channel.getValueType(), value);
		ChannelRecord write = channel.createWriteRecord(typedValue.get());

		assetService.getAsset(thingName).write(Arrays.asList(write));
	}
}
