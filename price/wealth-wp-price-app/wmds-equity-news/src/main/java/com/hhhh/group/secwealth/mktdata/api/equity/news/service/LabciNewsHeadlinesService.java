package com.hhhh.group.secwealth.mktdata.api.equity.news.service;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.LabciProtalTokenService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.request.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProtalProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.PredSrchProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.token.LabciToken;
import com.hhhh.group.secwealth.mktdata.api.equity.news.constants.NewsConstant;
import com.hhhh.group.secwealth.mktdata.api.equity.news.request.NewsDetailRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.news.request.NewsHeadlinesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.News;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.NewsHeadlinesResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.labci.Body;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.labci.Envelop;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.labci.NewsList;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.*;

@Service
@ConditionalOnProperty(value = "service.newsHeadlines.Labci.injectEnabled")
public class LabciNewsHeadlinesService extends AbstractBaseService<NewsHeadlinesRequest, NewsHeadlinesResponse, CommonRequestHeader> {

    private static final Logger logger = LoggerFactory.getLogger(LabciNewsHeadlinesService.class);

    private String ifSkipCallLabciFlag = "ifSkipCallLabciFlag";

    @Autowired
    private PredSrchProperties predSrchProps;
    @Autowired
    private HttpClientHelper httpClientHelper;

    @Autowired
    @Getter
    @Setter
    private LabciProtalProperties labciProtalProperties;

    @Autowired
    private LabciProtalTokenService labciProtalTokenService;
    @Autowired
    private LabciAmhNewsCommonService labciAmhNewsCommonService;
    @Autowired
    private PredSrchService predSrchService;

    @Override
    protected Object convertRequest(NewsHeadlinesRequest request, CommonRequestHeader header) throws Exception {
        String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER, header);
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_QUOTES_REQUEST, request);

        String locale = header.getLocale();
        String productCodeIndicator = request.getProductCodeIndicator();
        List<String> symbols = request.getSymbol() != null ? request.getSymbol() : new ArrayList<>();
        String message;
        boolean ifSkipCallLabci = false;
        if (Constant.PROD_CDE_ALT_CLASS_CODE_M.equals(productCodeIndicator)) {
            List<String> ricCodeList = new ArrayList<>();
            for (String symbol : symbols) {
                String ricCode = getSymbolString(request, header, symbol);
                if (ricCode == null || ricCode.isEmpty() || ricCode.equals("")) {

                } else {
                    ricCodeList.add(ricCode);
                }
            }
            if (ricCodeList.isEmpty() && !symbols.isEmpty()) {
                ifSkipCallLabci = true;
            }
            message = assembleRequest(request, locale, ricCodeList);
        } else {
            message = assembleRequest(request, locale, symbols);
        }
        ArgsHolder.putArgs(ifSkipCallLabciFlag, ifSkipCallLabci);
        LabciToken tokenModel = labciAmhNewsCommonService.getTokenModel(header.getChannelId(), request.getMarket());
        String token = labciProtalTokenService.encryptLabciToken(site, tokenModel);
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair(NewsConstant.HTTP_PARAM_MESSAGE, message));
        params.add(new BasicNameValuePair(NewsConstant.HTTP_PARAM_TOKEN, token));
        //requestMap.put(symbol, params);

        return params;
    }

    private String getSymbolString(NewsHeadlinesRequest request, CommonRequestHeader header, String symbol) {
        String ricCode = "";
        if (symbol != null && symbol.trim() != "") {
            String[] symbols = new String[]{symbol};
            PredSrchRequest predSrchRequest = new PredSrchRequest();
            predSrchRequest.setKeyword(symbols);
            String[] preciseSrchFields = new String[]{"ALL"};
            predSrchRequest.setAssetClasses(preciseSrchFields);
            predSrchRequest.setSearchFields(this.predSrchProps.getRequestParams().getSearchFields());
            predSrchRequest.setSortingFields(this.predSrchProps.getRequestParams().getSearchFields());
            predSrchRequest.setTopNum(String.valueOf(symbols.length));
            predSrchRequest.setMarket(request.getMarket());
            predSrchRequest.setPreciseSrch(this.predSrchProps.getRequestParams().isPreciseSrch());
            List<PredSrchResponse> predSrchResponses = predSrchService.precSrchForNews(predSrchRequest, header);
            if (predSrchResponses == null || predSrchResponses.isEmpty()) {
                return "";
            } else {
                for (PredSrchResponse predSrchRespons : predSrchResponses) {
                    for (ProdAltNumSeg prodAltNumSeg : predSrchRespons.getProdAltNumSegs()) {
                        if (Constant.PROD_CDE_ALT_CLASS_CODE_T.equals(prodAltNumSeg.getProdCdeAltClassCde())) {
                            ricCode = prodAltNumSeg.getProdAltNum();
                        }
                    }
                }
            }
        }
        return ricCode;
    }

    @Override
    protected Object execute(Object obj) throws Exception {

        List<NameValuePair> params = (List<NameValuePair>) obj;
        String xmlResp = "";
        try {
            boolean ifSkipCallLabci = (boolean) ArgsHolder.getArgs(ifSkipCallLabciFlag);
            if (!ifSkipCallLabci) {
                xmlResp = this.httpClientHelper.doPost(labciProtalProperties.getProxyName(), labciProtalProperties.getQueryUrl(), params, null);
            }
        } catch (Exception e) {
            logger.error("Access Labci encounter error", e);
            throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_LABCI_ERROR, e);
        }

        return xmlResp;
    }

    @Override
    protected Object validateServiceResponse(Object obj) throws Exception {
        String xmlResp = (String) obj;
        boolean ifSkipCallLabci = (boolean) ArgsHolder.getArgs(ifSkipCallLabciFlag);


        String temp = new String(xmlResp.getBytes(), Charset.forName("UTF-8"));
        Envelop responseEnvelop = labciAmhNewsCommonService.getResponseEnvelop(temp);
        if (!ifSkipCallLabci) {
            if (!labciAmhNewsCommonService.NORMAL_RESPONSE_CODE.equals(responseEnvelop
                    .getHeader().getResponsecode())) {
                logger.error("Labci praotal return invalid response: {}", responseEnvelop.getHeader().getErrormsg());
                throw new VendorException(ExCodeConstant.EX_CODE_LABCI_INVALID_RESPONSE);
            }
        }
        return responseEnvelop;
    }

    @Override
    protected NewsHeadlinesResponse convertResponse(Object obj) throws Exception {
        NewsHeadlinesRequest request = (NewsHeadlinesRequest) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_REQUEST);
        NewsHeadlinesResponse newsHeadlinesResponse = new NewsHeadlinesResponse();
        boolean ifSkipCallLabci = (boolean) ArgsHolder.getArgs(ifSkipCallLabciFlag);
        List<News> newsHeadlineList = new ArrayList<News>();
        if (!ifSkipCallLabci) {
            Envelop responseEnvelop = (Envelop) obj;
            List<NewsList> tempNewsList = new ArrayList<>();

            Body responseBody = responseEnvelop.getBody();
            List<NewsList> reNewsList = responseBody.getNewslist();
            if (!CollectionUtils.isEmpty(reNewsList)) {
                List<NewsList> pageList = pageList(reNewsList, request.getPageId(), request.getRecordsPerPage());
                tempNewsList.addAll(pageList);
            }

            if (tempNewsList != null && !tempNewsList.isEmpty()) {
                for (NewsList news : tempNewsList) {
                    News newsHeadline = new News();
//                }
                    newsHeadline.setId(news.getNewsid());
                    newsHeadline.setSource(news.getNewssource());
                    newsHeadline.setNewsLang(news.getNewslang());
                    newsHeadline.setHeadline(labciAmhNewsCommonService.removeCdataTag(news.getNewstopic()));
                    newsHeadline.setAsOfDateTime(labciAmhNewsCommonService.getFormatDataString(news.getNewsdate(), news.getNewstime(), news.getTimezone()));
                    newsHeadline.setAsOfDate(news.getNewsdate());
                    newsHeadline.setAsOfTime(news.getNewstime());
                    newsHeadlineList.add(newsHeadline);
                }
            }
        }

        newsHeadlinesResponse.setNewsList(newsHeadlineList);
        return newsHeadlinesResponse;
    }


    /**
     * Assemble LabCI request body.
     *
     * @param request
     * @return
     */
    private String assembleRequest(NewsHeadlinesRequest request, String locale, List<String> symbolList) {
        Integer recordsPerPage = request.getRecordsPerPage();
        Integer pageId = request.getPageId();
        if (request.getPageId() > 1) {
            recordsPerPage = pageId * recordsPerPage;
        }
        StringBuffer labciRequestString = new StringBuffer(256);
        StringBuilder symbolListString = new StringBuilder();
        if (!(symbolList == null || symbolList.isEmpty())) {
            for (int i = 0; i < symbolList.size(); i++) {
                symbolListString.append(symbolList.get(i));
                if (i < symbolList.size() - 1) {
                    symbolListString.append(" ");
                }
            }
        }
        labciRequestString
                .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<envelop><header>")
                .append("<msgid>newsheader</msgid>").append("<marketid>")
                .append(request.getMarket())
                .append("</marketid>").append("</header><body>")
                .append("<stocksymbol>")
                .append(symbolListString)
                .append("</stocksymbol>")
                .append("<exchange>")
                .append(request.getExchange() == null ? "" : request.getExchange())
                .append("</exchange>")
                .append("<keyword>")
                .append(request.getKeyword() == null ? "" : request.getKeyword())
                .append("</keyword>")
                .append("<location>")
                .append(request.getLocation() == null ? "" : request.getLocation())
                .append("</location>")
                .append("<topics>")
                .append(request.getCategory() == null ? "" : request.getCategory())
                .append("</topics>")
                .append("<startdate>")
                .append(request.getStartDate() == null ? "" : request.getStartDate())
                .append("</startdate>")
                .append("<enddate>")
                .append(request.getEndDate() == null ? "" : request.getEndDate())
                .append("</enddate>")
                .append("<recordsperpage>")
                .append(recordsPerPage == null ? "" : recordsPerPage)
                .append("</recordsperpage>")
                .append("<useragenttype>")
                .append("")
                .append("</useragenttype>")
                .append("<locale>")
                .append(labciAmhNewsCommonService.LABCI_LOCALE_MAPPING.get(locale))
                .append("</locale>")
                .append("<translate>")
                .append(request.isTranslate() == true ? "Y" : "N")
                .append("</translate>")
                .append("</body></envelop>");

        return labciRequestString.toString();
    }

    private Envelop getNewsDetail(String id, String market, Boolean translate) {
        CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
        String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
        NewsDetailRequest request = new NewsDetailRequest();
        request.setId(id);
        request.setTranslate(translate);
        String message = labciAmhNewsCommonService.assembleRequest(request, header.getLocale());
        LabciToken tokenModel = labciAmhNewsCommonService.getTokenModel(header.getChannelId(), market);
        String token = labciProtalTokenService.encryptLabciToken(site, tokenModel);
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair(NewsConstant.HTTP_PARAM_MESSAGE, message));
        params.add(new BasicNameValuePair(NewsConstant.HTTP_PARAM_TOKEN, token));
        String xmlResp;
        try {
            xmlResp = this.httpClientHelper.doGet(labciProtalProperties.getProxyName(), labciProtalProperties.getQueryUrl(), params, null);
        } catch (Exception e) {
            logger.error("Access Labci encounter error", e);
            throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_LABCI_ERROR, e);
        }
        Envelop responseEnvelop = labciAmhNewsCommonService.getResponseEnvelop(xmlResp);
        if (!labciAmhNewsCommonService.NORMAL_RESPONSE_CODE.equals(responseEnvelop
                .getHeader().getResponsecode())) {
            logger.error("Labci praotal return invalid response: {}", responseEnvelop.getHeader().getErrormsg());
            throw new VendorException(ExCodeConstant.EX_CODE_LABCI_INVALID_RESPONSE);
        }
        return responseEnvelop;
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
        List pageList = new ArrayList<>();
        for (int i = startIndex; i < lastIndex; i++) {
            pageList.add(list.get(i));
        }
        return pageList;
    }
}
