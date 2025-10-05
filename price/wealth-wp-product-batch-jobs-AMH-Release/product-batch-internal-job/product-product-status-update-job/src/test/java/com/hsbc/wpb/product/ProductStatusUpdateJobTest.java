package com.dummy.wpb.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.service.GraphQLService;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@SpringBootTest(classes = ProductStatusUpdateJobApplication.class, args = {"HK", "HBAP", "BOND,DPS,ELI,SN,UT"})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitConfig
@SpringBatchTest
public class ProductStatusUpdateJobTest {

    @MockBean
    private MongoTemplate mongoTemplate;

    @MockBean
    private GraphQLService graphQLService;

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    Job productStatusUpdateJob;

    @Test
    @DirtiesContext
    public void testProductTenorDayUpdateJob() throws Exception {
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

        Mockito.when(graphQLService.executeRequest(any(GraphQLRequest.class), Mockito.<TypeReference<List<Document>>>any()))
                .thenAnswer(invocation -> {
                    GraphQLRequest request = invocation.getArgument(0, GraphQLRequest.class);
                    int skip = (int) request.getVariables().get("skip");
                    if (skip == 0) {
                        return prodList;
                    } else {
                        return Collections.emptyList();
                    }
                });

        jobLauncherTestUtils.setJob(productStatusUpdateJob);
        MongoCollection<Document> productCollection = Mockito.mock(MongoCollection.class);
        Mockito.when(mongoTemplate.getCollection(CollectionName.product.name())).thenReturn(productCollection);
        BulkWriteResult bulkWriteResult = BulkWriteResult.acknowledged(0, 8, 0, 8, Collections.emptyList(), Collections.emptyList());
        Mockito.when(productCollection.bulkWrite(any())).thenReturn(bulkWriteResult);
        JobExecution jobExecution =  jobLauncherTestUtils.launchJob(jobParameters);
        StepExecution stepExecution = new ArrayList<>(jobExecution.getStepExecutions()).get(0);
        Assert.assertEquals(8, stepExecution.getWriteCount());
    }
}

