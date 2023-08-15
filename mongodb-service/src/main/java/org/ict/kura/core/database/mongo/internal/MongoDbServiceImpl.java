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
package org.ict.kura.core.database.mongo.internal;

import static com.mongodb.client.model.Filters.eq;
import static java.util.Objects.isNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.eclipse.kura.configuration.ConfigurableComponent;
import org.eclipse.kura.crypto.CryptoService;
import org.ict.gson.utils.AdapterFactory;
import org.ict.kura.core.database.mongo.MongoDbService;
import org.ict.model.wot.core.Thing;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

/**
 * Example OSGi Kura bundle which supports a ConfigurableComponent to configure
 * the bundle via Kura Web Admin and which supports methods to communicate with
 * a MongoDB.
 * 
 * @author MB, MK
 * @version 2020-02-20
 */

@Designate(ocd = MongoConfig.class, factory = true)
@Component(immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE, property = "service.pid=org.ict.kura.core.database.mongo.MongoDbService", name = "org.ict.kura.core.database.mongo.MongoDbService")
public class MongoDbServiceImpl implements MongoDbService, ConfigurableComponent {

	private static final Logger LOGGER = LoggerFactory.getLogger(MongoDbServiceImpl.class);
	private static final String APP_ID = "org.ict.kura.core.database.mongo.MongoDbService";

	/* The MongoDB instance */
	private MongoClient mongoClient;

	/* The MogoDB credentials */
	@SuppressWarnings("unused")
	private MongoCredential mongoCredential;

	/* The MongoDB */
	private MongoDatabase mongoDatabase;

	/* The collection container to read write mongo documents */
	private MongoCollection<Document> mongoCollection;

	/* The gson object to convert a thing in json format */
	private static Gson gson;

	/* The kura crypto service instance */
	private CryptoService cryptoService;

	private MongoDbServiceOptions options;

	/**
	 * Binding function which starts the bundle, see component.xml, is called by the
	 * OSGi framework
	 * 
	 * @see 
	 */
	@Activate
	protected void activate(ComponentContext componentContext, Map<String, Object> properties) {
		LOGGER.info("Bundle " + APP_ID + " has started!");
		/* Updates the configuration */
		updated(properties);
	}

	/**
	 * Binding function which to shutdown the bundle, see component.xml, is called
	 * by the OSGi framework
	 */
	@Deactivate
	protected void deactivate(ComponentContext componentContext) {
		LOGGER.info("Bundle " + APP_ID + " has stopped!");
		/* Closes all resources */
		doDeactivate();
	}

	/**
	 * Method to handle configuration updates
	 * 
	 * @param properties Properties that are configured via the admin web interface
	 *                   in kura
	 */
	@Modified
	public void updated(Map<String, Object> properties) {
		/* Deactivates all components */
		doDeactivate();

		/* Updates all components with new configuration */
		doUpdate(properties);
		LOGGER.info("updated");
	}

	@Override
	public void doUpdate(Map<String, Object> properties) {
		try {
			this.options = new MongoDbServiceOptions(properties, cryptoService);

			if (options.getEnableDatabaseConnection()) {
				/* Creates the connection to the database server */
				this.mongoClient = new MongoClient(options.getDataBaseConnectionIp(),
						options.getDataBaseConnectionPort());

				LOGGER.info("Mongo client connected to " + options.getDataBaseConnectionIp() + ":"
						+ options.getDataBaseConnectionPort());

				/*
				 * Specifies the name of the database to the getDatabase() method. If a database
				 * does not exist, MongoDB creates the database when you first store data for
				 * that database.
				 */
				mongoDatabase = mongoClient.getDatabase(options.getDatabaseName());

				/* Creates a collection container to read write mongo documents */
				mongoCollection = this.mongoDatabase.getCollection("things");

				/* Initializes the gson object */
				gson = AdapterFactory.getGsonWithDefaultWotTypeAdapters();
			}

		} catch (Throwable T) {
			LOGGER.error(T.getMessage(), T);
			System.out.print(T.getMessage());
		}
	}

	@Override
	public void doDeactivate() {
		/* Closes the client connection */
		if (mongoClient != null)
			mongoClient.close();

		/* Resets objects */
		mongoDatabase = null;
		mongoCollection = null;
		gson = null;
		LOGGER.info("deactivate");
	}

	@Reference(name = "CryptoService", service = CryptoService.class, cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "unsetCryptoService", bind = "setCryptoService")
	protected synchronized void setCryptoService(final CryptoService cryptoService) {
		if (isNull(this.cryptoService)) {
			this.cryptoService = cryptoService;
		}
	}

	protected synchronized void unsetCryptoService(final CryptoService cryptoService) {
		if (this.cryptoService == cryptoService) {
			this.cryptoService = null;
		}
	}

	@Override
	public List<Thing> findAll() {
		MongoCursor<Document> cursor = null;
		List<Thing> things = new ArrayList<>();
		try {
			LOGGER.info("Returns all things from the repository (mongo)");
			/* Looking for things */
			cursor = mongoCollection.find().iterator();
			/* Creates things */

			while (cursor.hasNext()) {
				String cursorNext = cursor.next().toJson();
				things.add(gson.fromJson(cursorNext, Thing.class));
			}
		} finally {
			LOGGER.info("Closes the cursor");
			cursor.close();
		}
		/* Returns the thing list */
		return things;
	}

	@Override
	public Thing findById(URI uri) {
		LOGGER.info("Returns the thing with the given id from the repository (mongo) {}", uri.toString());

		/* Finds thing by id */
		Document doc = mongoCollection.find(eq("id", uri.toString())).first();

		/* Checks if the thing with the given uri exists */
		if (doc == null) {
			/* No thing found */
			throw new IndexOutOfBoundsException(
					String.format("Thing with the given uri '%s' not found", uri.toString()));
		} else {
			/* Thing found */
			LOGGER.info("Thing found: {}", doc.toJson());
			return gson.fromJson(doc.toJson(), Thing.class);
		}
	}

	@Override
	public Thing save(Thing thing) {
		LOGGER.info("Saves the given thing in the repository (mongo) {}", thing.toString());

		/* Converts the thing to json string and document */
		Document doc = Document.parse(gson.toJson(thing));

		doc.put("_id", thing.getBase() + thing.getIdRDF()); // put ObjectId to document
		/* Inserts the thing in the mongo repository */
		mongoCollection.insertOne(doc);

		LOGGER.info("Bundle {} writes thing in the repository successful {}", APP_ID, doc.toJson());
		return thing;
	}

	@Override
	public Thing update(Thing thing) {
		LOGGER.info("Deletes thing with the given id saves the given thing in the repository (mongo) {}",
				thing.toString());

		/* Converts the thing to json string and document */
		Document doc = Document.parse(gson.toJson(thing));
//		doc.put("_id", thing.getBase()+thing.getIdRDF());  //put ObjectId to document
		/* Updates the thing with the given id */
		mongoCollection.updateMany(eq("id", thing.getId().toString()), new Document("$set", doc));

		LOGGER.info("Bundle {} writes thing in the repository successful {}", APP_ID, thing.toString());
		return thing;
	}

	@Override
	public void delete(Thing thing) {
		LOGGER.info("Deletes the given thing in the repository (mongo) {}", thing.toString());

		/* Deletes thing whose id field equals the given uri */
		DeleteResult deleteResult = mongoCollection.deleteOne(eq("id", thing.getId().toString()));

		/* The thing does not exists */
		if (deleteResult.getDeletedCount() == 0) {
			throw new IndexOutOfBoundsException(
					String.format("Thing with the given uri '%s' not found", thing.getId()));
		}
	}

	@Override
	public void deleteById(URI uri) {
		LOGGER.info("Deletes the thing with the given id in the repository (mongo) {}", uri.toString());

		/* Deletes thing whose uri field equals the given uri */
		DeleteResult deleteResult = mongoCollection.deleteOne(eq("id", uri.toString()));

		/* The thing does not exists */
		if (deleteResult.getDeletedCount() == 0) {
			throw new IndexOutOfBoundsException(
					String.format("Thing with the given uri '%s' not found", uri.toString()));
		}
	}

	@Override
	public void deleteAll() {
		LOGGER.info("Deletes all things in the repository (mongo)");

		/* Deletes all documents from collection */
		FindIterable<Document> findIterable = mongoCollection.find();
		for (Document document : findIterable) {
			mongoCollection.deleteMany(document);
		}
	}

}