package com.hhhh.group.secwealth.mktdata.api.equity.quotes.util;

import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertNotNull;

public class DateUtilTest {

    @Test
    public void testGetCurrentMonth(){
        assertNotNull(DateUtil.getCurrentMonth(Calendar.getInstance()));
    }

    @Test
    public void testGetCurrentYear(){
        assertNotNull(DateUtil.getCurrentYear(Calendar.getInstance()));
    }

    @Test
    public void testGetCalendar(){
        assertNotNull(DateUtil.getCalendar(2));
    }

    @Test
    public void testGetDateBefore(){
        assertNotNull(DateUtil.getDateBefore(2));
    }

    @Test
    public void testGetDateWithoutTime(){
        assertNotNull(DateUtil.getDateWithoutTime(new Date()));
    }

    @Test
    public void testConvert(){
        assertNotNull(DateUtil.convert(20000,10000));
    }

    @Test
    public void testConvert2(){
        try {
            assertNotNull(DateUtil.convert(-99999999,-99999999));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testConvert3(){
        try {
            assertNotNull(DateUtil.convert(-99999999,10000));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testGetMachineDateTime(){
        assertNotNull(DateUtil.getMachineDateTime());
    }

    @Test
    public void testGetMachineCalendar(){
        assertNotNull(DateUtil.getMachineCalendar(TimeZone.getTimeZone("1111"), Locale.getDefault()));
    }

    @Test
    public void testGetMachineCalendar2(){
        assertNotNull(DateUtil.getMachineCalendar(TimeZone.getTimeZone("1111"),null));
    }

    @Test
    public void testGetMachineCalendar3(){
        assertNotNull(DateUtil.getMachineCalendar(null,Locale.getDefault()));
    }

    @Test
    public void testGetMachineCalendar4(){
        assertNotNull(DateUtil.getMachineCalendar(null,null));
    }

    @Test
    public void testAppendCurrentDateTime(){
        assertNotNull(DateUtil.appendCurrentDateTime(new Date()));
    }

    @Test
    public void testAppendTimeToDate(){
        assertNotNull(DateUtil.appendTimeToDate(new Date(),10000));
    }

    @Test
    public void testGetTodayDate(){
        assertNotNull(DateUtil.getTodayDate(TimeZone.getTimeZone("1111")));
    }

    @Test
    public void testGetTime(){
        assertNotNull(DateUtil.getTime(Calendar.getInstance(),"2020-08-24 23:00:00",2));
    }

    @Test
    public void testAfterMinutesOfCurrent(){
        assertNotNull(DateUtil.afterMinutesOfCurrent(Constant.DATE_FORMAT_FOT_LABCI,TimeZone.getTimeZone("1111"),2));
    }

    @Test
    public void testMillis2DateString(){
        assertNotNull(DateUtil.millis2DateString(2021-07-13L, Constant.DATE_FORMAT_FOT_LABCI));
    }

    @Test
    public void testGetDurationBreakdown(){
        assertNotNull(DateUtil.getDurationBreakdown(72003638));
    }
}
