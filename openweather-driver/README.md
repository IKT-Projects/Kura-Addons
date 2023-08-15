[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/built-by-developers.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)
# OpenWeatherMap Driver


[![Javadocs](http://www.javadoc.io/badge/org.apache.camel/apache-camel.svg?color=brightgreen)](http://www.javadoc.io/doc/org.ict.kura.driver.openweather-provider)
[![Stack Overflow](https://img.shields.io/:stack%20overflow-Kura--thing--directory-brightgreen.svg)](http://stackoverflow.com/questions/tagged/openweather-provider)
[![Gitter](https://img.shields.io/gitter/room/apache/apache-camel.js.svg)](https://gitter.im/apache/apache-camel)

The openWeatherMap driver handles the communication with the openWeatherMap api.

### Introduction
The openWeatherMap driver implements the communication between the openWeatherMap api and the kura assets and channels. The implementation based on the WoT description and using the semantic software layer for mapping the metadata and payload from the openWeatherMap api.

OpenWeatherMap provides weather forecasts, nowcasts and history in a fast and elegant way.

Dependencies are managed via Maven (see the [POM](url to pom))

## Components
The openWeatherMap driver contains the following technologies:
* [Kura](https://github.com/eclipse/kura) - is an OSGi-based Application Framework for M2M Service Gateways.
* [GSON](https://github.com/google/gson) -  a Java serialization/deserialization library to convert Java Objects into JSON and back
* [Unirest](https://github.com/Kong/unirest-java) - is used for the RESTFull communication with the openWeatherMap api
* [Semantic WoT API](https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/base/semantic-wot-api) - a Kura Semantic layer bundle
* [Semantic WoT Core](https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/base/semantic-wot-core) - a Kura Semantic layer bundle


## Structure of the repository
* The package org.ict.kura.internal.driver.openweather includes the OSGi declarative service implementation and classes to configure the bundle.

* The package org.ict.kura.internal.driver.openweather.client includes the rest client implementation to access the openWeatherMap api.

* The package org.ict.kura.internal.driver.openweather.util includes util classes.

### Configuration
For requesting the weather data, you need to create a account on [OpenWeatherMap]() and create an api token.

Following configuration parameter are needed: 

- **api key** The created api key for accessing the rest api 
- **city name** The name of the city for which the weather data is to be retrieved  
- **state code** State code for the city e.g DE or EN
- **polling interval** The interval in which current weather data can be retrieved. 
- **thing folder or configuration file location** The location of the thing or of the configuration file to creating assets and channels.


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
