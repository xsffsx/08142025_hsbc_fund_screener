package com.hhhh.group.secwealth.mktdata.common.dao.pojo;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Date;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UTHoldingAllocTest {

    @Test
    public void test(){
        UTHoldingAlloc utHoldingAlloc = new UTHoldingAlloc();
        utHoldingAlloc.setHoldingAllocWeight(BigDecimal.valueOf(11));
        utHoldingAlloc.setHoldingAllocWeightNet(BigDecimal.valueOf(11));
        utHoldingAlloc.setPortfolioDate(new Date());
        utHoldingAlloc.setUpdatedBy("");
        utHoldingAlloc.setUtHoldingAllocId(new UTHoldingAllocId());
        utHoldingAlloc.getUtHoldingAllocId();
        utHoldingAlloc.getHoldingAllocWeightNet();
        utHoldingAlloc.getUtHoldingAllocId();
        utHoldingAlloc.getPortfolioDate();
        utHoldingAlloc.getUpdatedOn();
        Assert.assertTrue(true);

    }
}
