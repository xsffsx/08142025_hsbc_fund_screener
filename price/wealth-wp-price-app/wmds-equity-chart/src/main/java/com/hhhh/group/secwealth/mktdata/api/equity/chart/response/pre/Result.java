package com.hhhh.group.secwealth.mktdata.api.equity.chart.response.pre;

import java.io.Serializable;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String market;
    
    private String productType;
    
//    private Boolean delay;
    
    private String[] symbol;
    
    private String displayName;
    
    private Object[] data;
    
    private Object[] fields;

    private Object timeZone;

    
}
