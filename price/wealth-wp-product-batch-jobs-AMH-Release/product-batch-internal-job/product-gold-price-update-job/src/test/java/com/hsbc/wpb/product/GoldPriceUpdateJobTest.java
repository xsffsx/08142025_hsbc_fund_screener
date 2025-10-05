package com.dummy.wpb.product;

import com.google.common.collect.Lists;
import com.dummy.wpb.product.config.TokenizedGoldProductConfig;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.entity.TokenizedGoldProduct;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.util.LegacyConfig;
import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.utils.JsonUtil;
import org.bson.Document;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;

@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes = GoldPriceUpdateJobApplication.class)
@SpringJUnitConfig
@RunWith(SpringJUnit4ClassRunner.class)
public class GoldPriceUpdateJobTest {

    private static JobParameters jobParameters;

    private static Document originalProduct;

    private static MockedStatic<LegacyConfig> legacyConfigMockedStatic;

    @Autowired
    private ApplicationContext applicationContext;

    @MockBean
    private ProductService productService;

    @MockBean
    private LegacyConfig legacyConfig;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeClass
    public static void setUp() {
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .toJobParameters();
        legacyConfigMockedStatic = Mockito.mockStatic(LegacyConfig.class);
        legacyConfigMockedStatic.when(() -> LegacyConfig.getList(anyString())).thenReturn(Collections.emptyList());
        originalProduct = Document.parse(CommonUtils.readResource("/product-original.json"));
    }

    public JobExecution getJobExecution() {
        return MetaDataInstanceFactory.createJobExecution("goldPriceUpdateJob", 1L, 1L, jobParameters);
    }

    public StepExecution getStepExecution() {
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }

    @Test
    public void processSuccessTest1() throws Exception {
        Mockito.when(productService.productByFilters(Mockito.anyMap())).thenReturn(Lists.newArrayList(originalProduct));
        Mockito.when(productService.batchUpdateProductById(Mockito.anyList())).thenReturn(new ProductBatchUpdateResult());

        Mockito.doNothing().when(legacyConfig).init();
        // mock response body
        Document responseBody1 = JsonUtil.convertJson2Object(CommonUtils.readResource("/market-rate-response-body-1.json"), Document.class);
        ResponseEntity<Document> marketRateResponse1 = new ResponseEntity<>(responseBody1, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(Mockito.any(RequestEntity.class), Mockito.<Class<Document>>any()))
                .thenReturn(marketRateResponse1);
        // run goldPriceUpdateJobProcessor
        GoldPriceUpdateJobProcessor processor = applicationContext.getBean("goldPriceUpdateJobProcessor", GoldPriceUpdateJobProcessor.class);
        ProductStreamItem productStreamItem = processor.process(originalProduct);
        Document updatedProduct = Document.parse(CommonUtils.readResource("/product-updated-1.json"));
        // compare marketRate
        Map digtlAssetCcy1 = updatedProduct.get(Field.digtlAssetCcy, Map.class);
        Map digtlAssetCcy2 = productStreamItem.getUpdatedProduct().get(Field.digtlAssetCcy, Map.class);
        List expected = (List) digtlAssetCcy1.get(Field.marketRate);
        List actual = (List) digtlAssetCcy2.get(Field.marketRate);
        Assert.assertEquals(expected.size(), actual.size());
    }

    @Test
    public void processSuccessTest2() throws Exception {
        Mockito.when(productService.productByFilters(Mockito.anyMap())).thenReturn(Lists.newArrayList(originalProduct));
        Mockito.when(productService.batchUpdateProductById(Mockito.anyList())).thenReturn(new ProductBatchUpdateResult());

        Mockito.doNothing().when(legacyConfig).init();
        // mock response body
        Document responseBody1 = JsonUtil.convertJson2Object(CommonUtils.readResource("/market-rate-response-body-1.json"), Document.class);
        ResponseEntity<Document> marketRateResponse1 = new ResponseEntity<>(responseBody1, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(Mockito.any(RequestEntity.class), Mockito.<Class<Document>>any()))
                .thenReturn(marketRateResponse1);
        // run goldPriceUpdateJobProcessor
        GoldPriceUpdateJobProcessor processor = applicationContext.getBean("goldPriceUpdateJobProcessor", GoldPriceUpdateJobProcessor.class);
        ProductStreamItem productStreamItem = processor.process(originalProduct);
        Document updatedProduct = Document.parse(CommonUtils.readResource("/product-updated-2.json"));
        // compare marketRate
        Map digtlAssetCcy1 = updatedProduct.get(Field.digtlAssetCcy, Map.class);
        Map digtlAssetCcy2 = productStreamItem.getUpdatedProduct().get(Field.digtlAssetCcy, Map.class);
        List expected = (List) digtlAssetCcy1.get(Field.marketRate);
        List actual = (List) digtlAssetCcy2.get(Field.marketRate);
        Assert.assertEquals(expected.size(), actual.size());
    }

    @Test
    public void processFailureTest1() throws Exception {
        Mockito.when(productService.productByFilters(Mockito.anyMap())).thenReturn(Lists.newArrayList(originalProduct));
        Mockito.when(productService.batchUpdateProductById(Mockito.anyList())).thenReturn(new ProductBatchUpdateResult());

        Mockito.doNothing().when(legacyConfig).init();
        // mock http post exception
        Mockito.when(restTemplate.exchange(Mockito.any(RequestEntity.class), Mockito.<Class<Document>>any()))
                .thenThrow(RestClientException.class);
        // run goldPriceUpdateJobProcessor
        GoldPriceUpdateJobProcessor processor = applicationContext.getBean("goldPriceUpdateJobProcessor", GoldPriceUpdateJobProcessor.class);
        ProductStreamItem productStreamItem = processor.process(originalProduct);
        Assert.assertEquals(originalProduct, productStreamItem.getUpdatedProduct());
    }

    @Test
    public void processFailureTest2() throws Exception {
        Mockito.when(productService.productByFilters(Mockito.anyMap())).thenReturn(Lists.newArrayList(originalProduct));
        Mockito.when(productService.batchUpdateProductById(Mockito.anyList())).thenReturn(new ProductBatchUpdateResult());

        Mockito.doNothing().when(legacyConfig).init();
        // mock response body
        Document responseBody2 = JsonUtil.convertJson2Object(CommonUtils.readResource("/market-rate-response-body-2.json"), Document.class);
        ResponseEntity<Document> marketRateResponse2 = new ResponseEntity<>(responseBody2, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(Mockito.any(RequestEntity.class), Mockito.<Class<Document>>any()))
                .thenReturn(marketRateResponse2);
        // run goldPriceUpdateJobProcessor
        GoldPriceUpdateJobProcessor processor = applicationContext.getBean("goldPriceUpdateJobProcessor", GoldPriceUpdateJobProcessor.class);
        ProductStreamItem productStreamItem = processor.process(originalProduct);
        Document updatedProduct = Document.parse(CommonUtils.readResource("/product-updated-2.json"));
        // compare marketRate
        Map digtlAssetCcy1 = updatedProduct.get(Field.digtlAssetCcy, Map.class);
        Map digtlAssetCcy2 = productStreamItem.getUpdatedProduct().get(Field.digtlAssetCcy, Map.class);
        List expected = (List) digtlAssetCcy1.get(Field.marketRate);
        List actual = (List) digtlAssetCcy2.get(Field.marketRate);
        Assert.assertEquals(expected.size(), actual.size());
    }

    @Test
    public void getMarketRateTest() {
        GoldPriceUpdateJobService service = new GoldPriceUpdateJobService();
        Integer prodId = 123;
        // case1: prodId not match
        TokenizedGoldProduct gold = new TokenizedGoldProduct();
        gold.setProdId(124L);
        TokenizedGoldProductConfig config1 = new TokenizedGoldProductConfig();
        config1.setProducts(Collections.singletonList(gold));
        // reflect tokenizedGoldProductConfig
        ReflectionTestUtils.setField(service, "tokenizedGoldProductConfig", config1);
        Assert.assertEquals(0, service.getMarketRate(prodId).size());
        
        // case2: tokenizedGoldProductConfig.getProducts is null
        TokenizedGoldProductConfig config2 = new TokenizedGoldProductConfig();
        config2.setProducts(new ArrayList<>());
        // reflect tokenizedGoldProductConfig
        ReflectionTestUtils.setField(service, "tokenizedGoldProductConfig", config2);
        Assert.assertEquals(0, service.getMarketRate(prodId).size());
        
        // case3: tokenizedGoldProductConfig is null
        ReflectionTestUtils.setField(service, "tokenizedGoldProductConfig", null);
        Assert.assertEquals(0, service.getMarketRate(prodId).size());
    }
}
