
package com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "keyValueCriteriaWithIndex", propOrder = {
    "keyValueWithIndex",
    "value"
})
public class KeyValueCriteriaWithIndex {

    protected KeyValueWithIndex keyValueWithIndex;
    @XmlElement(nillable = true)
    protected List<Value> value;


    public KeyValueWithIndex getKeyValueWithIndex() {
        return keyValueWithIndex;
    }


    public void setKeyValueWithIndex(KeyValueWithIndex value) {
        this.keyValueWithIndex = value;
    }


    public List<Value> getValue() {
        if (value == null) {
            value = new ArrayList<Value>();
        }
        return this.value;
    }

}
