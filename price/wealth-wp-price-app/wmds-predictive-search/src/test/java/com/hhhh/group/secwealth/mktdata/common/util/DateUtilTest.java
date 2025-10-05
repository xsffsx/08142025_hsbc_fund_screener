package com.hhhh.group.secwealth.mktdata.common.util;

import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertNotNull;


public class DateUtilTest {

    @Test
    public void testGetTimestampByString() {
        Timestamp timestamp = DateUtil.getTimestampByString("20210825174751");
        assertNotNull(timestamp);
    }

    @Test
    public void testGetTimestampByStringEX() {
        Boolean flag=true;
        try {
            DateUtil.getTimestampByString("1");
        } catch (Exception e) {
            flag=false;
        }
       org.junit.Assert.assertTrue(flag);
    }


    @Test
    public void testNull() {
        assertNotNull(Calendar.getInstance());
        assertNotNull(DateUtil.getCurrentYear(Calendar.getInstance()));
        assertNotNull(DateUtil.getTodayDateByGMT());
        assertNotNull(DateUtil.getDateBefore(2));
        assertNotNull(DateUtil.getBmcDateFormat());
        assertNotNull(DateUtil.getDateString());
        assertNotNull(DateUtil.getTime());
        assertNotNull(DateUtil.getDate());
        assertNotNull(DateUtil.getSimpleDateFormat(new Date(), DateConstants.DateFormat_yyyyMMddHHmmss));
        assertNotNull(DateUtil.getSimpleDateFormat(new Date(), DateConstants.DateFormat_yyyyMMddHHmmss, TimeZone.getDefault()));
        assertNotNull(DateUtil.getSimpleDateFormat(Calendar.getInstance(), DateConstants.DateFormat_yyyyMMddHHmmss));
        assertNotNull(DateUtil.getCalendar(1));
        assertNotNull(DateUtil.getDateWithoutTime(new Date()));
        assertNotNull(DateUtil.getTodayDate(TimeZone.getDefault()));

    }




    @Test
    public void testContinueNull () {
        try {
            assertNotNull(DateUtil.convert(10001,230000));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            assertNotNull(DateUtil.convertToDate(10001,230000));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(DateUtil.getMachineDateTime());
        assertNotNull(DateUtil.getMachineCalendar(TimeZone.getDefault(), Locale.CANADA));
        assertNotNull(DateUtil.appendCurrentDateTime(new Date()));
        try {
            assertNotNull(DateUtil.appendTimeToDate(new Date(),1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNotNull(DateUtil.getTime(Calendar.getInstance(),"115532523454235525435",1));
        assertNotNull(DateUtil.string2Date(new Date().toString()));
        assertNotNull(DateUtil.millis2DateString("2012",DateConstants.DateFormat_yyyyMMddHHmmss));

    }

    @Test
    public void testContinueDataNull () {
        assertNotNull(DateUtil.newDate2String(TimeZone.getDefault(),DateConstants.DateFormat_yyyyMMddHHmmss));
        assertNotNull(DateUtil.newDate2String(DateConstants.DateFormat_yyyyMMddHHmmss));
        try {
            assertNotNull(DateUtil.string2FormatDateString("20210825174751",DateConstants.DateFormat_yyyyMMddHHmmss,DateConstants.DateFormat_yyyyMMddHHmmss));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Boolean flag=false;
        try {
            assertNotNull(DateUtil.string2FormatDateString("20210825174751",DateConstants.DateFormat_yyyyMMddHHmmss));
        } catch (Exception e) {
            flag=true;
        }
       org.junit.Assert.assertTrue(flag);


        try {
            assertNotNull(DateUtil.string2FormatDateString(new Date(),DateConstants.DateFormat_yyyyMMddHHmmss));
            assertNotNull(DateUtil.dateToISO8601DateString(new Date(),DateConstants.DateFormat_yyyyMMddHHmmss,TimeZone.getDefault()));
            assertNotNull(DateUtil.stringToISO8601DateString("20210825174751",DateConstants.DateFormat_yyyyMMddHHmmss,DateConstants.DateFormat_yyyyMMddHHmmss,TimeZone.getDefault()));
            assertNotNull(DateUtil.stringToISO8601DateString("20210825174751",DateConstants.DateFormat_yyyyMMddHHmmss,DateConstants.DateFormat_yyyyMMddHHmmss));
            assertNotNull(DateUtil.stringToISO8601DateString("20210825174751",DateConstants.DateFormat_yyyyMMddHHmmss,DateConstants.DateFormat_yyyyMMddHHmmss,TimeZone.getDefault(),TimeZone.getDefault()));
            assertNotNull(DateUtil.dateByTimeZone("20210825174751",DateConstants.DateFormat_yyyyMMddHHmmss,DateConstants.DateFormat_yyyyMMddHHmmss,TimeZone.getDefault(),TimeZone.getDefault()));
            assertNotNull(DateUtil.convertToString("20210825174751",DateConstants.DateFormat_yyyyMMddHHmmss,DateConstants.DateFormat_yyyyMMddHHmmss,TimeZone.getDefault(),TimeZone.getDefault()));
            assertNotNull(DateUtil.convertObjectToString("20210825174751"));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertNotNull(DateUtil.getCurrentMonth(Calendar.getInstance()));
    }


}
