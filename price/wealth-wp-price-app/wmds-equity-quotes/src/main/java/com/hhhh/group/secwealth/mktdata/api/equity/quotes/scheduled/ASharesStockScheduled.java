package com.hhhh.group.secwealth.mktdata.api.equity.quotes.scheduled;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.service.ASharesStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
@EnableScheduling
public class ASharesStockScheduled implements CommandLineRunner {

    private static final long SCHEDULED_DELAY_SECONDS;

    static {
        SCHEDULED_DELAY_SECONDS = new Random().nextInt(5 * 60);
        log.info("In this instance, a shares stock scheduled task will be delayed by {} seconds", SCHEDULED_DELAY_SECONDS);
    }

    @Autowired
    private ASharesStockService aSharesStockService;

    // note: please pay attention to the time zone difference
    @Scheduled(cron = "0 30 0 * * ?")
    public void refreshASharesStock() {
        try {
            // avoid simultaneous execution of multiple instances
            Thread.sleep(SCHEDULED_DELAY_SECONDS * 1000);
            int size = aSharesStockService.reloadASharesStockFromETNet();
            if(size==0){
                log.error("Can not load a shares stock from ETNet");
                return;
            }
            log.info("refresh a shares stock from ETNet with {} records", size);
        } catch (Exception e) {
            log.error("a shares stock loaded has error", e);
        }
    }

    @Override
    public void run(String... strings) {
        aSharesStockService.initASharesStock();
    }

}
