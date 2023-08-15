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
package org.ict.kura.thing.creator;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.ict.kura.asset.creator.thing.util.ThingContainer;
import org.ict.model.wot.core.ActionAffordance;
import org.ict.model.wot.core.PropertyAffordance;
import org.ict.model.wot.core.Thing;

/**
 * The interface description to create kura asset and channels from a thing
 * description.
 * 
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2021-02-03
 */
public interface ThingProvider {
	/**
	 * Creates assets and channels from a list of web of thing descriptions and
	 * holds them in an internal memory (configuration service).
	 * 
	 * @param thingContainer the list of web of thing descriptions
	 * @exception an Exception
	 */
	public void createAssetsWithChannels(ThingContainer thingContainer) throws Exception;

	/**
	 * Creates an asset and channels with a driverPID and a web of thing description
	 * and holds this in an internal memory (configuration service).
	 * 
	 * @param driverPID the id of the driver
	 * @param thing     the thing
	 * @exception an Exception
	 * @see #createAssetsWithChannels(ThingContainer)
	 */
	public void createAssetsWithChannels(String driverPID, Thing thing) throws Exception;

	/**
	 * Deletes all assets and channels with the given driver id and the component
	 * configuration via the configuration service.
	 * 
	 * @param driverPID the driver id
	 * @exception an Exception
	 */
	public void deleteAssetsWithChannels(String driverPID) throws Exception;

	/**
	 * Gets a map with thing description property/action form href (key) linked to
	 * the corresponding semantic type informations (value).
	 * 
	 * @return a map with key (thing description property/action form href) and
	 *         value (semantic type)
	 */
	public Map<String, List<URI>> getSemanticTypes();

	/**
	 * Gets a map with modified thing ids. The property/action form href (key)
	 * linked to the corresponding thing id (value). the modified thing ids -
	 * without base url - are used as asset names.
	 * 
	 * @return a map with key (thing property/action form href) and value (modified
	 *         thing id)
	 */
	public Map<String, String> getIds();

	/**
	 * Gets a map with thing description property form href (key) linked to the
	 * corresponding {@link PropertyAffordance} (value).
	 * 
	 * @return a map with key (thing description property form href) and value
	 *         {@link PropertyAffordance}
	 */
	public Map<String, Map.Entry<String, PropertyAffordance>> getProperties();

	/**
	 * Gets a map with thing description action form href (key) linked to the
	 * corresponding {@link ActionAffordance} (value).
	 * 
	 * @return a map with key (thing description action form href) and value
	 *         {@link ActionAffordance}
	 */
	public Map<String, Map.Entry<String, ActionAffordance>> getActions();

	/**
	 * Gets {@link PropertyAffordance} with the given key (property form href).
	 * 
	 * @param key the property form href
	 * @return {@link PropertyAffordance} with the given key
	 */
	public PropertyAffordance getPropertyAffordance(String key);

	/**
	 * Gets {@link ActionAffordance} with the given key (action form href).
	 * 
	 * @param key the action form href
	 * @return {@link ActionAffordance} with the given key
	 */
	public ActionAffordance getActionAffordance(String key);
}
