package com.hhhh.group.secwealth.mktdata.elastic.util;

import lombok.extern.slf4j.Slf4j;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Slf4j
public class DateUtil {
    public static final String DEFAULT_TIMEZONE_ID = "GMT";

    public static final String YYYY_M_MDD_H_HMMSS = "yyyyMMddHHmmss";

    private DateUtil() {}

    public static Date addMinutesForCurrentDate(final int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }

    public static String afterMinutesOfCurrent(final String pattern, final TimeZone timezone, final int minutes) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
        setTimeZoneForDateFormatter(timezone, formatter);
        return formatter.format(addMinutesForCurrentDate(minutes));
    }

    private static void setTimeZoneForDateFormatter(final TimeZone timezone, final SimpleDateFormat formatter) {
        if (timezone != null) {
            formatter.setTimeZone(timezone);
        } else {
            formatter.setTimeZone(TimeZone.getTimeZone(DateUtil.DEFAULT_TIMEZONE_ID));
        }
    }


}
