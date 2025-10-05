/*
 * COPYRIGHT. hhhh HOLDINGS PLC 2018. ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it has been
 * provided. No part of it is to be reproduced, disassembled, transmitted,
 * stored in a retrieval system nor translated in any human or computer
 * language in any way or for any other purposes whatsoever without the prior
 * written consent of hhhh Holdings plc.
 */
package com.hhhh.group.secwealth.mktdata.common.http_client_manager.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Connection {

	@Value("#{systemConfig['connection.maxTotal']}")
    private Integer maxTotal;
	
	@Value("#{systemConfig['connection.maxPerRoute']}")
    private Integer maxPerRoute;

	@Value("#{systemConfig['connection.connectTimeout']}")
    private Integer connectTimeout;

	@Value("#{systemConfig['connection.socketTimeout']}")
    private Integer socketTimeout;

	@Value("#{systemConfig['connection.enableProxy']}")
    private Boolean enableProxy;

	@Value("#{systemConfig['connection.proxyHostname']}")
    private String proxyHostname;

	@Value("#{systemConfig['connection.proxyPort']}")
    private Integer proxyPort;

	@Value("#{systemConfig['connection.retryCount']}")
    private Integer retryCount;

	@Value("#{systemConfig['connection.enableGzip']}")
    private Boolean enableGzip;
	
	@Value("#{systemConfig['connection.defaultHttpClientName']}")
	private String defaultHttpClientName;

	public Integer getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(Integer maxTotal) {
		this.maxTotal = maxTotal;
	}

	public Integer getMaxPerRoute() {
		return maxPerRoute;
	}

	public void setMaxPerRoute(Integer maxPerRoute) {
		this.maxPerRoute = maxPerRoute;
	}

	public Integer getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public Integer getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(Integer socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public Boolean getEnableProxy() {
		return enableProxy;
	}

	public void setEnableProxy(Boolean enableProxy) {
		this.enableProxy = enableProxy;
	}

	public String getProxyHostname() {
		return proxyHostname;
	}

	public void setProxyHostname(String proxyHostname) {
		this.proxyHostname = proxyHostname;
	}

	public Integer getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

	public Boolean getEnableGzip() {
		return enableGzip;
	}

	public void setEnableGzip(Boolean enableGzip) {
		this.enableGzip = enableGzip;
	}

	public String getDefaultHttpClientName() {
		return defaultHttpClientName;
	}

	public void setDefaultHttpClientName(String defaultHttpClientName) {
		this.defaultHttpClientName = defaultHttpClientName;
	}
    
}
