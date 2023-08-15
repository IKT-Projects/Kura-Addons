package $package;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.Map;

public class PublisherOptions {

	private static final String DEFAULT_CLOUD_PUBLISHER_PID = "";
	private static final int DEFAULT_PUBLISH_RATE = 1000;
	private static final String DEFAULT_SEMANTIC_TOPIC = "publish";

	private static final String CLOUD_PUBLISHER_PROP_NAME = "CloudPublisher.target";
	private static final String PUBLISH_RATE_PROP_NAME = "publish.rate";
	private static final String SEMANTIC_TOPIC_PROP_NAME = "semanticTopic";

	private final Map<String, Object> properties;

	public PublisherOptions(final Map<String, Object> properties) {
		requireNonNull(properties);
		this.properties = properties;
	}

	String getCloudPublisherPid() {
		String cloudPublisherPid = DEFAULT_CLOUD_PUBLISHER_PID;
		Object configCloudPublisherPid = this.properties.get(CLOUD_PUBLISHER_PROP_NAME);
		if (nonNull(configCloudPublisherPid) && configCloudPublisherPid instanceof String) {
			cloudPublisherPid = (String) configCloudPublisherPid;
		}
		return cloudPublisherPid;
	}

	int getPublishRate() {
		int publishRate = DEFAULT_PUBLISH_RATE;
		Object rate = this.properties.get(PUBLISH_RATE_PROP_NAME);
		if (nonNull(rate) && rate instanceof Integer) {
			publishRate = (int) rate;
		}
		return publishRate;
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
