
package com.hhhh.group.secwealth.mktdata.fund.beans.request;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.svc.request.Request;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;


public class FundHoldingsDiversifiRequest extends Request {

    private List<ProductKey> productKeys;

    
    public List<ProductKey> getProductKeys() {
        return this.productKeys;
    }

    
    public void setProductKeys(final List<ProductKey> productKeys) {
        this.productKeys = productKeys;
    }

}
