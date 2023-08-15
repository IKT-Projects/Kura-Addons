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
package org.ict.kura.core.database.influx.internal;



import static java.util.Objects.isNull;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.kura.crypto.CryptoService;
import org.ict.kura.core.database.influx.InfluxDbService;
import org.ict.kura.core.database.influx.internal.value.Value;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.influxdb.client.DeleteApi;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxTable;
import com.influxdb.query.dsl.Flux;
import com.influxdb.query.dsl.functions.restriction.Restrictions;

/**
 * InfluxDB OSGi Kura service bundle which supports a ConfigurableComponent to
 * configure the bundle via Kura Web Admin and which supports methods to
 * communicate with an InfluxDB Flux language.
 * 
 * @author L. Staude
 * @author ICT M. Biskup
 * @author ICT M. Kuller
 * @version 2022-02-24
 */
@Designate(ocd = InfluxConfig.class, factory = true)
@Component(immediate = true, enabled = true, configurationPolicy = ConfigurationPolicy.REQUIRE, property = "service.pid=org.ict.kura.core.database.influx.InfluxDbService", name = "org.ict.kura.core.database.influx.InfluxDbService")

public class InfluxDbServiceImpl implements InfluxDbService {

	private static final Logger LOGGER = LoggerFactory.getLogger(InfluxDbServiceImpl.class);
	private static final String APP_ID = "org.ict.kura.core.database.influx.InfluxDbService";

	private static final String MEASUREMENT = "property";
	private static final String SOURCE_ID = "source";
	private static final String SOURCE = "fhdo";
	private static final String THING_ID = "thingId";
	private static final String PROPERTY_ID = "propertyId";

	/* The InfluxDb client instance */
	private InfluxDBClient influxDBClient;

	/* The InfluxDb bucket */
	private String bucket;

	/* The InfluxDb organization */
	private String org;

	/* The Kura crypto service instance */
	private CryptoService cryptoService;

	/* Options class to save properties and provides getter methods */
	private InfluxDbServiceOptions options;

	private WriteApi writeApi;

	private QueryApi queryApi;

	/*
	 * The constructor is only used for Junit test without using the OSGi framework.
	 */
	public InfluxDbServiceImpl(InfluxDBClient influxDB, String bucket, String org) {
		super();
		this.influxDBClient = influxDB;
		this.bucket = bucket;
		this.org = org;
	}

	public InfluxDbServiceImpl() {
		super();
	}

	/**
	 * Binding function which starts the bundle, see component.xml, is called by the
	 * OSGi framework.
	 */
	@Activate
	protected void activate(ComponentContext componentContext, Map<String, Object> properties) {
		LOGGER.info("Bundle " + APP_ID + " has started!");
		/* Updates the configuration */
		updated(properties);
	}

	/**
	 * Binding function which to shutdown the bundle, see component.xml, is called
	 * by the OSGi framework.
	 */
	@Deactivate
	protected void deactivate(ComponentContext componentContext) {
		LOGGER.info("Bundle " + APP_ID + " has stopped!");
		/* Closes all resources */
		doDeactivate();
	}

	/**
	 * Method to handle configuration updates.
	 * 
	 * @param properties Properties that are configured via the admin web interface
	 *                   in kura
	 */
	@Modified
	public void updated(Map<String, Object> properties) {
		/* Deactivates all components */
		doDeactivate();

		/* Updates all components with new configuration */
		doUpdate(properties);
	}

	@Override
	public void doUpdate(Map<String, Object> properties) {
		options = new InfluxDbServiceOptions(properties, cryptoService);

		/* If dbEnabled equals true, the connection to the database is established. */
		if (options.getEnableDatabaseConnection()) {
			String host = "http://" + options.getDataBaseConnectionIp() + ":" + options.getDataBaseConnectionPort();
			this.influxDBClient = InfluxDBClientFactory.create(host, options.getDatabaseToken(),
					options.getDatabaseOrg(), options.getDatabaseBucket());
			this.bucket = options.getDatabaseBucket();
			this.writeApi = influxDBClient.getWriteApi();
			this.queryApi = influxDBClient.getQueryApi();
		}
	}

	@Override
	public void doDeactivate() {
		/* Closes the client connection */
		if (influxDBClient != null)
			influxDBClient.close();
	}

	@Reference(name = "CryptoService", service = CryptoService.class, cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "unsetCryptoService", bind = "setCryptoService")
	protected synchronized void setCryptoService(final CryptoService cryptoService) {
		if (isNull(this.cryptoService)) {
			this.cryptoService = cryptoService;
		}
	}

	protected synchronized void unsetCryptoService(final CryptoService cryptoService) {
		if (this.cryptoService == cryptoService) {
			this.cryptoService = null;
		}
	}

	@Override
	public String ping() {
		return influxDBClient.health().getMessage();
	}

	@Override
	public void delete(OffsetDateTime start, OffsetDateTime stop, String predicate) {
		DeleteApi deleteApi = influxDBClient.getDeleteApi();
		deleteApi.delete(start, stop, predicate, bucket, org);
	}

	@Override
	public void save(String thingId, String propertyId, long time, Map<String, Object> fields) {
		Point point = Point.measurement(MEASUREMENT);
		point.addTag(SOURCE_ID, SOURCE);
		point.addTag(THING_ID, thingId);
		point.addTag(PROPERTY_ID, propertyId);
		point.addFields(fields);
		point.time(time, WritePrecision.MS);

		try {
			this.writeApi.writePoint(point);
		} catch (Throwable t) {
			LOGGER.error("", t);
			throw t;
		}
	}

	@Override
	public boolean savePoints(List<Point> points) {
		try {
			this.writeApi.writePoints(points);
			return true;
		} catch (Throwable t) {
			LOGGER.error("{}", t);
			return false;
		}

	}

	@Override
	public Map<String, Value> getLastValue(String measurement, Set<String> fields, String thingId, String propertyId) {
		Map<String, Value> lastValues = new HashMap<>();

		try {
			for (String key : fields) {
				lastValues.put(key, this.getLastValue(key, thingId, propertyId));
			}
		} catch (Throwable t) {
			LOGGER.error("", t);
			throw t;
		}
		return lastValues;
	}

	@Override
	public Value getLastValue(String measurement, String field, String thingId, String propertyId) {
		Value lastValue = null;

		try {
			Flux flux = Flux.from(bucket).range(-30L, ChronoUnit.MINUTES)
					.filter(Restrictions.and(Restrictions.measurement().equal(measurement)))
					.filter(Restrictions.and(Restrictions.field().equal(field)))
					.filter(Restrictions.and(Restrictions.tag(THING_ID).equal(thingId)))
					.filter(Restrictions.and(Restrictions.tag(PROPERTY_ID).equal(propertyId))).last();
			LOGGER.debug("Flux query: {}", flux.toString());

			List<Value> values = queryApi.query(flux.toString(), Value.class);
			lastValue = values.get(values.size() - 1);
			LOGGER.debug("lastValue: {}", lastValue);
		} catch (Throwable t) {
			LOGGER.error("", t);
			throw t;
		}
		return lastValue;
	}

	@Override
	public Map<String, List<Value>> getHistory(String measurement, Set<String> fields, String thingId,
			String propertyId, Instant start, Instant end) {
		Map<String, List<Value>> histories = new HashMap<>();

		try {
			for (String key : fields) {
				histories.put(key, this.getHistory(measurement, key, thingId, propertyId, start, end));
			}
		} catch (Throwable t) {
			LOGGER.error("", t);
			throw t;
		}
		return histories;
	}

	@Override
	public List<Value> getHistory(String measurement, String field, String thingId, String propertyId, Instant start,
			Instant end) {
		List<Value> values = null;

		try {
			Flux flux = Flux.from(bucket).range(start, end)
					.filter(Restrictions.and(Restrictions.measurement().equal(measurement)))
					.filter(Restrictions.and(Restrictions.field().equal(field)))
					.filter(Restrictions.and(Restrictions.tag(THING_ID).equal(thingId)))
					.filter(Restrictions.and(Restrictions.tag(PROPERTY_ID).equal(propertyId)));
			LOGGER.info("Flux query: {}", flux.toString());

			values = queryApi.query(flux.toString(), Value.class);
			LOGGER.info("values: {}", values);
		} catch (Throwable t) {
			LOGGER.error("", t);
			throw t;
		}
		return values;
	}

	@Override
	public List<Point> getHistory(String measurement, Instant start, Instant end) {
		try {
			List<Point> points = new ArrayList<Point>();
			// define Flux DSL query
			Flux flux = Flux.from(options.getDatabaseBucket()).range(start, end)
					.filter(Restrictions.measurement().equal(measurement));

			LOGGER.info("Flux query: {}", flux.toString());

			// Query the flux API
			List<FluxTable> tables = queryApi.query(flux.toString());

			// iterate over the FluxTables and create point objects from FluxRecord
			for (FluxTable fluxTable : tables) {
				points.addAll(FluxRecordToPointMapper.toPoints(fluxTable.getRecords()));
			}
			// Return all tables
			return points;

		} catch (Throwable t) {
			return null;
		}

	}

	@Override
	public Map<String, Value> getFirstValue(String measurement, Set<String> fields, String thingId, String propertyId) {
		Map<String, Value> firstValues = new HashMap<>();

		try {
			for (String key : fields) {
				firstValues.put(key, this.getFirstValue(key, thingId, propertyId));
			}
		} catch (Throwable t) {
			LOGGER.error("", t);
			throw t;
		}
		return firstValues;
	}

	@Override
	public Value getFirstValue(String measurement, String field, String thingId, String propertyId) {
		Value firstValue = null;

		try {
			Flux flux = Flux.from(bucket).range(-30L, ChronoUnit.MINUTES)
					.filter(Restrictions.and(Restrictions.measurement().equal(measurement)))
					.filter(Restrictions.and(Restrictions.field().equal(field)))
					.filter(Restrictions.and(Restrictions.tag(THING_ID).equal(thingId)))
					.filter(Restrictions.and(Restrictions.tag(PROPERTY_ID).equal(propertyId))).first();
			LOGGER.debug("Flux query: {}", flux.toString());

			List<Value> values = queryApi.query(flux.toString(), Value.class);
			firstValue = values.get(0);
			LOGGER.debug("firstValue: {}", firstValue);
		} catch (Throwable t) {
			LOGGER.error("", t);
			throw t;
		}
		return firstValue;
	}

	@Override
	public Map<String, Value> getMinValue(String measurement, Set<String> fields, String thingId, String propertyId) {
		Map<String, Value> minValues = new HashMap<>();

		try {
			for (String key : fields) {
				minValues.put(key, this.getMinValue(key, thingId, propertyId));
			}
		} catch (Throwable t) {
			LOGGER.error("", t);
			throw t;
		}
		return minValues;
	}

	@Override
	public Value getMinValue(String measurement, String field, String thingId, String propertyId) {
		Value minValue = null;

		try {
			Flux flux = Flux.from(bucket).range(-30L, ChronoUnit.MINUTES)
					.filter(Restrictions.and(Restrictions.measurement().equal(measurement)))
					.filter(Restrictions.and(Restrictions.field().equal(field)))
					.filter(Restrictions.and(Restrictions.tag(THING_ID).equal(thingId)))
					.filter(Restrictions.and(Restrictions.tag(PROPERTY_ID).equal(propertyId))).min();
			LOGGER.debug("Flux query: {}", flux.toString());

			List<Value> values = queryApi.query(flux.toString(), Value.class);
			minValue = values.get(0);
			LOGGER.debug("minimumValue: {}", minValue);
		} catch (Throwable t) {
			LOGGER.error("", t);
			throw t;
		}
		return minValue;
	}

	@Override
	public Map<String, Value> getMaxValue(String measurement, Set<String> fields, String thingId, String propertyId) {
		Map<String, Value> maxValues = new HashMap<>();

		try {
			for (String key : fields) {
				maxValues.put(key, this.getMaxValue(key, thingId, propertyId));
			}
		} catch (Throwable t) {
			LOGGER.error("", t);
			throw t;
		}
		return maxValues;
	}

	@Override
	public Value getMaxValue(String measurement, String field, String thingId, String propertyId) {
		Value maxValue = null;

		try {
			Flux flux = Flux.from(bucket).range(-30L, ChronoUnit.MINUTES)
					.filter(Restrictions.and(Restrictions.measurement().equal(measurement)))
					.filter(Restrictions.and(Restrictions.field().equal(field)))
					.filter(Restrictions.and(Restrictions.tag(THING_ID).equal(thingId)))
					.filter(Restrictions.and(Restrictions.tag(PROPERTY_ID).equal(propertyId))).max();
			LOGGER.debug("Flux query: {}", flux.toString());


			List<Value> values = queryApi.query(flux.toString(), Value.class);
			maxValue = values.get(0);
			LOGGER.debug("maximumValue: {}", maxValue);
		} catch (Throwable t) {
			LOGGER.error("", t);
			throw t;
		}
		return maxValue;
	}

	@Override
	public Map<String, Value> countValues(String measurement, Set<String> fields, String thingId, String propertyId) {
		Map<String, Value> countValues = new HashMap<>();

		try {
			for (String key : fields) {
				countValues.put(key, this.countValues(key, thingId, propertyId));
			}
		} catch (Throwable t) {
			LOGGER.error("", t);
			throw t;
		}
		return countValues;
	}

	@Override
	public Value countValues(String measurement, String field, String thingId, String propertyId) {
		Value countValue = null;

		try {
			Flux flux = Flux.from(bucket).range(-30L, ChronoUnit.MINUTES)
					.filter(Restrictions.and(Restrictions.measurement().equal(measurement)))
					.filter(Restrictions.and(Restrictions.field().equal(field)))
					.filter(Restrictions.and(Restrictions.tag(THING_ID).equal(thingId)))
					.filter(Restrictions.and(Restrictions.tag(PROPERTY_ID).equal(propertyId))).count();
			LOGGER.debug("Flux query: {}", flux.toString());


			List<Value> values = queryApi.query(flux.toString(), Value.class);
			countValue = values.get(values.size() - 1);
			LOGGER.debug("CountValues: {}", countValue);
		} catch (Throwable t) {
			LOGGER.error("", t);
			throw t;
		}
		return countValue;
	}

	@Override
	public Map<String, Value> sumValues(String measurement, Set<String> fields, String thingId, String propertyId) {
		Map<String, Value> sumValues = new HashMap<>();

		try {
			for (String key : fields) {
				sumValues.put(key, this.sumValues(key, thingId, propertyId));
			}
		} catch (Throwable t) {
			LOGGER.error("", t);
			throw t;
		}
		return sumValues;
	}

	@Override
	public Value sumValues(String measurement, String field, String thingId, String propertyId) {
		Value sumValue = null;

		try {
			Flux flux = Flux.from(bucket).range(-30L, ChronoUnit.MINUTES)
					.filter(Restrictions.and(Restrictions.measurement().equal(measurement)))
					.filter(Restrictions.and(Restrictions.field().equal(field)))
					.filter(Restrictions.and(Restrictions.tag(THING_ID).equal(thingId)))
					.filter(Restrictions.and(Restrictions.tag(PROPERTY_ID).equal(propertyId))).sum();
			LOGGER.debug("Flux query: {}", flux.toString());


			List<Value> values = queryApi.query(flux.toString(), Value.class);
			sumValue = values.get(0);
			LOGGER.debug("sumValues: {}", sumValue);
		} catch (Throwable t) {
			LOGGER.error("", t);
			throw t;
		}
		return sumValue;
	}

	@Override
	public Map<String, Value> meanValue(String measurement, Set<String> fields, String thingId, String propertyId) {
		Map<String, Value> meanValues = new HashMap<>();

		try {
			for (String key : fields) {
				meanValues.put(key, this.meanValue(key, thingId, propertyId));
			}
		} catch (Throwable t) {
			LOGGER.error("", t);
			throw t;
		}
		return meanValues;
	}

	@Override
	public Value meanValue(String measurement, String field, String thingId, String propertyId) {
		Value meanValue = null;

		try {
			Flux flux = Flux.from(bucket).range(-30L, ChronoUnit.MINUTES)
					.filter(Restrictions.and(Restrictions.measurement().equal(measurement)))
					.filter(Restrictions.and(Restrictions.field().equal(field)))
					.filter(Restrictions.and(Restrictions.tag(THING_ID).equal(thingId)))
					.filter(Restrictions.and(Restrictions.tag(PROPERTY_ID).equal(propertyId))).mean();
			LOGGER.debug("Flux query: {}", flux.toString());


			List<Value> values = queryApi.query(flux.toString(), Value.class);
			meanValue = values.get(values.size() - 1);
			LOGGER.debug("meanValues: {}", meanValue);
		} catch (Throwable t) {
			LOGGER.error("", t);
			throw t;
		}
		return meanValue;
	}

	@Override
	public Map<String, Value> integralValue(String measurement, Set<String> fields, String thingId, String propertyId) {
		Map<String, Value> integralValues = new HashMap<>();

		try {
			for (String key : fields) {
				integralValues.put(key, this.integralValue(key, thingId, propertyId));
			}
		} catch (Throwable t) {
			LOGGER.error("", t);
			throw t;
		}
		return integralValues;
	}

	@Override
	public Value integralValue(String measurement, String field, String thingId, String propertyId) {
		Value integralValue = null;

		try {
			Flux flux = Flux.from(bucket).range(-30L, ChronoUnit.MINUTES)
					.filter(Restrictions.and(Restrictions.measurement().equal(measurement)))
					.filter(Restrictions.and(Restrictions.field().equal(field)))
					.filter(Restrictions.and(Restrictions.tag(THING_ID).equal(thingId)))
					.filter(Restrictions.and(Restrictions.tag(PROPERTY_ID).equal(propertyId))).integral();
			LOGGER.debug("Flux query: {}", flux.toString());


			List<Value> values = queryApi.query(flux.toString(), Value.class);
			integralValue = values.get(0);
			LOGGER.debug("integralValues: {}", integralValue);
		} catch (Throwable t) {
			LOGGER.error("", t);
			throw t;
		}
		return integralValue;
	}

	@Override
	public Map<String, List<Value>> differenceValues(String measurement, Set<String> fields, String thingId,
			String propertyId) {
		Map<String, List<Value>> difference = new HashMap<>();

		try {
			for (String key : fields) {
				difference.put(key, this.differenceValues(measurement, key, thingId, propertyId));
			}
		} catch (Throwable t) {
			LOGGER.error("", t);
			throw t;
		}
		return difference;
	}

	@Override
	public List<Value> differenceValues(String measurement, String field, String thingId, String propertyId) {
		List<Value> difference = null;

		try {
			Flux flux = Flux.from(bucket).range(-30L, ChronoUnit.MINUTES)
					.filter(Restrictions.and(Restrictions.measurement().equal(measurement)))
					.filter(Restrictions.and(Restrictions.field().equal(field)))
					.filter(Restrictions.and(Restrictions.tag(THING_ID).equal(thingId)))
					.filter(Restrictions.and(Restrictions.tag(PROPERTY_ID).equal(propertyId))).difference();
			LOGGER.info("Flux query: {}", flux.toString());


			difference = queryApi.query(flux.toString(), Value.class);
			LOGGER.info("difference: {}", difference);
		} catch (Throwable t) {
			LOGGER.error("", t);
			throw t;
		}
		return difference;
	}

}
