/*
 */
package com.hhhh.group.secwealth.mktdata.common.criteria;

/**
 * <p>
 * <b> SearchMinMaxCriteriaValue </b>
 * </p>
 */
public class SearchMinMaxCriteriaValue {

    private String minOperator;
    private Number minCriteriaLimit;
    private String maxOperator;
    private Number maxCriteriaLimit;

    /**
     * @return the minOperator
     */
    public String getMinOperator() {
        return this.minOperator;
    }

    /**
     * @param minOperator
     *            the minOperator to set
     */
    public void setMinOperator(final String minOperator) {
        this.minOperator = minOperator;
    }

    /**
     * @return the minCriteriaLimit
     */
    public Number getMinCriteriaLimit() {
        return this.minCriteriaLimit;
    }

    /**
     * @param minCriteriaLimit
     *            the minCriteriaLimit to set
     */
    public void setMinCriteriaLimit(final Number minCriteriaLimit) {
        this.minCriteriaLimit = minCriteriaLimit;
    }

    /**
     * @return the maxOperator
     */
    public String getMaxOperator() {
        return this.maxOperator;
    }

    /**
     * @param maxOperator
     *            the maxOperator to set
     */
    public void setMaxOperator(final String maxOperator) {
        this.maxOperator = maxOperator;
    }

    /**
     * @return the maxCriteriaLimit
     */
    public Number getMaxCriteriaLimit() {
        return this.maxCriteriaLimit;
    }

    /**
     * @param maxCriteriaLimit
     *            the maxCriteriaLimit to set
     */
    public void setMaxCriteriaLimit(final Number maxCriteriaLimit) {
        this.maxCriteriaLimit = maxCriteriaLimit;
    }

}
