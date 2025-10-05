package com.hhhh.group.secwealth.mktdata.api.equity.quotes.scheduled;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.service.TradingSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@EnableScheduling
public class TradingSesionSEHKScheduled {
    private static final Logger logger = LoggerFactory.getLogger(TradingSesionSEHKScheduled.class);
    @Autowired
    private TradingSessionService tradingSessionService;



    @Scheduled(cron = "0 55 * ? * MON-FRI")
    public void storeTradingSessionSEHK() throws Exception {
        TradingSesionSEHKScheduled.logger.info("SEHK trading session cron job start...");
        tradingSessionService.retrieveTradingSessionSEHK();
        TradingSesionSEHKScheduled.logger.info("SEHK trading session cron job completed...");
    }
}
