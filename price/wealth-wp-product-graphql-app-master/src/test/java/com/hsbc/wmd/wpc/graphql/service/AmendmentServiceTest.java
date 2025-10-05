package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.ApprovalAction;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.constant.Sequence;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class AmendmentServiceTest {
    @Mock
    private MongoCollection<Document> colAmendment;
    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private ProductChangeService productChangeService;
    @Mock
    private ReferenceDataChangeService referenceDataChangeService;

    @Mock
    private StaffEligibilityChangeService staffEligibilityChangeService;

    @Mock
    private SequenceService sequenceService;

    @Mock
    private FinDocHistChangeService finDocHistChangeService;
    @Mock
    private ChanlRelatedFieldsChangeService chanlRelatedFieldsChangeService;

    @Mock
    private UpdateResult updateResult;
    @Mock
    private DeleteResult deleteResult;
    @Mock
    private FindIterable<Document> iterable;

    @InjectMocks
    private AmendmentService amendmentService;

    @Before
    public void setUp() throws Exception {
        amendmentService = new AmendmentService();
        ReflectionTestUtils.setField(amendmentService, "mongoTemplate", mongoTemplate);
        Mockito.when(mongoTemplate.getCollection(CollectionName.amendment.name())).thenReturn(colAmendment);
        ReflectionTestUtils.setField(amendmentService, "sequenceService", sequenceService);
        ReflectionTestUtils.setField(amendmentService, "productChangeService", productChangeService);
        ReflectionTestUtils.setField(amendmentService, "staffEligibilityChangeService", staffEligibilityChangeService);
        ReflectionTestUtils.setField(amendmentService, "referenceDataChangeService", referenceDataChangeService);
        ReflectionTestUtils.setField(amendmentService, "finDocHistChangeService", finDocHistChangeService);
        ReflectionTestUtils.setField(amendmentService, "chanlRelatedFieldsChangeService",
                chanlRelatedFieldsChangeService);
    }

    @Test
    public void testGetLatestDocument_givenDocTypeAndDocId_returnsDoc() {
        Object docId = 1;
        DocType docType = DocType.product;
        Document doc = CommonUtils.readResourceAsDocument("/files/UT-40004996.json");
        Mockito.when(productChangeService.findFirst(eq(Field._id, docId))).thenReturn(doc);
        Document document = amendmentService.getLatestDocument(docType, docId);
        assertNotNull(document);
    }

    @Test
    public void testGetLatestDocument_givenDocTypeDocIdAndDocChange_returnsDoc() {
        Object docId = 1;
        DocType docType = DocType.product_staff_eligibility;
        Document doc = CommonUtils.readResourceAsDocument("/files/UT-40004996.json");
        Mockito.when(staffEligibilityChangeService.findFirst(eq(Field._id, docId), doc)).thenReturn(doc);
        Document document = amendmentService.getLatestDocument(docType, docId, doc);
        assertNotNull(document);
    }

    @Test
    public void testGetCollectionName_givenDocType_returnString()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = amendmentService.getClass().getDeclaredMethod("getCollectionName", DocType.class);
        method.setAccessible(true);
        DocType docType = DocType.product;
        String collectionName = (String) method.invoke(amendmentService, docType);
        assertEquals(collectionName, docType.toString());
    }

    @Test
    public void testHasConflict_givenDocTypeDocIdAndDocRevision_returnBoolean() {
        String docType = DocType.product.toString();
        long docId = 1L;
        long docRevision = 1L;
        Boolean hasConflict = amendmentService.hasConflict(docType, docId, docRevision);
        assertEquals(false, hasConflict);
    }

    @Test
    public void testUpdateAmendment_givenDocument_returnDocument() {
        Document doc = CommonUtils.readResourceAsDocument("/files/UT-40004996.json");
        Mockito.when(mongoTemplate.getCollection(CollectionName.amendment.name())
                .replaceOne(eq(Field._id, doc.get(Field._id)), doc)).thenReturn(updateResult);
        Document document = amendmentService.updateAmendment(doc);
        assertNotNull(document);
    }

    @Test
    public void testCreateAmendment_givenDocument_returnsDocument() {
        Document doc = CommonUtils.readResourceAsDocument("/files/UT-40004996.json");
        Mockito.when(sequenceService.nextId(Sequence.amendmentId)).thenReturn(1L);
        Document document = amendmentService.createAmendment(doc);
        assertNotNull(document);
    }

    @Test
    public void testAmendmentById_givenId_ReturnsDocument() {
        Long amendmentId = 1L;
        Document document = new Document("key", "value");
        mockCollFindFirst(document);
        assertNotNull(amendmentService.amendmentById(amendmentId));
    }

    @Test
    public void testApproveAmendment_givenArgs_returnNull() {
        Long amendmentId = 1L;
        mockCollFindFirst(null);
        // Run the test
        Document result = amendmentService.approveAmendment("approvedBy", amendmentId, ApprovalAction.approved,
                "comment");
        // Verify the results
        assertNull(result);
    }

    @Test(expected = productErrorException.class)
    public void testApproveAmendment_givenArgs_throwException1() {
        Long amendmentId = 1L;
        Document document = new Document("key", "value");
        document.put(Field.emplyNum, "approvedBy");
        mockCollFindFirst(document);
        // Run the test
        amendmentService.approveAmendment("approvedBy", amendmentId, ApprovalAction.approved,
                "comment");
    }

    @Test(expected = productErrorException.class)
    public void testApproveAmendment_givenArgs_throwException2() {
        Long amendmentId = 1L;
        Document document = new Document("key", "value");
        document.put(Field.emplyNum, "test");
        document.put(Field.recStatCde, "approved");
        mockCollFindFirst(document);
        // Run the test
        amendmentService.approveAmendment("approvedBy", amendmentId, ApprovalAction.approved,
                "comment");
    }

    @Test(expected = productErrorException.class)
    public void testApproveAmendment_givenArgs_validateException() {
        Long amendmentId = 1L;
        Document document = new Document("key", "value");
        document.put(Field.emplyNum, "test");
        document.put(Field.recStatCde, "pending");
        document.put(Field.docType, "product");
        List<Error> errors = new ArrayList<>();
        Error error = new Error("test", "test", "error");
        errors.add(error);
        Mockito.when(productChangeService.validate(document)).thenReturn(errors);
        mockCollFindFirst(document);
        // Run the test
        amendmentService.approveAmendment("approvedBy", amendmentId, ApprovalAction.approved,
                "comment");
    }

    @Test
    public void testApproveAmendment_givenActionCdeAdd_returnDoc() {
        Long amendmentId = 1L;
        Document document = new Document("key", "value");
        Document doc = CommonUtils.readResourceAsDocument("/files/UT-40028782.json");
        document.put(Field.doc, doc);
        document.put(Field.emplyNum, "test");
        document.put(Field.recStatCde, "pending");
        document.put(Field.docType, "product");
        document.put(Field.actionCde, "add");
        mockCollFindFirst(document);
        Mockito.doNothing().when(productChangeService).add(any(Document.class));
        Mockito.when(mongoTemplate.getCollection(CollectionName.amendment.name()).findOneAndReplace(any(Bson.class),
                any(Document.class), any(FindOneAndReplaceOptions.class))).thenReturn(document);
        // Run the test
        Document result = amendmentService.approveAmendment("approvedBy", amendmentId, ApprovalAction.approved,
                "comment");
        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testApproveAmendment_givenActionCdeUpdate_returnDoc() {
        Long amendmentId = 1L;
        Document document = new Document("key", "value");
        Document doc = CommonUtils.readResourceAsDocument("/files/UT-40028782.json");
        document.put(Field.doc, doc);
        document.put(Field.docBase, doc);
        document.put(Field.emplyNum, "test");
        document.put(Field.recStatCde, "pending");
        document.put(Field.docType, "product");
        document.put(Field.actionCde, "update");
        mockCollFindFirst(document);
        Mockito.when(mongoTemplate.getCollection(CollectionName.amendment.name()).findOneAndReplace(any(Bson.class),
                any(Document.class), any(FindOneAndReplaceOptions.class))).thenReturn(document);
        Mockito.when(productChangeService.findFirst(any(Bson.class), any(Document.class))).thenReturn(doc);
        Mockito.doNothing().when(productChangeService).update(any(Document.class));
        // Run the test
        Document result = amendmentService.approveAmendment("approvedBy", amendmentId, ApprovalAction.approved,
                "comment");
        // Verify the results
        assertNotNull(result);
    }

    @Test(expected = productErrorException.class)
    public void testApproveAmendment_givenActionCdeUpdate_DocumentNotFoundException() {
        Long amendmentId = 1L;
        Document document = new Document("key", "value");
        Document doc = CommonUtils.readResourceAsDocument("/files/UT-40028782.json");
        document.put(Field.doc, doc);
        document.put(Field.docBase, doc);
        document.put(Field.emplyNum, "test");
        document.put(Field.recStatCde, "pending");
        document.put(Field.docType, "product");
        document.put(Field.actionCde, "update");
        mockCollFindFirst(document);
        // Run the test
        Document result = amendmentService.approveAmendment("approvedBy", amendmentId, ApprovalAction.approved,
                "comment");
    }

    @Test
    public void testApproveAmendment_givenActionCdeReject_returnDoc() {
        Long amendmentId = 1L;
        Document document = new Document("key", "value");
        Document doc = CommonUtils.readResourceAsDocument("/files/UT-40028782.json");
        document.put(Field.doc, doc);
        document.put(Field.docBase, doc);
        document.put(Field.emplyNum, "test");
        document.put(Field.recStatCde, "pending");
        document.put(Field.docType, "fin_doc_hist");
        document.put(Field.actionCde, "add");
        mockCollFindFirst(document);
        Mockito.when(mongoTemplate.getCollection(CollectionName.amendment.name()).findOneAndReplace(any(Bson.class),
                any(Document.class), any(FindOneAndReplaceOptions.class))).thenReturn(document);
        Mockito.doNothing().when(finDocHistChangeService).rejectOpation(any());
        // Run the test
        Document result = amendmentService.approveAmendment("approvedBy", amendmentId, ApprovalAction.rejected,
                "comment");
        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testApproveAmendment_givenActionCdeDelete_returnDoc() {
        Long amendmentId = 1L;
        Document document = new Document();
        Document doc = CommonUtils.readResourceAsDocument("/files/UT-40028782.json");
        document.put(Field.doc, doc);
        document.put(Field.docBase, doc);
        document.put(Field.emplyNum, "test");
        document.put(Field.recStatCde, "pending");
        document.put(Field.docType, "product");
        document.put(Field.actionCde, "delete");
        mockCollFindFirst(document);
        Mockito.when(mongoTemplate.getCollection(CollectionName.amendment.name()).findOneAndReplace(any(Bson.class),
                any(Document.class), any(FindOneAndReplaceOptions.class))).thenReturn(document);
        Mockito.doNothing().when(productChangeService).delete(any(Document.class));
        // Run the test
        Document result = amendmentService.approveAmendment("approvedBy", amendmentId, ApprovalAction.approved,
                "comment");
        // Verify the results
        assertNotNull(result);
    }

    @Test(expected = productErrorException.class)
    public void testValidateCreateDocument_GivenDocTypeProductAndDocument_throwsException1() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/UT-40028782.json");
        doc.remove(Field.ctryRecCde);
        // Run the test
        amendmentService.validateCreateDocument(DocType.product, doc);
    }

    @Test(expected = productErrorException.class)
    public void testValidateCreateDocument_GivenDocTypeProductAndDocument_throwsException2() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/UT-40028782.json");
        Mockito.when(mongoTemplate.getCollection(CollectionName.amendment.name()).find(any(Bson.class)))
                .thenReturn(iterable);
        Mockito.when(iterable.first()).thenReturn(doc);
        // Run the test
        amendmentService.validateCreateDocument(DocType.product, doc);

    }

    @Test(expected = productErrorException.class)
    public void testValidateCreateDocument_GivenDocTypeProductAndDocument_throwsException3() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/UT-40028782.json");
        mockCollFindFirst(null);
        Mockito.when(productChangeService.findFirst(any(Bson.class))).thenReturn(doc);
        // Run the test
        amendmentService.validateCreateDocument(DocType.product, doc);

    }

    @Test
    public void testValidateCreateDocument_GivenDocTypeProductAndDocument_DoesNotThrow() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/UT-40028782.json");
        mockCollFindFirst(null);
        Mockito.when(productChangeService.findFirst(any(Bson.class))).thenReturn(null);
        // Run the test
        try {
            amendmentService.validateCreateDocument(DocType.product, doc);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test(expected = productErrorException.class)
    public void testValidateCreateDocument_GivenDocTypeReferenceDataAndDocument_throwsException() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/reference-data-APRVACTCOD-A.json");
        doc.remove(Field.ctryRecCde);
        doc.remove(Field.grpMembrRecCde);
        // Run the test
        amendmentService.validateCreateDocument(DocType.reference_data, doc);
    }

    @Test(expected = productErrorException.class)
    public void testValidateCreateDocument_GivenDocTypeReferenceDataAndDocument_throwsException2() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/reference-data-APRVACTCOD-A.json");
        mockCollFindFirst(doc);
        // Run the test
        amendmentService.validateCreateDocument(DocType.reference_data, doc);
    }

    @Test(expected = productErrorException.class)
    public void testValidateCreateDocument_GivenDocTypeReferenceDataAndDocument_throwsException3() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/reference-data-APRVACTCOD-A.json");
        mockCollFindFirst(null);
        Mockito.when(referenceDataChangeService.findFirst(any(Bson.class))).thenReturn(doc);
        // Run the test
        amendmentService.validateCreateDocument(DocType.reference_data, doc);
    }

    @Test
    public void testValidateCreateDocument_GivenDocTypeReferenceDataAndDocument() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/reference-data-APRVACTCOD-A.json");
        mockCollFindFirst(null);
        Mockito.when(referenceDataChangeService.findFirst(any(Bson.class))).thenReturn(null);
        // Run the test
        try {
            amendmentService.validateCreateDocument(DocType.reference_data, doc);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test(expected = productErrorException.class)
    public void testRequestApproval_throwsException1() {
        // Setup
        mockCollFindFirst(null);
        // Run the test
        amendmentService.requestApproval(0L, "comment");
    }

    @Test(expected = productErrorException.class)
    public void testRequestApproval_throwsException2() {
        // Setup
        Document document = new Document(Field.recStatCde, "returned");
        mockCollFindFirst(document);
        // Run the test
        amendmentService.requestApproval(0L, "comment");
    }

    @Test
    public void testRequestApproval() {
        // Setup
        Document document = new Document(Field.recStatCde, "draft");
        Document doc = CommonUtils.readResourceAsDocument("/files/UT-40028782.json");
        document.put(Field.doc, doc);
        document.put(Field.docType, "product");
        mockCollFindFirst(document);
        Mockito.when(mongoTemplate.getCollection(CollectionName.amendment.name()).findOneAndReplace(any(Bson.class),
                any(Document.class), any(FindOneAndReplaceOptions.class))).thenReturn(document);
        // Run the test
        Document result = amendmentService.requestApproval(0L, "comment");
        // Verify the results
        assertEquals(document, result);
    }

    @Test
    public void testGetAmendmentById() {
        // Setup
        Document document = new Document("key", "value");
        mockCollFindFirst(document);
        // Run the test
        Document result = amendmentService.getAmendmentById(0L);
        // Verify the results
        assertEquals(document, result);
    }

    @Test
    public void testHasOngoingAmendment_givenDocTypeProduct_returnsBoolean() {
        // Setup
        Long docId = 1L;
        Document docChanged = new Document("key", "value");
        Mockito.when(mongoTemplate.getCollection(CollectionName.amendment.name()).countDocuments(any(Bson.class)))
                .thenReturn(1L);
        // Run the test
        boolean result = amendmentService.hasOngoingAmendment(docChanged, DocType.product, docId);
        // Verify the results
        assertTrue(result);
    }

    @Test
    public void testHasOngoingAmendment_givenDocTypeStaffEligibility_returnsBoolean() {
        // Setup
        Long docId = 1L;
        Document docChanged = new Document("key", "value");
        Document employPosnCde = new Document("employPosnCde", "test");
        docChanged.put("stafLicCheck", employPosnCde);
        Mockito.when(mongoTemplate.getCollection(CollectionName.amendment.name()).countDocuments(any(Bson.class)))
                .thenReturn(1L);
        // Run the test
        boolean result = amendmentService.hasOngoingAmendment(docChanged, DocType.product_staff_eligibility, docId);
        // Verify the results
        assertTrue(result);
    }

    @Test
    public void testHasOngoingAmendment_givenDocTypeStaffEligibility_returnsBoolean2() {
        // Setup
        Long docId = 1L;
        Document docChanged = new Document("key", "value");
        Mockito.when(mongoTemplate.getCollection(CollectionName.amendment.name()).countDocuments(any(Bson.class)))
                .thenReturn(1L);
        // Run the test
        boolean result = amendmentService.hasOngoingAmendment(docChanged, DocType.product_staff_eligibility, docId);
        // Verify the results
        assertTrue(result);
    }

    @Test
    public void testDeleteAmendmentById_givenAmendmentId_returnsLong() {
        // Setup
        Long amendmentId = 1L;
        Mockito.when(mongoTemplate.getCollection(CollectionName.amendment.name()).deleteOne(any(Bson.class)))
                .thenReturn(deleteResult);
        Mockito.when(deleteResult.getDeletedCount()).thenReturn(1L);
        // Run the test
        long result = amendmentService.deleteAmendmentById(amendmentId);
        // Verify the results
        assertEquals(1L, result);
    }

    @Test
    public void getChangeService_givenDocType_returnChangeService() throws Exception {
        Method method = amendmentService.getClass().getDeclaredMethod("getChangeService", DocType.class);
        method.setAccessible(true);
        DocType aset_voltl_class_char = DocType.aset_voltl_class_char;
        ChangeService changeService = (ChangeService) method.invoke(amendmentService, aset_voltl_class_char);
        DocType aset_voltl_class_corl = DocType.aset_voltl_class_corl;
        changeService = (ChangeService) method.invoke(amendmentService, aset_voltl_class_corl);
        DocType product_customer_eligibility = DocType.product_customer_eligibility;
        changeService = (ChangeService) method.invoke(amendmentService, product_customer_eligibility);
        DocType staff_license_check = DocType.staff_license_check;
        changeService = (ChangeService) method.invoke(amendmentService, staff_license_check);
        DocType product_prod_relation = DocType.product_prod_relation;
        changeService = (ChangeService) method.invoke(amendmentService, product_prod_relation);
        DocType fin_doc_hist = DocType.fin_doc_hist;
        changeService = (ChangeService) method.invoke(amendmentService, fin_doc_hist);
        DocType product = DocType.product;
        changeService = (ChangeService) method.invoke(amendmentService, product);
        assertNotNull(changeService);
        assertNotNull(method.invoke(amendmentService, DocType.chanl_related_fileds));
    }

    @Test(expected = InvocationTargetException.class)
    public void getChangeService_givenDocType_throwsException() throws Exception {
        Method method = amendmentService.getClass().getDeclaredMethod("getChangeService", DocType.class);
        method.setAccessible(true);
        DocType amendment = DocType.amendment;
        method.invoke(amendmentService, amendment);
    }

    private void mockCollFindFirst(Document amdmDoc) {
        Mockito.when(mongoTemplate.getCollection(CollectionName.amendment.name()).find(any(Bson.class)))
                .thenReturn(iterable);
        Mockito.when(iterable.first()).thenReturn(amdmDoc);
    }

}
