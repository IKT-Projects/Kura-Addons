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
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "fwversion",
    "version",
    "device"
})

public class Devicelist {

    @JsonProperty("fwversion")
    private Double fwversion;
    @JsonProperty("version")
    private Integer version;
    @JsonProperty("device")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Device> device;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("fwversion")
    public Double getFwversion() {
        return fwversion;
    }

    @JsonProperty("fwversion")
    public void setFwversion(Double fwversion) {
        this.fwversion = fwversion;
    }

    @JsonProperty("version")
    public Integer getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(Integer version) {
        this.version = version;
    }

    @JsonProperty("device")
    public List<Device> getDevice() {
        return device;
    }

    @JsonProperty("device")
    public void setDevice(List<Device> device) {
        this.device = device;
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
		return "Devicelist [fwversion=" + fwversion + ", version=" + version + ", device=" + device
				+ ", additionalProperties=" + additionalProperties + "]";
	}
    
    

}
