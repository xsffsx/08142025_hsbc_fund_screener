/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.index.service;

import com.hhhh.group.secwealth.mktdata.api.equity.common.name.id.handler.NameIdHandler;
import com.hhhh.group.secwealth.mktdata.api.equity.index.dao.IndexQuoteCounterUSRepository;
import com.hhhh.group.secwealth.mktdata.api.equity.index.dao.entiry.IndexQuoteCounterUSPo;
import com.hhhh.group.secwealth.mktdata.api.equity.index.response.Indice;
import com.hhhh.group.secwealth.mktdata.api.equity.index.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service("indexQuotesCounterUSService")
@ConditionalOnProperty(value = "service.quotes.Labci.injectEnabled")
public class IndexQuotesCounterUSService {

    private static final Logger logger = LoggerFactory.getLogger(IndexQuotesCounterUSService.class);

    @Autowired
    private NameIdHandler nameIdHandler;

    @Autowired()
    @Qualifier("indexQuoteCounterUSRepository")
    private IndexQuoteCounterUSRepository quoteCounterRepository;

    public void addStockQuoteCounter(final Long customerId, final Set<String> exchangeSet, final String market, final Map<String, List<Indice>> labciQuoteWithExchge, final String quoteType) {
        CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
        String entityCode = header.getAppCode();

        if(entityCode!=null&&!entityCode.equalsIgnoreCase("CMB")){
            entityCode = "WPB";
        } else {
            //do nothing
        }

        IndexQuoteCounterUSPo quoteCounter = new IndexQuoteCounterUSPo();
        quoteCounter.setPlayerReferenceNumber(customerId);
        quoteCounter.setTradingMarketCode(market);
        quoteCounter.setQuoteType(quoteType);
        Calendar instance = Calendar.getInstance();
        instance.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        quoteCounter.setQuoteYear(DateUtil.getCurrentYear(instance));
        quoteCounter.setQuoteMonth(DateUtil.getCurrentMonth(instance));
        quoteCounter.setEntityCode(entityCode);
        List<IndexQuoteCounterUSPo> quoteCounterList = this.quoteCounterRepository.findAll(Example.of(quoteCounter, ExampleMatcher.matching().withIgnorePaths("quoteCounter")));
        Set<String> unsaveType = new HashSet<String>();
        long totalQuoteCounter = 0;
        Map<String, Long> detailQuoteCounterMap = new HashMap<String, Long>();
        Timestamp timestamp = new Timestamp(DateUtil.getMachineDateTime().getTime());
        unsaveType.addAll(exchangeSet);
        for (IndexQuoteCounterUSPo qCounter : quoteCounterList) {
            if (exchangeSet.contains(qCounter.getSubQuoteType())) {
                unsaveType.remove(qCounter.getSubQuoteType());
            }
        }
        if (unsaveType.size() > 0) {
            for (String subQuoteType : unsaveType) {
                IndexQuoteCounterUSPo unsaveQuoteCounter = new IndexQuoteCounterUSPo();
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
                this.quoteCounterRepository.saveAndFlush(unsaveQuoteCounter);
                detailQuoteCounterMap.put(subQuoteType, newquoteUsageCounter.longValue());
                totalQuoteCounter = totalQuoteCounter + newquoteUsageCounter;
            }
        }
        long sumQuoteUsage = 0;
        long sumQuoteCounter = 0;
        for (Map.Entry<String, List<Indice>> quoteUsage : labciQuoteWithExchge.entrySet()) {
            sumQuoteUsage = sumQuoteUsage + quoteUsage.getValue().size();
        }

        for (IndexQuoteCounterUSPo aQuoteCounter : quoteCounterList) {
            sumQuoteCounter = sumQuoteCounter + aQuoteCounter.getQuoteCounter();
        }

        if (sumQuoteUsage + sumQuoteCounter <= IndexLabciProtalService.LARGE_QUOTE_COUNT) {
            for (IndexQuoteCounterUSPo aQuoteCounter : quoteCounterList) {
                if (labciQuoteWithExchge.get(aQuoteCounter.getSubQuoteType()) != null) {
                    aQuoteCounter.setLastUpdate(timestamp);
                    int returnCode = this.quoteCounterRepository.addQuoteAndUpdate(
                            labciQuoteWithExchge.get(aQuoteCounter.getSubQuoteType()).size(), timestamp,
                            aQuoteCounter.getPlayerReferenceNumber(), aQuoteCounter.getTradingMarketCode(),
                            aQuoteCounter.getQuoteYear(), aQuoteCounter.getQuoteMonth(), aQuoteCounter.getQuoteType(),
                            aQuoteCounter.getSubQuoteType());
                    if (returnCode > 0) {
                        detailQuoteCounterMap.put(aQuoteCounter.getSubQuoteType(), aQuoteCounter.getQuoteCounter());
                        totalQuoteCounter = totalQuoteCounter + aQuoteCounter.getQuoteCounter();
                    }
                }
            }
        } else {
            // quote count exceed error
            IndexQuotesCounterUSService.logger.error(IndexQuotesLogUSService.QUOTE_COUNT_EXCEED_ERROR);
            for (IndexQuoteCounterUSPo aQuoteCounter : quoteCounterList) {
                detailQuoteCounterMap.put(aQuoteCounter.getSubQuoteType(), aQuoteCounter.getQuoteCounter());
                totalQuoteCounter = totalQuoteCounter + aQuoteCounter.getQuoteCounter();
            }
        }
    }

    public String getUserType() {
        CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
        //customerId|OHI|OHM|CUST,staffId|OHCC|OHB|OHBR|STFF
        String userType = IndexLabciProtalService.USER_TYPE_CDE_CUST;
        String channelId = header.getChannelId();
        if (this.nameIdHandler.getCustomerChannel().contains(channelId)) {
            userType = IndexLabciProtalService.USER_TYPE_CDE_CUST;
        } else if (this.nameIdHandler.getStaffChannel().contains(channelId)) {
            userType = IndexLabciProtalService.USER_TYPE_CDE_STFF;
        } else {
            IndexQuotesCounterUSService.logger.warn("Undefined channelId: " + channelId);
        }
        return userType;
    }

}
