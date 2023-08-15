[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/built-by-developers.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)
# Semantic WoT Core


[![Javadocs](http://www.javadoc.io/badge/org.apache.camel/apache-camel.svg?color=brightgreen)](http://www.javadoc.io/doc/org.ict.kura.core.semantic-core)
[![Stack Overflow](https://img.shields.io/:stack%20overflow-Kura--thing--directory-brightgreen.svg)](http://stackoverflow.com/questions/tagged/semantic-core)
[![Gitter](https://img.shields.io/gitter/room/apache/apache-camel.js.svg)](https://gitter.im/apache/apache-camel)

The semantic wot core projects implements the functionality to create assets and channels and thing descriptions

### Introduction
The semantic wot core service is the implementation of the semantic wot api. The service is designed as OSGI declarative service

Dependencies are managed via Maven (see the [POM](url to pom))

## Components
The {project name} application contains the following technologies:
* [Kura](https://github.com/eclipse/kura) - is an OSGi-based Application Framework for M2M Service Gateways.
* [Lombock](https://github.com/projectlombok/lombok) - Library to autogenerate getter/setter and constructors.
* [GSON](https://github.com/google/gson) -  a Java serialization/deserialization library to convert Java Objects into JSON and back.
* [Semantic WoT API](https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/base/semantic-wot-api) - a Kura Semantic layer bundle.
* [Jackson](https://github.com/FasterXML/jackson) - Used for working with JSON.

## Interacting with the service
To use the asset/channel creator, following service reference should be set:

```
@Reference(name = "ThingProvider", service = ThingProvider.class, cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, bind = "setAssetProvider", unbind = "unsetAssetProvider")

public void setThingProvider(ThingProvider thingProvider) {
	this.setThingProvider(thingProvider);
}

public void unsetThingProvider(ThingProvider thingProvider) {
	this.setThingProvider(null);
}
	
```

To create assets and channel, following method should be used:

```
/* Create Asset and Channels for each thing in map */
getThingProvider().createAssetsWithChannels(options.getDriverServicePID(), thing);

```

## Structure of the repository
* The package org.ict.kura.core.asset.creator.impl implements the asset and channel creator
* The package org.ict.kura.core.thing.asset.creator.impl implements the OSGi declarative service for the asset and channel creator
* The package org.ict.kura.core.thing.asset.creator.util implements a util class for the asset and channel creator
* The package org.ict.kura.core.thing.creator.impl implements the thing creator



### Installing
To clone and run this application, you'll need Git and Java installed on your computer. Type this in your command line:

```
# Clone this repository
git clone https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/base/semantic-wot-core

# Build the project in Eclipse
1. Run As => Maven Build and clean install
2. Maven => Update Project

# Build the project with Maven
1. mvn clean install

```
