
package com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "productKeyListWithEligibilityCheckResponse", propOrder = {
    "responseDetails",
    "reasonCode",
    "productKeyObjectListJsonString"
})
public class ProductKeyListWithEligibilityCheckResponse {

    protected ResponseDetails responseDetails;
    @XmlElement(nillable = true)
    protected List<ReasonCode> reasonCode;
    protected ProductKeyObjectListJsonString productKeyObjectListJsonString;


    public ResponseDetails getResponseDetails() {
        return responseDetails;
    }


    public void setResponseDetails(ResponseDetails value) {
        this.responseDetails = value;
    }


    public List<ReasonCode> getReasonCode() {
        if (reasonCode == null) {
            reasonCode = new ArrayList<ReasonCode>();
        }
        return this.reasonCode;
    }


    public ProductKeyObjectListJsonString getProductKeyObjectListJsonString() {
        return productKeyObjectListJsonString;
    }


    public void setProductKeyObjectListJsonString(ProductKeyObjectListJsonString value) {
        this.productKeyObjectListJsonString = value;
    }

}
