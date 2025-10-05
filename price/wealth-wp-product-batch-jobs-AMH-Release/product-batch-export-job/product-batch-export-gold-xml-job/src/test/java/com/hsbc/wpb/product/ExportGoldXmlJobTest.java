package com.dummy.wpb.product;

import com.google.common.collect.Lists;
import com.dummy.wpb.product.constant.BatchConstants;
import com.dummy.wpb.product.model.SystemParameter;
import com.dummy.wpb.product.model.xml.DigitalAssetCurrency;
import com.dummy.wpb.product.service.GraphqlServiceItemReader;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.util.LegacyConfig;
import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.utils.JsonUtil;
import org.bson.Document;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.item.support.SynchronizedItemStreamWriter;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes = ExportGoldXmlJobApplication.class, args = {"ctryRecCde=HK", "grpMembrRecCde=HBAP", "ctryRecCde=GSOPSD", "prodStatCde=A,S,P", "fileType=DELTA"})
@SpringJUnitConfig
@RunWith(SpringJUnit4ClassRunner.class)
public class ExportGoldXmlJobTest {

    private static JobParameters jobParameters;

    private static Document originalProduct;

    private static Document nullProduct;

    private static MockedStatic<LegacyConfig> legacyConfigMockedStatic;

    private static File outputPath;

    @Autowired
    private ApplicationContext applicationContext;

    @MockBean
    private ProductService productService;

    @MockBean
    private LegacyConfig legacyConfig;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private MongoOperations mongoOperations;
    @BeforeClass
    public static void setUp() throws IOException {
        // job parameters
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde","HK")
                .addString("grpMembrRecCde","HBAP")
                .addString("systemCde","GSOPSD")
                .addString("prodStatCde","A,S,P")
                .addString("fileType","DELTA")
                .toJobParameters();
        legacyConfigMockedStatic = Mockito.mockStatic(LegacyConfig.class);
        legacyConfigMockedStatic.when(() -> LegacyConfig.getList(anyString())).thenReturn(Collections.emptyList());
        // read resources
        originalProduct = Document.parse(CommonUtils.readResource("/product-original.json"));
        nullProduct = Document.parse(CommonUtils.readResource("/product-digtlAssetCcy-null.json"));
        // test output path
        String testClassPath = new ClassPathResource("/").getFile().getCanonicalPath();
        System.setProperty("batch.outgoing.path", testClassPath);
        outputPath = new File(testClassPath + "/HKHBAP/GSOPSD/").getCanonicalFile();
        if (!outputPath.exists()) {
            outputPath.mkdirs();
        } else {
            Arrays.stream(outputPath.listFiles()).forEach(File::delete);
        }
    }

    public JobExecution getJobExecution() {
        return MetaDataInstanceFactory.createJobExecution("goldPriceUpdateJob", 1L, 1L, jobParameters);
    }

    public StepExecution getStepExecution() {
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }

    @Test
    public void testProcessor () throws Exception {
        Mockito.when(productService.productByFilters(Mockito.anyMap())).thenReturn(Lists.newArrayList(originalProduct));

        Mockito.doNothing().when(legacyConfig).init();
        // mock response body
        ResponseEntity<Document> responseEntity = new ResponseEntity<>(new Document(), HttpStatus.OK);
        Mockito.when(restTemplate.exchange(Mockito.any(RequestEntity.class), Mockito.<Class<Document>>any()))
                .thenReturn(responseEntity);
        // run ExportGoldXmlJobProcessor
        ExportGoldXmlJobProcessor processor = applicationContext.getBean("exportGoldXmlJobProcessor", ExportGoldXmlJobProcessor.class);
        processor.beforeStep(getStepExecution());
        // digtlAssetCcy != null
        DigitalAssetCurrency digitalAssetCurrency1 = processor.process(originalProduct);
        Assertions.assertNotNull(digitalAssetCurrency1);
        Assertions.assertNotNull(digitalAssetCurrency1.getProdKeySeg());
        Assertions.assertEquals("32188", digitalAssetCurrency1.getProdKeySeg().getProdCde());
        // digtlAssetCcy == null
        DigitalAssetCurrency digitalAssetCurrency2 = processor.process(nullProduct);
        Assertions.assertNotNull(digitalAssetCurrency2);
        Assertions.assertNotNull(digitalAssetCurrency2.getDigtlAssetCcySeg());
        Assertions.assertNull(digitalAssetCurrency2.getDigtlAssetCcySeg().getDigtlCcy());
    }

    @Test
    public void testProcessor_createHeaderFile_exists() throws Exception {
        // outgoing file settings
        System.setProperty(BatchConstants.EGRESS_GSOPSD_LAST_SUCCESSFUL_DT_TM, "20230820042649");
        System.setProperty(BatchConstants.EGRESS_GSOPSD_SEQ, "123");
        // create header file
        File headerFile = new File(outputPath.getCanonicalPath() + "/HK_HBAP_GSOPSD_DAC_20230820042649_123.out");
        headerFile.createNewFile();
        // run ExportGoldXmlJobProcessor
        ExportGoldXmlJobProcessor processor = applicationContext.getBean("exportGoldXmlJobProcessor", ExportGoldXmlJobProcessor.class);
        ReflectionTestUtils.setField(processor,"ctryRecCde","HK");
        ReflectionTestUtils.setField(processor,"grpMembrRecCde","HBAP");
        ReflectionTestUtils.setField(processor,"systemCde","GSOPSD");
        ExitStatus exitStatus = processor.afterStep(getStepExecution());
        Assertions.assertEquals(ExitStatus.COMPLETED, exitStatus);
        // check header file
        File[] files = outputPath.listFiles(((dir, name) -> name.endsWith("123.out")));
        Assertions.assertEquals(2, files.length);
    }

    @Test
    public void testProcessor_createHeaderFile_notExists() {
        // outgoing file settings
        System.setProperty(BatchConstants.EGRESS_GSOPSD_LAST_SUCCESSFUL_DT_TM, "20230820042649");
        System.setProperty(BatchConstants.EGRESS_GSOPSD_SEQ, "124");
        // run ExportGoldXmlJobProcessor
        ExportGoldXmlJobProcessor processor = applicationContext.getBean("exportGoldXmlJobProcessor", ExportGoldXmlJobProcessor.class);
        ReflectionTestUtils.setField(processor,"ctryRecCde","HK");
        ReflectionTestUtils.setField(processor,"grpMembrRecCde","HBAP");
        ReflectionTestUtils.setField(processor,"systemCde","GSOPSD");
        ExitStatus exitStatus = processor.afterStep(getStepExecution());
        Assertions.assertEquals(ExitStatus.COMPLETED, exitStatus);
        // check header file
        File[] files = outputPath.listFiles(((dir, name) -> name.endsWith("124.out")));
        Assertions.assertEquals(1, files.length);
    }

    @Test
    public void testReader_prodStatCde_empty() throws Exception {
        Mockito.when(productService.productByFilters(Mockito.anyMap())).thenReturn(Lists.newArrayList(originalProduct));

        Mockito.doNothing().when(legacyConfig).init();
        // run productItemReader
        GraphqlServiceItemReader reader = applicationContext.getBean("productItemReader", GraphqlServiceItemReader.class);
        // prodStatCde is empty, fileType is empty
        Assertions.assertDoesNotThrow(() -> {
            reader.open(getStepExecution().getExecutionContext());
        });
    }

    @Test
    public void testReader_prodStatCde_notEmpty() throws Exception {
        Mockito.when(productService.productByFilters(Mockito.anyMap())).thenReturn(Lists.newArrayList(originalProduct));

        Mockito.doNothing().when(legacyConfig).init();
        // run productItemReader
        GraphqlServiceItemReader reader = applicationContext.getBean("productItemReader", GraphqlServiceItemReader.class);
        Assertions.assertDoesNotThrow(() -> {
            reader.open(getStepExecution().getExecutionContext());
        });
    }

    @Test
    public void testReader_delta_timestampNotEmpty() throws Exception {
        Mockito.when(productService.productByFilters(Mockito.anyMap())).thenReturn(Lists.newArrayList(originalProduct));

        Mockito.doNothing().when(legacyConfig).init();
        // mock system parameter
        SystemParameter timestamp = JsonUtil.convertJson2Object(CommonUtils.readResource("/sys_parm_timestamp.json"), SystemParameter.class);
        Mockito.when(mongoOperations.findOne(Mockito.any(), Mockito.any())).thenReturn(timestamp);
        // run productItemReader
        GraphqlServiceItemReader reader = applicationContext.getBean("productItemReader", GraphqlServiceItemReader.class);
        Assertions.assertDoesNotThrow(() -> {
            reader.open(getStepExecution().getExecutionContext());
        });
    }

    @Test
    public void testWriter() {

        Mockito.doNothing().when(legacyConfig).init();
        // run exportGoldXmlJobWriter
        SynchronizedItemStreamWriter writer = applicationContext.getBean("exportGoldXmlJobWriter", SynchronizedItemStreamWriter.class);
        // Cover test failure in Jenkins (org.springframework.batch.item.ItemStreamException: Unable to create file)
        writer.open(getStepExecution().getExecutionContext());
        // check output file
        File[] files = outputPath.listFiles(((dir, name) -> name.endsWith(".xml")));
        Assertions.assertEquals(1, files.length);
    }
}
