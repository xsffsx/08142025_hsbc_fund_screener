
package com.hhhh.group.secwealth.mktdata.fund.dao;

import java.util.Date;
import java.util.List;

import com.hhhh.group.secwealth.mktdata.fund.beans.helper.TopAndBottomCategoryResponse;
import com.hhhh.group.secwealth.mktdata.fund.constants.TimeScale;



public interface TopAndBottmMufCatServiceDao {

    public TopAndBottomCategoryResponse searchTopAndBottomList(int index, List<String> productTypes, List<String> productSubTypes,
        List<String> countryCriterias, String assetClassCode, TimeScale timeScale, Integer limit, String channelRestrictCode,
        String restrOnlScribInd) throws Exception;

    public Date searchPerformanceTableLastUpdateDate(String channelRestrictCode, String restrOnlScribInd) throws Exception;

}