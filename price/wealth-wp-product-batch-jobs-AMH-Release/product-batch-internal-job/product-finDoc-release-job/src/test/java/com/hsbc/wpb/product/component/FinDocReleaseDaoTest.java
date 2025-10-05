package com.dummy.wpb.product.component;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.FinDocConstants;
import com.dummy.wpb.product.exception.DuplicateKeyException;
import com.dummy.wpb.product.exception.RecordNotFoundException;
import com.dummy.wpb.product.model.FinDocHistPo;
import com.dummy.wpb.product.model.FinDocPo;
import com.dummy.wpb.product.model.FinDocULReqPo;
import com.dummy.wpb.product.model.SystemParmPo;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


class FinDocReleaseDaoTest {

    @Mock
    private MongoTemplate mongoTemplate;
    @Mock
    private MongoDatabase mongoDatabase;
    @Mock
    private MongoCollection<Document> colFinDocUpld;
    @Mock
    private MongoCollection<Document> colFinDocHist;

    @Mock
    private MongoCollection<Document> colProdTypeFinDoc;

    @Mock
    private MongoCollection<Document> colProduct;

    private FinDocReleaseDao finDocReleaseDao;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        finDocReleaseDao = new FinDocReleaseDao(mongoDatabase);
        ReflectionTestUtils.setField(finDocReleaseDao, "mongoTemplate", mongoTemplate);
        ReflectionTestUtils.setField(finDocReleaseDao, "colFinDocHist", colFinDocHist);
        ReflectionTestUtils.setField(finDocReleaseDao, "colFinDocUpld", colFinDocUpld);
        ReflectionTestUtils.setField(finDocReleaseDao, "colProduct", colProduct);
        ReflectionTestUtils.setField(finDocReleaseDao, "colProdTypeFinDoc", colProdTypeFinDoc);
    }

    @Test
    void shouldRetrieveFinDocSmryRecordByStatusRelease() {

        String ctryCde = "US";
        String orgnCde = "123";
        String statusFs = "APPROVED";
        String archReq = "YES";
        String archStatus = "ARCHIVED";

        FinDocHistPo finDocHistPo1 = new FinDocHistPo();
        FinDocHistPo finDocHistPo2 = new FinDocHistPo();
        List<FinDocHistPo> expectedResults = Arrays.asList(finDocHistPo1, finDocHistPo2);

        Query expectedQuery = new Query();
        expectedQuery.addCriteria(Criteria.where("ctryRecCde").is(ctryCde));
        expectedQuery.addCriteria(Criteria.where("grpMembrRecCde").is(orgnCde));
        expectedQuery.addCriteria(Criteria.where("docStatCde").is(FinDocConstants.APPROVAL));
        expectedQuery.addCriteria(Criteria.where("docServrStatCde").is(statusFs));
        expectedQuery.addCriteria(Criteria.where("reqArchInd").is(archReq));
        expectedQuery.addCriteria(Criteria.where("docArchStatCde").is(archStatus));

        when(mongoTemplate.find(expectedQuery, FinDocHistPo.class)).thenReturn(expectedResults);


        FinDocHistPo[] actualResults = finDocReleaseDao.retrieveFinDocSmryRecordByStatusFS(ctryCde, orgnCde, statusFs);
        Assertions.assertEquals(0, actualResults.length);

        finDocReleaseDao.retrieveFinDocSmryRecordByStatCde(ctryCde, orgnCde, statusFs);
        finDocReleaseDao.retrieveByStatCde(ctryCde, orgnCde, statusFs);

        Assertions.assertEquals(0, actualResults.length);
    }

    @Test
    void shouldUpdateBatchFinDocHist() {
        FinDocHistPo finDocHistPo1 = new FinDocHistPo();
        finDocHistPo1.setRsrcItemIdFinDoc(1L);

        FinDocHistPo finDocHistPo2 = new FinDocHistPo();
        finDocHistPo2.setRsrcItemIdFinDoc(2L);

        List<FinDocHistPo> finDocHistPos = Arrays.asList(finDocHistPo1, finDocHistPo2);

        BulkWriteResult mockResult = mock(BulkWriteResult.class);
        when(colFinDocHist.bulkWrite(anyList())).thenReturn(mockResult);

        finDocReleaseDao.updateBatchFinDocHist(finDocHistPos);

        verify(colFinDocHist, times(1)).bulkWrite(argThat(argument -> argument.size() == 2));
    }

    @Test
    void shouldUpdateBatchFinDocULReq() {
        FinDocULReqPo finDocULReqPo1 = new FinDocULReqPo();
        finDocULReqPo1.setCtryRecCde("HK");
        finDocULReqPo1.setGrpMembrRecCde("HBAP");
        finDocULReqPo1.setFileRqstName("file1");
        finDocULReqPo1.setDocUpldSeqNum(1L);
        finDocULReqPo1.setDocStatCde("A");
        finDocULReqPo1.setDocServrStatCde("C");

        FinDocULReqPo finDocULReqPo2 = new FinDocULReqPo();
        finDocULReqPo2.setCtryRecCde("HK");
        finDocULReqPo2.setGrpMembrRecCde("HBAP");
        finDocULReqPo2.setFileRqstName("file2");
        finDocULReqPo2.setDocUpldSeqNum(2L);
        finDocULReqPo2.setDocStatCde("R");
        finDocULReqPo2.setDocServrStatCde("P");

        List<FinDocULReqPo> finDocULReqPos = Arrays.asList(finDocULReqPo1, finDocULReqPo2);

        BulkWriteResult mockResult = mock(BulkWriteResult.class);
        when(colFinDocUpld.bulkWrite(anyList())).thenReturn(mockResult);

        finDocReleaseDao.updateBatchFinDocULReq(finDocULReqPos);
        verify(colFinDocUpld, times(1)).bulkWrite(argThat(argument -> argument.size() == 2));
    }

    @Test
    void shouldUpdateFinDocSmry2() throws RecordNotFoundException {

        FinDocHistPo finDocHistPo = new FinDocHistPo();
        finDocHistPo.setRsrcItemIdFinDoc(123l);

        Bson expectedFilter = Filters.eq("rsrcItemIdFinDoc", finDocHistPo.getRsrcItemIdFinDoc());

        FindIterable<Document> findIterable = mock(FindIterable.class);
        MongoCursor<Document> mongoCursor = mock(MongoCursor.class);
        when(colFinDocHist.find(expectedFilter)).thenReturn(findIterable);
        when(findIterable.iterator()).thenReturn(mongoCursor);
        Assertions.assertThrows(RecordNotFoundException.class, () -> {
            finDocReleaseDao.update(finDocHistPo);
        });

        when(mongoCursor.hasNext()).thenReturn(true);
        finDocReleaseDao.update(finDocHistPo);

        verify(colFinDocHist, times(2)).find(expectedFilter);
        verify(colFinDocHist, times(1)).updateOne(eq(expectedFilter), any(Document.class));
    }

    @Test
    void shouldRetrieveByDocSerNum() throws RecordNotFoundException {

        FinDocHistPo finDocHistPo = new FinDocHistPo();
        finDocHistPo.setCtryRecCde("US");
        finDocHistPo.setGrpMembrRecCde("123");
        finDocHistPo.setDocFinTypeCde("TYPE");
        finDocHistPo.setDocFinCatCde("CAT");
        finDocHistPo.setDocFinCde("CDE");
        finDocHistPo.setRsrcItemIdFinDoc(456l);

        Query expectedQuery = new Query();
        expectedQuery.addCriteria(Criteria.where("ctryRecCde").is(finDocHistPo.getCtryRecCde()));
        expectedQuery.addCriteria(Criteria.where("grpMembrRecCde").is(finDocHistPo.getGrpMembrRecCde()));
        expectedQuery.addCriteria(Criteria.where("docFinTypeCde").is(finDocHistPo.getDocFinTypeCde()));
        expectedQuery.addCriteria(Criteria.where("docFinCatCde").is(finDocHistPo.getDocFinCatCde()));
        expectedQuery.addCriteria(Criteria.where("docFinCde").is(finDocHistPo.getDocFinCde()));
        expectedQuery.addCriteria(Criteria.where("docSerNum").is(finDocHistPo.getRsrcItemIdFinDoc()));

        FinDocULReqPo expectedResult = new FinDocULReqPo();

        when(mongoTemplate.findOne(expectedQuery, FinDocULReqPo.class)).thenReturn(expectedResult);

        FinDocULReqPo actualResult = finDocReleaseDao.retrieveByDocSerNum(finDocHistPo);

        Assertions.assertEquals(expectedResult, actualResult);
        verify(mongoTemplate, times(1)).findOne(expectedQuery, FinDocULReqPo.class);
    }

    @Test
    void shouldThrowRecordNotFoundExceptionWhenRetrievingNonExistingByDocSerNum() {

        FinDocHistPo finDocHistPo = new FinDocHistPo();
        finDocHistPo.setCtryRecCde("US");
        finDocHistPo.setGrpMembrRecCde("123");
        finDocHistPo.setDocFinTypeCde("TYPE");
        finDocHistPo.setDocFinCatCde("CAT");
        finDocHistPo.setDocFinCde("CDE");
        finDocHistPo.setRsrcItemIdFinDoc(456l);

        Query expectedQuery = new Query();
        expectedQuery.addCriteria(Criteria.where("ctryRecCde").is(finDocHistPo.getCtryRecCde()));
        expectedQuery.addCriteria(Criteria.where("grpMembrRecCde").is(finDocHistPo.getGrpMembrRecCde()));
        expectedQuery.addCriteria(Criteria.where("docFinTypeCde").is(finDocHistPo.getDocFinTypeCde()));
        expectedQuery.addCriteria(Criteria.where("docFinCatCde").is(finDocHistPo.getDocFinCatCde()));
        expectedQuery.addCriteria(Criteria.where("docFinCde").is(finDocHistPo.getDocFinCde()));
        expectedQuery.addCriteria(Criteria.where("docSerNum").is(finDocHistPo.getRsrcItemIdFinDoc()));

        when(mongoTemplate.findOne(expectedQuery, FinDocULReqPo.class)).thenReturn(null);

        Assertions.assertThrows(RecordNotFoundException.class, () -> {
            finDocReleaseDao.retrieveByDocSerNum(finDocHistPo);
        });
        verify(mongoTemplate, times(1)).findOne(expectedQuery, FinDocULReqPo.class);
    }

    @Test
    void shouldUpdateUploadReqTO() throws RecordNotFoundException {

        FinDocULReqPo finDocULReqPo = new FinDocULReqPo();
        finDocULReqPo.setCtryRecCde("US");
        finDocULReqPo.setGrpMembrRecCde("123");
        finDocULReqPo.setFileRqstName("test");
        finDocULReqPo.setDocUpldSeqNum(1L);
        finDocULReqPo.setDocStatCde("A");
        finDocULReqPo.setDocServrStatCde("C");

        Bson expectedFilter = Filters.and(Filters.eq(Field.ctryRecCde, finDocULReqPo.getCtryRecCde()),
                Filters.eq(Field.grpMembrRecCde, finDocULReqPo.getGrpMembrRecCde()),
                Filters.eq(Field.fileRqstName, finDocULReqPo.getFileRqstName()),
                Filters.eq(Field.docUpldSeqNum, finDocULReqPo.getDocUpldSeqNum()));

        FindIterable<Document> findIterable = mock(FindIterable.class);
        MongoCursor<Document> mongoCursor = mock(MongoCursor.class);
        when(colFinDocUpld.find(expectedFilter)).thenReturn(findIterable);
        when(findIterable.iterator()).thenReturn(mongoCursor);
        Assertions.assertThrows(RecordNotFoundException.class, () -> {
            finDocReleaseDao.update(finDocULReqPo);
        });

        when(mongoCursor.hasNext()).thenReturn(true);
        finDocReleaseDao.update(finDocULReqPo);

        finDocULReqPo.setDocStatCde("R");
        finDocULReqPo.setDocServrStatCde("P");
        finDocReleaseDao.update(finDocULReqPo);

        finDocULReqPo.setDocStatCde("V");
        finDocULReqPo.setDocServrStatCde("P");
        finDocReleaseDao.update(finDocULReqPo);

        verify(colFinDocUpld, times(4)).find(expectedFilter);
        verify(colFinDocUpld, times(3)).updateOne(eq(expectedFilter), any(Document.class));
    }


    @Test
    void retrieveFinDocSysPramByProdType() {
        FinDocHistPo finDocHistPo = createFindDocHistPo();

        Query expectedQuery = new Query();
        expectedQuery.addCriteria(Criteria.where("ctryRecCde").is(finDocHistPo.getCtryRecCde()));
        expectedQuery.addCriteria(Criteria.where("grpMembrRecCde").is(finDocHistPo.getGrpMembrRecCde()));
        expectedQuery.addCriteria(Criteria.where("docFinTypeCde").is(finDocHistPo.getDocFinTypeCde()));
        expectedQuery.addCriteria(Criteria.where("docFinCatCde").is(finDocHistPo.getDocFinCatCde()));
        expectedQuery.addCriteria(Criteria.where("parmCde").is(FinDocConstants.FSURL));

        SystemParmPo expectedResult = new SystemParmPo();

        when(mongoTemplate.findOne(expectedQuery, SystemParmPo.class)).thenReturn(expectedResult);

        SystemParmPo actualResult = finDocReleaseDao.retrieveFinDocSysPramByProdType(finDocHistPo.getCtryRecCde(),finDocHistPo.getGrpMembrRecCde(),
                finDocHistPo.getDocFinTypeCde(), finDocHistPo.getDocFinCatCde(), FinDocConstants.FSURL);

        Assertions.assertEquals(expectedResult, actualResult);
        verify(mongoTemplate, times(1)).findOne(expectedQuery, SystemParmPo.class);
    }

    @Test
    void retrieveFinDocSmryRecordByDocCdeNLangCdeLatest() {
        FinDocHistPo finDocHistPo = createFindDocHistPo();


        Query expectedQuery = new Query();
        expectedQuery.addCriteria(Criteria.where(Field.ctryRecCde).is(finDocHistPo.getCtryRecCde()));
        expectedQuery.addCriteria(Criteria.where(Field.grpMembrRecCde).is(finDocHistPo.getGrpMembrRecCde()));
        expectedQuery.addCriteria(Criteria.where(Field.docFinTypeCde).is(finDocHistPo.getDocFinTypeCde()));
        expectedQuery.addCriteria(Criteria.where(Field.docFinCatCde).is(finDocHistPo.getDocFinCatCde()));
        expectedQuery.addCriteria(Criteria.where(Field.docFinCde).is(finDocHistPo.getDocFinCde()));
        expectedQuery.addCriteria(Criteria.where(Field.langFinDocCde).is(finDocHistPo.getLangFinDocCde()));
        expectedQuery.addCriteria(Criteria.where(Field.docStatCde).is(FinDocConstants.APPROVAL));
        expectedQuery.addCriteria(Criteria.where(Field.docServrStatCde).is(FinDocConstants.CONFIRM));

        FinDocPo expectedResult = new FinDocPo();

        when(mongoTemplate.findOne(expectedQuery, FinDocPo.class)).thenReturn(expectedResult);

        FinDocPo actualResult = finDocReleaseDao.retrieveFinDocSmryRecordByDocCdeNLangCdeLatest(finDocHistPo);

        Assertions.assertEquals(expectedResult, actualResult);
        verify(mongoTemplate, times(1)).findOne(expectedQuery, FinDocPo.class);
    }

    @Test
    void updateLatest() {

        FinDocHistPo fds = createFindDocHistPo();

        Query expectedFilter = new Query();
        expectedFilter.addCriteria(Criteria.where(Field.ctryRecCde).is(fds.getCtryRecCde()));
        expectedFilter.addCriteria(Criteria.where(Field.grpMembrRecCde).is(fds.getGrpMembrRecCde()));
        expectedFilter.addCriteria(Criteria.where(Field.docFinTypeCde).is(fds.getDocFinTypeCde()));
        expectedFilter.addCriteria(Criteria.where(Field.docFinCatCde).is(fds.getDocFinCatCde()));
        expectedFilter.addCriteria(Criteria.where(Field.docFinCde).is(fds.getDocFinCde()));
        expectedFilter.addCriteria(Criteria.where(Field.langFinDocCde).is(fds.getLangFinDocCde()));
        expectedFilter.addCriteria(Criteria.where(Field.docStatCde).is(fds.getDocStatCde()));
        expectedFilter.addCriteria(Criteria.where(Field.docServrStatCde).is(fds.getDocServrStatCde()));

        Mockito.when(mongoTemplate.findAndReplace(eq(expectedFilter),any(FinDocPo.class),eq("fin_doc"))).thenReturn(null);


        finDocReleaseDao.updateLatest(fds);

        verify(mongoTemplate, times(1)).findAndReplace(eq(expectedFilter), any(FinDocPo.class),eq("fin_doc"));
    }

    @Test
    void updateProdRLByDocSerNum() {
        {
            Long oldDocSerNum = 123L;
            Long newDocSerNum =456L;

            Bson expectedFilter1 = Filters.eq(Field.rsrcItemIdFinDoc, oldDocSerNum);
            Bson expectedFilter2 = Filters.eq(Field.finDoc + "." + Field.rsrcItemIdFinDoc, oldDocSerNum);

            finDocReleaseDao.updateProdRLByDocSerNum(oldDocSerNum, newDocSerNum);

            verify(colProdTypeFinDoc, times(1)).updateMany(eq(expectedFilter1), any(Document.class));
            verify(colProduct, times(1)).updateMany(eq(expectedFilter2), any(Document.class), any(UpdateOptions.class));
        }
    }

    @Test
    void insertLatest() throws DuplicateKeyException {

        FinDocHistPo fds = createFindDocHistPo();

        ArgumentMatcher<Query> argumentMatcher = argument -> argument != null && argument.getSkip() == 0;

        FinDocPo finDocPo = new FinDocPo();
        finDocPo.setRsrcItemIdFinDoc(12345L);
        Mockito.when(mongoTemplate.findOne(argThat(argumentMatcher), any())).thenReturn(finDocPo);
        //Mockito.when(colFinDoc.insertOne(any(Document.class))).thenReturn(mock(InsertOneResult.class));

        Assertions.assertThrows(DuplicateKeyException.class, () -> {
            finDocReleaseDao.insertLatest(fds);
        });

        Mockito.when(mongoTemplate.findOne(argThat(argumentMatcher), any())).thenReturn(null);
        Mockito.when(mongoTemplate.save(any(FinDocPo.class),eq("fin_doc"))).thenReturn(null);

        finDocReleaseDao.insertLatest(fds);

        verify(mongoTemplate, times(1)).save(any(FinDocPo.class),eq("fin_doc"));
    }

    private FinDocHistPo createFindDocHistPo() {
        FinDocHistPo fds = new FinDocHistPo();
        fds.setCtryRecCde("HK");
        fds.setGrpMembrRecCde("HBAP");
        fds.setDocFinTypeCde("USERDOC-5");
        fds.setDocFinCatCde("FC007");
        fds.setDocFinCde("ABC");
        fds.setLangFinDocCde("En");
        fds.setRsrcItemIdFinDoc(16888L);
        fds.setDocRecvName("Test-007");
        fds.setDocStatCde(FinDocConstants.APPROVAL);
        fds.setDocServrStatCde(FinDocConstants.CONFIRM);
        return fds;
    }
}