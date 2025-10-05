/*
 */
package com.hhhh.group.secwealth.mktdata.starter.http_client_manager.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "httpclient")
@Getter
@Setter
public class HttpClientProperties {

    private String initDefaultExCode = "IllegalConfiguration";

    private String processDefaultExCode = "AccessVendorError";

    private String defaultHttpClientName = "";

}
