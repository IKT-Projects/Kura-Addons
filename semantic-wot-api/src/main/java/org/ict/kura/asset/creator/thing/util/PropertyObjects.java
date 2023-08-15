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
package org.ict.kura.asset.creator.thing.util;

import static org.ict.kura.asset.creator.thing.util.PropertyData.activeEnergy;
import static org.ict.kura.asset.creator.thing.util.PropertyData.activePower;
import static org.ict.kura.asset.creator.thing.util.PropertyData.angle;
import static org.ict.kura.asset.creator.thing.util.PropertyData.apparentEnergy;
import static org.ict.kura.asset.creator.thing.util.PropertyData.apparentPower;
import static org.ict.kura.asset.creator.thing.util.PropertyData.battery;
import static org.ict.kura.asset.creator.thing.util.PropertyData.carbonDioxideConcentration;
import static org.ict.kura.asset.creator.thing.util.PropertyData.dimmer;
import static org.ict.kura.asset.creator.thing.util.PropertyData.electricCurrent;
import static org.ict.kura.asset.creator.thing.util.PropertyData.flowRate;
import static org.ict.kura.asset.creator.thing.util.PropertyData.generalEnergy;
import static org.ict.kura.asset.creator.thing.util.PropertyData.generalPower;
import static org.ict.kura.asset.creator.thing.util.PropertyData.generalValue;
import static org.ict.kura.asset.creator.thing.util.PropertyData.humidity;
import static org.ict.kura.asset.creator.thing.util.PropertyData.illuminance;
import static org.ict.kura.asset.creator.thing.util.PropertyData.level;
import static org.ict.kura.asset.creator.thing.util.PropertyData.location;
import static org.ict.kura.asset.creator.thing.util.PropertyData.lowBattery;
import static org.ict.kura.asset.creator.thing.util.PropertyData.measuredTotal;
import static org.ict.kura.asset.creator.thing.util.PropertyData.motion;
import static org.ict.kura.asset.creator.thing.util.PropertyData.onOff;
import static org.ict.kura.asset.creator.thing.util.PropertyData.openClose;
import static org.ict.kura.asset.creator.thing.util.PropertyData.powerFactor;
import static org.ict.kura.asset.creator.thing.util.PropertyData.pressure;
import static org.ict.kura.asset.creator.thing.util.PropertyData.reactiveEnergy;
import static org.ict.kura.asset.creator.thing.util.PropertyData.reactivePower;
import static org.ict.kura.asset.creator.thing.util.PropertyData.status;
import static org.ict.kura.asset.creator.thing.util.PropertyData.tVOC;
import static org.ict.kura.asset.creator.thing.util.PropertyData.temperature;
import static org.ict.kura.asset.creator.thing.util.PropertyData.time;
import static org.ict.kura.asset.creator.thing.util.PropertyData.upDown;
import static org.ict.kura.asset.creator.thing.util.PropertyData.voltage;
import static org.ict.kura.asset.creator.thing.util.PropertyData.wind;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.ict.model.wot.dataschema.DataSchema;
import org.ict.model.wot.dataschema.ObjectSchema;


public class PropertyObjects {

	private static ObjectSchema createPropertyObject(String keyName, DataSchema property, DataSchema time) {
		Map<String, DataSchema> map = new HashMap<>();
		map.put(keyName, property);
		map.put("time", time);
		ObjectSchema objectSchema = ObjectSchema.builder().properties(map).required(Arrays.asList("time", keyName))
				.readOnly(true).build();
		return objectSchema;
	}

	public static ObjectSchema timePropertyObject(String keyName, String unit) {
		DataSchema time = time();
		DataSchema property = time(time, unit);
		Map<String, DataSchema> map = new HashMap<>();
		map.put(keyName, property);
		return ObjectSchema.builder().properties(map).required(Arrays.asList("time", keyName))
				.readOnly(true).build();
	}
	
	public static ObjectSchema generalValuePropertyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = generalValue(time, keyName);
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema anglePropertyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = angle(time);
		return createPropertyObject(keyName, property, time);
	}
	
	
	public static ObjectSchema temperaturePropertyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = temperature(time);
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema temperaturePropertyObject(String keyName, String unit) {
		DataSchema time = time();
		DataSchema property = temperature(time, unit);
		return createPropertyObject(keyName, property, time);
	}

	public static ObjectSchema humidityPropertyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = humidity(time);
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema pressurePropertyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = pressure(time);
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema locationLengthPropertyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = location(time, "longitude");
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema locationWidthPropertyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = location(time, "latitude");
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema flowRatePropertyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = flowRate(time, keyName);
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema flowRatePropertyObject(String keyName, String unit) {
		DataSchema time = time();
		DataSchema property = flowRate(time, keyName, unit);
		return createPropertyObject(keyName, property, time);
	}

	public static ObjectSchema measuredTotalPropertyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = measuredTotal(time, keyName);
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema measuredTotalPropertyObject(String keyName, String unit) {
		DataSchema time = time();
		DataSchema property = measuredTotal(time, keyName, unit);
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema carbonDioxidePropertyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = carbonDioxideConcentration(time);
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema tvocPropertyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = tVOC(time, keyName);
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema windPropertyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = wind(time, keyName);
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema illuminancePropertyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = illuminance(time);
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema statusPropertyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = status(time, keyName);
		return createPropertyObject(keyName, property, time);
	}


	public static ObjectSchema onOffPropertyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = onOff(time);
		return createPropertyObject(keyName, property, time);
	}

	public static ObjectSchema openClosePropertyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = openClose(time);
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema upDownPropertyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = upDown(time);
		return createPropertyObject(keyName, property, time);
	}

	public static ObjectSchema motionPropertyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = motion(time);
		return createPropertyObject(keyName, property, time);
	}

	public static ObjectSchema dimmerPropertyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = dimmer(time);
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema levelPropertyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = level(time, keyName);
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema batteryPropertyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = battery(time, keyName);
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema reactiveEnergyImportedObject(String keyName) {
		DataSchema time = time();
		DataSchema property = reactiveEnergy(time);
		return createPropertyObject(keyName, property, time);
	}

	public static ObjectSchema reactivePowerObject() {
		DataSchema time = time();
		DataSchema property = reactivePower(time);
		String keyName = "power";
		return createPropertyObject(keyName, property, time);
	}

	public static ObjectSchema activeEnergyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = activeEnergy(time);
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema reactiveEnergyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = reactiveEnergy(time);
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema generalEnergyObject(String keyName, String unit) {
		DataSchema time = time();
		DataSchema property = generalEnergy(time, unit);
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema generalPowerObject(String keyName, String unit) {
		DataSchema time = time();
		DataSchema property = generalPower(time, unit);
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema apparentEnergyObject(String keyName) {
		DataSchema time = time();
		DataSchema property = apparentEnergy(time);
		return createPropertyObject(keyName, property, time);
	}
	public static ObjectSchema apparentPowerObject(String keyName) {
		DataSchema time = time();
		DataSchema property = apparentPower(time);
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema activePowerObject(String name) {
		DataSchema time = time();
		DataSchema property = activePower(time);
		String keyName = name;
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema reactivePowerObject(String name) {
		DataSchema time = time();
		DataSchema property = reactivePower(time);
		String keyName = name;
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema voltageObject(String keyName) {
		DataSchema time = time();
		DataSchema property = voltage(time);
		return createPropertyObject(keyName, property, time);
	}
	
	public static ObjectSchema electricCurrentObject(String keyName) {
		DataSchema time = time();
		DataSchema property = electricCurrent(time);
		return createPropertyObject(keyName, property, time);
	}

	public static ObjectSchema powerFactorObject(String keyName) {
		DataSchema time = time();
		DataSchema property = powerFactor(time);
		return createPropertyObject(keyName, property, time);
	}

	public static ObjectSchema lowBatteryObject(String keyName) {
		DataSchema time = time();
		DataSchema property = lowBattery(time, keyName);
		return createPropertyObject(keyName, property, time);
	}
}
