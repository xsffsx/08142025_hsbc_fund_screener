package com.dummy.wpb.product.listener;

import com.dummy.wpb.product.configuration.SystemDefaultValuesHolder;
import com.dummy.wpb.product.configuration.SystemUpdateConfigHolder;
import com.dummy.wpb.product.model.SystemDefaultValues;
import com.dummy.wpb.product.model.SystemUpdateConfig;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.Collections;


@RunWith(SpringJUnit4ClassRunner.class)
public class SystemCdeCheckListenerTest {

    @Test
    public void testBeforeJob() {
        SystemCdeCheckListener systemCdeCheckListener = new SystemCdeCheckListener();
        SystemUpdateConfigHolder systemUpdateConfigHolder = new SystemUpdateConfigHolder();
        SystemUpdateConfig systemUpdateConfig = new SystemUpdateConfig();
        systemUpdateConfig.setSystemCde("MS");
        systemUpdateConfigHolder.setSystemUpdateConfig(Collections.singletonList(systemUpdateConfig));
        systemCdeCheckListener.setSystemUpdateConfigHolder(systemUpdateConfigHolder);
        SystemDefaultValuesHolder systemDefaultValuesHolder = new SystemDefaultValuesHolder();
        systemDefaultValuesHolder.setSystemDefaultValues(Collections.singletonList(new SystemDefaultValues()));
        systemCdeCheckListener.setSystemDefaultValuesHolder(systemDefaultValuesHolder);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("systemCde", "MDS")
                .toJobParameters();
        JobExecution importGacJsonFileJob = MetaDataInstanceFactory.createJobExecution("importGacJsonFileJob", 1L, 1L, jobParameters);
        systemCdeCheckListener.beforeJob(importGacJsonFileJob);
        Assert.assertEquals(BatchStatus.STOPPING, importGacJsonFileJob.getStatus());
    }

}