package com.paypal.bfs.customAnnotator;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.jsonschema2pojo.AbstractAnnotator;
import org.jsonschema2pojo.GenerationConfig;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;

/**
 * A custom annotator that enhances the functionalities provided by
 * jsonschema2pojo maven plugin. The jsonschema2pojo plugin has limited
 * capabilities. When it comes to adding jpa capabilities, it is impossible
 * without a custom implementation such as this which can help solve a lot of
 * problems.
 * 
 * @author Mithun
 *
 */
public class CustomAnnotator extends AbstractAnnotator {

	private Map<String, JsonNode> schemas = new HashMap<>();

	public CustomAnnotator(GenerationConfig generationConfig) {
		super(generationConfig);
	}

	@Override
	public void propertyInclusion(JDefinedClass clazz, JsonNode schema) {
		super.propertyInclusion(clazz, schema);

		if (schema.has("entity") && schema.get("entity").asBoolean()) {
			clazz.annotate(Entity.class);
		}
		schemas.put(clazz.name(), schema);
	}

	@Override
	public void propertyField(JFieldVar field, JDefinedClass clazz, String propertyName, JsonNode propertyNode) {
		super.propertyField(field, clazz, propertyName, propertyNode);

		if (propertyNode.has("jsonIgnore") && propertyNode.get("jsonIgnore").asBoolean()) {
			field.annotate(JsonIgnore.class);
		}

		if (propertyNode.has("id") && propertyNode.get("id").asBoolean()) {
			field.annotate(Id.class);		// Mark the respective field with the Id annotation, adding JPA capabilities
			if (propertyNode.has("generatedValue") && propertyNode.get("generatedValue").asBoolean()) {
				field.annotate(GeneratedValue.class);
			}
		}
		checkAndApplyEntityMappings(field, clazz, propertyName, propertyNode,schemas);
	}
	
	

	@Override
	public void propertyGetter(JMethod getter, JDefinedClass clazz, String propertyName) {
		super.propertyGetter(getter, clazz, propertyName);
		
		String className = clazz.name();
		JsonNode schema = schemas.get(className);
		if (schema != null) {
			if (schema.has("properties")) {
				JsonNode properties = schema.get("properties");
				if (properties.has(propertyName)) {
					JsonNode property = properties.get(propertyName);
					if (property.has("jsonIgnore") && property.get("jsonIgnore").asBoolean()) {
						getter.annotate(JsonIgnore.class);
					}
				}
			}
		}
	}

	private void checkAndApplyEntityMappings(JFieldVar field, JDefinedClass clazz, String propertyName,
			JsonNode propertyNode, Map<String, JsonNode> schemas) {
		String className = clazz.name();
		JsonNode schema = schemas.get(className);
		if (schema != null) {
			
			/**
			 * Adding a property like this to the main schema, would the corresponding relation annotation
			 * to the field and also include the cascade type property with the annotation.
			 * "entityToEntityMapping" :{
			 * 		"address": {
			 * 			"relation": "OneToOne",
			 * 			"cascadeType": "all"
			 * 		}
			 * }
			*/
			if (schema.has("entityToEntityMapping")) {
				JsonNode entityMappings = schema.get("entityToEntityMapping");
				if (entityMappings.has(propertyName)) {
					JsonNode entityMapping = entityMappings.get(propertyName);
					if (entityMapping.has("relation")) {
						String relationValue = entityMapping.get("relation").asText().trim();
						CascadeType cascadeType = null;
						if (entityMapping.has("cascadeType")) {
							String cType = entityMapping.get("cascadeType").asText().toUpperCase().trim();
							try {
								cascadeType = CascadeType.valueOf(cType);
							} catch (IllegalArgumentException e) {
								// Do nothing
							}
						}
						JAnnotationUse annotation = null;
						if (relationValue.compareToIgnoreCase(OneToOne.class.getSimpleName()) == 0) {
							annotation = field.annotate(OneToOne.class);
						} else if (relationValue.compareToIgnoreCase(OneToMany.class.getSimpleName()) == 0) {
							annotation = field.annotate(OneToMany.class);
						} else if (relationValue.compareToIgnoreCase(ManyToOne.class.getSimpleName()) == 0) {
							annotation = field.annotate(ManyToOne.class);
						} else if (relationValue.compareToIgnoreCase(ManyToMany.class.getSimpleName()) == 0) {
							annotation = field.annotate(ManyToMany.class);
						}
						if (cascadeType != null) {
							annotation.param("cascade", cascadeType);
						}
					}
				}
			}
		}
	}

}
