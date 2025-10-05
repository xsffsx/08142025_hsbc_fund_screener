package com.hhhh.group.secwealth.mktdata.api.equity.topmover.service;

import com.hhhh.group.secwealth.mktdata.api.equity.common.token.LabciToken;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Component
public class LabciUSCommonService {

    private static final int MINUTES = 15;
    private static final String OBO = "N";
    private static final String APP_CODE = "PIB";
    private static final String DURATION = "15";
    private static final String MARKET_FLAG = "Y";
    private static final String NUMBER_OF_MARKET = "1";

    public static final Map<String, String> LABCI_LOCALE_MAPPING = new HashMap<String, String>() {
        {
            put("en", "en");
            put("en_US", "en");
            put("zh_HK", "hk");
            put("zh_CN", "cn");
            put("zh", "hk");
        }
    };

    public LabciToken getTokenModel(String channelId, String market) {
        LabciToken labciToken = new LabciToken();
        labciToken.setChannelId(channelId);
        labciToken.setCustomerId(null);
        labciToken.setMarketId(market);
        labciToken.setAppCode(APP_CODE);
        labciToken.setNumberOfMarket(NUMBER_OF_MARKET);
        labciToken.setDuration(DURATION);
        labciToken.setMarketFlag(MARKET_FLAG);
        labciToken.setObo(OBO);
        String timeStamp = DateUtil.afterMinutesOfCurrent(DateUtil.DATE_DAY_PATTERN_yyyyMMddHHmmss, TimeZone.getTimeZone(DateUtil.DEFAULT_TIMEZONE_ID),
                MINUTES);
        labciToken.setTimeStamp(timeStamp);
        return labciToken;
    }

}
