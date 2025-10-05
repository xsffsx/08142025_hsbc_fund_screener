
package com.hhhh.group.secwealth.mktdata.fund.dao;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;

import java.util.List;
import java.util.Map;


public interface AdvanceChartDao {

    public List<UtProdInstm> getUtProdInstmList(final List<ProductKey> productKeys, final Map<String, String> headers) throws Exception;


    public List<UtProdInstm> getEnableCacheProdInstmList() throws Exception;

}
