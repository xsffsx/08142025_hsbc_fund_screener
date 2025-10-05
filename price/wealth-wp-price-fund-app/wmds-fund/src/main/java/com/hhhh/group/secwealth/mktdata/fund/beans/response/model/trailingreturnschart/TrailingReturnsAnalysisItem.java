package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.trailingreturnschart;


public class TrailingReturnsAnalysisItem {

    private String itemName;
    private String returnValue;
    private String startPeriod;
    private String endPeriod;

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(final String itemName) {
        this.itemName = itemName;
    }

    public String getReturnValue() {
        return this.returnValue;
    }

    public void setReturnValue(final String returnValue) {
        this.returnValue = returnValue;
    }

    public String getStartPeriod() {
        return this.startPeriod;
    }

    public void setStartPeriod(final String startPeriod) {
        this.startPeriod = startPeriod;
    }

    public String getEndPeriod() {
        return this.endPeriod;
    }

    public void setEndPeriod(final String endPeriod) {
        this.endPeriod = endPeriod;
    }
}
