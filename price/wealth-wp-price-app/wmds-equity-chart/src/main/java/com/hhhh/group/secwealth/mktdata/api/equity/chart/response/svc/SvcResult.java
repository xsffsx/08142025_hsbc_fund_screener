package com.hhhh.group.secwealth.mktdata.api.equity.chart.response.svc;

import java.io.Serializable;
import java.util.Arrays;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcResult implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private Currency currency;
    
    private Object[] data;
    
    private String displayName;
    
    private String[] field;
    
    private Split[] split;
    
    private TimeZone timeZone;
    
    private TradingWeek[] tradingWeek;
    
    private UnitFormat unitFormat;

    @Override
    public String toString() {
        return "SvcResult [currency=" + currency + ", data=" + Arrays.toString(data) + ", displayName=" + displayName + ", field="
            + Arrays.toString(field) + ", split=" + Arrays.toString(split) + ", timeZone=" + timeZone + ", tradingWeek="
            + Arrays.toString(tradingWeek) + ", unitFormat=" + unitFormat + "]";
    }
    
    
}
