package com.dummy.wpc.datadaptor.writer;

import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.EventWriterSerializer;
import  org.springframework.core.io.Resource;

public interface XMLItemWriter extends ItemWriter, ItemStream {
	public void setRootTagName(String rootTagName);
	public void setResource(Resource resource);
	public void setSerializer(EventWriterSerializer serializer);
	public void setOverwriteOutput(boolean overwriteOutput);
}
