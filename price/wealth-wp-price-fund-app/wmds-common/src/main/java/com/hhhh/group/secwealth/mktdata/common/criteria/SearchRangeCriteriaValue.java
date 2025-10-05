/*
 */
package com.hhhh.group.secwealth.mktdata.common.criteria;

import java.util.List;

/**
 * <p>
 * <b> SearchRangeCriteriaValue. </b>
 * </p>
 */
public class SearchRangeCriteriaValue {

    private String criteriaKey;
    private List<SearchMinMaxCriteriaValue> criteriaValues;

    /**
     * @return the criteriaKey
     */
    public String getCriteriaKey() {
        return this.criteriaKey;
    }

    /**
     * @param criteriaKey
     *            the criteriaKey to set
     */
    public void setCriteriaKey(final String criteriaKey) {
        this.criteriaKey = criteriaKey;
    }

    /**
     * @return the criteriaValues
     */
    public List<SearchMinMaxCriteriaValue> getCriteriaValues() {
        return this.criteriaValues;
    }

    /**
     * @param criteriaValues
     *            the criteriaValues to set
     */
    public void setCriteriaValues(final List<SearchMinMaxCriteriaValue> criteriaValues) {
        this.criteriaValues = criteriaValues;
    }
}
