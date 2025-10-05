package com.hhhh.group.secwealth.mktdata.api.equity.topmover.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.service.MarketAgreementService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.TrisTokenService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.TrisProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.BigDecimalUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.bean.ChainMapping;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.request.TopMoverRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.request.service.TrisTopMoverRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.response.TopMoverProduct;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.response.TopMoverResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.response.TopMoverTable;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.IllegalInitializationException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ParameterException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service("trisTopmover")
public class TopMoverService extends AbstractBaseService<TopMoverRequest, TopMoverResponse, CommonRequestHeader> {

    private static final Logger logger = LoggerFactory.getLogger(TopMoverService.class);

    @Autowired
    private TrisTokenService trisTokenService;

    @Autowired
    private HttpClientHelper httpClientHelper;

    @Autowired
    private TrisProperties trisProps;

    @Autowired
    private ChainMapping chainMapping;

    @Autowired
    private PredSrchService predSrchService;

    // Chain Type: key=TURNOVER value=1 , key=VOLUME value=2 , key=GAINERS
    // value=3 ,
    // key=LOSERS value=4 -->
    private Map<String, String> mappingRule = new ConcurrentHashMap<String, String>();

    @Autowired
    MarketAgreementService marketAgreementservice;

    @Override
    protected Object convertRequest(final TopMoverRequest request, final CommonRequestHeader header) throws Exception {
        TopMoverService.logger.debug("convert TopMoverRequest to TrisTopMoverRequest list......");
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER, header);
        String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
        String closure = this.trisProps.getTrisClosure(site);
        String appCode = this.trisProps.getTrisTokenAppCode(site);
        String timezone = this.trisProps.getTrisTokenTimezone(site);
        String token = this.trisTokenService.getEncryptedTrisToken(site, header, appCode, closure, timezone);
        boolean delay = request.getDelay();
        if (this.marketAgreementservice.isRealtimeQuotesEnable()) {
            // When client query real-time data, do agreement checking and market hour checking
            if (!delay) {
                delay = this.marketAgreementservice.checkAgreementAndMarketHour(request.getExchangeCode()) ? false : true;
            }
        } else {
            delay = true;
        }
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_DELAY, delay);

        String service = this.trisProps.getTrisService(site, false);
        List<String> items = null;
        Map<String, TrisTopMoverRequest> serviceRequestMapper = new HashMap<String, TrisTopMoverRequest>();
        if (StringUtil.isValid(request.getProductType()) && StringUtil.isValid(request.getExchangeCode())) {
            List<Map<String, String>> ricCodes =
                this.getRicCode(request.getProductType().trim() + "." + request.getExchangeCode().trim());
            if (ricCodes != null && ricCodes.size() > 0) {
                TrisTopMoverRequest TrisTopMoverRequest = null;
                for (Map<String, String> ricCode : ricCodes) {
                    TrisTopMoverRequest = new TrisTopMoverRequest();
                    TrisTopMoverRequest.setClosure(closure);
                    TrisTopMoverRequest.setService(service);
                    TrisTopMoverRequest.setToken(token);
                    TrisTopMoverRequest.setExpandChain(true);
                    TrisTopMoverRequest.setChainLink(false);
                    TrisTopMoverRequest.setEmptyLink(false);
                    items = new ArrayList<String>();
                    if (delay) {
                        // the item like '/.PLST.HK' , need to add '/' in the
                        // front , if delay is true
                        items.add(SymbolConstant.SLASH + ricCode.get(Constant.RIC));
                    } else {
                        items.add(ricCode.get(Constant.RIC));
                    }
                    TrisTopMoverRequest.setItem(items);
                    TrisTopMoverRequest.setFilter(this.chainMapping.getFields());
                    serviceRequestMapper.put(ricCode.get(Constant.CHAIN_TYPE), TrisTopMoverRequest);
                }
                TopMoverService.logger.debug("tristopmoverRequestMap is " + serviceRequestMapper);
                TopMoverService.logger.debug("convert TopMoverRequest to TrisTopMoverRequest list successfully......");
                return serviceRequestMapper;
            } else {
                TopMoverService.logger.info(" Ric Codes is empty......");
                return null;
            }
        } else {
            TopMoverService.logger.info(" ProductType or Exchange is empty......");
            return null;
        }
    }

    @Override
    protected TopMoverResponse convertResponse(final Object validServiceResponse) throws Exception {

        TopMoverService.logger.debug("convert Response to TopMoverResponse ......");
        CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
        @SuppressWarnings("unchecked")
        Map<String, JsonArray> resultJsonArrayMapper = (Map<String, JsonArray>) validServiceResponse;
        TopMoverResponse TopMoverResponse = new TopMoverResponse();
        Object obj = ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_TOP_TEN_MOVERS_REQUEST);
        TopMoverRequest request = null;
        if (obj instanceof TopMoverRequest) {
            request = (TopMoverRequest) obj;
        }
        List<TopMoverTable> topmover = new ArrayList<TopMoverTable>();
        // if the TopNum is null, the default TopNum size is 10
        int size = 10;
        if (request != null) {
            if (request.getTopNum() != null && request.getTopNum() > 0) {
                size = request.getTopNum();
            }
        }
        String key = null;
        JsonArray value = null;
        JsonObject productJson = null;
        TopMoverProduct product = null;
        JsonObject jsonObect = null;
        TopMoverTable table = null;
        List<TopMoverProduct> products = null;
        JsonArray child = null;
        for (Map.Entry<String, JsonArray> entry : resultJsonArrayMapper.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            if (value.size() != 1) {
                TopMoverService.logger.info("The response value is not unique , the response size is " + value.size());
                TopMoverService.logger.info("The response value is " + resultJsonArrayMapper);
                throw new VendorException(ExCodeConstant.EX_CODE_INVALID_RESPONSE);
            }
            jsonObect = value.get(0).getAsJsonObject();
            table = new TopMoverTable();
            products = new ArrayList<TopMoverProduct>();
            table.setTableKey(key);
            table.setChainKey(this.mappingRule.getOrDefault(key, null));
            child = jsonObect.get("child").getAsJsonArray();
            // if the result size less than the TopNum , the TopNum size is
            // result size
            if (child != null && child.size() > 0 && child.size() < size) {
                size = child.size();
            }
            String date = "";
            String time = "";
            String symbol = "";
            String market = "";
            // transform the exchangeUpdatedTime
            DateTimeFormatter parser = ISODateTimeFormat.dateTime();
            DateTime exchangeUpdatedTime = null;
            Date exchangeUpdatedDate = null;
            for (int i = 0; i < size; i++) {
                productJson = child.get(i).getAsJsonObject();
                product = new TopMoverProduct();
                String ric = this.getJsonValue(productJson, "_item");
                boolean delay = (Boolean) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_DELAY);
                if (delay && StringUtils.isNotBlank(ric) && ric.startsWith(SymbolConstant.SLASH)) {
                    ric = ric.replaceFirst(SymbolConstant.SLASH, "");
                }
                product.setRic(ric);
                symbol = this.getJsonValue(productJson, "MNEMONIC");
                product.setSymbol(symbol);
                // query the product code from predSearch
                List<String> symbols = new ArrayList<String>();
                if (StringUtils.isNotBlank(symbol)) {
                    symbols.add(symbol);
                    List<PredSrchResponse> responses = this.predSrchService.precSrchNoValidation(symbols, header);
                    if (!responses.isEmpty()) {
                        PredSrchResponse predSrchResp = responses.get(0);
                        for (ProdAltNumSeg prodAltNumSeg : predSrchResp.getProdAltNumSegs()) {
                            if ("M".equals(prodAltNumSeg.getProdCdeAltClassCde()) && symbol.equals(prodAltNumSeg.getProdAltNum())
                                && market.equals(predSrchResp.getMarket())) {
                                product.setProdAltNumSegs(predSrchResp.getProdAltNumSegs());
                                break;
                            }
                        }
                    }
                }
                if (request != null) {
                    market = StringUtil.isInValid(request.getMarket()) ? "" : request.getMarket();
                    product.setMarket(market);
                    product.setProductType(StringUtil.isInValid(request.getProductType()) ? "" : request.getProductType());
                    product.setDelay(request.getDelay());
                }
                product.setName(this.getJsonValue(productJson, "DSPLY_NAME"));
                product.setPrice(BigDecimalUtil.fromString(JsonUtil.getAsString(productJson, "TRDPRC_1")));
                product.setChange(BigDecimalUtil.fromString(JsonUtil.getAsString(productJson, "NETCHNG_1")));
                product.setChangePercent(BigDecimalUtil.fromString(JsonUtil.getAsString(productJson, "PCTCHNG")));
                product.setCurrency(this.getJsonValue(productJson, "CURRENCY"));
                // product.setTurnover(this.stringToNum(productJson,
                // "TURNOVER"));
                date = this.getJsonValue(productJson, "TRADE_DATE");
                time = this.getJsonValue(productJson, "TRDTIM_1");
                String exchangeUpdatedTimeStr = "";
                if (StringUtils.isNoneBlank(date, time)) {
                    exchangeUpdatedTimeStr = new StringBuilder(date.toString()).append("T").append(time.toString()).toString();
                    try {
                        exchangeUpdatedTime = parser.parseDateTime(exchangeUpdatedTimeStr);
                        if (exchangeUpdatedTime != null) {
                            exchangeUpdatedDate = new Date(exchangeUpdatedTime.getMillis());
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        TopMoverService.logger.error("transform the exchangeUpdatedTime error......");
                        TopMoverService.logger.error("exchangeUpdatedTime : {} ", exchangeUpdatedTimeStr);
                        throw new ApplicationException(ExCodeConstant.EX_CODE_INVALID_RESPONSE, e);
                    }
                }
                product.setExchangeUpdatedTime(exchangeUpdatedDate);
                product.setVolume(BigDecimalUtil.fromString(JsonUtil.getAsString(productJson, "ACVOL_1")));
                products.add(product);
            }
            table.setProducts(products);
            topmover.add(table);
        }
        TopMoverResponse.setTopMovers(topmover);
        TopMoverService.logger.debug("TopMoverResponse is :" + TopMoverResponse);
        TopMoverService.logger.debug("convert Response to TopMoverResponse successfully ......");
        return TopMoverResponse;
    }

    @Override
    protected Object execute(final Object serviceRequest) throws Exception {

        Map<String, String> serviceResponseMapper = new HashMap<>();
        String key = null;
        TrisTopMoverRequest request = null;
        if (serviceRequest != null && (serviceRequest instanceof Map)) {
            Map<String, TrisTopMoverRequest> serviceRequestMapper = (Map<String, TrisTopMoverRequest>) serviceRequest;

            for (Map.Entry<String, TrisTopMoverRequest> requestMapper : serviceRequestMapper.entrySet()) {
                key = requestMapper.getKey();
                request = requestMapper.getValue();
                try {
                    serviceResponseMapper.put(key,
                        this.httpClientHelper.doPost(this.trisProps.getTrisUrl(), JsonUtil.toJson(request), null));
                } catch (Exception e) {
                    TopMoverService.logger.error("Access Tris encounter error", e);
                    throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_TRIS_ERROR, e);
                }
            }
        }
        return serviceResponseMapper;
    }

    @Override
    protected Object validateServiceResponse(final Object serviceResponse) throws Exception {

        Map<String, JsonArray> validServiceResponseMapper = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, String> serviceResponseMapper = (Map<String, String>) serviceResponse;
        String key = null;
        String responseValue = null;
        JsonObject responseJson = null;
        JsonArray jsonArray = null;
        for (Map.Entry<String, String> responseMapper : serviceResponseMapper.entrySet()) {
            key = responseMapper.getKey();
            responseValue = responseMapper.getValue();
            // Convert response into JsonObject
            responseJson = JsonUtil.getAsJsonObject(responseValue);
            // Determine whether the request was successful
            if (responseJson == null || (!"0".equals(responseJson.get("stsCode").toString()))) {
                TopMoverService.logger.error("The response is error ......");
                TopMoverService.logger.error("the Chain Type is : " + key + " , the response is : " + responseJson);
                throw new VendorException(ExCodeConstant.EX_CODE_INVALID_RESPONSE);
            } else {
                jsonArray = validateEachServiceResponse(responseMapper.getValue());
            }
            validServiceResponseMapper.put(key, jsonArray);
        }
        return validServiceResponseMapper;
    }


    private List<Map<String, String>> getRicCode(final String chainName) {
        if (this.chainMapping != null && StringUtil.isValid(this.chainMapping.getMappingRule())) {
            TopMoverService.logger.debug("Chain Mapping Rule is " + this.chainMapping.getMappingRule());
            if (StringUtil.isValid(chainName)) {
                // get the chainType and ric
                JsonParser parser = new JsonParser();
                JsonObject jsonObject = parser.parse(this.chainMapping.getMappingRule()).getAsJsonObject();
                if (jsonObject != null && jsonObject.get(chainName) != null) {
                    JsonArray jsonArray = jsonObject.get(chainName).getAsJsonArray();
                    if (jsonArray.size() < 1) {
                        throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_PARAMETER_NOT_SUPPORT);
                    }
                    List<Map<String, String>> chainList = new ArrayList<Map<String, String>>();
                    Iterator<JsonElement> it = jsonArray.iterator();
                    JsonObject chainItem = null;
                    Map<String, String> map = null;
                    while (it.hasNext()) {
                        chainItem = it.next().getAsJsonObject();
                        map = new HashMap<String, String>();
                        map.put(Constant.CHAIN_TYPE, chainItem.get(Constant.CHAIN_TYPE).toString()
                            .replaceAll(SymbolConstant.SYMBOL_DOUBLE_QUOTATION_MARKS, ""));
                        map.put(Constant.CHAIN_CODE, chainItem.get(Constant.CHAIN_CODE).toString()
                            .replaceAll(SymbolConstant.SYMBOL_DOUBLE_QUOTATION_MARKS, ""));
                        map.put(Constant.RIC,
                            chainItem.get(Constant.RIC).toString().replaceAll(SymbolConstant.SYMBOL_DOUBLE_QUOTATION_MARKS, ""));
                        chainList.add(map);
                        // set Mapping Rule value
                        if (!this.mappingRule.containsKey(chainItem.get(Constant.CHAIN_TYPE).toString()
                            .replaceAll(SymbolConstant.SYMBOL_DOUBLE_QUOTATION_MARKS, ""))) {
                            this.mappingRule.put(
                                chainItem.get(Constant.CHAIN_TYPE).toString()
                                    .replaceAll(SymbolConstant.SYMBOL_DOUBLE_QUOTATION_MARKS, ""),
                                chainItem.get(Constant.CHAIN_CODE).toString()
                                    .replaceAll(SymbolConstant.SYMBOL_DOUBLE_QUOTATION_MARKS, ""));
                        }
                    }
                    TopMoverService.logger.debug(chainName + " chain type , ric and chain code is " + chainList);
                    return chainList;
                } else {
                    TopMoverService.logger
                        .error("The chain name can not find from chain mapping rule , chain name is " + chainName);
                    throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_PARAMETER_ERROR);
                }
            } else {
                TopMoverService.logger.error("The chain name is empty....");
                throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_PARAMETER_ERROR);
            }

        } else {
            TopMoverService.logger.error("The chain mapping rule is empty ....");
            throw new IllegalInitializationException(ExCodeConstant.EX_CODE_ILLEGAL_CONFIGURATION);
        }
    }

    private JsonArray validateEachServiceResponse(final String serviceResponse) {
        JsonObject respJsonObj = JsonUtil.getAsJsonObject(serviceResponse);
        if (respJsonObj == null) {
            TopMoverService.logger.error("Invalid response: " + serviceResponse);
            throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
        }
        String statusKey = this.trisProps.getResponseStatusCodeKey();
        String status = JsonUtil.getAsString(respJsonObj, statusKey);
        if (!this.trisProps.isCorrectResponseStatus(status)) {
            TopMoverService.logger.error("Invalid response, status is incorrect: " + serviceResponse);
            throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
        }
        JsonArray resultJsonArray = JsonUtil.getAsJsonArray(respJsonObj, "result");
        if (resultJsonArray == null || resultJsonArray.size() <= 0) {
            TopMoverService.logger.error("Invalid response, result array is empty: " + serviceResponse);
            throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
        }
        JsonObject resultJsonObj = null;
        for (int i = 0; i < resultJsonArray.size(); i++) {
            resultJsonObj = resultJsonArray.get(i).getAsJsonObject();
            status = JsonUtil.getAsString(resultJsonObj, statusKey);
            if (!this.trisProps.isCorrectResponseStatus(status)) {
                TopMoverService.logger.error("Invalid response, result status is incorrect: " + serviceResponse);
                throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
            }
        }
        return resultJsonArray;
    }

    private String getJsonValue(final JsonObject json, final String key) {
        if (null != json.get(key)) {
            return StringUtil.isValidResp(json.get(key).toString().replaceAll(SymbolConstant.SYMBOL_DOUBLE_QUOTATION_MARKS, ""))
                ? null
                : json.get(key).toString().replaceAll(SymbolConstant.SYMBOL_DOUBLE_QUOTATION_MARKS, "").replaceAll("\\/d", "");
        } else {
            return null;
        }
    }

}
