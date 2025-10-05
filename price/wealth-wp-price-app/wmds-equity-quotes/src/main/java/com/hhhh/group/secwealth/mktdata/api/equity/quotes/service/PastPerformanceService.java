package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import com.google.gson.Gson;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.BigDecimalUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.properties.LabciPortalProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.labciportal.performance.PastPerformanceRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.PastPerformance;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.labciPortal.performance.PastPerformanceResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.labciPortal.performance.entity.Data;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service("pastPerformanceService")
@ConditionalOnProperty(value = "service.quotes.usEquity.injectEnabled", havingValue = "true")
public class PastPerformanceService {

    private static final Logger logger = LoggerFactory.getLogger(PastPerformanceService.class);

    @Autowired
    private HttpClientHelper httpClientHelper;

    @Autowired
    private LabciPortalTokenService labciPortalTokenService;

    @Autowired
    private LabciPortalProperties labciPortalProperties;

    public PastPerformance getPastPerformance(String channelId, String marketCode, String tCode) {

        String tokenStr = null;
        try {
            tokenStr = this.labciPortalTokenService.encryptLabciPortalToken(channelId, marketCode);
        } catch (Exception e) {
            logger.error("Failed to generate LabCI Portal token string.", e);
        }
        String proxyName = this.labciPortalProperties.getProxyName();
        String pastPerformanceUrl = this.labciPortalProperties.getPastPerformanceUrl();

        PastPerformanceRequest pastPerformanceRequest = new PastPerformanceRequest();
        pastPerformanceRequest.setToken(tokenStr);
        pastPerformanceRequest.setNbRic(tCode);

        String responseStr = null;
        PastPerformance pastPerformance = null;
        try {
            responseStr = this.httpClientHelper.doPost(
                    proxyName,
                    pastPerformanceUrl,
                    new Gson().toJson(pastPerformanceRequest),
                    null);

            PastPerformanceResponse pastPerformanceResponse = new Gson().fromJson(responseStr, PastPerformanceResponse.class);
            pastPerformance = new PastPerformance();
            if(pastPerformanceResponse.getStatusCode().equalsIgnoreCase("000")
                    && pastPerformanceResponse.getStatusText().equalsIgnoreCase("OK")) {
                Data data = pastPerformanceResponse.getResult().getData();
                pastPerformance.setMPC1(BigDecimalUtil.fromStringAndCheckNull(data.getMPC1()));
                pastPerformance.setMPC3(BigDecimalUtil.fromStringAndCheckNull(data.getMPC3()));
                pastPerformance.setWPC1(BigDecimalUtil.fromStringAndCheckNull(data.getWPC1()));
                pastPerformance.setYPC1(BigDecimalUtil.fromStringAndCheckNull(data.getYPC1()));
                pastPerformance.setYPC3(BigDecimalUtil.fromStringAndCheckNull(data.getYPC3()));
                pastPerformance.setNbRic(data.getNbRic());
            } else {
                logger.error("Failed to get past performance from LabCI portal, response msg:" + responseStr);
            }
        } catch (Exception e) {
            logger.error("Failed to get past performance from LabCi Portal. T code: [" + tCode + "] ", e);
        }

        return pastPerformance;
    }
}
