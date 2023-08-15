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
import java.util.Map;
import java.util.Optional;

import org.eclipse.kura.KuraException;
import org.eclipse.kura.asset.AssetService;
import org.eclipse.kura.channel.Channel;
import org.eclipse.kura.channel.ChannelRecord;
import org.eclipse.kura.cloudconnection.message.KuraMessage;
import org.eclipse.kura.cloudconnection.publisher.CloudPublisher;
import org.eclipse.kura.message.KuraPayload;
import org.eclipse.kura.type.TypedValue;
import org.ict.kura.core.cloud.command.provider.rpc.tb.request.Request;
import org.ict.kura.core.cloud.command.provider.rpc.tb.response.Data;
import org.ict.kura.core.cloud.command.provider.rpc.tb.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * This strategy implements the ThingsBoard MQTT server-side RPC API.
 * 
 * Receives a payload from the ThingsBoard MQTT broker, redirects the payload
 * directly to the driver/asset/channel and sends a response back to the MQTT
 * broker. That the driver processes the command correctly is not guaranteed!
 * 
 * <pre>
 * Received Request:
 * 
 * Payload example: {"device": "TestDeviceA", "data": {"id": 51, "method": "onOff", "params": {"onOff":true}}}
 * ... also possible ..
 * Payload example: {"device": "TestDeviceB", "data": {"id": 52, "method": "color", "params": {"r":255, "g":255}}}
 * 
 * Topic (subscribe): v1/gateway/rpc
 * 
 * Send response:
 * 
 * Payload example: {"device": "TestDeviceA", "id": 51, "data": {"success": true}}
 * ... or
 * Payload example: {"device": "TestDeviceA", "id": 51, "data": {"success": false}}
 * 
 * Topic (publish): v1/gateway/rpc
 * </pre>
 * 
 * @author MK
 * @version 2023-06-28
 * @see https://thingsboard.io/docs/reference/gateway-mqtt-api/
 */
public class CommandStrategyTbTwoWaySync implements CommandStrategyIf {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommandStrategyTbTwoWaySync.class);

	CloudPublisher cloudPublisher;
	Gson gson = new Gson();

	public CommandStrategyTbTwoWaySync(CloudPublisher cloudPublisher) {
		super();
		this.cloudPublisher = cloudPublisher;
	}

	public void convertAndRedirectMessage(KuraMessage message, AssetService assetService) throws KuraException {
		String thingName = null;
		String actionName = null;
		Request request = null;
		try {
			LOGGER.info("Command strategy TB (two way directly) converts, redirects and responses the given message {}",
					message.toString());

			/* Extracts the topic and the body from the request */
			String topic = (String) message.getProperties().get("topic");
			String payloadRequest = new String(message.getPayload().getBody());
			LOGGER.info("Message arrived on topic:{} with body:{}", topic, payloadRequest);

			/* Extracts the thing name and action name from the body */
			request = gson.fromJson(payloadRequest, Request.class);
			thingName = request.getDevice();
			actionName = request.getData().getMethod();
			LOGGER.info("Extract the ThingName:{} and ActionName:{}", thingName, actionName);

			/*
			 * Creates and redirects the WoT Kura payload for the driver. THIS
			 * IMPLEMENTATION supports only ONE ENTRY param !!!
			 */
			for (Map.Entry<String, String> entry : request.getData().getParams().entrySet()) {
				// Creates the Kura redirect payload
				JsonObject payloadRedirect = new JsonObject();
				// Saves the e.g. payload into the "onOff":true redirect payload
				payloadRedirect.addProperty(entry.getKey(), entry.getValue());
				// Gets the channel corresponds with the given thing name
				Channel channel = assetService.getAsset(thingName).getAssetConfiguration().getAssetChannels()
						.get(actionName);
				LOGGER.info("Found channel {} / redirect payload {}", channel.toString(), payloadRedirect.toString());
				// Converts from String (JsonObject) to Optional<TypedValue<?>>
				Optional<TypedValue<?>> typedValue = Util.getTypedValue(channel.getValueType(),
						payloadRedirect.toString());
				LOGGER.info("typedValue is present {}", typedValue.isPresent());
				ChannelRecord channelRecord = channel.createWriteRecord(typedValue.get());
				LOGGER.info("Redirect the channel record {}", channelRecord.toString());
				assetService.getAsset(thingName).write(Arrays.asList(channelRecord));
				assetService.listAssets().forEach(e -> System.out.println("Assets: " + e.toString()));
			}

			/* Creates and publishes the good response */
			KuraPayload payloadResponse = new KuraPayload();
			// Creates a response
			Response response = new Response();
			response.setDevice(thingName);
			response.setId(request.getData().getId());
			Data data = new Data();
			data.setSuccess(true);
			response.setData(data);
			payloadResponse.setBody(gson.toJson(response).getBytes());
			LOGGER.info("Response the request with the payload {}", new String(payloadResponse.getBody()));
			// Publishes the response back to the server
			if(cloudPublisher==null)
				LOGGER.info("The cloudPublisher is null");
			cloudPublisher.publish(new KuraMessage(payloadResponse));
		} catch (Exception e) {
			LOGGER.error("EXCEPTION: ", e);
			/* Creates and publishes the bad response */
			KuraPayload payloadResponse = new KuraPayload();
			// Creates a response
			Response response = new Response();
			response.setDevice(thingName);
			response.setId(request.getData().getId());
			Data data = new Data();
			data.setSuccess(false);
			response.setData(data);
			payloadResponse.setBody(gson.toJson(response).getBytes());
			LOGGER.info("Response the request with the payload {}", new String(payloadResponse.getBody()));
			// Publishes the response back to the server
			cloudPublisher.publish(new KuraMessage(payloadResponse));
		}
	}

	public CloudPublisher getCloudPublisher() {
		return cloudPublisher;
	}

	public void setCloudPublisher(CloudPublisher cloudPublisher) {
		this.cloudPublisher = cloudPublisher;
	}
}
