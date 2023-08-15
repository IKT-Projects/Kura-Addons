package $package;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

//@formatter:off
@ObjectClassDefinition(
		id = "org.ict.kura.example.dataService.DataServiceExample",
		name = "DataServiceExample Configuration",
		description = "The Configuration for the DataServiceExample",
		localization = "en_US")

@interface DataServiceConfig {
	
	@AttributeDefinition(
			name = "DataService Target Filter",
			type = AttributeType.STRING, 
			required = true,
			cardinality = 0, 
			defaultValue = "(kura.service.pid=changeme)",
			description = "Specifies an OSGi target filter, the pid of the Data Service used to publish messages to the cloud platform.")
	String DataService_target();

	@AttributeDefinition(
			name = "MQTT topic",
			type = AttributeType.STRING,
			required = true,
			cardinality = 0, 
			defaultValue = "",
			description = "MQTT Topic")
	String mqtt_topic();
	
	@AttributeDefinition(
			name = "MQTT qos",
			type = AttributeType.INTEGER,
			required = true,
			cardinality = 0, 
			defaultValue = "0",
			description = "MQTT QoS")
	String mqtt_qos();


}
