package com.dummy.wpc.datadaptor.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;

public abstract class AbstractFieldSetMapper implements FieldSetMapper {
	protected String jobCode;

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}
	
}
