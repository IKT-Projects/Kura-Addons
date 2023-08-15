package $package;

import java.util.Map;

import org.eclipse.kura.KuraException;
import org.eclipse.kura.data.DataService;
import org.eclipse.kura.data.listener.DataServiceListener;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@formatter:off
@Designate(
		ocd = DataServiceConfig.class, 
		factory = true)
@Component(
		name = "org.ict.kura.example.dataService.DataServiceExample", 
		immediate = true, 
		configurationPolicy = ConfigurationPolicy.REQUIRE, 
		property = "service.pid=org.ict.kura.example.dataService.DataServiceExample"
)
//@formatter:on
public class DataServiceExample implements DataServiceListener {
	private static final Logger logger = LoggerFactory.getLogger(DataServiceExample.class);

	private DataService dataService;

	private DataServiceOptions options;

	@Activate
	protected void activate(ComponentContext componentContext, Map<String, Object> properties) {
		logger.info("Activate dataservice example");
		options = new DataServiceOptions(properties);

		try {
			dataService.subscribe(options.getTopic(), options.getQos());
		} catch (KuraException e) {
			logger.error("", e);
		}
	}

	@Deactivate
	protected void deactivate(ComponentContext componentContext) {
		logger.info("Deactivate dataservice example");
	}

	@Modified
	protected void updated(Map<String, Object> properties) {
		logger.info("Update dataservice example");
		try {
			dataService.unsubscribe(options.getTopic());
		} catch (KuraException e) {
			logger.error("", e);
		}
		options = new DataServiceOptions(properties);
		try {
			dataService.subscribe(options.getTopic(), options.getQos());
		} catch (KuraException e) {
			logger.error("", e);
		}
	}

	//@formatter:off
	@Reference(
			name = "DataService", 
			service = DataService.class, 
			cardinality = ReferenceCardinality.OPTIONAL, 
			policy = ReferencePolicy.DYNAMIC, 
			policyOption = ReferencePolicyOption.GREEDY, 
			unbind = "unsetDataService", 
			bind = "setDataService"
	)
	//@formatter:on
	protected synchronized void setDataService(DataService dataService) {
		logger.info("setDataService...");
		this.dataService = dataService;
		this.dataService.addDataServiceListener(this);
	}

	protected synchronized void unsetDataService(DataService dataService) {
		logger.info("unsetDataService...");
		if (this.dataService == dataService) {
			this.dataService.removeDataServiceListener(this);
			this.dataService = null;
		}
	}

	@Override
	public void onConnectionEstablished() {
		logger.info("onConnectionEstablished...");

	}

	@Override
	public void onDisconnecting() {
		logger.info("onDisconnecting...");

	}

	@Override
	public void onDisconnected() {
		logger.info("onDisconnected...");

	}

	@Override
	public void onConnectionLost(Throwable cause) {
		logger.info("onConnectionLost...");

	}

	@Override
	public void onMessageArrived(String topic, byte[] payload, int qos, boolean retained) {
		logger.info("onMessageArrived topic: {}, payload: {}, qos: {}, retained:{}", topic, new String(payload), qos,
				retained);

	}

	@Override
	public void onMessagePublished(int messageId, String topic) {
		logger.info("onMessagePublished id: {}, topic: {}", messageId, topic);

	}

	@Override
	public void onMessageConfirmed(int messageId, String topic) {
		logger.info("onMessageConfirmed id: {}, topic: {}", messageId, topic);

	}

}
