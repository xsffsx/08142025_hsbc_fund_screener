package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.utils.CommonUtils;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonArray;
import org.bson.Document;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.assertj.core.api.Assertions.*;

@SpringJUnitConfig
@SpringBatchTest
@ActiveProfiles("test")
@SpringBootTest(classes = GBAGenerationJobApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class GBAGenerateJobTest {
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

    public File outputFile;

    public List productList;

    private final static String CTRY_REC_CDE = "HK";

    private final static String GRP_MEMBR_REC_CDE = "HBAP";

    private final static String SYSTEM_CDE = "ELI";

    private final static String PROD_TYPE_CDE = "ALL";

    public JobExecution getJobExecution() {
        return MetaDataInstanceFactory.createJobExecution("gbaGenerationJob", 1L, 1L, jobParameters);
    }

    public StepExecution getStepExecution() {
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }

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
                .addString("ctryRecCde",        CTRY_REC_CDE)
                .addString("grpMembrRecCde",    GRP_MEMBR_REC_CDE)
                .addString("systemCde",         SYSTEM_CDE)
                .addString("prodTypeCde",       PROD_TYPE_CDE)
                .addString("outputPath",        outputFile.getAbsolutePath())
                .toJobParameters();

        productList = BsonArray.parse(CommonUtils.readResource("/product.json"))
                .getValues()
                .stream()
                .map(item -> Document.parse(item.asDocument().toJson()))
                .collect(Collectors.toList());
    }

    @Test
    public void testGBA_Ind_Item_Writer_Operation() throws Exception {
        ArgumentMatcher<Query> argumentMatcher = argument -> argument != null && argument.getSkip() == 0;
        Assertions.assertNotNull(argumentMatcher);
        Assertions.assertNotNull(mongoTemplate);
        Mockito.when(mongoTemplate.find(argThat(argumentMatcher), any(), Mockito.eq(CollectionName.product.name()))).thenReturn(productList);

        Job gbaGenerationJob = applicationContext.getBean("gbaGenerationJob", Job.class);
        jobLauncherTestUtils.setJob(gbaGenerationJob);
        jobLauncherTestUtils.launchJob(jobParameters);

        File[] files = outputFile.listFiles((dir, fileName) ->
                fileName.startsWith(String.format("%s%s%s%s%s",
                        CTRY_REC_CDE,
                        GRP_MEMBR_REC_CDE,
                        SYSTEM_CDE,
                        PROD_TYPE_CDE,
                        LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)))
                        && fileName.endsWith(".txt"));
        Assertions.assertNotNull(files);
        Assertions.assertEquals(1, files.length);

        Mockito.verify(mongoTemplate, times(2))
                .find(argThat(argumentMatcher), any(), Mockito.eq(CollectionName.product.name()));
    }

}
