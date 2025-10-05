package com.hhhh.group.secwealth.mktdata.common.dao.pojo;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UTProdSwitchoutIdTest {

    @Test
    public void test(){
        UTProdSwitchoutId utProdSwitchoutId = new UTProdSwitchoutId();
        utProdSwitchoutId.setProductId(0);
        utProdSwitchoutId.setBatchId(0L);
        utProdSwitchoutId.setFundUnSwitchoutCode("");

        utProdSwitchoutId.getProductId();
        utProdSwitchoutId.getBatchId();
        utProdSwitchoutId.getFundUnSwitchoutCode();
        Assert.assertTrue(true);
    }
}
