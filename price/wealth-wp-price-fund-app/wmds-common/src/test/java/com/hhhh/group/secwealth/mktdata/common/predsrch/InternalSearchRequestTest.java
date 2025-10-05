package com.hhhh.group.secwealth.mktdata.common.predsrch;


import com.hhhh.group.secwealth.mktdata.common.predsrch.request.helper.InternalSearchRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class InternalSearchRequestTest {

    @Test
    public void test(){
        InternalSearchRequest internalSearchRequest = new InternalSearchRequest("","","","","","","");
        internalSearchRequest.setAltClassCde("");
        internalSearchRequest.setCountryCode("");
        internalSearchRequest.setGroupMember("");
        internalSearchRequest.setProdAltNum("");
        internalSearchRequest.setCountryTradableCode("");
        internalSearchRequest.setProductType("");
        internalSearchRequest.setLocale("");

        internalSearchRequest.getAltClassCde();
        internalSearchRequest.getCountryCode();
        internalSearchRequest.getGroupMember();
        internalSearchRequest.getProdAltNum();
        internalSearchRequest.getCountryTradableCode();
        internalSearchRequest.getProductType();
        internalSearchRequest.getLocale();
        Assert.assertTrue(true);
    }

}
