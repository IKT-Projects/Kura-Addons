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
package org.ict.kura.internal.driver.avm.api.data;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "mode", "lock", "state", "devicelock" })

public class Switch {

	@JsonProperty("mode")
	private String mode;
	@JsonProperty("lock")
	private Integer lock;
	@JsonProperty("state")
	private Integer state;
	@JsonProperty("devicelock")
	private Integer devicelock;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

	@JsonProperty("mode")
	public String getMode() {
		return mode;
	}

	@JsonProperty("mode")
	public void setMode(String mode) {
		this.mode = mode;
	}

	@JsonProperty("lock")
	public Integer getLock() {
		return lock;
	}

	@JsonProperty("lock")
	public void setLock(Integer lock) {
		this.lock = lock;
	}

	@JsonProperty("state")
	public Integer getState() {
		return state;
	}

	@JsonProperty("state")
	public void setState(Integer state) {
		this.state = state;
	}

	@JsonProperty("devicelock")
	public Integer getDevicelock() {
		return devicelock;
	}

	@JsonProperty("devicelock")
	public void setDevicelock(Integer devicelock) {
		this.devicelock = devicelock;
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
	public String toString() {
		return "Switch [mode=" + mode + ", lock=" + lock + ", state=" + state + ", devicelock=" + devicelock
				+ ", additionalProperties=" + additionalProperties + "]";
	}

}
