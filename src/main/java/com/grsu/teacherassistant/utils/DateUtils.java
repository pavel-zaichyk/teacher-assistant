package com.grsu.teacherassistant.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
	private static final String FORMAT_DATE = "dd.MM.yyyy";
	public static final String FORMAT_DATE_SHORT_YEAR = "dd.MM.yy";
	private static final String FORMAT_TIME = "HH:mm";
	private static final String FORMAT_TIME_SECOND = "HH:mm:ss";
	private static final String FORMAT_DATE_TIME = "dd.MM.yyyy HH:mm";

	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(FORMAT_DATE);
	public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(FORMAT_TIME);
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(FORMAT_DATE_TIME);

	private static String formatDate(LocalDateTime date, DateTimeFormatter formatter) {
		return date.format(formatter);
	}

	public static String formatDate(LocalDateTime date, String format) {
		return date.format(DateTimeFormatter.ofPattern(format));
	}

	public static String formatDate(LocalDateTime date) {
		return formatDate(date, DATE_FORMATTER);
	}

	public static String formatDateTime(LocalDateTime dateTime) {
		return formatDate(dateTime, DATE_TIME_FORMATTER);
	}

	public static String formatTime(LocalDateTime date) {
		return formatDate(date, TIME_FORMATTER);
	}

	public static String formatTime(LocalTime time) {
		if (time == null) {
			return null;
		}
		return time.format(DateTimeFormatter.ofPattern(FORMAT_TIME_SECOND));
	}

	public static LocalDateTime parseDate(String date, DateTimeFormatter formatter) {
		return LocalDate.parse(date, formatter).atStartOfDay();
	}

	public static LocalDateTime parseDate(String date) {
		return parseDate(date, DATE_FORMATTER);
	}

	public static LocalDateTime parseTime(String date) {
		return parseDate(date, TIME_FORMATTER);
	}
}
