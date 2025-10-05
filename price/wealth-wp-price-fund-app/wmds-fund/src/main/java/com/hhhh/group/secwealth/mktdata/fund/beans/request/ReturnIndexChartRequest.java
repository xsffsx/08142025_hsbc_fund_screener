
package com.hhhh.group.secwealth.mktdata.fund.beans.request;

import com.hhhh.group.secwealth.mktdata.common.svc.request.Request;



public class ReturnIndexChartRequest extends Request {

    private String market;
    private String productType;
    private String prodCdeAltClassCde;
    private String prodAltNum;
    private String timeZone;
    private String startDate;
    private String endDate;
    private String period;
    private String frequency;


    public String getMarket() {
        return this.market;
    }


    public void setMarket(final String market) {
        this.market = market;
    }


    public String getProductType() {
        return this.productType;
    }


    public void setProductType(final String productType) {
        this.productType = productType;
    }


    public String getProdCdeAltClassCde() {
        return this.prodCdeAltClassCde;
    }


    public void setProdCdeAltClassCde(final String prodCdeAltClassCde) {
        this.prodCdeAltClassCde = prodCdeAltClassCde;
    }


    public String getProdAltNum() {
        return this.prodAltNum;
    }


    public void setProdAltNum(final String prodAltNum) {
        this.prodAltNum = prodAltNum;
    }


    public String getTimeZone() {
        return this.timeZone;
    }


    public void setTimeZone(final String timeZone) {
        this.timeZone = timeZone;
    }



    public String getStartDate() {
        return this.startDate;
    }


    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }


    public String getEndDate() {
        return this.endDate;
    }


    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }


    public String getPeriod() {
        return this.period;
    }


    public void setPeriod(final String period) {
        this.period = period;
    }


    public String getFrequency() {
        return this.frequency;
    }


    public void setFrequency(final String frequency) {
        this.frequency = frequency;
    }


}
