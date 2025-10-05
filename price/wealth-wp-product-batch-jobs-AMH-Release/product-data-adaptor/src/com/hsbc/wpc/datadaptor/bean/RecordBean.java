package com.dummy.wpc.datadaptor.bean;

import java.util.HashMap;
import java.util.Map;

import com.dummy.wpc.common.tng.TNGMessage;
import com.dummy.wpc.common.tng.TNGMsgConstants;

public class RecordBean {
    
    private static Map<String, RecordBean> curRecordBeanMap = new HashMap();
	private int totalRecord;
	private int errorRecord;
	private int blankRecord;
	private StringBuffer logBuffer = new StringBuffer();

	
    public static RecordBean getCurRecordBean(String key) {
        return curRecordBeanMap.get(key);
    }

    public static void setCurRecordBean(String key, RecordBean curRecordBean) {
        curRecordBeanMap.put(key, curRecordBean);
    }
    
	public  void logBadRecord(String msg) {
		logBuffer.append(msg + "\n");
	}
	
	public StringBuffer getBadRecordBuffer(){
		return logBuffer;
	}
	
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	public void increaseTotalRecord() {
		totalRecord++;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void increaseErrorRecord() {
		errorRecord++;
	}

	public void increaseBlankRecord() {
		blankRecord++;
	}
	
	public String getLogSummary(){
		StringBuffer buffer = new StringBuffer();
//		buffer.append("----------------------------------------------------------------------------------");
		buffer.append("\r\n");
		buffer.append("Total logical records error: " + errorRecord);
        buffer.append("\r\n");
        buffer.append("Total logical records blank: " + blankRecord);
        buffer.append("\r\n");
		buffer.append("Total logical records success: " + (totalRecord - errorRecord - blankRecord));
		buffer.append("\r\n");
		buffer.append("Total logical records read: " + totalRecord);
        buffer.append("\r\n");
		buffer.append("----------------------------------------------------------------------------------");
		buffer.append("\r\n");
		return buffer.toString();
	}


	public void logTNGmsg(String ctryCde, String orgnCde, String jobCode,String dataFileName) {
		if(errorRecord > 0){
			String msg = "Job [" + ctryCde + "," + orgnCde + "," + jobCode + "], File[" + dataFileName + "] has " + errorRecord + " invalid records.";
			TNGMessage.logTNGMsgExInfo("009", "E", TNGMsgConstants.SERVICE_NAME, msg);
		}
	}

    public int getErrorRecord() {
        return errorRecord;
    }

    public void setErrorRecord(int errorRecord) {
        this.errorRecord = errorRecord;
    }

    public int getBlankRecord() {
        return blankRecord;
    }

    public void setBlankRecord(int blankRecord) {
        this.blankRecord = blankRecord;
    }
	
	
}