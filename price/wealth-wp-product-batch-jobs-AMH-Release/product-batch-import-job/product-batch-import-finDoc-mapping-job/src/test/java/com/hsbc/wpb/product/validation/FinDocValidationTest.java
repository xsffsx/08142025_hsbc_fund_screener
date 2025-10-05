package com.dummy.wpb.product.validation;

import com.dummy.wpb.product.constant.FinDocConstants;
import com.dummy.wpb.product.service.ReferenceDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinDocValidationTest {

    @Mock
    private ReferenceDataService referenceDataService;

    FinDocValidation validation;

    @BeforeEach
    void setUp() {
        validation = new FinDocValidation() {
            @Override
            protected boolean validation() {
                return false;
            }
        };
        ReflectionTestUtils.setField(validation, "referenceDataService", referenceDataService);
        ReflectionTestUtils.setField(validation, "version", FinDocConstants.VER_1_1);
    }

    @Test
    void testCheckCtryCdeWithNull() {
        // Arrange
        String ctryCde = null;

        // Act
        boolean result = validation.checkCtryCde(ctryCde);

        // Assert
        assertTrue(result);
        assertNull(validation.errmsg);
    }

    @Test
    void testCheckCtryCdeWithValidValue() {
        // Arrange
        String ctryCde = "USA";

        // Act
        boolean result = validation.checkCtryCde(ctryCde);

        // Assert
        assertFalse(result);
    }

    @Test
    void testCheckOrgnCdeWithNull() {
        // Arrange
        String orgnCde = null;

        // Act
        boolean result = validation.checkOrgnCde(orgnCde);

        // Assert
        assertTrue(result);
        assertNull(validation.errmsg);
    }

    @Test
    void testCheckOrgnCdeWithValidValue() {
        // Arrange
        String orgnCde = "12345";

        // Act
        boolean result = validation.checkOrgnCde(orgnCde);

        // Assert
        assertFalse(result);
    }

    @Test
    void testCheckDocTypeCdeWithNull() {
        // Arrange
        String ctryCde = "US";
        String orgnCde = "1234";
        String docTypeCde = null;

        // Act
        boolean result = validation.checkDocTypeCde(ctryCde, orgnCde, docTypeCde);

        // Assert
        assertTrue(result);
        assertNull(validation.errmsg);
    }

    @Test
    void testCheckDocTypeCdeWithValidValue() throws Exception {
        // Arrange
        String ctryCde = "US";
        String orgnCde = "1234";
        String docTypeCde = "DOC_TYPE";

        // Mock the reference data service
        when(referenceDataService.referenceDataByFilter(any())).thenReturn(new ArrayList<>());

        // Act
        boolean result = validation.checkDocTypeCde(ctryCde, orgnCde, docTypeCde);

        // Assert
        assertFalse(result);
    }

    @Test
    void testCheckDocTypeCdeWithInvalidValue() throws Exception {
        // Arrange
        String ctryCde = "US";
        String orgnCde = "1234";
        String docTypeCde = "INVALID_TYPEINVALID_TYPEINVALID_TYPEINVALID_TYPE";

        // Act
        boolean result = validation.checkDocTypeCde(ctryCde, orgnCde, docTypeCde);

        // Assert
        assertFalse(result);
    }

    @Test
    void testCheckDocSubtpCdeWithNull() {
        // Arrange
        String ctryCde = "US";
        String orgnCde = "1234";
        String docSubtpCde = null;

        // Act
        boolean result = validation.checkDocSubtpCde(ctryCde, orgnCde, docSubtpCde);

        // Assert
        assertTrue(result);
        assertNull(validation.errmsg);
    }

    @Test
    void testCheckDocSubtpCdeWithValidValue() throws Exception {
        // Arrange
        String ctryCde = "US";
        String orgnCde = "1234";
        String docSubtpCde = "DOC_TYPE";

        // Mock the reference data service
        when(referenceDataService.referenceDataByFilter(any())).thenReturn(new ArrayList<>());

        // Act
        boolean result = validation.checkDocSubtpCde(ctryCde, orgnCde, docSubtpCde);

        // Assert
        assertFalse(result);
    }

    @Test
    void testCheckDocSubtpCdeWithInvalidValue() throws Exception {
        // Arrange
        String ctryCde = "US";
        String orgnCde = "1234";
        String docSubtpCde = "INVALID_TYPEINVALID_TYPEINVALID_TYPEINVALID_TYPE";

        // Act
        boolean result = validation.checkDocSubtpCde(ctryCde, orgnCde, docSubtpCde);

        // Assert
        assertFalse(result);
    }
    @Test
    void checkDocId() {
        assertFalse(validation.checkDocId("12345678901234567890123456789012345678901234567890"));
    }

    @Test
    void checkLangCatCdeIsNull() {
        assertTrue(validation.checkLangCatCde(null));
    }

    @Test
    void checkLangCatCdeLength() {
        assertFalse(validation.checkLangCatCde("123"));
    }

    @Test
    void testCheckProdTypeCdeWithNull() {
        // Arrange
        String ctryCde = "US";
        String orgnCde = "1234";
        String prodTypeCde = null;

        // Act
        boolean result = validation.checkProdTypeCde(ctryCde, orgnCde, prodTypeCde);

        // Assert
        assertTrue(result);
        assertNull(validation.errmsg);
    }

    @Test
    void testCheckProdTypeCdeWithValidValue() throws Exception {
        // Arrange
        String ctryCde = "US";
        String orgnCde = "1234";
        String prodTypeCde = "DOC_TYPE";

        // Mock the reference data service
        when(referenceDataService.referenceDataByFilter(any())).thenReturn(new ArrayList<>());

        // Act
        boolean result = validation.checkProdTypeCde(ctryCde, orgnCde, prodTypeCde);

        // Assert
        assertFalse(result);
    }

    @Test
    void testCheckProdTypeCdeWithInvalidValue() throws Exception {
        // Arrange
        String ctryCde = "US";
        String orgnCde = "1234";
        String prodTypeCde = "INVALID_TYPEINVALID_TYPEINVALID_TYPEINVALID_TYPE";

        // Act
        boolean result = validation.checkProdTypeCde(ctryCde, orgnCde, prodTypeCde);

        // Assert
        assertFalse(result);
    }

    @Test
    void testCheckProdSubtpCdeWithNull(){
        // Arrange
        String ctryCde = "US";
        String orgnCde = "1234";
        String prodTypeCde = "";
        String prodSubtpCde = null;

        // Act
        boolean result = validation.checkProdSubtpCde(ctryCde, orgnCde, prodSubtpCde, prodTypeCde);

        // Assert
        assertTrue(result);
    }

    @Test
    void testCheckProdSubtpCdeWithValidValue() throws Exception {
        // Arrange
        String ctryCde = "US";
        String orgnCde = "1234";
        String prodTypeCde = "DOC_TYPE";
        String prodSubtpCde = "DOC_TYPE";

        // Act
        boolean result = validation.checkProdSubtpCde(ctryCde, orgnCde, prodSubtpCde, prodTypeCde);

        // Assert
        assertFalse(result);
    }

    @Test
    void testCheckProdSubtpCdeWithInvalidValue() throws Exception {
        // Arrange
        String ctryCde = "US";
        String orgnCde = "1234";
        String prodTypeCde = "INVALID_TYPEINVALID_TYPEINVALID_TYPEINVALID_TYPE";
        String prodSubtpCde = "INVALID_TYPEINVALID_TYPEINVALID_TYPEINVALID_TYPE";

        // Act
        boolean result = validation.checkProdSubtpCde(ctryCde, orgnCde, prodSubtpCde, prodTypeCde);

        // Assert
        assertFalse(result);
    }

    @Test
    void checkEmailAdrRpyTextLength() {
        assertFalse(validation.checkEmailAdrRpyText("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"));
    }


}