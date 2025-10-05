package com.dummy.wpb.wpc.utils.model;

import org.junit.Assert;
import org.junit.Test;

public class RowSnapshotTests {

    @Test
    public void propertyTest_DoesNotThrow() {
        try {
            PropertyUtils.setProperty(new RowSnapshot());
        } catch (ClassNotFoundException e) {
            Assert.fail();
        }
    }

}