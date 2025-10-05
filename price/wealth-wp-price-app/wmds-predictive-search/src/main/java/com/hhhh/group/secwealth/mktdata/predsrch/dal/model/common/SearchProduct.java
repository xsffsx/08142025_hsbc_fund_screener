/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common;


/**
 * The Class SearchProduct.
 */
public class SearchProduct {


    /** The External key. */
    private String externalKey;

    /** The prodCdeAltClassCde. */
    private String prodCdeAltClassCde;

    /** The SearchableObject. */
    private SearchableObject searchObject;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SearchProduct [externalKey=");
        builder.append(this.externalKey);
        builder.append(", prodCdeAltClassCde=");
        builder.append(this.prodCdeAltClassCde);
        builder.append(", searchObject=");
        /* builder.append(this.searchObject); */
        builder.append("]");
        return builder.toString();
    }

    /**
     * @return externalKey
     */
    public String getExternalKey() {
        return this.externalKey;
    }

    /**
     * @param externalKey
     *            The name to be set externalKey
     */
    public void setExternalKey(final String externalKey) {
        this.externalKey = externalKey;
    }

    /**
     * @return searchObject
     */

    public SearchableObject getSearchObject() {
        return this.searchObject;
    }

    /**
     * @param searchObject
     *            The name to be set searchObject
     */

    public void setSearchObject(final SearchableObject searchObject) {
        this.searchObject = searchObject;
    }


    /**
     * @return prodCdeAltClassCde
     */
    public String getProdCdeAltClassCde() {
        return this.prodCdeAltClassCde;
    }

    /**
     * @param prodCdeAltClassCde
     *            The name to be set prodCdeAltClassCde
     */
    public void setProdCdeAltClassCde(final String prodCdeAltClassCde) {
        this.prodCdeAltClassCde = prodCdeAltClassCde;
    }

}