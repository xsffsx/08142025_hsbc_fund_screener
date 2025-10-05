package com.dummy.wpc.datadaptor.log;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;

import com.dummy.wpc.common.tng.TNGMessage;
import com.dummy.wpc.common.tng.TNGMsgConstants;
import com.dummy.wpc.datadaptor.bean.RecordBean;

import com.dummy.wpc.datadaptor.util.ConfigMapHelper;
import com.dummy.wpc.datadaptor.util.Constants;
import com.dummy.wpc.datadaptor.util.DateHelper;
import com.dummy.wpc.datadaptor.util.StringHelper;

public class DataAdaptorAppLogger extends DataAdaptorLogger {
	private Logger log = Logger.getLogger(DataAdaptorAppLogger.class);
	private long startTime;
	private long endTime;
	private String recordBeanKey;


	public DataAdaptorAppLogger(String logFile,String recordBeanKey) throws IOException {
		super(logFile);
		this.recordBeanKey = recordBeanKey;
		init();
	}

	public DataAdaptorAppLogger(Resource logFile) throws IOException {
		super(logFile);
		init();
	}

	public void start() {
		startTime = System.currentTimeMillis();
	}

	public void stop() {
		endTime = System.currentTimeMillis();
	}

	public void log(String msg) {
		if (bufferedWriter != null) {
			try {
				bufferedWriter.write(msg);
				bufferedWriter.flush();
			} catch (IOException e) {
				log.error("error occured when flush/close the logger",e);
			}
		}
	}

	public void log(Map configMap) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("----------------------------------------------------------------------------------");
		buffer.append("\r\n");

		if (configMap != null) {
			Iterator<String> it = configMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if(Constants.RECORD_BEAN_UUID.equals(key) || Constants.CTRY_CODE.equals(key) 
						|| Constants.ORGN_CODE.equals(key) || Constants.JOB_CODE.equals(key)){
					continue;
				}
				buffer.append(key + " : " + configMap.get(key));
				buffer.append("\r\n");
				buffer.append("----------------------------------------------------------------------------------");
				buffer.append("\r\n");
			}
		}

		/*
		 * spent time
		 */
		buffer.append("\r\n");
		buffer.append("----------------------------------------------------------------------------------");
		buffer.append("\r\n");
		buffer.append("Run began on ");
		buffer.append(new Date(startTime));
		buffer.append("\r\n");
		buffer.append("Run ended on ");
		buffer.append(new Date(endTime));
		buffer.append("\r\n");
		long spent = endTime - startTime;
		buffer.append("program total runs : " + DateHelper.elapsedTime2Str(spent / 1000));
		buffer.append("\r\n");
		buffer.append("----------------------------------------------------------------------------------");
		
		RecordBean recordBean = RecordBean.getCurRecordBean(recordBeanKey);
		/*
		 * record
		 */
		if(recordBean != null){
			String fileName = getDataFileName(configMap);
//			recordBean.setDataFileName(fileName);
			buffer.append(recordBean.getLogSummary());
			String ctryCode = (String) configMap.get(Constants.CTRY_CODE);
			String orgnCode = (String)configMap.get(Constants.ORGN_CODE);
			String jobCode = (String) configMap.get(Constants.JOB_CODE);
			recordBean.logTNGmsg(ctryCode,orgnCode,jobCode,fileName);
			/*
			 * bad record
			 */
			buffer.append("\r\n");
			if(recordBean.getBadRecordBuffer() != null){
				String badLog = recordBean.getBadRecordBuffer().toString();
				if (StringUtils.isNotBlank(badLog)) {
					
					buffer.append("[ERROR] File Adaptor got error when processing data file \"" + fileName + "\"\r\n");
					buffer.append(badLog);
					String msg = "Job [" + ctryCode + "," + orgnCode + "," + jobCode + "],"+badLog ;
					TNGMessage.logTNGMsgExInfo("009", "E", TNGMsgConstants.DA009, msg);
				}
			}
			
		}
		/*
		 * log
		 */
		//logs MS.UT record count
		RecordBean MSUtRecordBean = RecordBean.getCurRecordBean("ExcelMSUtRecordBean");
		if(MSUtRecordBean!=null){
			log.info(MSUtRecordBean.getLogSummary());
		}
		
		this.log(buffer.toString());
	}
	
	
	private String getDataFileName(Map<String, String> configMap){
		String fileName = "";
		if(ConfigMapHelper.isMultiReader(configMap)){
			int readerCount  = Integer.parseInt( configMap.get(Constants.READER_COUNT));
			fileName = "[";
			for (int i = 1; i <= readerCount; i++) {
				String keyPreifx = "reader." + i + ".";
				
				if(i > 1){
					fileName += ",";
				}
				File file = new File((String)configMap.get(keyPreifx + Constants.DATA_FILE_PATH));
				fileName += file.getName();
			}
			fileName += "]";
		}
		else{
			File file = new File((String)configMap.get(Constants.DATA_FILE_PATH));
			fileName = file.getName();
		}
		return fileName;
	}
	
	
	
	
}
