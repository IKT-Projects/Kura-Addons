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
package org.ict.kura.internal.driver.avm.things.util;

import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.ACTIVEENERGYEXPORTED;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.ACTIVEENERGYIMPORTED;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.ACTIVEPOWERA;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.ACTIVEPOWERB;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.ACTIVEPOWERC;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.AIRTEMPERATURE;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.APPARENTENERGY;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.APPARENTPOWERA;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.APPARENTPOWERB;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.APPARENTPOWERC;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.AVERAGEELECTRICCURRENT;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.CARBONDIOXIDECONCENTRATION;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.CURRENTDIMMER;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.CURRENTLEVEL;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.ELECTRICCURRENTA;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.ELECTRICCURRENTB;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.ELECTRICCURRENTC;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.ELECTRICITY;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.ENERGY;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.GEOCOORDINATES;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.HUMIDITY;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.ILLUMINANCE;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.LOWBATTERY;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.MOTIONTYPE;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.MULTILEVEL_STATE;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.POWER;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.PROVIDESFLOWRATEDATA;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.REACTIVEENERGYEXPORTED;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.REACTIVEENERGYIMPORTED;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.STATEOFCHARGE;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.SWITCHSTATUS;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.TEMPERATURE;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.TIMESERIES;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.TOTALACTIVEPOWER;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.TOTALAPPARENTPOWER;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.TOTALPOWERFACTOR;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.VALUE;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.WINDSTRENGTH;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.activeEnergyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.activePowerObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.anglePropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.apparentEnergyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.apparentPowerObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.batteryPropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.carbonDioxidePropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.dimmerPropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.electricCurrentObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.flowRatePropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.generalEnergyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.generalPowerObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.generalValuePropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.humidityPropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.illuminancePropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.levelPropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.locationLengthPropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.locationWidthPropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.lowBatteryObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.measuredTotalPropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.motionPropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.onOffPropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.openClosePropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.powerFactorObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.pressurePropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.reactiveEnergyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.statusPropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.temperaturePropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.timePropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.tvocPropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.upDownPropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.voltageObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.windPropertyObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import org.ict.model.wot.constant.Op;
import org.ict.model.wot.core.PropertyAffordance;
import org.ict.model.wot.dataschema.DataSchema;
import org.ict.model.wot.hypermedia.Form;


public class InteractionProperties {
	
	public static PropertyAffordance createTimeProperty(String href, String propertyId, String description, String keyName, String unit)
	          throws MalformedURLException {
			ArrayList<URI> types = new ArrayList<>();
			types.add(TIMESERIES);
			DataSchema propertieObject = timePropertyObject(keyName, unit);
			return createProperty(href, types, keyName, propertyId, description, propertieObject);
		}

	public static PropertyAffordance createTemperatureProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createTemperatureProperty(href, propertyId, "Current value of temperature", propertyId);
		return propertie;
	}

	public static PropertyAffordance createTemperatureProperty(String href, String propertyId, String description, String keyName)
          throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(TEMPERATURE);
		types.add(AIRTEMPERATURE);
		DataSchema propertieObject = temperaturePropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createTemperatureProperty(String href, String propertyId, String description, String keyName, String unit)
	          throws MalformedURLException {
			ArrayList<URI> types = new ArrayList<>();
			types.add(TEMPERATURE);
			types.add(AIRTEMPERATURE);
			DataSchema propertieObject = temperaturePropertyObject(propertyId, unit);
			return createProperty(href, types, keyName, propertyId, description, propertieObject);
		}
	
	
	public static PropertyAffordance createPressureProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createPressureProperty(href, propertyId, "Current value of pressure");
		return propertie;
	}

	public static PropertyAffordance createPressureProperty(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(CURRENTLEVEL);
		String keyName = "pressure";
		DataSchema propertieObject = pressurePropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createLevelCustomProperty(String href, String propertyId, String description, String keyName)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(CURRENTLEVEL);
		DataSchema propertieObject = levelPropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createMutliLevelStateProperty(String href, String propertyId, String description, String keyName)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(MULTILEVEL_STATE);
		DataSchema propertieObject = generalValuePropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createFlowRateProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createFlowRateProperty(href, propertyId, "Current value of flow rate");
		return propertie;
	}

	public static PropertyAffordance createFlowRateProperty(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(PROVIDESFLOWRATEDATA);
		String keyName = "flowRate";
		DataSchema propertieObject = flowRatePropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createFlowRateProperty(String href, String propertyId, String description, String keyName, String unit)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(PROVIDESFLOWRATEDATA);
		DataSchema propertieObject = flowRatePropertyObject(keyName, unit);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	
	public static PropertyAffordance createVolumeTotalProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createVolumeTotalProperty(href, propertyId, "The total value of flow");
		return propertie;
	}

	public static PropertyAffordance createVolumeTotalProperty(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(VALUE);
		String keyName = "volumeTotal";
		DataSchema propertieObject = measuredTotalPropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createMeasuredTotalProperty(String href, String propertyId, String description, String keyName, String unit)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(VALUE);
		DataSchema propertieObject = measuredTotalPropertyObject(keyName, unit);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createLocationLengthProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createLocationLengthProperty(href, propertyId, "Current value of longitude");
		return propertie;
	}

	public static PropertyAffordance createLocationLengthProperty(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(GEOCOORDINATES);
		String keyName = "locationLength";
		DataSchema propertieObject = locationLengthPropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createLocationWidthProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createLocationWidthProperty(href, propertyId, "Current value of latitude");
		return propertie;
	}

	public static PropertyAffordance createLocationWidthProperty(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(GEOCOORDINATES);
		String keyName = "locationWidth";
		DataSchema propertieObject = locationWidthPropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createCarbonDioxideProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createCarbonDioxideProperty(href, propertyId, "Current value of carbon dioxide concentration");
		return propertie;
	}

	public static PropertyAffordance createCarbonDioxideProperty(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(CARBONDIOXIDECONCENTRATION);
		String keyName = "CO2";
		DataSchema propertieObject = carbonDioxidePropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createTvocProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createTvocProperty(href, propertyId, "Current value of tvoc");
		return propertie;
	}

	public static PropertyAffordance createTvocProperty(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(CURRENTLEVEL);
		String keyName = "TVOC";
		DataSchema propertieObject = tvocPropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createWindProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createWindProperty(href, propertyId, "Current wind strength");
		return propertie;
	}

	public static PropertyAffordance createWindProperty(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(WINDSTRENGTH);
		String keyName = "wind";
		DataSchema propertieObject = windPropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	

	public static PropertyAffordance createPositionProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createPositionProperty(href, propertyId,
				"Current value of the shutter position");
		return propertie;
	}

	public static PropertyAffordance createPositionProperty(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(CURRENTLEVEL);
		String keyName = "position";
		DataSchema propertieObject = levelPropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createBatteryProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createBatteryProperty(href, propertyId,
				"Current charging level of the battery");
		return propertie;
	}

	public static PropertyAffordance createBatteryProperty(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(STATEOFCHARGE);
		String keyName = "battery";
		DataSchema propertieObject = batteryPropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createSlatAngleProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createSlatAngleProperty(href, propertyId,
				"Current value of the slat position");
		return propertie;
	}

	public static PropertyAffordance createSlatAngleProperty(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(CURRENTLEVEL);
		String keyName = "slatAngle";
		DataSchema propertieObject = levelPropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}

	public static PropertyAffordance createHumidityProperty(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(HUMIDITY);
		String keyName = "humidity";
		DataSchema propertieObject = humidityPropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}

	public static PropertyAffordance createHumidityProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createHumidityProperty(href, propertyId, "Current value of humidity");
		return propertie;
	}

	public static PropertyAffordance createIlluminanceProperty(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(ILLUMINANCE);
		String keyName = "brightness";
		DataSchema propertieObject = illuminancePropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}

	public static PropertyAffordance createIlluminanceProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createIlluminanceProperty(href, propertyId, "Current value of the brightness");
		return propertie;
	}
	
	public static PropertyAffordance createLockProperty(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(SWITCHSTATUS);
		String keyName = "lock";
		DataSchema propertieObject = statusPropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);

	}

	public static PropertyAffordance createLockProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createLockProperty(href, propertyId,
				"Current state of the lock status");
		return propertie;
	}

	public static PropertyAffordance createSwitchStatusProperty(String keyName, String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(SWITCHSTATUS);
		DataSchema propertieObject = onOffPropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);

	}

	public static PropertyAffordance createSwitchStatusProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createSwitchStatusProperty("onOff", href, propertyId,
				"Current value of the switch status");
		return propertie;
	}

	public static PropertyAffordance createMotionProperty(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(MOTIONTYPE);
		String keyName = "motionDetected";
		DataSchema propertieObject = motionPropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createMotionProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createMotionProperty(href, propertyId,
				"Current value of the motion status");
		return propertie;
	}

	public static PropertyAffordance createOpenCloseProperty(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(SWITCHSTATUS);
		String keyName = "openClose";
		DataSchema propertieObject = openClosePropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createOpenCloseProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createOpenCloseProperty(href, propertyId,
				"Current value of the switch status");
		return propertie;
	}

	public static PropertyAffordance createUpDownroperty(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(SWITCHSTATUS);
		String keyName = "upDown";
		DataSchema propertieObject = upDownPropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createUpDownroperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createUpDownroperty(href, propertyId,
				"Current value of the switch status");
		return propertie;
	}

	public static PropertyAffordance createDimmingProperty(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(CURRENTDIMMER);
		String keyName = "dimming";
		DataSchema propertieObject = dimmerPropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}

	public static PropertyAffordance createDimmingProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createDimmingProperty(href, propertyId, "Current dimming value");
		return propertie;
	}

	public static PropertyAffordance createActiveEnergyImportedProperty(String href, String propertyId, String description, String keyName)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(ACTIVEENERGYIMPORTED);
		DataSchema propertieObject = activeEnergyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createActiveEnergyImportedProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createActiveEnergyImportedProperty(href, propertyId, "The imported electrical energy", "energy");
		return propertie;
	}

	public static PropertyAffordance createActiveEnergyExportedProperty(String href, String propertyId, String description, String keyName)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(ACTIVEENERGYEXPORTED);
		DataSchema propertieObject = activeEnergyObject(propertyId);
		return createProperty(href, types, keyName, propertyId,  description, propertieObject);
	}
	
	public static PropertyAffordance createActiveEnergyExportedProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createActiveEnergyExportedProperty(href, propertyId, "The exported electrical energy", "energy");
		return propertie;
	}
	
	public static PropertyAffordance createReactiveEnergyImportedProperty(String href, String propertyId, String description, String keyName)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(REACTIVEENERGYIMPORTED);
		DataSchema propertieObject = reactiveEnergyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createReactiveEnergyImportedProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createReactiveEnergyImportedProperty(href, propertyId, "The imported electrical energy", "energy");
		return propertie;
	}
	
	public static PropertyAffordance createReactiveEnergyExportedProperty(String href, String propertyId, String description, String keyName)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(REACTIVEENERGYEXPORTED);
		DataSchema propertieObject = reactiveEnergyObject(propertyId);
		return createProperty(href, types, keyName, propertyId,  description, propertieObject);
	}
	
	public static PropertyAffordance createReactiveEnergyExportedProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createReactiveEnergyExportedProperty(href, propertyId, "The exported electrical energy", "energy");
		return propertie;
	}
	
	public static PropertyAffordance createApparentEnergyProperty(String href, String propertyId, String description, String keyName)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(APPARENTENERGY);
		DataSchema propertieObject = apparentEnergyObject(propertyId);
		return createProperty(href, types, keyName, propertyId,  description, propertieObject);
	}
	
	public static PropertyAffordance createApparentEnergyProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createApparentEnergyProperty(href, propertyId, "The exported electrical energy", "energy");
		return propertie;
	}
	
	public static PropertyAffordance createGeneralElectricityProperty(String href, String propertyId, String description, String keyName)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(ELECTRICITY);
		DataSchema propertieObject = apparentEnergyObject(propertyId); 
		return createProperty(href, types, keyName, propertyId,  description, propertieObject);
	}
	
	public static PropertyAffordance createGeneralEnergyProperty(String href, String propertyId, String description, String keyName, String unit)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(ENERGY);
		DataSchema propertieObject = generalEnergyObject(keyName, unit);
		return createProperty(href, types, keyName, propertyId,  description, propertieObject);
	}
	
	public static PropertyAffordance createGeneralPowerProperty(String href, String propertyId, String description, String keyName, String unit)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(POWER);
		DataSchema propertieObject = generalPowerObject(keyName, unit);
		return createProperty(href, types, keyName, propertyId,  description, propertieObject);
	}
	
	public static PropertyAffordance createVoltageProperty(String href, String propertyId, String description, String keyName)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(null);
		DataSchema propertieObject = voltageObject(propertyId);
		return createProperty(href, types, keyName, propertyId,  description, propertieObject);
	}
	
	public static PropertyAffordance createCurrentProperty(String href, String propertyId, String description, String keyName)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(AVERAGEELECTRICCURRENT);
		types.add(ELECTRICCURRENTA);
		types.add(ELECTRICCURRENTB);
		types.add(ELECTRICCURRENTC);
		DataSchema propertieObject = electricCurrentObject(propertyId);
		return createProperty(href, types, keyName, propertyId,  description, propertieObject);
	}
	
	public static PropertyAffordance createApparentPowerProperty(String href, String propertyId, String description, String keyName)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(TOTALAPPARENTPOWER);
		types.add(APPARENTPOWERA); 
		types.add(APPARENTPOWERB);
		types.add(APPARENTPOWERC);
		DataSchema propertieObject = apparentPowerObject(propertyId);
		return createProperty(href, types, keyName, propertyId,  description, propertieObject);
	}
	
	public static PropertyAffordance createTotalPowerFactorProperty(String href, String propertyId, String description, String keyName)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(TOTALPOWERFACTOR);
		DataSchema propertieObject = powerFactorObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createActivePowerCustomProperty(String href, String propertyId, String description, String keyName, URI uri)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(uri);
		DataSchema propertieObject = activePowerObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createTotalActivePowerProperty(String href, String propertyId, String description, String keyName)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(TOTALACTIVEPOWER);
		DataSchema propertieObject = activePowerObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createTotalActivePowerProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createTotalActivePowerProperty(href, propertyId, "The total active power", "power");
		return propertie;
	}

	public static PropertyAffordance createAngleProperty(String href, String propertyId, String description, String keyName)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(VALUE);
		DataSchema propertieObject = anglePropertyObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createActivePowerAProperty(String href, String propertyId, String description, String keyName)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(ACTIVEPOWERA);
		DataSchema propertieObject = activePowerObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createActivePowerAProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createActivePowerAProperty(href, propertyId, "The active power for phase a", "powerA");
		return propertie;
	}

	public static PropertyAffordance createActivePowerBProperty(String href, String propertyId, String description, String keyName)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(ACTIVEPOWERB);
		DataSchema propertieObject = activePowerObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createActivePowerBPropertie(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createActivePowerBProperty(href, propertyId, "The active power for phase b", "powerB");
		return propertie;
	}
	
	public static PropertyAffordance createActivePowerCProperty(String href, String propertyId, String description, String keyName)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(ACTIVEPOWERC);
		DataSchema propertieObject = activePowerObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createActivePowerCPropertie(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createActivePowerCProperty(href, propertyId, "The active power for phase c", "powerC");
		return propertie;
	}
	
	public static PropertyAffordance createLowBatteryProperty(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(LOWBATTERY);
		String keyName = "batteryLow";
		DataSchema propertieObject = lowBatteryObject(propertyId);
		return createProperty(href, types, keyName, propertyId, description, propertieObject);
	}
	
	public static PropertyAffordance createLowBatteryProperty(String href, String propertyId)
			throws MalformedURLException {
		PropertyAffordance propertie = createLowBatteryProperty(href, propertyId, "The battery is low");
		return propertie;
	}
	
	private static PropertyAffordance createProperty(String href, ArrayList<URI> types, String keyName,
			String propertyId, String description, DataSchema propertieObject) throws MalformedURLException {
		
//		Form mqtt = createMqttForm(href, keyName, propertyId);
		
		PropertyAffordance propertie = PropertyAffordance.builder().ds(propertieObject)
				.forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
						.href(URI.create(href + "/properties/" + propertyId))
						.contentType("application/json").build()))
				//.subprotocol("https")
				.build();
		propertie.setAtType(types);
		propertie.setDescription(description);
		propertie.setReadOnly(true);
		return propertie;
	}
	
//	private static Form createMqttForm(String href, String keyName, String propertyId) {
//		Map<String, JsonElement> map = new HashMap<String, JsonElement>();
//		map.put("mqv:controlPacketValue", new JsonParser().parse("SUBSCRIBE"));
//		
//		String host = URI.create(href).getHost();
//		Form mqtt = Form.builder().op(Arrays.asList(Op.readproperty)).href(URI.create("mqtts://" + host + ":5671" +  "/things/properties/" + propertyId + "/" + keyName))
//				.contentType("application/json").additionalProperties(map).build();
//		return mqtt;
//	}
}
