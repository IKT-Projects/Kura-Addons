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
package org.ict.kura.core.database.influx.internal.value;


import java.time.Instant;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

@Measurement(name = "property_raw")
public class Value {
	@Column
	Object value;
	@Column(tag = true, name = "propertyId")
	String propertyId;
	@Column(tag = true, name = "_field")
	String field;
	@Column(tag = true, name = "source")
	String source;
	@Column(tag = true, name = "thingId")
	String thingId;
	@Column(timestamp = true)
	Instant time;

	@java.lang.SuppressWarnings("all")
	private static String $default$source() {
		return "fhdo";
	}

	@java.lang.SuppressWarnings("all")
	public static class ValueBuilder {
		@java.lang.SuppressWarnings("all")
		private Object value;
		@java.lang.SuppressWarnings("all")
		private String propertyId;
		@java.lang.SuppressWarnings("all")
		private String field;
		@java.lang.SuppressWarnings("all")
		private boolean source$set;
		@java.lang.SuppressWarnings("all")
		private String source$value;
		@java.lang.SuppressWarnings("all")
		private String thingId;
		@java.lang.SuppressWarnings("all")
		private Instant time;

		@java.lang.SuppressWarnings("all")
		ValueBuilder() {
		}

		@java.lang.SuppressWarnings("all")
		public Value.ValueBuilder value(final Object value) {
			this.value = value;
			return this;
		}

		@java.lang.SuppressWarnings("all")
		public Value.ValueBuilder propertyId(final String propertyId) {
			this.propertyId = propertyId;
			return this;
		}

		@java.lang.SuppressWarnings("all")
		public Value.ValueBuilder field(final String field) {
			this.field = field;
			return this;
		}

		@java.lang.SuppressWarnings("all")
		public Value.ValueBuilder source(final String source) {
			this.source$value = source;
			source$set = true;
			return this;
		}

		@java.lang.SuppressWarnings("all")
		public Value.ValueBuilder thingId(final String thingId) {
			this.thingId = thingId;
			return this;
		}

		@java.lang.SuppressWarnings("all")
		public Value.ValueBuilder time(final Instant time) {
			this.time = time;
			return this;
		}

		@java.lang.SuppressWarnings("all")
		public Value build() {
			String source$value = this.source$value;
			if (!this.source$set)
				source$value = Value.$default$source();
			return new Value(this.value, this.propertyId, this.field, source$value, this.thingId, this.time);
		}

		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "Value.ValueBuilder(value=" + this.value + ", propertyId=" + this.propertyId + ", field="
					+ this.field + ", source$value=" + this.source$value + ", thingId=" + this.thingId + ", time="
					+ this.time + ")";
		}
	}

	@java.lang.SuppressWarnings("all")
	public static Value.ValueBuilder builder() {
		return new Value.ValueBuilder();
	}

	@java.lang.SuppressWarnings("all")
	public Value() {
		this.source = Value.$default$source();
	}

	@java.lang.SuppressWarnings("all")
	public Value(final Object value, final String propertyId, final String field, final String source,
			final String thingId, final Instant time) {
		this.value = value;
		this.propertyId = propertyId;
		this.field = field;
		this.source = source;
		this.thingId = thingId;
		this.time = time;
	}

	public String getType() {
		return this.value.getClass().getSimpleName();
	}

	@java.lang.SuppressWarnings("all")
	public Object getValue() {
		return this.value;
	}

	@java.lang.SuppressWarnings("all")
	public String getPropertyId() {
		return this.propertyId;
	}

	@java.lang.SuppressWarnings("all")
	public String getField() {
		return this.field;
	}

	@java.lang.SuppressWarnings("all")
	public String getSource() {
		return this.source;
	}

	@java.lang.SuppressWarnings("all")
	public String getThingId() {
		return this.thingId;
	}

	@java.lang.SuppressWarnings("all")
	public Instant getTime() {
		return this.time;
	}

	@java.lang.SuppressWarnings("all")
	public void setValue(final Object value) {
		this.value = value;
	}

	@java.lang.SuppressWarnings("all")
	public void setPropertyId(final String propertyId) {
		this.propertyId = propertyId;
	}

	@java.lang.SuppressWarnings("all")
	public void setField(final String field) {
		this.field = field;
	}

	@java.lang.SuppressWarnings("all")
	public void setSource(final String source) {
		this.source = source;
	}

	@java.lang.SuppressWarnings("all")
	public void setThingId(final String thingId) {
		this.thingId = thingId;
	}

	@java.lang.SuppressWarnings("all")
	public void setTime(final Instant time) {
		this.time = time;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Value))
			return false;
		final Value other = (Value) o;
		if (!other.canEqual((java.lang.Object) this))
			return false;
		final java.lang.Object this$value = this.getValue();
		final java.lang.Object other$value = other.getValue();
		if (this$value == null ? other$value != null : !this$value.equals(other$value))
			return false;
		final java.lang.Object this$propertyId = this.getPropertyId();
		final java.lang.Object other$propertyId = other.getPropertyId();
		if (this$propertyId == null ? other$propertyId != null : !this$propertyId.equals(other$propertyId))
			return false;
		final java.lang.Object this$field = this.getField();
		final java.lang.Object other$field = other.getField();
		if (this$field == null ? other$field != null : !this$field.equals(other$field))
			return false;
		final java.lang.Object this$source = this.getSource();
		final java.lang.Object other$source = other.getSource();
		if (this$source == null ? other$source != null : !this$source.equals(other$source))
			return false;
		final java.lang.Object this$thingId = this.getThingId();
		final java.lang.Object other$thingId = other.getThingId();
		if (this$thingId == null ? other$thingId != null : !this$thingId.equals(other$thingId))
			return false;
		final java.lang.Object this$time = this.getTime();
		final java.lang.Object other$time = other.getTime();
		if (this$time == null ? other$time != null : !this$time.equals(other$time))
			return false;
		return true;
	}

	@java.lang.SuppressWarnings("all")
	protected boolean canEqual(final java.lang.Object other) {
		return other instanceof Value;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final java.lang.Object $value = this.getValue();
		result = result * PRIME + ($value == null ? 43 : $value.hashCode());
		final java.lang.Object $propertyId = this.getPropertyId();
		result = result * PRIME + ($propertyId == null ? 43 : $propertyId.hashCode());
		final java.lang.Object $field = this.getField();
		result = result * PRIME + ($field == null ? 43 : $field.hashCode());
		final java.lang.Object $source = this.getSource();
		result = result * PRIME + ($source == null ? 43 : $source.hashCode());
		final java.lang.Object $thingId = this.getThingId();
		result = result * PRIME + ($thingId == null ? 43 : $thingId.hashCode());
		final java.lang.Object $time = this.getTime();
		result = result * PRIME + ($time == null ? 43 : $time.hashCode());
		return result;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "Value(value=" + this.getValue() + ", propertyId=" + this.getPropertyId() + ", field=" + this.getField()
				+ ", source=" + this.getSource() + ", thingId=" + this.getThingId() + ", time=" + this.getTime() + ")";
	}
}
