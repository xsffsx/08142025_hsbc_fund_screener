package com.hhhh.group.secwealth.mktdata.api.equity.chart.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.authentication.LabciTokenService;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.constant.ChartConstant;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.request.ChartRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.request.service.LabciChartRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.ChartResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.labci.LabciChartResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.labci.LabciResult;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.pre.Result;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.utils.ChartDateUtils;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.request.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.PredSrchProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.token.LabciToken;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;

@Service
@ConditionalOnProperty(value = "service.chart.Labci.injectEnabled")
public class LabciChartService extends AbstractBaseService<ChartRequest, ChartResponse, CommonRequestHeader> {

    private static final Logger logger = LoggerFactory.getLogger(LabciChartService.class);
    private static final String APP_CODE = "MDS_HASE";
    private static final String PRODUCT_TYPE_INDEX = "INDEX";
    private static final String PRODUCT_TYPE_SEC = "SEC";
    public static final String SPOT = ".";

    @Autowired
    private PredSrchProperties predSrchProps;

    @Autowired
    private HttpClientHelper httpClientHelper;

    @Autowired
    private LabciProperties labciProperties;

    @Autowired
    private LabciTokenService labciTokenService;

    @Autowired
    private PredSrchService predSrchService;

//    @Autowired
//    private OatProperties oatProperties;

    @Override
    protected Object convertRequest(final ChartRequest request, final CommonRequestHeader header) throws Exception {

        String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
        String timezone = this.labciProperties.getLabciTokenTimezone(site);

        String customerId = header.getCustomerId();
        String channelId = header.getChannelId();

        String market = request.getMarket();
        String[] symbol = request.getSymbol();
        String startTm = request.getStartTime();
        String endTm = request.getEndTime();
        String productType = request.getProductType();
        Number period = request.getPeriod();
        String intType = request.getIntType();
        Number intCnt = request.getIntCnt();
        String[] filters = request.getFilters();
        // Boolean timeZoneRequired = request.getTimeZoneRequired();

        if ((StringUtil.isInValid(startTm) && StringUtil.isInValid(endTm) && period == null) || intCnt == null
                || !(PRODUCT_TYPE_INDEX.equals(productType) || PRODUCT_TYPE_SEC.equals(productType))) {
            logger.error("Invalid request: {}", request);
            throw new VendorException(ExCodeConstant.EX_CODE_REQUEST_PARAMETER_ERROR);
        }

        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_MARKET, market);
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_PRODUCTTYPE, productType);
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_SYMBOL, symbol);

        // get ric code by symbol
        List<String> symbols = Arrays.asList(symbol);
        Map<String, String> ricCodesMappings = getRicCodeBySymbol(header, symbols, productType,market);
        String encryptLabciToken = generatorToken(site, customerId, channelId, timezone);

        Map<String, LabciChartRequest> requestMap = new HashMap<>();
        for (Entry<String, String> map : ricCodesMappings.entrySet()) {
            LabciChartRequest labciChartRequest = new LabciChartRequest();
            labciChartRequest.setToken(encryptLabciToken);
            labciChartRequest.setFilter(filters);
            // labciChartRequest.setTimeZone(timeZoneRequired);
            labciChartRequest.setIntCnt(intCnt);
            labciChartRequest.setIntType(intType);
            labciChartRequest.setPer(period);
            if (StringUtil.isValid(startTm)) {
                String requestStartTime = ChartDateUtils.format(
                        ChartDateUtils.parse(startTm, Constant.DATE_FORMAT_FOT_LABCI), Constant.DATE_FORMAT_FOT_LABCI);
                labciChartRequest.setStartTm(requestStartTime);
            }
            if (StringUtil.isValid(endTm)) {
                String requestEndTime = ChartDateUtils.format(
                        ChartDateUtils.parse(endTm, Constant.DATE_FORMAT_FOT_LABCI), Constant.DATE_FORMAT_FOT_LABCI);
                labciChartRequest.setEndTm(requestEndTime);
            }
            labciChartRequest.setItem(new String[]{map.getKey()});
            requestMap.put(map.getValue(), labciChartRequest);
        }

        return requestMap;
    }

    private Map<String, String> getRicCodeBySymbol(CommonRequestHeader header, List<String> symbols,
                                                   String productType,String market) {

        PredSrchRequest predSrchRequest = new PredSrchRequest();
        predSrchRequest.setKeyword(symbols.toArray(new String[symbols.size()]));
        String[] preciseSrchFields = new String[]{productType};
        predSrchRequest.setAssetClasses(preciseSrchFields);
        predSrchRequest.setSearchFields(this.predSrchProps.getRequestParams().getSearchFields());
        predSrchRequest.setSortingFields(this.predSrchProps.getRequestParams().getSearchFields());
        predSrchRequest.setTopNum(String.valueOf(symbols.size()));
        predSrchRequest.setPreciseSrch(this.predSrchProps.getRequestParams().isPreciseSrch());
        predSrchRequest.setMarket(market);
        List<PredSrchResponse> predSrchResponses = predSrchService.precSrch(predSrchRequest, header);
        Map<String, String> ricCodesMappings = new HashMap<>();
        for (int i = 0; i < predSrchResponses.size(); i++) {
            PredSrchResponse predSrchResponse = predSrchResponses.get(i);
            for (ProdAltNumSeg prodAltNumSeg : predSrchResponse.getProdAltNumSegs()) {
                // TODO
//                if (PRODUCT_TYPE_INDEX.equals(productType)) {
//                    if (Constant.PROD_CDE_ALT_CLASS_CODE_M.equals(prodAltNumSeg.getProdCdeAltClassCde())) {
//                        ricCodesMappings.put(SPOT + prodAltNumSeg.getProdAltNum(), predSrchResponse.getProductName());
//                    }
//                } else {
//                }
                if (Constant.PROD_CDE_ALT_CLASS_CODE_T.equals(prodAltNumSeg.getProdCdeAltClassCde())) {
                    ricCodesMappings.put(prodAltNumSeg.getProdAltNum(), predSrchResponse.getProductName());
                }
            }
        }
        return ricCodesMappings;
    }

    private String generatorToken(String site, String customerId, String channelId, String timezone) {
        LabciToken token = new LabciToken();
        token.setAppCode(APP_CODE);
        token.setCustomerId(customerId);
        String timeStamp = DateUtil.current(DateUtil.DATE_DAY_PATTERN_yyyyMMddHHmmss, TimeZone.getTimeZone(timezone));
        token.setTimeStamp(timeStamp);
        token.setChannelId(channelId);
        return labciTokenService.encryptLabciToken(site, token);
    }

    /**
     * Get data from TRIS by HTTP call Put result into map<symbol, jsonResult>
     */
    @Override
    protected Object execute(final Object serviceRequest) {
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            @SuppressWarnings("unchecked")
            Map<String, LabciChartRequest> labciChartRequest = (Map<String, LabciChartRequest>) serviceRequest;
            for (Map.Entry<String, LabciChartRequest> map : labciChartRequest.entrySet()) {
                String result;
//                if (oatProperties.getLabciFlag()) {
//                    result = "{\"stsCode\":\"000\",\"stsTxt\":\"OK\",\"result\":[{\"item\":\"000001.SZ\",\"field\":[\"DATE\",\"OPEN\",\"HIGH\",\"LOW\",\"CLOSE\",\"VOLUME\",\"TURNOVER\"],\"data\":[[\"2019-08-31T00:00:00.000Z\",14.060,15.220,13.030,14.160,2993290000,43313600000.000]]}]}\n";
//                } else {
                    result = this.httpClientHelper.doPost(labciProperties.getProxyName(), labciProperties.getChartDataUrl(),
                            gson.toJson(map.getValue()), null);
//                }
                resultMap.put(map.getKey(), result);
            }
        } catch (Exception e) {
            LabciChartService.logger.error("Access hang seng encounter error", e);
            throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_LABCI_ERROR, e);
        }
        return resultMap;
    }

    /**
     * validate json result from tris
     */
    @Override
    protected Object validateServiceResponse(final Object serviceResponse) throws Exception {
        if (null == serviceResponse) {
            LabciChartService.logger.error("Invalid response: {}", serviceResponse);
            throw new VendorException(ExCodeConstant.EX_CODE_LABCI_INVALID_RESPONSE);
        }
        return serviceResponse;
    }

    @Override
    protected ChartResponse convertResponse(final Object validServiceResponse) throws Exception {
        String market = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_MARKET).toString());
        String productType = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_PRODUCTTYPE));
        ChartResponse response = new ChartResponse();
        List<Result> preResultList = new ArrayList<Result>();
        @SuppressWarnings("unchecked")
        Map<String, String> responseMap = (Map<String, String>) validServiceResponse;
        for (Map.Entry<String, String> map : responseMap.entrySet()) {
            String reslutString = map.getValue();
            LabciChartResponse labciChartResponse = JsonUtil.readValue(reslutString, LabciChartResponse.class);
            String stsCode = "";
            if (null != labciChartResponse) {
                stsCode = labciChartResponse.getStsCode();
            }
            if (!labciProperties.isCorrectResponseStatus(stsCode)) {
                String errReasonMsg = ChartConstant.LABCI_PORTAL_ERROR_CODE_INVALID_REQUEST_MAPPING.get(stsCode);
                if (StringUtil.isValid(errReasonMsg)) {
                    LabciChartService.logger.error("The error code: {}, and the error reason: {}", stsCode,
                            errReasonMsg);
                    throw new VendorException(ExCodeConstant.EX_CODE_LABCI_PORTAL_INVALID_REQUEST);
                }
                errReasonMsg = ChartConstant.LABCI_PORTAL_ERROR_CODE_INVALID_TOKEN_MAPPING.get(stsCode);
                if (StringUtil.isValid(errReasonMsg)) {
                    LabciChartService.logger.error("The error code: {}, and the error reason: {}", stsCode,
                            errReasonMsg);
                    throw new VendorException(ExCodeConstant.EX_CODE_LABCI_PORTAL_INVALID_TOKEN);
                }
            }
            if (null != labciChartResponse && null != labciChartResponse.getResult()
                    && labciChartResponse.getResult().length > 0) {
                LabciResult[] results = labciChartResponse.getResult();
                for (LabciResult result : results) {
                    String[] symbols = new String[]{result.getItem()};
                    Result preResult = new Result();
                    preResult.setMarket(market);
                    preResult.setProductType(productType);
                    preResult.setSymbol(symbols);
                    preResult.setDisplayName(map.getKey());
                    preResult.setData(result.getData());
                    // preResult.setTimeZone(result.getTimeZone());
                    preResult.setFields(result.getField());
                    preResultList.add(preResult);
                }
            }
        }
        response.setResult(preResultList);
        return response;
    }

}
