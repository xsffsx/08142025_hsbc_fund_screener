package com.hhhh.group.secwealth.mktdata.api.equity.quotes.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.EtnetProperties;

@Component
@EnableScheduling
public class EtnetTokenVerifyScheduled {

	private static final Logger logger = LoggerFactory.getLogger(EtnetTokenVerifyScheduled.class);

	@Autowired
	private EtnetProperties etnetProperties;

	@Scheduled(cron = "0 0/12 * * * ?")
	public void autoVerifyEtnetToken() {
		try {
			logger.info("Start autoVerifyEtnetToken to verify etnet token...");
			String token = etnetProperties.getEtnetToken();
			logger.info("End autoVerifyEtnetToken to verify etnet token...");
		} catch (Exception e) {
			logger.error("autoVerifyEtnetToken has error", e);
		}
	}

}
