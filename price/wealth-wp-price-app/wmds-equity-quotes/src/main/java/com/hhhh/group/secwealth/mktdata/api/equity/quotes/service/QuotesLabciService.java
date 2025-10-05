/**
 * @Title QuoteLABCIService.java
 * @description TODO
 * @author OJim
 * @time May 30, 2019 11:50:08 AM
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.hhhh.group.secwealth.mktdata.api.equity.common.util.BigDecimalUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.PastPerformance;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.util.ListUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci.LabciResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci.Ric;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci.Watchlist;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.request.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.ApplicationProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.MarketHourProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.LabciPropsUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.LabciServletBoConvertor;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.QuoteAccessLogForQuotes;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.QuoteUserForQuotes;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.QuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.SECQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ServiceProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.service.QuotesServiceRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.Message;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesLabciQuote;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesLabciResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.util.ConvertorsUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResponse;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResult;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResultStateEnum;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.utils.CacheDistributeHolder;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.exception.ExTraceCodeGenerator;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.ExResponseComponent;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.entity.ExResponseEntity;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

/**
 *
 */

/**
 * @Title QuoteLABCIService.java
 * @description TODO
 * @author OJim
 * @time May 30, 2019 11:50:08 AM
 */
@Service("labciQuoteService")
@ConditionalOnProperty(value = "service.quotes.Labci.injectEnabled")
public class QuotesLabciService
        extends AbstractBaseService<SECQuotesRequest, QuotesLabciResponse, CommonRequestHeader> {

    private static final Logger logger = LoggerFactory.getLogger(QuotesLabciService.class);

    private static final String DATAREPRESENTATION_XML = "XML";

    private static final String ACCES_TYPE_DELAY = "EDSTK";

    private static final String ACCES_TYPE_REAL_TIME = "STOCK";

    private static final String ACCES_CMND_CDE = "QUOTE_LABCI_ACCES_CMND_CDE";

    private static final String ACCES_CMND_CDE_QUOTE_DETAIL = "QUOTE_DETAIL";

    private static final String ACCES_CMND_CDE_QUOTE_LIST = "QUOTE_LIST";

    private static final String CUSTOMER_ID = "QUOTE_LABCI_CUSTOMER_ID";

    private static final String WD_APP_CODE = "WD";

     private static final String USER_TYPE_CDE_STFF = "STFF";

    private static final String USER_TYPE_CDE_CUST = "CUST";

    public static final String THREAD_INVISIBLE_DELAY = "QUOTE_DELAY";

    private static final String KEY_L = "L=";

    private static final String KEY_D = "d";

    private static final String HEADER_FUNCTION_ID_CHECK = "|00|01|02|03|04|05|06|07|08|09|10|21|22|23|24|25|26|27|28|29|41|42|43|44|45|46|61|62|63|64|71|72|73|74|75|76|91|92|93|94|111|112|131|132|";

    private static final String REQUEST_PRODUCTKEYS_PRODUCTTYPE_CHECK = "|ALL|SEC|WRTS|";

    @Autowired
    private PredSrchService predSrchService;

    @Autowired
    private ExResponseComponent exRespComponent;

    @Autowired
    private ApplicationProperties appProps;

    @Autowired
    private LabciProperties labciProps;

    @Autowired
    private HttpClientHelper httpClientHelper;

    @Autowired
    private MarketHourProperties marketHourProperties;

    @Autowired
    @Qualifier("quotesQuoteUserService")
    private QuoteUserService quoteUserService;

    @Autowired
    @Qualifier("quotesQuoteAccessLogService")
    private QuoteAccessLogService quoteAccessLogService;

    @Autowired
    @Qualifier("aSharesStockService")
    private ASharesStockService aSharesStockService;

    /*
     * (non-Javadoc)
     *
     * @see
     * com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService#
     * convertRequest(java.lang.Object, java.lang.Object)
     */
    @Override
    protected Object convertRequest(final SECQuotesRequest request, final CommonRequestHeader header) throws Exception {
        //TODO
        if (!QuotesLabciService.HEADER_FUNCTION_ID_CHECK.contains(
                SymbolConstant.SYMBOL_VERTICAL_LINE + header.getFunctionId() + SymbolConstant.SYMBOL_VERTICAL_LINE)) {
            QuotesLabciService.logger.error("Header FunctionId don't match the rules or can't be empty.");
            throw new CommonException(ExCodeConstant.EX_CODE_HEADER_FUNCTIONID_INVALID);
        }

        String eID = request.getEid();
        String appCode = header.getAppCode();
        //1.wds pass eid
        //2.staff channel
        //3.customer channel to caching
        //4.customer channel to eid
        //customer channel: investment

        if (StringUtil.isValid(eID) || QuotesLabciService.WD_APP_CODE.equals(appCode)) {
            ArgsHolder.putArgs(QuotesLabciService.CUSTOMER_ID, eID);
        } else {

            //todo Temporary plan, will be deleted in the future
            if ("OHB".equals(header.getChannelId())|| "CMB".equals(appCode)) {
                HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                String staffID = httpRequest.getHeader(Constant.REQUEST_HEADER_KEY_USER_ID);
                ArgsHolder.putArgs(QuotesLabciService.CUSTOMER_ID, staffID);
            } else {

                /**
                 * Before get result from the Cache Distribute, you can do some other business
                 * logic
                 */
                //check the user info
                CacheDistributeResult result = CacheDistributeHolder.getCacheDistribute();
                CacheDistributeResultStateEnum resultState = result.getResultState();
                if (resultState == CacheDistributeResultStateEnum.OK) {
                    CacheDistributeResponse response = result.getResponse();
                    String value = response.getValue();
                    // Get the value you are interested in
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        JsonNode node = mapper.readTree(value);
                        String customerId = node.get("eID").asText();
                        if (StringUtil.isInValid(customerId)) {
                            QuotesLabciService.logger.error("No eID found from rbp cache");
                            throw new VendorException(ExCodeConstant.EX_CODE_CACHE_NO_EID_FOUND);
                        }
                        ArgsHolder.putArgs(QuotesLabciService.CUSTOMER_ID, customerId);
                    } catch (Exception e) {
                        QuotesLabciService.logger.error("Cache Distribute Bad Request");
                        throw new VendorException(ExCodeConstant.EX_CODE_CACHE_BAD_REQUEST);
                    }
                } else {
                    if (resultState == CacheDistributeResultStateEnum.INVALID_PARAMETERS) {
                        QuotesLabciService.logger.error("Cache Distribute Bad Request");
                        throw new VendorException(ExCodeConstant.EX_CODE_CACHE_BAD_REQUEST);
                    }
                    if (resultState == CacheDistributeResultStateEnum.UNCACHED_RECORD) {
                        QuotesLabciService.logger.error("Cache Distribute don't contains the key you sent");
                        throw new VendorException(ExCodeConstant.EX_CODE_CACHE_UNCACHED_RECORD);
                    }
                    if (resultState == CacheDistributeResultStateEnum.INVALID_HTTP_STATUS) {
                        QuotesLabciService.logger.error("Get response from the Cache Distribute encounter error");
                        throw new VendorException(ExCodeConstant.EX_CODE_CACHE_INVALID_HTTP_STATUS);
                    }
                }


            }
        }

        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER, header);
        QuotesServiceRequest quotesServiceRequest = buildLabciServiceRequest(request, header);

        if (Constant.CASE_20.equals(request.getRequestType())) {
            ArgsHolder.putArgs(QuotesLabciService.ACCES_CMND_CDE, QuotesLabciService.ACCES_CMND_CDE_QUOTE_DETAIL);
        } else {
            ArgsHolder.putArgs(QuotesLabciService.ACCES_CMND_CDE, QuotesLabciService.ACCES_CMND_CDE_QUOTE_LIST);
        }
        List<ServiceProductKey> serviceProductKeys = quotesServiceRequest.getServiceProductKeys();
        String tCode = "";
        boolean sendDelay = isDelay(request);
        if (serviceProductKeys != null && !serviceProductKeys.isEmpty()) {
            for (int i = 0; i < serviceProductKeys.size(); i++) {
                tCode = serviceProductKeys.get(i).getProdAltNum();
                PredSrchResponse predResp = new PredSrchResponse();
                predResp = this.predSrchService.localPredSrch(tCode);
                if (predResp != null && this.marketHourProperties
                        .hasExchangeCode(this.labciProps.getExchangeMapping(predResp.getExchange()))) {
                    if (!isDelay(request)) {
                        sendDelay = this.marketHourProperties.checkTradingHoursByExchange(
                                this.labciProps.getExchangeMapping(predResp.getExchange()));
                        break;
                    }
                }
            }
        }
        ArgsHolder.putArgs(QuotesLabciService.THREAD_INVISIBLE_DELAY, sendDelay);
        Map<String, String> serviceRequestMapper = new LinkedHashMap<>();
        String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
        String service = this.labciProps.getLabciService(site, false);
        Map<String, List<ServiceProductKey>> groupedProductKeysMapper = getGroupedProductKeysMapper(
                quotesServiceRequest);
        for (Map.Entry<String, List<ServiceProductKey>> groupedProductKeys : groupedProductKeysMapper.entrySet()) {
            String key = groupedProductKeys.getKey();
            String[] keys = key.split(SymbolConstant.SYMBOL_VERTICAL_LINE_ESCAPE);
            List<ServiceProductKey> productKeys = groupedProductKeys.getValue();
            List<String> items = new ArrayList<>();
            for (int i = 0; i < productKeys.size(); i++) {
                ServiceProductKey productKey = productKeys.get(i);
                items.add(productKey.getProdAltNum());
            }
            String symbolList = LabciServletBoConvertor.genSymbols(items, service);
            String labciBigAskSymbolList = LabciServletBoConvertor.genSymbolsForQuote(items, service);
            List<NameValuePair> labciParams = new ArrayList<NameValuePair>();
            labciParams.add(new BasicNameValuePair("SymbolList", symbolList + ";" + labciBigAskSymbolList));
            labciParams.add(new BasicNameValuePair("FieldList",
                    StringUtils.join(
                            this.labciProps.getLabciFields(this.appProps.getQuotesResponseLabciFields(keys), keys),
                            SymbolConstant.SYMBOL_SEMISOLON)));
            labciParams.add(new BasicNameValuePair("DataRepresentation", QuotesLabciService.DATAREPRESENTATION_XML));
            String labciReqParams = URLEncodedUtils.format(labciParams, "UTF-8");
            serviceRequestMapper.put(key, labciReqParams);
        }
        return serviceRequestMapper;
    }

    /**
     * @Title buildServiceRequest
     * @Description
     * @param request
     * @param header
     * @return
     * @return QuotesServiceRequest
     * @Author OJim
     * @Date May 30, 2019 2:15:10 PM
     */
    private QuotesServiceRequest buildLabciServiceRequest(final SECQuotesRequest request,
                                                          final CommonRequestHeader header) {
        String requestType = Constant.CASE_0;
        if (StringUtil.isValid(request.getRequestType())) {
            requestType = request.getRequestType();
        }
        if (!Constant.CASE_0.equals(requestType) && !Constant.CASE_10.equals(requestType)
                && !Constant.CASE_20.equals(requestType)) {
            QuotesLabciService.logger.error("Wrong requestType");
            throw new CommonException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
        }
        QuotesServiceRequest quoteServiceRequest = new QuotesServiceRequest();
        quoteServiceRequest.setDelay(isDelay(request));
        List<String> symbols = new ArrayList<>();
        Map<String, List<String>> symbolsMap = new LinkedHashMap<>();
        for (int i = 0; i < request.getProductKeys().size(); i++) {
            if (Constant.PROD_CDE_ALT_CLASS_CODE_M.equals(request.getProductKeys().get(i).getProdCdeAltClassCde())) {
                if (StringUtil.isInValid(request.getProductKeys().get(i).getProductType())) {
                    request.getProductKeys().get(i).setProductType(Constant.APP_SUPPORT_ALL_EXCHANGES);
                } else if (!QuotesLabciService.REQUEST_PRODUCTKEYS_PRODUCTTYPE_CHECK
                        .contains(SymbolConstant.SYMBOL_VERTICAL_LINE + request.getProductKeys().get(i).getProductType()
                                + SymbolConstant.SYMBOL_VERTICAL_LINE)) {
                    QuotesLabciService.logger.error("Request productKeys productType don't match the rules.");
                    throw new CommonException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
                }
                symbols.add(request.getProductKeys().get(i).getProdAltNum());
                if (symbolsMap.containsKey(request.getProductKeys().get(i).getProductType())) {
                    symbolsMap.get(request.getProductKeys().get(i).getProductType())
                            .add(request.getProductKeys().get(i).getProdAltNum());
                } else {
                    List<String> symbolm = new ArrayList<>();
                    symbolm.add(request.getProductKeys().get(i).getProdAltNum());
                    symbolsMap.put(request.getProductKeys().get(i).getProductType(), symbolm);
                }
            }
        }
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_QUOTES_LABCI_REQUEST, request);
        if (Constant.CASE_20.equals(requestType)) {
            List<String> symbol = new ArrayList<>();
            if (request.getProductKeys().size() > 0) {
                ProductKey productKey = request.getProductKeys().get(0);
                symbol.add(productKey.getProdAltNum());

                symbolsMap = new LinkedHashMap<>();
                List<String> strings = new ArrayList<>();
                strings.add(productKey.getProdAltNum());
                symbolsMap.put(productKey.getProductType(), strings);
            }
            if (symbols.size() > 1) {
                ExResponseEntity exResponse = this.exRespComponent
                        .getExResponse(ExCodeConstant.EX_CODE_QUOTE_DETAIL_ONLY_SUPPORT_ONE_PRODALTNUM);
                addMessage(symbols, symbol, exResponse);
            }
            symbols = symbol;
        }

        List<PredSrchResponse> responses = new ArrayList<>();

        for (Map.Entry<String, List<String>> symbolMap : symbolsMap.entrySet()) {
            PredSrchRequest predSrchRequest = new PredSrchRequest();
            String[] assetClasses = new String[1];
            assetClasses[0] = symbolMap.getKey().toString();
            predSrchRequest.setAssetClasses(assetClasses);
            predSrchRequest.setKeyword(symbolMap.getValue().toArray(new String[symbolMap.getValue().size()]));
            predSrchRequest.setTopNum(String.valueOf(symbolMap.getValue().size()));
            try {
                responses.addAll(this.predSrchService.precSrch(predSrchRequest, header));
            } catch (Exception e) {
                QuotesLabciService.logger.warn("precSearch error is :" + e.toString());
            }
        }
        if (responses != null && responses.size() != symbols.size()) {
            ExResponseEntity exResponse = this.exRespComponent
                    .getExResponse(ExCodeConstant.EX_CODE_PREDSRCH_EMPTY_RESPONSE);
            List<String> validates = new ArrayList<>();
            for (int i = 0; i < responses.size(); i++) {
                validates.add(responses.get(i).getSymbol());
            }
            addMessage(symbols, validates, exResponse);
        }
        quoteServiceRequest.setServiceProductKeys(ConvertorsUtil.constructServiceProductKeys(request.getProductKeys(),
                symbolsMap, responses, Constant.PROD_CDE_ALT_CLASS_CODE_T));

        List<String> prodAltNums = new ArrayList<>();
        for (int i = 0; i < quoteServiceRequest.getServiceProductKeys().size(); i++) {
            prodAltNums.add(quoteServiceRequest.getServiceProductKeys().get(i).getProdAltNum());
        }
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_PRODUCT_KEY, prodAltNums);
        return quoteServiceRequest;
    }


    private void addMessage(List<String> symbols, List<String> validates, ExResponseEntity exResponse) {
        SECQuotesRequest request = (SECQuotesRequest) ArgsHolder
                .getArgs(Constant.THREAD_INVISIBLE_QUOTES_LABCI_REQUEST);
        List<String> invalidSymbols = getInvalidSymbols(symbols, validates);
        for (String invalidSymbol : invalidSymbols) {
            addMessage(invalidSymbol, exResponse, request);
        }
    }

    private void addMessage(String invalidSymbol, ExResponseEntity exResponse, SECQuotesRequest request) {
        Message message = new Message();
        message.setReasonCode(exResponse.getReasonCode());
        message.setText(exResponse.getText());
        String traceCode = ExTraceCodeGenerator.generate();
        message.setTraceCode(traceCode);
        message.setProductType("SEC");
        message.setProdCdeAltClassCde("M");
        message.setProdAltNum(invalidSymbol);
        for (ProductKey productKey : request.getProductKeys()) {
            if (invalidSymbol.contains(productKey.getProdAltNum())) {
                message.setProdAltNum(productKey.getProdAltNum());
                message.setProductType(productKey.getProductType());
                message.setProdCdeAltClassCde(productKey.getProdCdeAltClassCde());
                break;
            }
        }
        addMessage(message);
    }

    private Map<String, List<ServiceProductKey>> getGroupedProductKeysMapper(final QuotesServiceRequest request) {
        Map<String, List<ServiceProductKey>> groupedProductKeysMapper = new LinkedHashMap<>();
        String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
        List<ServiceProductKey> productKeys = request.getServiceProductKeys();
        for (int i = 0; i < productKeys.size(); i++) {
            ServiceProductKey productKey = productKeys.get(i);
            String key = new StringBuilder().append(site).append(SymbolConstant.SYMBOL_VERTICAL_LINE)
                    .append(productKey.getProductType()).append(SymbolConstant.SYMBOL_VERTICAL_LINE)
                    .append(this.labciProps.getExchangeMapping(productKey.getExchange())).toString();
            if (groupedProductKeysMapper.containsKey(key)) {
                groupedProductKeysMapper.get(key).add(productKey);
            } else {
                List<ServiceProductKey> groupedProductKeys = new ArrayList<>();
                groupedProductKeys.add(productKey);
                groupedProductKeysMapper.put(key, groupedProductKeys);
            }
        }
        return groupedProductKeysMapper;
    }

    private List<String> getInvalidSymbols(final List<String> symbols, final List<String> responses) {
        List<String> invalidSymbols = new ArrayList<>();
        for (String symbol : symbols) {
            invalidSymbols.add(symbol);
        }
        invalidSymbols.removeAll(responses);
        QuotesLabciService.logger.debug("Invalid Symbols: " + invalidSymbols);
        return invalidSymbols;
    }

    private void addMessage(final Message message) {
        if (ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES) == null) {
            ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES, new ArrayList<Message>());
        }
        @SuppressWarnings("unchecked")
        List<Message> messages = (List<Message>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES);
        messages.add(message);
    }

    private boolean isDelay(final QuotesRequest request) {
        if (request.getDelay() != null) {
            return request.getDelay();
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService#
     * execute(java.lang.Object)
     */
    @Override
    protected Object execute(final Object serviceRequest) throws Exception {
        Map<String, String> serviceResponseMapper = new LinkedHashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, String> serviceRequestMapper = (Map<String, String>) serviceRequest;

        for (Map.Entry<String, String> requestMapper : serviceRequestMapper.entrySet()) {
            String key = requestMapper.getKey();
            String request = requestMapper.getValue();
            try {

                serviceResponseMapper.put(key,
                        this.httpClientHelper.doGet(this.labciProps.getLabciUrl(), request, null));
            } catch (Exception e) {
                if (e instanceof IOException) {
                    QuotesLabciService.logger.error("Labci Undefined error", e);
                    throw new VendorException(ExCodeConstant.EX_CODE_LABCI_UNDEFINED_ERROR, e);
                } else if (e instanceof ConnectTimeoutException) {
                    QuotesLabciService.logger.error("Access Labci error", e);
                    throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_LABCI_ERROR, e);
                } else {
                    QuotesLabciService.logger.error("Labci server error", e);
                    throw new VendorException(ExCodeConstant.EX_CODE_LABCI_SERVER_ERROR, e);
                }
            }
        }
        return serviceResponseMapper;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService#
     * validateServiceResponse(java.lang.Object)
     */
    @Override
    protected Object validateServiceResponse(final Object serviceResponse) throws Exception {
        @SuppressWarnings("unchecked")
        Map<String, String> serviceResponseMapper = (Map<String, String>) serviceResponse;
        Object validServiceResponseMapper = new Object();
        validServiceResponseMapper = LabciResponse(serviceResponseMapper);
        return validServiceResponseMapper;
    }

    /**
     * @Title LabciResponse
     * @Description
     * @param serviceResponseMapper
     * @return
     * @return Object
     * @throws Exception
     * @Author OJim
     * @Date Jun 24, 2019 2:40:35 PM
     */
    private Object LabciResponse(Map<String, String> serviceResponseMapper) throws Exception {
        Map<String, Map<String, LabciResponse>> validServiceResponseMapper = new LinkedHashMap<>();
        List<String> validates = new ArrayList<>();
        for (Map.Entry<String, String> responseMapper : serviceResponseMapper.entrySet()) {
            String key = responseMapper.getKey();
            Map<String, LabciResponse> map = EachLabciResponse(key, responseMapper.getValue());
            if (!map.isEmpty()) {
                validServiceResponseMapper.put(key, map);
                for (String string : map.keySet()) {
                    validates.add(string);
                }
            }
        }
        @SuppressWarnings("unchecked")
        List<String> symbols = (List<String>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_PRODUCT_KEY);
        if (validates != null && validates.size() != symbols.size()) {
            ExResponseEntity exResponse = this.exRespComponent
                    .getExResponse(ExCodeConstant.EX_CODE_LABCI_STOCK_NOT_FOUND);
            addMessage(symbols, validates, exResponse);
        }
        try {
            saveAccessLogLabci(validServiceResponseMapper);
//			throw new ApplicationException(ExCodeConstant.EX_CODE_DB_ACTION_FAIL);
        } catch (Exception e) {
            QuotesLabciService.logger.error("Error: Failed to write quotes history", e);
            throw new ApplicationException(ExCodeConstant.EX_CODE_DB_ACTION_FAIL);
        }
        return validServiceResponseMapper;
    }

    /**
     * @Title saveAccessLog
     * @Description
     * @param validServiceResponseMapper
     * @return void
     * @throws Exception
     * @Author OJim
     * @Date Jul 9, 2019 7:20:59 PM
     */
    private void saveAccessLogLabci(Map<String, Map<String, LabciResponse>> validServiceResponseMapper)
            throws Exception {
        List<QuoteAccessLogForQuotes> quoteAccessLogs = new ArrayList<>();
        SECQuotesRequest request = (SECQuotesRequest) ArgsHolder
                .getArgs(Constant.THREAD_INVISIBLE_QUOTES_LABCI_REQUEST);
        CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
        Long userReferenceId = getUserId();
        for (Map.Entry<String, Map<String, LabciResponse>> jsonArrayMapper : validServiceResponseMapper.entrySet()) {
            QuoteAccessLogForQuotes quoteAccessLog = new QuoteAccessLogForQuotes();
            Map<String, LabciResponse> resultJsonArray = jsonArrayMapper.getValue();
            String key = jsonArrayMapper.getKey();
            String exchangeCode = "";
            StringBuffer sb = new StringBuffer();
            StringBuffer stringBuffer = new StringBuffer();
            for (Map.Entry<String, LabciResponse> jsonArrayMapper2 : resultJsonArray.entrySet()) {
                if (jsonArrayMapper2.getKey().contains(QuotesLabciService.KEY_D)) {
                    continue;
                }
                Map<String, Object> data = jsonArrayMapper2.getValue().getFields();
                PredSrchResponse predSrchResp = this.predSrchService.localPredSrch(jsonArrayMapper2.getKey(),
                        jsonArrayMapper.getKey().split(SymbolConstant.SYMBOL_VERTICAL_LINE_ESCAPE)[1],
                        Constant.PROD_CDE_ALT_CLASS_CODE_T);
                String symbol = predSrchResp.getSymbol();
                BigDecimal quotePrice = LabciPropsUtil.inOrderNumberProps("nominalPrice", key, data);
                String quote = quotePrice != null ? quotePrice.toString() : "";
                exchangeCode = predSrchResp.getExchange();
                stringBuffer.append(symbolToStr(symbol, quote));
                stringBuffer.append(SymbolConstant.SYMBOL_SEMISOLON);
            }
            if (stringBuffer.length() == 0) {
                continue;
            }
            exchangeCode = this.labciProps.getExchangeMapping(exchangeCode);
            sb.append(stringBuffer);
            sb.append(SymbolConstant.SYMBOL_VERTICAL_LINE);
            quoteAccessLog.setUserReferenceId(userReferenceId);
            quoteAccessLog.setMarketCode(request.getMarket());
            quoteAccessLog.setExchangeCode(exchangeCode);
            quoteAccessLog.setApplicationCode(header.getAppCode());
            quoteAccessLog.setCountryCode(header.getCountryCode());
            quoteAccessLog.setGroupMember(header.getGroupMember());
            quoteAccessLog.setChannelId(header.getChannelId());
            quoteAccessLog.setFuntionId(header.getFunctionId());
            quoteAccessLog.setAccessCommand(ArgsHolder.getArgs(QuotesLabciService.ACCES_CMND_CDE).toString());
            // quoteAccessLog.setChargeCategory(null);
            quoteAccessLog.setRequestType(
                    isDelay(request) ? QuotesLabciService.ACCES_TYPE_DELAY : QuotesLabciService.ACCES_TYPE_REAL_TIME);
            quoteAccessLog.setAccessCount(resultJsonArray.entrySet().size());
            quoteAccessLog.setResponseType((boolean) ArgsHolder.getArgs(QuotesLabciService.THREAD_INVISIBLE_DELAY)
                    ? QuotesLabciService.ACCES_TYPE_DELAY
                    : QuotesLabciService.ACCES_TYPE_REAL_TIME);
            quoteAccessLog.setResponseText(sb.toString());
            // quoteAccessLog.setCommentText(QuotesLabciService.ACCES_SYS_CHANNEL);
            quoteAccessLog.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
            quoteAccessLogs.add(quoteAccessLog);
        }
        this.quoteAccessLogService.saveQuoteAccessLog(quoteAccessLogs);
    }

    private Long getUserId() {
        CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
        QuoteUserForQuotes quoteUser = new QuoteUserForQuotes();
        quoteUser.setUserExtnlId(ArgsHolder.getArgs(QuotesLabciService.CUSTOMER_ID).toString());
        String channelId = header.getChannelId();
        if ("OHB".equals(channelId)) {
            quoteUser.setUserType(QuotesLabciService.USER_TYPE_CDE_STFF);
        } else {
            quoteUser.setUserType(QuotesLabciService.USER_TYPE_CDE_CUST);
        }
        quoteUser.setGroupMember(header.getGroupMember());
        quoteUser.setUserMarketCode(header.getCountryCode());
        quoteUser.setMonitorFlag((long) 0);
        return this.quoteUserService.getUserByExtnlId(quoteUser);
    }

    private String symbolToStr(final String symbol, final String quote) {
        String priceDetail = "";
        if (null != quote) {
            priceDetail = QuotesLabciService.KEY_L + quote;
        }
        String result = symbol + SymbolConstant.SYMBOL_LEFT_CIRCLE_BRACKET + priceDetail
                + SymbolConstant.SYMBOL_RIGHT_CIRCLE_BRACKET;
        return result;
    }

    private Map<String, LabciResponse> EachLabciResponse(final String key, final String serviceResponse)
            throws JAXBException {
        String labciResponses = serviceResponse;
        JAXBContext jaxbContext = JAXBContext.newInstance(Watchlist.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Watchlist res = (Watchlist) unmarshaller.unmarshal(new StringReader(labciResponses));
        Map<String, LabciResponse> response = new LinkedHashMap<>();
        if (null != res) {
            List<Ric> ricList = res.getRic();
            if (!ListUtils.isEmpty(ricList)) {
                response = LabciServletBoConvertor.getResponseMap(ricList);
            }
        }
        response = convertLabciResponse(response, key);
        return response;
    }

    /**
     * @Title convertLabciResponse
     * @Description
     * @return
     * @return Map<String, com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci.LabciResponse>
     * @Author OJim
     */
    private Map<String, LabciResponse> convertLabciResponse(Map<String, LabciResponse> response, String key) {
        List<String> removeList = new ArrayList<>();
        for (Map.Entry<String, LabciResponse> mEntry : response.entrySet()) {
            String symbolName = mEntry.getKey() + QuotesLabciService.KEY_D;
            if (response.containsKey(symbolName)) {
                removeList.add(symbolName);
                Map<String, Object> data = mEntry.getValue().getFields();
                Map<String, Object> data2 = response.get(symbolName).getFields();
                String[] bidAskQueues = LabciPropsUtil.getLabciFields("bidAskQueues", key);
                for (int i = 0; i < bidAskQueues.length; i++) {
                    String bigAskString = bidAskQueues[i];
                    if (i < 6) {
                        data.put(bigAskString,
                                Constant.ZERO.equals(data2.get(bigAskString).toString()) ? data.get(bigAskString)
                                        : data2.get(bigAskString));
                    } else {
                        data.put(bigAskString, data2.get(bigAskString));
                    }
                }
                mEntry.getValue().setFields(data);
            }
        }
        for (String remove : removeList) {
            response.remove(remove);
        }
        return response;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService#
     * convertResponse(java.lang.Object)
     */
    @Override
    protected QuotesLabciResponse convertResponse(final Object validServiceResponse) throws Exception {
        QuotesLabciResponse response = new QuotesLabciResponse();
        response.setPriceQuotes(labciResponse(validServiceResponse));
        response.setMessages(getMessages());
        response.setRemainingQuota(new BigDecimal("-1"));
        response.setTotalQuota(new BigDecimal("-1"));
        return response;
    }

    /**
     * @Title labciResponse
     * @Description
     * @param validServiceResponse
     * @return
     * @return List<QuotesLabciQuote>
     * @Author OJim
     * @Date Jun 25, 2019 2:06:46 PM
     */
    private List<QuotesLabciQuote> labciResponse(Object validServiceResponse) {
        @SuppressWarnings("unchecked")
        Map<String, Map<String, LabciResponse>> resultJsonArrayMapper = (Map<String, Map<String, LabciResponse>>) validServiceResponse;
        List<QuotesLabciQuote> priceQuotes = new ArrayList<>();
        CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
        for (Map.Entry<String, Map<String, LabciResponse>> jsonArrayMapper : resultJsonArrayMapper.entrySet()) {
            String key = jsonArrayMapper.getKey();
            Map<String, LabciResponse> resultJsonArray = jsonArrayMapper.getValue();
            for (Map.Entry<String, LabciResponse> jsonArrayMapper2 : resultJsonArray.entrySet()) {
                if (jsonArrayMapper2.getKey().contains(QuotesLabciService.KEY_D)) {
                    continue;
                }
                Map<String, Object> data = jsonArrayMapper2.getValue().getFields();
                QuotesLabciQuote priceQuote = new QuotesLabciQuote();
                priceQuotes.add(priceQuote);
                PredSrchResponse predSrchResp = this.predSrchService.localPredSrch(jsonArrayMapper2.getKey(),
                        jsonArrayMapper.getKey().split(SymbolConstant.SYMBOL_VERTICAL_LINE_ESCAPE)[1],
                        Constant.PROD_CDE_ALT_CLASS_CODE_T);
                if (predSrchResp != null) {
                    priceQuote.setProdAltNumSegs(predSrchResp.getProdAltNumSegs());
                    priceQuote.setSymbol(predSrchResp.getSymbol());
                    priceQuote.setMarket(predSrchResp.getMarket());
                    priceQuote.setExchangeCode(this.labciProps.getExchangeMapping(predSrchResp.getExchange()));
                    priceQuote.setProductType(predSrchResp.getProductType());
                    priceQuote.setProductSubType(predSrchResp.getProductSubType());
                    priceQuote.setCompanyName(StringUtil.isInValid(predSrchResp.getProductName())
                            ? companyName(key, data, header.getLocale())
                            : predSrchResp.getProductName());
                    priceQuote.setRiskLvlCde(predSrchResp.getRiskLvlCde());
                    priceQuote.setAsOfDateTime(LabciPropsUtil.dateProps("asOfDateTime", key, data,
                            this.appProps.getTimezone(this.labciProps.getExchangeMapping(predSrchResp.getExchange()))));

                }
                priceQuote.setNominalPrice(LabciPropsUtil.inOrderNumberProps("nominalPrice", key, data));
                priceQuote.setCurrency(LabciPropsUtil.inOrderStrProps("currency", key, data));
                try {
                    String quoteOffClose = LabciPropsUtil.inOrderStrProps("quoteOffClose", key, data);
                    if (Constant.ZERO.equals(quoteOffClose) || StringUtil.isInValid(quoteOffClose)) {
                        priceQuote.setChangeAmount(LabciPropsUtil.subtractProps("changeAmount", key, data));
                        priceQuote.setChangePercent(LabciPropsUtil.growthRateProps("changePercent", key, data,
                                new MathContext(4, RoundingMode.HALF_UP)));
                    } else {
                        priceQuote.setChangeAmount(LabciPropsUtil.subtractProps("offCloseSubChangeAmount", key, data));
                        priceQuote.setChangePercent(LabciPropsUtil.growthRateProps("offCloseSubChangePercent", key,
                                data, new MathContext(4, RoundingMode.HALF_UP)));
                    }
                } catch (Exception e) {
                    priceQuote.setChangeAmount(LabciPropsUtil.subtractProps("changeAmount", key, data));
                    priceQuote.setChangePercent(LabciPropsUtil.growthRateProps("changePercent", key, data,
                            new MathContext(4, RoundingMode.HALF_UP)));
                }
                priceQuote.setDelay((boolean) ArgsHolder.getArgs(QuotesLabciService.THREAD_INVISIBLE_DELAY));

                priceQuote.setBidPrice(LabciPropsUtil.inOrderNumberProps("bidPrice", key, data));
                priceQuote.setBidSize(LabciPropsUtil.inOrderNumberProps("bidSize", key, data));
                priceQuote.setAskPrice(LabciPropsUtil.inOrderNumberProps("askPrice", key, data));
                priceQuote.setAskSize(LabciPropsUtil.inOrderNumberProps("askSize", key, data));
                priceQuote.setBidSpread(LabciPropsUtil.inOrderNumberProps("bidSpread", key, data));
                priceQuote.setAskSpread(LabciPropsUtil.inOrderNumberProps("askSpread", key, data));
                priceQuote.setOpenPrice(LabciPropsUtil.inOrderNumberProps("openPrice", key, data));
                priceQuote.setPreviousClosePrice(LabciPropsUtil.inOrderNumberProps("previousClosePrice", key, data));
                priceQuote.setDayLowPrice(LabciPropsUtil.inOrderNumberProps("dayLowPrice", key, data));
                priceQuote.setDayHighPrice(LabciPropsUtil.inOrderNumberProps("dayHighPrice", key, data));
                priceQuote.setYearLowPrice(LabciPropsUtil.inOrderNumberProps("yearLowPrice", key, data));
                priceQuote.setYearHighPrice(LabciPropsUtil.inOrderNumberProps("yearHighPrice", key, data));
                priceQuote.setVolume(LabciPropsUtil.inOrderNumberProps("volume", key, data));
                priceQuote.setBoardLotSize(LabciPropsUtil.inOrderNumberProps("boardLotSize", key, data));
                priceQuote.setMarketCap(LabciPropsUtil.inOrderNumberProps("marketCap", key, data));
                priceQuote.setPeRatio(LabciPropsUtil.inOrderNumberProps("peRatio", key, data));
                priceQuote.setEps(LabciPropsUtil.inOrderNumberProps("eps", key, data));
                priceQuote.setTurnover(LabciPropsUtil.inOrderNumberProps("turnover", key, data));
                priceQuote.setDividend(LabciPropsUtil.inOrderNumberProps("dividend", key, data));
                priceQuote.setDividendYield(LabciPropsUtil.inOrderNumberProps("dividendYield", key, data));
                priceQuote.setBidAskQueues(LabciPropsUtil.bidAskQueues(key, data));

                priceQuote.setUpperLimitPrice(LabciPropsUtil.inOrderNumberProps("upperTradingLimit", key, data));
                priceQuote.setLowerLimitPrice(LabciPropsUtil.inOrderNumberProps("lowerTradingLimit", key, data));

                priceQuote.setSharesOutstanding(LabciPropsUtil.inOrderNumberProps("sharesOutstanding", key, data));
                priceQuote.setNominalPriceType(LabciPropsUtil.inOrderStrProps("quoteNominalPriceType", key, data));

                priceQuote.setIsSuspended(LabciPropsUtil.inOrderBooPropsForIsSuspended(
                        LabciPropsUtil.inOrderStrProps("quoteNominalPriceType", key, data), header.getCountryCode(),
                        this.labciProps.getSuspendFlagConfig()));

                priceQuote.setIep(LabciPropsUtil.inOrderNumberProps("iep", key, data));
                priceQuote.setIev(LabciPropsUtil.inOrderNumberProps("iev", key, data));

                priceQuote.setRiskAlert(LabciPropsUtil
                        .inOrderBooPropsForRiskAlert(LabciPropsUtil.inOrderStrProps("riskAlert", key, data)));

                //add dp migration fields
                if ("CN".equals(predSrchResp.getMarket())) {
                    aSharesStockService.setASharesInfo(priceQuote);
                }
            }
        }
        return priceQuotes;
    }

    private String companyName(String key, Map<String, Object> data, String local) {
        String name = LabciPropsUtil.inOrderStrProps("localName", key, data);
        if (StringUtil.isValid(name)) {
            String[] names = name.split(SymbolConstant.SYMBOL_VERTICAL_LINE_ESCAPE);
            switch (local) {
                case "zh_CN":
                    return names[1];
                case "zh_HK":
                    return names[0];
                default:
                    return LabciPropsUtil.inOrderStrProps("quoteName", key, data);
            }
        }
        return LabciPropsUtil.inOrderStrProps("quoteName", key, data);
    }

    @SuppressWarnings("unchecked")
    private List<Message> getMessages() {
        if (ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES) == null) {
            return null;
        } else {
            return (List<Message>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES);
        }
    }
}
