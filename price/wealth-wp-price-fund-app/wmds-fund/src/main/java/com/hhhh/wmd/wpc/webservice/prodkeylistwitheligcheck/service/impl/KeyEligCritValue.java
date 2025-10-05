
package com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "keyEligCritValue", propOrder = {
    "key"
})
public class KeyEligCritValue {

    protected String key;


    public String getKey() {
        return key;
    }


    public void setKey(String value) {
        this.key = value;
    }

}
