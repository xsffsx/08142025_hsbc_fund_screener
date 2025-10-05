/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.index.service;

import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.index.dao.IndexAccessLogUSRepository;
import com.hhhh.group.secwealth.mktdata.api.equity.index.dao.IndexPlayerUSRepository;
import com.hhhh.group.secwealth.mktdata.api.equity.index.dao.entiry.IndexAccessLogUSPo;
import com.hhhh.group.secwealth.mktdata.api.equity.index.dao.entiry.IndexPlayerUSPo;
import com.hhhh.group.secwealth.mktdata.api.equity.index.request.IndexQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.index.response.Indice;
import com.hhhh.group.secwealth.mktdata.api.equity.index.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.index.util.TradingHourUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.datasource.aspect.annotation.SelectDatasource;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("indexQuotesLogAmhCNService")
@ConditionalOnProperty(value = "service.quotes.Labci.injectEnabled")
public class IndexQuotesLogUSService {

    private static final Logger logger = LoggerFactory.getLogger(IndexQuotesLogUSService.class);

    private static final String ZERO = "0";

    private static final String SYMBOL_VERTICAL = "|";

    private static final String SYMBOL_SEMICOLON = ";";

    private static final String SYMBOL_COLON = ":";

    private static final String KEY_L = "L=";

    public static final String QUOTE_COUNT_EXCEED_ERROR = "CN Market QuoteCount Exceed Error QMS003";

    @Autowired()
    @Qualifier("indexQuotesCounterUSService")
    private IndexQuotesCounterUSService quotesCounterUSService;

    @Autowired()
    @Qualifier("indexPlayerUSRepository")
    private IndexPlayerUSRepository playerRepository;

    @Autowired()
    @Qualifier("indexAccessLogUSRepository")
    private IndexAccessLogUSRepository accessLogRepository;

    @Autowired()
    @Qualifier("labciProtalIndicesService")
    private IndexLabciProtalService indexLabciProtalService;



    @SelectDatasource
    public void updateQuoteMeterAndLog(final Map<String, List<Indice>> labciQuoteWithExchge) throws Exception {
        IndexQuotesRequest request = (IndexQuotesRequest) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_REQUEST);
        CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
        String remarkField1 = header.getAppCode() +"_"+ header.getChannelId();
        String customerId = (String) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_RAW_NAME_ID);
        if (StringUtil.isInValid(customerId)) {
            customerId = (String) ArgsHolder.getArgs(IndexLabciProtalService.CUSTOMER_ID);
        }
        IndexPlayerUSPo player = new IndexPlayerUSPo();
        player.setPlayerType(this.quotesCounterUSService.getUserType());
        player.setPlayerId(customerId);
        player.setCountryCode(header.getCountryCode());
        player.setGroupMember(header.getGroupMember());
        IndexPlayerUSPo quoteUser = this.playerRepository.findOne(Example.of(player, ExampleMatcher.matching().withIgnorePaths("playerReferenceNumber")));
        Long playerReferenceNumber = 718529l;
        if (quoteUser == null) {// ConcurrentLogonValidator update time or not
            player.setLastLogonTime(new Timestamp(0));
            player.setLastUpdate(new Timestamp(DateUtil.getMachineDateTime().getTime()));
            IndexPlayerUSPo savedPlayer = this.playerRepository.save(player);
            playerReferenceNumber = savedPlayer.getPlayerReferenceNumber();
        } else {
            playerReferenceNumber = quoteUser.getPlayerReferenceNumber();
        }
        String market = request.getMarket();
        Set<String> exchangeSet = labciQuoteWithExchge.keySet();


        Boolean isTradingHour = false;

        if (this.indexLabciProtalService.checkTradingDate(header)) {
            if (TradingHourUtil.withinTradingHour(true, this.indexLabciProtalService.checkTradingHour(header))) {
                isTradingHour = true;
            }
        }
        if (isTradingHour) {
            //add Index quote counter
            this.quotesCounterUSService.addStockQuoteCounter(playerReferenceNumber, exchangeSet, market, labciQuoteWithExchge, IndexLabciProtalService.ACCES_TYPE_REAL_TIME);
            //add access log
            addAccessLog(playerReferenceNumber, market, labciQuoteWithExchge, IndexLabciProtalService.ACCES_TYPE_REAL_TIME,remarkField1);
        } else {
            this.quotesCounterUSService.addStockQuoteCounter(playerReferenceNumber, exchangeSet, market, labciQuoteWithExchge, IndexLabciProtalService.ACCES_TYPE_DELAY);
            addAccessLog(playerReferenceNumber, market, labciQuoteWithExchge, IndexLabciProtalService.ACCES_TYPE_DELAY,remarkField1);
        }

    }

    public void addAccessLog(final Long customerId, final String market, final Map<String, List<Indice>> labciQuoteWithExchge, final String accessType, final String remarkField1) {
        IndexAccessLogUSPo accessLog = new IndexAccessLogUSPo();
        StringBuffer sb = new StringBuffer();
        int countUsage = 0;
        for (Map.Entry<String, List<Indice>> quoteUsage : labciQuoteWithExchge.entrySet()) {
            String exchangeName = quoteUsage.getKey();
            sb.append(exchangeName);
            sb.append(IndexQuotesLogUSService.SYMBOL_COLON);
            sb.append(quoteUsage.getValue().size());
            sb.append(IndexQuotesLogUSService.SYMBOL_COLON);
            for (Indice index : quoteUsage.getValue()) {
                sb.append(symbolToStr(index.getSymbol(), index.getLastPrice().toString()));
                sb.append(IndexQuotesLogUSService.SYMBOL_SEMICOLON);
                countUsage++;
            }
            sb.append(IndexQuotesLogUSService.SYMBOL_VERTICAL);
        }

        Timestamp timestamp = new Timestamp(DateUtil.getMachineDateTime().getTime());
        accessLog.setPlayerReferenceNumber(customerId);
        accessLog.setTradingMarketCode(market);
        accessLog.setChargeableFlag(IndexQuotesLogUSService.ZERO);
        accessLog.setChargeCategory(IndexQuotesLogUSService.ZERO);
        accessLog.setQuoteUsage(countUsage);
        accessLog.setAccessType(accessType);
        accessLog.setAccessCommand((String) ArgsHolder.getArgs(IndexLabciProtalService.ACCES_CMND_CDE));
        accessLog.setAccessDetail(sb.toString());
        accessLog.setRemarkField(remarkField1);
        accessLog.setLastUpdate(timestamp);
        this.accessLogRepository.save(accessLog);
    }

    private String symbolToStr(final String symbol, final String quote) {
        String priceDetail = "";
        if (null != quote) {
            priceDetail = IndexQuotesLogUSService.KEY_L + quote;
        }
        String result =
            symbol + SymbolConstant.SYMBOL_LEFT_CIRCLE_BRACKET + priceDetail + SymbolConstant.SYMBOL_RIGHT_CIRCLE_BRACKET;
        return result;
    }

}
