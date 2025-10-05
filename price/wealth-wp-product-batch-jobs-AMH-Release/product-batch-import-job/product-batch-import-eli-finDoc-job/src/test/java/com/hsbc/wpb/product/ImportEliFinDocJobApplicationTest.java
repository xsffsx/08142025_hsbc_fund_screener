package com.dummy.wpb.product;

import com.dummy.wpb.product.component.ImportEliFinDocService;
import com.dummy.wpb.product.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.junit.jupiter.api.*;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ReflectionUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.batch.core.ExitStatus.COMPLETED;

@SpringBatchTest
@SpringBootTest(classes = ImportEliFinDocJobApplication.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ImportEliFinDocJobApplicationTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    ApplicationContext applicationContext;

    @MockBean
    ImportEliFinDocService importEliFinDocService;

    static Document prod;

    static InputStream inputStream;

    private static String ELITestFilePath = "/test/NO_CMB";

    JobParameters jobParameters;

    @BeforeAll
    public static void setUp() throws Exception {
        prod = Document.parse(CommonUtils.readResource("/product-import.json"));
        inputStream = new FileInputStream("src/test/resources/test.txt");
    }

    @AfterEach
    public void after() throws IOException {
        File testFolder = new ClassPathResource(ELITestFilePath).getFile();
        for (File file : testFolder.listFiles()) {
            if (file.getName().endsWith(".bak")){
                file.renameTo(new File(StringUtils.substringBefore(file.getAbsolutePath(),".bak")));
            }
        }
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .addString("prodTypeCde", "ELI")
                .addString("fileName", new ClassPathResource(ELITestFilePath).getFile().getAbsolutePath())
                .addString("actionCde", "Y")
                .toJobParameters();
    }

    @Test
    @Order(1)
    void testJobPostProduct_Y() throws Exception {
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HKAB")
                .addString("prodTypeCde", "ELI")
                .addString("fileName", new ClassPathResource(ELITestFilePath).getFile().getAbsolutePath())
                .addString("actionCde", "Y")
                .toJobParameters();
        Job importEliFinDocJob = applicationContext.getBean("importEliFinDocJob", Job.class);
        jobLauncherTestUtils.setJob(importEliFinDocJob);
        Mockito.when(importEliFinDocService.queryProductByPriNum(Mockito.any())).thenReturn(prod);

        Runtime mockedRuntime = Mockito.mock(Runtime.class);
        Mockito.mockStatic(Runtime.class);
        Mockito.when(Runtime.getRuntime()).thenReturn(mockedRuntime);
        Process mockedProcess = Mockito.mock(Process.class);
        Mockito.when(mockedRuntime.exec(Mockito.anyString())).thenReturn(mockedProcess);
        Mockito.when(mockedProcess.getInputStream()).thenReturn(inputStream);
        Mockito.when(mockedProcess.getErrorStream()).thenReturn(inputStream);

        BufferedReader mockedBufferedReader = Mockito.mock(BufferedReader.class);
        Mockito.when(mockedBufferedReader.readLine()).thenReturn("test readLine");
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        assertEquals(COMPLETED, jobExecution.getExitStatus());
    }

    @Test
    @Order(2)
    void testJobPostProduct_N() throws Exception {
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HKAB")
                .addString("prodTypeCde", "ELI")
                .addString("fileName", new ClassPathResource(ELITestFilePath).getFile().getAbsolutePath())
                .addString("actionCde", "N")
                .toJobParameters();
        Job importEliFinDocJob = applicationContext.getBean("importEliFinDocJob", Job.class);
        jobLauncherTestUtils.setJob(importEliFinDocJob);
        ExecutorService mockedExecutors = Mockito.mock(ExecutorService.class);
        Mockito.mockStatic(Executors.class);
        Mockito.when(Executors.newFixedThreadPool(anyInt())).thenReturn(mockedExecutors);

        //mock TimeUnit.HOURS.sleep
        Method getDeclaredFields0 = Class.class.getDeclaredMethod("getDeclaredFields0", boolean.class);
        getDeclaredFields0.setAccessible(true);
        Field[] fields = (Field[]) getDeclaredFields0.invoke(Field.class, false);
        Field modifyerField =
                Arrays.stream(fields).filter(it -> "modifiers".equals(it.getName())).findFirst().orElse(null);
        modifyerField.setAccessible(true);
        TimeUnit timeUnit = Mockito.mock(TimeUnit.class);
        Field hoursField = ReflectionUtils.findField(TimeUnit.class, "HOURS");
        hoursField.setAccessible(true);
        modifyerField.setInt(hoursField, hoursField.getModifiers() & ~Modifier.FINAL);
        ReflectionUtils.setField(hoursField, TimeUnit.class, timeUnit);
        Mockito.doNothing().when(timeUnit).sleep(Mockito.anyLong());//NOSONAR

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        assertEquals(COMPLETED, jobExecution.getExitStatus());
    }
}
