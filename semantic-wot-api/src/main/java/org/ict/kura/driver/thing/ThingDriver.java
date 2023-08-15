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
package org.ict.kura.driver.thing;

import static org.eclipse.kura.channel.ChannelFlag.SUCCESS;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.kura.channel.ChannelRecord;
import org.eclipse.kura.channel.ChannelStatus;
import org.eclipse.kura.channel.listener.ChannelListener;
import org.eclipse.kura.driver.Driver.ConnectionException;
import org.eclipse.kura.type.TypedValue;
import org.everit.json.schema.loader.SchemaLoader;
import org.ict.gson.utils.AdapterFactory;
import org.ict.kura.asset.creator.thing.util.ThingChannelDescriptor;
import org.ict.kura.asset.creator.util.ChannelDescriptorBase;
import org.ict.kura.thing.creator.ThingProvider;
import org.ict.model.wot.core.ActionAffordance;
import org.json.JSONObject;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This is the abstract thing driver implementation. All concrete technology
 * driver implementations, which using the WoT description terminology should
 * extends this class.
 * 
 * Important: All configuration maps are thread safe {@link ConcurrentHashMap},
 * because they are accessed by different threads in the activate and deactivate
 * phase.
 * 
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2022-12-13
 */
@ToString
public abstract class ThingDriver<T> implements Binding<T> {
	/* The logger instance */
	private static final Logger LOGGER = LoggerFactory.getLogger(ThingDriver.class);

	/*
	 * The technology update binding map. The technology implementation links to a
	 * {@link ThingChannelListener} for each KURA channel (value update direction:
	 * technology to KURA). The key (T) is the specific technology class.
	 * 
	 * Example: A driver implementation can use this map when sensor/actuator status
	 * updates are received from the technology and passed to the kura framework via
	 * the {@link ThingChannelListener}.
	 */
	@Setter
	@Getter
	private Map<T, ThingChannelListener> updateBindingConfigurations = new ConcurrentHashMap<>();

	/*
	 * The technology action binding map. The action form href links to a technology
	 * implementation (value write direction: KURA to technology). The key is the
	 * formHref (action). The value (T) is the specific technology class.
	 * 
	 * Example: We use this map only internally in the {@link
	 * ThingDriver#write(List<ChannelRecord>)}. This method receives a write command
	 * from the kura framework, which is passed to the technology with the method
	 * {@link Binding<T>#doWrite(T, ActionAffordance, JsonObject). The technology
	 * binding T from this map is used and passed to the method doWrite.
	 */
	@Setter
	@Getter
	private Map<String, T> actionBindingConfigurations = new ConcurrentHashMap<>();

	/*
	 * The technology property binding map. The property form href links to a
	 * technology implementation (value read direction: kura from technology). The
	 * key is the formHref (action). The value (T) is the specific technology class.
	 * 
	 * Example: We use this map only internally in the {@link
	 * ThingDriver#read(List<ChannelRecord>)}. This method receives a read command
	 * from the kura framework, which is passed to the technology with the method
	 * {@link Binding<T>#doRead(T). The technology binding T from this map is used
	 * and passed to the method doWrite.
	 */
	@Setter
	@Getter
	private Map<String, T> propertyBindingConfigurations = new ConcurrentHashMap<>();

	/*
	 * The technology property binding map. The property form href links to the
	 * {@link ThingChannelListener} (value read direction: from cache to KURA). The
	 * key is the formHref (property). The value (T) is the {@link
	 * ThingChannelListener}.
	 * 
	 * Example: We use this map only internally in the {@link
	 * ThingDriver#read(List<ChannelRecord>)}. The read value from the technology is
	 * pushed into the kura framework via the {@link ThingChannelListener}
	 */
	@Setter
	@Getter
	private Map<String, ThingChannelListener> listenerBindingConfigurations = new ConcurrentHashMap<>();

	/* The OSGI EventAdmin */
	private EventAdmin eventAdmin;

	/* The {@link ThingProvider} to generate assets and channels form WoT */
	@Setter
	@Getter
	private ThingProvider thingProvider;

	/*
	 * The {@link ThingChannelDescriptor}, is used in the KURA web admin ui and is
	 * provided by the asset provider.
	 */
	@Setter
	@Getter
	private ChannelDescriptorBase channelDescriptor = new ThingChannelDescriptor();

	/* Factory to create JSON objects */
	private final Gson gson = AdapterFactory.getGsonWithDefaultWotTypeAdapters(true);

	/**
	 * The {@link EventAdmin} getter - NO Lombok here !
	 * 
	 * @return the {@link EventAdmin}
	 */
	public EventAdmin getEA() {
		return eventAdmin;
	}

	/**
	 * The {@link EventAdmin} setter - NO Lombok here !
	 * 
	 * @param eventAdmin the {@link EventAdmin}
	 */
	public void setEA(EventAdmin eventAdmin) {
		this.eventAdmin = eventAdmin;
	}

	// @formatter:off
	/**
	 * This method creates the required bindings and the
	 * {@link ThingChannelListener}. A {@link ThingChannelListener} is only created
	 * if it is a property or property/action combination. So we have 3 cases:
	 * 
	 * 1) property: creates the instances
	 *    - {@link ThingChannelListener}
	 *    - {@link listenerBindingConfigurations}
	 *    - {@link propertyBindingConfiguration}
	 *    - {@link propertyBindingConfiguration}
	 *    
	 * 2) property/action: creates the instances
	 *    - {@link ThingChannelListener}
	 *    - {@link listenerBindingConfigurations}
	 *    - {@link propertyBindingConfiguration}
	 *    - {@link propertyBindingConfiguration}
	 *    - {@link actionBindingConfiguration}
	 *
	 * 3) action: creates only the instance
	 *    - {@link actionBindingConfiguration}
	 * 
	 * A kura {@link ChannelListener} is always generated !!!
	 * 
	 * @param channelConfiguration the kura channel configuration
	 * @param channelListener      the kura channel listener 
	 */
	// @formatter:on
	public void registerChannelListener(Map<String, Object> channelConfiguration, ChannelListener channelListener) {
		/*
		 * The {@link ThingChannelListener} is only generated if it is a property or
		 * property/action combination
		 */
		ThingChannelListener thingChannelListenerImpl = null;

		// ############################################################################################################

		if (channelConfiguration.get("formProperty") != null) {
			LOGGER.info("...registerChannelListener for thing {} and property {}",
					this.thingProvider.getIds().get(channelConfiguration.get("formProperty")),
					channelConfiguration.get("+name"));
			LOGGER.debug("The channelConfiguration - formPropertyBinding {}",
					channelConfiguration.get("formPropertyBinding"));
			LOGGER.debug("The channelConfiguration - formProperty {}", channelConfiguration.get("formProperty"));

			LOGGER.debug("The thingProvider.getIds() map {}", this.thingProvider.getIds().toString());
			LOGGER.debug(channelListener == null ? "ChannelListener is null" : "ChannelListener is not null");
		}

		if (channelConfiguration.get("formAction") != null) {
			LOGGER.info("...registerChannelListener for thing {} and action {}",
					this.thingProvider.getIds().get(channelConfiguration.get("formAction")),
					channelConfiguration.get("+name"));
			LOGGER.debug("The channelConfiguration - formActionBinding {}",
					channelConfiguration.get("formActionBinding"));
			LOGGER.debug("The channelConfiguration - formAction {}", channelConfiguration.get("formAction"));

			LOGGER.debug("The thingProvider.getIds() map {}", this.thingProvider.getIds().toString());
			LOGGER.debug(channelListener == null ? "ChannelListener is null" : "ChannelListener is not null");
		}

		// ############################################################################################################

		/*
		 * Creates a new {@link ThingChannelListener} only for properties or
		 * property/action pairs , but not for actions only !!!
		 */
		if (channelConfiguration.get("formProperty") != null) {
			thingChannelListenerImpl = new ThingChannelListener(
					this.thingProvider.getIds().get(channelConfiguration.get("formProperty")), channelConfiguration,
					channelListener, getEA(),
					this.thingProvider.getPropertyAffordance(channelConfiguration.get("formProperty").toString()));

			LOGGER.info("New ThingChannelListener created: {}",
					thingChannelListenerImpl.getChannelConfiguration().get("formProperty"));
		}
		// ############################################################################################################

		/* Creates the {@listenerBindingConfigurations} for this property */
		if (channelConfiguration.get("formProperty") != null) {
			listenerBindingConfigurations.put(channelConfiguration.get("formProperty").toString(),
					thingChannelListenerImpl);

			/* Logs the content of the {@link listenerBindingConfigurations} map */
			LOGGER.debug("The listenerBindingConfigurations map {}", listenerBindingConfigurations.toString());
		} else
			LOGGER.warn("This channel configuration has no formProperty !");

		/*
		 * If a formProperty exists, a formPropertyBinding must also exist !!!
		 * two if cases could also be merged !!!
		 */
		if (channelConfiguration.get("formPropertyBinding") != null) {
			/* Creates the technology binding. */
			T binding = createBinding(channelConfiguration.get("formPropertyBinding").toString());

			/* Creates the property binding. */
			propertyBindingConfigurations.put(channelConfiguration.get("formProperty").toString(), binding);

			/* Creates the update binding. */
			updateBindingConfigurations.put(binding, thingChannelListenerImpl);

			/* Logs the content of the {@link updateBindingConfigurations} map */
			LOGGER.debug("The updateBindingConfigurations map {}", updateBindingConfigurations.toString());
		} else
			LOGGER.warn("This channel configuration has no formPropertyBinding !");

		// ############################################################################################################

		/*
		 * Creates the {@actionBindingConfigurations}. If this action has no property,
		 * in this case this action has no {@link ThingChannelListener}.
		 */
		if (channelConfiguration.get("formAction") != null) {
			actionBindingConfigurations.put(channelConfiguration.get("formAction").toString(),
					createBinding(channelConfiguration.get("formActionBinding").toString()));

			/* Logs the content of the {@link actionBindingConfigurations} map */
			LOGGER.debug("The actionBindingConfigurations map {}", actionBindingConfigurations.toString());
		} else
			LOGGER.warn("This channel configuration has no formAction !");
	}

	/**
	 * Removes the link between the KURA {link ChannelListener} with the driver
	 * {link ThingChannelListener}. Deletes only the link in the {link
	 * updateBindingConfigurations} map!
	 */
	public final void unregisterChannelListener(ChannelListener channelListener) throws ConnectionException {
		LOGGER.info("...unregisterChannelListener ");

		/* Logs the channel listener status */
		LOGGER.debug(channelListener == null ? "ChannelListener is null" : "ChannelListener is not null");

		/* Logs the content of the {@link updateBindingConfigurations} map */
		updateBindingConfigurations
				.forEach((key, value) -> LOGGER.debug("updateBindingConfigurations: " + key + "|" + value));

		/*
		 * Searches for the KURA channel listener in the {@link
		 * updateBindingConfigurations} map and removes it
		 */
		for (Iterator<Entry<T, ThingChannelListener>> it = updateBindingConfigurations.entrySet().iterator(); it
				.hasNext();) {
			/* Gets an iterator */
			Map.Entry<T, ThingChannelListener> e = it.next();
			LOGGER.debug("{}", e.getValue().toString());

			/* Checks listener instance to be deleted */
			if (e.getValue().getChannelListener() == channelListener) {
				LOGGER.debug("Removes the listener with channel name: {}, channel configuration {}",
						e.getValue().getThingName(), e.getValue().getChannelConfiguration().toString());
				/* Closes the listener */
				e.getValue().close();
				/* Removes the listener from the map */
				it.remove();
			}
		}
		
		/* Logs the content of the {@link listerBindingConfigurations} map */
		listenerBindingConfigurations
				.forEach((key, value) -> LOGGER.debug("listenerBindingConfigurations: " + key + "|" + value));

		/*
		 * Searches for the KURA channel listener in the {@link
		 * updateBindingConfigurations} map and removes it
		 */
		for (Iterator<Entry<String, ThingChannelListener>> it = listenerBindingConfigurations.entrySet().iterator(); it
				.hasNext();) {
			/* Gets an iterator */
			Map.Entry<String, ThingChannelListener> e = it.next();
			LOGGER.debug("{}", e.getValue().toString());

			/* Checks listener instance to be deleted */
			if (e.getValue().getChannelListener() == channelListener) {
				LOGGER.debug("Removes the listener with channel name: {}, channel configuration {}",
						e.getValue().getThingName(), e.getValue().getChannelConfiguration().toString());
				/* Closes the listener */
				e.getValue().close();
				/* Removes the listener from the map */
				it.remove();
			}
		}
	}

	/**
	 * Read request from the KURA framework - contains 1-N requests (channel
	 * records). This method reads the actual value from the
	 * {@link KuraChannelListenerImpl} - not from the technology !
	 */
	public void read(List<ChannelRecord> channelRecords) throws ConnectionException {
		LOGGER.info("...read");
		/* Logs the content of the {@link ChannelRecord} instance */
		channelRecords.forEach(cr -> LOGGER.debug("" + cr.toString()));
		/* Logs the content of the {@link listenerBindingConfigurations} map */
		listenerBindingConfigurations
				.forEach((key, value) -> LOGGER.debug("readBindingConfigurations: " + key + "|" + value));

		/* Iterates over the channel list ... */
		for (final ChannelRecord channelRecord : channelRecords) {
			/* ... and try to read the newest channel value. */
			try {
				/* Gets the unique form property href */
				String formProperty = (String) channelRecord.getChannelConfig().get("formProperty");

				LOGGER.debug("formProperty from channelRecord => topic {}", formProperty);

				/* Reads the value from the driver */
				Optional<JsonObject> oValue = doRead(propertyBindingConfigurations.get(formProperty));

				/*
				 * If a value is present, the channel listener is called to save the value in
				 * the Kura environment, otherwise we do nothing here.
				 */
				if (oValue.isPresent()) {
					JsonObject value = oValue.get();
					listenerBindingConfigurations.get(formProperty).doUpdate(value);
				}

				// #########################################################################################
				// This section is important if no oValue is present, so the old value from the
				// {@link ThingChannelListener} or the value that came in asynchronously
				// must be sent into the Kura framework again
				// #########################################################################################

				/*
				 * Gets the value and the type of the value from the {@link
				 * ThingChannelListener} - the {@link KuraDriver} supports only the data type
				 * string with a JSON formatted time/value pair.
				 */
				final Optional<TypedValue<?>> typedValue = listenerBindingConfigurations.get(formProperty)
						.getTypedValue();

				/*
				 * Sets the actual value of the driver device parameter into the corresponding
				 * KURA channel record
				 */
				channelRecord.setValue(typedValue.get());
				/*
				 * Sets the actual channel status of the driver device parameter into the
				 * corresponding KURA channel record
				 */
				channelRecord.setChannelStatus(new ChannelStatus(SUCCESS));

				/*
				 * Sets the actual update time stamp of the driver device parameter into the
				 * corresponding KURA channel record
				 */
				channelRecord.setTimestamp(System.currentTimeMillis());
			} catch (Throwable t) {
				/*
				 * CThing thing = gsonThing.fromJson(new FileReader(f), Thing.class);reates a
				 * failure channel record
				 */
				Util.setFailureRecord(channelRecord, t.toString());
				LOGGER.error("", t);
			}
		}
	}

	/**
	 * Write request from the KURA framework - contains 1-N requests (channel
	 * records). This method writes the actual value to the technology via the
	 * {@link ThingListener} using the {@link Binding#doWrite()}.
	 */
	public final void write(List<ChannelRecord> channelRecords) throws ConnectionException {
		LOGGER.info("...write");
		/* Logs the content of the {@link ChannelRecord} instance */
		channelRecords.forEach(cr -> LOGGER.debug("" + cr.toString()));

		/* Iterates over the channel list ... */
		for (final ChannelRecord channelRecord : channelRecords) {
			/* ... and try to write the channel value. */
			try {
				/* Gets the unique form action href */
				String formAction = (String) channelRecord.getChannelConfig().get("formAction");

				/* Gets the value in string format */
				String jsonValue = String.valueOf(channelRecord.getValue().getValue());
				LOGGER.info("formAction {}, payload in string format {}", formAction, jsonValue);

				// ################################################################################################
				/* Searches the T via formHref in the map {@link write(formHref, T)} */
				T t = this.actionBindingConfigurations.get(formAction);

				/* Gets the {@link ActionAffordance} corresponds with this channel */
				ActionAffordance actionAffordance = this.thingProvider.getActionAffordance(formAction);

				/* Validates the schema of the value here check the input schema only */
				SchemaLoader.load(new JSONObject(gson.toJson(actionAffordance))).validate(new JSONObject(jsonValue));

				/* Creates a JSON Object from jsonValue */
				JsonObject jsonObject = new JsonParser().parse(jsonValue).getAsJsonObject();

				/*
				 * Calls the method doWrite(T technologyBindingConfiguration, String
				 * payloadSchema, String jsonValue);
				 */
				this.doWrite(t, actionAffordance, jsonObject);
				// ################################################################################################

				/* Sets the actual channel status on success - write command was successful. */
				channelRecord.setChannelStatus(new ChannelStatus(SUCCESS));

				/* Sets the actual update time stamp */
				channelRecord.setTimestamp(System.currentTimeMillis());
			} catch (Throwable t) {
				/*
				 * Sets the actual channel status on not success. Creates a failure channel
				 * record.
				 */
				Util.setFailureRecord(channelRecord, t.toString());
				LOGGER.error("", t);
			}
		}
	}

	/**
	 * Creates s technology specific configuration implementation.
	 * 
	 * @param jsonBinding the technology specific configuration in json format
	 * @return the technology specific configuration instance (java pojo)
	 */
	public abstract T createBinding(String jsonBinding);
}
