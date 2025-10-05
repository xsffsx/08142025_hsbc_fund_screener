
package com.hhhh.group.secwealth.mktdata.fund.dao;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.fund.beans.helper.TopAndBottomCategoryChartResponse;
import com.hhhh.group.secwealth.mktdata.fund.constants.TimeScale;



public interface TopAndBottmCatChartServiceDao {

    public TopAndBottomCategoryChartResponse searchUtProdCatList(int index, List<String> productTypes, List<String> productSubTypes,
        List<String> countryCriterias, List<String> categoryCodeList, TimeScale timeScale, Integer limit) throws Exception;

}