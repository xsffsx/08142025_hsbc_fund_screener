/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.news.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.TrisTokenService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.ApplicationProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.TrisProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.news.constants.NewsConstant;
import com.hhhh.group.secwealth.mktdata.api.equity.news.request.NewsDetailRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.news.request.service.TrisNewsDetailServiceRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.NewsDetailResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.news.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.news.util.TrisPropsUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@ConditionalOnProperty(value = "service.newsDetail.Tris.injectEnabled")
public class TrisNewsDetailService extends AbstractBaseService<NewsDetailRequest, NewsDetailResponse, CommonRequestHeader> {

    private static final Logger logger = LoggerFactory.getLogger(TrisNewsDetailService.class);


    @Autowired
    private TrisProperties trisProps;

    @Autowired
    private ApplicationProperties appProps;

    @Autowired
    private TrisTokenService trisTokenService;

    @Autowired
    private HttpClientHelper httpClientHelper;

    @Override
    protected Object convertRequest(NewsDetailRequest newsDetailRequest, CommonRequestHeader header) throws Exception {
        TrisNewsDetailServiceRequest trisNewsDetailServiceRequest = new TrisNewsDetailServiceRequest();
        String id = newsDetailRequest.getId();
        String locale = header.getLocale();

        String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
        String closure = this.trisProps.getTrisClosure(site);
        String appCode = this.trisProps.getTrisTokenAppCode(site);
        String timezone = this.trisProps.getTrisTokenTimezone(site);

        //token
        String token = this.trisTokenService.getEncryptedTrisToken(site, header, appCode, closure, timezone);
        String[] keys = new String[]{site, appCode, NewsConstant.DETAIL_FILTER};
        ArgsHolder.putArgs(NewsConstant.KEYS, keys);
        List<String> filters = this.trisProps.getTrisFields(this.appProps.getNewsResponseTrisFields(keys), keys);
        if ("zh_CN".equalsIgnoreCase(locale)) {
            filters.add("_LANG_LZS");
        } else if ("zh_HK".equalsIgnoreCase(locale)) {
            filters.add("_LANG_LZT");
        }
        filters.add("NEWS_SRC");
        trisNewsDetailServiceRequest.setFilter(filters);
        List<String> items = new ArrayList<>();
        items.add(id);
        trisNewsDetailServiceRequest.setToken(token);
        trisNewsDetailServiceRequest.setItem(items);
        return trisNewsDetailServiceRequest;
    }

    @Override
    protected Object execute(Object serviceRequest) {
        TrisNewsDetailServiceRequest request = (TrisNewsDetailServiceRequest) serviceRequest;
        String response;
            try {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=UTF-8");
                response = this.httpClientHelper.doPost(this.trisProps.getNewsDetailUrl(), JsonUtil.toJson(request), headers);
            } catch (Exception e) {
                TrisNewsDetailService.logger.error("Access Tris encounter error", e);
                throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_TRIS_ERROR, e);
            }
        return response;
    }

    @Override
    protected Object validateServiceResponse(Object response) {
        String serviceResponse = (String) response;
        JsonArray jsonObject = this.validateEachServiceResponse(serviceResponse);
        return jsonObject;
    }


    private JsonArray validateEachServiceResponse(final String serviceResponse) {
        JsonObject respJsonObj = JsonUtil.getAsJsonObject(serviceResponse);
        if (respJsonObj == null) {
            logger.error("Invalid response: {}", serviceResponse);
            throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
        }
        String statusKey = this.trisProps.getResponseStatusCodeKey();
        String status = JsonUtil.getAsString(respJsonObj, statusKey);
        if (!this.trisProps.isCorrectResponseStatus(status)) {
            logger.error("Invalid response, status is incorrect: {}", serviceResponse);
            String exceptionName = NewsConstant.TRIS_ERROR_CODE_EXCEPTION_MAPPING.get(status);
            if (StringUtil.isValid(exceptionName)) {
                throw new VendorException(ExCodeConstant.EX_CODE_TRIS_UNDEFINED_ERROR);
            } else {
                throw new VendorException(exceptionName);
            }
        }
        JsonArray resultJsonArray = JsonUtil.getAsJsonArray(respJsonObj, "result");

        return resultJsonArray;
    }

    @Override
    protected NewsDetailResponse convertResponse(Object object) throws Exception {
        JsonArray resultJsonArray = (JsonArray) object;
        NewsDetailResponse response = new NewsDetailResponse();
        if (resultJsonArray != null && resultJsonArray.size() > 0) {
            String[] keys = (String[]) ArgsHolder.getArgs(NewsConstant.KEYS);
            String key = String.join(SymbolConstant.SYMBOL_VERTICAL_LINE, keys);
            TimeZone timeZone = TimeZone.getTimeZone(DateUtil.TIMEZONE_GMT8);
            TimeZone oldtimeZone = TimeZone.getTimeZone(DateUtil.TIMEZONE_GMT);
            for (JsonElement jsonElement : resultJsonArray) {
                JsonObject resultJsonObj = jsonElement.getAsJsonObject();
                response.setId(TrisPropsUtil.inOrderStrProps("id", key, jsonElement.getAsJsonObject()));
                response.setHeadline(TrisPropsUtil.inOrderStrProps("headline", key, resultJsonObj));
                response.setContent(TrisPropsUtil.inOrderStrProps("content", key, resultJsonObj));
                response.setContent(TrisPropsUtil.inOrderStrProps("content", key, resultJsonObj));
                String time = TrisPropsUtil.inOrderStrProps("asOfDateTime", key, resultJsonObj);
                String asOfDateTime = DateUtil.string2DateString(time,
                        DateUtil.DateFormat_yyyyMMddHHmmssSSS, DateUtil.DateFormat_yyyyMMddHHmmssSSSXXX, oldtimeZone, timeZone);
                response.setAsOfDateTime(asOfDateTime);
            }
        }
        return response;
    }
}
