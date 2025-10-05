package com.dummy.wpc.datadaptor.mapper;

import java.util.Map;

import com.dummy.wpc.datadaptor.reader.Sheet;


public interface RowMapper<T> {
    
    public T mapRow(Sheet sheet, Map<String, String> columnContentMappings) throws Exception;
    
    public void setJobCode(String jobCode);
    
    public String getJobCode();
}
