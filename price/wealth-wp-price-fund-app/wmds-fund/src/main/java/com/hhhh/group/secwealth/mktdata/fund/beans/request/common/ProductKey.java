
package com.hhhh.group.secwealth.mktdata.fund.beans.request.common;


public class ProductKey {
    private String prodCdeAltClassCde;
    private String prodAltNum;
    private String market;
    private String productType;

    
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((market == null) ? 0 : market.hashCode());
        result = prime * result + ((prodAltNum == null) ? 0 : prodAltNum.hashCode());
        result = prime * result + ((prodCdeAltClassCde == null) ? 0 : prodCdeAltClassCde.hashCode());
        result = prime * result + ((productType == null) ? 0 : productType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProductKey other = (ProductKey) obj;
        if (market == null) {
            if (other.market != null)
                return false;
        } else if (!market.equals(other.market))
            return false;
        if (prodAltNum == null) {
            if (other.prodAltNum != null)
                return false;
        } else if (!prodAltNum.equals(other.prodAltNum))
            return false;
        if (prodCdeAltClassCde == null) {
            if (other.prodCdeAltClassCde != null)
                return false;
        } else if (!prodCdeAltClassCde.equals(other.prodCdeAltClassCde))
            return false;
        if (productType == null) {
            if (other.productType != null)
                return false;
        } else if (!productType.equals(other.productType))
            return false;
        return true;
    }


}
