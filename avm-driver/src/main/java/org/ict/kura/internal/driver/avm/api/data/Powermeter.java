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
@JsonPropertyOrder({
    "power",
    "voltage",
    "energy"
})

public class Powermeter {

    @JsonProperty("power")
    private Integer power;
    @JsonProperty("voltage")
    private Integer voltage;
    @JsonProperty("energy")
    private Integer energy;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("power")
    public Integer getPower() {
        return power;
    }

    @JsonProperty("power")
    public void setPower(Integer power) {
        this.power = power;
    }

    @JsonProperty("voltage")
    public Integer getVoltage() {
        return voltage;
    }

    @JsonProperty("voltage")
    public void setVoltage(Integer voltage) {
        this.voltage = voltage;
    }

    @JsonProperty("energy")
    public Integer getEnergy() {
        return energy;
    }

    @JsonProperty("energy")
    public void setEnergy(Integer energy) {
        this.energy = energy;
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
		return "Powermeter [power=" + power + ", voltage=" + voltage + ", energy=" + energy + ", additionalProperties="
				+ additionalProperties + "]";
	}
    
    

}
