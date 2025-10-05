package com.dummy.wpb.product;

import com.google.common.collect.Lists;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.exception.InvalidRecordException;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.processor.WisdomBondProcessor;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.service.impl.DefaultProductFormatService;
import com.dummy.wpb.product.util.LegacyConfig;
import com.dummy.wpb.product.util.ProductKeyUtils;
import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.writer.WisdomBondItemWriter;
import org.bson.Document;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static com.dummy.wpb.product.constant.BatchConstants.*;
import static com.dummy.wpb.product.constant.BatchImportAction.ADD;
import static com.dummy.wpb.product.constant.BatchImportAction.UPDATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.batch.core.ExitStatus.COMPLETED;

@SpringJUnitConfig
@SpringBatchTest
@SpringBootTest(classes = ImportBondWisdomJobApplication.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class ImportWisdomBondXmlFileStepTest {

    static MockedStatic<LegacyConfig> legacyConfigMockedStatic;

    static JobParameters jobParameters;
    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean
    LegacyConfig legacyConfig;

    @MockBean(name = "productFormatService")
    DefaultProductFormatService productFormatService;

    @MockBean
    ProductService productService;

    @Autowired
    ApplicationContext applicationContext;

    static Document originalProduct;
    static Document importProduct;

    @BeforeClass
    public static void setUp() throws IOException {
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .addString("systemCde", "WISDOM")
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
        return MetaDataInstanceFactory.createJobExecution("importWisdomBondXmlFileJob", 1L, 1L, jobParameters);
    }

    public StepExecution getStepExecution() {
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }

    @Test
    public void testJob() throws Exception {
        // Assert.assertEquals("a","a");
        when(productService.productByFilters(Mockito.anyMap())).thenReturn(Lists.newArrayList(originalProduct));
        ProductBatchUpdateResult updateResult = new ProductBatchUpdateResult();
        updateResult.setUpdatedProducts(Collections.singletonList(originalProduct));
        updateResult.setInvalidProducts(Collections.emptyList());
        when(productService.batchUpdateProduct(any())).thenReturn(updateResult);

        Mockito.doNothing().when(legacyConfig).init();
        Mockito.doNothing().when(productFormatService).init();

        Job importWisdomBondXmlFileJob = applicationContext.getBean("importWisdomBondXmlFileJob", Job.class);
        jobLauncherTestUtils.setJob(importWisdomBondXmlFileJob);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        Assert.assertEquals(COMPLETED, jobExecution.getExitStatus());

    }

    @Test
    public void testWisdomBondProcessor() throws Exception {
        WisdomBondProcessor processor = new WisdomBondProcessor();
        ProductStreamItem streamItem = new ProductStreamItem();
        streamItem.setImportProduct(importProduct);
        streamItem.setOriginalProduct(originalProduct);
        streamItem.setActionCode(ADD);
        Assert.assertNull(processor.process(streamItem));

        streamItem.setActionCode(UPDATE);
        importProduct.put(Field.prodTopSellRankNum, null);
        Assert.assertThrows(InvalidRecordException.class, () -> processor.process(streamItem));


        importProduct.put(Field.prodTopSellRankNum, 3L);
        originalProduct.put(Field.prodStatCde, EXPIRED);
        Assert.assertNull(processor.process(streamItem));

        originalProduct.put(Field.prodStatCde, ACTIVE);
        originalProduct.put(Field.allowBuyProdInd, INDICATOR_NO);
        Assert.assertNull(processor.process(streamItem));


        originalProduct.put(Field.allowBuyProdInd, INDICATOR_YES);
        Assert.assertNotNull(processor.process(streamItem));
    }

    @Test
    public void testWisdomBondWrite() throws Exception {
        WisdomBondItemWriter wisdomBondItemWriter = applicationContext.getBean("wisdomBondItemWriter", WisdomBondItemWriter.class);
        ProductStreamItem productStreamItem = new ProductStreamItem();
        productStreamItem.setImportProduct(importProduct);
        productStreamItem.setOriginalProduct(originalProduct);
        productStreamItem.setActionCode(UPDATE);
        ProductBatchUpdateResult updateResult = new ProductBatchUpdateResult();
        updateResult.setUpdatedProducts(Collections.singletonList(originalProduct));
        updateResult.setInvalidProducts(Collections.emptyList());
        when(productService.batchUpdateProduct(any())).thenReturn(updateResult);
        wisdomBondItemWriter.write(Arrays.asList(productStreamItem));
        //method no return
        Assert.assertNull(null);
    }

}
