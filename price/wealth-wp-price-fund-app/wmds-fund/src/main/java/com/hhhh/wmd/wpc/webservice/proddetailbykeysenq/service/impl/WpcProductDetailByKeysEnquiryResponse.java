
package com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wpcProductDetailByKeysEnquiryResponse", propOrder = {
    "responseCode",
    "reasonCode",
    "productDetailByKeyResponseInfo"
})
public class WpcProductDetailByKeysEnquiryResponse {

    protected Integer responseCode;
    protected List<ReasonCode> reasonCode;
    protected List<ProductDetailByKeyResponseInfo> productDetailByKeyResponseInfo;


    public Integer getResponseCode() {
        return responseCode;
    }


    public void setResponseCode(Integer value) {
        this.responseCode = value;
    }


    public List<ReasonCode> getReasonCode() {
        if (reasonCode == null) {
            reasonCode = new ArrayList<ReasonCode>();
        }
        return this.reasonCode;
    }


    public List<ProductDetailByKeyResponseInfo> getProductDetailByKeyResponseInfo() {
        if (productDetailByKeyResponseInfo == null) {
            productDetailByKeyResponseInfo = new ArrayList<ProductDetailByKeyResponseInfo>();
        }
        return this.productDetailByKeyResponseInfo;
    }

}
