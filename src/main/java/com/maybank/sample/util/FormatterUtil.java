package com.maybank.sample.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class FormatterUtil {

	private static final XLogger log = XLoggerFactory.getXLogger(FormatterUtil.class.getName());

	public static final String DATETIMEMILLIS = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final String ISO_UTC_DATETIME = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	private static final ZoneId TIMEZONE = TimeZoneUtil.getSystemTimezone();

	public static String getDefaultDateFormat() {
		if (TIMEZONE.toString().equals("UTC")) {
			return FormatterUtil.ISO_UTC_DATETIME;
		} else {
			return FormatterUtil.DATETIMEMILLIS;
		}
	}

	public static String getStringFromInstant(final Instant date, final String outputPattern) {
		try {
			if (TimeZoneUtil.getSystemTimezone() != ZoneId.of("UTC")) {
				log.trace("Your time zone is {}, not UTC, Do not pass in system generated time here", TimeZoneUtil.getSystemTimezone());
			}
			// TODO remove hard code when all project is on board, hardcoded to UTC
			ZonedDateTime zdt = ZonedDateTime.ofInstant(date, ZoneOffset.UTC);

			return DateTimeFormatter.ofPattern(outputPattern).format(zdt);

		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static String getFormattedDateTimeMillis(final Instant date) {
		return getStringFromInstant(date, DATETIMEMILLIS);
	}

	public static Instant getInstantFromString(final String date, final String inputPattern) {
		try {
			return ZonedDateTime.parse(date).toInstant();
		} catch (DateTimeParseException e) {
			// TODO flag to fall back to our old implementation, remove hard code when all project is on board, hardcoded to UTC
			log.warn("Input {} is not in UTC format, using alternate method, result might be inaccurate", date);
			return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(inputPattern)).toInstant(ZoneOffset.UTC);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
}
