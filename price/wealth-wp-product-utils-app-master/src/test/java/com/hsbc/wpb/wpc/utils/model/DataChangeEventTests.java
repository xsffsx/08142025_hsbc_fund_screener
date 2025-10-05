package com.dummy.wpb.wpc.utils.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

public class DataChangeEventTests {

    private DataChangeEvent dataChangeEvent;

    @Before
    public void setUp() throws Exception {
        dataChangeEvent = new DataChangeEvent(1, new Timestamp(System.currentTimeMillis()), "tableName", "op", "rowId", "id", 1L);
    }

    @Test
    public void propertyTest_DoesNotThrow() {
        try {
            PropertyUtils.setProperty(new DataChangeEvent());
        } catch (ClassNotFoundException e) {
            Assert.fail();
        }
    }

    @Test
    public void testAsKey3_noArgs_returnsString() {
        String asKey3 = dataChangeEvent.asKey3();
        Assert.assertNotNull(asKey3);
    }
}
