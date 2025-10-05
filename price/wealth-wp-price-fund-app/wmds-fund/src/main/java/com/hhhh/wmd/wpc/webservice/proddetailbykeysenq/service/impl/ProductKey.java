
package com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "productKey", propOrder = {
    "currencyProductCode",
    "productAlternativeNumber",
    "productCodeAlternativeClassCode",
    "productTradableCode",
    "productTypeCode"
})
public class ProductKey {

    protected String currencyProductCode;
    protected String productAlternativeNumber;
    protected String productCodeAlternativeClassCode;
    protected String productTradableCode;
    protected String productTypeCode;


    public String getCurrencyProductCode() {
        return currencyProductCode;
    }


    public void setCurrencyProductCode(String value) {
        this.currencyProductCode = value;
    }


    public String getProductAlternativeNumber() {
        return productAlternativeNumber;
    }


    public void setProductAlternativeNumber(String value) {
        this.productAlternativeNumber = value;
    }


    public String getProductCodeAlternativeClassCode() {
        return productCodeAlternativeClassCode;
    }


    public void setProductCodeAlternativeClassCode(String value) {
        this.productCodeAlternativeClassCode = value;
    }


    public String getProductTradableCode() {
        return productTradableCode;
    }


    public void setProductTradableCode(String value) {
        this.productTradableCode = value;
    }


    public String getProductTypeCode() {
        return productTypeCode;
    }


    public void setProductTypeCode(String value) {
        this.productTypeCode = value;
    }

}
