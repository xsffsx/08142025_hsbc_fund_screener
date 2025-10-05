/**
 * @Title TopMoverLabciService.java
 * @description TODO
 * @author OJim
 * @time Jun 27, 2019 7:45:05 PM
 */
package com.hhhh.group.secwealth.mktdata.api.equity.topmover.service;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.collections.MapUtils;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.bean.ChainMapping;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.dao.entiry.QuoteAccessLogForTopMover;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.dao.entiry.QuoteUserForTopMover;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.request.TopMoverRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.response.TopMoverLabciProduct;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.response.TopMoverLabciResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.response.TopMoverLabciTable;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResponse;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResult;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResultStateEnum;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.utils.CacheDistributeHolder;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ParameterException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

/**
 * 
 */
/**
 * @Title TopMoverLabciService.java
 * @description TODO
 * @author OJim
 * @time Jun 27, 2019 7:45:05 PM
 */
@Service("labciTopmoverService")
@ConditionalOnProperty(value = "service.quotes.Labci.injectEnabled")
public class TopMoverLabciService
		extends AbstractBaseService<TopMoverRequest, TopMoverLabciResponse, CommonRequestHeader> {

	private static final Logger logger = LoggerFactory.getLogger(TopMoverLabciService.class);

	private static final String DATAREPRESENTATION_XML = "XML";

	private static final String DEFAULT_OPTION = "DEFAULT|DEFAULT|DEFAULT";

	private static final String EXPANDCHAIN = "ExpandChain";

	private static final String ACCES_TYPE_DELAY = "EDSTK";

	private static final String ACCES_TYPE_REAL_TIME = "STOCK";

	private static final String ACCES_CMND_CDE_TOP_MOVER = "MOVER";

	private static final String CUSTOMER_ID = "MOVER_LABCI_CUSTOMER_ID";

	// private static final String USER_TYPE_CDE_STFF = "STFF";

	private static final String USER_TYPE_CDE_CUST = "CUST";

	private static final String KEY_L = "L=";

	private static final String HEADER_FUNCTION_ID_CHECK = "|01|02|03|04|05|06|07|08|09|10|21|22|23|24|25|26|27|28|29|";

	@Autowired
	private LabciProperties labciProps;

	@Autowired
	private ApplicationProperties appProps;

	@Autowired
	private ChainMapping chainMapping;

	@Autowired
	private HttpClientHelper httpClientHelper;

	@Autowired
	private PredSrchService predSrchService;

	@Autowired
	private MarketHourProperties marketHourProperties;

	@Autowired()
	@Qualifier("topMoverQuoteUserService")
	private QuoteUserService quoteUserService;

	@Autowired()
	@Qualifier("topMoverQuoteAccessLogService")
	private QuoteAccessLogService quoteAccessLogService;

	@Override
	protected Object convertRequest(final TopMoverRequest request, final CommonRequestHeader header) throws Exception {
		if (!TopMoverLabciService.HEADER_FUNCTION_ID_CHECK.contains(
				SymbolConstant.SYMBOL_VERTICAL_LINE + header.getFunctionId() + SymbolConstant.SYMBOL_VERTICAL_LINE)) {
			TopMoverLabciService.logger.error("Header FunctionId don't match the rules or can't be empty.");
			throw new CommonException(ExCodeConstant.EX_CODE_HEADER_FUNCTIONID_INVALID);
		}
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
					TopMoverLabciService.logger.error("No eID found from rbp cache");
					throw new VendorException(ExCodeConstant.EX_CODE_CACHE_NO_EID_FOUND);
				}
				ArgsHolder.putArgs(TopMoverLabciService.CUSTOMER_ID, customerId);
			} catch (Exception e) {
				TopMoverLabciService.logger.error("Cache Distribute Bad Request");
				throw new VendorException(ExCodeConstant.EX_CODE_CACHE_BAD_REQUEST);
			}
		} else {
			if (resultState == CacheDistributeResultStateEnum.INVALID_PARAMETERS) {
				TopMoverLabciService.logger.error("Cache Distribute Bad Request");
				throw new VendorException(ExCodeConstant.EX_CODE_CACHE_BAD_REQUEST);
			}
			if (resultState == CacheDistributeResultStateEnum.UNCACHED_RECORD) {
				TopMoverLabciService.logger.error("Cache Distribute don't contains the key you sent");
				throw new VendorException(ExCodeConstant.EX_CODE_CACHE_UNCACHED_RECORD);
			}
			if (resultState == CacheDistributeResultStateEnum.INVALID_HTTP_STATUS) {
				TopMoverLabciService.logger.error("Get response from the Cache Distribute encounter error");
				throw new VendorException(ExCodeConstant.EX_CODE_CACHE_INVALID_HTTP_STATUS);
			}
		}
		ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER, header);
		ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_TOP_TEN_MOVERS_REQUEST, request);
		String key = TopMoverLabciService.DEFAULT_OPTION;
		String[] keys = key.split(SymbolConstant.SYMBOL_VERTICAL_LINE_ESCAPE);
		Map<String, String> serviceRequestMapper = new LinkedHashMap<String, String>();
		if (StringUtil.isInValid(request.getProductType()) || StringUtil.isInValid(request.getExchangeCode())
				|| StringUtil.isInValid(request.getMoverType())
				|| (request.getTopNum() != null && request.getTopNum() <= 0)) {
			TopMoverLabciService.logger.error(" ProductType or Exchange or MoverType or TopNum is empty...... ");
			throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
		}
		List<Map<String, String>> ricCodes = this
				.getRicCode(request.getProductType().trim() + "." + request.getExchangeCode().trim());
		if (ricCodes != null && ricCodes.size() > 0) {
			for (Map<String, String> ricCode : ricCodes) {
				if (ricCode.get(Constant.CHAIN_TYPE).equals(request.getMoverType())) {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("SymbolList", ricCode.get(Constant.RIC)));
					params.add(new BasicNameValuePair("FieldList",
							StringUtils.join(this.labciProps
									.getLabciFields(this.appProps.getQuotesResponseTopMoverFields(keys), keys),
									SymbolConstant.SYMBOL_SEMISOLON)));
					params.add(
							new BasicNameValuePair("DataRepresentation", TopMoverLabciService.DATAREPRESENTATION_XML));
					params.add(new BasicNameValuePair(TopMoverLabciService.EXPANDCHAIN, "true"));
					String reqParams = URLEncodedUtils.format(params, "UTF-8");
					serviceRequestMapper.put(ricCode.get(Constant.CHAIN_TYPE) + SymbolConstant.SYMBOL_VERTICAL_LINE
							+ ricCode.get(Constant.CHAIN_CODE), reqParams);
				}
			}
		} else {
			TopMoverLabciService.logger.error(" Ric Codes is empty......");
			throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
		}
		if (MapUtils.isEmpty(serviceRequestMapper)) {
			TopMoverLabciService.logger.error(" MoverType error ......");
			throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
		}
		TopMoverLabciService.logger.debug("topmoverRequestMap is " + serviceRequestMapper);
		TopMoverLabciService.logger.debug("convert TopMoverRequest to TopMoverRequest list successfully......");
		return serviceRequestMapper;
	}

	private boolean isDelay(final TopMoverRequest request) {
		if (request.getDelay() != null) {
			return request.getDelay();
		}
		return true;
	}

	private List<Map<String, String>> getRicCode(final String chainName) {
		TopMoverLabciService.logger.debug("Chain Mapping Rule is " + this.chainMapping.getMappingRule());
		JsonParser parser = new JsonParser();
		JsonObject jsonObject = parser.parse(this.chainMapping.getMappingRule()).getAsJsonObject();
		if (jsonObject != null && jsonObject.get(chainName) != null) {
			JsonArray jsonArray = jsonObject.get(chainName).getAsJsonArray();
			List<Map<String, String>> chainList = new ArrayList<Map<String, String>>();
			Iterator<JsonElement> it = jsonArray.iterator();
			JsonObject chainItem = null;
			Map<String, String> map = null;
			while (it.hasNext()) {
				chainItem = it.next().getAsJsonObject();
				map = new LinkedHashMap<String, String>();
				map.put(Constant.CHAIN_TYPE, chainItem.get(Constant.CHAIN_TYPE).toString()
						.replaceAll(SymbolConstant.SYMBOL_DOUBLE_QUOTATION_MARKS, ""));
				map.put(Constant.CHAIN_CODE, chainItem.get(Constant.CHAIN_CODE).toString()
						.replaceAll(SymbolConstant.SYMBOL_DOUBLE_QUOTATION_MARKS, ""));
				map.put(Constant.RIC, chainItem.get(Constant.RIC).toString()
						.replaceAll(SymbolConstant.SYMBOL_DOUBLE_QUOTATION_MARKS, ""));
				chainList.add(map);
			}
			TopMoverLabciService.logger.debug(chainName + " chain type , ric and chain code is " + chainList);
			return chainList;
		} else {
			TopMoverLabciService.logger
					.error("The chain name can not find from chain mapping rule , chain name is " + chainName);
			throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
		}
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
		ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
		ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_TOP_TEN_MOVERS_REQUEST);
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
					TopMoverLabciService.logger.error("Labci Undefined error", e);
					throw new VendorException(ExCodeConstant.EX_CODE_LABCI_UNDEFINED_ERROR, e);
				} else if (e instanceof ConnectTimeoutException) {
					TopMoverLabciService.logger.error("Access Labci error", e);
					throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_LABCI_ERROR, e);
				} else {
					TopMoverLabciService.logger.error("Labci server error", e);
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
		@SuppressWarnings("unchecked")
		Map<String, String> serviceResponseMapper = (Map<String, String>) serviceResponse;
		Object validServiceResponseMapper = new Object();
		validServiceResponseMapper = LabciResponse(serviceResponseMapper);
		return validServiceResponseMapper;
	}

	private Object LabciResponse(Map<String, String> serviceResponseMapper) throws Exception {
		Map<String, Map<String, LabciResponse>> validServiceResponseMapper = new LinkedHashMap<>();
		List<String> validates = new ArrayList<>();
		for (Map.Entry<String, String> responseMapper : serviceResponseMapper.entrySet()) {
			String key = responseMapper.getKey();
			Map<String, LabciResponse> map = EachLabciResponse(responseMapper.getValue());
			if (!map.isEmpty()) {
				validServiceResponseMapper.put(key, map);
				for (Map.Entry<String, LabciResponse> entry : map.entrySet()) {
					LabciResponse labciResponse = entry.getValue();
					if (!StringUtil.isValid(labciResponse.getParent())) {
						continue;
					}
					validates.add(entry.getKey());
				}
			}
		}
		try {
			saveAccessLogLabci(validServiceResponseMapper);
//			throw new ApplicationException(ExCodeConstant.EX_CODE_DB_ACTION_FAIL);
		} catch (Exception e) {
			TopMoverLabciService.logger.error("Error: Failed to write quotes history", e);
			throw new ApplicationException(ExCodeConstant.EX_CODE_DB_ACTION_FAIL);
		}
		return validServiceResponseMapper;
	}

	private void saveAccessLogLabci(Map<String, Map<String, LabciResponse>> validServiceResponseMapper)
			throws ParseException {
		List<QuoteAccessLogForTopMover> quoteAccessLogs = new ArrayList<>();
		TopMoverRequest request = (TopMoverRequest) ArgsHolder
				.getArgs(Constant.THREAD_INVISIBLE_TOP_TEN_MOVERS_REQUEST);
		CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
		int size = 10;
		if (request.getTopNum() != null) {
			size = request.getTopNum();
		}
		Long userReferenceId = getUserId();
		for (Map.Entry<String, Map<String, LabciResponse>> jsonArrayMapper : validServiceResponseMapper.entrySet()) {
			QuoteAccessLogForTopMover quoteAccessLog = new QuoteAccessLogForTopMover();
			Map<String, LabciResponse> resultJsonArray = jsonArrayMapper.getValue();
			if (resultJsonArray.entrySet() != null && resultJsonArray.entrySet().size() > 0
					&& resultJsonArray.entrySet().size() < size) {
				size = resultJsonArray.entrySet().size();
			}
			if (size > 10) {
				size = 10;
			}
			List<String> symbols = new ArrayList<>();
			int num = 0;
			for (Map.Entry<String, LabciResponse> jsonArrayMapper2 : resultJsonArray.entrySet()) {
				LabciResponse labciResponse = jsonArrayMapper2.getValue();
				if (!StringUtil.isValid(labciResponse.getParent())) {
					continue;
				}
				if (num == size) {
					break;
				}
				Map<String, Object> data = jsonArrayMapper2.getValue().getFields();
				symbols.add(LabciPropsUtil.inOrderStrProps("mnemonic", TopMoverLabciService.DEFAULT_OPTION, data));
				num++;
			}
			try {
				this.predSrchService.precSrch(symbols, header);
			} catch (Exception e) {
				TopMoverLabciService.logger.debug(" PredSrch is null ");
			}
			String exchangeCode = "";
			StringBuffer sb = new StringBuffer();
			StringBuffer stringBuffer = new StringBuffer();
			num = 0;
			for (Map.Entry<String, LabciResponse> jsonArrayMapper2 : resultJsonArray.entrySet()) {
				LabciResponse labciResponse = jsonArrayMapper2.getValue();
				if (!StringUtil.isValid(labciResponse.getParent())) {
					continue;
				}
				if (num == size) {
					break;
				}
				Map<String, Object> data = jsonArrayMapper2.getValue().getFields();
				PredSrchResponse predSrchResp = this.predSrchService.localPredSrch(jsonArrayMapper2.getKey(),
						Constant.PROD_CDE_ALT_CLASS_CODE_T);
				if (predSrchResp == null) {
					predSrchResp = new PredSrchResponse();
				}
				String symbol = StringUtil.isValid(predSrchResp.getSymbol()) ? predSrchResp.getSymbol()
						: LabciPropsUtil.inOrderStrProps("mnemonic", TopMoverLabciService.DEFAULT_OPTION, data);
				BigDecimal quotePrice = LabciPropsUtil.inOrderNumberProps("nominalPrice",
						TopMoverLabciService.DEFAULT_OPTION, data);
				String quote = quotePrice != null ? quotePrice.toString() : "";
				exchangeCode = StringUtil.isValid(predSrchResp.getExchange()) ? predSrchResp.getExchange()
						: LabciPropsUtil.inOrderStrProps("exchangeCode", TopMoverLabciService.DEFAULT_OPTION, data);
				stringBuffer.append(symbolToStr(symbol, quote));
				stringBuffer.append(SymbolConstant.SYMBOL_SEMISOLON);
				num++;
			}
			if (stringBuffer.length() == 0) {
				continue;
			}
			exchangeCode = this.labciProps.getExchangeMapping(exchangeCode);
			sb.append(stringBuffer);
			sb.append(SymbolConstant.SYMBOL_VERTICAL_LINE);
			boolean rqstType = isDelay(request);
			String respType = TopMoverLabciService.ACCES_TYPE_DELAY;
			if (!rqstType) {
				respType = this.marketHourProperties.checkTradingHoursByExchange(exchangeCode)
						? TopMoverLabciService.ACCES_TYPE_DELAY
						: TopMoverLabciService.ACCES_TYPE_REAL_TIME;
			}
			quoteAccessLog.setUserReferenceId(userReferenceId);
			quoteAccessLog.setMarketCode(request.getMarket());
			quoteAccessLog.setExchangeCode(exchangeCode);
			quoteAccessLog.setApplicationCode(header.getAppCode());
			quoteAccessLog.setCountryCode(header.getCountryCode());
			quoteAccessLog.setGroupMember(header.getGroupMember());
			quoteAccessLog.setChannelId(header.getChannelId());
			quoteAccessLog.setFuntionId(header.getFunctionId());
			quoteAccessLog.setAccessCommand(TopMoverLabciService.ACCES_CMND_CDE_TOP_MOVER);
			// quoteAccessLog.setChargeCategory(null);
			quoteAccessLog.setRequestType(
					rqstType ? TopMoverLabciService.ACCES_TYPE_DELAY : TopMoverLabciService.ACCES_TYPE_REAL_TIME);
			quoteAccessLog.setAccessCount(num);
			quoteAccessLog.setResponseType(respType);
			quoteAccessLog.setResponseText(sb.toString());
			// quoteAccessLog.setCommentText(QuotesLabciService.ACCES_SYS_CHANNEL);
			quoteAccessLog.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
			quoteAccessLogs.add(quoteAccessLog);
		}
		this.quoteAccessLogService.saveQuoteAccessLog(quoteAccessLogs);
	}

	private Long getUserId() {
		CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
		QuoteUserForTopMover quoteUser = new QuoteUserForTopMover();
		quoteUser.setUserExtnlId(ArgsHolder.getArgs(TopMoverLabciService.CUSTOMER_ID).toString());
		quoteUser.setUserType(TopMoverLabciService.USER_TYPE_CDE_CUST);
		quoteUser.setGroupMember(header.getGroupMember());
		quoteUser.setUserMarketCode(header.getCountryCode());
		quoteUser.setMonitorFlag((long) 0);
		return this.quoteUserService.getUserByExtnlId(quoteUser);
	}

	private String symbolToStr(final String symbol, final String quote) {
		String priceDetail = "";
		if (null != quote) {
			priceDetail = TopMoverLabciService.KEY_L + quote;
		}
		String result = symbol + SymbolConstant.SYMBOL_LEFT_CIRCLE_BRACKET + priceDetail
				+ SymbolConstant.SYMBOL_RIGHT_CIRCLE_BRACKET;
		return result;
	}

	private Map<String, LabciResponse> EachLabciResponse(final String serviceResponse) throws JAXBException {
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
	protected TopMoverLabciResponse convertResponse(final Object validServiceResponse) throws Exception {
		TopMoverLabciResponse response = new TopMoverLabciResponse();
		response.setTopMovers(labciResponse(validServiceResponse));
		return response;
	}

	private String companyName(String key, Map<String, Object> data, String local) {
		String name = LabciPropsUtil.inOrderStrProps("localName", key, data);
		if (StringUtil.isValid(name)) {
			String[] names = name.split(SymbolConstant.SYMBOL_VERTICAL_LINE_ESCAPE);
			switch (local) {
			case "zh_CN":
				return names[1];
			case "zh_HK":
				return names[0];
			default:
				return LabciPropsUtil.inOrderStrProps("quoteName", key, data);
			}
		}
		return LabciPropsUtil.inOrderStrProps("quoteName", key, data);
	}

	private List<TopMoverLabciTable> labciResponse(Object validServiceResponse) {
		CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
		TopMoverRequest request = (TopMoverRequest) ArgsHolder
				.getArgs(Constant.THREAD_INVISIBLE_TOP_TEN_MOVERS_REQUEST);
		int size = 10;
		if (request.getTopNum() != null) {
			size = request.getTopNum();
		}
		@SuppressWarnings("unchecked")
		Map<String, Map<String, LabciResponse>> resultJsonArrayMapper = (Map<String, Map<String, LabciResponse>>) validServiceResponse;
		List<TopMoverLabciTable> topMoverLabciTables = new ArrayList<>();
		for (Map.Entry<String, Map<String, LabciResponse>> jsonArrayMapper : resultJsonArrayMapper.entrySet()) {
			TopMoverLabciTable topMoverLabciTable = new TopMoverLabciTable();

			List<TopMoverLabciProduct> topMoverLabciProducts = new ArrayList<>();
			Map<String, LabciResponse> resultJsonArray = jsonArrayMapper.getValue();
			if (resultJsonArray.entrySet() != null && resultJsonArray.entrySet().size() > 0
					&& resultJsonArray.entrySet().size() < size) {
				size = resultJsonArray.entrySet().size();
			}
			if (size > 10) {
				size = 10;
			}
			int num = 0;
			for (Map.Entry<String, LabciResponse> jsonArrayMapper2 : resultJsonArray.entrySet()) {
				LabciResponse labciResponse = jsonArrayMapper2.getValue();
				if (!StringUtil.isValid(labciResponse.getParent())) {
					continue;
				}
				if (num == size) {
					break;
				}
				Map<String, Object> data = labciResponse.getFields();
				TopMoverLabciProduct topMoverLabciProduct = new TopMoverLabciProduct();
				topMoverLabciProducts.add(topMoverLabciProduct);
				PredSrchResponse predSrchResponse = this.predSrchService.localPredSrch(jsonArrayMapper2.getKey(),
						Constant.PROD_CDE_ALT_CLASS_CODE_T);
				boolean delay = isDelay(request);
				if (predSrchResponse == null) {
					predSrchResponse = new PredSrchResponse();
				}
				String exchangeCode = this.labciProps.getExchangeMapping(
						StringUtil.isValid(predSrchResponse.getExchange()) ? predSrchResponse.getExchange()
								: LabciPropsUtil.inOrderStrProps("exchangeCode", TopMoverLabciService.DEFAULT_OPTION,
										data));
				if (!delay) {
					delay = this.marketHourProperties.checkTradingHoursByExchange(exchangeCode);
				}
				topMoverLabciProduct.setSymbol(StringUtil.isValid(predSrchResponse.getSymbol())
						? predSrchResponse.getSymbol()
						: LabciPropsUtil.inOrderStrProps("mnemonic", TopMoverLabciService.DEFAULT_OPTION, data));
				topMoverLabciProduct.setRic(jsonArrayMapper2.getKey());
				topMoverLabciProduct.setMarket(request.getMarket());
				topMoverLabciProduct.setName(
						StringUtil.isValid(predSrchResponse.getProductName()) ? predSrchResponse.getProductName()
								: companyName(TopMoverLabciService.DEFAULT_OPTION, data, header.getLocale()));
				topMoverLabciProduct.setPrice(
						LabciPropsUtil.inOrderNumberProps("nominalPrice", TopMoverLabciService.DEFAULT_OPTION, data));
				topMoverLabciProduct.setDelay(delay);
				topMoverLabciProduct.setIsQuotable(predSrchResponse.getMarket() != null ? true : false);
				topMoverLabciProduct.setChangeAmount(
						LabciPropsUtil.subtractProps("changeAmount", TopMoverLabciService.DEFAULT_OPTION, data));
				topMoverLabciProduct.setChangePercent(LabciPropsUtil.growthRateProps("changePercent",
						TopMoverLabciService.DEFAULT_OPTION, data, new MathContext(4, RoundingMode.HALF_UP)));
				topMoverLabciProduct.setVolume(
						LabciPropsUtil.inOrderNumberProps("volume", TopMoverLabciService.DEFAULT_OPTION, data));
				topMoverLabciProduct.setOpenPrice(
						LabciPropsUtil.inOrderNumberProps("openPrice", TopMoverLabciService.DEFAULT_OPTION, data));
				topMoverLabciProduct.setPreviousClosePrice(LabciPropsUtil.inOrderNumberProps("previousClosePrice",
						TopMoverLabciService.DEFAULT_OPTION, data));
				topMoverLabciProduct.setTurnover(
						LabciPropsUtil.inOrderNumberProps("turnover", TopMoverLabciService.DEFAULT_OPTION, data));
				topMoverLabciProduct.setScore(null);
				topMoverLabciProduct.setProductType(
						StringUtil.isValid(predSrchResponse.getProductType()) ? predSrchResponse.getProductType()
								: request.getProductType());
				topMoverLabciProduct.setCurrency(
						LabciPropsUtil.inOrderStrProps("currency", TopMoverLabciService.DEFAULT_OPTION, data));
				topMoverLabciProduct.setAsOfDateTime(LabciPropsUtil.dateProps("asOfDateTime",
						TopMoverLabciService.DEFAULT_OPTION, data, this.appProps.getTimezone(exchangeCode)));
				num++;
			}
			if (topMoverLabciProducts.size() > 0) {
				String[] key = jsonArrayMapper.getKey().split(SymbolConstant.SYMBOL_VERTICAL_LINE_ESCAPE);
				topMoverLabciTable.setTableKey(key[0]);
				topMoverLabciTable.setChainKey(key[1]);
				topMoverLabciTable.setProducts(topMoverLabciProducts);
				topMoverLabciTables.add(topMoverLabciTable);
			}
		}
		return topMoverLabciTables;
	}
}
