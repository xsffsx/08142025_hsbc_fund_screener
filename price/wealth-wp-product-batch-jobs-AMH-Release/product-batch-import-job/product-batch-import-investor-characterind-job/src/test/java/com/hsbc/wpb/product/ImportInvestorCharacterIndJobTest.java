package com.dummy.wpb.product;

import com.google.common.collect.Lists;
import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.InvestorCharacter;
import com.dummy.wpb.product.model.InvestorCharacterConverter;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateInput;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.util.LegacyConfig;
import com.dummy.wpb.product.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
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

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.dummy.wpb.product.model.InvestorCharacterUtil.EFFECTIVE_DATE_FORMAT;
import static org.mockito.ArgumentMatchers.*;

@SpringJUnitConfig
@SpringBatchTest
@SpringBootTest(classes = ImportInvestorCharacterIndApplication.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class ImportInvestorCharacterIndJobTest {

    @Autowired
    ApplicationContext applicationContext;

    @MockBean
    private MongoTemplate mongoTemplate;
    @MockBean
    private LegacyConfig legacyConfig;

    @MockBean
    ProductService productService;
    JobParameters jobParameters;
    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    Job investorCharacterIndJob;

    @Before
    public void before() {
        jobLauncherTestUtils.setJob(investorCharacterIndJob);
    }

    @Test
    public void testInitStep() throws Exception {
        jobParameters = new JobParametersBuilder()
                .addString(Field.ctryRecCde, "HK")
                .addString(Field.grpMembrRecCde, "HBAP")
                .addString("batchCode", "I")
                .addString("incomingPath", new ClassPathResource("/test").getFile().getAbsolutePath())
                .toJobParameters();

        Mockito.when(mongoTemplate.exists(
                argThat(query -> null != query && query.toString().contains("U62792")),
                eq("product")
        )).thenReturn(false);
        Mockito.when(mongoTemplate.exists(
                argThat(query -> null != query && query.toString().contains("U62793")),
                eq("product")
        )).thenReturn(true);

        ProductBatchUpdateResult result = new ProductBatchUpdateResult();
        Document product = new Document();
        product.put(Field.ctryRecCde, "HK");
        product.put(Field.grpMembrRecCde, "HBAP");
        product.put(Field.prodAltPrimNum, "U62793");
        product.put(Field.prodTypeCde, "UT");

        result.setUpdatedProducts(Collections.singletonList(product));
        result.setInvalidProducts(Collections.emptyList());
        Mockito.when(productService.batchUpdateProduct(any())).thenReturn(result);

        jobLauncherTestUtils.launchJob(jobParameters);

        ArgumentCaptor<ProductBatchUpdateInput> inputCaptor = ArgumentCaptor.forClass(ProductBatchUpdateInput.class);
        Mockito.verify(productService, Mockito.atLeast(1)).batchUpdateProduct(inputCaptor.capture());
        ProductBatchUpdateInput updateInput = inputCaptor.getValue();
        Assertions.assertTrue(updateInput.getOperations().stream().anyMatch(op -> op.getPath().contains(Field.prodDervtCde)));
    }

    @Test
    public void testCommonStep() throws Exception {
        jobParameters = new JobParametersBuilder()
                .addString(Field.ctryRecCde, "HK")
                .addString(Field.grpMembrRecCde, "HBAP")
                .addString("batchCode", "M")
                .addString("incomingPath", new ClassPathResource("/test").getFile().getAbsolutePath())
                .toJobParameters();

        Mockito.when(mongoTemplate.exists(
                argThat(query -> null != query && query.toString().contains("U62793")),
                eq("product")
        )).thenReturn(false);

        Mockito.when(mongoTemplate.exists(
                argThat(query -> null != query && StringUtils.containsAny(query.toString(), "U62795", "U62796", "U62797")),
                eq("product")
        )).thenReturn(true);

        ProductBatchUpdateResult result = new ProductBatchUpdateResult();
        Document U62795 = new Document();
        U62795.put(Field.ctryRecCde, "HK");
        U62795.put(Field.grpMembrRecCde, "HBAP");
        U62795.put(Field.prodAltPrimNum, "U62795");
        U62795.put(Field.prodTypeCde, "UT");

        Document U62796 = new Document(U62795);
        U62796.put(Field.ctryRecCde, "HK");
        U62796.put(Field.grpMembrRecCde, "HBAP");
        U62796.put(Field.prodAltPrimNum, "U62796");
        U62796.put(Field.prodTypeCde, "UT");


        result.setUpdatedProducts(Lists.newArrayList(U62795, U62796));
        result.setInvalidProducts(Collections.emptyList());
        Mockito.when(productService.batchUpdateProduct(any())).thenReturn(result);

        jobLauncherTestUtils.launchJob(jobParameters);

        ArgumentCaptor<ProductBatchUpdateInput> inputCaptor = ArgumentCaptor.forClass(ProductBatchUpdateInput.class);
        Mockito.verify(productService, Mockito.atLeast(1)).batchUpdateProduct(inputCaptor.capture());
        ProductBatchUpdateInput updateInput = inputCaptor.getValue();
        Assertions.assertTrue(updateInput.getOperations().stream().anyMatch(op -> op.getPath().contains(Field.prodDervtCde)));
    }

    @Test
    public void testExRetryStep() throws Exception {
        jobParameters = new JobParametersBuilder()
                .addString(Field.ctryRecCde, "HK")
                .addString(Field.grpMembrRecCde, "HBAP")
                .addString("batchCode", "E")
                .addString("incomingPath", new ClassPathResource("/test").getFile().getAbsolutePath())
                .toJobParameters();
        InvestorCharacterConverter converter = new InvestorCharacterConverter();
        List<InvestorCharacter> prodDervtHandlReqmtList = BsonArray.parse(CommonUtils.readResource("/response/prod_dervt_handl_reqmt.json"))
                .stream()
                .map(BsonValue::asDocument)
                .map(BsonDocument::toJson)
                .map(Document::parse)
                .map(converter::convert)
                .collect(Collectors.toList());

        Mockito.when(mongoTemplate.find(
                any(Query.class),
                eq(InvestorCharacter.class),
                eq(CollectionName.prod_dervt_handl_reqmt.name())
        )).thenAnswer(invocation -> {
            Query query = invocation.getArgument(0);
            if (0 == query.getSkip()) {
                return prodDervtHandlReqmtList;
            } else {
                return Collections.emptyList();
            }
        });

        Mockito.when(mongoTemplate.exists(
                argThat(query -> null != query && StringUtils.containsAny(query.toString(), "U63336")),
                eq("product")
        )).thenReturn(false);

        Mockito.when(mongoTemplate.exists(
                argThat(query -> null != query && StringUtils.containsAny(query.toString(), "U63337")),
                eq("product")
        )).thenReturn(true);
        Document U63337 = new Document();
        U63337.put(Field.ctryRecCde, "HK");
        U63337.put(Field.grpMembrRecCde, "HBAP");
        U63337.put(Field.prodAltPrimNum, "U63337");
        U63337.put(Field.prodTypeCde, "UT");

        ProductBatchUpdateResult result = new ProductBatchUpdateResult();
        result.setUpdatedProducts(Lists.newArrayList(U63337));
        result.setInvalidProducts(Collections.emptyList());
        Mockito.when(productService.batchUpdateProduct(any())).thenReturn(result);
        jobLauncherTestUtils.launchJob(jobParameters);

        Mockito.verify(mongoTemplate, Mockito.atLeast(1)).remove(any(Query.class), eq(CollectionName.prod_dervt_handl_reqmt.name()));
    }

    @Test
    public void testDailyUpdateStep() throws Exception {
        jobParameters = new JobParametersBuilder()
                .addString(Field.ctryRecCde, "HK")
                .addString(Field.grpMembrRecCde, "HBAP")
                .addString("batchCode", "D")
                .addString("incomingPath", new ClassPathResource("/test").getFile().getAbsolutePath())
                .toJobParameters();
        InvestorCharacterConverter converter = new InvestorCharacterConverter();
        List<InvestorCharacter> prodDervtHandlReqmtList = BsonArray.parse(CommonUtils.readResource("/response/prod_dervt_handl_reqmt.json"))
                .stream()
                .map(BsonValue::asDocument)
                .map(BsonDocument::toJson)
                .map(Document::parse)
                .map(converter::convert)
                .collect(Collectors.toList());

        Mockito.when(mongoTemplate.find(
                any(Query.class),
                eq(InvestorCharacter.class),
                eq(CollectionName.product.name())
        )).thenAnswer(invocation -> {
            Query query = invocation.getArgument(0);
            if (0 == query.getSkip()) {
                return prodDervtHandlReqmtList;
            } else {
                return Collections.emptyList();
            }
        });

        Document U63337 = new Document();
        U63337.put(Field.ctryRecCde, "HK");
        U63337.put(Field.grpMembrRecCde, "HBAP");
        U63337.put(Field.prodAltPrimNum, "U63337");
        U63337.put(Field.prodTypeCde, "UT");

        ProductBatchUpdateResult result = new ProductBatchUpdateResult();
        result.setUpdatedProducts(Lists.newArrayList(U63337));
        result.setInvalidProducts(Collections.emptyList());
        Mockito.when(productService.batchUpdateProduct(any())).thenReturn(result);
        jobLauncherTestUtils.launchJob(jobParameters);

        Mockito.verify(productService, Mockito.atLeast(1)).batchUpdateProduct(any(ProductBatchUpdateInput.class));
    }

    @Test
    public void testMonlyReconStep() throws Exception {
        jobParameters = new JobParametersBuilder()
                .addString(Field.ctryRecCde, "HK")
                .addString(Field.grpMembrRecCde, "HBAP")
                .addString("batchCode", "R")
                .addString("incomingPath", new ClassPathResource("/test").getFile().getAbsolutePath())
                .toJobParameters();

        Document U62794 = new Document();
        U62794.put(Field.ctryRecCde, "HK");
        U62794.put(Field.grpMembrRecCde, "HBAP");
        U62794.put(Field.prodAltPrimNum, "U62794");
        U62794.put(Field.prodTypeCde, "UT");
        U62794.put(Field.prodDervtCde, "N");
        U62794.put(Field.prodDervtRvsCde, "S");
        U62794.put(Field.prdDervRvsEffDt, DateUtils.parseDate("20230908", EFFECTIVE_DATE_FORMAT));

        Document U62793 = new Document();
        U62793.put(Field.ctryRecCde, "HK");
        U62793.put(Field.grpMembrRecCde, "HBAP");
        U62793.put(Field.prodAltPrimNum, "U62793");
        U62793.put(Field.prodTypeCde, "UT");
        U62793.put(Field.prodDervtCde, "G");
        U62793.put(Field.prodDervtRvsCde, "S");

        Mockito.when(mongoTemplate.findOne(
                any(Query.class),
                eq(Document.class),
                eq(CollectionName.product.name())
        )).thenAnswer(invocation -> {
            Query query = invocation.getArgument(0);
            if (query.toString().contains("U62794")) {
                return U62794;
            } else if (query.toString().contains("62793")) {
                return U62793;
            } else {
                return null;
            }
        });

        ProductBatchUpdateResult result = new ProductBatchUpdateResult();
        result.setUpdatedProducts(Lists.newArrayList(U62793));
        result.setInvalidProducts(Collections.emptyList());
        Mockito.when(productService.batchUpdateProduct(any())).thenReturn(result);
        jobLauncherTestUtils.launchJob(jobParameters);

        Mockito.verify(productService, Mockito.atLeast(1)).batchUpdateProduct(any(ProductBatchUpdateInput.class));
    }

    @Test
    public void testUnknownStep() throws Exception {
        jobParameters = new JobParametersBuilder()
                .addString(Field.ctryRecCde, "HK")
                .addString(Field.grpMembrRecCde, "HBAP")
                .addString("batchCode", "UNKOWN")
                .addString("incomingPath", new ClassPathResource("/test").getFile().getAbsolutePath())
                .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        Assert.assertNotEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
    }

    @After
    public void after() throws IOException {
        File testFolder = new ClassPathResource("/test").getFile();
        for (File file : testFolder.listFiles()) {
            if (file.getName().endsWith(".bak")){
                file.renameTo(new File(StringUtils.substringBefore(file.getAbsolutePath(),".bak")));
            }
        }
    }
}
