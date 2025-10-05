package com.dummy.wpc.datadaptor.bean;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.dummy.wpc.datadaptor.util.CommonPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DateHelper;

public class ReportBean {
	private static Logger log = Logger.getLogger(ReportBean.class);

	private StringBuffer logBuffer = new StringBuffer();
	private int failed = 0;
	private String ctryCode = "";
	private String orgCode = "";
	private String reportFilePath = "";

	public ReportBean(final String ctryCode, final String orgCode) {
		this.ctryCode = ctryCode;
		this.orgCode = orgCode;
	}

	public void log(final String msg) {
		this.logBuffer.append(msg + "\n");
	}

	public void increaseFailed() {
		this.failed++;

	}

	public void writeToFileAccordingToDate() {
		this.reportFilePath = CommonPropertiesHelper.getReportFilePath();

		// reportFilePath = FilePathHandler.handle(reportFilePath);
		long millis = System.currentTimeMillis();
		Date date = new Date(millis);
		String curDate = DateHelper.formatDate2String(date, "yyyyMMdd");

		if (!this.reportFilePath.endsWith(File.separator)) {
			this.reportFilePath += File.separator;
		}
		this.reportFilePath += this.ctryCode + this.orgCode + "ADPREPORT"
				+ curDate + ".txt";

		File reportFile = new File(this.reportFilePath);
		try {
			String runDateTime = "Run datetime: "
					+ DateHelper.formatDate2String(new Date(millis),
							"yyyyMMdd HH:mm:ss");
			if (FileUtils.isFileOlder(reportFile, date)) {
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(reportFile,
								true)));
				writer.write("\r\n");
				writer.write(runDateTime);
				writer.write("\r\n");
				writer.write(this.logBuffer.toString());
				writer.flush();
				writer.close();
			} else {
				StringBuffer addedDateLogBuffer = new StringBuffer();
				addedDateLogBuffer.append(runDateTime);
				addedDateLogBuffer.append("\r\n");
				addedDateLogBuffer.append(this.logBuffer);
				FileUtils.writeStringToFile(new File(this.reportFilePath),
						addedDateLogBuffer.toString());
			}
			String msg = "[" + DateHelper.getCurrentDateDefaultStr() + "] All jobs done, please check report file "
					+ getReportFilePath();
			ReportBean.log.info(msg);
			System.err.println(msg);
		} catch (IOException e) {
			ReportBean.log.error("error occured when write string to file.", e);
		}
	}

	public String getReportFilePath() {
		return this.reportFilePath;
	}
}
