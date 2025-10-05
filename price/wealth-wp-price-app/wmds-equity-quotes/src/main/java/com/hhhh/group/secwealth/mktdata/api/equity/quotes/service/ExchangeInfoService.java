package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.ExchangeInfoRepository;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.ExchangeInfo;

@Service
public class ExchangeInfoService {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeInfoService.class);

    @Autowired
    private ExchangeInfoRepository exchangeInfoRepository;


    @Cacheable(cacheNames = "exchangeCache", key = "#exchangeCode", unless = "#result == null")
    public int getExchangeId(final String exchangeCode) {
        ExchangeInfoService.logger.debug("Getting exchange code by Spring Data JPA : " + exchangeCode);
        ExchangeInfo exchangeInfo = this.exchangeInfoRepository.findByExchangeCode(exchangeCode);
        ExchangeInfoService.logger.debug("Got exchange Id {}", exchangeInfo);
        if (exchangeInfo != null) {
            return exchangeInfo.getId();
        } else {
            return 0;
        }
    }
}
