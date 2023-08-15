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
package org.ict.kura.asset.creator.thing.util;

import java.io.File;
import java.io.IOException;

import org.ict.kura.thing.model.ThingsConfig;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.*;

public abstract class ConfigReader {
	private ObjectMapper mapper;

	public ConfigReader() {
		this.mapper = new ObjectMapper(new YAMLFactory());
		this.mapper.findAndRegisterModules();
	}

	/**
	 * Default configuration reader implementation for YAML files. Can be overridden
	 * to read other formats(e.g. JSON, XML) or even technology specific formats
	 * like KNX-ETS files.
	 * 
	 * @param config The configuration file to read
	 * @return The configuration object
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public ThingsConfig readConfig(File config) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(config, ThingsConfig.class);
	}
}
