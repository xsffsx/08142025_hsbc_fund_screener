package com.hhhh.group.secwealth.mktdata.api.equity.chart.request.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TrisChartRequest implements Serializable {

    private static final long serialVersionUID = 4924763788463173498L;

    private String token;
    
    private String closure;

    private String service;
    
    private String market;
    
    private String productType;
    
    //private Boolean split;

    private Boolean displayName;
    
    //private Boolean currency;
    
    private Boolean timeZone;
    
    //private Boolean tradingWeek;
    
    private final List<String> item = new ArrayList<>();
    
    private Number intCnt;
    
    private String intType;
    
    private String startTm;
    
    private String endTm;
    
    //private Boolean unitFormat;
    
    private Number period;
    
    private Number maxPts;
    
    private Boolean timeZoneRequired;
    
    private final List<String> filter = new ArrayList<>();
    
    public void addItem(final String item) {
        this.item.add(item);
    }

    public void addFilter(final String filter) {
        this.filter.add(filter);
    }

    public void addItem(final List<String> item) {
        this.item.addAll(item);
    }

    public void addFilter(final List<String> filter) {
        this.filter.addAll(filter);
    }

}