/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;

/**
 *
 * <p>
 * <b> Date operation util class. </b>
 * </p>
 */
public final class DateUtil {

    public static final String ISO8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    public static final String DEFAULT_TIMEZONE_ID = "GMT";

    public static final String DATE_DAY_PATTERN = "yyyy-MM-dd";

    public static final String DATE_DAY_PATTERN_yyyyMMddHHmmss = "yyyyMMddHHmmss";

    public static final String DATE_HOUR_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final String DATE_HOUR_SIMPLE_PATTERN = "yyyy-MM-dd H:m:s";

    public static final String UTC_TIME_ZONE = "UTC";

    public static final int MINUTE_CYCLE = 60;
    public static final int SECOND_CYCLE = 60;
    public static final int MILLISECOND_CYCLE = 1000;

    private DateUtil() {}

    public static String convertDate(String dateStr, String scrFormat, String targetFormat) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(scrFormat, Locale.ENGLISH);
        Date date = simpleDateFormat.parse(dateStr);
        simpleDateFormat.applyPattern(targetFormat);

        return simpleDateFormat.format(date);
    }

    /**
     *
     * <p>
     * <b> Convert the date and time string to ISO8601 format(yyyy-MM-dd'T'HH:mm:ss.SSSXXX) with the given pattern and timezone. If
     * date is invalid, return "". Note: locale is Locale.ENGLISH, default timezone is GMT. </b>
     * </p>
     *
     * @param date
     * @param pattern
     * @param timezone
     * @param iso8601Timezone
     * @return
     * @throws ParseException
     */
    public static String convertToISO8601Format(final String date, final String pattern, final TimeZone timezone,
        final TimeZone iso8601Timezone) throws ParseException {
        String result = "";
        if (date != null && date.length() > 0) {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
            if (timezone != null) {
                formatter.setTimeZone(timezone);
            } else {
                formatter.setTimeZone(TimeZone.getTimeZone(DateUtil.DEFAULT_TIMEZONE_ID));
            }
            SimpleDateFormat iso8601Formatter = new SimpleDateFormat(DateUtil.ISO8601_PATTERN, Locale.ENGLISH);
            if (iso8601Timezone != null) {
                iso8601Formatter.setTimeZone(iso8601Timezone);
            } else {
                iso8601Formatter.setTimeZone(TimeZone.getTimeZone(DateUtil.DEFAULT_TIMEZONE_ID));
            }
            result = iso8601Formatter.format(formatter.parse(date));
        }
        return result;
    }


    public static String parseDateByTimezone(final Date date, final String timezone, final String formate) {
        SimpleDateFormat format = new SimpleDateFormat();
        if (date != null) {
            format = new SimpleDateFormat(formate);
            if (timezone != null && !"".equals(timezone.trim())) {
                format.setTimeZone(TimeZone.getTimeZone(timezone));
            }
            return format.format(date);
        } else {
            return null;
        }
    }

    public static Date parseString(final String dateString, final String pattern, final TimeZone timezone) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
        if (timezone != null) {
            formatter.setTimeZone(timezone);
        } else {
            formatter.setTimeZone(TimeZone.getTimeZone(DateUtil.DEFAULT_TIMEZONE_ID));
        }
        return formatter.parse(dateString);
    }

    /**
     *
     * <p>
     * <b> Get the current date and time string with the given pattern and timezone. Note: locale is Locale.ENGLISH, default
     * timezone is GMT. </b>
     * </p>
     *
     * @param pattern
     * @param timezone
     * @return
     */
    public static String current(final String pattern, final TimeZone timezone) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
        if (timezone != null) {
            formatter.setTimeZone(timezone);
        } else {
            formatter.setTimeZone(TimeZone.getTimeZone(DateUtil.DEFAULT_TIMEZONE_ID));
        }
        return formatter.format(new Date());
    }


    public static String afterMinutesOfCurrent(final String pattern, final TimeZone timezone, final int minutes) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
        setTimeZoneForDateFormatter(timezone, formatter);
        return formatter.format(addMinutesForCurrentDate(minutes));
    }

    public static String afterHoursOfCurrent(final String pattern, final TimeZone timezone, final int hours) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
        setTimeZoneForDateFormatter(timezone, formatter);
        return formatter.format(addHoursForCurrentDate(hours));
    }

    public static String afterDaysOfCurrent(final String pattern, final TimeZone timezone, final int days) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
        setTimeZoneForDateFormatter(timezone, formatter);
        return formatter.format(addDaysForCurrentDate(days));
    }

    private static void setTimeZoneForDateFormatter(final TimeZone timezone, final SimpleDateFormat formatter) {
        if (timezone != null) {
            formatter.setTimeZone(timezone);
        } else {
            formatter.setTimeZone(TimeZone.getTimeZone(DateUtil.DEFAULT_TIMEZONE_ID));
        }
    }


    public static Date addMinutesForCurrentDate(final int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }

    public static Date addHoursForCurrentDate(final int hours) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, hours);
        return cal.getTime();
    }

    public static Date addDaysForCurrentDate(final int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    public static String stringToISO8601DateString(final String dateStr, final String oldFormatPattern,
        final String newFormatPattern, final TimeZone timeZone) throws ParseException {
        return stringToISO8601DateString(dateStr, oldFormatPattern, newFormatPattern, timeZone, timeZone);
    }

    public static String stringToISO8601DateString(final String dateStr, final String oldFormatPattern,
        final String newFormatPattern, final TimeZone oldTimezone, final TimeZone newTimezone) throws ParseException {
        String date = null;
        if (dateStr != null && dateStr.length() > 0) {
            SimpleDateFormat newFormat = new SimpleDateFormat(newFormatPattern, Locale.ENGLISH);
            if (newTimezone != null) {
                newFormat.setTimeZone(newTimezone);
            }
            SimpleDateFormat oldFormat = new SimpleDateFormat(oldFormatPattern, Locale.ENGLISH);
            if (oldTimezone != null) {
                oldFormat.setTimeZone(oldTimezone);
            }
            Date tempDate = oldFormat.parse(dateStr);
            date = newFormat.format(tempDate);
        }
        if (date != null && date.length() > 0) {
            int length = date.length();
            StringBuilder sb = new StringBuilder(date);
            sb.insert(length - 2, SymbolConstant.SYMBOL_COLON);
            date = sb.toString();
        }
        return date;
    }


    public static String getTimeZoneName() {
        TimeZone timeZone = Calendar.getInstance().getTimeZone();
        return timeZone.getDisplayName(timeZone.inDaylightTime(new Date()), TimeZone.SHORT);
    }

    public static SimpleDateFormat getSimpleDateFormat(final String pattern, final TimeZone timeZone) {
        if (StringUtils.isBlank(pattern)) {
            throw new IllegalArgumentException("SimpleDateFormat parameter cannot be null!");
        }
        if (timeZone == null) {
            throw new IllegalArgumentException("Timezone parameter cannot be null!");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(timeZone);
        return sdf;
    }

    /**
     *
     * <p>
     * <b> TODO : get specify day. </b>
     * </p>
     *
     * @param date
     * @param days
     * @return
     */
    public static Date getDay(final Date date, final Integer days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + days);
        return calendar.getTime();
    }

    /**
     * <p>
     * <b> Format date object to String object by pattern. </b>
     * </p>
     *
     * @param pattern
     * @param date
     * @return
     */
    public static String formatToString(final String pattern, final Date date, final String... timeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        if (timeZone != null && timeZone.length > 0) {
            sdf.setTimeZone(TimeZone.getTimeZone(timeZone[0]));
        }
        return sdf.format(date);
    }

    public static void main(final String[] args) {

    }

    /**
     *
     * <p>
     * <b> TODO : 1:Monday 2:Tuesday 3:Wednesday 4:Thursday; 5:Friday 6:Saturday 0:Sunday </b>
     * </p>
     *
     * @param date
     *            (format yyyy-MM-dd)
     * @return
     */
    public static int getDayOfWeek(final String date) {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * calcute the new datetime by tiemzone
     *
     * @param dateStr
     * @param oldFormatPattern
     * @param newFormatPattern
     * @param oldTimezone
     * @param newTimezone
     * @return
     * @throws ParseException
     */
    public static String dateByTimeZone(final String dateStr, final String oldFormatPattern, final String newFormatPattern,
        final TimeZone oldTimezone, final TimeZone newTimezone) throws ParseException {
        String date = null;
        if (StringUtil.isValid(dateStr)) {
            SimpleDateFormat newFormat = new SimpleDateFormat(newFormatPattern, Locale.ENGLISH);
            if (newTimezone != null) {
                newFormat.setTimeZone(newTimezone);
            }

            SimpleDateFormat oldFormat = new SimpleDateFormat(oldFormatPattern, Locale.ENGLISH);
            if (oldTimezone != null) {
                oldFormat.setTimeZone(oldTimezone);
            }

            Date tempDate = oldFormat.parse(dateStr);
            date = newFormat.format(tempDate);
        }

        return date;
    }

}
