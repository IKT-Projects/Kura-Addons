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
package org.ict.kura.internal.driver.avm.things;

import static org.ict.kura.internal.driver.avm.things.util.InteractionActions.createOnOffAction;
import static org.ict.kura.internal.driver.avm.things.util.InteractionProperties.createActiveEnergyImportedProperty;
import static org.ict.kura.internal.driver.avm.things.util.InteractionProperties.createSwitchStatusProperty;
import static org.ict.kura.internal.driver.avm.things.util.InteractionProperties.createTemperatureProperty;
import static org.ict.kura.internal.driver.avm.things.util.InteractionProperties.createTotalActivePowerProperty;
import static org.ict.model.wot.constant.In.Header;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ict.model.jsonld.context.Context;
import org.ict.model.wot.add.hardware.HardwareRef;
import org.ict.model.wot.constant.Op;
import org.ict.model.wot.constant.SecuritySchemaType;
import org.ict.model.wot.core.ActionAffordance;
import org.ict.model.wot.core.PropertyAffordance;
import org.ict.model.wot.core.Thing;
import org.ict.model.wot.core.VersionInfo;
import org.ict.model.wot.hypermedia.AdditionalProperty;
import org.ict.model.wot.hypermedia.Form;
import org.ict.model.wot.security.BasicSecurityScheme;
import org.ict.model.wot.security.SecurityScheme;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ThingDect2XX {
//	private static final Gson gson = AdapterFactory.getGsonWithDefaultWotTypeAdapters();
	private Thing thing;

	public ThingDect2XX(String href, String thingID, String titel, String description, String ain)
			throws MalformedURLException {
		this.thing = createThing(href, thingID, titel, description, ain);
	}

	private Thing createThing(String href, String thingID, String titel, String description, String ain)
			throws MalformedURLException {

		Context[] contexts = new Context[] {
				Context.builder().prefix(null).namespace("https://www.w3.org/2019/wot/td/v1").build(),
				Context.builder().prefix("schema").namespace("http://schema.org/").build(),
				Context.builder().prefix("iot").namespace("http://iotschema.org/").build(),
				Context.builder().prefix("http").namespace("http://iotschema.org/protocol/http").build(),
				Context.builder().prefix("saref4ener").namespace("https://saref.etsi.org/saref4ener/").build(),
				Context.builder().prefix("om").namespace("http://www.ontology-of-units-of-measure.org/resource/om-2/")
						.build() };

		URI uri = URI.create(href + "/things/" + thingID);
		Map<String, SecurityScheme> secDef = new HashMap<>();
		secDef.put("basic_sc", BasicSecurityScheme.builder().scheme(SecuritySchemaType.Basic).in(Header).build());
		List<String> security = new ArrayList<>();
		security.add("basic_sc");

		List<AdditionalProperty> bindings = new ArrayList<>();
		bindings.add(createBinding(ain));
		bindings.add(new AdditionalProperty("binding", new JsonParser().parse("avm")));

		/* create properties */

		Map<String, PropertyAffordance> properties = new HashMap<>();
		PropertyAffordance energy = createActiveEnergyImportedProperty(uri.toString(), "energy");
		energy.getForms().get(0).setAdditionalProperties(bindings);
		properties.put("energy", energy);

		PropertyAffordance power = createTotalActivePowerProperty(uri.toString(), "power");
		power.getForms().get(0).setAdditionalProperties(bindings);
		properties.put("power", power);

		PropertyAffordance temp = createTemperatureProperty(uri.toString(), "temperature");
		temp.getForms().get(0).setAdditionalProperties(bindings);
		properties.put("temperature", temp);

		PropertyAffordance switchStatus = createSwitchStatusProperty(uri.toString(), "onOff");
		switchStatus.getForms().get(0).setAdditionalProperties(bindings);
		properties.put("onOff", switchStatus);

		/* actions */
		Map<String, ActionAffordance> actions = new HashMap<>();

		ActionAffordance onOff = createOnOffAction(uri.toString(), "onOff");
		onOff.getForms().get(0).setAdditionalProperties(bindings);
		actions.put("onOff", onOff);


		/* Create Thing */
		return Thing.builder().contexts(contexts).atId(uri).id(uri).title(titel).description(description)
				.version(VersionInfo.builder().instance("0.1").build()).created(new Date()).modified(new Date())
				.support(URI.create("https://www.fh-dortmund.de/de/fb/10/ikt/index.php")).base(URI.create(href))
				.atType(Arrays.asList(URI.create("iot:Actuator"), URI.create("iot:BinarySwitchControl"),
						URI.create("iot:Sensor"), URI.create("iot:PowerMonitoring"),
						URI.create("iot:TemperatureSensing"), URI.create("iot:EnergyMonitoring")))
				.securityDefinitions(secDef).security(security).properties(properties).actions(actions)
				.hardware(HardwareRef.builder()
						.href(Form.builder().op(Arrays.asList(Op.readproperty))
								.href(URI.create("https://avm.de/produkte/fritzdect/")).contentType("application/json")
								.subprotocol("https").build())
						.build())
				.build();

	}

	public Thing getThing() {
		return thing;
	}

	private AdditionalProperty createBinding(String ain) {
		JsonObject obj = new JsonObject();
		obj.addProperty("ain", ain);

		AdditionalProperty prop = new AdditionalProperty("avm", obj);

		return prop;
	}

}
