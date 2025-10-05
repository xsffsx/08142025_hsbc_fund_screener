/**
 * @Title QuoteLABCIService.java
 * @description TODO
 * @author OJim
 * @time May 30, 2019 11:50:08 AM
 */
package com.hhhh.group.secwealth.mktdata.api.equity.index.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.EtnetProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.LabciPropsUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.LabciServletBoConvertor;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.index.dao.entiry.QuoteAccessLogForIndex;
import com.hhhh.group.secwealth.mktdata.api.equity.index.dao.entiry.QuoteUserForIndex;
import com.hhhh.group.secwealth.mktdata.api.equity.index.request.IndexQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.index.response.IndexQuotesResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.index.response.Indice;
import com.hhhh.group.secwealth.mktdata.api.equity.index.response.Messages;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResponse;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResult;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResultStateEnum;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.utils.CacheDistributeHolder;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.exception.ExTraceCodeGenerator;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.ExResponseComponent;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.entity.ExResponseEntity;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ParameterException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

/**
 * @Title QuoteLABCIService.java
 * @description TODO
 * @author OJim
 * @time May 30, 2019 11:50:08 AM
 */
@Service("etnetIndicesService")
@ConditionalOnProperty(value = "service.quotes.Labci.injectEnabled")
public class IndexETNetService
		extends AbstractBaseService<IndexQuotesRequest, IndexQuotesResponse, CommonRequestHeader> {

	private static final Logger logger = LoggerFactory.getLogger(IndexETNetService.class);

	private static final String HK_MARKET_QUOTE_MARKET = "HK";

	private static final String GLOBAL_MARKET_QUOTE_MARKET = "GLOBAL";

	private static final String DEFAULT_OPTION = "DEFAULT|DEFAULT|DEFAULT";

	private static final String ACCES_TYPE_DELAY = "EDIDX";

	private static final String ACCES_TYPE_REAL_TIME = "INDEX";

	private static final String ACCES_CMND_CDE = "INDEX_ETNET_ACCES_CMND_CDE";

	private static final String ACCES_CMND_CDE_INDEX_DETAIL = "QUOTE_DETAIL";

	private static final String ACCES_CMND_CDE_INDEX_LIST = "QUOTE_LIST";

	private static final String INDEX_QUOTE_HK_SYMBOL_LIST = "|HSI|FSI|PSI|USI|CSI|CEI|CCI|HSC|HFI|GEM|TEH|";

	private static final String INDEX_QUOTE_GLOBAL_SYMBOL_LIST = "|NKI|KSI|KLSE|DJI|NDI|FTSE|GDAX|FCAC|";

	private static final String CUSTOMER_ID = "INDEX_ETNET_CUSTOMER_ID";

	private static final String USER_TYPE_CDE_CUST = "CUST";

	private static final String KEY_L = "L=";

	@Autowired
	private LabciProperties labciProps;

	@Autowired
	private ExResponseComponent exRespComponent;

	@Autowired
	private EtnetProperties etnetProperties;

	@Autowired
	private HttpClientHelper httpClientHelper;

	@Autowired()
	@Qualifier("indexQuoteUserService")
	private QuoteUserService quoteUserService;

	@Autowired()
	@Qualifier("indexQuoteAccessLogService")
	private QuoteAccessLogService quoteAccessLogService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService#
	 * convertRequest(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected Object convertRequest(IndexQuotesRequest request, CommonRequestHeader header) throws Exception {
		/**
		 * Before get result from the Cache Distribute, you can do some other business
		 * logic
		 */
		CacheDistributeResult result = CacheDistributeHolder.getCacheDistribute();
		CacheDistributeResultStateEnum resultState = result.getResultState();
		if (resultState == CacheDistributeResultStateEnum.OK) {
			CacheDistributeResponse response = result.getResponse();
			String value = response.getValue();
			// Get the value you are interested in
			ObjectMapper mapper = new ObjectMapper();
			try {
				JsonNode node = mapper.readTree(value);
				String customerId = node.get("eID").asText();
				if (StringUtil.isInValid(customerId)) {
					IndexETNetService.logger.error("No eID found from rbp cache");
					throw new VendorException(ExCodeConstant.EX_CODE_CACHE_NO_EID_FOUND);
				}
				ArgsHolder.putArgs(IndexETNetService.CUSTOMER_ID, customerId);
			} catch (Exception e) {
				IndexETNetService.logger.error("Cache Distribute Bad Request");
				throw new VendorException(ExCodeConstant.EX_CODE_CACHE_BAD_REQUEST);
			}
		} else {
			if (resultState == CacheDistributeResultStateEnum.INVALID_PARAMETERS) {
				IndexETNetService.logger.error("Cache Distribute Bad Request");
				throw new VendorException(ExCodeConstant.EX_CODE_CACHE_BAD_REQUEST);
			}
			if (resultState == CacheDistributeResultStateEnum.UNCACHED_RECORD) {
				IndexETNetService.logger.error("Cache Distribute don't contains the key you sent");
				throw new VendorException(ExCodeConstant.EX_CODE_CACHE_UNCACHED_RECORD);
			}
			if (resultState == CacheDistributeResultStateEnum.INVALID_HTTP_STATUS) {
				IndexETNetService.logger.error("Get response from the Cache Distribute encounter error");
				throw new VendorException(ExCodeConstant.EX_CODE_CACHE_INVALID_HTTP_STATUS);
			}
		}
		ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER, header);
		// TODO
		try {
			request.getSymbol().remove("DJI");
		} catch (Exception e) {
			IndexETNetService.logger.error(" filter DJI will fallback ");
		}
		ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_QUOTES_REQUEST, request);
		ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_PRODUCT_KEY, request.getSymbol());
		String key = IndexETNetService.DEFAULT_OPTION;
		List<String> items = new ArrayList<>();
		for (String string : request.getSymbol()) {
			if (StringUtil.isValid(string) && (IndexETNetService.INDEX_QUOTE_HK_SYMBOL_LIST
					.contains(SymbolConstant.SYMBOL_VERTICAL_LINE + string + SymbolConstant.SYMBOL_VERTICAL_LINE)
					|| IndexETNetService.INDEX_QUOTE_GLOBAL_SYMBOL_LIST.contains(
							SymbolConstant.SYMBOL_VERTICAL_LINE + string + SymbolConstant.SYMBOL_VERTICAL_LINE))) {
				items.add(string);
			} else {
				IndexETNetService.logger.error(" wrong symbol ");
				throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
			}
		}
		if (items.size() <= 1) {
			ArgsHolder.putArgs(IndexETNetService.ACCES_CMND_CDE, IndexETNetService.ACCES_CMND_CDE_INDEX_DETAIL);
		} else {
			ArgsHolder.putArgs(IndexETNetService.ACCES_CMND_CDE, IndexETNetService.ACCES_CMND_CDE_INDEX_LIST);
		}
		String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
		String service = this.labciProps.getLabciService(site, false);
		Map<String, String> serviceRequestMapper = new LinkedHashMap<>();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String symbolList = LabciServletBoConvertor.genSymbols(items, service);
		try {
			String token = etnetProperties.getEtnetTokenWithoutVerify();
			params.add(new BasicNameValuePair("symbol",
					symbolList.replaceAll(SymbolConstant.SYMBOL_SEMISOLON, SymbolConstant.SYMBOL_COMMA)));
			params.add(new BasicNameValuePair("locale", getLocale(header)));
			params.add(new BasicNameValuePair("token", token));
		} catch (Exception e) {
			if (e instanceof IOException) {
				IndexETNetService.logger.error("ETNet Undefined error", e);
				throw new VendorException(ExCodeConstant.EX_CODE_ETNET_UNDERFINED_ERROR, e);
			} else if (e instanceof ConnectTimeoutException) {
				IndexETNetService.logger.error("Access ETNet error", e);
				throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_ETNET_ERROR, e);
			} else {
				IndexETNetService.logger.error("ETNet server error", e);
				throw new VendorException(ExCodeConstant.EX_CODE_ETNET_SERVER_ERROR, e);
			}
		}
		String reqParams = URLEncodedUtils.format(params, "UTF-8");
		serviceRequestMapper.put(key, reqParams);
		return serviceRequestMapper;
	}

	/**
	 * @Title getLocale
	 * @Description
	 * @param header
	 * @return
	 * @return String
	 * @Author OJim
	 */
	private String getLocale(CommonRequestHeader header) {
		String locale = header.getLocale();
		switch (locale) {
		case "zh_HK":
			return "zh_HK";
		case "zh_CN":
			return "zh";
		default:
			return "en";
		}
	}

	private List<String> getInvalidSymbols(final List<String> symbols, final List<String> responses) {
		List<String> invalidSymbols = new ArrayList<>();
		for (String symbol : symbols) {
			invalidSymbols.add(symbol);
		}
		invalidSymbols.removeAll(responses);
		IndexETNetService.logger.debug("Invalid Symbols: " + invalidSymbols);
		return invalidSymbols;
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
			try {
				String result = this.httpClientHelper.doGet(this.etnetProperties.getProxyName(),
						this.etnetProperties.getEtnetIndicesUrl(), request, null);
				JsonObject respJsonObj = JsonUtil.getAsJsonObject(result);
				String errCode = JsonUtil.getAsString(respJsonObj, "err_code");
				if (StringUtil.isValid(errCode) && EtnetProperties.EX_CODE_ETNET_INVALID_TOKEN_CODE.equals(errCode)) {
					List<NameValuePair> params = URLEncodedUtils.parse(request, Charset.forName("UTF-8"));
					int index = 0;
					for (int i = 0; i < params.size(); i++) {
						if ("token".equals(params.get(i).getName())) {
							index = i;
						}
					}
					params.remove(index);
					params.add(new BasicNameValuePair("token", etnetProperties.getEtnetToken()));
					result = this.httpClientHelper.doGet(this.etnetProperties.getProxyName(),
							this.etnetProperties.getEtnetIndicesUrl(), params, null);
				}
				serviceResponseMapper.put(key, result);
			} catch (Exception e) {
				if (e instanceof IOException) {
					IndexETNetService.logger.error("ETNet Undefined error", e);
					throw new VendorException(ExCodeConstant.EX_CODE_ETNET_UNDERFINED_ERROR, e);
				} else if (e instanceof ConnectTimeoutException) {
					IndexETNetService.logger.error("Access ETNet error", e);
					throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_ETNET_ERROR, e);
				} else {
					IndexETNetService.logger.error("ETNet server error", e);
					throw new VendorException(ExCodeConstant.EX_CODE_ETNET_SERVER_ERROR, e);
				}
			}
		}
		return serviceResponseMapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService#
	 * validateServiceResponse(java.lang.Object)
	 */
	@Override
	protected Object validateServiceResponse(final Object serviceResponse) throws Exception {
		Object validServiceResponseMapper = new LinkedHashMap<>();
		@SuppressWarnings("unchecked")
		Map<String, String> serviceResponseMapper = (Map<String, String>) serviceResponse;
		validServiceResponseMapper = HKMarketResponse(serviceResponseMapper);
		return validServiceResponseMapper;
	}

	/**
	 * @Title HKMarketResponse
	 * @Description
	 * @param serviceResponseMapper
	 * @return
	 * @return Map<String,Map<String,LabciResponse>>
	 * @Author OJim
	 */
	private Object HKMarketResponse(Map<String, String> serviceResponseMapper) {
		Map<String, JsonArray> validServiceResponseMapper = new LinkedHashMap<>();
		List<String> validates = new ArrayList<>();
		for (Map.Entry<String, String> responseMapper : serviceResponseMapper.entrySet()) {
			JsonArray jsonArray = validateEachMarketResponse(responseMapper.getValue());
			if (jsonArray != null && jsonArray.size() > 0) {
				Iterator<JsonElement> it = jsonArray.iterator();
				while (it.hasNext()) {
					JsonObject resultJsonObj = it.next().getAsJsonObject();
					if (resultJsonObj.get("symbol") != null) {
						String symbol = LabciPropsUtil.inOrderStrProps("symbol", resultJsonObj);
						validates.add(symbol);
						if (IndexETNetService.INDEX_QUOTE_GLOBAL_SYMBOL_LIST.contains(symbol)) {
							if (validServiceResponseMapper.containsKey(IndexETNetService.GLOBAL_MARKET_QUOTE_MARKET)) {
								validServiceResponseMapper.get(IndexETNetService.GLOBAL_MARKET_QUOTE_MARKET)
										.add(resultJsonObj);
							} else {
								JsonArray jsonArray1 = new JsonArray();
								jsonArray1.add(resultJsonObj);
								validServiceResponseMapper.put(IndexETNetService.GLOBAL_MARKET_QUOTE_MARKET,
										jsonArray1);
							}
						} else {
							if (validServiceResponseMapper.containsKey(IndexETNetService.HK_MARKET_QUOTE_MARKET)) {
								validServiceResponseMapper.get(IndexETNetService.HK_MARKET_QUOTE_MARKET)
										.add(resultJsonObj);
							} else {
								JsonArray jsonArray1 = new JsonArray();
								jsonArray1.add(resultJsonObj);
								validServiceResponseMapper.put(IndexETNetService.HK_MARKET_QUOTE_MARKET, jsonArray1);
							}
						}
					}
				}
			}
		}
		@SuppressWarnings("unchecked")
		List<String> symbols = (List<String>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_PRODUCT_KEY);
		if (validates != null && validates.size() != symbols.size()) {
			ExResponseEntity exResponse = this.exRespComponent
					.getExResponse(ExCodeConstant.EX_CODE_ETNET_INVALID_RESPONSE);
			List<String> invalidSymbols = getInvalidSymbols(symbols, validates);
			for (String invalidSymbol : invalidSymbols) {
				Messages message = new Messages();
				message.setReasonCode(exResponse.getReasonCode());
				message.setText(exResponse.getText());
				String traceCode = ExTraceCodeGenerator.generate();
				message.setTraceCode(traceCode);
				message.setSymbol(invalidSymbol);
				addMessage(message);
			}
		}
		try {
			saveAccessLogHKMarket(validServiceResponseMapper);
//			throw new ApplicationException(ExCodeConstant.EX_CODE_DB_ACTION_FAIL);
		} catch (Exception e) {
			IndexETNetService.logger.error("Error: Failed to write quotes history", e);
			throw new ApplicationException(ExCodeConstant.EX_CODE_DB_ACTION_FAIL);
		}
		return validServiceResponseMapper;
	}

	private JsonArray validateEachMarketResponse(final String serviceResponse) {
		JsonObject respJsonObj = JsonUtil.getAsJsonObject(serviceResponse);
		JsonArray resultJsonArray = JsonUtil.getAsJsonArray(respJsonObj, "indices");
		return resultJsonArray;
	}

	private void saveAccessLogHKMarket(Map<String, JsonArray> validServiceResponseMapper) throws ParseException {
		List<QuoteAccessLogForIndex> quoteAccessLogs = new ArrayList<>();
		IndexQuotesRequest request = (IndexQuotesRequest) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_REQUEST);
		CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
		Long userReferenceId = getUserId();
		for (Map.Entry<String, JsonArray> jsonArrayMapper : validServiceResponseMapper.entrySet()) {
			QuoteAccessLogForIndex quoteAccessLog = new QuoteAccessLogForIndex();
			quoteAccessLogs.add(quoteAccessLog);

			String key = jsonArrayMapper.getKey();
			JsonArray resultJsonArray = jsonArrayMapper.getValue();
			StringBuffer sb = new StringBuffer();
			StringBuffer stringBuffer = new StringBuffer();
			for (int i = 0; i < resultJsonArray.size(); i++) {
				JsonObject resultJsonObj = resultJsonArray.get(i).getAsJsonObject();
				String symbol = LabciPropsUtil.inOrderStrProps("symbol", resultJsonObj);
				String quote = LabciPropsUtil.inOrderStrProps("lastPrice", resultJsonObj);
				stringBuffer.append(symbolToStr(symbol, quote));
				stringBuffer.append(SymbolConstant.SYMBOL_SEMISOLON);
			}
			sb.append(stringBuffer);
			sb.append(SymbolConstant.SYMBOL_VERTICAL_LINE);
			quoteAccessLog.setUserReferenceId(userReferenceId);
			quoteAccessLog.setMarketCode(request.getMarket());
			quoteAccessLog.setExchangeCode("SEHK");
			quoteAccessLog.setApplicationCode(header.getAppCode());
			quoteAccessLog.setCountryCode(header.getCountryCode());
			quoteAccessLog.setGroupMember(header.getGroupMember());
			quoteAccessLog.setChannelId(header.getChannelId());
			quoteAccessLog.setFuntionId(header.getFunctionId());
			quoteAccessLog.setAccessCommand(ArgsHolder.getArgs(IndexETNetService.ACCES_CMND_CDE).toString());
			// quoteAccessLog.setChargeCategory(null);
			if (IndexETNetService.GLOBAL_MARKET_QUOTE_MARKET.equals(key)) {
				quoteAccessLog.setRequestType(IndexETNetService.ACCES_TYPE_DELAY);
				quoteAccessLog.setResponseType(IndexETNetService.ACCES_TYPE_DELAY);
			} else if (IndexETNetService.HK_MARKET_QUOTE_MARKET.equals(key)) {
				quoteAccessLog.setRequestType(IndexETNetService.ACCES_TYPE_REAL_TIME);
				quoteAccessLog.setResponseType(IndexETNetService.ACCES_TYPE_REAL_TIME);
			}
			quoteAccessLog.setAccessCount(resultJsonArray.size());
			quoteAccessLog.setResponseText(sb.toString());
			// quoteAccessLog.setCommentText(QuotesLabciService.ACCES_SYS_CHANNEL);
			quoteAccessLog.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
		}
		this.quoteAccessLogService.saveQuoteAccessLog(quoteAccessLogs);
	}

	private Long getUserId() {
		CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
		QuoteUserForIndex quoteUser = new QuoteUserForIndex();
		quoteUser.setUserExtnlId(ArgsHolder.getArgs(IndexETNetService.CUSTOMER_ID).toString());
		quoteUser.setUserType(IndexETNetService.USER_TYPE_CDE_CUST);
		quoteUser.setGroupMember(header.getGroupMember());
		quoteUser.setUserMarketCode(header.getCountryCode());
		quoteUser.setMonitorFlag((long) 0);
		return this.quoteUserService.getUserByExtnlId(quoteUser);
	}

	private String symbolToStr(final String symbol, final String quote) {
		String priceDetail = "";
		if (null != quote) {
			priceDetail = IndexETNetService.KEY_L + quote;
		}
		String result = symbol + SymbolConstant.SYMBOL_LEFT_CIRCLE_BRACKET + priceDetail
				+ SymbolConstant.SYMBOL_RIGHT_CIRCLE_BRACKET;
		return result;
	}

	private void addMessage(final Messages message) {
		if (ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES) == null) {
			ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES, new ArrayList<Messages>());
		}
		@SuppressWarnings("unchecked")
		List<Messages> messages = (List<Messages>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES);
		messages.add(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService#
	 * convertResponse(java.lang.Object)
	 */
	@Override
	protected IndexQuotesResponse convertResponse(final Object validServiceResponse) throws Exception {
		IndexQuotesResponse response = new IndexQuotesResponse();
		response.setIndices(HKResponse(validServiceResponse));
		response.setMessages(getMessages());
		return response;
	}

	/**
	 * @Title HKResponse
	 * @Description
	 * @param validServiceResponse
	 * @return
	 * @return List<Indice>
	 * @Author OJim
	 */
	private List<Indice> HKResponse(Object validServiceResponse) {
		@SuppressWarnings("unchecked")
		Map<String, JsonArray> resultJsonArrayMapper = (Map<String, JsonArray>) validServiceResponse;
		List<Indice> indices = new ArrayList<>();
		for (Map.Entry<String, JsonArray> jsonArrayMapper : resultJsonArrayMapper.entrySet()) {
			JsonArray resultJsonArray = jsonArrayMapper.getValue();
			for (int i = 0; i < resultJsonArray.size(); i++) {
				JsonObject resultJsonObj = resultJsonArray.get(i).getAsJsonObject();
				Indice indice = new Indice();
				indices.add(indice);

				indice.setSymbol(LabciPropsUtil.inOrderStrProps("symbol", resultJsonObj));
				indice.setName(LabciPropsUtil.inOrderStrProps("name", resultJsonObj));
				indice.setLastPrice(LabciPropsUtil.inOrderNumberProps("lastPrice", resultJsonObj));
				indice.setChangeAmount(LabciPropsUtil.inOrderNumberProps("changeAmount", resultJsonObj));
				indice.setChangePercent(LabciPropsUtil.inOrderNumberProps("changePercent", resultJsonObj));
				indice.setOpenPrice(LabciPropsUtil.inOrderNumberProps("openPrice", resultJsonObj));
				indice.setPreviousClosePrice(LabciPropsUtil.inOrderNumberProps("previousClosePrice", resultJsonObj));
				indice.setDayRangeHigh(LabciPropsUtil.inOrderNumberProps("dayHighPrice", resultJsonObj));
				indice.setDayRangeLow(LabciPropsUtil.inOrderNumberProps("dayLowPrice", resultJsonObj));
				indice.setChangePercent1M(LabciPropsUtil.inOrderNumberProps("changePercent1M", resultJsonObj));
				indice.setChangePercent2M(LabciPropsUtil.inOrderNumberProps("changePercent2M", resultJsonObj));
				indice.setChangePercent3M(LabciPropsUtil.inOrderNumberProps("changePercent3M", resultJsonObj));
				indice.setChangePercent1Y(LabciPropsUtil.inOrderNumberProps("changePercent1Y", resultJsonObj));
				indice.setOneMonthLowPrice(LabciPropsUtil.inOrderNumberProps("oneMonthLowPrice", resultJsonObj));
				indice.setTwoMonthLowPrice(LabciPropsUtil.inOrderNumberProps("twoMonthLowPrice", resultJsonObj));
				indice.setThreeMonthLowPrice(LabciPropsUtil.inOrderNumberProps("threeMonthLowPrice", resultJsonObj));
				indice.setOneMonthHighPrice(LabciPropsUtil.inOrderNumberProps("oneMonthHighPrice", resultJsonObj));
				indice.setTwoMonthHighPrice(LabciPropsUtil.inOrderNumberProps("twoMonthHighPrice", resultJsonObj));
				indice.setThreeMonthHighPrice(LabciPropsUtil.inOrderNumberProps("threeMonthHighPrice", resultJsonObj));
				indice.setYearHighPrice(LabciPropsUtil.inOrderNumberProps("yearHighPrice", resultJsonObj));
				indice.setYearLowPrice(LabciPropsUtil.inOrderNumberProps("yearLowPrice", resultJsonObj));
				indice.setAsOfDateTime(LabciPropsUtil.inOrderStrProps("asOfDateTime", resultJsonObj));
			}
		}
		return indices;
	}

	@SuppressWarnings("unchecked")
	private List<Messages> getMessages() {
		if (ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES) == null) {
			return null;
		} else {
			return (List<Messages>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES);
		}
	}
}
