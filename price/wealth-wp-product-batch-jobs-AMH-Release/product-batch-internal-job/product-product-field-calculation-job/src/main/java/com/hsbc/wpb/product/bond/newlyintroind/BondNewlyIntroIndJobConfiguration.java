package com.dummy.wpb.product.bond.newlyintroind;

import com.dummy.wpb.product.FieldCalculationJobConfiguration;
import com.dummy.wpb.product.condition.ConditionalOnCalculatedField;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnCalculatedField("bondNewlyIntroducedInd")
public class BondNewlyIntroIndJobConfiguration extends FieldCalculationJobConfiguration {

    @Bean
    public Step bondNewlyIntroIndCleanOldSortStep() {
        return createTaskletStep(bondNewlyIntroIndCleanOldSortTasklet(),"bondNewlyIntroIndCleanOldSortStep");
    }

    @Bean
    public Tasklet bondNewlyIntroIndCleanOldSortTasklet() {
        return new BondNewlyIntroIndCleanOldSortTasklet();
    }

    @Bean
    public Step newlyIntroIndUpdateStep() {
        return createTaskletStep(bondNewlyIntroIndUpdateTasklet(),"bondNewlyIntroIndUpdateStep");
    }

    @Bean
    public Tasklet bondNewlyIntroIndUpdateTasklet() {
        return new BondNewlyIntroIndUpdateTasklet();
    }

    @Override
    protected String getJobName() {
        return "Internal Calculate BOND Newly Introduced Job";
    }
}
