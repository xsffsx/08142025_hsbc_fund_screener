
package com.hhhh.group.secwealth.mktdata.fund.beans.response;


import java.util.List;

import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quoteperformance.MultiTimeChartData;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quoteperformance.QuotePerformance;

public class QuotePerformanceResponse {

    private QuotePerformance performance;

    List<MultiTimeChartData> multiTimeChartDatas;

    public QuotePerformance getPerformance() {
        return this.performance;
    }

    public void setPerformance(final QuotePerformance performance) {
        this.performance = performance;
    }

    public List<MultiTimeChartData> getMultiTimeChartDatas() {
        return this.multiTimeChartDatas;
    }

    public void setMultiTimeChartDatas(final List<MultiTimeChartData> multiTimeChartDatas) {
        this.multiTimeChartDatas = multiTimeChartDatas;
    }

}
