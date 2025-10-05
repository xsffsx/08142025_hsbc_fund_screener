package com.hhhh.group.secwealth.mktdata.api.equity.quotes.util;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.rbpTradingHour.RetrieveTradeDateInfoResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.rbpTradingHour.RetrieveTradeHourInfoResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.rbpTradingHour.TradeDateInfo;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.rbpTradingHour.TradingHoursInfo;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;


public class TradingHourUtilTest {
    private RetrieveTradeDateInfoResponse response;


    private TradingHourUtil tradingHourUtil = new TradingHourUtil();

    private RetrieveTradeHourInfoResponse retrieveResponse;

    @Before
    public void init(){
        response = new RetrieveTradeDateInfoResponse();
        List<TradeDateInfo> tradeDateList = new ArrayList<>();
        TradeDateInfo tradeDateInfo = new TradeDateInfo();
        tradeDateInfo.setOrderTradeDate("2018-06-22 00:00:00");
        tradeDateList.add(tradeDateInfo);
        response.setTradeDateList(tradeDateList);

        retrieveResponse = new RetrieveTradeHourInfoResponse();
        List<TradingHoursInfo> tradingHoursList = new ArrayList<>();
        TradingHoursInfo tradingHoursInfo = new TradingHoursInfo();
        tradingHoursInfo.setTradingSessionCode("NORMAL");
        tradingHoursInfo.setTradingSessionStartTime("2018-06-22T15:51:00.000+08:00");
        tradingHoursInfo.setTradingSessionEndTime("2018-07-22T15:51:00.000+08:00");
        tradingHoursList.add(tradingHoursInfo);
        retrieveResponse.setTradingHoursList(tradingHoursList);
    }
    @Test
    public void testGetTradeDayinfo(){
        boolean isContainToday = tradingHourUtil.getTradeDayinfo(response);
        assertEquals(false,isContainToday);
    }

    @Test
    public void testCurrent(){
        String date = tradingHourUtil.current(Constant.DATE_FORMAT_FOT_LABCI, TimeZone.getTimeZone("2021-08-24"));
        assertNotEquals("2021-09-09T08:17:43",date);
    }

    @Test
    public void testTimeZoneNullCurrent(){
        String date = tradingHourUtil.current(Constant.DATE_FORMAT_FOT_LABCI,null);
        assertNotEquals("2021-09-09T08:17:43",date);
    }

    @Test
    public void testGetTradeHourInfo() throws Exception{
        String tradeHourInfo = tradingHourUtil.getTradeHourInfo(true,retrieveResponse);
        assertEquals("2018-06-22T15:51:00.000+08:00115959:1200002018-07-22T15:51:00.000+08:00",tradeHourInfo);
    }

    @Test
    public void testWithinTradingHour() throws Exception{
        boolean withinTradingHour = tradingHourUtil.withinTradingHour(true,retrieveResponse);
        assertEquals(false,withinTradingHour);
    }

}
