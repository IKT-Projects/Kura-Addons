[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/built-by-developers.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)

# Cloud-Command-Service

[![Javadocs](http://www.javadoc.io/badge/org.apache.camel/apache-camel.svg?color=brightgreen)](http://www.javadoc.io/doc/org.ict.kura.core.cloud.command.provider)
[![Stack Overflow](https://img.shields.io/:stack%20overflow-cloud--command--service-brightgreen.svg)](http://stackoverflow.com/questions/tagged/cloud-command-service)
[![Gitter](https://img.shields.io/gitter/room/apache/apache-camel.js.svg)](https://gitter.im/apache/apache-camel)

The cloud command service handles state changes of actuators from a MQTT broker.

### Introduction
The cloud command service receives state changes via a MQTT broker using the OSGi {@link DataService}, redirects the state changes to the corresponding asset and response the request directly without a feedback from the asset.

The command payload data format is JSON. The service supports two semantic strategies:
1. A simple payload based on the "Web of Things" description to use with your own MQTT broker.
2. A payload based on the specification of the ThingsBoard 

Dependencies are managed via Maven (see the [POM](https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/cloud/cloud-command-service/pom.xml))

## Components
This application contains the following technologies:
* [Kura](https://github.com/eclipse/kura) - is an OSGi-based Application Framework for M2M Service Gateways.
* [GSON](https://github.com/google/gson) -  a Java serialization/deserialization library to convert Java Objects into JSON and back

## Interacting with the service
The service implements two request/response and convert strategies.

1. WoT strategy
- The service received a command request on the topic things/# without sending a response. 

| Topic | Payload | Note | 
 | --------  | ----------- | ------- |
 | things/thingName/actions/actionName | {"onOff": true} | thingName is the asset, actionName is the channel

- The new conditional status will be send with the #cloud-telemetry-service back to the MQTT broker. 

| Topic | Payload | Note | 
 | --------  | ----------- | ------- |
 | things/thingName/properties/propertyName |  {"time": 1690356551064, "onOff": true} | thingName is the asset, propertyName is the channel

2. ThingsBoard strategy: 

- The service received a command request from the ThingsBoard.

| Topic | Payload | Note |
 | --------  | ----------- | ----------- |
 | v1/gateway/rpc | {"device": "TestDeviceA", "data": {"id": 51, "method": "onOff", "params": {"onOff":true}}} | device is the asset, method is the channel, params is the WoT payload |

- The service sends a command response back to the ThingsBoard.

| Topic | Payload | Note |
 | --------  | ----------- | ----------- | 
 | v1/gateway/rpc | {"device": "TestDeviceA", "id": 51, "data": {"success": true/false}} | device is the asset

## Structure of the repository
The package org.ict.kura.core.cloud.command.provider includes the OSGi declarative service implementation, the classes to configure the bundle.

The package org.ict.kura.core.cloud.command.provider.util includes the converter implementations.

### Installing
To clone and run this application, you'll need Git and Java installed on your computer. Type this in your command line:
```
# Clone this repository
git clone https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/cloud/cloud-command-service

# Build the project in Eclipse
1. Run As => Maven Build and clean install
2. Maven => Update Project

# Build the project with Maven
1. mvn clean install
