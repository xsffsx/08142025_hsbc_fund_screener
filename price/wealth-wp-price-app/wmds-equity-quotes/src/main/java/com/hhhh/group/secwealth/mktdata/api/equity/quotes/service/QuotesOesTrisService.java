/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import java.io.IOException;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.TrisTokenService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.ApplicationProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.TrisProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.QuoteHistoryRepository;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.QuoteHistory;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.OESQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ServiceProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.service.QuotesServiceRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.service.TrisQuoteServiceRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.Message;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesPriceQuote;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.util.TrisPropsUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.handler.BMCComponent;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.exception.ExTraceCodeGenerator;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.ExResponseComponent;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.entity.ExResponseEntity;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Service("oesTris")
@ConditionalOnProperty(value = "service.quotes.OES.injectEnabled")
public class QuotesOesTrisService extends AbstractBaseService<OESQuotesRequest, QuotesResponse, CommonRequestHeader> {

    private static final Logger logger = LoggerFactory.getLogger(QuotesOesTrisService.class);

    @Autowired
    private ApplicationProperties appProps;

    @Autowired
    private TrisProperties trisProps;

    @Autowired
    private TrisTokenService trisTokenService;

    @Autowired
    private HttpClientHelper httpClientHelper;

    @Autowired
    private QuoteHistoryService quoteHistoryService;

    @Autowired
    private QuoteHistoryRepository quoteHistoryDao;

    @Autowired
    private ExResponseComponent exRespComponent;

    @Autowired
    private BMCComponent bmcComponent;


    /*
     * (non-Javadoc)
     *
     * @see com.hhhh.group.secwealth.mktdata.starter.core.service.
     * AbstractBaseService#convertRequest(java.lang.Object, java.lang.Object)
     */
    protected Object convertRequest(final OESQuotesRequest request, final CommonRequestHeader header) throws Exception {
        QuotesServiceRequest quoteServiceRequest = buildQuoteServiceRequest(request);
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER, header);
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_QUOTES_REQUEST, request);
        Map<String, TrisQuoteServiceRequest> serviceRequestMapper = new HashMap<>();
        String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
        String closure = this.trisProps.getTrisClosure(site);
        String appCode = this.trisProps.getTrisTokenAppCode(site);
        String timezone = this.trisProps.getTrisTokenTimezone(site);
        String token = this.trisTokenService.getEncryptedTrisToken(site, header, appCode, closure, timezone);
        String service = this.trisProps.getTrisService(site, quoteServiceRequest.getDelay());
        Map<String, List<ServiceProductKey>> groupedProductKeysMapper = getGroupedProductKeysMapper(quoteServiceRequest);
        for (Map.Entry<String, List<ServiceProductKey>> groupedProductKeys : groupedProductKeysMapper.entrySet()) {
            String key = groupedProductKeys.getKey();
            String[] keys = key.split(SymbolConstant.SYMBOL_VERTICAL_LINE_ESCAPE);
            List<ServiceProductKey> productKeys = groupedProductKeys.getValue();
            List<String> items = new ArrayList<>();
            for (int i = 0; i < productKeys.size(); i++) {
                ServiceProductKey productKey = productKeys.get(i);
                items.add(productKey.getProdAltNum());
            }
            TrisQuoteServiceRequest serviceRequest = new TrisQuoteServiceRequest();
            serviceRequest.setClosure(closure);
            serviceRequest.setToken(token);
            serviceRequest.setService(service);
            serviceRequest.addItem(items);
            serviceRequest.addFilter(this.trisProps.getTrisFields(this.appProps.getQuotesResponseTrisFields(keys), keys));
            serviceRequestMapper.put(key, serviceRequest);

        }
        return serviceRequestMapper;
    }

    private QuotesServiceRequest buildQuoteServiceRequest(final OESQuotesRequest request) {
        QuotesServiceRequest quoteServiceRequest = new QuotesServiceRequest();
        quoteServiceRequest.setDelay(request.getDelay());
        String prodAltNums[] = request.getProdAltNums();
        List<ServiceProductKey> serviceProductKeyList = new ArrayList<>();

        for (String prodAltNum : prodAltNums) {
            ServiceProductKey serviceProductKey = new ServiceProductKey();
            serviceProductKey.setMarket(request.getMarket());
            serviceProductKey.setProdAltNum(prodAltNum);
            serviceProductKey.setProdCdeAltClassCde(request.getProdCdeAltClassCde());
            serviceProductKey.setProductType(request.getProductType());
            serviceProductKeyList.add(serviceProductKey);
        }
        quoteServiceRequest.setServiceProductKeys(serviceProductKeyList);
        return quoteServiceRequest;
    }

    /**
     *
     * <p>
     * <b> {site}_{product}_{exchange} => List<ProductKey>. </b>
     * </p>
     *
     * @param request
     * @return
     */
    private Map<String, List<ServiceProductKey>> getGroupedProductKeysMapper(final QuotesServiceRequest request) {
        Map<String, List<ServiceProductKey>> groupedProductKeysMapper = new HashMap<>();
        String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
        List<ServiceProductKey> productKeys = request.getServiceProductKeys();
        for (int i = 0; i < productKeys.size(); i++) {
            ServiceProductKey productKey = productKeys.get(i);
            String key =
                new StringBuilder().append(site).append(SymbolConstant.SYMBOL_VERTICAL_LINE).append(productKey.getProductType())
                .append(SymbolConstant.SYMBOL_VERTICAL_LINE).append(productKey.getMarket()).toString();
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


    /*
     * (non-Javadoc)
     *
     * @see com.hhhh.group.secwealth.mktdata.starter.core.service.
     * AbstractBaseService#execute(java.lang.Object)
     */
    protected Object execute(final Object serviceRequest) throws Exception {
        Map<String, String> serviceResponseMapper = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, TrisQuoteServiceRequest> serviceRequestMapper = (Map<String, TrisQuoteServiceRequest>) serviceRequest;
        for (Map.Entry<String, TrisQuoteServiceRequest> requestMapper : serviceRequestMapper.entrySet()) {
            String key = requestMapper.getKey();
            TrisQuoteServiceRequest request = requestMapper.getValue();
            try {
                serviceResponseMapper.put(key,
                    this.httpClientHelper.doPost(this.trisProps.getTrisUrl(), JsonUtil.toJson(request), null));
            } catch (Exception e) {
                QuotesOesTrisService.logger.error("Access Tris encounter error", e);
                throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_TRIS_ERROR, e);
            }
        }
        return serviceResponseMapper;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.hhhh.group.secwealth.mktdata.starter.core.service.
     * AbstractBaseService#validateServiceResponse(java.lang.Object)
     */
    protected Object validateServiceResponse(final Object serviceResponse) throws Exception {
        Map<String, JsonArray> validServiceResponseMapper = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, String> serviceResponseMapper = (Map<String, String>) serviceResponse;
        for (Map.Entry<String, String> responseMapper : serviceResponseMapper.entrySet()) {
            String key = responseMapper.getKey();
            JsonArray jsonArray = validateEachServiceResponse(responseMapper.getValue());
            if (jsonArray.size() > 0) {
                validServiceResponseMapper.put(key, jsonArray);
            }
        }
        return validServiceResponseMapper;
    }

    private JsonArray validateEachServiceResponse(final String serviceResponse) {
        OESQuotesRequest quotesRequest = (OESQuotesRequest) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_REQUEST);
        JsonObject respJsonObj = JsonUtil.getAsJsonObject(serviceResponse);
        if (respJsonObj == null) {
            QuotesOesTrisService.logger.error("Invalid response: " + serviceResponse);
            throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
        }
        String statusKey = this.trisProps.getResponseStatusCodeKey();
        String status = JsonUtil.getAsString(respJsonObj, statusKey);
        if (!this.trisProps.isCorrectResponseStatus(status)) {
            QuotesOesTrisService.logger.error("Invalid response, status is incorrect: " + serviceResponse);
            throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
        }
        JsonArray resultJsonArray = JsonUtil.getAsJsonArray(respJsonObj, "result");
        if (resultJsonArray == null || resultJsonArray.size() <= 0) {
            QuotesOesTrisService.logger.error("Invalid response, result array is empty: " + serviceResponse);
            throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
        }
        Iterator<JsonElement> it = resultJsonArray.iterator();
        List<JsonObject> JsonObjectList = new ArrayList<>();
        while (it.hasNext()) {
            JsonObject resultJsonObj = it.next().getAsJsonObject();
            JsonObjectList.add(resultJsonObj);
            status = JsonUtil.getAsString(resultJsonObj, statusKey);
            try {
                if (!this.trisProps.isCorrectResponseStatus(status)) {
                    QuotesOesTrisService.logger.error("Invalid response, result status is incorrect: " + serviceResponse);
                    throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
                }
            } catch (Exception ex) {
                Message message = new Message();
                ExResponseEntity exResponse = this.exRespComponent.getExResponse(ex.getMessage());
                message.setReasonCode(exResponse.getReasonCode());
                message.setText(exResponse.getText());
                String traceCode = ExTraceCodeGenerator.generate();
                message.setTraceCode(traceCode);
                message.setProductType(quotesRequest.getProductType());
                message.setProdAltNum(JsonUtil.getAsString(resultJsonObj, "_item"));
                QuotesOesTrisService.logger.info("tris return \"_item\"= " + message.getProdAltNum());
                message.setProdCdeAltClassCde(quotesRequest.getProdCdeAltClassCde());

                addMessage(message);
                it.remove();
                try {
                    this.bmcComponent.doBMC(new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE), traceCode);
                } catch (IOException e) {
                    QuotesOesTrisService.logger.error("Write BMC encounter error", e);
                }
            }
        }
        try {
            saveQuoteHistory(JsonObjectList, (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            QuotesOesTrisService.logger.error("Write quotes History encounter error", e);

        }
        return resultJsonArray;
    }

    private void saveQuoteHistory(final List<JsonObject> jsonObject, final CommonRequestHeader commonHeader) throws Exception {
        OESQuotesRequest quotesRequest = (OESQuotesRequest) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_REQUEST);
        Long seqNum = this.quoteHistoryDao.querySeqNum();
        String subscriberId = (String) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_ENCRYPTED_CUSTOMER_ID);
        // String subscriberId = null != customerId ? customerId :
        // this.appProps.getCustomerId();
        String timestampString = DateUtil.parseDateByTimezone(new Date(), Constant.TIMEZONE, Constant.DATE_FORMAT_TRIS_ISO8601);
        List<QuoteHistory> quoteList = new ArrayList<>();
        for (JsonObject object : jsonObject) {
            QuoteHistory quoteHistory = new QuoteHistory();
            String trisCode = JsonUtil.getAsString(object, "_item");
            String peCode = JsonUtil.getAsString(object, "PROD_PERM");
            quoteHistory.setPeCode(peCode);
            quoteHistory.setTradeDatetime(timestampString);
            quoteHistory.setUpdatedOn(Calendar.getInstance().getTime());
            String exchangeCode = JsonUtil.getAsString(object, "RDN_EXCHD2");
            quoteHistory.setExchangeCode(exchangeCode);
            quoteHistory.setQuotHistBatId(seqNum);
            quoteHistory.setMarketCode(quotesRequest.getMarket());
            quoteHistory.setSubscriberId(subscriberId);
            quoteHistory.setSymbol(trisCode);
            if (object != null) {
                quoteHistory.setResponseText(object.toString());
            } else {
                quoteHistory.setResponseText("Response Error");
            }
            String statusKey = this.trisProps.getResponseStatusCodeKey();
            String status = JsonUtil.getAsString(object, statusKey);
            String stateCode = status.equals(Constant.ZERO) ? "Y" : "N";
            quoteHistory.setRequestStatus(stateCode);

            if (quotesRequest.getDelay()) {
                quoteHistory.setRequestType(Constant.DELAYED);
            } else {
                quoteHistory.setRequestType(Constant.REAL_TIME);
            }
            quoteHistory.setRequestTime(Calendar.getInstance().getTime());
            quoteHistory.setCountryCode(commonHeader.getCountryCode());
            quoteHistory.setGroupMember(commonHeader.getGroupMember());
            quoteHistory.setRequestTimeZone(DateUtil.getTimeZoneName());
            quoteHistory.setChannelId(commonHeader.getChannelId());
            String appCode = commonHeader.getAppCode();
            if (null != appCode && !"".equals(appCode)) {
                quoteHistory.setAppCode(appCode);
            }
            quoteList.add(quoteHistory);
        }
        this.quoteHistoryService.updatequoteHistory(quoteList);

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

    /*
     * (non-Javadoc)
     *
     * @see com.hhhh.group.secwealth.mktdata.starter.core.service.
     * AbstractBaseService#convertResponse(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    protected QuotesResponse convertResponse(final Object validServiceResponse) throws Exception {
        OESQuotesRequest quotesRequest = (OESQuotesRequest) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_REQUEST);
        QuotesResponse response = new QuotesResponse();
        List<QuotesPriceQuote> priceQuotes = new ArrayList<>();
        response.setPriceQuotes(priceQuotes);
        Map<String, JsonArray> resultJsonArrayMapper = (Map<String, JsonArray>) validServiceResponse;

        // loop by keys
        for (Map.Entry<String, JsonArray> jsonArrayMapper : resultJsonArrayMapper.entrySet()) {
            String key = jsonArrayMapper.getKey();
            JsonArray resultJsonArray = jsonArrayMapper.getValue();
            // loop by results
            for (int i = 0; i < resultJsonArray.size(); i++) {
                JsonObject resultJsonObj = resultJsonArray.get(i).getAsJsonObject();
                QuotesPriceQuote priceQuote = new QuotesPriceQuote();
                String trisCode = JsonUtil.getAsString(resultJsonObj, "_item");
                priceQuote.setSymbol(trisCode);

                List<ProdAltNumSeg> prodAltNumSegList = new ArrayList<ProdAltNumSeg>();
                ProdAltNumSeg prodAltNumSeg = new ProdAltNumSeg();
                prodAltNumSeg.setProdAltNum(trisCode);
                prodAltNumSeg.setProdCdeAltClassCde(quotesRequest.getProdCdeAltClassCde());
                prodAltNumSegList.add(prodAltNumSeg);
                priceQuote.setProdAltNumSegs(prodAltNumSegList);
                priceQuotes.add(priceQuote);
                priceQuote.setCurrency(TrisPropsUtil.inOrderStrProps("currency", key, resultJsonObj));
                priceQuote.setChangeAmount(TrisPropsUtil.subtractProps("changeAmount", key, resultJsonObj));
                priceQuote.setChangePercent(
                    TrisPropsUtil.growthRateProps("changePercent", key, resultJsonObj, new MathContext(4, RoundingMode.HALF_UP)));
                priceQuote.setAskSize(TrisPropsUtil.inOrderNumberProps("askSize", key, resultJsonObj));

                priceQuote.setOpenPrice(TrisPropsUtil.inOrderNumberProps("openPrice", key, resultJsonObj));
                priceQuote.setDayLowPrice(TrisPropsUtil.inOrderNumberProps("dayLowPrice", key, resultJsonObj));
                priceQuote.setDayHighPrice(TrisPropsUtil.inOrderNumberProps("dayHighPrice", key, resultJsonObj));
                priceQuote.setBoardLotSize(TrisPropsUtil.inOrderNumberProps("boardLotSize", key, resultJsonObj));
                priceQuote.setMarketCap(TrisPropsUtil.inOrderNumberProps("marketCap", key, resultJsonObj));
                priceQuote.setEps(TrisPropsUtil.inOrderNumberProps("eps", key, resultJsonObj));
                priceQuote.setDividend(TrisPropsUtil.inOrderNumberProps("dividend", key, resultJsonObj));
                priceQuote.setYearLowPrice(TrisPropsUtil.inOrderNumberProps("yearLowPrice", key, resultJsonObj));
                priceQuote.setYearHighPrice(TrisPropsUtil.inOrderNumberProps("yearHighPrice", key, resultJsonObj));
                // TODO: market trading hour
                priceQuote.setBidPrice(TrisPropsUtil.inOrderNumberProps("bidPrice", key, resultJsonObj));
                priceQuote.setAskPrice(TrisPropsUtil.inOrderNumberProps("askPrice", key, resultJsonObj));
                priceQuote.setPreviousClosePrice(TrisPropsUtil.inOrderNumberProps("previousClosePrice", key, resultJsonObj));
                priceQuote.setVolume(TrisPropsUtil.inOrderNumberProps("volume", key, resultJsonObj));
                priceQuote.setUnscaledTurnover(TrisPropsUtil.inOrderNumberProps("unscaledTurnover", key, resultJsonObj));
                priceQuote.setAccumulatedVolume(TrisPropsUtil.inOrderStrProps("accumulatedVolume", key, resultJsonObj));
                priceQuote.setAsOfDateTime(TrisPropsUtil.dateProps("asOfDateTime", key, resultJsonObj,
                    this.appProps.getTimezone(Constant.DEFAULT_OPTION)));

                priceQuote.setDividendYield(TrisPropsUtil.inOrderNumberProps("dividendYield", key, resultJsonObj));
                priceQuote.setNominalPrice(TrisPropsUtil.inOrderNumberProps("nominalPrice", key, resultJsonObj));
                priceQuote.setPeRatio(TrisPropsUtil.inOrderNumberProps("peRatio", key, resultJsonObj));
                priceQuote.setPrevTradePrice(TrisPropsUtil.inOrderNumberProps("prevTradePrice", key, resultJsonObj));
                priceQuote.setPrimaryLastActivity(TrisPropsUtil.inOrderNumberProps("primaryLastActivity", key, resultJsonObj));
                priceQuote.setSettlementPrice(TrisPropsUtil.inOrderNumberProps("settlementPrice", key, resultJsonObj));
                priceQuote.setSharesOutstanding(TrisPropsUtil.inOrderNumberProps("sharesOutstanding", key, resultJsonObj));
                priceQuote.setTotalIssuedShares(TrisPropsUtil.inOrderNumberProps("totalIssuedShares", key, resultJsonObj));
                priceQuote
                .setTotalSharesOutstanding(TrisPropsUtil.inOrderNumberProps("totalSharesOutstanding", key, resultJsonObj));
                priceQuote.setTurnover(TrisPropsUtil.inOrderNumberProps("turnover", key, resultJsonObj));
                priceQuote.setDaySecLowLimPrice(TrisPropsUtil.inOrderNumberProps("daySecLowLimPrice", key, resultJsonObj));
                priceQuote.setDaySecUpperLimPrice(TrisPropsUtil.inOrderNumberProps("daySecUpperLimPrice", key, resultJsonObj));
                priceQuote.setDayLowLimPrice(TrisPropsUtil.inOrderNumberProps("dayLowLimPrice", key, resultJsonObj));
                priceQuote.setDayUpperLimPrice(TrisPropsUtil.inOrderNumberProps("dayUpperLimPrice", key, resultJsonObj));
                priceQuote.setLimitReferencePrice(TrisPropsUtil.inOrderNumberProps("limitReferencePrice", key, resultJsonObj));
                priceQuote.setPriceCode(TrisPropsUtil.inOrderStrProps("priceCode", key, resultJsonObj));
                priceQuote.setBidAskQueues(TrisPropsUtil.bidAskQuote(key, resultJsonObj));

            }
        }

        response.setMessages(getMessages());

        return response;
    }

}
