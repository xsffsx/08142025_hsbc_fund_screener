package com.dummy.wpc.datadaptor.util;

import java.io.File;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CurrencyHelper {
	private static Logger log = Logger.getLogger(CurrencyHelper.class);
	private static Properties prop;

	static {
		if (log.isDebugEnabled()) {
			log.debug("Initial Currency config begin.");
		}

		String currencyConfig = System.getProperty(Constants.CONFIG_PATH) + File.separator +  Constants.CURRENCY_PROPERTIES;
		if (log.isDebugEnabled()) {
			log.debug("Currency config file path is: " + currencyConfig);
		}

		PropertiesHelper proHelper = new PropertiesHelper();
		if (StringUtils.isNotEmpty(currencyConfig)) {
			try {
				prop = proHelper.loadProperties(currencyConfig);
			} catch (RuntimeException e) {
				log.error("Initial Currency encounter error.", e);
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("Initial Currency config end.");
		}
	}

	public static String getCurrency(String ctryCde, String orgnCde, String prodType) {
		String currencyStr = "";
		if (null != prop) {
			// Get Currency by country code ,organization code and prodtypd
			// e.g. TW.dummy.SID=USD
			currencyStr = prop.getProperty(ctryCde + Constants.DOT + orgnCde + Constants.DOT + prodType);
			if (null == currencyStr) {
				if (log.isDebugEnabled()) {
					log.debug("No Currency config entry for [Country Code: " + ctryCde + " and Organization Code: " + orgnCde + " Prod Type: " + prodType + "].");
				}
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Use Currency based on the Currency Values where the program is running.");
			}
		}
		return currencyStr;
	}
}
