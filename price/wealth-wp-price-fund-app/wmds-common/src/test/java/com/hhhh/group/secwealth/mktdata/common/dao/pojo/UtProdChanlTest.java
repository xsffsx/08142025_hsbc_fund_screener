package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

class UtProdChanlTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new UtProdChanl().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new UtProdChanl().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new UtProdChanl());
    }

    @Test
    void testSetterAndGetter() {
        UtProdChanl obj = new UtProdChanl();
        obj.setUtProdChanlId(new UtProdChanlId());
        obj.setCountryCode("");
        obj.setGroupMember("");
        obj.setChannelComnCde("");
        obj.setChannelCde("");
        obj.setAllowBuyProdInd("");
        obj.setAllowSellProdInd("");
        obj.setUpdaetdOn(new Date());
        obj.setUpdatedBy("");

        Assertions.assertNotNull(obj.getUtProdChanlId());
        Assertions.assertNotNull(obj.getCountryCode());
        Assertions.assertNotNull(obj.getGroupMember());
        Assertions.assertNotNull(obj.getChannelComnCde());
        Assertions.assertNotNull(obj.getChannelCde());
        Assertions.assertNotNull(obj.getAllowBuyProdInd());
        Assertions.assertNotNull(obj.getAllowSellProdInd());
        Assertions.assertNotNull(obj.getUpdaetdOn());
        Assertions.assertNotNull(obj.getUpdatedBy());
    }
}
