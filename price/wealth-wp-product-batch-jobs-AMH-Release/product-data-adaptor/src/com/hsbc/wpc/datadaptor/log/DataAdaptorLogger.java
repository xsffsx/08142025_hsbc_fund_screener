package com.dummy.wpc.datadaptor.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;

public abstract class DataAdaptorLogger {
	private Logger log = Logger.getLogger(DataAdaptorLogger.class);

	protected File logFile;
	protected BufferedWriter bufferedWriter;

	protected void init() throws IOException {
		if (logFile != null) {
			checkAndCreateFile(logFile);
			initWriter();
		}
	}

	protected void initWriter() {
		OutputStream outputStream = null;
		try {
			// append
			outputStream = new FileOutputStream(logFile, true);
		} catch (FileNotFoundException e) {
			log.error("can not found " + logFile,e);
		}
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
		bufferedWriter = new BufferedWriter(outputStreamWriter);
	}

	public DataAdaptorLogger() throws IOException {
		init();
	}

	public DataAdaptorLogger(String logFile) throws IOException {
		if (StringUtils.isEmpty(logFile)) {
			this.logFile = null;
			return;
		}
		this.logFile = new File(logFile);
	}

	public DataAdaptorLogger(Resource logFile) throws IOException {
		if (logFile == null || logFile.getFile() == null) {
			logFile = null;
			return;
		}
		this.logFile = logFile.getFile();
	}

	protected void checkAndCreateFile(File file) throws IOException {
		if (file.getName().startsWith("%") && file.getName().endsWith("%")) {
			logFile = null;
			return;
		}
		if (file.isDirectory()) {
			throw new IOException("The file \"" + file.getAbsolutePath() + "\" is not a valid file");
		}
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
	}

	public void closeLogger() {
		if (bufferedWriter != null) {
			try {
				bufferedWriter.flush();
				bufferedWriter.close();
			} catch (IOException e) {
				log.error("error occured when flush/close the logger",e);
			}
		}
	}

	public abstract void log(String msg);

	public File getLogFile() {
		return logFile;
	}

	public void setLogFile(File logFile) {
		this.logFile = logFile;
	}
}
