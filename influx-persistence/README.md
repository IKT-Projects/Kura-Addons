[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/built-by-developers.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)
# InfluxDB-Persistence


[![Javadocs](http://www.javadoc.io/badge/org.apache.camel/apache-camel.svg?color=brightgreen)](http://www.javadoc.io/doc/org.ict.kura.core.persistence.influxdb-persistence)
[![Stack Overflow](https://img.shields.io/:stack%20overflow-Kura--thing--directory-brightgreen.svg)](http://stackoverflow.com/questions/tagged/influxdb-persistence)
[![Gitter](https://img.shields.io/gitter/room/apache/apache-camel.js.svg)](https://gitter.im/apache/apache-camel)

The influxdb persistence service is used for saving sensor and actuator conditional values to an influx database.

### Introduction
The influxdb persistence service captures all channel state changes using the {@link EventHandler} of the {@link EventAdmin} service and

Dependencies are managed via Maven (see the [POM](url to pom))


## Components
The {project name} application contains the following technologies:
* [Kura](https://github.com/eclipse/kura) - is an OSGi-based Application Framework for M2M Service Gateways.
* [InfluxDB](https://github.com/influxdata/influxdb) - is a timeseries database for storing data
* [GSON](https://github.com/google/gson) -  a Java serialization/deserialization library to convert Java Objects into JSON and back

## Requiremends
* Eclpse Kura v.5.0.1
* Java Version 11
* InfluxDB v.2.0
* Kura InfluxDB Service

## Interacting with the service
1. The service subscribes to the OSGi {@link EventAdmin} and receives telemetry data from the drivers on the topic telemetry/* with the following example OSGi {@link Event} properties: 

 | thingName  | propertyName | value | 
 | --------    | ----------- | ------- |
 | multisensor | temperature | {"time": 1690356551064, "temperature": 20.1} | 


## Structure of the repository
The package org.ict.kura.core.persistence.influx.internal includes the OSGi declarative service implementation and classes to configure the bundle.

### Configuration
To run this bundle, a influxDB server and the kura [influxDB service](https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/database/influx-service) need to be running. 
With the flag: 

- **InfluxDb Target Filter** a instance of the InfluxDB service can be selected. 

It is possible to run this bundle more times.

### Installing
To clone and run this application, you'll need Git and Java installed on your computer. Type this in your command line:
```
# Clone this repository
git clone https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/database/influx-persistence

# Build the project in Eclipse
1. Run As => Maven Build and clean install
2. Maven => Update Project

# Build the project with Maven
1. mvn clean install

```

