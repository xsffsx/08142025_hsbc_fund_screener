/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.news.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.TrisTokenService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.ApplicationProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.TrisProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.news.constants.NewsConstant;
import com.hhhh.group.secwealth.mktdata.api.equity.news.request.NewsHeadlinesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.news.request.service.TrisNewsHeadlinesServiceRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.News;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.NewsHeadlinesResponse;
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
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

@Service
@ConditionalOnProperty(value = "service.newsHeadlines.Tris.injectEnabled")
public class TrisNewsHeadlinesService extends AbstractBaseService<NewsHeadlinesRequest, NewsHeadlinesResponse, CommonRequestHeader> {

    private static final Logger logger = LoggerFactory.getLogger(TrisNewsHeadlinesService.class);

    @Autowired
    private TrisProperties trisProps;

    @Autowired
    private ApplicationProperties appProps;

    @Autowired
    private TrisTokenService trisTokenService;

    @Autowired
    private HttpClientHelper httpClientHelper;

    @Autowired
    private PredSrchService predSrchService;

    @Override
    protected Object convertRequest(NewsHeadlinesRequest request, CommonRequestHeader header) {
        TrisNewsHeadlinesServiceRequest trisNewsHeadlinesServiceRequest = buildServiceRequest();
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER, header);
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_QUOTES_REQUEST, request);

        String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
        String closure = this.trisProps.getTrisClosure(site);
        String appCode = this.trisProps.getTrisTokenAppCode(site);
        String timezone = this.trisProps.getTrisTokenTimezone(site);

        String locale = header.getLocale();
        Integer recordsPerPage = request.getRecordsPerPage();
        Integer pageId = request.getPageId();
        String query = "";

        //validate
        if (recordsPerPage <= 0 || recordsPerPage > 10 || pageId <= 0 || pageId > 10) {
            logger.error("the recordsPerPage or pageId more than 10");
            throw new VendorException(ExCodeConstant.EX_CODE_INPUT_PARAMETER_INVALID);
        }

        //token
        String token = this.trisTokenService.getEncryptedTrisToken(site, header, appCode, closure, timezone);

        //filters
        String[] keys = new String[]{site, appCode, NewsConstant.HEADLINE_FILTER};
        ArgsHolder.putArgs(NewsConstant.KEYS, keys);
        List<String> filters = this.trisProps.getTrisFields(this.appProps.getNewsResponseTrisFields(keys), keys);
        if ("zh_CN".equalsIgnoreCase(locale)) {
            filters.add("_LANG_LZS");
        } else if ("zh_HK".equalsIgnoreCase(locale)) {
            filters.add("_LANG_LZT");
        }
        //query
        query = getCNQuery(request, header);

        trisNewsHeadlinesServiceRequest.setQuery(query);
        trisNewsHeadlinesServiceRequest.setToken(token);
        trisNewsHeadlinesServiceRequest.setClosure(closure);
        trisNewsHeadlinesServiceRequest.addFilter(filters);
        if (request.getSymbol() != null && request.getSymbol().size() >= 2) {
            trisNewsHeadlinesServiceRequest.setNumResult(10);
        } else {
            trisNewsHeadlinesServiceRequest.setNumResult(pageId * recordsPerPage);
        }
        return trisNewsHeadlinesServiceRequest;
    }

    private TrisNewsHeadlinesServiceRequest buildServiceRequest() {
        TrisNewsHeadlinesServiceRequest serviceRequest = new TrisNewsHeadlinesServiceRequest();
        serviceRequest.setTotalHitCount(true);
        serviceRequest.setHighlight(false);
        serviceRequest.setTeaser(true);
        serviceRequest.setResultOffset(0);
        serviceRequest.setSortby(NewsConstant.SORT_BY);
        return serviceRequest;
    }

    private List<String> removeNullStringList(List<String> oriList) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < oriList.size(); i++) {
            if (oriList.get(i) != null && StringUtil.isValid(oriList.get(i))) {
                list.add(oriList.get(i));
            }
        }
        return list;
    }


    private String getCNQuery(NewsHeadlinesRequest request, CommonRequestHeader header) {
        List<String> symbols = request.getSymbol();
        String productCodeIndicator = request.getProductCodeIndicator();
        String category = request.getCategory();
        String locale = header.getLocale();
        String query = null;

        List<String> ricCode = new ArrayList<>();
        if (Constant.PROD_CDE_ALT_CLASS_CODE_M.equals(productCodeIndicator)) {
            if (symbols != null) {
                symbols = removeNullStringList(symbols);
                request.setSymbol(symbols);
                if (symbols.size() > 0) {
                    if (symbols.size() > 20) {
                        logger.error("the symbols size more than 20!");
                        throw new VendorException(ExCodeConstant.EX_CODE_INPUT_PARAMETER_INVALID);
                    }
                    List<PredSrchResponse> predSrchResponses = predSrchService.precSrch(symbols, header);
                    for (PredSrchResponse predSrchRespons : predSrchResponses) {
                        for (ProdAltNumSeg prodAltNumSeg : predSrchRespons.getProdAltNumSegs()) {
                            if (Constant.PROD_CDE_ALT_CLASS_CODE_T.equals(prodAltNumSeg.getProdCdeAltClassCde())) {
                                ricCode.add(prodAltNumSeg.getProdAltNum());
                            }
                        }
                    }
                    request.setSymbol(ricCode);
                }
            }
        }
        if (category.equalsIgnoreCase(NewsConstant.MARKET_CN_CATEGORY_TN)) {
            query = this.assembleTNQuery(NewsConstant.TOP_NEWS_QUERY_PATTERN, locale, request);
        } else if (category.equalsIgnoreCase(NewsConstant.MARKET_CN_CATEGORY_LN)) {
            query = this.assembleLNQuery(NewsConstant.LATEST_NEWS_QUERY_PATTERN, locale, request);
        }
        return query;
    }

    private String assembleTNQuery(String queryPattern, String locale, NewsHeadlinesRequest request) {
        StringBuffer queryBuffer = new StringBuffer(256).append(queryPattern);
        String headline = request.getHeadline();
        String content = request.getContent();
        List<String> ricCodeList = request.getSymbol();

        if (StringUtil.isValid(locale) && locale.equalsIgnoreCase("en")) {
            queryBuffer.append(" AND (topics:BRV AND topics:LEN)");
        }
        if (StringUtil.isValid(locale) && locale.equalsIgnoreCase("zh_CN") || locale.equalsIgnoreCase("zh_HK")) {
            queryBuffer.append(" AND (topics:CNEWS)");
        }
        if (StringUtil.isValid(headline)) {
            queryBuffer.append(" AND (headline:HEADLINE)".replace("HEADLINE", headline));
        }
        if (StringUtil.isValid(content)) {
            queryBuffer.append(" AND (content:\"CONTENT\")".replace("CONTENT", content));
        }

        // handle the multiple stocks TODO
//        if (null != ricCodeList && !ricCodeList.isEmpty()) {
//            int listSize = ricCodeList.size();
//            int index = 0;
//
//            StringBuffer ricCodeCriteriasBuf = new StringBuffer(256);
//            while (index <= listSize - 1) {
//                String ricCode = ricCodeList.get(index);
//                if (StringUtil.isValid(ricCode)) {
//                    if (index == listSize - 1) {
//                        ricCodeCriteriasBuf.append("(companies:RIC_CODE)".replace("RIC_CODE", ricCode));
//                    } else {
//                        ricCodeCriteriasBuf.append("(companies:RIC_CODE) OR ".replace("RIC_CODE", ricCode));
//                    }
//                }
//                index++;
//            }
//
//            String ricCodeCriterias = ricCodeCriteriasBuf.toString();
//            queryBuffer.append("AND (RIC_CODE_CRITERIAS)".replace("RIC_CODE_CRITERIAS", ricCodeCriterias));
//        }
        return queryBuffer.toString();
    }

    private String assembleLNQuery(String queryPattern, String locale, NewsHeadlinesRequest request) {
        StringBuffer queryBuffer = new StringBuffer(256).append(queryPattern);
        String headline = request.getHeadline();
        String content = request.getContent();
        List<String> ricCodeList = request.getSymbol();

        if (StringUtil.isValid(locale) && locale.equalsIgnoreCase("en")) {
            queryBuffer.append(" AND (topics:LEN)");
        }
        if (StringUtil.isValid(locale) && (locale.equalsIgnoreCase("zh_CN") || locale.equalsIgnoreCase("zh_HK"))) {
            queryBuffer.append(" AND (topics:LZH)");
        }
        if (StringUtil.isValid(headline)) {
            queryBuffer.append(" AND (headline:HEADLINE)".replace("HEADLINE", headline));
        }
        if (StringUtil.isValid(content)) {
            queryBuffer.append(" AND (content:\"CONTENT\")".replace("CONTENT", content));
        }

        // handle the multiple stocks
        HandleMultipleStocks(queryBuffer, ricCodeList);

        return queryBuffer.toString();
    }

    private void HandleMultipleStocks(StringBuffer queryBuffer, List<String> ricCodeList) {
        if (null != ricCodeList && !ricCodeList.isEmpty()) {
            int listSize = ricCodeList.size();
            int index = 0;

            StringBuffer ricCodeCriteriasBuf = new StringBuffer(256);
            while (index <= listSize - 1) {
                String ricCode = ricCodeList.get(index);
                if (StringUtil.isValid(ricCode)) {
                    if (index == listSize - 1) {
                        ricCodeCriteriasBuf.append("(companies:RIC_CODE)".replace("RIC_CODE", ricCode));
                    } else {
                        ricCodeCriteriasBuf.append("(companies:RIC_CODE) OR ".replace("RIC_CODE", ricCode));
                    }
                }
                index++;
            }

            String ricCodeCriterias = ricCodeCriteriasBuf.toString();
            queryBuffer.append("AND (RIC_CODE_CRITERIAS)".replace("RIC_CODE_CRITERIAS", ricCodeCriterias));
        }
    }

    @Override
    protected Object execute(Object serviceRequest) {
        TrisNewsHeadlinesServiceRequest request = (TrisNewsHeadlinesServiceRequest) serviceRequest;
        String response;

//        if (oatProperties.getTrisFlag()) {
//            response = "{\"TOT_VOLUME\":34302,\"closure\":\"app_tris_rbwm_stockconnect_sat\",\"result\":[{\"HDLN_TES\":\" (Adds details on close ally of Xi Jinping meeting Rajapaksas)\\n    By Shihar Aneez\\n    COLOMBO, Sept 19 (Reuters) - Sri Lankan presidential nominee\\nGotabaya Rajapaksa would &quot;restore relations&quot; with the country&#39s\\ntop lender China if he wins the Nov. 16 ...\",\"NEWSHDLN\":\"UPDATE 1-Sri Lanka presidential nominee Rajapaksa would restore relations with China -adviser\",\"NEWS_LT\":\"2019-09-20T07:15:44.813Z\",\"GUID\":\"reuters.com:20190920:nL3N26B19J\"}],\"stsTxt\":\"OK\",\"stsCode\":0}\n";
//        } else {
            try {
                StringEntity entity = new StringEntity(JsonUtil.toJson(request), "utf-8");
                entity.setContentType("application/json");
                response = this.httpClientHelper.doPost(this.trisProps.getNewsHeadlinesUrl(), entity, null);
            } catch (Exception e) {
                logger.error("Access Tris encounter error", e);
                throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_TRIS_ERROR, e);
            }
//        }
        return response;
    }

    @Override
    protected Object validateServiceResponse(Object response) {
        String serviceResponse = (String) response;
        JsonArray jsonArray = this.validateEachServiceResponse(serviceResponse);
        return jsonArray;
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
            if (StringUtil.isValid(exceptionName)){
                throw new VendorException(ExCodeConstant.EX_CODE_TRIS_UNDEFINED_ERROR);
            }else{
                throw new VendorException(exceptionName);
            }
        }
        JsonArray resultJsonArray = JsonUtil.getAsJsonArray(respJsonObj, "result");
        return resultJsonArray;
    }

    @Override
    protected NewsHeadlinesResponse convertResponse(Object obj) throws Exception {
        NewsHeadlinesResponse newsHeadlinesResponse = new NewsHeadlinesResponse();
        if (obj == null) {
            return newsHeadlinesResponse;
        }
        JsonArray resultJsonArray = (JsonArray) obj;
        List<News> newsList = new ArrayList<>();

        TimeZone timeZone = TimeZone.getTimeZone(DateUtil.TIMEZONE_GMT8);
        TimeZone oldtimeZone = TimeZone.getTimeZone(DateUtil.TIMEZONE_GMT);
        String[] keys = (String[]) ArgsHolder.getArgs(NewsConstant.KEYS);
        NewsHeadlinesRequest request = (NewsHeadlinesRequest) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_REQUEST);

        String key = String.join(SymbolConstant.SYMBOL_VERTICAL_LINE, keys);
        if (resultJsonArray != null || resultJsonArray.size() > 0) {
            for (JsonElement jsonElement : resultJsonArray) {
                JsonObject resultJsonObj = jsonElement.getAsJsonObject();
                News news = new News();
                news.setId(TrisPropsUtil.inOrderStrProps("id", key, jsonElement.getAsJsonObject()));
                news.setHeadline(TrisPropsUtil.inOrderStrProps("headline", key, resultJsonObj));
                news.setBrief(TrisPropsUtil.inOrderStrProps("brief", key, resultJsonObj));
                String time = TrisPropsUtil.inOrderStrProps("asOfDateTime", key, resultJsonObj);
                String asOfDateTime = DateUtil.string2DateString(time,
                        DateUtil.DateFormat_yyyyMMddHHmmssSSS, DateUtil.DateFormat_yyyyMMddHHmmssSSSXXX, oldtimeZone, timeZone);
                news.setAsOfDateTime(asOfDateTime);
                newsList.add(news);
            }
            newsList = this.pageList(newsList, request.getPageId(), request.getRecordsPerPage());
        }

        newsHeadlinesResponse.setNewsList(newsList);
        return newsHeadlinesResponse;
    }

    private List pageList(List list, int pageIndex, int pageSize) {
        int startIndex = (pageIndex - 1) * pageSize;
        int lastIndex = pageIndex * pageSize;
        int count = list.size();
        if (lastIndex >= count) {
            lastIndex = count;
        }
        if (startIndex > lastIndex) {
            return Collections.emptyList();
        }
        return list.subList(startIndex, lastIndex);
    }

}
