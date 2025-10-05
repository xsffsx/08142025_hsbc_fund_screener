
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.advanceChart;

import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;

import java.util.List;

public class ResultV2 {

    private List<ProdAltNumSeg> prodAltNumSegs;

    private List<DataV2> data;

    private String productName;

    private String indexName;

    private String currency;

    private String frequency;



    public List<ProdAltNumSeg> getProdAltNumSegs() {
        return this.prodAltNumSegs;
    }


    public void setProdAltNumSegs(final List<ProdAltNumSeg> prodAltNumSegs) {
        this.prodAltNumSegs = prodAltNumSegs;
    }


    public List<DataV2> getData() {
        return data;
    }

    public void setData(List<DataV2> data) {
        this.data = data;
    }


    public String getProductName() {
        return this.productName;
    }


    public void setProductName(final String productName) {
        this.productName = productName;
    }


    public String getCurrency() {
        return this.currency;
    }


    public void setCurrency(final String currency) {
        this.currency = currency;
    }


    public String getFrequency() {
        return this.frequency;
    }


    public void setFrequency(final String frequency) {
        this.frequency = frequency;
    }


    public String getIndexName() {
        return indexName;
    }


    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }


}
