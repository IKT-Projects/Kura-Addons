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
package org.ict.kura.driver.multisensor.uri;

import java.net.URI;
import java.net.URL;

public class URI_T {

	public static void main(String[] args) {
		try {
			// String u = "uri:urn:058df4f8-2184-47ce-9e74-d85c49b19e4d";
			String u = "http://www.g.de/things/01";
			URI uri = new URI(u);
			System.out.println(String.format("%s, %s, %s, %s,", uri.getSchemeSpecificPart(), uri.getScheme(),
					uri.getUserInfo(), uri.getHost()));
			System.out.println(String.format("%s", uri.toASCIIString()));
			URL url = uri.toURL();
			System.out.println(String.format("%s", url));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
