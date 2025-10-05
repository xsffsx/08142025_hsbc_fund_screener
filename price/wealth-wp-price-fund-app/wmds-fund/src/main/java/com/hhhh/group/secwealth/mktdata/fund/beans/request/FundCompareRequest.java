
package com.hhhh.group.secwealth.mktdata.fund.beans.request;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.svc.request.Request;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;


public class FundCompareRequest extends Request {

    private List<ProductKey> ProductKeys;


    public List<ProductKey> getProductKeys() {
        return this.ProductKeys;
    }


    public void setProductKeys(final List<ProductKey> productKeys) {
        this.ProductKeys = productKeys;
    }
}
