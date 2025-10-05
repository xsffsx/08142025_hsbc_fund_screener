package com.dummy.wpb.wpc.utils;

import org.junit.Assert;
import org.junit.Test;

public class AdminControlTests {

    @Test
    public void validateSecretKey() {
        boolean key = AdminControl.validateSecretKey("key");
        Assert.assertFalse(key);
    }
}
