package $package;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

//@formatter:off
@ObjectClassDefinition(
		id = "org.ict.kura.example.publisher.PublisherExample",
		name = "PublisherConfiguration",
		description = "This is the configuration of the publisher example",
	    localization = "en_US")
@interface PublisherConfiguration {
	
	@AttributeDefinition(
            name = "Publish rate",
            type = AttributeType.INTEGER,
            defaultValue = "30",
            required = true,
            min = "10",
            description = "Default message publishing rate in seconds (min 60)."
    )
    String publish_rate();

    @AttributeDefinition(
            name = "Publish semantic topic",
            type = AttributeType.STRING,
            required = true,
            defaultValue = "data",
            description = "Default semantic topic to publish the message to."
    )
    String semanticTopic();
    
    @AttributeDefinition(name = "Publisher Target Filter",
			type = AttributeType.STRING,
			required = true,
			cardinality = 0,
			defaultValue = "(kura.service.pid=changeme)",
			description = "Specifies, as an OSGi target filter, the pid of the cloud publisher used to publish messages to the cloud platform.")
	String CloudPublisher_target();

}
