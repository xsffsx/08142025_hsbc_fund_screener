package com.dummy.wpb.product;

import com.dummy.wpb.product.model.Product;
import com.dummy.wpb.product.utils.CommonUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.batch.core.ExitStatus.COMPLETED;

@SpringBatchTest
@SpringBootTest(classes = PW0JsonFileApplication.class)
@ActiveProfiles("test")
class PW0JsonFileJobTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    ApplicationContext applicationContext;
    @MockBean
    private RestTemplate restTemplate;
    @MockBean
    private MongoTemplate mongoTemplate;
    static JobParameters jobParameters;
    private static File outputFile;

    private static final String outputPath = "/output";

    @BeforeAll
    public static void setUp() throws Exception {
        // Create output path
        String testClassPath = new ClassPathResource("/").getFile().getAbsolutePath();
        outputFile = new File(testClassPath + outputPath);
        if (!outputFile.exists()) {
            outputFile.mkdirs();
        } else {
            Arrays.stream(outputFile.listFiles()).forEach(File::delete);
        }
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "dummy")
                .addString("prodTypCde", "WRTS")
                .addString("targetName", "apexaProduct")
                .addString("outputPath", outputFile.getAbsolutePath()+"/")
                .toJobParameters();
    }

    @Test
    void testExportPW0JsonFileJob() throws Exception {
        String xmlResponse = CommonUtils.readResource("/response/mock-response.xml");
        Mockito.when(restTemplate.exchange(Mockito.any(RequestEntity.class), Mockito.<Class<String>>any())).thenReturn(ResponseEntity.ok(xmlResponse));
        Product product = new Product();
        product.set_id("123456");
        product.setPrcEffDt(LocalDate.now().minusDays(1));
        Mockito.when(mongoTemplate.findOne(Mockito.any(), Mockito.anyObject())).thenReturn(product);
        Job importEliFinDocJob = applicationContext.getBean("pw0JsonFileJob", Job.class);
        jobLauncherTestUtils.setJob(importEliFinDocJob);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        assertEquals(COMPLETED, jobExecution.getExitStatus());
        // check test result
        File[] files = outputFile.listFiles(((dir, name) -> name.endsWith(".json")));
        assertNotNull(files);
        assertEquals(1, files.length);
    }
}