
package com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "productAttribute", propOrder = {
    "attributeName",
    "attributeValue"
})
public class ProductAttribute {

    protected String attributeName;
    protected List<String> attributeValue;


    public String getAttributeName() {
        return attributeName;
    }


    public void setAttributeName(String value) {
        this.attributeName = value;
    }


    public List<String> getAttributeValue() {
        if (attributeValue == null) {
            attributeValue = new ArrayList<String>();
        }
        return this.attributeValue;
    }

}
