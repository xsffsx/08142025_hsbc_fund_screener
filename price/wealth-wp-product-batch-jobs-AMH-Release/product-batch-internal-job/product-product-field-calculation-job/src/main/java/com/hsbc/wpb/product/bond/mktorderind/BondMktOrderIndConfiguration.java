package com.dummy.wpb.product.bond.mktorderind;

import com.dummy.wpb.product.FieldCalculationJobConfiguration;
import com.dummy.wpb.product.condition.ConditionalOnCalculatedField;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnCalculatedField("bondMktOrderInd")
public class BondMktOrderIndConfiguration extends FieldCalculationJobConfiguration {
    @Bean
    public Step bondMktOrderIndUpdateStep() {
        return createTaskletStep(bondMktOrderIndCalculationTasklet(), "bondMktOrderIndCalculationStep");
    }

    @Bean
    public Tasklet bondMktOrderIndCalculationTasklet() {
        return new BondMktOrderIndCalculationTasklet();
    }

    @Override
    protected String getJobName() {
        return "Internal Calculate Market Order Indicator (BOND) Job";
    }
}
