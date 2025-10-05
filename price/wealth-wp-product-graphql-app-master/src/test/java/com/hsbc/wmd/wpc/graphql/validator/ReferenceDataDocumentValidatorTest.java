package com.dummy.wmd.wpc.graphql.validator;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.service.MongoDBService;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ReferenceDataDocumentValidatorTest {

    @Mock
    private MongoDatabase mockMongoDatabase;

    @InjectMocks
    private ReferenceDataDocumentValidator referenceDataDocumentValidator;

    private Document docSmaple;

    @Before
    public void setUp() {
        docSmaple = new Document();
        docSmaple.put("ctryRecCde", "a");
        docSmaple.put("grpMembrRecCde", "b");
        docSmaple.put("cdvTypeCde", "c");
        docSmaple.put("cdvCde", "d");
        docSmaple.put("cdvDesc", "e");
        docSmaple.put("cdvDispSeqNum", "f");
        docSmaple.put("cdvParntTypeCde", "g");
        docSmaple.put("cdvParntCde", "h");
    }

    @Test
    public void testValidateImport_giveNull_returnsErrorList() {
        List<Error> errorList = referenceDataDocumentValidator.validateImport(null);
        assertFalse(errorList.isEmpty());
        assertEquals(1, errorList.size());
        assertNotNull(errorList.get(0));
        assertEquals("@null", errorList.get(0).getCode());
    }

    @Test
    public void testValidateImport_giveEmptyDoc_returnsErrorList() {
        Document doc = new Document();

        MockedStatic<MongoDBService> mongoService = Mockito.mockStatic(MongoDBService.class);
        List<Error> errorList;
        try {
            mockCountREFTYP(mongoService, doc, 0L);
            errorList = referenceDataDocumentValidator.validateImport(doc);
        } finally {
            mongoService.close();
        }
        assertFalse(errorList.isEmpty());
        assertTrue(errorList.stream().anyMatch(error -> "cdvDesc@required".equals(error.getCode())));
        assertTrue(errorList.stream().anyMatch(error -> "cdvDispSeqNum@required".equals(error.getCode())));
        assertTrue(errorList.stream().anyMatch(error -> "cdvTypeCde@invalid".equals(error.getCode())));
    }

    @Test
    public void testValidateImport_giveEmptyDoc_returnsErrorList2() {
        Document doc = new Document();

        MockedStatic<MongoDBService> mongoService = Mockito.mockStatic(MongoDBService.class);
        List<Error> errorList;
        try{
            mockCountREFTYP(mongoService, doc, 1L);
            errorList = referenceDataDocumentValidator.validateImport(doc);
        } finally {
            mongoService.close();
        }

        assertFalse(errorList.isEmpty());
        assertTrue(errorList.stream().anyMatch(error -> "cdvDesc@required".equals(error.getCode())));
        assertTrue(errorList.stream().anyMatch(error -> "cdvDispSeqNum@required".equals(error.getCode())));
        assertFalse(errorList.stream().anyMatch(error -> "cdvTypeCde@invalid".equals(error.getCode())));
    }

    @Test
    public void testValidateImport_giveValidDoc_returnsEmptyErrorList() {

        MockedStatic<MongoDBService> mongoService = Mockito.mockStatic(MongoDBService.class);
        List<Error> errorList;
        try{
            mockCountREFTYP(mongoService, docSmaple, 1L);
            mockCountParent(mongoService, docSmaple, 1L);
            errorList = referenceDataDocumentValidator.validateImport(docSmaple);
        } finally {
            mongoService.close();
        }

        assertTrue(errorList.isEmpty());
    }

    @Test
    public void testValidateImport_giveREFTYP_returnsEmptyErrorList() {
        docSmaple.put(Field.cdvTypeCde, "REFTYP");

        MockedStatic<MongoDBService> mongoService = Mockito.mockStatic(MongoDBService.class);
        List<Error> errorList;
        try{
            mockCountParent(mongoService, docSmaple, 1L);
            errorList = referenceDataDocumentValidator.validateImport(docSmaple);
        } finally {
            mongoService.close();
        }

        assertTrue(errorList.isEmpty());
    }

    @Test
    public void testValidateCreate_giveValidDoc_returnsEmptyErrorList() {
        MockedStatic<MongoDBService> mongoService = Mockito.mockStatic(MongoDBService.class);
        List<Error> errorList;
        try{
            mockCountReferenceData(mongoService, docSmaple, 0L);
            mockCountREFTYP(mongoService, docSmaple, 1L);
            mockCountParent(mongoService, docSmaple, 1L);
            errorList = referenceDataDocumentValidator.validateCreate(docSmaple);
        } finally {
            mongoService.close();
        }

        assertTrue(errorList.isEmpty());
    }

    @Test
    public void testValidateCreate_giveExistingDoc_returnsErrorList() {
        MockedStatic<MongoDBService> mongoService = Mockito.mockStatic(MongoDBService.class);
        List<Error> errorList;
        try{
            mockCountReferenceData(mongoService, docSmaple, 1L);
            errorList = referenceDataDocumentValidator.validateCreate(docSmaple);
        } finally {
            mongoService.close();
        }

        assertFalse(errorList.isEmpty());
        assertTrue(errorList.stream().anyMatch(error -> "@duplicate".equals(error.getCode())));
    }

    @Test
    public void testValidateUpdate_giveValidDoc_returnsEmptyErrorList() {
        MockedStatic<MongoDBService> mongoService = Mockito.mockStatic(MongoDBService.class);
        List<Error> errorList;
        try{
            mockCountReferenceData(mongoService, docSmaple, 1L);
            mockCountREFTYP(mongoService, docSmaple, 1L);
            mockCountParent(mongoService, docSmaple, 1L);
            errorList = referenceDataDocumentValidator.validateUpdate(docSmaple);
        } finally {
            mongoService.close();
        }

        assertTrue(errorList.isEmpty());
    }

    @Test
    public void testValidateUpdate_giveNotExistingDoc_returnsErrorList() {
        MockedStatic<MongoDBService> mongoService = Mockito.mockStatic(MongoDBService.class);
        List<Error> errorList;
        try{
            mockCountReferenceData(mongoService, docSmaple, 0L);
            mockCountREFTYP(mongoService, docSmaple, 1L);
            mockCountParent(mongoService, docSmaple, 1L);
            errorList = referenceDataDocumentValidator.validateUpdate(docSmaple);
        } finally {
            mongoService.close();
        }

        assertFalse(errorList.isEmpty());
        assertTrue(errorList.stream().anyMatch(error -> "@notFound".equals(error.getCode())));
    }

    @Test
    public void testValidateUpdate_giveMissingCdvParntTypeCde_returnsErrorList() {
        docSmaple.remove(Field.cdvParntTypeCde);

        MockedStatic<MongoDBService> mongoService = Mockito.mockStatic(MongoDBService.class);
        List<Error> errorList;
        try{
            mockCountReferenceData(mongoService, docSmaple, 1L);
            mockCountREFTYP(mongoService, docSmaple, 1L);
            errorList = referenceDataDocumentValidator.validateUpdate(docSmaple);
        } finally {
            mongoService.close();
        }

        assertFalse(errorList.isEmpty());
        assertTrue(errorList.stream().anyMatch(error -> "cdvParntTypeCde@required".equals(error.getCode())));
    }

    @Test
    public void testValidateUpdate_giveMissingCdvParntCde_returnsErrorList() {
        docSmaple.remove(Field.cdvParntCde);

        MockedStatic<MongoDBService> mongoService = Mockito.mockStatic(MongoDBService.class);
        List<Error> errorList;
        try{
            mockCountReferenceData(mongoService, docSmaple, 1L);
            mockCountREFTYP(mongoService, docSmaple, 1L);
            errorList = referenceDataDocumentValidator.validateUpdate(docSmaple);
        } finally {
            mongoService.close();
        }

        assertFalse(errorList.isEmpty());
        assertTrue(errorList.stream().anyMatch(error -> "cdvParntCde@required".equals(error.getCode())));
    }


    @Test
    public void testValidateCreate_giveExcessiveByteLength_returnsErrorList() {
        docSmaple.put(Field.cdvDesc, StringUtils.repeat("如果华佗再世，崇洋都被医治。", 3));
        List<Error> errorList;
        try (MockedStatic<MongoDBService> mongoService = Mockito.mockStatic(MongoDBService.class)) {
            mockCountReferenceData(mongoService, docSmaple, 1L);
            errorList = referenceDataDocumentValidator.validateCreate(docSmaple);
        }

        assertFalse(errorList.isEmpty());
        assertTrue(errorList.stream().anyMatch(error -> "cdvDesc@bytes".equals(error.getCode())));
    }

    private void mockCountREFTYP(MockedStatic<MongoDBService> mongoService, Document doc, long count) {
        Bson filter = and(
                eq(Field.ctryRecCde, doc.get(Field.ctryRecCde)),
                eq(Field.grpMembrRecCde, doc.get(Field.grpMembrRecCde)),
                eq(Field.cdvTypeCde, "REFTYP"),
                eq(Field.cdvCde, doc.getString(Field.cdvTypeCde))
        );
        mongoService.when(() -> MongoDBService.countDocuments(CollectionName.reference_data, filter)).thenReturn(count);
    }

    private void mockCountParent(MockedStatic<MongoDBService> mongoService, Document doc, long count) {
        Bson filter = and(
                eq(Field.ctryRecCde, doc.get(Field.ctryRecCde)),
                eq(Field.grpMembrRecCde, doc.get(Field.grpMembrRecCde)),
                eq(Field.cdvTypeCde, doc.get(Field.cdvParntTypeCde)),
                eq(Field.cdvCde, doc.getString(Field.cdvParntCde))
        );
        mongoService.when(() -> MongoDBService.countDocuments(CollectionName.reference_data, filter)).thenReturn(count);
    }

    private void mockCountReferenceData(MockedStatic<MongoDBService> mongoService, Document doc, long count) {
        Bson filter = and(
                eq(Field.ctryRecCde, doc.get(Field.ctryRecCde)),
                eq(Field.grpMembrRecCde, doc.get(Field.grpMembrRecCde)),
                eq(Field.cdvTypeCde, doc.get(Field.cdvTypeCde)),
                eq(Field.cdvCde, doc.get(Field.cdvCde)));
        mongoService.when(() -> MongoDBService.countDocuments(CollectionName.reference_data, filter)).thenReturn(count);
    }

}
