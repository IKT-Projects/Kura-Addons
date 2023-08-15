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
package $package;

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
 * @author 
 * @author 
 * @version 
 */
/* Annotation to point a {@link ExampleDriverConfig} class */
@Designate(ocd = ExampleConfig.class, factory = true)
/* Annotation to create the component.xml from source code */
@Component(name = "$package", immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE, property = "service.pid=$package")
public class ExampleDriver extends ThingDriver<ExampleDriverBindingConfig> implements Driver, ConfigurableComponent {
	/* The dummy driver logger instance */
	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleDriver.class);
	/* The unique driver app id - in this case the driver package name */
	private static final String APP_ID = "$package";

	/* The driver properties defined by the user in the kura admin web interface */
	@SuppressWarnings("unused")
	private Map<String, Object> properties;

	/* The map of technology bindings */
	private Map<String, ExampleBindingConfig> driverBindingConfigs = new ConcurrentHashMap<>();

	/* The Example options */
	private ExampleDriverOptions options;

	@Reference
	private ThingCreatorHref thingCreatorImpl;


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
	 * @param properties the configuration parameters 
	 */
	private void doUpdate(Map<String, Object> properties) {
		try {
			LOGGER.info("doUpdate...");

			/* Gets the options (configuration) */
			this.options = new ExampleDriverOptions(properties);

			//TODO create things oder read configuration.yml
			
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

		
	}

	/*
	 * This annotation adds the event admin service methods to the
	 * OSGI-INF/component.xml from source code
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

	/*
	 * This annotation adds the thing provider service methods to the
	 * OSGI-INF/component.xml from source code
	 */
	@Reference(name = "ThingProvider", service = ThingProvider.class, cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, bind = "setThingProvider", unbind = "unsetThingProvider")

	/**
	 * This methods receives the {@link ThingProvider} from OSGI
	 * 
	 * @param thingProvider the {@link ThingProvider} service to send events
	 */
	public final void setThingProvider(ThingProvider thingProvider) {
		this.setThingProvider(thingProvider);
	}

	/**
	 * Removes the {@link ThingProvider} - sets the instance equals null.
	 * 
	 * @param thingProvider the {@link ThingProvider} service to send events
	 */
	public final void unsetThingProvider(ThingProvider thingProvider) {
		this.setThingProvider(null);
	}

	/*
	 * This annotation adds the thing creator service methods to the
	 * OSGI-INF/component.xml from source code
	 */
	@Reference(name = "ThingCreator", service = ThingCreatorHref.class, cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, bind = "setThingCreator", unbind = "unsetThingCreator")
	
	/**
	 * This method receives the {@link ThingCreatorHref} from OSGI
	 * 
	 * @param thingCreator the {@link ThingCreatorHref} service to send events
	 */
	public final void setThingCreator(ThingCreatorHref thingCreator) {
		this.thingCreatorImpl = thingCreator;
	}

	/**
	 * Removes the {@link ThingCreatorHref} - sets the instance equals null.
	 * 
	 * @param thingCreator the {@link ThingCreatorHref} service to send events
	 */
	public final void unsetThingCreator(ThingCreatorHref thingCreator) {
		this.thingCreatorImpl = null;
	}

	@Override
	public Optional<JsonObject> doRead(ExampleBindingConfig technologyBindingConfiguration) {
		LOGGER.info("...doRead");

		LOGGER.info(technologyBindingConfiguration.toString());

		//TODO read actual value
		
		JsonObject value = new JsonObject();
		
		//TODO write value to json value
		
		Optional<JsonObject> oValue = Optional.of(value);
		return oValue;
	}

	@Override
	public void doWrite(ExampleBindingConfig technologyBindingConfiguration, ActionAffordance actionAffordance,
			JsonObject jsonValue) {
		LOGGER.info("...doWrite");
		
		//TODO write
	}

	@Override
	public ExampleDriverBindingConfig createBinding(String jsonBinding) {
		/* Creates a JSON Object from jsonBinding */
		JsonObject jsonObject = new JsonParser().parse(jsonBinding).getAsJsonObject();
		LOGGER.info("...createBinding for href {}", jsonObject.get("href").getAsString());

		// ################################################################################################
		// This area does not need to be edited.

		/* Creates a technology binding configuration from the JSON object */
		final ExampleDriverBindingConfig driverBindingConfig = new ExampleDriverBindingConfig();
		
		String bindingName = jsonObject.get("binding").getAsString();
		
		JsonObject ele = jsonObject.get(bindingName).getAsJsonObject();
		
		String name = ele.get("name").getAsString();

		/* Extracts the href for better handling */
		String href = jsonObject.get("href").getAsString();

		/* Sets the name with given name from the form element */
		driverBindingConfig.setName(name);

		/* Sets the href from the form element */
		driverBindingConfig.setHref(href);

		LOGGER.info("Created driverBindingConfig {}", driverBindingConfig);
		/*
		 * Puts {@OExampleBindingConfig} sorted by the form element href into the
		 * local {@ExampleBindingConfigs} map
		 */
		driverBindingConfigs.put(href, driverBindingConfig);

		LOGGER.info("New binding added into the driverBindingConfigs : key {}, value {}", href,
				driverBindingConfigs.get(href));
		LOGGER.info("ExampleBindingConfigs size: {}", driverBindingConfigs.size());
		// ################################################################################################
		return driverBindingConfig;
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