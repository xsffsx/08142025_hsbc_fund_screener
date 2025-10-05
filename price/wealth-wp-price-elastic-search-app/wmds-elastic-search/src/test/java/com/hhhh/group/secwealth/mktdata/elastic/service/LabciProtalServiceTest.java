package com.hhhh.group.secwealth.mktdata.elastic.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class LabciProtalServiceTest {
    @InjectMocks
    private LabciProtalService underTest;

    @Nested
    class WhenEncryptingLabciProtalToken {
        private final String CHANNEL_ID = "OHI";
        private final String CUSTOMER_ID = "HK00900047200101";
        private final String MARKET = "HK";

        @Test
        void test_encryptingLabciProtalToken() {
            assertThrows(NullPointerException.class, () -> underTest.encryptLabciProtalToken(CHANNEL_ID, CUSTOMER_ID, MARKET));
        }
    }
}
