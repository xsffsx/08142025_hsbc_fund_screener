package com.dummy.wpb.wpc.utils.model;

import org.junit.Assert;
import org.junit.Test;

public class MetadataTests {

    @Test
    public void propertyTest_DoesNotThrow() {
        try {
            PropertyUtils.setProperty(new Metadata());
        } catch (ClassNotFoundException e) {
            Assert.fail();
        }
    }

}
