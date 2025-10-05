/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.util;

import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.rbpTradingHour.RetrieveTradeDateInfoResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.rbpTradingHour.RetrieveTradeHourInfoResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.rbpTradingHour.TradeDateInfo;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.rbpTradingHour.TradingHoursInfo;

import javax.xml.datatype.DatatypeConfigurationException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TradingHourUtil {

    public static final String DATE_DAY_PATTERN = "yyyy-MM-dd";

    public static final String HK_TIMEZONE = "GMT+8";

    public static final String DEFAULT_TIMEZONE_ID = "GMT";

    private static final String MORNING_END = "115959";

    private static final String AFTERNOON_START = "120000";

    public static final String US_TIMEZONE = "America/New_York";

    public static final String ASIA_SHANGHAI_TIMEZONE = "Asia/Shanghai";

    public static final int DELAY_CLOSE = 15;

    // to be removed
    public boolean getTradeDayinfo(final RetrieveTradeDateInfoResponse response) {
        boolean isContainToday = false;
        for (TradeDateInfo tradeDateInfo : response.getTradeDateList()) {
            // only compare day , ignore HH:mm:ss TimeZone.getTimeZone("GMT+8")
            if (tradeDateInfo.getOrderTradeDate()
                .compareTo(current(TradingHourUtil.DATE_DAY_PATTERN, TimeZone.getTimeZone(TradingHourUtil.HK_TIMEZONE))) == 0) {
                isContainToday = true;
                break;
            }
        }
        return isContainToday;
    }

    public static String current(final String pattern, final TimeZone timezone) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
        if (timezone != null) {
            formatter.setTimeZone(timezone);
        } else {
            formatter.setTimeZone(TimeZone.getTimeZone(TradingHourUtil.DEFAULT_TIMEZONE_ID));
        }
        return formatter.format(new Date());
    }

    // to be removed
    public String getTradeHourInfo(final boolean isTradeDay, final RetrieveTradeHourInfoResponse response)
        throws DatatypeConfigurationException, MalformedURLException {
        if (isTradeDay) {
            for (TradingHoursInfo tradingHoursInfo : response.getTradingHoursList()) {
                if ("NORMAL".equals(tradingHoursInfo.getTradingSessionCode())) {
                    if (StringUtil.isValid(tradingHoursInfo.getTradingSessionStartTime())
                        && StringUtil.isValid(tradingHoursInfo.getTradingSessionEndTime())) {
                        // 000001115959:120000235959 000001
                        return tradingHoursInfo.getTradingSessionStartTime() + TradingHourUtil.MORNING_END + ":"
                            + TradingHourUtil.AFTERNOON_START + tradingHoursInfo.getTradingSessionEndTime();
                    }
                }
            }
        }
        return null;
    }

    public static boolean withinTradingHour(final boolean isTradeDay, final RetrieveTradeHourInfoResponse response) {
        String startString = "";
        String endString = "";
        if (isTradeDay && response != null) {
            for (TradingHoursInfo tradingHoursInfo : response.getTradingHoursList()) {
                if ("NORMAL".equals(tradingHoursInfo.getTradingSessionCode())) {
                    if (StringUtil.isValid(tradingHoursInfo.getTradingSessionStartTime())
                        && StringUtil.isValid(tradingHoursInfo.getTradingSessionEndTime())) {
                        startString = tradingHoursInfo.getTradingSessionStartTime();
                        endString = tradingHoursInfo.getTradingSessionEndTime();
                    }
                }
            }
        }

        Calendar today = Calendar.getInstance(TimeZone.getTimeZone(TradingHourUtil.US_TIMEZONE));
        Calendar nowDate = getTodayDate();
        if (StringUtil.isValid(startString)) {
            Calendar startTime = DateUtil.getTime(today, startString, 0);
            Calendar endTime = DateUtil.getTime(today, endString, TradingHourUtil.DELAY_CLOSE);
            if (nowDate.after(startTime) && nowDate.before(endTime)) {
                return true;
            }
        } // 1580724066134 1580793299000 1580724066135

        return false;
    }

    private static Calendar getTodayDate() {
        Calendar todayDate = Calendar.getInstance(TimeZone.getTimeZone(TradingHourUtil.US_TIMEZONE));
        todayDate.set(Calendar.MILLISECOND, 1);// 1580725813671 1580725792164
        return todayDate;
    }
}
