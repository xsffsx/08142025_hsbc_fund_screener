package com.hhhh.group.secwealth.mktdata.common.validator.vo;

import net.sf.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

class RuleSetTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new RuleSet().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new RuleSet().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new RuleSet());
    }

    @Test
    void testSetterAndGetter() {
        RuleSet obj = new RuleSet();
        obj.setType("");
        obj.setFields("");
        obj.setErrorKey("");
        obj.setAssociateFields("");

        Assertions.assertNotNull(obj.getType());
        Assertions.assertNotNull(obj.getFields());
        Assertions.assertNotNull(obj.getErrorKey());
        Assertions.assertNotNull(obj.getAssociateFields());
    }

    @Test
    void testPreValidate() throws Exception {
        RuleSet obj = new RuleSet();
        obj.preValidate("obj");
        Assertions.assertNotNull(obj);
    }

    @Test
    void testValidate() throws Exception {
        RuleSet obj = new RuleSet();
        List<ValidatorError> list = obj.validate(new HashMap<>(), new JSONObject(false));
        Assertions.assertNotNull(list);
    }
}
