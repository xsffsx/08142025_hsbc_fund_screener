package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.utils.CommonUtils;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonArray;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
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
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

@SpringJUnitConfig
@SpringBatchTest
@SpringBootTest(classes = WimEgressJobApplication.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class WimEgressJobTest {

    @MockBean
    MongoTemplate mongoTemplate;

    @MockBean
    MongoClient mongoClient;

    @MockBean
    MongoDatabase mongoDatabase;

    @MockBean
    MongoDatabaseFactory mongoDatabaseFactory;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    static JobParameters jobParameters;

    public JobExecution getJobExecution() {
        return MetaDataInstanceFactory.createJobExecution("importSecXmlFileJob", 1L, 1L, jobParameters);
    }

    public StepExecution getStepExecution() {
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }

    public List productList;

    public List referenceDataList;

    public File outputFile;

    @Before
    public void before() throws IOException {
        String testClassPath = new ClassPathResource("/").getFile().getAbsolutePath();
        outputFile = new File(testClassPath + "/output");
        if (!outputFile.exists()) {
            outputFile.mkdirs();
        }else {
            Arrays.stream(outputFile.listFiles()).forEach(File::delete);
        }

        jobParameters = new JobParametersBuilder()
                .addString("outputPath", outputFile.getAbsolutePath())
                .toJobParameters();

        productList = BsonArray.parse(CommonUtils.readResource("/product.json"))
                .getValues()
                .stream()
                .map(item -> Document.parse(item.asDocument().toJson()))
                .collect(Collectors.toList());
        referenceDataList = BsonArray.parse(CommonUtils.readResource("/referenceData.json"))
                .getValues()
                .stream()
                .map(item -> Document.parse(item.asDocument().toJson()))
                .collect(Collectors.toList());
    }

    @Test
    public void testWimEgressJob() throws Exception {
        ArgumentMatcher<Query> argumentMatcher = argument -> argument != null && argument.getSkip() == 0;
        Mockito.when(mongoTemplate.find(argThat(argumentMatcher), any(), Mockito.eq(CollectionName.product.name()))).thenReturn(productList);
        Mockito.when(mongoTemplate.find(argThat(argumentMatcher), any(), Mockito.eq(CollectionName.reference_data.name()))).thenReturn(referenceDataList);
        Job wimEgressJob = applicationContext.getBean("wimEgressJob", Job.class);
        jobLauncherTestUtils.setJob(wimEgressJob);
        jobLauncherTestUtils.launchJob(jobParameters);

        File[] files = outputFile.listFiles(((dir, name) -> name.endsWith(".csv")));
        Assert.assertNotNull(files);
        Assert.assertEquals(5, files.length);
    }
}
