package com.hhhh.group.secwealth.mktdata.api.equity.chart.response.svc;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChartSvcResponse implements Serializable {
        
    private static final long serialVersionUID = 1L;

    private SvcResult[] result; 
    
    private Integer stsCode;
    
    private String stsTxt;
    
}
