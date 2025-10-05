package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.model.graphql.ReferenceData;
import com.dummy.wpb.product.service.ReferenceDataService;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.internal.bulk.WriteRequest;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.batch.core.ExitStatus.COMPLETED;

@SpringJUnitConfig
@SpringBatchTest
@SpringBootTest(classes = ImportEsgApplication.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class EsgImportJobTest {
    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    ApplicationContext applicationContext;

    @MockBean
    private MongoTemplate mongoTemplate;

    @MockBean
    private ReferenceDataService referenceDataService;

    @SneakyThrows
    @Before
    public void setUp() {
        List<Document> documentList = new ArrayList<>();
        Document document = new Document();
        Document esgDocument = new Document();
        esgDocument.put("esgInd", "Y");
        document.put("prodId", "280010355");
        document.put("esg", esgDocument);
        documentList.add(document);
        Mockito.when(mongoTemplate.find(any(Query.class), any(Document.class.getClass()), anyString())).thenReturn(documentList);
        List<ReferenceData> referenceDataList = new ArrayList<>();
        ReferenceData referenceData = new ReferenceData();
        referenceData.setCdvCde("NOT-ETI");
        referenceDataList.add(referenceData);
        ReferenceData referenceData2 = new ReferenceData();
        referenceData2.setCdvCde("THEMATIC");
        referenceDataList.add(referenceData2);
        Mockito.when(referenceDataService.referenceDataByFilter(any())).thenReturn(referenceDataList);
        BulkOperations bulkOperations = mock(BulkOperations.class);
        when(mongoTemplate.bulkOps(
                BulkOperations.BulkMode.UNORDERED,
                CollectionName.product.name()
        )).thenReturn(bulkOperations);
        when(bulkOperations.updateOne(anyList())).thenReturn(bulkOperations);
        when(bulkOperations.execute()).thenReturn(BulkWriteResult.acknowledged(
                WriteRequest.Type.UPDATE,
                2,
                2,
                Collections.emptyList(),
                Collections.emptyList()
        ));
    }

    @After
    public void clean() throws IOException {
        File testFolder = new ClassPathResource("/test").getFile();
        for (File file : Objects.requireNonNull(testFolder.listFiles())) {
            if (file.getName().endsWith(".bak")) {
                file.renameTo(new File(StringUtils.substringBefore(file.getAbsolutePath(), ".bak")));
            }
        }
    }

    @Test
    public void testJob() throws Exception {

        Job esgImportJob = applicationContext.getBean("esgImportJob", Job.class);
        jobLauncherTestUtils.setJob(esgImportJob);

        String absolutePath = new ClassPathResource("/test").getFile().getAbsolutePath();

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("incomingPath", absolutePath)
                .addString("filePattern", "GLOBAL_ESG.csv")
                .addString("ctryRecCde","HK")
                .addString("grpMembrRecCde","HBAP")
                .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        Assert.assertEquals(COMPLETED, jobExecution.getExitStatus());
    }
}
