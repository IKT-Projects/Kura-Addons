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
    "tist",
    "komfort",
    "nextchange",
    "tsoll",
    "battery",
    "absenk",
    "windowopenactiv",
    "summeractive",
    "boostactive",
    "windowopenactiveendtime",
    "boostactiveendtime",
    "holidayactive",
    "batterylow",
    "lock",
    "devicelock",
    "errorcode"
})

public class Hkr {

    @JsonProperty("tist")
    private String tist;
    @JsonProperty("komfort")
    private String komfort;
    @JsonProperty("nextchange")
    private Nextchange nextchange;
    @JsonProperty("tsoll")
    private String tsoll;
    @JsonProperty("battery")
    private Integer battery;
    @JsonProperty("absenk")
    private String absenk;
    @JsonProperty("windowopenactiv")
    private Integer windowopenactiv;
    @JsonProperty("summeractive")
    private String summeractive;
    @JsonProperty("boostactive")
    private Integer boostactive;
    @JsonProperty("windowopenactiveendtime")
    private Integer windowopenactiveendtime;
    @JsonProperty("boostactiveendtime")
    private Integer boostactiveendtime;
    @JsonProperty("holidayactive")
    private String holidayactive;
    @JsonProperty("batterylow")
    private Integer batterylow;
    @JsonProperty("lock")
    private String lock;
    @JsonProperty("devicelock")
    private String devicelock;
    @JsonProperty("errorcode")
    private Integer errorcode;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("tist")
    public String getTist() {
        return tist;
    }

    @JsonProperty("tist")
    public void setTist(String tist) {
        this.tist = tist;
    }

    @JsonProperty("komfort")
    public String getKomfort() {
        return komfort;
    }

    @JsonProperty("komfort")
    public void setKomfort(String komfort) {
        this.komfort = komfort;
    }

    @JsonProperty("nextchange")
    public Nextchange getNextchange() {
        return nextchange;
    }

    @JsonProperty("nextchange")
    public void setNextchange(Nextchange nextchange) {
        this.nextchange = nextchange;
    }

    @JsonProperty("tsoll")
    public String getTsoll() {
        return tsoll;
    }

    @JsonProperty("tsoll")
    public void setTsoll(String tsoll) {
        this.tsoll = tsoll;
    }

    @JsonProperty("battery")
    public Integer getBattery() {
        return battery;
    }

    @JsonProperty("battery")
    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    @JsonProperty("absenk")
    public String getAbsenk() {
        return absenk;
    }

    @JsonProperty("absenk")
    public void setAbsenk(String absenk) {
        this.absenk = absenk;
    }

    @JsonProperty("windowopenactiv")
    public Integer getWindowopenactiv() {
        return windowopenactiv;
    }

    @JsonProperty("windowopenactiv")
    public void setWindowopenactiv(Integer windowopenactiv) {
        this.windowopenactiv = windowopenactiv;
    }

    @JsonProperty("summeractive")
    public String getSummeractive() {
        return summeractive;
    }

    @JsonProperty("summeractive")
    public void setSummeractive(String summeractive) {
        this.summeractive = summeractive;
    }

    @JsonProperty("boostactive")
    public Integer getBoostactive() {
        return boostactive;
    }

    @JsonProperty("boostactive")
    public void setBoostactive(Integer boostactive) {
        this.boostactive = boostactive;
    }

    @JsonProperty("windowopenactiveendtime")
    public Integer getWindowopenactiveendtime() {
        return windowopenactiveendtime;
    }

    @JsonProperty("windowopenactiveendtime")
    public void setWindowopenactiveendtime(Integer windowopenactiveendtime) {
        this.windowopenactiveendtime = windowopenactiveendtime;
    }

    @JsonProperty("boostactiveendtime")
    public Integer getBoostactiveendtime() {
        return boostactiveendtime;
    }

    @JsonProperty("boostactiveendtime")
    public void setBoostactiveendtime(Integer boostactiveendtime) {
        this.boostactiveendtime = boostactiveendtime;
    }

    @JsonProperty("holidayactive")
    public String getHolidayactive() {
        return holidayactive;
    }

    @JsonProperty("holidayactive")
    public void setHolidayactive(String holidayactive) {
        this.holidayactive = holidayactive;
    }

    @JsonProperty("batterylow")
    public Integer getBatterylow() {
        return batterylow;
    }

    @JsonProperty("batterylow")
    public void setBatterylow(Integer batterylow) {
        this.batterylow = batterylow;
    }

    @JsonProperty("lock")
    public String getLock() {
        return lock;
    }

    @JsonProperty("lock")
    public void setLock(String lock) {
        this.lock = lock;
    }

    @JsonProperty("devicelock")
    public String getDevicelock() {
        return devicelock;
    }

    @JsonProperty("devicelock")
    public void setDevicelock(String devicelock) {
        this.devicelock = devicelock;
    }

    @JsonProperty("errorcode")
    public Integer getErrorcode() {
        return errorcode;
    }

    @JsonProperty("errorcode")
    public void setErrorcode(Integer errorcode) {
        this.errorcode = errorcode;
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
		return "Hkr [tist=" + tist + ", komfort=" + komfort + ", nextchange=" + nextchange + ", tsoll=" + tsoll
				+ ", battery=" + battery + ", absenk=" + absenk + ", windowopenactiv=" + windowopenactiv
				+ ", summeractive=" + summeractive + ", boostactive=" + boostactive + ", windowopenactiveendtime="
				+ windowopenactiveendtime + ", boostactiveendtime=" + boostactiveendtime + ", holidayactive="
				+ holidayactive + ", batterylow=" + batterylow + ", lock=" + lock + ", devicelock=" + devicelock
				+ ", errorcode=" + errorcode + ", additionalProperties=" + additionalProperties + "]";
	}
    
    

}
