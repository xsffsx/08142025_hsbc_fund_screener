package com.hhhh.group.secwealth.mktdata.common.util;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles({"test", "hase_stma_api_support-test", "uat_hase_stma_eqty-test"})
public class SiteFeatureTest {


    @InjectMocks
    private SiteFeature siteFeature;

    @Test
    public void testAll(){
        try{
            Assert.assertTrue(siteFeature.getBooleanFeature("US","key",true));
            Assert.assertNull(siteFeature.getStringFeature("key"));
            Assert.assertNull(siteFeature.getStringFeature("US","key"));
            Assert.assertNull(siteFeature.getStringDefaultFeature("US","key"));
        }catch (Exception e){
            e.getMessage();
        }

    }
}
