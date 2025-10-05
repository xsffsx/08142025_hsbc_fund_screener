/**
 * 
 */
package com.hhhh.group.secwealth.mktdata.common.criteria;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * The Class SearchListCriteriaItem.
 */
public class SearchListCriteriaItem {

    /** The item key. */
    private Object itemKey;

    /** The item value. */
    private Object itemValue;

    /** The item parent. */
    private Object parent;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).appendSuper(super.toString()).append("itemKey", this.itemKey)
            .append("itemValue", this.itemValue).append("parent", this.parent).toString();
    }

    /**
     * Gets the item key.
     * 
     * @return the itemKey
     */
    public Object getItemKey() {
        return this.itemKey;
    }

    /**
     * Sets the item key.
     * 
     * @param itemKey
     *            the itemKey to set
     */
    public void setItemKey(final Object itemKey) {
        this.itemKey = itemKey;
    }

    /**
     * Gets the item value.
     * 
     * @return the itemValue
     */
    public Object getItemValue() {
        return this.itemValue;
    }

    /**
     * Sets the item value.
     * 
     * @param itemValue
     *            the itemValue to set
     */
    public void setItemValue(final Object itemValue) {
        this.itemValue = itemValue;
    }

    /**
     * @return the parent
     */
    public Object getParent() {
        return this.parent;
    }

    /**
     * @param parent
     *            the parent to set
     */
    public void setParent(final Object parent) {
        this.parent = parent;
    }
}
