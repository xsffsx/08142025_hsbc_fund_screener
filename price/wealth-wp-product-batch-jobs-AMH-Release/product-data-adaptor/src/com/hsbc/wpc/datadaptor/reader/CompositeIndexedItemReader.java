package com.dummy.wpc.datadaptor.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.MarkFailedException;
import org.springframework.batch.item.NoWorkFoundException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.ResetFailedException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.batch.item.xml.EventReaderDeserializer;
import org.springframework.batch.item.xml.oxm.UnmarshallingEventReaderDeserializer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.GenericMarshallingFailureException;

import com.dummy.wpc.datadaptor.bean.RecordBean;
import com.dummy.wpc.datadaptor.beanconverter.BeanConverter;
import com.dummy.wpc.datadaptor.beanconverter.DefaultBeanConverter;
import com.dummy.wpc.datadaptor.mapper.MultiWriterObj;
import com.dummy.wpc.datadaptor.util.Constants;
import com.dummy.wpc.datadaptor.util.ExecutionContextHelper;

public class CompositeIndexedItemReader implements ItemReader, ItemStream ,InitializingBean,XMLItemReader{
	
	private int readerCount = 0;
	
	private String itemReaderClass;
	
	private List<XMLItemReader> delegates;
	
	private BeanConverter beanConverter;
	
	private RecordBean recordBean;
	
	private Validator validator;
	
	public int getReaderCount() {
		return readerCount;
	}

	public void setReaderCount(int readerCount) {
		this.readerCount = readerCount;
	}
	
	

	public void setItemReaderClass(String itemReaderClass) {
		this.itemReaderClass = itemReaderClass;
	}

	public void mark() throws MarkFailedException {
		for(XMLItemReader reader : delegates){
			reader.mark();
		}
	}

	public Object read() throws Exception, UnexpectedInputException, NoWorkFoundException, ParseException {
		
		
//		return obj;
		
		do {
			Object result = null;
			MultiWriterObj multiObj = new MultiWriterObj();
			try {
				for(XMLItemReader reader : delegates){
					multiObj.addObj(reader.read());
				}
				
				if(multiObj.areAllObjectsNull()){
					return null;
				}
				
				recordBean.increaseTotalRecord();
				result = beanConverter.convert(multiObj);
				if (this.validator != null) {
					validator.validate(result);
				}
				return result;
			} catch (ValidationException ex) {
				recordBean.increaseErrorRecord();
				String errorMsgs = ReaderHelper.getErrorMsg(ex);
				recordBean.logBadRecord("Element " + recordBean.getTotalRecord() + " :\r\n Error Messages : \r\n" + errorMsgs + "\r\n Key Data : "
						+ ErrorObjFactory.toString(result) + "\r\n");
			} catch (RuntimeException ex) {
				recordBean.increaseTotalRecord();
				recordBean.increaseErrorRecord();
				recordBean.logBadRecord("Element " + recordBean.getTotalRecord() + " :\r\n Error Messages : \r\n" + ex.getMessage() + "\r\n Key Data : "
						+ ErrorObjFactory.toString(result) + "\r\n");
				return null;
			}
			
//			catch (GenericMarshallingFailureException gmfe) {
//				recordBean.increaseTotalRecord();
//				recordBean.increaseErrorRecord();
//				recordBean.logBadRecord("Element " + recordBean.getTotalRecord() + " :\r\n Error Messages : \r\n" + gmfe.getMessage() + "\r\n Key Data : "
//						+ ErrorObjFactory.toString(result) + "\r\n");
//				return null;
//			} catch (RuntimeException ex) {
//				// add current line count to message and re-throw
//				recordBean.increaseErrorRecord();
//				recordBean.logBadRecord("Element " + recordBean.getTotalRecord() + " :\r\n Error Messages : \r\n" + ex.getMessage() + "\r\n Key Data : "
//						+ ErrorObjFactory.toString(result) + "\r\n");
//			}

		} while (true);
	}

	public void reset() throws ResetFailedException {
		for(XMLItemReader reader : delegates){
			reader.reset();
		}
	}

	public void close(ExecutionContext executionContext) throws ItemStreamException {
		for(XMLItemReader reader : delegates){
			reader.close(executionContext);
		}
		
	}
	
	private void init(ExecutionContext executionContext) {
		this.readerCount = Integer.parseInt(ExecutionContextHelper.getString(executionContext,Constants.READER_COUNT));
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
		
		executionContext.putString(Constants.INIT_PROPERTIES_IN_SUPER_CLASS, "true");
	}
	
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		init(executionContext);
		if (itemReaderClass == null && delegates == null) {
			throw new IllegalArgumentException("Please indicate itemReaderClass or delegates property!");
		} else if (itemReaderClass != null && delegates == null) {
			delegates = new ArrayList<XMLItemReader>();
			for (int i = 0; i < this.readerCount; i++) {
				XMLItemReader reader = constructItemReader();
				delegates.add(reader);
			}
		}
		if (delegates.size() != readerCount) {
			throw new IllegalArgumentException("Please ensure the delegates size equal to readerCount!");
		}
		
		
		try {
			initProperties4ReadersFromConfigMap(ExecutionContextHelper.copyProperties(executionContext));
		} catch (ClassNotFoundException e) {
			throw new ItemStreamException(e);
		} catch (IOException e) {
			throw new ItemStreamException(e);
		}
		for(XMLItemReader reader : delegates){
			reader.open(executionContext);
		}
		
		recordBean = new RecordBean();
		RecordBean.setCurRecordBean(ExecutionContextHelper.getString(executionContext,Constants.RECORD_BEAN_UUID),this.recordBean);
		
	}

	public void update(ExecutionContext executionContext) throws ItemStreamException {
		for(XMLItemReader reader : delegates){
			reader.update(executionContext);
		}
	}

	public void afterPropertiesSet() throws Exception {
		
		
	}
	
	private XMLItemReader constructItemReader() {
		if (StringUtils.isEmpty(this.itemReaderClass)) {
			throw new IllegalArgumentException("Please indicate the itemReaderClass!");
		}
		Object obj = null;
		try {
			Class clz = Class.forName(itemReaderClass);
			obj = clz.newInstance();

		} catch (Exception e) {
			throw new IllegalArgumentException("Construct Item Reader failed!");
		}
		if (!(obj instanceof XMLItemReader)) {
			throw new IllegalArgumentException("The itemReaderClass MUST can be instance of com.dummy.wpc.datadaptor.reader.XMLItemReader!");
		}
		XMLItemReader reader = (XMLItemReader) obj;
		reader.setThrowErrWhenReadFailed(true);
//		reader.setResource(resource);
		
		/**
		
		<property name="beanConverter">
			<bean class="${bean_converter}" />
		</property>
		<property name="validator" ref="defaultValidator" />
		<!-- <property name="recordBean" ref="recordBean" /> -->
		
		 */

		return reader;
	}
	private UnmarshallingEventReaderDeserializer constructSerialzier(String xmlItemBean) throws ClassNotFoundException, IOException {
		org.springframework.oxm.castor.CastorMarshaller marshaller = new org.springframework.oxm.castor.CastorMarshaller();
		marshaller.setTargetClass(Class.forName(xmlItemBean));
		marshaller.afterPropertiesSet();
		org.springframework.batch.item.xml.oxm.UnmarshallingEventReaderDeserializer serializer = new org.springframework.batch.item.xml.oxm.UnmarshallingEventReaderDeserializer(marshaller);
		
		return serializer;
	}
	public void initProperties4ReadersFromConfigMap(Map configMap) throws ClassNotFoundException, IOException {
//		Map<String, String> configMap = Env.configMap;
		if (configMap != null) {
			
			for (int i = 1; i <= readerCount; i++) {
				XMLItemReader reader = delegates.get(i - 1);
				
				String keyPrefix = "reader." + i + ".";
				String key =  keyPrefix + Constants.DATA_FILE_PATH;
				String dataFilePath = (String)configMap.get(key);

				if (StringUtils.isEmpty(dataFilePath)) {
					throw new RuntimeException("Cannot find this property: " + key);
				}
				
				reader.setResource(new FileSystemResource(dataFilePath));

				key = keyPrefix + Constants.FRAGMENT_ROOT_ELEMENT_NAME;
				String fragmentRootElementName = (String)configMap.get(key);
				if (StringUtils.isEmpty(fragmentRootElementName)) {
					throw new RuntimeException("Cannot find this property: " + key);
				}
				reader.setFragmentRootElementName(fragmentRootElementName);
				
				key = keyPrefix + Constants.XML_ITEM_BEAN;
				String xmlItemBean = (String)configMap.get(key);
				if(StringUtils.isEmpty(xmlItemBean)){
					throw new RuntimeException("Cannot find this property: " + key);
				}
				reader.setFragmentDeserializer(constructSerialzier(xmlItemBean));
				
				reader.setBeanConverter(new DefaultBeanConverter());
			}
		}
	}

	public void setResource(Resource resource) {
		throw new java.lang.UnsupportedOperationException();
	}
	
	public void setFragmentRootElementName(String fragmentRootElementName){
		throw new java.lang.UnsupportedOperationException();
	}
	
	public void setFragmentDeserializer(EventReaderDeserializer eventReaderDeserializer) {
		throw new java.lang.UnsupportedOperationException();
	}
	
	
	public BeanConverter getBeanConverter() {
		return beanConverter;
	}
	
	public void setBeanConverter(BeanConverter beanConverter) {
		this.beanConverter = beanConverter;
	}
	
	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	public void setThrowErrWhenReadFailed(boolean throwErrWhenReadFailed) {
		throw new java.lang.UnsupportedOperationException();
	}
}
