/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.TrisTokenService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.TrisProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.QuoteHistoryRepository;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.properties.EquityHealthcheckProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.service.TrisQuoteServiceRequest;
import com.hhhh.group.secwealth.mktdata.starter.healthcheck.bean.Status;
import com.hhhh.group.secwealth.mktdata.starter.healthcheck.service.Healthcheck;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.IllegalConfigurationException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
@ConditionalOnProperty(value = "service.equity.healthcheck.default")
public class EquityHealthCheckService implements Healthcheck {

    private static final Logger logger = LoggerFactory.getLogger(EquityHealthCheckService.class);

    private static final String HEALTHCHECK_TRIS = "TRIS";

    private static final String HEALTHCHECK_PREDICTIVE_SEARCH = "Predictive Search";

    private static final String HEALTHCHECK_DB = "DB";

    private static final String DOWN_LINE = "_";


    @Autowired
    private HttpClientHelper httpClientHelper;

    @Autowired
    private TrisProperties trisProps;

    @Autowired
    private EquityHealthcheckProperties healthCheckProperties;

    @Autowired
    private PredSrchService predSrchService;

    @Autowired
    private QuoteHistoryRepository quoteHistoryDao;

    @Autowired
    private TrisTokenService trisTokenService;

    /*
     * (non-Javadoc)
     *
     * @see com.hhhh.group.secwealth.mktdata.starter.healthcheck.service.Healthcheck #getStatus()
     */
    @Override
    public List<Status> getStatus() {
        List<Status> statusList = new ArrayList<>();
        statusList.add(DbCheck());
        statusList.add(TrisCheck());
        statusList.add(PredictiveSearchCheck());
        return statusList;
    }

    private Status DbCheck() {
        long startTime = System.currentTimeMillis();
        try {
            Integer num = this.quoteHistoryDao.dbCheck();
            EquityHealthCheckService.logger.warn("EquityHealthCheckService", "dbHealthCheckMsg: " + num);
            long endTime = System.currentTimeMillis();
            return new Status(Status.SUCCESS, EquityHealthCheckService.HEALTHCHECK_DB, (endTime - startTime));
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            EquityHealthCheckService.logger.error("Error: Failed to database health check.", e);
            return new Status(Status.FAILURE, EquityHealthCheckService.HEALTHCHECK_DB, (endTime - startTime));
        }
    }

    private Status TrisCheck() {
        long startTime = System.currentTimeMillis();
        try {
            TrisQuoteServiceRequest req = new TrisQuoteServiceRequest();
            req.setService(this.healthCheckProperties.getService());
            CommonRequestHeader commonHeader = this.healthCheckProperties.getHeader();
            String site = commonHeader.getCountryCode() + EquityHealthCheckService.DOWN_LINE + commonHeader.getGroupMember();
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
                EquityHealthCheckService.logger.error("Invalid response: " + serviceResponse);
                throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
            }
            String statusKey = this.trisProps.getResponseStatusCodeKey();
            String status = JsonUtil.getAsString(respJsonObj, statusKey);
            if (!this.trisProps.isCorrectResponseStatus(status)) {
                EquityHealthCheckService.logger.error("Invalid response, status is incorrect: " + serviceResponse);
                throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
            }
            JsonArray resultJsonArray = JsonUtil.getAsJsonArray(respJsonObj, "result");
            if (resultJsonArray == null || resultJsonArray.size() <= 0) {
                EquityHealthCheckService.logger.error("Invalid response, result array is empty: " + serviceResponse);
                throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
            }
            long endTime = System.currentTimeMillis();
            return new Status(Status.SUCCESS, EquityHealthCheckService.HEALTHCHECK_TRIS, (endTime - startTime));
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            EquityHealthCheckService.logger.error("Error: Failed to tris health check.", e);
            return new Status(Status.FAILURE, EquityHealthCheckService.HEALTHCHECK_TRIS, (endTime - startTime));
        }
    }


    private Status PredictiveSearchCheck() {
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        try {
            CommonRequestHeader commonHeader = this.healthCheckProperties.getHeader();
            if (null == this.healthCheckProperties.getKeyWord()) {
                EquityHealthCheckService.logger.error("Please check your configuration \"healthcheck: key-word\"");
                throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_APP_ILLEGAL_CONFIGURATION);
            }
            List<String> symbols = Arrays.asList(this.healthCheckProperties.getKeyWord());
            List<PredSrchResponse> responses = (List<PredSrchResponse>) this.predSrchService.precSrch(symbols, commonHeader);
            if (responses.isEmpty()) {
                EquityHealthCheckService.logger.error("Invalid response, result array is empty: " + responses);
                throw new VendorException(ExCodeConstant.EX_CODE_PREDSRCH_INVALID_RESPONSE);
            }
            endTime = System.currentTimeMillis();
            return new Status(Status.SUCCESS, EquityHealthCheckService.HEALTHCHECK_PREDICTIVE_SEARCH, (endTime - startTime));
        } catch (Exception e) {
            endTime = System.currentTimeMillis();
            return new Status(Status.FAILURE, EquityHealthCheckService.HEALTHCHECK_PREDICTIVE_SEARCH, (endTime - startTime));
        }

    }
}
