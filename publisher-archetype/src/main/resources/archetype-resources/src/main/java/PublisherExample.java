package $package;

import static java.util.Objects.nonNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.kura.cloudconnection.listener.CloudConnectionListener;
import org.eclipse.kura.cloudconnection.listener.CloudDeliveryListener;
import org.eclipse.kura.cloudconnection.message.KuraMessage;
import org.eclipse.kura.cloudconnection.publisher.CloudPublisher;
import org.eclipse.kura.configuration.ConfigurableComponent;
import org.eclipse.kura.message.KuraPayload;
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
@Designate(ocd = PublisherConfiguration.class, factory = true)
@Component(
		immediate = true,
		configurationPolicy = ConfigurationPolicy.REQUIRE,
		name = "$package.PublisherExample",
		property = "service.pid=$package.PublisherExample")
//@formatter:on
public class PublisherExample implements ConfigurableComponent, CloudConnectionListener, CloudDeliveryListener {
	private static final Logger logger = LoggerFactory.getLogger(PublisherExample.class);

	private ScheduledExecutorService worker;
	private ScheduledFuture<?> handle;

	private CloudPublisher cloudPublisher;

	private PublisherOptions options;

	private int value = 0;

	//@formatter:off
    @Reference(
    		name = "CloudPublisher", 
    		service = CloudPublisher.class, 
    		cardinality = ReferenceCardinality.OPTIONAL, 
    		policy = ReferencePolicy.DYNAMIC, 
    		unbind = "unsetCloudPublisher", 
    		bind = "setCloudPublisher")
    //@formatter:on

    protected synchronized void setCloudPublisher(CloudPublisher cloudPublisher) {
		logger.info("setPublisher...");
		this.cloudPublisher = cloudPublisher;
		this.cloudPublisher.registerCloudConnectionListener(PublisherExample.this);
		this.cloudPublisher.registerCloudDeliveryListener(PublisherExample.this);
	}

    protected synchronized void unsetCloudPublisher(CloudPublisher cloudPublisher) {
    	logger.info("unsetPublisher...");
		this.cloudPublisher.unregisterCloudConnectionListener(PublisherExample.this);
		this.cloudPublisher.unregisterCloudDeliveryListener(PublisherExample.this);
		this.cloudPublisher = null;
	}

	@Activate
	protected void activate(ComponentContext componentContext, Map<String, Object> properties) {
		logger.info("Activate publisher example");

		dumpProperties("Activate", properties);
		this.options = new PublisherOptions(properties);

		this.worker = Executors.newSingleThreadScheduledExecutor();

		runSchedular();
	}

	@Deactivate
	protected void deactivate(ComponentContext componentContext) {
		logger.info("Deactivate publisher example");

		this.worker.shutdown();

		logger.info("Deactivate publisher example done...");
	}

	@Modified
	protected void updated(Map<String, Object> properties) {
		logger.info("Update publisher example");

		dumpProperties("Update", properties);
		this.options = new PublisherOptions(properties);

		runSchedular();
	}

	@Override
	public void onMessageConfirmed(String messageId) {
		logger.info("Ob Message Confirmed: '%s'", messageId);
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

	/**
	 * 
	 */
	private void doPublish() {

		KuraPayload payload = new KuraPayload();
		payload.setTimestamp(new Date());
		payload.addMetric("value", value);

		try {
			if (nonNull(this.cloudPublisher)) {
				Map<String, Object> messageProperties = new HashMap<>();
				messageProperties.put("semanticTopic", options.getSemanticTopic());

				KuraMessage message = new KuraMessage(payload, messageProperties);

				String messageId = this.cloudPublisher.publish(message);
				logger.info("Published to message: {} with ID: {}", message, messageId);
			}
		} catch (Exception e) {
			logger.error("Cannot publish: ", e);
		}

		value++;

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
	
	private void runSchedular() {
		if (this.handle != null) {
			this.handle.cancel(true);
		}

		int pubrate = options.getPublishRate();
		this.handle = this.worker.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				doPublish();
			}
		}, 0, pubrate, TimeUnit.SECONDS);

		logger.info("Update publisher example done...");
	}
}
