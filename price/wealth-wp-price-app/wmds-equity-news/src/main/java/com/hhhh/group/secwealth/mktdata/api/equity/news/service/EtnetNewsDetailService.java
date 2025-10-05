/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.news.service;

import com.google.gson.JsonObject;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.EtnetProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.news.constants.NewsConstant;
import com.hhhh.group.secwealth.mktdata.api.equity.news.request.NewsDetailRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.NewsDetailResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.etnet.EtnetNewsDetail;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Service
@ConditionalOnProperty(value = "service.newsDetail.Etnet.injectEnabled")
public class EtnetNewsDetailService
		extends AbstractBaseService<NewsDetailRequest, NewsDetailResponse, CommonRequestHeader> {

	private static final Logger logger = LoggerFactory.getLogger(EtnetNewsDetailService.class);

	@Autowired
	private EtnetProperties etnetProperties;

	@Autowired
	private HttpClientHelper httpClientHelper;

	@Override
	protected Object convertRequest(NewsDetailRequest newsDetailRequest, CommonRequestHeader header) throws Exception {
		String market = newsDetailRequest.getMarket();
		if (!NewsConstant.MARKET_HK.equals(market)) {
			logger.error("The market {} not support!", market);
			throw new VendorException(ExCodeConstant.EX_CODE_INPUT_PARAMETER_INVALID);
		}
		String locale = header.getLocale();
		String id = newsDetailRequest.getId();
		String source = newsDetailRequest.getSource();
		String token = etnetProperties.getEtnetTokenWithoutVerify();
		if (NewsConstant.Locale_ZH_CN.equals(locale)) {
			locale = NewsConstant.Locale_ZH;
		}
		List<NameValuePair> params = new ArrayList();
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("source", source));
		params.add(new BasicNameValuePair("locale", locale));
		params.add(new BasicNameValuePair("token", token));
		return params;
	}

	@Override
	protected Object execute(Object serviceRequest) {
		List<NameValuePair> request = (List<NameValuePair>) serviceRequest;

		String response;
		try {
			response = this.httpClientHelper.doGet(etnetProperties.getProxyName(), etnetProperties.getNewsDetailUrl(),
					request, null);
			JsonObject respJsonObj = JsonUtil.getAsJsonObject(response);
			String errCode = JsonUtil.getAsString(respJsonObj, "err_code");
			if (StringUtil.isValid(errCode) && EtnetProperties.EX_CODE_ETNET_INVALID_TOKEN_CODE.equals(errCode)) {
				List<NameValuePair> params = request;
				int index = 0;
				for (int i = 0; i < params.size(); i++) {
					if ("token".equals(params.get(i).getName())) {
						index = i;
					}
				}
				params.remove(index);
				params.add(new BasicNameValuePair("token", etnetProperties.getEtnetToken()));
				response = this.httpClientHelper.doGet(etnetProperties.getProxyName(),
						etnetProperties.getNewsDetailUrl(), params, null);
			}
		} catch (Exception e) {
			EtnetNewsDetailService.logger.error("Access Etnet encounter error", e);
			throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_ETNET_ERROR, e);
		}
		// }
		return response;
	}

	@Override
	protected Object validateServiceResponse(Object response) throws IOException {
		String serviceResponse = (String) response;

		if (serviceResponse == null) {
			logger.error("Invalid response: {}", serviceResponse);
			throw new VendorException(ExCodeConstant.EX_CODE_ETNET_INVALID_RESPONSE);
		}
		EtnetNewsDetail etnetNewsDetail = JsonUtil.readValue(serviceResponse, EtnetNewsDetail.class);
		String err_code = etnetNewsDetail.getErr_code();
		if (StringUtil.isValid(err_code)) {
			logger.error("Invalid response, status is incorrect: {}", err_code);
			if (StringUtil.isValid(err_code)) {
				etnetProperties.throwExceptionByErrorCode(err_code);
			}
		}
		return etnetNewsDetail;
	}

	@Override
	protected NewsDetailResponse convertResponse(Object object) throws Exception {
		EtnetNewsDetail etnetNewsDetail = (EtnetNewsDetail) object;
		NewsDetailResponse response = new NewsDetailResponse();
		response.setId(etnetNewsDetail.getId());
		response.setContent(etnetNewsDetail.getContent());
		response.setHeadline(etnetNewsDetail.getHeadline());
		response.setAsOfDateTime(etnetNewsDetail.getAsOfDateTime());
		return response;
	}
}
