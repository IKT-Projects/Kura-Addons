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

public class EventAffordanceConfig implements Bindable {
	private String event;
	private String template;
	private String description;
	private FormConfig form;
	private BindingConfig binding;
	private List<EventConfig> properties;

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public FormConfig getForm() {
		return form;
	}

	public void setForm(FormConfig form) {
		this.form = form;
	}

	public BindingConfig getBinding() {
		return binding;
	}

	public void setBinding(BindingConfig binding) {
		this.binding = binding;
	}

	public List<EventConfig> getProperties() {
		return properties;
	}

	public void setProperties(List<EventConfig> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return "EventAffordanceConfig [event=" + event + ", template=" + template + ", description=" + description
				+ ", form=" + form + ", binding=" + binding + ", properties=" + properties + "]";
	}
}