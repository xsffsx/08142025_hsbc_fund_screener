package com.dummy.wpb.product;


import com.google.common.collect.Lists;
import com.dummy.wpb.product.component.ActiveFileProcesser;
import com.dummy.wpb.product.component.ActiveFileWriter;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.jpo.EquityRecommendationsPo;
import com.dummy.wpb.product.model.EquityRecommendations;
import com.dummy.wpb.product.model.graphql.InvalidProduct;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateInput;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.util.LegacyConfig;
import com.dummy.wpb.product.utils.CommonUtils;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.*;

import static org.springframework.batch.core.ExitStatus.COMPLETED;

@SpringJUnitConfig
@SpringBatchTest
@SpringBootTest(classes = ImportEquityRecActiveJobApplication.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class ImportEquityRecomActiveFileJobTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    static JobParameters jobParameters;

    @Autowired
    ApplicationContext applicationContext;

    @MockBean
    MongoTemplate mongoTemplate;

    @MockBean
    UpdateResult updateResult;

    @MockBean
    ProductService productService;

    static Document originalProduct;

    static JobExecution jobExecution;

    @MockBean
    LegacyConfig legacyConfig;

    static EquityRecommendationsPo equityRecommendationsPo;


    @BeforeClass
    public static void setUp() throws IOException {
        jobParameters = new JobParametersBuilder()
                .addString("incomingPath", new ClassPathResource("/test").getFile().getAbsolutePath())
                .toJobParameters();
        jobExecution = MetaDataInstanceFactory.createJobExecution("equityRecActiveJob", 1L, 1L, jobParameters);
        originalProduct = Document.parse(CommonUtils.readResource("/product-original.json"));
        equityRecommendationsPo = new EquityRecommendationsPo();
        equityRecommendationsPo.setRating("rating");
        equityRecommendationsPo.setRecommended("Y");
        equityRecommendationsPo.setReuters("R");
        equityRecommendationsPo.setTargetPrice(23.3);
        equityRecommendationsPo.setUpside(23.3);
        equityRecommendationsPo.setKeyHighlight("keyHighlight");
        equityRecommendationsPo.setRationale("rationale");
        equityRecommendationsPo.setRisk("risk");
        equityRecommendationsPo.setUrl("url");
        equityRecommendationsPo.setSector("sector");
        equityRecommendationsPo.setIndustryGroup("industryGroup");
        equityRecommendationsPo.setForwardPe(33.3);
        equityRecommendationsPo.setForwardPb(33.3);
        equityRecommendationsPo.setForwardDividendYield(33.3);
        equityRecommendationsPo.setHistoricDividendYield(33.3);
    }

    public JobExecution getJobExecution() {
        return jobExecution;
    }

    public StepExecution getStepExecution() {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters);
        ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();
        jobContext.put("failUpdate", 0);
        jobContext.put("prodNotFound", 0);
        return stepExecution;
    }

    @Test
    public void testJob() throws Exception {
        Job equityRecActiveJob = applicationContext.getBean("equityRecActiveJob", Job.class);
        jobLauncherTestUtils.setJob(equityRecActiveJob);

        Mockito.doNothing().when(legacyConfig).init();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        Assert.assertEquals(COMPLETED, jobExecution.getExitStatus());
    }


    @Test
    public void testActiveFileProcesser_matchEquityRecCovAndProduct_retrunMap() throws Exception {
        ActiveFileProcesser activeFileProcesser = buildProcesserCondition();
        Mockito.when(productService.productByFilters(Mockito.anyMap())).thenReturn(Lists.newArrayList(originalProduct));
        Map<EquityRecommendationsPo, List<Document>> process = activeFileProcesser.process(new EquityRecommendationsPo());
        Assert.assertNotNull(process);
    }

    @Test
    public void testActiveFileProcesser_matchEquityRecCovAndNotMatchProduct_retrunNull() throws Exception {
        ActiveFileProcesser activeFileProcesser = buildProcesserCondition();
        Mockito.when(productService.productByFilters(Mockito.anyMap())).thenReturn(Collections.emptyList());
        Map<EquityRecommendationsPo, List<Document>> process = activeFileProcesser.process(new EquityRecommendationsPo());
        Assert.assertNull(process);
    }

    public ActiveFileProcesser buildProcesserCondition() {
        ActiveFileProcesser activeFileProcesser = applicationContext.getBean("activeFileProcesser", ActiveFileProcesser.class);
        ReflectionTestUtils.setField(activeFileProcesser, "stepExecution", getStepExecution());
        ReflectionTestUtils.setField(activeFileProcesser, "ctryRecCde", "HK");
        ReflectionTestUtils.setField(activeFileProcesser, "grpMembrRecCde", "HBAP");
        Mockito.when(mongoTemplate.findOne(Mockito.any(), Mockito.anyObject())).thenReturn(new EquityRecommendationsPo());
        return activeFileProcesser;
    }
    @Test
    public void testActiveFileWriter_FailUpdate() {
        try {
            ActiveFileWriter activeFileWriter = applicationContext.getBean("activeFileWriter", ActiveFileWriter.class);
            ReflectionTestUtils.setField(activeFileWriter, "stepExecution", getStepExecution());
            ProductBatchUpdateResult productBatchUpdateResult = new ProductBatchUpdateResult();
            productBatchUpdateResult.setInvalidProducts(Collections.singletonList(new InvalidProduct()));
            Mockito.when(productService.batchUpdateProduct(Mockito.any(ProductBatchUpdateInput.class))).thenReturn(productBatchUpdateResult);
            Map<EquityRecommendationsPo, List<Document>> map = new HashMap<>();
            map.put(equityRecommendationsPo, new ArrayList<>());
            activeFileWriter.write(Collections.singletonList(map));
        } catch (Exception e) {
            Assert.fail("An unexpected exception occurred.");
        }
    }

    @Test
    public void testActiveFileWriter_NormalUpdate() {
        try {
            ActiveFileWriter activeFileWriter = applicationContext.getBean("activeFileWriter", ActiveFileWriter.class);
            ReflectionTestUtils.setField(activeFileWriter, "stepExecution", getStepExecution());
            Mockito.when(productService.batchUpdateProduct(Mockito.any(ProductBatchUpdateInput.class))).thenReturn(new ProductBatchUpdateResult());
            Map<EquityRecommendationsPo, List<Document>> map = new HashMap<>();
            Document document = new Document();
            document.put(Field.prodId, 10086);
            map.put(equityRecommendationsPo, Collections.singletonList(document));
            activeFileWriter.write(Collections.singletonList(map));
        } catch (Exception e) {
            Assert.fail("An unexpected exception occurred.");
        }
    }

    @Test
    public void testEquityRecommendations() {
        EquityRecommendations equityRecommendations = new EquityRecommendations();
        BeanUtils.copyProperties(equityRecommendationsPo, equityRecommendations);
        Assert.assertEquals("EquityRecommendations(rating=rating, targetPrice=23.3, upside=23.3, keyHighlight=keyHighlight, " +
                "rationale=rationale, risk=risk, url=url, sector=sector, industryGroup=industryGroup, forwardPe=33.3, forwardPb=33.3, " +
                "forwardDividendYield=33.3, historicDividendYield=33.3, recommended=Y)", equityRecommendations.toString());
    }

}
