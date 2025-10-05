package com.hhhh.group.secwealth.mktdata.api.equity.news.util;

import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    public static final String TIMEZONE_GMT = "GMT";
    public static final String TIMEZONE_GMT8 = "GMT+8";
    public static final String DateFormat_yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public static final String DateFormat_yyyyMMddHHmmss_withSymbol = "yyyy-MM-dd HH:mm:ss";
    public static final String DateFormat_yyyyMMddHHmmssSSS = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String DateFormat_yyyyMMddHHmmssSSSXXX = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";


    public static String string2DateString (String dateStr, String oldFormat, String newFormat, TimeZone oldTimezone, TimeZone newTimezone) throws ParseException {
        String date = null;
        SimpleDateFormat formater1 = null;
        SimpleDateFormat formater2 = null;
        if(StringUtil.isValid(dateStr) && null != oldTimezone && null!=newTimezone){
            if(dateStr.contains("&#x1C;")){
                dateStr = dateStr.replace("&#x1C;", "");
            }
            if(dateStr.length()>12){
                formater1 = new SimpleDateFormat(newFormat);
                formater1.setTimeZone(newTimezone);

                formater2 = new SimpleDateFormat(oldFormat, Locale.ENGLISH);
                formater2.setTimeZone(oldTimezone);
                Date temp = formater2.parse(dateStr);
                date = formater1.format(temp);
            }else{
                formater1 = new SimpleDateFormat(newFormat);
                formater1.setTimeZone(newTimezone);

                formater2 = new SimpleDateFormat("yyyy/MM/dd");
                formater2.setTimeZone(oldTimezone);
                Date temp = formater2.parse(dateStr);
                date = formater1.format(temp);
            }
        }
        return date;
    }
}
