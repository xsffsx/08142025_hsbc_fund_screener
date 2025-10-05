/*
 * ***************************************************************
 * Copyright. dummy Holdings plc 2006 ALL RIGHTS RESERVED.
 * 
 * This software is only to be used for the purpose for which it has been
 * provided. No part of it is to be reproduced, disassembled, transmitted,
 * stored in a retrieval system or translated in any human or computer language
 * in any way or for any other purposes whatsoever without the prior written
 * consent of dummy Holdings plc.
 * ***************************************************************
 * 
 * Class Name DateHelper
 * 
 * Creation Date Mar 23, 2006
 * 
 * Amendment History (In chronological sequence):
 * 
 * Amendment Date Mar 23, 2006 CMM/PPCR No. Programmer 35021438 Description
 * This class provides Date utility functions.
 */
package com.dummy.wpb.product.helper;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class DateHelper {


    public static final String SEPARATOR = ".";
    public static final String SUFFIX = "TIMEZONE";
    public static final String DEFAULT = "DEFAULT";

    public static final TimeZone DEFAULT_TIMEZONE = TimeZone.getDefault();

    /** The Constant DEFAULT_TIMESTAMP_FORMAT_WITHTIMEZONE. */
    public static final String DEFAULT_TIMESTAMP_FORMAT_WITHTIMEZONE = "yyyy-MM-dd HH:mm:ss.SSS Z";

    public static final String DEFAULT_FORMAT_WITHTIMEZONE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /** The Constant DEFAULT_TIMESTAMP_FORMAT. */
    public static final String DEFAULT_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final String DEFAULT_TIMESTAMP_FORMAT_WITHTIMEZONE_Z = "yyyy-MM-dd HH:mm:ss.SSS z";

    /** The Constant DEFAULT_DATETIME_FORMAT. */
    // yyyy-MM-dd HH:mm:ss.SSS
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** The Constant DEFAULT_DATE_FORMAT. */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /** The Constant DEFAULT_TIME_FORMAT. */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    public static final String TIME_NOSEC_FORMAT = "HH:mm";

    public static final String DATE_SHORT_FORMAT = "yyyyMMdd";
    public static final String TIME_SHORT_FORMAT = "hhmmss";

    // separtor for 'yyyy-MM-dd'
    public static final String DEFAULT_XML_DATE_SEPARTOR = "-";
    /** Initial a Calendar Instance */

    private DateHelper(){}


    public static Date getCurrentDate() {
        return new Date(new GregorianCalendar().getTime().getTime());
    }


    public static GregorianCalendar getCalendar() {
        GregorianCalendar result = new GregorianCalendar();

        result.set(Calendar.HOUR_OF_DAY, 0);
        result.set(Calendar.MINUTE, 0);
        result.set(Calendar.SECOND, 0);
        result.set(Calendar.MILLISECOND, 0);

        return result;
    }



    public static LocalDateTime getLocalDateTimeByDate(final java.util.Date date, final java.util.Date time) {
        Timestamp timestampFromDateTime = getTimestampFromDateTime(date, time);
        if (timestampFromDateTime != null) {
            return timestampFromDateTime.toLocalDateTime();
        }
        return null;
    }

    public static Timestamp getTimestampFromDateTime(final java.util.Date date, final java.util.Date time) {
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        String dateTemp = null;
        String timeTemp = null;
        if (date == null && time == null) {

            return null;

        } else if (date != null && time == null) {

            return new Timestamp(date.getTime());

        } else if (date == null) {

            dateTemp = df.format(new java.util.Date());
            timeTemp = time.toString();

        } else {

            dateTemp = df.format(date);
            timeTemp = time.toString();

        }
        df.applyPattern(DEFAULT_DATETIME_FORMAT);
        java.util.Date tempDate = null;

        try {
            tempDate = df.parse(dateTemp + " " + timeTemp);
        } catch (java.text.ParseException e) {
           return null;
        }
        return new Timestamp(tempDate.getTime());

    }


    public static String formatDate2String(final java.util.Date date, final String format) {
        if (null == date || StringUtils.isBlank(format)) {
            return null;
        }
        return DateFormatUtils.format(date, format);
    }

}
