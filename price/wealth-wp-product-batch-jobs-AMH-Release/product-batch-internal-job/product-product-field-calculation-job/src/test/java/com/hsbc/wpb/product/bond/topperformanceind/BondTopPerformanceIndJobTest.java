package com.dummy.wpb.product.bond.topperformanceind;

import com.dummy.wpb.product.FieldCalculationJobApplication;
import com.dummy.wpb.product.constant.Field;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonValue;
import org.bson.Document;
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
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@ActiveProfiles("test")
@SpringBootTest(classes = FieldCalculationJobApplication.class,
        args = {"ctryRecCde=HK", "grpMembrRecCde=HBAP", "calculatedField=bondTopPerformanceInd"})
@RunWith(SpringJUnit4ClassRunner.class)
public class BondTopPerformanceIndJobTest {

    @MockBean
    MongoTemplate mongoTemplate;
    @MockBean
    JobLauncherApplicationRunner applicationRunner;
    @Autowired
    private BondTopPerfmIndUpdateTasklet bondTopPerfmIndUpdateTasklet;
    @Autowired
    private BondTopPerfmIndCleanOldSortTasklet bondTopPerfmIndCleanOldSortTasklet;

    static JobParameters jobParameters;

    @Before
    public void before() {
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .toJobParameters();
    }

    public JobExecution getJobExecution() {
        return MetaDataInstanceFactory.createJobExecution("bondTopPerformanceIndJob", 1L, 1L,jobParameters);
    }

    public StepExecution getStepExecution() {
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }


    @Test
    public void testBondTopPerfmIndCleanOldSortJob() throws Exception {
        Mockito.when(mongoTemplate.updateMulti(any(Query.class), any(Update.class), anyString()))
                .thenReturn(UpdateResult.acknowledged(anyLong(),anyLong(),any(BsonValue.class)));

        StepContribution stepContribution = new StepContribution(getStepExecution());
        ChunkContext chunkContext = new ChunkContext(new StepContext(getStepExecution()));
        Assertions.assertDoesNotThrow(() -> bondTopPerfmIndCleanOldSortTasklet.execute(stepContribution,chunkContext));
    }

    @Test
    public void testBondTopPerfmIndUpdateJob() throws Exception {
        Document document = new Document();
        document.put(Field.cdvCde,"1");
        ArrayList<Document> list = new ArrayList<>();
        list.add(document);
        Mockito.when(mongoTemplate.find(any(Query.class),  Mockito.<Class<Document>>any(), anyString())).thenReturn(list);

        List<Document> mappedResults = new ArrayList<>();
        Document document2 = new Document();
        document.put(Field.cdvCde,"1");
        document.put(Field.prodId,"1");
        mappedResults.add(document2);
        AggregationResults<Document> aggregationResults = new AggregationResults<>(mappedResults, new Document());
        Mockito.when(mongoTemplate.aggregate(any(Aggregation.class), anyString(), Mockito.<Class<Document>>any()))
                .thenReturn(aggregationResults);

        Mockito.when(mongoTemplate.updateMulti(any(BasicQuery.class), any(Update.class), anyString()))
                .thenReturn(UpdateResult.acknowledged(anyLong(),anyLong(),any(BsonValue.class)));

        StepContribution stepContribution = new StepContribution(getStepExecution());
        ChunkContext chunkContext = new ChunkContext(new StepContext(getStepExecution()));
        Assertions.assertDoesNotThrow(() -> bondTopPerfmIndUpdateTasklet.execute(stepContribution,chunkContext));
    }
}
