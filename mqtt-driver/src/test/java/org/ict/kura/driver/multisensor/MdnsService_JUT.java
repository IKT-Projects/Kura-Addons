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
package org.ict.kura.driver.multisensor;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import javax.jmdns.ServiceInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ict.kura.internal.driver.multisensor.mdns.MdnsService;
import org.ict.kura.internal.driver.multisensor.mdns.MdnsServiceFactory;
import org.ict.kura.internal.driver.multisensor.mdns.MdnsServiceListener;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for JMDNS Service.
 */
public class MdnsService_JUT {
	/* log4j logger reference for class Start_T */
	private static final Logger logger = LogManager.getFormatterLogger(MdnsService_JUT.class);

	/* Discovery service */
	private MdnsService mdnsServiceD;

	/* Register service */
	private MdnsService mdnsServiceR;

	/* Empirically delay between the discovery and register */
	private final int TIMEOUT = 2000;

	/* DNS service type e.g. for HTTP */
	private final String SERVICE_TYPE = "_multisensor._tcp.local.";

	/* DNS symbolic service name */
	private final String SERVICE_NAME = "MKEY";

	/* Port under which the service is available */
	private final int SERVICE_PORT = 1099;

	/* IP under which the service is available */
	private final String SERVICE_IP = "192.168.178.105";

	@BeforeClass
	public static void beforeClass() {
		logger.info("@BeforeClass - do nothing.");
	}

	@Before
	public void setUp() {
		logger.info("@Before - do nothing.");
	}

	@After
	public void tearDown() {
		try {
			logger.info("@After - do nothing.");

			/* Closes the discovery service */
			mdnsServiceD.close();
			/* Closes the register service */
			mdnsServiceR.close();
		} catch (Throwable T) {
			/** Trace error */
			logger.catching(T);
		} finally {
		}
	}

	@AfterClass
	public static void afterClass() {
		logger.info("@AfterClass - do nothing.");
	}

	@Test
	public void test() {
		try {
			/* Creates a listener */
			MdnsServiceListener dnsServiceListener = new MdnsServiceListener();

			/* Empirically delay */
			Thread.sleep(TIMEOUT); 

			logger.info("Starts the JMDNS register service ...");

			/* Creates a service description */
			Map<String, String> serviceDescription = new HashMap<String, String>();
			serviceDescription.put("MAC", "00:11:7F:54:DF:0B");

			/* Starts the register service */
			mdnsServiceR = MdnsServiceFactory.createRegister(SERVICE_TYPE, SERVICE_NAME, serviceDescription,
					SERVICE_PORT, InetAddress.getByName(SERVICE_IP));

			logger.info("dnsServicR: " + mdnsServiceR.toString());

			logger.info("Starts the JMDNS discovery service ...");

			/* Starts the discovery service with a service listener */
			mdnsServiceD = MdnsServiceFactory.createDiscovery(SERVICE_TYPE, dnsServiceListener,
					InetAddress.getByName(SERVICE_IP));

			logger.info("dnsServicD: " + mdnsServiceD.toString());

			/* Waiting empirically until the discovery is completed */
			Thread.sleep(5000000);

			/* Logs the serviceInfos with forEach and Lambda */
			dnsServiceListener.getServiceInfos().forEach((k, v) -> logger.info("Key : " + k + " Value : " + v));

			/* Gets the own service */
			ServiceInfo servicInfo = dnsServiceListener.getServiceInfos().get("http://10.3.2.207:1099");

			Thread.sleep(5000);

			assertNotNull(servicInfo);
		} catch (Throwable T) {
			/* Trace error */
			logger.catching(T);
		} finally {
		}
	}
}