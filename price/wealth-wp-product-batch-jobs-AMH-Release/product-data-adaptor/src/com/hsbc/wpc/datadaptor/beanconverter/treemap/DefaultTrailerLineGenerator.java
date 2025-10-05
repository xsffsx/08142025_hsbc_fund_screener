package com.dummy.wpc.datadaptor.beanconverter.treemap;


import java.util.Map;

import com.dummy.wpc.datadaptor.util.StringHelper;
import com.dummy.wpc.datadaptor.writer.TrailerLineGenerator;

public class DefaultTrailerLineGenerator implements TrailerLineGenerator{

	public String[] gen(Map configMap,int totalLine) {
		String line = configMap.get("app_name").toString();
		line = "TRAILER" +  StringHelper.fillStr(line, 50);
		line += StringHelper.fillNumber(totalLine,7);
		return new String[]{line};
	}

	

}
