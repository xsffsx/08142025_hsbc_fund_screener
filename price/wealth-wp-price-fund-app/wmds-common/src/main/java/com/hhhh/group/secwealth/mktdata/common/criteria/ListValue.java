/*
 */
package com.hhhh.group.secwealth.mktdata.common.criteria;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * <p>
 * <b> ListValue. </b>
 * </p>
 */
public class ListValue {

    private String criteriaKey;
    private Map<Object, Object> mapItems = new LinkedHashMap<Object, Object>();

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
     * @return the mapItems
     */
    public Map<Object, Object> getMapItems() {
        return this.mapItems;
    }

    /**
     * @param mapItems
     *            the mapItems to set
     */
    public void setMapItems(final Map<Object, Object> mapItems) {
        this.mapItems = mapItems;
    }

    public void addMapItem(final Object dbResultLine, final Object dbResultLine2) {
        this.mapItems.put(dbResultLine, dbResultLine2);
    }

    @Override
    public String toString() {
        return new StringBuilder("Criteria Key:").append(this.criteriaKey).append(" Value:").append(this.mapItems.toString())
            .toString();
    }

}
