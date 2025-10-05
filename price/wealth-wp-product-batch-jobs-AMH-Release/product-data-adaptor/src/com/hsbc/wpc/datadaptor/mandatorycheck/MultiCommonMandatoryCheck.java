package com.dummy.wpc.datadaptor.mandatorycheck;

import org.apache.commons.lang.StringUtils;

import com.dummy.wpc.datadaptor.util.Constants;
import com.dummy.wpc.datadaptor.util.FileUtils;

public class MultiCommonMandatoryCheck extends CommonMandatoryCheck {

	@Override
	public boolean check() {
		boolean success = super.check();
		if(success == false){
			return false;
		}
		String readerCountStr = configMap.get(Constants.READER_COUNT);
		int readerCount = 0;
		try{
			readerCount  = Integer.parseInt(readerCountStr);
		}
		catch(Exception ex){
			readerCount = 0;
		}
		if(StringUtils.isEmpty(readerCountStr) || readerCount <= 0){
			msg = "The property \"" + Constants.READER_COUNT + "\" is necessary and should be a valid number which greater than 0.";
			log.error(msg);
			reportBean.log("----Failed, Result: " + msg);
			reportBean.increaseFailed();
			return false;
		}
		for(int i = 1 ; i <= readerCount ; i ++ ){
			String keyPrefix = "reader." + i + ".";
			String key = keyPrefix + Constants.DATA_FILE_PATH;
			String dataFilePath = configMap.get(key);
			
			if(StringUtils.isEmpty(dataFilePath)){
				msg = "The property \"" + key + "\"" + necessaryMsg;
				log.error(msg);
				reportBean.log("----Failed, Result: " + msg);
				reportBean.increaseFailed();
				return false;
			}
			if (!FileUtils.isFolder(dataFilePath)) {
				msg = "The property \"" + key + "\" must be a directory," + necessaryMsg;
				log.error(msg);
				reportBean.log("----Failed, Result: " + msg);
				reportBean.increaseFailed();
				return false;
			}

			
			key = keyPrefix + Constants.INPUT_FILE_PATTERN;
			String inputFilePattern = configMap.get(key);
			if(StringUtils.isEmpty(inputFilePattern)){
				msg = "The property \"" + key + "\"" + necessaryMsg;
				log.error(msg);
				reportBean.log("----Failed, Result: " + msg);
				reportBean.increaseFailed();
				return false;
			}
			
			if (inputFilePattern.indexOf("><") != -1 || inputFilePattern.indexOf(">*") != -1 || inputFilePattern.indexOf("*<") != -1) {
				msg = "The format of \"" + key + "\" is wrong, it cannot contains these cases:\n";
				msg += "        <p1><p2>\n";
				msg += "        <p1>*<p2>\n";
				msg += "        <p1>*\n";
				msg += "        *<p1>";
				log.error(msg);
				reportBean.log("----Failed, Result: " + msg);
				reportBean.increaseFailed();
				return false;
			}
			
			key = keyPrefix + Constants.FRAGMENT_ROOT_ELEMENT_NAME;
			String rootElementName = configMap.get(key);
			if (StringUtils.isEmpty(rootElementName)) {
				msg = "The property \"" + key + "\"" + necessaryMsg;
				log.error(msg);
				reportBean.log("----Failed, Result: " + msg);
				reportBean.increaseFailed();
				return false;
			}
			
			key = keyPrefix + Constants.XML_ITEM_BEAN;
			String xmlItemBean = configMap.get(key);
			if (StringUtils.isEmpty(xmlItemBean)) {
				msg = "The property \"" + key + "\"" + necessaryMsg;
				log.error(msg);
				reportBean.log("----Failed, Result: " + msg);
				reportBean.increaseFailed();
				return false;
			}
		}//end-for
		
		
		String beanConverter = configMap.get(Constants.BEAN_CONVERTER);
		if (StringUtils.isEmpty(beanConverter)) {
			msg = "The property \"" + Constants.BEAN_CONVERTER + "\"" + necessaryMsg;
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
			msg = "The property \"" + Constants.LOG_FILE_PATH + "\" must be a directory." + necessaryMsg;
			log.error(msg);
			reportBean.log("----Failed, Result: " + msg);
			reportBean.increaseFailed();
			return false;
		}
		
		return checkMultiWriter();
	}

}
