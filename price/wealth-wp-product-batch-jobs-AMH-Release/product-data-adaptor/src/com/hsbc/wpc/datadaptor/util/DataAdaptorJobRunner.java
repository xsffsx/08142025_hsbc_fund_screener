package com.dummy.wpc.datadaptor.util;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.repository.NoSuchJobException;

import com.dummy.esf.batch.JobIdentifier;
import com.dummy.esf.batch.JobResult;
import com.dummy.esf.batch.launch.impl.DefaultJobRunner;

public class DataAdaptorJobRunner extends DefaultJobRunner {
	/**
	 * job launcher.
	 */
	private JobLauncher jobLauncher = null;

	/**
	 * job locator.
	 */
	private JobLocator jobLocator = null;

	/**
	 * job parameters converter.
	 */
	private JobParametersConverter jobParametersConverter;

	public JobParametersConverter getJobParametersConverter() {
		return jobParametersConverter;
	}

	public void setJobParametersConverter(JobParametersConverter jobParametersConverter) {
		this.jobParametersConverter = jobParametersConverter;
	}

	/**
	 * Runs a job.
	 * 
	 * @param jobIdentifier
	 *            - job identifier.
	 * @return {@link JobResult}
	 * @throws JobExecutionAlreadyRunningException
	 *             - if the desired job already has an execution running.
	 * 
	 * @throws JobRestartException
	 *             - if the job has been run before and circumstances that
	 *             preclude a re-start.
	 * 
	 * @throws JobInstanceAlreadyCompleteException
	 *             - if the job has been run before with the same parameters and
	 *             completed successfully.
	 * 
	 * @throws NoSuchJobException
	 *             - if the desired job is not configured.
	 * 
	 */
	public JobResult run(final JobIdentifier jobIdentifier) throws JobExecutionAlreadyRunningException,
			JobRestartException, JobInstanceAlreadyCompleteException, NoSuchJobException {

		Job job = jobLocator.getJob(jobIdentifier.getJobName());
		JobExecution jobExecution;
		JobParameters jobParameters = jobParametersConverter.getJobParameters(jobIdentifier.getParameters());
		jobExecution = jobLauncher.run(job, jobParameters);
		JobResult result = JobResult.populate(jobExecution);
		return result;
	}

	/**
	 * Get the jobLauncher value.
	 * 
	 * @return the jobLauncher
	 */
	public JobLauncher getJobLauncher() {
		return this.jobLauncher;
	}

	/**
	 * Set the jobLauncher value.
	 * 
	 * @param pJobLauncher
	 *            the jobLauncher to set
	 */
	public void setJobLauncher(final JobLauncher pJobLauncher) {
		this.jobLauncher = pJobLauncher;
	}

	/**
	 * Get the jobLocator value.
	 * 
	 * @return the jobLocator
	 */
	public JobLocator getJobLocator() {
		return this.jobLocator;
	}

	/**
	 * Set the jobLocator value.
	 * 
	 * @param pJobLocator
	 *            the jobLocator to set
	 */
	public void setJobLocator(final JobLocator pJobLocator) {
		this.jobLocator = pJobLocator;
	}
}
