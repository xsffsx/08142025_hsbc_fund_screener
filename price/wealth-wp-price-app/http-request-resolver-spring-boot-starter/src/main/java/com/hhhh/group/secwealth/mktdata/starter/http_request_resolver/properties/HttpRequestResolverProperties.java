/*
 */
package com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "http.request.resolver")
@Getter
@Setter
public class HttpRequestResolverProperties {

    private String defaultExCode = "InvalidRequest";

}
