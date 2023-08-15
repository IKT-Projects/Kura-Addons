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
package org.ict.kura.internal.driver.avm;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "ain" })
public class AvmBindingConfig implements Comparable<AvmBindingConfig> {
	@JsonProperty("ain")
	private String ain;
	
	@JsonProperty("href")
	private String href;

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("ain")
	public String getAin() {
		return ain;
	}

	@JsonProperty("ain")
	public void setAin(String ain) {
		this.ain = ain;
	}
	
	@JsonProperty("href")
	public String getHref() {
		return href;
	}

	@JsonProperty("href")
	public void setHref(String href) {
		this.href = href;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public int compareTo(AvmBindingConfig o) {
		return this.ain.compareTo(o.ain);
	}

	@Override
	public String toString() {
		return "AvmBindingConfig [ain=" + ain + ", href=" + href + ", additionalProperties=" + additionalProperties
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(additionalProperties, ain, href);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AvmBindingConfig other = (AvmBindingConfig) obj;
		return Objects.equals(additionalProperties, other.additionalProperties) && Objects.equals(ain, other.ain)
				&& Objects.equals(href, other.href);
	}

	

}
