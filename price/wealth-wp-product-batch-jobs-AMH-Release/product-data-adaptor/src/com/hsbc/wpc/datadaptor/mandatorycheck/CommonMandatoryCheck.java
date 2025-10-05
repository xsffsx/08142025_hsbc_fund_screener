package com.dummy.wpc.datadaptor.mandatorycheck;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.dummy.wpc.datadaptor.bean.ReportBean;
import com.dummy.wpc.datadaptor.util.ConfigMapHelper;
import com.dummy.wpc.datadaptor.util.Constants;
import com.dummy.wpc.datadaptor.util.FileUtils;

public class CommonMandatoryCheck implements MandatoryCheck {
	protected static Logger log = Logger.getLogger(CommonMandatoryCheck.class);
	
	protected Map<String, String> configMap;
	protected String ctryCode;
	protected String jobCode;
	protected String orgnCode;
	protected ReportBean reportBean;
	
	protected String necessaryMsg = "";
	protected String msg = "";
	public boolean check() {
		necessaryMsg = "is necessary for [" + ctryCode + "," + orgnCode + "," + jobCode + "], this job is skipped.";
		
		String jobBeanId = configMap.get(Constants.JOB_BEAN_ID);
		if (StringUtils.isEmpty(jobBeanId)) {
			msg = "The configMaperty \"" + Constants.JOB_BEAN_ID + "\"" + necessaryMsg;
			log.error(msg);
			reportBean.log("----Failed, Result: " + msg);
			reportBean.increaseFailed();
			return false;
		}
		
		return true;
	}

	public Map<String, String> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map<String, String> configMap) {
		this.configMap = configMap;
	}

	public String getCtryCode() {
		return ctryCode;
	}

	public void setCtryCode(String ctryCode) {
		this.ctryCode = ctryCode;
	}

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public String getOrgnCode() {
		return orgnCode;
	}

	public void setOrgnCode(String orgnCode) {
		this.orgnCode = orgnCode;
	}

	public ReportBean getReportBean() {
		return reportBean;
	}

	public void setReportBean(ReportBean reportBean) {
		this.reportBean = reportBean;
	}

	protected boolean checkMultiWriter(){
		boolean multiWriter = ConfigMapHelper.isMultiWriter(configMap);
		if (multiWriter) {
			String outputFilePattern = configMap.get(Constants.OUTPUT_FILE_PATTERN);
			if (!checkOuputFilePattern(outputFilePattern, reportBean, "")) {
				return false;
			}
			int writerCount = Integer.valueOf(configMap.get(Constants.WRITER_COUNT));
			for (int i = 1; i <= writerCount; i++) {
				// writer.1.output_file_path
				String outputFilePath = configMap.get("writer." + i + "." + Constants.OUTPUT_FILE_PATH);
				// writer.1.output_file_pattern
				outputFilePattern = configMap.get("writer." + i + "." + Constants.OUTPUT_FILE_PATTERN);
				if (!checkOuputFilePatternAndPath(outputFilePath, outputFilePattern, i, ctryCode, orgnCode, jobCode, reportBean)) {
					return false;
				}
			}
		} else {
			String outputFilePath = configMap.get(Constants.OUTPUT_FILE_PATH);
			String outputFilePattern = configMap.get(Constants.OUTPUT_FILE_PATTERN);
			if (!checkOuputFilePatternAndPath(outputFilePath, outputFilePattern, 0, ctryCode, orgnCode, jobCode, reportBean)) {
				return false;
			}
		}
		return true;
	}
	

	
	protected  boolean checkOuputFilePatternAndPath(String outputFilePath, String outputFilePattern, int writerIndex, String ctryCode, String orgnCode, String jobCode,
			ReportBean reportBean) {
		String itemPrefix = writerIndex > 0 ? "writer." + writerIndex + "." : "";
		if (StringUtils.isEmpty(outputFilePath)) {
			msg = "The property \"" + itemPrefix + Constants.OUTPUT_FILE_PATH + "\"" + necessaryMsg;
			log.error(msg);
			reportBean.log("----Failed, Result: " + msg);
			reportBean.increaseFailed();
			return false;
		}

		if (!FileUtils.isFolder(outputFilePath)) {
			msg = "The property \"" + itemPrefix + Constants.OUTPUT_FILE_PATH + "\" must be a directory.";
			log.error(msg);
			reportBean.log("----Failed, Result: " + msg);
			reportBean.increaseFailed();
			return false;
		}

		if (!checkOuputFilePattern(outputFilePattern, reportBean, itemPrefix)) {
			return false;
		}
		return true;
	}

	protected  boolean checkOuputFilePattern(String outputFilePattern, ReportBean reportBean, String itemPrefix) {
		String msg;
		if (StringUtils.isEmpty(outputFilePattern)) {
			msg = "The property \"" + itemPrefix + Constants.OUTPUT_FILE_PATTERN + "\" is necessary.";
			log.error(msg);
			reportBean.log("----Failed, Result: " + msg);
			reportBean.increaseFailed();
			return false;
		}
		if (outputFilePattern.indexOf("*") != -1) {
			msg = "The property \"" + itemPrefix + Constants.OUTPUT_FILE_PATTERN + "\" cannot contains \"*\".";
			log.error(msg);
			reportBean.log("----Failed, Result: " + msg);
			reportBean.increaseFailed();
			return false;
		}
		return true;
	}
	
	protected void initLinksToSkip(){
		String linesToSkip = configMap.get(Constants.LINES_TO_SKIP);
		if (StringUtils.isEmpty(linesToSkip) || !org.apache.commons.lang.math.NumberUtils.isDigits(linesToSkip)) {
			configMap.put(Constants.LINES_TO_SKIP, "0");
		} else {
			configMap.put(Constants.LINES_TO_SKIP, linesToSkip);
		}
	}

}
