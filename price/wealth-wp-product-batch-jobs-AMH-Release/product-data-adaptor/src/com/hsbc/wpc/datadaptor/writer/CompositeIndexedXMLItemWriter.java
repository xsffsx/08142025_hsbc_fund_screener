package com.dummy.wpc.datadaptor.writer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.ClearFailedException;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.FlushFailedException;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.EventWriterSerializer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.dummy.wpc.datadaptor.mapper.MultiWriterObj;
import com.dummy.wpc.datadaptor.util.Constants;
import com.dummy.wpc.datadaptor.util.ExecutionContextHelper;

public class CompositeIndexedXMLItemWriter implements ItemWriter, ItemStream, InitializingBean,XMLItemWriter {

	private int writerCount;

	private String itemWriterClass;

	private List<XMLItemWriter> delegates;

	private EventWriterSerializer serializer;

	private boolean overwriteOutput;

	public void setOverwriteOutput(boolean overwriteOutput) {
		this.overwriteOutput = overwriteOutput;
	}

	/**
	 * Set Object to XML serializer.
	 * 
	 * @param serializer
	 *            the Object to XML serializer
	 */
	public void setSerializer(EventWriterSerializer serializer) {
		this.serializer = serializer;
	}

	public void setDelegates(List<XMLItemWriter> delegates) {
		this.delegates = delegates;
		if (delegates != null) {
			this.writerCount = delegates.size();
		}
	}

	public List<XMLItemWriter> getDelegates() {
		return delegates;
	}

	public int getWriterCount() {
		return writerCount;
	}

	public void setWriterCount(int writerCount) {
		this.writerCount = writerCount;
	}

	public void setItemWriterClass(String itemWriterClass) {
		this.itemWriterClass = itemWriterClass;
	}

	/**
	 * Calls injected ItemProcessors in order.
	 */
	public void write(Object data) throws Exception {
	    if (data == null) {
            return;
        }
        if (delegates == null) {
            return;
        }
        
		if (!(data instanceof MultiWriterObj)) {
			throw new IllegalArgumentException("The data should be an instance of class com.dummy.wpc.datadaptor.mapper.Array!");
		}
		
		Object[] dataArr = (Object[]) ((MultiWriterObj) data).getArray();
		if (dataArr.length != delegates.size()) {
			throw new IllegalArgumentException("The data array length must be equal to delegates size!");
		}
		for (int i = 0; i < delegates.size(); i++) {
			ItemWriter writer = (ItemWriter) delegates.get(i);
			if (writer != null && dataArr[i] != null) {
				writer.write(dataArr[i]);
			}
		}
	}

	public void clear() throws ClearFailedException {
		for (Iterator<XMLItemWriter> iterator = delegates.listIterator(); iterator.hasNext();) {
			iterator.next().clear();
		}
	}

	public void flush() throws FlushFailedException {
		for (Iterator<XMLItemWriter> iterator = delegates.listIterator(); iterator.hasNext();) {
			iterator.next().flush();
		}
	}

	public void afterPropertiesSet() throws Exception {

		
	}

	private XMLItemWriter constructItemWriter() {
		if (StringUtils.isEmpty(this.itemWriterClass)) {
			throw new IllegalArgumentException("Please indicate the itemriterClass!");
		}
		Object obj = null;
		try {
			Class clz = Class.forName(itemWriterClass);
			obj = clz.newInstance();

		} catch (Exception e) {
			throw new IllegalArgumentException("Construct Item writer failed!");
		}
		if (!(obj instanceof XMLItemWriter)) {
			throw new IllegalArgumentException("The itemWriterClass MUST can be instance of com.dummy.wpc.datadaptor.writer.XMLItemWriter!");
		}
		XMLItemWriter writer = (XMLItemWriter) obj;
		writer.setOverwriteOutput(this.overwriteOutput);
		writer.setSerializer(serializer);

		return writer;
	}



	public void initProperties4WritersFromConfigMap(Map configMap) {

		if (configMap != null) {
			for (int i = 1; i <= writerCount; i++) {
				String key = "writer." + i + "." + Constants.OUTPUT_FILE_PATH;
				String outputFilePath = (String)configMap.get(key);

				if (StringUtils.isEmpty(outputFilePath)) {
					throw new RuntimeException("Cannot find this property: " + key);
				}
				XMLItemWriter writer = delegates.get(i - 1);
				writer.setResource(new FileSystemResource(outputFilePath));

				key = "writer." + i + "." + Constants.XML_ROOT_TAG_NAME;
				String rootTagName = (String)configMap.get(key);
				if (!StringUtils.isEmpty(rootTagName)) {
					writer.setRootTagName(rootTagName);
				}
			}
		}
	}

	public void close(ExecutionContext executionContext) throws ItemStreamException {
		if (itemWriterClass == null && delegates == null) {
			throw new IllegalArgumentException("Please indicate itemriterClass or delegates property!");
		} else if (itemWriterClass != null && delegates == null) {
			delegates = new ArrayList<XMLItemWriter>();
				XMLItemWriter writer = constructItemWriter();
				delegates.add(writer);			
		}
		
		
		for (Iterator<XMLItemWriter> iterator = delegates.listIterator(); iterator.hasNext();) {
			iterator.next().close(executionContext);
		}
	}

	public void open(ExecutionContext executionContext) throws ItemStreamException {
		this.writerCount = Integer.parseInt(ExecutionContextHelper.getString(executionContext,Constants.WRITER_COUNT));
		if (itemWriterClass == null && delegates == null) {
			throw new IllegalArgumentException("Please indicate itemriterClass or delegates property!");
		} else if (itemWriterClass != null && delegates == null) {
			delegates = new ArrayList<XMLItemWriter>();
			for (int i = 0; i < this.writerCount; i++) {
				XMLItemWriter writer = constructItemWriter();
				delegates.add(writer);
			}
		}
		if (delegates.size() != writerCount) {
			throw new IllegalArgumentException("Please ensure the delegates size equal to writerCount!");
		}
		
		initProperties4WritersFromConfigMap(ExecutionContextHelper.copyProperties(executionContext));
		for (Iterator<XMLItemWriter> iterator = delegates.listIterator(); iterator.hasNext();) {
			iterator.next().open(executionContext);
		}
	}

	public void update(ExecutionContext executionContext) throws ItemStreamException {
		for (Iterator<XMLItemWriter> iterator = delegates.listIterator(); iterator.hasNext();) {
			iterator.next().update(executionContext);
		}
	}

	public void setResource(Resource resource) {
		throw new java.lang.UnsupportedOperationException();
	}

	public void setRootTagName(String rootTagName) {
		throw new java.lang.UnsupportedOperationException();
	}
}
