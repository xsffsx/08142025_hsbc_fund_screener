/*
 */
package com.hhhh.group.secwealth.mktdata.common.criteria;

/**
 * <p>
 * <b>  Insert description of the class's responsibility/role. </b>
 * </p>
 */
public class SortCriteriaValue {

    private String sortKey;

    private String sortOrder;

    public SortCriteriaValue() {}

    public SortCriteriaValue(final String sortKey, final String sortOrder) {
        this.sortKey = sortKey;
        this.sortOrder = sortOrder;
    }

    /**
     * @return the sortKey
     */
    public String getSortKey() {
        return this.sortKey;
    }

    /**
     * @param sortKey
     *            the sortKey to set
     */
    public void setSortKey(final String sortKey) {
        this.sortKey = sortKey;
    }

    /**
     * @return the sortOrder
     */
    public String getSortOrder() {
        return this.sortOrder;
    }

    /**
     * @param sortOrder
     *            the sortOrder to set
     */
    public void setSortOrder(final String sortOrder) {
        this.sortOrder = sortOrder;
    }


}
