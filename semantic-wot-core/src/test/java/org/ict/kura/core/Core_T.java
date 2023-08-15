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
package org.ict.kura.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ict.gson.utils.AdapterFactory;
import org.ict.kura.asset.creator.thing.util.DefaultConfigReader;
import org.ict.kura.core.thing.creator.impl.ThingCreatorImpl;
import org.ict.kura.thing.creator.ActionProviderImpl;
import org.ict.kura.thing.creator.PropertyProviderImpl;
import org.ict.model.jsonld.context.Context;
import org.ict.model.wot.constant.Op;
import org.ict.model.wot.core.ActionAffordance;
import org.ict.model.wot.core.Thing;
import org.ict.model.wot.dataschema.IntegerSchema;
import org.ict.model.wot.dataschema.NumberSchema;
import org.ict.model.wot.hypermedia.AdditionalProperty;
import org.ict.model.wot.hypermedia.Form;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Core_T {
	private static final Logger LOGGER = LoggerFactory.getLogger(Core_T.class);
	private Thing default_thing;

	private Gson fac = AdapterFactory.getGsonWithDefaultWotTypeAdapters(true);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		var devid = "Default-Thing-1";
		var creater = new ThingCreatorImpl(URI.create("https://ikt-do.de"));
		this.default_thing = creater.create(devid, "Thing-1", "Thing-1 description",
				Arrays.asList(URI.create("iot:Actuator"), URI.create("iot:BinarySwitchControl"),
						URI.create("iot:Sensor"), URI.create("iot:TemperatureSensing")).toArray(new URI[0]));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testThingCreater() {
		try {
			var base = URI.create("https://ikt-do.de");
			var devid = "DECT200-1";
			var expectedId = URI.create(base.toString() + "/things/" + devid);
			var creater = new ThingCreatorImpl(URI.create("https://ikt-do.de"));
			var thing = creater.create(devid, "DECT 200", "AVM DECT 200",
					Arrays.asList(URI.create("iot:Actuator"), URI.create("iot:BinarySwitchControl"),
							URI.create("iot:Sensor"), URI.create("iot:PowerMonitoring"),
							URI.create("iot:TemperatureSensing"), URI.create("iot:EnergyMonitoring"))
							.toArray(new URI[0]));
			assertNotNull(thing);
			assertEquals(expectedId, thing.getId());

			var properties = Map.of("energy", PropertyProviderImpl.ActiveEnergyImported, "power",
					PropertyProviderImpl.TotalActivePower, "temperature", PropertyProviderImpl.Temperature, "onOff",
					PropertyProviderImpl.SwitchStatus);
			thing = creater.addPropertyTemplates(thing, properties);
			assertEquals(properties.size(), thing.getProperties().size());
			assertTrue(thing.getProperties().containsKey("energy"));
			assertTrue(thing.getProperties().containsKey("power"));
			assertTrue(thing.getProperties().containsKey("temperature"));
			assertTrue(thing.getProperties().containsKey("onOff"));

			var expectedEneryPropertyId = URI
					.create(expectedId.toString() + URI.create("/properties/energy").toString());
			assertEquals(expectedEneryPropertyId, thing.getProperties().get("energy").getForms().get(0).getHref());

			var actions = Map.of("onOff", ActionProviderImpl.OnOff, "targetTemperature",
					ActionProviderImpl.TargetTemperature);
			thing = creater.addActionTemplates(thing, actions);
			assertEquals(actions.size(), thing.getActions().size());
			assertTrue(thing.getActions().containsKey("onOff"));
			assertTrue(thing.getActions().containsKey("targetTemperature"));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testPropertyBindings() {
		try {
			var creater = new ThingCreatorImpl(URI.create("https://ikt-do.de"));
			var properties = Map.of("energy", PropertyProviderImpl.ActiveEnergyImported);
			var thing = creater.addPropertyTemplates(default_thing, properties);
			assertEquals(properties.size(), thing.getProperties().size());
			assertTrue(thing.getProperties().containsKey("energy"));

			// create and add the bindings
			var bindings = new ArrayList<AdditionalProperty>();
			var root = new JsonObject();
			root.addProperty("conf1", "value1");
			root.addProperty("conf2", "value2");
			bindings.add(new AdditionalProperty("binding", new JsonPrimitive("virtual")));
			bindings.add(new AdditionalProperty("virtual", root));
			thing = creater.addPropertyBinding(default_thing, "energy", bindings);
			assertNotNull(thing.getProperties().get("energy").getForms().get(0).getAdditionalProperties());
			assertEquals("{\"conf1\":\"value1\",\"conf2\":\"value2\"}", thing.getProperties().get("energy").getForms()
					.get(0).getAdditionalProperties().get(1).getElement().toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testActionBindings() {
		try {
			var creater = new ThingCreatorImpl(URI.create("https://ikt-do.de"));
			var action = ActionAffordance.builder().title("onOff").description("On/Off switch")
					.forms(List.of(Form.builder().op(Arrays.asList(Op.writeproperty))
							.href(URI.create("https://ikt-do.de" + "/actions/" + "onOff"))
							.contentType("application/json").build()))
					.build();
			var map = new HashMap<String, ActionAffordance>();
			map.put("onOff", action);
			var thing = creater.addActions(default_thing, map);
			assertEquals(map.size(), thing.getActions().size());
			assertTrue(thing.getActions().containsKey("onOff"));

			// create and add the bindings
			var bindings = new ArrayList<AdditionalProperty>();
			var root = new JsonObject();
			root.addProperty("conf1", "value1");
			root.addProperty("conf2", "value2");
			bindings.add(new AdditionalProperty("binding", new JsonPrimitive("virtual")));
			bindings.add(new AdditionalProperty("virtual", root));
			thing = creater.addActionBinding(thing, "onOff", bindings);
			assertNotNull(thing.getActions().get("onOff").getForms().get(0).getAdditionalProperties());
			assertEquals("{\"conf1\":\"value1\",\"conf2\":\"value2\"}", thing.getActions().get("onOff").getForms()
					.get(0).getAdditionalProperties().get(1).getElement().toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testAddContext() {
		try {
			var creater = new ThingCreatorImpl(URI.create("https://ikt-do.de"));
			var thing = creater.addContext(default_thing,
					Context.builder().prefix("saref").namespace("https://saref.etsi.org/core/").build());
			var contexts = thing.getContexts();
			boolean isContext = false;
			for (Context context : contexts) {
				if (context.getPrefix() != null && context.getPrefix().equals("saref")) {
					isContext = true;
					break;
				}
			}
			assertTrue(isContext);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testSetMinMax() {
		try {
			var creater = new ThingCreatorImpl(URI.create("https://ikt-do.de"));
			var thing = default_thing;
			var properties = Map.of("energy", PropertyProviderImpl.ActiveEnergyImported);
			thing = creater.addPropertyTemplates(thing, properties);
			assertEquals(properties.size(), thing.getProperties().size());
			assertTrue(thing.getProperties().containsKey("energy"));
			thing = creater.setPropertyMinMax(thing, "energy", 0, 100);
			var newenergy = thing.getProperties().get("energy");
			assertNotNull(newenergy);
			var newenergyproperty = newenergy.getProperties().stream().filter(p -> p.getName().equals("energy"))
					.findFirst().get();
			assertNotNull(newenergyproperty);
			var intds = (IntegerSchema) newenergyproperty;
			assertEquals(0, intds.getMinimum().intValue());
			assertEquals(100, intds.getMaximum().intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testSetMinMaxNumber() {
		try {
			var creater = new ThingCreatorImpl(URI.create("https://ikt-do.de"));
			var thing = default_thing;
			var properties = Map.of("humidity", PropertyProviderImpl.Humidity);
			thing = creater.addPropertyTemplates(thing, properties);
			assertEquals(properties.size(), thing.getProperties().size());
			assertTrue(thing.getProperties().containsKey("humidity"));
			thing = creater.setPropertyMinMax(thing, "humidity", 5.0d, 99.5d);
			var newhum = thing.getProperties().get("humidity");
			assertNotNull(newhum);
			var newhumproperty = newhum.getProperties().stream().filter(p -> p.getName().equals("humidity")).findFirst()
					.get();
			assertNotNull(newhumproperty);
			var numds = (NumberSchema) newhumproperty;
			assertEquals(5.0d, numds.getMinimum().doubleValue(), 0.0001d);
			assertEquals(99.5d, numds.getMaximum().doubleValue(), 0.0001d);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testSetUnitForProperty() {
		try {
			var creater = new ThingCreatorImpl(URI.create("https://ikt-do.de"));
			var thing = default_thing;
			var properties = Map.of("humidity", PropertyProviderImpl.Humidity);
			thing = creater.addPropertyTemplates(thing, properties);
			assertEquals(properties.size(), thing.getProperties().size());
			assertTrue(thing.getProperties().containsKey("humidity"));
			// Set unit to Watts. Makes no sense but it is fine for testing
			thing = creater.setInteractionUnit(thing, "humidity", "Watt");
			var newhum = thing.getProperties().get("humidity");
			assertNotNull(newhum);
			var newhumproperty = newhum.getProperties().stream().filter(p -> p.getName().equals("humidity")).findFirst()
					.get();
			assertNotNull(newhumproperty);
			assertEquals("Watt", newhumproperty.getUnit());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testSetUnitForAction() {
		try {
			var creater = new ThingCreatorImpl(URI.create("https://ikt-do.de"));
			var thing = default_thing;
			var actions = Map.of("targetTemperature", ActionProviderImpl.TargetTemperature);
			thing = creater.addActionTemplates(thing, actions);
			assertEquals(actions.size(), thing.getActions().size());
			assertTrue(thing.getActions().containsKey("targetTemperature"));
			// Set unit to Watts. Makes no sense but it is fine for testing
			thing = creater.setInteractionUnit(thing, "targetTemperature", "Watt");
			var newtarget = thing.getActions().get("targetTemperature");
			assertNotNull(newtarget);
			var newtargetaction = newtarget.getOutput();
			assertNotNull(newtargetaction);
			assertEquals("Watt", newtargetaction.getUnit());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testConfigReader() {
		try {
			var reader = new DefaultConfigReader();
			var thingsConfig = reader.readConfig(new File("src/test/resources/dortmund.yml"));
			var creater = new ThingCreatorImpl(URI.create("https://ikt-do.de"));
			var things = creater.createFromConfig(thingsConfig);

			LOGGER.info(fac.toJson(things.get(0)).toString());

			assertNotNull(things);
			assertTrue(things.size() == 1);
			var firstthing = things.get(0);
			assertNotNull(firstthing);
			assertTrue(firstthing.getProperties().size() == 2);
			// test for forms href
			var p1 = firstthing.getProperties().get("temperature");
			assertNotNull(p1);
			var f1 = p1.getForms().get(0);
			assertEquals(URI.create(creater.getBaseHref().toString() + "/things/" + firstthing.getTitle()
					+ "/properties/" + p1.getName()), f1.getHref());
//			var secondthing = things.get(1);
//			assertNotNull(secondthing);
//			assertTrue(secondthing.getProperties().size() == 1);
//			assertTrue(secondthing.getActions().size() == 1);
//			var lastthing = things.get(2);
//			assertTrue(lastthing.getProperties().size() == 1);
//			assertTrue(lastthing.getActions().size() == 1);
//			assertTrue(lastthing.getEvents().size() == 2);
//			assertNotNull(lastthing);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testTemplateNotFound() {
		try {
			var reader = new DefaultConfigReader();
			var thingsConfig = reader.readConfig(new File("src/test/resources/things_err.yml"));
			var creater = new ThingCreatorImpl(URI.create("https://ikt-do.de"));
			var things = creater.createFromConfig(thingsConfig);
			// we expect a empty map of properties, because the template is not found
			assertEquals(0, things.get(0).getProperties().size());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
