/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.properties;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.vendor.tris.ResponseStatus;
import com.hhhh.group.secwealth.mktdata.api.equity.common.token.EtnetToken;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.handler.BMCComponent;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.IllegalConfigurationException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.NameValuePair;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * <b> Provides a series of methods to get Etnet related attribute values. </b>
 * </p>
 */
@Component
@ConfigurationProperties(prefix = "etnet")
@ConditionalOnProperty(value = "service.properties.Etnet.injectEnabled")
@Getter
@Setter
public class EtnetProperties {

	private static final Logger logger = LoggerFactory.getLogger(EtnetProperties.class);

	private static final String EX_CODE_ETNET_AUTHENTICATION_FAIL_CODE = "1";
	public static final String EX_CODE_ETNET_INVALID_TOKEN_CODE = "2";
	private static final String EX_CODE_ETNET_INVALID_PARAM_CODE = "4";
	private static final String EX_CODE_ETNET_SERVER_ERROR_CODE = "3";

	private String getTokenUrl;

	private String validateTokenUrl;

	private String quoteUrl;

	private String indicesUrl;

	private String topmoversUrl;

	private String chartDataUrl;

	private String newsHeadlinesUrl;

	private String newsDetailUrl;
	
	private String listedipoUrl;

	private String currentipoUrl;

	private String asharesStockUrl;

	private String proxyName;

	private ResponseStatus responseStatus;

	@Autowired
	private HttpClientHelper httpClientHelper;

	@Autowired
	private BMCComponent bmcComponent;

	private Map<String, String> headersForEtnetAuth = new HashMap<>();

	public String getEtnetQuoteUrl() {
		if (StringUtil.isInValid(this.quoteUrl)) {
			EtnetProperties.logger
					.error("Please check your configuration: \"etnet.QuoteUrl\", the returned value is invalid");
			throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_MIDFS_ILLEGAL_CONFIGURATION);
		}
		return this.quoteUrl;
	}

	public String getEtnetIndicesUrl() {
		if (StringUtil.isInValid(this.indicesUrl)) {
			EtnetProperties.logger
					.error("Please check your configuration: \"etnet.IndicesUrl\", the returned value is invalid");
			throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_MIDFS_ILLEGAL_CONFIGURATION);
		}
		return this.indicesUrl;
	}

	public String getEtnetTopMoversUrl() {
		if (StringUtil.isInValid(this.topmoversUrl)) {
			EtnetProperties.logger
					.error("Please check your configuration: \"etnet.TopMoversUrl\", the returned value is invalid");
			throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_MIDFS_ILLEGAL_CONFIGURATION);
		}
		return this.topmoversUrl;
	}

	public boolean isCorrectResponseStatus(final String responseCode) {
		return this.responseStatus.getCorrectStatus().equals(responseCode);
	}

	// @Autowired
	// private OatProperties oatProperties;

	public String getEtnetToken() throws Exception {
		try {
			// if (oatProperties.getEtnetFlag()){
			// return "57ad9b14dd0a440444446595fcf381c21dc9cb8a17dc257403d79ff1e43c11b7";
			// }else{
			String token = EtnetToken.getTokenWithLock();
			if (StringUtil.isInValid(token)) {
				return sendRequestToGetToken();
			}
			Boolean requireGetToken = sendRequestToVerifyToken(token);
			if (requireGetToken) {
				return sendRequestToGetToken();
			} else {
				return token;
			}
			// }
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.error("ETNet Undefined error", e);
				throw new VendorException(ExCodeConstant.EX_CODE_ETNET_UNDERFINED_ERROR, e);
			} else if (e instanceof ConnectTimeoutException) {
				logger.error("Access ETNet error", e);
				throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_ETNET_ERROR, e);
			} else {
				logger.error("ETNet server error", e);
				throw new VendorException(ExCodeConstant.EX_CODE_ETNET_SERVER_ERROR, e);
			}
		}
	}

	public void throwExceptionByErrorCode(String errCode) {
		switch (errCode) {
		case EX_CODE_ETNET_AUTHENTICATION_FAIL_CODE:
			throw new VendorException(ExCodeConstant.EX_CODE_ETNET_AUTHENTICATION_FAIL);
		case EX_CODE_ETNET_INVALID_TOKEN_CODE:
			throw new VendorException(ExCodeConstant.EX_CODE_ETNET_INVALID_TOKEN);
		case EX_CODE_ETNET_SERVER_ERROR_CODE:
			throw new VendorException(ExCodeConstant.EX_CODE_ETNET_SERVER_ERROR);
		case EX_CODE_ETNET_INVALID_PARAM_CODE:
			throw new VendorException(ExCodeConstant.EX_CODE_ETNET_INVALID_PARAM);
		default:
			throw new VendorException(ExCodeConstant.EX_CODE_ETNET_UNDERFINED_ERROR);
		}
	}

	private Boolean sendRequestToVerifyToken(String token) throws Exception {
		List<NameValuePair> params = new ArrayList();
		params.add(new BasicNameValuePair("token", token));
		String verifyTokenJSONString = httpClientHelper.doGet(this.proxyName, this.validateTokenUrl, params,
				headersForEtnetAuth);
		JsonObject jsonObject = JsonUtil.getAsJsonObject(verifyTokenJSONString);
		JsonElement requireGetToken = jsonObject.get("requireGetToken");
		if (requireGetToken == null) {
			logger.error("Invalid response{}", verifyTokenJSONString);
			throw new VendorException(ExCodeConstant.EX_CODE_ETNET_INVALID_RESPONSE);
		}
		return jsonObject.get("requireGetToken").getAsBoolean();
	}

	public String sendRequestToGetToken() throws Exception {
		String tokenJSONString = httpClientHelper.doGet(this.proxyName, this.getTokenUrl, "", headersForEtnetAuth);
		JsonObject jsonObject = JsonUtil.getAsJsonObject(tokenJSONString);
		String token = JsonUtil.getAsString(jsonObject, "token");
		if (StringUtil.isInValid(token)) {
			logger.error("Invalid response{}", tokenJSONString);
			throw new VendorException(ExCodeConstant.EX_CODE_ETNET_INVALID_RESPONSE);
		}
		EtnetToken.setTokenWithLock(token);
		return token;
	}

	public String getEtnetTokenWithoutVerify() throws Exception {
		try {
			// if (oatProperties.getEtnetFlag()){
			// return "57ad9b14dd0a440444446595fcf381c21dc9cb8a17dc257403d79ff1e43c11b7";
			// }else{
			String token = EtnetToken.getTokenWithLock();
			if (StringUtil.isInValid(token)) {
				return sendRequestToGetToken();
			}
			return token;
			// }
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.error("ETNet Undefined error", e);
				throw new VendorException(ExCodeConstant.EX_CODE_ETNET_UNDERFINED_ERROR, e);
			} else if (e instanceof ConnectTimeoutException) {
				logger.error("Access ETNet error", e);
				throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_ETNET_ERROR, e);
			} else {
				logger.error("ETNet server error", e);
				throw new VendorException(ExCodeConstant.EX_CODE_ETNET_SERVER_ERROR, e);
			}
		}
	}

}
