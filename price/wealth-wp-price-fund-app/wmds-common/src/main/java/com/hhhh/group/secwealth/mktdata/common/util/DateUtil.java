/*
 */

package com.hhhh.group.secwealth.mktdata.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.time.FastDateFormat;

import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;

/**
 * A utility class to support date operation.
 */
public final class DateUtil {

    private DateUtil() {}

    /**
     * Gets the timestamp.
     * 
     * @param timeString
     *            the time string
     * 
     * @return the timestamp
     */
    @SuppressWarnings("deprecation")
    public static Timestamp getTimestampByString(final String timeString) {
        try {
            DateFormat df = new SimpleDateFormat(DateConstants.DateFormat_yyyyMMddHHmmss);
            Date date = df.parse(timeString);
            return new Timestamp(date.getYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(),
                date.getSeconds(), 0);
        } catch (Exception e) {
            LogUtil.error(DateUtil.class, "SystemException: DateUtil, getTimestampByString, param: " + timeString, e);
            return new Timestamp(110, 03, 01, 10, 10, 10, 0);
        }
    }

    /**
     * Gets the current month.
     * 
     * @param timezone
     *            the timezone
     * 
     * @return the current month
     */
    public static String getCurrentMonth(final Calendar calendar) {
        StringBuffer sb = new StringBuffer();
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month < 10) {
            sb.append("0");
        }
        sb.append(month);
        return sb.toString();
    }

    /**
     * Gets the current year.
     * 
     * @param timezone
     *            the timezone
     * 
     * @return the current year
     */
    public static String getCurrentYear(final Calendar calendar) {
        return Integer.toString(calendar.get(Calendar.YEAR));
    }

    public static Calendar getTodayDateByGMT() {
        Calendar todayDate = Calendar.getInstance(TimeZone.getTimeZone(DateConstants.TIMEZONE_GMT));
        todayDate.set(Calendar.MILLISECOND, 1);
        return todayDate;
    }

    public static Calendar getDateBefore(final int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now;
    }

    /**
     * BMC data format string
     * <p>
     * <b> Get Current Time with format sample 1019174150080 </b>
     * </p>
     * .
     * 
     * @return the bmc date format
     */
    public static String getBmcDateFormat() {
        // time pattern
        return getDataString(DateConstants.DateFormat_MMddHHmmssSSS);
    }

    public static String getDateString() {
        FastDateFormat df2 = FastDateFormat.getInstance(DateConstants.DateFormat_ddMMMyyyyHHmm_withSymbol,
            TimeZone.getTimeZone(DateConstants.TIMEZONE_GMT8));
        String dStr = df2.format(new Date().getTime());
        return dStr;
    }

    /**
     * Get Current Time with format sample 09:06:32.
     * 
     * @return time string
     */
    public static String getTime() {
        // time pattern
        return getDataString(DateConstants.DateFormat_HHmmss_withColon);
    }

    /**
     * Get Current Date with format sample 2007-06-11.
     * 
     * @return date string
     */
    public static String getDate() {
        // time pattern
        return getDataString(DateConstants.DateFormat_yyyyMMdd_withHyphen);
    }

    /**
     * <p>
     * <b> Common method for return current time with different date format
     * </b>
     * </p>
     * .
     * 
     * @param formatString
     *            the format string
     * 
     * @return the data string
     */
    public static String getDataString(final String formatString) {
        String dateString = null;
        if (StringUtil.isValid(formatString)) {
            FastDateFormat formatter = FastDateFormat.getInstance(formatString);
            long currentTime = System.currentTimeMillis();
            Date targetDate = new Date(currentTime);
            dateString = formatter.format(targetDate);
        }
        return dateString;
    }

    /**
     * Convert date format from java.util.Date e.g. formatString = "yyyyMMdd";
     * 
     * @param date
     *            input date
     * @param formatString
     *            format string
     * 
     * @return simple date
     */
    public static String getSimpleDateFormat(final Date date, final String formatString) {
        String dateString = null;
        if (StringUtil.isValid(formatString) && null != date) {
            FastDateFormat formatter = FastDateFormat.getInstance(formatString);
            dateString = formatter.format(date);
        }
        return dateString;
    }

    public static String getSimpleDateFormat(final Date date, final String formatString, final TimeZone timzone) {
        String dateString = null;
        if (StringUtil.isValid(formatString) && null != date && null != timzone) {
            FastDateFormat formatter = FastDateFormat.getInstance(formatString, timzone);
            dateString = formatter.format(date);
        }
        return dateString;
    }

    /**
     * Convert date format from java.util.Calendar e.g. formatString =
     * "yyyyMMdd";
     * 
     * @param date
     *            input Calendar
     * @param formatString
     *            format string
     * 
     * @return simple date
     */
    public static String getSimpleDateFormat(final Calendar date, final String formatString) {
        FastDateFormat formatter = FastDateFormat.getInstance(formatString);
        String dateString = formatter.format(date);

        return dateString;
    }

    /**
     * Returns a Calendar Object which represents the integer date input.
     * 
     * @param date
     *            the given date
     * 
     * @return Calendar Object that represents the given date
     */
    public static Calendar getCalendar(final int date) {
        GregorianCalendar calendarDate;

        // Get date information
        int[] dateInfo = getDateInfo(date);
        int year = dateInfo[0];
        int month = dateInfo[1];
        int day = dateInfo[2];

        calendarDate = new GregorianCalendar();
        calendarDate.set(year, --month, day, 0, 0, 0);

        return calendarDate;
    }

    /**
     * Gets Information of the date input with predefined format.
     * 
     * @param date
     *            the given date
     * 
     * @return integer array with int[0] for year, int[1] for month, int[2] for
     *         day
     */
    private static int[] getDateInfo(final int date) {
        int[] dateInfo = new int[3];

        // Year
        dateInfo[0] = date / 10000;
        // Month
        dateInfo[1] = (date % 10000) / 100;
        // Day
        dateInfo[2] = date % 100;

        return dateInfo;
    }

    /**
     * Set the time portion of a given date to zero. For example, this method
     * wile return 2008/01/01 00:00:00 if the given date is 2008/01/01
     * 11:11:11.
     * 
     * @param date
     *            the given date object
     * 
     * @return date object with the time portion being zero
     */
    public static Date getDateWithoutTime(final Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * Convert date and time from AS400 to timestamp. (1) in AS400 the day can
     * be -99999999, the time can be -999999, for this case excepcted it
     * returns null. (2) otherwise if the input param invalid, throw
     * SystemException. (3) assumption: if the days of the month should be only
     * 30, but input 31, it will not throw exception and add the remaind days
     * into next month.
     * 
     * @param dateInEightDigits
     *            the date in eight digits
     * @param timeInSixDigits
     *            the time in six digits
     * 
     * @return the timestamp
     */
    public static Timestamp convert(final int dateInEightDigits, final int timeInSixDigits) throws Exception {
        Date date = convertToDate(dateInEightDigits, timeInSixDigits);
        if (date == null) {
            return null;
        }

        return new Timestamp(date.getTime());
    }

    /**
     * Convert date and time from AS400 to date. (1) in AS400 the day can be
     * -99999999, the time can be -999999, for this case excepcted it returns
     * null. (2) otherwise if the input param invalid, throw SystemException.
     * (3) assumption: if the days of the month should be only 30, but input
     * 31, it will not throw exception and add the remaind days into next
     * month.
     * 
     * @param dateInEightDigits
     *            the date in eight digits
     * @param timeInSixDigits
     *            the time in six digits
     * 
     * @return the date
     */
    public static Date convertToDate(final int dateInEightDigits, final int timeInSixDigits) throws Exception {
        if (dateInEightDigits == -99999999 && timeInSixDigits == -999999) {
            return null;
        } else if (timeInSixDigits < 0 || timeInSixDigits > 240000 || dateInEightDigits < 10000) {
            throw new SystemException("The input parameters are invalid.");
        }

        Calendar calendar = Calendar.getInstance();
        int year = dateInEightDigits / 10000;
        int month = (dateInEightDigits % 10000) / 100;
        int day = dateInEightDigits % 100;
        int hour = timeInSixDigits / 10000;
        int minute = (timeInSixDigits % 10000) / 100;
        int second = timeInSixDigits % 100;

        if (month > 12 || day > 31 || hour > 24 || minute > 60 || second > 60) {
            throw new SystemException("The input parameters are invalid.");
        }

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // index of month begins from
        // 0
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0); // set default millisecond = 0

        return calendar.getTime();
    }

    /**
     * Gets the machine date time.
     * <p>
     * TIPS: Replace this method, so that you can control the machine datetime
     * in your debug env.
     * 
     * @return the machine date time
     */
    public static Date getMachineDateTime() {
        return new Date();
    }

    /**
     * Gets the machine date time.
     * <p>
     * TIPS: Replace this method, so that you can control the machine datetime
     * in your debug env.
     * 
     * @param zone
     *            the zone
     * @param aLocale
     *            the a locale
     * 
     * @return the machine date time in GregorianCalendar
     */
    public static GregorianCalendar getMachineCalendar(final TimeZone zone, final Locale aLocale) {
        if (zone != null && aLocale != null) {
            return new GregorianCalendar(zone, aLocale);
        } else if (zone != null) {
            return new GregorianCalendar(zone);
        } else if (aLocale != null) {
            return new GregorianCalendar(aLocale);
        } else {
            return new GregorianCalendar();
        }
    }

    /**
     * Append the current date time to a date.
     * 
     * @param date
     *            the date
     * 
     * @return the date
     */
    public static Date appendCurrentDateTime(final Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar rightNow = Calendar.getInstance();
        calendar.set(Calendar.AM_PM, rightNow.get(Calendar.AM_PM));
        calendar.set(Calendar.HOUR_OF_DAY, rightNow.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, rightNow.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, rightNow.get(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, rightNow.get(Calendar.MILLISECOND));
        return calendar.getTime();
    }

    /**
     * Append the current date time to a date.
     * 
     * @param date
     *            the date
     * @param timeInSixDigits
     *            the time in six digits
     * 
     * @return the date
     */
    public static Date appendTimeToDate(final Date date, final int timeInSixDigits) throws Exception {
        if (timeInSixDigits == -999999) {
            return null;
        } else if (timeInSixDigits < 0 || timeInSixDigits > 240000) {
            throw new SystemException("The input parameters are invalid.");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = timeInSixDigits / 10000;
        int minute = (timeInSixDigits % 10000) / 100;
        int second = timeInSixDigits % 100;

        if (hour > 24 || minute > 60 || second > 60) {
            throw new SystemException("The input parameters are invalid.");
        }

        if (hour > 11) {
            calendar.set(Calendar.AM_PM, Calendar.PM);
        } else {
            calendar.set(Calendar.AM_PM, Calendar.AM);
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0); // set default millisecond = 0

        return calendar.getTime();
    }

    /**
     * Gets the today date.
     * 
     * @param timezone
     *            the timezone
     * 
     * @return the today date
     */
    public static Calendar getTodayDate(final TimeZone timezone) {
        Calendar todayDate = Calendar.getInstance(timezone);
        todayDate.set(Calendar.MILLISECOND, 1);
        return todayDate;
    }

    /**
     * Gets the time.
     * 
     * @param todayDate
     *            the today date
     * @param timeString
     *            the time string
     * @param minuteoffset
     *            the minuteoffset
     * 
     * @return the time
     */
    public static Calendar getTime(final Calendar todayDate, final String timeString, final int minuteoffset) {
        Calendar newTime = (Calendar) todayDate.clone();
        int hour = Integer.parseInt(timeString.substring(0, 2));
        int minute = Integer.parseInt(timeString.substring(2, 4));
        int second = Integer.parseInt(timeString.substring(4, 6));

        newTime.set(Calendar.HOUR_OF_DAY, hour);
        newTime.set(Calendar.MINUTE, minute);
        newTime.set(Calendar.SECOND, second);
        newTime.set(Calendar.MILLISECOND, 0);

        newTime.add(Calendar.MINUTE, minuteoffset);

        return newTime;
    }


    public static Date string2Date(final String str) {
        if (str != null && !"".equals(str)) {
            return new Date(str);
        }
        return null;
    }

    public static String millis2DateString(final String millis, final String format) {
        String date = null;
        if (StringUtil.isValid(millis)) {
            SimpleDateFormat formater = new SimpleDateFormat(format);
            date = formater.format(new Date(new Long(millis)));
        }
        return date;
    }

    public static String newDate2String(final TimeZone timeZone, final String format) {
        SimpleDateFormat formater = new SimpleDateFormat(format);
        formater.setTimeZone(timeZone);
        String date = formater.format(new Date());
        return date;
    }

    public static String newDate2String(final String format) {
        SimpleDateFormat formater = new SimpleDateFormat(format);
        String date = formater.format(new Date());
        return date;
    }

    public static String string2FormatDateString(final Date date, final String format) throws ParseException {
        if (null != date) {
            SimpleDateFormat formater = new SimpleDateFormat(format);
            return formater.format(date);
        }
        return null;
    }

    public static String string2FormatDateString(final String dateStr, final String format) throws ParseException {
        if (StringUtil.isValid(dateStr)) {
            SimpleDateFormat formater = new SimpleDateFormat(format);
            return formater.format(dateStr);
        }
        return null;
    }

    public static String string2FormatDateString(final String dateStr, final String oldFormat, final String newFormat)
        throws ParseException {
        String date = null;
        if (StringUtil.isValid(dateStr)) {
            SimpleDateFormat formater = new SimpleDateFormat(newFormat);
            Date temp = new SimpleDateFormat(oldFormat, Locale.ENGLISH).parse(dateStr);
            date = formater.format(temp);
        }
        return date;
    }

    public static String dateToISO8601DateString(final Date date, final String newFormatPattern, final TimeZone newTimezone)
        throws ParseException {
        SimpleDateFormat oldFormat = new SimpleDateFormat(DateConstants.DateFormat_yyyyMMddHHmmss);
        return stringToISO8601DateString(oldFormat.format(date), DateConstants.DateFormat_yyyyMMddHHmmss, newFormatPattern,
            newTimezone);
    }

    public static String stringToISO8601DateString(final String dateStr, final String oldFormatPattern,
        final String newFormatPattern, final TimeZone timeZone) throws ParseException {

        return stringToISO8601DateString(dateStr, oldFormatPattern, newFormatPattern, timeZone, timeZone);
    }

    public static String stringToISO8601DateString(final String dateStr, final String oldFormatPattern,
        final String newFormatPattern) throws ParseException {

        return stringToISO8601DateString(dateStr, oldFormatPattern, newFormatPattern, null, null);
    }

    public static String stringToISO8601DateString(final String dateStr, final String oldFormatPattern,
        final String newFormatPattern, final TimeZone oldTimezone, final TimeZone newTimezone) throws ParseException {
        String date = null;
        if (StringUtil.isValid(dateStr)) {
            SimpleDateFormat newFormat = new SimpleDateFormat(newFormatPattern);
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
        if (StringUtil.isValid(date)) {
            int length = date.length();
            StringBuilder sb = new StringBuilder(date);
            sb.insert(length - 2, ":");
            date = sb.toString();
        }
        return date;
    }

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

    public static String convertToString(final Object value, final Object... params) {
        if (null != value) {
            String dateFormat = "yyyyMMdd";
            String timeZone = "GMT";
            Object convertVal = value;
            if (value instanceof XMLGregorianCalendar) {
                XMLGregorianCalendar val = (XMLGregorianCalendar) value;
                GregorianCalendar cal = val.toGregorianCalendar();
                convertVal = cal.getTime();
            }
            if ((convertVal instanceof java.util.Date) || (convertVal instanceof java.sql.Date)) {
                if (null != params) {
                    if (params.length > 0) {
                        String param_1 = (String) params[0];
                        if (StringUtil.isValid(param_1)) {
                            dateFormat = param_1;
                        }
                    }
                    if (params.length > 1) {
                        String param_2 = (String) params[1];
                        if (StringUtil.isValid(param_2)) {
                            timeZone = param_2;
                        }
                    }
                }
                FastDateFormat df = FastDateFormat.getInstance(dateFormat, TimeZone.getTimeZone(timeZone));

                return df.format(convertVal);
            } else {
                return String.valueOf(value);
            }
        }

        return null;
    }
    
    public static String convertObjectToString(final Object value) {
        if (null != value) {
            String dateFormat = "yyyy-MM-dd";
            Object convertVal = value;
            if (value instanceof XMLGregorianCalendar) {
                XMLGregorianCalendar val = (XMLGregorianCalendar) value;
                GregorianCalendar cal = val.toGregorianCalendar();
                convertVal = cal.getTime();
            }
            if ((convertVal instanceof java.util.Date) || (convertVal instanceof java.sql.Date)) {
                FastDateFormat df = FastDateFormat.getInstance(dateFormat);
                return df.format(convertVal);
            } else {
                return String.valueOf(value);
            }
        }

        return null;
    }
    
    public static String getDateTimeByTimeZone(final String formatter,final TimeZone timeZone,final Date date) {
        SimpleDateFormat dateFormater = new SimpleDateFormat(formatter);
        if (timeZone != null) {
            dateFormater.setTimeZone(timeZone);
        }
        if(null != date) {
            return dateFormater.format(date);
        }else {
            return null;
        }
    }
}
