
package com.hhhh.group.secwealth.mktdata.fund.dao;

import java.util.List;


public interface CategoryOverviewDao {


    public List<Object[]> getCategoryOverviewList(String productType, String locale, String countryCode, String groupMember,
        String channelRestrictCode, String restrOnlScribInd) throws Exception;

}
