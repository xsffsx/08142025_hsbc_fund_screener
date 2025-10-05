package com.dummy.wpb.product;

import com.google.common.collect.Lists;
import com.dummy.wpb.product.configuration.SystemDefaultValuesHolder;
import com.dummy.wpb.product.configuration.SystemUpdateConfigHolder;
import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;

import com.dummy.wpb.product.service.ProductFormatService;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.service.ReferenceDataService;
import com.dummy.wpb.product.service.impl.DefaultProductFormatService;
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
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
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
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@SpringJUnitConfig
@SpringBatchTest
@SpringBootTest(classes = ImportSnXmlJobApplication.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class ImportSnXmlJobApplicationTest {

    static MockedStatic<LegacyConfig> legacyConfigMockedStatic;

    @MockBean(name = "productFormatService")
    DefaultProductFormatService productFormatService;


    static JobParameters jobParameters;

    @MockBean
    LegacyConfig legacyConfig;


    @MockBean
    ProductService productService;

    @Autowired
    ApplicationContext applicationContext;

    static Document originalProduct;
    static Document importProduct;

    static JobExecution jobExecution;


    @BeforeClass
    public static void setUp() throws IOException {
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .addString("systemCde", "AMHGSOPS.CE")
                .addString("incomingPath", new ClassPathResource("/test").getFile().getAbsolutePath())
                .toJobParameters();
        jobExecution = MetaDataInstanceFactory.createJobExecution("importSnXmlFileJob", 1L, 1L, jobParameters);
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
        return jobExecution;
    }

    public StepExecution getStepExecution() {
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }

    @Test
    public void testSNXmlReader() throws Exception {
        when(productService.productByFilters(Mockito.anyMap(), anyList())).thenReturn(Lists.newArrayList(originalProduct));
        Mockito.doNothing().when(legacyConfig).init();
        ItemStreamReader<ProductStreamItem> snXmlFileReader = applicationContext.getBean("snXmlFileReader", ItemStreamReader.class);
        snXmlFileReader.open(new ExecutionContext());
        ProductStreamItem streamItem = snXmlFileReader.read();
        String riskLvlCde = streamItem.getImportProduct().getString("riskLvlCde");
        Assert.assertEquals("1", riskLvlCde);
        String grpNumRiskDisclousure = streamItem.getOriginalProduct().getString("grpNumRiskDisclosure");
        Assert.assertEquals("RD21", grpNumRiskDisclousure);
    }

    @Test
    public void testSNProductValidator() throws Exception {
        SnProductValidator snProductValidator = new SnProductValidator();
        ProductStreamItem productStreamItem = new ProductStreamItem();
        productStreamItem.setImportProduct(importProduct);
        productStreamItem.setOriginalProduct(originalProduct);
        productStreamItem.setActionCode(BatchImportAction.ADD);
        Assert.assertThrows(ValidationException.class, () -> {
            snProductValidator.validate(productStreamItem);
        });
    }

    @Test
    public void testImportSNProcessor() throws Exception {
        CompositeItemProcessor<ProductStreamItem, ProductStreamItem> snProductXmlFileProcessor = applicationContext.getBean("snProductXmlFileProcessor", CompositeItemProcessor.class);
        ProductStreamItem productStreamItem = new ProductStreamItem();
        productStreamItem.setImportProduct(importProduct);
        productStreamItem.setOriginalProduct(originalProduct);
        productStreamItem.setActionCode(BatchImportAction.UPDATE);
        snProductXmlFileProcessor.process(productStreamItem);
        String riskLvlCde = productStreamItem.getImportProduct().getString("riskLvlCde");
        String prodName = productStreamItem.getOriginalProduct().getString("prodName");
        String prodShrtName = productStreamItem.getOriginalProduct().getString("prodShrtName");
        long termRemainDayCnt = productStreamItem.getOriginalProduct().getLong("termRemainDayCnt");
        Assert.assertEquals("1", riskLvlCde);
        Assert.assertEquals("ABN AMRO 1 8NOV04 HKD CMU", prodName);
        Assert.assertEquals(0, termRemainDayCnt);
        Assert.assertEquals("ABN AMRO 1 8NOV04 HKD CMU", prodShrtName);
    }

}

