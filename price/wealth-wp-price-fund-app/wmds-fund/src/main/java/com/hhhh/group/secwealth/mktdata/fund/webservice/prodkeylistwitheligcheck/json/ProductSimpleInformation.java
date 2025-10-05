
package com.hhhh.group.secwealth.mktdata.fund.webservice.prodkeylistwitheligcheck.json;

import java.util.List;

public class ProductSimpleInformation {

    private String riskLvlCde;
    private List<ProductInfo> productKey;
    private String prdSbTpCd;


    public String getRiskLvlCde() {
        return this.riskLvlCde;
    }


    public void setRiskLvlCde(final String riskLvlCde) {
        this.riskLvlCde = riskLvlCde;
    }


    public List<ProductInfo> getProductKey() {
        return this.productKey;
    }


    public void setProductKey(final List<ProductInfo> productKey) {
        this.productKey = productKey;
    }


    public String getPrdSbTpCd() {
        return this.prdSbTpCd;
    }


    public void setPrdSbTpCd(final String prdSbTpCd) {
        this.prdSbTpCd = prdSbTpCd;
    }
}
