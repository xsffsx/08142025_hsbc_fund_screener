
package com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "responseDetails", propOrder = {
    "responseCode"
})
public class ResponseDetails {

    protected Integer responseCode;

    
    public Integer getResponseCode() {
        return responseCode;
    }

    
    public void setResponseCode(Integer value) {
        this.responseCode = value;
    }

}
