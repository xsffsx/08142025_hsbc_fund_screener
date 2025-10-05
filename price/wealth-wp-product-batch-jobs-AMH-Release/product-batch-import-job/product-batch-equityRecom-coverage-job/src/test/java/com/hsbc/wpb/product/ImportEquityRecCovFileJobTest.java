package com.dummy.wpb.product;

import com.dummy.wpb.product.component.CoverageFileListener;
import com.dummy.wpb.product.component.CoverageFileWriter;
import com.dummy.wpb.product.jpo.EquityRecommendationsPo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.batch.core.ExitStatus.COMPLETED;

@SpringJUnitConfig
@SpringBatchTest
@SpringBootTest(classes = ImportEquityRecCovJobApplication.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class ImportEquityRecCovFileJobTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    static JobParameters jobParameters;

    @Autowired
    ApplicationContext applicationContext;

    static JobExecution jobExecution;

    @MockBean
    CoverageFileListener coverageFileListener;

    @MockBean
    MongoTemplate mongoTemplate;

    MongoCollection<Document> equityRecomColl = Mockito.mock(MongoCollection.class);


    @BeforeClass
    public static void setUp() throws IOException {
        jobParameters = new JobParametersBuilder()
                .addString("incomingPath", new ClassPathResource("/test").getFile().getAbsolutePath())
                .toJobParameters();
        jobExecution = MetaDataInstanceFactory.createJobExecution("equityRecCoverageJob", 1L, 1L, jobParameters);
    }

    public JobExecution getJobExecution() {
        return jobExecution;
    }

    public StepExecution getStepExecution() {
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }

    @Test
    public void testJob_completeJob() throws Exception {
        ReflectionTestUtils.setField(coverageFileListener, "equityRecomColl", equityRecomColl);
        ReflectionTestUtils.setField(coverageFileListener, "equityRecomCollNum", 1L);
        ReflectionTestUtils.setField(coverageFileListener, "currDate", new Date());
        Mockito.when(equityRecomColl.countDocuments()).thenReturn(2L);
        Mockito.when(equityRecomColl.deleteMany(any(Bson.class))).thenReturn(DeleteResult.acknowledged(1L));


        Job equityRecCoverageJob = applicationContext.getBean("equityRecCoverageJob", Job.class);
        jobLauncherTestUtils.setJob(equityRecCoverageJob);
        Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(equityRecomColl);
        Mockito.when(equityRecomColl.countDocuments()).thenReturn(0L);

        Mockito.doCallRealMethod().when(coverageFileListener).afterJob(any());
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        Assert.assertEquals(COMPLETED, jobExecution.getExitStatus());
    }

    @Test
    public void testCoverageFileProcesser_returnNothing() {
        try {
            CoverageFileWriter coverageFileWriter = new CoverageFileWriter();
            coverageFileWriter.write(Collections.singletonList(new EquityRecommendationsPo()));
        } catch (Exception e) {
            Assert.fail("An unexpected exception occurred.");
        }
    }

    @Test
    public void testCoverageFileListener_afterJob_returnNothing() {
        try {
            ReflectionTestUtils.setField(coverageFileListener, "equityRecomColl", equityRecomColl);
            ReflectionTestUtils.setField(coverageFileListener, "equityRecomCollNum", 1L);
            ReflectionTestUtils.setField(coverageFileListener, "currDate", new Date());
            Mockito.when(mongoTemplate.getCollection(Mockito.any())).thenReturn(equityRecomColl);
            Mockito.when(equityRecomColl.countDocuments()).thenReturn(2L);
            Mockito.when(equityRecomColl.deleteMany(any(Bson.class))).thenReturn(DeleteResult.acknowledged(1L));
            coverageFileListener.beforeJob(getJobExecution());
            coverageFileListener.afterJob(getJobExecution());
        } catch (Exception e) {
            Assert.fail("An unexpected exception occurred.");
        }
    }

}
