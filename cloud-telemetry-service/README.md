[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/built-by-developers.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)
# Cloud-Telemetry-Service

[![Javadocs](http://www.javadoc.io/badge/org.apache.camel/apache-camel.svg?color=brightgreen)](http://www.javadoc.io/doc/org.ict.kura.core.cloud.telemetry.provider)
[![Stack Overflow](https://img.shields.io/:stack%20overflow-cloud--telemetry--service-brightgreen.svg)](http://stackoverflow.com/questions/tagged/cloud-telemetry-service)
[![Gitter](https://img.shields.io/gitter/room/apache/apache-camel.js.svg)](https://gitter.im/apache/apache-camel)

The cloud telemetry service sends sensor and actuator conditional values to a configured MQTT broker.

### Introduction
The cloud telemetry service captures all channel state changes using the {@link EventHandler} of the {@link EventAdmin} service and redirects them to
a mqtt broker using the {@link DataService}. The {@link DataService} endpoint (MQTT broker) can be changed at runtime via the web admin - cloud service!

The telemetry payload data format is JSON. The service supports two semantic strategies:
1. A simple payload based on the "Web of Things" description to use with your own MQTT broker.
2. A payload based on the specification of the ThingsBoard 

Dependencies are managed via Maven (see the [POM](https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/cloud/cloud-telemetry-service/pom.xml))

## Components
This application contains the following technologies:
* [Kura](https://github.com/eclipse/kura) - is an OSGi-based Application Framework for M2M Service Gateways.
* [GSON](https://github.com/google/gson) -  a Java serialization/deserialization library to convert Java Objects into JSON and back

## Interacting with the service
1. The service subscribes to the OSGi {@link EventAdmin} and receives telemetry data from the drivers on the topic telemetry/* with the following example OSGi {@link Event} properties: 

 | thingName  | propertyName | value | 
 | --------    | ----------- | ------- |
 | multisensor | temperature | {"time": 1690356551064, "temperature": 20.1} | 


2. To redirect/publish the telemetry data to the MQTT broker two convert strategies are implemented: 

 | Converter    | Topic       | Payload | 
 | --------    | ----------- | ------- |
 | WoT         | things/multisensor/properties/temperature | {"time": 1690356551064, "temperature": 20.1} | 
 | ThingsBoard | v1/gateway/telemetry  |   {"Device A":[{"ts": 1690356551064, "values": {"temperature": 20.1}}]}    | 


## Structure of the repository
The package org.ict.kura.core.cloud.telemetry.provider includes the OSGi declarative service implementation, the classes to configure the bundle and a Task class, which implements the forwarding of the payload to the MQTT broker.

The package org.ict.kura.core.cloud.telemetry.provider.util includes the converter implementations.

### Installing
To clone and run this application, you'll need Git and Java installed on your computer. Type this in your command line:
```
# Clone this repository
git clone https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/cloud/cloud-telemetry-service

# Build the project in Eclipse
1. Run As => Maven Build and clean install
2. Maven => Update Project

# Build the project with Maven
1. mvn clean install

```
