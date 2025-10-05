/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.service;


import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.repository.ExchangeAgreementRepository;
import com.hhhh.group.secwealth.mktdata.api.equity.common.markethour.service.MarketHourService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.ApplicationProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;

/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
@Component
public class MarketAgreementService {

    private static final Logger logger = LoggerFactory.getLogger(MarketAgreementService.class);

    @Autowired
    private ApplicationProperties appProps;

    @Autowired
    private ExchangeAgreementRepository exchangeAgreementRepository;

    @Autowired
    private MarketHourService marketHourService;

    /**
     * <p>
     * <b> Check agreement is expired or not. </b>
     * </p>
     *
     * @param subscriberId
     * @param exchangeCode
     * @param currentDate
     * @return true: agreement is expired
     */
    public boolean hasEffectiveAgreement(final String subscriberId, final String exchangeCode, final Date currentDate) {
        MarketAgreementService.logger.debug("subscriberId ： {} ,exchangeCode : {}", subscriberId, exchangeCode);
        if (StringUtils.isBlank(subscriberId)) {
            throw new IllegalArgumentException("subscriberId Code cannot be blank!");
        }
        if (StringUtils.isBlank(exchangeCode)) {
            throw new IllegalArgumentException("exchangeCode Code cannot be blank!");
        }
        Integer effectiveCount = this.exchangeAgreementRepository.hasEffectiveAgreement(subscriberId, exchangeCode, currentDate);
        if (effectiveCount != null && effectiveCount > 0) {
            return true;
        }
        return false;
    }

    /**
     * <p>
     * <b> Check the real time quotes is enable or not. </b><br>
     * <b> If yes, will check agreement and market hour. </b><br>
     * <b> If not, will return the delay quotes data even if delay is false in client request URL. </b>
     * </p>
     *
     * @return
     */
    public boolean isRealtimeQuotesEnable() {
        return this.appProps.getRealTimeQuote().isEnable();
    }

    /**
     * <p>
     * <b>Check agreement and market hour. </b>
     * </p>
     *
     * @return true: call real-time data; false: call delay data
     */
    public boolean checkAgreementAndMarketHour(final String exchangeCode) {

        boolean result = false;
        boolean isMarketOpen = false;
        boolean hasEffectiveAgreement = false;
        boolean needAgreementChecking = hasExchangesToDoAgreementChecking(exchangeCode);
        if (needAgreementChecking) {
            String customerId = StringUtil.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_RAW_NAME_ID));
            hasEffectiveAgreement = this.hasEffectiveAgreement(customerId, exchangeCode, new Date());
            if (hasEffectiveAgreement) {
                isMarketOpen = this.marketHourService.isMarketOpen(exchangeCode);
            }
            result = hasEffectiveAgreement && isMarketOpen;
        } else {
            result = this.marketHourService.isMarketOpen(exchangeCode);
        }

        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_NEED_AGREEMENT_CHECKING, needAgreementChecking);
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_AGREEMENT_CHECK_RESULT, hasEffectiveAgreement);
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_MARKET_HOUR_CHECK_RESULT, isMarketOpen);

        return result;
    }

    private boolean hasExchangesToDoAgreementChecking(final String exchangeCode) {

        List<String> exchanges = this.appProps.getRealTimeQuote().getExchangeCodes();
        return exchanges != null && exchanges.size() > 0
            && (exchanges.contains(exchangeCode) || exchanges.contains(Constant.APP_SUPPORT_ALL_EXCHANGES));
    }

}
