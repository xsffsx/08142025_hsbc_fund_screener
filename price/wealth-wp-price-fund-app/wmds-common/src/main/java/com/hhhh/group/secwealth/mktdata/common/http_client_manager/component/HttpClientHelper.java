/*
 * COPYRIGHT. hhhh HOLDINGS PLC 2018. ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it has been
 * provided. No part of it is to be reproduced, disassembled, transmitted,
 * stored in a retrieval system nor translated in any human or computer
 * language in any way or for any other purposes whatsoever without the prior
 * written consent of hhhh Holdings plc.
 */
package com.hhhh.group.secwealth.mktdata.common.http_client_manager.component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.hhhh.group.secwealth.mktdata.common.http_client_manager.constant.Constant;

public class HttpClientHelper {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientHelper.class);

    private String processDefaultExCode;

    private String defaultHttpClientName;

    private Map<String, CloseableHttpClient> httpClientMapper;

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

	public Map<String, CloseableHttpClient> getHttpClientMapper() {
		return httpClientMapper;
	}

	public void setHttpClientMapper(Map<String, CloseableHttpClient> httpClientMapper) {
		this.httpClientMapper = httpClientMapper;
	}

	public String doGet(final String url, final String params, final Map<String, String> headers) throws Exception {
        return doGet(this.defaultHttpClientName, url, params, headers);
    }

    public String doGet(final String httpClientName, final String url, final String params, final Map<String, String> headers)
        throws Exception {
        final StringBuilder sb = new StringBuilder(url);
        if (!StringUtils.isEmpty(params)) {
            sb.append(Constant.SYMBOL_INTERROGATION).append(params);
        }
        return sendGetRequest(httpClientName, headers, sb.toString());
    }

    public String doGet(final String url, final List<NameValuePair> params, final Map<String, String> headers) throws Exception {
        return doGet(this.defaultHttpClientName, url, params, headers);
    }

    public String doGet(final String httpClientName, final String url, final List<NameValuePair> params,
        final Map<String, String> headers) throws Exception {
        final StringBuilder sb = new StringBuilder(url);
        if (params != null && !params.isEmpty()) {
            sb.append(Constant.SYMBOL_INTERROGATION).append(URLEncodedUtils.format(params, StandardCharsets.UTF_8));
        }
        return sendGetRequest(httpClientName, headers, sb.toString());
    }

    private String sendGetRequest(final String httpClientName, final Map<String, String> headers, final String uri)
        throws Exception {
        HttpClientHelper.logger.info(Constant.OUTBOUND_MSG_PREFIX + uri);
        final HttpGet httpGet = new HttpGet(uri);
        if (headers != null && !headers.isEmpty()) {
            for (final Map.Entry<String, String> header : headers.entrySet()) {
                httpGet.addHeader(header.getKey(), header.getValue());
            }
        }
        return sendRequest(this.httpClientMapper.get(httpClientName), httpGet);
    }

    public String doPost(final String url, final String params, final Map<String, String> headers) throws Exception {
        return doPost(this.defaultHttpClientName, url, new StringEntity(params, StandardCharsets.UTF_8), headers);
    }

    public String doPost(final String httpClientName, final String url, final String params, final Map<String, String> headers)
        throws Exception {
        return doPost(httpClientName, url, new StringEntity(params, StandardCharsets.UTF_8), headers);
    }

    public String doPost(final String url, final List<NameValuePair> params, final Map<String, String> headers) throws Exception {
        return doPost(this.defaultHttpClientName, url, new UrlEncodedFormEntity(params, StandardCharsets.UTF_8), headers);
    }

    public String doPost(final String httpClientName, final String url, final List<NameValuePair> params,
        final Map<String, String> headers) throws Exception {
        return doPost(httpClientName, url, new UrlEncodedFormEntity(params, StandardCharsets.UTF_8), headers);
    }

    public String doPost(final String url, final HttpEntity httpEntity, final Map<String, String> headers) throws Exception {
        return doPost(this.defaultHttpClientName, url, httpEntity, headers);
    }

    public String doPost(final String httpClientName, final String url, final HttpEntity httpEntity,
        final Map<String, String> headers) throws Exception {
        HttpClientHelper.logger
            .info(Constant.OUTBOUND_MSG_PREFIX + url + Constant.SYMBOL_INTERROGATION + EntityUtils.toString(httpEntity));
        final HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(httpEntity);
        if (headers != null && !headers.isEmpty()) {
            for (final Map.Entry<String, String> header : headers.entrySet()) {
                httpPost.addHeader(header.getKey(), header.getValue());
            }
        }
        return sendRequest(this.httpClientMapper.get(httpClientName), httpPost);
    }

    private String sendRequest(final CloseableHttpClient httpClient, final HttpUriRequest httpUriRequest) throws Exception {
        try {
            final CloseableHttpResponse response = httpClient.execute(httpUriRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                HttpClientHelper.logger.error("Can't retrieve data from vendor, statusCode: " + statusCode + ", reason: "
                    + response.getStatusLine().getReasonPhrase());
                httpUriRequest.abort();
                throw new Exception(this.processDefaultExCode);
            }
            final HttpEntity entity = response.getEntity();
            final String result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            HttpClientHelper.logger.info(Constant.INBOUND_MSG_PREFIX + result);
            return result;
        } catch (IOException e) {
            HttpClientHelper.logger.error("Send request encounter error", e);
            throw new Exception(this.processDefaultExCode, e);
        }
    }

}
