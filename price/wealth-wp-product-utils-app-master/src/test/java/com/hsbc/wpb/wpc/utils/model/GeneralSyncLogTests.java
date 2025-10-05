package com.dummy.wpb.wpc.utils.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class GeneralSyncLogTests {

    private GeneralSyncLog generalSyncLog;

    @Before
    public void setUp() throws Exception {
        generalSyncLog = new GeneralSyncLog(new ArrayList<>(), new Date(), new Date(), new Date());
    }

    @Test
    public void propertyTest_DoesNotThrow() {
        try {
            PropertyUtils.setProperty(generalSyncLog);
        } catch (ClassNotFoundException e) {
            Assert.fail();
        }
    }
}
