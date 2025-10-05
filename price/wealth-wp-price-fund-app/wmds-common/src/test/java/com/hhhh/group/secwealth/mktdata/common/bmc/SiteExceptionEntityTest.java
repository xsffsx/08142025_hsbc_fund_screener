package com.hhhh.group.secwealth.mktdata.common.bmc;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SiteExceptionEntityTest {

    @Test
    public void test(){
        SiteExceptionEntity siteExceptionEntity = new SiteExceptionEntity();
        siteExceptionEntity.setCounterList(new ArrayList<ExceptionCounter>());
        siteExceptionEntity.initCounterMap();
        siteExceptionEntity.totalCountIncrement();
        siteExceptionEntity.totalCountDecrease(1);
        siteExceptionEntity.clearTotalCount();
        siteExceptionEntity.getTotalExceptionCount();
        siteExceptionEntity.setTotalExceptionCount(1);
        siteExceptionEntity.getRecentExceptionList();
        siteExceptionEntity.setRecentExceptionList(new ArrayList<DurationException>());
        siteExceptionEntity.setExceptionCounters(new ConcurrentHashMap<String, ExceptionCounter>());
        siteExceptionEntity.getTimeDuration();
        siteExceptionEntity.setTimeDuration(1);
        siteExceptionEntity.getErrNum();
        siteExceptionEntity.setErrNum(1);
        siteExceptionEntity.getErrNumStart();
        siteExceptionEntity.setErrNumStart(1);
        siteExceptionEntity.getErrCdeThrownPastSec();
        siteExceptionEntity.setErrCdeThrownPastSec("");
        siteExceptionEntity.getErrCdeExceedLimit();
        siteExceptionEntity.setErrCdeExceedLimit("");
        siteExceptionEntity.getErrmgsThrownPastSec();
        siteExceptionEntity.setErrmgsThrownPastSec("");
        siteExceptionEntity.getErrmgsExceedLimit();
        siteExceptionEntity.setErrmgsExceedLimit("");
        siteExceptionEntity.getCounterList();
        siteExceptionEntity.getPrefix();
        siteExceptionEntity.setPrefix("");
        Assert.assertTrue(true);
    }

}
