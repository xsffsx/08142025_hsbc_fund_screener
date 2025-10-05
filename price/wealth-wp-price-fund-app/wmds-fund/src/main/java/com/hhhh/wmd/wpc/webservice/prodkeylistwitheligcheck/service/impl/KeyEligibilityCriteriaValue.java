
package com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "keyEligibilityCriteriaValue", propOrder = {
    "keyEligCritValue",
    "eligCrtaValue"
})
public class KeyEligibilityCriteriaValue {

    protected KeyEligCritValue keyEligCritValue;
    @XmlElement(nillable = true)
    protected List<EligCrtaValue> eligCrtaValue;


    public KeyEligCritValue getKeyEligCritValue() {
        return keyEligCritValue;
    }


    public void setKeyEligCritValue(KeyEligCritValue value) {
        this.keyEligCritValue = value;
    }


    public List<EligCrtaValue> getEligCrtaValue() {
        if (eligCrtaValue == null) {
            eligCrtaValue = new ArrayList<EligCrtaValue>();
        }
        return this.eligCrtaValue;
    }

}
