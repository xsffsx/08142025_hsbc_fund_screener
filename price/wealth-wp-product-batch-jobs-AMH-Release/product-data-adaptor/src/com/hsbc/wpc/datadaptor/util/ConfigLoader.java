package com.dummy.wpc.datadaptor.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ConfigLoader {

	public static void initConfigMap(Map<String, String> configMap, OrderedProperties prop) {
		Set<String> keys = prop.getKeys();
		Iterator<String> keyIte = keys.iterator();
		while (keyIte.hasNext()) {
			String key = keyIte.next();
			if (key.startsWith(Constants.COMMON_SYS_CODE)) {
				configMap.put(key.substring(Constants.COMMON_SYS_CODE.length(), key.length()), prop.getProperty(key));
			}
		}
	}

	public static void refreshConfigMap(String sysCode, Map<String, String> configMap, OrderedProperties prop) {
		Set<String> keys = prop.getKeys();
		Iterator<String> keyIte = keys.iterator();
		String sysCodeKey = sysCode + ".";
		while (keyIte.hasNext()) {
			String key = keyIte.next();
			if (key.startsWith(sysCodeKey)) {
				configMap.put(key.substring(sysCode.length() + 1, key.length()), prop.getProperty(key));
			}
		}
	}

	/**
	 * configMap --> properties
	 * 
	 * @param configMap
	 * @return
	 */
	public static Properties convertMaptoProperties(Map<String, String> configMap) {
		OrderedProperties prop = new OrderedProperties();
		Iterator<String> keys = configMap.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			prop.setProperty(key, configMap.get(key));
		}
		return prop;
	}

	/**
	 * ${ctry_cde} --> TW ${org_cde} --> dummy ${sys_cde} --> BOND
	 * ${current_dt_tm} --> 20110101_101010
	 * 
	 * @param configMap
	 * @param params
	 */
	public static void replaceConfigMap(Map<String, String> configMap, Map<String, String> replaceMap) {

		Iterator<String> keyIte = configMap.keySet().iterator();
		while (keyIte.hasNext()) {
			String k = keyIte.next();
			String v = configMap.get(k);

			Set<String> keySet = replaceMap.keySet();
			for (String key : keySet) {
				v = v.replace(key, replaceMap.get(key));
			}
			configMap.put(k, v);
		}
	}
}
