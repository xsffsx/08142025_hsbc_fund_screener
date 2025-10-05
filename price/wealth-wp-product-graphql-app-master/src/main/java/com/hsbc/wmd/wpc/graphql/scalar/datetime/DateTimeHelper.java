package com.dummy.wmd.wpc.graphql.scalar.datetime;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Alexey Zhokhov
 */
final class DateTimeHelper {

    private DateTimeHelper() {
    }

    // ISO_8601
    public static String toISOString(LocalDateTime dateTime) {
        Objects.requireNonNull(dateTime, "dateTime");

        return DateTimeFormatter.ISO_INSTANT.format(ZonedDateTime.of(dateTime, ZoneOffset.UTC));
    }

    public static String toISOString(LocalTime time) {
        Objects.requireNonNull(time, "time");

        return DateTimeFormatter.ISO_LOCAL_TIME.format(time);
    }

    public static String toISOString(Date date) {
        Objects.requireNonNull(date, "date");

        return toISOString(toLocalDateTime(date));
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        Objects.requireNonNull(date, "date");

        return date.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
    }

    public static Date toDate(LocalDateTime dateTime) {
        Objects.requireNonNull(dateTime, "dateTime");

        return Date.from(dateTime.atZone(ZoneOffset.UTC).toInstant());
    }

    public static Date createDate(int year, int month, int day) {
        return createDate(year, month, day, 0, 0, 0, 0);
    }

    public static Date createDate(int year, int month, int day, int hours, int min, int sec) {
        return createDate(year, month, day, hours, min, sec, 0);
    }

    public static Date createDate(int year, int month, int day, int hours, int min, int sec, int millis) {
        long nanos = TimeUnit.MILLISECONDS.toNanos(millis);
        LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hours, min, sec, (int) nanos);
        return DateTimeHelper.toDate(localDateTime);
    }

}
