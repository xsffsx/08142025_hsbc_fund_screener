package com.hhhh.group.secwealth.mktdata.elastic.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
public class ProductEntityConfigTest {
    @InjectMocks
    private ProductEntityConfig underTest;

    @Nested
    class WhenInitingProductEntities {

        @BeforeEach
        void setup() {
            ReflectionTestUtils.setField(underTest, "mappingFile", "/predsrch/productEntity-mapping.xml");
            ReflectionTestUtils.setField(underTest, "configFilePath", "/predsrch/product-description.xml");
        }

        @Test
        void initProductEntities () {
            assertDoesNotThrow(() -> underTest.initProductEntities());
        }
    }
}
