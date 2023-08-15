[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/built-by-developers.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)

# MQTT-driver

[![Javadocs](http://www.javadoc.io/badge/org.apache.camel/apache-camel.svg?color=brightgreen)](http://www.javadoc.io/doc/org.ict.kura.internal.driver.mqtt)
[![Stack Overflow](https://img.shields.io/:stack%20overflow-mqtt--driver-brightgreen.svg)](http://stackoverflow.com/questions/tagged/mqtt-driver)
[![Gitter](https://img.shields.io/gitter/room/apache/apache-camel.js.svg)](https://gitter.im/apache/apache-camel)

The MQTT driver handles the communication with MQTT capable devices.

### Introduction
The MQTT driver implements the communication with the MQTT devices, which supports WoT descriptions. The driver implementation based on the WoT descriptions and using the semantic software layer for creating assets and channels.

Dependencies are managed via Maven (see the [POM](https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/driver/mqtt-driver.git)

## Components
This application contains the following technologies:
* [Kura](https://github.com/eclipse/kura) - is an OSGi-based Application Framework for M2M Service Gateways.
* [GSON](https://github.com/google/gson) -  a Java serialization/deserialization library to convert Java Objects into JSON and back
* [JmDNS](https://github.com/jmdns/jmdns) - a java based MDNS framework
* [Semantic WoT API](TODO) - a Kura Semantic layer bundle
* [Semantic WoT Core](TODO) - a Kura Semantic layer bundle

## Interacting with the service
The driver using MDNS to discover multsensor devices. The MDNS service type is in general _wot_tcp.local, but this parameter is configerable via the Kura web admin. The device MUST its ID via MDNS, e.g. for constraint devices the MAC address. The device ID MUST corresponding with the WoT description ID.
 
The driver uses the Kura {@link DataService} to communicate with the MQTT broker. So this service must be configured first via the Kura web admin.
 
Short introduction how the driver works:
 | Step | Info |
 | --------  | --------  |
 | 1 | The driver starts the MDNS process to find WoT devices | 
 | 2 | If the driver found a device, the thing recource is requested via MQTT to receive/subscribe a WoT description on the topic things/< ID > | 
 | 3 | If the driver receives a WoT description, then the assets and channels are created based on the WoT description. |
| 4 | The driver subscribes to all properties, that are configered in the WoT descriptions Form elements. |

## Structure of the repository
The package org.ict.kura.internal.driver.mqtt implements the communication between the MQTT broker and the Kura system.

The package org.ict.kura.internal.driver.mqtt.util implements the technology binding.

The package org.ict.kura.internal.driver.coap.mdns implements a MDNS builder class which wraps the JmDNS framework.

### Installing
To clone and run this application, you'll need Git and Java installed on your computer. Type this in your command line:
```
# Clone this repository
git clone https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/driver/sml-driver.git

# Build the project in Eclipse
1. Run As => Maven Build and clean install
2. Maven => Update Project

# Build the project with Maven
1. mvn clean install
```

```
