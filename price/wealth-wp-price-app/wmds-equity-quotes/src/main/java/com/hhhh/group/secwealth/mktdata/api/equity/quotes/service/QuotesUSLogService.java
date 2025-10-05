package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import com.hhhh.group.secwealth.mktdata.api.equity.common.name.id.handler.NameIdHandler;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.QuoteAccessLogRepository;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.QuoteCounterRepository;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.QuotePlayerRepository;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.AccessLog;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.DomainError;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.Player;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.QuoteCounter;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.SECQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesLabciQuote;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.datasource.aspect.annotation.SelectDatasource;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(value = "service.quotes.usEquity.injectEnabled", havingValue = "true")
public class QuotesUSLogService extends QuotesHKLogService{

    private static final Logger logger = LoggerFactory.getLogger(QuotesUSLogService.class);

    private static final String QUOTE_CNT_TYPE_STOCK = "STOCK";

    private static final String QUOTE_CNT_TYPE_DLSTK = "DLSTK";

    private static final String QUOTE_CNT_TYPE_EDSTK = "EDSTK";

    @Autowired
    private QuotePlayerRepository quotePlayerRepository;

    @Autowired
    private NameIdHandler nameIdHandler;

    @Autowired
    private QuoteCounterRepository quoteCounterRepository;

    @Autowired
    private QuoteAccessLogRepository quoteAccessLogRepository;

    public String getUserType() {
        CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
        //customerId|OHI|OHM|CUST,staffId|OHCC|OHB|OHBR|STFF
        String userType = QuotesUSCommonService.USER_TYPE_CDE_CUST;
        String channelId = header.getChannelId();
        if (this.nameIdHandler.getCustomerChannel().contains(channelId)) {
            userType = QuotesUSCommonService.USER_TYPE_CDE_CUST;
        } else if (this.nameIdHandler.getStaffChannel().contains(channelId)) {
            userType = QuotesUSCommonService.USER_TYPE_CDE_STFF;
        } else {
            logger.warn("Undefined channelId: " + channelId);
        }
        return userType;
    }

    @SelectDatasource
    public void updateQuoteMeterAndLog(Map<String, List<QuotesLabciQuote>> labciQuoteWithExchge) {
        SECQuotesRequest request = (SECQuotesRequest) ArgsHolder
                .getArgs(Constant.THREAD_INVISIBLE_QUOTES_LABCI_REQUEST);
        CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
        String remarkField = header.getAppCode() +"_"+ header.getChannelId();
        String customerId = (String) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_RAW_NAME_ID);
        if (StringUtil.isInValid(customerId)) {
            customerId = (String) ArgsHolder.getArgs(QuotesUSCachingServerService.CUSTOMER_ID);
        }

        Player player = new Player();

        player.setPlayerType(getUserType());
        player.setPlayerId(customerId);
        player.setCountryCode(header.getCountryCode());
        player.setGroupMember(header.getGroupMember());


        player.setCountryCode(header.getCountryCode());
        player.setGroupMember(header.getGroupMember());
        Player quoteUser = quotePlayerRepository
                .findOne(Example.of(player, ExampleMatcher.matching().withIgnorePaths("playerReferenceNumber")));
        Long playerReferenceNumber = 718529l;
        if (quoteUser == null) {// ConcurrentLogonValidator update time or not
            player.setLastLogonTime(new Timestamp(0));
            player.setLastUpdate(new Timestamp(DateUtil.getMachineDateTime().getTime()));
            Player savedPlayer = quotePlayerRepository.save(player);
            playerReferenceNumber = savedPlayer.getPlayerReferenceNumber();
        } else {
            playerReferenceNumber = quoteUser.getPlayerReferenceNumber();
        }

        String market = request.getMarket();
        Set<String> exchangeSet = labciQuoteWithExchge.keySet();
        boolean sendDelay = (boolean) ArgsHolder.getArgs(QuotesUSCommonService.THREAD_INVISIBLE_QUOTES_DELAY);
        boolean isTradingHour = (boolean) ArgsHolder.getArgs(QuotesUSCommonService.THREAD_INVISIBLE_QUOTES_TRADING_HOUR);
//todo
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        String quoteType = null;
        if(!isTradingHour) {
            quoteType = QUOTE_CNT_TYPE_EDSTK;
        } else {
            quoteType = !sendDelay?QUOTE_CNT_TYPE_STOCK:QUOTE_CNT_TYPE_DLSTK;
        }

        addStockQuoteCounter(playerReferenceNumber, exchangeSet, market, labciQuoteWithExchge, quoteType, timeZone);

        addAccessLog(playerReferenceNumber, exchangeSet, market, labciQuoteWithExchge, quoteType, remarkField);
    }


    protected void addAccessLog(Long customerId, Set<String> exchangeSet, String market,
                                Map<String, List<QuotesLabciQuote>> labciQuoteWithExchge, String accessType, String remarkField) {
        AccessLog accessLog = new AccessLog();
        StringBuffer sb = new StringBuffer();

        int countUsage = 0;
        for (Map.Entry<String, List<QuotesLabciQuote>> quoteUsage : labciQuoteWithExchge.entrySet()) {
            String exchangeName = quoteUsage.getKey();
            // String mappedExchangeName = this.labciProps.getExchangeMapping(exchangeName);
            sb.append(exchangeName);
            sb.append(QuotesHKLabciServiceImpl.SYMBOL_COLON);
            sb.append(quoteUsage.getValue().size());
            sb.append(QuotesHKLabciServiceImpl.SYMBOL_COLON);
            for (QuotesLabciQuote labciQuote : quoteUsage.getValue()) {
                sb.append(symbolToStr(labciQuote.getSymbol(), labciQuote.getNominalPrice().toString()));
                sb.append(QuotesHKLabciServiceImpl.SYMBOL_SEMICOLON);
                countUsage++;
            }
            sb.append(QuotesHKLabciServiceImpl.SYMBOL_VERTICAL);
        }

        Timestamp timestamp = new Timestamp(DateUtil.getMachineDateTime().getTime());
        accessLog.setPlayerReferenceNumber(customerId);
        accessLog.setTradingMarketCode(market);
        accessLog.setChargeableFlag(QuotesHKLabciServiceImpl.ZERO);
        accessLog.setChargeCategory(QuotesHKLabciServiceImpl.ZERO);
        accessLog.setQuoteUsage(countUsage);
        accessLog.setAccessType(accessType);
        accessLog.setAccessCommand((String) ArgsHolder.getArgs(QuotesHKLogService.ACCES_CMND_CDE));// quotelist/detail
        accessLog.setAccessDetail(sb.toString());
        accessLog.setRemarkField(remarkField);
        accessLog.setLastUpdate(timestamp);
        quoteAccessLogRepository.save(accessLog);
        quoteAccessLogRepository.saveAndFlush(accessLog);
    }

    @Override
    protected void addStockQuoteCounter(Long customerId, Set<String> exchangeSet, String market,
                                        Map<String, List<QuotesLabciQuote>> labciQuoteWithExchge, String quoteType, TimeZone timeZone) {

        CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
        String entityCode = header.getAppCode();

        if(entityCode!=null&&!entityCode.equalsIgnoreCase("CMB")){
            entityCode = "WPB";
        } else {
            //do nothing
        }

        QuoteCounter quoteCounter = new QuoteCounter();
        quoteCounter.setPlayerReferenceNumber(customerId);
        quoteCounter.setTradingMarketCode(market);
        quoteCounter.setQuoteType(quoteType);
        Calendar instance = Calendar.getInstance();
        instance.setTimeZone(timeZone);
        quoteCounter.setQuoteYear(DateUtil.getCurrentYear(instance));
        quoteCounter.setQuoteMonth(DateUtil.getCurrentMonth(instance));
        quoteCounter.setEntityCode(entityCode);

        List<QuoteCounter> quoteCounterList = quoteCounterRepository
                .findAll(Example.of(quoteCounter, ExampleMatcher.matching().withIgnorePaths("quoteCounter")));
        Set<String> unsaveType = new HashSet<String>();
        List<DomainError> errorList = new ArrayList<DomainError>();
        long totalQuoteCounter = 0;
        Map<String, Long> detailQuoteCounterMap = new HashMap<String, Long>();
        Timestamp timestamp = new Timestamp(DateUtil.getMachineDateTime().getTime());

        if (quoteCounterList.size() < 1) {
            unsaveType.addAll(exchangeSet);
        }

        unsaveType = exchangeSet.stream().filter(exchangeName ->
            quoteCounterList.stream().noneMatch(
                    qCounter -> qCounter.getSubQuoteType().equalsIgnoreCase(exchangeName)
            )
        ).collect(Collectors.toSet());

        if (unsaveType.size() > 0) {
            for (String subQuoteType : unsaveType) {
                QuoteCounter unsaveQuoteCounter = new QuoteCounter();
                Integer newquoteUsageCounter = labciQuoteWithExchge.get(subQuoteType).size();
                unsaveQuoteCounter.setPlayerReferenceNumber(customerId);
                unsaveQuoteCounter.setTradingMarketCode(market);
                unsaveQuoteCounter.setQuoteType(quoteType);
                unsaveQuoteCounter.setQuoteYear(DateUtil.getCurrentYear(instance));
                unsaveQuoteCounter.setQuoteMonth(DateUtil.getCurrentMonth(instance));
                unsaveQuoteCounter.setSubQuoteType(subQuoteType);
                unsaveQuoteCounter.setQuoteCounter(newquoteUsageCounter);
                unsaveQuoteCounter.setLastUpdate(timestamp);
                unsaveQuoteCounter.setEntityCode(entityCode);
                quoteCounterRepository.save(unsaveQuoteCounter);
                detailQuoteCounterMap.put(subQuoteType, newquoteUsageCounter.longValue());
                totalQuoteCounter = totalQuoteCounter + newquoteUsageCounter;
            }
        }
        long sumQuoteUsage = 0;
        long sumQuoteCounter = 0;
        for (Map.Entry<String, List<QuotesLabciQuote>> quoteUsage : labciQuoteWithExchge.entrySet()) {
            sumQuoteUsage = sumQuoteUsage + quoteUsage.getValue().size();
        }

        for (QuoteCounter aQuoteCounter : quoteCounterList) {
            sumQuoteCounter = sumQuoteCounter + aQuoteCounter.getQuoteCounter();
        }

        if (sumQuoteUsage + sumQuoteCounter <= QuotesHKLabciServiceImpl.LARGE_QUOTE_COUNT) {
            for (QuoteCounter aQuoteCounter : quoteCounterList) {
                if(labciQuoteWithExchge.containsKey(aQuoteCounter.getSubQuoteType())) {
                    long theQuoteCounter = aQuoteCounter.getQuoteCounter()
                            + labciQuoteWithExchge.get(aQuoteCounter.getSubQuoteType()).size();
                    aQuoteCounter.setQuoteCounter(theQuoteCounter);
                    aQuoteCounter.setLastUpdate(timestamp);
                    QuoteCounter newQuoteCounter = quoteCounterRepository.save(aQuoteCounter);
                    if (newQuoteCounter != null) {
                        detailQuoteCounterMap.put(newQuoteCounter.getSubQuoteType(), newQuoteCounter.getQuoteCounter());
                        totalQuoteCounter = totalQuoteCounter + newQuoteCounter.getQuoteCounter();
                    }
                }
            }
        } else {
            // quote count exceed error
            errorList.add(new DomainError(QuotesHKLabciServiceImpl.QUOTE_COUNT_EXCEED_ERROR));
            for (QuoteCounter aQuoteCounter : quoteCounterList) {
                detailQuoteCounterMap.put(aQuoteCounter.getSubQuoteType(), aQuoteCounter.getQuoteCounter());
                totalQuoteCounter = totalQuoteCounter + aQuoteCounter.getQuoteCounter();
            }
        }
    }
}
