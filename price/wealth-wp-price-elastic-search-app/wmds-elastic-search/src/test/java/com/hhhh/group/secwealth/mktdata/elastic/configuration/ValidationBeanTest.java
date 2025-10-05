package com.hhhh.group.secwealth.mktdata.elastic.configuration;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
public class ValidationBeanTest {
    @InjectMocks
    private ValidationBean underTest;

    @Nested
    class WhenXssingValidator {
        @Test
        void test_xssValidator() {
            assertDoesNotThrow(() -> underTest.xssValidator());
        }
    }
}
