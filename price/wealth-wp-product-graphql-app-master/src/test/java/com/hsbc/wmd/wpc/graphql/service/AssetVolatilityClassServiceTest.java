package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AssetVolatilityClassServiceTest {

    @Mock
    private AssetVolatilityClassCorlChangeService mockCorlChangeService;
    @Mock
    private AssetVolatilityClassCharChangeService mockCharChangeService;
    @InjectMocks
    private AssetVolatilityClassService assetVolatilityClassServiceUnderTest;

    @Test
    public void testSaveAssetVolatilityClassChar_givenDocumentWithId_returnsDocument() {
        // Setup
        Document doc = new Document(Field._id, "id");
        Mockito.when(mockCharChangeService.validateDocument(any(Document.class))).thenReturn(Collections.emptyList());
        when(mockCharChangeService.updateDocument(any(Document.class))).thenReturn(doc);
        // Run the test
        Document result = assetVolatilityClassServiceUnderTest.saveAssetVolatilityClassChar(doc);
        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testSaveAssetVolatilityClassChar_givenDocumentWithoutId_returnsDocument() {
        // Setup
        Document doc = new Document("key", "value");
        Mockito.when(mockCharChangeService.validateDocument(any(Document.class))).thenReturn(Collections.emptyList());
        Mockito.when(mockCharChangeService.addDocument(any(Document.class))).thenReturn(doc);
        // Run the test
        Document result = assetVolatilityClassServiceUnderTest.saveAssetVolatilityClassChar(doc);
        // Verify the results
        assertNotNull(result);
    }

    @Test(expected = productErrorException.class)
    public void testSaveAssetVolatilityClassChar_givenDocument_throwsException() {
        // Setup
        Document doc = new Document("key", "value");
        List<Error> errorList = new ArrayList<>();
        errorList.add(new Error("error", "validationErrors", "validationErrors"));
        Mockito.when(mockCharChangeService.validateDocument(any(Document.class))).thenReturn(errorList);
        // Run the test
        assetVolatilityClassServiceUnderTest.saveAssetVolatilityClassChar(doc);

    }

    @Test
    public void testSaveAssetVolatilityClassCorl_givenDocument_returnsDocument() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/aset-voltl-class-corl-test.json");
        Mockito.when(mockCorlChangeService.validateDocument(any(Document.class))).thenReturn(Collections.emptyList());
        Mockito.when(mockCorlChangeService.updateDocument(any(Document.class))).thenReturn(doc);
        // Run the test
        Document result = assetVolatilityClassServiceUnderTest.saveAssetVolatilityClassCorl(doc);
        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testSaveAssetVolatilityClassCorl_givenDocumentWithoutId_returnsDocument() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/aset-voltl-class-corl-test.json");
        doc.remove(Field._id);
        Mockito.when(mockCorlChangeService.validateDocument(any(Document.class))).thenReturn(Collections.emptyList());
        Mockito.when(mockCorlChangeService.addDocument(any(Document.class))).thenReturn(doc);
        // Run the test
        Document result = assetVolatilityClassServiceUnderTest.saveAssetVolatilityClassCorl(doc);
        // Verify the results
        assertNotNull(result);
    }

}
