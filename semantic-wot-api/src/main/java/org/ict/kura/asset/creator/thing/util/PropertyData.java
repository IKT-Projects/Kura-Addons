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

import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.ACTIVEENERGYDATA;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.ACTIVEENERGYUNIT;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.ACTIVEPOWERDATA;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.ACTIVEPOWERUNIT;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.APPARENTENERGYDATA;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.APPARENTENERGYUNIT;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.APPARENTPOWERDATA;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.APPARENTPOWERUNIT;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.BOOLEAN;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.BOOLEANDATA;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.ELECTRICCURRENTUNIT;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.ELECTRICCURRENTYDATA;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.FLOWRATEDATA;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.FLOWRATEUNIT;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.LEVELDATA;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.LEVELUNIT;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.LOWBATTERYDATA;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.MOTIONSENSORTYPE;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.MOTIONTYPEDATA;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.NUMBER;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.POWERFACTORDATA;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.REACTIVEENERGYDATA;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.REACTIVEENERGYUNIT;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.REACTIVEPOWERDATA;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.REACTIVEPOWERUNIT;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.STATEOFCHARGEDATA;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.TIMEDATA;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.TIMEUNIT;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.UNITOFMEASURE;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.VALUE;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.VOLTAGEDATA;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.VOLTAGEUNIT;

import java.net.URI;
import java.util.Arrays;
import java.util.UUID;

import org.ict.model.wot.dataschema.BooleanSchema;
import org.ict.model.wot.dataschema.DataSchema;
import org.ict.model.wot.dataschema.IntegerSchema;
import org.ict.model.wot.dataschema.NumberSchema;
import org.ict.model.wot.dataschema.StringSchema;

public class PropertyData {

	public static StringSchema time() {
		String uuid = UUID.randomUUID().toString();
		StringSchema time = StringSchema.builder().id("urn:" + uuid).title("Date time")
				.description("The date time in ISO 8601 format").readOnly(true)
				.atType(Arrays.asList(URI.create("http://schema.org/DateTime"))).build();
		return time;
	}

	public static NumberSchema time(DataSchema time, String unit) {
		NumberSchema temperatur = NumberSchema.builder().minimum(-30.0).maximum(50.0).title("time")
				.description("time in " + unit).atType(Arrays.asList(TIMEDATA)).unit(TIMEUNIT).readOnly(true)
				.modified(time).build();
		return temperatur;
	}

	public static NumberSchema generalValue(DataSchema time, String title) {
		NumberSchema value = NumberSchema.builder().title(title).description("last value").atType(Arrays.asList(VALUE))
				.unit("").readOnly(true).modified(time).build();
		return value;
	}

	public static NumberSchema angle(DataSchema time) {
		NumberSchema temperatur = NumberSchema.builder().minimum(0.0).maximum(360.0).title("Angle")
				.description("angle in degree")
				.atType(Arrays.asList(URI.create("http://www.ontology-of-units-of-measure.org/resource/om-2/Angle")))
				.unit("degree").readOnly(true).modified(time).build();
		return temperatur;
	}

	public static NumberSchema temperature(DataSchema time) {
		NumberSchema temperatur = NumberSchema.builder().minimum(-30.0).maximum(50.0).title("Temperature")
				.description("Temperature sensor in °C")
				.atType(Arrays.asList(URI.create("http://iotschema.org/TemperatureData")))
				.unit("http://iotschema.org/TemperatureUnit").readOnly(true).modified(time).build();
		return temperatur;
	}

	public static NumberSchema temperature(DataSchema time, String unit) {
		NumberSchema temperatur = NumberSchema.builder().minimum(0.0).maximum(999.0).title("Temperature")
				.description("Temperature sensor in " + unit)
				.atType(Arrays.asList(URI.create("http://iotschema.org/TemperatureData")))
				.unit("http://iotschema.org/TemperatureUnit").readOnly(true).modified(time).build();
		return temperatur;
	}

	public static NumberSchema temperature() {
		NumberSchema temperatur = NumberSchema.builder().minimum(-30.0).maximum(50.0).title("Temperature")
				.description("Temperature sensor in °C")
				.atType(Arrays.asList(URI.create("http://iotschema.org/TemperatureData")))
				.unit("http://iotschema.org/TemperatureUnit").build();
		return temperatur;
	}

	public static NumberSchema pressure(DataSchema time) {
		NumberSchema pressure = NumberSchema.builder().minimum(300.0).maximum(1100.0).title("Pressure")
				.description("Atmospheric pressure in pascal")
				.atType(Arrays.asList(URI.create("http://iotschema.org/PressureData")))
				.unit("http://iotschema.org/PressureUnit").readOnly(true).modified(time).build();
		return pressure;
	}

	public static NumberSchema pressure() {
		NumberSchema pressure = NumberSchema.builder().minimum(300.0).maximum(1100.0).title("Pressure")
				.description("Atmospheric pressure in pascal")
				.atType(Arrays.asList(URI.create("http://iotschema.org/PressureData")))
				.unit("http://iotschema.org/PressureUnit").build();
		return pressure;
	}

	public static NumberSchema location(DataSchema time, String title) {
		NumberSchema pressure = NumberSchema.builder().minimum(-180.0).maximum(180.0).title(title)
				.description(title + " of a location").atType(Arrays.asList(URI.create("http://schema.org/" + title)))
				.unit("http://iotschema.org/Number").readOnly(true).modified(time).build();
		return pressure;
	}

	public static BooleanSchema status(DataSchema time, String title) {
		BooleanSchema switchStatus = BooleanSchema.builder().title(title).description("The current state (true/false)")
				.atType(Arrays.asList(BOOLEANDATA)).unit(BOOLEAN).readOnly(true).modified(time).build();
		return switchStatus;
	}

	public static BooleanSchema onOff(DataSchema time) {
		BooleanSchema switchStatus = BooleanSchema.builder().title("onOff")
				.description("The current state (true/false)")
				.atType(Arrays.asList(URI.create("http://iotschema.org/StatusData"))).readOnly(true).modified(time)
				.build();
		return switchStatus;
	}

	public static BooleanSchema onOff() {
		BooleanSchema switchStatus = BooleanSchema.builder().title("onOff")
				.description("The current state (true/false)")
				.atType(Arrays.asList(URI.create("http://iotschema.org/StatusData"))).build();
		return switchStatus;
	}

	public static BooleanSchema openClose(DataSchema time) {
		BooleanSchema switchStatus = BooleanSchema.builder().title("openClose")
				.description("The current state (true/false)")
				.atType(Arrays.asList(URI.create("http://iotschema.org/StatusData"))).readOnly(true).modified(time)
				.build();
		return switchStatus;
	}

	public static BooleanSchema openClose() {
		BooleanSchema switchStatus = BooleanSchema.builder().title("openClose")
				.description("The current state (true/false)")
				.atType(Arrays.asList(URI.create("http://iotschema.org/StatusData"))).build();
		return switchStatus;
	}

	public static BooleanSchema upDown(DataSchema time) {
		BooleanSchema switchStatus = BooleanSchema.builder().title("upDown")
				.description("The current state (true/false)")
				.atType(Arrays.asList(URI.create("http://iotschema.org/StatusData"))).readOnly(true).modified(time)
				.build();
		return switchStatus;
	}

	public static BooleanSchema upDown() {
		BooleanSchema switchStatus = BooleanSchema.builder().title("upDown")
				.description("The current state (true/false)")
				.atType(Arrays.asList(URI.create("http://iotschema.org/StatusData"))).build();
		return switchStatus;
	}

	public static IntegerSchema motion(DataSchema time) {
		IntegerSchema motionDetected = IntegerSchema.builder().title("motionDetected")
				.description("The current motion state (0%-no, 100%-yes)").atType(Arrays.asList(MOTIONTYPEDATA))
				.modified(time).minimum(0).maximum(100).unit(MOTIONSENSORTYPE).readOnly(true).build();

		@SuppressWarnings("unused")
		BooleanSchema motion = BooleanSchema.builder().title("motionDetected")
				.description("The current boolean state (true/false)")
				.atType(Arrays.asList(URI.create("http://iotschema.org/MotionTypeData"))).readOnly(true).modified(time)
				.build();
		return motionDetected;
	}

	public static BooleanSchema motion() {
		BooleanSchema motion = BooleanSchema.builder().title("motionDetected")
				.description("The current boolean state (true/false)")
				.atType(Arrays.asList(URI.create("http://iotschema.org/MotionTypeData"))).build();
		return motion;
	}

	public static IntegerSchema level(DataSchema time, String title) {
		String description = title.substring(0, 1).toUpperCase() + " value (%)";
		IntegerSchema temperatur = IntegerSchema.builder().title(title).description(description)
				.atType(Arrays.asList(LEVELDATA)).modified(time).minimum(0).maximum(100).unit(LEVELUNIT).readOnly(true)
				.build();
		return temperatur;
	}

	public static IntegerSchema battery(DataSchema time, String title) {
		IntegerSchema battery = IntegerSchema.builder().title(title).description("Battery state of charge (%)")
				.atType(Arrays.asList(STATEOFCHARGEDATA)).modified(time).minimum(0).maximum(100).unit(LEVELUNIT)
				.readOnly(true).build();
		return battery;
	}

	public static IntegerSchema dimmer(DataSchema time) {
		IntegerSchema dimmer = IntegerSchema.builder().title("dimmwert").description("Dimming value (%)").maximum(100)
				.minimum(0).atType(Arrays.asList(URI.create("http://iotschema.org/DimmerData"))).readOnly(true)
				.modified(time).build();
		return dimmer;
	}

	public static IntegerSchema rColour(DataSchema time) {
		IntegerSchema colour = IntegerSchema.builder().title("red").description("Red channel of RGB colour")
				.maximum(100).minimum(0).atType(Arrays.asList(URI.create("http://iotschema.org/RColourData")))
				.readOnly(true).modified(time).build();
		return colour;
	}

	public static IntegerSchema bColour(DataSchema time) {
		IntegerSchema colour = IntegerSchema.builder().title("blue").description("Blue channel of RGB colour")
				.maximum(100).minimum(0).atType(Arrays.asList(URI.create("http://iotschema.org/BColourData")))
				.readOnly(true).modified(time).build();
		return colour;
	}

	public static IntegerSchema gColour(DataSchema time) {
		IntegerSchema colour = IntegerSchema.builder().title("green").description("Green channel of RGB colour")
				.maximum(100).minimum(0).atType(Arrays.asList(URI.create("http://iotschema.org/GColourData")))
				.readOnly(true).modified(time).build();
		return colour;
	}

	public static NumberSchema humidity(DataSchema time) {
		NumberSchema humidity = NumberSchema.builder().title("humidity").description("Humidity(Percent)").maximum(100E0)
				.minimum(0E0).atType(Arrays.asList(URI.create("http://iotschema.org/HumidityData")))
				.unit("http://iotschema.org/Percent").readOnly(true).modified(time).build();
		return humidity;
	}

	public static NumberSchema illuminance(DataSchema time) {
		NumberSchema illuminance = NumberSchema.builder().title("illuminance").description("Illuminance(Lux)")
				.maximum(3.4028234663852886E38).minimum(-3.4028234663852886E38)
				.atType(Arrays.asList(URI.create("http://iotschema.org/IlluminanceData")))
				.unit("http://iotschema.org/Lux").readOnly(true).modified(time).build();
		return illuminance;
	}

	public static NumberSchema voltage(DataSchema time) {
		NumberSchema voltage = NumberSchema.builder().title("voltage").description("Voltage(Volt)").maximum(1000E3)
				.minimum(0E0).atType(Arrays.asList(VOLTAGEDATA)).unit(VOLTAGEUNIT).readOnly(true).modified(time)
				.build();
		return voltage;
	}

	public static IntegerSchema activeEnergy(DataSchema time) {
		IntegerSchema activeEnergy = IntegerSchema.builder().title("energie").description("Energie(Wh)")
				.atType(Arrays.asList(ACTIVEENERGYDATA)).unit("http://iotschema.org/WattHour").unit(ACTIVEENERGYUNIT)
				.readOnly(true).modified(time).build();
		return activeEnergy;
	}

	public static NumberSchema reactiveEnergy(DataSchema time) {
		NumberSchema voltage = NumberSchema.builder().title("energie").description("Energie(Wh)")
				.atType(Arrays.asList(REACTIVEENERGYDATA)).unit("http://iotschema.org/WattHour")
				.unit(REACTIVEENERGYUNIT).readOnly(true).modified(time).build();
		return voltage;
	}

	public static NumberSchema generalEnergy(DataSchema time, String unit) {
		NumberSchema voltage = NumberSchema.builder().title("energy").description("general energy in " + unit)
				.atType(Arrays.asList(VALUE)).unit(unit).readOnly(true).modified(time).build();
		return voltage;
	}

	public static NumberSchema generalPower(DataSchema time, String unit) {
		NumberSchema voltage = NumberSchema.builder().title("power").description("general power in " + unit)
				.atType(Arrays.asList(VALUE)).unit(unit).readOnly(true).modified(time).build();
		return voltage;
	}

	public static NumberSchema activePower(DataSchema time) {
		NumberSchema voltage = NumberSchema.builder().title("power").description("Power(W)")
				.atType(Arrays.asList(ACTIVEPOWERDATA)).unit("http://iotschema.org/Watt").unit(ACTIVEPOWERUNIT)
				.readOnly(true).modified(time).build();
		return voltage;
	}

	public static NumberSchema reactivePower(DataSchema time) {
		NumberSchema voltage = NumberSchema.builder().title("power").description("Power(W)")
				.atType(Arrays.asList(REACTIVEPOWERDATA)).unit("http://iotschema.org/Watt").unit(REACTIVEPOWERUNIT)
				.readOnly(true).modified(time).build();
		return voltage;
	}

	public static NumberSchema apparentPower(DataSchema time) {
		NumberSchema apparentPower = NumberSchema.builder().title("").description("")
				.atType(Arrays.asList(APPARENTPOWERDATA)).unit(APPARENTPOWERUNIT).readOnly(true).modified(time).build();
		return apparentPower;
	}

	public static NumberSchema apparentEnergy(DataSchema time) {
		NumberSchema apparentEnergy = NumberSchema.builder().title("").description("")
				.atType(Arrays.asList(APPARENTENERGYDATA)).unit("http://iotschema.org/VoltAmpereHours")
				.unit(APPARENTENERGYUNIT).readOnly(true).modified(time).build();
		return apparentEnergy;
	}

	public static NumberSchema powerFactor(DataSchema time) {
		NumberSchema powerFactor = NumberSchema.builder().title("").description("")
				.atType(Arrays.asList(POWERFACTORDATA)).unit("http://iotschema.org/Percent").readOnly(true)
				.modified(time).build();
		return powerFactor;
	}

	public static NumberSchema electricCurrent(DataSchema time) {
		NumberSchema electricCurrent = NumberSchema.builder().title("electric current").description("current in ampere")
				.atType(Arrays.asList(ELECTRICCURRENTYDATA)).unit(ELECTRICCURRENTUNIT).readOnly(true).modified(time)
				.build();
		return electricCurrent;
	}

	public static NumberSchema density(DataSchema time) {
		NumberSchema density = NumberSchema.builder().title("").description("")
				.atType(Arrays.asList(URI.create("http://iotschema.org/DensityData")))
				.unit("http://iotschema.org/KilogramPerCubicMeter").readOnly(true).modified(time).build();
		return density;
	}

//	public static NumberSchema flowRate(DataSchema time) {
//		NumberSchema flowRate = NumberSchema.builder().title("").description("")
//				.atType(Arrays.asList(URI.create("http://iotschema.org/FlowRateData"))).unit("http://iotschema.org/LitrePerMinute").readOnly(true)
//				.modified(time).build();
//		return flowRate;
//	}

	public static NumberSchema wind(DataSchema time, String title) {
		NumberSchema carbondioxide = NumberSchema.builder().minimum(0.0).maximum(35.0).title(title)
				.description("Wind strength in meter per seconds")
				.atType(Arrays.asList(URI.create("http://iotschema.org/WindData")))
				.unit("http://iotschema.org/numberDataType").readOnly(true).modified(time).build();
		return carbondioxide;
	}

	public static NumberSchema flowRate(DataSchema time, String title) {
		NumberSchema carbondioxide = NumberSchema.builder().minimum(0.0).maximum(15.0).title(title)
				.description("Flow rate in m³ per hour").atType(Arrays.asList(FLOWRATEDATA)).unit(FLOWRATEUNIT)
				.readOnly(true).modified(time).build();
		return carbondioxide;
	}

	public static NumberSchema flowRate(DataSchema time, String title, String unit) {
		NumberSchema carbondioxide = NumberSchema.builder().title(title).description("Flow rate in " + unit)
				.atType(Arrays.asList(FLOWRATEDATA)).unit(FLOWRATEUNIT).readOnly(true).modified(time).build();
		return carbondioxide;
	}

	public static NumberSchema measuredTotal(DataSchema time, String title) {
		NumberSchema carbondioxide = NumberSchema.builder().minimum(0.0).maximum(99999.0).title(title)
				.description("Total measured data in m³").atType(Arrays.asList(NUMBER)).unit(UNITOFMEASURE.toString())
				.readOnly(true).modified(time).build();
		return carbondioxide;
	}

	public static NumberSchema measuredTotal(DataSchema time, String title, String unit) {
		NumberSchema carbondioxide = NumberSchema.builder().minimum(0.0).maximum(99999.0).title(title)
				.description("Total measured data in " + unit).atType(Arrays.asList(NUMBER))
				.unit(UNITOFMEASURE.toString()).readOnly(true).modified(time).build();
		return carbondioxide;
	}

	public static NumberSchema carbonDioxideConcentration(DataSchema time) {
		NumberSchema carbondioxide = NumberSchema.builder().minimum(400.0).maximum(29206.0)
				.title("CarbondioxideConcentration").description("Carbon dioxide sconcentration in parts per million")
				.atType(Arrays.asList(URI.create("http://iotschema.org/CarbonDioxideConcentrationData")))
				.unit("http://iotschema.org/GasInAirConcentrationUnit").readOnly(true).modified(time).build();
		return carbondioxide;
	}

//	public static NumberSchema carbonDioxideConcentration(DataSchema time) {
//		NumberSchema carbonDioxideConcentration = NumberSchema.builder().title("").description("")
//				.atType(Arrays.asList(URI.create("http://iotschema.org/CarbonDioxideConcentrationData"))).unit("http://iotschema.org/PartsPerMillion")
//				.readOnly(true).modified(time).build();
//		return carbonDioxideConcentration;
//	}

	public static NumberSchema tVOC(DataSchema time, String title) {
		NumberSchema carbondioxide = NumberSchema.builder().minimum(0.0).maximum(32768.0).title(title)
				.description("Total Volatile Organic Compound in air in parts per billion")
				.atType(Arrays.asList(URI.create("http://iotschema.org/DemandControlledVentilationData")))
				.unit("http://iotschema.org/GasInAirConcentrationUnit").readOnly(true).modified(time).build();
		return carbondioxide;
	}

	public static NumberSchema nitrogenConcentration(DataSchema time) {
		NumberSchema nitrogenConcentration = NumberSchema.builder().title("").description("")
				.atType(Arrays.asList(URI.create("http://iotschema.org/NitrogenConcentrationData")))
				.unit("http://iotschema.org/PartsPerMillion").readOnly(true).modified(time).build();
		return nitrogenConcentration;
	}

	public static NumberSchema oxygenConcentration(DataSchema time) {
		NumberSchema oxygenConcentration = NumberSchema.builder().title("").description("")
				.atType(Arrays.asList(URI.create("http://iotschema.org/OxygenConcentrationData")))
				.unit("http://iotschema.org/PartsPerMillion").readOnly(true).modified(time).build();
		return oxygenConcentration;
	}

	public static NumberSchema argonConcentration(DataSchema time) {
		NumberSchema argonConcentration = NumberSchema.builder().title("").description("")
				.atType(Arrays.asList(URI.create("http://iotschema.org/ArgonConcentrationData")))
				.unit("http://iotschema.org/PartsPerMillion").readOnly(true).modified(time).build();
		return argonConcentration;
	}

	public static IntegerSchema stateOfCharge(DataSchema time) {
		IntegerSchema stateOfCharge = IntegerSchema.builder().title("").description("")
				.atType(Arrays.asList(URI.create("http://iotschema.org/StateOfChargeData"))).unit("iot.Percent")
				.readOnly(true).modified(time).build();
		return stateOfCharge;
	}

	public static IntegerSchema electricCharge(DataSchema time) {
		IntegerSchema electricCharge = IntegerSchema.builder().title("").description("")
				.atType(Arrays.asList(URI.create("http://iotschema.org/ElectricChargeData"))).unit("iot.AmpereHour")
				.readOnly(true).modified(time).build();
		return electricCharge;
	}

	public static BooleanSchema chargingStatus(DataSchema time) {
		BooleanSchema chargingStatus = BooleanSchema.builder().title("").description("")
				.atType(Arrays.asList(URI.create("http://iotschema.org/ChargingStatusData"))).readOnly(true)
				.modified(time).build();
		return chargingStatus;
	}

	public static IntegerSchema lowBattery(DataSchema time, String title) {
		IntegerSchema lowBattery = IntegerSchema.builder().title(title).description("battery is low")
				.atType(Arrays.asList(LOWBATTERYDATA)).unit(BOOLEAN).readOnly(true).modified(time).build();

//		BooleanSchema lowBattery = BooleanSchema.builder().title(title).description("battery is low")
//				.atType(Arrays.asList(LOWBATTERYDATA)).unit(BOOLEAN).readOnly(true).modified(time).build();
		return lowBattery;
	}

}
