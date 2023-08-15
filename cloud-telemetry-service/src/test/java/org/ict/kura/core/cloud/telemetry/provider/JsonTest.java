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
package org.ict.kura.core.cloud.telemetry.provider;

import org.ict.kura.core.cloud.telemetry.provider.util.Util;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonTest {

	@Test
	public void test1() {
		JsonObject root = new JsonObject();
		JsonArray device = new JsonArray();
		JsonObject property = new JsonObject();
		
		property.addProperty("ts", 12345);
		device.add(property);
		root.add("Device A", device);
		
		System.out.print(root.toString());
	}

	@Test
	public void test2() {
		String value_string = "{'time': 1001, " + "'temperature': 20.1}";
		JsonObject value = new JsonParser().parse(value_string).getAsJsonObject();

		JsonObject root = new JsonObject();
		JsonArray device = new JsonArray();
		JsonObject property = new JsonObject();

		property.addProperty("ts", value.get("time").getAsLong());
		property.add("values", Util.getValuesFromJson(value));
		device.add(property);
		root.add("Device A", device);

		System.out.print(root.toString());
	}

}
