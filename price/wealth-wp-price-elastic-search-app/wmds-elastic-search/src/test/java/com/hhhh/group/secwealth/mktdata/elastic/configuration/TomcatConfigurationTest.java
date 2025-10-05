package com.hhhh.group.secwealth.mktdata.elastic.configuration;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
public class TomcatConfigurationTest {
    @InjectMocks
    private TomcatConfiguration underTest;

    @Nested
    class WhenCustomizing {
        @Mock
        private TomcatServletWebServerFactory factory;

        @Test
        void test_customize() {
            assertDoesNotThrow(() -> underTest.customize(factory));
        }
    }
}
