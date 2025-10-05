package com.dummy.wpb.product;

import com.google.common.collect.Lists;
import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.service.impl.DefaultProductFormatService;
import com.dummy.wpb.product.util.LegacyConfig;
import com.dummy.wpb.product.util.ProductKeyUtils;
import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.utils.JsonPathUtils;
import com.dummy.wpb.product.utils.JsonUtil;
import com.dummy.wpb.product.writer.ProductImportItemWriter;
import org.bson.Document;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.dummy.wpb.product.constant.BatchConstants.ACTIVE;
import static com.dummy.wpb.product.constant.BatchConstants.DELISTED;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.batch.core.ExitStatus.COMPLETED;

@SpringJUnitConfig
@SpringBatchTest
@SpringBootTest(classes = ImportSecXmlJobApplication.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class ImportSecXmlJobTest {
    static MockedStatic<LegacyConfig> legacyConfigMockedStatic;

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean
    LegacyConfig legacyConfig;

    @MockBean
    ProductImportItemWriter productImportItemWriter;

    @MockBean
    ProductService productService;

    @MockBean(name = "productFormatService")
    DefaultProductFormatService productFormatService;

    @MockBean
    MongoTemplate mongoTemplate;

    @Autowired
    ApplicationContext applicationContext;

    static Document originalProduct;
    static Document importProduct;

    static JobParameters jobParameters;

    public JobExecution getJobExecution() {
        return MetaDataInstanceFactory.createJobExecution("importSecXmlFileJob", 1L, 1L, jobParameters);
    }

    public StepExecution getStepExecution() {
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }

    @BeforeClass
    public static void beforeClass() throws IOException {
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .addString("systemCde", "AMHGSOPS.AS")
                .addString("incomingPath", new ClassPathResource("/test").getFile().getAbsolutePath())
                .toJobParameters();

        legacyConfigMockedStatic = Mockito.mockStatic(LegacyConfig.class);
        originalProduct = Document.parse(CommonUtils.readResource("/product-original.json"));
        legacyConfigMockedStatic.when(() -> LegacyConfig.getList(anyString())).thenReturn(Collections.emptyList());
        ReflectionTestUtils.setField(ProductKeyUtils.class, "ignoreStatFilterList", Arrays.asList("D", "P"));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByProdCdeList", Collections.singletonList("W"));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByUniqueList", Collections.singletonList(""));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByPrimaryList", Arrays.asList("P", "T", "P"));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByAllList", Collections.singletonList("M"));
    }

    @AfterClass
    public static void afterClass() {
        legacyConfigMockedStatic.close();
    }

    @Before
    public void before() {
        Mockito.doCallRealMethod().when(productFormatService).formatByUpdateAttrs(any(),any(),anyList());
        importProduct = Document.parse(CommonUtils.readResource("/product-import.json"));
     }

    @Test
    public void testSecXmlFileJob() throws Exception {
        Mockito.when(productService.productByFilters(Mockito.anyMap(), anyList())).thenReturn(Lists.newArrayList(originalProduct));

        Mockito.when(productFormatService.initProduct(any())).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.doNothing().when(legacyConfig).init();
        Mockito.when(mongoTemplate.exists((Query) any(), (String) any()))
                .thenReturn(true)
                .thenReturn(false);
        Job importSecXmlFileJob = applicationContext.getBean("importSecXmlFileJob", Job.class);
        jobLauncherTestUtils.setJob(importSecXmlFileJob);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        Assert.assertEquals(COMPLETED, jobExecution.getExitStatus());
        StepExecution stepExecution = new ArrayList<>(jobExecution.getStepExecutions()).get(0);
        Assert.assertTrue(stepExecution.getFilterCount() > 0);
    }

    @Test
    public void testSecXmlFileProcessor_update_expectFormat() throws Exception {
        SecXmlFileProcessor secXmlFileProcessor = applicationContext.getBean("secXmlFileProcessor", SecXmlFileProcessor.class);
        ProductStreamItem productStreamItem = new ProductStreamItem();
        productStreamItem.setImportProduct(importProduct);
        productStreamItem.setOriginalProduct(originalProduct);
        productStreamItem.setUpdatedProduct(JsonUtil.deepCopy(originalProduct));
        productStreamItem.setActionCode(BatchImportAction.UPDATE);
        productStreamItem.setParams(Collections.singletonMap("fileName", "HK_HBAP_AMHGSOPS.AS_SEC~CN_20230411094503.xml"));
        secXmlFileProcessor.process(productStreamItem);

        Document updatedProduct = productStreamItem.getUpdatedProduct();
        Assert.assertNotNull(updatedProduct);
        Assert.assertEquals(JsonPathUtils.readValue(importProduct, "stockInstm.mktProdTrdCde", ""),
                JsonPathUtils.readValue(updatedProduct, "stockInstm.mktProdTrdCde", ""));
        Assert.assertEquals("Y", updatedProduct.get(Field.allowBuyProdInd));

        importProduct.put(Field.prodStatCde, DELISTED);
        secXmlFileProcessor.process(productStreamItem);
        Assert.assertEquals("N", importProduct.get(Field.allowBuyProdInd));

        importProduct.put(Field.prodStatCde, "");
        secXmlFileProcessor.process(productStreamItem);
        Assert.assertEquals(originalProduct.get(Field.allowBuyProdInd), importProduct.get(Field.allowBuyProdInd));
    }


    @Test
    public void testSecXmlFileProcessor_update_expectNotFormat() throws Exception {
        SecXmlFileProcessor secXmlFileProcessor = applicationContext.getBean("secXmlFileProcessor", SecXmlFileProcessor.class);
        ProductStreamItem productStreamItem = new ProductStreamItem();
        importProduct.put(Field.prodStatCde, "E");
        productStreamItem.setImportProduct(importProduct);
        productStreamItem.setOriginalProduct(originalProduct);
        productStreamItem.setUpdatedProduct(JsonUtil.deepCopy(originalProduct));
        productStreamItem.setActionCode(BatchImportAction.UPDATE);
        productStreamItem.setParams(Collections.singletonMap("fileName", "HK_HBAP_AMHGSOPS.AS_SEC~JP_20230411094503.xml"));

        String allowBuyProdInd = importProduct.getString(Field.allowBuyProdInd);
        secXmlFileProcessor.process(productStreamItem);

        Document updatedProduct = productStreamItem.getUpdatedProduct();
        Assert.assertNotNull(updatedProduct);
        Assert.assertEquals(allowBuyProdInd, importProduct.getString(Field.allowBuyProdInd));
    }

    @Test
    public void testSecXmlFileProcessor_create_expectFormat() throws Exception {
        Mockito.when(productFormatService.initProduct(any())).thenReturn(importProduct);
        SecXmlFileProcessor secXmlFileProcessor = applicationContext.getBean("secXmlFileProcessor", SecXmlFileProcessor.class);
        ProductStreamItem productStreamItem = new ProductStreamItem();
        productStreamItem.setImportProduct(importProduct);
        productStreamItem.setActionCode(BatchImportAction.ADD);
        productStreamItem.setParams(Collections.singletonMap("fileName", "HK_HBAP_AMHGSOPS.AS_SEC~CN_20230411094503.xml"));
        secXmlFileProcessor.process(productStreamItem);

        importProduct.put(Field.prodStatCde, ACTIVE);
        Assert.assertEquals("Y", importProduct.get(Field.allowBuyProdInd));

        importProduct.put(Field.prodStatCde, DELISTED);
        secXmlFileProcessor.process(productStreamItem);
        Assert.assertEquals("N", importProduct.get(Field.allowBuyProdInd));

    }
}
