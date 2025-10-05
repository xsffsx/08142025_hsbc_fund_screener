package com.dummy.wpc.datadaptor.util;

import java.io.File;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class ProdTypCdeMappingHelper {
	private static Logger log = Logger.getLogger(ProdTypCdeMappingHelper.class);
	private static Properties prop;
	private static String seperator = "_";

	static{
		if (log.isDebugEnabled()) {
			log.debug("Initial Product Type Code Mapping config begin.");
		}
		
		String prodTypCdeMappingConfig = System.getProperty(Constants.CONFIG_PATH) + File.separator + Constants.PROD_TYP_CDE_MAPPING_PROPERTIES;
		if (log.isDebugEnabled()) {
			log.debug("Product Type Code Mapping config file path is: " + prodTypCdeMappingConfig);
		}
		
		if (StringUtils.isNotEmpty(prodTypCdeMappingConfig)) {
			try {
				prop = PropertiesHelper.loadProperties(prodTypCdeMappingConfig);
				// Display the prodTypCdeMapping item
				if (log.isDebugEnabled()) {
					log.debug("Product Type Code Mapping item:");
					Set<String> keys = ((OrderedProperties)prop).getKeys();
					Iterator<String> keyIte = keys.iterator();
					while (keyIte.hasNext()) {
						String key = keyIte.next();
						log.debug(key + "=" + prop.getProperty(key));
					}
				}
			} catch (RuntimeException e) {
				log.error("Initial Product Type Code Mapping encounter error.", e);
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("Initial Product Type Code Mapping config end.");
		}		
	}
	
	public static String getInternalProdTypCde(String ctryCde, String orgnCde, String externalProdTypCde) {
		String internalProdTypCde = "";
		// Get internal product type code by country code, organization code and external product type code
		// e.g. CN_dummy_BOND=BOND	
		String key = ctryCde + seperator + orgnCde + seperator + externalProdTypCde;
		if (null != prop) {
			internalProdTypCde = prop.getProperty(key);
		} 

		//Specical Handling, if cann't find the internalProdTypCde, just retrun externalProdTypCde
		if(StringUtils.isBlank(internalProdTypCde)) {
			if (log.isDebugEnabled()) {
				log.debug("Can't find related internalProdTypCde for " + key + ", so just use the externalProdTypCde.");
			}
			internalProdTypCde = externalProdTypCde;
		}
		
		return internalProdTypCde;
	}
}
