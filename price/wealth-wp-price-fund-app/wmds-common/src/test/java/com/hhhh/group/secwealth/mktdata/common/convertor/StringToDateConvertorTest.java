package com.hhhh.group.secwealth.mktdata.common.convertor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(MockitoJUnitRunner.Silent.class)
public class StringToDateConvertorTest {

    @InjectMocks
    private StringToDateConvertor stringToDateConvertor;

    @Test
    public void test_doConvert() {
        stringToDateConvertor.setTimeZone("GMT+5");
        stringToDateConvertor.setDateFormat("MM/dd/yyyy");
        assertNull(stringToDateConvertor.doConvert(""));
        assertNotNull(stringToDateConvertor.doConvert("10/15/2024"));
        stringToDateConvertor.setDateFormat("yyyy-MM-ddTHH:mm");
        assertNotNull(stringToDateConvertor.doConvert("2024-10-25T15:20"));

        assertNotNull(stringToDateConvertor.doConvert("2024-10-25 15:20", "yyyy-MM-dd HH:mm"));
        assertNotNull(stringToDateConvertor.doConvert("2024-10-25 15:20", "yyyy-MM-dd HH:mm", "GMT+8"));
        assertNotNull(stringToDateConvertor.doConvert("2024-10-25T15:20", "null", "null"));
    }
}
