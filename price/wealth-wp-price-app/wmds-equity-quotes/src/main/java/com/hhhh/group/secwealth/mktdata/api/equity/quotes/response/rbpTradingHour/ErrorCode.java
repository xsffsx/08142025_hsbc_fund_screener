package com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.rbpTradingHour;

import lombok.Data;

import java.util.List;

@Data
public class ErrorCode {

    protected String code;
    protected List<String> causes;
    private Object extendedDetails;
    private String traceCode;


}
