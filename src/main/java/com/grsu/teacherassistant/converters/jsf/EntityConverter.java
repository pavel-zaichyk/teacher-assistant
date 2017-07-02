package com.grsu.teacherassistant.converters.jsf;

import com.grsu.teacherassistant.entities.AssistantEntity;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;


/**
 * http://stackoverflow.com/a/23213333
 */
@FacesConverter(value = "entityConverter", forClass = AssistantEntity.class)
public class EntityConverter implements Converter {

	private static Map<AssistantEntity, Integer> entities = new WeakHashMap<>();

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object entity) {
		synchronized (entities) {
			if (entity == null) {
				return null;
			}
			if (!entities.containsKey(entity)) {
				AssistantEntity e = (AssistantEntity) entity;
				entities.put(e, e.hashCode());
				return String.valueOf(e.hashCode());
			} else {
				return entities.get(entity).toString();
			}
		}
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String id) {
		for (Entry<AssistantEntity, Integer> entry : entities.entrySet()) {
			if (entry.getValue().toString().equals(id)) {
				return entry.getKey();
			}
		}
		return null;
	}

}
