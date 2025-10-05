/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.token;


import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

import lombok.Setter;

@Setter
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

    public TrisToken(final CommonRequestHeader header) {
        this.countryCode = header.getCountryCode();
        this.groupMember = header.getGroupMember();
        this.channelId = header.getChannelId();
    }

    @Override
    public String toString() {
        return new StringBuilder().append(this.appCode).append(TrisToken.SEPARATOR).append(this.countryCode)
            .append(TrisToken.SEPARATOR).append(this.groupMember).append(TrisToken.SEPARATOR).append(this.channelId)
            .append(TrisToken.SEPARATOR).append(this.customerId).append(TrisToken.SEPARATOR).append(this.closure)
            .append(TrisToken.SEPARATOR).append(this.timezone).append(TrisToken.SEPARATOR).append(this.timestamp).toString();
    }

}
