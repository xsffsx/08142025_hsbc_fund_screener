
package com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "requestAttribute", propOrder = {
    "attributeName"
})
public class RequestAttribute {

    protected String attributeName;


    public String getAttributeName() {
        return attributeName;
    }


    public void setAttributeName(String value) {
        this.attributeName = value;
    }

}
