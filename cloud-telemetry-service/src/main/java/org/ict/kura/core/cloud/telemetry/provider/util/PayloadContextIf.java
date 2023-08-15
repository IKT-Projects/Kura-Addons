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
package org.ict.kura.core.cloud.telemetry.provider.util;

import org.eclipse.kura.cloudconnection.message.KuraMessage;
import org.osgi.service.event.Event;

//****************************************************************************
/**
 * The Context interface ...
 *
 * @author IKT MK
 * @author IKT MB
 * @version 2023-05-24
 */
//****************************************************************************
public interface PayloadContextIf {
	/**
	 * Execute method which implements the concrete algorithm and delegates the call
	 * of the method in the strategy interface
	 * 
	 * @param the kura event
	 * @return the new kura message forwarded to the MQTT broker
	 */
	public KuraMessage convert(Event event);

	/**
	 * Sets the strategy interface
	 * 
	 * @param strategy the strategy interface (interface of the concrete
	 *                 implementation)
	 */
	public void setStrategy(PayloadStrategyIf strategy);

	/**
	 * Gets the strategy interface
	 * 
	 * @return the strategy interface
	 */
	public PayloadStrategyIf getStrategy();
}
