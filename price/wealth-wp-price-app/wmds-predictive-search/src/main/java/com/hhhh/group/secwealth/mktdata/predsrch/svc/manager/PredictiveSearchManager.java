/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.svc.manager;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.SearchableObject;

public interface PredictiveSearchManager {

    public void loadData() throws Exception;

    public List<SearchableObject> findProductListByKey(final String searchClauses, final String[] sortingFields, final String site,
        final int topNum) throws Exception;

}