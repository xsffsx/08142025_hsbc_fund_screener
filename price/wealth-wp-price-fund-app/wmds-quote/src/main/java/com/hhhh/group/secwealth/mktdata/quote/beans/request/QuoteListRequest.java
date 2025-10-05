
package com.hhhh.group.secwealth.mktdata.quote.beans.request;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.svc.request.Request;


public class QuoteListRequest extends Request {

    private List<ProductKey> productKeys;
    private String market;
    private Boolean delay;

    // optional
    private Boolean tradeDay;
    private String tradeHours;
    private Number freeQuote;
    private String requestType;
    private String skipAgreementCheck;
    private String entityTimezone;


    public List<ProductKey> getProductKeys() {
        return this.productKeys;
    }


    public void setProductKeys(final List<ProductKey> productKeys) {
        this.productKeys = productKeys;
    }


    public String getMarket() {
        return this.market;
    }


    public void setMarket(final String market) {
        this.market = market;
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
}
