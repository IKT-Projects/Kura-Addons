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
package org.ict.kura.core.thing.asset.creator.util;

import java.util.List;

import org.ict.kura.asset.creator.thing.util.ThingContainer;
import org.ict.model.wot.core.Thing;

/**
 * Implementation of the @ThingContainer interface
 * 
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2020-12-17
 */
public class ThingContainerImpl implements ThingContainer {

	/* The unique driver id */
	private String driverPID;
	/* List of things */
	private List<Thing> things;

	public ThingContainerImpl(String driverPID, List<Thing> things) {
		super();
		this.driverPID = driverPID;
		this.things = things;
	}

	@Override
	public void setDriverPID(String driverPID) {
		this.driverPID = driverPID;
	}

	@Override
	public String getDriverPID() {
		return driverPID;
	}

	@Override
	public void setThings(List<Thing> things) {
		this.things = things;
	}

	@Override
	public List<Thing> getThings() {
		return things;
	}

	@Override
	public String toString() {
		return "ThingContainerImpl [driverPID=" + driverPID + ", things=" + things + "]";
	}
}
