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

//*************************************************************************************
/**
 * Demonstrates the DNS register and discover service.
 * 
 * Default servicePath: "path=service:jmx:jmxmp"
 * 
 * @author ICT M. Kuller
 * @version 2020-11-10
 */
// *************************************************************************************
public class MdnsService implements AutoCloseable {
	/* The logger instance */
	private static final Logger LOGGER = LoggerFactory.getLogger(MdnsService.class);

	/* The JmDNS module */
	private final JmDNS jmdns;

	/* The service listener for discovery only */
	private final ServiceListener serviceListener;

	/* The service path without host address and service port */
	private final String servicePath;

	/* The service name */
	private final String serviceType;

	/* The service name */
	private final String serviceName;

	/* The service info */
	private final ServiceInfo serviceInfo;

	/* User defined service informations */
	private final Map<String, String> serviceDescription;

	/* The InetAddress */
	private final InetAddress inetAddress;

	/* The host name */
	private final String hostname;

	/* The port */
	private final int port;

	/** The constructor to build the dns service */
	public MdnsService(MdnsServiceBuilder builder) {
		super();
		this.jmdns = builder.jmdns;
		this.servicePath = builder.servicePath;
		this.serviceType = builder.serviceType;
		this.serviceName = builder.serviceName;
		this.serviceInfo = builder.serviceInfo;
		this.serviceDescription = builder.serviceDescription;
		this.serviceListener = builder.serviceListener;
		this.inetAddress = builder.inetAddress;
		this.hostname = builder.hostname;
		this.port = builder.port;
	}

	// The getter section

	public JmDNS getJmdns() {
		return jmdns;
	}

	public ServiceListener getServiceListener() {
		return serviceListener;
	}

	public String getServicePath() {
		return servicePath;
	}

	public String getServiceType() {
		return serviceType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public ServiceInfo getServiceInfo() {
		return serviceInfo;
	}

	public Map<String, String> getServiceDescription() {
		return serviceDescription;
	}

	public InetAddress getInetAddress() {
		return inetAddress;
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}

	@Override
	public String toString() {
		return "DnsService [jmdns=" + jmdns + ", serviceListener=" + serviceListener + ", servicePath=" + servicePath
				+ ", serviceType=" + serviceType + ", serviceName=" + serviceName + ", serviceInfo=" + serviceInfo
				+ ", serviceDescription=" + serviceDescription + ", inetAddress=" + inetAddress + ", hostname="
				+ hostname + ", port=" + port + "]";
	}

	@Override
	public void close() throws Exception {
		/** Trace info */
		LOGGER.info("DNS service unregister !");

		/** Unregisters the DNS service */
		if (jmdns != null)
			jmdns.unregisterAllServices();
	}

	// *************************************************************************************
	/**
	 * The builder class to build a DNS register or discover service.
	 * 
	 * @author IKT MK
	 * @version 2019-04-16
	 */
	// *************************************************************************************
	public static class MdnsServiceBuilder {
		/** log4j logger reference for this class */
		private static final Logger LOGGER = LoggerFactory.getLogger(MdnsServiceBuilder.class);

		/** The JmDNS module */
		private JmDNS jmdns;

		/** The service listener for discovery only */
		private ServiceListener serviceListener;

		/** The service path without host address and service port */
		private String servicePath = "path=service:jmx:jmxmp";

		/** The service name */
		private String serviceType;

		/** The service name */
		private String serviceName;

		/** The service info */
		private ServiceInfo serviceInfo;

		/** User defined service informations */
		private Map<String, String> serviceDescription;

		/** The InetAddress */
		private InetAddress inetAddress;

		/** The host name */
		private String hostname;

		/** The port */
		private int port;

		// The setter/getter section

		public JmDNS getJmdns() {
			return jmdns;
		}

		public MdnsServiceBuilder setJmdns(JmDNS jmdns) {
			this.jmdns = jmdns;
			return this;
		}

		public ServiceListener getServiceListener() {
			return serviceListener;
		}

		public MdnsServiceBuilder setServiceListener(ServiceListener serviceListener2) {
			this.serviceListener = serviceListener2;
			return this;
		}

		public String getServicePath() {
			return servicePath;
		}

		public MdnsServiceBuilder setServicePath(String servicePath) {
			this.servicePath = servicePath;
			return this;
		}

		public String getServiceType() {
			return serviceType;
		}

		public MdnsServiceBuilder setServiceType(String serviceType) {
			this.serviceType = serviceType;
			return this;
		}

		public String getServiceName() {
			return serviceName;
		}

		public MdnsServiceBuilder setServiceName(String serviceName) {
			this.serviceName = serviceName;
			return this;
		}

		public ServiceInfo getServiceInfo() {
			return serviceInfo;
		}

		public MdnsServiceBuilder setServiceInfo(ServiceInfo serviceInfo) {
			this.serviceInfo = serviceInfo;
			return this;
		}

		public Map<String, String> getServiceDescription() {
			return serviceDescription;
		}

		public MdnsServiceBuilder setServiceDescription(Map<String, String> serviceDescription) {
			this.serviceDescription = serviceDescription;
			return this;
		}

		public InetAddress getInetAddress() {
			return inetAddress;
		}

		public MdnsServiceBuilder setInetAddress(InetAddress inetAddress) {
			this.inetAddress = inetAddress;
			return this;
		}

		public String getHostname() {
			return hostname;
		}

		public MdnsServiceBuilder setHostname(String hostname) {
			this.hostname = hostname;
			return this;
		}

		public int getPort() {
			return port;
		}

		public MdnsServiceBuilder setPort(int port) {
			this.port = port;
			return this;
		}

		/**
		 * Function to build a discovery service instance.
		 * 
		 * @return The DnsService
		 */
		public MdnsService build() {
			LOGGER.debug("DnsServiceBuilder build");

			/** Returns the built dns service */
			return new MdnsService(this);
		}
	}
}