package org.ict.kura.example.subscriber;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

//@formatter:off
@ObjectClassDefinition(
		id = "org.ict.kura.example.subscriber.SubscriberExample",
		name = "SubscriberConfiguration",
		description = "This is the configuration of the subscriber example",
		localization = "en_US")
@interface SubscriberConfiguration {
	

    @AttributeDefinition(
            name = "Subscribe semantic topic",
            type = AttributeType.STRING,
            required = true,
            defaultValue = "data",
            description = "Default semantic topic for subscribe the message."
    )
    String semanticTopic();
    
    @AttributeDefinition(name = "Subscriber Target Filter",
			type = AttributeType.STRING,
			required = true,
			cardinality = 0,
			defaultValue = "(kura.service.pid=changeme)",
			description = "Specifies, as an OSGi target filter, the pid of the cloud publisher used to subscribe messages from the cloud platform.")
	String CloudSubscriber_target();

}
