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
package org.ict.kura.internal.driver.avm.api;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.ict.kura.driver.thing.ThingChannelListener;
import org.ict.kura.internal.driver.avm.AvmBindingConfig;
import org.ict.kura.internal.driver.avm.api.data.Device;
import org.ict.kura.internal.driver.avm.api.data.Devicelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The AVM update Thread
 * 
 * This class starts a thread to update the avm data with the given poll
 * interval.
 * 
 * @author biskup
 * @version 2023-02-03
 */
public class AvmUpdateThread implements Runnable, Closeable {
	/* The open weather update thread logger instance */
	private static final Logger LOGGER = LoggerFactory.getLogger(AvmUpdateThread.class);

	private static final String PAYLOAD = "{\"time\" : %d, \"%s\": %s}";

	/* The Avm api implementation */
	private AvmApi avmApi;

	/* The {@link ThingChannelListener} map */
	private Map<String, ThingChannelListener> thingChannelListeners;

	/* The map of technology bindings */
	private Map<String, AvmBindingConfig> avmBindingConfigs = new HashMap<>();

	/* The poll interval */
	private int pollInterval;

	/* the Thread instance */
	private Thread runner;

	public AvmUpdateThread(AvmApi avmApi, int pollInterval, Map<String, ThingChannelListener> thingChannelListeners,
			Map<String, AvmBindingConfig> avmBindingConfigs) {
		LOGGER.info("constructor...");
		Objects.requireNonNull(avmApi, "INVALID - the avm api is null !");
		Objects.requireNonNull(thingChannelListeners, "INVALID - the thingChannelListeners is null !");
		this.avmApi = avmApi;
		this.pollInterval = pollInterval;
		this.thingChannelListeners = thingChannelListeners;
		this.avmBindingConfigs = avmBindingConfigs;

		/* Creates a thread instance */
		this.runner = new Thread(this);
		/* Starts the module as a thread */
		this.runner.start();
	}

	@Override
	public void close() throws IOException {
		LOGGER.info("Stops the thread");
		if (runner != null)
			runner.interrupt();
	}

	@Override
	public void run() {
		String threadName = Thread.currentThread().getName();
		LOGGER.info("Start thread {}", threadName);
		try {
			/* Empirically first sleep */
			TimeUnit.SECONDS.sleep(20);

			/* Thread is running while it is not interrupted */
			while (!Thread.interrupted()) {
				LOGGER.info("Polling device list");
				try {
					Devicelist devicelist = avmApi.getDeviceList();
					/* Creates the actual time stamp */
					long newTime = System.currentTimeMillis();

					/* iterate over the device list and extract all devices */
					for (Device device : devicelist.getDevice()) {
						String deviceName = device.getName().replace(" ", "").replace("!", "-").replace("#", "-");
						List<String> hrefs = avmBindingConfigs.keySet().stream()
								.filter(ref -> ref.contains(deviceName) && ref.contains("properties"))
								.collect(Collectors.toList());

						if (device.getProductname().equals("FRITZ!DECT 500")) {
							if (device.getIdentifier().contains("-1")) {
								setValue(hrefs, device, newTime);
							}
						} else {
							setValue(hrefs, device, newTime);
						}
					}

					/* Sleep for seconds */
					TimeUnit.SECONDS.sleep(pollInterval);
				} catch (Exception e) {
					LOGGER.error("", e);
				}

			}
		} catch (

		Throwable t) {
			LOGGER.error("", t);
		}

	}

	private void setValue(List<String> hrefs, Device device, long newTime) {
		for (String string : hrefs) {
			String propertie = string.substring(string.lastIndexOf("/") + 1);
			try {
				switch (propertie) {
				case "battery":
					String battery = String.format(PAYLOAD, newTime, "battery", device.getBattery());
					thingChannelListeners.get(string).doUpdate(battery);
					break;
				case "batteryLow":
					String batteryLow = String.format(PAYLOAD, newTime, "batteryLow", device.getBatterylow());
					thingChannelListeners.get(string).doUpdate(batteryLow);
					break;
				case "temperature":
					// temperature must be converted eg.: 100 -> 10.0
					float temp = device.getTemperature().getCelsius() / 10.0f;
					String tempValue = String.format(PAYLOAD, newTime, "temperature", temp);
					thingChannelListeners.get(string).doUpdate(tempValue);
					break;
				case "tempOffset":
					// temperature offset needs to be converted eg.: 100 -> 10.0
					float tOffset = device.getTemperature().getOffset() / 10.0f;
					String tOffsetValue = String.format(PAYLOAD, newTime, "tempOffset", tOffset);
					thingChannelListeners.get(string).doUpdate(tOffsetValue);
					break;
				case "tIst":
					// temperature ist must be divided by 2
					double ist = Double.valueOf(device.getHkr().getTist());
					ist = ist / 2;
					String tIst = String.format(PAYLOAD, newTime, "tIst", ist);
					thingChannelListeners.get(string).doUpdate(tIst);
					break;
				case "holidayActive":
					String holidayActive = String.format(PAYLOAD, newTime, "holidayActive",
							device.getHkr().getHolidayactive());
					thingChannelListeners.get(string).doUpdate(holidayActive);
					break;
				case "summerActive":
					String summerActive = String.format(PAYLOAD, newTime, "summerActive",
							device.getHkr().getSummeractive());
					thingChannelListeners.get(string).doUpdate(summerActive);
					break;
				case "windowOpenActive":
					String windowOpenActive = String.format(PAYLOAD, newTime, "windowOpenActive",
							device.getHkr().getWindowopenactiv());
					thingChannelListeners.get(string).doUpdate(windowOpenActive);
					break;
				case "lock":
					String lock = String.format(PAYLOAD, newTime, "lock", device.getHkr().getDevicelock());
					thingChannelListeners.get(string).doUpdate(lock);
					break;
				case "tSoll":
					// temperature soll must be divided by 2
					double soll = Double.valueOf(device.getHkr().getTsoll());
					soll = soll / 2;
					String tSoll = String.format(PAYLOAD, newTime, "tSoll", soll);
					thingChannelListeners.get(string).doUpdate(tSoll);
					break;
				case "humidity":
					int humidity = device.getHumidity().getRelHumidity();
					String humidityValue = String.format(PAYLOAD, newTime, "humidity", humidity);
					thingChannelListeners.get(string).doUpdate(humidityValue);
					break;
				case "onOff":
					// state must be converted to a boolean value eg.: 1 = true; 0 = false
					boolean state = device.getSimpleonoff().getState() == 1 ? true : false;
					String onOff = String.format(PAYLOAD, newTime, "onOff", state);
					thingChannelListeners.get(string).doUpdate(onOff);
					break;
				case "energy":
					String energy = String.format(PAYLOAD, newTime, "energy", device.getPowermeter().getEnergy());
					thingChannelListeners.get(string).doUpdate(energy);
					break;
				case "power":
					// temperature must be divided by 1000.0 eg.: 5281 -> 5.2 watt
					float power = device.getPowermeter().getPower() / 1000.0f;
					String powerValue = String.format(PAYLOAD, newTime, "power", power);
					thingChannelListeners.get(string).doUpdate(powerValue);
					break;
				default:
					break;
				}
			} catch (Exception e) {
				LOGGER.error("Value is null", e);
			}
		}
	}

}
