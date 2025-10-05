package com.hhhh.group.secwealth.mktdata.predsrch.svc.service;

import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.LabciToken;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.properties.LabciProtalProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.TimeZone;

@Service
public class LabciProtalService {

    @Autowired
    private LabciProtalProperties labciProtalProperties;

    private static final int MINUTES = 15;
    private static final String OBO = "N";
    private static final String APP_CODE = "MDS_HASE";
    private static final String DURATION = "15";
    private static final String MARKET_FLAG = "Y";
    private static final String NUMBER_OF_MARKET = "1";
    private static final String CHANNEL_ID = "OHI";

    private LabciToken getTokenModel( String market) {
        LabciToken labciToken = new LabciToken();
        labciToken.setChannelId(CHANNEL_ID);
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

    public String encryptLabciProtalToken(String market) throws Exception {
        LabciToken tokenModel = getTokenModel(market);

        String oriToken = tokenModel.generateTokenStr();
        return labciProtalProperties.miCipher.encrypt(oriToken);
    }
}
