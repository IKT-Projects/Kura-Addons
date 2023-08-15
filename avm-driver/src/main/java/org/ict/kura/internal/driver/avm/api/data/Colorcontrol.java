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
@JsonPropertyOrder({ "supported_modes", "saturation", "fullcolorsupport", "unmapped_saturation", "current_mode",
		"mapped", "unmapped_hue", "temperature", "hue" })
public class Colorcontrol {
	@JsonProperty("supported_modes")
	private Integer supportedModes;
	@JsonProperty("saturation")
	private String saturation;
	@JsonProperty("fullcolorsupport")
	private Integer fullcolorsupport;
	@JsonProperty("unmapped_saturation")
	private String unmappedSaturation;
	@JsonProperty("current_mode")
	private String currentMode;
	@JsonProperty("mapped")
	private Integer mapped;
	@JsonProperty("unmapped_hue")
	private String unmappedHue;
	@JsonProperty("temperature")
	private String temperature;
	@JsonProperty("hue")
	private String hue;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

	@JsonProperty("supported_modes")
	public Integer getSupportedModes() {
		return supportedModes;
	}

	@JsonProperty("supported_modes")
	public void setSupportedModes(Integer supportedModes) {
		this.supportedModes = supportedModes;
	}

	@JsonProperty("saturation")
	public String getSaturation() {
		return saturation;
	}

	@JsonProperty("saturation")
	public void setSaturation(String saturation) {
		this.saturation = saturation;
	}

	@JsonProperty("fullcolorsupport")
	public Integer getFullcolorsupport() {
		return fullcolorsupport;
	}

	@JsonProperty("fullcolorsupport")
	public void setFullcolorsupport(Integer fullcolorsupport) {
		this.fullcolorsupport = fullcolorsupport;
	}

	@JsonProperty("unmapped_saturation")
	public String getUnmappedSaturation() {
		return unmappedSaturation;
	}

	@JsonProperty("unmapped_saturation")
	public void setUnmappedSaturation(String unmappedSaturation) {
		this.unmappedSaturation = unmappedSaturation;
	}

	@JsonProperty("current_mode")
	public String getCurrentMode() {
		return currentMode;
	}

	@JsonProperty("current_mode")
	public void setCurrentMode(String currentMode) {
		this.currentMode = currentMode;
	}

	@JsonProperty("mapped")
	public Integer getMapped() {
		return mapped;
	}

	@JsonProperty("mapped")
	public void setMapped(Integer mapped) {
		this.mapped = mapped;
	}

	@JsonProperty("unmapped_hue")
	public String getUnmappedHue() {
		return unmappedHue;
	}

	@JsonProperty("unmapped_hue")
	public void setUnmappedHue(String unmappedHue) {
		this.unmappedHue = unmappedHue;
	}

	@JsonProperty("temperature")
	public String getTemperature() {
		return temperature;
	}

	@JsonProperty("temperature")
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	@JsonProperty("hue")
	public String getHue() {
		return hue;
	}

	@JsonProperty("hue")
	public void setHue(String hue) {
		this.hue = hue;
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
		return "Colorcontrol [supportedModes=" + supportedModes + ", saturation=" + saturation + ", fullcolorsupport="
				+ fullcolorsupport + ", unmappedSaturation=" + unmappedSaturation + ", currentMode=" + currentMode
				+ ", mapped=" + mapped + ", unmappedHue=" + unmappedHue + ", temperature=" + temperature + ", hue="
				+ hue + ", additionalProperties=" + additionalProperties + "]";
	}
	
	
}