
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quickview;

import java.math.BigDecimal;


public class QuickViewRespItem {


    private String name;


    private String market;


    private String productType;


    private String prodCdeAltClassCde;


    private String prodAltNum;


    private BigDecimal trailingTotalReturn;

    private String riskLvlCde;

    private String currency;

    private String esgInd;

    private String gbaInd;

    private String dividendYield;

    private String prodStatCde;




    public String getName() {
        return this.name;
    }


    public void setName(final String name) {
        this.name = name;
    }


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


    public BigDecimal getTrailingTotalReturn() {
        return this.trailingTotalReturn;
    }


    public void setTrailingTotalReturn(final BigDecimal trailingTotalReturn) {
        this.trailingTotalReturn = trailingTotalReturn;
    }


    public String getRiskLvlCde() {
        return this.riskLvlCde;
    }


    public void setRiskLvlCde(final String riskLvlCde) {
        this.riskLvlCde = riskLvlCde;
    }

    public String getEsgInd() {
        return esgInd;
    }

    public void setEsgInd(String esgInd) {
        this.esgInd = esgInd;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getGbaInd() {
        return gbaInd;
    }

    public void setGbaInd(String gbaInd) {
        this.gbaInd = gbaInd;
    }

    public String getDividendYield() {
        return dividendYield;
    }

    public void setDividendYield(String dividendYield) {
        this.dividendYield = dividendYield;
    }

    public String getProdStatCde() {
        return prodStatCde;
    }

    public void setProdStatCde(String prodStatCde) {
        this.prodStatCde = prodStatCde;
    }
}
