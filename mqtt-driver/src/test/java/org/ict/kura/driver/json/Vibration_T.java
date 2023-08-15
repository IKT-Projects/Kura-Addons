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
package org.ict.kura.driver.json;

import java.util.Map.Entry;
import java.util.Set;

import org.ict.gson.utils.AdapterFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Vibration_T {
	@SuppressWarnings("unused")
	private static final Gson gson = AdapterFactory.getGsonWithDefaultWotTypeAdapters(true);
	private static JsonParser parser = new JsonParser();

	private static String payload = "{\"vibration\":[{\"X\":10303,\"Y\":7831,\"Z\":172},{\"X\":10325,\"Y\":7830,\"Z\":150},{\"X\":10305,\"Y\":7827,\"Z\":131},{\"X\":10301,\"Y\":7840,\"Z\":148},{\"X\":10315,\"Y\":7839,\"Z\":138},{\"X\":10333,\"Y\":7822,\"Z\":119},{\"X\":10308,\"Y\":7818,\"Z\":98},{\"X\":10335,\"Y\":7845,\"Z\":168},{\"X\":10338,\"Y\":7861,\"Z\":147},{\"X\":10320,\"Y\":7867,\"Z\":202},{\"X\":10324,\"Y\":7842,\"Z\":142},{\"X\":10322,\"Y\":7827,\"Z\":130},{\"X\":10281,\"Y\":7834,\"Z\":125},{\"X\":10324,\"Y\":7833,\"Z\":119},{\"X\":10322,\"Y\":7845,\"Z\":149},{\"X\":10293,\"Y\":7849,\"Z\":129},{\"X\":10351,\"Y\":7832,\"Z\":163},{\"X\":10343,\"Y\":7829,\"Z\":158},{\"X\":10295,\"Y\":7840,\"Z\":159},{\"X\":10328,\"Y\":7836,\"Z\":166},{\"X\":10311,\"Y\":7832,\"Z\":131},{\"X\":10280,\"Y\":7832,\"Z\":139},{\"X\":10320,\"Y\":7831,\"Z\":116},{\"X\":10324,\"Y\":7832,\"Z\":161},{\"X\":10331,\"Y\":7836,\"Z\":89},{\"X\":10345,\"Y\":7844,\"Z\":206},{\"X\":10323,\"Y\":7854,\"Z\":173},{\"X\":10291,\"Y\":7832,\"Z\":172},{\"X\":10311,\"Y\":7811,\"Z\":132},{\"X\":10306,\"Y\":7815,\"Z\":121},{\"X\":10294,\"Y\":7843,\"Z\":108},{\"X\":10339,\"Y\":7852,\"Z\":156},{\"X\":10347,\"Y\":7836,\"Z\":146},{\"X\":10329,\"Y\":7855,\"Z\":165},{\"X\":10331,\"Y\":7858,\"Z\":171},{\"X\":10348,\"Y\":7840,\"Z\":169},{\"X\":10308,\"Y\":7853,\"Z\":132},{\"X\":10293,\"Y\":7832,\"Z\":148},{\"X\":10309,\"Y\":7832,\"Z\":115},{\"X\":10308,\"Y\":7831,\"Z\":137},{\"X\":10313,\"Y\":7824,\"Z\":127},{\"X\":10335,\"Y\":7835,\"Z\":167},{\"X\":10344,\"Y\":7849,\"Z\":164},{\"X\":10324,\"Y\":7852,\"Z\":143},{\"X\":10327,\"Y\":7842,\"Z\":169},{\"X\":10321,\"Y\":7833,\"Z\":172},{\"X\":10290,\"Y\":7825,\"Z\":136},{\"X\":10318,\"Y\":7815,\"Z\":116},{\"X\":10323,\"Y\":7846,\"Z\":134},{\"X\":10292,\"Y\":7832,\"Z\":129},{\"X\":10318,\"Y\":7839,\"Z\":166},{\"X\":10301,\"Y\":7836,\"Z\":132},{\"X\":10318,\"Y\":7820,\"Z\":100},{\"X\":10330,\"Y\":7812,\"Z\":148},{\"X\":10327,\"Y\":7843,\"Z\":151},{\"X\":10337,\"Y\":7860,\"Z\":161},{\"X\":10339,\"Y\":7865,\"Z\":171},{\"X\":10315,\"Y\":7833,\"Z\":142},{\"X\":10303,\"Y\":7808,\"Z\":114},{\"X\":10305,\"Y\":7814,\"Z\":141},{\"X\":10305,\"Y\":7855,\"Z\":146},{\"X\":10309,\"Y\":7866,\"Z\":150},{\"X\":10332,\"Y\":7840,\"Z\":154},{\"X\":10334,\"Y\":7821,\"Z\":158},{\"X\":10316,\"Y\":7827,\"Z\":154},{\"X\":10327,\"Y\":7838,\"Z\":143},{\"X\":10327,\"Y\":7855,\"Z\":164},{\"X\":10291,\"Y\":7854,\"Z\":152},{\"X\":10302,\"Y\":7833,\"Z\":138},{\"X\":10339,\"Y\":7803,\"Z\":119},{\"X\":10329,\"Y\":7816,\"Z\":102},{\"X\":10324,\"Y\":7852,\"Z\":158},{\"X\":10329,\"Y\":7871,\"Z\":180},{\"X\":10328,\"Y\":7855,\"Z\":180},{\"X\":10309,\"Y\":7834,\"Z\":143},{\"X\":10303,\"Y\":7807,\"Z\":132},{\"X\":10318,\"Y\":7832,\"Z\":116},{\"X\":10318,\"Y\":7861,\"Z\":161},{\"X\":10315,\"Y\":7854,\"Z\":147},{\"X\":10326,\"Y\":7828,\"Z\":140},{\"X\":10318,\"Y\":7814,\"Z\":143},{\"X\":10317,\"Y\":7831,\"Z\":165},{\"X\":10318,\"Y\":7854,\"Z\":175},{\"X\":10312,\"Y\":7861,\"Z\":152},{\"X\":10307,\"Y\":7842,\"Z\":148},{\"X\":10329,\"Y\":7813,\"Z\":133},{\"X\":10317,\"Y\":7817,\"Z\":137},{\"X\":10304,\"Y\":7840,\"Z\":149},{\"X\":10321,\"Y\":7856,\"Z\":146},{\"X\":10315,\"Y\":7852,\"Z\":135},{\"X\":10298,\"Y\":7828,\"Z\":168},{\"X\":10318,\"Y\":7820,\"Z\":159},{\"X\":10325,\"Y\":7839,\"Z\":142},{\"X\":10308,\"Y\":7840,\"Z\":144},{\"X\":10328,\"Y\":7849,\"Z\":161},{\"X\":10333,\"Y\":7837,\"Z\":152},{\"X\":10325,\"Y\":7822,\"Z\":122},{\"X\":10312,\"Y\":7825,\"Z\":131},{\"X\":10319,\"Y\":7845,\"Z\":147},{\"X\":10318,\"Y\":7853,\"Z\":174},{\"X\":10329,\"Y\":7854,\"Z\":159},{\"X\":10321,\"Y\":7824,\"Z\":139},{\"X\":10319,\"Y\":7810,\"Z\":100},{\"X\":10328,\"Y\":7828,\"Z\":159},{\"X\":10312,\"Y\":7847,\"Z\":158},{\"X\":10314,\"Y\":7848,\"Z\":162},{\"X\":10325,\"Y\":7842,\"Z\":150},{\"X\":10321,\"Y\":7827,\"Z\":129},{\"X\":10316,\"Y\":7814,\"Z\":139},{\"X\":10326,\"Y\":7836,\"Z\":144}],\"time\":1623851737091}";

	public static void main(String[] args) {

		long time = 1;
		JsonElement value = parser.parse(payload);
		if (value.isJsonObject()) {
			Set<Entry<String, JsonElement>> entries = value.getAsJsonObject().entrySet();
			for (Entry<String, JsonElement> entry : entries) {
				String key = entry.getKey();
				if (key.equals("time")) {
					time = entry.getValue().getAsLong();
				}
			}
			for (Entry<String, JsonElement> entry : entries) {
				String key = entry.getKey();
				if (key.equals("vibration")) {
					if (entry.getValue().isJsonArray()) {
						for(int i = 0; i < entry.getValue().getAsJsonArray().size();i=i+6) {
							JsonObject newValue = new JsonObject();
							newValue.addProperty("time", time);
							Set<Entry<String, JsonElement>> values = entry.getValue().getAsJsonArray().get(i).getAsJsonObject().entrySet();
							for (Entry<String, JsonElement> v : values) {
								newValue.addProperty(v.getKey(), v.getValue().getAsNumber());
							}
							System.out.println(newValue);
							time += 1;
						}
//						for (JsonElement ele : entry.getValue().getAsJsonArray()) {
//							JsonObject newElement = new JsonObject();
//							newElement.addProperty("time", time);
//							Set<Entry<String, JsonElement>> values = ele.getAsJsonObject().entrySet();
//							for (Entry<String, JsonElement> v : values) {
//								newElement.addProperty(v.getKey(), v.getValue().getAsNumber());
//							}
////							JsonArray arr = new JsonArray();
////							arr.add(ele.getAsJsonObject());
////							newElement.add("vibration", arr);
//							System.out.println(newElement);
//							time += 1;
//						}
					}
				}
			}
		}
	}

}
