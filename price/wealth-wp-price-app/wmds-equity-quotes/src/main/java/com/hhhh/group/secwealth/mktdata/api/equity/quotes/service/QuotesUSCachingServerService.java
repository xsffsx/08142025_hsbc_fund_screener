/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

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
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.*;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.pojo.LastTradeRecord;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.pojo.StockInfo;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.SECQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ServiceProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.service.QuotesServiceRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.Message;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.PastPerformance;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesLabciQuote;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesLabciResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.service.stockinfo.QuotesUSLabciPortalService;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.util.ConvertorsUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.util.TradingHourUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResponse;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResult;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResultStateEnum;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.utils.CacheDistributeHolder;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.exception.ExTraceCodeGenerator;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.ExResponseComponent;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.entity.ExResponseEntity;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
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

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service("quotesUSCachingServerService")
@ConditionalOnProperty(value = "service.quotes.usEquity.injectEnabled", havingValue = "true")
@SuppressWarnings({"java:S3776", "java:S1452"})
public class QuotesUSCachingServerService extends AbstractBaseService<SECQuotesRequest, QuotesLabciResponse, CommonRequestHeader> {

    private static final Logger logger = LoggerFactory.getLogger(QuotesUSCachingServerService.class);

    public static final String ACCES_CMND_CDE = "QUOTE_LABCI_ACCES_CMND_CDE";

    protected static final String ACCES_CMND_CDE_QUOTE_DETAIL = "QUOTE_DETAIL";

    public static final String ACCES_CMND_CDE_QUOTE_LIST = "QUOTE_LIST";

    public static final String THREAD_INVISIBLE_QUOTES_US_REQUEST = "QUOTES_US_REQUEST";

    private static final String THREAD_INVISIBLE_QUOTES_TRADING_HOUR = "QUOTES_TRADING_HOUR";

    public static final String THREAD_INVISIBLE_QUOTES_PRODUCT_KEY_MAPPER = "PRODUCT_KEY_MAPPER";

    public static final String MARKET_CODE = "US";

    private static final String SATFF_CHANNEL_ID = "OHB";

    private static final String WD_APP_CODE = "WD";

    private static final String REQUEST_PRODUCTKEYS_PRODUCTTYPE_CHECK = "|ALL|SEC|WRTS|";

    protected static final String KEY_D = "d";

    private static final String KEY_L = "L=";

    private static final String ACCES_TYPE_DELAY = "EDSTK";

    private static final String ACCES_TYPE_REAL_TIME = "STOCK";

    public static final String CUSTOMER_ID = "CUSTOMER_ID";

    private static final String USER_TYPE_CDE_CUST = "CUST";

    private static final String HEADER_FUNCTION_ID_CHECK = "|00|01|02|03|04|05|06|07|08|09|10|21|22|23|24|25|26|27|28|29|";

    private static final String DATEFORMAT_DDMMYYYHHMMSS = "dd MMM yyyy'T'HH:mm:ss";

    @Autowired
    private ExResponseComponent exRespComponent;

    @Autowired
    private ApplicationProperties appProps;

    @Autowired
    private QuotesUSCommonService quotesUSCommonService;

    @Autowired
    private PredSrchService predSrchService;

    @Autowired
    private LabciProperties labciProperties;

    @Autowired
    @Qualifier("quotesQuoteUserService")
    private QuoteUserService quoteUserService;

    @Autowired
    @Setter
    @Getter
    private QuotesUSLabciPortalService quotesUSLabciPortalService;

    @Autowired
    private PastPerformanceService pastPerformanceService;

    @Autowired
    private QuotesUSLogService quotesUSLogService;

    protected void initCustomerID(CommonRequestHeader header) {
        if ("OHB".equals(header.getChannelId())||"CMB".equals(header.getAppCode())){
            HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String staffID = httpRequest.getHeader(Constant.REQUEST_HEADER_KEY_USER_ID);
            ArgsHolder.putArgs(QuotesUSCachingServerService.CUSTOMER_ID, staffID);
        }else {
            /**
             * Before get result from the Cache Distribute, you can do some other business
             * logic
             */
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
                        logger.error("No eID found from rbp cache");
                        throw new VendorException(ExCodeConstant.EX_CODE_CACHE_NO_EID_FOUND);
                    }
                    ArgsHolder.putArgs(QuotesUSCachingServerService.CUSTOMER_ID, customerId);
                } catch (Exception e) {
                    logger.error("Cache Distribute Bad Request");
                    throw new VendorException(ExCodeConstant.EX_CODE_CACHE_BAD_REQUEST);
                }
            } else {
                if (resultState == CacheDistributeResultStateEnum.INVALID_PARAMETERS) {
                    logger.error("Cache Distribute Bad Request");
                    throw new VendorException(ExCodeConstant.EX_CODE_CACHE_BAD_REQUEST);
                }
                if (resultState == CacheDistributeResultStateEnum.UNCACHED_RECORD) {
                    logger.error("Cache Distribute don't contains the key you sent");
                    throw new VendorException(ExCodeConstant.EX_CODE_CACHE_UNCACHED_RECORD);
                }
                if (resultState == CacheDistributeResultStateEnum.INVALID_HTTP_STATUS) {
                    logger.error("Get response from the Cache Distribute encounter error");
                    throw new VendorException(ExCodeConstant.EX_CODE_CACHE_INVALID_HTTP_STATUS);
                }
            }
        }
    }

    @Override
    protected Object convertRequest(final SECQuotesRequest request, final CommonRequestHeader header) throws Exception {

        if (!HEADER_FUNCTION_ID_CHECK.contains(
                SymbolConstant.SYMBOL_VERTICAL_LINE + header.getFunctionId() + SymbolConstant.SYMBOL_VERTICAL_LINE)) {
            logger.error("Header FunctionId don't match the rules or can't be empty.");
            throw new CommonException(ExCodeConstant.EX_CODE_HEADER_FUNCTIONID_INVALID);
        }
        String eID = request.getEid();
        String appCode = header.getAppCode();
        if(StringUtil.isValid(eID) || QuotesUSCachingServerService.WD_APP_CODE.equals(appCode)){
            ArgsHolder.putArgs(QuotesUSCachingServerService.CUSTOMER_ID, eID);
        }else {
            initCustomerID(header);
        }
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER, header);
        ArgsHolder.putArgs(QuotesUSCachingServerService.THREAD_INVISIBLE_QUOTES_US_REQUEST, request);

        this.quotesUSCommonService.validate(request, header);

        boolean isTradingHour = false;
        boolean isDelay = request.getDelay();

        if (!Arrays.asList(QuotesUSCommonService.EXCLUDE_APPCODE).contains(header.getAppCode())
                && this.quotesUSCommonService.checkTradingDate(header)) {
            if (TradingHourUtil.withinTradingHour(true, this.quotesUSCommonService.checkTradingHour(header))) {
                isTradingHour = true;
            }
        }

        QuotesServiceRequest quotesServiceRequest = buildServiceRequest(request, header);

        if (Constant.CASE_20.equals(request.getRequestType()) || Constant.CASE_0.equals(request.getRequestType())) {
            ArgsHolder.putArgs(ACCES_CMND_CDE, ACCES_CMND_CDE_QUOTE_DETAIL);
        } else {
            ArgsHolder.putArgs(ACCES_CMND_CDE, ACCES_CMND_CDE_QUOTE_LIST);
        }

        ArgsHolder.putArgs(QuotesUSCommonService.THREAD_INVISIBLE_QUOTES_DELAY, isDelay);
        ArgsHolder.putArgs(QuotesUSCachingServerService.THREAD_INVISIBLE_QUOTES_TRADING_HOUR, isTradingHour);


        String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
        // realtime=IDN_RDF, delay=dIDN_RDF
        String service = this.labciProperties.getLabciService(site, this.isDelayedQuotes(isDelay, isTradingHour));

        // key=HK_hhhh|SEC|NYQ value=productKey
        Map<String, List<ServiceProductKey>> groupedProductKeysMapper = getGroupedProductKeysMapper(site, quotesServiceRequest);
        ArgsHolder.putArgs(QuotesUSCachingServerService.THREAD_INVISIBLE_QUOTES_PRODUCT_KEY_MAPPER, groupedProductKeysMapper);
        return extractServiceMapper(groupedProductKeysMapper, service);
    }

    // to reuse method when need to re-call service with delay
    protected Map<String, String> extractServiceMapper(final Map<String, List<ServiceProductKey>> groupedProductKeysMapper,
                                                     final String service) {
        Map<String, String> serviceRequestMapper = new LinkedHashMap<String, String>();
        for (Map.Entry<String, List<ServiceProductKey>> groupedProductKeys : groupedProductKeysMapper.entrySet()) {
            String key = groupedProductKeys.getKey();
            String[] keys = key.split(SymbolConstant.SYMBOL_VERTICAL_LINE_ESCAPE);
            List<ServiceProductKey> productKeys = groupedProductKeys.getValue();
            List<String> items = new ArrayList<>();
            for (ServiceProductKey productKey : productKeys) {
                items.add(productKey.getProdAltNum());
            } // dIDN_RDF.ANY.VOO.NaE
            String symbolList = LabciServletBoConvertor.genSymbols(items, service);
            List<NameValuePair> labciParams = new ArrayList<NameValuePair>();
            labciParams.add(new BasicNameValuePair("SymbolList", symbolList));
            labciParams.add(new BasicNameValuePair("FieldList",
                    StringUtils.join(this.labciProperties.getLabciFields(
                            this.appProps.getQuotesResponseLabciFields(keys), keys),
                            SymbolConstant.SYMBOL_SEMISOLON)));
            labciParams.add(new BasicNameValuePair("DataRepresentation", QuotesUSCommonService.LABCI_DATA_REPRESENTATION_XML));
            String labciReqParams = URLEncodedUtils.format(labciParams, QuotesUSCommonService.LABCI_UNICODE);

            serviceRequestMapper.put(key, labciReqParams);
        }
        return serviceRequestMapper;
    }

    protected QuotesServiceRequest buildServiceRequest(final SECQuotesRequest request, final CommonRequestHeader header) {
        QuotesServiceRequest quoteServiceRequest = new QuotesServiceRequest();
        quoteServiceRequest.setDelay(request.getDelay() == null ? true : request.getDelay());
        List<String> reqSymbolList = new ArrayList<String>();
        List<ProductKey> reqInvalidSymbolList = new ArrayList<ProductKey>();
        Map<String, List<String>> symbolsMap = new LinkedHashMap<>();

        List<ProductKey> productKeyList = request.getProductKeys();

        for (int i = 0; i < productKeyList.size(); i++) {
            if (Constant.PROD_CDE_ALT_CLASS_CODE_M.equals(productKeyList.get(i).getProdCdeAltClassCde())) {

                ProductKey productKey = productKeyList.get(i);

                if (StringUtil.isInValid(productKey.getProductType())) {
                    productKey.setProductType(Constant.APP_SUPPORT_ALL_EXCHANGES);
                } else if (!REQUEST_PRODUCTKEYS_PRODUCTTYPE_CHECK
                        .contains(SymbolConstant.SYMBOL_VERTICAL_LINE + productKey.getProductType()
                                + SymbolConstant.SYMBOL_VERTICAL_LINE)) {
                    logger.error("Request productKeys productType don't match the rules.");
                    throw new CommonException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
                }
                reqSymbolList.add(productKey.getProdAltNum());
                if (symbolsMap.containsKey(productKey.getProductType())) {
                    symbolsMap.get(productKey.getProductType())
                            .add(productKey.getProdAltNum());
                } else {
                    List<String> symbolm = new ArrayList<>();
                    symbolm.add(productKey.getProdAltNum());
                    symbolsMap.put(productKey.getProductType(), symbolm);
                }
            }
        }

        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_QUOTES_LABCI_REQUEST, request);
        if (Constant.CASE_20.equals(request.getRequestType())) {
            List<String> symbol = new ArrayList<>();
            if (request.getProductKeys().size() > 0) {
                ProductKey productKey = request.getProductKeys().get(0);
                symbol.add(productKey.getProdAltNum());

                symbolsMap = new LinkedHashMap<>();
                List<String> strings = new ArrayList<>();
                strings.add(productKey.getProdAltNum());
                symbolsMap.put(productKey.getProductType(), strings);
            }
            if (reqSymbolList.size() > 1) {
                ExResponseEntity exResponse = this.exRespComponent
                        .getExResponse(ExCodeConstant.EX_CODE_QUOTE_DETAIL_ONLY_SUPPORT_ONE_PRODALTNUM);
                addMessage(reqSymbolList, symbol, exResponse);
            }
            reqSymbolList = symbol;
        }

        List<PredSrchResponse> responses = new ArrayList<>();
        for (Map.Entry<String, List<String>> symbolMap : symbolsMap.entrySet()) {
            PredSrchRequest predSrchRequest = new PredSrchRequest();
            String[] assetClasses = new String[1];
            assetClasses[0] = symbolMap.getKey();
            predSrchRequest.setAssetClasses(assetClasses);
            predSrchRequest.setKeyword(symbolMap.getValue().toArray(new String[symbolMap.getValue().size()]));
            predSrchRequest.setTopNum(String.valueOf(symbolMap.getValue().size()));
            predSrchRequest.setMarket(MARKET_CODE);
            try {
                responses.addAll(this.predSrchService.precSrch(predSrchRequest, header));
            } catch (VendorException e) {
                String message = e.getMessage();
                if (ExCodeConstant.EX_CODE_ACCESS_PREDSRCH_ERROR.equals(message)){
                    throw e;
                }
            } catch (Exception e) {
                logger.warn("precSearch error is :" + e.toString());
            }
        }
        if (responses.size() != reqSymbolList.size()) {
            ExResponseEntity exResponse = this.exRespComponent
                    .getExResponse(ExCodeConstant.EX_CODE_PREDSRCH_EMPTY_RESPONSE);
            List<String> validates = new ArrayList<>();
            for (int i = 0; i < responses.size(); i++) {
                validates.add(responses.get(i).getSymbol());
            }
            addMessage(reqSymbolList, validates, exResponse);
        }
        setServiceProductKeys(quoteServiceRequest, request, symbolsMap, responses);

        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_PRODUCT_KEY, reqSymbolList);
        return quoteServiceRequest;
    }

    protected void setServiceProductKeys(QuotesServiceRequest quoteServiceRequest,
                                         SECQuotesRequest request,
                                         Map<String, List<String>> symbolsMap,
                                         List<PredSrchResponse> responses) {
        quoteServiceRequest.setServiceProductKeys(ConvertorsUtil.constructServiceProductKeys(request.getProductKeys(),
                symbolsMap, responses, Constant.PROD_CDE_ALT_CLASS_CODE_T));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object execute(final Object serviceRequest) throws Exception {
        Map<String, String> serviceResponseMapper = new LinkedHashMap<String, String>();
        Map<String, String> serviceRequestMapper = (Map<String, String>) serviceRequest;

        String url = this.labciProperties.getLabciNbUrl();
        for (Map.Entry<String, String> requestMapper : serviceRequestMapper.entrySet()) {
            String key = requestMapper.getKey();
            String request = requestMapper.getValue();
            String labciResponse = this.quotesUSCommonService.callLabci(url, request);
            serviceResponseMapper.put(key, labciResponse);
        }
        return serviceResponseMapper;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object validateServiceResponse(final Object serviceResponse) throws Exception {
        Map<String, String> serviceResponseMapper = (Map<String, String>) serviceResponse;
        Object validServiceResponseMapper = validateLabciResponse(serviceResponseMapper);
        return validServiceResponseMapper;
    }

    @Override
    protected QuotesLabciResponse convertResponse(final Object validServiceResponse) throws Exception {
        QuotesLabciResponse response = new QuotesLabciResponse();
        response.setPriceQuotes(convertLabciResponse(validServiceResponse));
        response.setMessages(getMessages());
        response.setRemainingQuota(new BigDecimal("-1"));
        response.setTotalQuota(new BigDecimal("-1"));
        return response;
    }

    protected List<Message> getMessages() {
        if (ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES) == null) {
            return null;
        } else {
            return (List<Message>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES);
        }
    }

    private boolean isDelayedQuotes(final boolean isDelay, final boolean isTradingHour) {
        if (isDelay || !isTradingHour) {
            return true;
        } else {
            return false;
        }
    }

    protected Map<String, List<ServiceProductKey>> getGroupedProductKeysMapper(final String site,
                                                                             final QuotesServiceRequest request) {
        Map<String, List<ServiceProductKey>> groupedProductKeysMapper = new LinkedHashMap<String, List<ServiceProductKey>>();
        List<ServiceProductKey> productKeys = request.getServiceProductKeys();
        for (ServiceProductKey productKey : productKeys) {
            String key =
                    new StringBuilder().append(site).append(SymbolConstant.SYMBOL_VERTICAL_LINE).append(productKey.getProductType())
                            .append(SymbolConstant.SYMBOL_VERTICAL_LINE).append(productKey.getExchange()).toString();
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

    @SuppressWarnings("unchecked")
    private Object validateLabciResponse(final Map<String, String> serviceResponseMapper) throws Exception {
        //Map<String, String> serviceResponseMapper = (Map<String, String>) serviceResponse;
        Object validServiceResponseMapper = LabciResponse(serviceResponseMapper);
        return validServiceResponseMapper;
    }

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

        return validServiceResponseMapper;
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

    private Map<String, LabciResponse> convertLabciResponse(Map<String, LabciResponse> response, String key) {
        List<String> removeList = new ArrayList<>();
        for (Map.Entry<String, LabciResponse> mEntry : response.entrySet()) {
            String symbolName = mEntry.getKey() + KEY_D;
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

    protected List<? extends QuotesLabciQuote> convertLabciResponse(Object validServiceResponse) {
        @SuppressWarnings("unchecked")
        Map<String, Map<String, LabciResponse>> resultJsonArrayMapper = (Map<String, Map<String, LabciResponse>>) validServiceResponse;
        List<QuotesLabciQuote> priceQuotes = new ArrayList<>();
        Map<String, List<QuotesLabciQuote>> labciQuoteWithExchge = new HashMap<String, List<QuotesLabciQuote>>();
        CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
        for (Map.Entry<String, Map<String, LabciResponse>> jsonArrayMapper : resultJsonArrayMapper.entrySet()) {
            String key = jsonArrayMapper.getKey();
            Map<String, LabciResponse> resultJsonArray = jsonArrayMapper.getValue();
            for (Map.Entry<String, LabciResponse> jsonArrayMapper2 : resultJsonArray.entrySet()) {
                if (jsonArrayMapper2.getKey().contains(KEY_D)) {
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
                    priceQuote.setExchangeCode(this.labciProperties.getExchangeMapping(predSrchResp.getExchange()));
                    priceQuote.setProductType(predSrchResp.getProductType());
                    priceQuote.setProductSubType(predSrchResp.getProductSubType());
                    priceQuote.setCompanyName(StringUtil.isInValid(predSrchResp.getProductName())
                            ? companyName(key, data, header.getLocale())
                            : predSrchResp.getProductName());
                    priceQuote.setRiskLvlCde(predSrchResp.getRiskLvlCde());
                }

                //priceQuote.setNominalPrice(LabciPropsUtil.inOrderNumberProps("nominalPrice", key, data));
                String[] labciFields = new String[]{"TRDPRC_1", "HST_CLOSE"};
                priceQuote.setNominalPrice(inOrderNumberProps4US(labciFields, data));
                priceQuote.setCurrency(LabciPropsUtil.inOrderStrProps("currency", key, data));

                if (!afterCleanRun(data)) {
                    priceQuote.setChangeAmount(LabciPropsUtil.inOrderNumberProps("changeAmount", key, data));
                    priceQuote.setChangePercent(LabciPropsUtil.inOrderNumberProps("changePercent", key, data));
                } else {
                    priceQuote.setChangeAmount(BigDecimalUtil.fromString("0"));
                    priceQuote.setChangePercent(BigDecimalUtil.fromString("0"));
                }

                priceQuote.setDelay((boolean) ArgsHolder.getArgs(QuotesUSCommonService.THREAD_INVISIBLE_QUOTES_DELAY));

                priceQuote.setBidPrice(LabciPropsUtil.inOrderNumberProps("bidPrice", key, data));
                priceQuote.setBidSize(LabciPropsUtil.inOrderNumberProps("bidSize", key, data));
                priceQuote.setAskPrice(LabciPropsUtil.inOrderNumberProps("askPrice", key, data));
                priceQuote.setAskSize(LabciPropsUtil.inOrderNumberProps("askSize", key, data));
                priceQuote.setBidSpread(LabciPropsUtil.inOrderNumberProps("bidSpread", key, data));
                priceQuote.setAskSpread(LabciPropsUtil.inOrderNumberProps("askSpread", key, data));
                priceQuote.setOpenPrice(LabciPropsUtil.inOrderNumberProps("openPrice", key, data));
                priceQuote.setPreviousClosePrice(inOrderNumberProps4US(new String[]{"HST_CLOSE"}, data));
                priceQuote.setDayLowPrice(LabciPropsUtil.inOrderNumberProps("dayLowPrice", key, data));
                priceQuote.setDayHighPrice(LabciPropsUtil.inOrderNumberProps("dayHighPrice", key, data));
                priceQuote.setYearLowPrice(LabciPropsUtil.inOrderNumberProps("yearLowPrice", key, data));
                priceQuote.setYearHighPrice(LabciPropsUtil.inOrderNumberProps("yearHighPrice", key, data));
                priceQuote.setVolume(LabciPropsUtil.inOrderNumberProps("volume", key, data));
                priceQuote.setBoardLotSize(LabciPropsUtil.inOrderNumberProps("boardLotSize", key, data));
                priceQuote.setMarketCap(LabciPropsUtil.inOrderNumberProps("marketCap", key, data));
                //priceQuote.setPeRatio(LabciPropsUtil.inOrderNumberProps("peRatio", key, data));
                priceQuote.setEps(LabciPropsUtil.inOrderNumberProps("eps", key, data));
                priceQuote.setDividend(LabciPropsUtil.inOrderNumberProps("dividend", key, data));
                //priceQuote.setDividendYield(LabciPropsUtil.inOrderNumberProps("dividendYield", key, data));
                priceQuote.setBidAskQueues(LabciPropsUtil.bidAskQueues(key, data));
                priceQuote.setUpperLimitPrice(LabciPropsUtil.inOrderNumberProps("upperTradingLimit", key, data));
                priceQuote.setLowerLimitPrice(LabciPropsUtil.inOrderNumberProps("lowerTradingLimit", key, data));
                priceQuote.setSharesOutstanding(LabciPropsUtil.inOrderNumberProps("sharesOutstanding", key, data));
                priceQuote.setNominalPriceType(LabciPropsUtil.inOrderStrProps("quoteNominalPriceType", key, data));
                priceQuote.setIsSuspended(LabciPropsUtil.inOrderBooPropsForIsSuspended(
                        LabciPropsUtil.inOrderStrProps("quoteNominalPriceType", key, data), header.getCountryCode(),
                        this.labciProperties.getSuspendFlagConfig()));
                priceQuote.setIep(LabciPropsUtil.inOrderNumberProps("iep", key, data));
                priceQuote.setIev(LabciPropsUtil.inOrderNumberProps("iev", key, data));

                //US stock required fields
                priceQuote.setFinancialStatus(LabciPropsUtil.inOrderStrProps("financialStatus", key, data));
                priceQuote.setTradingStatus(LabciPropsUtil.inOrderStrProps("tradingStatus", key, data));
                priceQuote.setMarketStatus(LabciPropsUtil.inOrderStrProps("marketStatus", key, data));

                List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
                List<LastTradeRecord> lastTradeRecords = numbers.stream().map(index -> {
                    LastTradeRecord lastTradeRecord = new LastTradeRecord();
                    lastTradeRecord.setExecutionPrice(LabciPropsUtil.inOrderNumberProps("executionPrice" + index, key, data));
                    lastTradeRecord.setQuantity(LabciPropsUtil.inOrderNumberProps("quantity" + index, key, data));
                    return lastTradeRecord;
                }).collect(Collectors.toList());

                priceQuote.setLastTradeRecords(lastTradeRecords);

                Long lastExecutionTimeAsLong = Long.valueOf(
                        LabciPropsUtil.inOrderStrProps("lastExecutionTime", key, data)
                );

                if(lastExecutionTimeAsLong > 0) {
                    String lastExecutionTimeStr =
                            com.hhhh.group.secwealth.mktdata.api.equity.quotes.util.DateUtil.getDurationBreakdown(lastExecutionTimeAsLong);
                    priceQuote.setAsOfTime(lastExecutionTimeStr);

                    String asOfDateStr = (String)data.get("TRADE_DATE");
                    String lastExecutionDateTimeStr = convertDateTimeInISO8601(asOfDateStr, lastExecutionTimeStr);
                    priceQuote.setAsOfDateTime(lastExecutionDateTimeStr);
                    priceQuote.setLastExecutionTime(lastExecutionDateTimeStr);
                } else {
                    priceQuote.setAsOfDateTime(null);
                    priceQuote.setLastExecutionTime(null);
                }

                priceQuote.setAsOfDate(convertDateFormat((String)data.get("TRADE_DATE")));

                //get stock info
                if((ArgsHolder.getArgs(ACCES_CMND_CDE).toString()).
                        equalsIgnoreCase(ACCES_CMND_CDE_QUOTE_DETAIL)) {
                    String channelId = header.getChannelId();
                    String tCode = predSrchResp.getProdAltNumSegs().stream().
                            filter(prodAltNumSeg -> prodAltNumSeg.getProdCdeAltClassCde().equalsIgnoreCase("T"))
                            .findFirst().get().getProdAltNum();

                    String locale = header.getLocale();

                    StockInfo stockInfo = this.quotesUSLabciPortalService.getStockInfo(channelId, MARKET_CODE, tCode, locale);
                    //StockInfo stockInfo = this.quotesUSLabciPortalService.getStockInfo(channelId, MARKET_CODE, tCode, locale);

                    priceQuote.setIndustry(stockInfo.getIndustry());
                    priceQuote.setExDivDate(stockInfo.getExDivDate());
                    priceQuote.setPeRatio(stockInfo.getPERatio());
                    priceQuote.setExpectedPERatio(stockInfo.getExpectedPERatio());
                    priceQuote.setDividendYield(stockInfo.getDivYield());
                    priceQuote.setPredictDividendYield(stockInfo.getExpectedDividendYield());
                    priceQuote.setIsADR(stockInfo.getIsADR());
                    priceQuote.setIsETF(stockInfo.getIsETF());
                    priceQuote.setHkStockCode(stockInfo.getSymbolHK());
                    priceQuote.setHkStockName(stockInfo.getNameHK());
                    priceQuote.setAdrPrice(stockInfo.getAdrPrice());
                    priceQuote.setAdrCcy(stockInfo.getAdrCcy());
                    priceQuote.setAdrRatio(stockInfo.getAdrRatio());

                    priceQuote.setMarketCap(stockInfo.getMarketCap());
                    priceQuote.setTurnover(stockInfo.getTurnover());

                    if(stockInfo.getIsETF()) {
                        //get past performance info
                        PastPerformance pastPerformance = this.pastPerformanceService.getPastPerformance(channelId, MARKET_CODE, tCode);
                        priceQuote.setPastPerformance(pastPerformance);
                        priceQuote.setRegion(stockInfo.getFundRegion());
                        priceQuote.setBeta6m(stockInfo.getBeta6M());
                        priceQuote.setBeta1y(stockInfo.getBeta1y());
                        priceQuote.setBeta3y(stockInfo.getBeta3y());
                        priceQuote.setBeta5y(stockInfo.getBeta5y());
                        priceQuote.setBeta10y(stockInfo.getBeta10y());
                        priceQuote.setAum(stockInfo.getAmount());
                        priceQuote.setAumCcy(stockInfo.getAmountCcy());
                        priceQuote.setAumDate(stockInfo.getAmountDate());
                        priceQuote.setExpenseRatio(stockInfo.getExpenseRatio());
                        priceQuote.setFundType(stockInfo.getFundType());
                        priceQuote.setInvestmentSector(stockInfo.getFundTypeSubClass());
                    }
                } else {
                    //do nothing for quote list
                }

                //quote counter
                String mappedExchangeName = this.labciProperties.getExchangeMapping(predSrchResp.getExchange());

                mappedExchangeName = mappedExchangeName.equalsIgnoreCase("AMEX")||mappedExchangeName.equalsIgnoreCase("NYARC")?"Others":mappedExchangeName;

                if (labciQuoteWithExchge.get(mappedExchangeName) != null) {
                    labciQuoteWithExchge.get(mappedExchangeName).add(priceQuote);
                } else {
                    List<QuotesLabciQuote> labciQuoteList = new ArrayList<>();
                    labciQuoteList.add(priceQuote);
                    labciQuoteWithExchge.put(mappedExchangeName, labciQuoteList);
                }
            }
        }

        if(!labciQuoteWithExchge.isEmpty()) {
            this.quotesUSLogService.updateQuoteMeterAndLog(labciQuoteWithExchge);
        } else {
            logger.warn("There is an invalid request to LabCI, it will NOT be updated to the quote log.");
        }

        return priceQuotes;
    }

    protected String convertDateFormat(String dateStr) {
        String _dateStr = null;
        try {
            _dateStr = DateUtil.convertDate(dateStr, "dd MMM yyyy", DateUtil.DATE_DAY_PATTERN);
        } catch (ParseException e) {
            logger.error("Failed to convert trade date.", e);
        }

        return _dateStr;
    }

    private boolean afterCleanRun(final Map<String, Object> fields) {
        if (zeroFiled(fields.get("TRDPRC_1")) && zeroFiled(fields.get("NETCHNG_1")) && zeroFiled(fields.get("PCTCHNG"))
                && zeroFiled(fields.get("OPEN_PRC"))) {
            return true;
        }
        return false;
    }

    private boolean zeroFiled(final Object field) {
        if (field != null) {
            if (field instanceof String) {
                if ("0.00".equals(field) || "0".equals(field)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected String convertDateTimeInISO8601(String dateStr, String timeStr) {
        String iso8601Str = dateStr + "T" + timeStr;
        try {
            timeStr = DateUtil.convertToISO8601Format(iso8601Str, DATEFORMAT_DDMMYYYHHMMSS,
                    TimeZone.getTimeZone(Constant.TIMEZONE), TimeZone.getTimeZone(Constant.TIMEZONE));
        } catch (ParseException e) {
            logger.error("Failed to convert last execution time.", e);
        }

        return timeStr;
    }

    protected String companyName(String key, Map<String, Object> data, String local) {
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

    private List<String> getInvalidSymbols(final List<String> symbols, final List<String> responses) {//TODO
        List<String> invalidSymbols = new ArrayList<>();
        for (String symbol : symbols) {
            invalidSymbols.add(symbol);
        }
        invalidSymbols.removeAll(responses);
        logger.debug("Invalid Symbols: " + invalidSymbols);
        return invalidSymbols;
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

    private void addMessage(final Message message) {
        if (ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES) == null) {
            ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES, new ArrayList<Message>());
        }
        @SuppressWarnings("unchecked")
        List<Message> messages = (List<Message>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES);
        messages.add(message);
    }

    protected static BigDecimal inOrderNumberProps4US(String[] labciFields, Map<String, Object> mapObj) {
        BigDecimal result = null;
        for (String labciField : labciFields) {
            result = BigDecimalUtil.fromString((String) mapObj.get(labciField));
            if (isValidNumber(result)) {
                break;
            }
        }
        return result;
    }

    private static boolean isValidNumber(final BigDecimal b) {
        return b != null && b.compareTo(BigDecimal.ZERO) != 0;
    }
}
