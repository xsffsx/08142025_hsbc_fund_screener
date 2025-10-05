package com.hhhh.group.secwealth.mktdata.common.validator.vo;

import com.google.common.collect.Maps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RegionEntityTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new RegionEntity().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new RegionEntity().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new RegionEntity());
    }

    @Test
    void testSetterAndGetter() {
        RegionEntity obj = new RegionEntity();

        obj.setSiteKey("");
        obj.setServiceEntities(Maps.newHashMap());

        Assertions.assertNotNull(obj.getSiteKey());
        Assertions.assertNotNull(obj.getServiceEntities());
    }
}
