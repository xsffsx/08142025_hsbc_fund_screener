
package com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "marketCriteria", propOrder = {
    "countryRecordCode",
    "groupMemberRecordCode"
})
public class MarketCriteria {

    protected String countryRecordCode;
    protected String groupMemberRecordCode;

    
    public String getCountryRecordCode() {
        return countryRecordCode;
    }

    
    public void setCountryRecordCode(String value) {
        this.countryRecordCode = value;
    }

    
    public String getGroupMemberRecordCode() {
        return groupMemberRecordCode;
    }

    
    public void setGroupMemberRecordCode(String value) {
        this.groupMemberRecordCode = value;
    }

}
