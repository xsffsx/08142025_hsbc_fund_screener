package com.dummy.wpb.product.eli.risklvlcde;

import com.dummy.wpb.product.FieldCalculationJobApplication;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonValue;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
@ActiveProfiles("test")
@SpringBootTest(
        classes = FieldCalculationJobApplication.class,
        args = {"ctryRecCde=HK", "grpMembrRecCde=HBAP", "calculatedField=eliRiskLvlCde"})
@RunWith(SpringJUnit4ClassRunner.class)
public class EliRiskLvlCalculationJobTest {

    private Logger logger = LoggerFactory.getLogger(EliRiskLvlCalculationJobTest.class);

    @MockBean
    MongoTemplate mongoTemplate;
    @MockBean
    JobLauncherApplicationRunner applicationRunner;

    @Autowired
    private EliRiskLvlCalculationTasklet eliRiskLvlCalculationTasklet;

    static JobParameters jobParameters;

    @Before
    public void before() {
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .toJobParameters();
    }

    public JobExecution getJobExecution() {
        return MetaDataInstanceFactory.createJobExecution("eliRiskLvlCalculationJob", 1L, 1L,jobParameters);
    }

    public StepExecution getStepExecution() {
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }


    @Test
    public void testEliRiskJob() {

        List<EqtyLinkInvstRiskLevel> mappedResults = new ArrayList<>();
        EqtyLinkInvstRiskLevel level = new EqtyLinkInvstRiskLevel();
        level.setUndlQtyInd("1");
        level.setRiskLvlCde("2");
        level.setUndlRiskLvlCde("1");
        level.setCptlProtcPct(new BigDecimal(1));
        level.setTenor(1L);
        logger.info(level.getUndlQtyInd() + level.getRiskLvlCde() + level.getUndlRiskLvlCde()
                + level.getCptlProtcPct() + level.getTenor());
        mappedResults.add(level);
        AggregationResults<EqtyLinkInvstRiskLevel> aggregationResults = new AggregationResults<>(mappedResults, new Document());
        Mockito.when(mongoTemplate.aggregate(any(Aggregation.class), anyString(), Mockito.<Class<EqtyLinkInvstRiskLevel>>any()))
                .thenReturn(aggregationResults);

        Mockito.when(mongoTemplate.updateFirst(any(Query.class), any(Update.class), anyString()))
                .thenReturn(UpdateResult.acknowledged(anyLong(),anyLong(),any(BsonValue.class)));

        StepContribution stepContribution = new StepContribution(getStepExecution());
        ChunkContext chunkContext = new ChunkContext(new StepContext(getStepExecution()));
        Assertions.assertDoesNotThrow(() -> eliRiskLvlCalculationTasklet.execute(stepContribution,chunkContext));
    }

}
