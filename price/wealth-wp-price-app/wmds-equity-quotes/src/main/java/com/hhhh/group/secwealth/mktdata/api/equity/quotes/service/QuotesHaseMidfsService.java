/**
 * @Title QuoteLABCIService.java
 * @description TODO
 * @author OJim
 * @time May 30, 2019 11:50:08 AM
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.request.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.EtnetProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.MarketHourProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
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
import com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.handler.BMCComponent;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 */

/**
 * @author OJim
 * @Title QuoteLABCIService.java
 * @description TODO
 * @time May 30, 2019 11:50:08 AM
 */
@Service("midfsQuoteService")
@ConditionalOnProperty(value = "service.quotes.Labci.injectEnabled")
public class QuotesHaseMidfsService
        extends AbstractBaseService<SECQuotesRequest, QuotesLabciResponse, CommonRequestHeader> {

    private static final Logger logger = LoggerFactory.getLogger(QuotesHaseMidfsService.class);

    private static final String HK_MARKET_QUOTE_STATUS = "HK_MARKET_QUOTE_STATUS";

    private static final String ACCES_TYPE_DELAY = "EDSTK";

    private static final String ACCES_TYPE_REAL_TIME = "STOCK";

    private static final String ACCES_CMND_CDE = "QUOTE_MIDFS_ACCES_CMND_CDE";

    private static final String ACCES_CMND_CDE_QUOTE_DETAIL = "QUOTE_DETAIL";

    private static final String ACCES_CMND_CDE_QUOTE_LIST = "QUOTE_LIST";

    private static final String CUSTOMER_ID = "QUOTE_MIDFS_CUSTOMER_ID";


    private static final String WD_APP_CODE = "WD";

    private static final String USER_TYPE_CDE_CUST = "CUST";

    private static final String TRADE_DATE = "tradeDay";

    private static final String USER_TYPE_CDE_STFF = "STFF";

    private static final String KEY_L = "L=";

    private static final String SPECIAL_SEPARATOR = "|SPECIAL_SEPARATOR|";

    private static final String HEADER_FUNCTION_ID_CHECK = "|01|02|03|04|05|06|07|08|09|10|21|22|23|24|25|26|27|28|29|41|42|43|44|45|46|61|62|63|64|71|72|73|74|75|76|91|92|93|94|111|112|131|132|";

    private static final String REQUEST_PRODUCTKEYS_PRODUCTTYPE_CHECK = "|ALL|SEC|WRTS|";

    @Autowired
    private PredSrchService predSrchService;

    @Autowired
    private ExResponseComponent exRespComponent;

    @Autowired
    private LabciProperties labciProps;

    @Autowired
    private EtnetProperties etnetProperties;

    @Autowired
    private HttpClientHelper httpClientHelper;

    @Autowired()
    @Qualifier("quotesQuoteUserService")
    private QuoteUserService quoteUserService;

    @Autowired()
    @Qualifier("quotesQuoteAccessLogService")
    private QuoteAccessLogService quoteAccessLogService;

    @Autowired
    private BMCComponent bmcComponent;

    @Autowired
    private MarketHourProperties marketHourProperties;
    @Autowired
    private TradingSessionService tradingSessionService;


    @SuppressWarnings("java:S3776")
    @Override
    protected Object convertRequest(final SECQuotesRequest request, final CommonRequestHeader header) throws Exception {
        if (!QuotesHaseMidfsService.HEADER_FUNCTION_ID_CHECK.contains(
                SymbolConstant.SYMBOL_VERTICAL_LINE + header.getFunctionId() + SymbolConstant.SYMBOL_VERTICAL_LINE)) {
            QuotesHaseMidfsService.logger.error("Header FunctionId don't match the rules or can't be empty.");
            throw new CommonException(ExCodeConstant.EX_CODE_HEADER_FUNCTIONID_INVALID);
        }
        String eID = request.getEid();
        String appCode = header.getAppCode();
        if (StringUtil.isValid(eID) || QuotesHaseMidfsService.WD_APP_CODE.equals(appCode)) {
            ArgsHolder.putArgs(QuotesHaseMidfsService.CUSTOMER_ID, eID);
        } else if ((Constant.APP_CODE_STB.equals(header.getAppCode()) && Constant.CHANNEL_ID_OHB.equals(header.getChannelId()))
                ||Constant.APP_CODE_CMB.equals(header.getAppCode())) {
            HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String staffID = httpRequest.getHeader(Constant.REQUEST_HEADER_KEY_USER_ID);
            ArgsHolder.putArgs(QuotesHaseMidfsService.CUSTOMER_ID, staffID);
        } else {
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
                        QuotesHaseMidfsService.logger.error("No eID found from rbp cache");
                        throw new VendorException(ExCodeConstant.EX_CODE_CACHE_NO_EID_FOUND);
                    }
                    ArgsHolder.putArgs(QuotesHaseMidfsService.CUSTOMER_ID, customerId);
                } catch (Exception e) {
                    QuotesHaseMidfsService.logger.error("Cache Distribute Bad Request");
                    throw new VendorException(ExCodeConstant.EX_CODE_CACHE_BAD_REQUEST);
                }
            } else {
                if (resultState == CacheDistributeResultStateEnum.INVALID_PARAMETERS) {
                    QuotesHaseMidfsService.logger.error("Cache Distribute Bad Request");
                    throw new VendorException(ExCodeConstant.EX_CODE_CACHE_BAD_REQUEST);
                }
                if (resultState == CacheDistributeResultStateEnum.UNCACHED_RECORD) {
                    QuotesHaseMidfsService.logger.error("Cache Distribute don't contains the key you sent");
                    throw new VendorException(ExCodeConstant.EX_CODE_CACHE_UNCACHED_RECORD);
                }
                if (resultState == CacheDistributeResultStateEnum.INVALID_HTTP_STATUS) {
                    QuotesHaseMidfsService.logger.error("Get response from the Cache Distribute encounter error");
                    throw new VendorException(ExCodeConstant.EX_CODE_CACHE_INVALID_HTTP_STATUS);
                }
            }
        }

        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER, header);
        String timeZone = this.marketHourProperties.getTimeZone(Constant.EXCHANGE_SEHK);
        boolean isDelaySTB = true;
        if ( (!isDelay(request)) && Constant.APP_CODE_STB.equals(header.getAppCode()) && Constant.CHANNEL_ID_OHB.equals(header.getChannelId()) ) {
            if (this.tradingSessionService.isTradingDay()) {
                isDelaySTB = this.marketHourProperties.checkTradingHoursByExchange(Constant.EXCHANGE_SEHK);
            }
            ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_DELAY, isDelaySTB);
        } else {
            ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_DELAY, isDelay(request));
        }
        QuotesServiceRequest quotesServiceRequest = buildLabciServiceRequest(request, header);

        if (Constant.CASE_20.equals(request.getRequestType())) {
            ArgsHolder.putArgs(QuotesHaseMidfsService.ACCES_CMND_CDE,
                    QuotesHaseMidfsService.ACCES_CMND_CDE_QUOTE_DETAIL);
        } else {
            ArgsHolder.putArgs(QuotesHaseMidfsService.ACCES_CMND_CDE, QuotesHaseMidfsService.ACCES_CMND_CDE_QUOTE_LIST);
        }
        Map<String, String> serviceRequestMapper = new LinkedHashMap<>();
        String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
        String service = this.labciProps.getLabciService(site, false);
        Map<String, List<ServiceProductKey>> groupedProductKeysMapper = getGroupedProductKeysMapper(
                quotesServiceRequest);
        for (Map.Entry<String, List<ServiceProductKey>> groupedProductKeys : groupedProductKeysMapper.entrySet()) {
            String key = groupedProductKeys.getKey();
            List<ServiceProductKey> productKeys = groupedProductKeys.getValue();
            List<String> items = new ArrayList<>();
            for (int i = 0; i < productKeys.size(); i++) {
                ServiceProductKey productKey = productKeys.get(i);
                items.add(productKey.getProdAltNum());
            }
            String symbolList = LabciServletBoConvertor.genSymbols(items, service);

            List<NameValuePair> midfsParams = new ArrayList<NameValuePair>();
            midfsParams.add(new BasicNameValuePair("symbol", symbolList));
            midfsParams.add(new BasicNameValuePair("channel", header.getChannelId()));
            if (Constant.APP_CODE_STB.equals(header.getAppCode()) && Constant.CHANNEL_ID_OHB.equals(header.getChannelId())) {
                midfsParams.add(new BasicNameValuePair("delay", isDelaySTB + ""));
            } else {
                midfsParams.add(new BasicNameValuePair("delay", isDelay(request) + ""));
            }
            String midfsReqParams = URLEncodedUtils.format(midfsParams, "UTF-8");

            List<NameValuePair> etnetParam = new ArrayList<NameValuePair>();
            try {
                String token = etnetProperties.getEtnetTokenWithoutVerify();
                if (Constant.APP_CODE_STB.equals(header.getAppCode()) && Constant.CHANNEL_ID_OHB.equals(header.getChannelId())) {
                    etnetParam.add(new BasicNameValuePair("realTime", isDelaySTB ? "N" : "Y"));
                } else {
                    etnetParam.add(new BasicNameValuePair("realTime", isDelay(request) ? "N" : "Y"));
                }
                etnetParam.add(new BasicNameValuePair("symbol",
                        symbolList.replaceAll(SymbolConstant.SYMBOL_SEMISOLON, SymbolConstant.SYMBOL_COMMA)));
                etnetParam.add(new BasicNameValuePair("token", token));
            } catch (Exception e) {
                try {
                    this.bmcComponent.doBMC(new VendorException(ExCodeConstant.EX_CODE_ACCESS_ETNET_ERROR),
                            ExTraceCodeGenerator.generate());
                } catch (IOException e2) {
                    QuotesHaseMidfsService.logger.error("Write BMC encounter error", e2);
                }
                QuotesHaseMidfsService.logger.error("Access ETNet encounter error", e);
            }
            String etnetReqParams = URLEncodedUtils.format(etnetParam, "UTF-8");
            serviceRequestMapper.put(key, midfsReqParams + QuotesHaseMidfsService.SPECIAL_SEPARATOR + etnetReqParams);
        }
        return serviceRequestMapper;
    }

    /**
     * @param request
     * @param header
     * @return
     * @return QuotesServiceRequest
     * @Title buildServiceRequest
     * @Description
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
            QuotesHaseMidfsService.logger.error("Wrong requestType");
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
                } else if (!QuotesHaseMidfsService.REQUEST_PRODUCTKEYS_PRODUCTTYPE_CHECK
                        .contains(SymbolConstant.SYMBOL_VERTICAL_LINE + request.getProductKeys().get(i).getProductType()
                                + SymbolConstant.SYMBOL_VERTICAL_LINE)) {
                    QuotesHaseMidfsService.logger.error("Request productKeys productType don't match the rules.");
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
        if (Constant.CASE_0.equals(requestType) && symbols.size() > 20) {
            QuotesHaseMidfsService.logger.error("Request ProdAltNum more than 20");
            throw new VendorException(ExCodeConstant.EX_CODE_MIDFS_REQUEST_PRODALTNUM_MORE_THAN_20);
        }
        ArgsHolder.putArgs(QuotesHaseMidfsService.HK_MARKET_QUOTE_STATUS, requestType);
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
                QuotesHaseMidfsService.logger.warn("precSearch error is :" + e.toString());
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
                symbolsMap, responses, Constant.PROD_CDE_ALT_CLASS_CODE_M));
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
            Message message = new Message();
            message.setText(exResponse.getText());
            message.setReasonCode(exResponse.getReasonCode());
            String traceCode = ExTraceCodeGenerator.generate();
            message.setTraceCode(traceCode);
            message.setProdAltNum(invalidSymbol);
            message.setProdCdeAltClassCde("M");
            message.setProductType("SEC");
            for (ProductKey productKey : request.getProductKeys()) {
                if (invalidSymbol.contains(productKey.getProdAltNum())) {
                    message.setProductType(productKey.getProductType());
                    message.setProdCdeAltClassCde(productKey.getProdCdeAltClassCde());
                    message.setProdAltNum(productKey.getProdAltNum());
                    break;
                }
            }
            addMessage(message);
        }
    }

    private Map<String, List<ServiceProductKey>> getGroupedProductKeysMapper(final QuotesServiceRequest request) {
        Map<String, List<ServiceProductKey>> groupedProductKeysMapper = new LinkedHashMap<>();
        String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
        List<ServiceProductKey> productKeys = request.getServiceProductKeys();
        for (int i = 0; i < productKeys.size(); i++) {
            ServiceProductKey productKey = productKeys.get(i);
            String key = new StringBuilder().append(site).append(SymbolConstant.SYMBOL_VERTICAL_LINE)
                    .append(productKey.getProductType()).append(SymbolConstant.SYMBOL_VERTICAL_LINE)
                    .append(productKey.getExchange()).toString();
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
        QuotesHaseMidfsService.logger.debug("Invalid Symbols: " + invalidSymbols);
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
            int index = request.indexOf(QuotesHaseMidfsService.SPECIAL_SEPARATOR);
            String midfsRequest = request.substring(0, index);
            String etnetRequest = request.substring(index + QuotesHaseMidfsService.SPECIAL_SEPARATOR.length());
            String status = (String) ArgsHolder.getArgs(QuotesHaseMidfsService.HK_MARKET_QUOTE_STATUS);
            try {
                switch (status) {
                    case Constant.CASE_10:
                        serviceResponseMapper.put(key,
                                CallMidfsAndETnet(midfsRequest, etnetRequest, this.labciProps.getMidfsQuoteListUrl()));
                        break;
                    case Constant.CASE_20:
                        serviceResponseMapper.put(key,
                                CallMidfsAndETnet(midfsRequest, etnetRequest, this.labciProps.getMidfsQuoteDetailUrl()));
                        break;
                    default:
                        serviceResponseMapper.put(key,
                                CallMidfsAndETnet(midfsRequest, etnetRequest, this.labciProps.getMidfsBmpQuoteUrl()));
                        break;
                }
            } catch (Exception e) {
                if (e instanceof IOException) {
                    QuotesHaseMidfsService.logger.error("Midfs Undefined error", e);
                    throw new VendorException(ExCodeConstant.EX_CODE_MIDFS_UNDEFINED_ERROR, e);
                } else if (e instanceof ConnectTimeoutException) {
                    QuotesHaseMidfsService.logger.error("Access Midfs error", e);
                    throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_Midfs_ERROR, e);
                } else {
                    QuotesHaseMidfsService.logger.error("Midfs server error", e);
                    throw new VendorException(ExCodeConstant.EX_CODE_MIDFS_SERVER_ERROR, e);
                }
            }

        }
        return serviceResponseMapper;
    }

    private String CallMidfsAndETnet(String midfsRequest, String etnetRequest, String midfsUrl) throws Exception {
        String midfsResponse = this.httpClientHelper.doGet(midfsUrl, midfsRequest, null);
        String etnetResponse = "";
        try {
            etnetResponse = this.httpClientHelper.doGet(this.etnetProperties.getProxyName(),
                    this.etnetProperties.getEtnetQuoteUrl(), etnetRequest, null);
            JsonObject respJsonObj = JsonUtil.getAsJsonObject(etnetResponse);
            String errCode = JsonUtil.getAsString(respJsonObj, "err_code");
            if (StringUtil.isValid(errCode) && EtnetProperties.EX_CODE_ETNET_INVALID_TOKEN_CODE.equals(errCode)) {
                List<NameValuePair> params = URLEncodedUtils.parse(etnetRequest, Charset.forName("UTF-8"));
                int index = 0;
                for (int i = 0; i < params.size(); i++) {
                    if ("token".equals(params.get(i).getName())) {
                        index = i;
                    }
                }
                params.remove(index);
                params.add(new BasicNameValuePair("token", etnetProperties.getEtnetToken()));
                etnetResponse = this.httpClientHelper.doGet(this.etnetProperties.getProxyName(),
                        this.etnetProperties.getEtnetQuoteUrl(), params, null);
            }
        } catch (Exception e) {
            try {
                this.bmcComponent.doBMC(new VendorException(ExCodeConstant.EX_CODE_ACCESS_ETNET_ERROR),
                        ExTraceCodeGenerator.generate());
            } catch (IOException e2) {
                QuotesHaseMidfsService.logger.error("Write BMC encounter error", e2);
            }
            QuotesHaseMidfsService.logger.error("Access ETNet encounter error", e);
        }
        String response = midfsResponse + QuotesHaseMidfsService.SPECIAL_SEPARATOR + etnetResponse;
        return response;
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
        validServiceResponseMapper = HKMarketResponse(serviceResponseMapper);
        return validServiceResponseMapper;
    }

    private Long getUserId() {
        CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
        QuoteUserForQuotes quoteUser = new QuoteUserForQuotes();
        quoteUser.setUserExtnlId(ArgsHolder.getArgs(QuotesHaseMidfsService.CUSTOMER_ID).toString());
        if (Constant.APP_CODE_STB.equals(header.getAppCode()) && Constant.CHANNEL_ID_OHB.equals(header.getChannelId())) {
            quoteUser.setUserType(QuotesHaseMidfsService.USER_TYPE_CDE_STFF);
        } else {
            quoteUser.setUserType(QuotesHaseMidfsService.USER_TYPE_CDE_CUST);
        }
        //quoteUser.setUserType(QuotesHaseMidfsService.USER_TYPE_CDE_CUST);
        quoteUser.setGroupMember(header.getGroupMember());
        quoteUser.setUserMarketCode(header.getCountryCode());
        quoteUser.setMonitorFlag((long) 0);
        return this.quoteUserService.getUserByExtnlId(quoteUser);
    }

    private String symbolToStr(final String symbol, final String quote) {
        String priceDetail = "";
        if (null != quote) {
            priceDetail = QuotesHaseMidfsService.KEY_L + quote;
        }
        String result = symbol + SymbolConstant.SYMBOL_LEFT_CIRCLE_BRACKET + priceDetail
                + SymbolConstant.SYMBOL_RIGHT_CIRCLE_BRACKET;
        return result;
    }

    /**
     * @param serviceResponseMapper
     * @return
     * @return Object
     * @Title BMPResponse
     * @Description
     * @Author OJim
     * @Date Jun 24, 2019 2:41:51 PM
     */
    private Object HKMarketResponse(Map<String, String> serviceResponseMapper) {
        Map<String, JsonArray> validServiceResponseMapper = new LinkedHashMap<>();
        List<String> validates = new ArrayList<>();
        for (Map.Entry<String, String> responseMapper : serviceResponseMapper.entrySet()) {
            String key = responseMapper.getKey();
            JsonArray jsonArray = validateEachMarketResponse(responseMapper.getValue());
            if (jsonArray != null && jsonArray.size() > 0) {
                validServiceResponseMapper.put(key, jsonArray);
                Iterator<JsonElement> it = jsonArray.iterator();
                while (it.hasNext()) {
                    JsonObject resultJsonObj = it.next().getAsJsonObject();
                    if (resultJsonObj.get("symbol") != null) {
                        validates.add(LabciPropsUtil.inOrderStrProps("symbol", resultJsonObj));
                    }
                }
            }
        }
        @SuppressWarnings("unchecked")
        List<String> symbols = (List<String>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_PRODUCT_KEY);
        if (validates != null && validates.size() != symbols.size()) {
            ExResponseEntity exResponse = this.exRespComponent
                    .getExResponse(ExCodeConstant.EX_CODE_MIDFS_STOCK_NOT_FOUND);
            addMessage(symbols, validates, exResponse);
        }
        try {
            saveAccessLogHKMarket(validServiceResponseMapper);
//			throw new ApplicationException(ExCodeConstant.EX_CODE_DB_ACTION_FAIL);
        } catch (Exception e) {
            QuotesHaseMidfsService.logger.error("Error: Failed to write quotes history", e);
            throw new ApplicationException(ExCodeConstant.EX_CODE_DB_ACTION_FAIL);
        }
        return validServiceResponseMapper;
    }

    /**
     * @param validServiceResponseMapper
     * @return void
     * @throws ParseException
     * @Title saveAccessLogHKMarket
     * @Description
     * @Author OJim
     * @Date Jul 10, 2019 2:18:56 PM
     */
    private void saveAccessLogHKMarket(Map<String, JsonArray> validServiceResponseMapper) throws ParseException {
        List<QuoteAccessLogForQuotes> quoteAccessLogs = new ArrayList<>();
        boolean isDelay = (Boolean) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_DELAY);
        SECQuotesRequest request = (SECQuotesRequest) ArgsHolder
                .getArgs(Constant.THREAD_INVISIBLE_QUOTES_LABCI_REQUEST);
        CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
        String status = (String) ArgsHolder.getArgs(QuotesHaseMidfsService.HK_MARKET_QUOTE_STATUS);
        Long userReferenceId = getUserId();
        for (Map.Entry<String, JsonArray> jsonArrayMapper : validServiceResponseMapper.entrySet()) {
            QuoteAccessLogForQuotes quoteAccessLog = new QuoteAccessLogForQuotes();
            JsonArray resultJsonArray = jsonArrayMapper.getValue();
            String exchangeCode = "";
            StringBuffer sb = new StringBuffer();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < resultJsonArray.size(); i++) {
                JsonObject resultJsonObj = resultJsonArray.get(i).getAsJsonObject();
                PredSrchResponse predSrchResp = this.predSrchService.localPredSrch(
                        LabciPropsUtil.inOrderStrProps("symbol", resultJsonObj),
                        jsonArrayMapper.getKey().split(SymbolConstant.SYMBOL_VERTICAL_LINE_ESCAPE)[1],
                        Constant.PROD_CDE_ALT_CLASS_CODE_M);
                String symbol = predSrchResp.getSymbol();
                String quote = LabciPropsUtil.inOrderStrProps("nominalPrice", resultJsonObj);
                exchangeCode = predSrchResp.getExchange();
                stringBuffer.append(symbolToStr(symbol, quote));
                stringBuffer.append(SymbolConstant.SYMBOL_SEMISOLON);
            }
            if (stringBuffer.length() == 0) {
                continue;
            }
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
            quoteAccessLog.setAccessCommand(ArgsHolder.getArgs(QuotesHaseMidfsService.ACCES_CMND_CDE).toString());
            quoteAccessLog.setChargeCategory(status);
            quoteAccessLog.setRequestType(isDelay(request) ? QuotesHaseMidfsService.ACCES_TYPE_DELAY
                    : QuotesHaseMidfsService.ACCES_TYPE_REAL_TIME);
            quoteAccessLog.setAccessCount(resultJsonArray.size());
            if (Constant.APP_CODE_STB.equals(header.getAppCode()) && Constant.CHANNEL_ID_OHB.equals(header.getChannelId())) {
                quoteAccessLog.setResponseType(isDelay ? QuotesHaseMidfsService.ACCES_TYPE_DELAY
                        : QuotesHaseMidfsService.ACCES_TYPE_REAL_TIME);
            } else {
                quoteAccessLog.setResponseType(isDelay(request) ? QuotesHaseMidfsService.ACCES_TYPE_DELAY
                        : QuotesHaseMidfsService.ACCES_TYPE_REAL_TIME);
            }
            quoteAccessLog.setResponseText(sb.toString());
            quoteAccessLog.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
            quoteAccessLogs.add(quoteAccessLog);
        }
        if (quoteAccessLogs.size() > 0) {
            this.quoteAccessLogService.saveQuoteAccessLog(quoteAccessLogs);
        }
    }

    private JsonArray validateEachMarketResponse(final String serviceResponse) {
        int index = serviceResponse.indexOf(QuotesHaseMidfsService.SPECIAL_SEPARATOR);
        String midfsResponses = serviceResponse;
        String etnetResponses = "";
        if (index > -1) {
            midfsResponses = serviceResponse.substring(0, index);
            etnetResponses = serviceResponse.substring(index + QuotesHaseMidfsService.SPECIAL_SEPARATOR.length());
        }
        JsonObject respJsonObj = JsonUtil.getAsJsonObject(midfsResponses);
        if (respJsonObj == null) {
            QuotesHaseMidfsService.logger.error("Invalid response: {}", serviceResponse);
            throw new VendorException(ExCodeConstant.EX_CODE_MIDFS_INVALID_RESPONSE);
        }
        String responseCode = JsonUtil.getAsString(respJsonObj, "responseCode");
        switch (responseCode) {
            case "01":
                QuotesHaseMidfsService.logger.error("Invalid request parameters", serviceResponse);
                throw new VendorException(ExCodeConstant.EX_CODE_MIDFS_INVALID_REQUEST);
            case "02":
                QuotesHaseMidfsService.logger.error("Internal system error", serviceResponse);
                throw new VendorException(ExCodeConstant.EX_CODE_MIDFS_SERVER_ERROR);
            case "03":
                QuotesHaseMidfsService.logger.error("Stock not found", serviceResponse);
                throw new VendorException(ExCodeConstant.EX_CODE_MIDFS_STOCK_NOT_FOUND);
            default:
                break;
        }
        JsonArray resultJsonArray = JsonUtil.getAsJsonArray(respJsonObj, "priceQuotes");
        try {
            if (StringUtil.isValid(etnetResponses)) {
                JsonObject etnetJsonObj = JsonUtil.getAsJsonObject(etnetResponses);
                JsonArray etnetJsonArray = JsonUtil.getAsJsonArray(etnetJsonObj, "priceQuotes");
                if (etnetJsonArray != null) {
                    for (int i = 0; i < resultJsonArray.size(); i++) {
                        JsonObject resultJsonObj = resultJsonArray.get(i).getAsJsonObject();
                        for (int j = 0; j < etnetJsonArray.size(); j++) {
                            JsonObject etnetJson = etnetJsonArray.get(j).getAsJsonObject();
                            if (resultJsonObj.get("symbol") != null && etnetJson.get("symbol") != null
                                    && LabciPropsUtil.inOrderStrProps("symbol", resultJsonObj)
                                    .equals(LabciPropsUtil.inOrderStrProps("symbol", etnetJson))) {
                                resultJsonObj.add("yearHighPrice", etnetJson.get("yearHighPrice"));
                                resultJsonObj.add("yearLowPrice", etnetJson.get("yearLowPrice"));
                                resultJsonObj.add("marketCap", etnetJson.get("marketCap"));
                                resultJsonObj.add("eps", etnetJson.get("eps"));
                                resultJsonObj.add("peRatio", etnetJson.get("peRatio"));
                                resultJsonObj.add("dividendYield", etnetJson.get("dividendYield"));
                                resultJsonObj.add("expectedPERatio", etnetJson.get("expectedPERatio"));
                                resultJsonObj.add("predictDividendYield", etnetJson.get("predictDividendYield"));
                                resultJsonObj.add("gearingRatio", etnetJson.get("gearingRatio"));
                                resultJsonObj.add("impliedVolatility", etnetJson.get("impliedVolatility"));
                                resultJsonObj.add("realtime", etnetJson.get("realtime"));
                                resultJsonObj.add("underlyingProduct", etnetJson.get("underlyingProduct"));
                                resultJsonObj.add("lastTradeDate", etnetJson.get("lastTradeDate"));
                                resultJsonObj.add("cbbcType", etnetJson.get("cbbcType"));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            QuotesHaseMidfsService.logger.error("Access ETNet encounter error", e);
        }
        return resultJsonArray;
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
        String status = (String) ArgsHolder.getArgs(QuotesHaseMidfsService.HK_MARKET_QUOTE_STATUS);
        switch (status) {
            case Constant.CASE_10:
                response.setPriceQuotes(quotesDetailResponse(validServiceResponse));
                break;
            case Constant.CASE_20:
                response.setPriceQuotes(quotesDetailResponse(validServiceResponse));
                break;
            default:
                response.setPriceQuotes(quotesDetailResponse(validServiceResponse));
                break;
        }
        response.setMessages(getMessages());
        response.setRemainingQuota(new BigDecimal("-1"));
        response.setTotalQuota(new BigDecimal("-1"));
        return response;
    }

    /**
     * @param validServiceResponse
     * @return
     * @return List<QuotesLabciQuote>
     * @Title quoteDetailResponse
     * @Description
     * @Author OJim
     * @Date Jun 25, 2019 2:09:07 PM
     */
    private List<QuotesLabciQuote> quotesDetailResponse(Object validServiceResponse) {
        @SuppressWarnings("unchecked")
        Map<String, JsonArray> resultJsonArrayMapper = (Map<String, JsonArray>) validServiceResponse;
        List<QuotesLabciQuote> priceQuotes = new ArrayList<>();
        CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
        for (Map.Entry<String, JsonArray> jsonArrayMapper : resultJsonArrayMapper.entrySet()) {
            JsonArray resultJsonArray = jsonArrayMapper.getValue();
            for (int i = 0; i < resultJsonArray.size(); i++) {
                JsonObject resultJsonObj = resultJsonArray.get(i).getAsJsonObject();
                QuotesLabciQuote priceQuote = new QuotesLabciQuote();
                priceQuotes.add(priceQuote);
                PredSrchResponse predSrchResp = this.predSrchService.localPredSrch(
                        LabciPropsUtil.inOrderStrProps("symbol", resultJsonObj),
                        jsonArrayMapper.getKey().split(SymbolConstant.SYMBOL_VERTICAL_LINE_ESCAPE)[1],
                        Constant.PROD_CDE_ALT_CLASS_CODE_M);
                if (predSrchResp != null) {
                    priceQuote.setProdAltNumSegs(predSrchResp.getProdAltNumSegs());
                    priceQuote.setSymbol(predSrchResp.getSymbol());
                    priceQuote.setMarket(predSrchResp.getMarket());
                    priceQuote.setExchangeCode(predSrchResp.getExchange());
                    priceQuote.setProductType(StringUtil.isInValid(predSrchResp.getProductType())
                            ? LabciPropsUtil.inOrderStrProps("productType", resultJsonObj)
                            : predSrchResp.getProductType());
                    priceQuote.setProductSubType(StringUtil.isInValid(predSrchResp.getProductSubType())
                            ? LabciPropsUtil.inOrderStrProps("productSubType", resultJsonObj)
                            : predSrchResp.getProductSubType());
                    priceQuote.setCompanyName(companyName(resultJsonObj, header.getLocale()));
                    priceQuote.setRiskLvlCde(predSrchResp.getRiskLvlCde());
                    //HASE SWT requires to T.Chinese name
                    priceQuote.setCompanySecondName(predSrchResp.getProductSecondName());
                }
                priceQuote.setAsOfDateTime(LabciPropsUtil.inOrderStrProps("asOfDateTime", resultJsonObj));
                priceQuote.setIsSuspended(LabciPropsUtil.inOrderBooProps("suspendedIndicator", resultJsonObj));
                String sessionType = LabciPropsUtil.inOrderStrProps("sessionType", resultJsonObj);
                if (sessionType != null) {
                    sessionType = sessionType.trim();
                }
                priceQuote.setAuctionIndicator("A".equalsIgnoreCase(sessionType) ? "Y" : "N");
                priceQuote.setCurrency(LabciPropsUtil.inOrderStrProps("currency", resultJsonObj));
                priceQuote.setNominalPrice(LabciPropsUtil.inOrderNumberProps("nominalPrice", resultJsonObj));
                priceQuote.setNominalPriceType(LabciPropsUtil.inOrderStrProps("nominalPriceType", resultJsonObj));
                priceQuote.setChangeAmount(LabciPropsUtil.inOrderNumberProps("changeAmount", resultJsonObj));
                priceQuote.setChangePercent(LabciPropsUtil.inOrderNumberProps("changePercent", resultJsonObj));
                priceQuote.setDelay((boolean) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_DELAY));
                priceQuote.setPreviousClosePrice(LabciPropsUtil.inOrderNumberProps("preClosePrice", resultJsonObj));
                priceQuote.setDayHighPrice(LabciPropsUtil.inOrderNumberProps("dayHighPrice", resultJsonObj));
                priceQuote.setDayLowPrice(LabciPropsUtil.inOrderNumberProps("dayLowPrice", resultJsonObj));
                priceQuote.setOpenPrice(LabciPropsUtil.inOrderNumberProps("openPrice", resultJsonObj));
                priceQuote.setVolume(LabciPropsUtil.inOrderNumberProps("volume", resultJsonObj));
                priceQuote.setTurnover(LabciPropsUtil.inOrderNumberProps("turnover", resultJsonObj));
                priceQuote.setBoardLotSize(LabciPropsUtil.inOrderNumberProps("boardLotSize", resultJsonObj));
                priceQuote.setIep(LabciPropsUtil.inOrderNumberProps("iep", resultJsonObj));
                priceQuote.setIev(LabciPropsUtil.inOrderNumberProps("iev", resultJsonObj));
                priceQuote.setBidPrice(LabciPropsUtil.inOrderNumberProps("bidPrice", resultJsonObj));
                priceQuote.setBidSize(LabciPropsUtil.inOrderNumberProps("bidSize", resultJsonObj));
                priceQuote.setBidSpread(LabciPropsUtil.inOrderNumberProps("bidSpread", resultJsonObj));
                priceQuote.setAskPrice(LabciPropsUtil.inOrderNumberProps("askPrice", resultJsonObj));
                priceQuote.setAskSize(LabciPropsUtil.inOrderNumberProps("askSize", resultJsonObj));
                priceQuote.setAskSpread(LabciPropsUtil.inOrderNumberProps("askSpread", resultJsonObj));

                priceQuote.setBidAskQueues(LabciPropsUtil.bidAskQueues("bidaskQueue", resultJsonObj));

                priceQuote.setDerivativeFlag(LabciPropsUtil.inOrderStrProps("derivativeFlag", resultJsonObj));
                priceQuote.setStrikePrice(LabciPropsUtil.inOrderNumberProps("strikePrice", resultJsonObj));
                priceQuote.setStrikeUpper(LabciPropsUtil.inOrderNumberProps("upperStrike", resultJsonObj));
                priceQuote.setStrikeLower(LabciPropsUtil.inOrderNumberProps("lowerStrike", resultJsonObj));
                priceQuote.setCallPrice(LabciPropsUtil.inOrderNumberProps("callPrice", resultJsonObj));
                priceQuote.setPremium(LabciPropsUtil.inOrderStrProps("premium", resultJsonObj));
                priceQuote.setConversionRatio(LabciPropsUtil.inOrderNumberProps("conversionRatio", resultJsonObj));
                SimpleDateFormat formater1 = new SimpleDateFormat(Constant.DATE_FORMAT_YYYY_MM_DD);
                SimpleDateFormat formater2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                String maturitydate = LabciPropsUtil.inOrderStrProps("maturitydate", resultJsonObj);
                try {
                    if (StringUtil.isValid(maturitydate)) {
                        priceQuote.setMaturityDate(formater1.format(formater2.parse(maturitydate)));
                    }
                } catch (ParseException e) {
                    QuotesHaseMidfsService.logger.error("format maturitydate error", e);
                }
                priceQuote.setCallPutIndicator(LabciPropsUtil.inOrderStrProps("callputIndicator", resultJsonObj));
                priceQuote.setWarrantType(LabciPropsUtil.inOrderStrProps("warrantType", resultJsonObj));
                if (StringUtil.isValid(LabciPropsUtil.inOrderStrProps("underlyingProduct", resultJsonObj))
                        && !"null".equals(LabciPropsUtil.inOrderStrProps("underlyingProduct", resultJsonObj))) {
                    priceQuote.setUnderlyingProduct(LabciPropsUtil.inOrderStrProps("underlyingProduct", resultJsonObj));
                }
                if (StringUtil.isValid(LabciPropsUtil.inOrderStrProps("cbbcType", resultJsonObj))
                        && !"null".equals(LabciPropsUtil.inOrderStrProps("cbbcType", resultJsonObj))) {
                    priceQuote.setCbbcType(LabciPropsUtil.inOrderStrProps("cbbcType", resultJsonObj));
                }
                if (StringUtil.isValid(LabciPropsUtil.inOrderStrProps("lastTradeDate", resultJsonObj))
                        && !"null".equals(LabciPropsUtil.inOrderStrProps("lastTradeDate", resultJsonObj))) {
                    priceQuote.setLastTradeDate(LabciPropsUtil.inOrderStrProps("lastTradeDate", resultJsonObj));
                }
                priceQuote.setVcmEligible(LabciPropsUtil.inOrderStrProps("vcmEligible", resultJsonObj));
                priceQuote.setVcmStatus(LabciPropsUtil.inOrderStrProps("vcmStatus", resultJsonObj));
                priceQuote.setVcmStartTime(LabciPropsUtil.inOrderStrProps("vcmStarttime", resultJsonObj));
                priceQuote.setVcmEndTime(LabciPropsUtil.inOrderStrProps("vcmEndtime", resultJsonObj));
                priceQuote.setVcmReferencePrice(LabciPropsUtil.inOrderNumberProps("vcmRefPrice", resultJsonObj));
                priceQuote
                        .setVcmUpperLimitPrice(LabciPropsUtil.inOrderNumberProps("vcmUpperLimitPrice", resultJsonObj));
                priceQuote
                        .setVcmLowerLimitPrice(LabciPropsUtil.inOrderNumberProps("vcmLowerLimitPrice", resultJsonObj));
                priceQuote.setCasEligible(LabciPropsUtil.inOrderStrProps("casEligible", resultJsonObj));
                priceQuote.setCasReferencePrice(LabciPropsUtil.inOrderNumberProps("casReferencePrice", resultJsonObj));
                priceQuote
                        .setCasUpperLimitPrice(LabciPropsUtil.inOrderNumberProps("casUpperLimitPrice", resultJsonObj));
                priceQuote
                        .setCasLowerLimitPrice(LabciPropsUtil.inOrderNumberProps("casLowerLimitPrice", resultJsonObj));
                priceQuote.setOrderImbalanceDirection(
                        LabciPropsUtil.inOrderStrProps("orderImbalanceDirection", resultJsonObj));
                priceQuote.setOrderImbalanceQuantity(
                        LabciPropsUtil.inOrderStrProps("orderImbalanceQuantity", resultJsonObj));
                priceQuote.setMarketStatus(LabciPropsUtil.inOrderStrProps("marketStatus", resultJsonObj));

                priceQuote.setYearLowPrice(LabciPropsUtil.inOrderNumberProps("yearLowPrice", resultJsonObj));
                priceQuote.setYearHighPrice(LabciPropsUtil.inOrderNumberProps("yearHighPrice", resultJsonObj));
                priceQuote.setPeRatio(LabciPropsUtil.inOrderNumberProps("peRatio", resultJsonObj));
                priceQuote.setDividendYield(LabciPropsUtil.inOrderNumberProps("dividendYield", resultJsonObj));
                priceQuote.setMarketCap(LabciPropsUtil.inOrderNumberProps("marketCap", resultJsonObj));
                priceQuote.setExpectedPERatio(LabciPropsUtil.inOrderNumberProps("expectedPERatio", resultJsonObj));
                priceQuote.setPredictDividendYield(
                        LabciPropsUtil.inOrderNumberProps("predictDividendYield", resultJsonObj));
                priceQuote.setGearingRatio(LabciPropsUtil.inOrderNumberProps("gearingRatio", resultJsonObj));
                priceQuote.setImpliedVolatility(LabciPropsUtil.inOrderNumberProps("impliedVolatility", resultJsonObj));
                priceQuote.setEps(LabciPropsUtil.inOrderNumberProps("eps", resultJsonObj));

                priceQuote.setSpreadCode(LabciPropsUtil.inOrderStrProps("spreadCode", resultJsonObj));

                priceQuote.setPosEligible(LabciPropsUtil.inOrderStrProps("posEligible", resultJsonObj));
                priceQuote.setAuctionType(LabciPropsUtil.inOrderStrProps("auctionType", resultJsonObj));
                priceQuote.setPosReferencePrice(LabciPropsUtil.inOrderNumberProps("posReferencePrice", resultJsonObj));
                priceQuote.setPosBuyLowerPrice(LabciPropsUtil.inOrderNumberProps("posBuyLowerPrice", resultJsonObj));
                priceQuote.setPosBuyUpperPrice(LabciPropsUtil.inOrderNumberProps("posBuyUpperPrice", resultJsonObj));
                priceQuote.setPosSellLowerPrice(LabciPropsUtil.inOrderNumberProps("posSellLowerPrice", resultJsonObj));
                priceQuote.setPosSellUpperPrice(LabciPropsUtil.inOrderNumberProps("posSellUpperPrice", resultJsonObj));

                priceQuote.setRiskAlert(null);

            }
        }
        return priceQuotes;
    }

    private String companyName(JsonObject resultJsonObj, String local) {
        switch (local) {
            case "zh_CN":
                return LabciPropsUtil.inOrderStrProps("companyNameSC", resultJsonObj);
            case "zh_HK":
                return LabciPropsUtil.inOrderStrProps("companyNameTC", resultJsonObj);
            default:
                return LabciPropsUtil.inOrderStrProps("companyNameEN", resultJsonObj);
        }
    }

    @SuppressWarnings("unchecked")
    private List<Message> getMessages() {
        if (ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES) == null) {
            return null;
        } else {
            return (List<Message>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES);
        }
    }

    public boolean isWeekend(final String timeZone, final Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        cal.setTime(date);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        }
        return false;
    }

}
