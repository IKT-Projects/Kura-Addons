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
package org.ict.kura.internal.driver.coap.client;

import static java.util.Objects.requireNonNull;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.ict.kura.driver.thing.ThingChannelListener;
import org.ict.kura.internal.driver.coap.CoapDriver;
import org.ict.kura.internal.driver.coap.util.CoapBindingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The implementation of interface {@link CoapHandler} handling received<br/>
 * notifications about changed state of observed resources or devices.<br/>
 * <br/>
 * Each notification triggers implemented method {@link onLoad} calling
 * {@link doUpdate} of bound<br/>
 * {@link ThingChannelListener} to refresh associated channel value in kura ui.
 * 
 * @author IKT B. Helgers
 * 
 * @throws NullPointerException if bound instance of CoapListener is null
 */
public class ObserveHandler implements CoapHandler {

	/* The logger instance */
	private static final Logger LOGGER = LoggerFactory.getLogger(ObserveHandler.class);

	/* The instance of bound CoapListener */
	private CoapBindingConfig bindingConfig;
	private CoapDriver coapDriver;
	private CoapObserveRelation observeRelation;

	public ObserveHandler(CoapBindingConfig coapBindingConfig, CoapDriver driver) {
		this.bindingConfig = requireNonNull(coapBindingConfig, "Instance of CoapBindingConfig can't be null!");
		this.coapDriver = requireNonNull(driver, "Instance of CoapDriver can't be null!");
	}

	@Override
	public void onError() {

		if (observeRelation.isCanceled()) {
			LOGGER.info("Observe relation was reregistered...");
			observeRelation.reregister();
		}
	}

	@Override
	public void onLoad(CoapResponse arg0) {

		/* Prints out response for control */
//		LOGGER.info("OnLoad response payload: " + arg0.getResponseText());

		if (!observeRelation.isCanceled()) {

			/* Gets current response directly from CoapObserveRelation instance */
			CoapResponse response = observeRelation.getCurrent();

			if (response != null) {

				/* Print information on current response */
				LOGGER.info("Current response from CoapObserveRelation: " + Utils.prettyPrint(response));

				/* Updates channel data using linked implementation of CoapListener */
				coapDriver.getUpdateBindingConfigurations().get(bindingConfig).doUpdate(response.getResponseText());
			}
		}

	}

	public void bindCoapObserveRelationToHandler(CoapObserveRelation relation) {
		this.observeRelation = requireNonNull(relation, "Instance of observe relation can't be null!");
	}
}