
package com.hhhh.group.secwealth.mktdata.fund.beans.request;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.criteria.Criterias;
import com.hhhh.group.secwealth.mktdata.common.svc.request.Request;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;


public class FundListRequest extends Request {

    private List<ProductKey> productKeys;
    private List<Criterias> criterias;
    private String channelRestrictCode;


    public List<ProductKey> getProductKeys() {
        return this.productKeys;
    }


    public void setProductKeys(final List<ProductKey> productKeys) {
        this.productKeys = productKeys;
    }


    public List<Criterias> getCriterias() {
        return this.criterias;
    }


    public void setCriterias(final List<Criterias> criterias) {
        this.criterias = criterias;
    }


    public String getChannelRestrictCode() {
        return this.channelRestrictCode;
    }


    public void setChannelRestrictCode(final String channelRestrictCode) {
        this.channelRestrictCode = channelRestrictCode;
    }

}