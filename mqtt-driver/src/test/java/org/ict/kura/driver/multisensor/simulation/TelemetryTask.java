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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * The telemetry task to publish data to the given topic.
 * 
 * @author IKT M. Kuller
 * @version 2020-11-26
 */
public class TelemetryTask implements Runnable {
	/* log4j logger reference for class Start_T */
	private static final Logger LOGGER = LogManager.getFormatterLogger(TelemetryTask.class);

	/* MQTT Client to publish messages */
	private MqttClient mqttClient;

	/* Property name */
	private String propertyName;

	/* MQTT topic */
	private String topic;

	/* MQTT qos */
	private int mqttQos;

	/* MQTT pub task delay */
	private int delay;

	/* Number of messages to be sent */
	private int number;

	/**
	 * Constructor.
	 * 
	 * @param mqttClient   the MQTT client
	 * @param propertyName the WoT property name
	 * @param topic        the MQTT topic from WoT description
	 * @param mqttQos      the MQTT qos
	 * @param delay        the delay in milliseconds
	 * @param number       the number of messages to be sent
	 */
	public TelemetryTask(MqttClient mqttClient, String propertyName, String topic, int mqttQos, int delay, int number) {
		this.mqttClient = mqttClient;
		this.propertyName = propertyName;
		this.topic = topic;
		this.mqttQos = mqttQos;
		this.delay = delay;
		this.number = number;
	}

	@Override
	public void run() {
		/* MQTT message */
		MqttMessage message = null;

		/* Number of actual sent messages */
		int i = 0;

		try {
			LOGGER.info("Task property name '%s' => STARTED to sent '%d' messages, refresh after delay '%d' msec !",
					propertyName, number, delay);
			for (i = 0; i < number; i++) {
				switch (propertyName) {
				case "airqualityindex":
				case "distance":
				case "pressure":
				case "magnetic":
				case "co2equivalent":
					/* Creates a int payload */
					message = new MqttMessage(Util.getInstance().createIntPayload(propertyName).getBytes());
					break;
				case "humidity":
				case "breathevoc":
				case "temperature":
				case "rssi":
					/* Creates a double payload */
					message = new MqttMessage(Util.getInstance().createDoublePayload(propertyName).getBytes());
					break;
				case "lightcolorvalue":
					/* Creates a color payload */
					message = new MqttMessage(Util.getInstance().createLightColorPayload().getBytes());
					break;
				case "vibration":
					/* Creates a accelerometer payload */
					message = new MqttMessage(Util.getInstance().createVibrationPayload().getBytes());
					break;
				case "thermocam":
					/* Creates a thermocam payload */
					message = new MqttMessage(Util.getInstance().createThermocamPayload().getBytes());
					break;
				default:
					throw new IllegalArgumentException(
							String.format("NOT supported property name '%s' !", propertyName));
				}
				LOGGER.info("Send message payload: '%s'", new String(message.getPayload()));
//				FileWriter fileWriter = new FileWriter(String
//						.format("/home/karaoglan/Dokumente/Projekte/Foresight/Multisensor/%s.json", propertyName));
//				fileWriter.write(new String(message.getPayload()));
//				fileWriter.flush();
//				fileWriter.close();
				/* Publishes the message to the given topic, true = retained message */
				mqttClient.publish(topic, message.getPayload(), mqttQos, true);

				/* Sleep delay */
				Thread.sleep(delay);
			}
		} catch (Throwable t) {
			if (t instanceof IllegalArgumentException)
				LOGGER.info("NOT supported property name '%s' => task SHUTDOWN !", propertyName);
			else
				LOGGER.error("", t);
		} finally {
			LOGGER.info("Task property name '%s' => READY '%d' from '%d' messages successful sent !", propertyName, i,
					number);
		}
	}
}
