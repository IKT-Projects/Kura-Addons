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
package org.ict.kura.internal.driver.knx.util;

import static tuwien.auto.calimero.dptxlator.DPTXlator2ByteFloat.DPT_AIRQUALITY;
//DPT ID 9.xxx
import static tuwien.auto.calimero.dptxlator.DPTXlator2ByteFloat.DPT_HUMIDITY;
import static tuwien.auto.calimero.dptxlator.DPTXlator2ByteFloat.DPT_INTENSITY_OF_LIGHT;
import static tuwien.auto.calimero.dptxlator.DPTXlator2ByteFloat.DPT_TEMPERATURE;
import static tuwien.auto.calimero.dptxlator.DPTXlator2ByteFloat.DPT_TEMPERATURE_DIFFERENCE;
import static tuwien.auto.calimero.dptxlator.DPTXlator2ByteFloat.DPT_WIND_SPEED;
//DPT ID 7.xxx
import static tuwien.auto.calimero.dptxlator.DPTXlator2ByteUnsigned.DPT_BRIGHTNESS;
//DPT ID 14.xxx
import static tuwien.auto.calimero.dptxlator.DPTXlator4ByteFloat.DPT_POWER;
//DPT ID 5.xxx
import static tuwien.auto.calimero.dptxlator.DPTXlator8BitUnsigned.DPT_SCALING;
import static tuwien.auto.calimero.dptxlator.DPTXlator8BitUnsigned.DPT_VALUE_1_UCOUNT;
// DPT ID 1.xxx
import static tuwien.auto.calimero.dptxlator.DPTXlatorBoolean.DPT_BOOL;
import static tuwien.auto.calimero.dptxlator.DPTXlatorBoolean.DPT_ENABLE;
import static tuwien.auto.calimero.dptxlator.DPTXlatorBoolean.DPT_START;
import static tuwien.auto.calimero.dptxlator.DPTXlatorBoolean.DPT_STEP;
import static tuwien.auto.calimero.dptxlator.DPTXlatorBoolean.DPT_SWITCH;
import static tuwien.auto.calimero.dptxlator.DPTXlatorBoolean.DPT_UPDOWN;
import static tuwien.auto.calimero.dptxlator.DPTXlatorBoolean.DPT_WINDOW_DOOR;

import java.util.HashMap;
import java.util.Map;

import org.ict.kura.internal.driver.knx.util.dpt.Dpt;
import org.ict.kura.internal.driver.knx.util.dpt.Dpt1;
import org.ict.kura.internal.driver.knx.util.dpt.Dpt14;
import org.ict.kura.internal.driver.knx.util.dpt.Dpt5;
import org.ict.kura.internal.driver.knx.util.dpt.Dpt7;
import org.ict.kura.internal.driver.knx.util.dpt.Dpt9;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tuwien.auto.calimero.dptxlator.DPTXlator2ByteFloat;
import tuwien.auto.calimero.dptxlator.DPTXlator2ByteUnsigned;
import tuwien.auto.calimero.dptxlator.DPTXlator4ByteFloat;
import tuwien.auto.calimero.dptxlator.DPTXlator8BitUnsigned;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;

/**
 * This class implements required helper methods and focus on e.g. type
 * conversion between KNX and WoT data types.
 * 
 * add further KNX DPTs !
 * 
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2022-12-02
 */
final public class Tool {
	/* The logger instance */
	private static final Logger LOGGER = LoggerFactory.getLogger(Tool.class);

	/** To create an instance - its not possible */
	private Tool() {
	}

	/**
	 * Creates a new DPT object, if necessary (singleton).
	 * 
	 * @param DptId the DPT id
	 * @return the DPT object as singleton
	 */
	public static Dpt createDptXlator(String DptId) {
		try {
			switch (DptId) {
			case "1.001":
				LOGGER.info("Create DPT with ID {}", DptId);
				return new Dpt1(new DPTXlatorBoolean(DPT_SWITCH));
			case "1.002":
				LOGGER.info("Create DPT with ID {}", DptId);
				return new Dpt1(new DPTXlatorBoolean(DPT_BOOL));
			case "1.003":
				LOGGER.info("Create DPT with ID {}", DptId);
				return new Dpt1(new DPTXlatorBoolean(DPT_ENABLE));
			case "1.007":
				LOGGER.info("Create DPT with ID {}", DptId);
				return new Dpt1(new DPTXlatorBoolean(DPT_STEP));
			case "1.008":
				LOGGER.info("Create DPT with ID {}", DptId);
				return new Dpt1(new DPTXlatorBoolean(DPT_UPDOWN));
			case "1.010":
				LOGGER.info("Create DPT with ID {}", DptId);
				return new Dpt1(new DPTXlatorBoolean(DPT_START));
			case "1.019":
				LOGGER.info("Create DPT with ID {}", DptId);
				return new Dpt1(new DPTXlatorBoolean(DPT_WINDOW_DOOR));
			case "5.001":
				LOGGER.info("Create DPT with ID {}", DptId);
				return new Dpt5(new DPTXlator8BitUnsigned(DPT_SCALING));
			case "5.010":
				LOGGER.info("Create DPT with ID {}", DptId);
				return new Dpt5(new DPTXlator8BitUnsigned(DPT_VALUE_1_UCOUNT));
			case "7.013":
				LOGGER.info("Create DPT with ID {}", DptId);
				return new Dpt7(new DPTXlator2ByteUnsigned(DPT_BRIGHTNESS));
			case "9.001":
				LOGGER.info("Create DPT with ID {}", DptId);
				return new Dpt9(new DPTXlator2ByteFloat(DPT_TEMPERATURE));
			case "9.002":
				LOGGER.info("Create DPT with ID {}", DptId);
				return new Dpt9(new DPTXlator2ByteFloat(DPT_TEMPERATURE_DIFFERENCE));
			case "9.004":
				LOGGER.info("Create DPT with ID {}", DptId);
				return new Dpt9(new DPTXlator2ByteFloat(DPT_INTENSITY_OF_LIGHT));
			case "9.005":
				LOGGER.info("Create DPT with ID {}", DptId);
				return new Dpt9(new DPTXlator2ByteFloat(DPT_WIND_SPEED));
			case "9.007":
				LOGGER.info("Create DPT with ID {}", DptId);
				return new Dpt9(new DPTXlator2ByteFloat(DPT_HUMIDITY));
			case "9.008":
				LOGGER.info("Create DPT with ID {}", DptId);
				return new Dpt9(new DPTXlator2ByteFloat(DPT_AIRQUALITY));
			case "14.056":
				LOGGER.info("Create DPT with ID {}", DptId);
				return new Dpt14(new DPTXlator4ByteFloat(DPT_POWER));
			default:
				LOGGER.info("NOT supported DPT ID {}", DptId);
				return null;
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return null;
	}

	/**
	 * Creates a keepalived message payload and forwards this to the EventAdmin.
	 * 
	 * @param driverPid the service pid of the driver
	 * @param topic     the topic
	 * @param connected the connection status true or false
	 * @param ea        the {@link EventAdmin} instance
	 */
	public static void sendKeepalived(String driverServicePid, String topic, boolean connected, EventAdmin ea) {
		LOGGER.info("Sends the keepalived of driver: " + driverServicePid);
		// The paylaod map
		Map<String, Object> eventAdminProperties = new HashMap<>();

		// We need a reference to the driver pid
		eventAdminProperties.put("driverServicePid", driverServicePid);

		// Sets the status of the drivers connector
		eventAdminProperties.put("connected", connected);
		LOGGER.info("Sends the connection status " + connected + " to EventAdmin with Topic: " + topic);

		// Gets the {@link EventAdmin} instance and publish the keepalived message
		ea.sendEvent(new Event(topic, eventAdminProperties));
	}
}