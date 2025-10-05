package com.dummy.wpc.datadaptor.beanconverter;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.dummy.wpc.datadaptor.writer.TrailerLineGenerator;

public class ProdTypeListTrailerLineGenerator implements TrailerLineGenerator{

	public String[] gen(Map configMap, int totalLine) {
        StringBuffer trailer = new StringBuffer(100);
        trailer.append("TRAILER                        RIS_PL  RIS CBD ");
        trailer.append(StringUtils.leftPad(Integer.toString(totalLine), 7, '0'));
        trailer.append("000000000000000+");
		return new String[]{trailer.toString()};
	}

}
