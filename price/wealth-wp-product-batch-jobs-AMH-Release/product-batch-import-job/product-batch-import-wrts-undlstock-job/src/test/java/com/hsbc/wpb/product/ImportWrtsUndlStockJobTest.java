package com.dummy.wpb.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.Product;
import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.utils.JsonUtil;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.internal.bulk.WriteRequest;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.item.ExecutionContext;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.dummy.wpb.product.constant.JobExecutionContextKey.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
@SpringBatchTest
@SpringBootTest(classes = ImportWrtsUndlStockJobApplication.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class ImportWrtsUndlStockJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean
    private MongoTemplate mongoTemplate;

    @Autowired
    private ApplicationContext applicationContext;

    private static final String INCOMING_PATH_KEY = "incomingPath";

    private static final String INCOMING_PATH = "/test";

    private static final String MOCKED_PRODUCT_DATA_FILE = "/response/combined-existing-products.json";

    @Test
    @DirtiesContext
    public void testImportWrtsUndlStockJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString(Field.ctryRecCde, "HK")
                .addString(Field.grpMembrRecCde, "HASE")
                .addString(INCOMING_PATH_KEY, new ClassPathResource(INCOMING_PATH).getFile().getAbsolutePath())
                .toJobParameters();
        when(mongoTemplate.find(any(Query.class), eq(Product.class))).thenReturn(mockedCombinedExistingProductList());
        BulkOperations bulkOperations = mock(BulkOperations.class);
        when(mongoTemplate.bulkOps(
                BulkOperations.BulkMode.UNORDERED,
                Product.class
        )).thenReturn(bulkOperations);
        when(bulkOperations.updateOne(anyList())).thenReturn(bulkOperations);
        when(bulkOperations.execute()).thenReturn(BulkWriteResult.acknowledged(
                WriteRequest.Type.UPDATE,
                2,
                2,
                Collections.emptyList(),
                Collections.emptyList()
        ));
        Job importWrtsUndlStockJob = applicationContext.getBean("importWrtsUndlStockJob", Job.class);
        jobLauncherTestUtils.setJob(importWrtsUndlStockJob);
        JobExecution actualJobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        Optional<StepExecution> stepExecutionOpt = actualJobExecution.getStepExecutions().stream().findFirst();
        if (stepExecutionOpt.isPresent()) {
            ExecutionContext stepContext = stepExecutionOpt.get().getExecutionContext();
            Assertions.assertEquals(8, stepContext.getInt(READ_COUNT));
            Assertions.assertEquals(2, stepContext.getInt(UPDATED_COUNT));
            Assertions.assertEquals(0, stepContext.getInt(FAILED_COUNT));
            Assertions.assertEquals(6, stepContext.getInt(SKIPPED_COUNT));
        }
    }

    private List<Product> mockedCombinedExistingProductList() {
        return JsonUtil.convertJson2Object(
                CommonUtils.readResource(MOCKED_PRODUCT_DATA_FILE),
                new TypeReference<List<Product>>() {
                }
        );
    }

    @After
    public void after() throws IOException {
        File testFolder = new ClassPathResource(INCOMING_PATH).getFile();
        for (File file : Objects.requireNonNull(testFolder.listFiles())) {
            if (file.getName().endsWith(".bak")) {
                boolean ignored = file.renameTo(
                        new File(StringUtils.substringBefore(file.getAbsolutePath(), ".bak"))
                );
            }
        }
    }
}