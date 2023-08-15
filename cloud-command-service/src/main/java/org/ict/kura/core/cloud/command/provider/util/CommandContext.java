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
package org.ict.kura.core.cloud.command.provider.util;

import org.eclipse.kura.KuraException;
import org.eclipse.kura.asset.AssetService;
import org.eclipse.kura.cloudconnection.message.KuraMessage;

/**
 * The Context implementation ...
 *
 * @author IKT MK
 * @version 2020-06-19
 */
public class CommandContext implements CommandContextIf {
	/**
	 * The currently loaded strategy - the PayloadStrategyWot is the default
	 * strategy
	 */
	private CommandStrategyIf strategy = new CommandStrategyWotOneWay();

	public void convert(KuraMessage message, AssetService as) throws KuraException {
		strategy.convertAndRedirectMessage(message, as);
	}

	public void setStrategy(CommandStrategyIf strategy) {
		this.strategy = strategy;
	}

	public CommandStrategyIf getStrategy() {
		return strategy;
	}

	@Override
	public String toString() {
		if (strategy instanceof CommandStrategyWotOneWay)
			return "PayloadContext with [strategy=WoT]";
		else if (strategy instanceof CommandStrategyTbTwoWaySync)
			return "PayloadContext with [strategy=Thingsboard]";
		else
			return "PayloadContext with [strategy=UNDEFINED]";
	}
}
