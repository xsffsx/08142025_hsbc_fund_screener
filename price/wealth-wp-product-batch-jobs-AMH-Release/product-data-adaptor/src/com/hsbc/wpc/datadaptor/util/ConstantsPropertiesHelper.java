package com.dummy.wpc.datadaptor.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class ConstantsPropertiesHelper {
	private static Map<String,OrderedProperties> propMap = new HashMap();
	private static Map<String,Boolean> initMap = new HashMap();

	
	public static void init(String keyPreifx,String constantsFilePath){
		Boolean initialized = initMap.get(keyPreifx);
		if(initialized != null && initialized == true){
			return;
		}
		initMap.put(keyPreifx, true);

		if(!StringUtils.isEmpty(constantsFilePath)){
			File file = new File(constantsFilePath);
			if(file.exists()){
				OrderedProperties properties = PropertiesHelper.loadProperties(constantsFilePath);
				if(properties != null){
					propMap.put(keyPreifx, properties);
				}
			}
		}
	}
	
	public static String getValue(String keyPrefix,String key){
		if(StringUtils.isEmpty(keyPrefix)){
			System.err.println("keyPrefix cannot be empty:"+keyPrefix + "." + key);
		}
		key = keyPrefix + "." + key;
		OrderedProperties properties = propMap.get(keyPrefix);
		if(properties != null){
			return properties.getProperty(key);
		}
		else{
			return null;
		}
	}
}
