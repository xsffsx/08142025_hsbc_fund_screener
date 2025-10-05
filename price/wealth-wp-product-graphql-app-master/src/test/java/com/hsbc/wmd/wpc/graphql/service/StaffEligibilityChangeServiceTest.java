package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
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

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class StaffEligibilityChangeServiceTest {

    @Mock
    private MongoDatabase mockMongoDatabase;
    @Mock
    private MongoCollection<Document> colProductData;
    @Mock
    private FindIterable<Document> findIterable;
    @Mock
    private FindIterable<Document> projectionIterable;
    @InjectMocks
    private StaffEligibilityChangeService staffEligibilityChangeService;

    @Before
    public void setUp() {
        staffEligibilityChangeService = new StaffEligibilityChangeService(mockMongoDatabase);
        ReflectionTestUtils.setField(staffEligibilityChangeService, "colProductData", colProductData);
    }

    @Test
    public void testValidate_givenDocumentWithoutEmployPosnCde_returnsErrorList() {
        // Setup
        Document document = new Document(Field.actionCde, "add");
        Document docChange = CommonUtils.readResourceAsDocument("/files/staff-license-check-doc-change.json");
        Document stafLicCheck = (Document) docChange.get("stafLicCheck");
        stafLicCheck.remove("employPosnCde");
        docChange.put("stafLicCheck", stafLicCheck);
        document.put(Field.doc, docChange);
        // Run the test
        List<Error> result = staffEligibilityChangeService.validate(document);
        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testValidate_givenDocumentAndActionCdeAdd_returnsErrorList() {
        // Setup
        Document document = new Document(Field.actionCde, "add");
        Document docChange = CommonUtils.readResourceAsDocument("/files/staff-license-check-doc-change.json");
        document.put(Field.doc, docChange);
        Document docBase = CommonUtils.readResourceAsDocument("/files/staff-license-check.json");
        Mockito.when(colProductData.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.projection(any(Bson.class))).thenReturn(projectionIterable);
        Mockito.when(projectionIterable.first()).thenReturn(docBase);
        // Run the test
        List<Error> result = staffEligibilityChangeService.validate(document);
        // Verify the results
        assertEquals(0L, result.size());
    }

    @Test
    public void testValidate_givenDocumentAndActionCdeAddWithDuplicateCde_returnsErrorList() {
        // Setup
        Document document = new Document(Field.actionCde, "add");
        Document docChange = CommonUtils.readResourceAsDocument("/files/staff-license-check-doc-change.json");
        Document stafLicCheck = (Document) docChange.get("stafLicCheck");
        stafLicCheck.put("employPosnCde", "R3");
        docChange.put("stafLicCheck", stafLicCheck);
        document.put(Field.doc, docChange);
        Document docBase = CommonUtils.readResourceAsDocument("/files/staff-license-check.json");
        Mockito.when(colProductData.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.projection(any(Bson.class))).thenReturn(projectionIterable);
        Mockito.when(projectionIterable.first()).thenReturn(docBase);
        // Run the test
        List<Error> result = staffEligibilityChangeService.validate(document);
        // Verify the results
        assertEquals(1L, result.size());
    }

    @Test
    public void testValidate_givenDocumentAndActionCdeUpdate_returnsErrorList() {
        // Setup
        Document document = new Document(Field.actionCde, "update");
        Document docChange = CommonUtils.readResourceAsDocument("/files/staff-license-check-doc-change.json");
        document.put(Field.doc, docChange);
        Document docBase = (Document) docChange.get("docBase");
        document.put(Field.docBase, docBase);
        // Run the test
        List<Error> result = staffEligibilityChangeService.validate(document);
        // Verify the results
        assertEquals(0L, result.size());
    }

    @Test
    public void testValidate_givenDocumentAndActionCdeUpdateWithEmptyStafLicCheck_returnsErrorList() {
        // Setup
        Document document = new Document(Field.actionCde, "update");
        Document docChange = CommonUtils.readResourceAsDocument("/files/staff-license-check-doc-change.json");
        document.put(Field.doc, docChange);
        Document docBase = (Document) docChange.get("docBase");
        docBase.remove("stafLicCheck");
        document.put(Field.docBase, docBase);
        // Run the test
        List<Error> result = staffEligibilityChangeService.validate(document);
        // Verify the results
        assertEquals(1L, result.size());
    }

    @Test
    public void testDelete_givenDocument_returnsNull() {
        // Setup
        Document document = CommonUtils.readResourceAsDocument("/files/staff-license-check-doc-change.json");
        Document docBase = CommonUtils.readResourceAsDocument("/files/staff-license-check.json");
        Mockito.when(colProductData.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.first()).thenReturn(docBase);
        // Run the test
        try {
            staffEligibilityChangeService.delete(document);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testFindFirst_givenFilterAndNull_returnDocument() {
        // Setup
        Bson filter = Filters.eq("key", "value");
        Document document = CommonUtils.readResourceAsDocument("/files/staff-license-check.json");
        Mockito.when(colProductData.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.projection(any(Bson.class))).thenReturn(projectionIterable);
        Mockito.when(projectionIterable.first()).thenReturn(document);
        // Run the test
        Document result = staffEligibilityChangeService.findFirst(filter, null);
        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testFindFirst_givenFilterAndDocChange_returnDocument() {
        // Setup
        Bson filter = Filters.eq("key", "value");
        Document document = CommonUtils.readResourceAsDocument("/files/staff-license-check.json");
        Mockito.when(colProductData.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.projection(any(Bson.class))).thenReturn(projectionIterable);
        Mockito.when(projectionIterable.first()).thenReturn(document);
        Document docChange = CommonUtils.readResourceAsDocument("/files/staff-license-check-doc-change.json");
        // Run the test
        Document result = staffEligibilityChangeService.findFirst(filter, docChange);
        // Verify the results
        assertNotNull(result);
    }

    @Test(expected = productErrorException.class)
    public void testAdd_givenDocument_throwsException() {
        Document docChange = CommonUtils.readResourceAsDocument("/files/staff-license-check-doc-change.json");
        Mockito.when(colProductData.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.first()).thenReturn(null);
        staffEligibilityChangeService.add(docChange);
    }

    @Test
    public void testAdd_givenDocument_returnsNull() {
        Document docChange = CommonUtils.readResourceAsDocument("/files/staff-license-check-doc-change.json");
        Document document = CommonUtils.readResourceAsDocument("/files/staff-license-check.json");
        Mockito.when(colProductData.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.first()).thenReturn(document);
        try {
            staffEligibilityChangeService.add(docChange);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testupdate_givenDocument_returnsNull() {
        Document docChange = CommonUtils.readResourceAsDocument("/files/staff-license-check-doc-change.json");
        Document document = CommonUtils.readResourceAsDocument("/files/staff-license-check.json");
        Mockito.when(colProductData.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.first()).thenReturn(document);
        try {
            staffEligibilityChangeService.update(docChange);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testValidate_givenInvalidFrmlaEmpEligBfrCtoffText_returnsErrorList() {
        // Setup
        Document document = new Document(Field.actionCde, "add");
        Document docChange = CommonUtils.readResourceAsDocument("/files/staff-license-check-doc-change.json");
        Document stafLicCheck = (Document) docChange.get("stafLicCheck");
        stafLicCheck.put("frmlaEmpEligBfrCtoffText", "*");
        docChange.put("stafLicCheck", stafLicCheck);
        document.put(Field.doc, docChange);
        // Run the test
        List<Error> result = staffEligibilityChangeService.validate(document);
        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testValidate_givenInvalidFrmlaEmplyEligText_returnsErrorList() {
        // Setup
        Document document = new Document(Field.actionCde, "add");
        Document docChange = CommonUtils.readResourceAsDocument("/files/staff-license-check-doc-change.json");
        Document stafLicCheck = (Document) docChange.get("stafLicCheck");
        stafLicCheck.put("frmlaEmplyEligText", "*");
        docChange.put("stafLicCheck", stafLicCheck);
        document.put(Field.doc, docChange);
        // Run the test
        List<Error> result = staffEligibilityChangeService.validate(document);
        // Verify the results
        assertNotNull(result);
    }
}
