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
package org.ict.kura.service.influx;


import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ict.kura.core.database.influx.InfluxDbService;
import org.ict.kura.core.database.influx.internal.InfluxDbServiceImpl;
import org.ict.kura.core.database.influx.internal.value.Value;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;

/**
 * JUnit test for the {@link InfluxDbServiceImpl} class.
 * 
 * @author L. Staude
 * @author ICT M. Biskup
 * @author ICT M. Kuller
 * @version 2022-02-24
 */
public class Influx_T {
	/* The Logger instance */
	private static final Logger LOGGER = LogManager.getLogger(InfluxDbServiceImpl.class);

	/* InfluxDB server */
	private static final String SERVER = "http://localhost:8086";

	/* The Token instance */
	private static final char[] TOKEN = "ulG8-TFGe0FhqqAnidz8MkhZVcX6H4icTpi0uxjpWv3y-33hE1nOm6FemHHeoyNSAfwarHcXsldv-yBbZ759vw=="
			.toCharArray();

	/* The Org instance */
	private static final String ORG = "ce13027b2a030e30";

	/* The Bucket instance */
	// private static final String BUCKET = "test";
	private static final String BUCKET = "Kura";

	/* InfluxDB properties - special ICT format */
	private static final String MEASUREMENT = "property";
	private static final String THING_ID = "123";
	private static final String PROPERTY_ID = "temperature";

	/* A list of fields (complex data points) */
	private static Map<String, Object> fieldValueMap = new HashMap<>();

	/*
	 * Prepares an InfluxDB entry => 1.0,2.0,3.0 ... 10.0 / 10 elements / time
	 * interval 1000 msec
	 */

	private static final double TIME_SERIES_MIN = 1.0;
	private static final double TIME_SERIES_MAX = 10.0;
	private static final long TIME_STAMP_START = System.currentTimeMillis() - 30000;
	private static final long TIME_STAMP_INTERVAL = 1000;

	private static final Instant TIME_STAMP_GET_HISTORY_START = Instant.now().minusSeconds(60);
	private static final Instant TIME_STAMP_GET_HISTORY_END = Instant.now();

	/* Connection InfluxDb Service */
	static InfluxDbService influxDbService;

	/* Connection InfluxDb Client */
	static InfluxDBClient influxDBClient;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			LOGGER.info("Test SetUpBeforeClass");
			influxDBClient = InfluxDBClientFactory.create(SERVER, TOKEN, ORG, BUCKET);
			influxDbService = new InfluxDbServiceImpl(influxDBClient, BUCKET, ORG);
		} catch (Throwable T) {
			LOGGER.catching(T);
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		try {
			LOGGER.info("Test TearDownAfterClass");
			if (influxDBClient != null)
				influxDBClient.close();
		} catch (Throwable T) {
			LOGGER.catching(T);
		}
	}

	@Before
	public void setUpBeforeTest() throws Exception {
		try {
			LOGGER.info("Test setUpBeforeTest");
			/* Deletes the given measurement of the last hour */
			influxDbService.delete(OffsetDateTime.now().minus(12, ChronoUnit.HOURS), OffsetDateTime.now(),
					"_measurement=property");

			long timeStamp = TIME_STAMP_START;
			try {
				for (double i = TIME_SERIES_MIN; i < TIME_SERIES_MAX + 1; i++) {
					fieldValueMap.put(PROPERTY_ID, i);
					influxDbService.save(THING_ID, PROPERTY_ID, timeStamp += TIME_STAMP_INTERVAL, fieldValueMap);
				}
			} catch (Throwable T) {
				LOGGER.catching(T);
			}
		} catch (Throwable T) {
			LOGGER.catching(T);
		}
	}

	@After
	public void tearDownAfterTest() throws Exception {
		try {
			LOGGER.info("Test tearDownAfterTest");
		} catch (Throwable T) {
			LOGGER.catching(T);
		}
	}

	@Test
	public void ping() {
		String message = null;
		message = influxDbService.ping();
		LOGGER.info(message);
		assertEquals("ready for queries and writes", message);
	}

	@Test
	public void getLastValue() {
		/* Executes the query */
		Map<String, Value> lastValues = influxDbService.getLastValue(MEASUREMENT, fieldValueMap.keySet(), THING_ID,
				PROPERTY_ID);
		LOGGER.info(lastValues);

		/* Checks the result */
		assertEquals(THING_ID, lastValues.get("temperature").getThingId());
		assertEquals(PROPERTY_ID, lastValues.get("temperature").getPropertyId());
		assertEquals(10.0, lastValues.get("temperature").getValue());
		assertEquals("Double", lastValues.get("temperature").getType());
	}

	@Test
	public void getHistory() {
		/* Executes the query */
		Map<String, List<Value>> historyValues = influxDbService.getHistory(MEASUREMENT, fieldValueMap.keySet(),
				THING_ID, PROPERTY_ID, TIME_STAMP_GET_HISTORY_START, TIME_STAMP_GET_HISTORY_END);
		LOGGER.info(historyValues);

		/* Checks the result */
		assertEquals(THING_ID, historyValues.get("temperature").get(0).getThingId());
		assertEquals(PROPERTY_ID, historyValues.get("temperature").get(0).getPropertyId());
		assertEquals(10, historyValues.get("temperature").size());

		assertEquals(1.0, historyValues.get("temperature").get(0).getValue());
		assertEquals(10.0, historyValues.get("temperature").get(9).getValue());
	}

	@Test
	public void getFirstValue() {
		/* Executes the query */
		Map<String, Value> firstValues = influxDbService.getFirstValue(MEASUREMENT, fieldValueMap.keySet(), THING_ID,
				PROPERTY_ID);
		LOGGER.info(firstValues);

		/* Checks the result */
		assertEquals(THING_ID, firstValues.get("temperature").getThingId());
		assertEquals(PROPERTY_ID, firstValues.get("temperature").getPropertyId());
		assertEquals(1.0, firstValues.get("temperature").getValue());
		assertEquals("Double", firstValues.get("temperature").getType());
	}

	@Test
	public void getMinValue() {
		/* Executes the query */
		Map<String, Value> minValues = influxDbService.getMinValue(MEASUREMENT, fieldValueMap.keySet(), THING_ID,
				PROPERTY_ID);
		LOGGER.info(minValues);

		/* Checks the result */
		assertEquals(THING_ID, minValues.get("temperature").getThingId());
		assertEquals(PROPERTY_ID, minValues.get("temperature").getPropertyId());
		assertEquals(1.0, minValues.get("temperature").getValue());
		assertEquals("Double", minValues.get("temperature").getType());
	}

	@Test
	public void getMaxValue() {
		/* Executes the query */
		Map<String, Value> maxValues = influxDbService.getMaxValue(MEASUREMENT, fieldValueMap.keySet(), THING_ID,
				PROPERTY_ID);
		LOGGER.info(maxValues);

		/* Checks the result */
		assertEquals(THING_ID, maxValues.get("temperature").getThingId());
		assertEquals(PROPERTY_ID, maxValues.get("temperature").getPropertyId());
		assertEquals(10.0, maxValues.get("temperature").getValue());
		assertEquals("Double", maxValues.get("temperature").getType());
	}

	@Test
	public void countValues() {
		/* Executes the query */
		Map<String, Value> countValues = influxDbService.countValues(MEASUREMENT, fieldValueMap.keySet(), THING_ID,
				PROPERTY_ID);
		LOGGER.info(countValues);

		/* Checks the result */
		assertEquals(THING_ID, countValues.get("temperature").getThingId());
		assertEquals(PROPERTY_ID, countValues.get("temperature").getPropertyId());
		assertEquals("10", countValues.get("temperature").getValue().toString());
		assertEquals("Long", countValues.get("temperature").getType());
	}

	@Test
	public void sumValues() {
		/* Executes the query */
		Map<String, Value> sumValues = influxDbService.sumValues(MEASUREMENT, fieldValueMap.keySet(), THING_ID,
				PROPERTY_ID);
		LOGGER.info(sumValues);

		/* Checks the result */
		assertEquals(THING_ID, sumValues.get("temperature").getThingId());
		assertEquals(PROPERTY_ID, sumValues.get("temperature").getPropertyId());
		assertEquals(55.0, sumValues.get("temperature").getValue());
		assertEquals("Double", sumValues.get("temperature").getType());
	}

	@Test
	public void meanValue() {
		/* Executes the query */
		Map<String, Value> meanValues = influxDbService.meanValue(MEASUREMENT, fieldValueMap.keySet(), THING_ID,
				PROPERTY_ID);
		LOGGER.info(meanValues);

		/* Checks the result */
		assertEquals(THING_ID, meanValues.get("temperature").getThingId());
		assertEquals(PROPERTY_ID, meanValues.get("temperature").getPropertyId());
		assertEquals(5.5, meanValues.get("temperature").getValue());
		assertEquals("Double", meanValues.get("temperature").getType());
	}

	@Test
	public void integralValue() {
		/* Executes the query */
		Map<String, Value> integralValues = influxDbService.integralValue(MEASUREMENT, fieldValueMap.keySet(), THING_ID,
				PROPERTY_ID);
		LOGGER.info(integralValues);

		/* Checks the result */
		assertEquals(THING_ID, integralValues.get("temperature").getThingId());
		assertEquals(PROPERTY_ID, integralValues.get("temperature").getPropertyId());
		assertEquals(49.5, integralValues.get("temperature").getValue());
		assertEquals("Double", integralValues.get("temperature").getType());
	}

	@Test
	public void differenceValues() {
		/* Executes the query */
		Map<String, List<Value>> differenceValues = influxDbService.differenceValues(MEASUREMENT,
				fieldValueMap.keySet(), THING_ID, PROPERTY_ID);
		LOGGER.info(differenceValues);

		/* Checks the result */
		assertEquals(THING_ID, differenceValues.get("temperature").get(0).getThingId());
		assertEquals(PROPERTY_ID, differenceValues.get("temperature").get(0).getPropertyId());
		assertEquals(9, differenceValues.get("temperature").size());

		assertEquals(1.0, differenceValues.get("temperature").get(0).getValue());
		assertEquals(1.0, differenceValues.get("temperature").get(8).getValue());
	}
}
