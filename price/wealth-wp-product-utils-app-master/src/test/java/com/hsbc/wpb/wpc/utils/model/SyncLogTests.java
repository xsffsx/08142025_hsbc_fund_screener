package com.dummy.wpb.wpc.utils.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class SyncLogTests {

    @Test
    public void propertyTest_DoesNotThrow() {
        try {
            PropertyUtils.setProperty(new SyncLog());
        } catch (ClassNotFoundException e) {
            Assert.fail();
        }
    }

    @Test
    public void testSetSyncEndTime_givenDate_doesNotThrow() {
        try {
            SyncLog syncLog = new SyncLog();
            syncLog.setSyncStartTime(new Date());
            syncLog.setSyncEndTime(new Date());
        } catch (Exception e) {
            Assert.fail();
        }
    }

}