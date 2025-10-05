package com.dummy.wpb.wpc.utils.model;

import org.junit.Assert;
import org.junit.Test;

public class MongoIndexInfoTests {

    @Test
    public void propertyTest_DoesNotThrow() {
        try {
            PropertyUtils.setProperty(new MongoIndexInfo());
        } catch (ClassNotFoundException e) {
            Assert.fail();
        }
    }

}
