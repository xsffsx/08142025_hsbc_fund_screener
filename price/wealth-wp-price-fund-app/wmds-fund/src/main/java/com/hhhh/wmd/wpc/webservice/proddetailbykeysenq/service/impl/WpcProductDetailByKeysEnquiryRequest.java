
package com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wpcProductDetailByKeysEnquiryRequest", propOrder = {
    "channelCode",
    "countryCode",
    "groupMemberCode",
    "localeCode",
    "productKey",
    "requestAttribute",
    "sortingCriteriaList",
    "createIndicatorList"
})
public class WpcProductDetailByKeysEnquiryRequest {

    protected String channelCode;
    protected String countryCode;
    protected String groupMemberCode;
    protected String localeCode;
    protected List<ProductKey> productKey;
    protected List<RequestAttribute> requestAttribute;
    protected List<SortingCriteria> sortingCriteriaList;
    protected List<ProductTypeIndicator> createIndicatorList;


    public String getChannelCode() {
        return channelCode;
    }


    public void setChannelCode(String value) {
        this.channelCode = value;
    }


    public String getCountryCode() {
        return countryCode;
    }


    public void setCountryCode(String value) {
        this.countryCode = value;
    }


    public String getGroupMemberCode() {
        return groupMemberCode;
    }


    public void setGroupMemberCode(String value) {
        this.groupMemberCode = value;
    }


    public String getLocaleCode() {
        return localeCode;
    }


    public void setLocaleCode(String value) {
        this.localeCode = value;
    }


    public List<ProductKey> getProductKey() {
        if (productKey == null) {
            productKey = new ArrayList<ProductKey>();
        }
        return this.productKey;
    }


    public List<RequestAttribute> getRequestAttribute() {
        if (requestAttribute == null) {
            requestAttribute = new ArrayList<RequestAttribute>();
        }
        return this.requestAttribute;
    }


    public List<SortingCriteria> getSortingCriteriaList() {
        if (sortingCriteriaList == null) {
            sortingCriteriaList = new ArrayList<SortingCriteria>();
        }
        return this.sortingCriteriaList;
    }


    public List<ProductTypeIndicator> getCreateIndicatorList() {
        if (createIndicatorList == null) {
            createIndicatorList = new ArrayList<ProductTypeIndicator>();
        }
        return this.createIndicatorList;
    }

}
