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
package org.ict.kura.driver.multisensor.simulation;

import java.io.Closeable;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.ict.gson.utils.AdapterFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The command task to subcribe from and publish data to the given topic.
 * 
 * @author IKT M. Kuller
 * @version 2020-11-26
 */
public class CommandTask implements MqttCallback, Closeable {
	/* log4j logger reference for class Start_T */
	private static final Logger LOGGER = LogManager.getFormatterLogger(CommandTask.class);

	/* MQTT Client to publish messages */
	private MqttClient mqttClient;

	/* Property name */
	private String propertyName;

	/* MQTT property topic */
	private String propertyTopic;

	/* Action name */
	private String actionName;

	/* MQTT action topic */
	private String actionTopic;

	/* MQTT qos */
	private int mqttQos;

	/* Gson instance to modify the received payload */
	Gson gson = AdapterFactory.getGsonWithDefaultWotTypeAdapters(true);

	/**
	 * Constructor.
	 * 
	 * @param mqttClient   the MQTT client
	 * @param propertyName the WoT property name
	 * @param topic        the MQTT topic from WoT description
	 * @param mqttQos      the MQTT qos
	 */
	public CommandTask(MqttClient mqttClient, String propertyName, String propertyTopic, String actionName,
			String actionTopic, int mqttQos) {
		LOGGER.info("... CommandTask");
		this.mqttClient = mqttClient;
		this.propertyName = propertyName;
		this.propertyTopic = propertyTopic;
		this.actionName = actionName;
		this.actionTopic = actionTopic;
		this.mqttQos = mqttQos;
		try {
			/* Register this be the MQTT service */
			mqttClient.setCallback(this);
			/* Subscribes to the actionTopic */
			mqttClient.subscribe(this.actionTopic);
		} catch (Throwable t) {
			LOGGER.error("", t);
		}
		LOGGER.info("Command task started with propertyName %s, propertyTopic %s, actionName %s, actionTopic %s",
				this.propertyName, this.propertyTopic, this.actionName, this.actionTopic);
	}

	@Override
	public void close() throws IOException {
		try {
			/* Unsubscribes to the actionTopic */
			mqttClient.unsubscribe(this.actionTopic);
		} catch (Throwable t) {
			LOGGER.error("", t);
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
		LOGGER.info("... connectionLost");
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		LOGGER.info("... messageArrived");

		try {
			LOGGER.info("... on topic: %s, with message: %s", topic, message.toString());

			/* Creates a property payload with timestamp and value */
			JsonElement jsonElement = gson.fromJson(new String(message.getPayload()), JsonElement.class);
			JsonObject jsonObj = jsonElement.getAsJsonObject();
			jsonObj.addProperty("time", System.currentTimeMillis());
			MqttMessage response = new MqttMessage(jsonObj.toString().getBytes());

			/*
			 * Publishes (send back) the received message to the given property topic, true
			 * = retained message
			 */
			mqttClient.publish(propertyTopic, response.getPayload(), mqttQos, true);
		} catch (Throwable t) {
			LOGGER.error("", t);
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// ... do nothing
	}
}
