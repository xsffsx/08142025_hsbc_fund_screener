package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

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
import org.thymeleaf.util.ListUtils;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci.LabciResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci.Ric;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci.Watchlist;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.ApplicationProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.CachingServerProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.BigDecimalUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.LabciPropsUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.LabciServletBoConvertor;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.ProdInbndRepository;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.ProdInbnd;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.SECQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ServiceProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.service.QuotesServiceRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.Message;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesLabciQuote;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesLabciResponse;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResult;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.utils.CacheDistributeHolder;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.exception.ExTraceCodeGenerator;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.ExResponseComponent;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.entity.ExResponseEntity;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ParameterException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

@Service("quotesHKCachingServerService")
@ConditionalOnProperty(value = "service.quotes.hkEquity.injectEnabled", havingValue = "true")
public class QuotesHKCachingServerService extends AbstractBaseService<SECQuotesRequest, QuotesLabciResponse, CommonRequestHeader> {

    private static final Logger logger = LoggerFactory.getLogger(QuotesHKCachingServerService.class);

    private static final String QLST_FUNCTION_NAME = "QLST";

    private static final String PROF_FUNCTION_NAME = "PROF";

    private static final String TRADING_MARKET_CODE = "US";

    private static final String THREAD_INVISIBLE_QUOTES_CACHINGSERVER_REQUEST = "QUOTES_CACHINGSERVER_REQUEST";

    private static final String THREAD_INVISIBLE_QUOTES_FUNCTION_NAME = "FUNCTION_NAME";

    private static final String THREAD_INVISIBLE_QUOTES_DELAY = "QUOTES_DELAY";

    private static final String THREAD_INVISIBLE_QUOTES_TRADING_HOUR = "QUOTES_TRADING_HOUR";

    private static final String THREAD_INVISIBLE_QUOTES_SEARCH_FROM_DB = "QUOTES_SEARCH_FROM_DB";

    private static final String DATAREPRESENTATION_XML = "XML";

    private static final String UNICODE = "UTF-8";

    private static final String TIMEZONE_EST = "EST";

    private static final String DATETIME_SEPARATOR = "T";

    private static final String SATFF_CHANNEL_ID = "OHB";

    private static final int STATUS_OK = 0;

    // private static final String STATUS_HALTED = "halted";

    @Autowired
    private ExResponseComponent exRespComponent;

    @Autowired
    private ApplicationProperties appProps;

    @Autowired
    private CachingServerProperties cachingServerProperties;

    @Autowired
    private HttpClientHelper httpClientHelper;

    @Autowired
    private QuotesHKCommonService quotesHKCommonService;

    @Autowired()
    @Qualifier("prodInbndRepository")
    private ProdInbndRepository prodInbndRepository;

    @Override
    protected Object convertRequest(final SECQuotesRequest request, final CommonRequestHeader header) throws Exception {
        QuotesHKCachingServerService.logger.info("Call Quotes HK Caching Server");

        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER, header);
        ArgsHolder.putArgs(QuotesHKCachingServerService.THREAD_INVISIBLE_QUOTES_CACHINGSERVER_REQUEST, request);

        List<String> symbols = this.validate(request, header);
        if (!QuotesHKCachingServerService.SATFF_CHANNEL_ID.equalsIgnoreCase(header.getChannelId())) {
            CacheDistributeResult result = CacheDistributeHolder.getCacheDistribute();
            this.quotesHKCommonService.validateCacheDistributeResult(result);
        }

        String lang = header.getLocale();
        boolean isTradingHour = false;// TODO
        boolean isDelay = request.getDelay();

        String functionName = QuotesHKCachingServerService.QLST_FUNCTION_NAME;
        if (Constant.CASE_20.equals(request.getRequestType())) {
            functionName = QuotesHKCachingServerService.PROF_FUNCTION_NAME;
        }
        QuotesServiceRequest quotesServiceRequest = buildServiceRequest(request, header, symbols);

        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_PRODUCT_KEY, symbols);
        ArgsHolder.putArgs(QuotesHKCachingServerService.THREAD_INVISIBLE_QUOTES_FUNCTION_NAME, functionName);
        ArgsHolder.putArgs(QuotesHKCachingServerService.THREAD_INVISIBLE_QUOTES_DELAY, isDelay);
        ArgsHolder.putArgs(QuotesHKCachingServerService.THREAD_INVISIBLE_QUOTES_TRADING_HOUR, isTradingHour);

        Map<String, String> serviceRequestMapper = new LinkedHashMap<String, String>();
        String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
        String service = this.cachingServerProperties.getLabciService(site, this.isDelayedQuotes(isDelay, isTradingHour));

        Map<String, List<ServiceProductKey>> groupedProductKeysMapper = getGroupedProductKeysMapper(site, quotesServiceRequest);
        for (Map.Entry<String, List<ServiceProductKey>> groupedProductKeys : groupedProductKeysMapper.entrySet()) {
            String key = groupedProductKeys.getKey();
            String[] keys = key.split(SymbolConstant.SYMBOL_VERTICAL_LINE_ESCAPE);
            List<ServiceProductKey> productKeys = groupedProductKeys.getValue();
            List<String> items = new ArrayList<>();
            for (ServiceProductKey productKey : productKeys) {
                items.add(productKey.getProdAltNum());
            }
            String symbolList = LabciServletBoConvertor.genSymbols(items, service);
            List<NameValuePair> labciParams = new ArrayList<NameValuePair>();
            labciParams.add(new BasicNameValuePair("SymbolList", symbolList));
            labciParams.add(new BasicNameValuePair("FieldList",
                StringUtils.join(
                    this.cachingServerProperties.getLabciFields(this.appProps.getQuotesResponseCachingServerFields(keys), keys),
                    SymbolConstant.SYMBOL_SEMISOLON)));
            labciParams.add(new BasicNameValuePair("DataRepresentation", QuotesHKCachingServerService.DATAREPRESENTATION_XML));
            String labciReqParams = URLEncodedUtils.format(labciParams, QuotesHKCachingServerService.UNICODE);

            serviceRequestMapper.put(key, labciReqParams);
        }
        return serviceRequestMapper;
    }

    private List<String> validate(final SECQuotesRequest request, final CommonRequestHeader header) {
        // Validator request
        String requestType = Constant.CASE_0;
        if (StringUtil.isValid(request.getRequestType())) {
            requestType = request.getRequestType();
        }
        if (!Constant.CASE_0.equals(requestType) && !Constant.CASE_10.equals(requestType)
            && !Constant.CASE_20.equals(requestType)) {
            QuotesHKCachingServerService.logger.error("request Parameter error, requestType: " + requestType);
            throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
        }

        List<ProductKey> productKeyList = request.getProductKeys();
        if (ListUtil.isInvalid(productKeyList)) {
            QuotesHKCachingServerService.logger.error("request Parameter error, productKeys is Invalid");
            throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
        }

        // Validator request
        List<String> symbols = new ArrayList<String>();
        List<ProductKey> invalidSymbols = new ArrayList<ProductKey>();
        if (Constant.CASE_20.equals(requestType)) {
            for (ProductKey productKey : productKeyList) {
                if (Constant.PROD_CDE_ALT_CLASS_CODE_M.equals(productKey.getProdCdeAltClassCde())) {
                    if (symbols.size() == 0) {
                        symbols.add(productKey.getProdAltNum());
                    } else {
                        invalidSymbols.add(productKey);
                    }

                } else {
                    invalidSymbols.add(productKey);
                }
                if (invalidSymbols.size() > 0) {
                    ExResponseEntity exResponse =
                        this.exRespComponent.getExResponse(ExCodeConstant.EX_CODE_QUOTE_DETAIL_ONLY_SUPPORT_ONE_PRODALTNUM);
                    addMessage(invalidSymbols, exResponse);
                }
            }
        } else {
            for (ProductKey productKey : productKeyList) {
                if (Constant.PROD_CDE_ALT_CLASS_CODE_M.equals(productKey.getProdCdeAltClassCde())) {
                    symbols.add(productKey.getProdAltNum());
                } else {
                    invalidSymbols.add(productKey);
                }
            }
            if (invalidSymbols.size() > 0) {
                ExResponseEntity exResponse =
                    this.exRespComponent.getExResponse(ExCodeConstant.EX_CODE_QUOTES_PRODCDEALTCLASSCDE_ONLY_SUPPORT_M_CODE);
                addMessage(invalidSymbols, exResponse);
            }
        }

        return symbols;
    }

    private QuotesServiceRequest buildServiceRequest(final SECQuotesRequest request, final CommonRequestHeader header,
        final List<String> symbols) {
        // Build request
        QuotesServiceRequest quoteServiceRequest = new QuotesServiceRequest();
        quoteServiceRequest.setDelay(request.getDelay() == null ? true : request.getDelay());
        List<ProductKey> productKeys = request.getProductKeys();

        List<ProdInbnd> prodInbnds =
            this.prodInbndRepository.getProdInbndList(symbols, QuotesHKCachingServerService.TRADING_MARKET_CODE);
        ArgsHolder.putArgs(QuotesHKCachingServerService.THREAD_INVISIBLE_QUOTES_SEARCH_FROM_DB, prodInbnds);
        if (prodInbnds != null && prodInbnds.size() != symbols.size()) {
            ExResponseEntity exResponse = this.exRespComponent.getExResponse(ExCodeConstant.EX_CODE_DB_NO_RECORD);
            List<String> validates = new ArrayList<String>();
            for (ProdInbnd prod : prodInbnds) {
                validates.add(prod.getExternalNumber());
            }
            addMessage(symbols, validates, exResponse);
        }

        List<ServiceProductKey> serviceProductKeys = new ArrayList<ServiceProductKey>();
        for (ProdInbnd prod : prodInbnds) {
            ServiceProductKey serviceProductKey = new ServiceProductKey();
            serviceProductKey.setExchange(prod.getExchangeCode());
            serviceProductKey.setMarket(QuotesHKCachingServerService.TRADING_MARKET_CODE);
            serviceProductKey.setProdAltNum(prod.getExternalCode());
            serviceProductKey.setProdCdeAltClassCde(Constant.PROD_CDE_ALT_CLASS_CODE_R);
            for (ProductKey key : productKeys) {
                if (prod.getExternalNumber().equalsIgnoreCase(key.getProdAltNum())) {
                    serviceProductKey.setProductType(key.getProductType());
                    break;
                }
            }
            serviceProductKeys.add(serviceProductKey);
        }
        quoteServiceRequest.setServiceProductKeys(serviceProductKeys);
        return quoteServiceRequest;
    }

    private void addMessage(final List<ProductKey> invalidSymbols, final ExResponseEntity exResponse) {
        for (ProductKey productKey : invalidSymbols) {
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
    }

    private void addMessage(final List<String> symbols, final List<String> validates, final ExResponseEntity exResponse) {
        SECQuotesRequest request =
            (SECQuotesRequest) ArgsHolder.getArgs(QuotesHKCachingServerService.THREAD_INVISIBLE_QUOTES_CACHINGSERVER_REQUEST);
        List<String> invalidSymbols = getInvalidSymbols(symbols, validates);
        for (String invalidSymbol : invalidSymbols) {
            addMessage(invalidSymbol, exResponse, request);
        }
    }

    private List<String> getInvalidSymbols(final List<String> symbols, final List<String> validates) {
        List<String> invalidSymbols = new ArrayList<String>();
        for (String symbol : symbols) {
            invalidSymbols.add(symbol);
        }
        invalidSymbols.removeAll(validates);
        QuotesHKCachingServerService.logger.error("Invalid Symbols: " + invalidSymbols);
        return invalidSymbols;
    }

    private void addMessage(final String invalidSymbol, final ExResponseEntity exResponse, final SECQuotesRequest request) {
        Message message = new Message();
        message.setReasonCode(exResponse.getReasonCode());
        message.setText(exResponse.getText());
        String traceCode = ExTraceCodeGenerator.generate();
        message.setTraceCode(traceCode);
        message.setProductType("SEC");
        message.setProdCdeAltClassCde(Constant.PROD_CDE_ALT_CLASS_CODE_M);
        for (ProductKey productKey : request.getProductKeys()) {
            if (productKey.getProdAltNum().equals(invalidSymbol)) {
                message.setProductType(productKey.getProductType());
                message.setProdCdeAltClassCde(productKey.getProdCdeAltClassCde());
                break;
            }
        }
        message.setProdAltNum(invalidSymbol);
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

    @SuppressWarnings("unchecked")
    private List<Message> getMessages() {
        if (ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES) == null) {
            return null;
        } else {
            return (List<Message>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES);
        }
    }

    private Map<String, List<ServiceProductKey>> getGroupedProductKeysMapper(final String site,
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
    @Override
    protected Object execute(final Object serviceRequest) throws Exception {
        Map<String, String> serviceResponseMapper = new LinkedHashMap<String, String>();
        Map<String, String> serviceRequestMapper = (Map<String, String>) serviceRequest;

        for (Map.Entry<String, String> requestMapper : serviceRequestMapper.entrySet()) {
            String key = requestMapper.getKey();
            String request = requestMapper.getValue();
            serviceResponseMapper.put(key, CallLabci(request));
        }
        return serviceResponseMapper;
    }

    private String CallLabci(final String labciRequest) throws Exception {
        String labciResponse = "";
        try {
            labciResponse = this.httpClientHelper.doGet(this.cachingServerProperties.getLabciUrl(), labciRequest, null);
        } catch (Exception e) {
            if (e instanceof IOException) {
                QuotesHKCachingServerService.logger.error("Labci Undefined error", e);
                throw new VendorException(ExCodeConstant.EX_CODE_LABCI_UNDEFINED_ERROR, e);
            } else if (e instanceof ConnectTimeoutException) {
                QuotesHKCachingServerService.logger.error("Access Labci error", e);
                throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_LABCI_ERROR, e);
            } else {
                QuotesHKCachingServerService.logger.error("Labci server error", e);
                throw new VendorException(ExCodeConstant.EX_CODE_LABCI_SERVER_ERROR, e);
            }
        }
        return labciResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object validateServiceResponse(final Object serviceResponse) throws Exception {
        Map<String, String> serviceResponseMapper = (Map<String, String>) serviceResponse;
        Object validServiceResponseMapper = labciResponse(serviceResponseMapper);
        return validServiceResponseMapper;
    }

    @SuppressWarnings("unchecked")
    private Object labciResponse(final Map<String, String> serviceResponseMapper) throws Exception {
        Map<String, Map<String, LabciResponse>> validServiceResponseMapper =
            new LinkedHashMap<String, Map<String, LabciResponse>>();

        List<String> validates = new ArrayList<String>();
        for (Map.Entry<String, String> responseMapper : serviceResponseMapper.entrySet()) {
            String key = responseMapper.getKey();
            Map<String, LabciResponse> map = eachLabciResponse(key, responseMapper.getValue());
            if (!map.isEmpty()) {
                List<ProdInbnd> prodInbnds =
                    (List<ProdInbnd>) ArgsHolder.getArgs(QuotesHKCachingServerService.THREAD_INVISIBLE_QUOTES_SEARCH_FROM_DB);
                validServiceResponseMapper.put(key, map);
                for (String string : map.keySet()) {
                    ProdInbnd prod = this.getProdInbndByExternalCode(prodInbnds, string);
                    validates.add(prod.getExternalNumber());
                }
            }
        }

        List<String> symbols = (List<String>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_PRODUCT_KEY);
        if (validates != null && validates.size() != symbols.size()) {
            ExResponseEntity exResponse = this.exRespComponent.getExResponse(ExCodeConstant.EX_CODE_LABCI_STOCK_NOT_FOUND);
            addMessage(symbols, validates, exResponse);
        }
        return validServiceResponseMapper;
    }

    private Map<String, LabciResponse> eachLabciResponse(final String key, final String labciResponses) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Watchlist.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Watchlist res = (Watchlist) unmarshaller.unmarshal(new StringReader(labciResponses));
        Map<String, LabciResponse> response = new LinkedHashMap<String, LabciResponse>();
        if (null != res) {
            List<Ric> ricList = res.getRic();
            if (!ListUtils.isEmpty(ricList)) {
                response = LabciServletBoConvertor.getResponseMap(ricList);
            }
        }
        return response;
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

    @SuppressWarnings("unchecked")
    private List<QuotesLabciQuote> convertLabciResponse(final Object validServiceResponse) throws Exception {
        Map<String, Map<String, LabciResponse>> result = (Map<String, Map<String, LabciResponse>>) validServiceResponse;
        List<QuotesLabciQuote> priceQuotes = new ArrayList<QuotesLabciQuote>();
        boolean isDelay = (boolean) ArgsHolder.getArgs(QuotesHKCachingServerService.THREAD_INVISIBLE_QUOTES_DELAY);
        boolean isTradingHour = (boolean) ArgsHolder.getArgs(QuotesHKCachingServerService.THREAD_INVISIBLE_QUOTES_TRADING_HOUR);
        List<ProdInbnd> prodInbnds =
            (List<ProdInbnd>) ArgsHolder.getArgs(QuotesHKCachingServerService.THREAD_INVISIBLE_QUOTES_SEARCH_FROM_DB);

        for (Map.Entry<String, Map<String, LabciResponse>> jsonArrayMapper : result.entrySet()) {
            String key = jsonArrayMapper.getKey();
            Map<String, LabciResponse> resultJsonArray = jsonArrayMapper.getValue();
            for (Map.Entry<String, LabciResponse> json : resultJsonArray.entrySet()) {
                Map<String, Object> data = json.getValue().getFields();

                int status = json.getValue().getStatus();
                String symbol = json.getValue().getSymbol();
                ProdInbnd prod = this.getProdInbndByExternalCode(prodInbnds, symbol);

                if (QuotesHKCachingServerService.STATUS_OK == status) {
                    QuotesLabciQuote quote = new QuotesLabciQuote();
                    quote.setSymbol(symbol);
                    if (null != prod) {
                        quote.setSymbol(prod.getExternalNumber());
                        quote.setCompanyName(prod.getProdcutText());
                    }

                    quote.setMarket(QuotesHKCachingServerService.TRADING_MARKET_CODE);
                    // int statusCode = json.getValue().getStatus();
                    // quote.setStatus(this.cachingServerProperties.getResponseStatus(statusCode));
                    quote.setNominalPrice(LabciPropsUtil.inOrderNumberProps("nominalPrice", key, data));
                    quote.setCurrency(LabciPropsUtil.inOrderStrProps("currency", key, data));
                    if (!afterCleanRun(data)) {
                        quote.setChangeAmount(BigDecimalUtil.fromString((String) data.get("NETCHNG_1")));
                        quote.setChangePercent(BigDecimalUtil.fromString((String) data.get("PCTCHNG")));
                        quote.setAsOfDateTime(getUpdateTime((String) data.get("TRADE_DATE"), (String) data.get("TRDTIM_1")));
                    } else {
                        quote.setChangeAmount(BigDecimalUtil.fromString("0"));
                        quote.setChangePercent(BigDecimalUtil.fromString("0"));
                        quote.setAsOfDateTime(getUpdateTime((String) data.get("HSTCLSDATE"), (String) data.get("TRDTIM_1")));
                    }
                    quote.setBidPrice(LabciPropsUtil.inOrderNumberProps("bidPrice", key, data));
                    quote.setBidSize(LabciPropsUtil.inOrderNumberProps("bidSize", key, data));
                    quote.setAskPrice(LabciPropsUtil.inOrderNumberProps("askPrice", key, data));
                    quote.setAskSize(LabciPropsUtil.inOrderNumberProps("askSize", key, data));
                    quote.setOpenPrice(LabciPropsUtil.inOrderNumberProps("openPrice", key, data));
                    quote.setPreviousClosePrice(LabciPropsUtil.inOrderNumberProps("previousClosePrice", key, data));
                    quote.setDayLowPrice(LabciPropsUtil.inOrderNumberProps("dayLowPrice", key, data));
                    quote.setDayHighPrice(LabciPropsUtil.inOrderNumberProps("dayHighPrice", key, data));
                    quote.setYearLowPrice(LabciPropsUtil.inOrderNumberProps("yearLowPrice", key, data));
                    quote.setYearHighPrice(LabciPropsUtil.inOrderNumberProps("yearHighPrice", key, data));
                    quote.setVolume(LabciPropsUtil.inOrderNumberProps("volume", key, data));
                    quote.setPeRatio(LabciPropsUtil.inOrderNumberProps("peRatio", key, data));
                    quote.setEps(LabciPropsUtil.inOrderNumberProps("eps", key, data));
                    quote.setDividendYield(LabciPropsUtil.inOrderNumberProps("dividendYield", key, data));
                    String labciExchangeCode = LabciPropsUtil.inOrderStrProps("exchangeCode", key, data);
                    quote.setExchangeCode(this.cachingServerProperties.getExchangeMapping(labciExchangeCode));
                    quote.setCompanyName(LabciPropsUtil.inOrderStrProps("companyName", key, data));
                    // quote.setAvgVolume(LabciPropsUtil.inOrderNumberProps("avgVolume",
                    // key, data));
                    // quote.setExDividendDate(LabciPropsUtil.inOrderStrProps("exDividendDate",
                    // key, data));
                    // quote.setDividendPayDate(LabciPropsUtil.inOrderStrProps("dividendPayDate",
                    // key, data));
                    // if (isProductSuspended(data, isTradingHour)) {
                    // quote.setStatus(QuotesHKCachingServerService.STATUS_HALTED);
                    // }
                    quote.setDelay(this.isDelayedQuotes(isDelay, isTradingHour));

                    priceQuotes.add(quote);
                } else {
                    ExResponseEntity exResponse = this.exRespComponent.getExResponse(ExCodeConstant.EX_CODE_LABCI_STOCK_NOT_FOUND);
                    SECQuotesRequest request = (SECQuotesRequest) ArgsHolder
                        .getArgs(QuotesHKCachingServerService.THREAD_INVISIBLE_QUOTES_CACHINGSERVER_REQUEST);
                    addMessage(prod.getExternalNumber(), exResponse, request);
                }
            }
        }
        return priceQuotes;
    }

    private ProdInbnd getProdInbndByExternalCode(final List<ProdInbnd> prodInbnds, final String externalCode) {
        ProdInbnd prod = null;
        for (ProdInbnd in : prodInbnds) {
            if (externalCode.equals(in.getExternalCode())) {
                prod = in;
                break;
            }
        }
        return prod;
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

    private String getUpdateTime(final String day, final String gmtTime) throws Exception {
        String result = "";
        if (StringUtil.isValid(day) && StringUtil.isValid(gmtTime)) {
            String iso8601Str = day + QuotesHKCachingServerService.DATETIME_SEPARATOR + gmtTime;
            try {
                result = DateUtil.convertToISO8601Format(iso8601Str, Constant.DATEFORMAT_DDMMYYYHHMM,
                    TimeZone.getTimeZone(Constant.TIMEZONE), TimeZone.getTimeZone(QuotesHKCachingServerService.TIMEZONE_EST));
            } catch (ParseException e) {
                QuotesHKCachingServerService.logger.error("Parse date encounter exception, date is " + iso8601Str, e);
            }
        }
        return result;
    }

    private boolean isDelayedQuotes(final boolean isDelay, final boolean isTradingHour) {
        if (isDelay || !isTradingHour) {
            return true;
        } else {
            return false;
        }
    }
    // private boolean isProductSuspended(final Map<String, Object> fields,
    // final boolean tradingHour) {
    // if ("TH".equals(fields.get("CTS_QUAL")) ||
    // "TH".equals(fields.get("PRC_QL2"))) {
    // return true;
    // }
    // return false;
    // }

}
