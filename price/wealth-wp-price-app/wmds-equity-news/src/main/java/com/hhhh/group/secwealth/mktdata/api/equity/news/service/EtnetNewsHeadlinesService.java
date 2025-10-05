/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.news.service;

import com.google.gson.JsonObject;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.EtnetProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.news.constants.NewsConstant;
import com.hhhh.group.secwealth.mktdata.api.equity.news.request.NewsHeadlinesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.News;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.NewsHeadlinesResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.etnet.EtnetNews;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.etnet.EtnetNewsHeadlines;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@ConditionalOnProperty(value = "service.newsHeadlines.Etnet.injectEnabled")
public class EtnetNewsHeadlinesService
		extends AbstractBaseService<NewsHeadlinesRequest, NewsHeadlinesResponse, CommonRequestHeader> {

	private static final Logger logger = LoggerFactory.getLogger(EtnetNewsHeadlinesService.class);

	@Autowired
	private EtnetProperties etnetProperties;

	@Autowired
	private HttpClientHelper httpClientHelper;

	@Override
	protected Object convertRequest(NewsHeadlinesRequest request, CommonRequestHeader header) throws Exception {
		String market = request.getMarket();
		if (!NewsConstant.MARKET_HK.equals(market)) {
			logger.error("The market {} not support!", market);
			throw new CommonException(ExCodeConstant.EX_CODE_INPUT_PARAMETER_INVALID);
		}
		String category = request.getCategory();
		List<String> symbols = request.getSymbol();
		Integer recordsPerPage = request.getRecordsPerPage();
		Integer pageId = request.getPageId();
		if(recordsPerPage>20){
			throw new CommonException(ExCodeConstant.INPUT_PARAMETER_INVALID);
		}

		String locale = header.getLocale();
		if (NewsConstant.Locale_ZH_CN.equals(locale)) {
			locale = NewsConstant.Locale_ZH;
		}
		String token = etnetProperties.getEtnetTokenWithoutVerify();

		List<NameValuePair> params = new ArrayList();
		params.add(new BasicNameValuePair("category", category));
		if (NewsConstant.CATEGORY_RELATEDNEWS.equals(category)) {
			if (symbols != null && symbols.size() > 0) {
				params.add(new BasicNameValuePair("symbol", converSymbol(symbols)));
			} else {
				throw new VendorException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
			}
		}
		params.add(new BasicNameValuePair("recordsPerPage", String.valueOf(recordsPerPage)));
		params.add(new BasicNameValuePair("pageId", String.valueOf(pageId)));
		params.add(new BasicNameValuePair("locale", locale));
		params.add(new BasicNameValuePair("token", token));
		return params;
	}

	public String converSymbol(List<String> symbols) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < symbols.size(); i++) {
			sb.append(symbols.get(i));
			if (i < symbols.size() - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	@Override
	protected Object execute(Object request) throws Exception {
		List<NameValuePair> requestParams = (List<NameValuePair>) request;
		String response;
		try {
			response = this.httpClientHelper.doGet(etnetProperties.getProxyName(),
					etnetProperties.getNewsHeadlinesUrl(), requestParams, null);
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
						etnetProperties.getNewsHeadlinesUrl(), params, null);
			}
		} catch (Exception e) {
			throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_ETNET_ERROR, e);
		}
		return response;
	}

	@Override
	protected Object validateServiceResponse(Object response) {
		String serviceResponse = (String) response;
		if (serviceResponse == null) {
			logger.error("Invalid response: {}", serviceResponse);
			throw new VendorException(ExCodeConstant.EX_CODE_ETNET_INVALID_RESPONSE);
		}
		EtnetNewsHeadlines etnetNewsHeadlines = JsonUtil.fromJson(serviceResponse, EtnetNewsHeadlines.class);
		String err_code = etnetNewsHeadlines.getErr_code();
		if (StringUtil.isValid(err_code)&&!"4".equals(err_code)) {
			logger.error("Invalid response, status is incorrect: {}", err_code);
			if (StringUtil.isValid(err_code)) {
				etnetProperties.throwExceptionByErrorCode(err_code);
			}
		}

		return etnetNewsHeadlines;
	}

	@Override
	protected NewsHeadlinesResponse convertResponse(Object response) throws Exception {
		EtnetNewsHeadlines etnetNewsHeadlines = (EtnetNewsHeadlines) response;

		NewsHeadlinesResponse newsHeadlinesResponse = new NewsHeadlinesResponse();
		List<EtnetNews> etnetNewsList = etnetNewsHeadlines.getNewsList();
		List<News> newsList = new ArrayList<>();
		if (etnetNewsList != null && etnetNewsList.size() > 0) {
			for (EtnetNews etnetNews : etnetNewsList) {
				News news = new News();
				news.setId(etnetNews.getId());
				news.setBrief(etnetNews.getBrief());
				news.setAsOfDateTime(etnetNews.getAsOfDateTime());
				news.setHeadline(etnetNews.getHeadline());
				news.setRelatedCodes(etnetNews.getRelatedCodes());
				news.setSource(etnetNews.getSource());
				newsList.add(news);
			}
		}
		newsHeadlinesResponse.setNewsList(newsList);
		return newsHeadlinesResponse;
	}
}
