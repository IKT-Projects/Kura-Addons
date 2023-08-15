[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/built-by-developers.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)

# Cloud-Keepalived-Service

[![Javadocs](http://www.javadoc.io/badge/org.apache.camel/apache-camel.svg?color=brightgreen)](http://www.javadoc.io/doc/org.ict.kura.core.cloud.keepalived.provider)
[![Stack Overflow](https://img.shields.io/:stack%20overflow-cloud--keepalived--service-brightgreen.svg)](http://stackoverflow.com/questions/tagged/cloud-keepalived-service)
[![Gitter](https://img.shields.io/gitter/room/apache/apache-camel.js.svg)](https://gitter.im/apache/apache-camel)

The cloud keepalived service sends a keepalived message for each asset.

### Introduction
The keepalied payload data format is JSON. The service supports one semantic strategy:
1. A payload based on the specification of the ThingsBoard 

Dependencies are managed via Maven (see the [POM](https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/cloud/cloud-telemetry-service/pom.xml))

## Components
This application contains the following technologies:
* [Kura](https://github.com/eclipse/kura) - is an OSGi-based Application Framework for M2M Service Gateways.
* [GSON](https://github.com/google/gson) -  a Java serialization/deserialization library to convert Java Objects into JSON and back

## Interacting with the service
The service implements only one convert strategy.

1. This strategy implements the ThingsBoard MQTT keepalied message. The message is sent only at asset level, not at channel level.

| Topic | Payload | Note | 
 | --------  | ----------- | ------- |
 | v1/gateway/connect | {"device":"multisensor"} | device is the asset


## Structure of the repository
The package org.ict.kura.core.cloud.keepalived.provider includes the OSGi declarative service implementation, the classes to configure the bundle and the keepalived task.

The package org.ict.kura.core.cloud.keepalived.provider.util includes the converter implementations.

### Installing
To clone and run this application, you'll need Git and Java installed on your computer. Type this in your command line:
```
# Clone this repository
git clone https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/cloud/cloud-keepalived-service

# Build the project in Eclipse
1. Run As => Maven Build and clean install
2. Maven => Update Project

# Build the project with Maven
1. mvn clean install

