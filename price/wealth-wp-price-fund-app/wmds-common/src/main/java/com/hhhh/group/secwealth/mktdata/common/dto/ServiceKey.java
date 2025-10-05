/*
 */
package com.hhhh.group.secwealth.mktdata.common.dto;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;

/**
 * The Class ServiceKey.
 */
public class ServiceKey {

    /** The service id. */
    private String serviceId;

    /** The service type. */
    private String serviceType;

    /** The country code. */
    private String countryCode;

    /** The group member. */
    private String groupMember;

    /** The countryTradableCode. */
    private String countryTradableCode;

    /** The product type. */
    private String productType;

    /**
     * Instantiates a new service key.
     */
    public ServiceKey() {
        super();
    }

    /**
     * Instantiates a new service key.
     * 
     * @param serviceId
     *            the service id
     */
    public ServiceKey(final String serviceId) {
        super();
        this.serviceId = serviceId;
    }

    /**
     * Instantiates a new service key.
     * 
     * @param serviceId
     *            the service id
     * @param serviceType
     *            the service type
     */
    public ServiceKey(final String serviceId, final String serviceType) {
        super();
        this.serviceId = serviceId;
        this.serviceType = serviceType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(this.serviceId);
        if (StringUtil.isValid(this.serviceType)) {
            sb.append(CommonConstants.SYMBOL_UNDERLINE).append(this.serviceType);
        }
        if (StringUtil.isValid(this.countryCode)) {
            sb.append(CommonConstants.SYMBOL_UNDERLINE).append(this.countryCode);
        }
        if (StringUtil.isValid(this.groupMember)) {
            sb.append(CommonConstants.SYMBOL_UNDERLINE).append(this.groupMember);
        }
        if (StringUtil.isValid(this.countryTradableCode)) {
            sb.append(CommonConstants.SYMBOL_UNDERLINE).append(this.countryTradableCode);
        }
        if (StringUtil.isValid(this.productType)) {
            sb.append(CommonConstants.SYMBOL_UNDERLINE).append(this.productType);
        }
        return sb.toString();
    }

    /**
     * Gets the service id.
     * 
     * @return the serviceId
     */
    public String getServiceId() {
        return this.serviceId;
    }

    /**
     * Sets the service id.
     * 
     * @param serviceId
     *            the serviceId to set
     */
    public void setServiceId(final String serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * Gets the service type.
     * 
     * @return the serviceType
     */
    public String getServiceType() {
        return this.serviceType;
    }

    /**
     * Sets the service type.
     * 
     * @param serviceType
     *            the serviceType to set
     */
    public void setServiceType(final String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * @return the countryCode
     */
    public String getCountryCode() {
        return this.countryCode;
    }

    /**
     * @param countryCode
     *            the countryCode to set
     */
    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * @return the groupMember
     */
    public String getGroupMember() {
        return this.groupMember;
    }

    /**
     * @param groupMember
     *            the groupMember to set
     */
    public void setGroupMember(final String groupMember) {
        this.groupMember = groupMember;
    }

    /**
     * @return the productType
     */
    public String getProductType() {
        return this.productType;
    }

    /**
     * @param productType
     *            the productType to set
     */
    public void setProductType(final String productType) {
        this.productType = productType;
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
