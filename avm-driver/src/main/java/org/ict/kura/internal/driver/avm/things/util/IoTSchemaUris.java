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
package org.ict.kura.internal.driver.avm.things.util;

import java.net.URI;

public class IoTSchemaUris {

	public static String IOT = "http://iotschema.org/%s";
	public static String OM = "http://www.ontology-of-units-of-measure.org/resource/om-2/%s";
	public static String SAREF = "https://w3id.org/saref#%s";
	public static String SAREF4ENER = "https://saref.etsi.org/saref4ener/%s";
	public static String SAREFCORE = "https://saref.etsi.org/core/%s";
	public static String HTTP = "http://iotschema.org/protocol/http";
	public static String SCHEMA = "http://schema.org/%s";
	
	/* Devices */
	public static final URI SENSOR = URI.create(String.format(IOT, "Sensor"));
	public static final URI ACTUATOR = URI.create(String.format(IOT, "Actuator"));
	
	/* Capabilities */
	public static final URI ELECTRICPOWERSYSTEM = URI.create(String.format(IOT, "ElectricPowerSystem"));
	public static final URI THERMOSTAT = URI.create(String.format(IOT, "Thermostat"));
	public static final URI TEMPERATURESENSING = URI.create(String.format(IOT, "TemperatureSensing"));
	public static final URI HUMIDITYSENSING = URI.create(String.format(IOT, "HumiditySensing"));
	public static final URI ILLUMINANCESENSING = URI.create(String.format(IOT, "IlluminanceSensing"));
	public static final URI BINARYSWITCHCONTROL = URI.create(String.format(IOT, "BinarySwitchControl"));
	public static final URI MOTIONCONTROL = URI.create(String.format(IOT, "MotionControl"));
	public static final URI DIMMERCONTROL = URI.create(String.format(IOT, "DimmerControl"));
	public static final URI COLOURCONTROL = URI.create(String.format(IOT, "ColourControl"));
	public static final URI ENERGYMONITORING = URI.create(String.format(IOT, "EnergyMonitoring"));
	public static final URI POWERMONITORING = URI.create(String.format(IOT, "PowerMonitoring"));
	public static final URI AIRCONDITONER = URI.create(String.format(IOT, "AirConditioner"));
	public static final URI AMBIENTAIR = URI.create(String.format(IOT, "AmbientAirs"));
	public static final URI VALVE = URI.create(String.format(IOT, "Valve"));
	public static final URI ELECTRICBATTERY = URI.create(String.format(IOT, "ElectricBattery"));
	public static final URI LEVELSWITCH = URI.create(String.format(IOT, "LevelSwitch"));
	public static final URI FLOATSWITCH = URI.create(String.format(IOT, "FloatSwitch"));
	public static final URI PROXIMITYSENSING = URI.create(String.format(IOT, "ProximitySensing"));
	public static final URI SOUNDPRESSURE = URI.create(String.format(IOT, "SoundPressure"));
	public static final URI PUMP = URI.create(String.format(IOT, "Pump"));
	public static final URI CAMERA = URI.create(String.format(IOT, "Camera"));
	public static final URI MICROPHONE = URI.create(String.format(IOT, "Microphone"));
	public static final URI ULTRASONICSENSING = URI.create(String.format(IOT, "UltrasonicSensing"));
	public static final URI VOLTAGEMONITORING = URI.create(String.format(IOT, "VoltageMonitoring"));
	public static final URI ELECTRICCURRENTMONITORING = URI.create(String.format(IOT, "ElectricCurrentMonitoring"));
	
	/* Properties */
	public static final URI TEMPERATURE = URI.create(String.format(IOT, "Temperature"));
	public static final URI AIRTEMPERATURE = URI.create(String.format(IOT, "AirTemperature"));
	public static final URI CARBONDIOXIDECONCENTRATION = URI.create(String.format(IOT, "CarbonDioxideConcentration"));
	public static final URI CURRENTLEVEL = URI.create(String.format(IOT, "CurrentLevel"));
	public static final URI HUMIDITY = URI.create(String.format(IOT, "Humidity"));
	public static final URI ILLUMINANCE = URI.create(String.format(IOT, "Illuminance"));
	public static final URI SWITCHSTATUS = URI.create(String.format(IOT, "SwitchStatus"));
	public static final URI MOTIONTYPE = URI.create(String.format(IOT, "MotionType"));
	public static final URI CURRENTDIMMER = URI.create(String.format(IOT, "CurrentDimmer"));
	public static final URI ACTIVEENERGYIMPORTED = URI.create(String.format(IOT, "ActiveEnergyImported"));
	public static final URI ACTIVEENERGYEXPORTED = URI.create(String.format(IOT, "ActiveEnergyExported"));
	public static final URI REACTIVEENERGYIMPORTED = URI.create(String.format(IOT, "ReactiveEnergyImported"));
	public static final URI REACTIVEENERGYEXPORTED = URI.create(String.format(IOT, "ReactiveEnergyExported"));
	public static final URI TOTALACTIVEPOWER = URI.create(String.format(IOT, "TotalActivePower"));
	public static final URI ACTIVEPOWERA = URI.create(String.format(IOT, "ActivePowerA"));
	public static final URI ACTIVEPOWERB = URI.create(String.format(IOT, "ActivePowerB"));
	public static final URI ACTIVEPOWERC = URI.create(String.format(IOT, "ActivePowerC"));
	public static final URI TOTALREACTIVEPOWER = URI.create(String.format(IOT, "TotalReactivePower"));
	public static final URI REACTIVEPOWERA = URI.create(String.format(IOT, "ReactivePowerA"));
	public static final URI REACTIVEPOWERB = URI.create(String.format(IOT, "ReactivePowerB"));
	public static final URI REACTIVEPOWERC = URI.create(String.format(IOT, "ReactivePowerC"));
	public static final URI TARGETTEMPERATURE = URI.create(String.format(IOT, "TargetTemperature"));
	public static final URI CURRENTCOLOUR = URI.create(String.format(IOT, "CurrentColour"));
	public static final URI WINDSTRENGTH = URI.create(String.format(IOT, "WindStrength"));
	public static final URI LOWBATTERY = URI.create(String.format(IOT, "LowBattery"));
	public static final URI STATEOFCHARGE = URI.create(String.format(IOT, "StateOfCharge"));	
	public static final URI TOTALAPPARENTPOWER = URI.create(String.format(IOT, "TotalApparentPower"));
	public static final URI APPARENTENERGY = URI.create(String.format(IOT, "ApparentEnergy"));
	public static final URI TOTALPOWERFACTOR = URI.create(String.format(IOT, "TotalActivePower"));
	
	public static final URI APPARENTPOWERA = URI.create(String.format(IOT, "ApparentPowerA"));
	public static final URI APPARENTPOWERB = URI.create(String.format(IOT, "ApparentPowerB"));
	public static final URI APPARENTPOWERC = URI.create(String.format(IOT, "ApparentPowerC"));
	public static final URI POWERFACTORA = URI.create(String.format(IOT, "PowerFactorA"));
	public static final URI POWERFACTORB = URI.create(String.format(IOT, "PowerFactorB"));
	public static final URI POWERFACTORC = URI.create(String.format(IOT, "PowerFactorC"));
	public static final URI MAXCHARGEPOWER = URI.create(String.format(IOT, "MaxChargePower"));
	public static final URI MAXDISCHARGEPOWER = URI.create(String.format(IOT, "MaxDischargePower"));
	public static final URI PCCPOWER = URI.create(String.format(IOT, "PccPower"));
	public static final URI ELECTRICCURRENTA = URI.create(String.format(IOT, "ElectricCurrentA"));
	public static final URI ELECTRICCURRENTB = URI.create(String.format(IOT, "ElectricCurrentB"));
	public static final URI ELECTRICCURRENTC = URI.create(String.format(IOT, "ElectricCurrentC"));
	public static final URI AVERAGEELECTRICCURRENT = URI.create(String.format(IOT, "AverageElectricCurrent"));
	
	public static final URI PHASE_TO_NEUTRAL_VOLTAGE_A = URI.create(String.format(IOT, "PhaseToNeutralVoltageA"));
	public static final URI PHASE_TO_NEUTRAL_VOLTAGE_B = URI.create(String.format(IOT, "PhaseToNeutralVoltageB"));
	public static final URI PHASE_TO_NEUTRAL_VOLTAGE_C = URI.create(String.format(IOT, "PhaseToNeutralVoltageC"));
	public static final URI PHASE_TO_PHASE_VOLTAGE_AB = URI.create(String.format(IOT, "PhaseToPhaseVoltageAB"));
	public static final URI PHASE_TO_PHASE_VOLTAGE_BC = URI.create(String.format(IOT, "PhaseToPhaseVoltageBC"));
	public static final URI PHASE_TO_PHASE_VOLTAGE_CA = URI.create(String.format(IOT, "PhaseToPhaseVoltageCA"));
	public static final URI AVERAGE_PHASE_TO_NEUTRAL_VOLTAGE = URI.create(String.format(IOT, "AveragePhaseToNeutralVoltage"));
	public static final URI AVERAGE_PHASE_TO_PHASE_VOLTAGE = URI.create(String.format(IOT, "AveragePhaseToPhaseVoltage"));
	
	public static final URI TIMESERIES = URI.create(String.format(IOT, "Timeseries"));	
	
	/* Actions */
	public static final URI TURNOFF = URI.create(String.format(IOT, "TurnOff"));
	public static final URI TURNON = URI.create(String.format(IOT, "TurnOn"));
	public static final URI SETLEVEL = URI.create(String.format(IOT, "SetLevel"));
	public static final URI SETDIMMER = URI.create(String.format(IOT, "SetDimmer"));
	public static final URI SETCOLOUR = URI.create(String.format(IOT, "SetColour"));
	public static final URI STARTRECORDING = URI.create(String.format(IOT, "StartRecording"));
	public static final URI PAUSERECORDING = URI.create(String.format(IOT, "PauseRecording"));
	public static final URI RESUMERECORDING = URI.create(String.format(IOT, "ResumeRecording"));
	public static final URI STOPRECORDING = URI.create(String.format(IOT, "StopRecording"));
	public static final URI TOGGLEACTION = URI.create(String.format(IOT, "ToggleAction"));
	public static final URI CHANGEPROPERTYACTION = URI.create(String.format(IOT, "ChangePropertyAction"));
	public static final URI MAKEIMAGE = URI.create(String.format(IOT, "MakeImage"));
	public static final URI SETPOINTBATTERY = URI.create(String.format(IOT, "SetPointBattery"));
	public static final URI DISCHARGEBATTERY = URI.create(String.format(IOT, "DischargeBattery"));
	public static final URI CHARGEBATTERY = URI.create(String.format(IOT, "ChargeBattery"));
	
	/* Data */
	public static final URI CARBONDIOXIDECONCENTRATIONDATA = URI.create(String.format(IOT, "CarbonDioxideConcentrationData"));
	public static final URI CARBONDIOXIDECONCENTRATIONLIMITDATA = URI.create(String.format(IOT, "CarbonDioxideConcentrationLimitData"));
	public static final URI ELECTRICCHARGEDATA = URI.create(String.format(IOT, "ElectricChargeData"));
	public static final URI SOUNDPRESSUREDATA = URI.create(String.format(IOT, "SoundPressureData"));
	public static final URI DIMMERDATA = URI.create(String.format(IOT, "DimmerData"));
	public static final URI LEVELDATA = URI.create(String.format(IOT, "LevelData"));
	public static final URI PRESSUREDATA = URI.create(String.format(IOT, "PressureData"));
	public static final URI TEMPERATUREDATA = URI.create(String.format(IOT, "TemperatureData"));
	public static final URI RAMPTIMEDATA = URI.create(String.format(IOT, "RampTimeData"));
	public static final URI DEMANDCONTROLLEDVENTILATIONDATA = URI.create(String.format(IOT, "DemandControlledVentilationData"));
	public static final URI DENSITYDATA = URI.create(String.format(IOT, "DensityData"));
	public static final URI WINDDATA = URI.create(String.format(IOT, "WindData"));
	public static final URI ARGONCONCENTRATIONDATA = URI.create(String.format(IOT, "ArgonConcentrationData"));
	public static final URI HUMIDITYDATA = URI.create(String.format(IOT, "HumidityData"));
	public static final URI IMMERSIONDEPTHDATA = URI.create(String.format(IOT, "ImmersionDepthData"));
	public static final URI ILLUMINANCEDATA = URI.create(String.format(IOT, "IlluminanceData"));
	public static final URI LINEFREQUENCYDATA = URI.create(String.format(IOT, "LineFrequencyData"));
	public static final URI AUDIOVIDEODATA = URI.create(String.format(IOT, "AudioVideoData"));
	public static final URI RCOLOURDATA = URI.create(String.format(IOT, "RColourData"));
	public static final URI FLOWRATEDATA = URI.create(String.format(IOT, "FlowRateData"));
	public static final URI RUNMODEDATA = URI.create(String.format(IOT, "RunModeData"));
	public static final URI BCOLOURDATA = URI.create(String.format(IOT, "BColourData"));
	public static final URI CHARGINGSTATUSDATA = URI.create(String.format(IOT, "ChargingStatusData"));
	public static final URI CRATEDATA = URI.create(String.format(IOT, "CRateData"));
	public static final URI OXYGENCONCENTRATIONDATA = URI.create(String.format(IOT, "OxygenConcentrationData"));
	public static final URI ERATEDATA = URI.create(String.format(IOT, "ERateData"));
	public static final URI GCOLOURDATA = URI.create(String.format(IOT, "GColourData"));
	public static final URI NITROGENCONCENTRATIONDATA = URI.create(String.format(IOT, "NitrogenConcentrationData"));
	
	public static final URI LOWBATTERYDATA = URI.create(String.format(IOT, "LowBatteryData"));
	public static final URI STATEOFCHARGEDATA = URI.create(String.format(IOT, "StateOfChargeData"));
	public static final URI BOOLEANDATA = URI.create(String.format(IOT, "providesBooleanData"));
	public static final URI TIMEDATA = URI.create(String.format(IOT, "TimeData"));
	public static final URI MOTIONTYPEDATA = URI.create(String.format(IOT, "MotionTypeData"));
	
	public static final URI REACTIVEENERGYDATA = URI.create(String.format(IOT, "ReactiveEnergyData"));
	public static final URI ACTIVEENERGYDATA = URI.create(String.format(IOT, "ActiveEnergyData"));
	public static final URI APPARENTENERGYDATA = URI.create(String.format(IOT, "ApparentEnergyData"));
	
	public static final URI ACTIVEPOWERDATA = URI.create(String.format(IOT, "ActivePowerData"));
	public static final URI REACTIVEPOWERDATA = URI.create(String.format(IOT, "ReactivePowerData"));
	public static final URI POWERFACTORDATA = URI.create(String.format(IOT, "PowerFactorData"));
	public static final URI APPARENTPOWERDATA = URI.create(String.format(IOT, "ApparentPowerData"));
	
	public static final URI VOLTAGEDATA = URI.create(String.format(IOT, "VoltageData"));
	public static final URI ELECTRICCURRENTYDATA = URI.create(String.format(IOT, "ElectricCurrentData"));
	
	/* Unit */
	public static final String LEVELUNIT = String.format(IOT, "LevelUnit");
	public static final String PRESSUREUNIT = String.format(IOT, "PressureUnit");
	public static final String DOMAIN = String.format(IOT, "Domain");
	public static final String REACTIVEENERGYUNIT = String.format(IOT, "ReactiveEnergyUnit");
	public static final String ELECTRICCURRENTUNIT = String.format(IOT, "ElectricCurrentUnit");
	public static final String LINEFREQUENCYUNIT = String.format(IOT, "LineFrequencyUnit");
	public static final String REACTIVEPOWERUNIT = String.format(IOT, "ReactivePowerUnit");
	public static final String ACTIVEENERGYUNIT = String.format(IOT, "ActiveEnergyUnit");
	public static final String TEMPERATUREUNIT = String.format(IOT, "TemperatureUnit");
	public static final String GASINAIRCONCENTRATIONUNIT = String.format(IOT, "GasInAirConcentrationUnit");
	public static final String ACTIVEPOWERUNIT = String.format(IOT, "ActivePowerUnit");
	public static final String MOTIONSENSORTYPE = String.format(IOT, "MotionSensorType");
	public static final String APPARENTPOWERUNIT = String.format(IOT, "ApparentPowerUnit");
	public static final String APPARENTENERGYUNIT = String.format(IOT, "ApparentEnergyUnit");
	public static final String RUNMODETYPE = String.format(IOT, "RunModeType");
	public static final String AUDIOVIDEODATAMODE = String.format(IOT, "AudioVideoDataMode");
	public static final String TIMEUNIT = String.format(IOT, "TimeUnit");
	public static final String FLOWRATEUNIT = String.format(IOT, "FlowRateUnit");
	public static final String VOLTAGEUNIT = String.format(IOT, "VoltageUnit");
	
	
	/* Schema */
	public static final URI LATITUDE = URI.create(String.format(SCHEMA, "latitude"));	
	public static final URI LONGITUDE = URI.create(String.format(SCHEMA, "longitude"));	
	public static final URI GEOCOORDINATES = URI.create(String.format(SCHEMA, "GeoCoordinates"));
	public static final URI PROVIDESFLOWRATEDATA = URI.create(String.format(SCHEMA, "providesFlowRateData"));
	public static final URI NUMBER = URI.create(String.format(SCHEMA, "Number"));
	public static final String BOOLEAN = String.format(SCHEMA, "Boolean");
	public static final URI VALUE = URI.create(String.format(SCHEMA, "value"));
	
	
	/* Saref */
	public static final URI LIGHTNINGDEVICE = URI.create(String.format(SAREF, "LightingDevice"));	
	public static final URI UNITOFMEASURE = URI.create(String.format(SAREF, "UnitOfMeasure"));	
	public static final URI GAS = URI.create(String.format(SAREF, "Gas"));	
	public static final URI WATER = URI.create(String.format(SAREF, "Water"));	
	public static final URI METER = URI.create(String.format(SAREF, "Meter"));
	
	
	public static final URI ENERGY = URI.create(String.format(SAREFCORE, "Energy"));
	public static final URI POWER = URI.create(String.format(SAREFCORE, "Power"));
	public static final URI MULTILEVEL_STATE = URI.create(String.format(SAREFCORE, "MultiLevelState"));
	public static final URI ELECTRICITY = URI.create(String.format(SAREFCORE, "Electricity"));
}
