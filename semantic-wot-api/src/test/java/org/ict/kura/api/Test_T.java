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
package org.ict.kura.api;

import org.ict.gson.utils.AdapterFactory;

import com.google.gson.Gson;

public class Test_T {
	
	@SuppressWarnings("unused")
	static private Gson gson = AdapterFactory.getGsonWithDefaultWotTypeAdapters(true);
	
	static final String propertyAffordance = " {\n" + 
			"      \"observable\": false,\n" + 
			"      \"properties\": {\n" + 
			"        \"delta\": {\n" + 
			"          \"instanceOf\": \"org.ict.model.wot.dataschema.IntegerSchema\",\n" + 
			"          \"@type\": [\n" + 
			"            \"time:TODO\"\n" + 
			"          ],\n" + 
			"          \"title\": \"Time interval between 2 times\",\n" + 
			"          \"description\": \"Time interval between 2 times.\",\n" + 
			"          \"type\": \"integer\",\n" + 
			"          \"readOnly\": false,\n" + 
			"          \"writeOnly\": false\n" + 
			"        },\n" + 
			"        \"time\": {\n" + 
			"          \"@id\": \"urn:e7c6e83b-eb43-4d20-a58c-6007eecfa4b1\",\n" + 
			"          \"instanceOf\": \"org.ict.model.wot.dataschema.IntegerSchema\",\n" + 
			"          \"@type\": [\n" + 
			"            \"time:TimePosition\"\n" + 
			"          ],\n" + 
			"          \"title\": \"Time position\",\n" + 
			"          \"description\": \"A temporal position described using either a (nominal) value from an ordinal reference system, or a (numeric) value in a temporal coordinate system.\",\n" + 
			"          \"type\": \"integer\",\n" + 
			"          \"readOnly\": false,\n" + 
			"          \"writeOnly\": false\n" + 
			"        },\n" + 
			"        \"vibration\": {\n" + 
			"          \"items\": [\n" + 
			"            {\n" + 
			"              \"properties\": {\n" + 
			"                \"x\": {\n" + 
			"                  \"minimum\": 0,\n" + 
			"                  \"maximum\": 255,\n" + 
			"                  \"schema:dateModified\": {\n" + 
			"                    \"@id\": \"urn:e7c6e83b-eb43-4d20-a58c-6007eecfa4b1\"\n" + 
			"                  },\n" + 
			"                  \"instanceOf\": \"org.ict.model.wot.dataschema.IntegerSchema\",\n" + 
			"                  \"@type\": [\n" + 
			"                    \"iot:TODO\"\n" + 
			"                  ],\n" + 
			"                  \"description\": \"TODO\",\n" + 
			"                  \"type\": \"integer\",\n" + 
			"                  \"readOnly\": false,\n" + 
			"                  \"writeOnly\": false\n" + 
			"                },\n" + 
			"                \"y\": {\n" + 
			"                  \"minimum\": 0,\n" + 
			"                  \"maximum\": 255,\n" + 
			"                  \"schema:dateModified\": {\n" + 
			"                    \"@id\": \"urn:e7c6e83b-eb43-4d20-a58c-6007eecfa4b1\"\n" + 
			"                  },\n" + 
			"                  \"instanceOf\": \"org.ict.model.wot.dataschema.IntegerSchema\",\n" + 
			"                  \"@type\": [\n" + 
			"                    \"iot:TODO\"\n" + 
			"                  ],\n" + 
			"                  \"description\": \"TODO\",\n" + 
			"                  \"type\": \"integer\",\n" + 
			"                  \"readOnly\": false,\n" + 
			"                  \"writeOnly\": false\n" + 
			"                },\n" + 
			"                \"z\": {\n" + 
			"                  \"minimum\": 0,\n" + 
			"                  \"maximum\": 255,\n" + 
			"                  \"schema:dateModified\": {\n" + 
			"                    \"@id\": \"urn:e7c6e83b-eb43-4d20-a58c-6007eecfa4b1\"\n" + 
			"                  },\n" + 
			"                  \"instanceOf\": \"org.ict.model.wot.dataschema.IntegerSchema\",\n" + 
			"                  \"@type\": [\n" + 
			"                    \"iot:TODO\"\n" + 
			"                  ],\n" + 
			"                  \"description\": \"TODO\",\n" + 
			"                  \"type\": \"integer\",\n" + 
			"                  \"readOnly\": false,\n" + 
			"                  \"writeOnly\": false\n" + 
			"                }\n" + 
			"              },\n" + 
			"              \"required\": [\n" + 
			"                \"x\",\n" + 
			"                \"y\",\n" + 
			"                \"z\"\n" + 
			"              ],\n" + 
			"              \"instanceOf\": \"org.ict.model.wot.dataschema.ObjectSchema\",\n" + 
			"              \"type\": \"object\",\n" + 
			"              \"readOnly\": false,\n" + 
			"              \"writeOnly\": false\n" + 
			"            }\n" + 
			"          ],\n" + 
			"          \"instanceOf\": \"org.ict.model.wot.dataschema.ArraySchema\",\n" + 
			"          \"type\": \"array\",\n" + 
			"          \"readOnly\": false,\n" + 
			"          \"writeOnly\": false\n" + 
			"        }\n" + 
			"      },\n" + 
			"      \"required\": [\n" + 
			"        \"time\",\n" + 
			"        \"vibration\",\n" + 
			"        \"delta\"\n" + 
			"      ],\n" + 
			"      \"forms\": [\n" + 
			"        {\n" + 
			"          \"op\": [\n" + 
			"            \"readproperty\"\n" + 
			"          ],\n" + 
			"          \"href\": \"things/01-02-03-04-05-06/properties/vibration\",\n" + 
			"          \"contentType\": \"application/json\",\n" + 
			"          \"mqv:controlPacketValue\": \"SUBSCRIBE\"\n" + 
			"        }\n" + 
			"      ],\n" + 
			"      \"instanceOf\": \"org.ict.model.wot.dataschema.ObjectSchema\",\n" + 
			"      \"@type\": [\n" + 
			"        \"iot:TODO\"\n" + 
			"      ],\n" + 
			"      \"type\": \"object\",\n" + 
			"      \"readOnly\": false,\n" + 
			"      \"writeOnly\": false\n" + 
			"    }";

	public static void main(String[] args) {
		
//		PropertyAffordance prop = gson.fromJson(propertyAffordance, PropertyAffordance.class);
//
//		List<DataSchema> data = prop.getProperties();
//		
//		System.out.println(prop.getProperties().toString());
	}

}
