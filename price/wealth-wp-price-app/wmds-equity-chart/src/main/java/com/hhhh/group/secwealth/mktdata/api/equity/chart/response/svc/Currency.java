package com.hhhh.group.secwealth.mktdata.api.equity.chart.response.svc;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Currency implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private String baseCurrency;
    
    private boolean isPrimary;
    
    private boolean isQuotedAgainst; 
    
    private String shortName;
    
    
}
