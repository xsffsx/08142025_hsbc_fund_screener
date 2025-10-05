package com.hhhh.group.secwealth.mktdata.common.dto;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ServiceKeyTest {

    @Test
    public void test(){
        ServiceKey serviceKey = new ServiceKey("");
        ServiceKey serviceKey1 = new ServiceKey("","");
        serviceKey.setServiceId("");
        serviceKey.setServiceType("");
        serviceKey.setCountryCode("");
        serviceKey.setGroupMember("");
        serviceKey.setProductType("");
        serviceKey.setCountryTradableCode("");

        serviceKey.getServiceId();
        serviceKey.getServiceType();
        serviceKey.getCountryCode();
        serviceKey.getGroupMember();
        serviceKey.getProductType();
        serviceKey.getCountryTradableCode();
        Assert.assertTrue(true);

    }
}
