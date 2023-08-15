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
package org.ict.kura.core.thing.creator.impl;

import static org.ict.model.wot.constant.In.Header;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.ict.kura.asset.creator.thing.util.PropertyData;
import org.ict.kura.thing.creator.ActionProviderImpl;
import org.ict.kura.thing.creator.EventProviderImpl;
import org.ict.kura.thing.creator.PropertyProviderImpl;
import org.ict.kura.thing.creator.ThingCreator;
import org.ict.kura.thing.model.ActionAffordanceConfig;
import org.ict.kura.thing.model.Bindable;
import org.ict.kura.thing.model.EventAffordanceConfig;
import org.ict.kura.thing.model.EventConfig;
import org.ict.kura.thing.model.PropertyAffordanceConfig;
import org.ict.kura.thing.model.PropertyConfig;
import org.ict.kura.thing.model.ThingConfig;
import org.ict.kura.thing.model.ThingsConfig;
import org.ict.model.jsonld.context.Context;
import org.ict.model.wot.constant.Op;
import org.ict.model.wot.constant.SecuritySchemaType;
import org.ict.model.wot.core.ActionAffordance;
import org.ict.model.wot.core.EventAffordance;
import org.ict.model.wot.core.PropertyAffordance;
import org.ict.model.wot.core.Thing;
import org.ict.model.wot.core.VersionInfo;
import org.ict.model.wot.dataschema.BooleanSchema;
import org.ict.model.wot.dataschema.DataSchema;
import org.ict.model.wot.dataschema.IntegerSchema;
import org.ict.model.wot.dataschema.NumberSchema;
import org.ict.model.wot.dataschema.ObjectSchema;
import org.ict.model.wot.hypermedia.AdditionalProperty;
import org.ict.model.wot.hypermedia.Form;
import org.ict.model.wot.security.BasicSecurityScheme;
import org.ict.model.wot.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;


public class ThingCreatorImpl implements ThingCreator {
	private static final Logger LOGGER = LoggerFactory.getLogger(ThingCreatorImpl.class);
	
	@SuppressWarnings("serial")
	private static final List<Context> DEFAULT_CONTEXT = new ArrayList<Context>() {
		{
			add(Context.builder().prefix(null).namespace("https://www.w3.org/2019/wot/td/v1").build());
			add(Context.builder().prefix("schema").namespace("http://schema.org/").build());
			add(Context.builder().prefix("iot").namespace("http://iotschema.org/").build());
			add(Context.builder().prefix("http").namespace("http://iotschema.org/protocol/http").build());
			add(Context.builder().prefix("om").namespace("http://www.ontology-of-units-of-measure.org/resource/om-2/")
					.build());
		}
	};
	private List<Context> context;
	private URI baseHref;

	@SuppressWarnings("unused")
	private ThingCreatorImpl() {
	}

	/**
	 * Construct a instance of the ThingCreater class with the given base URI. As
	 * long as the models thing belong to the same domain the base URI should stay
	 * the same. Otherwise the base URI can be modified with
	 * {@link #setBaseHref(URI) setBaseHref} method. The context is set to the
	 * default context containing schema.org, iotschema.org,
	 * iotschema.org/protocol/http and ontology-of-units-of-measure.org.
	 * 
	 * @param baseHref The basic URI used to create thing id's and form URIs
	 */
	public ThingCreatorImpl(URI baseHref) {
		if (baseHref.toString().endsWith("/")) {
			this.baseHref = URI.create(baseHref.toString().substring(0, baseHref.toString().length() - 1));
		} else {
			this.baseHref = baseHref;
		}
		this.context = DEFAULT_CONTEXT;
	}

	/**
	 * Construct a instance of the ThingCreater class with the given base URI. As
	 * long as the models thing belong to the same domain the base URI should stay
	 * the same. Otherwise the base URI can be modified with
	 * {@link #setBaseHref(URI) setBaseHref} method. The context is set to the
	 * default context containing schema.org, iotschema.org,
	 * iotschema.org/protocol/http and ontology-of-units-of-measure.org.
	 * 
	 * @param baseHref      The basic URI used to create thing id's and form URIs
	 * @param customContext A custom context for the TD creation
	 */
	public ThingCreatorImpl(URI baseHref, List<Context> customContext) {
		if (baseHref.toString().endsWith("/")) {
			this.baseHref = URI.create(baseHref.toString().substring(0, baseHref.toString().length() - 1));
		} else {
			this.baseHref = baseHref;
		}
		this.context = customContext;
	}

	/**
	 * Create a new thing description with the provided informations. The full thing
	 * id is generated by the combination of the {@link #baseHref} and the
	 * {@link thingId}
	 * 
	 * @param thingId       A relative thingId which is used to create the full id
	 *                      in combination with the {@link #baseHref}
	 * @param title         The title of the thing
	 * @param description   A description of the thing
	 * @param semanticTypes The semantic type this thing should be tagged with
	 * @return A new thing containing the provided informations
	 */
	
	@Override
	public Thing create(String thingId, String title, String description, URI... semanticTypes) {
		final URI id = URI.create(this.baseHref.toString() + "/things/" + thingId);
		final Map<String, SecurityScheme> secDef = new HashMap<>();
		secDef.put("basic_sc", BasicSecurityScheme.builder().scheme(SecuritySchemaType.Basic).in(Header).build());
		final List<String> security = new ArrayList<>();
		security.add("basic_sc");

		return Thing.builder().contexts(context.toArray(new Context[0])).atId(id).id(id).title(title)
				.description(description).version(VersionInfo.builder().instance("0.1").build()).created(new Date())
				.modified(new Date()).support(URI.create("https://www.fh-dortmund.de/de/fb/10/ikt/index.php"))
				.base(baseHref).atType(Arrays.asList(semanticTypes)).securityDefinitions(secDef).security(security)
				.build();
	}

	
	@Override
	public List<Thing> createFromConfig(ThingsConfig config) {
		var things = new ArrayList<Thing>(config.getThings().size());
		config.getThings().stream().map(c -> {
			var thingURI = URI.create(this.baseHref + "/" + "things/" + c.getThing());
			var thing = create(c.getThing(), c.getThing(), c.getDescription(), (URI) null);
			if (c.getProperties() != null && c.getProperties().size() > 0) {
				var properties = processPropertyConfigs(thingURI, c.getProperties());
				addProperties(thing, properties);
			}
			if (c.getActions() != null && c.getActions().size() > 0) {
				var actions = processActionConfigs(thingURI, c.getActions());
				addActions(thing, actions);
			}
			if (c.getEvents() != null && c.getEvents().size() > 0) {
				var events = processEventConfigs(thingURI, c.getEvents());
				addEvents(thing, events);
			}
			return thing;
		}).forEach(things::add);
		return things;
	}

	private Map<String, PropertyAffordance> processPropertyConfigs(URI thingURI,
			List<PropertyAffordanceConfig> properties) {
		return properties.stream().map(p -> {
			try {
				if (p.getTemplate() != null) {
					var template = PropertyProviderImpl.valueOf(p.getTemplate());
					var property = template.create(thingURI.toString(), p.getProperty());
					var bindings = createBindings(p);
					addPropertyBinding(property, bindings);
					return property;
				} else {
					var property = createCustomProperty(thingURI, p);
					var bindings = createBindings(p);
					addPropertyBinding(property, bindings);
					return property;
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.warn(e.getMessage());
			}
			return null;
		}).filter(prop -> prop != null).collect(Collectors.toMap(prop -> prop.getName(), Function.identity()));
	}

	private Map<String, ActionAffordance> processActionConfigs(URI thingURI, List<ActionAffordanceConfig> actions) {
		return actions.stream().map(a -> {
			try {
				if (a.getTemplate() != null) {
					var template = ActionProviderImpl.valueOf(a.getTemplate());
					var action = template.create(thingURI.toString(), a.getAction());
					var bindings = createBindings(a);
					addActionBinding(action, bindings);
					return action;
				} else {
					var bindings = createBindings(a);
					var action = createCustomAction(thingURI, a);
					addActionBinding(action, bindings);
					return action;
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.warn(e.getMessage());
			}
			return null;
		}).filter(act -> act != null).collect(Collectors.toMap(act -> act.getName(), Function.identity()));
	}

	private Map<String, EventAffordance> processEventConfigs(URI thingURI, List<EventAffordanceConfig> events) {
		return events.stream().map(evt -> {
			try {
				if (evt.getTemplate() != null) {
					var template = EventProviderImpl.valueOf(evt.getTemplate());
					var event = template.create(thingURI.toString(), evt.getEvent());
					var bindings = createBindings(evt);
					addEventBinding(event, bindings);
					return event;
				} else {
					var bindings = createBindings(evt);
					var event = createCustomEvent(thingURI, evt);
					addEventBinding(event, bindings);
					return event;
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.warn(e.getMessage());
			}
			return null;
		}).filter(evt -> evt != null).collect(Collectors.toMap(evt -> evt.getName(), Function.identity()));
	}

	private PropertyAffordance createCustomProperty(URI thingURI, PropertyAffordanceConfig p) throws Exception {
		var name = p.getProperty();
		var desc = p.getDescription();
		var time = PropertyData.time();
		var confproperties = p.getProperties();
		var properties = new HashMap<String, DataSchema>();
		for (PropertyConfig propertyConfig : confproperties) {
			DataSchema property = null;
			var dtype = propertyConfig.getDtype().toLowerCase();
			var pname = propertyConfig.getProperty();
			switch (dtype) {
			case "boolean":
				property = BooleanSchema.builder().title(pname).description(desc)
						.atType(Arrays.asList(URI.create("http://iotschema.org/StatusData"))).readOnly(true)
						.modified(time).build();
				properties.put(pname, property);
				break;
			case "integer":
				var min = Integer.parseInt(propertyConfig.getMin());
				var max = Integer.parseInt(propertyConfig.getMax());
				var unit = propertyConfig.getUnit();
				property = IntegerSchema.builder().title(pname).description(desc).readOnly(true).modified(time)
						.minimum(min).maximum(max).unit(unit).build();
				properties.put(pname, property);
				break;
			case "number":
				var dmin = Double.parseDouble(propertyConfig.getMin());
				var dmax = Double.parseDouble(propertyConfig.getMax());
				var dunit = propertyConfig.getUnit();
				property = NumberSchema.builder().title(pname).description(desc).readOnly(true).modified(time)
						.minimum(dmin).maximum(dmax).unit(dunit).build();
				properties.put(pname, property);
				break;
			default:
				throw new Exception(
						String.format("Dtype: %s -> Unknown or unsupported datatypye for property creation.", dtype));
			}
		}

		properties.put("time", time);
		var objectSchema = ObjectSchema.builder().properties(properties).required(Arrays.asList("time", name))
				.readOnly(true).build();
		var formHref = (p.getForm() != null && p.getForm().getHref() != null) ? p.getForm().getHref()
				: thingURI + "/properties/" + p.getProperty();
		var pa = PropertyAffordance.builder().ds(objectSchema)
				.forms(Arrays
						.asList(Form.builder().href(URI.create(formHref)).op(Arrays.asList(Op.readproperty)).build()))
				.build();
		pa.setTitle(name);
		pa.setName(name);
		pa.setDescription(desc);

		return pa;
	}

	private ActionAffordance createCustomAction(URI thingURI, ActionAffordanceConfig a) throws Exception {
		var name = a.getAction();
		var desc = a.getDescription();
		var time = PropertyData.time();
		var input = a.getInput();
		var output = a.getOutput();
		var iproperties = new HashMap<String, DataSchema>();
		var oproperties = new HashMap<String, DataSchema>();
		for (PropertyConfig propertyConfig : input) {
			DataSchema property = null;
			var dtype = propertyConfig.getDtype().toLowerCase();
			var pname = propertyConfig.getProperty();
			switch (dtype) {
			case "boolean":
				property = BooleanSchema.builder().title(pname).description(desc)
						.atType(Arrays.asList(URI.create("http://iotschema.org/StatusData"))).readOnly(true)
						.modified(time).build();
				iproperties.put(propertyConfig.getProperty(), property);
				break;
			case "integer":
				var min = Integer.parseInt(propertyConfig.getMin());
				var max = Integer.parseInt(propertyConfig.getMax());
				var unit = propertyConfig.getUnit();
				property = IntegerSchema.builder().title(pname).description(desc).readOnly(true).modified(time)
						.minimum(min).maximum(max).unit(unit).build();
				iproperties.put(propertyConfig.getProperty(), property);
				break;
			case "number":
				var dmin = Double.parseDouble(propertyConfig.getMin());
				var dmax = Double.parseDouble(propertyConfig.getMax());
				var dunit = propertyConfig.getUnit();
				property = NumberSchema.builder().title(pname).description(desc).readOnly(true).modified(time)
						.minimum(dmin).maximum(dmax).unit(dunit).build();
				iproperties.put(propertyConfig.getProperty(), property);
				break;
			default:
				throw new Exception(
						String.format("Dtype: %s -> Unknown or unsupported datatypye for property creation.", dtype));
			}
		}
		for (PropertyConfig propertyConfig : output) {
			DataSchema property = null;
			var dtype = propertyConfig.getDtype().toLowerCase();
			var pname = propertyConfig.getProperty();
			switch (dtype) {
			case "boolean":
				property = BooleanSchema.builder().title(pname).description(desc)
						.atType(Arrays.asList(URI.create("http://iotschema.org/StatusData"))).readOnly(true)
						.modified(time).build();
				oproperties.put(propertyConfig.getProperty(), property);
				break;
			case "integer":
				var min = Integer.parseInt(propertyConfig.getMin());
				var max = Integer.parseInt(propertyConfig.getMax());
				var unit = propertyConfig.getUnit();
				property = IntegerSchema.builder().title(pname).description(desc).readOnly(true).modified(time)
						.minimum(min).maximum(max).unit(unit).build();
				oproperties.put(propertyConfig.getProperty(), property);
				break;
			case "number":
				var dmin = Double.parseDouble(propertyConfig.getMin());
				var dmax = Double.parseDouble(propertyConfig.getMax());
				var dunit = propertyConfig.getUnit();
				property = NumberSchema.builder().title(pname).description(desc).readOnly(true).modified(time)
						.minimum(dmin).maximum(dmax).unit(dunit).build();
				oproperties.put(propertyConfig.getProperty(), property);
				break;
			default:
				throw new Exception(
						String.format("Dtype: %s -> Unknown or unsupported datatypye for property creation.", dtype));
			}
		}
		oproperties.put("time", time);
		var outputObjectSchema = ObjectSchema.builder().properties(oproperties)
				.required(Arrays.asList(oproperties.keySet().toArray(new String[0]))).readOnly(true).build();
		var inputObjectSchema = ObjectSchema.builder().properties(iproperties)
				.required(Arrays.asList(iproperties.keySet().toArray(new String[0]))).readOnly(false).writeOnly(true)
				.build();
		var formHref = (a.getForm() != null && a.getForm().getHref() != null) ? a.getForm().getHref()
				: thingURI + "/actions/" + a.getAction();
		var aa = ActionAffordance
				.builder().input(inputObjectSchema).output(outputObjectSchema).forms(Arrays.asList(Form.builder()
						.op(Arrays.asList(Op.readproperty, Op.writeproperty)).href(URI.create(formHref)).build()))
				.build();
		aa.setTitle(name);
		aa.setName(name);
		aa.setDescription(desc);

		return aa;
	}

	private EventAffordance createCustomEvent(URI thingURI, EventAffordanceConfig e) throws Exception {
		var name = e.getEvent();
		var desc = e.getDescription();
		var time = PropertyData.time();
		var confproperties = e.getProperties();
		var properties = new HashMap<String, DataSchema>();
		for (EventConfig eventConfig : confproperties) {
			DataSchema property = null;
			var dtype = eventConfig.getDtype().toLowerCase();
			var pname = eventConfig.getProperty();
			switch (dtype) {
			case "boolean":
				property = BooleanSchema.builder().title(pname).description(desc)
						.atType(Arrays.asList(URI.create("http://iotschema.org/StatusData"))).readOnly(true)
						.modified(time).build();
				properties.put(pname, property);
				break;
			case "integer":
				var min = Integer.parseInt(eventConfig.getMin());
				var max = Integer.parseInt(eventConfig.getMax());
				var unit = eventConfig.getUnit();
				property = IntegerSchema.builder().title(pname).description(desc).readOnly(true).modified(time)
						.minimum(min).maximum(max).unit(unit).build();
				properties.put(pname, property);
				break;
			case "number":
				var dmin = Double.parseDouble(eventConfig.getMin());
				var dmax = Double.parseDouble(eventConfig.getMax());
				var dunit = eventConfig.getUnit();
				property = NumberSchema.builder().title(pname).description(desc).readOnly(true).modified(time)
						.minimum(dmin).maximum(dmax).unit(dunit).build();
				properties.put(pname, property);
				break;
			default:
				throw new Exception(
						String.format("Dtype: %s -> Unknown or unsupported datatypye for property creation.", dtype));
			}
		}
		properties.put("time", time);
		var objectSchema = ObjectSchema.builder().properties(properties).required(Arrays.asList("time", name))
				.readOnly(true).build();
		var formHref = (e.getForm() != null && e.getForm().getHref() != null) ? e.getForm().getHref()
				: thingURI + "/evnts/" + e.getEvent();
		var ea = EventAffordance.builder().data(objectSchema)
				.forms(Arrays
						.asList(Form.builder().href(URI.create(formHref)).op(Arrays.asList(Op.readproperty)).build()))
				.build();
		ea.setTitle(name);
		ea.setName(name);
		ea.setDescription(desc);
		return ea;
	}

	private ArrayList<AdditionalProperty> createBindings(Bindable p) {
		var bindingname = p.getBinding().getType();
		var bindindata = p.getBinding().getData();
		var bindings = new ArrayList<AdditionalProperty>();
		var root = new JsonObject();
		bindindata.entrySet().stream().forEach(e -> root.addProperty(e.getKey(), e.getValue()));
		bindings.add(new AdditionalProperty("binding", new JsonPrimitive(bindingname)));
		bindings.add(new AdditionalProperty(bindingname, root));
		return bindings;
	}

	
	@Override
	public List<Thing> createFromConfig(ThingsConfig config, Function<ThingConfig, Thing> mapper) {
		var things = new ArrayList<Thing>(config.getThings().size());
		config.getThings().stream().map(mapper).forEach(t -> things.add(t));
		return things;
	}

	/**
	 * Adds a {@code List<PropertyAffordance>} to the provided thing. Since the
	 * thing description contains a {@code Map<String,PropertyAffordance} the
	 * property name is used as the map key.
	 * 
	 * @param thing      The thing to be modified
	 * @param properties A {@code List<PropertyAffordance>} of properties
	 * @return The modified thing
	 */
	
	@Override
	public Thing addProperties(Thing thing, List<PropertyAffordance> properties) {
		Objects.requireNonNull(thing, "Provided thing should not be null!");
		Objects.requireNonNull(properties, "Provided properties should not be null!");
		if (!thing.getId().toString().contains(this.baseHref.toString())) {
			throw new IllegalArgumentException(
					String.format("Mismatching base href in provided thing. Expected base href: %s", this.baseHref));
		}
		final Map<String, PropertyAffordance> propertyMap = new HashMap<>(properties.size());
		for (PropertyAffordance p : properties) {
			propertyMap.put(p.getName(), p);
		}

		return addProperties(thing, propertyMap);
	}

	/**
	 * Adds a {@code Map<String, PropertyAffordance>} to the provided thing.
	 * 
	 * @param thing      The thing to be modified
	 * @param properties A {@code Map<String, PropertyAffordance>} of properties
	 * @return The modified thing
	 */
	
	@Override
	public Thing addProperties(Thing thing, Map<String, PropertyAffordance> properties) {
		Objects.requireNonNull(thing, "Provided thing should not be null!");
		Objects.requireNonNull(properties, "Provided properties should not be null!");
		if (!thing.getId().toString().contains(this.baseHref.toString())) {
			throw new IllegalArgumentException(
					String.format("Mismatching base href in provided thing. Expected base href: %s", this.baseHref));
		}
		Map<String, PropertyAffordance> oldProperties = thing.getProperties();
		if (oldProperties == null) {
			oldProperties = new HashMap<>(properties.size());
		}
		oldProperties.putAll(properties);
		thing.setProperties(oldProperties);
		return thing;
	}

	/**
	 * Adds a {@code Map<String, PropertyProviderImpl>} to the provided thing. The
	 * {@link PropertyProviderImpl} implement default configurations of common
	 * properties and simplify the creation of {@link PropertyAffordance}
	 * 
	 * @param thing             The thing to be modified
	 * @param propertyProviders A {@code Map<String, PropertyProviderImpl>} of
	 *                          {@link PropertyProviderImpl}
	 * @return The modified thing
	 * @throws Exception
	 */
	
	@Override
	public Thing addPropertyTemplates(Thing thing, Map<String, PropertyProviderImpl> propertyProviders)
			throws Exception {
		Objects.requireNonNull(thing, "Provided thing should not be null!");
		Objects.requireNonNull(propertyProviders, "Property providers should not be null!");
		if (!thing.getId().toString().contains(this.baseHref.toString())) {
			throw new IllegalArgumentException(
					String.format("Mismatching base href in provided thing. Expected base href: %s", this.baseHref));
		}
		final Map<String, PropertyAffordance> propertyMap = new HashMap<>(propertyProviders.size());
		final String propertyBasePath = thing.getId().toString();
		for (Entry<String, PropertyProviderImpl> p : propertyProviders.entrySet()) {
			propertyMap.put(p.getKey(), p.getValue().create(propertyBasePath, p.getKey()));
		}

		return addProperties(thing, propertyMap);
	}

	/**
	 * Adds a {@code List<ActionAffordance> } to the provided thing. Since the thing
	 * description contains a {@code Map<String,ActionAffordance} the action name is
	 * used as the map key.
	 * 
	 * @param thing   The thing to be modified
	 * @param actions A {@code List<ActionAffordance>} of actions
	 * @return The modified thing
	 */
	
	@Override
	public Thing addActions(Thing thing, List<ActionAffordance> actions) {
		Objects.requireNonNull(thing, "Provided thing should not be null!");
		Objects.requireNonNull(actions, "Provided actions should not be null!");
		if (!thing.getId().toString().contains(this.baseHref.toString())) {
			throw new IllegalArgumentException(
					String.format("Mismatching base href in provided thing. Expected base href: %s", this.baseHref));
		}
		final Map<String, ActionAffordance> actionMap = new HashMap<>(actions.size());
		for (ActionAffordance a : actions) {
			actionMap.put(a.getName(), a);
		}
		return addActions(thing, actionMap);
	}

	/**
	 * Adds a {@code Map<String, ActionAffordance>} to the provided thing.
	 * 
	 * @param thing   The thing to be modified
	 * @param actions A {@code Map<String, ActionAffordance>} of actions
	 * @return The modified thing
	 */
	
	@Override
	public Thing addActions(Thing thing, Map<String, ActionAffordance> actions) {
		Objects.requireNonNull(thing, "Provided thing should not be null!");
		Objects.requireNonNull(actions, "Provided actions should not be null!");
		if (!thing.getId().toString().contains(this.baseHref.toString())) {
			throw new IllegalArgumentException(
					String.format("Mismatching base href in provided thing. Expected base href: %s", this.baseHref));
		}
		Map<String, ActionAffordance> oldActions = thing.getActions();
		if (oldActions == null) {
			oldActions = new HashMap<>(actions.size());
		}
		oldActions.putAll(actions);
		thing.setActions(oldActions);
		return thing;
	}

	/**
	 * Adds a {@code Map<String, ActionProviderImpl>} to the provided thing. The
	 * {@link ActionProviderImpl} implements default configurations of common
	 * actions and simplify the creation of {@link ActionAffordance}
	 * 
	 * @param thing           The thing to be modified
	 * @param actionProviders A {@code Map<String, ActionProviderImpl>} of
	 *                        {@link ActionProviderImpl}
	 * @return The modified thing
	 * @throws Exception
	 */
	
	@Override
	public Thing addActionTemplates(Thing thing, Map<String, ActionProviderImpl> actionProviders) throws Exception {
		Objects.requireNonNull(thing, "Provided thing should not be null!");
		Objects.requireNonNull(actionProviders, "Action providers should not be null!");
		if (!thing.getId().toString().contains(this.baseHref.toString())) {
			throw new IllegalArgumentException(
					String.format("Mismatching base href in provided thing. Expected base href: %s", this.baseHref));
		}
		final Map<String, ActionAffordance> actionMap = new HashMap<>(actionProviders.size());
		final String actionBasePath = thing.getId().toString();
		for (Entry<String, ActionProviderImpl> a : actionProviders.entrySet()) {
			actionMap.put(a.getKey(), a.getValue().create(actionBasePath, a.getKey()));
		}

		return addActions(thing, actionMap);
	}

	/**
	 * Adds a {@code List<EventAffordance>} to the provided thing. Since the thing
	 * description contains a {@code Map<String,EventAffordance} the event name is
	 * used as the map key.
	 * 
	 * @param thing  The thing to be modified
	 * @param events A {@code List<EventAffordance>} of events
	 * @return The modified thing
	 */
	
	@Override
	public Thing addEvents(Thing thing, List<EventAffordance> events) {
		Objects.requireNonNull(thing, "Provided thing should not be null!");
		Objects.requireNonNull(events, "Provided events should not be null!");
		if (!thing.getId().toString().contains(this.baseHref.toString())) {
			throw new IllegalArgumentException(
					String.format("Mismatching base href in provided thing. Expected base href: %s", this.baseHref));
		}
		final Map<String, EventAffordance> propertyMap = new HashMap<>(events.size());
		for (EventAffordance e : events) {
			propertyMap.put(e.getName(), e);
		}

		return addEvents(thing, propertyMap);
	}

	/**
	 * Adds a {@code Map<String, EventAffordance>} to the provided thing.
	 * 
	 * @param thing  The thing to be modified
	 * @param events A {@code Map<String, EventAffordance>} of events
	 * @return The modified thing
	 */
	
	@Override
	public Thing addEvents(Thing thing, Map<String, EventAffordance> events) {
		Objects.requireNonNull(thing, "Provided thing should not be null!");
		Objects.requireNonNull(events, "Provided events should not be null!");
		if (!thing.getId().toString().contains(this.baseHref.toString())) {
			throw new IllegalArgumentException(
					String.format("Mismatching base href in provided thing. Expected base href: %s", this.baseHref));
		}
		Map<String, EventAffordance> oldEvents = thing.getEvents();
		if (oldEvents == null) {
			oldEvents = new HashMap<>(events.size());
		}
		oldEvents.putAll(events);
		thing.setEvents(oldEvents);
		return thing;
	}

	/**
	 * Adds a new binding to a specific property identified by the property key. The
	 * binding is currently always added to the first form element of the specified
	 * property.
	 * 
	 * @param thing       The thing to be modified
	 * @param propertyKey The property key of the property to be modified with the
	 *                    new binding
	 * @param bindings    The binding that should be added
	 * @return The modified thing
	 * @throws Exception
	 */
	
	@Override
	public Thing addPropertyBinding(Thing thing, String propertyKey, List<AdditionalProperty> bindings)
			throws Exception {
		Objects.requireNonNull(thing, "Provided thing should not be null!");
		Objects.requireNonNull(bindings, "Provided bindings should not be null!");
		if (!thing.getId().toString().contains(this.baseHref.toString())) {
			throw new IllegalArgumentException(
					String.format("Mismatching base href in provided thing. Expected base href: %s", this.baseHref));
		}
		var properties = thing.getProperties();
		if (!properties.containsKey(propertyKey)) {
			throw new IllegalArgumentException(
					String.format("Things '%s' has no property '%s'", thing.getName(), propertyKey));
		}
		var prop = properties.get(propertyKey);
		if (prop.getForms() != null && prop.getForms().size() > 0) {
			prop.getForms().get(0).setAdditionalProperties(bindings);
		}
		return thing;
	}

	/**
	 * Adds a new binding to single property affordance.
	 * 
	 * @param property The property to be modified
	 * @param bindings The new binding for the property
	 * @return The modified property with the new binding
	 * @throws Exception
	 */
	
	@Override
	public PropertyAffordance addPropertyBinding(PropertyAffordance property, List<AdditionalProperty> bindings)
			throws Exception {
		Objects.requireNonNull(property, "Provided property should not be null!");
		if (property.getForms() != null && property.getForms().size() > 0) {
			property.getForms().get(0).setAdditionalProperties(bindings);
		}
		return property;
	}

	/**
	 * Adds a new binding to single action affordance.
	 * 
	 * @param action   The action to be modified
	 * @param bindings The new binding for the action
	 * @return The modified action with the new binding
	 * @throws Exception
	 */
	
	@Override
	public ActionAffordance addActionBinding(ActionAffordance action, List<AdditionalProperty> bindings) {
		Objects.requireNonNull(action, "Provided action should not be null!");
		if (action.getForms() != null && action.getForms().size() > 0) {
			action.getForms().get(0).setAdditionalProperties(bindings);
		}
		return action;
	}

	/**
	 * Adds a new binding to a specific action identified by the action key. The
	 * binding is currently always added to the first form element of the specified
	 * action.
	 * 
	 * @param thing     The thing to be modified
	 * @param actionKey The action key of the action to be modified with the new
	 *                  binding
	 * @param bindings  The binding that should be added
	 * @return The modified thing
	 * @throws Exception
	 */
	
	@Override
	public Thing addActionBinding(Thing thing, String actionKey, List<AdditionalProperty> bindings) throws Exception {
		Objects.requireNonNull(thing, "Provided thing should not be null!");
		Objects.requireNonNull(bindings, "Provided bindings should not be null!");
		if (!thing.getId().toString().contains(this.baseHref.toString())) {
			throw new IllegalArgumentException(
					String.format("Mismatching base href in provided thing. Expected base href: %s", this.baseHref));
		}
		var actions = thing.getActions();
		if (!actions.containsKey(actionKey)) {
			throw new IllegalArgumentException(
					String.format("Things '%s' has no action '%s'", thing.getName(), actionKey));
		}
		var action = actions.get(actionKey);
		if (action.getForms() != null && action.getForms().size() > 0) {
			action.getForms().get(0).setAdditionalProperties(bindings);
		}
		return thing;
	}

	/**
	 * Adds a new binding to single event affordance.
	 * 
	 * @param event    The event to be modified
	 * @param bindings The new binding for the event
	 * @return The modified event with the new binding
	 * @throws Exception
	 */
	
	@Override
	public EventAffordance addEventBinding(EventAffordance event, List<AdditionalProperty> bindings) {
		Objects.requireNonNull(event, "Provided event should not be null!");
		if (event.getForms() != null && event.getForms().size() > 0) {
			event.getForms().get(0).setAdditionalProperties(bindings);
		}
		return event;
	}

	/**
	 * Adds a single new context element to the default or previously created
	 * context array.
	 * 
	 * @param thing   The modified thing
	 * @param context The context to add
	 * @return The modified thing
	 */
	
	@Override
	public Thing addContext(Thing thing, Context context) {
		var currentContext = new ArrayList<Context>(Arrays.asList(thing.getContexts()));
		currentContext.add(context);
		thing.setContexts(currentContext.toArray(new Context[0]));
		return thing;
	}

	/**
	 * Deletes/removes a single context element from the current context identified
	 * by the prefix.
	 * 
	 * @param thing   The modified thing
	 * @param context The context to delete
	 * @return The modified thing
	 */
	
	@Override
	public Thing deleteContext(Thing thing, Context context) {
		var currentContext = new ArrayList<Context>(Arrays.asList(thing.getContexts()));
		var remaining = currentContext.stream().filter(c -> !c.getPrefix().equals(context.getPrefix()))
				.collect(Collectors.toList());
		thing.setContexts((Context[]) remaining.toArray());
		return thing;
	}

	/**
	 * Helper method to set the minimum and maximum values of a specific property.
	 * 
	 * @param thing          The modified thing
	 * @param interactionKey The property key
	 * @param min            New minimum value
	 * @param max            New maximum value
	 * @return The modified thing with the new minimum and maximum values of the
	 *         specified property
	 */
	
	@Override
	public Thing setPropertyMinMax(Thing thing, String interactionKey, Integer min, Integer max) {
		if (thing.getProperties() != null && thing.getProperties().containsKey(interactionKey)) {
			var interaction = thing.getProperties().get(interactionKey);
			var oprop = interaction.getProperties().stream().filter(p -> p.getName().equals(interactionKey))
					.findFirst();
			if (oprop.isEmpty()) {
				LOGGER.warn(String.format("Unknown property '%s' thing not modified!", interactionKey));
				return thing;
			}
			var prop = oprop.get();
			var datatype = prop.getType();
			if (!datatype.toLowerCase().equals("integer")) {
				LOGGER.warn(String.format("Property '%s' has unexpected datatype '%s'!", interactionKey, datatype));
				return thing;
			}
			var otime = interaction.getProperties().stream().filter(p -> p.getName().equals("time")).findFirst();
			if (otime.isEmpty()) {
				LOGGER.warn("Invalid property structure. Missing time element!");
				return thing;
			}
			var time = otime.get();
			var otherprops = interaction.getProperties().stream().filter(p -> !p.getName().equals(interactionKey))
					.collect(Collectors.toList());

			var modprop = IntegerSchema.builder().atType(prop.getAtType()).constant(prop.getConstant())
					.description(prop.getDescription()).descriptions(prop.getDescriptions()).modified(time)
					.enumeration(prop.getEnumeration()).format(prop.getFormat()).id(prop.getId()).minimum(min)
					.maximum(max).oneOf(prop.getOneOf()).readOnly(prop.getReadOnly()).title(prop.getTitle())
					.titles(prop.getTitles()).unit(prop.getUnit()).writeOnly(prop.getWriteOnly()).build();
			var ds = (DataSchema) modprop;
			ds.setName(interactionKey);
			otherprops.add(ds);
			interaction.setProperties(otherprops);
			thing.getProperties().put(interactionKey, interaction);
		}
		return thing;
	}

	/**
	 * Helper method to set the minimum and maximum values of a specific property.
	 * 
	 * @param thing          The modified thing
	 * @param interactionKey The property key
	 * @param min            New minimum value
	 * @param max            New maximum value
	 * @return The modified thing with the new minimum and maximum values of the
	 *         specified property
	 */
	
	@Override
	public Thing setPropertyMinMax(Thing thing, String interactionKey, Double min, Double max) {
		if (thing.getProperties() != null && thing.getProperties().containsKey(interactionKey)) {
			var interaction = thing.getProperties().get(interactionKey);
			var oprop = interaction.getProperties().stream().filter(p -> p.getName().equals(interactionKey))
					.findFirst();
			if (oprop.isEmpty()) {
				LOGGER.warn(String.format("Unknown property '%s' thing not modified!", interactionKey));
				return thing;
			}
			var prop = oprop.get();
			var datatype = prop.getType();
			if (!datatype.toLowerCase().equals("number")) {
				LOGGER.warn(String.format("Property '%s' has unexpected datatype '%s'!", interactionKey, datatype));
				return thing;
			}
			var otime = interaction.getProperties().stream().filter(p -> p.getName().equals("time")).findFirst();
			if (otime.isEmpty()) {
				LOGGER.warn("Invalid property structure. Missing time element!");
				return thing;
			}
			var time = otime.get();
			var otherprops = interaction.getProperties().stream().filter(p -> !p.getName().equals(interactionKey))
					.collect(Collectors.toList());

			var modprop = NumberSchema.builder().atType(prop.getAtType()).constant(prop.getConstant())
					.description(prop.getDescription()).descriptions(prop.getDescriptions()).modified(time)
					.enumeration(prop.getEnumeration()).format(prop.getFormat()).id(prop.getId()).minimum(min)
					.maximum(max).oneOf(prop.getOneOf()).readOnly(prop.getReadOnly()).title(prop.getTitle())
					.titles(prop.getTitles()).unit(prop.getUnit()).writeOnly(prop.getWriteOnly()).build();
			var ds = (DataSchema) modprop;
			ds.setName(interactionKey);
			otherprops.add(ds);
			interaction.setProperties(otherprops);
			thing.getProperties().put(interactionKey, interaction);
		}
		return thing;
	}

	/**
	 * Helper method to set the unit of a specific property, action or event
	 * identified by the interactionKey In case of a action the unit is set for the
	 * output schema only, because the input schema is not expected to have a
	 * specific unit.
	 * 
	 * @param thing          The modified thing
	 * @param interactionKey The interaction key
	 * @param unit           New unit as a string
	 * @return The modified thing with the new minimum and maximum values of the
	 *         specified property
	 */
	
	@Override
	public Thing setInteractionUnit(Thing thing, String interactionKey, String unit) {
		if (thing.getProperties() != null && thing.getProperties().containsKey(interactionKey)) {
			var interaction = thing.getProperties().get(interactionKey);
			var oprop = interaction.getProperties().stream().filter(p -> p.getName().equals(interactionKey))
					.findFirst();
			if (oprop.isEmpty()) {
				LOGGER.warn(String.format("Unknown property '%s' thing not modified!", interactionKey));
				return thing;
			}
			var prop = oprop.get();
			prop.setUnit(unit);
		} else if (thing.getActions() != null && thing.getActions().containsKey(interactionKey)) {
			var interaction = thing.getActions().get(interactionKey);
			interaction.getOutput().setUnit(unit);
		} else if (thing.getEvents() != null && thing.getEvents().containsKey(interactionKey)) {
			var interaction = thing.getEvents().get(interactionKey);
			interaction.getData().setUnit(unit);
		} else {
			LOGGER.warn(
					String.format("No interaction with key 's' found. Returning unmodified thing!", interactionKey));
		}
		return thing;
	}

	/**
	 * Set a custom context by overwriting the current context of the provided
	 * thing.
	 * 
	 * @param thing          The thing to be modified
	 * @param customContexts The new custom context
	 * @return The modified thing
	 */
	
	@Override
	public Thing setCustomContexts(Thing thing, Context[] customContexts) {
		thing.setContexts(customContexts);
		return thing;
	}

	/**
	 * Set a new base HREF for the creator context
	 * 
	 * @param baseHref The new base HREF
	 */
	
	@Override
	public void setBaseHref(URI baseHref) {
		this.baseHref = baseHref;
	}

	
	@Override
	public URI getBaseHref() {
		return baseHref;
	}
}
