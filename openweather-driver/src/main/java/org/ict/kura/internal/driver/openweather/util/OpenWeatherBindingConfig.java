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
package org.ict.kura.internal.driver.openweather.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This is the OpenWeatherMap (OWM) configuration which links the technology
 * parameters (OWM) to the corresponding WoT description parameters 8in this
 * case the href).
 * 
 * @author M. Biskup
 * @author M. Kuller
 * @version 2021-10-20
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "name" })
public class OpenWeatherBindingConfig implements Comparable<OpenWeatherBindingConfig> {
	@JsonProperty("name")
	private String name;

	@JsonProperty("href")
	private String href;

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("href")
	public String getHref() {
		return href;
	}

	@JsonProperty("href")
	public void setHref(String href) {
		this.href = href;
	}

	@Override
	public int compareTo(OpenWeatherBindingConfig o) {
		return this.href.compareTo(o.href);
	}

	@Override
	public String toString() {
		return "OpenWeatherBindingConfig [name=" + name + ", href=" + href + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((href == null) ? 0 : href.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OpenWeatherBindingConfig other = (OpenWeatherBindingConfig) obj;
		if (href == null) {
			if (other.href != null)
				return false;
		} else if (!href.equals(other.href))
			return false;
		return true;
	}
}
