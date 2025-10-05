package com.dummy.wmd.wpc.graphql.utils;

import com.dummy.wmd.wpc.graphql.service.MetadataService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class DataProcessingUtilsTest {
    @Mock
    private MetadataService MetadataService;
    @InjectMocks
    private DataProcessingUtils dataProcessingUtils;
    private Map<String, String> businessNameMap = new LinkedHashMap<>();

    @Before
    public void setUp() {
        dataProcessingUtils = new DataProcessingUtils(MetadataService);
        businessNameMap.put("key", "value");
        ReflectionTestUtils.setField(dataProcessingUtils, "businessNameMap", businessNameMap);
    }

    @Test
    public void testSimplifyErrorMessage_givenInvalidProductsErrorMessage_returnsString() {
        String errorMessage = CommonUtils.readResource("/files/error-message.json");
        String message = DataProcessingUtils.simplifyErrorMessage(errorMessage);
        assertNotNull(message);
    }

    @Test
    public void testSimplifyErrorMessage_givenErrorMessage_returnsString() {
        String errorMessage1 = "com.dummy.wpb.product.error.ProductNotFoundException:  Product not found: (CTRY_REC_CDE: GB, GRP_MEMBR_REC_CDE: HBEU, PROD_TYPE_CDE: UT, PROD_ALT_PRIM_NUM: 0585239)";
        String errorMessage2 = "Exception: Product not found\n ";
        String errorMessage3 = "Product not found";
        String message1 = DataProcessingUtils.simplifyErrorMessage(errorMessage1);
        assertNotNull(message1);
        String message2 = DataProcessingUtils.simplifyErrorMessage(errorMessage2);
        assertNotNull(message2);
        String message3 = DataProcessingUtils.simplifyErrorMessage(errorMessage3);
        assertNotNull(message3);
    }

    @Test
    public void testSimplifyErrorMessage_givenExceptionWithLongMessage_returnsString() {
        String errorMessage = "Exception: throws an Exception, want to make the message length > 200 so that we have to " +
                "give a long String when the message length is > 200 , the massage will be shorted to MAX_MESSAGE_LEN " +
                "that we set in DataProcessingUtils";
        String message = DataProcessingUtils.simplifyErrorMessage(errorMessage);
        assertNotNull(message);
    }

    @Test
    public void testConvertPayload_givenJsonList_returnsMapList() {
        // Setup
        String data = "[{\"key1\":\"value1\"},{\"key2\":\"value2\"}]";
        // Run the test
        List<Map<String, Object>> result = DataProcessingUtils.convertPayload(data);
        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testConvertRawData_givenJsonList_returnsMap() {
        // Setup
        String data = "[{\"key1\":\"value1\"},{\"key2\":\"value2\"}]";
        // Run the test
        Map<String, Object> result = DataProcessingUtils.convertRawData(data);
        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testConvertRawData_givenJsonString_returnsMap() {
        // Setup
        String data = "{\"key1\":\"value1\"}";
        // Run the test
        Map<String, Object> result = DataProcessingUtils.convertRawData(data);
        // Verify the results
        assertNotNull(result);
    }
}
