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
package org.ict.kura.core.thing.creator.impl;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
		id = "org.ict.kura.core.thing.creator.impl.ThingCreatorHref",
		name = "ThingCreator",
		description = "Configuration parameters | ThingCreator")
@interface ThingCreatorHrefConfig {
	@AttributeDefinition(name = "Base Href",
			type = AttributeType.STRING,
			required = true,
			cardinality = 0,
			defaultValue = "http://localhost:8080/services",
			description = "Base href of gateway to use for thing descriptions")
	String base_href();
}
