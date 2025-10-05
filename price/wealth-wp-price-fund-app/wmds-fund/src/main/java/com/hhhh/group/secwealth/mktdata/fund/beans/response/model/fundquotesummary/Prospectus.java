
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary;

import java.math.BigDecimal;


public class Prospectus {
    private String indexId;
    private String indexName;
    private BigDecimal weighting;
    private BigDecimal prospectusPrimaryIndex;

    public Prospectus(final String indexId, final String indexName, final BigDecimal weighting,
        final BigDecimal prospectusPrimaryIndex) {
        this.indexId = indexId;
        this.indexName = indexName;
        this.weighting = weighting;
        this.prospectusPrimaryIndex = prospectusPrimaryIndex;
    }


    public String getIndexId() {
        return this.indexId;
    }


    public void setIndexId(final String indexId) {
        this.indexId = indexId;
    }


    public String getIndexName() {
        return this.indexName;
    }


    public void setIndexName(final String indexName) {
        this.indexName = indexName;
    }


    public BigDecimal getWeighting() {
        return this.weighting;
    }


    public void setWeighting(final BigDecimal weighting) {
        this.weighting = weighting;
    }


    public BigDecimal getProspectusPrimaryIndex() {
        return this.prospectusPrimaryIndex;
    }


    public void setProspectusPrimaryIndex(final BigDecimal prospectusPrimaryIndex) {
        this.prospectusPrimaryIndex = prospectusPrimaryIndex;
    }
}
