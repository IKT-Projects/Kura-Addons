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
package org.ict.kura.internal.driver.coap;

import java.util.Map;

import org.ict.kura.internal.driver.coap.client.CustomCoapClient;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * The driver configuration for coap driver.
 * 
 * Used by kura webinterface to setup and start {@link CustomCoapClient) and
 * request data from selected server.
 * 
 * The parameter "coap.connection.ip" is read out from the
 * {@link CoapDriver#updated(Map)} </br>
 * and defines target address of internal {@link CustomCoapClient} to request
 * server data.
 * 
 * The parameter "coap.connection.port" is read out from the
 * {@link CoapDriver#updated(Map)}</br>
 * and defines used port for communication between client and server.
 * 
 * The parameter "coap.connection.timeOut" is read out from the
 * {@link CoapDriver#updated(Map)}</br>
 * and defines acceptable time interval between send request and received
 * response by server.
 * 
 * With this annotation the metatype xml file in the META-INF folder is created,
 * which is used to create a configuration page in kura web ui. This allows the
 * bundle to be configured via the UI.
 * 
 * @author IKT B. Helgers
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2020-10-20
 */
//@formatter:off
@ObjectClassDefinition(id = "org.ict.kura.driver.coap", name = "Coap Driver", description = "Configuration parameters | Coap Driver", localization = "en_us")

@interface CoapConfig {

	/** Defines a boolean field indicating if service should be started */
	@AttributeDefinition(name = "Enable", type = AttributeType.BOOLEAN, required = true, defaultValue = "false", description = "Enables the service")
	String common_enable();

	/** Defines a field for the type of the mdns service */
	@AttributeDefinition(
			name = "MDNS service type", type = AttributeType.STRING, required = true, defaultValue = "_udp.local.", description = "The MDNS discover service type, e.g. _tcp.local.")
	String mdns_service_type();

	/** Defines a fieled for the name of the mdns service */
	@AttributeDefinition(
			name = "MDNS service name", type = AttributeType.STRING, required = true, defaultValue = "_wot.", description = "The MDNS discover service name, e.g. the multisenssor_wot., the room controller _instahubX.")
	String mdns_service_name();

	/** Defines a field for the ip address and port of the CoAP server */
	@AttributeDefinition(name = "CoAP server address", type = AttributeType.STRING, required = false, defaultValue = "", description = "CoAP server IP address and port (Layout e.g.: $ip:$port or $ip:$port;$ip:$port for more than one address). If this is empty the driver uses the MDNS discovery service to find CoAP server in the network.")
	String coap_connection_server_addresses();

	/** Defines a field for the connection timeOut between request and response. Input "0" sets client to wait indefinitely. */
	@AttributeDefinition(name = "CoAP server connection timeout", type = AttributeType.INTEGER, required = true, defaultValue = "0", description = "Client timeOut for requests.")
	String coap_connection_timeOut();
//@formatter:on	
}