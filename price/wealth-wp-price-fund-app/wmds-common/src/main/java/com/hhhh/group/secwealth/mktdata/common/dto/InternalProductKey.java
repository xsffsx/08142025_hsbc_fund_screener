/*
 */
package com.hhhh.group.secwealth.mktdata.common.dto;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * The Class InternalProductKey.
 */
public class InternalProductKey implements Comparable<Object> {

    /** The Constant for countryTradableCode. */
    public static final String HTTP_PARAM_MARKET = "market";

    /** The Constant HTTP_PARAM_PRODUCTTYPE. */
    public static final String HTTP_PARAM_PRODUCTTYPE = "productType";

    /** The Constant HTTP_PARAM_PRODCDEALTCLASSCDE. */
    public static final String HTTP_PARAM_PRODCDEALTCLASSCDE = "prodCdeAltClassCde";

    /** The Constant HTTP_PARAM_PRODALTNUM. */
    public static final String HTTP_PARAM_PRODALTNUM = "prodAltNum";

    public static final String HTTP_PARAM_PRODUCTKEYS = "productKeys";

    public static final String HTTP_PARAM_INTERNAL_PRODUCTKEY = "internalProductKey";

    public static final String HTTP_PARAM_INTERNAL_PRODUCTKEYS = "internalProductKeys";

    public static final String HTTP_PARAM_COUNTRYCODE = "countryCode";

    public static final String HTTP_PARAM_GROUPMEMBER = "groupMember";

    /** The country code. */
    private String countryCode;

    /** The group member. */
    private String groupMember;

    /** The countryTradableCode. */
    private String countryTradableCode;

    /** The product type. */
    private String productType;

    /** The prod cde alt class cde. */
    private String prodCdeAltClassCde;

    /** The prod alt num. */
    private String prodAltNum;

    /**
     * Instantiates a new internal product key.
     */
    public InternalProductKey() {}

    /**
     * Instantiates a new internal product key.
     * 
     * @param countryCode
     *            the country code
     * @param groupMember
     *            the group member
     * @param countryTradableCode
     *            the countryTradableCode
     * @param productType
     *            the product type
     * @param prodCdeAltClassCde
     *            the prod cde alt class cde
     * @param prodAltNum
     *            the prod alt num
     */
    public InternalProductKey(final String countryCode, final String groupMember, final String countryTradableCde,
        final String productType, final String prodCdeAltClassCde, final String prodAltNum) {
        super();
        this.countryCode = countryCode;
        this.groupMember = groupMember;
        this.countryTradableCode = countryTradableCde;
        this.productType = productType;
        this.prodCdeAltClassCde = prodCdeAltClassCde;
        this.prodAltNum = prodAltNum;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof InternalProductKey)) {
            return false;
        }
        InternalProductKey castOther = (InternalProductKey) other;
        return new EqualsBuilder().append(this.countryCode, castOther.countryCode).append(this.groupMember, castOther.groupMember)
            .append(this.countryTradableCode, castOther.countryTradableCode).append(this.productType, castOther.productType)
            .append(this.prodCdeAltClassCde, castOther.prodCdeAltClassCde).append(this.prodAltNum, castOther.prodAltNum).isEquals();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.countryCode).append(this.groupMember).append(this.countryTradableCode)
            .append(this.productType).append(this.prodCdeAltClassCde).append(this.prodAltNum).toHashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final Object other) {
        InternalProductKey castOther = (InternalProductKey) other;
        return new CompareToBuilder().append(this.countryCode, castOther.countryCode)
            .append(this.groupMember, castOther.groupMember).append(this.countryTradableCode, castOther.countryTradableCode)
            .append(this.productType, castOther.productType).append(this.prodCdeAltClassCde, castOther.prodCdeAltClassCde)
            .append(this.prodAltNum, castOther.prodAltNum).toComparison();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("countryCode", this.countryCode)
            .append("groupMember", this.groupMember).append("countryTradableCode", this.countryTradableCode)
            .append("productType", this.productType).append("prodCdeAltClassCde", this.prodCdeAltClassCde)
            .append("prodAltNum", this.prodAltNum).toString();
    }

    /**
     * Gets the country code.
     * 
     * @return the countryCode
     */
    public String getCountryCode() {
        return this.countryCode;
    }

    /**
     * Sets the country code.
     * 
     * @param countryCode
     *            the countryCode to set
     */
    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * Gets the group member.
     * 
     * @return the groupMember
     */
    public String getGroupMember() {
        return this.groupMember;
    }

    /**
     * Sets the group member.
     * 
     * @param groupMember
     *            the groupMember to set
     */
    public void setGroupMember(final String groupMember) {
        this.groupMember = groupMember;
    }

    /**
     * Gets the product type.
     * 
     * @return the productType
     */
    public String getProductType() {
        return this.productType;
    }

    /**
     * Sets the product type.
     * 
     * @param productType
     *            the productType to set
     */
    public void setProductType(final String productType) {
        this.productType = productType;
    }

    /**
     * Gets the prod cde alt class cde.
     * 
     * @return the prodCdeAltClassCde
     */
    public String getProdCdeAltClassCde() {
        return this.prodCdeAltClassCde;
    }

    /**
     * Sets the prod cde alt class cde.
     * 
     * @param prodCdeAltClassCde
     *            the prodCdeAltClassCde to set
     */
    public void setProdCdeAltClassCde(final String prodCdeAltClassCde) {
        this.prodCdeAltClassCde = prodCdeAltClassCde;
    }

    /**
     * Gets the prod alt num.
     * 
     * @return the prodAltNum
     */
    public String getProdAltNum() {
        return this.prodAltNum;
    }

    /**
     * Sets the prod alt num.
     * 
     * @param prodAltNum
     *            the prodAltNum to set
     */
    public void setProdAltNum(final String prodAltNum) {
        this.prodAltNum = prodAltNum;
    }

    /**
     * @return the countryTradableCode
     */
    public String getCountryTradableCode() {
        return this.countryTradableCode;
    }

    /**
     * @param countryTradableCode
     *            the countryTradableCode to set
     */
    public void setCountryTradableCode(final String countryTradableCode) {
        this.countryTradableCode = countryTradableCode;
    }

}
