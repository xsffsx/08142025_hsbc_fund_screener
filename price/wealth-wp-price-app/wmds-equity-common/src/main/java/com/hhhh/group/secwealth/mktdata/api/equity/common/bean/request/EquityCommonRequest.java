/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.bean.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquityCommonRequest {

    /**
     * processingFlag: whether fetch the user id info from saml taken or not. If "SKIP".equalsIgnoreCase(processingFlag) is true,
     * will do not fetch user id from saml token.
     */
    private String processingFlag;
}
