package com.grsu.teacherassistant.converters.jsf;

/**
 * Created by zaychick-pavel on 2/7/17.
 */

import com.grsu.teacherassistant.utils.DateUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.time.LocalDateTime;
import java.time.LocalTime;

@FacesConverter(value = "localTimeConverter")
public class LocalTimeConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
		if (s == null) {
			return null;
		}
		return LocalDateTime.parse(s);
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
		if (o == null) {
			return null;
		}
		return DateUtils.formatTime((LocalTime) o);
	}
}
