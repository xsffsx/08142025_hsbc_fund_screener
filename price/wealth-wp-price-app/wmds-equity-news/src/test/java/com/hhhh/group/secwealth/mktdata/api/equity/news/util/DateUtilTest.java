package com.hhhh.group.secwealth.mktdata.api.equity.news.util;

import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.assertNotNull;

public class DateUtilTest {

    @Test
    public void testString2DateString() throws Exception{
        assertNotNull(DateUtil.string2DateString("2016-09-22T14:24:00.000+08:00&#x1C;", Constant.DATE_FORMAT_FOT_LABCI,Constant.DATE_FORMAT_FOT_LABCI, TimeZone.getTimeZone("1111"),TimeZone.getTimeZone("2222")));
    }

    @Test
    public void testString2DateString2() throws Exception{
        assertNotNull(DateUtil.string2DateString("2016/09/22", Constant.DATE_FORMAT_YYYY_MM_DD,Constant.DATE_FORMAT_YYYY_MM_DD, TimeZone.getTimeZone("1111"),TimeZone.getTimeZone("2222")));
    }
}
