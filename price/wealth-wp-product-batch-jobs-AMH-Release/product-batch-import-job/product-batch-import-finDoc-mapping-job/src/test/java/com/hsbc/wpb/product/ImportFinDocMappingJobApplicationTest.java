package com.dummy.wpb.product;


import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.component.FinDocFilesProcessor;
import com.dummy.wpb.product.component.FinDocFilesWriter;
import com.dummy.wpb.product.component.FinDocMappingUploadJoblistener;
import com.dummy.wpb.product.component.FinDocProdRLService;
import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.EmailContent;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.FinDocMapInput;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.model.ProdTypeFinDocRelPo;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateByIdInput;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateInput;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.model.graphql.ReferenceData;
import com.dummy.wpb.product.service.FinDocCollectionsService;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.service.ReferenceDataService;
import com.dummy.wpb.product.util.LegacyConfig;
import com.dummy.wpb.product.utils.FinDocUtils;
import com.dummy.wpb.product.utils.Namefilter;
import com.dummy.wpb.product.validation.MappingValidation;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.batch.core.ExitStatus.COMPLETED;

@SpringBatchTest
@SpringBootTest(classes = ImportFinDocMappingJobApplication.class)
@ActiveProfiles("test")
public class ImportFinDocMappingJobApplicationTest {


    @Autowired
    private MappingValidation fdv;

    @MockBean
    MongoTemplate mongoTemplate;


    ArrayList<EmailContent> rejectRec = new ArrayList<>();

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
    ProdTypeFinDocRelPo prodTypeFinDocRelPo;

    static Document prod;
    static JobParameters jobParameters;
    static JobExecution jobExecution;

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    FinDocFilesProcessor processor;


    public static JobExecution getJobExecution() {
        return MetaDataInstanceFactory.createJobExecution("finDocMappingUploadJob", 1L, 1L, jobParameters);
    }

    public StepExecution getStepExecution() {
        return MetaDataInstanceFactory.createStepExecution("importFinDocMappingWithExcelStep", 1L);
    }



    @BeforeAll
    public static void setUp() throws IOException {
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
//                 .addString("mappingFilePaths",String.join(",", mappingFile))
                .addString("mappingFilePaths", "HKHBAP_MAP.WPCE0040.D230428_T153500.230428073559.230627180330.xls")
                .toJobParameters();
        jobExecution = MetaDataInstanceFactory.createJobExecution("finDocMappingUploadJob", 1L, 1L, jobParameters);
        prod = new Document();
        prod.put(Field.prodId, 10086);
        prod.put(Field._id, 10086);
    }

    @Test
    void testJobLaunch() throws Exception {
        Job finDocMappingUploadJob = applicationContext.getBean("finDocMappingUploadJob", Job.class);
        jobLauncherTestUtils.setJob(finDocMappingUploadJob);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        assertEquals(COMPLETED, jobExecution.getExitStatus());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testFinDocFilesProcess(boolean isValid) throws Exception {
        FinDocMapInput finDocMapInput = new FinDocMapInput();
        finDocMapInput.setProdId("*");
        finDocMapInput.setProdRealID(0L);
        finDocMapInput.setActnCde("A");
        List<String> mappingFileNames = new ArrayList<>();
        mappingFileNames.add("file1");
        mappingFileNames.add("file2");
        StepExecution stepExecution = Mockito.mock(StepExecution.class);
        JobExecution jobExecution = Mockito.mock(JobExecution.class);
        ExecutionContext executionContext = Mockito.mock(ExecutionContext.class);
        MappingValidation mappingValidation = Mockito.mock(MappingValidation.class);
        when(mappingValidation.validation()).thenReturn(isValid);
        when(jobExecution.getExecutionContext()).thenReturn(executionContext);
        when(stepExecution.getJobExecution()).thenReturn(jobExecution);
        when(jobExecution.getExecutionContext().get("mappingFileNames")).thenReturn(mappingFileNames);
        ReflectionTestUtils.setField(processor, "stepExecution", stepExecution);
        ReflectionTestUtils.setField(processor, "fdv", mappingValidation);
        processor.process(finDocMapInput);
        assertTrue(true);
    }


    @Test
    void testReadFileNameTasklet_existFile() {
        MappingValidation mappingValidation = Mockito.mock(MappingValidation.class);
        EmailContent emailContent = new EmailContent();
        rejectRec.add(emailContent);
        Mockito.when(mappingValidation.getRejectRec()).thenReturn(rejectRec);
        List<String> mappingFileNames = Arrays.asList("abc", "def");
        FinDocMappingUploadJoblistener finDocMappingUploadJoblistener = new FinDocMappingUploadJoblistener();
        ReflectionTestUtils.setField(finDocMappingUploadJoblistener, "fdv", mappingValidation);
        ReflectionTestUtils.setField(finDocMappingUploadJoblistener, "mappingFilePaths", Collections.singletonList("/test/req/incoming"));
        ReflectionTestUtils.setField(finDocMappingUploadJoblistener, "mappingFileNames", mappingFileNames);
        ReflectionTestUtils.setField(finDocMappingUploadJoblistener, "finDocMapInFilePrefix", "MAP.WPCE,MAP.HFIE");
        ReflectionTestUtils.setField(finDocMappingUploadJoblistener, "finDocPath", "src/test/resources/test/req");
        ReflectionTestUtils.setField(finDocMappingUploadJoblistener, "finDocSrcPath", "src/test/resources/test/req/incoming/HKHBAP_MAP.WPCE0040.D230428_T153500.230428073559.230627180330.xls");
        JobExecution jobExecution = getJobExecution();
        jobExecution.setStatus(BatchStatus.FAILED);
        finDocMappingUploadJoblistener.beforeJob(getJobExecution());
        finDocMappingUploadJoblistener.afterJob(getJobExecution());
        assertNotNull(jobExecution.getJobId());
    }

    @Test
    void testFinDocFilesWriterCrud1() throws Exception {

        when(mongoDatabase.getCollection(CollectionName.prod_type_fin_doc.toString())).thenReturn(anyColl);
        FinDocProdRLService finDocProdRLService = new FinDocProdRLService(mongoDatabase);
        when(productService.productByFilters(Mockito.anyMap())).thenReturn(Arrays.asList(prod));
        FinDocCollectionsService finDocCollectionsService = new FinDocCollectionsService(graphQLService);
        List<FinDocMapInput> list = new ArrayList<>();
        FinDocMapInput finDocMapInput = new FinDocMapInput();
        finDocMapInput.setProdId("*");
        finDocMapInput.setProdRealID(0L);
        finDocMapInput.setActnCde("A");
        list.add(finDocMapInput);

        FinDocMapInput finDocMapInput1 = new FinDocMapInput();
        finDocMapInput1.setProdId("*");
        finDocMapInput1.setProdRealID(0L);
        finDocMapInput1.setActnCde("D");
        list.add(finDocMapInput1);
        fdv.setRejectRec(rejectRec);

        FinDocMapInput finDocMapInput3 = new FinDocMapInput();
        finDocMapInput3.setProdId("*");
        finDocMapInput3.setProdRealID(0L);
        finDocMapInput3.setActnCde("C");
        list.add(finDocMapInput3);

        FinDocMapInput finDocMapInput4 = new FinDocMapInput();
        finDocMapInput4.setProdId("*");
        finDocMapInput4.setProdRealID(0L);
        finDocMapInput4.setActnCde("M");
        list.add(finDocMapInput4);

        ProductBatchUpdateInput[] productBatchUpdateInput = new ProductBatchUpdateInput[1];
        Mockito.when(productService.batchUpdateProduct(Mockito.any(ProductBatchUpdateInput.class))).then(invocationOnMock -> {
            productBatchUpdateInput[0] = invocationOnMock.getArgument(1, ProductBatchUpdateInput.class);
            return new ProductBatchUpdateResult();
        });

        FinDocFilesWriter finDocFilesWriter = new FinDocFilesWriter();
        ReflectionTestUtils.setField(finDocFilesWriter, "fdv", fdv);
        ReflectionTestUtils.setField(finDocFilesWriter, "finDocProdRLService", finDocProdRLService);
        ReflectionTestUtils.setField(finDocFilesWriter, "stepExecution", getStepExecution());
        ReflectionTestUtils.setField(finDocProdRLService, "finDocCollectionsService", finDocCollectionsService);
        ReflectionTestUtils.setField(finDocProdRLService, "productService", productService);
        finDocFilesWriter.write(list);
        assertNotNull(productBatchUpdateInput);
    }

    @Test
    void testFinDocFilesWriterForCrud2() throws Exception {

        when(mongoDatabase.getCollection(CollectionName.prod_type_fin_doc.toString())).thenReturn(anyColl);
        FinDocProdRLService finDocProdRLService = new FinDocProdRLService(mongoDatabase);
        when(productService.productByFilters(Mockito.anyMap())).thenReturn(Arrays.asList(prod));
        FinDocCollectionsService finDocCollectionsService = new FinDocCollectionsService(graphQLService);
        List<FinDocMapInput> list = new ArrayList<>();
        FinDocMapInput finDocMapInput = new FinDocMapInput();
        finDocMapInput.setProdId("1");
        finDocMapInput.setProdRealID(10086L);
        finDocMapInput.setActnCde("A");
        list.add(finDocMapInput);

        FinDocMapInput finDocMapInput1 = new FinDocMapInput();
        finDocMapInput1.setProdId("1");
        finDocMapInput1.setProdRealID(10086L);
        finDocMapInput1.setActnCde("D");
        list.add(finDocMapInput1);
        fdv.setRejectRec(rejectRec);

        FinDocMapInput finDocMapInput3 = new FinDocMapInput();
        finDocMapInput3.setProdId("1");
        finDocMapInput3.setProdRealID(10086L);
        finDocMapInput3.setActnCde("C");
        list.add(finDocMapInput3);

        FinDocMapInput finDocMapInput4 = new FinDocMapInput();
        finDocMapInput4.setProdId("1");
        finDocMapInput4.setProdRealID(10086L);
        finDocMapInput4.setActnCde("M");
        list.add(finDocMapInput4);

        FinDocMapInput finDocMapInput5 = new FinDocMapInput();
        finDocMapInput5.setProdId("1");
        finDocMapInput5.setProdSubtpCde("test");
        finDocMapInput5.setActnCde("M");
        list.add(finDocMapInput5);

        FinDocMapInput finDocMapInput6 = new FinDocMapInput();
        finDocMapInput6.setProdId("*");
        finDocMapInput6.setProdRealID(10086L);
        finDocMapInput6.setActnCde("D");
        list.add(finDocMapInput6);

        ProductBatchUpdateByIdInput[] productBatchUpdateByIdInput = new ProductBatchUpdateByIdInput[1];
        Mockito.when(productService.batchUpdateProductById(Mockito.any())).then(invocationOnMock -> {
            productBatchUpdateByIdInput[0] = (ProductBatchUpdateByIdInput)invocationOnMock.getArgument(0, List.class).get(0);
            ProductBatchUpdateResult updateResult = new ProductBatchUpdateResult();
            updateResult.setUpdatedProducts(Collections.emptyList());
            updateResult.setInvalidProducts(Collections.emptyList());
            return updateResult;
        });

        Document inner = new Document();
        inner.put("value", "abc");
        Mockito.when(graphQLService.executeRequest(Mockito.any(GraphQLRequest.class), Mockito.any(TypeReference.class))).thenReturn(Collections.singletonList(inner));

        FinDocFilesWriter finDocFilesWriter = new FinDocFilesWriter();
        ReflectionTestUtils.setField(finDocFilesWriter, "fdv", fdv);
        ReflectionTestUtils.setField(finDocFilesWriter, "finDocProdRLService", finDocProdRLService);
        ReflectionTestUtils.setField(finDocFilesWriter, "stepExecution", getStepExecution());
        ReflectionTestUtils.setField(finDocProdRLService, "finDocCollectionsService", finDocCollectionsService);
        ReflectionTestUtils.setField(finDocProdRLService, "productService", productService);
        finDocFilesWriter.write(list);
        assertNotNull(productBatchUpdateByIdInput);
    }


    @Test
    void testMppindValidation() throws Exception {

        StepExecution stepExecution = new StepExecution("teststep", null);
        List<String> mappingFileNames = Arrays.asList("abc", "def");
        stepExecution.getExecutionContext().put("mappingFileNames", mappingFileNames);

        FinDocMapInput finDocMapInput = new FinDocMapInput();
        finDocMapInput.setActnCde("D");
        finDocMapInput.setCtryCde("HK");
        finDocMapInput.setOrgnCde("HBAP");
        finDocMapInput.setProdTypeCde("GN");
        finDocMapInput.setProdSubtpCde("*");
        finDocMapInput.setProdId("a");
        finDocMapInput.setDocTypeCdeP("P");
        finDocMapInput.setDocSource("DOC");
        finDocMapInput.setEmailAdrRpyText("abc");
        finDocMapInput.setLangCatCde("TW");
        finDocMapInput.setLangCatCdeP("TW");
        ReferenceData referenceData = new ReferenceData();
        referenceData.setCdvDesc("abc");
        referenceData.setCdvCde("efg");


        Mockito.when(referenceDataService.referenceDataByFilter(Mockito.any())).thenReturn(Collections.singletonList(referenceData));
        List<ProdTypeFinDocRelPo> list = new ArrayList<>();
        ProdTypeFinDocRelPo prodTypeFinDocRelPo = new ProdTypeFinDocRelPo();
        prodTypeFinDocRelPo.setCtryRecCde("HK");
        list.add(prodTypeFinDocRelPo);
        MappingValidation mappingValidation = new MappingValidation();
        Mockito.when(mongoTemplate.find(Mockito.any(), Mockito.eq(ProdTypeFinDocRelPo.class))).thenReturn(list);

        ReflectionTestUtils.setField(mappingValidation, "fd", finDocMapInput);
        ReflectionTestUtils.setField(mappingValidation, "referenceDataService", referenceDataService);
        ReflectionTestUtils.setField(mappingValidation, "rejectRec", rejectRec);
        ReflectionTestUtils.setField(mappingValidation, "mongoTemplate", mongoTemplate);
        Namefilter namefilter = new Namefilter("B", true);
        namefilter.accept(new File(""), "ut_fs_U500077_en.pdf");
        namefilter.matching("HKHBAP_MAP.WPCE0040.D230428_T153500.230428073559.230627180330.xls", "HKHBAP_MAP.WPCE0040.D230428_T153500.230428073559.230627180330.xls", true, true);
        namefilter.matching("HKHBAP_MAP.WPCE0040.D230428_T153500.230428073559.230627180330.xls", "HKHBAP_MAP.WPCE0040.D230428_T153500.230428073559.230627180330.xls", false, true);
        boolean matching = namefilter.matching("HKHBAP_MAP.WPCE0040.D230428_T153500.230428073559.230627180330.xls", "HKHBAP_MAP.WPCE0040.D230428_T153500.230428073559.230627180330.xls", true, false);
        mappingValidation.validation();
        assertTrue(matching);
        assertThrows(IOException.class, () -> {
            FinDocUtils.copyAndRenameFile(new File(""));
        });
    }


}