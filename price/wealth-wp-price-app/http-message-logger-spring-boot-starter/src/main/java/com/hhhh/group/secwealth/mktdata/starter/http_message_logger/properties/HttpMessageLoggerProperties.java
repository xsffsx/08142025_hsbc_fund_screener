/*
 */
package com.hhhh.group.secwealth.mktdata.starter.http_message_logger.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "http.message.logger")
@Getter
@Setter
public class HttpMessageLoggerProperties {

    private String urlPatterns = "/wealth/api/v1/market-data/**";

}
