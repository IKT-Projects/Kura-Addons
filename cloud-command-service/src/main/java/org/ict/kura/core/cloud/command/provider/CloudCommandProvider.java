/*
 * Copyright © 2023 Institut fuer Kommunikationstechnik - FH-Dortmund (codebase.ikt@fh-dortmund.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ict.kura.core.cloud.command.provider;

import java.util.Map;

import org.eclipse.kura.asset.AssetService;
import org.eclipse.kura.cloudconnection.listener.CloudConnectionListener;
import org.eclipse.kura.cloudconnection.message.KuraMessage;
import org.eclipse.kura.cloudconnection.publisher.CloudPublisher;
import org.eclipse.kura.cloudconnection.subscriber.CloudSubscriber;
import org.eclipse.kura.cloudconnection.subscriber.listener.CloudSubscriberListener;
import org.eclipse.kura.configuration.ConfigurableComponent;
import org.eclipse.kura.data.DataService;
import org.ict.kura.core.cloud.command.provider.util.CommandContext;
import org.ict.kura.core.cloud.command.provider.util.CommandStrategyTbTwoWaySync;
import org.ict.kura.core.cloud.command.provider.util.CommandStrategyWotOneWay;
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

/**
 * The cloud command service receives state changes via a MQTT broker using the
 * {@link DataService}, redirects the state changes to the corresponding asset
 * and response the request directly without a feedback from the asset.
 * 
 * The {@link DataService} endpoint (MQTT broker) for subscribe and publish can
 * be changed at runtime via the web admin - cloud service!
 * 
 * @author ICT M. Biskup
 * @author ICT M. Kuller
 * @version 2023-07-26
 * 
 * @see {@link CommandStrategyTbTwoWaySync} and {@link CommandStrategyWotOneWay}
 *      javadoc for more details.
 */

@Designate(ocd = CloudCommandProviderConfig.class, factory = true)
@Component(immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE, service = { ConfigurableComponent.class,
		CloudConnectionListener.class, CloudSubscriberListener.class }, property = {
				"service.pid=org.ict.kura.core.cloud.command.provider.CloudCommandProvider" })
public class CloudCommandProvider implements ConfigurableComponent, CloudConnectionListener, CloudSubscriberListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(CloudCommandProvider.class);
	private static final String APP_ID = "org.ict.kura.core.cloud.command.provider.CloudCommandProvider";

	/* The asset service */
	private AssetService assetService;

	/* The cloud endpoint (MQTT) to subscribe, receive command requests */
	private CloudSubscriber cloudSubscriber;

	/* The cloud endpoint (MQTT) to publish, send command responses */
	private CloudPublisher cloudPublisher;

	/* The {@link ComponentContext} with basic informations about this component. */
	@SuppressWarnings("unused")
	private ComponentContext componentContext;

	/* The service options */
	private CloudCommandProviderOptions options;

	/* Saves the strategy to convert the payload; */
	private CommandContext commandContext = new CommandContext();

	/* The properties from the web admin */
	Map<String, Object> properties;

	@Activate
	protected void activate(ComponentContext componentContext, Map<String, Object> properties) {
		LOGGER.info("Bundle " + APP_ID + " has started!");
		/*
		 * Saves the component context - we use this to get the bundle context
		 * (EventHandler)
		 */
		this.componentContext = componentContext;
		/* Updates the configuration */
		doUpdate(properties);
	}

	@Deactivate
	protected void deactivate(ComponentContext componentContext) {
		LOGGER.info("Bundle " + APP_ID + " has stopped!");
		/* Closes all resources */
		doDeactivate();
	}

	@Modified
	public void updated(Map<String, Object> properties) {
		LOGGER.info("Bundle " + APP_ID + " has updated!");
		/* Deactivates all components */
		doDeactivate();

		/* Updates all components with new configuration */
		doUpdate(properties);
	}

	private void doUpdate(Map<String, Object> properties) {
		LOGGER.info("doUpdate...");
		this.properties = properties;
		/* Sets the command payload convert strategy */
		this.options = new CloudCommandProviderOptions(properties);
		switch (this.options.getStrategy()) {
		case "wot":
			commandContext.setStrategy(new CommandStrategyWotOneWay());
			break;
		case "thingsBoard":
			// The instance of {@link CloudPublisher} seems to be valid
			commandContext.setStrategy(new CommandStrategyTbTwoWaySync(this.cloudPublisher));
			break;
		default:
			LOGGER.info(
					"INVALID command convert strategy configuration, working with the default command strategy WoT");
		}
	}

	private void doDeactivate() {
		LOGGER.info("doDeactivate...");

	}

	@Reference(name = "CloudPublisher", service = CloudPublisher.class, cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC, unbind = "unsetCloudPublisher", bind = "setCloudPublisher")
	public void setCloudPublisher(CloudPublisher cloudPublisher) {
		this.cloudPublisher = cloudPublisher;
	}

	public void unsetCloudPublisher(CloudPublisher cloudPublisher) {
		this.cloudPublisher = null;
	}

	@Reference(name = "CloudSubscriber", service = CloudSubscriber.class, cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC, unbind = "unsetCloudSubscriber", bind = "setCloudSubscriber")
	protected synchronized void setCloudSubscriber(CloudSubscriber cloudSubscriber) {
		LOGGER.info("setSubscriber...");
		this.cloudSubscriber = cloudSubscriber;
		this.cloudSubscriber.registerCloudSubscriberListener(this);
		this.cloudSubscriber.registerCloudConnectionListener(this);
	}

	protected synchronized void unsetCloudSubscriber(CloudSubscriber cloudSubscriber) {
		LOGGER.info("unsetSubscriber...");
		this.cloudSubscriber.unregisterCloudSubscriberListener(this);
		this.cloudSubscriber.unregisterCloudConnectionListener(this);
		this.cloudSubscriber = null;
	}

	@Reference(name = "AssetService", service = AssetService.class, cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "unsetAssetService", bind = "setAssetService")
	protected void setAssetService(AssetService assetService) {
		this.assetService = assetService;
	}

	protected void unsetAssetService(AssetService assetService) {
		this.assetService = null;
	}

	@Override
	public void onConnectionEstablished() {
		LOGGER.info("On connection established...");
	}

	@Override
	public void onDisconnected() {
		LOGGER.warn("On disconnected...");

	}

	@Override
	public void onConnectionLost() {
		LOGGER.warn("On connection lost...");

	}

	@Override
	public void onMessageArrived(KuraMessage message) {
		try {
			commandContext.convert(message, assetService);
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}
}
