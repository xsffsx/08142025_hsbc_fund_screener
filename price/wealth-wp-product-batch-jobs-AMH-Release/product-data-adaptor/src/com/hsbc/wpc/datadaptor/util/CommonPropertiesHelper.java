package com.dummy.wpc.datadaptor.util;

import java.io.File;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CommonPropertiesHelper {
	private static Logger log = Logger.getLogger(CommonPropertiesHelper.class);
	private static Properties prop;
	
	static {
		if (log.isDebugEnabled()) {
			log.debug("[" + DateHelper.getCurrentDateDefaultStr() + "] Initial common config begin.");
		}
		
		String commonPropPath = System.getProperty(Constants.CONFIG_PATH) + File.separator + Constants.COMMON_PROPERTIES;
		if (log.isDebugEnabled()) {
			log.debug("[" + DateHelper.getCurrentDateDefaultStr() + "] common config file path is: " + commonPropPath);
		}
		PropertiesHelper proHelper = new PropertiesHelper();

		if (StringUtils.isNotEmpty(commonPropPath)) {
			try {
				prop = proHelper.loadProperties(commonPropPath);
			} catch (RuntimeException e) {
				log.error("Initial common config error.", e);
			}
		}
		
		if (log.isDebugEnabled()) {
			log.debug("[" + DateHelper.getCurrentDateDefaultStr() + "] Initial common config end.");
		}
	}
	
	public static String getReportFilePath(){
		String reportFilePath = null;
		if(prop != null){
			reportFilePath = prop.getProperty(Constants.REPORT_FILE_PATH);
		}
		if(StringUtils.isEmpty(reportFilePath)){
			reportFilePath = System.getProperty(Constants.CONFIG_PATH);
		}
		return reportFilePath;
	}
	
	public static String getValue(String key){
		String value = "";
		if(prop != null){
			value = prop.getProperty(key);
		}
		return value;
	}
}
