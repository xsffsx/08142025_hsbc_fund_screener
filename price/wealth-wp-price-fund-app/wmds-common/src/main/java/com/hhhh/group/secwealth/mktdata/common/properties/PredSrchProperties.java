/*
 * COPYRIGHT. hhhh HOLDINGS PLC 2018. ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it has been
 * provided. No part of it is to be reproduced, disassembled, transmitted,
 * stored in a retrieval system nor translated in any human or computer
 * language in any way or for any other purposes whatsoever without the prior
 * written consent of hhhh Holdings plc.
 */
package com.hhhh.group.secwealth.mktdata.common.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.predsrch.request.PredSrchRequest;

@Component
public class PredSrchProperties {

	@Value("#{systemConfig['predsrch.url']}")
    private String url;
    
	@Value("#{systemConfig['predsrch.internalUrl']}")
    private String internalUrl;
	
	@Value("#{systemConfig['predsrch.bodyPrefix']}")
    private String bodyPrefix;

    private PredSrchRequest requestParams;

    public String getPredSrchUrl() {
        return this.url;
    }
    
    public String getInternalUrl() {
        return this.internalUrl;
    }

    public String getPredSrchBodyPrefix() {
        return this.bodyPrefix;
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBodyPrefix() {
		return bodyPrefix;
	}

	public void setBodyPrefix(String bodyPrefix) {
		this.bodyPrefix = bodyPrefix;
	}

	public PredSrchRequest getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(PredSrchRequest requestParams) {
		this.requestParams = requestParams;
	}

	public void setInternalUrl(String internalUrl) {
		this.internalUrl = internalUrl;
	}
    
}
