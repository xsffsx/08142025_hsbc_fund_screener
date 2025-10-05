package com.dummy.wpb.product.helper;

import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class DateHelperTest {

    @Test
    public void getCurrentDate() {
        assertEquals(DateHelper.getCurrentDate(), new java.sql.Date(new GregorianCalendar().getTime().getTime()));
    }

    @Test
    public void getCalendar() {
        GregorianCalendar calendar = DateHelper.getCalendar();
        GregorianCalendar expectedCalendar = new GregorianCalendar();
        expectedCalendar.set(Calendar.HOUR_OF_DAY, 0);
        expectedCalendar.set(Calendar.MINUTE, 0);
        expectedCalendar.set(Calendar.SECOND, 0);
        expectedCalendar.set(Calendar.MILLISECOND, 0);

        assertEquals(expectedCalendar, calendar);
    }

    @Test
    public void getLocalDateTimeByDate_GivenNullDateAndTime() {
        LocalDateTime localDateTime = DateHelper.getLocalDateTimeByDate(null, null);
        assertNull(localDateTime);
    }

    @Test
    public void getLocalDateTimeByDate_GivenValidDateAndNullTime() {
        Date date = new Date();
        Date time = null;
        LocalDateTime localDateTime = DateHelper.getLocalDateTimeByDate(date, time);
        assertEquals(new Timestamp(date.getTime()).toLocalDateTime(), localDateTime);
    }

    @Test
    public void getLocalDateTimeByDate_GivenNullDateAndValidTime() {
        Date time = new Date();
        Date date = null;
        LocalDateTime localDateTime = DateHelper.getLocalDateTimeByDate(date, time);
        assertNull(localDateTime);
    }

    @Test
    public void getLocalDateTimeByDate_GivenValidDateAndTime() {
        Date time = new Date();
        Date date = new Date();
        LocalDateTime localDateTime = DateHelper.getLocalDateTimeByDate(date, time);
        assertNull(localDateTime);
    }

    @Test
    public void formatDate2String() {
        Date date = new Date();
        assertEquals(DateHelper.formatDate2String(date, DateHelper.DEFAULT_DATE_FORMAT), DateFormatUtils.format(date, DateHelper.DEFAULT_DATE_FORMAT));
    }

    @Test
    public void formatDate2String_GivenNullFormat() {
        Date date = new Date();
        assertNull(DateHelper.formatDate2String(date, ""));
    }

    @Test
    public void formatDate2String_GivenNullDate() {
        assertNull(DateHelper.formatDate2String(null, DateHelper.DEFAULT_DATE_FORMAT));
    }
}