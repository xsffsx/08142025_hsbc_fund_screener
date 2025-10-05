/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.vendor.midfs.RequestPattern;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.TrisTokenService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.MIDFSProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.TrisProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.QuoteHistoryRepository;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.properties.HcbsHealthcheckProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.service.TrisQuoteServiceRequest;
import com.hhhh.group.secwealth.mktdata.starter.healthcheck.bean.Status;
import com.hhhh.group.secwealth.mktdata.starter.healthcheck.service.Healthcheck;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
@ConditionalOnProperty(value = "service.quotes.OES.injectEnabled")
public class HcbsHealthCheckService implements Healthcheck {

    private static final Logger logger = LoggerFactory.getLogger(HcbsHealthCheckService.class);

    private static final String HEALTHCHECK_MIDFS = "MIDFS";

    private static final String HEALTHCHECK_TRIS = "TRIS";

    private static final String HEALTHCHECK_DB = "DB";

    private static final String VENDOR_API = "StockPortfolio";

    private static final String DOWN_LINE = "_";

    private static final String HEADER = "header";

    @Autowired
    private HttpClientHelper httpClientHelper;

    @Autowired
    private MIDFSProperties midfsProperties;

    @Autowired
    private TrisProperties trisProps;

    @Autowired
    private HcbsHealthcheckProperties healthCheckProperties;

    @Autowired
    private QuoteHistoryRepository quoteHistoryDao;

    @Autowired
    private TrisTokenService trisTokenService;

    @Override
    public List<Status> getStatus() {
        List<Status> statusList = new ArrayList<>();
        statusList.add(DbCheck());
        statusList.add(TrisCheck());
        statusList.add(MidfsCheck());
        return statusList;
    }

    public Status DbCheck() {
        long startTime = System.currentTimeMillis();
        try {
            Integer num = this.quoteHistoryDao.dbCheck();
            HcbsHealthCheckService.logger.warn("HcbsHealthCheckService", "dbHealthCheckMsg: " + num);
            long endTime = System.currentTimeMillis();
            return new Status(Status.SUCCESS, HcbsHealthCheckService.HEALTHCHECK_DB, (endTime - startTime));
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            return new Status(Status.FAILURE, HcbsHealthCheckService.HEALTHCHECK_DB, (endTime - startTime));
        }
    }

    public Status TrisCheck() {
        long startTime = System.currentTimeMillis();
        try {
            TrisQuoteServiceRequest req = new TrisQuoteServiceRequest();
            req.setService(this.healthCheckProperties.getService());
            CommonRequestHeader commonHeader = this.healthCheckProperties.getHeader();
            String site = commonHeader.getCountryCode() + HcbsHealthCheckService.DOWN_LINE + commonHeader.getGroupMember();
            String closure = this.trisProps.getTrisClosure(site);
            req.setClosure(closure);
            String appCode = this.trisProps.getTrisTokenAppCode(site);
            String timezone = this.trisProps.getTrisTokenTimezone(site);
            String token = this.trisTokenService.getEncryptedTrisToken(site, commonHeader, appCode, closure, timezone);
            req.setToken(token);
            req.addFilter(this.healthCheckProperties.getFilter());
            req.addItem(this.healthCheckProperties.getItem());
            String serviceResponse = this.httpClientHelper.doPost(this.trisProps.getTrisUrl(), JsonUtil.toJson(req), null);
            JsonObject respJsonObj = JsonUtil.getAsJsonObject(serviceResponse);
            if (respJsonObj == null) {
                HcbsHealthCheckService.logger.error("Invalid response: " + serviceResponse);
                throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
            }
            String statusKey = this.trisProps.getResponseStatusCodeKey();
            String status = JsonUtil.getAsString(respJsonObj, statusKey);
            if (!this.trisProps.isCorrectResponseStatus(status)) {
                HcbsHealthCheckService.logger.error("Invalid response, status is incorrect: " + serviceResponse);
                throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
            }
            JsonArray resultJsonArray = JsonUtil.getAsJsonArray(respJsonObj, "result");
            if (resultJsonArray == null || resultJsonArray.size() <= 0) {
                HcbsHealthCheckService.logger.error("Invalid response, result array is empty: " + serviceResponse);
                throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
            }
            long endTime = System.currentTimeMillis();
            return new Status(Status.SUCCESS, HcbsHealthCheckService.HEALTHCHECK_TRIS, (endTime - startTime));
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            return new Status(Status.FAILURE, HcbsHealthCheckService.HEALTHCHECK_TRIS, (endTime - startTime));
        }
    }

    public Status MidfsCheck() {
        long startTime = System.currentTimeMillis();
        try {
            String commandId = this.midfsProperties.getCommandId(HcbsHealthCheckService.VENDOR_API, "en_US", false);
            RequestPattern requestPattern = this.midfsProperties.getRequestPattern(HcbsHealthCheckService.VENDOR_API);
            String dummyParameter = requestPattern.getDummyParameter();
            String requestParameter = dummyParameter.replace(Constant.REQUEST_PARAMETER_COMMAND_ID, commandId)
                .replace(Constant.REQUEST_PARAMETER_QUOTE_STOCK, this.healthCheckProperties.getMidfsProdNum());
            String serviceResponse = this.httpClientHelper.doGet(requestPattern.getUrl(), requestParameter, null);
            JsonNode jsonNode = JsonUtil.readTree(String.valueOf(serviceResponse));
            String status = jsonNode.findPath(this.midfsProperties.getResponseStatusKey()).asText();
            if (!this.midfsProperties.isCorrectResponseStatus(status)) {
                String errorMessage = this.midfsProperties.getResponseMessage(status);
                HcbsHealthCheckService.logger.error(errorMessage);
                throw new Exception(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
            }
            long endTime = System.currentTimeMillis();
            return new Status(Status.SUCCESS, HcbsHealthCheckService.HEALTHCHECK_MIDFS, (endTime - startTime));
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            return new Status(Status.FAILURE, HcbsHealthCheckService.HEALTHCHECK_MIDFS, (endTime - startTime));
        }
    }

}
