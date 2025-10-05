package com.hhhh.group.secwealth.mktdata.predsrch.svc.util;


import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.assertNotNull;

public class DateUtilTest  {

    @Test
    public void testAddMinutesForCurrentDate(){
        assertNotNull(DateUtil.addMinutesForCurrentDate(11));
    }
    @Test
    public void testAfterMinutesOfCurrent(){
        assertNotNull(DateUtil.afterMinutesOfCurrent(DateUtil.YYYY_M_MDD_H_HMMSS, TimeZone.getDefault(),11));
    }

}
