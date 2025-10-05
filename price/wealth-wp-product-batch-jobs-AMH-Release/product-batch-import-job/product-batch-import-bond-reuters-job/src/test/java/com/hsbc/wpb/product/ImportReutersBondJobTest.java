package com.dummy.wpb.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ExcelColumnInfo;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.model.graphql.ReferenceData;
import com.dummy.wpb.product.service.ProductFormatService;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.service.ReferenceDataService;
import com.dummy.wpb.product.service.impl.DefaultProductFormatService;
import com.dummy.wpb.product.util.LegacyConfig;
import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.utils.JsonUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.bson.Document;
import org.junit.*;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.*;

import static com.dummy.wpb.product.constant.BatchConstants.EXPIRED;
import static org.mockito.ArgumentMatchers.*;

@SpringJUnitConfig
@SpringBatchTest
@SpringBootTest(classes = ImportReutersBondJobApplication.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class ImportReutersBondJobTest {
    static MockedStatic<LegacyConfig> legacyConfigMockedStatic;

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean
    LegacyConfig legacyConfig;

    @MockBean
    ProductService productService;
    @MockBean(name = "productFormatService")
    DefaultProductFormatService productFormatService;

    @MockBean
    ReferenceDataService referenceDataService;

    @Autowired
    ApplicationContext applicationContext;

    static Document originalProduct;
    static Document importProduct;
    static TypeReference<List<ExcelColumnInfo>> columnInfoTypeReference = new TypeReference<List<ExcelColumnInfo>>() {
    };
    List<List<ExcelColumnInfo>> excelColumnInfos;

    static JobParameters jobParameters;


    public JobExecution getJobExecution() {
        return MetaDataInstanceFactory.createJobExecution("importbondReutersFileJob", 1L, 1L, jobParameters);
    }

    public StepExecution getStepExecution() {
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }

    @Before
    public void before() {
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .addString("systemCde", "REUTERSBOND")
                .toJobParameters();

        legacyConfigMockedStatic = Mockito.mockStatic(LegacyConfig.class);
        originalProduct = Document.parse(CommonUtils.readResource("/product-original.json"));
        legacyConfigMockedStatic.when(() -> LegacyConfig.getList(anyString())).thenReturn(Collections.emptyList());
        List<ExcelColumnInfo> excelColumnInfo = JsonUtil.convertJson2Object(CommonUtils.readResource("/excel-column-info.json"), columnInfoTypeReference);
        excelColumnInfos = Collections.singletonList(excelColumnInfo);

        Mockito.doCallRealMethod().when(productFormatService).formatByUpdateAttrs(any(), any(), anyCollection());
        importProduct = Document.parse(CommonUtils.readResource("/product-import.json"));
    }

    @Value("${BOND.REUTERS.FILENAME}")
    private String fileName = null;

    @Autowired
    DefaultResourceLoader resourceLoader;

    @Test
        public void testBondReutersFileReader() throws Exception {
        Mockito.when(productService.productByFilters(Mockito.anyMap(), Mockito.anyList())).thenReturn(Lists.newArrayList(originalProduct));
        Mockito.doNothing().when(legacyConfig).init();
        RestTemplate bondRestTemplate = Mockito.mock(RestTemplate.class);
        BondReutersService bondReutersService = applicationContext.getBean("bondReutersService", BondReutersService.class);
        //Mockito.when(bondReutersService.run()).thenReturn(excelColumnInfos);
        java.lang.reflect.Field restTemplate = bondReutersService.getClass().getDeclaredField("bondRestTemplate");
        restTemplate.setAccessible(true);
        restTemplate.set(bondReutersService, bondRestTemplate);

        java.lang.reflect.Field path = bondReutersService.getClass().getDeclaredField("wpcLocalPath");
        path.setAccessible(true);
        String newpath = new ClassPathResource("/").getFile().getAbsolutePath();
        path.set(bondReutersService, newpath);
        Map mockToken = JsonUtil.convertJson2Object(CommonUtils.readResource("/mock-token.json"), new TypeReference<Map>() {
        });
        ResponseEntity<Map<String, Object>> token = new ResponseEntity<>(mockToken, HttpStatus.OK);
        Mockito.when(bondRestTemplate.exchange(Mockito.any(RequestEntity.class), Mockito.<ParameterizedTypeReference<Map<String, Object>>>any())).thenReturn(token);

        Map<String, Object> mockScheduleId = JsonUtil.convertJson2Object(CommonUtils.readResource("/mock-scheduleId.json"), new TypeReference<Map<String, Object>>() {
        });
        ResponseEntity<Map<String, Object>> scheduleId = new ResponseEntity<>(mockScheduleId, HttpStatus.OK);
        Mockito.when(bondRestTemplate.exchange(
                ArgumentMatchers.argThat(argument -> null != argument && argument.getPath().contains("ScheduleGetByName")),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ParameterizedTypeReference<Map<String, Object>>>any()
        )).thenReturn(scheduleId);

        Map<String, Object> mockReportId = JsonUtil.convertJson2Object(CommonUtils.readResource("/mock-reportId.json"), new TypeReference<Map<String, Object>>() {
        });
        ResponseEntity<Map<String, Object>> reportId = new ResponseEntity<>(mockReportId, HttpStatus.OK);
        Mockito.when(bondRestTemplate.exchange(anyString(),
                        ArgumentMatchers.any(HttpMethod.class),
                        ArgumentMatchers.any(),
                        ArgumentMatchers.eq(new ParameterizedTypeReference<Map<String, Object>>() {
                        })))
                .thenReturn(reportId);


        //InputStream mockReport = ImportReutersBondJobTest.class.getResourceAsStream("/mock-report.csv");
        Resource mockReport = this.resourceLoader.getResource("/mock-report.csv");
        //Resource rpt =
        ResponseEntity<Resource> report = new ResponseEntity<>(mockReport, HttpStatus.OK);
        Mockito.when(bondRestTemplate.exchange(
                        anyString(),
                        ArgumentMatchers.any(HttpMethod.class),
                        ArgumentMatchers.any(),
                        ArgumentMatchers.<Class<Resource>>any()))
                .thenReturn(report);

        List<ReferenceData> referenceData = JsonUtil.convertJson2Object(CommonUtils.readResource("/ref-data.json"), new TypeReference<List<ReferenceData>>() {
        });
        Mockito.when(referenceDataService.referenceDataByFilter(Mockito.anyMap())).thenReturn(referenceData);

        ProductBatchUpdateResult result = new ProductBatchUpdateResult();
        result.setUpdatedProducts(Collections.singletonList(
                new Document().append(Field.ctryRecCde, "HK")
                        .append(Field.grpMembrRecCde, "HBAP")
                        .append(Field.prodTypeCde, "BOND")
                        .append(Field.prodAltPrimNum, "US404280AG49")
        ));
        result.setInvalidProducts(Collections.emptyList());
        Mockito.when(productService.batchUpdateProductById(any())).thenReturn(result);

        Job importBondCsvJob = applicationContext.getBean("importBondCsvJob", Job.class);

        try (MockedStatic<DateFormatUtils> dateFormatUtilsMockedStatic = Mockito.mockStatic(DateFormatUtils.class)){
            String dateStr = "20240314_095159";
            Mockito.when(DateFormatUtils.format(any(Date.class), eq("yyyyMMdd_HHmmss"))).thenReturn(dateStr);
            String filePath =newpath + File.separator + fileName + "_" + dateStr + ".bak";
            new File(filePath).createNewFile();
            jobLauncherTestUtils.setJob(importBondCsvJob);
            jobLauncherTestUtils.launchJob(jobParameters);
        }
        Mockito.verify(productService, Mockito.atLeast(1)).batchUpdateProductById(any());
    }

    @Test
    public void testBondReutersProcessor_update_ExceptionCase() throws Exception {
        Mockito.when(referenceDataService.referenceDataByFilter(Mockito.anyMap())).thenReturn(null);
        ItemProcessor bondReutersProcessor = applicationContext.getBean("bondReutersProcessor", ItemProcessor.class);
        BondReutersStreamItem productStreamItem = new BondReutersStreamItem();
        productStreamItem.setImportProduct(importProduct);
        productStreamItem.setOriginalProduct(originalProduct);
        productStreamItem.setActionCode(BatchImportAction.UPDATE);

        Assertions.assertNull(bondReutersProcessor.process(productStreamItem));

        originalProduct.put(Field.prodStatCde, EXPIRED);
        productStreamItem.setOriginalProduct(originalProduct);
        Assertions.assertNull(bondReutersProcessor.process(productStreamItem));

        productStreamItem.setOriginalProduct(null);
        Assertions.assertNull(bondReutersProcessor.process(productStreamItem));
    }

    @After
    public void after() {
        legacyConfigMockedStatic.close();
    }
}
