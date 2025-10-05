package com.dummy.wpc.datadaptor.mandatorycheck;

import java.util.Map;

import com.dummy.wpc.datadaptor.bean.ReportBean;

public interface MandatoryCheck {

	boolean check();

	void setConfigMap(Map<String, String> configMap);

	void setCtryCode(String ctryCode);

	void setOrgnCode(String orgnCode);

	void setJobCode(String jobCode);

	void setReportBean(ReportBean reportBean);

}
