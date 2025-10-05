package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.properties.LabciPortalProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.LabciToken;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.TimeZone;

@Service
public class LabciPortalTokenService {

    @Autowired
    private LabciPortalProperties labciPortalProperties;

    private static final int MINUTES = 15;
    private static final String OBO = "N";
    private static final String APP_CODE = "MDS_HASE";
    private static final String DURATION = "15";
    private static final String MARKET_FLAG = "Y";
    private static final String NUMBER_OF_MARKET = "1";

    private LabciToken getTokenModel(String channelId, String market) {
        LabciToken labciToken = new LabciToken();
        labciToken.setChannelId(channelId);
        labciToken.setCustomerId(null);
        labciToken.setMarketId(market);
        labciToken.setAppCode(APP_CODE);
        labciToken.setNumberOfMarket(NUMBER_OF_MARKET);
        labciToken.setDuration(DURATION);
        labciToken.setMarketFlag(MARKET_FLAG);
        labciToken.setObo(OBO);
        String timeStamp = DateUtil.afterMinutesOfCurrent(DateUtil.YYYY_M_MDD_H_HMMSS, TimeZone.getTimeZone(DateUtil.DEFAULT_TIMEZONE_ID),
                MINUTES);
        labciToken.setTimeStamp(timeStamp);
        return labciToken;
    }

    public String encryptLabciPortalToken(String channelId, String market) throws Exception {
        LabciToken tokenModel = getTokenModel(channelId, market);

        String oriToken = tokenModel.generateTokenStr();
        return labciPortalProperties.miCipher.encrypt(oriToken);
    }
}
