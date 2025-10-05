package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UtReturnsIdTest {

    @Test
    public void test(){
        UtReturnsId utReturnsId = new UtReturnsId();
        utReturnsId.setBatchId(0L);
        utReturnsId.setPerformanceId("");
        utReturnsId.setFundReturnTypeCode("");

        utReturnsId.getBatchId();
        utReturnsId.getPerformanceId();
        utReturnsId.getFundReturnTypeCode();
        Assert.assertTrue(true);

    }
}
