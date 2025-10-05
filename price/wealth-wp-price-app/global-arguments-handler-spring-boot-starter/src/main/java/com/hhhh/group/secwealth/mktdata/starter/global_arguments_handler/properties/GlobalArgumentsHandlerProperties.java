/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_arguments_handler.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "global.arguments.handler")
@Getter
@Setter
public class GlobalArgumentsHandlerProperties {

    private String patterns = "SITE~~X-hhhh-Chnl-CountryCode~~<_>~~X-hhhh-Chnl-Group-Member~~HEADER";

    private String urlPatterns = "/wealth/api/v1/market-data/**";

    private String defaultExCode = "IllegalConfiguration";

}
