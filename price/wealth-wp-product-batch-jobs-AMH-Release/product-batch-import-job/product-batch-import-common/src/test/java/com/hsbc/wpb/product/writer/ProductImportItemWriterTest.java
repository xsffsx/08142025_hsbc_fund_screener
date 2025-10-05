package com.dummy.wpb.product.writer;

import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.model.graphql.InvalidProduct;
import com.dummy.wpb.product.model.graphql.ProductBatchCreateResult;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateByIdInput;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.util.LegacyConfig;
import com.dummy.wpb.product.utils.CommonUtils;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProductImportItemWriterTest {

    private ProductService productService = mock(ProductService.class);

    private ProductImportItemWriter writer;

    private Document product;

    private Document newProduct;

    static MockedStatic<LegacyConfig> legacyConfigMockedStatic;

    private JobExecution jobExecution = mock(JobExecution.class);

    private JobInstance jobInstance = mock(JobInstance.class);

    private JobParameters jobParameters = mock(JobParameters.class);

    @Before
    public void setUp() {
        writer = new ProductImportItemWriter(productService);
        String json = CommonUtils.readResource("/test/UT-50108709.json");
        String json2 = CommonUtils.readResource("/test/UT-50108709(2).json");
        product = Document.parse(json);
        newProduct = Document.parse(json2);
        legacyConfigMockedStatic = Mockito.mockStatic(LegacyConfig.class);
        legacyConfigMockedStatic.when(() -> LegacyConfig.getList(anyString())).thenReturn(Collections.emptyList());
        ReflectionTestUtils.setField(writer, "jobExecution", jobExecution);
        Mockito.when(jobExecution.getJobInstance()).thenReturn(jobInstance);
        Mockito.when(jobInstance.getJobName()).thenReturn("importUtXmlFileJob");
        Mockito.when(jobExecution.getJobParameters()).thenReturn(jobParameters);
        Mockito.when(jobParameters.getString(anyString())).thenReturn("MS");
    }

    @After
    public void after() {
        legacyConfigMockedStatic.close();
    }

    @Test
    public void testWrite() throws Exception {
        ProductStreamItem productStreamItem = new ProductStreamItem();
        productStreamItem.setActionCode(BatchImportAction.UPDATE);
        productStreamItem.setOriginalProduct(product);
        productStreamItem.setUpdatedProduct(newProduct);
        ProductStreamItem productStreamItem2 = new ProductStreamItem();
        productStreamItem2.setActionCode(BatchImportAction.ADD);
        productStreamItem2.setUpdatedProduct(newProduct);
        ProductStreamItem productStreamItem3 = new ProductStreamItem();
        productStreamItem3.setActionCode(BatchImportAction.VALIDATE_ADD);
        productStreamItem3.setUpdatedProduct(product);
        ProductStreamItem productStreamItem4 = new ProductStreamItem();
        productStreamItem4.setActionCode(BatchImportAction.UPDATE);
        productStreamItem4.setOriginalProduct(product);
        productStreamItem4.setUpdatedProduct(product);
        List<ProductStreamItem> list = Arrays.asList(productStreamItem, productStreamItem2, productStreamItem3, productStreamItem4);
        ProductBatchUpdateResult productBatchUpdateResult = new ProductBatchUpdateResult();
        productBatchUpdateResult.setUpdatedProducts(Collections.singletonList(newProduct));
        productBatchUpdateResult.setInvalidProducts(Collections.EMPTY_LIST);

        ProductBatchCreateResult productBatchCreateResult = new ProductBatchCreateResult();
        productBatchCreateResult.setCreatedProducts(Collections.singletonList(newProduct));
        productBatchCreateResult.setInvalidProducts(Collections.EMPTY_LIST);
        Mockito.when(productService.batchCreateProduct(Mockito.anyList())).thenReturn(productBatchCreateResult);
        ArgumentCaptor<List<ProductBatchUpdateByIdInput>> captor = ArgumentCaptor.forClass(List.class);
        Mockito.when(productService.batchUpdateProductById(captor.capture())).thenReturn(productBatchUpdateResult);
        writer.write(list);
        ProductBatchUpdateByIdInput productBatchUpdateByIdInput = captor.getValue().get(0);
        Assert.assertEquals(new Long(50108709), productBatchUpdateByIdInput.getProdId());
    }

    @Test
    public void testAfterStep() throws Exception {
        int writeCount = 10;
        StepExecution stepExecution = new StepExecution("Step", jobExecution);
        stepExecution.setWriteCount(writeCount);

        InvalidProduct invalidProduct1 = new InvalidProduct();
        invalidProduct1.setProduct(Document.parse(CommonUtils.readResource("/test/BOND-50743650.json")));

        InvalidProduct invalidProduct2 = new InvalidProduct();
        invalidProduct2.setProduct(Document.parse(CommonUtils.readResource("/test/UT-50108709.json")));
        List<InvalidProduct> updatedFailedProducts = (List<InvalidProduct>) ReflectionTestUtils.getField(writer, "updatedFailedProducts");
        updatedFailedProducts.add(invalidProduct1);
        updatedFailedProducts.add(invalidProduct2);

        List<InvalidProduct> createdFailedProducts = (List<InvalidProduct>) ReflectionTestUtils.getField(writer, "createdFailedProducts");
        createdFailedProducts.add(invalidProduct1);

        writer.afterStep(stepExecution);

        Assert.assertEquals(writeCount - updatedFailedProducts.size() - createdFailedProducts.size(), stepExecution.getWriteCount());
        ExecutionContext executionContext = stepExecution.getExecutionContext();
        Assert.assertEquals(updatedFailedProducts, executionContext.get("updatedFailedProducts"));
    }
}