
package com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reasonCode", propOrder = {
    "reasonCode",
    "trackingNumber"
})
public class ReasonCode {

    protected String reasonCode;
    protected String trackingNumber;


    public String getReasonCode() {
        return reasonCode;
    }


    public void setReasonCode(String value) {
        this.reasonCode = value;
    }


    public String getTrackingNumber() {
        return trackingNumber;
    }


    public void setTrackingNumber(String value) {
        this.trackingNumber = value;
    }

}
