/*
 * COPYRIGHT. hhhh HOLDINGS PLC 2018. ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it has been
 * provided. No part of it is to be reproduced, disassembled, transmitted,
 * stored in a retrieval system nor translated in any human or computer
 * language in any way or for any other purposes whatsoever without the prior
 * written consent of hhhh Holdings plc.
 */
package com.hhhh.group.secwealth.mktdata.common.http_client_manager.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HttpClientProperties {

    private String initDefaultExCode = "IllegalConfiguration";

    private String processDefaultExCode = "AccessVendorError";
    
    @Value("#{systemConfig['httpclient.defaultHttpClientName']}")
    private String defaultHttpClientName;

	public String getInitDefaultExCode() {
		return initDefaultExCode;
	}

	public void setInitDefaultExCode(String initDefaultExCode) {
		this.initDefaultExCode = initDefaultExCode;
	}

	public String getProcessDefaultExCode() {
		return processDefaultExCode;
	}

	public void setProcessDefaultExCode(String processDefaultExCode) {
		this.processDefaultExCode = processDefaultExCode;
	}

	public String getDefaultHttpClientName() {
		return defaultHttpClientName;
	}

	public void setDefaultHttpClientName(String defaultHttpClientName) {
		this.defaultHttpClientName = defaultHttpClientName;
	}
    
}
