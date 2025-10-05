/*
 */
package com.dummy.wpc.datadaptor.util;

import java.util.Map;
import com.dummy.wpc.datadaptor.constant.ConfigConstant;
import kanhan.Conversion;

/**
 * <p><b>
 * TODO : Insert description of the class's responsibility/role.
 * </b></p>
 */
public class SCConvertUtil {
	
	private static Map<String,Map<String,String>> converChinCharMap;
    private static final String CONV_SYNC_BLOCK = "CONV_SYNC_BLOCK";
    public static String convertFromTC2SC(final String str) {
        String result = str;
        Conversion scConv = null;
        String propPath = System.getProperty(Constants.CONFIG_PATH);

        // synchronized to prevent loading conversion object by other party
        synchronized (SCConvertUtil.CONV_SYNC_BLOCK) {
            scConv = new Conversion(propPath);
            scConv.useVocab(Conversion.B5_GB, true);
        }
        
        String temp = scConv.convert(str, Conversion.B5_GB);
        if (temp != null) {
            result = temp;
        }
        
        return result;
    }
    
    //Convert special code form TC to SC
    public static String convertSpecTC2SC(final String str) {
    	Map<String, String> chinSimpCharMap = null;
    	chinSimpCharMap =  (Map<String, String>) converChinCharMap.get(ConfigConstant.CHIN_SIMP_CHAR);
    	String prodName = str;
    	for(String simpChar : chinSimpCharMap.keySet()){
    		if(str.indexOf(simpChar)!=-1){
    			char[] prodNameChar = str.toCharArray();
    			for(int i=0; i<prodNameChar.length;i++){
    				String prodNameStr = null;
    				char item = str.charAt(i);
    				if(chinSimpCharMap.containsKey(String.valueOf(item))){
    					prodNameStr = chinSimpCharMap.get(String.valueOf(item));
    					prodNameChar[i] = prodNameStr.charAt(0);
    				}
    			}
    			prodName = String.valueOf(prodNameChar);
    			break;
    		}
    	}
		return prodName;
	}

	public Map<String, Map<String, String>> getConverChinCharMap() {
		return converChinCharMap;
	}

	public void setConverChinCharMap(
			Map<String, Map<String, String>> converChinCharMap) {
		this.converChinCharMap = converChinCharMap;
	}
    
}
