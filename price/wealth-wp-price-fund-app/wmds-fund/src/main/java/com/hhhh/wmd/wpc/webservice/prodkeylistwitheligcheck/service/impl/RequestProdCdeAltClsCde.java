
package com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "requestProdCdeAltClsCde", propOrder = {
    "productCodeAlternativeClassCode"
})
public class RequestProdCdeAltClsCde {

    protected String productCodeAlternativeClassCode;


    public String getProductCodeAlternativeClassCode() {
        return productCodeAlternativeClassCode;
    }


    public void setProductCodeAlternativeClassCode(String value) {
        this.productCodeAlternativeClassCode = value;
    }

}
