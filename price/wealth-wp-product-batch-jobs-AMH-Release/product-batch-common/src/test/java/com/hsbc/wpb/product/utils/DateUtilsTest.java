package com.dummy.wpb.product.utils;

import org.junit.Assert;
import org.junit.Test;

import java.time.*;

public class DateUtilsTest {

    @Test
    public void toSystemLocalTimeTest() {
        LocalTime localTime1 = LocalTime.now();
        ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
        LocalTime localTime2 = DateUtils.toSystemLocalTime(localTime1, zoneOffset);
        Assert.assertEquals(localTime1, localTime2);
        Assert.assertNull(DateUtils.toSystemLocalTime(null, zoneOffset));
    }

    @Test
    public void toSystemLocalDateTimeTest() {
        LocalDateTime localDateTime1 = LocalDateTime.now();
        ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
        LocalDateTime localDateTime2 = DateUtils.toSystemLocalDateTime(localDateTime1, zoneOffset);
        Assert.assertEquals(localDateTime1, localDateTime2);
        Assert.assertNull(DateUtils.toSystemLocalDateTime(null, zoneOffset));
    }
}
