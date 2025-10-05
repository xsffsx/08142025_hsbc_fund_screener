package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service.stockinfo;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.pojo.StockInfo;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.properties.LabciPortalProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service("quotesUSLabci4CmbPortalService")
@ConditionalOnProperty(value = "service.quotes.usEquity.injectEnabled", havingValue = "true")
public class QuotesUSLabci4CmbPortalService extends QuotesUSLabciPortalService{

    private static final Logger logger = LoggerFactory.getLogger(QuotesUSLabci4CmbPortalService.class);

    @Autowired
    @Qualifier("labciPortal4CmbProperties")
    private LabciPortalProperties labciPortalProperties;

    public QuotesUSLabci4CmbPortalService() {
        super();
    }

    public StockInfo getStockInfo(String channelId, String marketCode, String tCode, String language) {
        super.setLabciPortalProperties(this.labciPortalProperties);
        return super.getStockInfo(channelId, marketCode, tCode, language);
    }
}
