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

/**
 * The Context implementation ...
 *
 * @author IKT MK
 * @version 2020-06-19
 */
public class PayloadContext implements PayloadContextIf {
	/**
	 * The currently loaded strategy - the PayloadStrategyWot is the default
	 * strategy
	 */
	private PayloadStrategyIf strategy = new PayloadStrategyWot();

	public KuraMessage convert(Event event) {
		return strategy.convertMessage(event);
	}

	public void setStrategy(PayloadStrategyIf strategy) {
		this.strategy = strategy;
	}

	public PayloadStrategyIf getStrategy() {
		return strategy;
	}

	@Override
	public String toString() {
		if (strategy instanceof PayloadStrategyWot)
			return "PayloadContext with [strategy=WoT]";
		else if (strategy instanceof PayloadStrategyTb)
			return "PayloadContext with [strategy=Thingsboard]";
		else
			return "PayloadContext with [strategy=UNDEFINED]";
	}
}
