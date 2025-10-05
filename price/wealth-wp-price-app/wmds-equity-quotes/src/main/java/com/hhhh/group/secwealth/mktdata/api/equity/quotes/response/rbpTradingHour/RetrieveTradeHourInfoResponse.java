package com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.rbpTradingHour;

import lombok.Data;

import java.util.List;

@Data
public class RetrieveTradeHourInfoResponse {

    protected List<TradingHoursInfo> tradingHoursList;
    protected List<CoreReserve> coreReserveArea;
    protected List<LocalField> localFieldsArea;
    protected List<ErrorCode> errorInfo;


}
