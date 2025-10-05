package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.EtnetProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.IPORequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.CurrentIPOResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.EtnetErrResponse;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

@Service("currentIPOService")
public class CurrentIPOService extends AbstractBaseService<IPORequest, CurrentIPOResponse, CommonRequestHeader> {

	private static final Logger logger = LoggerFactory.getLogger(ListedIPOService.class);

	// @Autowired
	// private OatProperties oatProperties;

	@Autowired
	private EtnetProperties etnetProperties;

	@Autowired
	private HttpClientHelper httpClientHelper;

	@Override
	protected Object convertRequest(IPORequest request, CommonRequestHeader header) throws Exception {
		String locale = getLocale(header);
		String token = etnetProperties.getEtnetTokenWithoutVerify();

		List<NameValuePair> params = new ArrayList();
		params.add(new BasicNameValuePair("locale", locale));
		params.add(new BasicNameValuePair("token", token));
		return params;
	}

	@Override
	protected Object execute(Object request) throws Exception {
		List<NameValuePair> requestParams = (List<NameValuePair>) request;
		String response;
		try {
			response = this.httpClientHelper.doGet(etnetProperties.getProxyName(), etnetProperties.getCurrentipoUrl(),
					requestParams, null);
			JsonObject respJsonObj = JsonUtil.getAsJsonObject(response);
			String errCode = JsonUtil.getAsString(respJsonObj, "err_code");
			if (StringUtil.isValid(errCode) && EtnetProperties.EX_CODE_ETNET_INVALID_TOKEN_CODE.equals(errCode)) {
				List<NameValuePair> params = requestParams;
				int index = 0;
				for (int i = 0; i < params.size(); i++) {
					if ("token".equals(params.get(i).getName())) {
						index = i;
					}
				}
				params.remove(index);
				params.add(new BasicNameValuePair("token", etnetProperties.getEtnetToken()));
				response = this.httpClientHelper.doGet(etnetProperties.getProxyName(),
						etnetProperties.getCurrentipoUrl(), params, null);
			}
		} catch (Exception e) {
			throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_ETNET_ERROR, e);
		}
		// }
		return response;
	}

	@Override
	protected Object validateServiceResponse(Object response) {
		String serviceResponse = (String) response;
		if (serviceResponse == null) {
			logger.error("Invalid response: {}", serviceResponse);
			throw new VendorException(ExCodeConstant.EX_CODE_ETNET_INVALID_RESPONSE);
		}
		CurrentIPOResponse currentIPOResponse = JsonUtil.fromJson(serviceResponse, CurrentIPOResponse.class);
		EtnetErrResponse etnetErrResponse = JsonUtil.fromJson(serviceResponse, EtnetErrResponse.class);
		String err_code = etnetErrResponse.getErr_code();
		if (StringUtil.isValid(err_code)) {
			logger.error("Invalid response, status is incorrect: {}", err_code);
			if (StringUtil.isValid(err_code)) {
				etnetProperties.throwExceptionByErrorCode(err_code);
			}
		}
		return currentIPOResponse;
	}

	@Override
	protected CurrentIPOResponse convertResponse(Object response) throws Exception {
		CurrentIPOResponse listedIPOResponse = (CurrentIPOResponse) response;
		return listedIPOResponse;
	}

	private String getLocale(CommonRequestHeader header) {
		String locale = header.getLocale();
		switch (locale) {
		case "zh_HK":
			return "zh_HK";
		case "zh_CN":
			return "zh";
		default:
			return "en";
		}
	}

}
