/*
 */
package com.hhhh.group.secwealth.mktdata.common.criteria;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * <b> Min Max Value </b>
 * </p>
 */
public class MinMaxValue {

    private String criteriaKey;
    private Object minCriteria;
    private Object maxCriteria;

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
     * @return the minValue
     */
    public Object getMinCriteria() {
        return this.minCriteria;
    }

    /**
     * @param minValue
     *            the minValue to set
     */
    public void setMinCriteria(final Object minValue) {
        this.minCriteria = minValue;
    }

    /**
     * @return the maxValue
     */
    public Object getMaxCriteria() {
        return this.maxCriteria;
    }

    /**
     * @param maxValue
     *            the maxValue to set
     */
    public void setMaxCriteria(final Object maxValue) {
        this.maxCriteria = maxValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
