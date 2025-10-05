package com.dummy.wmd.wpc.graphql.utils;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.springframework.expression.AccessException;
import org.springframework.expression.TypedValue;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class NullableMapAccessorTests {

    private NullableMapAccessor nullableMapAccessor;

    @Before
    public void setUp() throws Exception {
        nullableMapAccessor = new NullableMapAccessor();
    }

    @Test
    public void testCanRead_givenObject_returnsBoolean() throws Exception {
        assertFalse(nullableMapAccessor.canRead(null, "target", "name"));
    }

    @Test
    public void testRead_givenObject_returnsValue() throws AccessException {
        Document document = new Document("key", "value");
        TypedValue result = nullableMapAccessor.read(null, document, "name");
        // Verify the results
        assertNotNull(result);
    }

}
