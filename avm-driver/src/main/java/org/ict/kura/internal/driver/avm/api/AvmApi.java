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
package org.ict.kura.internal.driver.avm.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpStatus;
import org.ict.kura.internal.driver.avm.api.data.Devicelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class AvmApi {
	private static final String CONTENT_TYPE = "Content-Type";

	private static final Logger log = LoggerFactory.getLogger(AvmApi.class);

	private final String BASE_URL;
	private final String IP;
	private final String USERNAME;
	private final String PASSWORD;

	private static final String AIN = "ain";
	private static final String GETSWITCHPOWER = "getswitchpower";
	private static final String GETDEVICELISTINFOS = "getdevicelistinfos";
	private static final String TEXT_PLAIN_CHARSET_UTF_8 = "text/plain; charset=utf-8";
	private static final String TEXT_XML_CHARSET_UTF_8 = "text/xml; charset=utf-8";
	private static final String ACCEPT = "Accept";
	private static final String SWITCHCMD = "switchcmd";
	private static final String SETHKRSOLL = "sethkrtsoll";
	private static final String SWITCH_ON = "setswitchon";
	private static final String SWITCH_OFF = "setswitchoff";
	private static final String _SID = "sid";

	private static final XmlMapper xmlMapper = new XmlMapper();

	private String SID;

	public AvmApi(String baseurl, String ip, String username, String password)
			throws NoSuchAlgorithmException, IOException {
		this.BASE_URL = baseurl;
		this.IP = ip;
		this.USERNAME = username;
		this.PASSWORD = password;
		this.SID = connect_FB(IP, username, password);
	}

	public Devicelist getDeviceList() throws Throwable {
		log.info("Requesting device list...");
		try {
			HttpResponse<String> res = Unirest.get(BASE_URL).header(ACCEPT, TEXT_XML_CHARSET_UTF_8)
					.queryString(SWITCHCMD, GETDEVICELISTINFOS).queryString(_SID, SID).asString();
			if (!checkResponse(res, HttpStatus.SC_OK)) {
				log.warn("Received http response code: %d (%s)", res.getStatus(), res.getStatusText());
				log.info("Trying to get new sessions id...");
				try {
					// try to get new session id
					this.SID = connect_FB(IP, USERNAME, PASSWORD);
					// try to get device list with the new session id
					HttpResponse<String> new_res = Unirest.get(BASE_URL).header(ACCEPT, TEXT_XML_CHARSET_UTF_8)
							.queryString(SWITCHCMD, GETDEVICELISTINFOS).queryString(_SID, SID).asString();
					if (!checkResponse(new_res, HttpStatus.SC_OK)) {
						log.error("Received http response code: %d (%s)", new_res.getStatus(), new_res.getStatusText());
						throw new IOException(String.format("Unable to fetch device list from fritzbox (%s).", IP));
					}
					log.info(new_res.getBody());

					Devicelist list = xmlMapper.readValue(new_res.getBody(), Devicelist.class);
					return list;

				} catch (Exception e) {
					log.error("Unable to login! No session id received!");
					log.error(e.getMessage());
					throw new IOException(String.format("Unable to fetch device list from fritzbox (%s).", IP));
				}
			}
			log.info(res.getBody());

			Devicelist list = xmlMapper.readValue(res.getBody(), Devicelist.class);
			return list;
		} catch (Throwable t) {
			throw t;
		}
	}

	public String readMeterValue(String ain) {
		log.debug("Reading meter value...");
		HttpResponse<String> res = Unirest.get(BASE_URL).header(ACCEPT, TEXT_PLAIN_CHARSET_UTF_8)
				.queryString(SWITCHCMD, GETSWITCHPOWER).queryString(_SID, SID).queryString(AIN, ain).asString();
		log.debug(res.getBody());
		return res.getBody();
	}

	public String switchStateDect2XX(String ain, boolean state) {
		log.debug("Switching state value...");
		HttpResponse<String> res = Unirest.get(BASE_URL).header(CONTENT_TYPE, TEXT_PLAIN_CHARSET_UTF_8)
				.queryString(_SID, SID).queryString(AIN, ain).queryString(SWITCHCMD, state ? SWITCH_ON : SWITCH_OFF)
				.asString();
		log.info("body: " + res.getBody() + " statusCode: " + res.getStatus());
		return res.getBody();
	}

	public String setTemperatureDect301(String ain, float temp) {
		log.debug("Setting temperature value...");
		HttpResponse<String> res = Unirest.get(BASE_URL).header(CONTENT_TYPE, TEXT_PLAIN_CHARSET_UTF_8)
				.queryString(_SID, SID).queryString(AIN, ain).queryString(SWITCHCMD, SETHKRSOLL)
				.queryString("param", floatTempToInt(temp)).asString();
		log.info("body: ", res.getStatusText());
		return res.getBody();
	}

	private Integer floatTempToInt(float temp) {
		Integer intTemp;
		intTemp = Math.round(temp) * 2;
		return intTemp;
	}


	private String connect_FB(String ip, String FB_Benutzername, String FB_Passwort)
			throws NoSuchAlgorithmException, IOException {
		String result = "";
		String response = "";
		String sid = "";

		log.debug("********** Connecting FB **********");

		// Get Challenge
		log.debug("Get Challenge");
		HttpResponse<String> res = Unirest.get(String.format("http://%s/login_sid.lua", ip)).asString();

		if (res.getStatus() != 200) {
			log.error("Get Challenge to Fritz!Box failed! Http status code: %d", res.getStatus());
			throw new IOException("Unable to initialize fritzbox session.");
		}

		log.debug("Get Challenge - OK");
		result = res.getBody();
		String challenge = result.substring(result.indexOf("<Challenge>") + 11, result.indexOf("<Challenge>") + 19);
		log.debug("Challenge: " + challenge);

		// Calculate Response
		log.debug("Calculating response");
		String reponseASCII = challenge + "-" + FB_Passwort;

		log.debug("Input: " + reponseASCII);
		response = getMD5Hash(reponseASCII);
		response = challenge + "-" + response;
		log.debug("Calculate response - OK");
		log.debug("Response: " + response);

		// Login and get SID
		log.debug("Login and get SID");
		HttpResponse<String> loginRes = Unirest
				.get(String.format("http://%s/login_sid.lua?user=", ip) + FB_Benutzername + "&response=" + response)
				.asString();
		if (loginRes.getStatus() != 200) {
			log.error("Login to Fritz!Box failed! Http status code: %d", loginRes.getStatus());
			throw new IOException("Unable to login to fritzbox.");
		}

		result = loginRes.getBody();
		sid = result.substring(result.indexOf("<SID>") + 5, result.indexOf("<SID>") + 21);
		if (sid.equals("0000000000000000")) {
			log.error("Login failed! Check user and password for Fritz!Box login. SID: %s", sid);
			throw new IOException("Unable to login to fritzbox.");
		}
		log.debug("SID - OK");
		log.debug("SID: " + sid);
		return sid;
	}

	public String getMD5Hash(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] data = md5.digest(input.getBytes(StandardCharsets.UTF_16LE));
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			sb.append(String.format("%02x", data[i]));
		}
		return sb.toString();
	}

//	@SuppressWarnings("unchecked")
//	private <T> T unmarshallXml(String body, Class<? extends T> clazz) throws JAXBException {
//		JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
//
//		
//		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//		return (T) jaxbUnmarshaller.unmarshal(new InputSource(new StringReader(body)));
//	}

	private boolean checkResponse(HttpResponse<?> res, int expectedStatus) {
		if (res.getStatus() == expectedStatus) {
			return true;
		}
		log.warn(String.format("Success: %b", false));
		log.warn(String.format("Status: %d ", res.getStatus()));
		return false;
	}
}