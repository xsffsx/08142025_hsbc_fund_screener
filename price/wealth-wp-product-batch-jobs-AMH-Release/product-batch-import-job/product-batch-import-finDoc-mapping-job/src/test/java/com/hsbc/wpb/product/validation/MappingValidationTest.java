package com.dummy.wpb.product.validation;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.FinDocConstants;
import com.dummy.wpb.product.model.FinDocMapInput;
import com.dummy.wpb.product.service.ProductService;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MappingValidationTest {
    @InjectMocks
    MappingValidation validation;
    @Mock
    private ProductService productService;

    @Test
    void testCheckUrl() {
        assertTrue(validation.checkURL("123"));
        assertFalse(validation.checkURL("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"));
    }

    @Test
    void testCheckProdExistWithNullProdCde() {
        // Arrange
        FinDocMapInput fd = new FinDocMapInput();
        fd.setCtryCde("US");
        fd.setOrgnCde("1234");
        fd.setProdTypeCde("PROD_TYPE");
        fd.setProdId(null);

        // Act
        boolean result = validation.checkProdExist(fd);

        // Assert
        assertTrue(result);
        assertEquals(0L, fd.getProdRealID());
    }

    @Test
    void testCheckProdExistWithNullProdTypeCde() {
        // Arrange
        FinDocMapInput fd = new FinDocMapInput();
        fd.setCtryCde("US");
        fd.setOrgnCde("1234");
        fd.setProdTypeCde(null);
        fd.setProdId("PROD_ID");

        // Act
        boolean result = validation.checkProdExist(fd);

        // Assert
        assertFalse(result);
    }

    @Test
    void testCheckProdExistWithValidProdCde() throws Exception {
        // Arrange
        FinDocMapInput fd = new FinDocMapInput();
        fd.setCtryCde("US");
        fd.setOrgnCde("1234");
        fd.setProdTypeCde("PROD_TYPE");
        fd.setProdId("PROD_ID");

        // Mock the product service
        List<Document> documents = new ArrayList<>();
        Document prodDocument = new Document();
        prodDocument.put(Field.prodId, 12345);
        documents.add(prodDocument);
        when(productService.productByFilters(any())).thenReturn(documents);

        // Act
        boolean result = validation.checkProdExist(fd);

        // Assert
        assertTrue(result);
        assertEquals(12345L, fd.getProdRealID());
    }

    @Test
    void testCheckProdExistWithInvalidProdCde() throws Exception {
        // Arrange
        FinDocMapInput fd = new FinDocMapInput();
        fd.setCtryCde("US");
        fd.setOrgnCde("1234");
        fd.setProdTypeCde("PROD_TYPE");
        fd.setProdId("INVALID_PROD_ID");

        // Mock the product service
        List<Document> documents = new ArrayList<>();
        when(productService.productByFilters(any())).thenReturn(documents);

        // Act
        boolean result = validation.checkProdExist(fd);

        // Assert
        assertFalse(result);
        assertEquals(0L, fd.getProdRealID());
    }

    @Test
    void testCheckLangCatCdeP() {
        assertTrue(validation.checkLangCatCdeP(null));
    }

    @Test
    void testCheckLangCatCdePLength() {
        assertFalse(validation.checkLangCatCdeP("123"));
    }

    @Test
    void testCheckDocSrc() {
        assertTrue(validation.checkDocSrc(null));
    }

    @Test
    void testCheckDocSrcTrue() {
        assertTrue(validation.checkDocSrc(FinDocConstants.DOC_SRC_TYP_URL));
    }
    @Test
    void testCheckDocSrcFalse() {
        assertFalse(validation.checkDocSrc(FinDocConstants.DOC_SRC));
    }

    @Test
    void testSkipMail() {
        FinDocMapInput finDocMapInput = new FinDocMapInput();
        finDocMapInput.setInputFileName("123");
        finDocMapInput.setEmailAdrRpyText("add");
        finDocMapInput.setRecNum(2);
        assertNotNull(validation.skipMail(finDocMapInput, "msg"));
    }

    @Test
    void testCheckActnCdeNull() {
        assertTrue(validation.checkActnCde(null));
    }

    @Test
    void testCheckActnCdeValidValue() {
        assertTrue(validation.checkActnCde(FinDocConstants.ADD_ACTION));
        assertTrue(validation.checkActnCde(FinDocConstants.CHANGE_ACTION));
        assertTrue(validation.checkActnCde(FinDocConstants.DELETE_ACTION));
        assertTrue(validation.checkActnCde(FinDocConstants.MODIFY_ACTION));
    }

    @Test
    void testCheckActnCdeInvalidValue() {
        assertFalse(validation.checkActnCde("123"));
        assertFalse(validation.checkActnCde("B"));
    }

    @Test
    void testCheckDocExistNull() throws Exception {
        FinDocMapInput finDocMapInput = new FinDocMapInput();
        finDocMapInput.setDocSource(FinDocConstants.DOC_SRC_TYP_URL);
        assertTrue(validation.checkDocExist(finDocMapInput));
    }
}