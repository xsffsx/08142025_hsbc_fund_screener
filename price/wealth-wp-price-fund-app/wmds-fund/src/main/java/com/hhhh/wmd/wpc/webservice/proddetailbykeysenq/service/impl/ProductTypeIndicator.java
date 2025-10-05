
package com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "productTypeIndicator", propOrder = {
    "productType"
})
public class ProductTypeIndicator {

    protected String productType;

    public String getProductType() {
        return productType;
    }


    public void setProductType(String value) {
        this.productType = value;
    }

}
