package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import com.google.gson.reflect.TypeToken;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.rbpTradingHour.RetrieveTradeDateInfoResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.rbpTradingHour.TradeDateInfo;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.scheduled.tradingsession.TradingSessionSEHK;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.util.TradingHourUtil;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


@Service("tradingSessionService")
public class TradingSessionService {
    private static final Logger logger = LoggerFactory.getLogger(TradingSessionService.class);

    private static final String TRADE_DATE = "tradeDay";
    @Autowired
    private LabciProperties labciProps;

    @Autowired
    private HttpClientHelper httpClientHelper;

    public RetrieveTradeDateInfoResponse retrieveTradingSessionSEHK() throws Exception {
        String rbpTradingDate = "";
        Map<String, String> saml = new HashMap<>();
        String current =
                TradingHourUtil.current(TradingHourUtil.DATE_DAY_PATTERN, TimeZone.getTimeZone(TradingHourUtil.ASIA_SHANGHAI_TIMEZONE));

        String params = new StringBuilder().append(this.labciProps.getTradingParamPrefix())
                .append(URLEncoder.encode(this.labciProps.getTradingParam(), "UTF-8")).toString();
        String url = this.labciProps.getTradingDayUrl().replace(TradingSessionService.TRADE_DATE, current);
        try {
            //saml.put(Constant.REQUEST_HEADER_KEY_SAML3, "<saml:Assertion xmlns:saml='http://www.hhhh.com/saas/assertion'><Signature><KeyAlias>E2E_TRUST_SAAS_EU01_PPROD_ALIAS</KeyAlias><SignatureValue>UU5pxHqwy9pvYzwY35DdHbY8+qRheNQLPejpNQO1HI21mEiQBXndbEIAtKkAyhcibzW5xBFgrwEe7w4sOZ3+7mXtEGlWdG//1mvExdFXSUcp9wFkVGwlusx3o6T9sd2gz0Ox6ymkaw9SSNeSGF+jyU8VxtuBnO4YbZiRzrViinVnQNVwX149D/BByC6FXuHJZW+TfmfHF/dS+Sihb3zW0HPjj4pPeGINmJ2yUbdEjo2VAlv5SXB3xaeJe/bV4NHrHhzyj/2JLKj/EVqpKdO9JBPKhJQ1tpG4T0jKwh2e5QkxLcOX7aYGlWWlL58MUZAqFqzFLm0fI06nbknvIhO0hA==</SignatureValue></Signature><saml:Subject><saml:NameID>HK26737354688201A</saml:NameID></saml:Subject><saml:Conditions NotBefore='2024-02-19T06:06:16.473Z' NotOnOrAfter='2024-02-19T06:07:16.473Z'/><saml:AttributeStatement><saml:Attribute Name='IP'><saml:AttributeValue>192.0.0.12</saml:AttributeValue></saml:Attribute><saml:Attribute Name='CAM'><saml:AttributeValue>40</saml:AttributeValue></saml:Attribute></saml:AttributeStatement> </saml:Assertion>");
            rbpTradingDate = this.httpClientHelper.doGet(url, params, saml);
        } catch (Exception e) {
            TradingSessionService.logger.error("access rbp TradingDate api failed, url is " + url);
            throw new VendorException(ExCodeConstant.EX_CODE_RBP_TRADE_HOUR_ERROR, e);
        }
        TradingSessionService.logger.info("SEHK Trading Session : {} ",rbpTradingDate);
        RetrieveTradeDateInfoResponse response  =
                JsonUtil.fromJson(rbpTradingDate, new TypeToken<RetrieveTradeDateInfoResponse>() {
                }.getType());
        TradingSessionSEHK.setTradingSessionWithLock(response);
        return response;
    }

    public RetrieveTradeDateInfoResponse tradingSessionFromCacheSEHK() throws Exception {
        RetrieveTradeDateInfoResponse response = TradingSessionSEHK.getTradingSessionWithLock();
        if(null == response || ListUtil.isInvalid(response.getTradeDateList())){
            response = retrieveTradingSessionSEHK();
        }
        return response;
    }

    public boolean isTradingDay() throws Exception{
        RetrieveTradeDateInfoResponse response = this.tradingSessionFromCacheSEHK();
        String current =
                TradingHourUtil.current(TradingHourUtil.DATE_DAY_PATTERN, TimeZone.getTimeZone(TradingHourUtil.ASIA_SHANGHAI_TIMEZONE));
        boolean isContainToday = false;
        if (response != null) {
            for (TradeDateInfo tradeDateInfo : response.getTradeDateList()) {
                // only compare day , ignore HH:mm:ss
                // TimeZone.getTimeZone("GMT+8")
                if (tradeDateInfo.getOrderTradeDate().compareTo(current) == 0) {
                    isContainToday = true;
                    break;
                }
            }
        }
        return isContainToday;
    }
}
