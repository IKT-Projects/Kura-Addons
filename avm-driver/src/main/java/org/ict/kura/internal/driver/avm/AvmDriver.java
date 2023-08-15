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
package org.ict.kura.internal.driver.avm;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.kura.channel.ChannelRecord;
import org.eclipse.kura.configuration.ConfigurableComponent;
import org.eclipse.kura.crypto.CryptoService;
import org.eclipse.kura.driver.Driver;
import org.eclipse.kura.driver.PreparedRead;
import org.ict.kura.asset.creator.thing.util.ThingPreparedRead;
import org.ict.kura.driver.thing.ThingDriver;
import org.ict.kura.internal.driver.avm.api.AvmApi;
import org.ict.kura.internal.driver.avm.api.AvmUpdateThread;
import org.ict.kura.internal.driver.avm.api.data.Device;
import org.ict.kura.internal.driver.avm.api.data.Devicelist;
import org.ict.kura.internal.driver.avm.things.ThingDect2XX;
import org.ict.kura.internal.driver.avm.things.ThingDect301;
import org.ict.kura.internal.driver.avm.things.ThingDect400;
import org.ict.kura.internal.driver.avm.things.ThingDect440;
import org.ict.kura.internal.driver.avm.things.ThingDect500;
import org.ict.kura.thing.creator.ThingProvider;
import org.ict.model.wot.core.ActionAffordance;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * This is a kura avm driver implementation based on the WoT description.
 * 
 * @author IKT M. Biskup
 * @version 2023-01-31
 */
/* Annotation to point a {@link AvmDriverConfig} class */
@Designate(ocd = AvmDriverConfig.class, factory = true)
/* Annotation to create the component.xml from source code */
@Component(name = "org.ict.kura.driver.avm", immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE, property = "service.pid=org.ict.kura.driver.avm")
public class AvmDriver extends ThingDriver<AvmBindingConfig> implements Driver, ConfigurableComponent {
	/* The dummy driver logger instance */
	private static final Logger LOGGER = LoggerFactory.getLogger(AvmDriver.class);
	/* The unique driver app id - in this case the driver package name */
	private static final String APP_ID = "org.ict.kura.driver.avm";

	private static final String HREF = "http://localhost:8080/things";

	private static final String FRITZ_BASE_URL = "http://%s/webservices/homeautoswitch.lua";

	/* The avm options */
	private AvmDriverOptions options;

	/* The kura crypto service instance */
	private CryptoService cryptoService;

	/* The map of technology bindings */
	private Map<String, AvmBindingConfig> avmBindingConfigs = new HashMap<>();

	/* The AVM Api to connect to the fritz box */
	private AvmApi avmApi;

	/* The update thread to fetch device informations */
	private AvmUpdateThread avmUpdateThread;

	/**
	 * Binding method which starts the bundle, see component.xml, is called by the
	 * OSGi framework
	 */
	@Activate
	protected void activate(ComponentContext componentContext, Map<String, Object> properties) {
		LOGGER.info("Bundle " + APP_ID + " has started with config!");

		/* Updates the configuration */
		doUpdate(properties);
	}

	/**
	 * Binding method to shutdown the bundle, see component.xml, is called by the
	 * OSGi framework.
	 */
	@Deactivate
	protected void deactivate() {
		LOGGER.info("Bundle " + APP_ID + " has stopped!");

		/* Closes all resources */
		doDeactivate();
	}

	/**
	 * Handles the configuration updates.
	 * 
	 * @param properties the configuration parameters, which are configured via the
	 *                   kura admin web interface
	 */
	@Modified
	public void updated(Map<String, Object> properties) {
		LOGGER.info("Bundle " + APP_ID + " has updated!");
		/*
		 * Deactivates all configurations, because it isn't able to modify an existing
		 * configuration
		 */
		doDeactivate();

		/* Updates all components with new configuration */
		doUpdate(properties);
	}

	private void doUpdate(Map<String, Object> properties) {
		LOGGER.info("doUpdate..., updated");
		try {
			this.options = new AvmDriverOptions(properties, cryptoService);

			LOGGER.info(options.toString(true));
			if (options.getFritzboxUsername() != null && !options.getFritzboxUsername().equals("")) {

				// Create AVM Api instance
				String baseUrl = String.format(FRITZ_BASE_URL, options.getFritzboxIp());
				this.avmApi = new AvmApi(baseUrl, options.getFritzboxIp(), options.getFritzboxUsername(),
						options.getFritzboxPassword());

				try {
					// Get list of AVM devices and iterate over them to create thing descriptions by
					// device product name. Thing descriptions are send over the event admin to the
					// thing creater.
					Devicelist list = this.avmApi.getDeviceList();
					LOGGER.info(list.toString());

					for (Device device : list.getDevice()) {
						String modDeviceName = device.getName().replace(" ", "").replace("!", "-").replace("#", "-");

						String topic = "things/" + modDeviceName;

						Map<String, Object> eventProperties = new HashMap<>();
						eventProperties.put("driverPid", options.getDriverServicePID());

						switch (device.getProductname()) {

						case "FRITZ!DECT 200":
						case "FRITZ!DECT 210":
							LOGGER.info("Send thing to EventAdmin with Topic: " + device.getName());
							eventProperties.put("thing", new ThingDect2XX(HREF, modDeviceName, device.getName(),
									device.getName(), device.getIdentifier()).getThing());

							getEA().sendEvent(new Event(topic, eventProperties));
							break;

						case "FRITZ!DECT 301":
							LOGGER.info("Send thing to EventAdmin with Topic: " + device.getName());
							eventProperties.put("thing", new ThingDect301(HREF, modDeviceName, device.getName(),
									device.getName(), device.getIdentifier()).getThing());

							getEA().sendEvent(new Event(topic, eventProperties));
							break;

						case "FRITZ!DECT 400":
							LOGGER.info("Send thing to EventAdmin with Topic: " + device.getName());
							eventProperties.put("thing", new ThingDect400(HREF, modDeviceName, device.getName(),
									device.getName(), device.getIdentifier()).getThing());

							getEA().sendEvent(new Event(topic, eventProperties));
							break;

						case "FRITZ!DECT 440":
							LOGGER.info("Send thing to EventAdmin with Topic: " + device.getName());
							eventProperties.put("thing", new ThingDect440(HREF, modDeviceName, device.getName(),
									device.getName(), device.getIdentifier()).getThing());

							getEA().sendEvent(new Event(topic, eventProperties));
							break;

						case "FRITZ!DECT 500":
							if (device.getIdentifier().contains("-1")) {
								LOGGER.info("Send thing to EventAdmin with Topic: " + device.getName());
								eventProperties.put("thing", new ThingDect500(HREF, modDeviceName, device.getName(),
										device.getName(), device.getIdentifier()).getThing());

								getEA().sendEvent(new Event(topic, eventProperties));

							}
							break;
						default:
							LOGGER.warn("Thing not found for: " + device.getProductname());
							break;
						}

					}

				} catch (Throwable e) {
					LOGGER.error("", e);
				}
				this.avmUpdateThread = new AvmUpdateThread(avmApi, options.getPollinterval(),
						getListenerBindingConfigurations(), avmBindingConfigs);
			}

		} catch (Exception e) {
			LOGGER.error("", e);
		}

	}

	/**
	 * Deactivates all resources means deletes all assets and devices - bundle
	 * shutdown.
	 */
	private void doDeactivate() {
		LOGGER.info("doDeactivate...");
		try {
			if (avmUpdateThread != null) {
				this.avmUpdateThread.close();
			}

		} catch (IOException e) {
			LOGGER.error("", e);
		}
		this.avmUpdateThread = null;
		this.avmApi = null;

	}

	@Reference(name = "CryptoService", service = CryptoService.class, cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "unsetCryptoService", bind = "setCryptoService")
	protected synchronized void setCryptoService(final CryptoService cryptoService) {
		if (isNull(this.cryptoService)) {
			this.cryptoService = cryptoService;
		}
	}

	protected synchronized void unsetCryptoService(final CryptoService cryptoService) {
		if (this.cryptoService == cryptoService) {
			this.cryptoService = null;
		}
	}

	/*
	 * This annotation adds the event admin service methods to the
	 * OSGI-INF/org.ict.kura.driver.dummy.xml from source code
	 */
	@Reference(name = "EventAdmin", service = EventAdmin.class, cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, bind = "setEventAdmin", unbind = "unsetEventAdmin")

	/**
	 * This method receives the {@link EventAdmin} from OSGI
	 * 
	 * @param eventAdmin the OSGI EventAdmin service to send events
	 */
	public void setEventAdmin(EventAdmin eventAdmin) {
		this.setEA(eventAdmin);
	}

	/**
	 * Removes the {@link EventAdmin} - sets the instance equals null.
	 * 
	 * @param eventAdmin the OSGI EventAdmin service to send events
	 */
	public void unsetEventAdmin(EventAdmin eventAdmin) {
		this.setEA(null);
	}

	/**
	 * This annotation adds the {@link EventAdmin} service methods to the
	 * OSGI-INF/org.ict.kura.driver.multisensor.xml from source code
	 */
	@Reference(name = "AssetProvider", service = ThingProvider.class, cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, bind = "setAssetProvider", unbind = "unsetAssetProvider")

	/**
	 * This methods receives the {@link EventAdmin} from OSGI
	 * 
	 * @param eventAdmin the OSGI {@link EventAdmin} service to send events
	 */
	public final void setAssetProvider(ThingProvider assetProvider) {
		this.setThingProvider(assetProvider);
	}

	public final void unsetAssetProvider(ThingProvider thingProvider) {
		this.setThingProvider(null);
	}

	@Override
	public AvmBindingConfig createBinding(String jsonBinding) {
		/* Creates a JSON Object from jsonValue */
		JsonObject jsonObject = new JsonParser().parse(jsonBinding).getAsJsonObject();

		String href = jsonObject.get("href").getAsString();

		LOGGER.info("...createBinding: jsonBinding {} {}", href, jsonObject);

		final AvmBindingConfig avmBindingConfig = new AvmBindingConfig();

		JsonObject ele = jsonObject.get("avm").getAsJsonObject();

		String ain = ele.get("ain").getAsString();
		LOGGER.info("ain {}", ain);

		avmBindingConfig.setAin(ain);
		avmBindingConfig.setHref(href);

		LOGGER.info("Created avmBindingConfig {}", avmBindingConfig);

		this.avmBindingConfigs.put(href, avmBindingConfig);

		return avmBindingConfig;
	}

	@Override
	public void connect() throws ConnectionException {
		LOGGER.info("Attempt connecting");

	}

	@Override
	public void disconnect() throws ConnectionException {
		LOGGER.info("Attempt disconnecting");

	}

	@Override
	public PreparedRead prepareRead(List<ChannelRecord> channelRecords) {
		LOGGER.info("...prepareRead");
		requireNonNull(channelRecords, "The channelRecords list must not be null");

		/* Creates the {@link PreparedRead} instance */
		try (ThingPreparedRead preparedRead = new ThingPreparedRead(this, channelRecords)) {
			preparedRead.getChannelRecords().stream()
					.forEach(o -> LOGGER.info("Prepare Read for chanel {}", o.getChannelName()));
			return preparedRead;
		}
	}

	public Optional<JsonObject> doRead(AvmBindingConfig technologyBindingConfiguration) {
		LOGGER.warn("...doRead is not implemented yet");
		return Optional.empty();
	}

	public void doWrite(AvmBindingConfig technologyBindingConfiguration, ActionAffordance actionAffordance,
			JsonObject jsonValue) {

		LOGGER.info("... doWrite");

		for (String key : jsonValue.keySet()) {
			switch (key) {
			case "tSoll":
				avmApi.setTemperatureDect301(technologyBindingConfiguration.getAin(),
						jsonValue.get("tSoll").getAsFloat());
				break;

			case "onOff":
				LOGGER.info("write on Off");
				avmApi.switchStateDect2XX(technologyBindingConfiguration.getAin(),
						jsonValue.get("onOff").getAsBoolean());
				break;
			default:
				break;
			}
		}

	}

}
