package com.dummy.wpb.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.component.FinDocReleaseService;
import com.dummy.wpb.product.exception.productBatchException;
import com.dummy.wpb.product.model.FinDocHistPo;
import com.dummy.wpb.product.model.FinDocULReqPo;
import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.utils.JsonUtil;
import org.junit.jupiter.api.AfterAll;
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
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.batch.core.ExitStatus.COMPLETED;

@SpringBatchTest
@SpringBootTest(classes = FinDocReleaseJobApplication.class)
@ActiveProfiles("test")
public class FinDocReleaseJobApplicationTest {
    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    ApplicationContext applicationContext;

    @MockBean
    FinDocReleaseService finDocReleaseService;

    static FinDocHistPo[] fds;

    static InputStream inputStream;

    private static String testFilePath = "src/test/resources/";

    @BeforeAll
    public static void setUp() throws Exception {
        fds = JsonUtil.convertJson2Object(CommonUtils.readResource("/fin_doc_hist.json"), new TypeReference<FinDocHistPo[]>() {});
        inputStream = new FileInputStream("src/test/resources/test/test.txt");
    }

    @AfterAll
    public static void after() throws IOException {
        File testFolder = new File(testFilePath);
        for (File file : testFolder.listFiles()) {
            if (file.getName().endsWith(".csv")){
                Files.delete(Paths.get(file.getPath()));
            }
        }
    }

    @Test
    void testFinDocRelease() throws Exception {
        Job finDocReleaseJob = applicationContext.getBean("finDocReleaseJob", Job.class);
        jobLauncherTestUtils.setJob(finDocReleaseJob);

        Mockito.when(finDocReleaseService.retrievePendingRecord("HK", "HBAP")).thenReturn(fds);

        Mockito.when(finDocReleaseService.retrieveUploadReqTO(fds[0])).thenReturn(mock(FinDocULReqPo.class));
        Mockito.when(finDocReleaseService.retrieveUploadReqTO(fds[0])).thenReturn(new FinDocULReqPo());

        productBatchException productBatchException = new productBatchException("WPCResourceException:" + "test");
        when(finDocReleaseService.retrieveUploadReqTO(fds[0]))
                .thenThrow(productBatchException);
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        assertEquals(COMPLETED, jobExecution.getExitStatus());


    }
}