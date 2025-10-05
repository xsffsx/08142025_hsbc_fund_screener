package com.dummy.wpc.datadaptor.listener;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.ExitStatus;

import com.dummy.wpc.common.tng.TNGMessage;
import com.dummy.wpc.common.tng.TNGMsgConstants;
import com.dummy.wpc.datadaptor.log.DataAdaptorAppLogger;
import com.dummy.wpc.datadaptor.util.ConfigMapHelper;
import com.dummy.wpc.datadaptor.util.Constants;
import com.dummy.wpc.datadaptor.util.ExecutionContextHelper;

/**
 * This class is used to copy the properties from JobParameters to
 * ExecutionContext in StepExecution.
 * 
 * @author Perry Guo
 * 
 */
public class InitExecutionContext implements StepExecutionListener {
	private static Logger log = Logger.getLogger(InitExecutionContext.class);
	private DataAdaptorAppLogger appLogger = null;
	private Map configMap = null;
	public ExitStatus afterStep(StepExecution stepExecution) {
		ConfigMapHelper.replaceTempOutputFileName(configMap);
		if(appLogger != null){
			appLogger.stop();
			appLogger.log(configMap);
			appLogger.closeLogger();
		}
		return null;
	}

	private void putParams(ExecutionContext ec, Map params) {
		
		if (params != null && params.size() > 0) {
			Iterator it = params.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				Object value = params.get(key);
				ec.put(key, value);
			}
		}
	}

	public void beforeStep(StepExecution stepExecution) {
		System.out.println("beforeStep...." + this);
		
		ExecutionContext ec = stepExecution.getExecutionContext();
		Map params = stepExecution.getJobParameters().getStringParameters();
		putParams(ec, params);

		params = stepExecution.getJobParameters().getLongParameters();
		putParams(ec, params);

		params = stepExecution.getJobParameters().getDateParameters();
		putParams(ec, params);

		params = stepExecution.getJobParameters().getDoubleParameters();
		putParams(ec, params);
		
		//
		String recordBeanKey = UUID.randomUUID().toString();
		ec.put(Constants.RECORD_BEAN_UUID, recordBeanKey);
		configMap = ExecutionContextHelper.copyProperties(ec);

		try {
			appLogger = new DataAdaptorAppLogger((String)configMap.get(Constants.LOG_FILE_PATH),recordBeanKey);
		} catch (IOException ex) {
			log.error(ex.getMessage(), ex);
			TNGMessage.logTNGMsgExInfo("005", "E", TNGMsgConstants.SERVICE_NAME, "Cannot initialize the log file.");
			throw new IllegalArgumentException("Cannot initialize the log file.");

		}
		appLogger.start();
	}

	public ExitStatus onErrorInStep(StepExecution stepExecution, Throwable e) {
		return null;
	}
	

}
