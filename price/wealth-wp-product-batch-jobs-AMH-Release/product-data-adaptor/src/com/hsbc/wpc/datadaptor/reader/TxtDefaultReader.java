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
import org.springframework.batch.item.file.separator.ResourceLineReader;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
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

public class TxtDefaultReader extends AbstractBufferedItemReaderItemStream implements ResourceAwareItemReaderItemStream, InitializingBean {
	private static Logger log = Logger.getLogger(TxtDefaultReader.class);
	// default encoding for input files
	public static final String DEFAULT_CHARSET = "UTF-8";
	private String encoding = DEFAULT_CHARSET;
	private Resource resource;

	private RecordSeparatorPolicy recordSeparatorPolicy;
	private String[] comments;
	private int linesToSkip = 0;
	private String headerLineSuffix = "HEADER";
	private String trailerLineSuffix = "TRAILER";
	private HeaderLineProcessor headerLineProcessor;
	private TrailerLineProcessor trailerLineProcessor;
	private LineTokenizer tokenizer = new DelimitedLineTokenizer();
	private FieldSetMapper fieldSetMapper;
	private Validator validator;
	private RecordBean recordBean;
	private LineReader reader;

	public TxtDefaultReader() {
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
//		System.out.println("TxtDefaultReader::::Open" + this);
		this.linesToSkip = Integer.parseInt(ExecutionContextHelper.getString(executionContext,Constants.LINES_TO_SKIP));
		recordBean = new RecordBean();
		RecordBean.setCurRecordBean(ExecutionContextHelper.getString(executionContext,Constants.RECORD_BEAN_UUID), this.recordBean);
		
		constructFieldSetMapperClass(executionContext);
		Assert.notNull(fieldSetMapper, "FieldSetMapper must not be null.");
		super.open(executionContext);
		
	}

	private void constructFieldSetMapperClass(ExecutionContext executionContext) {
		String fieldSetMapperClassName = ExecutionContextHelper.getString(executionContext,Constants.FIELD_SET_MAPPER);
		if (StringUtils.isEmpty(fieldSetMapperClassName)) {
			return;
		}
		try {
			Class clz = Class.forName(fieldSetMapperClassName);
			// FieldSetMapper fieldSetMapper = clz.newInstance();
			Object obj;
			obj = clz.newInstance();
			if (obj instanceof FieldSetMapper) {
				fieldSetMapper = (FieldSetMapper) obj;
			}
			if(fieldSetMapper instanceof AbstractFieldSetMapper){
				AbstractFieldSetMapper afs = (AbstractFieldSetMapper)fieldSetMapper;
				afs.setJobCode(ExecutionContextHelper.getString(executionContext,Constants.JOB_CODE));
			}
		} catch (Exception e) {
			log.error("Cannot instantiate the field set mapper \"" + fieldSetMapperClassName + "\", Please check carefully!",e);
		}
	}

	protected void doOpen() throws Exception {
		// badLogger = new DataAdaptorBadLogger(badFilePath);
		// appLogger = new DataAdaptorAppLogger(logFilePath);

		Assert.notNull(resource, "Input Resource must not be null");
		Assert.state(resource.exists(), "Resource must exist: [" + resource + "]");

		if (this.reader == null) {
			ResourceLineReader reader = new ResourceLineReader(resource, encoding);
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
			// if it is the blank line or just a 'enter' character

			if (line != null) {
				recordBean.increaseTotalRecord();
				if (StringUtils.isBlank(line.trim())) {
					recordBean.increaseBlankRecord();
					continue;
				}

				int lineCount = getReader().getPosition();
				try {
					if (isHeaderLine(line)) {
						processHeaderLine(line);
						continue;
					} else if (isTrailerLine(line)) {
						processTrailerLine(line);
						continue;
					} else {
						FieldSet tokenizedLine = tokenizer.tokenize(line);
						Object dataObj = fieldSetMapper.mapLine(tokenizedLine);
						if (validator != null) {
							validator.validate(dataObj);
						}
						return dataObj;
					}
				} catch (ValidationException ex) {
					recordBean.increaseErrorRecord();
					String errorMsgs = ReaderHelper.getErrorMsg(ex);
					recordBean.logBadRecord("line " + lineCount + " :\r\n Error Messages : \r\n" + errorMsgs + "\r\n Data :" + line + "\r\n");
				} catch (RuntimeException ex) {
					// add current line count to message and re-throw
					recordBean.increaseErrorRecord();
					recordBean.logBadRecord("line " + lineCount + " :\r\n Error Messages : \r\n" + ex.getMessage() + "\r\n Data :" + line + "\r\n");
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
				log.error("error occured when process " + line,e);
			}
		}
	}

	private void processHeaderLine(String line) {
		if (this.headerLineProcessor != null) {
			try {
				headerLineProcessor.process(line);
			} catch (Exception e) {
				log.error("error occured when process " + line,e);
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
		return line.startsWith(trailerLineSuffix);
	}

	private boolean isHeaderLine(String line) {
		if (StringUtils.isEmpty(headerLineSuffix)) {
			return false;
		}
		if (StringUtils.isEmpty(line)) {
			return false;
		}
		return line.startsWith(headerLineSuffix);
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

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}
