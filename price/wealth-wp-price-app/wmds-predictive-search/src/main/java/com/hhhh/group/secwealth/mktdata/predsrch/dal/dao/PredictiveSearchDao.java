/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.dal.dao;

import java.util.List;

import org.compass.core.CompassTemplate;

import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.SearchableObject;


public interface PredictiveSearchDao {

    public List<SearchableObject> findProductListByKey(final String searchString, final String[] sortingFields, final String site,
        final int topNum) throws Exception;

    public void refreshIndex(final String siteKey, final List<SearchableObject> data) throws Exception;

    public void createIndex(final String siteKey, final List<SearchableObject> data) throws Exception;

    public void save(CompassTemplate template, final List<SearchableObject> data) throws Exception;
}