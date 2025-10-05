package com.dummy.wpb.product.listener;

import com.dummy.wpb.product.configuration.SystemDefaultValuesHolder;
import com.dummy.wpb.product.configuration.SystemUpdateConfigHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class SystemCdeCheckListener extends JobExecutionListenerSupport {

    private SystemUpdateConfigHolder systemUpdateConfigHolder;

    private SystemDefaultValuesHolder systemDefaultValuesHolder;

    @Override
    public void beforeJob(JobExecution jobExecution) {

        String systemCde = jobExecution.getJobParameters().getString("systemCde");
        String jobName = jobExecution.getJobInstance().getJobName();
        if ((null == systemUpdateConfigHolder || !systemUpdateConfigHolder.isSupport(systemCde))
                && (null == systemDefaultValuesHolder || !systemDefaultValuesHolder.isSupport(systemCde))) {
            log.error("The {} does not support system code: {}. Please check the configuration.", jobName, systemCde);
            jobExecution.setStatus(BatchStatus.STOPPING);
        }
    }

    @Autowired(required = false)
    public void setSystemUpdateConfigHolder(SystemUpdateConfigHolder systemUpdateConfigHolder) {
        this.systemUpdateConfigHolder = systemUpdateConfigHolder;
    }

    @Autowired(required = false)
    public void setSystemDefaultValuesHolder(SystemDefaultValuesHolder systemDefaultValuesHolder) {
        this.systemDefaultValuesHolder = systemDefaultValuesHolder;
    }
}
