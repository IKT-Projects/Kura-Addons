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
package org.ict.kura.internal.driver.coap.util;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.kura.type.DataType;

public class Util {

	/* Regex templates for different elements */
	private final static String THING_REGEX = "^(['/']things['/'][a-zA-Z0-9-]+)$";
	private static final String PROP_REGEX = "^(['/']things['/'][a-zA-Z0-9-]+['/']property['/'][a-zA-Z0-9]+)$";
	private static final String ACT_REGEX = "^(['/']things['/'][a-zA-Z0-9-]+['/']action['/'][a-zA-Z0-9]+)$";

	// @formatter:off
	/**
	 * Extracts the thing id from the form element href.
	 * 
	 * input:  coap://ip:port/things/$MAC-ADDRESS/properties/temp
	 * output: $MAC-ADDRESS
	 * 
	 * @param formHref the form href of the action or property
	 * @return the thing id ($MAC-ADDRESS)
	 */
	public static String extractThingIdFromFormHref(String formHref) {
		Objects.requireNonNull(formHref, "arg0 is invalid!");
		String[] s = formHref.split("/");
		if (s.length != 7)
			throw new IllegalArgumentException(formHref + ": must have 7 token!");
		return s[4];
	}
	// @formatter:on

	// @formatter:off
	/**
	 * Extracts the thing id from the thing href.
	 * 
	 * input:  coap://ip:port/things/$MAC-ADDRESS
	 * output: $MAC-ADDRESS
	 * 
	 * @param thingHref the href of the thing (id)
	 * @return the thing id ($MAC-ADDRESS)
	 */
	public static String extractThingIdFromThingHref(String thingHref) {
		Objects.requireNonNull(thingHref, "arg0 is invalid!");
		String[] s = thingHref.split("/");
		if (s.length != 5)
			throw new IllegalArgumentException(thingHref + ": must have 5 token!");
		return s[4];
	}
	// @formatter:on

	// @formatter:off
	/**
	 * Extracts the ip and portfrom the thing href.
	 * 
	 * input:  coap://ip:port/things/$MAC-ADDRESS
	 * output: ip:port
	 * 
	 * @param thingHref the href of the thing (id)
	 * @return ip:port
	 */
	public static String extractIpPortFromThingHref(String thingHref) {
		Objects.requireNonNull(thingHref, "arg0 is invalid!");
		String[] s = thingHref.split("/");
		if (s.length != 5)
			throw new IllegalArgumentException(thingHref + ": must have 5 token!");
		return s[2];
	}
	// @formatter:on

	/**
	 * Extracts name of thing from absolute path with format '/thing/XXXXX'.
	 * 
	 * @param thingURI location of thing resource
	 * @return thing name
	 */
	public static String extractNameFromPath(String thingURI, String regex) {

		Optional<String> uri = Optional.ofNullable(thingURI);
		Optional<String> exp = Optional.ofNullable(regex);

		String thingName = "";
		int index = 0;
		Pattern pattern = null;
		Matcher matcher = null;

		try {

			/* Checks if regex expression is not empty and compiles it */
			if (exp.isPresent()) {

				/* Compiles pattern using regular expression from parameter 'regex' */
				pattern = Pattern.compile(exp.get());

			} else {
				throw new IllegalArgumentException(
						"Method extractNameFromPath() requires regual expression to check if input is valid...");
			}

			/* Tests if path string is empty and inserts path in matcher */
			if (uri.isPresent() && uri.get().isEmpty() == false) {
				matcher = pattern.matcher(uri.get());
			} else {
				throw new IllegalArgumentException("Resource path is empty string!");
			}

			/*
			 * Extracts name from string using substring method if regular expression
			 * matches
			 */
			if (matcher.matches()) {

				/* Stores thing path to local variable thingName */
				thingName = uri.get();

				/* Searches for index of last '/' character */
				index = thingName.lastIndexOf("/");

				/* Overrrides variable with substring starting from index */
				thingName = thingName.substring(index + 1);

			} else {
				throw new IllegalArgumentException("Resource path '" + uri.get() + "' and regular expression '"
						+ exp.get() + "' are not matching!");
			}

		} catch (Exception e) {
			throw e;
		}
		return thingName;
	}

	/* Overloaded methods for thing, property and action string paths */

	public static String extractThingName(String uriString) {
		return extractNameFromPath(uriString, THING_REGEX);
	}

	/**
	 * Extracts current resource name from resource path.
	 * 
	 * @param uriString string path of resource
	 * @return resource name
	 */
	public static String extractNameFromUri(String uriString) {

		int index = 0;
		String name = "";
		Pattern patternProp = null;
		Matcher matcherProp = null;
		Pattern patternAct = null;
		Matcher matcherAct = null;
		Pattern patternThing = null;
		Matcher matcherThing = null;

		try {

			patternProp = Pattern.compile(PROP_REGEX);
			patternAct = Pattern.compile(ACT_REGEX);
			patternThing = Pattern.compile(THING_REGEX);

			matcherProp = patternProp.matcher(uriString);
			matcherAct = patternAct.matcher(uriString);
			matcherThing = patternThing.matcher(uriString);

			if (matcherProp.matches() || matcherAct.matches() || matcherThing.matches()) {

				index = uriString.lastIndexOf("/");
				name = uriString.substring(index + 1);

			} else {
				return "";
			}
		} catch (Exception e) {
			throw e;
		}
		return name;
	}

	/**
	 * Returns the {@link DataType} as string determined from the value object
	 * argument.
	 * 
	 * @param value the value object
	 * @return String the {@link DataType} as string
	 */
	public static String returnValueType(Object value) {
		String s = "";
		switch (value.getClass().getName()) {
		case "java.lang.Boolean":
			s = DataType.BOOLEAN.name();
			break;
		case "java.lang.Double":
			s = DataType.DOUBLE.name();
			break;
		case "java.lang.Integer":
			s = DataType.INTEGER.name();
			break;
		default:
			throw new IllegalArgumentException("No value type matched, actual only boolean or integer allowed");
		}
		return s;
	}

	/**
	 * Creates server specific resource name using ip as identifier
	 * 
	 * @param path
	 * @return
	 */
	public static String createIndividualResourceName(String path) {

		String output = "";
		String[] filter1 = null;
		String[] filter2 = null;
		String server = "";
		String name = "";
		int size = 0;

		try {

			if (path.isEmpty()) {
				throw new IllegalArgumentException("Empty resource path.Unable to extract data!");
			}

			/* Filter for server ip */
			filter1 = path.split(":");

			/* Store server ip */
			server = filter1[1].replace("//", "");

			/* Filter for name */
			filter2 = path.split("/");

			/* Get size of filter2 array */
			size = filter2.length;

			/* Store name to string */
			name = filter2[size - 1];

			/* Build output string */
			output = new String(server + ":" + name);

		} catch (Exception e) {
			System.err.printf("", e);
		}
		return output;
	}

	/**
	 * Extracts thing title from absolute path
	 * 
	 * @param path
	 * @return
	 */
	public static String extractThingTitle(String path) {

		String output = "";
		String[] filter1 = null;

		try {

			if (path.isEmpty()) {
				throw new IllegalArgumentException("Empty resource path.Unable to extract data!");
			}

			filter1 = path.split("/");
			output = filter1[4];

		} catch (Exception e) {
			throw e;
		}
		return output;
	}

}