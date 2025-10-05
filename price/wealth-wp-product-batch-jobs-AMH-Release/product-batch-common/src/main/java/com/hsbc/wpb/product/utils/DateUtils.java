package com.dummy.wpb.product.utils;

import java.time.*;
import java.util.Date;

public class DateUtils {

    private DateUtils() {
    }

    /**
     * Prase LocalTime in specified zoneOffSet, and the format it in system default timezone.
     *
     * @param localTime  The time needed to convert.
     * @param zoneOffset The zoneOffset of time.
     * @return LocalTime
     */
    public static LocalTime toSystemLocalTime(LocalTime localTime, ZoneOffset zoneOffset) {
        if (null == localTime) {
            return null;
        }
        return OffsetTime.of(localTime, zoneOffset)
                .withOffsetSameInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))
                .toLocalTime();
    }

    public static LocalDateTime toSystemLocalDateTime(LocalDateTime localDateTime, ZoneOffset zoneOffset) {
        if (null == localDateTime) {
            return null;
        }
        return OffsetDateTime.of(localDateTime, zoneOffset)
                .withOffsetSameInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))
                .toLocalDateTime();
    }

    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneOffset.UTC).toLocalDate();
    }

}
