/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "global.bmc.handler")
@Getter
@Setter
public class GlobalBMCHandlerProperties {

    private String path = "classpath:bmc/";

    private String mapping = "bmc-mapping.xml";

    private String config = "bmc-config.xml";

    private String entityKey = "SITE";

    private String defaultEntityName = "Default";

    private String prefixMessage = "BMC_LOG=";

    private String interceptResponse = "com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.ExResponse";

    private String interceptKey = "exception,exCode,traceCode";

    private String defaultExKey = "Undefined";

}
