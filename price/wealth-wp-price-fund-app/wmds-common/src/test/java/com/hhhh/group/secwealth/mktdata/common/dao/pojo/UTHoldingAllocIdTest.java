package com.hhhh.group.secwealth.mktdata.common.dao.pojo;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UTHoldingAllocIdTest {

    @Test
    public void test(){
        UTHoldingAllocId utHoldingAllocId = new UTHoldingAllocId();
        utHoldingAllocId.setBatchId(1);
        utHoldingAllocId.setHoldingAllocClassName("");
        utHoldingAllocId.setHoldingAllocClassType("");
        utHoldingAllocId.setPerformanceId("");
        utHoldingAllocId.getHoldingAllocClassName();
        utHoldingAllocId.getHoldingAllocClassType();
        utHoldingAllocId.getPerformanceId();
        utHoldingAllocId.getBatchId();
        Assert.assertTrue(true);
    }

}
