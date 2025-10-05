package com.dummy.wpb.product;

import com.dummy.wpb.product.model.graphql.ProductBatchCreateResult;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.service.ReferenceDataService;
import com.dummy.wpb.product.service.impl.DefaultProductFormatService;
import com.dummy.wpb.product.util.LegacyConfig;
import com.dummy.wpb.product.util.ProductKeyUtils;
import com.dummy.wpb.product.utils.CommonUtils;
import org.bson.BsonArray;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
@SpringBatchTest
@SpringBootTest(classes = ImportBondXmlJobApplication.class, args = {"grpMembrRecCde=HBAP", "ctryRecCde=HK"})
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class ImportBondXmlFileJobTest {

    MockedStatic<LegacyConfig> legacyConfigMockedStatic;

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


    @Before
    public void setUp() throws Exception {
        legacyConfigMockedStatic = Mockito.mockStatic(LegacyConfig.class);
        Mockito.doCallRealMethod().when(productFormatService).formatByUpdateAttrs(any(), any(), anyCollection());
        Mockito.when(referenceDataService.referenceDataByFilter(anyMap())).thenReturn(Collections.emptyList());
        legacyConfigMockedStatic.when(() -> LegacyConfig.getList(anyString())).thenReturn(Collections.emptyList());
        ReflectionTestUtils.setField(ProductKeyUtils.class, "ignoreStatFilterList", Arrays.asList("D", "P"));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByProdCdeList", Collections.singletonList("W"));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByUniqueList", Collections.singletonList(""));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByPrimaryList", Arrays.asList("P", "T", "P"));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByAllList", Collections.singletonList("M"));
    }

    @After
    public void cleanUp() {
        legacyConfigMockedStatic.close();
    }

    @Test
    @DirtiesContext
    public void testImportBondXmlFileJob_givenRBTSystem() throws Exception {
        Mockito.doNothing().when(productFormatService).init();
        List<Document> productList = BsonArray.parse(CommonUtils.readResource("/response/product-RBT.json"))
                .getValues()
                .stream()
                .map(item -> Document.parse(item.asDocument().toJson()))
                .collect(Collectors.toList());
        when(productService.productByFilters(Mockito.anyMap(), anyList())).thenReturn(productList);

        when(productFormatService.initProduct(any())).thenAnswer(invocation -> {
            Document argument = invocation.getArgument(0);
            argument.remove("ext");
            return argument;
        });
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
                .addString("grpMembrRecCde", "dummy")
                .addString("systemCde", "RBT")
                .addString("incomingPath", new ClassPathResource("/test").getFile().getAbsolutePath())
                .toJobParameters();

        Job importProductPriceJob = applicationContext.getBean("importBondXmlFileJob", Job.class);
        jobLauncherTestUtils.setJob(importProductPriceJob);
        jobLauncherTestUtils.launchJob(jobParameters);

        Mockito.verify(productService, times(2)).batchCreateProduct(anyList());

        Mockito.verify(productService, times(1)).batchUpdateProductById(anyList());
    }

    @Test
    @DirtiesContext
    public void testImportBondXmlFileJob_givenHBAPRBTSystem() throws Exception {
        Mockito.doNothing().when(productFormatService).init();
        List<Document> productList = BsonArray.parse(CommonUtils.readResource("/response/product-HBAP-RBT.json"))
                .getValues()
                .stream()
                .map(item -> Document.parse(item.asDocument().toJson()))
                .collect(Collectors.toList());
        when(productService.productByFilters(Mockito.anyMap(), anyList())).thenReturn(productList);

        when(productFormatService.initProduct(any())).thenAnswer(invocation -> {
            Document argument = invocation.getArgument(0);
            argument.remove("ext");
            return argument;
        });
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
                .addString("systemCde", "RBT")
                .addString("incomingPath", new ClassPathResource("/test").getFile().getAbsolutePath())
                .toJobParameters();

        Job importProductPriceJob = applicationContext.getBean("importBondXmlFileJob", Job.class);
        jobLauncherTestUtils.setJob(importProductPriceJob);
        jobLauncherTestUtils.launchJob(jobParameters);

        Mockito.verify(productService, times(1)).batchCreateProduct(anyList());

        Mockito.verify(productService, times(1)).batchUpdateProductById(anyList());
    }

    @Test
    @DirtiesContext
    public void testImportBondXmlFileJob_givenGSOPSSystem() throws Exception {
        List<Document> productList = BsonArray.parse(CommonUtils.readResource("/response/product-AMHGSOPS.json"))
                .getValues()
                .stream()
                .map(item -> Document.parse(item.asDocument().toJson()))
                .collect(Collectors.toList());
        when(productService.productByFilters(Mockito.anyMap(), anyList())).thenReturn(productList);

        when(productFormatService.initProduct(any())).thenAnswer(invocation -> {
            Document argument = invocation.getArgument(0);
            argument.remove("ext");
            return argument;
        });

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
                .addString("systemCde", "AMHGSOPS.PD")
                .addString("incomingPath", new ClassPathResource("/test").getFile().getAbsolutePath())
                .toJobParameters();

        Job importProductPriceJob = applicationContext.getBean("importBondXmlFileJob", Job.class);
        jobLauncherTestUtils.setJob(importProductPriceJob);
        jobLauncherTestUtils.launchJob(jobParameters);

        Mockito.verify(productService, times(2)).batchCreateProduct(anyList());

        Mockito.verify(productService, times(2)).batchUpdateProductById(anyList());
    }

}
