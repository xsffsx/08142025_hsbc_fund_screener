package com.dummy.wpb.product.bond.highestyieldind;

import com.dummy.wpb.product.FieldCalculationJobConfiguration;
import com.dummy.wpb.product.condition.ConditionalOnCalculatedField;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnCalculatedField("bondHighestYieldInd")
public class BondHighestYieldIndConfiguration extends FieldCalculationJobConfiguration {
    @Bean
    public Step bondHighestYieldIndCleanOldSortStep() {
        return createTaskletStep(bondHighestYieldIndCleanOldSortTasklet(), "bondHighestYieldIndCleanOldSortStep");
    }

    @Bean
    public Tasklet bondHighestYieldIndCleanOldSortTasklet() {
        return new BondHighestYieldIndCleanOldSortTasklet();
    }

    @Bean
    public Step bondHighestYieldIndUpdateStep() {
        return createTaskletStep(bondHighestYieldIndUpdateTasklet(), "bondHighestYieldIndUpdateStep");
    }

    @Bean
    public Tasklet bondHighestYieldIndUpdateTasklet() {
        return new BondHighestYieldIndUpdateTasklet();
    }

    @Override
    protected String getJobName() {
        return "Internal Calculate BOND Highest Yield Job";
    }
}
