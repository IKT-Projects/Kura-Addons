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
package org.ict.kura.internal.driver.multisensor.mdns;

import java.util.TreeMap;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The DNS Event Listener
 * 
 * @author ICT M. Kuller
 * @version 2020-11-10
 */
public class MdnsServiceListener implements ServiceListener {
	/* The logger instance */
	private static final Logger LOGGER = LoggerFactory.getLogger(MdnsServiceListener.class);

	private TreeMap<String, ServiceInfo> serviceInfos = new TreeMap<>();

	public MdnsServiceListener() {
		/** Trace info */
		LOGGER.info("DNS service listener !");
	}

	public TreeMap<String, ServiceInfo> getServiceInfos() {
		return serviceInfos;
	}

	@Override
	/**
	 * All services with the filter serviceType - e.g. see
	 * jmdns.addServiceListener(serviceType, serviceListener);
	 */
	public void serviceAdded(ServiceEvent event) {
		LOGGER.info("DNS service added: " + event.getInfo().getURLs("_multisesnsor._tcp.local."));
		LOGGER.info("DNS service added: " + event.getInfo().getURLs());
	}

	@Override
	public void serviceRemoved(ServiceEvent event) {
		LOGGER.info("DNS service removed: " + event.getInfo());
		var urls = event.getInfo().getURLs();
		for (String string : urls) {
			serviceInfos.remove(string);
		}
	}

	@Override
	/** All service found */
	public void serviceResolved(ServiceEvent event) {
		LOGGER.info("==============================================================");
		LOGGER.info("DNS service resolved: {} with URL {}", event.getInfo(), event.getInfo().clone().getURLs());
//		serviceInfos.put(event.getInfo().clone().getURL(), event.getInfo());
//		this.getServiceInfos().forEach((k, v) -> LOGGER.info("Key : " + k + " Value : " + v));
		LOGGER.info("Host addresses: {}", event.getInfo().getHostAddresses().toString());
		LOGGER.info("Host port: {}", event.getInfo().getPort());
		LOGGER.info("Service key / name: {} / {}", event.getInfo().getKey(), event.getInfo().getName());
		LOGGER.info("URLs: {}", event.getInfo().getURLs().toString());
		LOGGER.info("Type: {}", event.getInfo().getType());
		LOGGER.info("QualifiedMap: {}", event.getInfo().getQualifiedNameMap().toString());
		LOGGER.info("Property names: {}", event.getInfo().getPropertyNames());
		LOGGER.info("Property string by name: {}", event.getInfo().getPropertyString("MAC"));
		LOGGER.info("==============================================================");
	}
}