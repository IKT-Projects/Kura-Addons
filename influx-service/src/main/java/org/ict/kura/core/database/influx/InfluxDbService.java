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
package org.ict.kura.core.database.influx;

import java.io.NotActiveException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ict.kura.core.database.influx.internal.value.Value;

import com.influxdb.client.write.Point;

/**
 * The interface with methods to communicate with an InfluxDB and which is
 * exported as OSGi declarative service.
 * 
 * @see <a href="https://docs.influxdata.com/influxdb/v1.7/">The InfluxDB
 *      site</a>
 * @see <a href="https://github.com/influxdata/influxdb-java/">The InfluxDB Java
 *      API site</a>
 * 
 * @author L. Staude
 * @author ICT M. Kuller
 * @version 2022-02-24
 */
public interface InfluxDbService {
	/**
	 * Updates all resources
	 * 
	 * @param properties configuration parameters
	 */
	public void doUpdate(Map<String, Object> properties);

	/**
	 * Closes all resources
	 */
	public void doDeactivate();

	/**
	 * Pings the database.
	 * 
	 * @return a string result
	 * @exception NotActiveException if database is not reachable
	 */
	public String ping();

	/**
	 * Deletes a time series with the given time period in the configured bucket and
	 * organization. Define a predicate with further drop informations.
	 * 
	 * @param start     a start time stamp
	 * @param stop      a stop time stamp
	 * @param predicate a drop query for example "_measurement=\"property\" AND
	 *                  _field = \"temperature\""
	 */
	public void delete(OffsetDateTime start, OffsetDateTime stop, String predicate);

	// Save ##########################################
	/**
	 * Save an element
	 * 
	 * @param thingId    a String that assigns a unique number sequence
	 * @param propertyId a String that assigns a unique id to the property
	 * @param time       a long that set the time stamp
	 * @param field      a Map<String, Object> that sets the data
	 */
	public void save(String thingId, String propertyId, long time, Map<String, Object> fields);

	// Save ##########################################
	/**
	 * Save an element
	 * 
	 * @param points    a list of Point objects
	 * 
	 */
	public boolean savePoints(List<Point> points);
	

	// Last Value ##########################################
	/**
	 * Output of the last element as Value
	 * 
	 * @param measurement a String that determines the type of InfluxDB _measurement
	 * @param fields      a set (no duplicate elements) of fields (1-N) - InfluxDB
	 *                    _fields
	 * @param thingId     a String that assigns a unique number sequence
	 * @param propertyId  a String that assigns a unique id to the property
	 * @return a result with a map of values corresponds with the field list
	 * @exception all exceptions will be forwarded
	 */
	public Map<String, Value> getLastValue(String measurement, Set<String> fields, String thingId, String propertyId);

	/**
	 * Output of the last element as Value
	 * 
	 * @param measurement a String that determines the type of measurement
	 * @param fields      a InfluxDB _field tag
	 * @param thingId     a String that assigns a unique number sequence
	 * @param propertyId  a String that assigns a unique id to the property
	 * @return a value result
	 * @exception all exceptions will be forwarded
	 */
	public Value getLastValue(String measurement, String field, String thingId, String propertyId);

	/**
	 * Output the last element of all elements, sets the measurement default value
	 * to 'property'
	 * 
	 * @see #getLastValue(String,String,String,String,String)
	 */
	default public Value getLastValue(String field, String thingId, String propertyId) {
		return getLastValue("property", field, thingId, propertyId);
	}

	// History ##########################################
	/**
	 * Outputs all elements of the list at the specified interval
	 * 
	 * @param measurement a String that determines the type of measurement
	 * @param fields      a set (no duplicate elements) of fields (1-N) - InfluxDB
	 *                    _field
	 * @param thingId     a String that assigns a unique number sequence
	 * @param propertyId  a String that assigns a unique id to the property
	 * @param start       a Instant that sets the start of the observation interval
	 * @param end         a Instant that sets the end of the observation interval
	 * @return a result with a map of time series values corresponds with the field
	 *         list
	 * @exception all exceptions will be forwarded
	 */
	public Map<String, List<Value>> getHistory(String measurement, Set<String> fields, String thingId,
			String propertyId, Instant start, Instant end);

	/**
	 * @see #getHistory(String,String,String,String,String,Instant,Instant)
	 */
	default public Map<String, List<Value>> getHistory(String measurement, Set<String> fields, String thingId,
			String propertyId, long start, long end) {
		return getHistory(measurement, fields, thingId, propertyId, Instant.ofEpochMilli(start),
				Instant.ofEpochMilli(end));
	}

	/**
	 * @see #getHistory(String,String,String,String,String,Instant,Instant)
	 */
	default public Map<String, List<Value>> getHistory(String measurement, Set<String> fields, String thingId,
			String propertyId, String start, String end) {
		return getHistory(measurement, fields, thingId, propertyId, Instant.parse(start), Instant.parse(end));
	}

	/**
	 * Outputs all elements of the list at the specified interval
	 * 
	 * @param measurement a String that determines the type of measurement
	 * @param fields      a InfluxDB _field tag
	 * @param thingId     a String that assigns a unique number sequence
	 * @param propertyId  a String that assigns a unique id to the property
	 * @param start       a Instant that sets the start of the observation interval
	 * @param end         a Instant that sets the end of the observation interval
	 * @return a result of values corresponds with the field
	 * @exception all exceptions will be forwarded
	 */
	public List<Value> getHistory(String measurement, String field, String thingId, String propertyId, Instant start,
			Instant end);

	/**
	 * @see #getHistory(String,String,String,String,Instant,Instant)
	 */
	default public List<Value> getHistory(String measurement, String field, String thingId, String propertyId,
			long start, long end) {
		return getHistory(measurement, field, thingId, propertyId, Instant.ofEpochMilli(start),
				Instant.ofEpochMilli(end));
	}

	/**
	 * @see #getHistory(String,String,String,String,Instant,Instant)
	 */
	default public List<Value> getHistory(String measurement, String field, String thingId, String propertyId,
			String start, String end) {
		return getHistory(measurement, field, thingId, propertyId, Instant.parse(start), Instant.parse(end));
	}

	/**
	 * Outputs of all elements from one measurement
	 * 
	 * @param measurement a String that determines the type of measurement
	 * @param start       a Instant that sets the start of the observation interval
	 * @param end         a Instant that sets the end of the observation interval
	 * @return
	 */
	public List<Point> getHistory(String measurement, Instant start, Instant end);

	/**
	 * 
	 * @see #getHistory(String, Instant, Instant)
	 */
	default public List<Point> getHistory(String measurement, long start, long end) {
		return getHistory(measurement, Instant.ofEpochMilli(start), Instant.ofEpochMilli(end));
	}

	// First Value ##########################################
	/**
	 * Output of the first element as Value
	 * 
	 * @param measurement a String that determines the type of InfluxDB _measurement
	 * @param fields      a set (no duplicate elements) of fields (1-N) - InfluxDB
	 *                    _fields
	 * @param thingId     a String that assigns a unique number sequence
	 * @param propertyId  a String that assigns a unique id to the property
	 * @return a result with a map of values corresponds with the field list
	 * @exception all exceptions will be forwarded
	 */
	public Map<String, Value> getFirstValue(String measurement, Set<String> fields, String thingId, String propertyId);

	/**
	 * Output of the first element as Value
	 * 
	 * @param measurement a String that determines the type of measurement
	 * @param fields      a InfluxDB _field tag
	 * @param thingId     a String that assigns a unique number sequence
	 * @param propertyId  a String that assigns a unique id to the property
	 * @return a value result
	 * @exception all exceptions will be forwarded
	 */
	public Value getFirstValue(String measurement, String field, String thingId, String propertyId);

	/**
	 * Output the first element of all elements, sets the measurement default value
	 * to 'property'
	 * 
	 * @see #getFirstValue(String,String,String,String,String)
	 */
	default public Value getFirstValue(String field, String thingId, String propertyId) {
		return getFirstValue("property", field, thingId, propertyId);
	}

	// Minimum Value ##########################################
	/**
	 * Output of the minimum element as Value
	 * 
	 * @param measurement a String that determines the type of InfluxDB _measurement
	 * @param fields      a set (no duplicate elements) of fields (1-N) - InfluxDB
	 *                    _fields
	 * @param thingId     a String that assigns a unique number sequence
	 * @param propertyId  a String that assigns a unique id to the property
	 * @return a result with a map of values corresponds with the field list
	 * @exception all exceptions will be forwarded
	 */
	public Map<String, Value> getMinValue(String measurement, Set<String> fields, String thingId, String propertyId);

	/**
	 * Output of the minimum element as Value
	 * 
	 * @param measurement a String that determines the type of measurement
	 * @param fields      a InfluxDB _field tag
	 * @param thingId     a String that assigns a unique number sequence
	 * @param propertyId  a String that assigns a unique id to the property
	 * @return a value result
	 * @exception all exceptions will be forwarded
	 */
	public Value getMinValue(String measurement, String field, String thingId, String propertyId);

	/**
	 * Output the minimum element of all elements, sets the measurement default
	 * value to 'property'
	 * 
	 * @see #getMinValue(String,String,String,String,String)
	 */
	default public Value getMinValue(String field, String thingId, String propertyId) {
		return getMinValue("property", field, thingId, propertyId);
	}

	// Maximum Value ##########################################
	/**
	 * Output of the maximum element as Value
	 * 
	 * @param measurement a String that determines the type of InfluxDB _measurement
	 * @param fields      a set (no duplicate elements) of fields (1-N) - InfluxDB
	 *                    _fields
	 * @param thingId     a String that assigns a unique number sequence
	 * @param propertyId  a String that assigns a unique id to the property
	 * @return a result with a map of values corresponds with the field list
	 * @exception all exceptions will be forwarded
	 */
	public Map<String, Value> getMaxValue(String measurement, Set<String> fields, String thingId, String propertyId);

	/**
	 * Output of the maximum element as Value
	 * 
	 * @param measurement a String that determines the type of measurement
	 * @param fields      a InfluxDB _field tag
	 * @param thingId     a String that assigns a unique number sequence
	 * @param propertyId  a String that assigns a unique id to the property
	 * @return a value result
	 * @exception all exceptions will be forwarded
	 */
	public Value getMaxValue(String measurement, String field, String thingId, String propertyId);

	/**
	 * Output the maximum element of all elements, sets the measurement default
	 * value to 'property'
	 * 
	 * @see #getMaxValue(String,String,String,String,String)
	 */
	default public Value getMaxValue(String field, String thingId, String propertyId) {
		return getMaxValue("property", field, thingId, propertyId);
	}

	// Count Values ##########################################
	/**
	 * Output of the number of element as Value
	 * 
	 * @param measurement a String that determines the type of InfluxDB _measurement
	 * @param fields      a set (no duplicate elements) of fields (1-N) - InfluxDB
	 *                    _fields
	 * @param thingId     a String that assigns a unique number sequence
	 * @param propertyId  a String that assigns a unique id to the property
	 * @return a result with a map of values corresponds with the field list
	 * @exception all exceptions will be forwarded
	 */
	public Map<String, Value> countValues(String measurement, Set<String> fields, String thingId, String propertyId);

	/**
	 * Output of the number of element as Value
	 * 
	 * @param measurement a String that determines the type of measurement
	 * @param fields      a InfluxDB _field tag
	 * @param thingId     a String that assigns a unique number sequence
	 * @param propertyId  a String that assigns a unique id to the property
	 * @return a value result
	 * @exception all exceptions will be forwarded
	 */
	public Value countValues(String measurement, String field, String thingId, String propertyId);

	/**
	 * Output the number of element of all elements, sets the measurement default
	 * value to 'property'
	 * 
	 * @see #countValues(String,String,String,String,String)
	 */
	default public Value countValues(String field, String thingId, String propertyId) {
		return countValues("property", field, thingId, propertyId);
	}

	// Sum of the Values ##########################################
	/**
	 * Output sum of the elements as Value
	 * 
	 * @param measurement a String that determines the type of InfluxDB _measurement
	 * @param fields      a set (no duplicate elements) of fields (1-N) - InfluxDB
	 *                    _fields
	 * @param thingId     a String that assigns a unique number sequence
	 * @param propertyId  a String that assigns a unique id to the property
	 * @return a result with a map of values corresponds with the field list
	 * @exception all exceptions will be forwarded
	 */
	public Map<String, Value> sumValues(String measurement, Set<String> fields, String thingId, String propertyId);

	/**
	 * Output sum of the elements as Value
	 * 
	 * @param measurement a String that determines the type of measurement
	 * @param fields      a InfluxDB _field tag
	 * @param thingId     a String that assigns a unique number sequence
	 * @param propertyId  a String that assigns a unique id to the property
	 * @return a value result
	 * @exception all exceptions will be forwarded
	 */
	public Value sumValues(String measurement, String field, String thingId, String propertyId);

	/**
	 * Output sum of the elements of all elements, sets the measurement default
	 * value to 'property'
	 * 
	 * @see #sumValues(String,String,String,String,String)
	 */
	default public Value sumValues(String field, String thingId, String propertyId) {
		return sumValues("property", field, thingId, propertyId);
	}

	// Mean of the Values ##########################################
	/**
	 * Output mean of the elements as Value
	 * 
	 * @param measurement a String that determines the type of InfluxDB _measurement
	 * @param fields      a set (no duplicate elements) of fields (1-N) - InfluxDB
	 *                    _fields
	 * @param thingId     a String that assigns a unique number sequence
	 * @param propertyId  a String that assigns a unique id to the property
	 * @return a result with a map of values corresponds with the field list
	 * @exception all exceptions will be forwarded
	 */
	public Map<String, Value> meanValue(String measurement, Set<String> fields, String thingId, String propertyId);

	/**
	 * Output mean of the elements as Value
	 * 
	 * @param measurement a String that determines the type of measurement
	 * @param fields      a InfluxDB _field tag
	 * @param thingId     a String that assigns a unique number sequence
	 * @param propertyId  a String that assigns a unique id to the property
	 * @return a value result
	 * @exception all exceptions will be forwarded
	 */
	public Value meanValue(String measurement, String field, String thingId, String propertyId);

	/**
	 * Output mean of the elements of all elements, sets the measurement default
	 * value to 'property'
	 * 
	 * @see #meanValue(String,String,String,String,String)
	 */
	default public Value meanValue(String field, String thingId, String propertyId) {
		return meanValue("property", field, thingId, propertyId);
	}

	// Integral of the Values ##########################################
	/**
	 * Output integral of the elements as Value
	 * 
	 * @param measurement a String that determines the type of InfluxDB _measurement
	 * @param fields      a set (no duplicate elements) of fields (1-N) - InfluxDB
	 *                    _fields
	 * @param thingId     a String that assigns a unique number sequence
	 * @param propertyId  a String that assigns a unique id to the property
	 * @return a result with a map of values corresponds with the field list
	 * @exception all exceptions will be forwarded
	 */
	public Map<String, Value> integralValue(String measurement, Set<String> fields, String thingId, String propertyId);

	/**
	 * Output integral of the elements as Value
	 * 
	 * @param measurement a String that determines the type of measurement
	 * @param fields      a InfluxDB _field tag
	 * @param thingId     a String that assigns a unique number sequence
	 * @param propertyId  a String that assigns a unique id to the property
	 * @return a value result
	 * @exception all exceptions will be forwarded
	 */
	public Value integralValue(String measurement, String field, String thingId, String propertyId);

	/**
	 * Output integral of the elements of all elements, sets the measurement default
	 * value to 'property'
	 * 
	 * @see #integralValue(String,String,String,String,String)
	 */
	default public Value integralValue(String field, String thingId, String propertyId) {
		return integralValue("property", field, thingId, propertyId);
	}

	// Difference of the Values ##########################################
	/**
	 * Output difference of the elements as list of Values
	 * 
	 * @param measurement a String that determines the type of InfluxDB _measurement
	 * @param fields      a set (no duplicate elements) of fields (1-N) - InfluxDB
	 *                    _fields
	 * @param thingId     a String that assigns a unique number sequence
	 * @param propertyId  a String that assigns a unique id to the property
	 * @return a result with a map of time series values corresponds with the field
	 *         list
	 * @exception all exceptions will be forwarded
	 */
	public Map<String, List<Value>> differenceValues(String measurement, Set<String> fields, String thingId,
			String propertyId);

	/**
	 * Output difference of the elements as list of Values
	 * 
	 * @param measurement a String that determines the type of measurement
	 * @param fields      a InfluxDB _field tag
	 * @param thingId     a String that assigns a unique number sequence
	 * @param propertyId  a String that assigns a unique id to the property
	 * @return a result of values corresponds with the field
	 * @exception all exceptions will be forwarded
	 */
	List<Value> differenceValues(String measurement, String field, String thingId, String propertyId);

	/**
	 * Output difference of the elements of all elements, sets the measurement
	 * default value to 'property'
	 * 
	 * @see #differenceValues(String,String,String,String,String)
	 */
	default public List<Value> differenceValues(String field, String thingId, String propertyId) {
		return differenceValues("property", field, thingId, propertyId);
	}

// Math distinct ##########################################

}
