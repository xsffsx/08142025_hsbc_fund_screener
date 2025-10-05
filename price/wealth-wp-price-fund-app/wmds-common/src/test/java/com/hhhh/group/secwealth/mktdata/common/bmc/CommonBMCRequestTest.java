package com.hhhh.group.secwealth.mktdata.common.bmc;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CommonBMCRequestTest {

    @Test
    public void common(){
        CommonBMCRequest commonBMCRequest = new CommonBMCRequest();
        commonBMCRequest.setCountryCode("");
        commonBMCRequest.setErrTrackerCode("");
        commonBMCRequest.setGroupMember("");
        commonBMCRequest.getCountryCode();
        commonBMCRequest.getErrTrackerCode();
        commonBMCRequest.getGroupMember();
        Assert.assertTrue(true);
    }

}
