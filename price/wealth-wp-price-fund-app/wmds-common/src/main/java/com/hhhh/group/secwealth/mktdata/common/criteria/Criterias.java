package com.hhhh.group.secwealth.mktdata.common.criteria;


/**
 * 
 * <p>
 * <b> SearchCriteria. </b>
 * </p>
 */
public class Criterias {

    private String criteriaKey;

    private Boolean criteriaValue;

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
    public Boolean getCriteriaValue() {
        return this.criteriaValue;
    }

    /**
     * @param criteriaValue
     *            the criteriaValue to set
     */
    public void setCriteriaValue(final Boolean criteriaValue) {
        this.criteriaValue = criteriaValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "Criterias [criteriaKey=" + this.criteriaKey + ", criteriaValue=" + this.criteriaValue + "]";
    }

}
