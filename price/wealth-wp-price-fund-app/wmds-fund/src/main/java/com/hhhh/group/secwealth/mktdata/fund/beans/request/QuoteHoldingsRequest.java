
package com.hhhh.group.secwealth.mktdata.fund.beans.request;

import com.hhhh.group.secwealth.mktdata.common.svc.request.Request;



public class QuoteHoldingsRequest extends Request {

    private String market;
    private String productType;
    private String prodCdeAltClassCde;
    private String prodAltNum;


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

}
