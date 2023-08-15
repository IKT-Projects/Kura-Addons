[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/built-by-developers.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)
# Driver-archetype

This is a archetype for a kura wot driver implementation

### Introduction
A archetype for a simple driver implementation, included basic implementation with relevant and optional services.


### Installing
To clone and run this application, you'll need Git and Java installed on your computer. Type this in your command line:
```
# Clone this repository
git clone https://gitlab.ikt.fh-dortmund.de:9443/ikt/kura/archetype/driver-archetype.git

# Build the project with Maven
mvn clean install

# Create new project from archetype
mvn archetype:generate  \
-DarchetypeGroupId=org.ict.kura.driver \
-DarchetypeArtifactId=driver-archetype \
-DarchetypeVersion=0.0.1-SNAPSHOT \
-DgroupId=custom-groupId \
-DartifactId=custom-service \
-Dversion=0.0.1-SNAPSHOT \
-DprojectName=custom-project-name
```

