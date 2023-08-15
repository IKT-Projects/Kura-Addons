[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/built-by-developers.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)

# KNX-driver

[![Javadocs](http://www.javadoc.io/badge/org.apache.camel/apache-camel.svg?color=brightgreen)](http://www.javadoc.io/doc/org.ict.kura.internal.driver.knx)
[![Stack Overflow](https://img.shields.io/:stack%20overflow-knx--driver-brightgreen.svg)](http://stackoverflow.com/questions/tagged/knx-driver)
[![Gitter](https://img.shields.io/gitter/room/apache/apache-camel.js.svg)](https://gitter.im/apache/apache-camel)

The KNX driver handles the communication with the KNX network.

### Introduction
The KNX driver implements the communication between the KNX network and the kura assets and channels. The implementation based on the WoT descriptions and using the semantic software layer for mapping the metadata and payload for both directions, from and to the KNX network.

Dependencies are managed via Maven (see the [POM](https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/cloud/cloud-telemetry-service/pom.xml))

## Components
This application contains the following technologies:
* [Kura](https://github.com/eclipse/kura) - is an OSGi-based Application Framework for M2M Service Gateways.
* [GSON](https://github.com/google/gson) -  a Java serialization/deserialization library to convert Java Objects into JSON and back
* [Calimero](TODO) - a KNX Java framework
* [KNX Converter](TODO) - a Java KNX ETS to WoT Converter 
* [Semantic WoT API](TODO) - a Kura Semantic layer bundle
* [Semantic WoT Core](TODO) - a Kura Semantic layer bundle

## Interacting with the service
The driver implements the communication with a KNX IP Gateway, converts the KNX payload to a WoT payload and back, maps the KNX group addresses to WoT ActionAffordance and PropertyAffordance and realizes the communication with the Kura assets and channels. 

Both directions are implemented, telemetry and command.

The driver monitores the connection to the KNX IP gateway. If the connection is interrupted, a reconnect will be automatically executed periodically. 
Depending on the connection status, a periodically keepalived message will be sent via the {@link EventAdmin} to the [cloud-keepalived-service](TODO). 

The supported DPTs of this driver are:
 | DPT IDs |
 | --------  |
 | 1.001, 1.002, 1.003, 1.007, 1.008, 1.010, 1.019 | 
 | 5.001, 5.010 | 
 | 7.013 | 
 | 9.001, 9.002, 9.004, 9.005, 9.007, 9.008 | 
 | 14.056 | 

The configuration of the driver is based on WoT descriptions. These must be created with the [KNX Converter](TODO) and stored in a folder. The location of the folder can be configured with the help of the Kura Web Admin. 

Short introduction how the driver works:
 | Step | Info |
 | --------  | --------  |
 | 1 | The driver reads the WoT descriptions from a folder location and creates assets and channels | 
 | 2 | The driver establishes the communication to the KNX IP gateway | 
 | 3 | A KNX network scan procces is started to record the current states of the sensors and actuators (this is only possible, if a group address is readable) |
 | 4 | A task is started to monitor the connection to the KNX IP gateway and try to reconnect the gateway, if the connection was interrupted. This task also sends a keepalived message periodically to the cloud-keepalived-service. |

## Structure of the repository
The package org.ict.kura.internal.driver.knx implements the communication between the KNX network and the Kura system.

The package org.ict.kura.internal.driver.knx.util implements the technology binding.

The package org.ict.kura.internal.driver.knx.util.dpt implements the KNX DPT to WoT payload mapping and back (both directions).

### Installing
To clone and run this application, you'll need Git and Java installed on your computer. Type this in your command line:
```
# Clone this repository
git clone https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/driver/knx-driver.git

# Build the project in Eclipse
1. Run As => Maven Build and clean install
2. Maven => Update Project

# Build the project with Maven
1. mvn clean install


