package com.dummy.wpb.product;


import com.dummy.wpb.product.component.GacItemWriter;
import com.dummy.wpb.product.component.GacService;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonObjectId;
import org.bson.types.ObjectId;
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
import java.util.ArrayList;
import java.util.HashMap;

import static org.springframework.batch.core.ExitStatus.COMPLETED;
import static org.springframework.batch.core.ExitStatus.FAILED;

@SpringJUnitConfig
@SpringBatchTest
@SpringBootTest(classes = ImportGacJsonJobApplication.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class ImportGacJsonFileJobTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    static JobParameters jobParameters;

    @Autowired
    ApplicationContext applicationContext;

    @MockBean
    MongoTemplate mongoTemplate;

    @MockBean
    UpdateResult updateResult;

    @BeforeClass
    public static void setUp() throws IOException {
        jobParameters = new JobParametersBuilder()
                .addString("incomingPath", new ClassPathResource("/test").getFile().getAbsolutePath())
                .toJobParameters();
    }

    public JobExecution getJobExecution() {
        return MetaDataInstanceFactory.createJobExecution("importGacJsonFileJob", 1L, 1L, jobParameters);
    }

    public StepExecution getStepExecution() {
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }

    @Test
    public void testJob() throws Exception {
        Job importGacJsonFileJob = applicationContext.getBean("importGacJsonFileJob", Job.class);
        jobLauncherTestUtils.setJob(importGacJsonFileJob);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        Mockito.when(mongoTemplate.updateFirst(Mockito.any(), Mockito.any(), Mockito.eq("product"))).thenReturn(UpdateResult.acknowledged(0, 0L, new BsonObjectId(new ObjectId())));

        Assert.assertEquals(COMPLETED, jobExecution.getExitStatus());
    }

    @Test
    public void testErrorCase() {
        GacItemWriter gacItemWriter = applicationContext.getBean("gacItemWriter", GacItemWriter.class);
        gacItemWriter.write(new ArrayList<>());

        GacService gacService = new GacService();
        ReflectionTestUtils.setField(gacService, "mongoTemplate", mongoTemplate);
        ReflectionTestUtils.setField(gacService, "prodNotFoundNum", 0);
        ReflectionTestUtils.setField(gacService, "stepExecution", getStepExecution());
        Mockito.when(mongoTemplate.updateFirst(Mockito.any(), Mockito.any(), Mockito.eq("product"))).thenReturn(updateResult);
        gacService.updateProducts(new HashMap<>());
        Assert.assertEquals(0, updateResult.getModifiedCount());
    }
}
