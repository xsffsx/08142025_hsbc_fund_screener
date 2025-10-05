package com.dummy.wpb.wpc.utils.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class DifferenceTests {

    private Difference difference;

    @Before
    public void setUp() throws Exception {
        difference = new Difference("path", new Object(), new Object());
    }

    @Test
    public void propertyTest_DoesNotThrow() {
        try {
            PropertyUtils.setProperty(new Difference());
        } catch (ClassNotFoundException e) {
            Assert.fail();
        }
    }

    @Test
    public void testToMap_givenStirngAndString_returnsMap() {
        Map<String, Object> map = difference.toMap("leftKey", "rightKey");
        Assert.assertNotNull(map);
    }
}
