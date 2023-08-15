[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/built-by-developers.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)
# Kura InfluxDB Service Bundle


[![Javadocs](http://www.javadoc.io/badge/org.apache.camel/apache-camel.svg?color=brightgreen)](http://www.javadoc.io/doc/org.ict.kura.core.database.influxdb-service)
[![Stack Overflow](https://img.shields.io/:stack%20overflow-Kura--thing--directory-brightgreen.svg)](http://stackoverflow.com/questions/tagged/influxdb-service)
[![Gitter](https://img.shields.io/gitter/room/apache/apache-camel.js.svg)](https://gitter.im/apache/apache-camel)

A service to connect to the InfluxDB and provide methods to store and read data.

### Introduction

This service implemends functions to store and read data to a InfluxDB. It provides the possibility to configure and establish the connection to an InfluxDB. In addition, multiple instances of this service can be initialized.

Dependencies are managed via Maven (see the [POM](https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/database/influx-service/-/blob/master/pom.xml))

## Components
The Kura-InfluxDB-Service contains the following technologies:
* [Kura](https://github.com/eclipse/kura) - is an OSGi-based Application Framework for M2M Service Gateways.
* [InfluxDB](https://github.com/influxdata/influxdb) - is a timeseries database for storing data

## Requiremends
* Eclpse Kura v.5.0.1
* Java Version 11
* InfluxDB v.2.0

## Interacting with the InfluxDB service
The influxDB service provides the following methods:

 | method name  | return type | description | 
 | --------    | ----------- | ------- |
 | delete | - | Deletes a time series with the given time period in the configured bucket and organization. Define a predicate with further drop informations |
 | save | - | Save an element |
 | savePoint | boolean | Save a list of pointer elements |
 | getLastValue | Map<String, Value> | Returns the last value for all passed fields  |
 | getLastValue | value | Returns the last value for one passed field |
 | getHistory | Map<String, List<Value>> | Return all values for all passed fields regarding the start and stop time  |
 | getHistory | List<Value> | Return all values for a field regarding the start and stop time |
 | getHistory | List<Point> | Return all values for a field regarding the start and stop time |
 | getFirstValue | Map<String, Value> | Returns the first value for all passed fields |
 | getFirstValue | Value | Returns the first value for one passed field |
 | getMinValue | Map<String, Value> | Returns the min value for all passed fields |
 | getMinValue | Value | Returns the min value for one passed field |
 | getMaxValue | Map<String, Value> | Returns the max value for all passed fields |
 | getMaxValue | Value | Returns the max value for one passed field |
 | countValues | Map<String, Value> | Count all values for all passed fields |
 | countValues | Value | Count all values for one passed field |
 | sumValues | Map<String, Value> | Sums all values for all passed fields |
 | sumValues | Value | Sums all values for one passed field |
 | meanValue | Map<String, Value> | Return the mean value for all passed fields |
 | meanValue | Value | Returns the mean value for one passed field |
 | integralValue | Map<String, Value> | Returns the integral value for all passed fields |
 | integralValue | Value | Returns the integral value for one passed field |
 | differenceValues | Map<String, List<Value>> | Returns the difference value for all passed fields |
 | differenceValues | List<Value> | Returns the difference value for one passed field |


## Structure of the repository
* The package org.ict.kura.core.database.influx includes the interface, which provides the methods to store values.

* The package org.ict.kura.core.database.influx.internal includes the OSGi declarative service implementation and classes to configure the bundle.

* The package org.ict.kura.core.database.influx.internal.value includes the class which defines the value.

### Configuration
To create a connection to a InfluxDB you need a running InfluxDB Database instance.
Following credentials are needed: 

- **database ip** The ip of the running influxDB database 
- **database port** The port of the running influxDB database 
- **token id** The token for authentication to the influxDB database
- **organisation id** The organisation if of the user 
- **bucket name** The bucket name in which values are stored 


### Installing
To clone and run this application, you'll need Git and Java installed on your computer. Type this in your command line:
```
# Clone this repository
git clone https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/database/influx-service

# Build the project in Eclipse
1. Run As => Maven Build and clean install
2. Maven => Update Project

# Build the project with Maven
1. mvn clean install

```


