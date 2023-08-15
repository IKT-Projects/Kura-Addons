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
package org.ict.kura.core.cloud.command.provider;

import org.ict.kura.core.cloud.command.provider.rpc.tb.request.Request;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Params_T {
	static Gson gson = new Gson();
	private static JsonParser parser = new JsonParser();

	public static void main(String[] args) {
		// String payload = "{\"device\": \"TestDeviceA\", \"data\": {\"id\": 51,
		// \"method\": \"onOff\", \"params\": {\"onOff\":true, \"pos\":1}}}";
		String payload = "{\"device\": \"TestDeviceB\", \"data\": {\"id\": 52, \"method\": \"color\", \"params\": {\"r\":255, \"g\":255}}}";
		System.out.println("Import");
		Request request = gson.fromJson(payload, Request.class);
		System.out.println("Import successful: " + request.toString());
		System.out.println("device: " + request.getDevice());
		System.out.println("method: " + request.getData().getMethod());

		System.out.println("Params:");
		request.getData().getParams().forEach((k, v) -> System.out.println((k + ":" + v)));
		System.out.println("params r: " + request.getData().getParams().get("r"));
		System.out.println("params g: " + request.getData().getParams().get("g"));

		JsonElement valueObj = parser.parse("{\"pos\":1}").getAsJsonObject();
				
		System.out.println("valueObj: " + gson.toJson(valueObj));
		System.out.println("value: " + valueObj.toString());
	}
}
