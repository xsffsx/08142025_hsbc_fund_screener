package com.dummy.wpb.product.bond.newlyissue;

import com.dummy.wpb.product.FieldCalculationJobConfiguration;
import com.dummy.wpb.product.condition.ConditionalOnCalculatedField;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnCalculatedField("bondNewlyIssueInd")
public class BondNewlyIssueIndJobConfiguration extends FieldCalculationJobConfiguration {

    protected String jobName = "Internal Calculate BOND Newly Issue Job";

    @Bean
    public Step bondNewlyIssueIndCleanOldSortStep() {
        return createTaskletStep(bondNewlyIssueIndCleanOldSortTasklet(), "bondNewlyIssueIndCleanOldSortStep");
    }

    @Bean
    public Tasklet bondNewlyIssueIndCleanOldSortTasklet() {
        return new BondNewlyIssueIndCleanOldSortTasklet();
    }

    @Bean
    public Step bondNewlyIssueIndUpdateStep() {
        return createTaskletStep(bondNewlyIssusIndUpdateTasklet(), "bondNewlyIssueIndUpdateStep");
    }

    @Bean
    public Tasklet bondNewlyIssusIndUpdateTasklet() {
        return new BondNewlyIssusIndUpdateTasklet();
    }

    @Override
    protected String getJobName() {
        return jobName;
    }
}
