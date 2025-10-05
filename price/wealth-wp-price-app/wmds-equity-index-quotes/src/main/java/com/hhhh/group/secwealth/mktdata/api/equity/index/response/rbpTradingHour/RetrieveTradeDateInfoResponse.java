package com.hhhh.group.secwealth.mktdata.api.equity.index.response.rbpTradingHour;

import java.util.List;

import lombok.Data;

@Data
public class RetrieveTradeDateInfoResponse {

    protected List<TradeDateInfo> tradeDateList;
    protected List<CoreReserve> coreReserveArea;
    protected List<LocalField> localFieldsArea;
    protected List<ErrorCode> errorInfo;


}
