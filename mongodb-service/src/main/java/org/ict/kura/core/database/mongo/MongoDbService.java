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
package org.ict.kura.core.database.mongo;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.ict.model.wot.core.Thing;

/**
 * The interface with methods to communicate with an MongoDB and which is
 * exported as OSGi declarative service.
 * 
 * @author MB, MK
 * @version 2020-02-12
 */
public interface MongoDbService {
	/**
	 * Updates all resources
	 * 
	 * @param properties
	 *            Configuration parameters
	 */
	public void doUpdate(Map<String, Object> properties);

	/**
	 * Closes all resources
	 */
	public void doDeactivate();

	/**
	 * Returns a list of entities, saved in the database.
	 * 
	 * @return all entities
	 */
	public List<Thing> findAll();

	/**
	 * Retrieves an entity by its id.
	 * 
	 * @return the entity with the given uri
	 * @exception IndexOutOfBoundsException
	 *                if the entity not exists
	 */
	public Thing findById(URI uri);

	/**
	 * Saves a given entity. Use the returned instance for further operations as the
	 * save operation might have changed the entity instance completely.
	 * 
	 * @param thing
	 *            must not be null.
	 * @return the saved entity.
	 */
	public Thing save(Thing thing);
	
	
	/**
	 * Updates a given entity. Use the returned instance for further operations as
	 * the update operation might have changed the entity instance completely.
	 * 
	 * @param thing
	 *            must not be null.
	 * @return the saved entity.
	 */
	public Thing update(Thing thing);

	/**
	 * Deletes a given entity.
	 * 
	 * @param thing
	 *            must not be null.
	 * @exception IndexOutOfBoundsException
	 *                if the entity not exists
	 */
	public void delete(Thing thing);

	/**
	 * Deletes the entity with the given id.
	 * 
	 * @param uri
	 *            must not be null.
	 * @exception IndexOutOfBoundsException
	 *                if the entity not exists
	 */
	public void deleteById(URI uri);

	/**
	 * Deletes all entities managed by the repository.
	 */
	public void deleteAll();

}

