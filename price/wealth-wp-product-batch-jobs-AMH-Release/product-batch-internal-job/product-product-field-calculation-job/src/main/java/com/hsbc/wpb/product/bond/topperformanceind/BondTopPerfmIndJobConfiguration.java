package com.dummy.wpb.product.bond.topperformanceind;

import com.dummy.wpb.product.FieldCalculationJobConfiguration;
import com.dummy.wpb.product.condition.ConditionalOnCalculatedField;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnCalculatedField("bondTopPerformanceInd")
public class BondTopPerfmIndJobConfiguration extends FieldCalculationJobConfiguration {

    protected String jobName = "Internal Calculate BOND Top Performance Job";

    @Bean
    public Step bondTopPerfmIndCleanOldSortStep() {
        return createTaskletStep(bondTopPerfmCleanOldSortTasklet(), "bondTopPerfmIndCleanOldSortStep");
    }

    @Bean
    public Tasklet bondTopPerfmCleanOldSortTasklet() {
        return new BondTopPerfmIndCleanOldSortTasklet();
    }

    @Bean
    public Step bondTopPerfmIndUpdateStep() {
        return createTaskletStep(bondTopPerfmUpdateTasklet(), "bondTopPerfmIndUpdateStep");
    }

    @Bean
    public Tasklet bondTopPerfmUpdateTasklet() {
        return new BondTopPerfmIndUpdateTasklet();
    }

    @Override
    protected String getJobName() {
        return jobName;
    }
}
