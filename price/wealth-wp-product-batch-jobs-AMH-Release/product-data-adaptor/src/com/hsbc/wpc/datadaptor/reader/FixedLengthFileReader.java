package com.dummy.wpc.datadaptor.reader;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReaderException;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ReaderNotOpenException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.file.mapping.FieldSet;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.separator.LineReader;
import org.springframework.batch.item.file.separator.RecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.support.AbstractBufferedItemReaderItemStream;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import com.dummy.wpc.datadaptor.bean.RecordBean;
import com.dummy.wpc.datadaptor.mapper.AbstractFieldSetMapper;
import com.dummy.wpc.datadaptor.util.Constants;
import com.dummy.wpc.datadaptor.util.ExecutionContextHelper;
import com.dummy.wpc.datadaptor.util.FixedLengthCommonUtility;

public class FixedLengthFileReader extends AbstractBufferedItemReaderItemStream
		implements ResourceAwareItemReaderItemStream, InitializingBean {
	private static Logger log = Logger.getLogger(FixedLengthFileReader.class);
	// default encoding for input files
	private static final String DEFAULT_CHARSET = "UTF-8";
	private String encoding = DEFAULT_CHARSET;
	private Resource resource;
	private RecordSeparatorPolicy recordSeparatorPolicy;
	private String[] comments;
	private int linesToSkip = 0;
	private int lineLength = 1700;
	private String headerLineSuffix = "HEADER";
	private String trailerLineSuffix = "TRAILER";
	private HeaderLineProcessor headerLineProcessor;
	private TrailerLineProcessor trailerLineProcessor;
	private LineTokenizer tokenizer;
	private FieldSetMapper fieldSetMapper;
	private Validator validator;
	private RecordBean recordBean;
	private LineReader reader;
	private int realRecordStart = 129;
	private int realRecordEnd = 1629;

	public FixedLengthFileReader() {
		setName(ClassUtils.getShortName(FlatFileItemReader.class));
	}

	private String readLine() {
		try {
			return (String) getReader().read();
		} catch (ItemStreamException e) {
			throw e;
		} catch (ItemReaderException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException();
		}
	}

	protected LineReader getReader() {
		if (reader == null) {
			throw new ReaderNotOpenException("Reader must be open before it can be read.");
			// reader is now not null, or else an exception is thrown
		}
		return reader;
	}

	public void afterPropertiesSet() throws Exception {
		// Assert.notNull(fieldSetMapper, "FieldSetMapper must not be null.");
	}

	protected void doClose() throws Exception {
		try {
			if (reader != null) {
				// log.debug("Closing flat file for reading: " + resource);
				reader.close();
			}
		} finally {
			reader = null;
		}
	}

	public void open(ExecutionContext executionContext) throws ItemStreamException {
		this.linesToSkip = Integer.parseInt(ExecutionContextHelper.getString(executionContext,Constants.LINES_TO_SKIP));
		recordBean = new RecordBean();
		RecordBean.setCurRecordBean(ExecutionContextHelper.getString(executionContext,Constants.RECORD_BEAN_UUID), this.recordBean);
		
		constructFieldSetMapperClass(executionContext);
		Assert.notNull(fieldSetMapper, "FieldSetMapper must not be null.");
		super.open(executionContext);
		
	}
	
	private void constructFieldSetMapperClass(ExecutionContext executionContext) {
		if(fieldSetMapper instanceof AbstractFieldSetMapper){
			AbstractFieldSetMapper afs = (AbstractFieldSetMapper)fieldSetMapper;
			afs.setJobCode(ExecutionContextHelper.getString(executionContext,Constants.JOB_CODE));
		}
	}

	protected void doOpen() throws Exception {
		// badLogger = new DataAdaptorBadLogger(badFilePath);
		// appLogger = new DataAdaptorAppLogger(logFilePath);

		Assert.notNull(resource, "Input Resource must not be null");
		Assert.state(resource.exists(), "Resource must exist: [" + resource + "]");

		if (this.reader == null) {
			FlatFileLineReader reader = new FlatFileLineReader(resource, lineLength, encoding);
			if (recordSeparatorPolicy != null) {
				
				reader.setRecordSeparatorPolicy(recordSeparatorPolicy);
			}
			if (comments != null) {
				reader.setComments(comments);
			}
			reader.open();
			this.reader = reader;
		}

		for (int i = 0; i < linesToSkip; i++) {
			readLine();
		}
	}

	protected Object doRead() throws Exception {

		do {
			String line = readLine();
			if (line != null) {
				recordBean.increaseTotalRecord();
				if (StringUtils.isBlank(line.trim())) {
					recordBean.increaseBlankRecord();
					continue;
				}

				int lineCount = getReader().getPosition();
				
				try {
					if (isHeaderLine(line)) {
						recordBean.increaseBlankRecord();
						processHeaderLine(line);
						continue;
					} else if (isTrailerLine(line)) {
						recordBean.increaseBlankRecord();
						processTrailerLine(line);
						continue;
					} else {
						String destLine = StringUtils.substring(line, realRecordStart, realRecordEnd);
						FieldSet tokenizedLine = tokenizer.tokenize(destLine);
						Object dataObj = fieldSetMapper.mapLine(tokenizedLine);
						if (dataObj == null) {
							recordBean.increaseBlankRecord();
							continue;
						}
						if (validator != null) {
							validator.validate(dataObj);
						}
						return dataObj;
					}
				} catch (ValidationException ex) {
					recordBean.increaseErrorRecord();
					String errorMsgs = ReaderHelper.getErrorMsg(ex);
					recordBean.logBadRecord("line " + lineCount + " :\r\n Error Messages : \r\n" + errorMsgs
							+ "\r\n Data :" + line + "\r\n");
				} catch (RuntimeException ex) {
					// add current line count to message and re-throw
					recordBean.increaseErrorRecord();
					recordBean.logBadRecord("line " + lineCount + " :\r\n Error Messages : \r\n" + ex.getMessage()
							+ "\r\n Data :" + line + "\r\n");
				}
			} else {
				return null;
			}
		} while (true);
	}

	public void close(ExecutionContext executionContext) throws ItemStreamException {

		super.close(executionContext);
	}

	private void processTrailerLine(String line) {
		if (this.trailerLineProcessor != null) {
			try {
				trailerLineProcessor.process(line);
			} catch (Exception e) {
				log.error("error occured when process " + line, e);
			}
		}
	}

	private void processHeaderLine(String line) {
		if (this.headerLineProcessor != null) {
			try {
				headerLineProcessor.process(line);
			} catch (Exception e) {
				log.error("error occured when process " + line, e);
			}
		}
	}

	private boolean isTrailerLine(String line) {
		if (StringUtils.isEmpty(trailerLineSuffix)) {
			return false;
		}
		if (StringUtils.isEmpty(line)) {
			return false;
		}
		String newLine = FixedLengthCommonUtility.formateEBCDIC(line, lineLength);
		return newLine.startsWith(trailerLineSuffix);
	}

	private boolean isHeaderLine(String line) {
		if (StringUtils.isEmpty(headerLineSuffix)) {
			return false;
		}
		if (StringUtils.isEmpty(line)) {
			return false;
		}
		String newLine = FixedLengthCommonUtility.formateEBCDIC(line, lineLength);
		return newLine.startsWith(headerLineSuffix);
	}

	public void setRecordSeparatorPolicy(RecordSeparatorPolicy recordSeparatorPolicy) {
		this.recordSeparatorPolicy = recordSeparatorPolicy;
	}

	public void setHeaderLineSuffix(String headerLineSuffix) {
		this.headerLineSuffix = headerLineSuffix;
	}

	public void setTrailerLineSuffix(String trailerLineSuffix) {
		this.trailerLineSuffix = trailerLineSuffix;
	}

	public void setHeaderLineProcessor(HeaderLineProcessor headerLineProcessor) {
		this.headerLineProcessor = headerLineProcessor;
	}

	public void setTrailerLineProcessor(TrailerLineProcessor trailerLineProcessor) {
		this.trailerLineProcessor = trailerLineProcessor;
	}

	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public void setLineTokenizer(LineTokenizer lineTokenizer) {
		this.tokenizer = lineTokenizer;
	}
	
	public void setFieldSetMapper(FieldSetMapper fieldSetMapper) {
		this.fieldSetMapper = fieldSetMapper;
	}

	public void setLinesToSkip(int linesToSkip) {
		this.linesToSkip = linesToSkip;
	}

	public void setLineLength(int lineLength) {
		this.lineLength = lineLength;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setRealRecordStart(int realRecordStart) {
		this.realRecordStart = realRecordStart;
	}

	public void setRealRecordEnd(int realRecordEnd) {
		this.realRecordEnd = realRecordEnd;
	}

}
