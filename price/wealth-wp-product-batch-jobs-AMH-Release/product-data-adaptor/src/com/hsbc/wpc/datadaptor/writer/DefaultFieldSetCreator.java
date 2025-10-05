package com.dummy.wpc.datadaptor.writer;

import org.springframework.batch.item.file.mapping.DefaultFieldSet;
import org.springframework.batch.item.file.mapping.FieldSet;
import org.springframework.batch.item.file.mapping.FieldSetCreator;

public class DefaultFieldSetCreator implements FieldSetCreator {
//	private String fieldNames = "";
//	private DefaultFieldSet defaultFieldSet = null;
//	
//	public String getFieldNames() {
//		return fieldNames;
//	}
//
//	public void setFieldNames(String fieldNames) {
//		this.fieldNames = fieldNames;
//		if(fieldNames == null){
//			fieldNames = "";
//		}
//		String[] tokens = fieldNames.split(",");
//		if(tokens == null){
//			tokens = new String[0];
//		}
//		defaultFieldSet = new DefaultFieldSet(tokens);
//	}

	public FieldSet mapItem(Object data) {
		if (data instanceof FieldSet) {
			return (FieldSet) data;
		}
		if (!(data instanceof String)) {
			data = "" + data;
		}
		return new DefaultFieldSet(new String[] { (String) data });
	}

}
