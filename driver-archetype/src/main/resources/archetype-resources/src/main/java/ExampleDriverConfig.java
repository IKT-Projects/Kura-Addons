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
package $package;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * The driver configuration.
 * 
 * @author 
 * @author 
 * @version 
 */

/*
 * The metatype xml file is created on the basis of these annotations, which is
 * used to create a configuration page in Kura Web Ui. This enables the
 * configuration of the bundle via the UI.
 */
@ObjectClassDefinition(id = "$package", name = "NAME Driver", description = "Name Driver", localization = "en_us")

@interface ExampleDriverConfig {
	@AttributeDefinition(name = "value", type = AttributeType.STRING, required = true, defaultValue = "12324", description = "example value")
	String example_value();

}
