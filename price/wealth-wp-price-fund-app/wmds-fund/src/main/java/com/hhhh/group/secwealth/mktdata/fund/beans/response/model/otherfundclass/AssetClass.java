
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.otherfundclass;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;


public class AssetClass {


    private List<ProdAltNumSeg> ProdAltNumSegs;
    private String productName;
    private String productShortName;
    private String fundShreClsName;
    private String prodStatCde;;


    public List<ProdAltNumSeg> getProdAltNumSegs() {
        return this.ProdAltNumSegs;
    }


    public void setProdAltNumSegs(final List<ProdAltNumSeg> prodAltNumSegs) {
        this.ProdAltNumSegs = prodAltNumSegs;
    }


    public String getProductName() {
        return this.productName;
    }


    public void setProductName(final String productName) {
        this.productName = productName;
    }


    public String getProductShortName() {
        return this.productShortName;
    }


    public void setProductShortName(final String productShortName) {
        this.productShortName = productShortName;
    }


    public String getFundShreClsName() {
        return this.fundShreClsName;
    }


    public void setFundShreClsName(final String fundShreClsName) {
        this.fundShreClsName = fundShreClsName;
    }

    public String getProdStatCde() {
        return this.prodStatCde;
    }

    public void setProdStatCde(final String prodStatCde) {
        this.prodStatCde = prodStatCde;
    }

}
