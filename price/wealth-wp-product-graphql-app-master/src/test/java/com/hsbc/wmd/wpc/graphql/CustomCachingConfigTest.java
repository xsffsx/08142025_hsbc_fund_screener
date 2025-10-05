package com.dummy.wmd.wpc.graphql;

import com.dummy.wmd.wpc.graphql.model.ProductMetadata;
import org.junit.Before;
import org.junit.Test;
import org.springframework.cache.interceptor.KeyGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CustomCachingConfigTest {

    private CustomCachingConfig customCachingConfigUnderTest;

    @Before
    public void setUp() throws Exception {
        customCachingConfigUnderTest = new CustomCachingConfig();
    }

    @Test
    public void testKeyGenerator_givenNull_returnsKeyGenerator() throws NoSuchMethodException {
        KeyGenerator result = customCachingConfigUnderTest.keyGenerator();
        assertNotNull(result);
        ProductMetadata object = new ProductMetadata();
        Object generate = result.generate(object, object.getClass().getMethod("equals", Object.class), "test");
        assertEquals("ProductMetadata.equals(test)", generate);
    }
}
