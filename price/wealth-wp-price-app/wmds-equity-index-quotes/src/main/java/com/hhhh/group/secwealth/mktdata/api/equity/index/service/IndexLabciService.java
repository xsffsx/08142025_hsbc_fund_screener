/**
 * @Title QuoteLABCIService.java
 * @description TODO
 * @author OJim
 * @time May 30, 2019 11:50:08 AM
 */
package com.hhhh.group.secwealth.mktdata.api.equity.index.service;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
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
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci.LabciResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci.Ric;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci.Watchlist;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.ApplicationProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.MarketHourProperties;
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
 * 
 */
/**
 * @Title QuoteLABCIService.java
 * @description TODO
 * @author OJim
 * @time May 30, 2019 11:50:08 AM
 */
@Service("labciIndicesService")
@ConditionalOnProperty(value = "service.quotes.Labci.injectEnabled")
public class IndexLabciService
		extends AbstractBaseService<IndexQuotesRequest, IndexQuotesResponse, CommonRequestHeader> {

	private static final Logger logger = LoggerFactory.getLogger(IndexLabciService.class);

	private static final String DATAREPRESENTATION_XML = "XML";

	private static final String DEFAULT_OPTION = "DEFAULT|DEFAULT|DEFAULT";

	private static final String ACCES_TYPE_DELAY = "EDIDX";

	private static final String ACCES_TYPE_REAL_TIME = "INDEX";

	private static final String ACCES_CMND_CDE = "INDEX_LABCI_ACCES_CMND_CDE";

	private static final String ACCES_CMND_CDE_INDEX_DETAIL = "QUOTE_DETAIL";

	private static final String ACCES_CMND_CDE_INDEX_LIST = "QUOTE_LIST";

	private static final String INDEX_QUOTE_CN_SYMBOL_LIST = "|CSI300|SSEC|SZSC|SZI|SZ100|CNT|";

	private static final String CUSTOMER_ID = "INDEX_LABCI_CUSTOMER_ID";

	private static final String USER_TYPE_CDE_CUST = "CUST";

	private static final String KEY_L = "L=";

	@Autowired
	private ApplicationProperties appProps;

	@Autowired
	private LabciProperties labciProps;

	@Autowired
	private ExResponseComponent exRespComponent;

	@Autowired
	private HttpClientHelper httpClientHelper;

	@Autowired()
	@Qualifier("indexQuoteUserService")
	private QuoteUserService quoteUserService;

	@Autowired()
	@Qualifier("indexQuoteAccessLogService")
	private QuoteAccessLogService quoteAccessLogService;

	@Autowired
	private MarketHourProperties marketHourProperties;

	@Autowired
	private PredSrchService predSrchService;

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
					IndexLabciService.logger.error("No eID found from rbp cache");
					throw new VendorException(ExCodeConstant.EX_CODE_CACHE_NO_EID_FOUND);
				}
				ArgsHolder.putArgs(IndexLabciService.CUSTOMER_ID, customerId);
			} catch (Exception e) {
				IndexLabciService.logger.error("Cache Distribute Bad Request");
				throw new VendorException(ExCodeConstant.EX_CODE_CACHE_BAD_REQUEST);
			}
		} else {
			if (resultState == CacheDistributeResultStateEnum.INVALID_PARAMETERS) {
				IndexLabciService.logger.error("Cache Distribute Bad Request");
				throw new VendorException(ExCodeConstant.EX_CODE_CACHE_BAD_REQUEST);
			}
			if (resultState == CacheDistributeResultStateEnum.UNCACHED_RECORD) {
				IndexLabciService.logger.error("Cache Distribute don't contains the key you sent");
				throw new VendorException(ExCodeConstant.EX_CODE_CACHE_UNCACHED_RECORD);
			}
			if (resultState == CacheDistributeResultStateEnum.INVALID_HTTP_STATUS) {
				IndexLabciService.logger.error("Get response from the Cache Distribute encounter error");
				throw new VendorException(ExCodeConstant.EX_CODE_CACHE_INVALID_HTTP_STATUS);
			}
		}

		ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER, header);
		ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_QUOTES_REQUEST, request);
		ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_PRODUCT_KEY, request.getSymbol());

		for (String string : request.getSymbol()) {
			if (StringUtil.isInValid(string) || !IndexLabciService.INDEX_QUOTE_CN_SYMBOL_LIST
					.contains(SymbolConstant.SYMBOL_VERTICAL_LINE + string + SymbolConstant.SYMBOL_VERTICAL_LINE)) {
				IndexLabciService.logger.error(" wrong symbol ");
				throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
			}
		}

		List<PredSrchResponse> predSrchResponses = this.predSrchService.precSrch(request.getSymbol(), header);

		String key = IndexLabciService.DEFAULT_OPTION;
		String[] keys = key.split(SymbolConstant.SYMBOL_VERTICAL_LINE_ESCAPE);
		List<String> items = new ArrayList<>();
		for (PredSrchResponse predSrchResponse : predSrchResponses) {
			if (StringUtil.isValid(predSrchResponse.getSymbol())
					&& IndexLabciService.INDEX_QUOTE_CN_SYMBOL_LIST.contains(SymbolConstant.SYMBOL_VERTICAL_LINE
							+ predSrchResponse.getSymbol() + SymbolConstant.SYMBOL_VERTICAL_LINE)) {
				List<ProdAltNumSeg> prodAltNumSegs = predSrchResponse.getProdAltNumSegs();
				for (ProdAltNumSeg prodAltNumSeg : prodAltNumSegs) {
					if (Constant.PROD_CDE_ALT_CLASS_CODE_T.equals(prodAltNumSeg.getProdCdeAltClassCde())) {
						items.add(prodAltNumSeg.getProdAltNum());
					}
				}
			} else {
				IndexLabciService.logger.error(" wrong symbol ");
				throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
			}
		}
		if (items.size() <= 1) {
			ArgsHolder.putArgs(IndexLabciService.ACCES_CMND_CDE, IndexLabciService.ACCES_CMND_CDE_INDEX_DETAIL);
		} else {
			ArgsHolder.putArgs(IndexLabciService.ACCES_CMND_CDE, IndexLabciService.ACCES_CMND_CDE_INDEX_LIST);
		}

		String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
		String service = this.labciProps.getLabciService(site, false);
		Map<String, String> serviceRequestMapper = new LinkedHashMap<>();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String symbolList = LabciServletBoConvertor.genSymbols(items, service);
		params.add(new BasicNameValuePair("SymbolList", symbolList));
		params.add(new BasicNameValuePair("FieldList",
				StringUtils.join(this.labciProps.getLabciFields(this.appProps.getQuotesResponseLabciFields(keys), keys),
						SymbolConstant.SYMBOL_SEMISOLON)));
		params.add(new BasicNameValuePair("DataRepresentation", IndexLabciService.DATAREPRESENTATION_XML));
		String reqParams = URLEncodedUtils.format(params, "UTF-8");

		serviceRequestMapper.put(key, reqParams);
		return serviceRequestMapper;
	}

	private List<String> getInvalidSymbols(final List<String> symbols, final List<String> responses) {
		List<String> invalidSymbols = new ArrayList<>();
		for (String symbol : symbols) {
			invalidSymbols.add(symbol);
		}
		invalidSymbols.removeAll(responses);
		IndexLabciService.logger.debug("Invalid Symbols: " + invalidSymbols);
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
				serviceResponseMapper.put(key,
						this.httpClientHelper.doGet(this.labciProps.getLabciUrl(), request, null));
			} catch (Exception e) {
				if (e instanceof IOException) {
					IndexLabciService.logger.error("Labci Undefined error", e);
					throw new VendorException(ExCodeConstant.EX_CODE_LABCI_UNDEFINED_ERROR, e);
				} else if (e instanceof ConnectTimeoutException) {
					IndexLabciService.logger.error("Access Labci error", e);
					throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_LABCI_ERROR, e);
				} else {
					IndexLabciService.logger.error("Labci server error", e);
					throw new VendorException(ExCodeConstant.EX_CODE_LABCI_SERVER_ERROR, e);
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
		validServiceResponseMapper = LabciResponse(serviceResponseMapper);
		return validServiceResponseMapper;
	}

	private Object LabciResponse(Map<String, String> serviceResponseMapper) throws Exception {
		List<String> validates = new ArrayList<>();
		Map<String, Map<String, LabciResponse>> validServiceResponseMapper = new LinkedHashMap<>();
		for (Map.Entry<String, String> responseMapper : serviceResponseMapper.entrySet()) {
			Map<String, LabciResponse> map = validateEachServiceResponse(responseMapper.getValue());
			if (!map.isEmpty()) {
				for (Map.Entry<String, LabciResponse> jsonArrayMapper2 : map.entrySet()) {
					Map<String, LabciResponse> map2 = new HashMap<String, LabciResponse>();
					map2.put(jsonArrayMapper2.getKey(), jsonArrayMapper2.getValue());
					Map<String, Object> data = jsonArrayMapper2.getValue().getFields();
					String exchangeCode = LabciPropsUtil.inOrderStrProps("exchangeCode",
							IndexLabciService.DEFAULT_OPTION, data);
					exchangeCode = this.labciProps.getExchangeMapping(exchangeCode);
					if (validServiceResponseMapper.get(exchangeCode) == null) {
						validServiceResponseMapper.put(exchangeCode, map2);
					} else {
						validServiceResponseMapper.get(exchangeCode).put(jsonArrayMapper2.getKey(),
								jsonArrayMapper2.getValue());
					}
				}
				for (String string : map.keySet()) {
					validates.add(string);
				}
			}
		}
		@SuppressWarnings("unchecked")
		List<String> symbols = (List<String>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_PRODUCT_KEY);
		if (validates != null && validates.size() != symbols.size()) {
			ExResponseEntity exResponse = this.exRespComponent
					.getExResponse(ExCodeConstant.EX_CODE_LABCI_STOCK_NOT_FOUND);
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
			saveAccessLogLabci(validServiceResponseMapper);
//			throw new ApplicationException(ExCodeConstant.EX_CODE_DB_ACTION_FAIL);
		} catch (Exception e) {
			IndexLabciService.logger.error("Error: Failed to write quotes history", e);
			throw new ApplicationException(ExCodeConstant.EX_CODE_DB_ACTION_FAIL);
		}
		return validServiceResponseMapper;
	}

	private void saveAccessLogLabci(Map<String, Map<String, LabciResponse>> validServiceResponseMapper)
			throws ParseException {
		List<QuoteAccessLogForIndex> quoteAccessLogs = new ArrayList<>();
		IndexQuotesRequest request = (IndexQuotesRequest) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_REQUEST);
		CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
		Long userReferenceId = getUserId();
		for (Map.Entry<String, Map<String, LabciResponse>> jsonArrayMapper : validServiceResponseMapper.entrySet()) {
			QuoteAccessLogForIndex quoteAccessLog = new QuoteAccessLogForIndex();
			quoteAccessLogs.add(quoteAccessLog);

			Map<String, LabciResponse> resultJsonArray = jsonArrayMapper.getValue();
			String exchangeCode = "";
			StringBuffer sb = new StringBuffer();
			StringBuffer stringBuffer = new StringBuffer();
			for (Map.Entry<String, LabciResponse> jsonArrayMapper2 : resultJsonArray.entrySet()) {
				PredSrchResponse predSrchResp = this.predSrchService.localPredSrch(jsonArrayMapper2.getKey(),
						Constant.PROD_CDE_ALT_CLASS_CODE_M);
				if (predSrchResp == null) {
					predSrchResp = new PredSrchResponse();
				}
				Map<String, Object> data = jsonArrayMapper2.getValue().getFields();
				String symbol = StringUtil.isInValid(predSrchResp.getSymbol()) ? jsonArrayMapper2.getKey()
						: predSrchResp.getSymbol();
				BigDecimal quotePrice = LabciPropsUtil.inOrderNumberProps("nominalPrice",
						IndexLabciService.DEFAULT_OPTION, data);
				String quote = quotePrice != null ? quotePrice.toString() : "";
				exchangeCode = LabciPropsUtil.inOrderStrProps("exchangeCode", IndexLabciService.DEFAULT_OPTION, data);
				stringBuffer.append(symbolToStr(symbol, quote));
				stringBuffer.append(SymbolConstant.SYMBOL_SEMISOLON);
			}
			exchangeCode = this.labciProps.getExchangeMapping(exchangeCode);
			sb.append(stringBuffer);
			sb.append(SymbolConstant.SYMBOL_VERTICAL_LINE);
			String delay = this.marketHourProperties.checkTradingHoursByExchange(exchangeCode)
					? IndexLabciService.ACCES_TYPE_DELAY
					: IndexLabciService.ACCES_TYPE_REAL_TIME;
			quoteAccessLog.setUserReferenceId(userReferenceId);
			quoteAccessLog.setMarketCode(request.getMarket());
			quoteAccessLog.setExchangeCode(exchangeCode);
			quoteAccessLog.setApplicationCode(header.getAppCode());
			quoteAccessLog.setCountryCode(header.getCountryCode());
			quoteAccessLog.setGroupMember(header.getGroupMember());
			quoteAccessLog.setChannelId(header.getChannelId());
			quoteAccessLog.setFuntionId(header.getFunctionId());
			quoteAccessLog.setAccessCommand(ArgsHolder.getArgs(IndexLabciService.ACCES_CMND_CDE).toString());
			// quoteAccessLog.setChargeCategory(null);
			quoteAccessLog.setRequestType(delay);
			quoteAccessLog.setAccessCount(resultJsonArray.entrySet().size());
			quoteAccessLog.setResponseType(delay);
			quoteAccessLog.setResponseText(sb.toString());
			// quoteAccessLog.setCommentText(QuotesLabciService.ACCES_SYS_CHANNEL);
			quoteAccessLog.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
		}
		this.quoteAccessLogService.saveQuoteAccessLog(quoteAccessLogs);
	}

	private Long getUserId() {
		CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
		QuoteUserForIndex quoteUser = new QuoteUserForIndex();
		quoteUser.setUserExtnlId(ArgsHolder.getArgs(IndexLabciService.CUSTOMER_ID).toString());
		quoteUser.setUserType(IndexLabciService.USER_TYPE_CDE_CUST);
		quoteUser.setGroupMember(header.getGroupMember());
		quoteUser.setUserMarketCode(header.getCountryCode());
		quoteUser.setMonitorFlag((long) 0);
		return this.quoteUserService.getUserByExtnlId(quoteUser);
	}

	private String symbolToStr(final String symbol, final String quote) {
		String priceDetail = "";
		if (null != quote) {
			priceDetail = IndexLabciService.KEY_L + quote;
		}
		String result = symbol + SymbolConstant.SYMBOL_LEFT_CIRCLE_BRACKET + priceDetail
				+ SymbolConstant.SYMBOL_RIGHT_CIRCLE_BRACKET;
		return result;
	}

	private Map<String, LabciResponse> validateEachServiceResponse(final String serviceResponse) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Watchlist.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		Watchlist res = (Watchlist) unmarshaller.unmarshal(new StringReader(serviceResponse));
		Map<String, LabciResponse> response = new LinkedHashMap<>();
		if (null != res) {
			List<Ric> ricList = res.getRic();
			if (!ListUtils.isEmpty(ricList)) {
				response = LabciServletBoConvertor.IndexGetResponseMap(ricList);
			}
		}
		return response;
	}

	private void addMessage(final Messages message) {
		if (ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES) == null) {
			ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES, new ArrayList<Messages>());
		}
		@SuppressWarnings("unchecked")
		List<Messages> messages = (List<Messages>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES);
		messages.add(message);
	}

	private String companyName(String key, Map<String, Object> data, String local) {
		// String name = LabciPropsUtil.inOrderStrProps("localName", key, data);
		// if(StringUtil.isValid(name)) {
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
		response.setIndices(labciResponse(validServiceResponse));
		response.setMessages(getMessages());
		return response;
	}

	/**
	 * @Title labciResponse
	 * @Description
	 * @param validServiceResponse
	 * @return
	 * @return List<Indice>
	 * @Author OJim
	 */
	private List<Indice> labciResponse(Object validServiceResponse) {
		CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
		IndexQuotesRequest request = (IndexQuotesRequest) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_REQUEST);
		List<Indice> indices = new ArrayList<>();
		@SuppressWarnings("unchecked")
		Map<String, Map<String, LabciResponse>> resultJsonArrayMapper = (Map<String, Map<String, LabciResponse>>) validServiceResponse;
		for (Map.Entry<String, Map<String, LabciResponse>> jsonArrayMapper : resultJsonArrayMapper.entrySet()) {
			Map<String, LabciResponse> resultJsonArray = jsonArrayMapper.getValue();
			for (Map.Entry<String, LabciResponse> jsonArrayMapper2 : resultJsonArray.entrySet()) {
				Map<String, Object> data = jsonArrayMapper2.getValue().getFields();
				Indice indice = new Indice();
				indices.add(indice);

				PredSrchResponse predSrchResp = this.predSrchService.localPredSrch(jsonArrayMapper2.getKey(),
						Constant.PROD_CDE_ALT_CLASS_CODE_M);
				if (predSrchResp == null) {
					predSrchResp = new PredSrchResponse();
				}
				indice.setSymbol(StringUtil.isInValid(predSrchResp.getSymbol()) ? jsonArrayMapper2.getKey()
						: predSrchResp.getSymbol());
				indice.setName(StringUtil.isInValid(predSrchResp.getProductName())
						? companyName(IndexLabciService.DEFAULT_OPTION, data, header.getLocale())
						: predSrchResp.getProductName());
				indice.setLastPrice(
						LabciPropsUtil.inOrderNumberProps("nominalPrice", IndexLabciService.DEFAULT_OPTION, data));
				try {
					String quoteOffClose = LabciPropsUtil.inOrderStrProps("quoteOffClose",
							IndexLabciService.DEFAULT_OPTION, data);
					if (Constant.ZERO.equals(quoteOffClose) || StringUtil.isInValid(quoteOffClose)) {
						indice.setChangeAmount(
								LabciPropsUtil.subtractProps("changeAmount", IndexLabciService.DEFAULT_OPTION, data));
						indice.setChangePercent(LabciPropsUtil.growthRateProps("changePercent",
								IndexLabciService.DEFAULT_OPTION, data, new MathContext(4, RoundingMode.HALF_UP)));
					} else {
						indice.setChangeAmount(LabciPropsUtil.subtractProps("offCloseSubChangeAmount",
								IndexLabciService.DEFAULT_OPTION, data));
						indice.setChangePercent(LabciPropsUtil.growthRateProps("offCloseSubChangePercent",
								IndexLabciService.DEFAULT_OPTION, data, new MathContext(4, RoundingMode.HALF_UP)));
					}
				} catch (Exception e) {
					indice.setChangeAmount(
							LabciPropsUtil.subtractProps("changeAmount", IndexLabciService.DEFAULT_OPTION, data));
					indice.setChangePercent(LabciPropsUtil.growthRateProps("changePercent",
							IndexLabciService.DEFAULT_OPTION, data, new MathContext(4, RoundingMode.HALF_UP)));
				}
				indice.setAsOfDateTime(LabciPropsUtil.dateProps("asOfDateTime", IndexLabciService.DEFAULT_OPTION, data,
						this.appProps.getTimezone(null)));
				indice.setOpenPrice(
						LabciPropsUtil.inOrderNumberProps("openPrice", IndexLabciService.DEFAULT_OPTION, data));
				indice.setPreviousClosePrice(LabciPropsUtil.inOrderNumberProps("previousClosePrice",
						IndexLabciService.DEFAULT_OPTION, data));
				indice.setYearLowPrice(
						LabciPropsUtil.inOrderNumberProps("yearLowPrice", IndexLabciService.DEFAULT_OPTION, data));
				indice.setYearHighPrice(
						LabciPropsUtil.inOrderNumberProps("yearHighPrice", IndexLabciService.DEFAULT_OPTION, data));
				indice.setDayRangeLow(
						LabciPropsUtil.inOrderNumberProps("dayLowPrice", IndexLabciService.DEFAULT_OPTION, data));
				indice.setDayRangeHigh(
						LabciPropsUtil.inOrderNumberProps("dayHighPrice", IndexLabciService.DEFAULT_OPTION, data));
			}
		}
		List<Indice> indicesResponse = new ArrayList<>();
		if (indices.size() > 0) {
			List<String> symbols = request.getSymbol();
			for (String symbol : symbols) {
				for (Indice indice : indices) {
					if (indice != null && StringUtils.isNotBlank(indice.getSymbol())
							&& indice.getSymbol().equals(symbol)) {
						indicesResponse.add(indice);
					}
				}
			}
		}
		return indicesResponse;
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
