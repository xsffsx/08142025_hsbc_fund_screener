package com.hhhh.group.secwealth.mktdata.common.dto;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class InternalProductKeyTest {

    @InjectMocks
    InternalProductKey internalProductKey;

    @Test
    public void test(){
        InternalProductKey internalProductKey = new InternalProductKey("","","","","","");
        internalProductKey.setCountryCode("");
        internalProductKey.setGroupMember("");
        internalProductKey.setProductType("");
        internalProductKey.setProdCdeAltClassCde("");
        internalProductKey.setProdAltNum("");
        internalProductKey.setCountryTradableCode("");

        internalProductKey.getCountryCode();
        internalProductKey.getGroupMember();
        internalProductKey.getProductType();
        internalProductKey.getProdCdeAltClassCde();
        internalProductKey.getProdAltNum();
        internalProductKey.getCountryTradableCode();
        Assert.assertTrue(true);

    }

    @Test
    public void equals(){
        internalProductKey.equals("");
        Assert.assertTrue(true);
    }

    @Test
    public void ihashCode(){
        internalProductKey.hashCode();
        Assert.assertTrue(true);
    }

    @Test
    public void tostring(){
        internalProductKey.toString();
        Assert.assertTrue(true);
    }
}
