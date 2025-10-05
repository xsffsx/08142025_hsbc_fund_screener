package com.hhhh.group.secwealth.mktdata.api.equity.chart.response.svc;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Split implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private String splitDate;
    
    private BigDecimal splitFactor;
}
