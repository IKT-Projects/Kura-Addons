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
package org.ict.kura.asset.creator;

import java.util.Map;

import org.eclipse.kura.channel.ChannelType;

/**
 * This interface describes a container to create, save and support kura assets
 * and channel configurations. 
 * 
 * @author IKT M. Biskup
 * @author IKT F. Kohlmorgen
 * @author IKT M. Kuller
 * @version 2020-11-30
 */
public interface AssetChannelCreator {
	/**
	 * Creates a kura asset with the given arguments.
	 * 
	 * @param driverPID   the name of the driver e.g. "DummyDriver" for example the
	 *                    name of the driver class
	 * @param componentID the componentID is the name of the asset and this is the
	 *                    unique identifier for the asset. The asset name can be the
	 *                    name of a device
	 * @return the asset configuration
	 */
	public Map<String, Object> createAsset(String driverPID, String componentID);

	/**
	 * Creates a kura standard channel configuration with the given arguments.
	 * 
	 * <pre>
	 * The standard channels:
	 * #+enabled: the channel is active or not
	 * #+name: the name of the channel
	 * #+type: the type of the channel
	 * #+value.type: the type of the value
	 * </pre>
	 * 
	 * @param channelName the channel name
	 * @param channelType the channel type
	 * @param valueType   the type of the value
	 * @return the channel configuration
	 */
	public Map<String, Object> createChannel(String channelName, ChannelType channelType, String valueType);

	/**
	 * Creates a kura standard and advanced channel configuration with the given
	 * arguments.
	 * 
	 * <pre>
	 * Advanced configuration example: 
	 * channels.put("Channel-" + channelName + "#" + "" + e.getKey(), e.getValue());
	 * </pre>
	 * 
	 * @param channelName the channel name
	 * @param channelType the channel type
	 * @param valueType   the type of the value
	 * @param form        the reference to the value
	 * @return the channel configuration
	 */
	public Map<String, Object> createChannel(String channelName, ChannelType channelType, String valueType,
			Map<String, Object> advancedConfiguration);
}
