package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
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
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

@Service
@ConditionalOnProperty(value = "service.quotes.hkEquity.injectEnabled", havingValue = "true")
public class QuotesHKLogService {

	private static final Logger logger = LoggerFactory.getLogger(QuotesHKLogService.class);

	protected static final String THREAD_CACHE_NODE = "node";

	protected static final String SATFF_CHANNEL_ID = "OHB";

	protected static final String ACCES_CMND_CDE = "QUOTE_LABCI_ACCES_CMND_CDE";

	protected static final String KEY_L = "L=";

	protected static final String USER_TYPE_CDE_CUST = "CUST";

	protected static final String USER_TYPE_CDE_STFF = "STFF";

	protected static final String QUOTE_TYPE_REAL_STOCK = "STOCK";

	protected static final String QUOTE_TYPE_DELAY_STOCK = "EDSTK";

	@Autowired
	private QuoteCounterRepository quoteCounterRepository;

	@Autowired
	private QuoteAccessLogRepository quoteAccessLogRepository;

	@Autowired
	private QuotePlayerRepository quotePlayerRepository;

	@SelectDatasource
	public void updateQuoteMeterAndLog(String datasource, Map<String, List<QuotesLabciQuote>> labciQuoteWithExchge) {
		SECQuotesRequest request = (SECQuotesRequest) ArgsHolder
				.getArgs(Constant.THREAD_INVISIBLE_QUOTES_LABCI_REQUEST);
		// TODO: add it to common
		CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
		// getUserId //true,customerId|OHI|OHM|CUST,staffId|OHCC|OHB|OHBR|STFF
		// 这里有一个误区，老代码是从customerId/staffId获得值作为eid，
		// 并作为id存入，这里需要了解。(但是OHB不用通过distribute cache)
		Player player = new Player();
		Object userNode = ArgsHolder.getArgs(QuotesHKLogService.THREAD_CACHE_NODE);
		Timestamp lastLogonTime = new Timestamp(0);
		boolean updateFlag = false;
		Long customerId = 718529l;
		Timestamp currentTime = new Timestamp(DateUtil.getMachineDateTime().getTime());
		if (QuotesHKLogService.SATFF_CHANNEL_ID.equalsIgnoreCase(header.getChannelId())) {
			player.setPlayerType(QuotesHKLogService.USER_TYPE_CDE_STFF);
			player.setPlayerId("HK26719342388801B");//TODO: hardcode playerId
		} else {
			JsonNode node = (JsonNode) userNode;
			player.setPlayerType(QuotesHKLogService.USER_TYPE_CDE_CUST);
			player.setPlayerId(node.get("permID").asText());
		}
		if (userNode != null) {
			JsonNode node = (JsonNode) userNode;
			if (isNumeric(node.get("lastLogonTime").asText())) {
				lastLogonTime = new Timestamp(Long.parseLong(node.get("lastLogonTime").asText()));
				updateFlag = true;
			}
		}
		player.setCountryCode(header.getCountryCode());
		player.setGroupMember(header.getGroupMember());
		Player quoteUser = quotePlayerRepository
				.findOne(Example.of(player, ExampleMatcher.matching().withIgnorePaths("playerReferenceNumber")));
		if (quoteUser == null) {// ConcurrentLogonValidator update time or not
			player.setLastLogonTime(lastLogonTime);
			player.setLastUpdate(currentTime);
			Player savedPlayer = quotePlayerRepository.save(player);
			customerId = savedPlayer.getPlayerReferenceNumber();
		} else {
			customerId = quoteUser.getPlayerReferenceNumber();
			if (updateFlag) {
				if (lastLogonTime.compareTo(quoteUser.getLastLogonTime()) > 0) {
					quoteUser.setLastLogonTime(lastLogonTime);
					quoteUser.setLastUpdate(currentTime);
					quotePlayerRepository.save(quoteUser);
				}
			}
		}
		String market = request.getMarket();
		Set<String> exchangeSet = labciQuoteWithExchge.keySet();
		boolean sendDelay = (boolean) ArgsHolder.getArgs(QuotesHKLabciServiceImpl.THREAD_INVISIBLE_DELAY);
		TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
		if (!sendDelay) {// quoteDetail只在tradingHour才保存accessLog QuoteMeterWrapper.class
			addStockQuoteCounter(customerId, exchangeSet, market, labciQuoteWithExchge, QuotesHKLogService.QUOTE_TYPE_REAL_STOCK, timeZone);
			addAccessLog(customerId, exchangeSet, market, labciQuoteWithExchge, QuotesHKLogService.QUOTE_TYPE_REAL_STOCK);
		} else {
			addStockQuoteCounter(customerId, exchangeSet, market, labciQuoteWithExchge, QuotesHKLogService.QUOTE_TYPE_DELAY_STOCK, timeZone);
			addAccessLog(customerId, exchangeSet, market, labciQuoteWithExchge, QuotesHKLogService.QUOTE_TYPE_DELAY_STOCK);
		}
	}

	public static boolean isNumeric(final String str) {
		if (!isInvalid(str)) {
			Pattern pattern = Pattern.compile("[0-9]*");
			Matcher isNum = pattern.matcher(str);
			if (isNum.matches()) {
				return true;
			}
		}
		return false;
	}

	public static boolean isInvalid(final String str) {
		return (str == null || str.trim().length() == 0 || "null".equals(str));
	}

	protected String symbolToStr(final String symbol, final String quote) {
		String priceDetail = "";
		if (null != quote) {
			priceDetail = QuotesHKLogService.KEY_L + quote;
		}
		String result = symbol + SymbolConstant.SYMBOL_LEFT_CIRCLE_BRACKET + priceDetail
				+ SymbolConstant.SYMBOL_RIGHT_CIRCLE_BRACKET;
		return result;
	}

	protected void addAccessLog(Long customerId, Set<String> exchangeSet, String market,
			Map<String, List<QuotesLabciQuote>> labciQuoteWithExchge, String accessType) {
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
		accessLog.setQuoteUsage(countUsage);// will
		accessLog.setAccessType(accessType);// TODO: need to modify
		accessLog.setAccessCommand((String) ArgsHolder.getArgs(QuotesHKLogService.ACCES_CMND_CDE));// quotelist/detail
		accessLog.setAccessDetail(sb.toString());
		accessLog.setRemarkField("");
		accessLog.setLastUpdate(timestamp);
		quoteAccessLogRepository.save(accessLog);
	}

	// should return something?like quoteNum is not enough.
	protected void addStockQuoteCounter(Long customerId, Set<String> exchangeSet, String market,
			Map<String, List<QuotesLabciQuote>> labciQuoteWithExchge, String quoteType, TimeZone timeZone) {
		QuoteCounter quoteCounter = new QuoteCounter();
		quoteCounter.setPlayerReferenceNumber(customerId);// TODO: id not confirmed
		quoteCounter.setTradingMarketCode(market);
		quoteCounter.setQuoteType(quoteType);
		Calendar instance = Calendar.getInstance();
		instance.setTimeZone(timeZone);
		quoteCounter.setQuoteYear(DateUtil.getCurrentYear(instance));
		quoteCounter.setQuoteMonth(DateUtil.getCurrentMonth(instance));
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
		for (QuoteCounter qCounter : quoteCounterList) {
			if (!exchangeSet.contains(qCounter.getSubQuoteType())) {
				unsaveType.add(qCounter.getSubQuoteType());
			}
		}
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
				unsaveQuoteCounter.setLastUpdate(timestamp);// is it ok?
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

		for (QuoteCounter aQuoteCounter : quoteCounterList) {// 数据库所有值相加
			sumQuoteCounter = sumQuoteCounter + aQuoteCounter.getQuoteCounter();
		}

		if (sumQuoteUsage + sumQuoteCounter <= QuotesHKLabciServiceImpl.LARGE_QUOTE_COUNT) {// availableQuoteLimit基本上不可能超的，数值非常大
			for (QuoteCounter aQuoteCounter : quoteCounterList) {
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
