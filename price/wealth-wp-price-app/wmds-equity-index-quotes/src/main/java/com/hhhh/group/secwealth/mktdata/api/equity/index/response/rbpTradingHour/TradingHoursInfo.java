package com.hhhh.group.secwealth.mktdata.api.equity.index.response.rbpTradingHour;

import lombok.Data;

@Data
public class TradingHoursInfo {

    protected String tradingSessionCode;
    protected String tradingSessionEndTime;
    protected String tradingSessionEndTimeTimezone;
    protected String tradingSessionStartTime;
    protected String tradingSessionStartTimeTimezone;
    protected String tradingSubSessionCode;

}
