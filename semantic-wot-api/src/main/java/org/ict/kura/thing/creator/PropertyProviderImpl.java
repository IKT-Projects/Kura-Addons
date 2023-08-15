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

import org.ict.kura.asset.creator.thing.util.InteractionProperties;
import org.ict.model.wot.core.PropertyAffordance;

public enum PropertyProviderImpl implements CreateableProperty {
	ActiveEnergyExported() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createActiveEnergyExportedProperty(baseHref, id);
		}
	},
	ActiveEnergyImported() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createActiveEnergyImportedProperty(baseHref, id);
		}
	},
	ActivePowerA() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createActivePowerAProperty(baseHref, id);
		}
	},
	ActivePowerB() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createActivePowerBPropertie(baseHref, id);
		}
	},
	ActivePowerC() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createActivePowerCPropertie(baseHref, id);
		}
	},
	ApparentEnergy() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createApparentEnergyProperty(baseHref, id);
		}
	},
	ApparentPower() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createApparentPowerProperty(baseHref, id,
					"Magnitude of the complex power |S|");
		}
	},
	CarbonDioxide() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createCarbonDioxideProperty(baseHref, id,
					"A chemical compound made up of molecules that each have one carbon atom covalently double bonded to two oxygen atoms");
		}
	},
	ElectricCurrent() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createCurrentProperty(baseHref, id,
					"A stream of charged particles, such as electrons or ions, moving through an electrical conductor or space");
		}
	},
	Dimming() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createDimmingProperty(baseHref, id,
					"Capability to lower the brightness of the light");
		}
	},
	GeneralElectricity() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createGeneralElectricityProperty(baseHref, id,
					"Property for general electricity measurements");
		}
	},
	Illuminance() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createIlluminanceProperty(baseHref, id);
		}
	},
	LevelCustom() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createLevelCustomProperty(baseHref, id, "A custom level property.");
		}
	},
	LocationLength() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createLocationLengthProperty(baseHref, id);
		}
	},
	LocationWidth() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createLocationWidthProperty(baseHref, id);
		}
	},
	MeasuredTotal() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createMeasuredTotalProperty(baseHref, id, "Measured total", "measured-total");
		}
	},
	Motion() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createMotionProperty(baseHref, id);
		}
	},
	MultiLevelState() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createMutliLevelStateProperty(baseHref, id, "Multi level state");
		}
	},
	OpenClose() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createOpenCloseProperty(baseHref, id);
		}
	},
	Position() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createPositionProperty(baseHref, id);
		}
	},
	ReactiveEnergyExported() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createReactiveEnergyExportedProperty(baseHref, id);
		}
	},
	SlatAngle() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createSlatAngleProperty(baseHref, id);
		}
	},
	TVOC() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createTvocProperty(baseHref, id);
		}
	},
	UpDown() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createUpDownroperty(baseHref, id);
		}
	},
	Voltage() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createVoltageProperty(baseHref, id,
					"Difference in electric potential between two points");
		}
	},
	Wind() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createWindProperty(baseHref, id);
		}
	},
	Temperature() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createTemperatureProperty(baseHref, id);
		}
	},
	Pressure() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createPressureProperty(baseHref, id);
		}
	},
	FlowRate() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createFlowRateProperty(baseHref, id);
		}

	},
	VolumeTotal() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createVolumeTotalProperty(baseHref, id);
		}
	},
	TotalActivePower() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createTotalActivePowerProperty(baseHref, id);
		}
	},
	SwitchStatus() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createSwitchStatusProperty(baseHref, id);
		}
	},
	LowBattery() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createLowBatteryProperty(baseHref, id);
		}
	},
	Lock() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createLockProperty(baseHref, id);
		}
	},
	Battery() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createBatteryProperty(baseHref, id);
		}
	},
	Humidity() {
		@Override
		public PropertyAffordance create(String baseHref, String id) throws Exception {
			return InteractionProperties.createHumidityProperty(baseHref, id);
		}
	}
}
