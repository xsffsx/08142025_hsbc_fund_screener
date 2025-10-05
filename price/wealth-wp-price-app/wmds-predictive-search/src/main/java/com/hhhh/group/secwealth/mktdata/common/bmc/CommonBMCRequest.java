/*
 */
package com.hhhh.group.secwealth.mktdata.common.bmc;

public class CommonBMCRequest implements BMCRequest {

    /** The country code. */
    private String countryCode;

    /** The group member. */
    private String groupMember;

    /** The err tracker code. */
    private String errTrackerCode;

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
     * Gets the err tracker code.
     * 
     * @return the errTrackerCode
     */
    public String getErrTrackerCode() {
        return this.errTrackerCode;
    }

    /**
     * Sets the err tracker code.
     * 
     * @param errTrackerCode
     *            the errTrackerCode to set
     */
    public void setErrTrackerCode(final String errTrackerCode) {
        this.errTrackerCode = errTrackerCode;
    }
}