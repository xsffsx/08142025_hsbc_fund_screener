package com.hhhh.group.secwealth.mktdata.common.dao.pojo;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UTProdSwitchoutGroupIdTest {

    @Test
    public void test(){
        UTProdSwitchoutGroupId utProdSwitchoutGroupId = new UTProdSwitchoutGroupId();
        utProdSwitchoutGroupId.setProductId(0);
        utProdSwitchoutGroupId.setBatchId(0L);
        utProdSwitchoutGroupId.setSwitchTableGroup("");

        utProdSwitchoutGroupId.getProductId();
        utProdSwitchoutGroupId.getBatchId();
        utProdSwitchoutGroupId.getSwitchTableGroup();
        Assert.assertTrue(true);
    }
}
