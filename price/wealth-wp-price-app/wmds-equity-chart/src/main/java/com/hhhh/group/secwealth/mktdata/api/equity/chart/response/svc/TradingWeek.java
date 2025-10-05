package com.hhhh.group.secwealth.mktdata.api.equity.chart.response.svc;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradingWeek implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private String endDay;
    
    private String endTm;
    
    private String startDay;
    
    private String startTm;
    
}
