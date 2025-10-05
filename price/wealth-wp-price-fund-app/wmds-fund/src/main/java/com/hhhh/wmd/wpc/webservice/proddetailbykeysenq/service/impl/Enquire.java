
package com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "enquire", propOrder = {
    "request"
})
public class Enquire {

    protected WpcProductDetailByKeysEnquiryRequest request;

    public WpcProductDetailByKeysEnquiryRequest getRequest() {
        return request;
    }


    public void setRequest(WpcProductDetailByKeysEnquiryRequest value) {
        this.request = value;
    }

}
