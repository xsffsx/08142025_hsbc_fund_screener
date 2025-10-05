/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.reflect.TypeToken;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.request.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.name.id.handler.NameIdHandler;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.ApplicationProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProtalProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.QuoteCounterRepository;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.QuoteCounter;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.QuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.SECQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ServiceProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.service.QuotesServiceRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.Message;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesLabciQuote;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.rbpTradingHour.RetrieveTradeDateInfoResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.rbpTradingHour.RetrieveTradeHourInfoResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.rbpTradingHour.TradeDateInfo;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.util.ConvertorsUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.util.TradingHourUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResponse;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResult;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResultStateEnum;
import com.hhhh.group.secwealth.mktdata.starter.datasource.aspect.annotation.SelectDatasource;
import com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.handler.BMCComponent;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.exception.ExTraceCodeGenerator;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.ExResponseComponent;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.entity.ExResponseEntity;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ParameterException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.apache.http.conn.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;

@Service("quotesUSCommonService")
//@ConditionalOnProperty(value = "service.quoteMS.quotes.common.injectEnabled")//TODO Jerry
public class QuotesUSCommonService {

    private static final Logger logger = LoggerFactory.getLogger(QuotesUSCommonService.class);

    public static final long LARGE_QUOTE_COUNT = 99999999;

    public static final String LABCI_DATA_REPRESENTATION_XML = "XML";
    public static final String LABCI_UNICODE = "UTF-8";

    public static final String THREAD_QUOTE_BMP = "QUOTE_BMP";
    public static final String THREAD_QUOTE_LevelOne = "QUOTE_LevelOne";
    public static final String THREAD_QUOTE_LIST = "QUOTE_LIST";
    public static final String THREAD_QUOTE_DETAIL = "QUOTE_DETAIL";
    public static final String THREAD_INVISIBLE_QUOTES_DELAY = "QUOTES_DELAY";
    public static final String THREAD_INVISIBLE_QUOTES_TRADING_HOUR = "QUOTES_TRADING_HOUR";

    // for distribute cache
    public static final String CUSTOMER_ID = "QUOTE_CUSTOMER_ID";
    public static final String THREAD_CACHE_NODE = "node";

    public static final String USER_TYPE_CDE_CUST = "CUST";
    public static final String USER_TYPE_CDE_STFF = "STFF";
    public static final String USER_TYPE_WD_STFF = "WDST";

    public static String[] EXCLUDE_APPCODE = {"SP"};

    private static final String SAML_STRING = "X-hhhh-Saml";

    private static final String SAML3_STRING = "X-hhhh-Saml3";

    private static final String JWT_STRING = "X-hhhh-E2E-TRUST-TOKEN";

    private static final String TRADE_DATE = "tradeDay";

/*    @Autowired
    private PredSrchService predSrchService;

    @Autowired
    private ExResponseComponent exRespComponent;

    @Autowired
    private BMCComponent bmcComponent;*/

    @Autowired
    private HttpClientHelper httpClientHelper;

    @Autowired
    private NameIdHandler nameIdHandler;

/*    @Autowired
    private ApplicationProperties props;*/

    @Autowired
    private LabciProtalProperties labciProtalProperties; //TODO Jerry adjust the config

    /*@Autowired
    private QuoteCounterRepository quoteCounterRepository;*/

    public void validate(final SECQuotesRequest request, final CommonRequestHeader header) throws IllegalAccessException,
        IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        String requestType = request.getRequestType();
        if (StringUtil.isInValid(requestType)) {
            requestType = Constant.CASE_0;
        }
        // invalid request type
        if (!Constant.CASE_0.equals(requestType) && !Constant.CASE_10.equals(requestType)
            && !Constant.CASE_20.equals(requestType)) {
            logger.error("request Parameter error, requestType: " + requestType);
            throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
        }

/*        if (this.props.getHeaderWithParam().size() > 0) {
            Map<String, Map<String, String>> headerWithParam = this.props.getHeaderWithParam();
            for (String headerName : headerWithParam.keySet()) {
                if (!headerName.equals(header.getAppCode())) {
                    continue;
                }
                Map<String, String> paramMap = headerWithParam.get(headerName);
                for (String method : paramMap.keySet()) {
                    Method declaredMethod = null;
                    try {
                        declaredMethod = request.getClass().getDeclaredMethod(method);
                    } catch (Exception e) {
                        declaredMethod = request.getClass().getSuperclass().getDeclaredMethod(method);
                    }
                    declaredMethod.setAccessible(true);
                    if (!paramMap.get(method).equals(declaredMethod.invoke(request).toString())) {
                        logger.error("request header " + headerName + " has wrong param with " + method);
                        // TODO: create new exception and throw exception
                        throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
                    }
                }
            }
        }*/

        // invalid request product key list
        List<ProductKey> productKeyList = request.getProductKeys();
        if (ListUtil.isInvalid(productKeyList)) {
            logger.error("request Parameter error, productKeys is Invalid");
            throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
        }


        // invalid request QuoteDetail, QuoteDetail only get one symbol and
        // return
        if (Constant.CASE_20.equals(requestType) && productKeyList.size() > 1) {
            logger.error("QuoteDetail only support one prodaltnum");
            throw new ParameterException(ExCodeConstant.EX_CODE_QUOTE_DETAIL_ONLY_SUPPORT_ONE_PRODALTNUM);
        }

        logger.info("request product number is " + productKeyList.size());
    }

    public void addMessage(final List<ProductKey> productKeyList, final ExResponseEntity exResponse) {
        for (ProductKey productKey : productKeyList) {
            this.addMessage(productKey, exResponse);
        }
    }

    public void addMessage(final ProductKey productKey, final ExResponseEntity exResponse) {
        Message message = new Message();
        message.setReasonCode(exResponse.getReasonCode());
        message.setText(exResponse.getText());
        String traceCode = ExTraceCodeGenerator.generate();
        message.setTraceCode(traceCode);
        message.setProductType(productKey.getProductType());
        message.setProdCdeAltClassCde(productKey.getProdCdeAltClassCde());
        message.setProdAltNum(productKey.getProdAltNum());
        addMessage(message);
    }

    public void addMessage(final String symbol, final ExResponseEntity exResponse, final SECQuotesRequest request) {
        Message message = new Message();
        message.setReasonCode(exResponse.getReasonCode());
        message.setText(exResponse.getText());
        String traceCode = ExTraceCodeGenerator.generate();
        message.setTraceCode(traceCode);
        message.setProductType("SEC");
        message.setProdCdeAltClassCde(Constant.PROD_CDE_ALT_CLASS_CODE_M);
        for (ProductKey productKey : request.getProductKeys()) {
            if (productKey.getProdAltNum().equals(symbol)) {
                message.setProductType(productKey.getProductType());
                message.setProdCdeAltClassCde(productKey.getProdCdeAltClassCde());
                break;
            }
        }
        message.setProdAltNum(symbol);
        addMessage(message);
    }

    public void addMessage(final Message message) {
        if (ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES) == null) {
            ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES, new ArrayList<Message>());
        }
        @SuppressWarnings("unchecked")
        List<Message> messages = (List<Message>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES);
        messages.add(message);
    }

    /*@Cacheable(cacheNames = "productSymbol", keyGenerator = "customerKeyGen", unless = "#result == null")
    public ProdInBndAmhUSPo addOrGetProdInBndFromCache(final ProdInBndAmhUSPo po, final String symbol) {
        return po;
    }*/

    @SuppressWarnings("unchecked")
    public List<Message> getMessages() {
        if (ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES) == null) {
            return null;
        } else {
            return (List<Message>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES);
        }
    }

    public void validateCacheDistributeResult(final CacheDistributeResult result) {
        CacheDistributeResultStateEnum resultState = result.getResultState();
        if (resultState == CacheDistributeResultStateEnum.OK) {
            CacheDistributeResponse response = result.getResponse();
            String value = response.getValue();
            // Get the value you are interested in
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode node = mapper.readTree(value);
                String customerId = node.get("permID").asText();
                if (StringUtil.isInValid(customerId)) {
                    logger.error("No permID found from rbp cache");
                    throw new VendorException("CacheNoPermIDFound"); //TODO Jerry
                }
                ArgsHolder.putArgs(QuotesUSCommonService.CUSTOMER_ID, customerId);
                ArgsHolder.putArgs(QuotesUSCommonService.THREAD_CACHE_NODE, node);
            } catch (Exception e) {
                QuotesUSCommonService.logger.error("Cache Distribute Bad Request");
                throw new VendorException(ExCodeConstant.EX_CODE_CACHE_BAD_REQUEST);
            }
        } else {
            if (resultState == CacheDistributeResultStateEnum.INVALID_PARAMETERS) {
                QuotesUSCommonService.logger.error("Cache Distribute Bad Request");
                throw new VendorException(ExCodeConstant.EX_CODE_CACHE_BAD_REQUEST);
            }
            if (resultState == CacheDistributeResultStateEnum.UNCACHED_RECORD) {
                QuotesUSCommonService.logger.error("Cache Distribute don't contains the key you sent");
                throw new VendorException(ExCodeConstant.EX_CODE_CACHE_UNCACHED_RECORD);
            }
            if (resultState == CacheDistributeResultStateEnum.INVALID_HTTP_STATUS) {
                QuotesUSCommonService.logger.error("Get response from the Cache Distribute encounter error");
                throw new VendorException(ExCodeConstant.EX_CODE_CACHE_INVALID_HTTP_STATUS);
            }
        }
    }


    /*@SuppressWarnings("unchecked")
    public QuotesServiceRequest buildServiceRequest(final SECQuotesRequest request, final CommonRequestHeader header,
                                                    final String prodClsCde) {
        QuotesServiceRequest quoteServiceRequest = new QuotesServiceRequest();
        String requestType = QuotesUSCommonService.THREAD_QUOTE_BMP; //TODO Jerry case 0?
        if(Constant.CASE_10.equals(request.getRequestType())){
            requestType = QuotesUSCommonService.THREAD_QUOTE_LIST;
        }else if(Constant.CASE_20.equals(request.getRequestType())) {
            requestType = QuotesUSCommonService.THREAD_QUOTE_DETAIL;
        }
        ArgsHolder.putArgs(Constant.THREAD_QUOTE_TYPE, requestType);

        quoteServiceRequest.setDelay(isDelay(request));
        List<String> symbols = new ArrayList<>();
        Map<String, List<String>> symbolsMap = new LinkedHashMap<>();
        List<ProductKey> productKeyList = request.getProductKeys();
        for (ProductKey productKey : productKeyList) {
            symbols.add(productKey.getProdAltNum());
            if (symbolsMap.containsKey(productKey.getProductType())) {
                symbolsMap.get(productKey.getProductType()).add(productKey.getProdAltNum());//
            } else {
                List<String> symbol = new ArrayList<>();
                symbol.add(productKey.getProdAltNum());
                symbolsMap.put(productKey.getProductType(), symbol);
            }
        }
        List<PredSrchResponse> responses = new ArrayList<>();
        // call predsrch,times depend on productType number
        for (Map.Entry<String, List<String>> symbolMap : symbolsMap.entrySet()) {
            PredSrchRequest predSrchRequest = new PredSrchRequest();
            String[] assetClasses = new String[1];
            assetClasses[0] = symbolMap.getKey().toString();//
            predSrchRequest.setAssetClasses(assetClasses);
            predSrchRequest.setKeyword(symbolMap.getValue().toArray(new String[symbolMap.getValue().size()]));
            predSrchRequest.setTopNum(String.valueOf(symbolMap.getValue().size()));
            try {
                List<PredSrchResponse> responseList = this.predSrchService.precSrch(predSrchRequest, header);
                responses.addAll(responseList);
            } catch (Exception e) {
                // will be exclude empty response,so many unnecessary mistake
                if (!e.getMessage().equals(ExCodeConstant.EX_CODE_PREDSRCH_EMPTY_RESPONSE)) {
                    try {
                        this.bmcComponent.doBMC(new VendorException(ExCodeConstant.EX_CODE_ACCESS_PREDSRCH_ERROR),
                            ExTraceCodeGenerator.generate());
                    } catch (IOException exception) {
                        logger.error("Write BMC encounter error", exception);
                    }
                    logger.error("precSearch error is :" + e.toString());
                }
            }
        }

        Map<String, Object> predsrchSymbol =
            ConvertorsUtil.getPredsrchSymbol(request.getProductKeys(), symbolsMap, responses, prodClsCde);
        // if response from predsrch is different from request, will remove the
        // missing req
        if (responses != null && responses.size() != symbols.size()) {
            ExResponseEntity exResponse = this.exRespComponent.getExResponse(ExCodeConstant.EX_CODE_PREDSRCH_INVALID_RESPONSE);
            List<ProductKey> missingProdKeys = (List<ProductKey>) predsrchSymbol.get(Constant.MISSING_PROD_KEYS);
            addMessage(missingProdKeys, exResponse);
        }
        quoteServiceRequest.setServiceProductKeys((List<ServiceProductKey>) predsrchSymbol.get(Constant.SERVICE_PROD_KEYS));
        ArgsHolder.putArgs(Constant.SERVICE_PROD_KEYS, predsrchSymbol.get(Constant.SERVICE_PROD_KEYS));
        return quoteServiceRequest;
    }*/

    public boolean isDelay(final QuotesRequest request) {
        if (request.getDelay() != null) {
            return request.getDelay();
        }
        return true;
    }

    public String callLabci(final String url, final String labciRequest) throws Exception {
        String labciResponse = "";
        try {
            labciResponse = this.httpClientHelper.doGet(url, labciRequest, null);
        } catch (Exception e) {
            if (e instanceof IOException) {
                logger.error("Labci Undefined error url: " + url, e);
                throw new VendorException(ExCodeConstant.EX_CODE_LABCI_UNDEFINED_ERROR, e);
            } else if (e instanceof ConnectTimeoutException) {
                logger.error("Access Labci error url: " + url, e);
                throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_LABCI_ERROR, e);
            } else {
                logger.error("Labci server error url: " + url, e);
                throw new VendorException(ExCodeConstant.EX_CODE_LABCI_SERVER_ERROR, e);
            }
        }
        return labciResponse;
    }

    public boolean checkTradingDate(final CommonRequestHeader header) throws Exception {
        String rbpTradingDate = "";
        Map<String, String> saml = new HashMap<>();
        String current =
            TradingHourUtil.current(TradingHourUtil.DATE_DAY_PATTERN, TimeZone.getTimeZone(TradingHourUtil.US_TIMEZONE));

        String params = new StringBuilder().append(this.labciProtalProperties.getTradingParamPrefix())
            .append(URLEncoder.encode(this.labciProtalProperties.getTradingParam(), "UTF-8")).toString();
        if (StringUtil.isValid(header.getSaml3String())) {
            saml.put(QuotesUSCommonService.SAML3_STRING, header.getSaml3String());
        } else if(StringUtil.isValid(header.getJwtString())){
            saml.put(QuotesUSCommonService.JWT_STRING, header.getJwtString());
        } else {
            saml.put(QuotesUSCommonService.SAML_STRING, header.getSamlString());
        }
        String url = this.labciProtalProperties.getTradingDayUrl().replace(QuotesUSCommonService.TRADE_DATE, current);
        try {
            rbpTradingDate = this.httpClientHelper.doGet(url, params, saml);
        } catch (Exception e) {
            QuotesUSCommonService.logger.error("access rbp TradingDate api failed, url is " + url);
            throw new VendorException(ExCodeConstant.EX_CODE_RBP_TRADE_HOUR_ERROR, e);
        }

        RetrieveTradeDateInfoResponse response =
            JsonUtil.fromJson(rbpTradingDate, new TypeToken<RetrieveTradeDateInfoResponse>() {}.getType());

        boolean isContainToday = false;
        if (response != null) {
            for (TradeDateInfo tradeDateInfo : response.getTradeDateList()) {
                // only compare day , ignore HH:mm:ss
                // TimeZone.getTimeZone("GMT+8")
                if (tradeDateInfo.getOrderTradeDate().compareTo(current) == 0) {
                    isContainToday = true;
                    break;
                }
            }
        }
        return isContainToday;
    }

    public RetrieveTradeHourInfoResponse checkTradingHour(final CommonRequestHeader header) throws Exception {
        String rbpTradingDate = "";
        Map<String, String> saml = new HashMap<>();
        if (StringUtil.isValid(header.getSaml3String())) {
            saml.put(QuotesUSCommonService.SAML3_STRING, header.getSaml3String());
        } else if(StringUtil.isValid(header.getJwtString())){
            saml.put(QuotesUSCommonService.JWT_STRING, header.getJwtString());
        } else {
            saml.put(QuotesUSCommonService.SAML_STRING, header.getSamlString());
        }
        String params = new StringBuilder().append(this.labciProtalProperties.getTradingParamPrefix())
            .append(URLEncoder.encode(this.labciProtalProperties.getTradingParam(), "UTF-8")).toString();
        String url = this.labciProtalProperties.getTradingHourUrl().replace(QuotesUSCommonService.TRADE_DATE,
            TradingHourUtil.current(TradingHourUtil.DATE_DAY_PATTERN, TimeZone.getTimeZone(TradingHourUtil.US_TIMEZONE)));
        try {
            rbpTradingDate = this.httpClientHelper.doGet(url, params, saml);
        } catch (Exception e) {
            QuotesUSCommonService.logger.error("access rbp TradingHour api failed, url is " + url);
            throw new VendorException(ExCodeConstant.EX_CODE_RBP_TRADE_HOUR_ERROR, e);
        }

        return JsonUtil.fromJson(rbpTradingDate, new TypeToken<RetrieveTradeHourInfoResponse>() {}.getType());
    }

    public String getUserTypeByCN() {
        CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
        String userType = QuotesUSCommonService.USER_TYPE_CDE_CUST;
        String channelId = header.getChannelId();
        if (this.nameIdHandler.getCustomerChannel().contains(channelId)) {
            userType = QuotesUSCommonService.USER_TYPE_CDE_CUST;
        } else if (this.nameIdHandler.getStaffChannel().contains(channelId)) {
            userType = QuotesUSCommonService.USER_TYPE_CDE_STFF;
        } else {
            QuotesUSCommonService.logger.warn("undefined channelId: " + channelId);
        }
        return userType;
    }

    public String getUserTypeByUS() {
        CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
        String userType = QuotesUSCommonService.USER_TYPE_CDE_CUST;
        String channelId = header.getChannelId();
        if (this.nameIdHandler.getCustomerChannel().contains(channelId)) {
            userType = QuotesUSCommonService.USER_TYPE_CDE_CUST;
        } else if (this.nameIdHandler.getStaffChannel().contains(channelId)) {
            //for WD/SP staff channel, will set playerType different to avoid staff records occur in monthly report.
            if(header.getAppCode().equals("WD") || header.getAppCode().equals("SP") ){
                userType= QuotesUSCommonService.USER_TYPE_WD_STFF;
            }else{
                userType = QuotesUSCommonService.USER_TYPE_CDE_STFF;
            }
        } else {
            QuotesUSCommonService.logger.warn("undefined channelId: " + channelId);
        }
        return userType;
    }

    // should return something?like quoteNum is not enough.
    /*@Async("quoteLogExecutor")
    @SelectDatasource
    public void addStockQuoteCounter(final String datasource, final Long customerId, final Set<String> exchangeSet,
                                     final String market, final Map<String, List<QuotesLabciQuote>> labciQuoteWithExchge, final String quoteType) {
        QuoteCounter quoteCounter = new QuoteCounter();
        // TODO: id not confirmed
        quoteCounter.setPlayerReferenceNumber(customerId);
        quoteCounter.setTradingMarketCode(market);
        quoteCounter.setQuoteType(quoteType);
        Calendar instance = Calendar.getInstance();
        instance.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        quoteCounter.setQuoteYear(DateUtil.getCurrentYear(instance));
        quoteCounter.setQuoteMonth(DateUtil.getCurrentMonth(instance));
        List<QuoteCounter> quoteCounterList = this.quoteCounterRepository
            .findAll(Example.of(quoteCounter, ExampleMatcher.matching().withIgnorePaths("quoteCounter")));
        Set<String> unsaveType = new HashSet<String>();
        long totalQuoteCounter = 0;
        Map<String, Long> detailQuoteCounterMap = new HashMap<String, Long>();
        Timestamp timestamp = new Timestamp(DateUtil.getMachineDateTime().getTime());
        unsaveType.addAll(exchangeSet);
        for (QuoteCounter qCounter : quoteCounterList) {
            if (exchangeSet.contains(qCounter.getSubQuoteType())) {
                unsaveType.remove(qCounter.getSubQuoteType());
            }
        }
        if (unsaveType.size() > 0) {
            for (String subQuoteType : unsaveType) {
                QuoteCounter unsaveQuoteCounter = new QuoteCounter();
                Integer newquoteUsageCounter = labciQuoteWithExchge.get(subQuoteType).size();
                unsaveQuoteCounter.setPlayerReferenceNumber(customerId);
                unsaveQuoteCounter.setTradingMarketCode(market);
                unsaveQuoteCounter.setQuoteType(quoteType);
                unsaveQuoteCounter.setQuoteYear(DateUtil.getCurrentYear(instance));
                unsaveQuoteCounter.setQuoteMonth(DateUtil.getCurrentMonth(instance));
                unsaveQuoteCounter.setSubQuoteType(subQuoteType);
                unsaveQuoteCounter.setQuoteCounter(newquoteUsageCounter);
                unsaveQuoteCounter.setLastUpdate(timestamp);// is it ok?
                this.quoteCounterRepository.save(unsaveQuoteCounter);
                detailQuoteCounterMap.put(subQuoteType, newquoteUsageCounter.longValue());
                totalQuoteCounter = totalQuoteCounter + newquoteUsageCounter;
            }
        }
        long sumQuoteUsage = 0;
        long sumQuoteCounter = 0;
        for (Map.Entry<String, List<QuotesLabciQuote>> quoteUsage : labciQuoteWithExchge.entrySet()) {
            sumQuoteUsage = sumQuoteUsage + quoteUsage.getValue().size();
        }

        for (QuoteCounter aQuoteCounter : quoteCounterList) {
            sumQuoteCounter = sumQuoteCounter + aQuoteCounter.getQuoteCounter();
        }

        if (sumQuoteUsage + sumQuoteCounter <= QuotesUSCommonService.LARGE_QUOTE_COUNT) {
            for (QuoteCounter aQuoteCounter : quoteCounterList) {
                if (labciQuoteWithExchge.get(aQuoteCounter.getSubQuoteType()) != null) {
                    long theQuoteCounter =
                        aQuoteCounter.getQuoteCounter() + labciQuoteWithExchge.get(aQuoteCounter.getSubQuoteType()).size();
                    aQuoteCounter.setQuoteCounter(theQuoteCounter);
                    aQuoteCounter.setLastUpdate(timestamp);
                    int returnCode = this.quoteCounterRepository.addQuoteAndUpdate(
                        labciQuoteWithExchge.get(aQuoteCounter.getSubQuoteType()).size(), timestamp,
                        aQuoteCounter.getPlayerReferenceNumber(), aQuoteCounter.getTradingMarketCode(),
                        aQuoteCounter.getQuoteYear(), aQuoteCounter.getQuoteMonth(), aQuoteCounter.getQuoteType(),
                        aQuoteCounter.getSubQuoteType());
                    if (returnCode > 0) {
                        detailQuoteCounterMap.put(aQuoteCounter.getSubQuoteType(), aQuoteCounter.getQuoteCounter());
                        totalQuoteCounter = totalQuoteCounter + aQuoteCounter.getQuoteCounter();
                    }
                }
            }
        } else {
            // quote count exceed error
            // TODO QuotesUSCommonService.logger.error(QuotesLogAmhCNService.QUOTE_COUNT_EXCEED_ERROR);
            for (QuoteCounter aQuoteCounter : quoteCounterList) {
                detailQuoteCounterMap.put(aQuoteCounter.getSubQuoteType(), aQuoteCounter.getQuoteCounter());
                totalQuoteCounter = totalQuoteCounter + aQuoteCounter.getQuoteCounter();
            }
        }
    }*/

}
