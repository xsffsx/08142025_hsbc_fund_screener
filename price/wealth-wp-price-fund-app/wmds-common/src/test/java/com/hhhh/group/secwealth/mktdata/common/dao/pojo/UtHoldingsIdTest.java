package com.hhhh.group.secwealth.mktdata.common.dao.pojo;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UtHoldingsIdTest {

    @Test
    public void test(){

        UtHoldingsId utHoldingsId = new UtHoldingsId();
        utHoldingsId.setBatchId(1);
        utHoldingsId.setPerformanceId("");
        utHoldingsId.setHolderName("");
        utHoldingsId.setHoldingId(1);
        utHoldingsId.getHolderName();
        utHoldingsId.getHoldingId();
        utHoldingsId.getPerformanceId();
        utHoldingsId.getBatchId();
        Assert.assertTrue(true);
    }
}
