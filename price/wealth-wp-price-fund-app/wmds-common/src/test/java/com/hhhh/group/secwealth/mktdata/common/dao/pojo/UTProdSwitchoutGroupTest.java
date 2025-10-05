package com.hhhh.group.secwealth.mktdata.common.dao.pojo;
import java.util.Date;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UTProdSwitchoutGroupId;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UTProdSwitchoutGroupTest {

    @Test
    public void  test(){
        UTProdSwitchoutGroup utProdSwitchoutGroup = new UTProdSwitchoutGroup();
        utProdSwitchoutGroup.setUtProdSwitchoutGroupId(new UTProdSwitchoutGroupId());
        utProdSwitchoutGroup.setUpdaetdOn(new Date());
        utProdSwitchoutGroup.setUpdatedBy("");

        utProdSwitchoutGroup.getUtProdSwitchoutGroupId();
        utProdSwitchoutGroup.getUpdaetdOn();
        utProdSwitchoutGroup.getUpdatedBy();
        Assert.assertTrue(true);

    }
}
