package com.hhhh.group.secwealth.mktdata.api.equity.index.response.rbpTradingHour;

import java.util.List;

import lombok.Data;

@Data
public class RetrieveTradeHourInfoResponse {

    protected List<TradingHoursInfo> tradingHoursList;
    protected List<CoreReserve> coreReserveArea;
    protected List<LocalField> localFieldsArea;
    protected List<ErrorCode> errorInfo;


}
