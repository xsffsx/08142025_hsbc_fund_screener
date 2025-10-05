package com.hhhh.group.secwealth.mktdata.common.exception;

import com.google.common.collect.Maps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ErrResponseHolderTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new ErrResponseHolder().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new ErrResponseHolder().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new ErrResponseHolder());
    }

    @Test
    void testSetterAndGetter() {
        ErrResponseHolder obj = new ErrResponseHolder();

        obj.setErrResponses(Maps.newHashMap());
        Assertions.assertNotNull(obj.getErrResponses());
    }
}
