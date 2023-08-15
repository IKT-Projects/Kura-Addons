[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/built-by-developers.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)
# AVM Driver


[![Javadocs](http://www.javadoc.io/badge/org.apache.camel/apache-camel.svg?color=brightgreen)](http://www.javadoc.io/doc/org.ict.kura.driver.avm-provider)
[![Stack Overflow](https://img.shields.io/:stack%20overflow-Kura--thing--directory-brightgreen.svg)](http://stackoverflow.com/questions/tagged/avm-provider)
[![Gitter](https://img.shields.io/gitter/room/apache/apache-camel.js.svg)](https://gitter.im/apache/apache-camel)

The avm driver handles the communication with the avm fritzbox api.

### Introduction
The avm driver implements the communication between the avm fritzbox api and the kura assets and channels. The implementation based on the WoT descriptions and using the semantic software layer for mapping the metadata and payload for both directions, from and to the avm api. The AVM driver does not require a thing description or configuration, it retrieves all device information and creates the required thing descriptions using the thing creator.

Dependencies are managed via Maven (see the [POM](url to pom))

## Components
The {project name} application contains the following technologies:
* [Kura](https://github.com/eclipse/kura) - is an OSGi-based Application Framework for M2M Service Gateways.
* [GSON](https://github.com/google/gson) -  a Java serialization/deserialization library to convert Java Objects into JSON and back
* [Semantic WoT API](https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/base/semantic-wot-api) - a Kura Semantic layer bundle
* [Semantic WoT Core](https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/base/semantic-wot-core) - a Kura Semantic layer bundle
* [Unirest](https://github.com/Kong/unirest-java) - is used for the RESTFull communication with the avm api


## Interacting with the service
The driver implements the http communication to the avm fritzbox, converts the payload to a WoT playload and back. Device informations are converted to a thing description by the thing creator implementation. A scheduler is polling new values by a defined interval.

The telemetry and command direction are implemented.

### Configuration
Following configuration parameter are needed: 

- **user name** The username for the smart home user
- **password** The password for the smart home user
- **ip address** the ip adress of the fritz box router
- **polling interval** The interval in which current values can be retrieved. 

## Structure of the repository
* The package org.ict.kura.internal.driver.avm includes the OSGi declarative service implementation and classes to configure the bundle.
* The package org.ict.kura.internal.driver.avm.api includes the rest client implementation to access the avm fritzbox api.
* The package org.ict.kura.internal.driver.avm.data describes the data model of the avm fritzbox device model


### Installing
To clone and run this application, you'll need Git and Java installed on your computer. Type this in your command line:
```
# Clone this repository
git clone https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/driver/openweather-driver

# Build the project in Eclipse
1. Run As => Maven Build and clean install
2. Maven => Update Project

# Build the project with Maven
1. mvn clean install

```
