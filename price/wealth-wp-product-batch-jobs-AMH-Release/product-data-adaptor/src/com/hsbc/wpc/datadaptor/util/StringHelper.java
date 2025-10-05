package com.dummy.wpc.datadaptor.util;


public class StringHelper {
	public static String fillNumber(int number, int maxLen) {
		String str = "" + number;
		if(str.length() < maxLen){
			while(str.length() < maxLen){
				str = "0" + str;
			}
		}
		else{
			str = str.substring(0,maxLen);
		}
		return str;
	}

	public static String fillStr(String str, int maxLen) {
		if(str == null){
			str = "";
		}
		if(str.length() < maxLen){
			while(str.length() < maxLen){
				str = str + " ";
			}
		}
		else{
			str = str.substring(0,maxLen);
		}
		return str;
	}
	
	
}
