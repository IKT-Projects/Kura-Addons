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

import java.util.Optional;

import org.eclipse.kura.type.DataType;
import org.eclipse.kura.type.TypedValue;
import org.eclipse.kura.type.TypedValues;
import org.eclipse.kura.util.base.TypeUtil;

/**
 * Utility class.s
 * 
 * @author MK
 * @version 2023-05-25
 */
public class Util {
	/** To create an instance is not possible! */
	private Util() {
	}

	/**
	 * Returns the {@link TypedValue} determined from the {@link DataType} and the
	 * value object.
	 * 
	 * @param valueType the {@link DataType} from the value
	 * @param value     the value object
	 * @return the {@link TypedValue} instance
	 */
	public static Optional<TypedValue<?>> getTypedValue(DataType valueType, Object value) {
		try {
			switch (valueType) {
			case LONG:
				return Optional.of(TypedValues.newLongValue((long) Double.parseDouble(value.toString())));
			case FLOAT:
				return Optional.of(TypedValues.newFloatValue(Float.parseFloat(value.toString())));
			case DOUBLE:
				return Optional.of(TypedValues.newDoubleValue(Double.parseDouble(value.toString())));
			case INTEGER:
				return Optional.of(TypedValues.newIntegerValue((int) Double.parseDouble(value.toString())));
			case BOOLEAN:
				return Optional.of(TypedValues.newBooleanValue(Boolean.parseBoolean(value.toString())));
			case STRING:
				return Optional.of(TypedValues.newStringValue(value.toString()));
			case BYTE_ARRAY:
				return Optional.of(TypedValues.newByteArrayValue(TypeUtil.objectToByteArray(value)));
			default:
				return Optional.empty();
			}
		} catch (final Exception ex) {
			return Optional.empty();
		}
	}
}
