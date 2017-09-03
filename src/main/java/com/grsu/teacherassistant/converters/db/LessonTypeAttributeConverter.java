package com.grsu.teacherassistant.converters.db;

import com.grsu.teacherassistant.models.LessonType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author Pavel Zaychick
 */
@Converter(autoApply = true)
public class LessonTypeAttributeConverter implements AttributeConverter<LessonType, Integer> {

	@Override
	public Integer convertToDatabaseColumn(LessonType lessonType) {
		return lessonType.getCode();
	}

	@Override
	public LessonType convertToEntityAttribute(Integer code) {
		return LessonType.getLessonTypeByCode(code);
	}
}
