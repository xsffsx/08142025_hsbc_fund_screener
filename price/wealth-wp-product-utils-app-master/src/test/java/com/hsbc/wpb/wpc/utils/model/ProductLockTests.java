package com.dummy.wpb.wpc.utils.model;

import org.junit.Assert;
import org.junit.Test;

public class productLockTests {

    @Test
    public void propertyTest_DoesNotThrow() {
        try {
            PropertyUtils.setProperty(new productLock());
        } catch (ClassNotFoundException e) {
            Assert.fail();
        }
    }

}
