package com.grsu.teacherassistant.converters.jsf;

import com.grsu.teacherassistant.models.LessonType;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * @author Pavel Zaychick
 */
@FacesConverter(value = "lessonTypeConverter")
public class LessonTypeConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s == null) {
            return null;
        }
        try {
            return LessonType.getLessonTypeByCode(Integer.valueOf(s));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o == null || !(o instanceof LessonType)) {
            return null;
        }
        return Integer.toString(((LessonType) o).getCode());
    }
}
