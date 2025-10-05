package com.hhhh.group.secwealth.mktdata.elastic.configuration;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class DataHandlerConfigTest {
    @InjectMocks
    private DataHandlerConfig underTest;

    @Nested
    class WhenDataingTypeBySiteMap {
        @Test
        void test_dataTypeBySiteMap() {
            ReflectionTestUtils.setField(underTest, "dataTypeBySiteStr", "HK_HBAP:SEC-CN,SEC-HK,SEC-US,WRTS-HK,UT");
            assertNotNull(underTest.dataTypeBySiteMap());
        }
    }

    @Nested
    class WhenXmlingInputFactory {
        @Test
        void test_xmlInputFactory() {
            assertNotNull(underTest.xmlInputFactory());
        }
    }
}
