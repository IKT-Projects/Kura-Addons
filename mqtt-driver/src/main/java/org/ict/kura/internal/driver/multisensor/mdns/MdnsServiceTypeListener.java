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
package org.ict.kura.internal.driver.multisensor.mdns;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceTypeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MdnsServiceTypeListener implements ServiceTypeListener{
	/* The logger instance */
	private static final Logger LOGGER = LoggerFactory.getLogger(MdnsServiceListener.class);

	@Override
	public void serviceTypeAdded(ServiceEvent event) {
		LOGGER.info("SE %s",event);
	}

	@Override
	public void subTypeForServiceTypeAdded(ServiceEvent event) {
		LOGGER.info("SE %s",event);
	}

}
