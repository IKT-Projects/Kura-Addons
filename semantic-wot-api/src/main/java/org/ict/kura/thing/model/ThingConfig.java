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
package org.ict.kura.thing.model;

import java.util.List;

public class ThingConfig {
	private String thing;
	private String description;
	private List<PropertyAffordanceConfig> properties;
	private List<ActionAffordanceConfig> actions;
	private List<EventAffordanceConfig> events;
	
	public String getThing() {
		return thing;
	}
	public void setThing(String thing) {
		this.thing = thing;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<PropertyAffordanceConfig> getProperties() {
		return properties;
	}
	public void setProperties(List<PropertyAffordanceConfig> properties) {
		this.properties = properties;
	}
	public List<ActionAffordanceConfig> getActions() {
		return actions;
	}
	public void setActions(List<ActionAffordanceConfig> actions) {
		this.actions = actions;
	}
	public List<EventAffordanceConfig> getEvents() {
		return events;
	}
	public void setEvents(List<EventAffordanceConfig> events) {
		this.events = events;
	}
	@Override
	public String toString() {
		return "ThingConfig [thing=" + thing + ", description=" + description + ", properties=" + properties
				+ ", actions=" + actions + ", events=" + events + "]";
	}
}
