package com.dummy.wpc.datadaptor.mandatorycheck;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.dummy.wpc.datadaptor.bean.ReportBean;
import com.dummy.wpc.datadaptor.util.Constants;


public class MandatoryCheckFactory {

	public static MandatoryCheck instance(Map<String, String> configMap, String ctryCode, String orgnCode, String jobCode, ReportBean reportBean) {
		
	    Logger log = Logger.getLogger(MandatoryCheckFactory.class);
		String dataSourceFormat = (String) configMap.get(Constants.DATA_SOURCE_FORMAT);
		if (StringUtils.isEmpty(dataSourceFormat)) {
			dataSourceFormat = Constants.DATA_SOURCE_FORMAT_TXT;
		}
		dataSourceFormat = dataSourceFormat.toLowerCase();
		MandatoryCheck mandatoryCheck = null;
		if (Constants.DATA_SOURCE_FORMAT_TXT.equals(dataSourceFormat)) {
			mandatoryCheck = new TxtMandatoryCheck();
		}
		else if (Constants.DATA_SOURCE_FORMAT_CSV.equals(dataSourceFormat)) {
			mandatoryCheck = new CSVMandatoryCheck();
		}
		else if (Constants.DATA_SOURCE_FORMAT_XML.equals(dataSourceFormat)) {
			mandatoryCheck = new XMLMandatoryCheck();
		}
		else if (Constants.DATA_SOURCE_FORMAT_XML_MULTI_READER_2_CSV.equals(dataSourceFormat)) {
			mandatoryCheck = new XMLMultiReaderMandatoryCheck();
		}
		else if (Constants.DATA_SOURCE_FORMAT_EXCEL_MULTI_READER_2_XML.equalsIgnoreCase(dataSourceFormat)){
		    mandatoryCheck = new ExcelMultiFileReaderMandatoryCheck();
		}
		else if (Constants.DATA_SOURCE_FORMAT_EXCEL_DEFAULT_READER_2_XML.equalsIgnoreCase(dataSourceFormat)){
            mandatoryCheck = new ExcelSingleFileReaderMandatoryCheck();
        }
		else if (Constants.DATA_SOURCE_FORMAT_EXCEL_SINGLE_READER_2_XML.equalsIgnoreCase(dataSourceFormat)){
            mandatoryCheck = new ExcelSingleFileReaderMandatoryCheck();
        }
		if(mandatoryCheck != null) {
		    mandatoryCheck.setConfigMap(configMap);
	        mandatoryCheck.setCtryCode(ctryCode);
	        mandatoryCheck.setOrgnCode(orgnCode);
	        mandatoryCheck.setJobCode(jobCode);
	        mandatoryCheck.setReportBean(reportBean);
		} else {
		    String msg = "Invalid data_source_format config: " + dataSourceFormat;
            log.error(msg);
            reportBean.log("----Failed, Result: " + msg);
            reportBean.increaseFailed();
		}
		
		return mandatoryCheck;
		
		
	}

}
