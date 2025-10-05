/*
 */
package com.hhhh.group.secwealth.mktdata.starter.http_client_manager.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Connection {

    private Integer maxTotal;

    private Integer maxPerRoute;

    private Integer connectTimeout;

    private Integer socketTimeout;

    private Boolean enableProxy;

    private String proxyHostname;

    private Integer proxyPort;

    private Integer retryCount;

    private Boolean enableGzip;

}
