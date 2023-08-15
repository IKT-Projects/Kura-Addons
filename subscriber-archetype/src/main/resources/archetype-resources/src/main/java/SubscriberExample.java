package org.ict.kura.example.subscriber;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.kura.cloudconnection.listener.CloudConnectionListener;
import org.eclipse.kura.cloudconnection.message.KuraMessage;
import org.eclipse.kura.cloudconnection.subscriber.CloudSubscriber;
import org.eclipse.kura.cloudconnection.subscriber.listener.CloudSubscriberListener;
import org.eclipse.kura.configuration.ConfigurableComponent;
import org.eclipse.kura.message.KuraPayload;
import org.eclipse.kura.message.KuraPosition;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@formatter:off
@Designate(ocd = SubscriberConfiguration.class, factory = true)
@Component(
		immediate = true,
		configurationPolicy = ConfigurationPolicy.REQUIRE,
		name = "org.ict.kura.example.subscriber.SubscriberExample",
		property = "service.pid=org.ict.kura.example.subscriber.SubscriberExample")
//@formatter:on
public class SubscriberExample implements ConfigurableComponent, CloudConnectionListener, CloudSubscriberListener {
	private static final Logger logger = LoggerFactory.getLogger(SubscriberExample.class);

	private CloudSubscriber cloudSubscriber;

	private SubscriberOptions options;

	//@formatter:off
    @Reference(
    		name = "CloudSubscriber", 
    		service = CloudSubscriber.class, 
    		cardinality = ReferenceCardinality.OPTIONAL, 
    		policy = ReferencePolicy.DYNAMIC, 
    		unbind = "unsetCloudSubscriber", 
    		bind = "setCloudSubscriber")
    //@formatter:on

    protected synchronized void setCloudSubscriber(CloudSubscriber cloudSubscriber) {
    	logger.info("setSubscriber...");
		this.cloudSubscriber = cloudSubscriber;
		this.cloudSubscriber.registerCloudSubscriberListener(SubscriberExample.this);
		this.cloudSubscriber.registerCloudConnectionListener(SubscriberExample.this);
	}

    protected synchronized void unsetCloudSubscriber(CloudSubscriber cloudSubscriber) {
    	logger.info("unsetSubscriber...");
		this.cloudSubscriber.unregisterCloudSubscriberListener(SubscriberExample.this);
		this.cloudSubscriber.unregisterCloudConnectionListener(SubscriberExample.this);
		this.cloudSubscriber = null;
	}

	@Activate
	protected void activate(ComponentContext componentContext, Map<String, Object> properties) {
		logger.info("Activate subscriber example");

		dumpProperties("Activate", properties);
		this.options = new SubscriberOptions(properties);

		logger.info("Activate subscriber example done...");
	}

	@Deactivate
	protected void deactivate(ComponentContext componentContext) {
		logger.info("Deactivate subscriber example");

		logger.info("Deactivate subscriber example done...");
	}

	@Modified
	protected void updated(Map<String, Object> properties) {
		logger.info("Update subscriber example");

		dumpProperties("Update", properties);
		this.options = new SubscriberOptions(properties);

		logger.info("Update subscriber example done...");
	}

	@Override
	public void onDisconnected() {
		logger.info("On disconnected");
	}

	@Override
	public void onConnectionLost() {
		logger.info("On connection lost");
	}

	@Override
	public void onConnectionEstablished() {
		logger.info("On connection established");
	}

	@Override
	public void onMessageArrived(KuraMessage message) {
		logger.info("On message arrived... ");
		logReceivedMessage(message);

	}

	/**
	 * 
	 * @param action
	 * @param properties
	 */
	private static void dumpProperties(final String action, final Map<String, Object> properties) {
		final Set<String> keys = new TreeSet<>(properties.keySet());
		for (final String key : keys) {
			logger.info("{} - {}: {}", action, key, properties.get(key));
		}
	}
	
	private void logReceivedMessage(KuraMessage msg) {
        KuraPayload payload = msg.getPayload();
        Date timestamp = payload.getTimestamp();
        if (timestamp != null) {
            logger.info("Message timestamp: {}", timestamp.getTime());
        }

        KuraPosition position = payload.getPosition();
        if (position != null) {
            logger.info("Position latitude: {}", position.getLatitude());
            logger.info("         longitude: {}", position.getLongitude());
            logger.info("         altitude: {}", position.getAltitude());
            logger.info("         heading: {}", position.getHeading());
            logger.info("         precision: {}", position.getPrecision());
            logger.info("         satellites: {}", position.getSatellites());
            logger.info("         speed: {}", position.getSpeed());
            logger.info("         status: {}", position.getStatus());
            logger.info("         timestamp: {}", position.getTimestamp());
        }

        byte[] body = payload.getBody();
        if (body != null && body.length != 0) {
            logger.info("Body lenght: {}", body.length);
        }

        if (payload.metrics() != null) {
            for (Entry<String, Object> entry : payload.metrics().entrySet()) {
                logger.info("Message metric: {}, value: {}", entry.getKey(), entry.getValue());
            }
        }
    }

}
