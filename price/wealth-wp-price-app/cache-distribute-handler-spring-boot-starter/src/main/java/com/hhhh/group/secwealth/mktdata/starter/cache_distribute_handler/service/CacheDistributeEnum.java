/*
 */
package com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.service;

/**
 * <p>
 * <b> Interested request parameters to the Cache Distribute. </b>
 * </p>
 */
public enum CacheDistributeEnum {

    X_hhhh_USER_ID("X-hhhh-User-Id"), X_hhhh_SESSION_CORRELATION_ID("X-hhhh-Session-Correlation-Id"), X_hhhh_SAML(
        "X-hhhh-Saml"), X_hhhh_SAML3("X-hhhh-Saml3"), X_hhhh_E2E_TRUST_TOKEN("x-hhhh-e2e-trust-token"),X_hhhh_SOURCE_SYSTEM_ID("X-hhhh-Source-System-Id"),X_hhhh_APP_CODE("X-hhhh-App-Code");

    private String value;

    private CacheDistributeEnum(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
