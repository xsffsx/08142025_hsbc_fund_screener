
package com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "enquire", propOrder = {
    "request"
})
public class Enquire {

    protected ProductKeyListWithEligibilityCheckRequest request;


    public ProductKeyListWithEligibilityCheckRequest getRequest() {
        return request;
    }


    public void setRequest(ProductKeyListWithEligibilityCheckRequest value) {
        this.request = value;
    }

}
