package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci.LabciResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci.Ric;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci.Watchlist;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.ApplicationProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.EquityHKLabciProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.EquityHKMarketHourProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.LabciPropsUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.LabciServletBoConvertor;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.QuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.SECQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ServiceProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.service.QuotesServiceRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.Message;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesLabciQuote;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesLabciResponse;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResult;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.utils.CacheDistributeHolder;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.ExResponseComponent;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.entity.ExResponseEntity;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

@Service("quotesHKLabciService")
@ConditionalOnProperty(value = "service.quotes.hkEquity.injectEnabled", havingValue = "true")
public class QuotesHKLabciServiceImpl
		extends AbstractBaseService<SECQuotesRequest, QuotesLabciResponse, CommonRequestHeader> {

	private static final Logger logger = LoggerFactory.getLogger(QuotesHKLabciServiceImpl.class);

	private static final String DATAREPRESENTATION_XML = "XML";

	private static final String ACCES_CMND_CDE = "QUOTE_LABCI_ACCES_CMND_CDE";

	private static final String ACCES_CMND_CDE_QUOTE_DETAIL = "QUOTE_DETAIL";

	private static final String ACCES_CMND_CDE_QUOTE_LIST = "QUOTE_LIST";

	public static final String THREAD_INVISIBLE_DELAY = "QUOTE_DELAY";

	private static final String SATFF_CHANNEL_ID = "OHB";

	public static long LARGE_QUOTE_COUNT = 99999999;

	public static final String QUOTE_COUNT_EXCEED_ERROR = "QMS003";

	public static final String ZERO = "0";

	public static final String SYMBOL_VERTICAL = "|";

	public static final String SYMBOL_SEMICOLON = ";";

	public static final String SYMBOL_COLON = ":";

	@Autowired
	private PredSrchService predSrchService;

	@Autowired
	private ExResponseComponent exRespComponent;

	@Autowired
	private ApplicationProperties appProps;

	@Autowired
	private EquityHKLabciProperties labciProps;

	@Autowired
	private HttpClientHelper httpClientHelper;

	@Autowired
	private EquityHKMarketHourProperties marketHourProperties;

	@Autowired
	@Qualifier("quotesQuoteUserService")
	private QuoteUserService quoteUserService;

	@Autowired
	@Qualifier("quotesQuoteAccessLogService")
	private QuoteAccessLogService quoteAccessLogService;

	@Autowired
	private QuotesHKCommonService quotesHKCommonService;

	@Autowired
	private QuotesHKLogService quotesHKLogService;

	@Override
	protected Object convertRequest(final SECQuotesRequest request, final CommonRequestHeader header) throws Exception {

		if (!QuotesHKLabciServiceImpl.SATFF_CHANNEL_ID.equalsIgnoreCase(header.getChannelId())) {
			CacheDistributeResult result = CacheDistributeHolder.getCacheDistribute();
			this.quotesHKCommonService.validateCacheDistributeResult(result);
		}
		ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER, header);
		ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_QUOTES_LABCI_REQUEST, request);
		QuotesServiceRequest quotesServiceRequest = quotesHKCommonService.buildServiceRequest(request, header,
				Constant.PROD_CDE_ALT_CLASS_CODE_T);
		if (Constant.CASE_20.equals(request.getRequestType())) {
			ArgsHolder.putArgs(QuotesHKLabciServiceImpl.ACCES_CMND_CDE,
					QuotesHKLabciServiceImpl.ACCES_CMND_CDE_QUOTE_DETAIL);
		} else {
			ArgsHolder.putArgs(QuotesHKLabciServiceImpl.ACCES_CMND_CDE,
					QuotesHKLabciServiceImpl.ACCES_CMND_CDE_QUOTE_LIST);
		}
		List<ServiceProductKey> serviceProductKeys = quotesServiceRequest.getServiceProductKeys();
		String tCode = "";
		boolean sendDelay = isDelay(request);
		if (serviceProductKeys != null && !serviceProductKeys.isEmpty()) {
			for (int i = 0; i < serviceProductKeys.size(); i++) {
				tCode = serviceProductKeys.get(i).getProdAltNum();// get(0)?
				PredSrchResponse predResp = this.predSrchService.localPredSrch(tCode);
				if (predResp != null && this.marketHourProperties
						.hasExchangeCode(this.labciProps.getExchangeMapping(predResp.getExchange()))) {
					if (!isDelay(request)) {
						sendDelay = this.marketHourProperties.checkTradingHoursByExchange(
								this.labciProps.getExchangeMapping(predResp.getExchange()));
						break;
					}
				}
			}
		}
		ArgsHolder.putArgs(QuotesHKLabciServiceImpl.THREAD_INVISIBLE_DELAY, sendDelay);// any function?
		Map<String, String> serviceRequestMapper = new LinkedHashMap<>();
		String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
		String service = this.labciProps.getLabciService(site, false);
		Map<String, List<ServiceProductKey>> groupedProductKeysMapper = getGroupedProductKeysMapper(
				quotesServiceRequest);
		for (Map.Entry<String, List<ServiceProductKey>> groupedProductKeys : groupedProductKeysMapper.entrySet()) {
			String key = groupedProductKeys.getKey();
			String[] keys = key.split(SymbolConstant.SYMBOL_VERTICAL_LINE_ESCAPE);
			List<ServiceProductKey> productKeys = groupedProductKeys.getValue();
			List<String> items = new ArrayList<>();
			for (int i = 0; i < productKeys.size(); i++) {
				ServiceProductKey productKey = productKeys.get(i);
				items.add(productKey.getProdAltNum());// 600685.SS
			}
			// fieldList are common between bigAsk/normal query
			String fieldList = StringUtils.join(
					this.labciProps.getLabciFields(this.appProps.getQuotesResponseLabciFields(keys), keys),
					SymbolConstant.SYMBOL_SEMISOLON);
			String symbolList = LabciServletBoConvertor.genSymbols(items, service);
			String labciBigAskSymbolList = LabciServletBoConvertor.genSymbolsForQuote(items, service);
			List<NameValuePair> labciParams = new ArrayList<NameValuePair>();
			labciParams.add(new BasicNameValuePair("SymbolList", symbolList + ";" + labciBigAskSymbolList));
			labciParams.add(new BasicNameValuePair("FieldList", fieldList));// really very complicated,is it necessary?
			labciParams
					.add(new BasicNameValuePair("DataRepresentation", QuotesHKLabciServiceImpl.DATAREPRESENTATION_XML));
			String labciReqParams = URLEncodedUtils.format(labciParams, "UTF-8");

			// String labciBigAskSymbolList =
			// LabciServletBoConvertor.genSymbolsForQuote(items, service);
			// List<NameValuePair> labciBigAskParams = new ArrayList<NameValuePair>();
			// labciBigAskParams.add(new BasicNameValuePair("SymbolList",
			// labciBigAskSymbolList));
			// labciBigAskParams.add(new BasicNameValuePair("FieldList",fieldList));// same
			// as before?can skip and fetch directly?
			// labciBigAskParams
			// .add(new BasicNameValuePair("DataRepresentation",
			// QuotesHKLabciServiceImpl.DATAREPRESENTATION_XML));
			// String labciBigAskReqParam = URLEncodedUtils.format(labciBigAskParams,
			// "UTF-8");
			serviceRequestMapper.put(key, labciReqParams);
		}
		return serviceRequestMapper;
	}

	private Map<String, List<ServiceProductKey>> getGroupedProductKeysMapper(final QuotesServiceRequest request) {
		Map<String, List<ServiceProductKey>> groupedProductKeysMapper = new LinkedHashMap<>();
		String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
		List<ServiceProductKey> productKeys = request.getServiceProductKeys();
		for (int i = 0; i < productKeys.size(); i++) {
			ServiceProductKey productKey = productKeys.get(i);
			String key = new StringBuilder().append(site).append(SymbolConstant.SYMBOL_VERTICAL_LINE)
					.append(productKey.getProductType()).append(SymbolConstant.SYMBOL_VERTICAL_LINE)
					.append(productKey.getExchange()).toString();
			if (groupedProductKeysMapper.containsKey(key)) {
				groupedProductKeysMapper.get(key).add(productKey);
			} else {
				List<ServiceProductKey> groupedProductKeys = new ArrayList<>();
				groupedProductKeys.add(productKey);
				groupedProductKeysMapper.put(key, groupedProductKeys);
			}
		}
		return groupedProductKeysMapper;
	}

	private boolean isDelay(final QuotesRequest request) {
		if (request.getDelay() != null) {
			return request.getDelay();
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService#
	 * execute(java.lang.Object)
	 */
	@Override
	protected Object execute(final Object serviceRequest) throws Exception {
		Map<String, String> serviceResponseMapper = new LinkedHashMap<>();
		@SuppressWarnings("unchecked")
		Map<String, String> serviceRequestMapper = (Map<String, String>) serviceRequest;

		for (Map.Entry<String, String> requestMapper : serviceRequestMapper.entrySet()) {
			String key = requestMapper.getKey();
			String request = requestMapper.getValue();
			serviceResponseMapper.put(key, CallLabciForBigAsk(request));
		}
		return serviceResponseMapper;
	}

	private String CallLabciForBigAsk(String request) throws Exception {
		// int index = request.indexOf(QuotesHKLabciServiceImpl.SPECIAL_SEPARATOR);
		// String labciRequest = request.substring(0, index);
		// String labciBigAskRequest = request.substring(index +
		// QuotesHKLabciServiceImpl.SPECIAL_SEPARATOR.length());
		String labciResponse = "";
		try {
			labciResponse = this.httpClientHelper.doGet(this.labciProps.getLabciUrl(), request, null);
		} catch (Exception e) {
			if (e instanceof IOException) {
				QuotesHKLabciServiceImpl.logger.error("Labci Undefined error", e);
				throw new VendorException(ExCodeConstant.EX_CODE_LABCI_UNDEFINED_ERROR, e);
			} else {
				QuotesHKLabciServiceImpl.logger.error("Labci server error", e);
				throw new VendorException(ExCodeConstant.EX_CODE_LABCI_SERVER_ERROR, e);
			}
		}
		// String labciBigAskResponse = "";
		// try {
		// labciBigAskResponse =
		// this.httpClientHelper.doGet(this.labciProps.getLabciUrl(),
		// labciBigAskRequest, null);
		// } catch (Exception e) {
		// QuotesHKLabciServiceImpl.logger.error("Access labci encounter error", e);
		// }
		// String response = labciResponse;
		return labciResponse;
	}

	@Override
	protected Object validateServiceResponse(final Object serviceResponse) throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, String> serviceResponseMapper = (Map<String, String>) serviceResponse;
		Object validServiceResponseMapper = new Object();
		validServiceResponseMapper = validateLabciResponse(serviceResponseMapper);
		return validServiceResponseMapper;
	}

	@SuppressWarnings("unchecked")
	private Object validateLabciResponse(Map<String, String> serviceResponseMapper) throws Exception {
		Map<String, Map<String, LabciResponse>> validServiceResponseMapper = new LinkedHashMap<>();
		List<ProductKey> invalidateList = new ArrayList<ProductKey>();
		for (Map.Entry<String, String> responseMapper : serviceResponseMapper.entrySet()) {
			String key = responseMapper.getKey();
			Map<String, LabciResponse> map = validateEachLabciResponse(key, responseMapper.getValue());
			if (!map.isEmpty()) {
				validServiceResponseMapper.put(key, map);
			}
			List<ProductKey> keyList = (List<ProductKey>) ArgsHolder.getArgs(Constant.CONTAINED_PROD_KEYS);
			for (ProductKey productKey : keyList) {
				if (!map.containsKey(productKey.getProdAltNum())) {
					invalidateList.add(productKey);
				}
			}
		}
		if (invalidateList.size() > 0) {
			ExResponseEntity exResponse = this.exRespComponent
					.getExResponse(ExCodeConstant.EX_CODE_LABCI_STOCK_NOT_FOUND);
			quotesHKCommonService.addMessage(invalidateList, exResponse);
		}
		try {
			long startTime = System.currentTimeMillis();
			// saveAccessLogLabci(validServiceResponseMapper);
			long endTime = System.currentTimeMillis();
			// System.out.println("--------------------save to db
			// lasts------------------------"+(endTime-startTime));
		} catch (Exception e) {
			QuotesHKLabciServiceImpl.logger.error("Error: Failed to write quotes history", e);
			throw new ApplicationException(ExCodeConstant.EX_CODE_DB_ACTION_FAIL);
		}
		return validServiceResponseMapper;
	}

	private Map<String, LabciResponse> validateEachLabciResponse(final String key, final String serviceResponse)
			throws JAXBException {
		// int index =
		// serviceResponse.indexOf(QuotesHKLabciServiceImpl.SPECIAL_SEPARATOR);
		// String labciResponses = serviceResponse;
		// String labciBigAskResponses = "";
		// if (index > -1) {
		// labciResponses = serviceResponse.substring(0, index);
		// labciBigAskResponses = serviceResponse
		// .substring(index + QuotesHKLabciServiceImpl.SPECIAL_SEPARATOR.length());
		// }
		JAXBContext jaxbContext = JAXBContext.newInstance(Watchlist.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		Watchlist res = (Watchlist) unmarshaller.unmarshal(new StringReader(serviceResponse));
		Map<String, LabciResponse> response = new LinkedHashMap<>();
		if (null != res) {
			List<Ric> ricList = res.getRic();
			if (!ListUtils.isEmpty(ricList)) {
				response = LabciServletBoConvertor.getResponseMap(ricList);
			}
		}

		// Map<String, LabciResponse> labciBigAskResponse = new LinkedHashMap<>();
		// try {
		// if (StringUtil.isValid(labciBigAskResponses)) {
		// Watchlist labciBigAskRes = (Watchlist) unmarshaller.unmarshal(new
		// StringReader(labciBigAskResponses));
		// if (null != labciBigAskRes) {
		// List<Ric> ricList = labciBigAskRes.getRic();
		// if (!ListUtils.isEmpty(ricList)) {
		// labciBigAskResponse = LabciServletBoConvertor.getResponseMap(ricList);
		// }
		// }
		// }
		// } catch (Exception e) {
		// QuotesHKLabciServiceImpl.logger.error("Access labci encounter error", e);
		// }

		response = convertLabciResponse(response, key);

		return response;
	}

	private Map<String, LabciResponse> convertLabciResponse(Map<String, LabciResponse> response, String key) {
		for (Map.Entry<String, LabciResponse> mEntry : response.entrySet()) {
			if (response.containsKey(mEntry.getKey() + "d")) {
				Map<String, Object> data = mEntry.getValue().getFields();
				Map<String, Object> data2 = response.get(mEntry.getKey() + "d").getFields();
				String[] bidAskQueues = LabciPropsUtil.getLabciFields("bidAskQueues", key);
				for (int i = 0; i < bidAskQueues.length; i++) {
					if (i < 6) {
						data.put(bidAskQueues[i],
								Constant.ZERO.equals(data2.get(bidAskQueues[i]).toString()) ? data.get(bidAskQueues[i])
										: data2.get(bidAskQueues[i]));
					} else {
						data.put(bidAskQueues[i], data2.get(bidAskQueues[i]));
					}
				}
				mEntry.getValue().setFields(data);
			}
		}
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService#
	 * convertResponse(java.lang.Object)
	 */
	@Override
	protected QuotesLabciResponse convertResponse(final Object validServiceResponse) throws Exception {
		QuotesLabciResponse response = new QuotesLabciResponse();
		response.setPriceQuotes(getQuotesFromlabciResponse(validServiceResponse));
		response.setMessages(getMessages());
		response.setRemainingQuota(new BigDecimal("-1"));
		response.setTotalQuota(new BigDecimal("-1"));
		return response;
	}

	private List<QuotesLabciQuote> getQuotesFromlabciResponse(Object validServiceResponse) {
		@SuppressWarnings("unchecked")
		Map<String, Map<String, LabciResponse>> resultJsonArrayMapper = (Map<String, Map<String, LabciResponse>>) validServiceResponse;
		List<QuotesLabciQuote> priceQuotes = new ArrayList<>();
		Map<String, List<QuotesLabciQuote>> labciQuoteWithExchge = new HashMap<String, List<QuotesLabciQuote>>();
		CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
		for (Map.Entry<String, Map<String, LabciResponse>> jsonArrayMapper : resultJsonArrayMapper.entrySet()) {
			String key = jsonArrayMapper.getKey();
			Map<String, LabciResponse> resultJsonArray = jsonArrayMapper.getValue();
			for (Map.Entry<String, LabciResponse> jsonArrayMapper2 : resultJsonArray.entrySet()) {
				Map<String, Object> data = jsonArrayMapper2.getValue().getFields();
				QuotesLabciQuote priceQuote = new QuotesLabciQuote();
				PredSrchResponse predSrchResp = this.predSrchService.localPredSrch(jsonArrayMapper2.getKey(),
						jsonArrayMapper.getKey().split(SymbolConstant.SYMBOL_VERTICAL_LINE_ESCAPE)[1],
						Constant.PROD_CDE_ALT_CLASS_CODE_T);
				if (predSrchResp != null) {
					priceQuote.setProdAltNumSegs(predSrchResp.getProdAltNumSegs());
					priceQuote.setSymbol(predSrchResp.getSymbol());
					priceQuote.setMarket(predSrchResp.getMarket());
					priceQuote.setExchangeCode(this.labciProps.getExchangeMapping(predSrchResp.getExchange()));
					priceQuote.setProductType(predSrchResp.getProductType());
					priceQuote.setProductSubType(predSrchResp.getProductSubType());
					priceQuote.setCompanyName(StringUtil.isInValid(predSrchResp.getProductName())
							? companyName(key, data, header.getLocale())
							: predSrchResp.getProductName());
					priceQuote.setRiskLvlCde(predSrchResp.getRiskLvlCde());
					priceQuote.setAsOfDateTime(LabciPropsUtil.dateProps("asOfDateTime", key, data,
							this.appProps.getTimezone(this.labciProps.getExchangeMapping(predSrchResp.getExchange()))));
				} else {
					continue;
				}
				priceQuote.setNominalPrice(LabciPropsUtil.inOrderNumberProps("nominalPrice", key, data));
				priceQuote.setCurrency(LabciPropsUtil.inOrderStrProps("currency", key, data));
				try {
					String quoteOffClose = LabciPropsUtil.inOrderStrProps("quoteOffClose", key, data);
					if (Constant.ZERO.equals(quoteOffClose) || StringUtil.isInValid(quoteOffClose)) {
						priceQuote.setChangeAmount(LabciPropsUtil.subtractProps("changeAmount", key, data));
						priceQuote.setChangePercent(LabciPropsUtil.growthRateProps("changePercent", key, data,
								new MathContext(4, RoundingMode.HALF_UP)));
					} else {
						priceQuote.setChangeAmount(LabciPropsUtil.subtractProps("offCloseSubChangeAmount", key, data));
						priceQuote.setChangePercent(LabciPropsUtil.growthRateProps("offCloseSubChangePercent", key,
								data, new MathContext(4, RoundingMode.HALF_UP)));
					}
				} catch (Exception e) {
					priceQuote.setChangeAmount(LabciPropsUtil.subtractProps("changeAmount", key, data));
					priceQuote.setChangePercent(LabciPropsUtil.growthRateProps("changePercent", key, data,
							new MathContext(4, RoundingMode.HALF_UP)));
				}
				priceQuote.setDelay((boolean) ArgsHolder.getArgs(QuotesHKLabciServiceImpl.THREAD_INVISIBLE_DELAY));

				priceQuote.setBidPrice(LabciPropsUtil.inOrderNumberProps("bidPrice", key, data));
				priceQuote.setBidSize(LabciPropsUtil.inOrderNumberProps("bidSize", key, data));
				priceQuote.setAskPrice(LabciPropsUtil.inOrderNumberProps("askPrice", key, data));
				priceQuote.setAskSize(LabciPropsUtil.inOrderNumberProps("askSize", key, data));
				priceQuote.setBidSpread(LabciPropsUtil.inOrderNumberProps("bidSpread", key, data));
				priceQuote.setAskSpread(LabciPropsUtil.inOrderNumberProps("askSpread", key, data));
				priceQuote.setOpenPrice(LabciPropsUtil.inOrderNumberProps("openPrice", key, data));
				priceQuote.setPreviousClosePrice(LabciPropsUtil.inOrderNumberProps("previousClosePrice", key, data));
				priceQuote.setDayLowPrice(LabciPropsUtil.inOrderNumberProps("dayLowPrice", key, data));
				priceQuote.setDayHighPrice(LabciPropsUtil.inOrderNumberProps("dayHighPrice", key, data));
				priceQuote.setYearLowPrice(LabciPropsUtil.inOrderNumberProps("yearLowPrice", key, data));
				priceQuote.setYearHighPrice(LabciPropsUtil.inOrderNumberProps("yearHighPrice", key, data));
				priceQuote.setVolume(LabciPropsUtil.inOrderNumberProps("volume", key, data));
				priceQuote.setBoardLotSize(LabciPropsUtil.inOrderNumberProps("boardLotSize", key, data));
				priceQuote.setMarketCap(LabciPropsUtil.inOrderNumberProps("marketCap", key, data));
				priceQuote.setPeRatio(LabciPropsUtil.inOrderNumberProps("peRatio", key, data));
				priceQuote.setEps(LabciPropsUtil.inOrderNumberProps("eps", key, data));
				priceQuote.setTurnover(LabciPropsUtil.inOrderNumberProps("turnover", key, data));
				priceQuote.setDividend(LabciPropsUtil.inOrderNumberProps("dividend", key, data));
				priceQuote.setDividendYield(LabciPropsUtil.inOrderNumberProps("dividendYield", key, data));
				priceQuote.setBidAskQueues(LabciPropsUtil.bidAskQueues(key, data));

				priceQuote.setUpperLimitPrice(LabciPropsUtil.inOrderNumberProps("upperTradingLimit", key, data));
				priceQuote.setLowerLimitPrice(LabciPropsUtil.inOrderNumberProps("lowerTradingLimit", key, data));

				priceQuote.setSharesOutstanding(LabciPropsUtil.inOrderNumberProps("sharesOutstanding", key, data));
				priceQuote.setNominalPriceType(LabciPropsUtil.inOrderStrProps("quoteNominalPriceType", key, data));
				priceQuote.setIsSuspended(LabciPropsUtil.inOrderBooPropsForIsSuspended(
						LabciPropsUtil.inOrderStrProps("quoteNominalPriceType", key, data), header.getCountryCode(),
						this.labciProps.getSuspendFlagConfig()));

				priceQuote.setIep(LabciPropsUtil.inOrderNumberProps("iep", key, data));
				priceQuote.setIev(LabciPropsUtil.inOrderNumberProps("iev", key, data));

				priceQuote.setRiskAlert(LabciPropsUtil
						.inOrderBooPropsForRiskAlert(LabciPropsUtil.inOrderStrProps("riskAlert", key, data)));
				priceQuotes.add(priceQuote);
				// String exchange = key.substring(key.lastIndexOf("|") + 1);//change the
				// exchange at the source
				String mappedExchangeName = this.labciProps.getExchangeMapping(priceQuote.getExchangeCode());//
				if (labciQuoteWithExchge.get(mappedExchangeName) != null) {
					labciQuoteWithExchge.get(mappedExchangeName).add(priceQuote);
				} else {
					List<QuotesLabciQuote> labciQuoteList = new ArrayList<>();
					labciQuoteList.add(priceQuote);
					labciQuoteWithExchge.put(mappedExchangeName, labciQuoteList);
				}
				// updateQuoteMeterAndLog(priceQuote);
			}
		}
		quotesHKLogService.updateQuoteMeterAndLog("labci", labciQuoteWithExchge);
		return priceQuotes;
	}

	private String companyName(String key, Map<String, Object> data, String local) {
		// String name = LabciPropsUtil.inOrderStrProps("localName", key, data);
		// if (StringUtil.isValid(name)) {
		// String[] names = name.split(SymbolConstant.SYMBOL_VERTICAL_LINE_ESCAPE);
		// switch (local) {
		// case "zh_CN":
		// return names[1];
		// case "zh_HK":
		// return names[0];
		// default:
		// return LabciPropsUtil.inOrderStrProps("quoteName", key, data);
		// }
		// }
		return LabciPropsUtil.inOrderStrProps("quoteName", key, data);
	}

	@SuppressWarnings("unchecked")
	private List<Message> getMessages() {
		if (ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES) == null) {
			return null;
		} else {
			return (List<Message>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES);
		}
	}
}
