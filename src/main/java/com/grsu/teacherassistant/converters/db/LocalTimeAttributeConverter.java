package com.grsu.teacherassistant.converters.db;

import com.grsu.teacherassistant.utils.DateUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * @author Pavel Zaychick
 */
@Converter(autoApply = true)
public class LocalTimeAttributeConverter implements AttributeConverter<LocalTime, String> {

	@Override
	public String convertToDatabaseColumn(LocalTime localTime) {
		if (localTime == null) {
			return null;
		}
		return localTime.format(DateTimeFormatter.ISO_TIME);
	}

	@Override
	public LocalTime convertToEntityAttribute(String s) {
		if (s == null) {
			return null;
		}
		try {
			return LocalTime.parse(s);
		} catch (DateTimeParseException ex) {
		}
		return LocalTime.parse(s, DateUtils.TIME_FORMATTER);
	}
}
