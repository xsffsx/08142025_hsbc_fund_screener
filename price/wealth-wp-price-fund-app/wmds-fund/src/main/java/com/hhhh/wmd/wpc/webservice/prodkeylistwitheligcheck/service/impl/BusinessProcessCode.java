
package com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "businessProcessCode", propOrder = {
    "businessProcessCode"
})
public class BusinessProcessCode {

    protected String businessProcessCode;


    public String getBusinessProcessCode() {
        return businessProcessCode;
    }


    public void setBusinessProcessCode(String value) {
        this.businessProcessCode = value;
    }

}
