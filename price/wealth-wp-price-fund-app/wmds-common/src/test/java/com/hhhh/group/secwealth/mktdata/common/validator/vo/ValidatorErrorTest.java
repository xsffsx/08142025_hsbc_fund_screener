package com.hhhh.group.secwealth.mktdata.common.validator.vo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ValidatorErrorTest {

    private ValidatorError validatorErrorUnderTest;

    @Test
    void testToString() {
        Assertions.assertNotNull(new ValidatorError().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new ValidatorError().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new ValidatorError());
    }

    @Test
    void testSetterAndGetter() {
        ValidatorError obj = new ValidatorError("", "", "", "");
        obj.setReasonCode("");
        obj.setField("");
        obj.setText("");
        obj.setValidatorMethod("");

        Assertions.assertNotNull(obj.getReasonCode());
        Assertions.assertNotNull(obj.getField());
        Assertions.assertNotNull(obj.getText());
        Assertions.assertNotNull(obj.getValidatorMethod());
    }
}
