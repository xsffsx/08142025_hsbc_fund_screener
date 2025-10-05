package com.hhhh.group.secwealth.mktdata.common.criteria;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * <p>
 * <b> SearchCriteria. </b>
 * </p>
 */
public class SearchCriteria {

    private String criteriaKey;

    private String criteriaValue;

    private String operator;

    public SearchCriteria() {}

    public SearchCriteria(final String criteriaKey, final String criteriaValue, final String operator) {
        this.criteriaKey = criteriaKey;
        this.criteriaValue = criteriaValue;
        this.operator = operator;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).appendSuper(super.toString())
            .append("criteriaKey", this.criteriaKey).append("criteriaValue", this.criteriaValue).append("operator", this.operator)
            .toString();
    }

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
     * @return the criteriaValue
     */
    public String getCriteriaValue() {
        return this.criteriaValue;
    }

    /**
     * @param criteriaValue
     *            the criteriaValue to set
     */
    public void setCriteriaValue(final String criteriaValue) {
        this.criteriaValue = criteriaValue;
    }

    /**
     * @return the operator
     */
    public String getOperator() {
        return this.operator;
    }

    /**
     * @param operator
     *            the operator to set
     */
    public void setOperator(final String operator) {
        this.operator = operator;
    }

}
