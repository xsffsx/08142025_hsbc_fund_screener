package com.dummy.wpb.product.mapper;

import com.dummy.wpb.product.constant.FinDocConstants;
import com.dummy.wpb.product.model.FinDocMapInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FinDocMappingExlRowMapperTest {

    @Mock
    private RowSet rowSet;

    private FinDocMappingExlRowMapper rowMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        rowMapper = new FinDocMappingExlRowMapper();
    }

    @Test
    void testMapRow() throws Exception {
        // Arrange
        String[] currentRow = new String[] {
                "actnCdeValue",
                "ctryCdeValue",
                "orgnCdeValue",
                "prodTypeCdeValue",
                "prodSubtpCdeValue",
                "prodIdValue",
                "docTypeCdePValue",
                "langCatCdePValue",
                "docSourceValue",
                "docTypeCdeValue",
                "docSubtpCdeValue",
                "docIdValue",
                "langCatCdeValue",
                "urlValue",
                "emailAdrRpyTextValue"
        };
        when(rowSet.getCurrentRow()).thenReturn(currentRow);
        when(rowSet.getCurrentRowIndex()).thenReturn(2);

        // Act
        FinDocMapInput result = rowMapper.mapRow(rowSet);

        // Assert
        assertEquals(0, result.getRecNum());
        assertEquals("actnCdeValue", result.getActnCde());
        assertEquals("ctryCdeValue", result.getCtryCde());
        assertEquals("orgnCdeValue", result.getOrgnCde());
        assertEquals("prodTypeCdeValue", result.getProdTypeCde());
        assertEquals("prodSubtpCdeValue", result.getProdSubtpCde());
        assertEquals("prodIdValue", result.getProdId());
        assertEquals("docTypeCdePValue", result.getDocTypeCdeP());
        assertEquals("langCatCdePValue", result.getLangCatCdeP());
        assertEquals("docSourceValue", result.getDocSource());
        assertEquals("docTypeCdeValue", result.getDocTypeCde());
        assertEquals("docSubtpCdeValue", result.getDocSubtpCde());
        assertEquals("docIdValue", result.getDocId());
        assertEquals("langCatCdeValue", result.getLangCatCde());
        assertEquals("urlValue", result.getUrl());
        assertEquals("emailAdrRpyTextValue", result.getEmailAdrRpyText());
        assertEquals(FinDocConstants.CUST_CLASS_CDE_DEFAULT, result.getCustClassCde());

        verify(rowSet, times(1)).getCurrentRow();
        verify(rowSet, times(1)).getCurrentRowIndex();
    }

    @Test
    void testMapRowWithMissingValues() throws Exception {
        // Arrange
        String[] currentRow = new String[] {
                "actnCdeValue",
                "ctryCdeValue",
                "orgnCdeValue",
                "prodTypeCdeValue"
        };
        when(rowSet.getCurrentRow()).thenReturn(currentRow);
        when(rowSet.getCurrentRowIndex()).thenReturn(2);

        // Act
        FinDocMapInput result = rowMapper.mapRow(rowSet);

        // Assert
        assertEquals(0, result.getRecNum());
        assertEquals("actnCdeValue", result.getActnCde());
        assertEquals("ctryCdeValue", result.getCtryCde());
        assertEquals("orgnCdeValue", result.getOrgnCde());
        assertEquals("prodTypeCdeValue", result.getProdTypeCde());
        assertNull(result.getProdSubtpCde());
        assertNull(result.getProdId());
        assertNull(result.getDocTypeCdeP());
        assertNull(result.getLangCatCdeP());
        assertNull(result.getDocSource());
        assertNull(result.getDocTypeCde());
        assertNull(result.getDocSubtpCde());
        assertNull(result.getDocId());
        assertNull(result.getLangCatCde());
        assertNull(result.getUrl());
        assertNull(result.getEmailAdrRpyText());
        assertEquals(FinDocConstants.CUST_CLASS_CDE_DEFAULT, result.getCustClassCde());

        verify(rowSet, times(1)).getCurrentRow();
        verify(rowSet, times(1)).getCurrentRowIndex();
    }
}