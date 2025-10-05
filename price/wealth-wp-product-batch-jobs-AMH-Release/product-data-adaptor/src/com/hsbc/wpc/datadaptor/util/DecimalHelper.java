package com.dummy.wpc.datadaptor.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class DecimalHelper {
    
    private static final Logger LOGGER = Logger.getLogger(DecimalHelper.class);
    
	public static String trimZero(String str) {
		if (StringUtils.isEmpty(str)) {
			return str;
		} else if (StringUtils.containsOnly(str, ".0")) {
			return "0";
		} else {
			return new BigDecimal(str).stripTrailingZeros().toPlainString();
		}
	}
	
	public static String bigDecimal2String(BigDecimal bigDecimal) {
		if (bigDecimal != null) {
		    return bigDecimal.toString();
		} else {
		    return null;
		}
	}
	
	public static BigDecimal formatDecimalPlace(BigDecimal bigDecimal, int decimalPlace) {
        if (bigDecimal != null) {
            try {
                String pattern = "#0.";
                for (int i = 0; i < decimalPlace; i++) {
                    pattern = pattern.concat("0");
                }
                
                DecimalFormat df = new DecimalFormat(pattern);
                double d = bigDecimal.doubleValue();
                return new BigDecimal(df.format(d));
            } catch (Exception e) {
                LOGGER.error("formatDecimalPlace exception"+bigDecimal.doubleValue()+":"+e.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }
    
}
