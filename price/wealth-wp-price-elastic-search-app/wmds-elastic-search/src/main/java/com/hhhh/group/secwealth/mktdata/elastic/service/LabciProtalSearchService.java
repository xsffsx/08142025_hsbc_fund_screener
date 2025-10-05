package com.hhhh.group.secwealth.mktdata.elastic.service;

import com.hhhh.group.secwealth.mktdata.elastic.bean.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.elastic.bean.labci.Header;
import com.hhhh.group.secwealth.mktdata.elastic.bean.labci.StocksList;
import com.hhhh.group.secwealth.mktdata.elastic.bean.labci.SymbolSearchBoby;
import com.hhhh.group.secwealth.mktdata.elastic.bean.labci.SymbolSearchEnvelop;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.MultiPredSrchRequest;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.elastic.properties.LabciProtalProperties;
import com.hhhh.group.secwealth.mktdata.elastic.util.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.elastic.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
    private static final String PRODUCT_CCY = "USD";
    private static final String HTTP_PARAM_TOKEN = "token";
    private static final String HTTP_PARAM_MESSAGE = "message";


    public List<PredSrchResponse> predsrch(final PredSrchRequest request, final CommonRequestHeader header) throws Exception {
        SymbolSearchEnvelop envelop = this.getSymbolSearchEnvelop(header, request.getMarket(), request.getKeyword(), request.getTopNum(), request.getLocale());
        return this.convertPredResponse(envelop);
    }

    private SymbolSearchEnvelop getSymbolSearchEnvelop(CommonRequestHeader header, String market, String keyword, String topNum, String locale) throws Exception {
        String message = buildMessageRequestForPredSearch(market, keyword, topNum, locale);
        String token = labciProtalService.encryptLabciProtalToken(header.getChannelId(), header.getCustomerId(), market);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(HTTP_PARAM_MESSAGE, message));
        params.add(new BasicNameValuePair(HTTP_PARAM_TOKEN, token));
        String xmlResp = this.httpClientHelper.doGet(labciProtalProperties.getProxyName(), labciProtalProperties.getQueryUrl(), params, null);
        InputStream is = new ByteArrayInputStream(xmlResp.getBytes());
        return (SymbolSearchEnvelop) unmarShallerClass(SymbolSearchEnvelop.class).unmarshal(new InputStreamReader(is, StandardCharsets.UTF_8));
    }

    private List<PredSrchResponse> convertPredResponse(SymbolSearchEnvelop envelop) {
        List<PredSrchResponse> predSrchResponses = new ArrayList<>();
        this.convertResp(envelop, predSrchResponses);
        return predSrchResponses;
    }

    private void convertResp(SymbolSearchEnvelop envelop, List<PredSrchResponse> predSrchResponses) {
        if (envelop != null) {
            SymbolSearchBoby body = envelop.getBody();
            List<StocksList> stockslist = body.getStockslist();
            if (!CollectionUtils.isEmpty(stockslist)) {
                Header header = envelop.getHeader();
                for (StocksList stocks : stockslist) {
                    List<ProdAltNumSeg> segList = new ArrayList<>();
                    PredSrchResponse predSrchResponse = new PredSrchResponse();
                    ProdAltNumSeg prodAltNumSegMarketCode = new ProdAltNumSeg();
                    prodAltNumSegMarketCode.setProdAltNum(stocks.getStocksymbol());
                    prodAltNumSegMarketCode.setProdCdeAltClassCde(PredictiveSearchConstant.PROD_ALT_CLASS_CDE_M);
                    segList.add(prodAltNumSegMarketCode);
                    ProdAltNumSeg prodAltNumSegTickerCode = new ProdAltNumSeg();
                    prodAltNumSegTickerCode.setProdAltNum(stocks.getRiccode());
                    prodAltNumSegTickerCode.setProdCdeAltClassCde(PredictiveSearchConstant.PROD_ALT_CLASS_CDE_T);
                    segList.add(prodAltNumSegTickerCode);
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

    public List<PredSrchResponse> multiPredsrch(final MultiPredSrchRequest request, final CommonRequestHeader header) throws Exception {
        String[] keywords = request.getKeyword();
        List<PredSrchResponse> predSrchTotalResponses = new ArrayList<>();

        for (String keyword : keywords) {
            SymbolSearchEnvelop envelop = this.getSymbolSearchEnvelop(header, request.getMarket(), keyword, request.getTopNum(), request.getLocale());
            convertResp(envelop, predSrchTotalResponses);
        }
        return predSrchTotalResponses;
    }

    private Unmarshaller unmarShallerClass(final Class<SymbolSearchEnvelop> clazz) {
        Unmarshaller unmarshaller;
        try {
            JAXBContext jsxbContext = JAXBContext.newInstance(clazz);
            unmarshaller = jsxbContext.createUnmarshaller();
        } catch (Exception e) {
            logger.error("Unmarshaller init fail.");
            throw new IllegalArgumentException();
        }
        return unmarshaller;
    }

    private String buildMessageRequestForPredSearch(String market, String keyword, String topNum, String locale) {
        if (StringUtil.isInvalid(topNum)) {
            topNum = DEFAULT_TOP_NUM;
        }
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
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
