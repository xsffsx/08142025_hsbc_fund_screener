
package com.hhhh.group.secwealth.mktdata.quote.beans.response;

import java.math.BigDecimal;
import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.quote.beans.response.common.ExchangeAgreement;
import com.hhhh.group.secwealth.mktdata.quote.beans.response.common.PriceQuote;



public class QuoteListResponse {

    /** use for future and the level should be: 1,2,3,4. */
    private String requestType;

    /** The remaining free quote for the user. */
    private BigDecimal remainingQuote;

    /** Total free quote available. */
    private BigDecimal totalQuote;

    /** Quote list. */
    private List<PriceQuote> priceQuotes;

    /** The date format:yyyyMMddHHmmss. */
    private String entityUpdatedTime;

    /** The entity timezone. */
    private String entityTimezone;

    /** The in daylight time. */
    private String inDaylightTime;

    /** The signed exchange. */
    private ExchangeAgreement[] signedExchanges;

    /** The unsigned exchange. */
    private ExchangeAgreement[] unSignedExchanges;

    /** ProdAltNumSeg List */
    private ProdAltNumSeg[] prodAltNumSegs;



    public ProdAltNumSeg[] getProdAltNumSegs() {
        return this.prodAltNumSegs;
    }


    public void setProdAltNumSegs(final ProdAltNumSeg[] prodAltNumSegs) {
        this.prodAltNumSegs = prodAltNumSegs;
    }


    public String getRequestType() {
        return this.requestType;
    }


    public void setRequestType(final String requestType) {
        this.requestType = requestType;
    }


    public BigDecimal getRemainingQuote() {
        return this.remainingQuote;
    }


    public void setRemainingQuote(final BigDecimal remainingQuote) {
        this.remainingQuote = remainingQuote;
    }


    public BigDecimal getTotalQuote() {
        return this.totalQuote;
    }


    public void setTotalQuote(final BigDecimal totalQuote) {
        this.totalQuote = totalQuote;
    }


    public List<PriceQuote> getPriceQuotes() {
        return this.priceQuotes;
    }


    public void setPriceQuotes(final List<PriceQuote> priceQuotes) {
        this.priceQuotes = priceQuotes;
    }


    public String getEntityUpdatedTime() {
        return this.entityUpdatedTime;
    }


    public void setEntityUpdatedTime(final String entityUpdatedTime) {
        this.entityUpdatedTime = entityUpdatedTime;
    }


    public ExchangeAgreement[] getSignedExchanges() {
        return this.signedExchanges;
    }


    public void setSignedExchanges(final ExchangeAgreement[] signedExchanges) {
        this.signedExchanges = signedExchanges;
    }


    public ExchangeAgreement[] getUnSignedExchanges() {
        return this.unSignedExchanges;
    }


    public void setUnSignedExchanges(final ExchangeAgreement[] unSignedExchanges) {
        this.unSignedExchanges = unSignedExchanges;
    }


    public String getEntityTimezone() {
        return this.entityTimezone;
    }


    public void setEntityTimezone(final String entityTimezone) {
        this.entityTimezone = entityTimezone;
    }


    public String getInDaylightTime() {
        return this.inDaylightTime;
    }


    public void setInDaylightTime(final String inDaylightTime) {
        this.inDaylightTime = inDaylightTime;
    }

}