package com.hhhh.group.secwealth.mktdata.quote.beans.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProductKeyTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new ProductKey().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new ProductKey().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new ProductKey());
    }

    @Test
    void testSetterAndGetter() {
        ProductKey obj = new ProductKey();

        obj.setProdCdeAltClassCde("test");
        obj.setProdAltNum("test");
        obj.setProductType("test");

        Assertions.assertNotNull(obj.getProdCdeAltClassCde());
        Assertions.assertNotNull(obj.getProdAltNum());
        Assertions.assertNotNull(obj.getProductType());
    }
}