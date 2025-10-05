package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UtProdChanlIdTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new UtProdChanlId().toString());
    }

    @Test
    void testHashCode() {
        UtProdChanlId obj = new UtProdChanlId();
        Assertions.assertNotEquals(new Object().hashCode(), obj.hashCode());
        obj.setProdId(0);
        Assertions.assertNotEquals(new Object().hashCode(), obj.hashCode());
        obj.setBatchId(0L);
        Assertions.assertNotEquals(new Object().hashCode(), obj.hashCode());
    }

    @Test
    void testNotEquals() {
        UtProdChanlId obj = new UtProdChanlId();
        Assertions.assertNotEquals(new Object(), obj);
        obj.setProdId(0);
        Assertions.assertNotEquals(new Object(), obj);
        obj.setBatchId(0L);
        Assertions.assertNotEquals(new Object(), obj);
    }

    @Test
    void testSetterAndGetter() {
        UtProdChanlId obj = new UtProdChanlId();
        obj.setProdId(0);
        obj.setBatchId(0L);

        Assertions.assertEquals(0, obj.getProdId());
        Assertions.assertEquals(0, obj.getBatchId());
    }
}
