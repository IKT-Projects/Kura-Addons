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
package org.ict.kura.asset.creator.util;

import java.util.List;

import org.eclipse.kura.core.configuration.metatype.Tad;
import org.eclipse.kura.driver.ChannelDescriptor;
import org.eclipse.kura.util.collection.CollectionUtil;

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
public abstract class ChannelDescriptorBase implements ChannelDescriptor {
	/*
	 * The {@link Tad} list. {@link Tad} specified a complex datatype structure
	 * which is also described in XML. In this case the structure is empty. We have
	 * no advanced configuration to visualize in the Kura web admin !
	 */
	protected final List<Tad> tads = CollectionUtil.newArrayList();
	

	@Override
	public Object getDescriptor() {
		return tads;
	}
}
