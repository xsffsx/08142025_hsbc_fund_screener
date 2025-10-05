package com.dummy.wpb.product;

import com.google.common.collect.Lists;
import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.service.ProductFormatService;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.service.impl.DefaultProductFormatService;
import com.dummy.wpb.product.util.LegacyConfig;
import com.dummy.wpb.product.util.ProductKeyUtils;
import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.utils.JsonPathUtils;
import com.dummy.wpb.product.utils.JsonUtil;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
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
import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;

@SpringJUnitConfig
@SpringBatchTest
@SpringBootTest(classes = ImportEliXmlJobApplication.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class ImportEliXmlFileJobTest {

    static MockedStatic<LegacyConfig> legacyConfigMockedStatic;

    @MockBean
    LegacyConfig legacyConfig;

    @MockBean
    ProductService productService;

    @MockBean(name = "productFormatService")
    DefaultProductFormatService productFormatService;

    @Autowired
    ApplicationContext applicationContext;

    Document originalProduct;
    Document importProduct;

    static JobParameters jobParameters;

    @Autowired
    EliXmlFileProcessor eliXmlFileProcessor;

    @BeforeClass
    public static void setUp() throws IOException {
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .addString("systemCde", "AMHGSOPS.PD")
                .addString("incomingPath", new ClassPathResource("/test").getFile().getAbsolutePath())
                .toJobParameters();

        legacyConfigMockedStatic = Mockito.mockStatic(LegacyConfig.class);
        legacyConfigMockedStatic.when(() -> LegacyConfig.getList(anyString())).thenReturn(Collections.emptyList());
        ReflectionTestUtils.setField(ProductKeyUtils.class, "ignoreStatFilterList", Arrays.asList("D", "P"));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByProdCdeList", Collections.singletonList("W"));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByUniqueList", Collections.singletonList(""));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByPrimaryList", Arrays.asList("P", "T", "P"));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByAllList", Collections.singletonList("M"));
    }

    @Before
    public void before() {
        originalProduct = Document.parse(CommonUtils.readResource("/product-original.json"));
        importProduct = Document.parse(CommonUtils.readResource("/product-import.json"));
    }

    public JobExecution getJobExecution() {
        return MetaDataInstanceFactory.createJobExecution("importEliXmlFileJob", 1L, 1L, jobParameters);
    }

    public StepExecution getStepExecution() {
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }


    @Test
    public void testEliXmlFileReader() throws Exception {
        Mockito.when(productService.productByFilters(Mockito.anyMap(),anyList())).thenReturn(Lists.newArrayList(originalProduct));
        Mockito.doNothing().when(legacyConfig).init();
        ItemStreamReader<ProductStreamItem> eliXmlFileReader = applicationContext.getBean("eliXmlFileReader", ItemStreamReader.class);
        eliXmlFileReader.open(new ExecutionContext());
        ProductStreamItem streamItem = eliXmlFileReader.read();

        Assert.assertEquals(BatchImportAction.UPDATE, streamItem.getActionCode());
        Assert.assertEquals("E", streamItem.getImportProduct().get("prodStatCde"));
    }

    @Test
    public void testEliXmlFileProcessor_success() throws Exception {
        Document undlProduct = new Document();
        undlProduct.put("prodAltPrimNum", "600056");
        undlProduct.put("prodId", 1000050786);
        Mockito.when(productService.productByFilters(anyMap(), anyCollection())).thenReturn(Collections.singletonList(undlProduct));

        ProductStreamItem productStreamItem = new ProductStreamItem();
        productStreamItem.setImportProduct(importProduct);
        productStreamItem.setOriginalProduct(originalProduct);
        productStreamItem.setUpdatedProduct(JsonUtil.deepCopy(originalProduct));
        productStreamItem.setActionCode(BatchImportAction.UPDATE);
        eliXmlFileProcessor.process(productStreamItem);

        JsonPathUtils.setValue(importProduct, "eqtyLinkInvst.undlStock", null);
        importProduct.put("prodStatCde", "");
        originalProduct.put("cptlGurntProdInd", "N");
        eliXmlFileProcessor.process(productStreamItem);

        originalProduct.put("prodStatCde", "P");
        ReflectionTestUtils.setField(eliXmlFileProcessor, "systemCde", "AMHGSOPS.PD");
        eliXmlFileProcessor.process(productStreamItem);

        Document updatedProduct = productStreamItem.getUpdatedProduct();
        Assert.assertEquals(updatedProduct.get("allowBuyProdInd"), originalProduct.get("allowBuyProdInd"));
        Assert.assertNotNull(JsonPathUtils.readValue(updatedProduct, "eqtyLinkInvst.undlStock"));
    }

    @Test
    public void testEliXmlFileProcessor_validate_noAllowCreate() throws Exception {
        ProductStreamItem productStreamItem = new ProductStreamItem();
        productStreamItem.setImportProduct(importProduct);
        productStreamItem.setActionCode(BatchImportAction.ADD);
        Assert.assertNull(eliXmlFileProcessor.process(productStreamItem));
    }

    @Test
    public void testEliXmlFileProcessor_validate_emptyProdIdUndlInstm() throws Exception {
        ProductStreamItem productStreamItem = new ProductStreamItem();
        productStreamItem.setImportProduct(importProduct);
        productStreamItem.setOriginalProduct(originalProduct);
        productStreamItem.setActionCode(BatchImportAction.UPDATE);
        ReflectionTestUtils.setField(eliXmlFileProcessor, "systemCde", "ABC");
        Mockito.when(productService.productByFilters(anyMap(), anyCollection())).thenReturn(Collections.emptyList());
        Assert.assertNull(eliXmlFileProcessor.process(productStreamItem));
    }
}
