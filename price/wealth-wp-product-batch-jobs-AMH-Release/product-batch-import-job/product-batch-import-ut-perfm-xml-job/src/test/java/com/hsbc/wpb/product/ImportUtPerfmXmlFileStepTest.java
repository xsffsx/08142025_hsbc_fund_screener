package com.dummy.wpb.product;

import com.google.common.collect.Lists;
import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.util.LegacyConfig;
import com.dummy.wpb.product.util.ProductKeyUtils;
import com.dummy.wpb.product.utils.CommonUtils;
import org.bson.Document;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.batch.core.ExitStatus.COMPLETED;

@SpringJUnitConfig
@SpringBatchTest
@SpringBootTest(classes = ImportUtPerfmXmlJobApplication.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class ImportUtPerfmXmlFileStepTest {

    static MockedStatic<LegacyConfig> legacyConfigMockedStatic;

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean
    LegacyConfig legacyConfig;

    @MockBean
    ProductService productService;

    @Autowired
    ApplicationContext applicationContext;

    static Document originalProduct;
    static Document importProduct;

    static JobParameters jobParameters;


    @BeforeClass
    public static void setUp() throws IOException {
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .addString("systemCde", "AMHCUTAS")
                .addString("incomingPath", new ClassPathResource("/test").getFile().getAbsolutePath())
                .toJobParameters();

        legacyConfigMockedStatic = Mockito.mockStatic(LegacyConfig.class);
        originalProduct = Document.parse(CommonUtils.readResource("/product-original.json"));
        importProduct = Document.parse(CommonUtils.readResource("/product-import.json"));
        legacyConfigMockedStatic.when(() -> LegacyConfig.getList(anyString())).thenReturn(Collections.emptyList());
        ReflectionTestUtils.setField(ProductKeyUtils.class, "ignoreStatFilterList", Arrays.asList("D", "P"));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByProdCdeList", Collections.singletonList("W"));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByUniqueList", Collections.singletonList(""));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByPrimaryList", Arrays.asList("P", "T", "P"));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByAllList", Collections.singletonList("M"));
    }

    public JobExecution getJobExecution() {
        return MetaDataInstanceFactory.createJobExecution("importUtPerfmXmlFileJob", 1L, 1L, jobParameters);
    }

    public StepExecution getStepExecution() {
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }


    @Test
    public void testUtPerfmXmlReader() throws Exception {
        Mockito.when(productService.productByFilters(Mockito.anyMap(), anyList())).thenReturn(Lists.newArrayList(originalProduct));

        Mockito.doNothing().when(legacyConfig).init();
        ItemStreamReader<ProductStreamItem> utPerfmXmlFileReader = applicationContext.getBean("utPerfmXmlFileReader", ItemStreamReader.class);
        utPerfmXmlFileReader.open(new ExecutionContext());
        ProductStreamItem streamItem = utPerfmXmlFileReader.read();
        Assert.assertEquals(BatchImportAction.UPDATE, streamItem.getActionCode());

        List<Document> performance = streamItem.getImportProduct().getList("performance", Document.class);
        Assert.assertEquals(2, performance.size());
        Assert.assertTrue(performance.stream().anyMatch(item -> "P".equals(item.getString("perfmTypeCde"))));
        Assert.assertTrue(performance.stream().anyMatch(item -> "B".equals(item.getString("perfmTypeCde"))));
    }

    @Test
    public void testUtPerfmXmlProcessor_success() throws Exception {
        UtPerfmXmlProcessor utPerfmXmlProcessor = applicationContext.getBean("utPerfmXmlProcessor", UtPerfmXmlProcessor.class);
        ProductStreamItem productStreamItem = new ProductStreamItem();
        productStreamItem.setImportProduct(importProduct);
        productStreamItem.setOriginalProduct(originalProduct);
        productStreamItem.setActionCode(BatchImportAction.UPDATE);
        utPerfmXmlProcessor.process(productStreamItem);

        Document updatedProduct = productStreamItem.getUpdatedProduct();
        Map performance = updatedProduct.get("performance", Map.class);
        Assert.assertEquals("2023-06-03", performance.get("perfmCalcDt"));
        Assert.assertEquals(11.333, performance.get("perfm6MoPct"));
        Map benchmark = updatedProduct.get("benchmark", Map.class);
        Assert.assertEquals(22.222, benchmark.get("perfm1YrPct"));
    }

    @Test
    public void testUtPerfmXmlProcessor_actionCodeIsAdd() throws Exception {
        UtPerfmXmlProcessor utPerfmXmlProcessor = applicationContext.getBean("utPerfmXmlProcessor", UtPerfmXmlProcessor.class);
        ProductStreamItem productStreamItem = new ProductStreamItem();
        productStreamItem.setActionCode(BatchImportAction.ADD);
        productStreamItem.setImportProduct(importProduct);
        Assert.assertNull(utPerfmXmlProcessor.process(productStreamItem));
    }

    @Test
    public void testUtPerfmXmlProcessor_allPerfmAreEmpty() throws Exception {
        UtPerfmXmlProcessor utPerfmXmlProcessor = applicationContext.getBean("utPerfmXmlProcessor", UtPerfmXmlProcessor.class);
        importProduct.put("performance", Arrays.asList(new Document("perfmTypeCde", "B"), new Document()));
        ProductStreamItem productStreamItem = new ProductStreamItem();
        productStreamItem.setImportProduct(importProduct);
        productStreamItem.setActionCode(BatchImportAction.UPDATE);
        productStreamItem.setOriginalProduct(originalProduct);
        Assert.assertNotNull(utPerfmXmlProcessor.process(productStreamItem));
    }
}
