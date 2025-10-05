package com.dummy.wpc.datadaptor.beanconverter;

import java.text.SimpleDateFormat;
import java.util.Map;

import com.dummy.wpc.datadaptor.writer.HeaderLineGenerator;

public class ProdTypeListHeaderLineGenerator implements HeaderLineGenerator{

	public String[] gen(Map configMap) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        StringBuffer header = new StringBuffer(100);
        header.append("HEADER                        RIS_PL  RIS CBD ");
        header.append(sdf.format(new java.util.Date()));
        header.append("P   ");
		return new String[]{header.toString()};
	}

}
