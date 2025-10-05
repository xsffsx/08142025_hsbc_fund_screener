package com.hhhh.group.secwealth.mktdata.api.equity.chart.response.labci;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LabciChartResponse implements Serializable {
        
    private static final long serialVersionUID = 1L;

    private LabciResult[] result;
    
    private String stsCode;
    
    private String stsTxt;

}
