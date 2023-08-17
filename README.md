[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/built-by-developers.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)
# Eclipse Kura Projects


[![Javadocs](http://www.javadoc.io/badge/org.apache.camel/apache-camel.svg?color=brightgreen)](http://www.javadoc.io/doc/org.ict.kura.service.thingdirectory)
[![Stack Overflow](https://img.shields.io/:stack%20overflow-Kura--thing--directory-brightgreen.svg)](http://stackoverflow.com/questions/tagged/thing-directory)
[![Gitter](https://img.shields.io/gitter/room/apache/apache-camel.js.svg)](https://gitter.im/apache/apache-camel)

This is a repository containing various projects for [Eclipse Kura™](https://eclipse.org/kura "Eclipse Kura™").

### Introduction
This repository holds several modules for the eclipse kura project. A big focus of these modules was to implement the WoT (Web of Things) standard defined by the W3C on the driver level to ensure a certain interoperability. Through a strong abstraction of the driver implementation, this could be reduced to the low level implementation. Different methods are provided, which enable the generation of the WoT Thing Description via Driver, but also the direct processing of the WoT Thing Description is possible. These methods were tested in the present driver implementations. Modules were also developed for cloud communication (telemetry, command), which follow the WoT standard. To store the telemetry data as well as the WoT Thing description on the gateway system itself, modules were developed that enable communication with other database systems (InfluxDB, MongoDB).

This projcets based on the kura version 5.0.1 and are tested with the [kura emulator](https://www.eclipse.org/downloads/download.php?file=/kura/releases/5.0.1/user_workspace_archive_5.0.1.zip)

Dependencies are managed via Maven (see the[url to pom](https://github.com/IKT-Projects/Kura-Projects/blob/main/pom.xml))

## Components

This repository contains following projects:

### Semantic-WoT-Api

The semantic wot api is the abstraction for the semantic wot layer in kura.

### Semantic-WoT-Core

The semantic wot core projects implements the functionality to create assets and channels and thing descriptions

### MongoDB-Service 

A service to connect to the MongoDB and provide methods to store and read thing descriptions.

### InfluxDB-Service 

A service to connect to the InfluxDB and provide methods to store and read data.

### InfluxDB-Persistence-Service

The influxdb persistence service is used for saving sensor and actuator conditional values to an influx database.

### Cloud-Telemetry-Service

The cloud telemetry service sends sensor and actuator conditional values to a configured MQTT broker.

### Cloud-Command-Service

The cloud command service handles state changes of actuators from a MQTT broker.

### Cloud-Keepalive-Service

The cloud keepalived service sends a keepalived message for each asset for ThingsBoard

### MQTT-Driver

The MQTT driver handles the communication with MQTT capable devices.

### AVM-Driver

The avm driver handles the communication with the avm fritzbox api.

### KNX-Driver

The KNX driver handles the communication with the KNX network.

### COAP-Driver

The coap driver handles the communication with the CoAP network.

### Openweather-Driver

The openWeatherMap driver handles the communication with the openWeatherMap api.

### Driver-Archetype

A archetype for a kura wot driver implementation

### Dataservice-Archetype

A archetype for a kura dataservice implementation

### Publisher-Archetype

A archetype for a kura publisher implementation

### Subscriber-Archetype

A archetype for a kura subscriber implementation
  

## Requirements
1. Create a Bundle of the following projects, using the jar with dependencies:
  -  [org.ict.model.jsonld:Context](https://github.com/IKT-Projects/W3C-WoT-BoT-Context.git)
  -  [org.ict.model:bot](https://github.com/IKT-Projects/W3C-BuildingTopologyOntology.git)
  -  [org.ict.model:wot](https://github.com/IKT-Projects/W3C-WebOfThings.git)
  ```
  In eclipse it is easy to create a new bundle from sources:
    1. file -> new -> other -> Plugin-Development -> Plugin from existing jars -> next
    2. Press "Add External" and search for the jar's
    3. Select the previously named jars and press next
    4. Fulfill the plugin properties and press finish
    5. Open the MANIFEST.MF and go to the tab runtime
    6. Press "Add" type in the filter org.ict and select all dependencies
    7. Type in the filter rdf and select all dependencies
    8. Safe the MANIFEST.MF
    9. Right klick on the plugin project -> export -> Plugin-Development -> Deployable plugins and fragements -> next -> select directory -> Finish
  ```
  
2. Extend Kura with the following bundles:
   - jsml-1.1.2.jar
   - jrxtx-1.0.1.jar
   - json-20220924.jar
   - stax2-api-4.2.jar
   - jackson-module-jaxb-annotations-2.11.3.jar
   - jackson-dataformat-xml-2.11.3.jar
   - reactive-streams-1.0.4.jar
   - rxjava-3.1.5.jar
   - validation-api-1.1.0.Final.jar
   - swagger-models-2.1.5.jar
   - swagger-jaxrs2-servlet-initializer-2.1.0.jar
   - swagger-jaxrs2-2.1.0.jar
   - swagger-integration-2.1.5.jar
   - swagger-core-2.1.5.jar
   - swagger-annotations-2.1.5.jar
   - snakeyaml-1.27.jar
   - snakeyaml-1.24.jar
   - org.everit.json.schema-1.5.1.jar
   - org.eclipse.kura.rest.configuration.provider-1.0.0-SNAPSHOT.jar
   - org.eclipse.kura.request.handler.jaxrs-1.0.0-SNAPSHOT.jar
   - mongo-java-driver-3.12.11.jar
   - mimepull-1.9.14.jar
   - json-20200518.jar
   - json-20160810.jar
   - joda-time-2.10.5.jar
   - jmdns-3.5.1.jar
   - jaxws-api-2.3.1.jar
   - jaxrs-ri-2.33.jar
   - jaxb-api-2.3.1.jar
   - javax.ws.rs-api-2.1.1.jar
   - javax.persistence-2.2.1.jar
   - javax.inject-2.5.0-b62.jar
   - javax.annotation-api-1.3.2.jar
   - javassist-3.22.0-GA.jar
   - jakarta.websocket-api-2.0.0.jar
   - jackson-module-jaxb-annotations-2.10.1.jar
   - jackson-jaxrs-json-provider-2.10.1.jar
   - jackson-jaxrs-base-2.10.1.jar
   - jackson-datatype-jsr310-2.11.3.jar
   - jackson-datatype-jsr310-2.10.1.jar
   - jackson-dataformat-yaml-2.11.3.jar
   - jackson-dataformat-yaml-2.10.1.jar
   - jackson-databind-2.11.3.jar
   - jackson-databind-2.10.1.jar
   - jackson-core-2.11.3.jar
   - jackson-core-2.10.1.jar
   - jackson-annotations-2.12.0.jar
   - jackson-annotations-2.11.3.jar
   - jackson-annotations-2.10.1.jar
   - guava-19.0.jar
   - gson-2.8.5.jar
   - element-connector-2.2.3.jar
   - commons-validator-1.5.1.jar
   - commons-lang3-3.7.jar
   - commons-digester-1.8.1.jar
   - classgraph-4.8.90.jar
   - classgraph-4.6.32.jar
   - californium-core-2.2.3.jar


### Installing
To clone and run this repository, you'll need Git and Java installed on your computer. Type this in your command line:
```
# Clone this repository
git clone https://github.com/IKT-Projects/Kura-Projects.git

# Build the project in Eclipse
1. Run As => Maven Build and clean install
2. Maven => Update Project

# Build the project with Maven
1. mvn clean install

```
