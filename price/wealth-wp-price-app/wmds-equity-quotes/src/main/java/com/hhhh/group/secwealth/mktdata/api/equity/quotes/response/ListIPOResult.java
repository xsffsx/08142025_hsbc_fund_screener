package com.hhhh.group.secwealth.mktdata.api.equity.quotes.response;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListIPOResult {
	
	private String symbol;

    private String name;

    private String listDate;
    
    private BigDecimal listPrice;

    private BigDecimal lastPrice; 
    
    private BigDecimal accumulatePerChange; 
    
    private BigDecimal changeAmount; 
    
    private BigDecimal changePercent; 
    
    private String currency;
    
    private String overSubscriptionRate;
    
    private String ipoSponsor;
    
    private BigDecimal peRatio;
    
    private BigDecimal firstDayPerformance;
    
    private BigDecimal ipoStatus;
    
    private String asOfDateTime;
}
