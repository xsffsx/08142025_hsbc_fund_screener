/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesLabciResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.service.stockinfo.QuotesUSLabciPortalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service("quotesUSCachingServer4CmbService")
@ConditionalOnProperty(value = "service.quotes.usEquity.injectEnabled", havingValue = "true")
public class QuotesUSCachingServer4CmbService extends QuotesUSCachingServerService {

    private static final Logger logger = LoggerFactory.getLogger(QuotesUSCachingServer4CmbService.class);

    @Autowired
    @Qualifier("quotesUSLabci4CmbPortalService")
    private QuotesUSLabciPortalService quotesUSLabciPortalService;

    @Override
    protected QuotesLabciResponse convertResponse(Object validServiceResponse) throws Exception {
        super.setQuotesUSLabciPortalService(this.quotesUSLabciPortalService);
        return super.convertResponse(validServiceResponse);
    }
}
