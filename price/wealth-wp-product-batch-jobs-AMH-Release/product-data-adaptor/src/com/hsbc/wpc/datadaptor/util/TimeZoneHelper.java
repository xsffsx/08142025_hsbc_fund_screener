package com.dummy.wpc.datadaptor.util;

import java.io.File;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class TimeZoneHelper {
	private static Logger log = Logger.getLogger(TimeZoneHelper.class);
	private static Properties prop;
	private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getDefault();

	static{
		if (log.isDebugEnabled()) {
			log.debug("[" + DateHelper.getCurrentDateDefaultStr() + "]Initial TimeZone config begin.");
		}
		
		String timeZoneConfig = System.getProperty(Constants.CONFIG_PATH) + File.separator + Constants.TIMEZONE_PROPERTIES;
		if (log.isDebugEnabled()) {
			log.debug("TimeZone config file path is: " + timeZoneConfig);
		}
		
		PropertiesHelper proHelper = new PropertiesHelper();
		if (StringUtils.isNotEmpty(timeZoneConfig)) {
			try {
				prop = proHelper.loadProperties(timeZoneConfig);
			} catch (RuntimeException e) {
				log.error("Initial TimeZone encounter error.", e);
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("[" + DateHelper.getCurrentDateDefaultStr() + "]Initial TimeZone config end.");
		}		
	}
	
	public static TimeZone getTimeZone(String ctryCde, String orgnCde) {
		String timeZoneStr = "";
		if (null != prop) {
			// Get TimeZone by country code and organization code
			// e.g. TW.dummy.TIMEZONE=Europe/London
			timeZoneStr = prop.getProperty(ctryCde + Constants.DOT + orgnCde + Constants.DOT + Constants.TIMEZONE);
			if (null == timeZoneStr) {
				if (log.isDebugEnabled()) {
					log.debug("No TimeZone config entry for [Country Code: "+ctryCde+" and Organization Code: "+orgnCde+"], so use default TimeZone config entry.");
				}
				// If no match for the specified country code and organization code then use default TimeZone
				// e.g. DEFAULT.TIMEZONE=Europe/London
				timeZoneStr = prop.getProperty(Constants.DEFAULT + Constants.DOT + Constants.TIMEZONE);
				if (null == timeZoneStr) {
					if (log.isDebugEnabled()) {
						log.debug("No default TimeZone config entry, so use TimeZone based on the time zone where the program is running.");
					}
					return DEFAULT_TIMEZONE;
				}
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Use TimeZone based on the time zone where the program is running.");
			}
			return DEFAULT_TIMEZONE;
		}
		
		return TimeZone.getTimeZone(timeZoneStr);
	}
}
