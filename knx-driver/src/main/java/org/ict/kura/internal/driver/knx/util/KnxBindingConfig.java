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
package org.ict.kura.internal.driver.knx.util;

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
 * The KNX binding configuration.
 * 
 * @author IKT F. Kohlmorgen
 * @author IKT M. Kuller
 * @version 2021-04-20
 */
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "name", "dptID", "groupAddress" })
public class KnxBindingConfig implements Comparable<KnxBindingConfig> {

	@JsonProperty("name")
	private String name;
	@JsonProperty("dptID")
	private String dptID;
	@JsonProperty("groupAddress")
	private String groupAddress;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("dptID")
	public String getDptID() {
		return dptID;
	}

	@JsonProperty("dptID")
	public void setDptID(String dptID) {
		this.dptID = dptID;
	}

	@JsonProperty("groupAddress")
	public String getGroupAddress() {
		return groupAddress;
	}

	@JsonProperty("groupAddress")
	public void setGroupAddress(String groupAddress) {
		this.groupAddress = groupAddress;
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
	public int compareTo(KnxBindingConfig o) {
		return this.groupAddress.compareTo(o.groupAddress);
	}
}