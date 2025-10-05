package com.dummy.wpc.datadaptor.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.log4j.Logger;

public class DateHelper {
	private static Logger log = Logger.getLogger(DateHelper.class);
	/** The Constant DEFAULT_DATETIME_FORMAT. */
	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static final String EXCEL_DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

	/** The Constant DEFAULT_DATE_FORMAT. */
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	
	public static final String EXCEL_DATE_FORMAT = "dd/MM/yyyy";

	public static final String DATE_SHORT_FORMAT = "ddMMMyyyy";
	public static final String DATE_SHORT_FORMAT_WITH_NUM_MONTH = "yyyyMMdd";
	public static final String DATE_TIME_SHORT_FORMAT = "ddMMMyyyyHHmm";
	public static final String DATE_TIME_DTHHMM_FORMAT = "yyyyMMddHHmm";

	public static final FastDateFormat defaultDateFormat = FastDateFormat.getInstance(DEFAULT_DATE_FORMAT);
	public static final FastDateFormat defaultDateTimeFormat = FastDateFormat.getInstance(DEFAULT_DATETIME_FORMAT);
	public static final SimpleDateFormat excelDateTimeFormat = new SimpleDateFormat(EXCEL_DATETIME_FORMAT);
	
	
	public static Timestamp getCurrentDateTime() {
		Date date = getCurrentDate();
		return new Timestamp(date.getTime());
	}

	public static Date getCurrentDate() {
		return new Date(new GregorianCalendar().getTime().getTime());
	}

	public static String getCurrentDateDefaultStr() {
		return formatDate2String(getCurrentDate(), DEFAULT_DATETIME_FORMAT);
	}

	public static String formatDate2String(Date date, String format) {
		if (null == date || StringUtils.isBlank(format)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static Date parseToDate(String dateStr, String format) {
		if (StringUtils.isBlank(dateStr) || StringUtils.containsOnly(dateStr, "0") || StringUtils.isBlank(format)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return new Date(sdf.parse(dateStr).getTime());
		} catch (ParseException e) {
			return null;
		}
	}

	public static String convertTimeZoneToGMTString(final TimeZone timeZone) {
		int timeZoneHour = timeZone.getOffset(DateHelper.getCurrentDate().getTime()) / 3600000;

		if (timeZoneHour >= 0) {
			return "GMT+".concat(String.valueOf(timeZoneHour));
		} else {
			timeZoneHour = timeZoneHour * (-1);
			return "GMT-".concat(String.valueOf(timeZoneHour));
		}
	}

	public static boolean pattern(String dateTimePart, String fileNamePattern, SimpleDateFormat format) {
		if (dateTimePart.length() == fileNamePattern.length()) {
			try {
				format.parse(dateTimePart);
				return true;
			} catch (ParseException e) {
				log.error("parse " + dateTimePart + " error.",e);
				return false;
			}
		}
		return false;
	}

	public static boolean pattern(String dateTimePart, SimpleDateFormat format) {
		try {
			format.parse(dateTimePart);
			return true;
		} catch (ParseException e) {
			log.error("parse " + dateTimePart + " error.",e);
			return false;
		}
	}
	

	
	public static String elapsedTime2Str(long time) {
		long hour = time / 3600;
		long min = (time % 3600) / 60;
		long sec = time % 60;
		return StringHelper.fillNumber((int)hour, 2) + ":" + StringHelper.fillNumber((int)min,2) + ":" + StringHelper.fillNumber((int)sec,2);
	}
	
    public static String convertExcelDate2XmlFormat(String date) {
        Date incoming = parseToDate(date, EXCEL_DATE_FORMAT);
        if (incoming != null) {
            return defaultDateFormat.format(incoming);
        } else {
            return null;
        }
    }
    
    public static String convertExcelDateTime2XmlFormat(String dateTime) throws ParseException {
        Date incoming = parseToDate(dateTime, EXCEL_DATETIME_FORMAT);
        if (incoming != null) {
            return defaultDateTimeFormat.format(incoming);
        } else {
            return null;
        }
    }
    
    public static java.util.Date getDateTm(String dateString, String dateFormat) {
    	SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        java.util.Date dateTime = null;
        try {
            dateTime = sdf.parse(dateString);
        } catch (ParseException e) {
            dateTime = null;
        }
        return dateTime;
    }

	public static int dateCompareTo(final Date dt1, final Date dt2) {
        if ((dt1 == null) || (dt2 == null)) {
            if ((dt1 == null) && (dt2 == null)) {
                return 0;
            } else if (dt1 == null) {
                return -1;
            } else {
                return 1;
            }
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(DateHelper.DEFAULT_DATE_FORMAT);
            String dateString1 = sdf.format(dt1);
            String dateString2 = sdf.format(dt2);
            Date date1 = Date.valueOf(dateString1);
            Date date2 = Date.valueOf(dateString2);
            return date1.compareTo(date2);
        }
    }
    
}
