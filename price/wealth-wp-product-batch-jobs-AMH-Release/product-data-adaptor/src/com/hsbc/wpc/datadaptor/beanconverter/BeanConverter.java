package com.dummy.wpc.datadaptor.beanconverter;

public interface BeanConverter {
	public Object convert(Object source);
	public String getJobCode();
	public void setJobCode(String jobCode);
}
