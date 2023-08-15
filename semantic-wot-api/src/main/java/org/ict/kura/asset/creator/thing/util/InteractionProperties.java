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

import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.ACTIVEENERGYEXPORTED;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.ACTIVEENERGYIMPORTED;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.ACTIVEPOWERA;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.ACTIVEPOWERB;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.ACTIVEPOWERC;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.AIRTEMPERATURE;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.APPARENTENERGY;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.APPARENTPOWERA;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.APPARENTPOWERB;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.APPARENTPOWERC;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.AVERAGEELECTRICCURRENT;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.CARBONDIOXIDECONCENTRATION;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.CURRENTDIMMER;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.CURRENTLEVEL;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.ELECTRICCURRENTA;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.ELECTRICCURRENTB;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.ELECTRICCURRENTC;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.ELECTRICITY;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.ENERGY;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.GEOCOORDINATES;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.HUMIDITY;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.ILLUMINANCE;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.LOWBATTERY;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.MOTIONTYPE;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.MULTILEVEL_STATE;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.POWER;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.PROVIDESFLOWRATEDATA;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.REACTIVEENERGYEXPORTED;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.REACTIVEENERGYIMPORTED;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.STATEOFCHARGE;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.SWITCHSTATUS;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.TEMPERATURE;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.TIMESERIES;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.TOTALACTIVEPOWER;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.TOTALAPPARENTPOWER;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.TOTALPOWERFACTOR;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.VALUE;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.WINDSTRENGTH;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.activeEnergyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.activePowerObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.anglePropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.apparentEnergyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.apparentPowerObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.batteryPropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.carbonDioxidePropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.dimmerPropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.electricCurrentObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.flowRatePropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.generalEnergyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.generalPowerObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.generalValuePropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.humidityPropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.illuminancePropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.levelPropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.locationLengthPropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.locationWidthPropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.lowBatteryObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.measuredTotalPropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.motionPropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.onOffPropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.openClosePropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.powerFactorObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.pressurePropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.reactiveEnergyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.statusPropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.temperaturePropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.timePropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.tvocPropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.upDownPropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.voltageObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.windPropertyObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import org.ict.model.wot.constant.Op;
import org.ict.model.wot.core.PropertyAffordance;
import org.ict.model.wot.dataschema.DataSchema;
import org.ict.model.wot.hypermedia.Form;

public class InteractionProperties {

	public static PropertyAffordance createTimeProperty(String href, String propertyName, String description,
			String unit) throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(TIMESERIES);
		DataSchema propertieObject = timePropertyObject(propertyName, unit);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createTemperatureProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createTemperatureProperty(href, propertyName, "Current value of temperature",
				propertyName);
		return propertie;
	}

	public static PropertyAffordance createTemperatureProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(TEMPERATURE);
		types.add(AIRTEMPERATURE);
		DataSchema propertieObject = temperaturePropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createTemperatureProperty(String href, String propertyName, String description,
			String unit) throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(TEMPERATURE);
		types.add(AIRTEMPERATURE);
		DataSchema propertieObject = temperaturePropertyObject(propertyName, unit);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createPressureProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createPressureProperty(href, propertyName, "Current value of pressure");
		return propertie;
	}

	public static PropertyAffordance createPressureProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(CURRENTLEVEL);
		DataSchema propertieObject = pressurePropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createLevelCustomProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(CURRENTLEVEL);
		DataSchema propertieObject = levelPropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createMutliLevelStateProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(MULTILEVEL_STATE);
		DataSchema propertieObject = generalValuePropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createFlowRateProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createFlowRateProperty(href, propertyName, "Current value of flow rate");
		return propertie;
	}

	public static PropertyAffordance createFlowRateProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(PROVIDESFLOWRATEDATA);
		DataSchema propertieObject = flowRatePropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createFlowRateProperty(String href, String propertyName, String description,
			String unit) throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(PROVIDESFLOWRATEDATA);
		DataSchema propertieObject = flowRatePropertyObject(propertyName, unit);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createVolumeTotalProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createVolumeTotalProperty(href, propertyName, "The total value of flow");
		return propertie;
	}

	public static PropertyAffordance createVolumeTotalProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(VALUE);
		DataSchema propertieObject = measuredTotalPropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createMeasuredTotalProperty(String href, String propertyName, String description,
			String unit) throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(VALUE);
		DataSchema propertieObject = measuredTotalPropertyObject(propertyName, unit);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createLocationLengthProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createLocationLengthProperty(href, propertyName, "Current value of longitude");
		return propertie;
	}

	public static PropertyAffordance createLocationLengthProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(GEOCOORDINATES);
		DataSchema propertieObject = locationLengthPropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createLocationWidthProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createLocationWidthProperty(href, propertyName, "Current value of latitude");
		return propertie;
	}

	public static PropertyAffordance createLocationWidthProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(GEOCOORDINATES);
		DataSchema propertieObject = locationWidthPropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createCarbonDioxideProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createCarbonDioxideProperty(href, propertyName,
				"Current value of carbon dioxide concentration");
		return propertie;
	}

	public static PropertyAffordance createCarbonDioxideProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(CARBONDIOXIDECONCENTRATION);
		DataSchema propertieObject = carbonDioxidePropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createTvocProperty(String href, String propertyName) throws MalformedURLException {
		PropertyAffordance propertie = createTvocProperty(href, propertyName, "Current value of tvoc");
		return propertie;
	}

	public static PropertyAffordance createTvocProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(CURRENTLEVEL);
		DataSchema propertieObject = tvocPropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createWindProperty(String href, String propertyName) throws MalformedURLException {
		PropertyAffordance propertie = createWindProperty(href, propertyName, "Current wind strength");
		return propertie;
	}

	public static PropertyAffordance createWindProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(WINDSTRENGTH);
		DataSchema propertieObject = windPropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createPositionProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createPositionProperty(href, propertyName,
				"Current value of the shutter position");
		return propertie;
	}

	public static PropertyAffordance createPositionProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(CURRENTLEVEL);
		DataSchema propertieObject = levelPropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createBatteryProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createBatteryProperty(href, propertyName,
				"Current charging level of the battery");
		return propertie;
	}

	public static PropertyAffordance createBatteryProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(STATEOFCHARGE);
		DataSchema propertieObject = batteryPropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createSlatAngleProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createSlatAngleProperty(href, propertyName,
				"Current value of the slat position");
		return propertie;
	}

	public static PropertyAffordance createSlatAngleProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(CURRENTLEVEL);
		DataSchema propertieObject = levelPropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createHumidityProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(HUMIDITY);
		DataSchema propertieObject = humidityPropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createHumidityProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createHumidityProperty(href, propertyName, "Current value of humidity");
		return propertie;
	}

	public static PropertyAffordance createIlluminanceProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(ILLUMINANCE);
		DataSchema propertieObject = illuminancePropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createIlluminanceProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createIlluminanceProperty(href, propertyName, "Current value of the brightness");
		return propertie;
	}

	public static PropertyAffordance createLockProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(SWITCHSTATUS);
		DataSchema propertieObject = statusPropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);

	}

	public static PropertyAffordance createLockProperty(String href, String propertyName) throws MalformedURLException {
		PropertyAffordance propertie = createLockProperty(href, propertyName, "Current state of the lock status");
		return propertie;
	}

	public static PropertyAffordance createSwitchStatusProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(SWITCHSTATUS);
		DataSchema propertieObject = onOffPropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);

	}

	public static PropertyAffordance createSwitchStatusProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createSwitchStatusProperty(href, propertyName,
				"Current value of the switch status");
		return propertie;
	}

	public static PropertyAffordance createMotionProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(MOTIONTYPE);
		DataSchema propertieObject = motionPropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createMotionProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createMotionProperty(href, propertyName, "Current value of the motion status");
		return propertie;
	}

	public static PropertyAffordance createOpenCloseProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(SWITCHSTATUS);
		DataSchema propertieObject = openClosePropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createOpenCloseProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createOpenCloseProperty(href, propertyName,
				"Current value of the switch status");
		return propertie;
	}

	public static PropertyAffordance createUpDownProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(SWITCHSTATUS);
		DataSchema propertieObject = upDownPropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createUpDownroperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createUpDownProperty(href, propertyName, "Current value of the switch status");
		return propertie;
	}

	public static PropertyAffordance createDimmingProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(CURRENTDIMMER);
		DataSchema propertieObject = dimmerPropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createDimmingProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createDimmingProperty(href, propertyName, "Current dimming value");
		return propertie;
	}

	public static PropertyAffordance createActiveEnergyImportedProperty(String href, String propertyName,
			String description) throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(ACTIVEENERGYIMPORTED);
		DataSchema propertieObject = activeEnergyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createActiveEnergyImportedProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createActiveEnergyImportedProperty(href, propertyName,
				"The imported electrical energy");
		return propertie;
	}

	public static PropertyAffordance createActiveEnergyExportedProperty(String href, String propertyName,
			String description) throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(ACTIVEENERGYEXPORTED);
		DataSchema propertieObject = activeEnergyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createActiveEnergyExportedProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createActiveEnergyExportedProperty(href, propertyName,
				"The exported electrical energy");
		return propertie;
	}

	public static PropertyAffordance createReactiveEnergyImportedProperty(String href, String propertyName,
			String description) throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(REACTIVEENERGYIMPORTED);
		DataSchema propertieObject = reactiveEnergyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createReactiveEnergyImportedProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createReactiveEnergyImportedProperty(href, propertyName,
				"The imported electrical energy");
		return propertie;
	}

	public static PropertyAffordance createReactiveEnergyExportedProperty(String href, String propertyName,
			String description) throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(REACTIVEENERGYEXPORTED);
		DataSchema propertieObject = reactiveEnergyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createReactiveEnergyExportedProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createReactiveEnergyExportedProperty(href, propertyName,
				"The exported electrical energy");
		return propertie;
	}

	public static PropertyAffordance createApparentEnergyProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(APPARENTENERGY);
		DataSchema propertieObject = apparentEnergyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createApparentEnergyProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createApparentEnergyProperty(href, propertyName,
				"The exported electrical energy");
		return propertie;
	}

	public static PropertyAffordance createGeneralElectricityProperty(String href, String propertyName,
			String description) throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(ELECTRICITY);
		DataSchema propertieObject = apparentEnergyObject(propertyName); 
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createGeneralEnergyProperty(String href, String propertyName, String description,
			String unit) throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(ENERGY);
		DataSchema propertieObject = generalEnergyObject(propertyName, unit);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createGeneralPowerProperty(String href, String propertyName, String description,
			String unit) throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(POWER);
		DataSchema propertieObject = generalPowerObject(propertyName, unit);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createVoltageProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(null); 
		DataSchema propertieObject = voltageObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createCurrentProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(AVERAGEELECTRICCURRENT);
		types.add(ELECTRICCURRENTA); 
		types.add(ELECTRICCURRENTB);
		types.add(ELECTRICCURRENTC);
		DataSchema propertieObject = electricCurrentObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createApparentPowerProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(TOTALAPPARENTPOWER);
		types.add(APPARENTPOWERA);
		types.add(APPARENTPOWERB);
		types.add(APPARENTPOWERC);
		DataSchema propertieObject = apparentPowerObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createTotalPowerFactorProperty(String href, String propertyName,
			String description) throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(TOTALPOWERFACTOR);
		DataSchema propertieObject = powerFactorObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createActivePowerCustomProperty(String href, String propertyName,
			String description, URI uri) throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(uri);
		DataSchema propertieObject = activePowerObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createTotalActivePowerProperty(String href, String propertyName,
			String description) throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(TOTALACTIVEPOWER);
		DataSchema propertieObject = activePowerObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createTotalActivePowerProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createTotalActivePowerProperty(href, propertyName, "The total active power");
		return propertie;
	}

	public static PropertyAffordance createAngleProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(VALUE);
		DataSchema propertieObject = anglePropertyObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createActivePowerAProperty(String href, String propertyName, String description) throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(ACTIVEPOWERA);
		DataSchema propertieObject = activePowerObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createActivePowerAProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createActivePowerAProperty(href, propertyName, "The active power for phase a");
		return propertie;
	}

	public static PropertyAffordance createActivePowerBProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(ACTIVEPOWERB);
		DataSchema propertieObject = activePowerObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createActivePowerBPropertie(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createActivePowerBProperty(href, propertyName, "The active power for phase b");
		return propertie;
	}

	public static PropertyAffordance createActivePowerCProperty(String href, String propertyName, String description) throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(ACTIVEPOWERC);
		DataSchema propertieObject = activePowerObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createActivePowerCPropertie(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createActivePowerCProperty(href, propertyName, "The active power for phase c");
		return propertie;
	}

	public static PropertyAffordance createLowBatteryProperty(String href, String propertyName, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(LOWBATTERY);
		DataSchema propertieObject = lowBatteryObject(propertyName);
		return createProperty(href, types, propertyName, description, propertieObject);
	}

	public static PropertyAffordance createLowBatteryProperty(String href, String propertyName)
			throws MalformedURLException {
		PropertyAffordance propertie = createLowBatteryProperty(href, propertyName, "The battery is low");
		return propertie;
	}

	private static PropertyAffordance createProperty(String href, ArrayList<URI> types, String propertyName,
			String description, DataSchema propertieObject) throws MalformedURLException {

//		Form mqtt = createMqttForm(href, propertyName);

		PropertyAffordance propertie = PropertyAffordance.builder().ds(propertieObject)
				.forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
						.href(URI.create(href + "/properties/" + propertyName)).contentType("application/json")
						.build()))
				// .subprotocol("https")
				.build();
		propertie.setAtType(types);
		propertie.setName(propertyName);
		propertie.setDescription(description);
		propertie.setReadOnly(true);
		return propertie;
	}

//	private static Form createMqttForm(String href, String String propertyName) {
//		Map<String, JsonElement> map = new HashMap<String, JsonElement>();
//		map.put("mqv:controlPacketValue", new JsonParser().parse("SUBSCRIBE"));
//		
//		String host = URI.create(href).getHost();
//		Form mqtt = Form.builder().op(Arrays.asList(Op.readproperty)).href(URI.create("mqtts://" + host + ":5671" +  "/things/properties/" + propertyName + "/" + keyName))
//				.contentType("application/json").additionalProperties(map).build();
//		return mqtt;
//	}
}
