[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/built-by-developers.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)
# Semantic WoT Api


[![Javadocs](http://www.javadoc.io/badge/org.apache.camel/apache-camel.svg?color=brightgreen)](http://www.javadoc.io/doc/org.ict.kura.api.semantic-api)
[![Stack Overflow](https://img.shields.io/:stack%20overflow-Kura--thing--directory-brightgreen.svg)](http://stackoverflow.com/questions/tagged/semantic-api)
[![Gitter](https://img.shields.io/gitter/room/apache/apache-camel.js.svg)](https://gitter.im/apache/apache-camel)

The semantic wot api is the abstraction for the semantic wot layer in kura.

### Introduction
The semantic wot api provides several classes and interfaces for the creation of thing descriptions, the creation of assets and channels, and an abstraction of the implementation of a thing driver.

Dependencies are managed via Maven (see the [POM](url to pom))

## Components
The {project name} application contains the following technologies:
* [Kura](https://github.com/eclipse/kura) - is an OSGi-based Application Framework for M2M Service Gateways.
* [Lombock](https://github.com/projectlombok/lombok) - Library to autogenerate getter/setter and constructors.
* [GSON](https://github.com/google/gson) -  a Java serialization/deserialization library to convert Java Objects into JSON and back.
* [Jackson](https://github.com/FasterXML/jackson) - Used for working with JSON.

## Interacting with the service
Every driver should extends the implementation with the ThingDriver class.

```
public class YourThingDriver extends ThingDriver<YourBindingConfig> implements Driver, ConfigurableComponent {
```

The configuration file is used to create the thing descriptions. To read this files, the class org.ict.kura.asset.creator.thing.util.ConfigReader should be used.

The senantic wot api offers following interaction types

 | property  | action |  
 | --------  | ----------- |
 | Temperature |  |
 | Pressure |  |
 | CurrentLevel |  |
 | FlowRate |  |
 | VolumeTotal |  |
 | MeasuredTotal |  |
 | LocationLength |  |
 | LocationWidth |  |
 | CarbonDioxide |  |
 | Tvoc |  |
 | Wind |  |
 | Position | Position |
 | Battery |  |
 | SlatAngle | SlatAngle |
 | Humidity |  |
 | Illuminanc |  |
 | Lock |  |
 | SwitchStatus | OnOff |
 | Motion |  |
 | OpenClose |  |
 | UpDown | UpDown |
 | Dimming |  |
 | ActiveEnergyImported |  |
 | ActiveEnergyExported |  |
 | ReactiveEnergyImported |  |
 | ReactiveEnergyExported |  |
 | ApparentEnergy |  |
 | GeneralElectricity |  |
 | GeneralPower |  |
 | Voltage |  |
 | Current |  |
 | ApparentPower |  |
 | TotalPowerFactor |  |
 | ActivePowerCustom |  |
 | TotalActivePower |  |
 | Angle |  |
 | ActivePowerA |  |
 | ActivePowerB |  |
 | ActivePowerC |  |
 | LowBattery |  |
 |  | TempOffSet |
 |  | TargetTemp |
 
 
## Structure of the repository
* The package org.ict.kura.asset.creator provides the interface for creating assets and channels
* The package org.ict.kura.asset.creator.thing.util implements methods to create thing descriptions and process them
* The package org.ict.kura.asset.creator.util implements the basic channel descriptor
* The package org.ict.kura.thing.creator provides interfaces for the thing creator
* The package org.ict.kura.thing.model provides basic model
* The package org.ict.kura.driver.thing provides the thing driver implementation
* The package org.ict.kura.util holds constant variables


### Installing
To clone and run this application, you'll need Git and Java installed on your computer. Type this in your command line:
```
# Clone this repository
git clone https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/base/semantic-wot-api

# Build the project in Eclipse
1. Run As => Maven Build and clean install
2. Maven => Update Project

# Build the project with Maven
1. mvn clean install

# Read configuration file and create thing descriptions
import org.ict.kura.thing.creator.ThingCreator;
import org.ict.kura.thing.model.ThingsConfig;
import org.ict.model.wot.core.Thing;

List<Thing> things = new ArrayList<>();
DefaultConfigReader reader = new DefaultConfigReader();
ThingsConfig thingsConfig = reader.readConfig(new File(options.getThingFolderLocation()));
ThingCreator creator = thingCreatorImpl.getThingCreator();
things = creator.createFromConfig(thingsConfig);

# Create assets and channels by thing descriptions

for (Thing thing : things) {
	Map<String, Object> eventAdminProperties = new HashMap<>();

	// Create a thin key referenced to the thing description
	eventAdminProperties.put("thing", thing);

	// We need a reference to the driver pid
	eventAdminProperties.put("driverPid", options.getDriverServicePID());

	// We need a topic where we publish the thing description
	String topic = "things/" + thing.getTitle();
	LOGGER.info("Send thing to EventAdmin with Topic: " + topic);

	// Gets the {@link EventAdmin} instance and publish the thing description
	getEA().sendEvent(new Event(topic, eventAdminProperties));
}

```
