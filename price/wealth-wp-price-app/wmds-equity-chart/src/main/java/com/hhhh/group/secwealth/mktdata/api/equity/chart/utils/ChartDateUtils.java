package com.hhhh.group.secwealth.mktdata.api.equity.chart.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
public class ChartDateUtils {

    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    public static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            log.error("Parse time error, the time content:{}", strDate);
            return null;
        }


    }



}