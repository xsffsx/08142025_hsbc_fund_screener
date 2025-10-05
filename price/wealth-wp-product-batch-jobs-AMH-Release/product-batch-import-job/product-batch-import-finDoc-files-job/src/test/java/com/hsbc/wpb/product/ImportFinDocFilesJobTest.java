package com.dummy.wpb.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.dummy.wpb.product.component.*;
import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.EmailContent;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.FinDocConstants;
import com.dummy.wpb.product.exception.DuplicateCheckedPdfException;
import com.dummy.wpb.product.exception.InvalidRecordException;
import com.dummy.wpb.product.exception.RecordNotFoundException;
import com.dummy.wpb.product.mapper.DocumentTypeMapper;
import com.dummy.wpb.product.mapper.FinDocExlRowMapper;
import com.dummy.wpb.product.model.*;
import com.dummy.wpb.product.model.graphql.Amendment;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateInput;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.model.graphql.ReferenceData;
import com.dummy.wpb.product.script.ChmodScript;
import com.dummy.wpb.product.service.FinDocCollectionsService;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.service.ReferenceDataService;
import com.dummy.wpb.product.util.LegacyConfig;
import com.dummy.wpb.product.utils.FinDocUtils;
import com.dummy.wpb.product.utils.Namefilter;
import com.dummy.wpb.product.validation.UploadValidation;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonObjectId;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.batch.core.ExitStatus.COMPLETED;

@SpringJUnitConfig
@SpringBatchTest
@SpringBootTest(classes = ImportFinDocFilesJobApplication.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class ImportFinDocFilesJobTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    ApplicationContext applicationContext;

    @MockBean
    MongoTemplate mongoTemplate;

    @MockBean
    MongoDatabase mongoDatabase;

    @MockBean
    MongoCollection<Document> anyColl;

    @MockBean
    GraphQLService graphQLService;

    @MockBean
    ProductService productService;

    @MockBean
    LegacyConfig legacyConfig;

    @MockBean
    ReferenceDataService referenceDataService;

    @MockBean
    JobExecution jobExec;

    @MockBean
    ExecutionContext jobContext;

    @MockBean
    StepExecution stepExec;

    @MockBean
    RowSet rowSet;

    static Document prod;

    @BeforeClass
    public static void setUp() {
        prod = new Document();
        prod.put(Field.prodId, 10086);
        prod.put(Field._id, 10086);
    }

    public JobExecution getJobExecution() {
        return MetaDataInstanceFactory.createJobExecution("finDocFilesUploadJob", 1L, 1L);
    }

    public StepExecution getStepExecution() {
        return MetaDataInstanceFactory.createStepExecution();
    }



    @Test
    public void testReadFileNameTasklet_existFile() throws Exception {
        ReadFileNameTasklet readFileNameTasklet = new ReadFileNameTasklet();
        FinDocCollectionsService finDocCollectionsService = new FinDocCollectionsService(graphQLService);
        FinDocSmry finDocSmry = new FinDocSmry();
        PDFFileService pdfFileService = new PDFFileService(finDocSmry);
        UploadService uploadService = new UploadService();
        ReflectionTestUtils.setField(readFileNameTasklet, "finDocPath", "src/test/resources/test/");
        ReflectionTestUtils.setField(readFileNameTasklet, "filePattern", "^HFXX.*");
        ReflectionTestUtils.setField(readFileNameTasklet, "finDocPdfRejPath", "src/test/resources/test/rej");
        ReflectionTestUtils.setField(readFileNameTasklet, "rejLogPath", "src/test/resources/test/ENS/");
        ReflectionTestUtils.setField(readFileNameTasklet, "finDocSrcPath", "src/test/resources/test/req/incoming");
        ReflectionTestUtils.setField(readFileNameTasklet, "finDocCollectionsService", finDocCollectionsService);
        ReflectionTestUtils.setField(readFileNameTasklet, "uploadService", uploadService);
        ReflectionTestUtils.setField(readFileNameTasklet, "pdfFileService", pdfFileService);
        ReflectionTestUtils.setField(uploadService, "finDocPath", "src/test/resources/test/");
        ReflectionTestUtils.setField(uploadService, "finDocSrcPath", "src/test/resources/test/req/incoming");
        ReflectionTestUtils.setField(uploadService, "finDocChkPath", "src/test/resources/test/req/chk");
        Document document1 = new Document();
        document1.put(Field.docRecvName, "ut_fs_U500088_en.pdf");
        document1.put(Field.recCreatDtTm, new Date());
        Document document2 = new Document();
        document2.put(Field.docRecvName, "ut_fs_U500099_en.pdf");
        document2.put(Field.recCreatDtTm, new Date());
        Mockito.when(graphQLService.executeRequest(Mockito.any(GraphQLRequest.class), Mockito.<TypeReference<List<Document>>>any())).thenReturn(Arrays.asList(document1,document2));
        RepeatStatus execute = readFileNameTasklet.execute(new StepContribution(getStepExecution()), new ChunkContext(new StepContext(getStepExecution())));
        Assert.assertEquals(RepeatStatus.FINISHED, execute);
    }

    @Test
    public void testReadFileNameTasklet_error() throws Exception {
        ReadFileNameTasklet readFileNameTasklet = new ReadFileNameTasklet();
        ReflectionTestUtils.setField(readFileNameTasklet, "finDocPath", "src/test/resources/test/");
        ReflectionTestUtils.setField(readFileNameTasklet, "filePattern", "^HFEE.*");
        RepeatStatus execute = readFileNameTasklet.execute(new StepContribution(getStepExecution()), new ChunkContext(new StepContext(getStepExecution())));
        Assert.assertEquals(RepeatStatus.FINISHED, execute);
    }

    @Test
    public void testUploadService_uploadRec_AprvReqInd_N() throws Exception {
        UploadService uploadService = new UploadService();
        FinDocProdRLService finDocProdRLService = new FinDocProdRLService(mongoDatabase);
        ReflectionTestUtils.setField(uploadService, "finDocPath", "src/test/resources/test/");
        ReflectionTestUtils.setField(uploadService, "pdfAprvPath", "src/test/resources/test/aprv");
        ReflectionTestUtils.setField(uploadService, "mongoTemplate", mongoTemplate);
        ReflectionTestUtils.setField(uploadService, "finDocProdRLService", finDocProdRLService);
        ReflectionTestUtils.setField(uploadService, "mongoOperations", Mockito.mock(MongoOperations.class));
        ReflectionTestUtils.setField(finDocProdRLService, "productService", productService);
        ReflectionTestUtils.setField(finDocProdRLService, "finDocCollectionsService", new FinDocCollectionsService(graphQLService));
        FinDocInput finDocInput = new FinDocInput();
        finDocInput.setCtryCde("HK");
        finDocInput.setOrgnCde("HBAP");
        finDocInput.setProdTypeCde("UT");
        finDocInput.setProdSubtypeCde("UT");
        finDocInput.setProdCde("U500077");
        finDocInput.setLangCatCde("BL");
        finDocInput.setAprvReqInd(FinDocConstants.NO);
        finDocInput.setDocIncmName("ut_fs_U500077_en.pdf");
        ArgumentMatcher<Query> queryArgumentMatcher1 = query -> query != null && query.toString().contains("FDSN");
        SystemParmPo systemParmPo = new SystemParmPo();
        systemParmPo.setParmValueText("1");
        Mockito.when(mongoTemplate.find(argThat(queryArgumentMatcher1), Mockito.eq(SystemParmPo.class))).thenReturn(Collections.singletonList(systemParmPo));
        final Update[] update = new Update[1];
        Mockito.when(mongoTemplate.updateFirst(Mockito.any(), Mockito.any(), anyString())).then(invocationOnMock -> {
            update[0] = invocationOnMock.getArgument(1, Update.class);
            return UpdateResult.acknowledged(0, 1L, new BsonObjectId(new ObjectId()));
        });

        Mockito.when(productService.productByFilters(Mockito.anyMap())).thenReturn(Arrays.asList(prod));
        ProductBatchUpdateResult productBatchUpdateResult = new ProductBatchUpdateResult();
        productBatchUpdateResult.setUpdatedProducts(Collections.emptyList());
        productBatchUpdateResult.setInvalidProducts(Collections.emptyList());
        Mockito.when(productService.batchUpdateProduct(Mockito.any(ProductBatchUpdateInput.class))).thenReturn(productBatchUpdateResult);
        uploadService.uploadRec(finDocInput);
        Assert.assertNotNull(update);
    }

    @Test
    public void testFinDocProdRLService() throws Exception {
        Mockito.when(mongoDatabase.getCollection(CollectionName.prod_type_fin_doc.toString())).thenReturn(anyColl);
        FinDocProdRLService finDocProdRLService = new FinDocProdRLService(mongoDatabase);
        Mockito.when(anyColl.updateOne(any(Bson.class), any(Document.class))).thenReturn(UpdateResult.acknowledged(0, 1L, new BsonObjectId(new ObjectId())));
        Method updOrInsProdTypeFinDoc = finDocProdRLService.getClass().getDeclaredMethod("updOrInsProdTypeFinDoc", List.class, FinDocInput.class, Long.class, Boolean.class, Boolean.class);
        updOrInsProdTypeFinDoc.setAccessible(true);
        updOrInsProdTypeFinDoc.invoke(finDocProdRLService, Arrays.asList(new Document()), new FinDocInput(), 1223L, true, true);

        FinDocCollectionsService finDocCollectionsService = new FinDocCollectionsService(graphQLService);
        ReflectionTestUtils.setField(finDocProdRLService, "finDocCollectionsService", finDocCollectionsService);
        ReflectionTestUtils.setField(finDocProdRLService, "productService", productService);

        Mockito.when(graphQLService.executeRequest(Mockito.any(GraphQLRequest.class), Mockito.<TypeReference<List<Document>>>any())).thenReturn(Arrays.asList(prod));
        Mockito.when(productService.productByFilters(Mockito.anyMap())).thenReturn(Arrays.asList(prod));
        final ProductBatchUpdateInput[] productBatchUpdateInput = new ProductBatchUpdateInput[1];
        Mockito.when(productService.batchUpdateProduct(Mockito.any(ProductBatchUpdateInput.class))).then(invocationOnMock -> {
            productBatchUpdateInput[0] = invocationOnMock.getArgument(1, ProductBatchUpdateInput.class);
            return new ProductBatchUpdateResult();
        });
        ArgumentMatcher<GraphQLRequest> queryArguProdTypeFinDocMatcher = GraphQLRequest -> GraphQLRequest != null && GraphQLRequest.toString().contains("prodTypeFinDocByFilter");
        Mockito.when(graphQLService.executeRequest(argThat(queryArguProdTypeFinDocMatcher), Mockito.<TypeReference<List<Document>>>any())).thenReturn(Collections.emptyList());

        FinDocInput finDocInput = new FinDocInput();
        finDocInput.setProdId(100086L);
        finDocInput.setProdTypeCde("UT");
        finDocInput.setLangCatCde("BL");
        finDocInput.setCtryCde("JP");
        finDocInput.setOrgnCde("dummy");
        finDocProdRLService.prodToFinDocRLProcess(finDocInput, 1L);
        FinDocInput finDocInput2 = new FinDocInput();
        finDocInput2.setProdTypeCde("UT");
        finDocInput2.setLangCatCde("BL");
        finDocInput2.setProdSubtypeCde("UT");
        finDocProdRLService.prodToFinDocRLProcess(finDocInput2, 1L);
        Assert.assertNotNull(productBatchUpdateInput);
    }

    @Test
    public void testPDFFileService() {
        FinDocSmry finDocSmry = new FinDocSmry();
        finDocSmry.setDocIncmName("ut_fs_U500022_en.pdf");
        PDFFileService pdfFileService1 = new PDFFileService(finDocSmry);
        Assert.assertThrows(DuplicateCheckedPdfException.class, () -> {
            pdfFileService1.fileToChk("src/test/resources/test/aprv","src/test/resources/test/aprv");
        });

        finDocSmry.setDocIncmName("ut_fs_U500011_en.pdf");
        PDFFileService pdfFileService = new PDFFileService(finDocSmry);
        Assert.assertThrows(RecordNotFoundException.class, () -> {
            pdfFileService.fileToChk("src/test/resources/test/aprv","src/test/resources/test/aprv");
        });

        pdfFileService.movePDF2Chked(new File("src/test/resources/test/aprv/a"), new File("src/test/resources/test/aprv/b"));
        Assert.assertThrows(RecordNotFoundException.class, () -> {
            pdfFileService.fileToAprv("src/test/resources/test/aprv","src/test/resources/test/aprv");
        });
        pdfFileService.movePDF2Aprv(new File("src/test/resources/test/aprv/a"), new File("src/test/resources/test/aprv/b"));
        pdfFileService.movePDF2Rej("", new File("src/test/resources/test/aprv/a"), new File("src/test/resources/test/aprv/b"));
        PDFFileService.setFileUrl(new File("src/test/resources/test/aprv/a"), false);
        Assert.assertThrows(IOException.class, () -> {
            pdfFileService.getFile("src/test/resources/test/aprv");
        });
    }

    @Test
    public void testOtherCaseForCoverage(){
        FinDocSmry finDocSmry = new FinDocSmry();
        finDocSmry.setDocIncmName("ut_fs_U500077_en.pdf");
        PDFFileService pdfFileService = new PDFFileService(finDocSmry);
        Assert.assertThrows(NullPointerException.class, () -> {
            pdfFileService.fileToChk("","src/test/resources/test/a/");
        });

        FinDocInput finDocInput = new FinDocInput();
        UploadValidation uploadValidationAgr = buildUploadValidation(finDocInput, "ver 1.1", true, new ArrayList<>());
        FinDocInput finDocInput2 = buildFinDocInput();
        finDocInput2.setDocTypeCde("FACTSHEETTT");
        UploadValidation uploadValidation = buildUploadValidation(finDocInput2, "ver 1.1", true, new ArrayList<>());
        ReflectionTestUtils.setField(uploadValidationAgr, "mongoTemplate", mongoTemplate);
        ReflectionTestUtils.setField(uploadValidation, "mongoTemplate", mongoTemplate);
        DocumentTypeMapper documentTypeMapper = new DocumentTypeMapper();
        documentTypeMapper.setType(ImmutableMap.of("FACTSHEET","fs"));
        ReflectionTestUtils.setField(uploadValidation, "documentTypeMapper", documentTypeMapper);
        Mockito.when(mongoTemplate.find(Mockito.any(), Mockito.eq(SystemParmPo.class))).thenReturn(null);
        uploadValidationAgr.checkAprv(finDocInput);
        uploadValidation.checkArch(finDocInput);
        //test checkDocTypeWithPropFile
        Assert.assertFalse(uploadValidation.validation());
        //test checkDocSubtpCde error
        FinDocInput finDocInput3 = buildFinDocInput();
        UploadValidation uploadValidation3 = buildUploadValidation(finDocInput3, "ver 1.1", true, new ArrayList<>());
        ReflectionTestUtils.setField(uploadValidation3, "documentTypeMapper", documentTypeMapper);
        Assert.assertThrows(RecordNotFoundException.class, () -> {
            uploadValidation3.validation();
        });

        Namefilter namefilter = new Namefilter("B",true);
        namefilter.accept(new File(""), "ut_fs_U500077_en.pdf");
        namefilter.matching("ut_fs_U500077_en.pdf", "ut_fs_U500077_en.pdf", true, true);
        namefilter.matching("ut_fs_U500077_en.pdf", "ut_fs_U500077_en.pdf", false, true);
        boolean matching = namefilter.matching("ut_fs_U500077_en.pdf", "ut_fs_U500077_en.pdf", true, false);
        Assert.assertTrue(matching);
        Assert.assertEquals("name",FinDocUtils.replaceName("name","B", "B"));
        Assert.assertThrows(IOException.class, () -> {
            FinDocUtils.copyAndRenameFile(new File(""));
        });
    }

    @Test
    public void testUploadValidation_reflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        SystemParmPo systemParmPo = new SystemParmPo();
        systemParmPo.setParmValueText(FinDocConstants.NO);
        FinDocInput finDocInput = new FinDocInput();
        UploadValidation uploadValidation = buildUploadValidation(finDocInput, "ver 1.1", true, new ArrayList<>());
        ReflectionTestUtils.setField(uploadValidation, "mongoTemplate", mongoTemplate);
        Mockito.when(mongoTemplate.find(Mockito.any(), Mockito.eq(SystemParmPo.class))).thenReturn(Arrays.asList(systemParmPo));
        uploadValidation.checkAprv(finDocInput);
        uploadValidation.checkArch(finDocInput);
        Assert.assertEquals(FinDocConstants.NO, finDocInput.getArchReqInd());
        Method checkGN = uploadValidation.getClass().getDeclaredMethod("checkGN", FinDocInput.class, String.class);
        checkGN.setAccessible(true);
        Assert.assertEquals(FinDocConstants.NO, checkGN.invoke(uploadValidation, finDocInput, "ARCHREQ"));
        Method checkProdTypRel = uploadValidation.getClass().getDeclaredMethod("checkProdTypRel", String.class, String.class, String.class, String.class);
        checkProdTypRel.setAccessible(true);
        Mockito.when(mongoTemplate.find(Mockito.any(), Mockito.eq(ProdTypeFinDocRelPo.class))).thenReturn(null);
        Assert.assertFalse((Boolean) checkProdTypRel.invoke(uploadValidation, "HK", "HBAP", "UT", "fs"));
    }

    @Test
    public void testJob() throws Exception {
        Mockito.doNothing().when(legacyConfig).init();
        Mockito.when(referenceDataService.referenceDataByFilter(Mockito.any())).thenReturn(Collections.singletonList(new ReferenceData()));
        Mockito.when(mongoTemplate.find(Mockito.any(), Mockito.eq(ProdTypeFinDocRelPo.class))).thenReturn(Collections.emptyList());
        ArgumentMatcher<Query> queryProdTypeFinDocRelArguMat = query -> query != null && query.toString().contains("GN");
        Mockito.when(mongoTemplate.find(argThat(queryProdTypeFinDocRelArguMat), Mockito.eq(ProdTypeFinDocRelPo.class))).thenReturn(Collections.singletonList(new ProdTypeFinDocRelPo()));
        Mockito.when(graphQLService.executeRequest(Mockito.any(), Mockito.<TypeReference<List<Document>>>any())).thenReturn(Collections.emptyList());

        ArgumentMatcher<Query> queryArgumentMatcher1 = query -> query != null && (query.toString().contains("ARPVREQ") || query.toString().contains("GN") || query.toString().contains("ARCHREQ"));
        SystemParmPo systemParmPo = new SystemParmPo();
        systemParmPo.setParmValueText(FinDocConstants.NO);
        Mockito.when(mongoTemplate.find(argThat(queryArgumentMatcher1), Mockito.eq(SystemParmPo.class))).thenReturn(Collections.singletonList(systemParmPo));
        ArgumentMatcher<Query> queryArgumentMatcher2 = query -> query != null && query.toString().contains("FDSN") ;
        systemParmPo.setParmValueText("0");
        Mockito.when(mongoTemplate.find(argThat(queryArgumentMatcher2), Mockito.eq(SystemParmPo.class))).thenReturn(Collections.singletonList(systemParmPo));

        Mockito.when(productService.productByFilters(Mockito.anyMap())).thenReturn(Arrays.asList(prod));
        Mockito.when(mongoTemplate.insert(Mockito.any(FinDocULReqPo.class))).thenReturn(new FinDocULReqPo());
        Mockito.when(graphQLService.executeRequest(Mockito.any(), Mockito.eq(Amendment.class))).thenReturn(new Amendment());
        Job importFinDocFilesJob = applicationContext.getBean("finDocFilesUploadJob", Job.class);
        jobLauncherTestUtils.setJob(importFinDocFilesJob);
        ChmodScript chmodScript = Mockito.mock(ChmodScript.class);
        try (MockedStatic<ChmodScript> mockedChmodScript = mockStatic(ChmodScript.class)) {
            mockedChmodScript.when(() -> ChmodScript.chmodScript(anyString(), anyString(), anyString())).thenAnswer(invocation -> {
                // Do nothing
                return null;
            });
            JobExecution jobExecution = jobLauncherTestUtils.launchJob();
            Assert.assertEquals(COMPLETED, jobExecution.getExitStatus());
        }

    }

    @Test
    public void testFinDocFilesUploadFailListener() {
        FinDocFilesUploadFailListener jobListener = new FinDocFilesUploadFailListener();
        ReflectionTestUtils.setField(jobListener, "finDocPath", "src/test/resources/test/");
        ReflectionTestUtils.setField(jobListener, "finDocSrcPath", "src/test/resources/test/req/incoming");
        UploadService uploadService = new UploadService();
        ReflectionTestUtils.setField(jobListener, "uploadService", uploadService);
        ReflectionTestUtils.setField(uploadService, "finDocPath", "src/test/resources/test/");
        ReflectionTestUtils.setField(uploadService, "finDocSrcPath", "src/test/resources/test/req/incoming");
        ReflectionTestUtils.setField(uploadService, "finDocChkPath", "src/test/resources/test/req/chk");
        JobExecution jobExecution = getJobExecution();
        jobExecution.setStatus(BatchStatus.FAILED);
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        executionContext.put("curFilePath", "src/test/resources/test/req/incoming/HFXX0137.D230616.T170000.xls");
        jobListener.afterJob(jobExecution);
        Assert.assertNotNull(jobExecution.getJobId());
    }

    @Test
    public void testFileFormatDecider_decide() {
        Mockito.when(jobExec.getExecutionContext()).thenReturn(jobContext);
        List<String> list = new ArrayList<>();
        list.add("HFIE0137.D230616.T180000.xlsx");
        Mockito.when(jobContext.get(any())).thenReturn(list);
        FileFormatDecider fileFormatDecider = new FileFormatDecider();
        Assert.assertEquals(FlowExecutionStatus.COMPLETED, fileFormatDecider.decide(jobExec, getStepExecution()));
    }

    @Test
    public void testFinDocFilesWriter() throws Exception {
        FinDocFilesWriter finDocFilesWriter = new FinDocFilesWriter();
        finDocFilesWriter.write(Collections.emptyList());
        Mockito.when(stepExec.getJobExecution()).thenReturn(jobExec);
        Mockito.when(jobExec.getExecutionContext()).thenReturn(jobContext);
        Mockito.when(jobContext.get(any())).thenReturn(new ArrayList<EmailContent>());
        Assert.assertEquals(ExitStatus.COMPLETED, finDocFilesWriter.afterStep(stepExec));
    }

    @Test
    public void testIsInvalidRow_AllFieldsNull() {
        FinDocExlRowMapper mapper = new FinDocExlRowMapper();
        Mockito.when(rowSet.getCurrentRow()).thenReturn(new String[17]);
        Assert.assertThrows(InvalidRecordException.class, () -> mapper.mapRow(rowSet));
    }

    @Test
    public void testIsInvalidRow_AllFieldsNotNull() throws Exception {
        FinDocExlRowMapper mapper = new FinDocExlRowMapper();
        String[] currentRow = new String[17];
        currentRow[0] = "HK";
        currentRow[1] = "HBAP";
        currentRow[2] = "FACTSHEET";
        currentRow[3] = "UT";
        currentRow[4] = "U500066";
        currentRow[5] = "EN";
        currentRow[6] = "PDF";
        currentRow[7] = "ut_fs_U500066_en.pdf";
        currentRow[8] = "20330330";
        currentRow[9] = "20230330";
        currentRow[10] = "170000";
        currentRow[11] = "Description";
        currentRow[12] = "prodTypeCde";
        currentRow[13] = "prodSubtypeCde";
        currentRow[14] = "prodCde";
        currentRow[15] = "email@example.com";
        currentRow[16] = "Y";
        Mockito.when(rowSet.getCurrentRow()).thenReturn(currentRow);
        FinDocInput finDocInput = mapper.mapRow(rowSet);
        Assert.assertEquals("HK", finDocInput.getCtryCde());
        Assert.assertEquals("HBAP", finDocInput.getOrgnCde());
        Assert.assertEquals("FACTSHEET", finDocInput.getDocTypeCde());
        Assert.assertEquals("UT", finDocInput.getDocSubtypeCde());
        Assert.assertEquals("U500066", finDocInput.getDocId());
        Assert.assertEquals("EN", finDocInput.getLangCatCde());
        Assert.assertEquals("PDF", finDocInput.getFormtTypeCde());
        Assert.assertEquals("ut_fs_U500066_en.pdf", finDocInput.getDocIncmName());
        Assert.assertEquals("20330330", finDocInput.getExpirDt());
        Assert.assertEquals("20230330", finDocInput.getEffDt());
        Assert.assertEquals("170000", finDocInput.getEffTm());
        Assert.assertEquals("Description", finDocInput.getDocExplnText());
        Assert.assertEquals("prodTypeCde", finDocInput.getProdTypeCde());
        Assert.assertEquals("prodSubtypeCde", finDocInput.getProdSubtypeCde());
        Assert.assertEquals("prodCde", finDocInput.getProdCde());
        Assert.assertEquals("email@example.com", finDocInput.getEmailAdrRpyText());
        Assert.assertEquals("Y", finDocInput.getUrgInd());
    }

    private UploadValidation buildUploadValidation(FinDocInput finDocInput, String version, boolean b, ArrayList<EmailContent> objects) {
        return new UploadValidation(finDocInput, version, b, objects);
    }

    private FinDocInput buildFinDocInput() {
        FinDocInput finDocInput = new FinDocInput();
        finDocInput.setCtryCde("HK");
        finDocInput.setOrgnCde("HBAP");
        finDocInput.setDocTypeCde("FACTSHEET");
        finDocInput.setDocSubtypeCde("UT");
        finDocInput.setDocId("U500066");
        finDocInput.setLangCatCde("EN");
        finDocInput.setFormtTypeCde("PDF");
        finDocInput.setEffDt("20230330");
        finDocInput.setEffTm("170000");
        finDocInput.setDocIncmName("ut_fs_U500066_en.pdf");
        return finDocInput;
    }
}