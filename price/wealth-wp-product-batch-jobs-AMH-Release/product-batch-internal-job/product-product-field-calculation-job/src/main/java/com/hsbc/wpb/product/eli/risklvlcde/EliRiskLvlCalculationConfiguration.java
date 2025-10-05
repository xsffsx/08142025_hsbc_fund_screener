package com.dummy.wpb.product.eli.risklvlcde;

import com.dummy.wpb.product.FieldCalculationJobConfiguration;
import com.dummy.wpb.product.condition.ConditionalOnCalculatedField;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@ConditionalOnCalculatedField("eliRiskLvlCde")
public class EliRiskLvlCalculationConfiguration extends FieldCalculationJobConfiguration {
    @Bean
    public Step eliRiskLvlCalculationStep(Tasklet eliRiskLvlCalculationTasklet) {
        return createTaskletStep(eliRiskLvlCalculationTasklet, "eliRiskLvlCalculationStep");
    }

    @Bean
    public Tasklet eliRiskLvlCalculationTasklet(MongoTemplate mongoTemplate) {
        return new EliRiskLvlCalculationTasklet(mongoTemplate);
    }

    @Override
    protected String getJobName() {
        return "Calculate Risk Level in day batch (ELI) Job";
    }
}
