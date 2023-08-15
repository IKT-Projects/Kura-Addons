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
package org.ict.kura.internal.driver.coap.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.ServiceUnavailableException;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.WebLink;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.network.config.NetworkConfigDefaults;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.ict.gson.utils.AdapterFactory;
import org.ict.kura.internal.driver.coap.util.Util;
import org.ict.model.wot.core.Thing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom coap client used to establish connection to sensor station. Has
 * additional functions to request data with thing structure for kura framework.
 * 
 * @author IKT B. Helgers
 *
 */
public class CustomCoapClient extends CoapClient implements Closeable {

	/* Preinitialized objects instances */
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomCoapClient.class);

	/* Regex expressions for requests */
	private final static String IP_REGEX = "^([0-9]{1,3}[\\.][0-9]{1,3}[\\.][0-9]{1,3}[\\.][0-9]{1,3})$";
	private final static String THING_REGEX = "^(['/']things['/'][a-zA-Z0-9-]+)$";

	/* Preinitialized variables */
	private final static String COAP_PREFIX = "coap://";
	private long DEFAULT_TIME_OUT = 30000;

	private String serverIp;
	private int serverPort;

	private Thing thing = null;

	/* HashMaps to manage discovered resources and CoapObserveRelations */
	private HashMap<String, CoapObserveRelation> coapObserveRelations = new HashMap<>();
	private Set<WebLink> webLinks;

	/**
	 * Constructor of CustomKuraClient with parameters</br>
	 * 'serverIp','serverPort' and 'timeOut' to establish connection to server.
	 * 
	 * @param serverIp   server ip with format 'xxx.xxx.xxx.xxx'
	 * @param serverPort server communication port
	 * @param timeOut    time duration till timeOut
	 * @throws IllegalArgumentException if server ip has wrong format
	 * @throws IOException              if server can't be reached
	 * @throws ConnectorException
	 */
	public CustomCoapClient(String serverIp, int serverPort, long timeOut) throws IOException, ConnectorException {
		super();

		/* Checks if server ip is nonNull and matches ip format ? */
		if (serverIp == null)
			throw new IllegalArgumentException("Server IP is null !");

		// Loads the IP regular expression
		Pattern p = Pattern.compile(IP_REGEX);
		Matcher m = p.matcher(serverIp);

		// Checks the IP format
		if (m.matches())
			this.serverIp = serverIp;
		else
			throw new IllegalArgumentException(
					"Server ip '" + serverIp + "' does not match expected format XXX.XXX.XXX.XXX!");

		/* Checks is chosen port is valid uses default coap port if null */
		if (serverPort > 0 && serverPort >= 65535)
			this.serverPort = serverPort;
		else
			this.serverPort = CoAP.DEFAULT_COAP_PORT;

		/* Checks client time out */
		if (timeOut < 0)
			this.setTimeout(DEFAULT_TIME_OUT);
		else
			this.setTimeout(timeOut);

		/* Sets up network config */
		NetworkConfig config = NetworkConfig.getStandard();
		config.set(NetworkConfig.Keys.MAX_RESOURCE_BODY_SIZE, 2 * 1024 * 1024);
		config.set(NetworkConfig.Keys.MAX_MESSAGE_SIZE, 1024);
		config.set(NetworkConfig.Keys.PREFERRED_BLOCK_SIZE, 1024);

		/* Sets custom network config */
		NetworkConfig.setStandard(config);

		/* Client settings for communication with notification for each request */
		useCONs();

		/* Sets the timeout */
		setTimeout(timeOut);

		/* Sets client to send message blocks of 1024 byte */
		useEarlyNegotiation(1024);

		// #####################################################################################################

		/* Builds target uri as string and connects client */
		String serverURI = new String(COAP_PREFIX + serverIp + ":" + serverPort);
		this.setURI(serverURI);

		/* Pings target server and exits client if endpoint is not responding */
		if (ping()) {
			LOGGER.info("Successfully tested connection to server " + serverIp + "...");

			/* Sends a discover request to server */
			webLinks = super.discover();

			/* Checks received Set<WebLink> from target server for null pointer */
			if (webLinks != null)
				LOGGER.info("Successfully received server resources!");
			else
				LOGGER.warn("No resources found. Error must have occured!");
		} else
			throw new IOException(
					"Request timed out due to error on server. Please check server connection and try restarting!!!");
	}

	/**
	 * Iterates through set of discovered WebLink objects. Filters each element with
	 * path of thing resource using regular expression and requests full thing
	 * description. If request to resource is successful thing description is added
	 * to list.
	 * 
	 * 
	 */
	public Thing requestThingResource() throws Exception {
		Map<String, String> map = null;
		Pattern pattern = null;
		Matcher matcher = null;
		String resURI = "";

		/* Initialize list of strings */
		map = new HashMap<>();

		/* Setup regex matcher for properties */
		pattern = Pattern.compile(THING_REGEX);

		/* Iterate through set of WebLinks */
		for (WebLink webLink : this.webLinks) {

			/* Insert path uri to matcher */
			matcher = pattern.matcher(webLink.getURI());

			/* Check if uri sting matches regex expression */
			if (matcher.matches()) {

				/* Remove '/' from string uri */
				resURI = new String("coap://" + serverIp + ":" + serverPort + webLink.getURI());

				/* Connect client to resource */
				setURI(resURI);
				LOGGER.info("Client connected to: " + this.getURI());

				/* Build request with options */
				Request request = new Request(Code.GET, Type.CON);
				request.getOptions().setAccept(MediaTypeRegistry.APPLICATION_JSON);
				int mid = new Random().nextInt((64000 - 0) + 1) + 0;
				request.setMID(mid);
				request.getOptions().setMaxAge(10);
				LOGGER.info("Sends request with options: " + Utils.prettyPrint(request));

				/* Request resource with expected type */
				CoapResponse response = this.advanced(request);

				if (response == null) {
					LOGGER.info("Request was not successful...");
					/* Handle response */
					handleResponseType(response);
				} else {
					LOGGER.info("Request was successful...");
					/* Handle response */
					handleResponseType(response);

					/* Check response for code indicating success */
					if (response.getCode() == ResponseCode.CONTENT) {
						/* Put thing description to list */
						map.put(Util.extractNameFromPath(webLink.getURI(), THING_REGEX), response.getResponseText());

						/* Deserialize thing description to object using gson */
						this.thing = AdapterFactory.getGsonWithDefaultWotTypeAdapters(true)
								.fromJson(response.getResponseText(), Thing.class);
					}
				}
			}
		}
		return this.thing;
	}

	/**
	 * Handles response code of received CoapResponse(Stack) returned by server.
	 * Throws error or exception depending on gravity of error.Returns resource
	 * payload or changed state if code indicates successful request.
	 * 
	 * @param response CoapResponse to request
	 * 
	 * @throws BadRequestException,InternalServerErrorException ,NotSupportedException,NotAllowedException,
	 *                                                          ServiceUnavailableException,NotAuthorizedException,
	 *                                                          NotFoundException
	 */
	public void handleResponseType(CoapResponse response)
			throws BadRequestException, InternalServerErrorException, NotSupportedException, NotAcceptableException,
			NotAllowedException, ServiceUnavailableException, NotAuthorizedException, NotFoundException {
		try {
			switch (response.getCode()) {
			case GATEWAY_TIMEOUT:
				if (response.advanced().isServerError()) {
					throw new ServiceUnavailableException(
							"Request timed out due to error on server. Please check server connection and try restarting!!!");
				} else {
					throw new ServiceUnavailableException(
							"Request timed out or connection was interrupted. Please check request and connection to server!!!");
				}
			case BAD_GATEWAY:
				throw new BadRequestException("Unable to process returned response on server or resource!!!");
			case INTERNAL_SERVER_ERROR:
				throw new InternalServerErrorException(
						"Unknown server error occurred!!! Please check connection settings and restart server.");
			case METHOD_NOT_ALLOWED:
				throw new NotAllowedException("Method call at resource not allowed!!!");
			case UNSUPPORTED_CONTENT_FORMAT:
				throw new NotSupportedException(String.format(
						"Unsupported content format '%s' in request!!! Please use stated types: %s, %s, %s ",
						response.advanced().getOptions().getContentFormat(), "MediaTypeRegistry.TEXT_PLAIN",
						"MediaTypeRegistry.APPLICATION_XML", "MediaTypeRegistry.APPLICATION_JASON"));
			case REQUEST_ENTITY_TOO_LARGE:
				throw new UnsupportedOperationException(String.format(
						"Resource can't handle request with size %d. Maximal request body size is by default %d byte.",
						response.advanced().getPayloadSize(), NetworkConfigDefaults.DEFAULT_MAX_RESOURCE_BODY_SIZE));
			case BAD_OPTION:
				throw new NotAcceptableException(
						"Unsupported options. Please check request options and retry sending request!!!");
			case UNAUTHORIZED:
				throw new NotAuthorizedException("Unauthorized access at resource. Please check security settings!!!");
			case CHANGED:
				LOGGER.info("State of actuator changed!!!");
				break;
			case CONTENT:
				LOGGER.debug("Received response from resource '%s': %s", response.advanced().getOptions().getUriPath(),
						response.getResponseText());
				break;
			case DELETED:
				LOGGER.debug("Deleted resource %s from server....", response.advanced().getOptions().getUriPath());
				break;
			case CREATED:
				LOGGER.info("Created new resource %s with content: %s !!!",
						response.advanced().getOptions().getUriPath(), response.getResponseText());
				break;
			case NOT_FOUND:
				throw new NotFoundException(String.format("Target resource '%s' could not be found!!!",
						response.advanced().getOptions().getUriPath()));
			default:
				throw new InternalServerErrorException(
						"Unknown response code. Can't process received response code!!!");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Tests if observe relation was already created and initializes new connection
	 * to resource. If user tries to observe other resource although connected to
	 * different resource observe relation is canceled forcefully. If observe
	 * relation was initialized or just canceled it reconnect to new resource. Uses
	 * handler to notify user about incoming responses or errors from server.
	 * 
	 * @param resourcePath Resource name as string
	 * 
	 * @throws IllegalArgumentException Thrown if resource name is null or empty
	 */
	public void observeResource(String resourcePath, CoapHandler handler, int mediaType) {
		/* Key name specific to server ip and resource name */
		String entryName = "";
		CoapObserveRelation r = null;

		/* Optional check of method parameters */
		Optional<String> name = Optional.of(resourcePath);
		if (!name.isPresent())
			throw new IllegalArgumentException("Argument 0 is invalid !");

		/* arg1 null check */
		Objects.requireNonNull(handler);

		/* Checks the media type */
		if (mediaType <= -1)
			throw new IllegalArgumentException("Method observeResource expects a valid media type for the observe!");

		/* Store build entry to string */
		entryName = Util.createIndividualResourceName(name.get());

		/* Searches map for key and checks state of observe relation if found */
		if (coapObserveRelations.containsKey(entryName)) {
			/* Stores observe relation from map */
			r = coapObserveRelations.get(entryName);

			/* Does nothing if active else relation is reregistered */
			if (r.isCanceled()) {
				r.reregister();
			}
		} else {
			/* Create instance of CoapObserveRelation to resource */
			r = observe(handler, mediaType);

			/* Bind instance of CoapObserveRelation to ObserveHandler */
			((ObserveHandler) handler).bindCoapObserveRelationToHandler(r);

			/* Adds observe relation to map */
			coapObserveRelations.put(entryName, r);
		}
		LOGGER.info("Starting to observe resource: " + entryName);
	}

	/**
	 * Reads current state of observe relation and tests if observeRelation is still
	 * active. If still active client sends a request with observe flag and cancels
	 * active observeRelation to resource. Notifies user about successfully aborted
	 * observe relation to resource.
	 * 
	 * @throws Exception
	 */
	public void cancelSelectedObserveRelation(String id) throws Exception {

		/* Initializes variables for observe relation and key string */
		CoapObserveRelation r = null;
		String relKey = "";

		/* Checks id string using optionals */
		Optional<String> key = Optional.of(id);
		if (key.isPresent()) {
			relKey = key.get();
		} else {
			throw new IllegalArgumentException("Key id of ObserveRelation can't be empty!");
		}

		try {

			if (coapObserveRelations.containsKey(relKey)) {

				/* Requests selected observe relation by key from map */
				r = coapObserveRelations.get(relKey);

				/* Checks state of stored observe relation and cancels it if still active */
				if (r == null) {
					LOGGER.info("Observe relation with id '" + relKey + "' was already successfully canceled!!!");
				} else {

					/*
					 * Active observe relation is canceled by 'forgetting' and later checked if
					 * successful
					 */
					if (!r.isCanceled()) {

						/* Forget observe relation */
						r.reactiveCancel();

						/* Check if cancel was successful */
						if (r.isCanceled() == true) {
							LOGGER.info("Observe relation with id '" + relKey + "' was successfully canceled!!!");
							coapObserveRelations.remove(relKey);
						}
					}
				}
			} else {
				throw new IllegalArgumentException("No ObserveRelation with key " + id + " found!");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * If client has active observe relations to server resources all are canceled.
	 * 
	 * @throws Exception
	 */
	public void cancelAllObserveRelations() throws Exception {

		String observName = "";
		CoapObserveRelation rel = null;

		try {

			if (coapObserveRelations.isEmpty()) {
				LOGGER.info("No active observe relations. Ready to shut down client...");
			} else {
				LOGGER.info("Starting to cancel all observe relations...");
				for (Map.Entry<String, CoapObserveRelation> r : coapObserveRelations.entrySet()) {

					/* Gets name of to be canceled observe relation */
					observName = r.getKey();
					LOGGER.debug("Trying to cancel observe relation with key: " + observName);

					/* Gets observe relation from list */
					rel = r.getValue();

					/* Forget observe relation */
					rel.reactiveCancel();

					/* Check if observe relation is canceled */
					if (rel.isCanceled()) {
						LOGGER.info("Observe relation with key '" + observName + "' was successfully canceled");
					}
					/* Wait 100ms to synchronize access to map */
					Thread.sleep(100);

				}
				/* Clears map holding observe relations */
				coapObserveRelations.clear();
			}
		} catch (Exception e) {
			throw e;
		}
	}

	// ##################################################################################################
	// The getter / setter section
	// ##################################################################################################

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public Thing getThing() {
		return thing;
	}

	public void setThing(Thing thing) {
		this.thing = thing;
	}

	public Set<WebLink> getWebLinks() {
		return webLinks;
	}

	public void setWebLinks(Set<WebLink> webLinks) {
		this.webLinks = webLinks;
	}

	@Override
	public void close() throws IOException {
		try {
			/* Cancel all active observe relations */
			cancelAllObserveRelations();
			/* Shutdown the CoapClient */
			shutdown();
		} catch (Exception e) {
			// here we do nothing
		}
	}
}