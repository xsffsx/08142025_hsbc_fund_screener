package com.dummy.wpc.datadaptor.beanconverter.treemap;

import java.sql.Date;
import java.util.Map;

import com.dummy.wpc.datadaptor.util.DateHelper;
import com.dummy.wpc.datadaptor.util.StringHelper;
import com.dummy.wpc.datadaptor.writer.HeaderLineGenerator;

public class DefaultHeaderLineGenerator implements HeaderLineGenerator {

	public String[] gen(Map configMap) {
		String fieldNames = configMap.get("field_names").toString();
		String header = configMap.get("app_name").toString();
		header = "HEADER" +  StringHelper.fillStr(header, 50);
		Date curDate = DateHelper.getCurrentDate();
		header += DateHelper.formatDate2String(curDate, "yyyyMMdd");
		header += DateHelper.formatDate2String(curDate, "HHmmss");
		return new String[]{header,fieldNames};
	}
}
