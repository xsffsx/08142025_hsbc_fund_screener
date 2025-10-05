package com.dummy.wpc.datadaptor.beanconverter;

public abstract class AbstractBeanConverter implements BeanConverter {

	private String jobCode;

	public String getJobCode() {
		
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

}
