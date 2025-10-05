/*
 */
package com.hhhh.group.secwealth.mktdata.wmds_prototype.header;

import org.hibernate.validator.constraints.NotEmpty;

import com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.header.Property;
import com.hhhh.group.secwealth.mktdata.starter.validation.annotation.RegEx;
import com.hhhh.group.secwealth.mktdata.starter.validation.annotation.SupportSite;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SupportSite(exist = "{app.site.support:ALL}")
public class CommonRequestHeader {

    @NotEmpty(message = "{validator.notEmpty.header.countryCode.message}")
    @Property(Constant.REQUEST_HEADER_KEY_COUNTRYCODE)
    private String countryCode;

    @NotEmpty(message = "{validator.notEmpty.header.groupMember.message}")
    @Property(Constant.REQUEST_HEADER_KEY_GROUP_MEMBER)
    private String groupMember;

    @NotEmpty(message = "{validator.notEmpty.header.channelId.message}")
    @Property(Constant.REQUEST_HEADER_KEY_CHANNEL_ID)
    @RegEx(regexp = "{validator.regex.header.channelId}", message = "{validator.regex.header.channelId.message}")
    private String channelId;

    @NotEmpty(message = "{validator.notEmpty.header.locale.message}")
    @Property(Constant.REQUEST_HEADER_KEY_LOCALE)
    @RegEx(regexp = "{validator.regex.header.locale}", message = "{validator.regex.header.locale.message}")
    private String locale;

    @NotEmpty(message = "{validator.notEmpty.header.appCode.message}")
    @Property(Constant.REQUEST_HEADER_KEY_APP_CODE)
    @RegEx(regexp = "{validator.regex.header.appCode}", message = "{validator.regex.header.appCode.message}")
    private String appCode;

    @Property(Constant.REQUEST_HEADER_KEY_SAML)
    private String samlString;
    
    @Property(Constant.REQUEST_HEADER_KEY_SAML3)
    private String saml3String;

    @Property(Constant.REQUEST_HEADER_KEY_JWT)
    private String jwtString;
    
    @Property(Constant.REQUEST_HEADER_KEY_FUNCTION_ID)
    private String functionId;

    @Property(Constant.REQUEST_HEADER_KEY_STAFF_ID)
    private String staffId;

    @Property(Constant.REQUEST_HEADER_KEY_CUSTOMER_ID)
    private String customerId;

    @Property(Constant.REQUEST_HEADER_KEY_SOURCE_SYSTEM_ID)
    private String sourceSystemId;
}
