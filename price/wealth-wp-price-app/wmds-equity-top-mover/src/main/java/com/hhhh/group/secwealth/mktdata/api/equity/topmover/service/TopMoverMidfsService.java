/**
 * @Title TopMoverLabciService.java
 * @description TODO
 * @author OJim
 * @time Jun 27, 2019 7:45:05 PM
 */
package com.hhhh.group.secwealth.mktdata.api.equity.topmover.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
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
import com.google.gson.JsonObject;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.EtnetProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.LabciPropsUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.LabciServletBoConvertor;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
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
@Service("midfsTopmoverService")
@ConditionalOnProperty(value = "service.quotes.Labci.injectEnabled")
public class TopMoverMidfsService
		extends AbstractBaseService<TopMoverRequest, TopMoverLabciResponse, CommonRequestHeader> {

	private static final Logger logger = LoggerFactory.getLogger(TopMoverMidfsService.class);

	private static final String HK_MARKET_QUOTE_STATUS = "HK_MARKET_STATUS";

	private static final String ACCES_TYPE_DELAY = "EDSTK";

	private static final String ACCES_TYPE_REAL_TIME = "STOCK";

	private static final String ACCES_CMND_CDE_TOP_MOVER = "MOVER";

	// private static final String USER_TYPE_CDE_STFF = "STFF";

	private static final String USER_TYPE_CDE_CUST = "CUST";

	private static final String CUSTOMER_ID = "MOVER_MIDFS_CUSTOMER_ID";

	private static final String KEY_L = "L=";

	private static final String HEADER_FUNCTION_ID_CHECK = "|01|02|03|04|05|06|07|08|09|10|21|22|23|24|25|26|27|28|29|76|";

	@Autowired
	private LabciProperties labciProps;

	@Autowired
	private HttpClientHelper httpClientHelper;

	@Autowired
	private EtnetProperties etnetProperties;

	@Autowired
	private PredSrchService predSrchService;

	@Autowired()
	@Qualifier("topMoverQuoteUserService")
	private QuoteUserService quoteUserService;

	@Autowired()
	@Qualifier("topMoverQuoteAccessLogService")
	private QuoteAccessLogService quoteAccessLogService;

	@Override
	protected Object convertRequest(final TopMoverRequest request, final CommonRequestHeader header) throws Exception {
		if (!TopMoverMidfsService.HEADER_FUNCTION_ID_CHECK.contains(
				SymbolConstant.SYMBOL_VERTICAL_LINE + header.getFunctionId() + SymbolConstant.SYMBOL_VERTICAL_LINE)) {
			TopMoverMidfsService.logger.error("Header FunctionId don't match the rules or can't be empty.");
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
					TopMoverMidfsService.logger.error("No eID found from rbp cache");
					throw new VendorException(ExCodeConstant.EX_CODE_CACHE_NO_EID_FOUND);
				}
				ArgsHolder.putArgs(TopMoverMidfsService.CUSTOMER_ID, customerId);
			} catch (Exception e) {
				TopMoverMidfsService.logger.error("Cache Distribute Bad Request");
				throw new VendorException(ExCodeConstant.EX_CODE_CACHE_BAD_REQUEST);
			}
		} else {
			if (resultState == CacheDistributeResultStateEnum.INVALID_PARAMETERS) {
				TopMoverMidfsService.logger.error("Cache Distribute Bad Request");
				throw new VendorException(ExCodeConstant.EX_CODE_CACHE_BAD_REQUEST);
			}
			if (resultState == CacheDistributeResultStateEnum.UNCACHED_RECORD) {
				TopMoverMidfsService.logger.error("Cache Distribute don't contains the key you sent");
				throw new VendorException(ExCodeConstant.EX_CODE_CACHE_UNCACHED_RECORD);
			}
			if (resultState == CacheDistributeResultStateEnum.INVALID_HTTP_STATUS) {
				TopMoverMidfsService.logger.error("Get response from the Cache Distribute encounter error");
				throw new VendorException(ExCodeConstant.EX_CODE_CACHE_INVALID_HTTP_STATUS);
			}
		}
		ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER, header);
		ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_TOP_TEN_MOVERS_REQUEST, request);
		Map<String, String> serviceRequestMapper = new LinkedHashMap<String, String>();
		if (StringUtil.isInValid(request.getProductType()) || StringUtil.isInValid(request.getExchangeCode())
				|| StringUtil.isInValid(request.getMoverType()) || StringUtil.isInValid(request.getBoardType())
				|| (request.getTopNum() != null && request.getTopNum() <= 0)) {
			TopMoverMidfsService.logger
					.error(" ProductType or Exchange or MoverType or BoardType or TopNum is empty...... ");
			throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
		}
		ArgsHolder.putArgs(TopMoverMidfsService.HK_MARKET_QUOTE_STATUS, request.getMarket());
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			String token = etnetProperties.getEtnetTokenWithoutVerify();
			params.add(new BasicNameValuePair("moverType", request.getMoverType()));
			params.add(new BasicNameValuePair("boardType", request.getBoardType()));
			params.add(new BasicNameValuePair("realTime", isDelay(request) ? "N" : "Y"));
			params.add(new BasicNameValuePair("locale", getLocale(header)));
			params.add(new BasicNameValuePair("token", token));
		} catch (Exception e) {
			if (e instanceof IOException) {
				TopMoverMidfsService.logger.error("ETNet Undefined error", e);
				throw new VendorException(ExCodeConstant.EX_CODE_ETNET_UNDERFINED_ERROR, e);
			} else if (e instanceof ConnectTimeoutException) {
				TopMoverMidfsService.logger.error("Access ETNet error", e);
				throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_ETNET_ERROR, e);
			} else {
				TopMoverMidfsService.logger.error("ETNet server error", e);
				throw new VendorException(ExCodeConstant.EX_CODE_ETNET_SERVER_ERROR, e);
			}
		}
		String reqParams = URLEncodedUtils.format(params, "UTF-8");
		serviceRequestMapper.put(request.getMoverType() + SymbolConstant.SYMBOL_VERTICAL_LINE + request.getBoardType(),
				reqParams);
		if (MapUtils.isEmpty(serviceRequestMapper)) {
			TopMoverMidfsService.logger.error(" MoverType error ......");
			throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
		}
		TopMoverMidfsService.logger.debug("topmoverRequestMap is " + serviceRequestMapper);
		TopMoverMidfsService.logger.debug("convert TopMoverRequest to TopMoverRequest list successfully......");
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

	private boolean isDelay(final TopMoverRequest request) {
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
		CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
		TopMoverRequest topMoverRequest = (TopMoverRequest) ArgsHolder
				.getArgs(Constant.THREAD_INVISIBLE_TOP_TEN_MOVERS_REQUEST);
		Map<String, String> serviceResponseMapper = new LinkedHashMap<>();
		@SuppressWarnings("unchecked")
		Map<String, String> serviceRequestMapper = (Map<String, String>) serviceRequest;
		for (Map.Entry<String, String> requestMapper : serviceRequestMapper.entrySet()) {
			String key = requestMapper.getKey();
			String request = requestMapper.getValue();
			String response = "";
			try {
				response = this.httpClientHelper.doGet(this.etnetProperties.getProxyName(),
						this.etnetProperties.getEtnetTopMoversUrl(), request, null);
				JsonObject respJsonObj = JsonUtil.getAsJsonObject(response);
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
					response = this.httpClientHelper.doGet(this.etnetProperties.getProxyName(),
							this.etnetProperties.getEtnetTopMoversUrl(), params, null);
				}
			} catch (Exception e) {
				if (e instanceof IOException) {
					TopMoverMidfsService.logger.error("ETNet Undefined error", e);
					throw new VendorException(ExCodeConstant.EX_CODE_ETNET_UNDERFINED_ERROR, e);
				} else if (e instanceof ConnectTimeoutException) {
					TopMoverMidfsService.logger.error("Access ETNet error", e);
					throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_ETNET_ERROR, e);
				} else {
					TopMoverMidfsService.logger.error("ETNet server error", e);
					throw new VendorException(ExCodeConstant.EX_CODE_ETNET_SERVER_ERROR, e);
				}
			}
			JsonObject respJsonObj = JsonUtil.getAsJsonObject(response);
			JsonArray resultJsonArray = new JsonArray();
			try {
				resultJsonArray = JsonUtil.getAsJsonArray(respJsonObj, "stockList");
			} catch (Exception e) {
				TopMoverMidfsService.logger.error("ETnet invalid response");
				throw new VendorException(ExCodeConstant.EX_CODE_ETNET_INVALID_RESPONSE);
			}
			String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
			String service = this.labciProps.getLabciService(site, false);
			List<String> items = new ArrayList<>();
			for (int i = 0; i < resultJsonArray.size(); i++) {
				JsonObject resultJsonObj = resultJsonArray.get(i).getAsJsonObject();
				String symbol = LabciPropsUtil.inOrderStrProps("symbol", resultJsonObj);
				if (StringUtil.isValid(symbol)) {
					items.add(symbol);
				}
			}
			if (items.size() > 0) {
				String symbolList = LabciServletBoConvertor.genSymbols(items, service);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("symbol", symbolList));
				params.add(new BasicNameValuePair("channel", header.getChannelId()));
				params.add(new BasicNameValuePair("delay", isDelay(topMoverRequest) + ""));
				String reqParams = URLEncodedUtils.format(params, "UTF-8");
				try {
					serviceResponseMapper.put(key,
							this.httpClientHelper.doGet(this.labciProps.getMidfsBmpQuoteUrl(), reqParams, null));
				} catch (Exception e) {
					if (e instanceof IOException) {
						TopMoverMidfsService.logger.error("Midfs Undefined error", e);
						throw new VendorException(ExCodeConstant.EX_CODE_MIDFS_UNDEFINED_ERROR, e);
					} else if (e instanceof ConnectTimeoutException) {
						TopMoverMidfsService.logger.error("Access Midfs error", e);
						throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_Midfs_ERROR, e);
					} else {
						TopMoverMidfsService.logger.error("Midfs server error", e);
						throw new VendorException(ExCodeConstant.EX_CODE_MIDFS_SERVER_ERROR, e);
					}
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
		validServiceResponseMapper = HKMarketResponse(serviceResponseMapper);
		return validServiceResponseMapper;
	}

	/**
	 * @Title HKMarketResponse
	 * @Description
	 * @param serviceResponseMapper
	 * @return
	 * @return Object
	 * @Author OJim
	 */
	private Object HKMarketResponse(Map<String, String> serviceResponseMapper) {
		Map<String, JsonArray> validServiceResponseMapper = new LinkedHashMap<>();
		for (Map.Entry<String, String> responseMapper : serviceResponseMapper.entrySet()) {
			String key = responseMapper.getKey();
			JsonArray jsonArray = validateEachMarketResponse(responseMapper.getValue());
			if (jsonArray != null && jsonArray.size() > 0) {
				validServiceResponseMapper.put(key, jsonArray);
			}
		}
		try {
			saveAccessLogHKMarket(validServiceResponseMapper);
//			throw new ApplicationException(ExCodeConstant.EX_CODE_DB_ACTION_FAIL);
		} catch (Exception e) {
			TopMoverMidfsService.logger.error("Error: Failed to write quotes log:", e);
			throw new ApplicationException(ExCodeConstant.EX_CODE_DB_ACTION_FAIL);
		}
		return validServiceResponseMapper;
	}

	/**
	 * @Title saveAccessLogHKMarket
	 * @Description
	 * @param validServiceResponseMapper
	 * @return void
	 * @throws ParseException
	 * @Author OJim
	 */
	private void saveAccessLogHKMarket(Map<String, JsonArray> validServiceResponseMapper) throws ParseException {
		List<QuoteAccessLogForTopMover> quoteAccessLogs = new ArrayList<>();
		TopMoverRequest request = (TopMoverRequest) ArgsHolder
				.getArgs(Constant.THREAD_INVISIBLE_TOP_TEN_MOVERS_REQUEST);
		CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
		int size = 10;
		if (request.getTopNum() != null) {
			size = request.getTopNum();
		}
		Long userReferenceId = getUserId();
		for (Map.Entry<String, JsonArray> jsonArrayMapper : validServiceResponseMapper.entrySet()) {
			QuoteAccessLogForTopMover quoteAccessLog = new QuoteAccessLogForTopMover();
			quoteAccessLogs.add(quoteAccessLog);

			JsonArray resultJsonArray = jsonArrayMapper.getValue();
			if (resultJsonArray.size() > 0 && resultJsonArray.size() < size) {
				size = resultJsonArray.size();
			}
			if (size > 10) {
				size = 10;
			}
			List<String> symbols = new ArrayList<>();
			int num = 0;
			for (int i = 0; i < resultJsonArray.size(); i++) {
				if (num == size) {
					break;
				}
				JsonObject resultJsonObj = resultJsonArray.get(i).getAsJsonObject();
				symbols.add(LabciPropsUtil.inOrderStrProps("symbol", resultJsonObj));
				num++;
			}
			try {
				this.predSrchService.precSrch(symbols, header);
			} catch (Exception e) {
				TopMoverMidfsService.logger.debug(" PredSrch is null ");
			}
			StringBuffer sb = new StringBuffer();
			StringBuffer stringBuffer = new StringBuffer();
			num = 0;
			for (int i = 0; i < resultJsonArray.size(); i++) {
				if (num == size) {
					break;
				}
				JsonObject resultJsonObj = resultJsonArray.get(i).getAsJsonObject();
				PredSrchResponse predSrchResp = this.predSrchService.localPredSrch(
						LabciPropsUtil.inOrderStrProps("symbol", resultJsonObj), Constant.PROD_CDE_ALT_CLASS_CODE_M);
				if (predSrchResp == null) {
					predSrchResp = new PredSrchResponse();
				}
				String symbol = StringUtil.isValid(predSrchResp.getSymbol()) ? predSrchResp.getSymbol()
						: LabciPropsUtil.inOrderStrProps("symbol", resultJsonObj);
				String quote = LabciPropsUtil.inOrderStrProps("nominalPrice", resultJsonObj);
				stringBuffer.append(symbolToStr(symbol, quote));
				stringBuffer.append(SymbolConstant.SYMBOL_SEMISOLON);
				num++;
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
			quoteAccessLog.setAccessCommand(TopMoverMidfsService.ACCES_CMND_CDE_TOP_MOVER);
			quoteAccessLog.setChargeCategory("0");
			quoteAccessLog.setRequestType(isDelay(request) ? TopMoverMidfsService.ACCES_TYPE_DELAY
					: TopMoverMidfsService.ACCES_TYPE_REAL_TIME);
			quoteAccessLog.setAccessCount(num);
			quoteAccessLog.setResponseType(isDelay(request) ? TopMoverMidfsService.ACCES_TYPE_DELAY
					: TopMoverMidfsService.ACCES_TYPE_REAL_TIME);
			quoteAccessLog.setResponseText(sb.toString());
			// quoteAccessLog.setCommentText(QuotesLabciService.ACCES_SYS_CHANNEL);
			quoteAccessLog.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
		}
		this.quoteAccessLogService.saveQuoteAccessLog(quoteAccessLogs);
	}

	/**
	 * @Title validateEachMarketResponse
	 * @Description
	 * @param value
	 * @return
	 * @return JsonArray
	 * @Author OJim
	 */
	private JsonArray validateEachMarketResponse(final String serviceResponse) {
		JsonObject respJsonObj = JsonUtil.getAsJsonObject(serviceResponse);
		if (respJsonObj == null) {
			TopMoverMidfsService.logger.error("Invalid response: {}", serviceResponse);
			throw new VendorException(ExCodeConstant.EX_CODE_MIDFS_INVALID_RESPONSE);
		}
		String responseCode = JsonUtil.getAsString(respJsonObj, "responseCode");
		switch (responseCode) {
		case "01":
			TopMoverMidfsService.logger.error("Invalid request parameters", serviceResponse);
			throw new VendorException(ExCodeConstant.EX_CODE_MIDFS_INVALID_REQUEST);
		case "02":
			TopMoverMidfsService.logger.error("Internal system error", serviceResponse);
			throw new VendorException(ExCodeConstant.EX_CODE_MIDFS_SERVER_ERROR);
		case "03":
			TopMoverMidfsService.logger.error("Stock not found", serviceResponse);
			throw new VendorException(ExCodeConstant.EX_CODE_MIDFS_STOCK_NOT_FOUND);
		default:
			break;
		}
		JsonArray resultJsonArray = JsonUtil.getAsJsonArray(respJsonObj, "priceQuotes");
		return resultJsonArray;
	}

	private Long getUserId() {
		CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
		QuoteUserForTopMover quoteUser = new QuoteUserForTopMover();
		quoteUser.setUserExtnlId(ArgsHolder.getArgs(TopMoverMidfsService.CUSTOMER_ID).toString());
		quoteUser.setUserType(TopMoverMidfsService.USER_TYPE_CDE_CUST);
		quoteUser.setGroupMember(header.getGroupMember());
		quoteUser.setUserMarketCode(header.getCountryCode());
		quoteUser.setMonitorFlag((long) 0);
		return this.quoteUserService.getUserByExtnlId(quoteUser);
	}

	private String symbolToStr(final String symbol, final String quote) {
		String priceDetail = "";
		if (null != quote) {
			priceDetail = TopMoverMidfsService.KEY_L + quote;
		}
		String result = symbol + SymbolConstant.SYMBOL_LEFT_CIRCLE_BRACKET + priceDetail
				+ SymbolConstant.SYMBOL_RIGHT_CIRCLE_BRACKET;
		return result;
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
		response.setTopMovers(quotesListResponse(validServiceResponse));
		return response;
	}

	/**
	 * @Title quotesListResponse
	 * @Description
	 * @param validServiceResponse
	 * @return
	 * @return List<TopMoverLabciTable>
	 * @Author OJim
	 */
	private List<TopMoverLabciTable> quotesListResponse(Object validServiceResponse) {
		TopMoverRequest request = (TopMoverRequest) ArgsHolder
				.getArgs(Constant.THREAD_INVISIBLE_TOP_TEN_MOVERS_REQUEST);
		CommonRequestHeader header = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
		int size = 10;
		if (request.getTopNum() != null) {
			size = request.getTopNum();
		}
		@SuppressWarnings("unchecked")
		Map<String, JsonArray> resultJsonArrayMapper = (Map<String, JsonArray>) validServiceResponse;
		List<TopMoverLabciTable> topMoverLabciTables = new ArrayList<>();
		for (Map.Entry<String, JsonArray> jsonArrayMapper : resultJsonArrayMapper.entrySet()) {
			TopMoverLabciTable topMoverLabciTable = new TopMoverLabciTable();
			topMoverLabciTables.add(topMoverLabciTable);
			String[] key = jsonArrayMapper.getKey().split(SymbolConstant.SYMBOL_VERTICAL_LINE_ESCAPE);
			topMoverLabciTable.setTableKey(key[0]);
			topMoverLabciTable.setBoardType(key[1]);
			List<TopMoverLabciProduct> topMoverLabciProducts = new ArrayList<>();
			topMoverLabciTable.setProducts(topMoverLabciProducts);
			JsonArray resultJsonArray = jsonArrayMapper.getValue();
			if (resultJsonArray.size() > 0 && resultJsonArray.size() < size) {
				size = resultJsonArray.size();
			}
			if (size > 10) {
				size = 10;
			}
			int num = 0;
			for (int i = 0; i < resultJsonArray.size(); i++) {
				if (num == size) {
					break;
				}
				JsonObject resultJsonObj = resultJsonArray.get(i).getAsJsonObject();
				TopMoverLabciProduct topMoverLabciProduct = new TopMoverLabciProduct();
				topMoverLabciProducts.add(topMoverLabciProduct);
				PredSrchResponse predSrchResponse = this.predSrchService.localPredSrch(
						LabciPropsUtil.inOrderStrProps("symbol", resultJsonObj), Constant.PROD_CDE_ALT_CLASS_CODE_M);
				String ric = LabciPropsUtil.inOrderStrProps("symbol", resultJsonObj);
				if (predSrchResponse == null) {
					predSrchResponse = new PredSrchResponse();
				}
				if (predSrchResponse != null && predSrchResponse.getProdAltNumSegs() != null
						&& predSrchResponse.getProdAltNumSegs().size() > 0) {
					for (ProdAltNumSeg prodAltNumSeg : predSrchResponse.getProdAltNumSegs()) {
						if (Constant.PROD_CDE_ALT_CLASS_CODE_T.equals(prodAltNumSeg.getProdCdeAltClassCde())) {
							ric = prodAltNumSeg.getProdAltNum();
							break;
						}
					}
				}
				topMoverLabciProduct
						.setSymbol(StringUtil.isValid(predSrchResponse.getSymbol()) ? predSrchResponse.getSymbol()
								: LabciPropsUtil.inOrderStrProps("symbol", resultJsonObj));
				topMoverLabciProduct.setRic(ric);
				topMoverLabciProduct.setMarket(request.getMarket());
				// topMoverLabciProduct.setName(StringUtil.isInValid(predSrchResponse.getProductName())
				// ? companyName(resultJsonObj, header.getLocale())
				// : predSrchResponse.getProductName());
				topMoverLabciProduct.setName(companyName(resultJsonObj, header.getLocale()));
				topMoverLabciProduct.setPrice(LabciPropsUtil.inOrderNumberProps("nominalPrice", resultJsonObj));
				// topMoverLabciProduct.setDelay(LabciPropsUtil.inOrderBooProps("delay",
				// resultJsonObj));
				topMoverLabciProduct.setChangeAmount(LabciPropsUtil.inOrderNumberProps("changeAmount", resultJsonObj));
				topMoverLabciProduct
						.setChangePercent(LabciPropsUtil.inOrderNumberProps("changePercent", resultJsonObj));
				// topMoverLabciProduct.setVolume(LabciPropsUtil.inOrderNumberProps("volume",
				// resultJsonObj));
				// topMoverLabciProduct.setOpenPrice(LabciPropsUtil.inOrderNumberProps("openPrice",
				// resultJsonObj));
				// topMoverLabciProduct
				// .setPreviousClosePrice(LabciPropsUtil.inOrderNumberProps("preClosePrice",
				// resultJsonObj));
				// topMoverLabciProduct.setTurnover(LabciPropsUtil.inOrderNumberProps("turnover",
				// resultJsonObj));
				topMoverLabciProduct.setScore(null);
				topMoverLabciProduct.setProductType(
						StringUtil.isValid(predSrchResponse.getProductType()) ? predSrchResponse.getProductType()
								: request.getProductType());
				// topMoverLabciProduct.setCurrency(LabciPropsUtil.inOrderStrProps("currency",
				// resultJsonObj));
				topMoverLabciProduct.setAsOfDateTime(LabciPropsUtil.inOrderStrProps("asOfDateTime", resultJsonObj));
				num++;
			}
		}
		return topMoverLabciTables;
	}

	private String companyName(JsonObject resultJsonObj, String local) {
		switch (local) {
		case "zh_CN":
			return LabciPropsUtil.inOrderStrProps("companyNameSC", resultJsonObj);
		case "zh_HK":
			return LabciPropsUtil.inOrderStrProps("companyNameTC", resultJsonObj);
		default:
			return LabciPropsUtil.inOrderStrProps("companyNameEN", resultJsonObj);
		}
	}
}
