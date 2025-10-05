package com.hhhh.group.secwealth.mktdata.common.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ErrResponseTest {

    private ErrResponse errResponseUnderTest;

    @Test
    void testToString() {
        Assertions.assertNotNull(new ErrResponse().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new ErrResponse().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new ErrResponse());
    }

    @Test
    void testSetterAndGetter() {
        ErrResponse obj = new ErrResponse();
        obj.setResponseCode("");
        obj.setReasonCode("");
        obj.setText("");

        Assertions.assertNotNull(obj.getResponseCode());
        Assertions.assertNotNull(obj.getReasonCode());
        Assertions.assertNotNull(obj.getText());
    }
}
