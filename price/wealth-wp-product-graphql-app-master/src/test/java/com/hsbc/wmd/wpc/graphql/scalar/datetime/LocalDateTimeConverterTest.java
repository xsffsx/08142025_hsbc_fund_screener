package com.dummy.wmd.wpc.graphql.scalar.datetime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.time.*;
import java.time.format.DateTimeFormatter;

@RunWith(MockitoJUnitRunner.class)
public class LocalDateTimeConverterTest {

    private LocalDateTimeConverter localDateTimeConverter;

    @Before
    public void setUp() {
        localDateTimeConverter = new LocalDateTimeConverter(true, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Test
    public void testFormatDate_givenLocalDateAndFormatter_returnsStringDate() {
        String date = localDateTimeConverter.formatDate(LocalDate.now(), DateTimeFormatter.ISO_LOCAL_DATE);
        Assert.assertNotNull(date);
    }

    @Test
    public void testFormatDate_givenLocalDateTimeAndFormatter_returnsStringDate() {
        String date = localDateTimeConverter.formatDate(LocalDateTime.now(), DateTimeFormatter.ISO_LOCAL_DATE);
        Assert.assertNotNull(date);
    }

    @Test
    public void testParseDate_givenString_returnsLocalDateTime() {
        String date = "2023-08-28T15:37:29.227";
        LocalDateTime localDateTime = localDateTimeConverter.parseDate(date);
        Assert.assertNotNull(localDateTime);
    }

    @Test
    public void testParseDate_givenString_returnsLocalDateTime2() {
        String date = "2023-08-28";
        LocalDateTime localDateTime = localDateTimeConverter.parseDate(date);
        Assert.assertNotNull(localDateTime);
    }

    @Test
    public void testParseDate_givenString_returnsNull() {
        String date = "2023-08-32T15:37:29.227";
        LocalDateTime localDateTime = localDateTimeConverter.parseDate(date);
        Assert.assertNull(localDateTime);
    }

    @Test
    public void testFromUTC_givenLocalDateTime_returnsLocalDateTime() {
        try {
            Method fromUTC = localDateTimeConverter.getClass().getDeclaredMethod("fromUTC", LocalDateTime.class);
            fromUTC.setAccessible(true);
            LocalDateTime localDateTime = (LocalDateTime)fromUTC.invoke(localDateTimeConverter, LocalDateTime.now());
            Assert.assertNotNull(localDateTime);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testToUTC_givenLocalDateTime_returnsZonedDateTime() {
        try {
            Method toUTC = localDateTimeConverter.getClass().getDeclaredMethod("toUTC", LocalDateTime.class);
            toUTC.setAccessible(true);
            ZonedDateTime zonedDateTime = (ZonedDateTime)toUTC.invoke(localDateTimeConverter, LocalDateTime.now());
            Assert.assertNotNull(zonedDateTime);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testConvert_givenLocalDateTimeAndZoneId_returnsLocalDateTime() {
        try {
            ReflectionTestUtils.setField(localDateTimeConverter, "zoneConversionEnabled", false);
            Method convert = localDateTimeConverter.getClass().getDeclaredMethod("convert", LocalDateTime.class, ZoneId.class, ZoneId.class);
            convert.setAccessible(true);
            LocalDateTime localDateTime = (LocalDateTime)convert.invoke(localDateTimeConverter, LocalDateTime.now(), ZoneOffset.UTC, ZoneOffset.systemDefault());
            Assert.assertNotNull(localDateTime);
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
