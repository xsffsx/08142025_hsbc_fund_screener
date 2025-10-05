package com.dummy.wpb.product;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

public abstract class FieldCalculationJobConfiguration {

    protected JobRepository jobRepository;

    protected PlatformTransactionManager transactionManager;

    @Bean
    public Job fieldCalculationJob(List<Step> stepList) {
        SimpleJob simpleJob = new SimpleJob();
        simpleJob.setJobRepository(jobRepository);
        simpleJob.setName(getJobName());
        simpleJob.setJobParametersIncrementer(new RunIdIncrementer());
        simpleJob.setSteps(stepList);
        return simpleJob;
    }

    protected Step createTaskletStep(Tasklet tasklet, String stepName) {
        TaskletStep taskletStep = new TaskletStep(stepName);
        taskletStep.setJobRepository(jobRepository);
        taskletStep.setTransactionManager(transactionManager);
        taskletStep.setTasklet(tasklet);
        return taskletStep;
    }

    @Autowired
    public void setJobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Autowired
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    protected abstract String getJobName();
}
