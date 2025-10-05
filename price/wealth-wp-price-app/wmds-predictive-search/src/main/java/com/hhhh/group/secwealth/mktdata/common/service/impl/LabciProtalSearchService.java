package com.hhhh.group.secwealth.mktdata.common.service.impl;

import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.pres.beans.MultiPredSrchRequest;
import com.hhhh.group.secwealth.mktdata.predsrch.pres.beans.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.predsrch.pres.beans.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.cache.LabciSymbolSearchCache;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.constants.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.properties.LabciProtalProperties;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.service.LabciProtalService;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.labci.Header;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.labci.StocksList;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.labci.SymbolSearchBoby;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.labci.SymbolSearchEnvelop;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class LabciProtalSearchService {

    public static final String PRODUCT_TYPE_SEC = "SEC";
    private static Logger logger = LoggerFactory.getLogger(LabciProtalSearchService.class);

    @Autowired
    private HttpClientHelper httpClientHelper;

    @Autowired
    private LabciProtalProperties labciProtalProperties;

    @Autowired
    private LabciProtalService labciProtalService;

    private static final String DEFAULT_TOP_NUM = "10";
    private static final String UTF_8 = "UTF-8";
    private static final String PRODUCT_CCY = "USD";
    private static final String HTTP_PARAM_TOKEN = "token";
    private static final String HTTP_PARAM_MESSAGE = "message";
    private static final Map<Class, JAXBContext> jaxbContextMap = new Hashtable<>();


    public List<PredSrchResponse> predsrch(final PredSrchRequest request) throws Exception {

        String message = buildMessageRequestForPredSearch(request.getMarket(), request.getKeyword(), request.getTopNum(), request.getLocale());
        String token = labciProtalService.encryptLabciProtalToken(request.getMarket());

        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair(HTTP_PARAM_MESSAGE, message));
        params.add(new BasicNameValuePair(HTTP_PARAM_TOKEN, token));

        String xmlResp = this.httpClientHelper.doGet(labciProtalProperties.getProxyName(), labciProtalProperties.getQueryUrl(), params, null);

        InputStream is = new ByteArrayInputStream(xmlResp.getBytes());
        SymbolSearchEnvelop envelop = (SymbolSearchEnvelop) unmarShallerClass(SymbolSearchEnvelop.class).unmarshal(new InputStreamReader(is, "UTF-8"));
        return converPredRespnose(envelop, request);
    }

    public List<PredSrchResponse> converPredRespnose(SymbolSearchEnvelop envelop, PredSrchRequest request) {
        List<PredSrchResponse> predSrchResponses = new ArrayList<>();
        if (envelop != null) {
            SymbolSearchBoby body = envelop.getBody();
            if (body != null) {
                List<StocksList> stockslist = body.getStockslist();
                if (!CollectionUtils.isEmpty(stockslist)) {
                    Header header = envelop.getHeader();
                    for (StocksList stocks : stockslist) {
                        List<ProdAltNumSeg> segList = new ArrayList<>();
                        PredSrchResponse predSrchResponse = new PredSrchResponse();
                        ProdAltNumSeg seg_M = new ProdAltNumSeg();
                        seg_M.setProdAltNum(stocks.getStocksymbol());
                        seg_M.setProdCdeAltClassCde(PredictiveSearchConstant.PROD_ALT_CLASS_CDE_M);
                        segList.add(seg_M);
                        ProdAltNumSeg seg_T = new ProdAltNumSeg();
                        seg_T.setProdAltNum(stocks.getRiccode());
                        seg_T.setProdCdeAltClassCde(PredictiveSearchConstant.PROD_ALT_CLASS_CDE_T);
                        segList.add(seg_T);
                        predSrchResponse.setProductCcy(PRODUCT_CCY);
                        predSrchResponse.setSymbol(stocks.getStocksymbol());
                        predSrchResponse.setExchange(stocks.getExchange());
                        predSrchResponse.setProductName(stocks.getStockname());
                        predSrchResponse.setProductShortName(stocks.getStockname());
                        predSrchResponse.setProdAltNumSegs(segList);
                        predSrchResponse.setCountryTradableCode(header.getMarketid());
                        predSrchResponse.setMarket(header.getMarketid());
                        predSrchResponse.setProductType(PRODUCT_TYPE_SEC);
                        predSrchResponses.add(predSrchResponse);
                    }
                }
            }
        }
        return predSrchResponses;
    }

    public List<PredSrchResponse> multiPredsrch(final MultiPredSrchRequest request) throws Exception {
        String[] keywords = request.getKeyword();
        List<PredSrchResponse> predSrchTotalResponses = new ArrayList<>();
        for (String keyword : keywords) {
            retrieveLabciResponse(predSrchTotalResponses, request, keyword);
        }
        return predSrchTotalResponses;
    }

    private void retrieveLabciResponse(List<PredSrchResponse> predSrchTotalResponses,
                                       MultiPredSrchRequest request,
                                       String keyword ) throws Exception {
        String message = buildMessageRequestForPredSearch(request.getMarket(), keyword, request.getTopNum(), request.getLocale());
        String token = labciProtalService.encryptLabciProtalToken(request.getMarket());
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair(HTTP_PARAM_MESSAGE, message));
        params.add(new BasicNameValuePair(HTTP_PARAM_TOKEN, token));
        String xmlResp = this.httpClientHelper.doGet(labciProtalProperties.getProxyName(), labciProtalProperties.getQueryUrl(), params, null);
        InputStream is = new ByteArrayInputStream(xmlResp.getBytes());
        SymbolSearchEnvelop envelop = (SymbolSearchEnvelop) unmarShallerClass(SymbolSearchEnvelop.class).unmarshal(new InputStreamReader(is, UTF_8));
        converMutiPredRespnose(predSrchTotalResponses, envelop);
    }

    public List<PredSrchResponse> multiPredsrchWithCache(final MultiPredSrchRequest request) throws Exception {
        String[] keywords = request.getKeyword();
        List<PredSrchResponse> predSrchTotalResponses = new ArrayList<>();
        LabciSymbolSearchCache labciSymbolSearchCache = LabciSymbolSearchCache.getInstance();

        for (String keyword : keywords) {
            PredSrchResponse predSrchResponse = labciSymbolSearchCache.get(keyword);
            if(predSrchResponse!=null) {
                predSrchTotalResponses.add(predSrchResponse);
                logger.info("Retrieve labci symbol search result from cache. Symbol: {}", keyword);
            } else {
                retrieveLabciResponse(predSrchTotalResponses, request, keyword);
            }
        }
        return predSrchTotalResponses;
    }


    public void converMutiPredRespnose(List<PredSrchResponse> responses, SymbolSearchEnvelop envelop) {
        if (envelop != null) {
            SymbolSearchBoby body = envelop.getBody();
            Header header = envelop.getHeader();
//            String totalrecordno = body.getTotalrecordno();
//            if (totalrecordno.equals(EMPTY_RECORDNO)) {
//                logger.error("Invalid response, error message:{}", header.getErrormsg());
//                throw new VendorException(ExCodeConstant.EX_CODE_LABCI_INVALID_RESPONSE);
//            }
            List<StocksList> stockslist = body.getStockslist();
            if (!CollectionUtils.isEmpty(stockslist)) {
                LabciSymbolSearchCache labciSymbolSearchCache = LabciSymbolSearchCache.getInstance();
                for (StocksList stocks : stockslist) {
                    List<ProdAltNumSeg> segList = new ArrayList<>();
                    PredSrchResponse predSrchResponse = new PredSrchResponse();
                    ProdAltNumSeg seg_M = new ProdAltNumSeg();
                    seg_M.setProdAltNum(stocks.getStocksymbol());
                    seg_M.setProdCdeAltClassCde(PredictiveSearchConstant.PROD_ALT_CLASS_CDE_M);
                    segList.add(seg_M);
                    ProdAltNumSeg seg_T = new ProdAltNumSeg();
                    seg_T.setProdAltNum(stocks.getRiccode());
                    seg_T.setProdCdeAltClassCde(PredictiveSearchConstant.PROD_ALT_CLASS_CDE_T);
                    segList.add(seg_T);
                    predSrchResponse.setProductCcy(PRODUCT_CCY);
                    predSrchResponse.setSymbol(stocks.getStocksymbol());
                    predSrchResponse.setExchange(stocks.getExchange());
                    predSrchResponse.setProductName(stocks.getStockname());
                    predSrchResponse.setProductShortName(stocks.getStockname());
                    predSrchResponse.setProdAltNumSegs(segList);
                    predSrchResponse.setCountryTradableCode(header.getMarketid());
                    predSrchResponse.setMarket(header.getMarketid());
                    predSrchResponse.setProductType(PRODUCT_TYPE_SEC);
                    responses.add(predSrchResponse);
                    labciSymbolSearchCache.add(stocks.getStocksymbol(), predSrchResponse);
                    logger.info("Add labci symbol search result to cache. Symbol: {}", stocks.getStocksymbol());
                }
            }
        }
    }

    private Unmarshaller unmarShallerClass(final Class clazz) {
        Unmarshaller unmarshaller = null;
        try {
            if (jaxbContextMap.containsKey(clazz)) {
                return jaxbContextMap.get(clazz).createUnmarshaller();
            } else {
                JAXBContext jsxbContext = JAXBContext.newInstance(clazz);
                unmarshaller = jsxbContext.createUnmarshaller();
                jaxbContextMap.put(clazz, jsxbContext);
            }
        } catch (JAXBException e) {
            logger.error("Unmarshaller init fail.");
        }
        return unmarshaller;
    }

    private String buildMessageRequestForPredSearch(String market, String keyword, String topNum, String locale) {
        if (StringUtil.isInvalid(topNum)) {
            topNum = DEFAULT_TOP_NUM;
        }
        StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<envelop><header>")
                .append("<msgid>symbolsearch</msgid>")
                .append("<marketid>").append(market).append("</marketid>")
                .append("</header><body>")
                .append("<keyword>").append(keyword).append("</keyword>")
                .append("<start>0</start>")
                .append("<limit>").append(topNum).append("</limit>")
                .append("<useragenttype>MDS</useragenttype>")
                .append("<locale>").append(locale).append("</locale>")
                .append("</body></envelop>");
        return sb.toString();
    }

}
