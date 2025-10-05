package com.dummy.wpb.product;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wpb.product.config.UserDefinedFieldConfig;
import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.jpo.*;
import com.dummy.wpb.product.loader.DBConfigLoader;
import com.dummy.wpb.product.repository.*;
import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.writer.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonArray;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes = MigrateDataJobApplication.class)
@SpringJUnitConfig
@RunWith(SpringJUnit4ClassRunner.class)
public class MigrateCognosTablesJobTest {

    private Logger logger = LoggerFactory.getLogger(MigrateCognosTablesJobTest.class);

    private static JobParameters jobParameters;

/*    private File outputFile;*/

    private List productList;

    private List referenceDataList;

    private List logEqtyLinkInvstList;

    private Document udfMapping;

    @MockBean
    public MongoClient mongoClient;

    @MockBean
    public MongoDatabase mongoDatabase;
    
    @MockBean
    public MongoTemplate mongoTemplate;
    
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private DBConfigLoader dbConfigLoader;

    @MockBean
    private DebtInstmRepository debtInstmRepository;

    @MockBean
    private EqtyLinkInvstRepository eqtyLinkInvstRepository;

    @MockBean
    private EqtyLinkInvstUndlStockRepository eqtyLinkInvstUndlStockRepository;

    @MockBean
    private LogEqtyLinkInvstRepository logEqtyLinkInvstRepository;

    @MockBean
    private ProdAltIdRepository prodAltIdRepository;

    @MockBean
    private ProdFormReqmtRepository prodFormReqmtRepository;

    @MockBean
    private ProdRestrCustCtryRepository prodRestrCustCtryRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ProdUserDefinExtFieldRepository prodUserDefinExtFieldRepository;

    @MockBean
    private ReferenceDataRepository referenceDataRepository;

    @MockBean
    private ProdOvridFieldRepository prodOvridFieldRepository;

    public JobExecution getJobExecution() {
        return MetaDataInstanceFactory.createJobExecution("migrateDataJob", 1L, 1L, jobParameters);
    }

    public StepExecution getStepExecution() {
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }


    @Before
    public void before() throws IOException {
        // set output path in job parameters
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .addString("isFullSycn", "true")
                .toJobParameters();
        // load product.json
        productList = BsonArray.parse(CommonUtils.readResource("/product.json"))
                .getValues()
                .stream()
                .map(item -> Document.parse(item.asDocument().toJson()))
                .peek(item -> {
                    item.put("_id",item.get("_id",Number.class).longValue());
                    item.put("prodId",item.get("prodId",Number.class).longValue());
                })
                .collect(Collectors.toList());
        // load referenceData.json
        referenceDataList = BsonArray.parse(CommonUtils.readResource("/referenceData.json"))
                .getValues()
                .stream()
                .map(item -> Document.parse(item.asDocument().toJson()))
                .collect(Collectors.toList());
        // load logEqtyLinkInvst.json
        logEqtyLinkInvstList = BsonArray.parse(CommonUtils.readResource("/logEqtyLinkInvst.json"))
                .getValues()
                .stream()
                .map(item -> Document.parse(item.asDocument().toJson()))
                .collect(Collectors.toList());
        // load user-defined-field-mapping.json
        udfMapping = Document.parse(CommonUtils.readResource("/user-defined-field-mapping.json"));
    }

    @Test
    public void testMigrateDataJob_fullSycn() throws Exception {
        // set output path in job parameters
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .addString("isFullSycn", "true")
                .toJobParameters();
        List<UserDefinedFieldConfig> userDefinedFieldConfigs = commondCde();
        Assert.assertNotNull(userDefinedFieldConfigs);
        UserDefinedFieldConfig userDefinedFieldConfig = new UserDefinedFieldConfig();
        userDefinedFieldConfig.setFieldCde("fundCatCde");
        userDefinedFieldConfig.setFieldTypeCde("A");
        userDefinedFieldConfig.setFieldDataTypeText("String");
        userDefinedFieldConfig.setJsonPath("utTrstInstm.fundCatCde");
        logger.info(userDefinedFieldConfig.getFieldCde() + userDefinedFieldConfig.getFieldTypeCde()
                + userDefinedFieldConfig.getFieldDataTypeText() + userDefinedFieldConfig.getJsonPath());
    }

    @Test
    public void testMigrateDataJob_deltaSycn() throws Exception {
        // set output path in job parameters
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .addString("isFullSycn", "false")
                .toJobParameters();

        Document deltaCofigDoc = new Document();
        deltaCofigDoc.put(Field.parmValueText, "2023-09-24T01:51:40.623");
        Query query = new Query().addCriteria(Criteria.where(Field.parmCde).is("COGNOS.PROD.LASTESTRECUPDTTM"))
                .addCriteria(Criteria.where(Field.ctryRecCde).is("HK"))
                .addCriteria(Criteria.where(Field.grpMembrRecCde).is("HBAP"));
        Mockito.when(mongoTemplate.find(query, Document.class, CollectionName.sys_parm.name())).thenReturn(Collections.singletonList(deltaCofigDoc));
        List<UserDefinedFieldConfig> userDefinedFieldConfigs = commondCde();
        Assert.assertNotNull(userDefinedFieldConfigs);

    }

    @Test
    public void testWriter_exception() {
        String s = CommonUtils.readResource("/ELI-50755766.json");
        Document prod = Document.parse(s);
        List<Document> prods = Collections.singletonList(prod);
        EqtyLinkInvstUndlStockWriter eqtyLinkInvstUndlStockWriter = new EqtyLinkInvstUndlStockWriter();
        ReflectionTestUtils.setField(eqtyLinkInvstUndlStockWriter, "objectMapper", new ObjectMapper());
        ReflectionTestUtils.setField(eqtyLinkInvstUndlStockWriter, "eqtyLinkInvstUndlStockRepository", eqtyLinkInvstUndlStockRepository);
        Assert.assertThrows(IllegalArgumentException.class, () -> eqtyLinkInvstUndlStockWriter.write(prods));

        ProdAltIdWriter prodAltIdWriter = new ProdAltIdWriter();
        ReflectionTestUtils.setField(prodAltIdWriter, "objectMapper", new ObjectMapper());
        ReflectionTestUtils.setField(prodAltIdWriter, "prodAltIdRepository", prodAltIdRepository);
        Assert.assertThrows(IllegalArgumentException.class, () -> prodAltIdWriter.write(prods));

        ProdFormReqmtWriter prodFormReqmtWriter = new ProdFormReqmtWriter();
        ReflectionTestUtils.setField(prodFormReqmtWriter, "objectMapper", new ObjectMapper());
        ReflectionTestUtils.setField(prodFormReqmtWriter, "prodFormReqmtRepository", prodFormReqmtRepository);
        Assert.assertThrows(IllegalArgumentException.class, () -> prodFormReqmtWriter.write(prods));

        ProdRestrCustCtryWriter prodRestrCustCtryWriter = new ProdRestrCustCtryWriter();
        ReflectionTestUtils.setField(prodRestrCustCtryWriter, "objectMapper", new ObjectMapper());
        ReflectionTestUtils.setField(prodRestrCustCtryWriter, "prodRestrCustCtryRepository", prodRestrCustCtryRepository);
        Assert.assertThrows(IllegalArgumentException.class, () -> prodRestrCustCtryWriter.write(prods));

        ProductRepositoryWriter productRepositoryWriter = new ProductRepositoryWriter();
        ReflectionTestUtils.setField(productRepositoryWriter, "objectMapper", new ObjectMapper());
        Assert.assertThrows(IllegalArgumentException.class, () -> productRepositoryWriter.write(prods));

        ProdOvridFieldWriter prodOvridFieldWriter = new ProdOvridFieldWriter();
        ReflectionTestUtils.setField(prodOvridFieldWriter, "objectMapper", new ObjectMapper());
        ReflectionTestUtils.setField(prodOvridFieldWriter, "prodOvridFieldRepository", prodOvridFieldRepository);
        Assert.assertThrows(IllegalArgumentException.class, () -> prodOvridFieldWriter.write(prods));
    }

    private List<UserDefinedFieldConfig> commondCde() throws Exception {
        ArgumentMatcher<Query> argumentMatcher = argument -> argument != null && argument.getSkip() == 0;
        // Mock product
        AtomicInteger countProduct = new AtomicInteger(0);
        Mockito.when(mongoTemplate.find(argThat(argumentMatcher), any(), Mockito.eq(CollectionName.product.name()))).thenAnswer(
                invocation -> {
                    Query argument = invocation.getArgument(0);
                    // Break doPageRead if count > 0
                    if (countProduct.get() == 0 && argument != null && argument.getSkip() == 0) {
                        countProduct.incrementAndGet();
                        return productList;
                    }
                    return new ArrayList<>();
                }
        );
        // Mock reference_data
        AtomicInteger countRefData = new AtomicInteger(0);
        Mockito.when(mongoTemplate.find(argThat(argumentMatcher), any(), Mockito.eq(CollectionName.reference_data.name()))).thenAnswer(
                invocation -> {
                    Query argument = invocation.getArgument(0);
                    // Break doPageRead if count > 0
                    if (countRefData.get() == 0 && argument != null && argument.getSkip() == 0) {
                        countRefData.incrementAndGet();
                        return referenceDataList;
                    }
                    return new ArrayList<>();
                }
        );
        // Mock log_eqty_link_invst
        AtomicInteger countLogEqty = new AtomicInteger(0);
        Mockito.when(mongoTemplate.find(argThat(argumentMatcher), any(), Mockito.eq("log_eqty_link_invst"))).thenAnswer(
                invocation -> {
                    Query argument = invocation.getArgument(0);
                    // Break doPageRead if count > 0
                    if (countLogEqty.get() == 0 && argument != null && argument.getSkip() == 0) {
                        countLogEqty.incrementAndGet();
                        return logEqtyLinkInvstList;
                    }
                    return new ArrayList<>();
                }
        );
        Mockito.when(debtInstmRepository.saveAll(Mockito.any())).thenReturn(Collections.singletonList(new DebtInstmPo()));
        Mockito.when(eqtyLinkInvstRepository.saveAll(Mockito.any())).thenReturn(Collections.singletonList(new EqtyLinkInvstPo()));
        Mockito.when(eqtyLinkInvstUndlStockRepository.saveAll(Mockito.any())).thenReturn(Collections.singletonList(new EqtyLinkInvstUndlStockPo()));
        Mockito.when(logEqtyLinkInvstRepository.saveAll(Mockito.any())).thenReturn(Collections.singletonList(new LogEqtyLinkInvstPo()));
        Mockito.when(prodAltIdRepository.saveAll(Mockito.any())).thenReturn(Collections.singletonList(new ProdAltIdPo()));
        Mockito.when(prodFormReqmtRepository.saveAll(Mockito.any())).thenReturn(Collections.singletonList(new ProdFormReqmtPo()));
        Mockito.when(prodRestrCustCtryRepository.saveAll(Mockito.any())).thenReturn(Collections.singletonList(new ProdRestrCustCtryPo()));
        Mockito.when(productRepository.saveAll(Mockito.any())).thenReturn(Collections.singletonList(new ProductPo()));
        Mockito.when(prodUserDefinExtFieldRepository.saveAll(Mockito.any())).thenReturn(Collections.singletonList(new ProdUserDefinExtFieldPo()));
        Mockito.when(referenceDataRepository.saveAll(Mockito.any())).thenReturn(Collections.singletonList(new ReferenceDataPo()));
        Mockito.when(prodOvridFieldRepository.saveAll(Mockito.any())).thenReturn(Collections.singletonList(new ProdOvridFieldPo()));

        // Mock user-defined-field-mapping
        Query query = new Query()
                .addCriteria(Criteria.where("name").is("user-defined-field-mapping"));
        query.fields().include("config");
        Mockito.when(mongoTemplate.findOne(query, Document.class, CollectionName.configuration.name())).thenReturn(udfMapping);

        dbConfigLoader.afterPropertiesSet();
        List<UserDefinedFieldConfig> tbProdUserDefinExtField = DBConfigLoader.getUserDefinedFieldConfig("TB_PROD_USER_DEFIN_EXT_FIELD");

        // run migrateDataJob
        Job migrateDataJob = applicationContext.getBean("migrateDataJob", Job.class);
        jobLauncherTestUtils.setJob(migrateDataJob);
        jobLauncherTestUtils.launchJob(jobParameters);
        return tbProdUserDefinExtField;
    }

}
