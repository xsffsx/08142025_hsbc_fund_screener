package com.hhhh.group.secwealth.mktdata.api.equity.index.response.rbpTradingHour;

import java.util.List;

import lombok.Data;

@Data
public class ErrorCode {

    protected String code;
    protected List<String> causes;
    private Object extendedDetails;
    private String traceCode;


}
