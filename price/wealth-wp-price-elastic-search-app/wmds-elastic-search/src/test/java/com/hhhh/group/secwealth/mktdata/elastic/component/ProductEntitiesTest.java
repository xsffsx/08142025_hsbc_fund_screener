package com.hhhh.group.secwealth.mktdata.elastic.component;

import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductEntitiesTest {

    @Mock
    private Map<String, Product> productEntities;
    @InjectMocks
    private ProductEntities underTest;

    @Nested
    class WhenGettingProductEntities {
        @Test
        void test_getProductEntities() {
            assertNotNull(underTest.getProductEntities());
        }
    }
}
