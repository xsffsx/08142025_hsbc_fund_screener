package com.hhhh.group.secwealth.mktdata.common.util;

import org.junit.Assert;
import org.junit.Test;


public class PropertyUtilTest {

    @Test
    public void testAll() throws Exception {
        PropertyUtil propertyUtil = new PropertyUtil("/sda/sda/1.log");
        Assert.assertNull(propertyUtil.getProperty("key"));
        Assert.assertNotNull(propertyUtil.getProperty("key","val"));
        Assert.assertNull(propertyUtil.getProperty("key","key","val"));
    }
}
