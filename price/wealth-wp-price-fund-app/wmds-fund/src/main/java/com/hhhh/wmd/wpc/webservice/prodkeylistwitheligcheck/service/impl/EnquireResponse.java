
package com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "enquireResponse", propOrder = {
    "_return"
})
public class EnquireResponse {

    @XmlElement(name = "return")
    protected ProductKeyListWithEligibilityCheckResponse _return;


    public ProductKeyListWithEligibilityCheckResponse getReturn() {
        return _return;
    }


    public void setReturn(ProductKeyListWithEligibilityCheckResponse value) {
        this._return = value;
    }

}
