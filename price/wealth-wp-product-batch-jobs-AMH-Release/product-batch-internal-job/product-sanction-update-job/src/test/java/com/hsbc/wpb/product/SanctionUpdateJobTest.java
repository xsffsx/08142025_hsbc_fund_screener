package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.utils.CommonUtils;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringJUnitConfig
@SpringBatchTest
@SpringBootTest(classes = SanctionUpdateJobApplication.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class SanctionUpdateJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean
    private MongoTemplate mongoTemplate;

    @Autowired
    ApplicationContext applicationContext;

    private static final String MOCKED_AGGREGATION_RAW_RESULTS_FILE = "/response/aggregation-raw-results-wrts.json";

    @Test
    public void testSanctionUpdateJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString(Field.ctryRecCde, "HK")
                .addString(Field.grpMembrRecCde, "HASE")
                .addString(Field.prodTypeCde, "WRTS")
                .toJobParameters();

        when(mongoTemplate.aggregate(
                any(Aggregation.class),
                eq(CollectionName.product.name()),
                eq(Document.class)
        )).thenReturn(getMockedAggregationResults());
        when(mongoTemplate.updateFirst(
                any(Query.class),
                any(Update.class),
                eq(CollectionName.product.name())
        )).thenReturn(UpdateResult.acknowledged(1, 1L, null));
        Job sanctionUpdateJob = applicationContext.getBean("sanctionUpdateJob", Job.class);
        jobLauncherTestUtils.setJob(sanctionUpdateJob);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        Optional<StepExecution> stepExecutionOpt = jobExecution.getStepExecutions().stream().findFirst();
        if (stepExecutionOpt.isPresent()) {
            StepExecution stepExecution = stepExecutionOpt.get();
            Assertions.assertEquals(1, stepExecution.getReadCount());
            Assertions.assertEquals(1, stepExecution.getWriteCount());
        }
    }

    private AggregationResults<Document> getMockedAggregationResults() {
        Document rawResults = Document.parse(CommonUtils.readResource(MOCKED_AGGREGATION_RAW_RESULTS_FILE));
        List<Document> mappedResults = rawResults.getList("results", Document.class);
        return new AggregationResults<>(mappedResults, rawResults);
    }

    @Test
    public void testSanctionUpdateJob_missingProdTypeCde() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString(Field.ctryRecCde, "HK")
                .addString(Field.grpMembrRecCde, "HASE")
                .toJobParameters();
        Job sanctionUpdateJob = applicationContext.getBean("sanctionUpdateJob", Job.class);
        jobLauncherTestUtils.setJob(sanctionUpdateJob);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        Assertions.assertEquals(ExitStatus.FAILED, jobExecution.getExitStatus());
    }

    @Test
    public void testSanctionUpdateJob_givenUnsupportedProdTypeCde() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString(Field.ctryRecCde, "HK")
                .addString(Field.grpMembrRecCde, "HASE")
                .addString(Field.prodTypeCde, "UT")
                .toJobParameters();
        Job sanctionUpdateJob = applicationContext.getBean("sanctionUpdateJob", Job.class);
        jobLauncherTestUtils.setJob(sanctionUpdateJob);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        Assertions.assertEquals(ExitStatus.FAILED, jobExecution.getExitStatus());
    }
}
