/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.service.MarketAgreementService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.TrisTokenService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.ApplicationProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.TrisProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.QuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.SECQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ProductKey;
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
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

@Service("tris")
@ConditionalOnProperty(value = "service.quotes.SG.injectEnabled")
public class QuotesSGTrisService extends AbstractBaseService<SECQuotesRequest, QuotesResponse, CommonRequestHeader> {

    private static final Logger logger = LoggerFactory.getLogger(QuotesSGTrisService.class);

    @Autowired
    private ApplicationProperties appProps;

    @Autowired
    private TrisProperties trisProps;

    @Autowired
    private TrisTokenService trisTokenService;

    @Autowired
    private HttpClientHelper httpClientHelper;

    @Autowired
    private PredSrchService predSrchService;

    @Autowired
    private QuoteCommonService quoteCommonService;

    @Autowired
    private ExResponseComponent exRespComponent;

    @Autowired
    private BMCComponent bmcComponent;

    @Autowired
    MarketAgreementService marketAgreementservice;

    @Override
    protected Object convertRequest(final SECQuotesRequest request, final CommonRequestHeader header) {
        QuotesSGTrisService.logger.info("SG Equity service init " + "service.quotes.SG.injectEnabled");
        if (!request.getDelay()) {
            QuotesSGTrisService.logger.error("Delay type not support, Dealy = " + request.getDelay());
            throw new ApplicationException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
        }

        QuotesServiceRequest quotesServiceRequest = buildServiceRequest(request, header);
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER, header);
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_QUOTES_REQUEST, request);


        // List<ServiceProductKey> serviceProductKeys =
        // quotesServiceRequest.getServiceProductKeys();
        // String tCode = "";
        // if (!Collections.isEmpty(serviceProductKeys)) {
        // tCode = serviceProductKeys.get(0).getProdAltNum();
        // }

        boolean delay = true;
        // check if skip agreemen check first
        // boolean isSkip =
        // this.marketAgreementservice.isSkipAgreementCheck(header.getAppCode(),
        // request.getRequestType());
        // here is no need to check when
        // this.predSrchService.localPredSrch(tCode) == null,
        // because if it is null, will throw exception when put in ThreadLocal
        // before
        // PredSrchResponse predResp =
        // this.predSrchService.localPredSrch(tCode);
        // if (predResp != null) {
        // String exchangeCode = predResp.getExchange();
        // if (isSkip) {
        // delay =
        // this.marketAgreementservice.checkTradingHoursByExchange(exchangeCode);
        // } else {
        // delay =
        // this.marketAgreementservice.doAgreementCheckByExchange(exchangeCode);
        // }
        // }
        Map<String, TrisQuoteServiceRequest> serviceRequestMapper = new HashMap<>();
        String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
        String closure = this.trisProps.getTrisClosure(site);
        String appCode = this.trisProps.getTrisTokenAppCode(site);
        String timezone = this.trisProps.getTrisTokenTimezone(site);

        String token = this.trisTokenService.getEncryptedTrisToken(site, header, appCode, closure, timezone);
        String service = this.trisProps.getTrisService(site, delay);
        Map<String, List<ServiceProductKey>> groupedProductKeysMapper = getGroupedProductKeysMapper(quotesServiceRequest);
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


    /**
     *
     * <p>
     * <b> Get the necessary attributes for access Tris by call PredSrch
     * service. </b>
     * </p>
     *
     * @param request
     * @param header
     * @return
     */
    private QuotesServiceRequest buildServiceRequest(final SECQuotesRequest request, final CommonRequestHeader header) {
        QuotesServiceRequest serviceRequest = new QuotesServiceRequest();
        serviceRequest.setDelay(isDelay(request));
        List<String> symbols = new ArrayList<>();
        List<ServiceProductKey> serviceProductKeys = new ArrayList<>();
        for (ProductKey productKey : request.getProductKeys()) {
            if (Constant.PROD_CDE_ALT_CLASS_CODE_M.equals(productKey.getProdCdeAltClassCde())) {
                symbols.add(productKey.getProdAltNum());
            }
            if (Constant.PROD_CDE_ALT_CLASS_CODE_R.equals(productKey.getProdCdeAltClassCde())) {
                ServiceProductKey serviceProductKey = new ServiceProductKey();
                serviceProductKey.setProdAltNum(productKey.getProdAltNum());
                serviceProductKey.setProdCdeAltClassCde(productKey.getProdCdeAltClassCde());
                serviceProductKey.setExchange(productKey.getExchange());
                serviceProductKey.setProductType(productKey.getProductType());
                serviceProductKeys.add(serviceProductKey);
            }
        }

        List<PredSrchResponse> responses = null;
        if (!ListUtils.isEmpty(symbols)) {
            responses = this.predSrchService.precSrch(symbols, header);
        }

        if (responses != null && responses.size() != symbols.size()) {
            ExResponseEntity exResponse = this.exRespComponent.getExResponse(ExCodeConstant.EX_CODE_PREDSRCH_INVALID_RESPONSE);
            List<String> invalidSymbols = getInvalidSymbols(symbols, responses);
            for (String invalidSymbol : invalidSymbols) {
                Message message = new Message();
                message.setReasonCode(exResponse.getReasonCode());
                message.setText(exResponse.getText());
                String traceCode = ExTraceCodeGenerator.generate();
                message.setTraceCode(traceCode);
                message.setProductType("SEC");
                message.setProdCdeAltClassCde(Constant.PROD_CDE_ALT_CLASS_CODE_M);
                message.setProdAltNum(invalidSymbol);
                addMessage(message);
                try {
                    this.bmcComponent.doBMC(new VendorException(ExCodeConstant.EX_CODE_PREDSRCH_INVALID_RESPONSE), traceCode);
                } catch (IOException e) {
                    QuotesSGTrisService.logger.error("Write BMC encounter error", e);
                }
            }
        }
        serviceRequest.setServiceProductKeys(constructServiceProductKeys(symbols, responses, serviceProductKeys));
        return serviceRequest;
    }



    private boolean isDelay(final QuotesRequest request) {
        return true;
    }

    private List<String> getInvalidSymbols(final List<String> symbols, final List<PredSrchResponse> responses) {
        List<String> validSymbols = new ArrayList<>();
        for (PredSrchResponse response : responses) {
            validSymbols.add(response.getSymbol());
        }
        List<String> invalidSymbols = new ArrayList<>();
        for (String symbol : symbols) {
            invalidSymbols.add(symbol);
        }
        invalidSymbols.removeAll(validSymbols);
        QuotesSGTrisService.logger.error("Invalid Symbols: {}", invalidSymbols);
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

    @SuppressWarnings("unchecked")
    private List<Message> getMessages() {
        if (ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES) == null) {
            return null;
        } else {
            return (List<Message>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES);
        }
    }

    private List<ServiceProductKey> constructServiceProductKeys(final List<String> symbols,
        final List<PredSrchResponse> responses, final List<ServiceProductKey> serviceProductKeysOfRCode) {
        Map<String, String> prodAltExchangeMap = new HashMap<>();
        List<ServiceProductKey> serviceProductKeys = new ArrayList<>();
        if (symbols != null && responses != null) {
        for (String symbol : symbols) {
            ServiceProductKey serviceProductKey = new ServiceProductKey();
            for (PredSrchResponse response : responses) {
                // Should be ignore upper case or lower case
                if (symbol.equalsIgnoreCase(response.getSymbol())) {
                    List<ProdAltNumSeg> prodAltNumSegs = response.getProdAltNumSegs();
                    for (ProdAltNumSeg prodAltNumSeg : prodAltNumSegs) {
                        if (Constant.PROD_CDE_ALT_CLASS_CODE_T.equals(prodAltNumSeg.getProdCdeAltClassCde())) {
                            serviceProductKey.setProdCdeAltClassCde(Constant.PROD_CDE_ALT_CLASS_CODE_T);
                            serviceProductKey.setProdAltNum(prodAltNumSeg.getProdAltNum());
                        }
                    }
                    serviceProductKey.setProductType(response.getProductType());
                    serviceProductKey.setMarket(response.getMarket());
                    serviceProductKey.setExchange(response.getExchange());
                    prodAltExchangeMap.put(serviceProductKey.getProdAltNum(), response.getExchange());
                    serviceProductKeys.add(serviceProductKey);
                }
            }
        }
        }
        for (ServiceProductKey serviceProductKey : serviceProductKeysOfRCode) {
            prodAltExchangeMap.put(serviceProductKey.getProdAltNum(), serviceProductKey.getExchange());
            serviceProductKeys.add(serviceProductKey);
        }
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_ALTNUM_EXCHNAGE, prodAltExchangeMap);
        return serviceProductKeys;
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

    @Override
    protected Object execute(final Object serviceRequest) {
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
                QuotesSGTrisService.logger.error("Access Tris encounter error", e);
                throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_TRIS_ERROR, e);
            }
        }
        return serviceResponseMapper;
    }

    @Override
    protected Object validateServiceResponse(final Object serviceResponse) {
        Map<String, JsonArray> validServiceResponseMapper = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, String> serviceResponseMapper = (Map<String, String>) serviceResponse;
        for (Map.Entry<String, String> responseMapper : serviceResponseMapper.entrySet()) {
            String key = responseMapper.getKey();
            JsonArray jsonArray = validateEachServiceResponse(responseMapper.getValue());
            if (jsonArray != null && jsonArray.size() > 0) {
                validServiceResponseMapper.put(key, jsonArray);
            }
        }
        return validServiceResponseMapper;
    }

    private JsonArray validateEachServiceResponse(final String serviceResponse) {
        JsonObject respJsonObj = JsonUtil.getAsJsonObject(serviceResponse);
        if (respJsonObj == null) {
            QuotesSGTrisService.logger.error("Invalid response: {}", serviceResponse);
            throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
        }
        String statusKey = this.trisProps.getResponseStatusCodeKey();
        String status = JsonUtil.getAsString(respJsonObj, statusKey);
        if (!this.trisProps.isCorrectResponseStatus(status)) {
            QuotesSGTrisService.logger.error("Invalid response, status is incorrect: {}", serviceResponse);
            throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
        }
        JsonArray resultJsonArray = JsonUtil.getAsJsonArray(respJsonObj, "result");
        if (resultJsonArray == null || resultJsonArray.size() <= 0) {
            QuotesSGTrisService.logger.error("Invalid response, result array is empty: {}", serviceResponse);
            throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
        }
        Iterator<JsonElement> it = resultJsonArray.iterator();
        List<JsonObject> JsonObjectList = new ArrayList<>();
        while (it.hasNext()) {
            JsonObject resultJsonObj = it.next().getAsJsonObject();
            status = JsonUtil.getAsString(resultJsonObj, statusKey);
            if (!this.trisProps.isCorrectResponseStatus(status)) {
                QuotesSGTrisService.logger.error("Invalid response, result status is incorrect:{} ", serviceResponse);
                Message message = new Message();
                ExResponseEntity exResponse = this.exRespComponent.getExResponse(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
                message.setReasonCode(exResponse.getReasonCode());
                message.setText(exResponse.getText());
                String traceCode = ExTraceCodeGenerator.generate();
                message.setTraceCode(traceCode);
                PredSrchResponse predSrchResp = this.predSrchService.localPredSrch(JsonUtil.getAsString(resultJsonObj, "_item"));
                if (null != predSrchResp) {
                    message.setProdAltNum(predSrchResp.getSymbol());
                    message.setProductType(predSrchResp.getProductType());
                    message.setProdCdeAltClassCde(Constant.PROD_CDE_ALT_CLASS_CODE_M);
                } else {
                    message.setProdAltNum(JsonUtil.getAsString(resultJsonObj, "_item"));
                    message.setProductType("INDEX");
                    message.setProdCdeAltClassCde(Constant.PROD_CDE_ALT_CLASS_CODE_R);
                }
                addMessage(message);
                it.remove();
                try {
                    this.bmcComponent.doBMC(new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE), traceCode);
                } catch (IOException e) {
                    QuotesSGTrisService.logger.error("Write BMC encounter error", e);
                }
            } else {
                JsonObjectList.add(resultJsonObj);
            }
        }
        this.quoteCommonService.saveQuoteHistory(JsonObjectList,
                (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER),
                (Map<String, String>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_ALTNUM_EXCHNAGE));
        return resultJsonArray;
    }

    @Override
    protected QuotesResponse convertResponse(final Object validServiceResponse) {
        QuotesResponse response = new QuotesResponse();
        Map<String, String> prodAltExchangeMap =
            (Map<String, String>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_ALTNUM_EXCHNAGE);
        List<QuotesPriceQuote> priceQuotes = new ArrayList<>();
        response.setPriceQuotes(priceQuotes);
        @SuppressWarnings("unchecked")
        Map<String, JsonArray> resultJsonArrayMapper = (Map<String, JsonArray>) validServiceResponse;
        // loop by keys
        for (Map.Entry<String, JsonArray> jsonArrayMapper : resultJsonArrayMapper.entrySet()) {
            String key = jsonArrayMapper.getKey();
            JsonArray resultJsonArray = jsonArrayMapper.getValue();
            // loop by results
            for (int i = 0; i < resultJsonArray.size(); i++) {
                JsonObject resultJsonObj = resultJsonArray.get(i).getAsJsonObject();
                QuotesPriceQuote priceQuote = new QuotesPriceQuote();
                priceQuotes.add(priceQuote);

                PredSrchResponse predSrchResp = this.predSrchService.localPredSrch(JsonUtil.getAsString(resultJsonObj, "_item"));
                if (predSrchResp != null) {
                    priceQuote.setProdAltNumSegs(predSrchResp.getProdAltNumSegs());
                    priceQuote.setSymbol(predSrchResp.getSymbol());
                    priceQuote.setMarket(predSrchResp.getMarket());
                    priceQuote.setExchangeCode(predSrchResp.getExchange());
                    priceQuote.setProductType(predSrchResp.getProductType());
                    priceQuote.setCompanyName(predSrchResp.getProductName());
                    priceQuote.setRiskLvlCde(predSrchResp.getRiskLvlCde());
                    priceQuote.setAsOfDateTime(TrisPropsUtil.dateProps("asOfDateTime", key, resultJsonObj,
                        this.appProps.getTimezone(predSrchResp.getExchange())));
                } else {
                    // for R Code product
                    String symbol = JsonUtil.getAsString(resultJsonObj, "_item");
                    priceQuote.setSymbol(symbol);
                    String exchange = prodAltExchangeMap.get(symbol);
                    priceQuote.setExchangeCode(exchange);
                    priceQuote.setAsOfDateTime(
                        TrisPropsUtil.dateProps("asOfDateTime", key, resultJsonObj, this.appProps.getTimezone(exchange)));
                }

                priceQuote.setNominalPrice(TrisPropsUtil.inOrderNumberProps("nominalPrice", key, resultJsonObj));
                priceQuote.setCurrency(TrisPropsUtil.inOrderStrProps("currency", key, resultJsonObj));
                priceQuote.setChangeAmount(TrisPropsUtil.subtractProps("changeAmount", key, resultJsonObj));
                priceQuote.setChangePercent(
                    TrisPropsUtil.growthRateProps("changePercent", key, resultJsonObj, new MathContext(4, RoundingMode.HALF_UP)));
                // TODO: realtime or delay quote
                priceQuote.setDelay(true);

                priceQuote.setBidPrice(TrisPropsUtil.inOrderNumberProps("bidPrice", key, resultJsonObj));
                priceQuote.setBidSize(TrisPropsUtil.inOrderNumberProps("bidSize", key, resultJsonObj));
                priceQuote.setAskPrice(TrisPropsUtil.inOrderNumberProps("askPrice", key, resultJsonObj));
                priceQuote.setAskSize(TrisPropsUtil.inOrderNumberProps("askSize", key, resultJsonObj));
                priceQuote.setBidSpread(TrisPropsUtil.inOrderNumberProps("bidSpread", key, resultJsonObj));
                priceQuote.setAskSpread(TrisPropsUtil.inOrderNumberProps("askSpread", key, resultJsonObj));
                priceQuote.setOpenPrice(TrisPropsUtil.inOrderNumberProps("openPrice", key, resultJsonObj));
                priceQuote.setPreviousClosePrice(TrisPropsUtil.inOrderNumberProps("previousClosePrice", key, resultJsonObj));
                priceQuote.setDayLowPrice(TrisPropsUtil.inOrderNumberProps("dayLowPrice", key, resultJsonObj));
                priceQuote.setDayHighPrice(TrisPropsUtil.inOrderNumberProps("dayHighPrice", key, resultJsonObj));
                priceQuote.setYearLowPrice(TrisPropsUtil.inOrderNumberProps("yearLowPrice", key, resultJsonObj));
                priceQuote.setYearHighPrice(TrisPropsUtil.inOrderNumberProps("yearHighPrice", key, resultJsonObj));
                priceQuote.setVolume(TrisPropsUtil.inOrderNumberProps("volume", key, resultJsonObj));
                priceQuote.setBoardLotSize(TrisPropsUtil.inOrderNumberProps("boardLotSize", key, resultJsonObj));
                priceQuote.setMarketCap(TrisPropsUtil.inOrderNumberProps("marketCap", key, resultJsonObj));
                priceQuote.setPeRatio(TrisPropsUtil.inOrderNumberProps("peRatio", key, resultJsonObj));
                priceQuote.setEps(TrisPropsUtil.inOrderNumberProps("eps", key, resultJsonObj));
                priceQuote.setTurnover(TrisPropsUtil.inOrderNumberProps("turnover", key, resultJsonObj));
                priceQuote.setDividend(TrisPropsUtil.inOrderNumberProps("dividend", key, resultJsonObj));
                priceQuote.setDividendYield(TrisPropsUtil.inOrderNumberProps("dividendYield", key, resultJsonObj));
                priceQuote.setBidAskQueues(TrisPropsUtil.bidAskQueues(key, resultJsonObj));
                // TODO: quota
                priceQuote.setRemainingQuota(new BigDecimal("-1"));
                priceQuote.setTotalQuota(new BigDecimal("-1"));
            }
        }
        response.setMessages(getMessages());
        return response;
    }

}
