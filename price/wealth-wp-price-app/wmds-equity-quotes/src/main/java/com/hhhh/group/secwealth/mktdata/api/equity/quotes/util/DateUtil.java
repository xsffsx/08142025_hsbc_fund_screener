/*
 */

package com.hhhh.group.secwealth.mktdata.api.equity.quotes.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateUtil {

	public static final String DEFAULT_TIMEZONE_ID = "GMT";
	public static final String YYYY_M_MDD_H_HMMSS = "yyyyMMddHHmmss";

	public static String getCurrentMonth(final Calendar calendar) {
		StringBuffer sb = new StringBuffer();
		int month = calendar.get(Calendar.MONTH) + 1;
		if (month < 10) {
			sb.append("0");
		}
		sb.append(month);
		return sb.toString();
	}

	public static String getCurrentYear(final Calendar calendar) {
		return Integer.toString(calendar.get(Calendar.YEAR));
	}

	public static Calendar getDateBefore(final int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now;
	}

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
	 * @return integer array with int[0] for year, int[1] for month, int[2] for day
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
	 * Set the time portion of a given date to zero. For example, this method wile
	 * return 2008/01/01 00:00:00 if the given date is 2008/01/01 11:11:11.
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
	 * Convert date and time from AS400 to timestamp. (1) in AS400 the day can be
	 * -99999999, the time can be -999999, for this case excepcted it returns null.
	 * (2) otherwise if the input param invalid, throw SystemException. (3)
	 * assumption: if the days of the month should be only 30, but input 31, it will
	 * not throw exception and add the remaind days into next month.
	 * 
	 * @param dateInEightDigits
	 *            the date in eight digits
	 * @param timeInSixDigits
	 *            the time in six digits
	 * 
	 * @return the timestamp
	 */
	public static Timestamp convert(final int dateInEightDigits, final int timeInSixDigits) {
		Date date = convertToDate(dateInEightDigits, timeInSixDigits);
		if (date == null) {
			return null;
		}

		return new Timestamp(date.getTime());
	}

	/**
	 * Convert date and time from AS400 to date. (1) in AS400 the day can be
	 * -99999999, the time can be -999999, for this case excepcted it returns null.
	 * (2) otherwise if the input param invalid, throw SystemException. (3)
	 * assumption: if the days of the month should be only 30, but input 31, it will
	 * not throw exception and add the remaind days into next month.
	 * 
	 * @param dateInEightDigits
	 *            the date in eight digits
	 * @param timeInSixDigits
	 *            the time in six digits
	 * 
	 * @return the date
	 */
	public static Date convertToDate(final int dateInEightDigits, final int timeInSixDigits) {
		if (dateInEightDigits == -99999999 && timeInSixDigits == -999999) {
			return null;
		} else if (timeInSixDigits < 0 || timeInSixDigits > 240000 || dateInEightDigits < 10000) {
			throw new RuntimeException("The input parameters are invalid.");
		}

		Calendar calendar = Calendar.getInstance();
		int year = dateInEightDigits / 10000;
		int month = (dateInEightDigits % 10000) / 100;
		int day = dateInEightDigits % 100;
		int hour = timeInSixDigits / 10000;
		int minute = (timeInSixDigits % 10000) / 100;
		int second = timeInSixDigits % 100;

		if (month > 12 || day > 31 || hour > 24 || minute > 60 || second > 60) {
			throw new RuntimeException("The input parameters are invalid.");
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
	 * TIPS: Replace this method, so that you can control the machine datetime in
	 * your debug env.
	 * 
	 * @return the machine date time
	 */
	public static Date getMachineDateTime() {
		return new Date();
	}

	/**
	 * Gets the machine date time.
	 * <p>
	 * TIPS: Replace this method, so that you can control the machine datetime in
	 * your debug env.
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
	public static Date appendTimeToDate(final Date date, final int timeInSixDigits) {
		if (timeInSixDigits == -999999) {
			return null;
		} else if (timeInSixDigits < 0 || timeInSixDigits > 240000) {
			throw new RuntimeException("The input parameters are invalid.");
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int hour = timeInSixDigits / 10000;
		int minute = (timeInSixDigits % 10000) / 100;
		int second = timeInSixDigits % 100;

		if (hour > 24 || minute > 60 || second > 60) {
			throw new RuntimeException("The input parameters are invalid.");
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

	public static String afterMinutesOfCurrent(final String pattern, final TimeZone timezone, final int minutes) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
		setTimeZoneForDateFormatter(timezone, formatter);
		return formatter.format(addMinutesForCurrentDate(minutes));
	}

	public static Date addMinutesForCurrentDate(final int minutes) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, minutes);
		return cal.getTime();
	}

	private static void setTimeZoneForDateFormatter(final TimeZone timezone, final SimpleDateFormat formatter) {
		if (timezone != null) {
			formatter.setTimeZone(timezone);
		} else {
			formatter.setTimeZone(TimeZone.getTimeZone(DateUtil.DEFAULT_TIMEZONE_ID));
		}
	}

	public static String millis2DateString(Long millis, String format) {
		String date = null;
		if (millis != null) {
			SimpleDateFormat formater = new SimpleDateFormat(format);
			date = formater.format(new Date(millis));
		}
		return date;
	}

	public static String getDurationBreakdown(long millis) {
		if(millis < 0) {
			throw new IllegalArgumentException("Duration must be greater than zero!");
		}

		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
		millis -= TimeUnit.SECONDS.toMillis(seconds);

		StringBuilder sb = new StringBuilder(64);
		sb.append(hours);
		sb.append(":");
		sb.append(minutes);
		sb.append(":");
		sb.append(seconds);
		sb.append(".");
		sb.append(millis);

		return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
	}
}
