package com.dummy.wpb.product;

import com.dummy.wpb.product.configuration.TermsheetConfiguration;
import com.dummy.wpb.product.constant.BatchConstants;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.model.TermsheetFile;
import com.dummy.wpb.product.model.graphql.*;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.util.LegacyConfig;
import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.utils.SidTsFinDocUtil;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;

@SpringJUnitConfig
@SpringBatchTest
@SpringBootTest(classes = ImportTermsheetJobApplication.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class ImportTermsheetJobApplicationTest {

    static List<Map<String, Object>> finDocList = new ArrayList<>();

    static List<Map<String, Object>> productList = new ArrayList<>();

    @Autowired
    TermsheetConfiguration termsheetConfiguration;

    @MockBean
    GraphQLService graphQLService;

    @Autowired
    ImportTermsheetService importTermsheetService;

    static Document originalProduct;

    @MockBean
    LegacyConfig legacyConfig;

    @MockBean
    GraphQLRequest graphQLRequest;
    @MockBean
    ProductService productService;

    private static final String CTRY_REC_CDE_HK = "HK";

    private static final String GRP_MEMBR_REC_CDE_HBAP = "HBAP";

    private final ImportTermsheetProcessor importTermsheetProcessor = new ImportTermsheetProcessor(termsheetConfiguration, importTermsheetService);

    @Before
    public void setUp() {
        originalProduct = Document.parse(CommonUtils.readResource("/product-original.json"));

        Map<String, Object> finDocMap = new LinkedHashMap<>();
        finDocMap.put("docFinTypeCde", "APPENDIX");
        finDocMap.put("langFinDocCde", "ZH");
        finDocMap.put("rsrcItemIdFinDoc", 800137);
        finDocList.add(finDocMap);

        Map<String, Object> productMap = new LinkedHashMap<>();
        productMap.put("ctryRecCode", CTRY_REC_CDE_HK);
        productMap.put("grpMembrRecCode", GRP_MEMBR_REC_CDE_HBAP);
        productMap.put("finDoc", finDocList);
        productList.add(productMap);

        importTermsheetProcessor.beforeStep(mock(StepExecution.class));

    }

    public StepContribution getStepContribution(String isPostProduct) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", CTRY_REC_CDE_HK)
                .addString("grpMembrRecCde", GRP_MEMBR_REC_CDE_HBAP)
                .addString("prodTypeCde", "SID")
                .addString("finDocPath", "src/test/resources/test/SID_termsheet_pdf")
                .addString("isPostProduct", isPostProduct)
                .toJobParameters();
        JobExecution jobExecution = new JobExecution(0L, jobParameters);
        StepExecution stepExecution = new StepExecution("importTermsheetStep", jobExecution);
        return new StepContribution(stepExecution);
    }

    @Test
    public void termsheetProcessorTest_noProduct() throws Exception {
        try (MockedStatic<SidTsFinDocUtil> sidTsFinDocUtil = Mockito.mockStatic(SidTsFinDocUtil.class)) {
            RepeatStatus rs = importTermsheetProcessor.execute(getStepContribution("Y"), mock(ChunkContext.class));
            Assert.assertEquals(RepeatStatus.FINISHED, rs);
        }
    }

    @Test
    public void termsheetProcessorTest_update() throws Exception {
        Mockito.doNothing().when(legacyConfig).init();
        Document product = new Document();
        product.put(Field.parmValueText, "123456");

        importTermsheetService = new ImportTermsheetService(termsheetConfiguration, graphQLService, productService);

        // Mock FinDocBatchUpdateResult
        FinDocBatchUpdateResult updateResult = new FinDocBatchUpdateResult();
        InvalidFinDoc invalidFinDoc = new InvalidFinDoc();
        InvalidProductError error = new InvalidProductError();
        error.setCode("404");
        error.setMessage("not found");
        error.setJsonPath("root");
        invalidFinDoc.setErrors(Collections.singletonList(error));
        updateResult.setInvalidFinDocs(Collections.singletonList(invalidFinDoc));

        ArgumentMatcher<GraphQLRequest> queryProductByCode = GraphQLRequest -> GraphQLRequest != null && GraphQLRequest.toString().contains("productByFilter");
        Mockito.when(graphQLService.executeRequest(argThat(queryProductByCode), Mockito.<Class<List>>any())).thenReturn(productList);

        ArgumentMatcher<GraphQLRequest> updateFinDoc = GraphQLRequest -> GraphQLRequest != null && GraphQLRequest.toString().contains("finDocBatchCreate");
        Mockito.when(graphQLService.executeRequest(argThat(updateFinDoc), Mockito.<Class<FinDocBatchUpdateResult>>any())).thenReturn(updateResult);
        Document document = new Document();
        document.put(Field.rsrcItemIdFinDoc, 800137);
        List<Document> list = new ArrayList();
        list.add(document);

        ArgumentMatcher<GraphQLRequest> queryFinDocById = GraphQLRequest -> GraphQLRequest != null && GraphQLRequest.toString().contains("finDocByFilter");
        Mockito.when(graphQLService.executeRequest(argThat(queryFinDocById), Mockito.<Class<List>>any())).thenReturn(list);
        updateFinDoc = GraphQLRequest -> GraphQLRequest != null && GraphQLRequest.toString().contains("finDocBatchUpdate");
        Mockito.when(graphQLService.executeRequest(argThat(updateFinDoc), Mockito.<Class<FinDocBatchUpdateResult>>any())).thenReturn(updateResult);
        try (MockedStatic<SidTsFinDocUtil> sidTsFinDocUtil = Mockito.mockStatic(SidTsFinDocUtil.class)) {
            RepeatStatus rs = importTermsheetProcessor.execute(getStepContribution("Y"), mock(ChunkContext.class));
            Assert.assertEquals(RepeatStatus.FINISHED, rs);
        }

    }

    @Test
    public void termsheetProcessorTest_create() throws Exception {
        Mockito.doNothing().when(legacyConfig).init();
        Document product = new Document();
        product.put(Field.parmValueText, "123456");
        importTermsheetService = new ImportTermsheetService(termsheetConfiguration, graphQLService, productService);
        ArgumentMatcher<GraphQLRequest> queryProductByCode = GraphQLRequest -> GraphQLRequest != null && GraphQLRequest.toString().contains("productByFilter");
        Mockito.when(graphQLService.executeRequest(argThat(queryProductByCode), Mockito.<Class<List>>any())).thenReturn(productList);
        ArgumentMatcher<GraphQLRequest> createFinDoc = GraphQLRequest -> GraphQLRequest != null && GraphQLRequest.toString().contains("finDocBatchCreate");
        // Mock FinDocBatchCreateResult
        FinDocBatchCreateResult createResult = new FinDocBatchCreateResult();
        Document createdFinDoc = new Document();
        createdFinDoc.put(Field.rsrcItemIdFinDoc, 123);
        createResult.setCreatedFinDocs(Collections.singletonList(createdFinDoc));
        Mockito.when(graphQLService.executeRequest(argThat(createFinDoc), Mockito.<Class<FinDocBatchCreateResult>>any())).thenReturn(createResult);

        List<ProductBatchUpdateResult> updateList = new ArrayList<>();
        ProductBatchUpdateResult productBatchUpdateResult = new ProductBatchUpdateResult();
        productBatchUpdateResult.setMatchProducts(Collections.singletonList(originalProduct));
        productBatchUpdateResult.setUpdatedProducts(Collections.singletonList(originalProduct));
        productBatchUpdateResult.setMatchCount(1);
        // create InvalidProduct to avoid renaming ack file
        productBatchUpdateResult.setInvalidProducts(Collections.singletonList(new InvalidProduct()));
        updateList.add(productBatchUpdateResult);
        ArgumentMatcher<GraphQLRequest> saveProductData = GraphQLRequest -> GraphQLRequest != null && GraphQLRequest.toString().contains("productBatchUpdate2");
        Mockito.when(graphQLService.executeRequest(argThat(saveProductData), Mockito.<Class<ProductBatchUpdateResult>>any())).thenReturn(updateList.get(0));

        try (MockedStatic<SidTsFinDocUtil> sidTsFinDocUtil = Mockito.mockStatic(SidTsFinDocUtil.class)) {
            RepeatStatus rs = importTermsheetProcessor.execute(getStepContribution("Y"), mock(ChunkContext.class));
            Assert.assertEquals(RepeatStatus.FINISHED, rs);
        }
    }

    @Test
    public void termsheetProcessorTest_skip() throws Exception {
        importTermsheetProcessor.beforeStep(mock(StepExecution.class));
        try (MockedStatic<TimeUnit> timeUnitMockedStatic = Mockito.mockStatic(TimeUnit.class)) {
            RepeatStatus rs = importTermsheetProcessor.execute(getStepContribution("N"), mock(ChunkContext.class));
            Assert.assertEquals(RepeatStatus.FINISHED, rs);
        }
    }

    @Test
    public void testProcessTermsheetData() throws Exception {
        ArgumentMatcher<GraphQLRequest> queryProductByCode = GraphQLRequest -> GraphQLRequest != null && GraphQLRequest.toString().contains("productByFilter");
        Mockito.when(graphQLService.executeRequest(argThat(queryProductByCode), Mockito.<Class<List>>any())).thenReturn(productList);
        ArgumentMatcher<GraphQLRequest> createFinDoc = GraphQLRequest -> GraphQLRequest != null && GraphQLRequest.toString().contains("finDocBatchCreate");
        // Mock FinDocBatchCreateResult
        FinDocBatchCreateResult createResult = new FinDocBatchCreateResult();
        Document createdFinDoc = new Document();
        createdFinDoc.put(Field.rsrcItemIdFinDoc, 123);
        createResult.setCreatedFinDocs(Collections.singletonList(createdFinDoc));
        Mockito.when(graphQLService.executeRequest(argThat(createFinDoc), Mockito.<Class<FinDocBatchCreateResult>>any())).thenReturn(createResult);
        // Mock importTermsheetService
        Mockito.when(importTermsheetService.queryFinDocByKey(
                CTRY_REC_CDE_HK, GRP_MEMBR_REC_CDE_HBAP, "APPENDIX", "SID", "CNY002", "BL"
        )).thenReturn(originalProduct);
        TermsheetFile termsheetFile = new TermsheetFile(
                new File(CommonUtils.readResource("/test/SID_termsheet_pdf/sid_ap_CNY002_bl.pdf")),
                BatchConstants.FILE_SUFFIX_PDF, "APPENDIX", "BL", CTRY_REC_CDE_HK, GRP_MEMBR_REC_CDE_HBAP, "SID", "CNY002");
        File ackFile = new File(CommonUtils.readResource("/test/SID_termsheet_pdf/sid_ap_CNY002_bl.ack"));
        importTermsheetService.processTermsheetData(originalProduct, termsheetFile, ackFile);
        Mockito.verify(productService,atLeast(1)).batchUpdateProductById(any());
    }
}
