package com.dummy.wpb.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.model.ProductPriceStreamItem;
import com.dummy.wpb.product.model.graphql.ProductPriceHistory;
import com.dummy.wpb.product.processor.MdsbePriceProcessor;
import com.dummy.wpb.product.processor.ProductPriceValidator;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.service.ProductPriceHistoryService;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.service.impl.DefaultProductFormatService;
import com.dummy.wpb.product.util.LegacyConfig;
import com.dummy.wpb.product.util.ProductKeyUtils;
import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.utils.JsonUtil;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
@SpringBatchTest
@SpringBootTest(classes = ImportProductPriceXmlJobApplication.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class ImportProductPriceXmlJobTest {


    MockedStatic<LegacyConfig> legacyConfigMockedStatic;

    JobParameters jobParameters = null;
    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean
    LegacyConfig legacyConfig;

    @MockBean(name = "productFormatService")
    DefaultProductFormatService productFormatService;

    @MockBean
    ProductService productService;

    @MockBean
    private ProductPriceHistoryService productPriceHistoryService;

    @MockBean
    GraphQLService graphQLService;


    @Autowired
    ApplicationContext applicationContext;

    static JobExecution jobExecution;


    @Before
    public void setUp()  {
        jobExecution = MetaDataInstanceFactory.createJobExecution("importProductPriceJob", 1L, 1L, jobParameters);
        legacyConfigMockedStatic = Mockito.mockStatic(LegacyConfig.class);
        legacyConfigMockedStatic.when(() -> LegacyConfig.getList(anyString())).thenReturn(Collections.emptyList());
        ReflectionTestUtils.setField(ProductKeyUtils.class, "ignoreStatFilterList", Arrays.asList("D", "P"));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByProdCdeList", Collections.singletonList("W"));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByUniqueList", Collections.singletonList(""));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByPrimaryList", Arrays.asList("P", "T", "P"));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByAllList", Collections.singletonList("M"));
    }

    public JobExecution getJobExecution() {
        return jobExecution;
    }

    public StepExecution getStepExecution() {
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }


    @Test
    @DirtiesContext
    public void testImportProductPriceJob_givenAMHCUTASSystem() throws Exception {
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .addString("systemCde", "AMHCUTAS")
                .addString("incomingPath", new ClassPathResource("/test").getFile().getAbsolutePath())
                .toJobParameters();
        List<Document> productList = JsonUtil.convertJson2Object(CommonUtils.readResource("/response/product-AMHCUTAS.json"), new TypeReference<List<Document>>() {
        });
        when(productService.productByFilters(Mockito.anyMap(), anyList())).thenReturn(productList);
        when(productPriceHistoryService.productPriceHistoryByFilter(anyMap()))
                .thenReturn(JsonUtil.convertJson2Object(CommonUtils.readResource("/response/product-price-history-U62448.json"), new TypeReference<List<ProductPriceHistory>>() {
                }));
        when(graphQLService.executeRequest(argThat(argument -> argument != null && argument.getQuery().contains("sysParmByFilter")), Mockito.<TypeReference<List<Document>>>any()))
                .thenReturn(JsonUtil.convertJson2Object(CommonUtils.readResource("/response/sys_parm_price.json"), new TypeReference<List<Document>>() {
                }));

        when(graphQLService.executeRequest(argThat(argument -> argument != null && argument.getQuery().contains("productPriceBatchUpdate")), Mockito.<TypeReference<Map<String, Object>>>any()))
                .thenReturn(JsonUtil.convertJson2Object(CommonUtils.readResource("/response/product-price-import-U62448.json"), new TypeReference<Map<String, Object>>() {
                }));

        doCallRealMethod().when(productFormatService).formatByUpdateAttrs(any(), any(), anyCollection());
        Job importProductPriceJob = applicationContext.getBean("importProductPriceJob", Job.class);
        jobLauncherTestUtils.setJob(importProductPriceJob);
        jobLauncherTestUtils.launchJob(jobParameters);

        ArgumentCaptor<GraphQLRequest> captor = ArgumentCaptor.forClass(GraphQLRequest.class);
        Mockito.verify(graphQLService, atLeast(1)).executeRequest(captor.capture(), Mockito.<TypeReference<Map<String, Object>>>any());
        GraphQLRequest graphQLRequest = captor.getValue();
        Assertions.assertEquals("productPriceBatchUpdate", graphQLRequest.getOperationName());
        Map<String, Object> variables = graphQLRequest.getVariables();
        Assertions.assertNotNull(variables.get("updateParams"));
    }

    @Test
    @DirtiesContext
    public void testImportProductPriceJob_givenMANLSystem() throws Exception {
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .addString("systemCde", "MANL")
                .addString("incomingPath", new ClassPathResource("/test").getFile().getAbsolutePath())
                .toJobParameters();

        Document product = Document.parse(CommonUtils.readResource("/response/product-RBSIN24A0033.json"));
        when(productService.productByFilters(Mockito.anyMap(), anyList())).thenReturn(Lists.newArrayList(product));

        when(productPriceHistoryService.productPriceHistoryByFilter(anyMap())).thenReturn(Collections.emptyList());
        when(graphQLService.executeRequest(argThat(argument -> argument != null && argument.getQuery().contains("sysParmByFilter")), Mockito.<TypeReference<List<Document>>>any()))
                .thenReturn(JsonUtil.convertJson2Object(CommonUtils.readResource("/response/sys_parm_price.json"), new TypeReference<List<Document>>() {
                }));

        when(graphQLService.executeRequest(argThat(argument -> argument != null && argument.getQuery().contains("productPriceBatchUpdate")), Mockito.<TypeReference<Map<String, Object>>>any()))
                .thenReturn(JsonUtil.convertJson2Object(CommonUtils.readResource("/response/product-price-import-RBSIN24A0033.json"), new TypeReference<Map<String, Object>>() {
                }));

        doCallRealMethod().when(productFormatService).formatByUpdateAttrs(any(), any(), anyCollection());
        Job importProductPriceJob = applicationContext.getBean("importProductPriceJob", Job.class);
        jobLauncherTestUtils.setJob(importProductPriceJob);
        jobLauncherTestUtils.launchJob(jobParameters);

        ArgumentCaptor<GraphQLRequest> captor = ArgumentCaptor.forClass(GraphQLRequest.class);
        Mockito.verify(graphQLService, atLeast(1)).executeRequest(captor.capture(), Mockito.<TypeReference<Map<String, Object>>>any());
        GraphQLRequest graphQLRequest = captor.getValue();
        Assertions.assertEquals("productPriceBatchUpdate", graphQLRequest.getOperationName());
        Map<String, Object> variables = graphQLRequest.getVariables();
        Assertions.assertNotNull(variables.get("updateParams"));
    }


    @Test
    public void testProductPriceValidator() {
        ProductPriceValidator productPriceValidator = new ProductPriceValidator();
        ProductPriceStreamItem priceStreamItem = new ProductPriceStreamItem();
        priceStreamItem.setImportProduct(Document.parse(CommonUtils.readResource("/response/product-RBSIN24A0033.json")));

        priceStreamItem.setActionCode(BatchImportAction.ADD);
        Assert.assertThrows(ValidationException.class, () -> {
            productPriceValidator.validate(priceStreamItem);
        });

        Document priceHistory = new Document();
        priceStreamItem.setImportPriceHistory(Collections.singletonList(priceHistory));
        priceStreamItem.setActionCode(BatchImportAction.UPDATE);
        Assert.assertThrows(ValidationException.class, () -> {
            productPriceValidator.validate(priceStreamItem);
        });


        priceHistory.put(Field.prcEffDt, LocalDate.now());
        priceHistory.put(Field.prcInpDt, LocalDate.now());
        priceHistory.put(Field.prodBidPrcAmt, new BigDecimal("1.23"));
        Assert.assertThrows(ValidationException.class, () -> {
            productPriceValidator.validate(priceStreamItem);
        });

        priceStreamItem.getImportProduct().put(Field.prodTypeCde, "UT");
        priceHistory.put(Field.prodNavPrcAmt,new BigDecimal(-1));
        priceHistory.put(Field.ccyProdMktPrcCde,"HKD");
        priceStreamItem.setImportPriceHistory(Collections.singletonList(priceHistory));
        priceStreamItem.setActionCode(BatchImportAction.UPDATE);
        Assert.assertThrows(ValidationException.class, () -> {
            productPriceValidator.validate(priceStreamItem);
        });

    }

    @Test
    public void testMdsbePriceFormatter() {
        MdsbePriceProcessor mdsbePriceProcessor = new MdsbePriceProcessor();
        ProductPriceStreamItem priceStreamItem = new ProductPriceStreamItem();
        Document importedProduct = new Document();
        Document updatedProduct = new Document();
        priceStreamItem.setImportProduct(importedProduct);
        priceStreamItem.setUpdatedProduct(updatedProduct);
        mdsbePriceProcessor.process(priceStreamItem);
        Map<String, ?> debtInstm = (Map<String, ?>) priceStreamItem.getUpdatedProduct().get(Field.debtInstm);
        Assert.assertNull(debtInstm.get(Field.prodCloseOffrPrcAmt));
        
        importedProduct.put(Field.prodBidPrcAmt, 100.2);
        importedProduct.put(Field.prodOffrPrcAmt, 99.9);
        mdsbePriceProcessor.process(priceStreamItem);
        debtInstm = (Map<String, ?>) priceStreamItem.getUpdatedProduct().get(Field.debtInstm);
        Assert.assertEquals(100.2, debtInstm.get(Field.prodCloseBidPrcAmt));
        Assert.assertEquals(99.9, debtInstm.get(Field.prodCloseOffrPrcAmt));
    }

    @After
    public void after() {
        legacyConfigMockedStatic.close();
    }
}
