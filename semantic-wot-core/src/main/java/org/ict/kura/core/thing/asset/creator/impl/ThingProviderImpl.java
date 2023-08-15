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
package org.ict.kura.core.thing.asset.creator.impl;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.eclipse.kura.channel.ChannelType;
import org.eclipse.kura.configuration.ComponentConfiguration;
import org.eclipse.kura.configuration.ConfigurationService;
import org.eclipse.kura.type.DataType;
import org.ict.gson.utils.AdapterFactory;
import org.ict.kura.asset.creator.thing.util.ThingContainer;
import org.ict.kura.core.asset.creator.impl.AssetChannelCreatorImpl;
import org.ict.kura.core.thing.asset.creator.util.ThingContainerImpl;
import org.ict.kura.thing.creator.ThingProvider;
import org.ict.model.wot.core.ActionAffordance;
import org.ict.model.wot.core.PropertyAffordance;
import org.ict.model.wot.core.Thing;
import org.ict.model.wot.hypermedia.Form;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * The implementation of the interface {@link ThingProvider} to create kura
 * asset and channels from a thing description. 
 * 
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2021-02-03
 */
@Component(immediate = true, property = { "event.topics=things/*",
		"service.pid=org.ict.kura.core.thing.asset.creator.impl.ThingProvider" }, name = "org.ict.kura.core.thing.asset.creator.impl.ThingProvider")
public class ThingProviderImpl implements ThingProvider, EventHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ThingProviderImpl.class);
	private static final String APP_ID = "org.ict.kura.service.asset.creator.thing.ThingProvider";
	private static final String FACTORY_PID = "org.eclipse.kura.wire.WireAsset";

	/* OSGi bundle configuration service */
	private ConfigurationService configurationService;

	/* ICT gson object and factory to create {@link Thing} from json string */
	Gson gson = AdapterFactory.getGsonWithDefaultWotTypeAdapters(true);

	/* Map with semantic type informations */
	private Map<String, List<URI>> semanticTypes = new HashMap<>();

	/* Map with modified thing ids (used as asset names) */
	private Map<String, String> ids = new HashMap<>();

	/* Map with thing properties */
	private Map<String, Map.Entry<String, PropertyAffordance>> properties = new HashMap<>();

	/* Map with action properties */
	private Map<String, Map.Entry<String, ActionAffordance>> actions = new HashMap<>();

	/**
	 * Binding function which starts the bundle, see component.xml, is called by the
	 * OSGi framework
	 */
	@Activate
	protected void activate(ComponentContext componentContext, Map<String, Object> properties) {
		/* Updates the configuration */
		updated(properties);
		LOGGER.info("Bundle " + APP_ID + " has started!");
	}

	/**
	 * Binding function which to shutdown the bundle, see component.xml, is called
	 * by the OSGi framework
	 */
	@Deactivate
	protected void deactivate(ComponentContext componentContext) {
		/* Closes all resources */
		doDeactivate();
		LOGGER.info("Bundle " + APP_ID + " has stopped!");
	}

	/**
	 * Method to handle configuration updates
	 * 
	 * @param properties Properties that are configured via the Kura web admin
	 */
	@Modified
	public void updated(Map<String, Object> properties) {
		/* Deactivates all components */
		doDeactivate();

		/* Updates all components with new configuration */
		doUpdate(properties);
		LOGGER.info("Bundle " + APP_ID + " has updated!");
	}

	/**
	 * Deletes all driver KURA configurations ! For the next start every driver have
	 * to reinitialize its configuration with the help of the {@link ThingProvider}.
	 */
	private void doDeactivate() {
		LOGGER.info("doDeactivate...");

		try {
			/* Gets all KURA driver configuration */
			List<ComponentConfiguration> componentConfigurations = configurationService.getComponentConfigurations();
			/* Iterates over all KURA driver configurations */
			for (ComponentConfiguration componentConfiguration : componentConfigurations) {
				/* Gets the KURA driver configuration */
				Map<String, Object> map = componentConfiguration.getConfigurationProperties();
				if(map == null) {
					break;
				}
				Iterator<Entry<String, Object>> it = map.entrySet().iterator();
				/*
				 * Iterates over the KURA driver configurations parameters and looking for the
				 * corresponding driver.pid
				 */
				while (it.hasNext()) {
					Entry<String, Object> entry = it.next();
					switch (entry.getKey()) {
					case "driver.pid":
						LOGGER.info("Deletes the driver configuration with Pid: " + componentConfiguration.getPid());
						/* Deletes the driver (asset) configuration */
						configurationService.deleteFactoryConfiguration(componentConfiguration.getPid(), true);
					default:
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("" + e);
		}
	}

	/**
	 * Updates all resources
	 * 
	 * @param properties configuration parameters
	 */
	private void doUpdate(Map<String, Object> properties) {
		// here we do nothing
	}

	/*
	 * This annotation adds the configuration service methods to the
	 * OSGI-INF/org.ict.kura.driver.dummy.xml from source code
	 */
	@Reference(name = "ConfigurationService", service = ConfigurationService.class, cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, bind = "setConfigurationService", unbind = "unsetConfigurationService")
	/**
	 * This methods receives the {@link ConfigurationService} from kura OSGi.
	 * 
	 * @param configurationService the OSGi configuration service to configure the
	 *                             driver bundle
	 */
	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	/**
	 * Removes the {@link ConfigurationService} - sets the instance equal null.
	 * 
	 * @param configurationService the OSGi configuration service to configure the
	 *                             driver bundle
	 */
	public void unsetConfigurationService(ConfigurationService configurationService) {
		this.configurationService = null;
	}

	@Override
	public void createAssetsWithChannels(ThingContainer thingContainer) throws Exception {
		/*
		 * Iterates over the thing list. A thing is equal to an asset, properties and
		 * actions are equal to channels.
		 */
		for (Thing thing : thingContainer.getThings()) {
			/*
			 * First, trims the thing id using as asset name (the modified assetName from
			 * the thing id without base url)
			 */
			String assetName = extractAssetName(thing);

			/* Creates helpful containers */
			createMapsFromThingDescription(thing, assetName);

			/* Creates a new AssetChannelCreator */
			AssetChannelCreatorImpl assetChannelCreator = new AssetChannelCreatorImpl();

			/* Creates a map for one asset configurations */
			Map<String, Object> asset = new HashMap<>();

			LOGGER.info("Create a new asset with driverPID {} and asset name {}", thingContainer.getDriverPID(),
					assetName);

			/* Creates the asset with the driver name and asset name */
			asset.putAll(assetChannelCreator.createAsset(thingContainer.getDriverPID(), assetName));

			LOGGER.info("Content of the actual asset: {}", asset);

			// Create separate list of actions, properties and action/properties
			Map<String, Set<String>> interactions = getSensorActuators(thing.getActions(), thing.getProperties());

			LOGGER.debug("Interactions: " + interactions);

			// Create channels from properties (Sensor)
			if (!interactions.get("properties").isEmpty()) {
				LOGGER.debug("add properties for: " + assetName);
				asset.putAll(createChannelsFromProperties(interactions.get("properties"), thing, assetChannelCreator));
			}

			// Create channels from actions (Actuator)
			if (!interactions.get("actions").isEmpty()) {
				LOGGER.debug("add actions for: " + assetName);
				asset.putAll(createChannelsFromActions(interactions.get("actions"), thing, assetChannelCreator));
			}

			// Create channels from properties and actions (Sensor/Actuator)
			if (!interactions.get("actionProperties").isEmpty()) {
				LOGGER.debug("add action properties for: " + assetName);
				asset.putAll(createChannelsFromPropertiesActions(interactions.get("actionProperties"), thing,
						assetChannelCreator));
			}

			LOGGER.debug("The new created asset: {}", asset);

			/*
			 * Adds the asset to the configuration service. If the asset do not exists, we
			 * create a new configuration. If the asset exist, we delete this asset and
			 * update the configuration - if we do not delete the configuration, the update
			 * method has no effect.
			 */
			if (!configurationService.getConfigurableComponentPids().contains(assetName)) {
				configurationService.createFactoryConfiguration(FACTORY_PID, assetName, asset, true);
				LOGGER.debug("Asset created with assetName {} / {}", assetName, asset);
			} else {
				/*
				 * Deletes the asset with the given asset name from the component configuration
				 * - otherwise the update method has no effect.
				 */
				configurationService.deleteFactoryConfiguration(assetName, false);
				/* Updates the asset configuration with the given asset name */
				configurationService.updateConfiguration(assetName, asset, false);
				LOGGER.debug("Asset updated with assetName {} / {}", assetName, asset);
			}
			try {
				LOGGER.debug(configurationService.getComponentConfiguration(assetName).toString());
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	private Map<? extends String, ? extends Object> createChannelsFromPropertiesActions(Set<String> set, Thing thing,
			AssetChannelCreatorImpl assetChannelCreator) {
		Map<String, Object> channel = new HashMap<>();
		for (String key : set) {
			LOGGER.info("Create a new channel with channel name {}, ChannelType.READ_WRITE and data type {}", key,
					DataType.STRING.name());

			Form formProperty = thing.getProperties().get(key).getForms().get(0);
			Form formAction = thing.getActions().get(key).getForms().get(0);
			/* Creates a temporary href variable */
			String hrefProperty = formProperty.getHref().toString();
			String hrefAction = formAction.getHref().toString();
			/* Creates a temporary channel type */
			ChannelType channelType = ChannelType.READ_WRITE;

			/*
			 * Creates an advanced channel descriptor map with key=form and value=href. The
			 * form key corresponding with the {link ThingChannelDescriptor}
			 */
			Map<String, Object> advancedConfiguration = new HashMap<>();
			advancedConfiguration.put("formProperty", hrefProperty);
			advancedConfiguration.put("formAction", hrefAction);

			/*
			 * Saves the whole form property binding configuration (in json format)-
			 * supports only ONE configuration !
			 */
			advancedConfiguration.put("formPropertyBinding", gson.toJson(formProperty));
			advancedConfiguration.put("formActionBinding", gson.toJson(formAction));

			/* Creates standard and advanced channel types */

			channel.putAll(
					assetChannelCreator.createChannel(key, channelType, DataType.STRING.name(), advancedConfiguration));
			/* Logs the advancedConfiguration */
			advancedConfiguration.forEach((k, value) -> LOGGER.debug("advancedConfiguration: " + k + "|" + value));
		}
		return channel;
	}

	private Map<? extends String, ? extends Object> createChannelsFromActions(Set<String> set, Thing thing,
			AssetChannelCreatorImpl assetChannelCreator) {
		Map<String, Object> channel = new HashMap<>();
		for (String key : set) {
			LOGGER.info("Create a new channel with channel name {}, ChannelType.Write and data type {}", key,
					DataType.STRING.name());

			Form form = thing.getActions().get(key).getForms().get(0);
			/* Creates a temporary href variable */
			String hrefAction = form.getHref().toString();
			/* Creates a temporary channel type */
			ChannelType channelType = ChannelType.WRITE;

			/*
			 * Creates an advanced channel descriptor map with key=form and value=href. The
			 * form key corresponding with the {link ThingChannelDescriptor}
			 */
			Map<String, Object> advancedConfiguration = new HashMap<>();
			advancedConfiguration.put("formAction", hrefAction);

			/*
			 * Saves the whole form property binding configuration (in json format)-
			 * supports only ONE configuration !
			 */
			advancedConfiguration.put("formActionBinding", gson.toJson(form));

			/* Creates standard and advanced channel types */

			channel.putAll(
					assetChannelCreator.createChannel(key, channelType, DataType.STRING.name(), advancedConfiguration));
			/* Logs the advancedConfiguration */
			advancedConfiguration.forEach((k, value) -> LOGGER.debug("advancedConfiguration: " + k + "|" + value));
		}
		return channel;
	}

	private Map<? extends String, ? extends Object> createChannelsFromProperties(Set<String> set, Thing thing,
			AssetChannelCreatorImpl assetChannelCreator) {
		Map<String, Object> channel = new HashMap<>();

		for (String key : set) {
			LOGGER.info("Create a new channel with channel name {}, ChannelType.READ and data type {}", key,
					DataType.STRING.name());

			Form form = thing.getProperties().get(key).getForms().get(0);
			/* Creates a temporary href variable */
			String hrefProperty = form.getHref().toString();
			/* Creates a temporary channel type */
			ChannelType channelType = ChannelType.READ;

			/*
			 * Creates an advanced channel descriptor map with key=form and value=href. The
			 * form key corresponding with the {link ThingChannelDescriptor}
			 */
			Map<String, Object> advancedConfiguration = new HashMap<>();
			advancedConfiguration.put("formProperty", hrefProperty);

			/*
			 * Saves the whole form property binding configuration (in json format)-
			 * supports only ONE configuration !
			 */
			advancedConfiguration.put("formPropertyBinding", gson.toJson(form));

			/* Creates standard and advanced channel types */

			channel.putAll(
					assetChannelCreator.createChannel(key, channelType, DataType.STRING.name(), advancedConfiguration));
			/* Logs the advancedConfiguration */
			advancedConfiguration.forEach((k, value) -> LOGGER.debug("advancedConfiguration: " + k + "|" + value));
		}
		return channel;
	}

	private Map<String, Set<String>> getSensorActuators(Map<String, ActionAffordance> actions,
			Map<String, PropertyAffordance> properties) {
		Optional<Set<String>> propertieKeys = Optional.empty();
		Optional<Set<String>> actionKeys = Optional.empty();
		try {
			propertieKeys = Optional.of(properties.keySet());
		} catch (Exception e) {
			propertieKeys = Optional.of(new HashSet<String>());
		}

		try {
			actionKeys = Optional.of(actions.keySet());
		} catch (Exception e) {
			actionKeys = Optional.of(new HashSet<String>());
		}

		LOGGER.debug("Actions: " + actionKeys.get());
		LOGGER.debug("Properties: " + propertieKeys.get());
		Map<String, Set<String>> map = new HashMap<>();
		Set<String> actionProperties = new HashSet<>();
		Set<String> actionSet = new HashSet<String>();
		Set<String> propertieSet = new HashSet<String>();

		for (String key : actionKeys.get()) {

			if (propertieKeys.get().contains(key)) {
				actionProperties.add(key);
			} else {
				actionSet.add(key);
			}
		}

		for (String key : propertieKeys.get()) {
			if (actionKeys.get().contains(key)) {
				actionProperties.add(key);
			} else {
				propertieSet.add(key);
			}

		}

		map.put("properties", propertieSet);
		map.put("actions", actionSet);
		map.put("actionProperties", actionProperties);

		return map;
	}

	private String extractAssetName(Thing thing) {
		return thing.getId().toString().substring(thing.getId().toString().lastIndexOf("/") + 1,
				thing.getId().toString().length());
	}

	@Override
	public void createAssetsWithChannels(String driverPID, Thing thing) throws Exception {
		/* Creates the thing container */
		ThingContainer thingContainer = new ThingContainerImpl(driverPID, Arrays.asList(thing));
		/* Creates the asset with channels */
		createAssetsWithChannels(thingContainer);
	}

	@Override
	public void deleteAssetsWithChannels(String driverPID) throws Exception {
		/* Deletes all assets from the component configuration */
		/* Gets the component configurations from the configuration service */
		List<ComponentConfiguration> componentConfigurations = configurationService.getComponentConfigurations();

		LOGGER.debug("componentConfigurations: {}", componentConfigurations);
		/*
		 * Iterates over all component configurations and looking for the configuration
		 * property with the given driverPID.
		 */
		for (ComponentConfiguration componentConfiguration : componentConfigurations) {
			/* Gets a map with configuration properties */
			Map<String, Object> configurationProperties = componentConfiguration.getConfigurationProperties();

			LOGGER.info("configurationProperties: {}", configurationProperties);
			/*
			 * Iterates over all properties and looking for the key driver.pid and the value
			 * equals the given driverPID
			 */
			for (Entry<String, Object> entry : configurationProperties.entrySet()) {
				if (entry.getKey().equals("driver.pid") && entry.getValue().equals(driverPID)) {
					/* Deletes the assets from the component configuration */
					configurationService.deleteFactoryConfiguration(componentConfiguration.getPid(), false);
				}
			}
		}
	}

	/**
	 * Parses the thing description and creates different helpful maps from the
	 * properties, action and events, if these exist.
	 * 
	 * @param thing     the thing description
	 * @param assetName the modified assetName (from the thing id without base url)
	 */
	private void createMapsFromThingDescription(Thing thing, String assetName) {

		/* Parses all properties, if these exist */
		if (thing.getProperties() != null) {
			for (Map.Entry<String, PropertyAffordance> entry : thing.getProperties().entrySet()) {
				String hrefProperty = null;
				for (Form form : entry.getValue().getForms()) {
					hrefProperty = form.getHref().toString();
				}
				ids.put(hrefProperty, assetName);
				semanticTypes.put(hrefProperty, entry.getValue().getAtType());
				properties.put(hrefProperty, entry);
			}
		}

		/* Parses all actions, if these exist */
		if (thing.getActions() != null) {
			for (Map.Entry<String, ActionAffordance> entry : thing.getActions().entrySet()) {
				String hrefAction = null;
				for (Form form : entry.getValue().getForms()) {
					hrefAction = form.getHref().toString();
				}
				ids.put(hrefAction, assetName);
				semanticTypes.put(hrefAction, entry.getValue().getAtType());
				actions.put(hrefAction, entry);
			}
		}

		// Parses all events, if these exist
	}

	@Override
	public Map<String, List<URI>> getSemanticTypes() {
		return semanticTypes;
	}

	@Override
	public Map<String, String> getIds() {
		return ids;
	}

	@Override
	public Map<String, Entry<String, PropertyAffordance>> getProperties() {
		return properties;
	}

	@Override
	public Map<String, Entry<String, ActionAffordance>> getActions() {
		return actions;
	}

	@Override
	public PropertyAffordance getPropertyAffordance(String key) {
		return properties.get(key).getValue();
	}

	@Override
	public ActionAffordance getActionAffordance(String key) {
		return actions.get(key).getValue();
	}

	@Override
	public void handleEvent(Event event) {
		LOGGER.info("Receiving the event: {}", event.getTopic());

		try {
			Thing thing = (Thing) event.getProperty("thing");
			String driverPid = (String) event.getProperty("driverPid");
			createAssetsWithChannels(driverPid, thing);
		} catch (Throwable t) {
			LOGGER.error("Error: ", t);
		}
	}
}
