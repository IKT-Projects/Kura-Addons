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
 * The Strategy interface ...
 *
 * @author IKT MK
 * @version 2020-06-19
 */
//****************************************************************************
interface PayloadStrategyIf {

	/**
	 * Execute method which implements the concrete algorithm
	 * 
	 * @param file the configuration file with path and name
	 */
	public KuraMessage convertMessage(Event event);
}
