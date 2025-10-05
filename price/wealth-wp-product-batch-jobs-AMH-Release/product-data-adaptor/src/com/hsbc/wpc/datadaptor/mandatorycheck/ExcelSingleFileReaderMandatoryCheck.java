package com.dummy.wpc.datadaptor.mandatorycheck;

import org.apache.commons.lang.StringUtils;

import com.dummy.wpc.datadaptor.util.Constants;
import com.dummy.wpc.datadaptor.util.FileUtils;


public class ExcelSingleFileReaderMandatoryCheck extends CommonMandatoryCheck {
    
    @Override
    public boolean check() {
        
        boolean success = super.check();
        if (!success) {
            return false;
        }
        
        String key = Constants.DATA_FILE_PATH;
        String dataFilePath = configMap.get(key);
        
        if (StringUtils.isEmpty(dataFilePath)) {
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
        
        key = Constants.INPUT_FILE_PATTERN;
        String inputFilePattern = configMap.get(key);
        if (StringUtils.isEmpty(inputFilePattern)) {
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
        
        String mapper = configMap.get(Constants.MAPPER);
        if (StringUtils.isEmpty(mapper)) {
            msg = "The property \"" + Constants.MAPPER + "\"" + necessaryMsg;
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
