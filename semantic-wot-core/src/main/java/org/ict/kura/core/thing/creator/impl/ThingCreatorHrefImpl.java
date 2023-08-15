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
package org.ict.kura.core.thing.creator.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.eclipse.kura.configuration.ConfigurableComponent;
import org.ict.kura.thing.creator.ThingCreator;
import org.ict.kura.thing.creator.ThingCreatorHref;
import org.ict.model.jsonld.context.Context;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class to help with the creation of W3C Thing Descriptions.
 * 
 * @author kohlmorgen
 */

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE, property = 
		"service.pid=org.ict.kura.core.thing.creator.impl.ThingCreatorHref", name = "org.ict.kura.core.thing.creator.impl.ThingCreatorHref")
@Designate(ocd = ThingCreatorHrefConfig.class)
public class ThingCreatorHrefImpl implements ThingCreatorHref, ConfigurableComponent {
	private static final Logger LOGGER = LoggerFactory.getLogger(ThingCreatorHrefImpl.class);
	private static final String APP_ID = "org.ict.kura.core.asset.creator.thing.ThingCreator";
	

	private ThingCreatorHrefOptions options;

	@Activate
	protected void activate(ComponentContext componentContext, Map<String, Object> properties) {
		/* Updates the configuration */
		LOGGER.info("Starting bundle " + APP_ID);
		doUpdate(properties);
		LOGGER.info("Bundle " + APP_ID + " has started!");
	}

	/**
	 * Binding function which to shutdown the bundle, see component.xml, is called
	 * by the OSGi framework
	 */
	@Deactivate
	protected void deactivate(ComponentContext componentContext) {
		LOGGER.info("Stopping bundle " + APP_ID);
		/* Closes all resources */
		doDeactivate();
		LOGGER.info("Bundle " + APP_ID + " has stopped!");
	}

	/**
	 * Method to handle configuration updates
	 * 
	 * @param properties Properties that are configured via the Kura web admin
	 * @throws URISyntaxException
	 */
	@Modified
	public void updated(Map<String, Object> properties) {
		LOGGER.info(" Updating bundle " + APP_ID);
		/* Deactivates all components */
		doDeactivate();

		/* Updates all components with new configuration */
		doUpdate(properties);
		LOGGER.info("Bundle " + APP_ID + " has updated!");
	}

	private void doUpdate(Map<String, Object> properties) {
		this.options = new ThingCreatorHrefOptions(properties);
		LOGGER.info(options.toString());

	}

	private void doDeactivate() {
		this.options = null;
	}

	@Override
	public ThingCreator getThingCreator() throws URISyntaxException {
		return new ThingCreatorImpl(new URI(options.getBaseHref()));
	}

	@Override
	public ThingCreator getThingCreatorWithContext(List<Context> contextList) throws URISyntaxException {
		return new ThingCreatorImpl(new URI(options.getBaseHref()), contextList);
	}


}
