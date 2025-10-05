package com.dummy.wpc.datadaptor.reader;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.NoWorkFoundException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.oxm.GenericMarshallingFailureException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.dummy.wpc.datadaptor.bean.RecordBean;
import com.dummy.wpc.datadaptor.beanconverter.AbstractBeanConverter;
import com.dummy.wpc.datadaptor.beanconverter.BeanConverter;
import com.dummy.wpc.datadaptor.beanconverter.DefaultBeanConverter;
import com.dummy.wpc.datadaptor.util.Constants;
import com.dummy.wpc.datadaptor.util.ExecutionContextHelper;

public class XMLDefaultReader extends StaxEventItemReader implements XMLItemReader {

	private Validator validator;

	private RecordBean recordBean;

	private BeanConverter beanConverter;
	
	private boolean throwErrWhenReadFailed = false;

	public XMLDefaultReader() {
		setName(ClassUtils.getShortName(XMLDefaultReader.class));
	}

	
	public void afterPropertiesSet() throws Exception {
	}
	
	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		init(executionContext);
		
		super.open(executionContext);
		recordBean = new RecordBean();
		RecordBean.setCurRecordBean(ExecutionContextHelper.getString(executionContext,Constants.RECORD_BEAN_UUID),this.recordBean);
		if(beanConverter instanceof AbstractBeanConverter){
			AbstractBeanConverter abc = (AbstractBeanConverter)beanConverter;
			abc.setJobCode(ExecutionContextHelper.getString(executionContext,Constants.JOB_CODE));
		}
	}

	private void init(ExecutionContext executionContext) {
		
		
		String initInSuperClass = ExecutionContextHelper.getString(executionContext,Constants.INIT_PROPERTIES_IN_SUPER_CLASS);
		if(StringUtils.isNotEmpty(initInSuperClass)){
			this.beanConverter = new DefaultBeanConverter();
			return;
		}
		initFragmentDeserializer(executionContext);
		
		String beanConverterStr = ExecutionContextHelper.getString(executionContext,Constants.BEAN_CONVERTER);
		if(StringUtils.isNotEmpty(beanConverterStr)){
			try {
				Class clz = Class.forName(beanConverterStr);
				beanConverter = (BeanConverter) clz.newInstance();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
		}
		
		String fragRootElName = ExecutionContextHelper.getString(executionContext,Constants.FRAGMENT_ROOT_ELEMENT_NAME);
		if(StringUtils.isNotEmpty(fragRootElName)){
			this.setFragmentRootElementName(fragRootElName);
		}
		
		
		
	}

	@Override
	public void close(ExecutionContext executionContext) throws ItemStreamException {
		super.close(executionContext);
	}

	@Override
	public Object read() throws Exception, UnexpectedInputException, NoWorkFoundException, ParseException {
		do {
			Object item = null;
			try {
				// if read failed a GenericMarshallingFailureException will be
				// thrown, then return null
				item = super.read();
				if (item == null) {
					return null;
				}
				recordBean.increaseTotalRecord();
				item = beanConverter.convert(item);
				if (this.validator != null) {
					validator.validate(item);
				}
				return item;
			} catch (ValidationException ex) {
				if(throwErrWhenReadFailed){
					throw ex;
				}
				recordBean.increaseErrorRecord();
				String errorMsgs = ReaderHelper.getErrorMsg(ex);
				recordBean.logBadRecord("Element " + recordBean.getTotalRecord() + " :\r\n Error Messages : \r\n" + errorMsgs + "\r\n Key Data : "
						+ ErrorObjFactory.toString(item) + "\r\n");
			} catch (RuntimeException ex) {
				if(throwErrWhenReadFailed){
					throw ex;
				}
				recordBean.increaseTotalRecord();
				recordBean.increaseErrorRecord();
				recordBean.logBadRecord("Element " + recordBean.getTotalRecord() + " :\r\n Error Messages : \r\n" + ex.getMessage() + "\r\n Key Data : "
						+ ErrorObjFactory.toString(item) + "\r\n");
				return null;
			}


		} while (true);
	}

	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	public BeanConverter getBeanConverter() {
		return beanConverter;
	}

	public void setBeanConverter(BeanConverter beanConverter) {
		this.beanConverter = beanConverter;
	}

	public void setThrowErrWhenReadFailed(boolean throwErrWhenReadFailed) {
		this.throwErrWhenReadFailed = throwErrWhenReadFailed;
	}
	
	 private void initFragmentDeserializer(ExecutionContext executionContext) {
		String xmlItemBean = ExecutionContextHelper.getString(executionContext,Constants.XML_ITEM_BEAN);
		Assert.notNull(xmlItemBean,"he property\"" + Constants.XML_ITEM_BEAN + "\" is necessary!");

		Class xmlItemBeanClz;
		try {
			xmlItemBeanClz = Class.forName(xmlItemBean);
			org.springframework.oxm.castor.CastorMarshaller castorMarshaller = new org.springframework.oxm.castor.CastorMarshaller();
			
			castorMarshaller.setTargetClass(xmlItemBeanClz);
			castorMarshaller.afterPropertiesSet();
			org.springframework.batch.item.xml.oxm.UnmarshallingEventReaderDeserializer deserializer = new org.springframework.batch.item.xml.oxm.UnmarshallingEventReaderDeserializer(castorMarshaller);
			this.setFragmentDeserializer(deserializer);
		} catch (ClassNotFoundException e) {
//			log.error(e.getMessage());
			throw new ItemStreamException(e);
		} catch (IOException e) {
			throw new ItemStreamException(e);
		}

	}



}
