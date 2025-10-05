/*
 */
package com.hhhh.group.secwealth.mktdata.quote.dao;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.dto.InternalProductKey;

import java.util.List;
import java.util.Map;

public interface FundQuoteDao {

    public abstract List<UtProdInstm> search(List<InternalProductKey> ipkList, Map<String, String> headers) throws Exception;

    public Map<Integer, List<String>> searchChanlFund(String chanlRestCde) throws Exception;

}