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

import static org.junit.Assert.*;

import org.ict.kura.internal.driver.avm.api.data.Devicelist;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class AVM_Test {
	private static final XmlMapper xmlMapper = new XmlMapper();

	private String xml = "<devicelist version=\"1\" fwversion=\"7.31\"><device identifier=\"08761 0328864\" id=\"16\" functionbitmask=\"35712\" fwversion=\"04.25\" manufacturer=\"AVM\" productname=\"FRITZ!DECT 200\"><present>1</present><txbusy>0</txbusy><name>A524-SchoKu-01</name><switch><state>1</state><mode>manuell</mode><lock>0</lock><devicelock>0</devicelock></switch><simpleonoff><state>1</state></simpleonoff><powermeter><voltage>232463</voltage><power>1850</power><energy>5281</energy></powermeter><temperature><celsius>190</celsius><offset>5</offset></temperature></device><device identifier=\"13979 0187474\" id=\"17\" functionbitmask=\"1048864\" fwversion=\"05.25\" manufacturer=\"AVM\" productname=\"FRITZ!DECT 440\"><present>1</present><txbusy>0</txbusy><name>A524-Taster-01</name><battery>100</battery><batterylow>0</batterylow><temperature><celsius>190</celsius><offset>0</offset></temperature><humidity><rel_humidity>47</rel_humidity></humidity><button identifier=\"13979 0187474-1\" id=\"5000\"><name>A524-Taster-01: Oben rechts</name><lastpressedtimestamp></lastpressedtimestamp></button><button identifier=\"13979 0187474-3\" id=\"5001\"><name>A524-Taster-01: Unten rechts</name><lastpressedtimestamp></lastpressedtimestamp></button><button identifier=\"13979 0187474-5\" id=\"5002\"><name>A524-Taster-01: Unten links</name><lastpressedtimestamp></lastpressedtimestamp></button><button identifier=\"13979 0187474-7\" id=\"5003\"><name>A524-Taster-01: Oben links</name><lastpressedtimestamp></lastpressedtimestamp></button></device><device identifier=\"11657 0354791\" id=\"18\" functionbitmask=\"35712\" fwversion=\"04.25\" manufacturer=\"AVM\" productname=\"FRITZ!DECT 210\"><present>1</present><txbusy>0</txbusy><name>A524-SchoKu-02</name><switch><state>1</state><mode>manuell</mode><lock>0</lock><devicelock>0</devicelock></switch><simpleonoff><state>1</state></simpleonoff><powermeter><voltage>231681</voltage><power>0</power><energy>291</energy></powermeter><temperature><celsius>190</celsius><offset>5</offset></temperature></device><device identifier=\"09995 0865325\" id=\"19\" functionbitmask=\"320\" fwversion=\"05.08\" manufacturer=\"AVM\" productname=\"FRITZ!DECT 301\"><present>1</present><txbusy>0</txbusy><name>A524-HKR-01</name><battery>100</battery><batterylow>0</batterylow><temperature><celsius>200</celsius><offset>0</offset></temperature><hkr><tist>40</tist><tsoll>32</tsoll><absenk>32</absenk><komfort>40</komfort><lock>0</lock><devicelock>0</devicelock><errorcode>0</errorcode><windowopenactiv>0</windowopenactiv><windowopenactiveendtime>0</windowopenactiveendtime><boostactive>0</boostactive><boostactiveendtime>0</boostactiveendtime><batterylow>0</batterylow><battery>100</battery><nextchange><endperiod>1677214800</endperiod><tchange>40</tchange></nextchange><summeractive>0</summeractive><holidayactive>0</holidayactive></hkr></device><device identifier=\"09995 0584801\" id=\"20\" functionbitmask=\"320\" fwversion=\"05.08\" manufacturer=\"AVM\" productname=\"FRITZ!DECT 301\"><present>1</present><txbusy>0</txbusy><name>A524-HKR-02</name><battery>90</battery><batterylow>0</batterylow><temperature><celsius>195</celsius><offset>0</offset></temperature><hkr><tist>39</tist><tsoll>32</tsoll><absenk>32</absenk><komfort>40</komfort><lock>0</lock><devicelock>0</devicelock><errorcode>0</errorcode><windowopenactiv>0</windowopenactiv><windowopenactiveendtime>0</windowopenactiveendtime><boostactive>0</boostactive><boostactiveendtime>0</boostactiveendtime><batterylow>0</batterylow><battery>90</battery><nextchange><endperiod>1677214800</endperiod><tchange>40</tchange></nextchange><summeractive>0</summeractive><holidayactive>0</holidayactive></hkr></device><device identifier=\"13077 0278613\" id=\"406\" functionbitmask=\"1\" fwversion=\"34.10.16.16.015\" manufacturer=\"AVM\" productname=\"FRITZ!DECT 500\"><present>0</present><txbusy>0</txbusy><name>A524-RGB-Lampe</name></device><device identifier=\"13077 0278613-1\" id=\"2000\" functionbitmask=\"237572\" fwversion=\"0.0\" manufacturer=\"AVM\" productname=\"FRITZ!DECT 500\"><present>0</present><txbusy>0</txbusy><name>A524-RGB-Lampe</name><simpleonoff><state></state></simpleonoff><levelcontrol><level>255</level><levelpercentage>100</levelpercentage></levelcontrol><colorcontrol supported_modes=\"0\" current_mode=\"\" fullcolorsupport=\"0\" mapped=\"0\"><hue></hue><saturation></saturation><unmapped_hue></unmapped_hue><unmapped_saturation></unmapped_saturation><temperature></temperature></colorcontrol><etsiunitinfo><etsideviceid>406</etsideviceid><unittype>278</unittype><interfaces>512,514,513</interfaces></etsiunitinfo></device></devicelist>";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		try {
			Devicelist list = xmlMapper.readValue(xml, Devicelist.class);
			System.out.println(list);
		} catch (Throwable t) {

		}

		fail("Not yet implemented");
	}

}
