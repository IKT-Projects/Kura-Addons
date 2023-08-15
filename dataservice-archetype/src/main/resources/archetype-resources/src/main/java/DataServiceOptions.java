package $package;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.Map;

public class DataServiceOptions {

	private final Map<String, Object> properties;

	private static final String MQTT_TOPIC = "mqtt.topic";
	private static final String MQTT_QOS = "mqtt.qos";

	public DataServiceOptions(Map<String, Object> properties) {
		requireNonNull(properties, "Properties cannot be null");
		this.properties = properties;

	}

	public String getTopic() {
		String topic = "";
		final Object obj = this.properties.get(MQTT_TOPIC);
		if (nonNull(obj) && obj instanceof String) {
			topic = (String) obj;
		}
		return topic;

	}

	public int getQos() {
		int qos = 0;
		final Object obj = this.properties.get(MQTT_QOS);
		if (nonNull(obj) && obj instanceof Integer) {
			qos = (Integer) obj;
		}
		return qos;
	}

}
