package com.hhhh.group.secwealth.mktdata.api.equity.chart.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import com.google.gson.JsonObject;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.request.ChartRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.request.ProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.request.service.TrisChartRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.ChartResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.helper.SymbolConverter;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.pre.Result;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.svc.ChartSvcResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.svc.SvcResult;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.TrisTokenService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.ApplicationProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.TrisProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

@ConfigurationProperties(prefix = "chart-data")
@Service
@ConditionalOnProperty(value = "service.chart.tris.injectEnabled")
public class ChartService extends AbstractBaseService<ChartRequest, ChartResponse, CommonRequestHeader> {

    private static final Logger logger = LoggerFactory.getLogger(ChartService.class);

    @Autowired
    private TrisTokenService trisTokenService;

    @Autowired
    private HttpClientHelper httpClientHelper;

    @Autowired
    private TrisProperties trisProps;

    @Autowired
    private PredSrchService predSrchService;

    @Autowired
    private ApplicationProperties appProps;

    private final static String YTD = "YTD";

    private final static String QTD = "QTD";

    private final static String MTD = "MTD";

    private final static String AltNumType = "altNumType";

    /**
     * Value Description 0 1D - Intra-day (5 mins) 2 5D - 5 days (15 mins) 5 1M
     * - 1 Month (daily) 7 3M - 3 Months (daily) 8 6M - 6 Months (daily) 11 1Y
     * - 1 Year (daily) 13 3Y - 3 Years (daily)
     */
    public static final Map<Number, String> PERIODS_MAPPING = new HashMap<Number, String>() {
        {
            put(0, "1D");
            put(1, "2D");
            put(2, "5D");
            put(3, "10D");
            put(4, "MTD");
            put(5, "1M");
            put(6, "2M");
            put(7, "3M");
            put(8, "6M");
            put(9, "QTD");
            put(10, "YTD");
            put(11, "1Y");
            put(12, "2Y");
            put(13, "3Y");
            put(14, "4Y");
            put(15, "5Y");
            put(16, "10Y");
        }
    };

    @Override
    protected Object convertRequest(final ChartRequest request, final CommonRequestHeader header) throws Exception {
        if (!request.getDelay()) {
            ChartService.logger.error("Delay type not support, Dealy = " + request.getDelay());
            throw new ApplicationException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
        }

        /**
         * put request parameters into thread local
         */
        String market = request.getMarket();
        Boolean delay = request.getDelay();
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_MARKET, market);
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_DELAY, delay);
        /**
         * Get m code and T code into List<SymbolConverter> USE SG UT's
         * predictive search
         * 
         */
        List<ProductKey> productKeyList = request.getProductKeys();
        List<SymbolConverter> scs = new ArrayList<SymbolConverter>();
        List<PredSrchResponse> responses = null;
        Map<String, String> altNumType = new HashMap<>();
        if (!ListUtils.isEmpty(productKeyList)) {
            List<String> symbols = new ArrayList<>();
            for (ProductKey productKey : productKeyList) {
                if (Constant.PROD_CDE_ALT_CLASS_CODE_M.equals(productKey.getProdCdeAltClassCde())) {
                    symbols.add(productKey.getProdAltNum());
                } else if (Constant.PROD_CDE_ALT_CLASS_CODE_R.equals(productKey.getProdCdeAltClassCde())) {
                    SymbolConverter sc = new SymbolConverter();
                    sc.setRcode(productKey.getProdAltNum());
                    scs.add(sc);
                    altNumType.put(productKey.getProdAltNum(), productKey.getProductType());
                }
            }
            if (!ListUtils.isEmpty(symbols)) {
                responses = this.predSrchService.precSrch(symbols, header);
            }
        }
        if (null != responses) {
            for (PredSrchResponse response : responses) {
                SymbolConverter sc = new SymbolConverter();
                for (ProdAltNumSeg prodAltNumSeg : response.getProdAltNumSegs()) {
                    if (Constant.PROD_CDE_ALT_CLASS_CODE_T.equals(prodAltNumSeg.getProdCdeAltClassCde())) {
                        sc.setTcode(prodAltNumSeg.getProdAltNum());
                    }
                    if (Constant.PROD_CDE_ALT_CLASS_CODE_M.equals(prodAltNumSeg.getProdCdeAltClassCde())) {
                        sc.setMcode(prodAltNumSeg.getProdAltNum());
                    }
                }
                scs.add(sc);
            }
            logger.info("symbols list {}", JsonUtil.toJson(scs));
        }
        /**
         * prepare common request parameter for call TRIS
         */
        ArgsHolder.putArgs(AltNumType, altNumType);
        String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
        String closure = this.trisProps.getTrisClosure(site);
        String appCode = this.trisProps.getTrisTokenAppCode(site);
        String timezone = this.trisProps.getTrisTokenTimezone(site);
        String token = this.trisTokenService.getEncryptedTrisToken(site, header, appCode, closure, timezone);
        String service = this.trisProps.getTrisService(site, delay);

        /**
         * create map<SymbolConverter, TrisChartrequest>
         */
        Map<SymbolConverter, TrisChartRequest> requestMap = new HashMap<SymbolConverter, TrisChartRequest>();
        for (SymbolConverter sc : scs) {
            String symbol = null != sc.getTcode() ? sc.getTcode() : sc.getRcode();
            TrisChartRequest trisChartrequest = getTrisChartrequest(closure, service, token, symbol, request);
            requestMap.put(sc, trisChartrequest);
        }
        return requestMap;
    }

    /**
     * 
     * Generate Tris request
     * 
     * @param closure
     * @param service
     * @param token
     * @param code
     * @param request
     * @return
     */
    private TrisChartRequest getTrisChartrequest(String closure, String service, String token, String code, ChartRequest request) {
        TrisChartRequest trisChartrequest = new TrisChartRequest();
        calculateTradeTime(request);
        trisChartrequest.setClosure(closure);
        trisChartrequest.setService(service);
        trisChartrequest.setToken(token);
        trisChartrequest.addItem(code);
        trisChartrequest.setIntCnt(request.getIntCnt());
        trisChartrequest.setIntType(request.getIntType());
        if (null != request.getFilters() && request.getFilters().length > 0) {
            trisChartrequest.addFilter(Arrays.asList(request.getFilters()));
        }
        trisChartrequest.setStartTm(request.getStartTime());
        trisChartrequest.setEndTm(request.getEndTime());
        trisChartrequest.setMarket(request.getMarket());
        trisChartrequest.setPeriod(request.getPeriod());
        trisChartrequest.setMaxPts(request.getMaxPts());
        trisChartrequest.setTimeZoneRequired(request.getTimeZoneRequired());
        trisChartrequest.setDisplayName(true);
        // trisChartrequest.setSplit(request.getSplit());
        // trisChartrequest.setCurrency(request.getCurrency());
        trisChartrequest.setTimeZone(true);
        return trisChartrequest;
    }

    /**
     * calculate the trade time by period
     * 
     * @param chartRequest
     */
    private void calculateTradeTime(final ChartRequest chartRequest) {
        Number period = chartRequest.getPeriod();
        String periodDesc = PERIODS_MAPPING.get(period);
        // Set the date depends on the period
        String startTime = chartRequest.getStartTime();
        String endTime = chartRequest.getEndTime();
        SimpleDateFormat df = new SimpleDateFormat(Constant.DATE_FORMAT_ISO8601);
        Calendar cal = Calendar.getInstance();
        String formatPattern = Constant.DATE_FORMAT_ISO8601;
        TimeZone oldTimezone = TimeZone.getDefault();
        String timeZone = appProps.getSiteTimezone(Constant.DEFAULT_OPTION);
        TimeZone newTimezone = TimeZone.getTimeZone(timeZone);
        if (StringUtil.isValid(startTime) && StringUtil.isValid(endTime)) {
            startTime = chartRequest.getStartTime();
            endTime = chartRequest.getEndTime();
        }
        if (StringUtil.isValid(periodDesc)) {
            try {
                endTime = df.format(cal.getTime());
                endTime = DateUtil.dateByTimeZone(endTime, formatPattern, formatPattern, oldTimezone, newTimezone);
                // calculate YTD/MTD/QTD satrt time
                if (periodDesc.equals(YTD)) {
                    cal.set(cal.get(Calendar.YEAR), 1 - 1, 1);
                    startTime = df.format(cal.getTime());
                    startTime = DateUtil.dateByTimeZone(startTime, formatPattern, formatPattern, oldTimezone, newTimezone);
                } else if (periodDesc.equals(MTD)) {
                    cal.add(Calendar.MONTH, 0);
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    startTime = df.format(cal.getTime());
                    startTime = DateUtil.dateByTimeZone(startTime, formatPattern, formatPattern, oldTimezone, newTimezone);
                } else if (periodDesc.equals(QTD)) {
                    cal.set(Calendar.MONTH, ((int) cal.get(Calendar.MONTH) / 3) * 3);
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    startTime = df.format(cal.getTime());
                    startTime = DateUtil.dateByTimeZone(startTime, formatPattern, formatPattern, oldTimezone, newTimezone);
                } else {
                    String dateType = periodDesc.substring(periodDesc.length() - 1).toUpperCase();
                    int number = Integer.parseInt(periodDesc.substring(0, periodDesc.length() - 1));
                    if (Constant.YEAR_PERIOD.equals(dateType)) {
                        cal.add(Calendar.YEAR, -number);
                    } else if (Constant.MONTH_PERIOD.equals(dateType)) {
                        cal.add(Calendar.MONTH, -number);
                    } else if (Constant.DAY_PERIOD.equals(dateType)) {
                        ArgsHolder.putArgs(Constant.TRADE_DAY_COUNT, number);
                        cal.add(Calendar.DATE, -(number + 10));
                    } else {
                        ChartService.logger.error("period is invalid, period: " + periodDesc);
                        throw new ApplicationException(ExCodeConstant.EX_CODE_INPUT_PARAMETER_INVALID);
                    }
                    String dateStr = df.format(cal.getTime());
                    startTime = DateUtil.dateByTimeZone(dateStr, formatPattern, formatPattern, oldTimezone, newTimezone);
                }
            } catch (Exception e) {
                ChartService.logger.error("InputParameterInvalid, period: " + periodDesc + ", timeZone: " + timeZone);
                throw new ApplicationException(ExCodeConstant.EX_CODE_INPUT_PARAMETER_INVALID);
            }
        }
        ChartService.logger.info("startTime: " + startTime + ", endTime: " + endTime);
        chartRequest.setStartTime(startTime);
        chartRequest.setEndTime(endTime);
    }


    /**
     * Get data from TRIS by HTTP call Put result into map<symbol, jsonResult>
     */
    @Override
    protected Object execute(final Object serviceRequest) {
        Map<String, String> resultMap = new HashMap<String, String>();
        Map<SymbolConverter, TrisChartRequest> requestMap = (Map<SymbolConverter, TrisChartRequest>) serviceRequest;
        for (Map.Entry<SymbolConverter, TrisChartRequest> map : requestMap.entrySet()) {
            try {
                resultMap.put(null != map.getKey().getTcode() ? map.getKey().getTcode() : map.getKey().getRcode(),
                    this.httpClientHelper.doPost(this.trisProps.getCharDataUrl(), JsonUtil.toJson(map.getValue()), null));
            } catch (Exception e) {
                ChartService.logger.error("Access Tris encounter error", e);
                throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_TRIS_ERROR, e);
            }
        }
        return resultMap;
    }

    /**
     * validate json result from tris
     */
    protected Object validateServiceResponse(final Object serviceResponse) throws Exception {
        if (null == serviceResponse) {
            ChartService.logger.error("Invalid response: {}", serviceResponse);
            throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
        }
        Map<String, String> resultMap = (Map<String, String>) serviceResponse;
        if (resultMap.isEmpty()) {
            ChartService.logger.error("Invalid response from TRIS " + resultMap.toString());
            throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
        }

        for (Map.Entry<String, String> map : resultMap.entrySet()) {
            if (StringUtil.isValid(map.getValue())) {
                JsonObject respJsonObj = JsonUtil.getAsJsonObject(map.getValue().toString());
                String stsCode = JsonUtil.getAsString(respJsonObj, "stsCode");
                if (!Constant.ZERO.equals(stsCode)) {
                    ChartService.logger.error("Invalid response, symbol is {}, stsCode is incorrect: {}", map.getKey(), map.getValue());
                    throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
                }
            }
        }
        return serviceResponse;
    }

    @Override
    protected ChartResponse convertResponse(final Object validServiceResponse) throws Exception {
        Map<String, String> resultMap = (Map<String, String>) validServiceResponse;
        String market = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_MARKET));
        ChartResponse response = new ChartResponse();
        List<Result> preResultList = new ArrayList<Result>();
        Map<String, String> altTypeMap = (Map<String, String>) ArgsHolder.getArgs(AltNumType);
            for (Map.Entry<String, String> map : resultMap.entrySet()) {
                if (StringUtil.isValid(map.getValue())) {
                    String[] symbols = new String[] {map.getKey()};
                    ChartSvcResponse svcResponse = JsonUtil.readValue(map.getValue(), ChartSvcResponse.class);
                    if (null != svcResponse && null != svcResponse.getResult()) {
                        SvcResult[] svcResults = svcResponse.getResult();
                        for (SvcResult svcResult : svcResults) {
                            Result preResult = new Result();
                            preResult.setMarket(market);
                            PredSrchResponse predSrchResp = this.predSrchService.localPredSrch(symbols[0]);
                            if (null != predSrchResp) {
                                preResult.setProductType(predSrchResp.getProductType());
                                preResult.setSymbol(new String[] {predSrchResp.getSymbol()});
                            } else if (altTypeMap.containsKey(symbols[0])) {
                                preResult.setProductType(altTypeMap.get(symbols[0]));
                                preResult.setSymbol(symbols);
                            }

                            Object[] data = svcResult.getData();
                            // get the gmt offset
                            BigDecimal offSet = svcResult.getTimeZone().getGmtOffset();
                            // convert to hour
                            Integer offSetTimeZone = offSet.intValue() / 60;
                            String newTimeZone = Constant.TIMEZONE;
                            // get the GMT(+/-) hour timezone
                            if (offSetTimeZone > 0) {
                                newTimeZone = Constant.TIMEZONE + "+" + offSetTimeZone;
                            } else {
                                newTimeZone = Constant.TIMEZONE + offSetTimeZone;
                            }
                            logger.info("GmtOffset is " + offSet + "newTimeZone is " + newTimeZone);
                                if (null != ArgsHolder.getArgs(Constant.TRADE_DAY_COUNT)) {
                                    int number = (int) ArgsHolder.getArgs(Constant.TRADE_DAY_COUNT);
                                    if (null != data && data.length > 0) {
                                        Set<String> dataSet = new TreeSet<>();
                                        int newCount = 0;
                                        for (int i = data.length - 1; i >= 0; i--) {
                                        List<Object> obj = (List<Object>) data[i];
                                        String tradeTime = (String) obj.get(0);
                                        // calculate the new dateTime by the tirs
                                        // response filed timezone
                                        String newTradeTime = DateUtil.dateByTimeZone(tradeTime, Constant.DATE_FORMAT_TRIS_ISO8601_V2,
                                            Constant.DATE_FORMAT_TRIS_ISO8601_V2, TimeZone.getTimeZone(Constant.TIMEZONE),
                                            TimeZone.getTimeZone(newTimeZone));
                                        obj.set(0, newTradeTime);
                                            String dataStr = data[i].toString().replace("[", "").replace("]", "");
                                            dataSet.add(dataStr.split("T")[0]);
                                            if (dataSet.size() > number) {
                                                break;
                                            } else {
                                                newCount += 1;
                                            }
                                        }
                                        data = Arrays.copyOfRange(data, (data.length - newCount), data.length);
                                    } else {
                                        logger.error(" tris return data error, data is  " + JsonUtil.writeValueAsString(data));
                                    }
                                }
                                preResult.setData(data);
                                preResult.setFields(svcResult.getField());
                                preResult.setDisplayName(svcResult.getDisplayName());
                                preResult.setTimeZone(svcResult.getTimeZone());
                                preResultList.add(preResult);
                            }
                        }
                    }
                }
            // }
        
//        catch (Exception e) {
//                ChartService.logger.error("Error: Failed to format json string to ChartSvcResponse.class", e);
//                ChartService.logger.error("Response from Tris is : {}", JsonUtil.writeValueAsString(validServiceResponse));
//                throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
//            }
        response.setResult(preResultList);
        return response;
        }

    }
