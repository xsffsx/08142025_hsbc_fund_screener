/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonObject;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.ApplicationProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.TrisProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.QuoteHistoryRepository;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.QuoteMeterRepository;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.SubscriberRepository;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.QuoteHistory;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.QuoteMeter;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.Subscriber;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.SubscriberType;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.SECQuotesRequest;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
@Service
public class QuoteCommonService {

    private final static Logger logger = LoggerFactory.getLogger(QuoteCommonService.class);

    private static final int INSERT_RETRIES = 3;

    private static final int SLEEP_RETRIES = 50; // 50ms

    @Autowired
    private QuoteHistoryRepository quoteHistoryDao;

    @Autowired
    private TrisProperties trisProps;

    @Autowired
    private QuoteHistoryService quoteHistoryService;

    @Autowired
    private ExchangeInfoService exchangeInfoService;

    @Autowired
    private QuoteMeterRepository quoteMeterRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private ApplicationProperties appProps;


    @Transactional(rollbackFor = Exception.class)
    public void saveQuoteHistory(final List<JsonObject> jsonObject, final CommonRequestHeader commonHeader,
        final Map<String, String> map) {
        SECQuotesRequest quotesRequest = (SECQuotesRequest) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_REQUEST);
        if (MapUtils.isNotEmpty(map)) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                writeQuoteMeterAndCustomer(commonHeader.getCustomerId(), entry.getValue(), commonHeader.getCountryCode(),
                    commonHeader.getGroupMember());
            }
        }
        Long seqNum = this.quoteHistoryDao.querySeqNum();

        // String timestampString = DateUtil.parseDateByTimezone(new Date(),
        // Constant.TIMEZONE, Constant.DATE_FORMAT_TRIS_ISO8601);
        List<QuoteHistory> quoteList = new ArrayList<>();
        String exchangeCode = "";
        Map<String, Long> exchangeCountMap = new HashMap<>();
        for (JsonObject object : jsonObject) {
            QuoteHistory quoteHistory = new QuoteHistory();
            String trisCode = JsonUtil.getAsString(object, "_item");
            if (map.containsKey(trisCode)) {
                exchangeCode = map.get(trisCode);
            }
            String peCode = JsonUtil.getAsString(object, "PROD_PERM");
            quoteHistory.setPeCode(peCode);
            TimeZone exchangeTimezone = appProps.getTimezone(exchangeCode);
            String timestampString = DateUtil.current(DateUtil.DATE_HOUR_PATTERN, exchangeTimezone);
            quoteHistory.setTradeDatetimeZone(exchangeTimezone.getID());
            quoteHistory.setTradeDatetime(timestampString);
            quoteHistory.setUpdatedOn(Calendar.getInstance().getTime());
            quoteHistory.setExchangeCode(exchangeCode);
            quoteHistory.setQuotHistBatId(seqNum);
            int exchangeId = exchangeInfoService.getExchangeId(exchangeCode);
            quoteHistory.setExchangeId(exchangeId);
            quoteHistory.setMarketCode(quotesRequest.getMarket());
            quoteHistory.setSubscriberId(commonHeader.getCustomerId());
            quoteHistory.setStaffid(commonHeader.getStaffId());
            quoteHistory.setSymbol(trisCode);
            if (object != null) {
                quoteHistory.setResponseText(object.toString());
            } else {
                quoteHistory.setResponseText("Response Error");
            }
            String statusKey = this.trisProps.getResponseStatusCodeKey();
            String status = JsonUtil.getAsString(object, statusKey);
            String stateCode = status.equals(Constant.ZERO) ? "Y" : "N";
            if (stateCode.equals("Y")) {
                quoteHistory.setUsageCount(Integer.valueOf(Constant.CASE_1));
                getExchangeCount(exchangeCode, exchangeCountMap);
            } else {
                quoteHistory.setUsageCount(Integer.valueOf(Constant.CASE_0));
            }

            quoteHistory.setRequestStatus(stateCode);

            if (quotesRequest.getDelay()) {
                quoteHistory.setRequestType(Constant.DELAYED);
            } else {
                quoteHistory.setRequestType(Constant.REAL_TIME);
            }
            quoteHistory.setRequestTime(Calendar.getInstance().getTime());
            quoteHistory.setCountryCode(commonHeader.getCountryCode());
            quoteHistory.setGroupMember(commonHeader.getGroupMember());
            quoteHistory.setRequestTimeZone(DateUtil.getTimeZoneName());
            quoteHistory.setChannelId(commonHeader.getChannelId());
            quoteHistory.setRequestForStock(exchangeCode);
            quoteHistory.setUpdatedBy(Constant.ONLINE_UPDATION);
            String symbol = JsonUtil.getAsString(object, "MNEMONIC");
            quoteHistory.setProdSymbol(symbol);
            quoteHistory.setRicCode(trisCode);
            String appCode = commonHeader.getAppCode();

            if (StringUtil.isValid(appCode)) {
                quoteHistory.setAppCode(appCode);
            }
            quoteList.add(quoteHistory);
        }
        this.quoteHistoryService.updatequoteHistory(quoteList);
        this.reduceQuote(commonHeader.getCustomerId(), true, exchangeCountMap);
    }


    public void getExchangeCount(final String exchange, final Map<String, Long> map) {
        synchronized (map) {
            if (map.containsKey(exchange)) {
                Long count = map.get(exchange);
            map.put(exchange, count + Long.valueOf(Constant.CASE_1));
            } else {
            map.put(exchange, Long.valueOf(Constant.CASE_1));
            }
        }
    }

    public void reduceQuote(final String customer, final Boolean quoteType, final Map<String, Long> exchangeCountMap) {
        if (MapUtils.isNotEmpty(exchangeCountMap)) {
            for (Map.Entry<String, Long> entry : exchangeCountMap.entrySet()) {
                QuoteCommonService.logger.debug("entering into the reduceQuote of equity daoimpl for exchangeid is--> {}",
                    entry.getKey() + "customer is-->{} " + customer);
                QuoteMeter quoteMeter = quoteMeterRepository.fetchQuometerByCustomer(customer, entry.getKey());
                if (null != quoteMeter) {
                    quoteMeter.setUpdatedBy(Constant.ONLINE_UPDATION);
                    quoteMeter.setUpdatedOn(Calendar.getInstance().getTime());
                    Long count = entry.getValue();
                    if (quoteType) {
                        Long userdDelayedQuote = quoteMeter.getUsedDelayedQuote() + count;
                        quoteMeter.setUsedDelayedQuote(userdDelayedQuote);
                    } else {
                        Long usedRealTimeQuote = quoteMeter.getUsedRealTimeQuote() + count;
                        quoteMeter.setUsedRealTimeQuote(usedRealTimeQuote);
                    }
                    quoteMeterRepository.save(quoteMeter);
                } else {
                    QuoteCommonService.logger.error("quoteMeter object not exist,exchangeCode is " + entry.getKey());
                    throw new ApplicationException(ExCodeConstant.EX_CODE_DB_DATA_UNAVAILABLE);
                }
            }
        }
    }
    public void writeQuoteMeterAndCustomer(final String customer, final String exchangeCode, final String countryCode,
        final String groupMember) {
        QuoteCommonService.logger.debug("entering into getQuoteMeterMap,requested member is: {}", customer);
        List<Object[]> object = quoteMeterRepository.getQuotaByExchangeID(customer, exchangeCode);
        QuoteCommonService.logger.debug("query fetched values are in getQuoteMeterMap:{}", object);
        if (object.isEmpty()) {
            QuoteCommonService.logger.debug("Quote Detail is not available this member, with provided exchange.");
            populateSubscriber(customer, countryCode, groupMember);
            populateDefaultQuoteMeter(customer, exchangeCode, countryCode, groupMember);
        }
        QuoteCommonService.logger.debug("finally quotemeter should be there");
    }


    private void populateSubscriber(final String subscriberId, final String countryCode, final String groupMember) {
        QuoteCommonService.logger.debug("entering into populateSubscriber of the customer :{}", subscriberId);
        Subscriber subscriber = new Subscriber();
        subscriber.setUpdatedOn(Calendar.getInstance().getTime());
        subscriber.setUpdatedBy(Constant.ONLINE_UPDATION);
        subscriber.setSubscriberId(subscriberId);
        subscriber.setCountry(countryCode);
        SubscriberType subscriberType = null;
        String subscribType = appProps.getQuoteMeter(Constant.DEFAULT_OPTION, Constant.SUBSCRIBERTYPE, Constant.DEFAULT_OPTION);
        if (Constant.SUBSCRIBERTYPE_PROF.equals(subscribType)) {
            subscriberType = SubscriberType.PROF;
        } else {
            subscriberType = SubscriberType.NONPROF;
        }
        subscriber.setSubscriberType(subscriberType);
        safeDuplicateSave(new RetryCallback() {
            public void save(final int retry) {
                subscriberRepository.save(subscriber);
                QuoteCommonService.logger.debug("Save subscriber table : {} retry : {}", subscriber.toString(), retry);
            }

        });
    }


    private void populateDefaultQuoteMeter(final String subscriberId, final String strExchangeCode, final String countryCode,
        final String groupMember) {
        QuoteCommonService.logger.debug("entering into the populateDefaultQuoteMeter {}", strExchangeCode);
        boolean isExchngDefaultMppngAvalble = Boolean.FALSE;
        String ExchangeMappingValue = Constant.DEFAULT_OPTION;
        int exchangeId = exchangeInfoService.getExchangeId(strExchangeCode);
        if (exchangeId == Integer.valueOf(Constant.ZERO)) {
            QuoteCommonService.logger.error("not exist exchnageCode from MKT_PRD_EXCH_INFO,exchangeCode is" + strExchangeCode);
            throw new ApplicationException(ExCodeConstant.EX_CODE_DB_DATA_UNAVAILABLE);
        }
        Map<String, Map<String, Map<String, String>>> quoteMeterMap = appProps.getQuoteMeter();
        if (!quoteMeterMap.keySet().isEmpty()) {
            if (quoteMeterMap.keySet().contains(strExchangeCode)) {
                isExchngDefaultMppngAvalble = Boolean.TRUE;
                ExchangeMappingValue = strExchangeCode;
            }
        }
        QuoteMeter quoteMeter = new QuoteMeter();
        QuoteCommonService.logger.debug("isExchngDefaultMppngAvalble is: {}", isExchngDefaultMppngAvalble);
        quoteMeter.setRealTimeQuota(Long.valueOf(appProps.getQuoteMeter(ExchangeMappingValue, Constant.REALTIME, Constant.QUOTE)));
        quoteMeter.setRealQuotaUnlimited(appProps.getQuoteMeter(ExchangeMappingValue, Constant.REALTIME, Constant.UNLIMITED));
        quoteMeter.setDelayedQuota(Long.valueOf(appProps.getQuoteMeter(ExchangeMappingValue, Constant.DELAYED, Constant.QUOTE)));
        quoteMeter.setDelayedQuotaUnlimited(appProps.getQuoteMeter(ExchangeMappingValue, Constant.REALTIME, Constant.UNLIMITED));
        quoteMeter.setExchangeId(exchangeId);
        quoteMeter.setSubscriberId(subscriberId);
        quoteMeter.setUsedRealTimeQuote(Long.valueOf(0));
        quoteMeter.setUsedDelayedQuote(Long.valueOf(0));
        quoteMeter.setUpdatedBy(Constant.ONLINE_UPDATION);
        quoteMeter.setUpdatedOn(Calendar.getInstance().getTime());
        safeDuplicateSave(new RetryCallback() {
            public void save(final int retry) {
                quoteMeterRepository.save(quoteMeter);
                QuoteCommonService.logger.debug("Save quoteMeter table : {} retry : {}", quoteMeter.toString(), retry);
            }
        });
    }


    private static interface RetryCallback {
        public void save(int retry);
    }


    private void safeDuplicateSave(final RetryCallback callback) {
        try {
            int retry = 0;
            do {
                try {
                    callback.save(retry);
                    break;
                } catch (org.springframework.transaction.TransactionSystemException e) {
                    QuoteCommonService.logger.error("TransactionSystemException while storing quote informration", e);
                    Thread.sleep(QuoteCommonService.SLEEP_RETRIES);
                }
            } while (retry++ < QuoteCommonService.INSERT_RETRIES);

            if (retry >= QuoteCommonService.INSERT_RETRIES) {
                throw new java.lang.UnsupportedOperationException("Unable to insert quote information. Retry [" + retry + "]");
            }

        } catch (Throwable e) {
            QuoteCommonService.logger.error("Error while storing quote info", e);
            throw new ApplicationException(ExCodeConstant.EX_CODE_DB_UNKNOWN_ERROR);
        } finally {
            QuoteCommonService.logger.debug("exit from safeDuplicateSave after persisting default quote");
        }
    }
}

