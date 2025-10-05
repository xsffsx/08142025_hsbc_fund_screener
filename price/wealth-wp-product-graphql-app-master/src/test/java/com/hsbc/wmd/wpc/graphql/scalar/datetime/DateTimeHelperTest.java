package com.dummy.wmd.wpc.graphql.scalar.datetime;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class DateTimeHelperTest {

    @Test
    public void TestToISOString_givenLocalDateTime_returnsToString() {
        String dateTime = DateTimeHelper.toISOString(LocalDateTime.now());
        Assert.assertNotNull(dateTime);
    }

    @Test
    public void TestToISOString_givenLocalTime_returnsToString() {
        String time = DateTimeHelper.toISOString(LocalTime.now());
        Assert.assertNotNull(time);
    }

    @Test
    public void TestToISOString_givenDate_returnsToString() {
        Date dt = new Date();
        String date = DateTimeHelper.toISOString(dt);
        Assert.assertNotNull(date);
    }

    @Test
    public void TestToLocalDateTime_givenDate_returnsLocalDateTime() {
        Date dt = new Date();
        LocalDateTime localDateTime = DateTimeHelper.toLocalDateTime(dt);
        Assert.assertNotNull(localDateTime);
    }

    @Test
    public void TestToDate_givenLocalDateTime_returnsDate() {
        Date date = DateTimeHelper.toDate(LocalDateTime.now());
        Assert.assertNotNull(date);
    }

    @Test
    public void TestCreateDate_givenYearAndMonthAndDay_returnsDate() {
        Date date = DateTimeHelper.createDate(2023, 8, 25);
        Assert.assertNotNull(date);
    }

    @Test
    public void TestCreateDate_givenYearAndMonthAndDayAndHoursAndMinsAndSec_returnsDate() {
        Date date = DateTimeHelper.createDate(2023, 8, 25, 12,13,14);
        Assert.assertNotNull(date);
    }

    @Test
    public void TestCreateDate_givenYearAndMonthAndDayAndHoursAndMinsAndSecAndMillis_returnsDate() {
        Date date = DateTimeHelper.createDate(2023, 8, 25, 12,13,14, 1);
        Assert.assertNotNull(date);
    }
}
