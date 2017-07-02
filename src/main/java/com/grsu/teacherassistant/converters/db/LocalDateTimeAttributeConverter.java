package com.grsu.teacherassistant.converters.db;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by pavel on 2/9/17.
 */
@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, String> {

	@Override
	public String convertToDatabaseColumn(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return null;
		}
		return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}

	@Override
	public LocalDateTime convertToEntityAttribute(String s) {
		if (s == null) {
			return null;
		}
		if (!s.contains("T")) {
			return LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
		} else {
			return LocalDateTime.parse(s);
		}
	}
}
