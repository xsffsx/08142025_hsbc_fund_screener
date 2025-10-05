
package com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "filterCriteriaFormula", propOrder = {
    "filterFormula"
})
public class FilterCriteriaFormula {

    protected String filterFormula;


    public String getFilterFormula() {
        return filterFormula;
    }


    public void setFilterFormula(String value) {
        this.filterFormula = value;
    }

}
