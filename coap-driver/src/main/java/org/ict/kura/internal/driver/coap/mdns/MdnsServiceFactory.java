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
package org.ict.kura.internal.driver.coap.mdns;

import java.net.InetAddress;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the MDNS service factory to easy create service register and
 * discovery instances.
 * 
 * @author ICT M. Kuller
 * @version 2021-02-04
 */
public class MdnsServiceFactory {
	/* The logger instance */
	private static final Logger LOGGER = LoggerFactory.getLogger(MdnsServiceFactory.class);

	/** Constructor - private because creating an instance is forbidden */
	private MdnsServiceFactory() {
		// Do nothing
	}

	/**
	 * Builds a mdns register service.
	 * 
	 * @param type               the service type
	 * @param name               the service name
	 * @param port               the port of the service
	 * @param serviceDescription the user defined additional service informations
	 * @return the MdnsService
	 */
	public static MdnsService createRegister(String serviceType, String serviceName,
			Map<String, String> serviceDescription, int port, InetAddress inetAddress) throws Throwable {
		LOGGER.debug("MdnsServiceBuilder register");

		try {
			/**
			 * Determines the IP address of a host, given the host's name.
			 */
			String hostname = InetAddress.getByName(inetAddress.getHostName()).toString();

			LOGGER.info("Creates a jmdns instance with hostname " + hostname);

			/** Creates a mdns service instance */
			JmDNS jmdns = JmDNS.create(inetAddress);

			ServiceInfo serviceInfo = ServiceInfo.create(serviceType, serviceName, port, 1, 1, true,
					serviceDescription);

			/** Registers the mdns service */
			jmdns.registerService(serviceInfo);

			/** Creates the MdnsService */
			return new MdnsService.MdnsServiceBuilder().setServiceType(serviceType).setServiceName(serviceName)
					.setServiceDescription(serviceDescription).setPort(port).setInetAddress(inetAddress)
					.setHostname(hostname).setJmdns(jmdns).setServiceInfo(serviceInfo).build();
		} catch (Throwable T) {
			LOGGER.error("" + T);
			throw T;
		}
	}

	/**
	 * Builds a mdns discovery service.
	 * 
	 * @param type            the service type
	 * @param serviceListener the user implementation of the service listener
	 * @return the MdnsService
	 */
	public static MdnsService createDiscovery(String serviceType, ServiceListener serviceListener,
			InetAddress inetAddress) throws Throwable {
		LOGGER.debug("DnsServiceBuilder discovery");

		try {
			/**
			 * Determines the IP address of a host, given the host's name.
			 */
			String hostname = InetAddress.getByName(inetAddress.getHostName()).toString();

			/** Creates a dns service instance */
			JmDNS jmdns = JmDNS.create(inetAddress);

			/** Adds the DNS service listener */
			jmdns.addServiceListener(serviceType, serviceListener);

			/** Creates the MdnsService */
			return new MdnsService.MdnsServiceBuilder().setServiceType(serviceType).setServiceListener(serviceListener)
					.setInetAddress(inetAddress).setHostname(hostname).setJmdns(jmdns).build();
		} catch (Throwable T) {
			LOGGER.error("" + T);
			throw T;
		}
	}
}