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
package org.ict.kura.internal.driver.mqtt.util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The multisensor binding configuration.
 * 
 * @author IKT F. Kohlmorgen
 * @author IKT M. Kuller
 * @version 2021-04-20
 */
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "href", "controlPacketValue", "groupAddress" })
public class MultisensorBindingConfig implements Comparable<MultisensorBindingConfig> {

	@JsonProperty("href")
	private String href;
	@JsonProperty("controlPacketValue")
	private String controlPacketValue;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("href")
	public String getHref() {
		return href;
	}

	@JsonProperty("href")
	public void setHref(String href) {
		this.href = href;
	}

	@JsonProperty("controlPacketValue")
	public String getControlPacketValue() {
		return controlPacketValue;
	}

	@JsonProperty("controlPacketValue")
	public void setControlPacketValue(String controlPacketValue) {
		this.controlPacketValue = controlPacketValue;
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
	public int compareTo(MultisensorBindingConfig o) {
		return this.href.compareTo(o.href);
	}
}