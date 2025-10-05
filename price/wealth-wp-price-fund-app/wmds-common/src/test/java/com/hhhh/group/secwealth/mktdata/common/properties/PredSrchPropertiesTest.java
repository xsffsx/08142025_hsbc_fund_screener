package com.hhhh.group.secwealth.mktdata.common.properties;
import com.hhhh.group.secwealth.mktdata.common.predsrch.request.PredSrchRequest;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class PredSrchPropertiesTest {

    @Test
    public void test(){
        PredSrchProperties predSrchProperties = new PredSrchProperties();
        predSrchProperties.setUrl("");
        predSrchProperties.setBodyPrefix("");
        predSrchProperties.setRequestParams(new PredSrchRequest());
        predSrchProperties.setInternalUrl("");

        predSrchProperties.getUrl();
        predSrchProperties.getBodyPrefix();
        predSrchProperties.getRequestParams();
        predSrchProperties.getInternalUrl();

        Assert.assertTrue(true);


    }
}
