
package com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "productKeyObjectListJsonString", propOrder = {
    "prodKeyObjListJsonStr"
})
public class ProductKeyObjectListJsonString {

    protected String prodKeyObjListJsonStr;


    public String getProdKeyObjListJsonStr() {
        return prodKeyObjListJsonStr;
    }


    public void setProdKeyObjListJsonStr(String value) {
        this.prodKeyObjListJsonStr = value;
    }

}
