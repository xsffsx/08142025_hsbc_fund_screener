package com.dummy.wpc.datadaptor.mandatorycheck;

import org.apache.commons.lang.StringUtils;

import com.dummy.wpc.datadaptor.util.Constants;
import com.dummy.wpc.datadaptor.util.FileUtils;

public class SingleCommonMandatoryCheck extends CommonMandatoryCheck {

	@Override
	public boolean check() {
		boolean success = super.check();
		if(success == false){
			return false;
		}
		String dataFilePath = configMap.get(Constants.DATA_FILE_PATH);
		if (StringUtils.isEmpty(dataFilePath)) {
			msg = "The property \"" + Constants.DATA_FILE_PATH + "\"" + necessaryMsg;
			log.error(msg);
			reportBean.log("----Failed, Result: " + msg);
			reportBean.increaseFailed();
			return false;
		}
		if (!FileUtils.isFolder(dataFilePath)) {
			msg = "The property \"" + Constants.DATA_FILE_PATH + "\" must be a directory.";
			log.error(msg);
			reportBean.log("----Failed, Result: " + msg);
			reportBean.increaseFailed();
			return false;
		}
		
		String inputFilePatten = configMap.get(Constants.INPUT_FILE_PATTERN);
		if (StringUtils.isEmpty(inputFilePatten)) {
			msg = "The property \"" + Constants.INPUT_FILE_PATTERN + "\"" + necessaryMsg;
			log.error(msg);
			reportBean.log("----Failed, Result: " + msg);
			reportBean.increaseFailed();
			return false;
		}
		
		if (inputFilePatten.indexOf("><") != -1 || inputFilePatten.indexOf(">*") != -1 || inputFilePatten.indexOf("*<") != -1) {
			msg = "The format of \"" + Constants.INPUT_FILE_PATTERN + "\" is wrong, it cannot contains these cases:\n";
			msg += "        <p1><p2>\n";
			msg += "        <p1>*<p2>\n";
			msg += "        <p1>*\n";
			msg += "        *<p1>";
			log.error(msg);
			reportBean.log("----Failed, Result: " + msg);
			reportBean.increaseFailed();
			return false;
		}
		
		String fileNamePattern = configMap.get(Constants.FILE_NAME_PATTERN);
		if (StringUtils.isEmpty(fileNamePattern)) {
			msg = "The property \"" + Constants.FILE_NAME_PATTERN + "\"" + necessaryMsg;
			log.error(msg);
			reportBean.log("----Failed, Result: " + msg);
			reportBean.increaseFailed();
			return false;
		}
		
		String logFilePath = configMap.get(Constants.LOG_FILE_PATH);
		if (StringUtils.isEmpty(logFilePath)) {
			msg = "The property \"" + Constants.LOG_FILE_PATH + "\"" + necessaryMsg;
			log.error(msg);
			reportBean.log("----Failed, Result: " + msg);
			reportBean.increaseFailed();
			return false;
		}
		
		if (!FileUtils.isFolder(logFilePath)) {
			msg = "The property \"" + Constants.LOG_FILE_PATH + "\" must be a directory.";
			log.error(msg);
			reportBean.log("----Failed, Result: " + msg);
			reportBean.increaseFailed();
			return false;
		}
		
		return checkMultiWriter();
	}
	
	
}
