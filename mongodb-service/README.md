[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/built-by-developers.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)
# Kura MongoDB Service


[![Javadocs](http://www.javadoc.io/badge/org.apache.camel/apache-camel.svg?color=brightgreen)](http://www.javadoc.io/doc/org.ict.kura.core.database.mongodb-service)
[![Stack Overflow](https://img.shields.io/:stack%20overflow-Kura--thing--directory-brightgreen.svg)](http://stackoverflow.com/questions/tagged/mongodb-service)
[![Gitter](https://img.shields.io/gitter/room/apache/apache-camel.js.svg)](https://gitter.im/apache/apache-camel)

A service to connect to the MongoDB and provide methods to store and read thing descriptions.

### Introduction
This service implemends functions to store and read thing descriptions to a mongodb. It provides the possibility to configure and establish the connection to an mongoDB. In addition, multiple instances of this service can be initialized.

Dependencies are managed via Maven (see the [POM](https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/database/mongodb-service/-/blob/master/pom.xml))

## Components
The mongoDB service application contains the following technologies:
* [Kura](https://github.com/eclipse/kura) - is an OSGi-based Application Framework for M2M Service Gateways.
* [MongoDB](https://github.com/mongodb/mongo-java-driver) - is an implementation to connect to a mongodb with java.

## Interacting with the service
 | method name  | return type | description | 
 | --------    | ----------- | ------- |
 | findAll | List<Thing> | Returns a list of thing descriptions, saved in the database. |
 | findById | Thing | Retrieves an thing description by its id. |
 | save | Thing | Saves a given thing description |
 | update | Thing | Updates a given thing description |
 | delete | - | Deletes a given thing description |
 | deleteById | - | Deletes a thing description with given id |
 | deleteAll | - | Deletes all thing descriptions |

## Structure of the repository
* The package org.ict.kura.core.database.mongo includes the interface, which provides the methods to store values.

* The package org.ict.kura.core.database.mongo.internal includes the OSGi declarative service implementation and classes to configure the bundle.

### Configuration
To create a connection to a MongoDB you need a running MongoDB database instance.
Following credentials are needed: 

- **database ip** The ip of the running mongoDB database 
- **database port** The port of the running mongoDB database 
- **username** The username for authentication to the mongoDB database
- **password** The password for authentication to the mongoDB database
- **database name** The database name which the documents should be stored


### Installing
To clone and run this application, you'll need Git and Java installed on your computer. Type this in your command line:
```
# Clone this repository
git clone https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/database/mongodb-service

# Build the project in Eclipse
1. Run As => Maven Build and clean install
2. Maven => Update Project

# Build the project with Maven
1. mvn clean install

```
