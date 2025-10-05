package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.utils.CommonUtils;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCollection;
import org.bson.BsonArray;
import org.bson.BsonDocumentReader;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.DocumentCodec;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ActiveProfiles("test")
@SpringBootTest(classes = ProductTenorDayUpdateJobApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitConfig
@SpringBatchTest
public class ProductTenorDayUpdateJobTest {

    @MockBean
    private MongoTemplate mongoTemplate;

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    Job productTenorDayUpdateJob;

    @Test
    @DirtiesContext
    public void testProductTenorDayUpdateJob_givenExpectUpdateCount() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .addString("prodTypeCde", "ALL")
                .addLong("run.id", 1L)
                .toJobParameters();
        List<Document> prodList = BsonArray.parse(CommonUtils.readResource("/product.json"))
                .stream()
                .map(BsonValue::asDocument)
                .map((bsonDocument) ->
                        new DocumentCodec().decode(new BsonDocumentReader(bsonDocument), DecoderContext.builder().build())
                )
                .collect(Collectors.toList());

        Mockito.when(mongoTemplate.find(any(Query.class), eq(Document.class), eq(CollectionName.product.name())))
                .thenAnswer(invocation -> {
                    Query query = invocation.getArgument(0, Query.class);
                    Long latestProductId = query.getQueryObject().getEmbedded(Arrays.asList("_id", "$gt"), Long.class);
                    if (new Long(0).equals(latestProductId)) {
                        return prodList;
                    } else {
                        return Collections.emptyList();
                    }
                });

        MongoCollection<Document> productCollection = Mockito.mock(MongoCollection.class);
        Mockito.when(mongoTemplate.getCollection(CollectionName.product.name())).thenReturn(productCollection);
        BulkWriteResult bulkWriteResult = BulkWriteResult.acknowledged(0, 3, 0, 3, Collections.emptyList(), Collections.emptyList());
        Mockito.when(productCollection.bulkWrite(any())).thenReturn(bulkWriteResult);
        jobLauncherTestUtils.setJob(productTenorDayUpdateJob);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        Assert.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        StepExecution stepExecution = new ArrayList<>(jobExecution.getStepExecutions()).get(0);
        Assert.assertEquals(3, stepExecution.getWriteCount());
    }

    @Test
    @DirtiesContext
    public void testProductTenorDayUpdateJob_givenUnExpectUpdateCount() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .addString("prodTypeCde", "ALL")
                .addLong("run.id", 2L)
                .toJobParameters();
        List<Document> prodList = BsonArray.parse(CommonUtils.readResource("/product.json"))
                .stream()
                .map(BsonValue::asDocument)
                .map((bsonDocument) ->
                        new DocumentCodec().decode(new BsonDocumentReader(bsonDocument), DecoderContext.builder().build())
                )
                .collect(Collectors.toList());

        Mockito.when(mongoTemplate.find(any(Query.class), eq(Document.class), eq(CollectionName.product.name())))
                .thenAnswer(invocation -> {
                    Query query = invocation.getArgument(0, Query.class);
                    Long latestProductId = query.getQueryObject().getEmbedded(Arrays.asList("_id", "$gt"), Long.class);
                    if (new Long(0).equals(latestProductId)) {
                        return prodList;
                    } else {
                        return Collections.emptyList();
                    }
                });

        MongoCollection<Document> productCollection = Mockito.mock(MongoCollection.class);
        Mockito.when(mongoTemplate.getCollection(CollectionName.product.name())).thenReturn(productCollection);
        BulkWriteResult bulkWriteResult = BulkWriteResult.acknowledged(0, 2, 0, 2, Collections.emptyList(), Collections.emptyList());
        Mockito.when(productCollection.bulkWrite(any())).thenReturn(bulkWriteResult);
        jobLauncherTestUtils.setJob(productTenorDayUpdateJob);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        Assert.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        StepExecution stepExecution = new ArrayList<>(jobExecution.getStepExecutions()).get(0);
        Assert.assertEquals(3, stepExecution.getWriteCount());
    }
}
