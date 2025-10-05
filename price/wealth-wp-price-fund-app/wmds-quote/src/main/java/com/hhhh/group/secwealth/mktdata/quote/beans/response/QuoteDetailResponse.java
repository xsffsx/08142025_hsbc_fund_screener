
package com.hhhh.group.secwealth.mktdata.quote.beans.response;

import java.math.BigDecimal;
import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.quote.beans.response.common.BidAskQuote;
import com.hhhh.group.secwealth.mktdata.quote.beans.response.common.IndexQuote;
import com.hhhh.group.secwealth.mktdata.quote.beans.response.common.QuoteData;


public class QuoteDetailResponse {

    /** use for future and the level should be: 1,2,3,4. */
    private String requestType;

    /** The remaining free quote for the user. */
    private BigDecimal remainingQuote;

    /** Total free quote available. */
    private BigDecimal totalQuote;

    /** Index quote list. */
    private IndexQuote[] indexQuotes;

    /** Bid or Ask Quote list. */
    private BidAskQuote[] bidAskQuotes;

    /** Price quote detail. */
    private QuoteData priceQuote;

    /** last update time of the entity. */
    private String entityUpdatedTime;

    /** ProdAltNumSeg List */
    private List<ProdAltNumSeg> ProdAltNumSegs;


    public List<ProdAltNumSeg> getProdAltNumSegs() {
        return this.ProdAltNumSegs;
    }


    public void setProdAltNumSegs(final List<ProdAltNumSeg> prodAltNumSegs) {
        this.ProdAltNumSegs = prodAltNumSegs;
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


    public String getEntityUpdatedTime() {
        return this.entityUpdatedTime;
    }


    public void setEntityUpdatedTime(final String entityUpdatedTime) {
        this.entityUpdatedTime = entityUpdatedTime;
    }


    public IndexQuote[] getIndexQuotes() {
        return this.indexQuotes;
    }


    public void setIndexQuotes(final IndexQuote[] indexQuotes) {
        this.indexQuotes = indexQuotes;
    }


    public BidAskQuote[] getBidAskQuotes() {
        return this.bidAskQuotes;
    }


    public void setBidAskQuotes(final BidAskQuote[] bidAskQuotes) {
        this.bidAskQuotes = bidAskQuotes;
    }


    public QuoteData getPriceQuote() {
        return this.priceQuote;
    }


    public void setPriceQuote(final QuoteData priceQuote) {
        this.priceQuote = priceQuote;
    }

}