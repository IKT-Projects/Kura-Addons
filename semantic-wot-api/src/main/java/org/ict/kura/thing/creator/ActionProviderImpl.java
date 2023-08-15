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

import org.ict.kura.asset.creator.thing.util.InteractionActions;
import org.ict.model.wot.core.ActionAffordance;

public enum ActionProviderImpl implements CreateableAction {

	OnOff() {
		@Override
		public ActionAffordance create(String baseHref, String id) throws Exception {
			return InteractionActions.createOnOffAction(baseHref, id);
		}
	},
	Position() {
		@Override
		public ActionAffordance create(String baseHref, String id) throws Exception {
			return InteractionActions.createPositionAction(baseHref, id);
		}
	},
	SlatAngle() {
		@Override
		public ActionAffordance create(String baseHref, String id) throws Exception {
			return InteractionActions.createSlatAngleAction(baseHref, id);
		}
	},
	TargetTemperature() {
		@Override
		public ActionAffordance create(String baseHref, String id) throws Exception {
			return InteractionActions.createTargetTempAction(baseHref, id);
		}
	},
	TemperatureOffSet() {
		@Override
		public ActionAffordance create(String baseHref, String id) throws Exception {
			return InteractionActions.createTempOffSetAction(baseHref, id);
		}
	},
	UpDown() {
		@Override
		public ActionAffordance create(String baseHref, String id) throws Exception {
			return InteractionActions.createUpDownAction(baseHref, id);
		}
	};
}
