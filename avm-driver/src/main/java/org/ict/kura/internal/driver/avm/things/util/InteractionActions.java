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

import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.SETLEVEL;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.TARGETTEMPERATURE;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.TURNOFF;
import static org.ict.kura.internal.driver.avm.things.util.IoTSchemaUris.TURNON;
import static org.ict.kura.internal.driver.avm.things.util.PropertyData.level;
import static org.ict.kura.internal.driver.avm.things.util.PropertyData.onOff;
import static org.ict.kura.internal.driver.avm.things.util.PropertyData.temperature;
import static org.ict.kura.internal.driver.avm.things.util.PropertyData.time;
import static org.ict.kura.internal.driver.avm.things.util.PropertyData.upDown;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.levelPropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.onOffPropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.temperaturePropertyObject;
import static org.ict.kura.internal.driver.avm.things.util.PropertyObjects.upDownPropertyObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.ict.model.wot.constant.Op;
import org.ict.model.wot.core.ActionAffordance;
import org.ict.model.wot.dataschema.DataSchema;
import org.ict.model.wot.dataschema.ObjectSchema;
import org.ict.model.wot.hypermedia.Form;

public class InteractionActions {
	public static ActionAffordance createOnOffAction(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(TURNOFF);
		types.add(TURNON);
		String keyName = "onOff";
		String title = "On/Off";
		Map<String, DataSchema> input = new HashMap<String, DataSchema>();
		input.put(keyName, onOff());
		ObjectSchema output = onOffPropertyObject(propertyId);
		return createAction(href, types, title, keyName, propertyId, description, input, output);
	}

	public static ActionAffordance createOnOffAction(String href, String propertyId) throws MalformedURLException {
		ActionAffordance action = createOnOffAction(href, propertyId, "The state to be set (true/false)");
		return action;
	}

	public static ActionAffordance createTempOffSetAction(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(TARGETTEMPERATURE);
		String keyName = "tempOffset";
		String title = "OffSet Temperature (Celsius)";
		Map<String, DataSchema> input = new HashMap<String, DataSchema>();
		input.put(keyName, temperature());
		ObjectSchema output = temperaturePropertyObject(propertyId);
		return createAction(href, types, title, keyName, propertyId, description, input, output);
	}

	public static ActionAffordance createTempOffSetAction(String href, String propertyId) throws MalformedURLException {
		ActionAffordance action = createTempOffSetAction(href, propertyId, "Offset value of temperature (Celsius)");
		return action;
	}
	
	public static ActionAffordance createTargetTempAction(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(TARGETTEMPERATURE);
		String keyName = "temperature";
		String title = "Temperature (Celsius)";
		Map<String, DataSchema> input = new HashMap<String, DataSchema>();
		input.put(keyName, temperature());
		ObjectSchema output = temperaturePropertyObject(propertyId);
		return createAction(href, types, title, keyName, propertyId, description, input, output);
	}

	public static ActionAffordance createTargetTempAction(String href, String propertyId) throws MalformedURLException {
		ActionAffordance action = createTargetTempAction(href, propertyId, "Target value of temperature (Celsius)");
		return action;
	}
	
	public static ActionAffordance createPositionAction(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(SETLEVEL);
		String keyName = "position";
		String title = "Position of the shutter";
		Map<String, DataSchema> input = new HashMap<String, DataSchema>();
		input.put(keyName, level(time(), title));
		ObjectSchema output = levelPropertyObject(propertyId);
		return createAction(href, types, title, keyName, propertyId, description, input, output);
	}

	public static ActionAffordance createPositionAction(String href, String propertyId) throws MalformedURLException {
		ActionAffordance action = createPositionAction(href, propertyId, "Set the shutter position");
		return action;
	}
	
	public static ActionAffordance createUpDownAction(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(TURNOFF);
		types.add(TURNON);
		String keyName = "upDown";
		String title = "upDown";
		Map<String, DataSchema> input = new HashMap<String, DataSchema>();
		input.put(keyName, upDown());
		ObjectSchema output = upDownPropertyObject(propertyId);
		return createAction(href, types, title, keyName, propertyId, description, input, output);
	}

	public static ActionAffordance createUpDownAction(String href, String propertyId) throws MalformedURLException {
		ActionAffordance action = createUpDownAction(href, propertyId, "The state to be set (true/false)");
		return action;
	}
	
	public static ActionAffordance createSlatAngleAction(String href, String propertyId, String description)
			throws MalformedURLException {
		ArrayList<URI> types = new ArrayList<>();
		types.add(SETLEVEL);
		String keyName = "slatAngle";
		String title = "slatAngle";
		Map<String, DataSchema> input = new HashMap<String, DataSchema>();
		input.put(keyName, level(time(), title));
		ObjectSchema output = levelPropertyObject(propertyId);
		return createAction(href, types, title, keyName, propertyId, description, input, output);
	}

	public static ActionAffordance createSlatAngleAction(String href, String propertyId) throws MalformedURLException {
		ActionAffordance action = createSlatAngleAction(href, propertyId, "Set the slat angle");
		return action;
	}

	private static ActionAffordance createAction(String href, ArrayList<URI> types, String title, String keyName,
			String propertyId, String description, Map<String, DataSchema> input, DataSchema output)
			throws MalformedURLException {
		
		ActionAffordance action = ActionAffordance.builder().title(title).description(description).atType(types)
				.input(ObjectSchema.builder().properties(input).build()).output(output)
				.forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.invokeaction))
						.href(URI.create(href + "/actions/" + propertyId))
						.contentType("application/json").build()))
//				.subprotocol("https")
				.build();
		return action;
	}
}