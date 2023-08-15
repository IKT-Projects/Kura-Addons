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
package org.ict.kura.driver.multisensor.simulation;

import java.util.Random;

final public class Util {
	/* Singleton instance - eager loading */
	private static Util instance = new Util();

	/* To create an instance - its not possible */
	private Util() {
		/* Exists only to defeat instantiation */
	}

	/*
	 * Supports only ONE (and the same) instance for all users
	 */
	public static Util getInstance() {
		/** Eager loading */
		return instance;
	}

	public String createIntPayload(String propertyName) {
		/* Json payload */
		final String payload = "{\"time\" : %d, \"%s\": %d}";
		/* Range min */
		final int MIN = 0;
		/* Range MAX */
		final int MAX = 100;
		/* Random instance */
		final Random r = new Random();

		/* Creates a new random value */
		int value = r.nextInt((MAX - MIN) + 1) + MIN;
		/* Creates the json payload */
		return String.format(payload, System.currentTimeMillis(), propertyName, value);
	}

//	public String createDoublePayload(String propertyName) {
//		/* Json payload */
//		final String payload = "{\"time\" : %d, \"%s\": %f}";
//		/* Range min */
//		final double MIN = 0;
//		/* Range MAX */
//		final double MAX = 100;
//		/* Random instance */
//		final Random r = new Random();
//
//		/* Creates a new random value */
//		double value = MIN + (MAX - MIN) * r.nextDouble();
//		/* Creates the json payload */
//		return String.format(payload, System.currentTimeMillis(), propertyName, value);
//	}

	public String createDoublePayload(String propertyName) {
		/* Json payload */
		final String payload = "{\"time\" : %d, \"%s\": %d.%d}";
		/* Range min */
		final int MIN = 0;
		/* Range MAX */
		final int MAX = 100;
		/* Random instance */
		final Random r = new Random();

		/* Creates a new random value */
		int value1 = r.nextInt((MAX - MIN) + 1) + MIN;
		int value2 = r.nextInt((MAX - MIN) + 1) + MIN;
		/* Creates the json payload */
		return String.format(payload, System.currentTimeMillis(), propertyName, value1, value2);
	}

	public String createLightColorPayload() {
		/* Json payload */
		final String payload = "{\"time\" : %d, \"Red\": %d, \"Green\": %d, \"Blue\": %d, \"Clear\": %d}";
		/* Range min */
		final int MIN = 0;
		/* Range MAX */
		final int MAX = 255;
		/* Random instance */
		final Random r = new Random();

		/* Creates the json payload */
		return String.format(payload, System.currentTimeMillis(), r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX - MIN) + 1) + MIN, r.nextInt((MAX - MIN) + 1) + MIN, r.nextInt((MAX - MIN) + 1) + MIN);
	}

	public String createVibrationPayload() {
		/* Json payload */
		final String payload = "{\"time\" : %d, \"vibration\":[" + "{\"x\": %d, \"y\": %d, \"z\": %d},"
				+ "{\"x\": %d, \"y\": %d, \"z\": %d}," + "{\"x\": %d, \"y\": %d, \"z\": %d},"
				+ "{\"x\": %d, \"y\": %d, \"z\": %d}," + "{\"x\": %d, \"y\": %d, \"z\": %d}]}";
		/* Range min */
		final int MIN = 0;
		/* Range MAX */
		final int MAX = 255;
		/* Random instance */
		final Random r = new Random();

		/* Creates the json payload */
		return String.format(payload, System.currentTimeMillis(), r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX - MIN) + 1) + MIN, r.nextInt((MAX - MIN) + 1) + MIN, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX - MIN) + 1) + MIN, r.nextInt((MAX - MIN) + 1) + MIN, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX - MIN) + 1) + MIN, r.nextInt((MAX - MIN) + 1) + MIN, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX - MIN) + 1) + MIN, r.nextInt((MAX - MIN) + 1) + MIN, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX - MIN) + 1) + MIN, r.nextInt((MAX - MIN) + 1) + MIN);
	}

	public String createThermocamPayload() {
		/* Json payload */
		final String payload = "{\"time\" : %d, \"thermocam\":[[%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d],[%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d],[%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d],[%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d],[%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d],[%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d],[%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d],[%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d,%d.%d]]}";
		/* Range min */
		final int MIN = -20;
		/* Range MAX */
		final int MAX = 80;
		/* Random instance */
		/* Range min */
		final int MIN2 = 0;
		/* Range MAX */
		final int MAX2 = 10;

		final Random r = new Random();

		/* Creates the json payload */
		return String.format(payload, System.currentTimeMillis(), r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2, r.nextInt((MAX - MIN) + 1) + MIN,
				r.nextInt((MAX2 - MIN2) + 1) + MIN2);
	}
}
