package com.grsu.teacherassistant.converters.jsf;

/**
 * Created by zaychick-pavel on 3/26/17.
 */

import com.grsu.teacherassistant.models.LessonType;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "lessonTypeConverter")
public class LessonTypeConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
		if (s == null) {
			return null;
		}
		return LessonType.getLessonTypeByCode(Integer.valueOf(s));
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
		if (o == null || !(o instanceof LessonType)) {
			return null;
		}
		return Integer.toString(((LessonType) o).getCode());
	}
}
