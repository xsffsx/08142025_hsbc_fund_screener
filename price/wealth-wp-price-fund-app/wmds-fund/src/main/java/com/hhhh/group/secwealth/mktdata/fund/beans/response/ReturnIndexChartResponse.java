package com.hhhh.group.secwealth.mktdata.fund.beans.response;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.returnindexchart.ReturnIndexChart;

public class ReturnIndexChartResponse {

    private List<ReturnIndexChart> result;


    public List<ReturnIndexChart> getResult() {
        return this.result;
    }


    public void setResult(final List<ReturnIndexChart> result) {
        this.result = result;
    }
}
