package com.dummy.wpc.datadaptor.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

public class JobListener extends JobExecutionListenerSupport {

	@Override
	public void afterJob(JobExecution jobExecution) {
		super.afterJob(jobExecution);
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		super.beforeJob(jobExecution);
	}

	@Override
	public void onError(JobExecution jobExecution, Throwable e) {
		super.onError(jobExecution, e);
	}

	@Override
	public void onInterrupt(JobExecution jobExecution) {
		super.onInterrupt(jobExecution);
	}
}
