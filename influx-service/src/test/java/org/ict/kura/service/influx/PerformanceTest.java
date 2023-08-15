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
package org.ict.kura.service.influx;



import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ict.kura.core.database.influx.InfluxDbService;
import org.ict.kura.core.database.influx.internal.InfluxDbServiceImpl;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;

public class PerformanceTest {

	private static InfluxDbService service;
	private static InfluxDBClient client;

	private static ExecutorService executorService = Executors.newCachedThreadPool();;

	public static void main(String[] args) throws Exception {
		//Anzahl der Threads festlegen
		int numberOfThreads = 1000;
		//Anzahl der zu sendenden datenpunkte
		int numberOfDatas = 10000;
		String ip = "192.168.13.100";
		int port = 8086;
		String databaseToken = "OzooESpnUksto_Nxsyjow-4g5hf7d8EfuzXYs_0lpmes0h6L9DdiagkASztV2ik6l0zhxBgOs-q7Uz006t2cmg==";
		String org = "21df672e94306aa7";
		String bucket = "data";
		String host = "http://" + ip + ":" + port;

		client = InfluxDBClientFactory.create(host, databaseToken.toCharArray(), org, bucket);
		service = new InfluxDbServiceImpl(client, bucket, org);

		for (int i = 0; i < numberOfThreads; i++) {
			executorService.submit(new Task(service, i, numberOfDatas));
			Thread.sleep((int) (Math.random() * 300) + 1);
		}

		

//		executorService.submit(() -> {
//			while (true) {
//				try {
//					try {
//						System.out.println("Version: " + client.version());
//					} catch (Exception e) {
//						System.err.println("Error version: " + e);
//					}
//
//					System.out.println("Ping: " + client.ping());
//					System.out.println("Ready: " + client.ready());
//
//					Thread.sleep(1000);
//				} catch (Exception e) {
//					System.err.println("Error on Ping: " + e);
//					Thread.sleep(1000);
//				}
//
//			}
//		});

	}

	static class Task implements Runnable {
		private InfluxDbService service;
		private int number;
		int numberOfDatas;

		public Task(InfluxDbService service, int number, int numberOfDatas) {
			this.service = service;
			this.number = number;
			this.numberOfDatas = numberOfDatas;
		}

		@Override
		public void run() {
//			System.out.println("Task: is running "+this);
			for (int i = 0; i < numberOfDatas; i++) {
				Map<String, Object> fieldValueMap = new HashMap<>();
				fieldValueMap.put("Property_" + number, (int) (Math.random() * 1000) + 20);
				service.save("Thing_" + number, "Property_" + number, System.currentTimeMillis(), fieldValueMap);
//				System.out.println("Task: write "+this);
				try {
					Thread.sleep(500);
				} catch (Exception e) {
					System.err.println("Error on Write: " + e);
				}
			}
		}
	}

}
