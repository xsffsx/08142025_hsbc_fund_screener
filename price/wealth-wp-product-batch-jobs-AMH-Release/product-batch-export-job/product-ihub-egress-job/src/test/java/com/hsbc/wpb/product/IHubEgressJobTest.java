package com.dummy.wpb.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.config.loader.DBConfigLoader;
import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.model.ProductMetadata;
import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.utils.JsonUtil;
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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes = IHubEgressJobApplication.class)
@SpringJUnitConfig
@RunWith(SpringJUnit4ClassRunner.class)
public class IHubEgressJobTest {

    private static JobParameters jobParameters;

    private File outputFile;

    private List productList;

    private List referenceDataList;

    private Document udfMapping;
    
    private List<ProductMetadata> productMetadataList;

    @MockBean
    public MongoClient mongoClient;

    @MockBean
    public MongoDatabase mongoDatabase;
    
    @MockBean
    public MongoTemplate mongoTemplate;
    
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private DBConfigLoader dbConfigLoader;

    public JobExecution getJobExecution() {
        return MetaDataInstanceFactory.createJobExecution("iHubEgressJob", 1L, 1L, jobParameters);
    }

    public StepExecution getStepExecution() {
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }


    @Before
    public void before() throws IOException {
        // Create output path
        String testClassPath = new ClassPathResource("/").getFile().getAbsolutePath();
        outputFile = new File(testClassPath + "/output");
        if (!outputFile.exists()) {
            outputFile.mkdirs();
        } else {
            Arrays.stream(outputFile.listFiles()).forEach(File::delete);
        }
        // set output path in job parameters
        jobParameters = new JobParametersBuilder()
                .addString("outputPath", outputFile.getAbsolutePath())
                .toJobParameters();
        // load product.json
        productList = BsonArray.parse(CommonUtils.readResource("/product.json"))
                .getValues()
                .stream()
                .map(item -> Document.parse(item.asDocument().toJson()))
                .peek(item -> item.put("_id",item.get("_id",Number.class).longValue()))
                .collect(Collectors.toList());
        // load referenceData.json
        referenceDataList = BsonArray.parse(CommonUtils.readResource("/referenceData.json"))
                .getValues()
                .stream()
                .map(item -> Document.parse(item.asDocument().toJson()))
                .collect(Collectors.toList());
        // load user-defined-field-mapping.json
        udfMapping = Document.parse(CommonUtils.readResource("/user-defined-field-mapping.json"));
    }

    @Test
    public void testIhubEgressJob() throws Exception {
        ArgumentMatcher<Query> argumentMatcher = argument -> argument != null && argument.getSkip() == 0;
        // Mock product
        AtomicInteger countProduct = new AtomicInteger(0);
        Mockito.when(mongoTemplate.find(argThat(argumentMatcher), any(), Mockito.eq(CollectionName.product.name()))).thenAnswer(
                invocation -> {
                    Query argument = invocation.getArgument(0);
                    // Break doPageRead if count > 0
                    if (countProduct.get() == 0 && argument != null && argument.getSkip() == 0) {
                        countProduct.incrementAndGet();
                        return productList;
                    }
                    return new ArrayList<>();
                }
        );
        // Mock reference_data
        AtomicInteger countRefData = new AtomicInteger(0);
        Mockito.when(mongoTemplate.find(argThat(argumentMatcher), any(), Mockito.eq(CollectionName.reference_data.name()))).thenAnswer(
                invocation -> {
                    Query argument = invocation.getArgument(0);
                    // Break doPageRead if count > 0
                    if (countRefData.get() == 0 && argument != null && argument.getSkip() == 0) {
                        countRefData.incrementAndGet();
                        return referenceDataList;
                    }
                    return new ArrayList<>();
                }
        );
        productMetadataList = JsonUtil.convertJson2Object(CommonUtils.readResource("/metadata.json"), new TypeReference<List<ProductMetadata>>() {
        });
        // Mock metadata
        Query queryMetadata = new Query()
                .addCriteria(Criteria.where("table").ne(null))
                .addCriteria(Criteria.where("fieldName").ne(null));
        Mockito.when(mongoTemplate.find(queryMetadata, ProductMetadata.class)).thenReturn(productMetadataList);
        // Mock user-defined-field-mapping
        Mockito.when(mongoTemplate.findOne(argThat(argumentMatcher), any(), Mockito.eq(CollectionName.configuration.name()))).thenReturn(udfMapping);

        dbConfigLoader.afterPropertiesSet();

        // run iHubEgressJob
        Job iHubEgressJob = applicationContext.getBean("iHubEgressJob", Job.class);
        jobLauncherTestUtils.setJob(iHubEgressJob);
        jobLauncherTestUtils.launchJob(jobParameters);
        // check test result
        File[] files = outputFile.listFiles(((dir, name) -> name.endsWith(".csv")));
        Assert.assertNotNull(files);
        Assert.assertEquals(14, files.length);
    }

}
