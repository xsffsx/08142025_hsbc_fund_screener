package com.dummy.wpb.wpc.utils;

import com.jayway.jsonpath.DocumentContext;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.LinkedHashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class ProductHelperTests {

    @Mock
    private DocumentContext documentContext;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testRemodelViaConfig_givenMapAndDocument_meetPriority() {
        try {
            Document prod = Document.parse(CommonUtils.readResource("/data/product-data2.json"));
            Document configuration = Document.parse(CommonUtils.readResource("/data/configuration2.json"));
            Document config = (Document) configuration.get("config");
            ProductHelper.remodelViaConfig(prod, config);
            Assert.assertEquals("Y", prod.get("extField"));
            Assert.assertEquals("Y", prod.get("extOpField"));
            Assert.assertEquals("Y", prod.get("extEgField"));
            Assert.assertEquals("Y", prod.get("udfField"));
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testCopy_givenDocumentContextAndFromAndTo_doesNotThrow() {
        try {
            Mockito.when(documentContext.read(anyString(), any(Object.class.getClass()))).thenReturn(new Object()).thenReturn(null);
            Mockito.when(documentContext.set(anyString(), any(Object.class))).thenReturn(documentContext);
            Mockito.when(documentContext.set(anyString(), any(LinkedHashMap.class))).thenReturn(documentContext);
            ProductHelper.copy(documentContext, "A.from", "B.to");
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCopy_givenDocumentContextAndFromAndTo_throwException() {
        Mockito.when(documentContext.read(anyString(), any(Object.class.getClass()))).thenReturn(new Object());
        ProductHelper.copy(documentContext, "A.from", "B].to");
    }
}
