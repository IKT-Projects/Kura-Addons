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
package org.ict.kura.core.asset.creator.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.eclipse.kura.channel.ChannelType;
import org.ict.kura.asset.creator.AssetChannelCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The container implementation to create, save and support kura assets and
 * channel configurations.
 * 
 * @author IKT M. Biskup
 * @author IKT F. Kohlmorgen
 * @author IKT M. Kuller
 * @version 2020-11-30
 */
public class AssetChannelCreatorImpl implements AssetChannelCreator {
	/* The Multisensor driver logger instance */
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetChannelCreatorImpl.class);

	/* Kura must to have - ALWAYS the wire asset package name */
	private static final String FACTORY_PID = "org.eclipse.kura.wire.WireAsset";

	@Override
	public Map<String, Object> createAsset(String driverPID, String componentID) {
		Map<String, Object> asset = new HashMap<>();
		asset.put("componentId", componentID);
		asset.put("componentName", new String("Wire Asset"));
		asset.put("driver.pid", driverPID);
		asset.put("factoryPid", FACTORY_PID);
		asset.put("component.name", FACTORY_PID);
		asset.put("factoryComponent", Boolean.FALSE);
		
		asset.put("componentDescription", new String("Configure Wire Asset Instance"));
		asset.put("service.pid", new String(FACTORY_PID + "-" + System.currentTimeMillis() + "-1"));
		asset.put("kura.service.pid", componentID);
		asset.put("kura.ui.service.hide", Boolean.TRUE);
		asset.put("timestamp.mode", new String("PER_CHANNEL"));
		asset.put("asset.desc", new String(""));
		asset.put("service.factoryPid", FACTORY_PID);
		asset.put("emit.all.channels", Boolean.FALSE);
		return asset;
	}

	@Override
	public Map<String, Object> createChannel(String channelName, ChannelType channelType, String valueType) {
		/* Section argument null check */

		Objects.requireNonNull(channelName);
		Objects.requireNonNull(channelType);
		Objects.requireNonNull(valueType);

		/* Creates a channel map */
		Map<String, Object> channels = new HashMap<>();

		/* Section kura standard channel attributes */

		channels.put(channelName + "#+enabled", Boolean.TRUE);
		channels.put(channelName + "#+name", new String(channelName.toString()));
		channels.put(channelName + "#+type", channelType.name());
		channels.put(channelName + "#+value.type", valueType);

		/*
		 * This is the kura standard attribute #+listen. If this attribute is set to
		 * true here, the corresponding channel listener is started when the driver is
		 * started.
		 */
		channels.put(channelName + "#+listen", Boolean.TRUE);

		/* Returns the channel configuration */
		return channels;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Object> createChannel(String channelName, ChannelType channelType, String valueType,
			Map<String, Object> advancedConfiguration) {
		/* Section argument null check */
		Objects.requireNonNull(advancedConfiguration);

		/* Creates the standard channels */
		Map<String, Object> channels = createChannel(channelName, channelType, valueType);

		/* Section dummy driver additional channel attributes */
		for (Entry e : advancedConfiguration.entrySet()) {
			LOGGER.info("Advanced channel configuration {} - {}", e.getKey(), e.getValue());
			channels.put(channelName + "#" + e.getKey(), e.getValue());
		}

		/* Returns the standard and advanced channel configuration */
		return channels;
	}
}
