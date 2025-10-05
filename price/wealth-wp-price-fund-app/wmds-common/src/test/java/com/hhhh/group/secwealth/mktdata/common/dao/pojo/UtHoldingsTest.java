package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Date;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UtHoldingsTest {

    @Test
    public void test(){
        UtHoldings utHoldings = new UtHoldings();
        utHoldings.setUpdatedBy("");
        utHoldings.setUpdatedOn(new Date());
        utHoldings.setUtHoldingsId(utHoldings.getUtHoldingsId());
        utHoldings.setWeight(BigDecimal.valueOf(1));
        utHoldings.getWeight();
        utHoldings.getUpdatedBy();
        utHoldings.getUpdatedOn();
        Assert.assertTrue(true);
    }
}
