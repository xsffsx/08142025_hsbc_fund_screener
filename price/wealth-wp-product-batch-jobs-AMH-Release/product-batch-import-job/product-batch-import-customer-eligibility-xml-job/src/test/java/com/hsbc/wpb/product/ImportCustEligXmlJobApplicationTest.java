package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateByIdInput;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.service.impl.DefaultProductFormatService;
import com.dummy.wpb.product.util.LegacyConfig;
import com.dummy.wpb.product.util.ProductKeyUtils;
import com.dummy.wpb.product.utils.CommonUtils;
import org.bson.BsonArray;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.item.validator.ValidationException;
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

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


@SpringJUnitConfig
@SpringBatchTest
@SpringBootTest(classes = ImportCustEligXmlJobApplication.class, args = {"systemCde=AMHGSOPS.CE", "grpMembrRecCde=HBAP", "ctryRecCde=HK"})
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class ImportCustEligXmlJobApplicationTest {

    static MockedStatic<LegacyConfig> legacyConfigMockedStatic;

    @MockBean(name = "productFormatService")
    DefaultProductFormatService productFormatService;

    JobParameters jobParameters;

    @MockBean
    LegacyConfig legacyConfig;

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;
    @MockBean
    ProductService productService;

    @Autowired
    ApplicationContext applicationContext;

    @Before
    public void setUp() {
        legacyConfigMockedStatic = Mockito.mockStatic(LegacyConfig.class);
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
    public void testImportCustEligXmlFileJob_givenAMHGSOPSSystem() throws Exception {
        Mockito.doNothing().when(productFormatService).init();
        List<Document> productList = BsonArray.parse(CommonUtils.readResource("/response/product.json"))
                .getValues()
                .stream()
                .map(item -> Document.parse(item.asDocument().toJson()))
                .collect(Collectors.toList());
        List<Document> hbapProducts = productList.stream().filter(prod -> prod.getString(Field.grpMembrRecCde).equals("HBAP")).collect(Collectors.toList());
        when(productService.productByFilters(argThat(argument -> Objects.toString(argument).contains("HBAP")), anyList())).thenReturn(hbapProducts);

        List<Document> dummyProducts = productList.stream().filter(prod -> prod.getString(Field.grpMembrRecCde).equals("dummy")).collect(Collectors.toList());
        when(productService.productByFilters(argThat(argument -> Objects.toString(argument).contains("dummy")))).thenReturn(dummyProducts);

        when(productService.batchUpdateProductById(anyList())).thenAnswer(invocation -> {
            List<ProductBatchUpdateByIdInput> updateParams = invocation.getArgument(0, List.class);
            ProductBatchUpdateResult updateResult = new ProductBatchUpdateResult();
            updateResult.setInvalidProducts(Collections.emptyList());
            List<Document> updateProducts = new ArrayList<>();
            for (ProductBatchUpdateByIdInput updateParam : updateParams) {
                updateProducts.add(productList.stream().filter(prod -> updateParam.getProdId().equals(prod.get(Field.prodId,Number.class).longValue())).findFirst().orElse(null));
            }
            updateResult.setUpdatedProducts(updateProducts);
            return updateResult;
        });

        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .addString("systemCde", "AMHGSOPS.CE")
                .addString("incomingPath", new ClassPathResource("/test").getFile().getAbsolutePath())
                .toJobParameters();

        Job importCustEligXmlFileJob = applicationContext.getBean("importCustEligXmlFileJob", Job.class);
        jobLauncherTestUtils.setJob(importCustEligXmlFileJob);
        jobLauncherTestUtils.launchJob(jobParameters);

        Mockito.verify(productService, times(3)).batchUpdateProductById(anyList());
    }


    @Test
    public void testCustEligValidator() {
        CustEligValidator CustEligValidator = new CustEligValidator();
        ProductStreamItem productStreamItem = new ProductStreamItem();
        productStreamItem.setImportProduct(new Document(Field.ctryRecCde,"HK")
                .append(Field.grpMembrRecCde,"HBAP")
                .append(Field.prodTypeCde,"BOND")
                .append(Field.prodAltPrimNum,"BOND-001"));
        productStreamItem.setActionCode(BatchImportAction.ADD);
        Assert.assertThrows(ValidationException.class, () -> {
            CustEligValidator.validate(productStreamItem);
        });

    }
}