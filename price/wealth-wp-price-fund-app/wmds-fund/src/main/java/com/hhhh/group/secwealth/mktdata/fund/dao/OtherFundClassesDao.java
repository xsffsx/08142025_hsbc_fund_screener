
package com.hhhh.group.secwealth.mktdata.fund.dao;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.OtherFundClassesRequest;


public interface OtherFundClassesDao {

    public List<UtProdInstm> getUtProdInstmList(OtherFundClassesRequest request) throws Exception ;

}
