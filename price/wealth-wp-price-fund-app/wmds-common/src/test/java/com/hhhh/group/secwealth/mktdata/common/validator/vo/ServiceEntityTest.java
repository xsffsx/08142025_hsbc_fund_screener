package com.hhhh.group.secwealth.mktdata.common.validator.vo;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ServiceEntityTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new ServiceEntity().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new ServiceEntity().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new ServiceEntity());
    }

    @Test
    void testSetterAndGetter() {
        ServiceEntity obj = new ServiceEntity();
        obj.setReqParameters(new String[]{""});
        obj.setReqListParameters(new String[]{""});
        obj.setValidators(Lists.newArrayList());
        obj.setServiceName("");
        obj.setRequestClassName("");

        Assertions.assertNotNull(obj.getReqParameters());
        Assertions.assertNotNull(obj.getReqListParameters());
        Assertions.assertNotNull(obj.getValidators());
        Assertions.assertNotNull(obj.getServiceName());
        Assertions.assertNotNull(obj.getRequestClassName());
    }
}
