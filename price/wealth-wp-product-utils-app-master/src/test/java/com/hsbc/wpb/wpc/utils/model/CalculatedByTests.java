package com.dummy.wpb.wpc.utils.model;

import org.junit.Assert;
import org.junit.Test;

public class CalculatedByTests {

    @Test
    public void propertyTest_DoesNotThrow() {
        try {
            PropertyUtils.setProperty(new CalculatedBy());
        } catch (ClassNotFoundException e) {
            Assert.fail();
        }
    }

}
