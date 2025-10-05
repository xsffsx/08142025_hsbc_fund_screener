package com.dummy.wpb.product.bond.mktorderind;

import com.dummy.wpb.product.FieldCalculationJobApplication;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.ArgumentMatchers.*;

@ActiveProfiles("test")
@SpringBootTest(classes = FieldCalculationJobApplication.class,
        args = {"ctryRecCde=HK", "grpMembrRecCde=HBAP", "calculatedField=bondMktOrderInd"})
@RunWith(SpringJUnit4ClassRunner.class)
public class BondMktOrderIndCalculationJobTest {

    @MockBean
    MongoTemplate mongoTemplate;
    @MockBean
    JobLauncherApplicationRunner applicationRunner;

    @Autowired()
    private BondMktOrderIndCalculationTasklet bondMktOrderIndCalculationTasklet;

    static JobParameters jobParameters;

    @Before
    public void before() {
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .toJobParameters();
    }

    public JobExecution getJobExecution() {
        return MetaDataInstanceFactory.createJobExecution("bondMktOrderIndCalculationJob", 1L, 1L,jobParameters);
    }

    public StepExecution getStepExecution() {
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }


    @Test
    public void testBondMktOrderIndCalculationJob() throws Exception {
        Mockito.when(mongoTemplate.updateMulti(any(Query.class), any(Update.class), anyString()))
                .thenReturn(UpdateResult.acknowledged(anyLong(),anyLong(),any(BsonValue.class)));

        StepContribution stepContribution = new StepContribution(getStepExecution());
        ChunkContext chunkContext = new ChunkContext(new StepContext(getStepExecution()));
        Assertions.assertDoesNotThrow(() -> bondMktOrderIndCalculationTasklet.execute(stepContribution,chunkContext));
    }
}
