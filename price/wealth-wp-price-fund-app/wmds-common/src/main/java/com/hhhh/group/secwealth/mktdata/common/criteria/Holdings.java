/*
 */
package com.hhhh.group.secwealth.mktdata.common.criteria;


/**
 * <p>
 * <b> SearchRangeCriteriaValue. </b>
 * </p>
 */
public class Holdings {

    private String criteriaKey;
    private Boolean criteriaValue;
    private Integer top;
    private Boolean others;

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

    /**
     * @return the top
     */
    public Integer getTop() {
        return this.top;
    }

    /**
     * @param top
     *            the top to set
     */
    public void setTop(final Integer top) {
        this.top = top;
    }

    /**
     * @return the others
     */
    public Boolean getOthers() {
        return this.others;
    }

    /**
     * @param others
     *            the others to set
     */
    public void setOthers(final Boolean others) {
        this.others = others;
    }

}
