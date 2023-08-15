[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/built-by-developers.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)

# CoAP-driver

[![Javadocs](http://www.javadoc.io/badge/org.apache.camel/apache-camel.svg?color=brightgreen)](http://www.javadoc.io/doc/org.ict.kura.internal.driver.coap)
[![Stack Overflow](https://img.shields.io/:stack%20overflow-coap--driver-brightgreen.svg)](http://stackoverflow.com/questions/tagged/coap-driver)
[![Gitter](https://img.shields.io/gitter/room/apache/apache-camel.js.svg)](https://gitter.im/apache/apache-camel)

The coap driver handles the communication with the CoAP network.

### Introduction
The coap driver implements the communication between the CoAP network and the kura assets and channels. The implementation based on the WoT descriptions and using the semantic software layer for mapping the metadata and payload for both directions, from and to the coap network. The driver only works with CoAP devices, which supports on application layer WoT descriptions.

Dependencies are managed via Maven (see the [POM](https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/cloud/cloud-telemetry-service/pom.xml))

## Components
This application contains the following technologies:
* [Kura](https://github.com/eclipse/kura) - is an OSGi-based Application Framework for M2M Service Gateways.
* [GSON](https://github.com/google/gson) -  a Java serialization/deserialization library to convert Java Objects into JSON and back
* [Eclipse Californium™ (Cf)](https://eclipse.dev/californium/categories/) - a CoAP framework
* [JmDNS](https://github.com/jmdns/jmdns) - a java based MDNS framework
* [Semantic WoT API](TODO) - a Kura Semantic layer bundle
* [Semantic WoT Core](TODO) - a Kura Semantic layer bundle

## Interacting with the service
The driver implements the communication with CoAP devices and realizes the communication with the Kura assets and channels. The CoAP devices MUST support WoT descriptions on application layer. 

Both directions are implemented, telemetry and command.

Short introduction how the driver works:
 | Step | Info |
 | --------  | --------  |
 | 1 | The driver statrts the MDNS process to find CoAP devices | 
 | 2 | If the driver found a device, the thing recource is requested to receive a WoT description | 
 | 3 | The driver creates the asset with channel based on WoT description. |
 | 4 | The driver request the CoAP device to get the current states of the sensors and actuators |
 | 5 | Based on the WoT description the driver creates observe relations for each sensor and actuator to receive the current states event based. |

The driver is plug-and-play capable. CoaP devices, which joins the network will automatically found via MDNS.   

The keepalived task to send keepalive messages to the cloud-keepalived-service actually is not implemented.

## Structure of the repository
The package org.ict.kura.internal.driver.coap implements the communication between the CoAP network and the Kura system.

The package org.ict.kura.internal.driver.coap.util implements the technology binding.

The package org.ict.kura.internal.driver.coap.client implements a special CoAP client which wraps the Eclipse Californium™ framework to deal with WoT descriptions on application layer.

The package org.ict.kura.internal.driver.coap.mdns implements a MDNS builder class which wraps the JmDNS framework.

### Installing
To clone and run this application, you'll need Git and Java installed on your computer. Type this in your command line:
```
# Clone this repository
git clone https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/driver/coap-driver.git

# Build the project in Eclipse
1. Run As => Maven Build and clean install
2. Maven => Update Project

# Build the project with Maven
1. mvn clean install