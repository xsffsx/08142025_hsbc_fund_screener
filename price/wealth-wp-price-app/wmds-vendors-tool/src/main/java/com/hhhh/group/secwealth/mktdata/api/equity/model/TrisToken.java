/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.model;



public class TrisToken {

    private static final String SEPARATOR = "~~";

    private String appCode;

    private String countryCode;

    private String groupMember;

    private String channelId;

    private String customerId;

    private String closure;

    private String timezone;

    private String timestamp;

    public TrisToken() {}

    @Override
    public String toString() {
        return new StringBuilder().append(this.appCode).append(TrisToken.SEPARATOR).append(this.countryCode)
            .append(TrisToken.SEPARATOR).append(this.groupMember).append(TrisToken.SEPARATOR).append(this.channelId)
            .append(TrisToken.SEPARATOR).append(this.customerId).append(TrisToken.SEPARATOR).append(this.closure)
            .append(TrisToken.SEPARATOR).append(this.timezone).append(TrisToken.SEPARATOR).append(this.timestamp).toString();
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setGroupMember(String groupMember) {
        this.groupMember = groupMember;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setClosure(String closure) {
        this.closure = closure;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
