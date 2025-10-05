
package com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sortingCriteria", propOrder = {
    "sortField",
    "sortOrder"
})
public class SortingCriteria {

    protected String sortField;
    protected String sortOrder;


    public String getSortField() {
        return sortField;
    }


    public void setSortField(String value) {
        this.sortField = value;
    }


    public String getSortOrder() {
        return sortOrder;
    }


    public void setSortOrder(String value) {
        this.sortOrder = value;
    }

}
