/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "global.exception.handler.response")
@Getter
@Setter
public class GlobalExceptionHandlerProperties {

    private String path = "classpath:exception/";

    private String mapping = "exception-response-mapping.xml";

    private String config = "exception-response-config.xml";

    private String defaultExCode = "Undefined";

}
