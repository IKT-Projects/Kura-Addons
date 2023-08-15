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

import org.eclipse.kura.core.configuration.metatype.Tad;
import org.eclipse.kura.core.configuration.metatype.Tscalar;
import org.ict.kura.asset.creator.util.ChannelDescriptorBase;

/**
 * This is an empty {@link ChannelDesciptor} implementation. The Kura framework
 * expects a channel descriptor instance. This must not be null ! So we give the
 * Kura framework what it wants - an empty {@link Tad} object.
 * 
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2020-12-20
 * @see {@link e.g. MultisensorDriver#getChannelDescriptor()}
 */
public class ThingChannelDescriptor extends ChannelDescriptorBase {

	/*
	 * if the first argument in the constructor is not equals null, this attributes
	 * are used
	 */

	private static final String PROP_FORMPROPERTY = "formProperty";
	private static final String PROP_FORMPROPERTY_BINDING = "formPropertyBinding";
	private static final String PROP_FORMACTION = "formAction";
	private static final String PROP_FORMACTION_BINDING = "formActionBinding";

	public ThingChannelDescriptor() {
		/* Adds the property form href to the {@link ChannelDescription} */
		final Tad formProperty = new Tad();
		formProperty.setName(PROP_FORMPROPERTY);
		formProperty.setId(PROP_FORMPROPERTY);
		formProperty.setDescription("Property Form");
		formProperty.setType(Tscalar.STRING);
		formProperty.setRequired(false);
		tads.add(formProperty);

		/* Adds the property form binding to the {@link ChannelDescription} */
		final Tad formPropertyBinding = new Tad();
		formPropertyBinding.setName(PROP_FORMPROPERTY_BINDING);
		formPropertyBinding.setId(PROP_FORMPROPERTY_BINDING);
		formPropertyBinding.setDescription("Property Form Binding");
		formPropertyBinding.setType(Tscalar.STRING);
		formPropertyBinding.setRequired(false);
		tads.add(formPropertyBinding);

		/* Adds the action form href to {@link ChannelDescription} */
		final Tad formAction = new Tad();
		formAction.setName(PROP_FORMACTION);
		formAction.setId(PROP_FORMACTION);
		formAction.setDescription("Action Form");
		formAction.setType(Tscalar.STRING);
		formAction.setRequired(false);
		tads.add(formAction);

		/* Adds the action form binding to {@link ChannelDescription} */
		final Tad formActionBinding = new Tad();
		formActionBinding.setName(PROP_FORMACTION_BINDING);
		formActionBinding.setId(PROP_FORMACTION_BINDING);
		formActionBinding.setDescription("Action Form Binding");
		formActionBinding.setType(Tscalar.STRING);
		formActionBinding.setRequired(false);
		tads.add(formActionBinding);
	}
}
