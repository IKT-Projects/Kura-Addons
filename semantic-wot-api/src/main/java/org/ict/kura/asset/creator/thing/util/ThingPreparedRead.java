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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.kura.channel.ChannelRecord;
import org.eclipse.kura.driver.Driver;
import org.eclipse.kura.driver.Driver.ConnectionException;
import org.eclipse.kura.driver.PreparedRead;

/**
 * Prepare read class implementation for the initial read of all channels
 * 
 * @author IKT M. Biskup
 * @author IKT M. Kuller
 * @version 2020-12-17
 */
public class ThingPreparedRead implements PreparedRead {
	/*
	 * The {@link Driver} - we need here in the method {@link #execute()} where the
	 * drivers {@link Driver#read()} method is called to set the initial value of
	 * each parameter.
	 */
	private final Driver driver;
	/* This is the prepared channel record list for the Kura framework */
	private volatile List<ChannelRecord> channelRecords;

	/**
	 * Constructor of {@link ThingPreparedRead}
	 * 
	 * @param driver         the {@link Driver} instance
	 * @param channelRecords the list of {@link ChannelRecord}
	 */
	public ThingPreparedRead(final Driver driver, final List<ChannelRecord> channelRecords) {
		this.driver = driver;
		this.channelRecords = channelRecords;
	}

	@Override
	public synchronized List<ChannelRecord> execute() throws ConnectionException {

		for (ChannelRecord record : this.channelRecords) {
			/* Sets the initial value of each parameter */
			driver.read(Arrays.asList(record));
		}
		/*
		 * In this case we allow the Kura framework to read all parameters (channel
		 * records)
		 */
		return Collections.unmodifiableList(this.channelRecords);
	}

	@Override
	public List<ChannelRecord> getChannelRecords() {
		return Collections.unmodifiableList(this.channelRecords);
	}

	@Override
	public void close() {
		// do nothing
	}

	@Override
	public String toString() {
		return "ThingPreparedRead [driver=" + driver + ", channelRecords=" + channelRecords + "]";
	}
}