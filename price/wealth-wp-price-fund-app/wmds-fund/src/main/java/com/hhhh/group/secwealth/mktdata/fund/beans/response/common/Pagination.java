package com.hhhh.group.secwealth.mktdata.fund.beans.response.common;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


public class Pagination {


    private int totalNumberOfRecords;


    private int numberOfRecords;


    private int startDetail;


    private int endDetail;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).appendSuper(super.toString())
            .append("totalNumberOfRecords", this.totalNumberOfRecords).append("numberOfRecords", this.numberOfRecords)
            .append("startDetail", this.startDetail).append("endDetail", this.endDetail).toString();
    }


    public int getTotalNumberOfRecords() {
        return this.totalNumberOfRecords;
    }


    public void setTotalNumberOfRecords(final int totalNumberOfRecords) {
        this.totalNumberOfRecords = totalNumberOfRecords;
    }


    public int getNumberOfRecords() {
        return this.numberOfRecords;
    }


    public void setNumberOfRecords(final int numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }


    public int getStartDetail() {
        return this.startDetail;
    }


    public void setStartDetail(final int startDetail) {
        this.startDetail = startDetail;
    }


    public int getEndDetail() {
        return this.endDetail;
    }


    public void setEndDetail(final int endDetail) {
        this.endDetail = endDetail;
    }

}
