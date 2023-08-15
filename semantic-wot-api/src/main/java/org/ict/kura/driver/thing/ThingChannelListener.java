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

import static org.eclipse.kura.channel.ChannelFlag.FAILURE;
import static org.eclipse.kura.channel.ChannelFlag.SUCCESS;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.eclipse.kura.channel.ChannelRecord;
import org.eclipse.kura.channel.ChannelStatus;
import org.eclipse.kura.channel.ChannelType;
import org.eclipse.kura.channel.listener.ChannelEvent;
import org.eclipse.kura.channel.listener.ChannelListener;
import org.eclipse.kura.type.DataType;
import org.eclipse.kura.type.TypedValue;
import org.eclipse.kura.type.TypedValues;
import org.ict.gson.utils.AdapterFactory;
import org.ict.kura.util.Constants;
import org.ict.model.wot.core.PropertyAffordance;
import org.json.JSONObject;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * This is the base implementation of a thing listener, which holds all default
 * channel and thing description action/property parameters.
 * 
 * This module also validates the payload with the WoT {link PropertyAffordance}
 * schema. @see method {link #update(jsonValue)}
 * 
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2021-03-18
 */
@AllArgsConstructor
@ToString
public class ThingChannelListener implements Update, Closeable {
	public static final Logger LOGGER = LoggerFactory.getLogger(ThingChannelListener.class);

	// #########################################
	// The KURA section
	// #########################################

	/* The name of the asset */
	@Getter
	private String thingName;

	/* The KURA channel name - corresponding with the WoT property name */
	@Getter
	private String propertyName;

	/* The channel type e.g. a property or action */
	@Getter
	private ChannelType channelType;

	/* The KURA channel listener */
	@Getter
	private ChannelListener channelListener;

	/* The KURA channel configuration */
	@Getter
	private final Map<String, Object> channelConfiguration;

	/*
	 * The data type of the payload - in this case always a string type (JSON
	 * formatted)
	 */
	@Getter
	private DataType dataType;

	/* The KURA value object - contains the value and the data type of the value */
	@Getter
	Optional<TypedValue<?>> typedValue = Optional.empty();

	/*
	 * 
	 * 
	 * The {@link EventAdmin} instance
	 */
	@Getter
	private final EventAdmin eventAdmin;

	// #########################################
	// The WoT description section
	// #########################################

	/* The WoT property JSON schema */
	@Getter
	private final PropertyAffordance propertyAffordance;

	/* The WoT property JSON schema formed as a {link JSONObject} */
	@Getter
	private final JSONObject propertyAffordanceJsonObjectType;

	/* The JSON schema checker */
//	private final Schema propertyAffordanceSchema;

	/* Creates a new payload in JSON format */
	private final Gson gson = AdapterFactory.getGsonWithDefaultWotTypeAdapters(true);

	/**
	 * Constructor.
	 * 
	 * @param thingName            the name of the asset to which the channel
	 *                             corresponds
	 * @param channelConfiguration the KURA configuration of the channel from which
	 *                             the parameters name of the channel, the type of
	 *                             the channel, the data type of the value and the
	 *                             WoT form href are fetched
	 * @param channelListener      the KURA channel listener
	 * @param eventAdmin           the OSGi {@link EventAdmin} service to receive
	 *                             and send messages via the OSGi framework directly
	 */
	public ThingChannelListener(String thingName, Map<String, Object> channelConfiguration,
			ChannelListener channelListener, EventAdmin eventAdmin, PropertyAffordance propertyAffordance) {
		this.thingName = thingName;
		this.channelConfiguration = channelConfiguration;
		this.channelListener = channelListener;
		this.eventAdmin = eventAdmin;
		this.propertyAffordance = propertyAffordance;

		/* Gets the channel name from the channel configuration */
		this.propertyName = (String) channelConfiguration.get("+name");
		/* Gets the channel type */
		this.channelType = ChannelType.valueOf(channelConfiguration.get("+type").toString());
		/* Gets the channel value type from the channel configuration */
		this.dataType = DataType.getDataType((String) channelConfiguration.get("+value.type"));
		/*
		 * Converts the {link PropertyAffordance} into a {link JSONObject} for schema
		 * validation
		 */
		this.propertyAffordanceJsonObjectType = new JSONObject(gson.toJson(propertyAffordance));

		/*
		 * Creates the schema based on the {link PropertyAffordance} to validate the
		 * {link #update(jsonValue)} payload
		 */
//		this.propertyAffordanceSchema = SchemaLoader.load(propertyAffordanceJsonObjectType);
	}

	@Override
	public final void doUpdate(String jsonValue) {
		try {
			LOGGER.info("ThingName {}, PropertyName {}, Value: {}", thingName, propertyName, jsonValue);

			/* Redirects to the method {link #update(JsonObject)} */
			this.doUpdate(new JsonParser().parse(jsonValue).getAsJsonObject());
		} catch (Throwable t) {
			LOGGER.error("", t);
		}
	}

	@Override
	public final void doUpdate(JsonObject jsonValue) {
		try {
			if(channelListener == null) {
				LOGGER.warn("Device was deletet, channelListener is null!");
				return;
			}
			LOGGER.info("ThingName {}, PropertyName {}, Value: {}", thingName, propertyName, jsonValue.toString());

			/*
			 * Validates the payload - converts to {link J
			 * 
			 * SONObject}
			 */
//			this.propertyAffordanceSchema.validate(new JSONObject(jsonValue.toString()));

			/*
			 * Creates a TypedValue object with the given actual value and saves this here
			 * locally (cache of the {@link TingChannelListener} instance)
			 */
			this.typedValue = getTypedValue(dataType, jsonValue);

			/* Creates a new empty channel record */
			ChannelRecord record = null;

			/* If the typed value is not set, the channel status is set to failure */
			if (!typedValue.isPresent()) {
				/*
				 * Creates a channel record as statusRecord with the channelName and Status
				 */
				record = ChannelRecord.createStatusRecord(propertyName, new ChannelStatus(FAILURE,
						"Error while converting the retrieved value to the defined typed", null));
				/* Sets the ChannelConfiguration */
				record.setChannelConfig(channelConfiguration);
				/* Sets the actual time stamp */
				record.setTimestamp(System.currentTimeMillis());
				/*
				 * the value (JSON object) Sends a failure as channelEvent via the KURA
				 * Listener, this value is visible in UI.
				 */
				channelListener.onChannelEvent(new ChannelEvent(record));
			} else {
				// *************************************************************************
				// Builds the kura channel event record
				// *************************************************************************

				/*
				 * Creates a channel record with propertyName (KURA channel name) and the
				 * dataType of the Value
				 */
				record = ChannelRecord.createReadRecord(propertyName, dataType);
				/* Sets the actual value */
				record.setValue(typedValue.get());
				/* Sets the channel status */
				record.setChannelStatus(new ChannelStatus(SUCCESS));
				/* Gets the actual time stamp from JsonObject */
				JsonElement timeValue = jsonValue.get("time");
				/* Sets the actual time stamp in long format */
				record.setTimestamp(timeValue.getAsLong());
				/* Sets the ChannelConfiguration */
				record.setChannelConfig(channelConfiguration);

				LOGGER.info("Sends an onChannelEvent (ChannelListener) record: {}",
						record.getChannelConfig().get("formProperty"));
				/*
				 * Sends the new value further as channelEvent via the KURA Listener, this value
				 * is visible in UI.
				 */
				channelListener.onChannelEvent(new ChannelEvent(record));

				// *************************************************************************
				// Builds a channel record and send this via the {@link EventAdmin}
				// *************************************************************************

				/* Creates map of property objects to be send via the {@link EventAdmin} */
				Map<String, Object> properties = new HashMap<>();

				/* Puts the value (JSON object) into the property object */
				properties.put("value", jsonValue);
				/* Puts only the thing name into the property object */
				properties.put("thingName", thingName);
				/* Puts the property name into the property object */
				properties.put("propertyName", propertyName);

				/* Builds the topic name */
				String topic = Constants.EVENT_ADMIN_BASE_TOPIC_NAME.value() + thingName
						+ Constants.EVENT_ADMIN_PROPERTY_TAG_TOPIC_NAME.value() + propertyName;

				LOGGER.info("Sends an sendEvent (EventAdmin) record with topic: {}, properties: {}", topic, properties);

				/* Sends the new value via the OSGi {@link EventAdmin} */
				this.eventAdmin.sendEvent(new Event(topic, properties));
			}
		} catch (Throwable t) {
			LOGGER.error("", t);
		}
	}

	@Override
	public void close() {
		try {
			LOGGER.info("... here we do not need to do anything !");
			this.channelListener = null;
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	/**
	 * Returns the {@link TypedValue} determined from the {@link DataType} and the
	 * value object.
	 * 
	 * @param valueType the {@link DataType} from the value
	 * @param value     the value object
	 * @return the {@link TypedValue} instance
	 */
	private static Optional<TypedValue<?>> getTypedValue(DataType valueType, Object value) {
		try {
			switch (valueType) {
			case STRING:
				return Optional.of(TypedValues.newStringValue(value.toString()));
			default:
				return Optional.empty();
			}
		} catch (final Exception ex) {
			return Optional.empty();
		}
	}
}
