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
package org.ict.kura.internal.driver.openweather;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.kura.channel.ChannelRecord;
import org.eclipse.kura.configuration.ConfigurableComponent;
import org.eclipse.kura.driver.Driver;
import org.eclipse.kura.driver.PreparedRead;
import org.ict.kura.asset.creator.thing.util.DefaultConfigReader;
import org.ict.kura.asset.creator.thing.util.ThingPreparedRead;
import org.ict.kura.driver.thing.ThingDriver;
import org.ict.kura.driver.thing.Util;
import org.ict.kura.internal.driver.openweather.client.OpenWeatherClient;
import org.ict.kura.internal.driver.openweather.client.OpenWeatherUpdateThread;
import org.ict.kura.internal.driver.openweather.util.OpenWeatherBindingConfig;
import org.ict.kura.internal.driver.openweather.util.Tool;
import org.ict.kura.thing.creator.ThingCreator;
import org.ict.kura.thing.creator.ThingCreatorHref;
import org.ict.kura.thing.creator.ThingProvider;
import org.ict.kura.thing.model.ThingsConfig;
import org.ict.model.wot.core.ActionAffordance;
import org.ict.model.wot.core.Thing;
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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * This is a kura OpenWeather driver implementation based on the WoT
 * description.
 * 
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2021-10-20
 */
/* Annotation to point a {@link OpenWeatherDriverConfig} class */
@Designate(ocd = OpenWeatherDriverConfig.class, factory = true)
/* Annotation to create the component.xml from source code */
@Component(name = "org.ict.kura.driver.openweather", immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE, property = "service.pid=org.ict.kura.driver.openweather")
public class OpenWeatherDriver extends ThingDriver<OpenWeatherBindingConfig> implements Driver, ConfigurableComponent {
	/* The dummy driver logger instance */
	private static final Logger LOGGER = LoggerFactory.getLogger(OpenWeatherDriver.class);
	/* The unique driver app id - in this case the driver package name */
	private static final String APP_ID = "org.ict.kura.driver.openweather";

	/* The driver properties defined by the user in the kura admin web interface */
	@SuppressWarnings("unused")
	private Map<String, Object> properties;

	/* The map of technology bindings */
	private Map<String, OpenWeatherBindingConfig> openWeatherBindingConfigs = new ConcurrentHashMap<>();

	/* The OpenWeather options */
	private OpenWeatherDriverOptions options;

	/* The OpenWeather client instance */
	private OpenWeatherClient openWeatherClient;
	/* The OpenWeather update thread to poll actual data */
	private OpenWeatherUpdateThread openWeatherUpdateThread;

	@Reference
	private ThingCreatorHref thingCreatorImpl;

//	/**
//	 * Binding method which starts the bundle, see component.xml, is called by the
//	 * OSGi framework
//	 */
//	@Activate
//	protected void activate(ComponentContext componentContext) {
//		LOGGER.info("Bundle " + APP_ID + " has started with config!");
//
//	}

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
	 * 
	 * @param componentContext The OSGi component informations of this bundle - in
	 *                         this case we do nothing there.
	 */
	@Deactivate
	protected void deactivate(ComponentContext componentContext) {
		LOGGER.info("Bundle " + APP_ID + " has stopped!");

		/* Closes all resources */
		doDeactivate(false);
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
		doDeactivate(true);

		/* Updates all components with new configuration */
		doUpdate(properties);
	}

	/**
	 * Updates all resources.
	 * 
	 * @param properties the configuration parameters {@link #updated(Map<String,
	 *                   Object> properties)}
	 */
	private void doUpdate(Map<String, Object> properties) {
		try {
			LOGGER.info("doUpdate...");

			/* Gets the options (configuration) */
			this.options = new OpenWeatherDriverOptions(properties);

			/* Exists a location folder for thing descriptions */
			LOGGER.info("Folder or file path: {}", this.options.getThingFolderLocation());

			Objects.requireNonNull(options.getThingFolderLocation(),
					"No folder or file has been defined for thing descriptions or configuration.yml !");
			if (options.getThingFolderLocation().isEmpty())
				throw new IllegalArgumentException("Invalid location folder has been defined for thing descriptions !");

			File file = new File(this.options.getThingFolderLocation());

			/* Creates a thing map based on the given folder and thing files */
			Map<String, Thing> things = new HashMap<>();

			if (file.isDirectory()) {
				things = Util.getThingsFromFolder(file, things);
			} else {
				DefaultConfigReader reader = new DefaultConfigReader();
				ThingsConfig thingsConfig = reader.readConfig(new File(options.getThingFolderLocation()));
				ThingCreator creator = thingCreatorImpl.getThingCreator();
				
				things = creator.createFromConfig(thingsConfig).stream()
						.collect(Collectors.toMap(Thing::getName, Function.identity()));
			}

			/* Iterates over the thing map and creates assets with channels */
			for (String key : things.keySet()) {
				try {
					LOGGER.info("Create asset for thing key: {} and driver service pid {}", things.get(key),
							this.options.getDriverServicePID());
					/* Creates kura asset with channels from the given thing description */
//					getThingProvider().createAssetsWithChannels(this.options.getDriverServicePID(), things.get(key));
					Map<String, Object> eventAdminProperties = new HashMap<>();
					// Create a thin key referenced to the thing description
					eventAdminProperties.put("thing", things.get(key));
					// We need a reference to the driver pid
					eventAdminProperties.put("driverPid", options.getDriverServicePID());
					// We need a topic where we publish the thing description
					String topic = "things/" + things.get(key).getTitle();
					LOGGER.info("Send thing to EventAdmin with Topic: " + topic);
					// Gets an event admin instance and publish the thing description
					getEA().sendEvent(new Event(topic, eventAdminProperties));
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}

			/* Creates the HTTP client */
			openWeatherClient = new OpenWeatherClient(this.options.getCityName(), this.options.getStateCode(),
					this.options.getApiKey());

			/* Starts the update thread, which updates the weather data */
			openWeatherUpdateThread = new OpenWeatherUpdateThread(openWeatherClient, openWeatherBindingConfigs,
					getListenerBindingConfigurations(), this.options.getPollinterval());
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	/**
	 * Deactivates all resources means deletes all assets and devices - bundle
	 * shutdown.
	 */
	private void doDeactivate(boolean updated) {
		LOGGER.info("doDeactivate...");

		if (!updated) {
			try {
				if (openWeatherUpdateThread != null) {
					openWeatherUpdateThread.close();
				}
			} catch (Exception e) {
				LOGGER.error("" + e);
			}
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

//	@Reference(name = "ThingCreator", service = ThingCreatorHref.class, cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, bind = "setThingCreator", unbind = "unsetThingCreator")
//	public final void setThingCreator(ThingCreatorHref thingCreator) {
//		this.thingCreatorImpl = thingCreator;
//	}
//
//	public final void unsetThingCreator(ThingCreatorHref thingCreator) {
//		this.thingCreatorImpl = null;
//	}

	@Override
	public Optional<JsonObject> doRead(OpenWeatherBindingConfig technologyBindingConfiguration) {
		LOGGER.info("...doRead");

		LOGGER.info(technologyBindingConfiguration.toString());
		JsonElement weatherValue = Tool.getValueByKey(technologyBindingConfiguration.getName(),
				openWeatherClient.getWeather());

		JsonObject value = new JsonObject();
		value.add(technologyBindingConfiguration.getName(), weatherValue);
		value.add("time", new JsonPrimitive(System.currentTimeMillis()));
		Optional<JsonObject> oValue = Optional.of(value);
		return oValue;
	}

	@Override
	public void doWrite(OpenWeatherBindingConfig technologyBindingConfiguration, ActionAffordance actionAffordance,
			JsonObject jsonValue) {
		LOGGER.info("...doWrite ... here we do nothing. The open weather thing driver is only a sensor !");
	}

	@Override
	public OpenWeatherBindingConfig createBinding(String jsonBinding) {
		/* Creates a JSON Object from jsonBinding */
		JsonObject jsonObject = new JsonParser().parse(jsonBinding).getAsJsonObject();
		LOGGER.info("...createBinding for href {}", jsonObject.get("href").getAsString());

		// ################################################################################################
		// This area does not need to be edited.

		/* Creates a technology binding configuration from the JSON object */
		final OpenWeatherBindingConfig openWeatherBindingConfig = new OpenWeatherBindingConfig();
		
		String bindingName = jsonObject.get("binding").getAsString();
		
		JsonObject ele = jsonObject.get(bindingName).getAsJsonObject();
		
		String name = ele.get("name").getAsString();

//		/* Extracts the name for better handling */
//		String owm_name = jsonObject.get("owm:name").getAsString();

		/* Extracts the href for better handling */
		String href = jsonObject.get("href").getAsString();

		/* Sets the name with given name from the form element */
		openWeatherBindingConfig.setName(name);

		/* Sets the href from the form element */
		openWeatherBindingConfig.setHref(href);

		LOGGER.info("Created OpenWeatherBindingConfig {}", openWeatherBindingConfig);
		/*
		 * Puts {@OpenWeatherBindingConfig} sorted by the form element href into the
		 * local {@OpenWeatherBindingConfigs} map
		 */
		openWeatherBindingConfigs.put(href, openWeatherBindingConfig);

		LOGGER.info("New binding added into the openWeatherBindingConfigs : key {}, value {}", href,
				openWeatherBindingConfigs.get(href));
		LOGGER.info("OpenWeatherBindingConfigs size: {}", openWeatherBindingConfigs.size());
		// ################################################################################################
		return openWeatherBindingConfig;
	}

	@Override
	/*
	 * Here we do nothing. In this method we can e.g. implement a real connection to
	 * other physical gateways, sensors and actuators.
	 */
	public void connect() throws ConnectionException {
		LOGGER.info("...connect");
	}

	@Override
	/*
	 * Here we do nothing. In this method we can e.g. implement a real disconnection
	 * to other physical gateways, sensors and actuators.
	 */
	public void disconnect() throws ConnectionException {
		LOGGER.info("...disconnect");
	}

	@Override
	public PreparedRead prepareRead(List<ChannelRecord> channelRecords) {
		LOGGER.info("...prepareRead");
		requireNonNull(channelRecords, "The channelRecords list must not be null");

		/* Creates the {@link PreparedRead} instance */
		try (ThingPreparedRead preparedRead = new ThingPreparedRead(this, channelRecords)) {
			LOGGER.debug("Prepared read: {}", preparedRead);
			return preparedRead;
		}
	}
}