
package com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eligCrtaValue", propOrder = {
    "value"
})
public class EligCrtaValue {

    protected String value;


    public String getValue() {
        return value;
    }


    public void setValue(String value) {
        this.value = value;
    }

}
