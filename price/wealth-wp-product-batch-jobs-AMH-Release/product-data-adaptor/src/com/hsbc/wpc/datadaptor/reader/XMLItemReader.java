package com.dummy.wpc.datadaptor.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.xml.EventReaderDeserializer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import com.dummy.wpc.datadaptor.beanconverter.BeanConverter;

public interface XMLItemReader extends ItemReader, ItemStream ,InitializingBean{
	public void setResource(Resource resource);
	public void setFragmentRootElementName(String fragmentRootElementName);
	public void setFragmentDeserializer(EventReaderDeserializer eventReaderDeserializer);
	public void setBeanConverter(BeanConverter beanConverter) ;
	public void setThrowErrWhenReadFailed(boolean throwErrWhenReadFailed);
}
