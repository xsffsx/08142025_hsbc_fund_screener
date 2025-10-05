package com.hhhh.group.secwealth.mktdata.common.dao.pojo;
import java.util.Date;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UTProdSwitchoutId;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UTProdSwitchoutTest {

    @Test
    public void test(){
        UTProdSwitchout utProdSwitchout = new UTProdSwitchout();
        utProdSwitchout.setUtProdSwitchoutId(new UTProdSwitchoutId());
        utProdSwitchout.setUpdaetdOn(new Date());
        utProdSwitchout.setUpdatedBy("");

        utProdSwitchout.getUtProdSwitchoutId();
        utProdSwitchout.getUpdaetdOn();
        utProdSwitchout.getUpdatedBy();
        Assert.assertTrue(true);

    }
}
