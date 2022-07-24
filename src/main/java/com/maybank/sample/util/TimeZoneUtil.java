package com.maybank.sample.util;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Properties;
import java.util.TimeZone;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

public final class TimeZoneUtil {

	private static final XLogger log = XLoggerFactory.getXLogger(TimeZoneUtil.class);
	private static ZoneId timezoneId;

	static {
		try {
			Properties prop = new Properties();

			ClassPathResource classPathResource = new ClassPathResource("application.properties");
			InputStream in = classPathResource.getInputStream();
			prop.load(in);

			String timezone = prop.getProperty("config.timezone");

			String envConfig = System.getProperty("config.timezone");
			if (StringUtils.hasText(envConfig)) {
				// override with environment variable
				timezone = envConfig;
			}

			if (StringUtils.hasText(timezone)) {
				if (!"UTC".equals(timezone)) {
					log.warn("Using [{}] as default timezone, might cause time inconsistency, UTC is recommended", timezone);
				}
				TimeZone.setDefault(TimeZone.getTimeZone(timezone));
				timezoneId = ZoneId.of(timezone);
				log.trace("[{}] is set as system timezone", timezone);
			} else {
				log.warn("Using system timezone, [{}], might cause time inconsistency", ZoneOffset.systemDefault());
				timezoneId = ZoneOffset.systemDefault();
			}
		} catch (IOException e) {
			log.warn("Error occurred when setting timezone, fallback to system timezone, might cause time inconsistency.\n Error {}",
					e.getMessage());
			timezoneId = ZoneOffset.systemDefault();
		}
	}

	private TimeZoneUtil() {
	}

	public static ZoneId getSystemTimezone() {
		return timezoneId;
	}

	public static ZoneId getMalaysiaTimezone() {
		return ZoneId.of("Asia/Kuala_Lumpur");
	}

	public static ZoneId getChinaTimezone() {
		return ZoneId.of("Asia/Shanghai");
	}

	public static ZoneId getTaiwanTimezone() {
		return ZoneId.of("Asia/Taipei");
	}

	public static ZoneId getThailandTimezone() {
		return ZoneId.of("Asia/Bangkok");
	}

	public static ZoneId getJapanTimezone() {
		return ZoneId.of("Asia/Tokyo");
	}

	public static ZoneId getSydneyTimezone() {
		return ZoneId.of("Australia/Sydney");
	}

	public static ZoneId getLosAngelesTimezone() {
		return ZoneId.of("America/Los_Angeles");
	}

	public static long getTimeDifferenceWithUTC(ZoneId zoneId) {
		LocalDateTime dt = LocalDateTime.now();
		ZonedDateTime fromZonedDateTime = dt.atZone(zoneId);
		ZonedDateTime toZonedDateTime = dt.atZone(ZoneId.of("UTC"));
		return Duration.between(fromZonedDateTime, toZonedDateTime).toHours();
	}

	public static long getSystemTimeDifferenceWithUTC() {
		LocalDateTime dt = LocalDateTime.now();
		ZonedDateTime fromZonedDateTime = dt.atZone(TimeZoneUtil.getSystemTimezone());
		ZonedDateTime toZonedDateTime = dt.atZone(ZoneId.of("UTC"));
		return Duration.between(fromZonedDateTime, toZonedDateTime).toHours();
	}

}
