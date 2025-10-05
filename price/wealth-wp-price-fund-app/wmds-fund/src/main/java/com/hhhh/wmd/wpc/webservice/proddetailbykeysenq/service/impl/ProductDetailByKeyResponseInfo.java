
package com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "productDetailByKeyResponseInfo", propOrder = {
    "recordResultCode",
    "productKey",
    "productAttribute"
})
public class ProductDetailByKeyResponseInfo {

    protected Integer recordResultCode;
    protected List<ProductKey> productKey;
    protected List<ProductAttribute> productAttribute;


    public Integer getRecordResultCode() {
        return recordResultCode;
    }


    public void setRecordResultCode(Integer value) {
        this.recordResultCode = value;
    }


    public List<ProductKey> getProductKey() {
        if (productKey == null) {
            productKey = new ArrayList<ProductKey>();
        }
        return this.productKey;
    }


    public List<ProductAttribute> getProductAttribute() {
        if (productAttribute == null) {
            productAttribute = new ArrayList<ProductAttribute>();
        }
        return this.productAttribute;
    }

}
