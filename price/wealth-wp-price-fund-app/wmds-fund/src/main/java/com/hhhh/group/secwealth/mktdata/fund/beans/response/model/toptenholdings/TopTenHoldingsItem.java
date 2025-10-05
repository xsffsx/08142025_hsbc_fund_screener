
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.toptenholdings;

import java.math.BigDecimal;


public class TopTenHoldingsItem {


    private String market;


    private String productType;


    private String prodCdeAltClassCde;


    private String prodAltNum;


    private String securityName;


    private BigDecimal weighting;


    private String currency;


    private BigDecimal marketValue;



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


    public String getSecurityName() {
        return this.securityName;
    }


    public void setSecurityName(final String securityName) {
        this.securityName = securityName;
    }


    public BigDecimal getWeighting() {
        return this.weighting;
    }


    public void setWeighting(final BigDecimal weighting) {
        this.weighting = weighting;
    }


    public String getCurrency() {
        return this.currency;
    }


    public void setCurrency(final String currency) {
        this.currency = currency;
    }


    public BigDecimal getMarketValue() {
        return this.marketValue;
    }


    public void setMarketValue(final BigDecimal marketValue) {
        this.marketValue = marketValue;
    }
}
