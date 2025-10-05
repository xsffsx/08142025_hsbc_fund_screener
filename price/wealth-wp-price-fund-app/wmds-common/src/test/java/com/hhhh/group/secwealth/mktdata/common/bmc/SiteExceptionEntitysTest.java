package com.hhhh.group.secwealth.mktdata.common.bmc;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SiteExceptionEntitysTest {
    @Test
    public void test(){
        SiteExceptionEntitys siteExceptionEntitys= new SiteExceptionEntitys();
        siteExceptionEntitys.setBmcLogFilePath("");
        siteExceptionEntitys.setSiteEntityList(new ArrayList<SiteExceptionEntity>());
        siteExceptionEntitys.setSiteEntityMap(new ConcurrentHashMap<String, SiteExceptionEntity>());
        siteExceptionEntitys.getBmcFilePath("");
        siteExceptionEntitys.getSiteEntityMap();
        siteExceptionEntitys.getBmcLogFilePath();
        siteExceptionEntitys.getSiteEntityList();
        Assert.assertTrue(true);
    }
}
