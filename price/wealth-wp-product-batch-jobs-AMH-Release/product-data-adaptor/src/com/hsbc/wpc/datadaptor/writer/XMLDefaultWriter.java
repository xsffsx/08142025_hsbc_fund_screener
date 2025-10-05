package com.dummy.wpc.datadaptor.writer;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.xml.StaxEventItemWriter;

import com.dummy.wpc.datadaptor.util.Constants;
import com.dummy.wpc.datadaptor.util.ExecutionContextHelper;

public class XMLDefaultWriter extends StaxEventItemWriter implements XMLItemWriter{
	@Override
	public void open(ExecutionContext executionContext) {
		String rootTagName = ExecutionContextHelper.getString(executionContext,Constants.XML_ROOT_TAG_NAME);
		if(StringUtils.isNotEmpty(rootTagName)){
			this.setRootTagName(rootTagName);
		}
		
		super.open(executionContext);
		
	}

	public void close(ExecutionContext executionContext) {
		try {
			super.close(executionContext);
		} catch (NullPointerException e) {
			return;
		}
	}
}