package com.hhhh.group.secwealth.mktdata.api.equity.chart.response;


import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.pre.Result;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ChartResponse implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private List<Result> result;
    
}
