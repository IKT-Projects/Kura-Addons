package org.ict.kura.example.subscriber;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.Map;

public class SubscriberOptions {

	private static final String DEFAULT_CLOUD_PUBLISHER_PID = "";
	private static final String DEFAULT_SEMANTIC_TOPIC = "subscriber";

	private static final String CLOUD_SUBSCRIBER_PROP_NAME = "CloudSubscriber.target";
	private static final String SEMANTIC_TOPIC_PROP_NAME = "semanticTopic";

	private final Map<String, Object> properties;

	public SubscriberOptions(final Map<String, Object> properties) {
		requireNonNull(properties);
		this.properties = properties;
	}

	String getCloudSubscriberPid() {
		String cloudSubscriberPid = DEFAULT_CLOUD_PUBLISHER_PID;
		Object configCloudSubscriberPid = this.properties.get(CLOUD_SUBSCRIBER_PROP_NAME);
		if (nonNull(configCloudSubscriberPid) && configCloudSubscriberPid instanceof String) {
			cloudSubscriberPid = (String) configCloudSubscriberPid;
		}
		return cloudSubscriberPid;
	}


	String getSemanticTopic() {
		String semanticTopic = DEFAULT_SEMANTIC_TOPIC;
		Object configSemanticTopic = this.properties.get(SEMANTIC_TOPIC_PROP_NAME);
		if (nonNull(configSemanticTopic) && configSemanticTopic instanceof String) {
			semanticTopic = (String) configSemanticTopic;
		}
		return semanticTopic;
	}

}
