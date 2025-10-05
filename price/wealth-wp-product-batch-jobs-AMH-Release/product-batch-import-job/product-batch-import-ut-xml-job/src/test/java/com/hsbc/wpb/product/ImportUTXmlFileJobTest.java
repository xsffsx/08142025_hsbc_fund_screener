package com.dummy.wpb.product;

import com.google.common.collect.ImmutableMap;
import com.dummy.wpb.product.component.SystemFormatProcessor;
import com.dummy.wpb.product.configuration.FundHouseCdeHolder;
import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.model.graphql.ProductBatchCreateResult;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.model.graphql.ReferenceData;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.service.ReferenceDataService;
import com.dummy.wpb.product.service.impl.DefaultProductFormatService;
import com.dummy.wpb.product.util.LegacyConfig;
import com.dummy.wpb.product.util.ProductKeyUtils;
import com.dummy.wpb.product.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonArray;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBatchTest
@SpringBootTest(classes = ImportUtXmlJobApplication.class)
@ActiveProfiles("test")
class ImportUTXmlFileJobTest {

    static MockedStatic<LegacyConfig> legacyConfigMockedStatic;

    JobParameters jobParameters;
    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean
    LegacyConfig legacyConfig;

    @MockBean(name = "productFormatService")
    DefaultProductFormatService productFormatService;

    @MockBean
    ProductService productService;

    @MockBean
    ReferenceDataService referenceDataService;

    @Autowired
    ApplicationContext applicationContext;

    static JobExecution jobExecution;

    @BeforeEach
    public void setUp() {
        legacyConfigMockedStatic = Mockito.mockStatic(LegacyConfig.class);
        Mockito.doNothing().when(productFormatService).init();
        Mockito.doCallRealMethod().when(productFormatService).formatByUpdateAttrs(any(), any(), anyCollection());
        legacyConfigMockedStatic.when(() -> LegacyConfig.getList(anyString())).thenReturn(Collections.emptyList());
        ReflectionTestUtils.setField(ProductKeyUtils.class, "ignoreStatFilterList", Arrays.asList("D", "P"));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByProdCdeList", Collections.singletonList("W"));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByUniqueList", Collections.singletonList(""));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByPrimaryList", Arrays.asList("P", "T", "P"));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByAllList", Collections.singletonList("M"));
    }

    @AfterEach
    public void cleanUp() {
        legacyConfigMockedStatic.close();
    }

    @Test
    @DirtiesContext
    void testImportUtXmlFileJob_givenCUTASSystem() throws Exception {
        List<Document> productList = BsonArray.parse(CommonUtils.readResource("/response/product.json"))
                .getValues()
                .stream()
                .map(item -> Document.parse(item.asDocument().toJson()))
                .collect(Collectors.toList());
        when(productService.productByFilters(Mockito.anyMap(), anyList())).thenReturn(productList);

        when(productFormatService.initProduct(any())).thenAnswer(invocation -> invocation.getArgument(0));
        ProductBatchUpdateResult updateResult = new ProductBatchUpdateResult();
        updateResult.setInvalidProducts(Collections.emptyList());
        updateResult.setUpdatedProducts(productList);
        when(productService.batchUpdateProductById(any())).thenReturn(updateResult);

        when(productService.batchCreateProduct(anyList())).thenAnswer(invocation -> {
            List<Map<String, Object>> createProducts = invocation.getArgument(0, List.class);
            ProductBatchCreateResult createResult = new ProductBatchCreateResult();
            createResult.setCreatedProducts(createProducts.stream().map(Document::new).collect(Collectors.toList()));
            createResult.setInvalidProducts(Collections.emptyList());
            return createResult;
        });

        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .addString("systemCde", "AMHCUTAS")
                .addString("incomingPath", new ClassPathResource("/test").getFile().getAbsolutePath())
                .toJobParameters();

        Job importUtXmlFileJob = applicationContext.getBean("importUtXmlFileJob", Job.class);
        jobLauncherTestUtils.setJob(importUtXmlFileJob);
        jobLauncherTestUtils.launchJob(jobParameters);

        Mockito.verify(productService, times(1)).batchCreateProduct(anyList());

        Mockito.verify(productService, times(1)).batchUpdateProductById(anyList());
    }

    @Test
    @DirtiesContext
    void testImportUtXmlFileJob_givenMSSystem() throws Exception {
        List<Document> productList = BsonArray.parse(CommonUtils.readResource("/response/product.json"))
                .getValues()
                .stream()
                .map(item -> Document.parse(item.asDocument().toJson()))
                .collect(Collectors.toList());
        when(productService.productByFilters(Mockito.anyMap(), anyList())).thenReturn(productList);

        when(productFormatService.initProduct(any())).thenAnswer(invocation -> invocation.getArgument(0));
        ProductBatchUpdateResult updateResult = new ProductBatchUpdateResult();
        updateResult.setInvalidProducts(Collections.emptyList());
        updateResult.setUpdatedProducts(productList);
        when(productService.batchUpdateProductById(any())).thenReturn(updateResult);

        when(productService.batchCreateProduct(anyList())).thenAnswer(invocation -> {
            List<Map<String, Object>> createProducts = invocation.getArgument(0, List.class);
            ProductBatchCreateResult createResult = new ProductBatchCreateResult();
            createResult.setCreatedProducts(createProducts.stream().map(Document::new).collect(Collectors.toList()));
            createResult.setInvalidProducts(Collections.emptyList());
            return createResult;
        });

        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .addString("systemCde", "MS")
                .addString("incomingPath", new ClassPathResource("/test").getFile().getAbsolutePath())
                .toJobParameters();

        Job importUtXmlFileJob = applicationContext.getBean("importUtXmlFileJob", Job.class);
        jobLauncherTestUtils.setJob(importUtXmlFileJob);
        jobLauncherTestUtils.launchJob(jobParameters);
        Mockito.verify(productService, times(1)).batchUpdateProductById(anyList());
    }

    @Test
    void testFundHouseCdeHolder() throws Exception {
        ReferenceData referenceData = new ReferenceData();
        referenceData.setCdvCde("efg");
        referenceData.setCdvDesc("abc");
        Mockito.when(referenceDataService.referenceDataByFilter(anyMap())).thenReturn(Collections.singletonList(referenceData));
        FundHouseCdeHolder fundHouseCdeHolder = new FundHouseCdeHolder(referenceDataService);
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP").toJobParameters();
        jobExecution = MetaDataInstanceFactory.createJobExecution("importUtXmlFileJob", 1L, 1L, jobParameters);
        fundHouseCdeHolder.beforeJob(jobExecution);
        assertEquals("abc", FundHouseCdeHolder.getFundHouseCde("efg"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "P", "C", "D", "S", "T"})
    void testUpdateFormat(String prodStatCde) {
        SystemFormatProcessor systemFormatProcessor = applicationContext.getBean("systemFormatProcessor", SystemFormatProcessor.class);
        ProductStreamItem productStreamItem = new ProductStreamItem();
        Document importProduct = new Document();
        importProduct.put("prodStatCde", prodStatCde);
        productStreamItem.setImportProduct(importProduct);
        productStreamItem.setOriginalProduct(new Document());
        productStreamItem.setActionCode(BatchImportAction.UPDATE);

        String[] allowFields = ReflectionTestUtils.invokeMethod(systemFormatProcessor, "updateFormat", productStreamItem);
        Assertions.assertNotNull(allowFields);
        Assertions.assertEquals(5, allowFields.length);
    }

    @ParameterizedTest
    @ValueSource(strings = {"HA", "GE", "BD", "GF", "MR"})
    void testInitFormatField(String prodSubtpCde) {
        SystemFormatProcessor systemFormatProcessor = applicationContext.getBean("systemFormatProcessor", SystemFormatProcessor.class);
        ProductStreamItem productStreamItem = new ProductStreamItem();
        Document importProduct = new Document();
        importProduct.put("prodSubtpCde", prodSubtpCde);
        productStreamItem.setImportProduct(importProduct);
        productStreamItem.setActionCode(BatchImportAction.ADD);
        ReflectionTestUtils.invokeMethod(systemFormatProcessor, "initFormatField", productStreamItem);
        if ("HA".equals(prodSubtpCde)) {
            assertNull(importProduct.get("undlAset"));
        } else {
            assertNotNull(importProduct.get("undlAset"));
        }
    }


    @ParameterizedTest
    @CsvSource({",Y", ",N", "D,Y", "S,Y", "C,Y", "T,Y", "A,Y"})
    void testSystemFormatProcessorAddFormat(String prodStatCde, String closeEndFundInd) throws Exception {
        ProductStreamItem productStreamItem = new ProductStreamItem();
        Document importProd = new Document();
        Document updatedProd = new Document();
        Map<String, String> utTrstInstm = ImmutableMap.of("closeEndFundInd", closeEndFundInd, "suptMipInd", "N");
        updatedProd.put("utTrstInstm", utTrstInstm);
        importProd.put("prodStatCde", prodStatCde);
        productStreamItem.setImportProduct(importProd);
        productStreamItem.setUpdatedProduct(updatedProd);
        productStreamItem.setActionCode(BatchImportAction.ADD);

        SystemFormatProcessor systemFormatProcessor = applicationContext.getBean("systemFormatProcessor", SystemFormatProcessor.class);

        String[] fields = ReflectionTestUtils.invokeMethod(systemFormatProcessor, "addFormat", productStreamItem);

        prodStatCde = StringUtils.isBlank(prodStatCde) ? "" : prodStatCde;
        switch (prodStatCde) {
            case "":
                if (closeEndFundInd.equals("Y")) {
                    assertEquals("Y", fields[1]);
                } else {
                    assertEquals("Y", fields[1]);
                }
                break;
            case "D":
                assertArrayEquals(new String[]{"N", "Y", "N", "N", "N"}, fields);
                break;
            case "S":
                assertArrayEquals(new String[]{"N", "N", null, "N", "N"}, fields);
                break;
            case "C":
                assertArrayEquals(new String[]{"N", "Y", "N", "N", "Y"}, fields);
                break;
            case "T":
                assertArrayEquals(new String[]{"N", "N", "N", "N", "N"}, fields);
                break;
            default:
                assertArrayEquals(new String[]{}, fields);
                break;
        }

    }
}
