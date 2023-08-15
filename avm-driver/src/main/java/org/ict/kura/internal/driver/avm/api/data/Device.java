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
    "identifier",
    "fwversion",
    "powermeter",
    "txbusy",
    "manufacturer",
    "switch",
    "simpleonoff",
    "name",
    "temperature",
    "humidity",
    "productname",
    "id",
    "present",
    "functionbitmask",
    "battery",
    "hkr",
    "batterylow",
    "button"
})
public class Device {

   
	@JsonProperty("identifier")
    private String identifier;
    @JsonProperty("fwversion")
    private String fwversion;
    @JsonProperty("powermeter")
    private Powermeter powermeter;
    @JsonProperty("txbusy")
    private Integer txbusy;
    @JsonProperty("manufacturer")
    private String manufacturer;
    @JsonProperty("switch")
    private Switch _switch;
    @JsonProperty("simpleonoff")
    private Simpleonoff simpleonoff;
    @JsonProperty("name")
    private String name;
    @JsonProperty("temperature")
    private Temperature temperature;
    @JsonProperty("humidity")
    private Humidity humidity ;
    @JsonProperty("levelcontrol")
    private Levelcontrol levelcontrol;
    @JsonProperty("colorcontrol")
    private Colorcontrol colorcontrol;
	@JsonProperty("productname")
    private String productname;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("present")
    private Integer present;
    @JsonProperty("functionbitmask")
    private Integer functionbitmask;
    @JsonProperty("battery")
    private Integer battery;
    @JsonProperty("hkr")
    private Hkr hkr;
    @JsonProperty("batterylow")
    private Integer batterylow;
    @JsonProperty("button")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Button> button;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("identifier")
    public String getIdentifier() {
        return identifier;
    }

    @JsonProperty("identifier")
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @JsonProperty("fwversion")
    public String getFwversion() {
        return fwversion;
    }

    @JsonProperty("fwversion")
    public void setFwversion(String fwversion) {
        this.fwversion = fwversion;
    }

    @JsonProperty("powermeter")
    public Powermeter getPowermeter() {
        return powermeter;
    }

    @JsonProperty("powermeter")
    public void setPowermeter(Powermeter powermeter) {
        this.powermeter = powermeter;
    }

    @JsonProperty("txbusy")
    public Integer getTxbusy() {
        return txbusy;
    }

    @JsonProperty("txbusy")
    public void setTxbusy(Integer txbusy) {
        this.txbusy = txbusy;
    }

    @JsonProperty("manufacturer")
    public String getManufacturer() {
        return manufacturer;
    }

    @JsonProperty("manufacturer")
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @JsonProperty("switch")
    public Switch getSwitch() {
        return _switch;
    }

    @JsonProperty("switch")
    public void setSwitch(Switch _switch) {
        this._switch = _switch;
    }

    @JsonProperty("simpleonoff")
    public Simpleonoff getSimpleonoff() {
        return simpleonoff;
    }

    @JsonProperty("simpleonoff")
    public void setSimpleonoff(Simpleonoff simpleonoff) {
        this.simpleonoff = simpleonoff;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("temperature")
    public Temperature getTemperature() {
        return temperature;
    }

    @JsonProperty("temperature")
    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }
    
    @JsonProperty("humidity")
    public void setHumidity(Humidity humidity) {
        this.humidity = humidity;
    }
    
    @JsonProperty("humidity")
    public Humidity getHumidity() {
        return humidity;
    }
    
    @JsonProperty("levelcontrol")
    public Levelcontrol getLevelcontrol() {
		return levelcontrol;
	}
    
    @JsonProperty("levelcontrol")
	public void setLevelcontrol(Levelcontrol levelcontrol) {
		this.levelcontrol = levelcontrol;
	}

    @JsonProperty("productname")
    public String getProductname() {
        return productname;
    }

    @JsonProperty("productname")
    public void setProductname(String productname) {
        this.productname = productname;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("present")
    public Integer getPresent() {
        return present;
    }

    @JsonProperty("present")
    public void setPresent(Integer present) {
        this.present = present;
    }

    @JsonProperty("functionbitmask")
    public Integer getFunctionbitmask() {
        return functionbitmask;
    }

    @JsonProperty("functionbitmask")
    public void setFunctionbitmask(Integer functionbitmask) {
        this.functionbitmask = functionbitmask;
    }

    @JsonProperty("battery")
    public Integer getBattery() {
        return battery;
    }

    @JsonProperty("battery")
    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    @JsonProperty("hkr")
    public Hkr getHkr() {
        return hkr;
    }

    @JsonProperty("hkr")
    public void setHkr(Hkr hkr) {
        this.hkr = hkr;
    }

    @JsonProperty("batterylow")
    public Integer getBatterylow() {
        return batterylow;
    }

    @JsonProperty("batterylow")
    public void setBatterylow(Integer batterylow) {
        this.batterylow = batterylow;
    }

    @JsonProperty("button")
    public List<Button> getButton() {
        return button;
    }

    @JsonProperty("button")
    public void setButton(List<Button> button) {
        this.button = button;
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
		return "Device [identifier=" + identifier + ", fwversion=" + fwversion + ", powermeter=" + powermeter
				+ ", txbusy=" + txbusy + ", manufacturer=" + manufacturer + ", _switch=" + _switch + ", simpleonoff="
				+ simpleonoff + ", name=" + name + ", temperature=" + temperature + ", humidity=" + humidity
				+ ", levelcontrol=" + levelcontrol + ", colorcontrol=" + colorcontrol + ", productname=" + productname
				+ ", id=" + id + ", present=" + present + ", functionbitmask=" + functionbitmask + ", battery="
				+ battery + ", hkr=" + hkr + ", batterylow=" + batterylow + ", button=" + button
				+ ", additionalProperties=" + additionalProperties + "]";
	}
    
   


}
