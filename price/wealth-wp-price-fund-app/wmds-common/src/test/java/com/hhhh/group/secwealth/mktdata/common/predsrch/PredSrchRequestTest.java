package com.hhhh.group.secwealth.mktdata.common.predsrch;
import com.google.common.collect.Lists;


import com.hhhh.group.secwealth.mktdata.common.criteria.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.common.predsrch.request.PredSrchRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.Silent.class)
public class PredSrchRequestTest {

    @Test
    public void test(){
        PredSrchRequest predSrchRequest = new PredSrchRequest();
        predSrchRequest.setKeyword("");
        predSrchRequest.setAssetClasses(new String[5]);
        predSrchRequest.setSearchFields(new String[5]);
        predSrchRequest.setSortingFields(new String[5]);
        predSrchRequest.setMarket("");
        predSrchRequest.setFilterCriterias(new ArrayList<SearchCriteria>());
        predSrchRequest.setTopNum("");
        predSrchRequest.setChannelRestrictCode("");
        predSrchRequest.setRestrOnlScribInd("");

        predSrchRequest.getKeyword();
        predSrchRequest.getAssetClasses();
        predSrchRequest.getSearchFields();
        predSrchRequest.getSortingFields();
        predSrchRequest.getMarket();
        predSrchRequest.getFilterCriterias();
        predSrchRequest.getTopNum();
        predSrchRequest.getChannelRestrictCode();
        predSrchRequest.getRestrOnlScribInd();
        Assert.assertTrue(true);
    }
}
