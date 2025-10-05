package com.dummy.wpb.wpc.utils.model;

import org.junit.Assert;
import org.junit.Test;

public class ConfigFileTests {

    @Test
    public void propertyTest_DoesNotThrow() {
        try {
            PropertyUtils.setProperty(new ConfigFile());
        } catch (ClassNotFoundException e) {
            Assert.fail();
        }
    }

}