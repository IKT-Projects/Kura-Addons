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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;

/**
 * Class to convert fluxrecord list into point object
 * 
 * @author MB
 * @version 2022-02-26
 */
public class FluxRecordToPointMapper {
	private static final String SOURCE = "source";
	private static final String THING_ID = "thingId";
	private static final String PROPERTY_ID = "propertyId";

	/**
	 * 
	 * @param records list of @see FluxRecord objects
	 * @return List<Point> list of @see Point objects
	 */
	public static List<Point> toPoints(List<FluxRecord> records) {
		List<Point> points = new ArrayList<>();

		// iterate over FluxRecord and create a Point object
		for (FluxRecord record : records) {
			// add point to points list
			points.add(createPoint(record));
		}

		return points;
	}

	/**
	 * Convert a single @see FluxRecord object to a @see Point object
	 * 
	 * @param record a single @see FluxRecord object
	 * @return a @see Point object
	 */
	private static Point createPoint(FluxRecord record) {
		// create a point object
		Point p = new Point(record.getMeasurement());
		// add write precision millisecond in point object
		p.time(record.getTime(), WritePrecision.MS);
		//
		p.addTags(getTags(record));

		// add value vom FluxRecord in Point object
		if (record.getValue() instanceof Integer) {
			p.addField(record.getField(), (Number) record.getValue());
		} else if (record.getValue() instanceof Boolean) {
			p.addField(record.getField(), (Boolean) record.getValue());
		} else if (record.getValue() instanceof String) {
			p.addField(record.getField(), (String) record.getValue());
		} else if (record.getValue() instanceof Double) {
			p.addField(record.getField(), (Number) record.getValue());
		} else if (record.getValue() instanceof Number) {
			p.addField(record.getField(), (Number) record.getValue());
		} else if (record.getValue() instanceof Float) {
			p.addField(record.getField(), (Number) record.getValue());
		} else if (record.getValue() instanceof Long) {
			p.addField(record.getField(), (Long) record.getValue());
		}

		return p;
	}

	/**
	 * Extract all tags from @see FluxRecord
	 * 
	 * @param record a single @see FluxRecord object
	 * @return a Map of strings with tags
	 */
	private static Map<String, String> getTags(FluxRecord record) {
		Map<String, String> tags = new HashMap<>();
		tags.put(SOURCE, record.getValueByKey(SOURCE).toString());
		tags.put(THING_ID, record.getValueByKey(THING_ID).toString());
		tags.put(PROPERTY_ID, record.getValueByKey(PROPERTY_ID).toString());
		return tags;
	}

}
