
package com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "keyValueWithIndex", propOrder = {
    "index",
    "keyType",
    "key",
    "operator",
    "valueType"
})
public class KeyValueWithIndex {

    protected String index;
    protected String keyType;
    protected String key;
    protected String operator;
    protected String valueType;


    public String getIndex() {
        return index;
    }


    public void setIndex(String value) {
        this.index = value;
    }


    public String getKeyType() {
        return keyType;
    }


    public void setKeyType(String value) {
        this.keyType = value;
    }


    public String getKey() {
        return key;
    }


    public void setKey(String value) {
        this.key = value;
    }


    public String getOperator() {
        return operator;
    }


    public void setOperator(String value) {
        this.operator = value;
    }


    public String getValueType() {
        return valueType;
    }


    public void setValueType(String value) {
        this.valueType = value;
    }

}
