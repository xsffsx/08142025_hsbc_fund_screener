package com.hhhh.group.secwealth.mktdata.api.equity.chart.utils;

import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
//import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

public class ChartDateUtilsTest {

    @Test
    public void testFormat() {
        assertNotNull(ChartDateUtils.format(new Date(), Constant.DATE_FORMAT_FOT_LABCI));
    }

    @Test
    public void testParse() {
        assertNotNull(ChartDateUtils.parse("2021-07-13T15:24:35", Constant.DATE_FORMAT_FOT_LABCI).toString());
    }

    @Test
    public void testErrorParse() {
        try {
            assertNotNull(ChartDateUtils.parse("2021-07-13", Constant.DATE_FORMAT_FOT_LABCI).toString());
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
