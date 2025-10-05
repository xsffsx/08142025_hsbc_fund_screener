package com.hhhh.group.secwealth.mktdata.api.equity.chart.response.etnet;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class EtnetChartResponse implements Serializable {
        
    private static final long serialVersionUID = 1L;

    private EtnetResult result;
    
    private Integer errCode;
    
    private String errDesc;

}
