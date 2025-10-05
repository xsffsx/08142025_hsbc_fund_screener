package com.dummy.wpc.datadaptor.job;

import java.util.Map;

public interface AfterJob {
	public void afterJob(Map<String,String> configMap) throws Exception;
}
