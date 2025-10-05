package com.dummy.wpb.product.jobs.registration;

import com.google.common.collect.ImmutableMap;
import com.dummy.wpb.product.model.SystemParameter;
import com.dummy.wpb.product.service.SystemParameterService;
import com.dummy.wpb.product.utils.CommonUtils;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonObjectId;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.batch.core.ExitStatus.COMPLETED;

@SpringJUnitConfig
@SpringBatchTest
@SpringBootTest(classes = ProductRegistrationApplication.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class RegistrationJobTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    static JobParameters jobParameters;

    static JobExecution jobExecution;

    @Autowired
    ApplicationContext applicationContext;

    @MockBean
    MongoTemplate mongoTemplate;

    @MockBean
    UpdateResult updateResult;

    @MockBean
    RestTemplate restTemplate;

    static Document originalProduct;

    @MockBean
    SystemParameterService systemParameterService;

    @BeforeClass
    public static void setUp() {
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .addString("prodTypeCde", "UT")
                .addString("groupSize", "300")
                .addString("supportAltIdCdes", "I,S")
                .addString("isDaltaSync", "true")
                .toJobParameters();
        jobExecution = MetaDataInstanceFactory.createJobExecution("productRegistrationJob", 1L, 1L, jobParameters);
        originalProduct = Document.parse(CommonUtils.readResource("/product-original.json"));
    }

    public JobExecution getJobExecution() {
        return jobExecution;
    }

    public StepExecution getStepExecution() {
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }

    @Test
    public void testJob() throws Exception {
        SystemParameter mockSystemParameter = new SystemParameter();
        mockSystemParameter.setParmValueText("2023-10-01T00:00:00Z");
        Mockito.when(systemParameterService.getSystemParameter(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(mockSystemParameter);
        Mockito.doNothing().when(systemParameterService).createSystemParameter(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        Mockito.doNothing().when(systemParameterService).updateSystemParameter(Mockito.any());
        Job productRegistrationJob = applicationContext.getBean("productRegistrationJob", Job.class);
        jobLauncherTestUtils.setJob(productRegistrationJob);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        Assert.assertEquals(COMPLETED, jobExecution.getExitStatus());
    }

    @Test
    public void testListener() {
        Mockito.when(systemParameterService.getSystemParameter(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        Mockito.doNothing().when(systemParameterService).createSystemParameter(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        ProductRegistrationListener listener = new ProductRegistrationListener(systemParameterService);
        ReflectionTestUtils.setField(listener, "isDaltaSync", "true");
        ReflectionTestUtils.setField(listener, "ctryRecCde", "HK");
        ReflectionTestUtils.setField(listener, "grpMembrRecCde", "HBAP");
        listener.beforeJob(getJobExecution());
        // Assertion to verify createSystemParameter is called
        Mockito.verify(systemParameterService, Mockito.times(1))
           .createSystemParameter(eq("HK"), eq("HBAP"), Mockito.anyString(), Mockito.anyString());
    }


    @Test
    public void testValidator() {
        ProductRegistrationService productRegistrationService = new ProductRegistrationService();
        productRegistrationService.registerProduct(Collections.emptyList());

        ProductDocumentValidator productDocumentValidator = new ProductDocumentValidator();
        Assert.assertThrows(ValidationException.class, () -> {
            productDocumentValidator.validate(originalProduct);
        });
    }

    @Test
    public void testWriter() {
        final Map<String, Object> map = new HashMap<>();
        ArrayList<Map<String, Object>> dataMaps = new ArrayList<>();
        dataMaps.add(ImmutableMap.of("prodId", "50439400", "globalId", "8888"));
        dataMaps.add(ImmutableMap.of("prodId", "50439401", "globalId", ""));
        map.put("tokenId", "xxxxxxxxxxxxxxxxxxxxx");
        map.put("issued_token", "xxxxxxxxxxxxxxxxxxxxx");
        map.put("data", dataMaps);

        @SuppressWarnings("unchecked")
        final ResponseEntity<Map<String, Object>> response = Mockito.mock(ResponseEntity.class);
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class), Mockito.any(ParameterizedTypeReference.class))).thenReturn(response);
        Mockito.when(response.getBody()).thenReturn(map);

        final Update[] update = new Update[1];
        Mockito.when(mongoTemplate.updateFirst(Mockito.any(), Mockito.any(), Mockito.anyString())).then(invocationOnMock -> {
                    update[0] = invocationOnMock.getArgument(1, Update.class);
                    return UpdateResult.acknowledged(0, 0L, new BsonObjectId(new ObjectId()));
                });

        ProductRegistrationWriter productRegistrationWriter = applicationContext.getBean("productRegistrationWriter", ProductRegistrationWriter.class);
        productRegistrationWriter.write(Collections.singletonList(originalProduct));
        Assert.assertNotNull(update[0]);
    }
}