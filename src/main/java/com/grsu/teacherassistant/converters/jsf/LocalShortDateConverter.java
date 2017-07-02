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
import java.time.format.DateTimeFormatter;

@FacesConverter(value = "localShortDateConverter")
public class LocalShortDateConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
		if (s == null) {
			return null;
		}

		return DateUtils.parseDate(s, DateTimeFormatter.ofPattern(DateUtils.FORMAT_DATE_SHORT_YEAR));
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
		if (o == null || (o instanceof String && "".equals(o))) {
			return null;
		}
		return DateUtils.formatDate((LocalDateTime) o, DateUtils.FORMAT_DATE_SHORT_YEAR);
	}
}
