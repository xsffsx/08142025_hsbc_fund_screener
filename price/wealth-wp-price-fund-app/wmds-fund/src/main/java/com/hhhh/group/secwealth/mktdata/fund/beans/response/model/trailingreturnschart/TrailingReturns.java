package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.trailingreturnschart;



public class TrailingReturns {

    private TrailingReturnItem[] chartDatas;

    private String returnPeriod;


    public TrailingReturnItem[] getChartDatas() {
        return this.chartDatas;
    }


    public void setChartDatas(final TrailingReturnItem[] chartDatas) {
        this.chartDatas = chartDatas;
    }


    public String getReturnPeriod() {
        return this.returnPeriod;
    }


    public void setReturnPeriod(final String returnPeriod) {
        this.returnPeriod = returnPeriod;
    }


}
