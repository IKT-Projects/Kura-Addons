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

import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.ACTIVEENERGYIMPORTED;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.ACTIVEPOWERA;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.ACTIVEPOWERB;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.ACTIVEPOWERC;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.AIRTEMPERATURE;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.CARBONDIOXIDECONCENTRATION;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.CURRENTDIMMER;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.CURRENTLEVEL;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.GEOCOORDINATES;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.HUMIDITY;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.ILLUMINANCE;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.LOWBATTERY;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.MOTIONTYPE;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.PROVIDESFLOWRATEDATA;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.STATEOFCHARGE;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.SWITCHSTATUS;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.TEMPERATURE;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.TIMESERIES;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.TOTALACTIVEPOWER;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.VALUE;
import static org.ict.kura.asset.creator.thing.util.IoTSchemaUris.WINDSTRENGTH;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.activeEnergyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.activePowerObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.batteryPropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.carbonDioxidePropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.dimmerPropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.flowRatePropertyObject;
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
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.pressurePropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.statusPropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.temperaturePropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.tvocPropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.upDownPropertyObject;
import static org.ict.kura.asset.creator.thing.util.PropertyObjects.windPropertyObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.ict.model.wot.constant.Op;
import org.ict.model.wot.core.ActionAffordance;
import org.ict.model.wot.core.PropertyAffordance;
import org.ict.model.wot.dataschema.ArraySchema;
import org.ict.model.wot.dataschema.DataSchema;
import org.ict.model.wot.dataschema.IntegerSchema;
import org.ict.model.wot.dataschema.ObjectSchema;
import org.ict.model.wot.hypermedia.Form;

public class InteractionHistory {

	public static ActionAffordance createTemperatureHistory(String href, String propertyId)
			throws MalformedURLException {
		String description = "The time series of the temperature";
		ActionAffordance action = createTemperatureHistory("temperature", href, propertyId, description);
		return action;
	}
	
	public static ActionAffordance createTemperatureHistory(String keyName, String href, String propertyId, String description)
			throws MalformedURLException {
		ObjectSchema object = temperaturePropertyObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(TEMPERATURE);
		addTypes.add(AIRTEMPERATURE);
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}

	public static ActionAffordance createWindHistory(String href, String propertyId)
			throws MalformedURLException {
		String keyName = "wind";
		String description = "The time series of the wind strength";
		ObjectSchema object = windPropertyObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(WINDSTRENGTH);
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createFlowRateHistory(String href, String propertyId)
			throws MalformedURLException {
		String keyName = "flowRate";
		String description = "The time series of the flow rate";
		ObjectSchema object = flowRatePropertyObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(PROVIDESFLOWRATEDATA);
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createVolumeTotalHistory(String href, String propertyId)
			throws MalformedURLException {
		String keyName = "volumeTotal";
		String description = "The time series of the total volume";
		ObjectSchema object = measuredTotalPropertyObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(VALUE);
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createLocationLengthHistory(String href, String propertyId)
			throws MalformedURLException {
		String keyName = "locationLength";
		String description = "The time series of the longitude of the location";
		ObjectSchema object = locationLengthPropertyObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(GEOCOORDINATES);
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createLocationWidthHistory(String href, String propertyId)
			throws MalformedURLException {
		String keyName = "locationWidth";
		String description = "The time series of the latitude of the location";
		ObjectSchema object = locationWidthPropertyObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(GEOCOORDINATES);
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createOnOffHistory(String href, String propertyId) throws MalformedURLException {
		String keyName = "onOff";
		ObjectSchema object = onOffPropertyObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(SWITCHSTATUS);
		String description = "The time series of the switch status";
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createOnOffHistory(String keyName, String href, String propertyId) throws MalformedURLException {
		ObjectSchema object = onOffPropertyObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(SWITCHSTATUS);
		String description = "The time series of the switch status";
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createOpenCloseHistory(String href, String propertyId, String description)
			throws MalformedURLException {
		String keyName = "openClose";
		ObjectSchema object = openClosePropertyObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(SWITCHSTATUS);
		return createHistory(href, propertyId, keyName, object, addTypes, description);
	}
	
	public static ActionAffordance createOpenCloseHistory(String href, String propertyId)
			throws MalformedURLException {
		ActionAffordance action = createOpenCloseHistory(href, propertyId,
				"The time series of the switch status");
		return action;
	}
	
	public static ActionAffordance createUpDownHistory(String href, String propertyId) throws MalformedURLException {
		String keyName = "upDown";
		ObjectSchema object = upDownPropertyObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(SWITCHSTATUS);
		String description = "The time series of the switch status";
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createPressureHistory(String href, String propertyId) throws MalformedURLException {
		String keyName = "pressure";
		ObjectSchema object = pressurePropertyObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(CURRENTLEVEL);
		String description = "The time series of the pressure";
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createCarbonDioxideHistory(String href, String propertyId) throws MalformedURLException {
		String keyName = "CO2";
		ObjectSchema object = carbonDioxidePropertyObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(CARBONDIOXIDECONCENTRATION);
		String description = "The time series of the carbon dioxide concentration";
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createTvocHistory(String href, String propertyId) throws MalformedURLException {
		String keyName = "TVOC";
		ObjectSchema object = tvocPropertyObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(CURRENTLEVEL);
		String description = "The time series of the tvoc";
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createPositionHistory(String href, String propertyId) throws MalformedURLException {
		String keyName = "position";
		ObjectSchema object = levelPropertyObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(CURRENTLEVEL);
		String description = "The time series of the position";
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createSlatAngleHistory(String href, String propertyId) throws MalformedURLException {
		String keyName = "slatAngle";
		ObjectSchema object = levelPropertyObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(CURRENTLEVEL);
		String description = "The time series of the position";
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}

	public static ActionAffordance createHumidityHistory(String href, String propertyId) throws MalformedURLException {
		String keyName = "humidity";
		ObjectSchema object = humidityPropertyObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(HUMIDITY);
		String description = "The time series of the humidity";
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createIlluminanceHistory(String href, String propertyId) throws MalformedURLException {
		String keyName = "brigthness";
		ObjectSchema object = illuminancePropertyObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(ILLUMINANCE);
		String description = "The time series of the brigthness";
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createMotionHistory(String href, String propertyId) throws MalformedURLException {
		String keyName = "motionDetected";
		ObjectSchema object = motionPropertyObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(MOTIONTYPE);
		String description = "The time series of the motion status";
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createDimmingHistory(String href, String propertyId)
			throws MalformedURLException {
		String keyName = "dimming";
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(CURRENTDIMMER);
		String description = "The time series of the dimming value";
		ObjectSchema object = dimmerPropertyObject(keyName);
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createLowBatteryHistory(String href, String propertyId) throws MalformedURLException {
		String keyName = "batteryLow";
		ObjectSchema object = lowBatteryObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(LOWBATTERY);
		String description = "The time series of the low battery status)";
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createBatteryHistory(String href, String propertyId) throws MalformedURLException {
		String keyName = "battery";
		ObjectSchema object = batteryPropertyObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(STATEOFCHARGE);
		String description = "The time series of the battery charge status)";
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createLockHistory(String href, String propertyId) throws MalformedURLException {
		String keyName = "lock";
		ObjectSchema object = statusPropertyObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(SWITCHSTATUS);
		String description = "The time series of the lock status)";
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createActiveEnergieImportedHistory(String href, String propertyId)
			throws MalformedURLException {
		String keyName = "energy";
		ObjectSchema object = activeEnergyObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(ACTIVEENERGYIMPORTED);
		String description = "The time series of the imported electrical energy";
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createTotalActivePowerHistory(String href, String propertyId)
			throws MalformedURLException {
		String keyName = "power";
		ObjectSchema object  = activePowerObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(TOTALACTIVEPOWER);
		String description = "The time series of the total active power";
		
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}

	public static ActionAffordance createActivePowerAHistory(String href, String propertyId)
			throws MalformedURLException {
		String keyName = "powerA";
		ObjectSchema object = activePowerObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(ACTIVEPOWERA);
		addTypes.add(TIMESERIES);
		String description = "The time series of the active power for phase a";
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createActivePowerBPropertieHistory(String href, String propertyId)
			throws MalformedURLException {
		String keyName = "powerB";
		ObjectSchema object = activePowerObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(ACTIVEPOWERB);
		addTypes.add(TIMESERIES);
		String description = "The time series of the active power for phase b";
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createActivePowerCHistory(String href, String propertyId)
			throws MalformedURLException {
		String keyName = "powerC";
		ObjectSchema object = activePowerObject(keyName);
		ArrayList<URI> addTypes = new ArrayList<>();
		addTypes.add(ACTIVEPOWERC);
		addTypes.add(TIMESERIES);
		String description = "The time series of the active power for phase c";
		ActionAffordance action = createHistory(href, propertyId, keyName, object, addTypes, description);
		return action;
	}
	
	public static ActionAffordance createHistory(String href, String propertyId, String keyName, ObjectSchema object,
			ArrayList<URI> addTypes, String description) throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.addAll(addTypes);
		types.add(TIMESERIES);

		Map<String, DataSchema> params = new HashMap<String, DataSchema>();
		params.put("start",
				IntegerSchema.builder().description("Start time in seconds (timestamp)")
						.atType(Arrays.asList(URI.create("https://saref.etsi.org/saref4ener/StartTime")))
						.unit("om:second-Time").minimum(0)
						.maximum(Integer.MAX_VALUE).build());
		params.put("end",
				IntegerSchema.builder().description("End time in seconds (timestamp)")
						.atType(Arrays.asList(URI.create("https://saref.etsi.org/saref4ener/EndTime")))
						.unit("om:second-Time").minimum(0)
						.maximum(Integer.MAX_VALUE).build());
		

		ActionAffordance action = ActionAffordance.builder().description(description).atType(types)
				.input(ObjectSchema.builder().properties(params).atType(Arrays.asList(TIMESERIES)).required(Arrays.asList("start", "end")).build())
				.output(ArraySchema.builder().items(Arrays.asList(PropertyAffordance.builder().ds(object).build())).minItems(0L).maxItems(100000L)
						.build())
				.forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.invokeaction))
						.href(URI.create(href + "/actions/" + propertyId + "/" + keyName + "/timeseries"))
						.contentType("application/json").build()))
				//.subprotocol("https")
				.build();
		return action;
	}
}
