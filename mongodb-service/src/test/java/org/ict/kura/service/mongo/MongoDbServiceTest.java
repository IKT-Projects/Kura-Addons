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
package org.ict.kura.service.mongo;

import static org.ict.model.wot.constant.In.Header;
import static org.ict.model.wot.constant.SecuritySchemaType.Basic;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.ict.kura.core.database.mongo.internal.MongoDbServiceImpl;
import org.ict.model.jsonld.context.Context;
import org.ict.model.wot.constant.Op;
import org.ict.model.wot.core.PropertyAffordance;
import org.ict.model.wot.core.Thing;
import org.ict.model.wot.core.VersionInfo;
import org.ict.model.wot.dataschema.BooleanSchema;
import org.ict.model.wot.hypermedia.Form;
import org.ict.model.wot.security.BasicSecurityScheme;
import org.ict.model.wot.security.SecurityScheme;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JUnit pojo test to test the MongoDbServiceImpl without OSGi functionality.
 * 
 * The following dependencies are required for slf4j logging:
 * org.apache.logging.log4j.api, org.apache.logging.log4j.core,
 * org.apache.logging.log4j.slf4j-impl and you also need the log4j.xml
 * 
 * The following dependencies are required for junit test: org.junit
 * 
 * @author MB, MK
 * @version 2020-02-20
 */
public class MongoDbServiceTest {
	/* The slf4j logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(MongoDbServiceTest.class);

	/* The mongo service implementation to be tested */
	private static MongoDbServiceImpl mongoDbServiceImpl = null;

	/* The mongo service configuration map */
	private static final Map<String, Object> PROPERTIES;
	static {
		Hashtable<String, Object> tmp = new Hashtable<String, Object>();
		tmp.put("param.dbUsername", "");
		tmp.put("param.dbPassword", "");
		tmp.put("param.dbAddress", "172.17.0.3");
		tmp.put("param.dbPort", 27017);
		tmp.put("param.dbName", "myMongoDB");
		tmp.put("param.dbEnable", true);
		PROPERTIES = Collections.unmodifiableMap(tmp);
	}

	@BeforeClass
	public static void beforeClass() {
		try {
			LOGGER.info("@BeforeClass - do nothing.");

			/* Creates a mongo service instance */
			mongoDbServiceImpl = new MongoDbServiceImpl();

			/* Connects the mongo service database */
			mongoDbServiceImpl.doUpdate(PROPERTIES);
		} catch (Throwable T) {
			System.out.print("ERROR " + T.getMessage() + T);
		}
	}

	@Before
	public void setUp() {
		LOGGER.info("@Before - delete all messages");
		/* Deletes all things */
		mongoDbServiceImpl.deleteAll();
	}

	@After
	public void tearDown() {
		try {
			LOGGER.info("@After - do nothing.");
		} catch (Throwable T) {
			LOGGER.error(T.getMessage(), T);
		} finally {
		}
	}

	@AfterClass
	public static void afterClass() {
		LOGGER.info("@AfterClass - do nothing.");

		/* Disconnects the mongo service database */
		mongoDbServiceImpl.doDeactivate();
	}

	@Test
	public void testSaveAndFindById() {
		Thing sThing = null;
		Thing fbiThing = null;
		try {
			LOGGER.info("@test - save thing.");

			/* Saves the thing with the given id in the mongo database */
			sThing = mongoDbServiceImpl.save(createLightThing("12345678"));

			/* Searches the thing with the given id in the mongo database */
			fbiThing = mongoDbServiceImpl.findById(new URI("12345678"));
		} catch (Throwable T) {
			LOGGER.error(T.getMessage(), T);
		}
		/* Compares only the id */
		Assert.assertEquals("Test: ", sThing.getId(), fbiThing.getId());
	}

	@Test
	public void testSaveAndFindAll() {
		List<Thing> things = null;
		try {
			/* Saves the thing with the given id in the mongo database */
			mongoDbServiceImpl.save(createLightThing("1"));
			mongoDbServiceImpl.save(createLightThing("2"));
			mongoDbServiceImpl.save(createLightThing("3"));

			/* This request throws an exception, the mongo database in empty */
			things = mongoDbServiceImpl.findAll();
		} catch (Throwable T) {
			LOGGER.error(T.getMessage(), T);
		}
		/* Compares only the id */
		Assert.assertEquals("Test: ", 3, things.size());
	}

	@Test
	public void testSaveAndUpdateAndFindById() {
		Thing sThing = null;
		Thing fbiThing = null;
		try {
			/* Saves the thing with the given id in the mongo database */
			mongoDbServiceImpl.save(sThing = createLightThing("999"));

			/* Modifies thing title */
			sThing.setTitle("Color Spot");

			/* Updates the thing */
			mongoDbServiceImpl.update(sThing);

			/* Searches the thing with the given id in the mongo database */
			fbiThing = mongoDbServiceImpl.findById(new URI("999"));
		} catch (Throwable T) {
			LOGGER.error(T.getMessage(), T);
		}
		/* Compares only the id */
		Assert.assertEquals("Test: ", sThing.getTitle(), fbiThing.getTitle());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testSaveAndDeleteByIdAndFindById() {
		try {
			/* Saves the thing with the given id in the mongo database */
			mongoDbServiceImpl.save(createLightThing("1"));

			/* Deletes all things */
			mongoDbServiceImpl.deleteById(new URI("1"));

			/* Searches the thing with the given id in the mongo database */
			mongoDbServiceImpl.findById(new URI("1"));
		} catch (URISyntaxException SE) {
			LOGGER.error(SE.getMessage(), SE);
		}
	}

	@Test
	public void testSaveAndDeleteAllAndFindAll() {
		List<Thing> things = null;
		try {
			LOGGER.info("@test - save thing.");

			/* Saves the thing with the given id in the mongo database */
			mongoDbServiceImpl.save(createLightThing("12345678"));

			/* Deletes all things */
			mongoDbServiceImpl.deleteAll();

			/* This request throws an exception, the mongo database in empty */
			things = mongoDbServiceImpl.findAll();
		} catch (Throwable T) {
			LOGGER.error(T.getMessage(), T);
		}
		/* Compares only the number of found things */
		Assert.assertEquals("Test: ", 0, things.size());
	}

	/**
	 * A dummy thing
	 */
	private Thing createLightThing(String id) {
		Context[] contexts = new Context[] {
				Context.builder().prefix(null).namespace("https://www.w3.org/2019/wot/td/v1").build(),
				Context.builder().prefix("saref").namespace("http://ontology.tno.nl/saref#").build(),
				Context.builder().prefix("knx").namespace("http://knx.org/ontology/").build() };

		URI thingId = URI.create(id);
		String title = "Color Light";
		Map<String, SecurityScheme> secDef = new HashMap<>();
		secDef.put("basic_sc", BasicSecurityScheme.builder().scheme(Basic).in(Header).build());
		List<String> security = new ArrayList<>();
		security.add("basic_sc");

		// create properties
		Map<String, PropertyAffordance> properties = new HashMap<>();

		Map<String, String> additionalProp = new HashMap<>();
		additionalProp.put("@type", "saref:GetCommand");
		PropertyAffordance switchStatus = PropertyAffordance.builder()
				.ds(BooleanSchema.builder().title("SwitchStatus").description("Current value of On/Off status")
						.atType(Arrays.asList(URI.create("saref:OnOffFunction"))).readOnly(true).build())
				.forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
						.href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b4211"))
						.contentType("application/json").subprotocol("https")
						.build()))
				.build();

		properties.put("status", switchStatus);
		Map<String, String> descriptions = new HashMap<>();
		descriptions.put("en", "A web connected dimmable light");
		descriptions.put("de", "Ein mit den Internet verbundenes dimmbares Licht");

		Map<String, String> titles = new HashMap<>();
		titles.put("en", "My Light 1");
		titles.put("de", "Meine 1. Lampe");

		return Thing.builder().contexts(contexts).id(thingId)
				.atType(Arrays.asList(URI.create("saref:Actuator"), URI.create("saref:LightSwitch"))).title(title)
				.titles(titles).descriptions(descriptions).version(VersionInfo.builder().instance("0.1").build())
				.created(new Date()).modified(new Date())
				.support(URI.create("https://www.fh-dortmund.de/de/fb/10/ikt/index.php"))
				.base(URI.create("https://iktsystems.goip.de:443")).atType(Arrays.asList(URI.create("iot:Actuator")))
				.securityDefinitions(secDef).security(security).properties(properties).build();
	}
}

