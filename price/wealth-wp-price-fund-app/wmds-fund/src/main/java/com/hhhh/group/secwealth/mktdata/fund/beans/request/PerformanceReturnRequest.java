
package com.hhhh.group.secwealth.mktdata.fund.beans.request;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.svc.request.Request;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;


public class PerformanceReturnRequest extends Request {

    private List<ProductKey> productKeys;
    private String currency;
    private String indicate;



    public List<ProductKey> getProductKeys() {
        return this.productKeys;
    }


    public void setProductKeys(final List<ProductKey> productKeys) {
        this.productKeys = productKeys;
    }


    public String getCurrency() {
        return this.currency;
    }


    public void setCurrency(final String currency) {
        this.currency = currency;
    }


    public String getIndicate() {
        return this.indicate;
    }


    public void setIndicate(final String indicate) {
        this.indicate = indicate;
    }

}