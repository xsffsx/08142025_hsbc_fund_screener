
package com.hhhh.group.secwealth.mktdata.quote.beans.request;

import com.hhhh.group.secwealth.mktdata.common.svc.request.Request;



public class QuoteDetailRequest extends Request {

    private String market;
    private String productType;
    private String prodCdeAltClassCde;
    private String prodAltNum;
    private Boolean delay;
    // optional
    private Boolean tradeDay;
    private String tradeHours;
    private Number freeQuote;
    private String requestType;
    private String skipAgreementCheck;
    private String entityTimezone;
    private String adjustedCusSegment;
    private String side;
    private Number quantity;
    private String channelRestrictCode;


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


    public Boolean getDelay() {
        return this.delay;
    }


    public void setDelay(final Boolean delay) {
        this.delay = delay;
    }


    public Boolean getTradeDay() {
        return this.tradeDay;
    }


    public void setTradeDay(final Boolean tradeDay) {
        this.tradeDay = tradeDay;
    }


    public String getTradeHours() {
        return this.tradeHours;
    }


    public void setTradeHours(final String tradeHours) {
        this.tradeHours = tradeHours;
    }


    public Number getFreeQuote() {
        return this.freeQuote;
    }


    public void setFreeQuote(final Number freeQuote) {
        this.freeQuote = freeQuote;
    }


    public String getRequestType() {
        return this.requestType;
    }


    public void setRequestType(final String requestType) {
        this.requestType = requestType;
    }


    public String getSkipAgreementCheck() {
        return this.skipAgreementCheck;
    }


    public void setSkipAgreementCheck(final String skipAgreementCheck) {
        this.skipAgreementCheck = skipAgreementCheck;
    }


    public String getEntityTimezone() {
        return this.entityTimezone;
    }


    public void setEntityTimezone(final String entityTimezone) {
        this.entityTimezone = entityTimezone;
    }


    public String getAdjustedCusSegment() {
        return this.adjustedCusSegment;
    }


    public void setAdjustedCusSegment(final String adjustedCusSegment) {
        this.adjustedCusSegment = adjustedCusSegment;
    }


    public String getSide() {
        return this.side;
    }


    public void setSide(final String side) {
        this.side = side;
    }

    public Number getQuantity() {
        return this.quantity;
    }


    public void setQuantity(final Number quantity) {
        this.quantity = quantity;
    }


    public String getChannelRestrictCode() {
        return this.channelRestrictCode;
    }


    public void setChannelRestrictCode(final String channelRestrictCode) {
        this.channelRestrictCode = channelRestrictCode;
    }

}
