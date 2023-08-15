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
package org.ict.kura.core.cloud.command.provider;

public class String_T {

	public static void main(String[] args) {
		String topic = "$EDC/account-name/client/telemetry/assets/asset1/channels/channel1";

		System.out.println(topic.lastIndexOf("assets/"));
		System.out.println(topic.indexOf("/channels"));

		String assetName = topic.substring(topic.lastIndexOf("assets/"), topic.indexOf("/channels"));
		assetName = assetName.substring(assetName.indexOf("/") + 1);
		String channelName = topic.substring(topic.indexOf("channels"));
		channelName = channelName.substring(channelName.indexOf("/") + 1);
		System.out.println(assetName);
		System.out.println(channelName);

		
		System.out.printf("AssetName:{%s} chanelName:{%s}",assetName, channelName);
	}

}
