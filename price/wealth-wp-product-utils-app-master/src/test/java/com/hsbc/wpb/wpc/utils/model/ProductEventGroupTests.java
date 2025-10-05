package com.dummy.wpb.wpc.utils.model;

import org.junit.Assert;
import org.junit.Test;

public class ProductEventGroupTests {

    @Test
    public void propertyTest_DoesNotThrow() {
        try {
            PropertyUtils.setProperty(new ProductEventGroup());
        } catch (ClassNotFoundException e) {
            Assert.fail();
        }
    }

}
