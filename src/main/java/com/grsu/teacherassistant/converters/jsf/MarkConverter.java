package com.grsu.teacherassistant.converters.jsf;

import com.grsu.teacherassistant.models.Mark;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * @author Pavel Zaychick
 */
@FacesConverter(value = "markConverter")
public class MarkConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s == null) {
            return null;
        }
        return Mark.getByFieldValue(s);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o == null || !(o instanceof Mark)) {
            return null;
        }
        return ((Mark) o).getFieldValue()[0];
    }
}
