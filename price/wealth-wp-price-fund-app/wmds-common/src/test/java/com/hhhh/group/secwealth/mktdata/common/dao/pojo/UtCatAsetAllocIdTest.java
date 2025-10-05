package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UtCatAsetAllocIdTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new UtCatAsetAllocId().toString());
    }

    @Test
    void testHashCode() {
        UtCatAsetAllocId obj = new UtCatAsetAllocId();
        Assertions.assertNotEquals(0, new Object().hashCode());
        obj.setBatchId(0L);
        Assertions.assertNotEquals(0, new Object().hashCode());
        obj.setProductId(0);
        Assertions.assertNotEquals(0, new Object().hashCode());
        obj.setClassTypeCode("");
        Assertions.assertNotEquals(0, new Object().hashCode());
        obj.setAssetClsCde("");
        Assertions.assertNotEquals(0, new Object().hashCode());
    }

    @Test
    void testNotEquals() {
        UtCatAsetAllocId obj = new UtCatAsetAllocId();
        Assertions.assertNotEquals(new Object(), obj);
        obj.setBatchId(0L);
        Assertions.assertNotEquals(new Object(), obj);
        obj.setProductId(0);
        Assertions.assertNotEquals(new Object(), obj);
        obj.setClassTypeCode("");
        Assertions.assertNotEquals(new Object(), obj);
        obj.setAssetClsCde("");
        Assertions.assertNotEquals(new Object(), obj);


    }

    @Test
    void testSetterAndGetter() {
        UtCatAsetAllocId obj = new UtCatAsetAllocId();

        obj.setBatchId(0L);
        obj.setProductId(0);
        obj.setClassTypeCode("");
        obj.setAssetClsCde("");

        Assertions.assertNotNull(obj.getBatchId());
        Assertions.assertNotNull(obj.getProductId());
        Assertions.assertNotNull(obj.getClassTypeCode());
        Assertions.assertNotNull(obj.getAssetClsCde());
    }
}
