/*
 */
package com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectionProperties {

    private Integer maxTotal = 100;

    private Integer maxPerRoute = 100;

    private Integer connectTimeout = 1000;

    private Integer socketTimeout = 500;

    private Boolean enableProxy = false;

    private String proxyHostname;

    private Integer proxyPort;

    private Integer retryCount = 2;

    private Boolean enableGzip = false;

}
