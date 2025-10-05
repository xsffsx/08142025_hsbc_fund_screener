
package com.hhhh.group.secwealth.mktdata.fund.beans.response;

import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.trailingreturnschart.TrailingReturns;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.trailingreturnschart.TrailingReturnsAnalysis;



public class TrailingReturnChartResponse {

    
    private TrailingReturns trailingReturns;

    
    private TrailingReturnsAnalysis trailingReturnsAnalysis;

    
    public TrailingReturns getTrailingReturns() {
        return this.trailingReturns;
    }

    
    public void setTrailingReturns(final TrailingReturns trailingReturns) {
        this.trailingReturns = trailingReturns;
    }

    
    public TrailingReturnsAnalysis getTrailingReturnsAnalysis() {
        return this.trailingReturnsAnalysis;
    }

    
    public void setTrailingReturnsAnalysis(final TrailingReturnsAnalysis trailingReturnsAnalysis) {
        this.trailingReturnsAnalysis = trailingReturnsAnalysis;
    }

}
