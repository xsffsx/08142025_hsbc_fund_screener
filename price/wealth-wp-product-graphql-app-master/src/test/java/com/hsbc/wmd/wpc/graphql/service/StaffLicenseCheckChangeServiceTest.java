package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class StaffLicenseCheckChangeServiceTest {
    @Mock
    private MongoDatabase mockMongoDatabase;
    @Mock
    private MongoCollection<Document> colStaffLicenseCheckData;
    @Mock
    private SequenceService sequenceService;
    @Mock
    private ReferenceDataService referenceDataService;
    @Mock
    private FindIterable<Document> findIterable;
    @InjectMocks
    private StaffLicenseCheckChangeService staffLicenseCheckChangeService;

    @Before
    public void setUp() {
        staffLicenseCheckChangeService = new StaffLicenseCheckChangeService(mockMongoDatabase);
        ReflectionTestUtils.setField(staffLicenseCheckChangeService, "sequenceService", sequenceService);
        ReflectionTestUtils.setField(staffLicenseCheckChangeService, "referenceDataService", referenceDataService);
        ReflectionTestUtils.setField(staffLicenseCheckChangeService, "colStaffLicenseCheckData", colStaffLicenseCheckData);
        Mockito.when(referenceDataService.getReferDataByFilter(any(Bson.class))).thenReturn(Collections.singletonList(new Document()));
    }

    @Test
    public void testValidate_givenDocumentWithOutProdTypeCdeAndSubCde_returnsErrorList() {
        // Setup
        Document document = new Document(Field.actionCde, "add");
        Document doc = new Document();
        document.put("doc", doc);
        // Run the test
        List<Error> result = staffLicenseCheckChangeService.validate(document);
        // Verify the results
        assertEquals(1L, result.size());
    }

    @Test
    public void testValidate_givenDocumentWithOutProdSubtpCde_returnsErrorList() {
        // Setup
        Document document = new Document(Field.actionCde, "add");
        Document doc = new Document();
        doc.put(Field.prodTypeCde, "prodTypeCde");
        doc.put(Field.employPosnCde, "employPosnCde");
        document.put("doc", doc);
        // Run the test
        List<Error> result = staffLicenseCheckChangeService.validate(document);
        // Verify the results
        assertEquals(0L, result.size());
    }

    @Test
    public void testValidate_givenDocument_returnsErrorList() {
        // Setup
        Document document = new Document(Field.actionCde, "add");
        Document doc = new Document();
        doc.put(Field.prodTypeCde, "prodTypeCde");
        doc.put(Field.employPosnCde, "employPosnCde");
        document.put("doc", doc);
        // Run the test
        List<Error> result = staffLicenseCheckChangeService.validate(document);
        // Verify the results
        assertEquals(0L, result.size());
    }

    @Test
    public void testValidate_givenDocumentMockCountWithOutProdSubtpCde_returnsErrorList() {
        // Setup
        Document document = new Document(Field.actionCde, "add");
        Document doc = new Document();
        doc.put(Field.prodTypeCde, "prodTypeCde");
        doc.put(Field.employPosnCde, "employPosnCde");
        document.put("doc", doc);
        Mockito.when(colStaffLicenseCheckData.countDocuments(any(Bson.class))).thenReturn(1L);
        // Run the test
        List<Error> result = staffLicenseCheckChangeService.validate(document);
        // Verify the results
        assertEquals(1L, result.size());
    }

    @Test
    public void testValidate_givenDocumentMockCount_returnsErrorList() {
        // Setup
        Document document = new Document(Field.actionCde, "add");
        Document doc = new Document();
        doc.put(Field.prodSubtpCde, "prodSubtpCde");
        doc.put(Field.employPosnCde, "employPosnCde");
        document.put("doc", doc);
        Mockito.when(colStaffLicenseCheckData.countDocuments(any(Bson.class))).thenReturn(1L);
        // Run the test
        List<Error> result = staffLicenseCheckChangeService.validate(document);
        // Verify the results
        assertEquals(1L, result.size());
    }

    @Test
    public void testAdd_givenDocument_returnsNull() {
        // Setup
        Document doc = new Document("key", "value");
        // Run the test
        try {
            staffLicenseCheckChangeService.add(doc);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test(expected = productErrorException.class)
    public void testUpdate_givenDocument_throwsException() {
        // Setup
        Document doc = new Document("key", "value");
        Mockito.when(colStaffLicenseCheckData.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.first()).thenReturn(null);
        // Run the test
        staffLicenseCheckChangeService.update(doc);
    }

    @Test
    public void testUpdate_givenDocument_returnsNull() {
        // Setup
        Document doc = new Document("key", "value");
        Mockito.when(colStaffLicenseCheckData.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.first()).thenReturn(doc);
        // Run the test
        try{
            staffLicenseCheckChangeService.update(doc);
        }catch (Exception e){
            Assert.fail();
        }

    }

    @Test
    public void testDelete_givenDocument_returnsNull() {
        // Setup
        Document doc = new Document("key", "value");
        // Run the test
        try {
            staffLicenseCheckChangeService.delete(doc);
        } catch (Exception e) {
            Assert.fail();
        }

    }

    @Test
    public void testFindFirst_givenFilter_returnsDocument() {
        // Setup
        Bson filter = Filters.eq("key", "value");
        Document document = new Document("key", "value");
        Mockito.when(colStaffLicenseCheckData.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.first()).thenReturn(document);
        // Run the test
        Document result = staffLicenseCheckChangeService.findFirst(filter);
        // Verify the results
        assertEquals(document, result);
    }

    @Test
    public void testValidate_givenInvalidFrmlaEmpEligBfrCtoffText() {
        // Setup
        Document document = new Document(Field.actionCde, "add");
        Document doc = new Document();
        doc.put(Field.prodTypeCde, "prodTypeCde");
        doc.put(Field.employPosnCde, "employPosnCde");
        doc.put("frmlaEmpEligBfrCtoffText", "*");
        document.put("doc", doc);
        // Run the test
        List<Error> result = staffLicenseCheckChangeService.validate(document);
        // Verify the results
        assertNotNull(result);
        assertEquals(result.get(0), new Error("$frmlaEmpEligBfrCtoffText", "frmlaEmpEligBfrCtoffText@invalid",
                    "Formula Employee Eligibility Before Cutoff Text is invalid."));
    }

    @Test
    public void testValidate_givenInvalidFrmlaEmplyEligText() {
        // Setup
        Document document = new Document(Field.actionCde, "add");
        Document doc = new Document();
        doc.put(Field.prodSubtpCde, "prodSubtpCde");
        doc.put(Field.employPosnCde, "employPosnCde");
        doc.put("frmlaEmplyEligText", "*");
        document.put("doc", doc);
        // Run the test
        List<Error> result = staffLicenseCheckChangeService.validate(document);
        // Verify the results
        assertNotNull(result);
        assertEquals(result.get(0), new Error("$frmlaEmplyEligText", "frmlaEmplyEligText@invalid",
                    "Formula Employee Eligibility Text is invalid."));
    }

    @Test
    public void testValidate_givenInvalidProductTypeCde() {
        Mockito.when(referenceDataService.getReferDataByFilter(any(Bson.class))).thenReturn(Collections.emptyList());
        // Setup
        Document document = new Document(Field.actionCde, "add");
        Document doc = new Document();
        doc.put(Field.prodTypeCde, "prodTypeCde");
        document.put("doc", doc);
        // Run the test
        List<Error> result = staffLicenseCheckChangeService.validate(document);
        // Verify the results
        assertNotNull(result);
        assertEquals(result.get(0), new Error("$prodTypeCde", "prodTypeCde@invalid", "Product Type Code is invalid."));
    }

    @Test
    public void testValidate_givenInvalidProdSubtpCde() {
        Mockito.when(referenceDataService.getReferDataByFilter(any(Bson.class))).thenReturn(Collections.emptyList());
        // Setup
        Document document = new Document(Field.actionCde, "add");
        Document doc = new Document();
        doc.put(Field.prodSubtpCde, "prodSubtpCde");
        doc.put(Field.employPosnCde, "employPosnCde");
        document.put("doc", doc);
        // Run the test
        List<Error> result = staffLicenseCheckChangeService.validate(document);
        // Verify the results
        assertNotNull(result);
        assertEquals(result.get(0), new Error("$prodSubtpCde", "prodSubtpCde@invalid", "Product Subtype Code is invalid."));
    }

    @Test
    public void testValidate_givenDuplicateProdTypeCde() {
        // Setup
        Document document = new Document(Field.actionCde, "add");
        Document doc = new Document();
        doc.put(Field.prodTypeCde, "prodTypeCde");
        doc.put(Field.prodSubtpCde, "prodSubtpCde");
        document.put("doc", doc);
        // Run the test
        List<Error> result = staffLicenseCheckChangeService.validate(document);
        // Verify the results
        assertNotNull(result);
        assertEquals(result.get(0), new Error("$", "@prodTypeCde", "Please input either prodTypeCde or prodSubtpCde"));
    }

    @Test
    public void testValidate_givenInvalidEmployPosnCde() {
        // Setup
        Document document = new Document(Field.actionCde, "add");
        Document doc = new Document();
        doc.put(Field.ctryRecCde, "HK");
        doc.put(Field.grpMembrRecCde, "HBAP");
        doc.put(Field.prodSubtpCde, "prodSubtpCde");
        document.put("doc", doc);
        // Run the test
        List<Error> result = staffLicenseCheckChangeService.validate(document);
        // Verify the results
        assertNotNull(result);
        assertEquals(result.get(0), new Error("$employPosnCde", "employPosnCde@invalid", "Employment Position Code is invalid."));
    }

}
