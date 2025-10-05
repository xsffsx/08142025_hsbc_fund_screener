package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.service.MarketAgreementService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.vendor.midfs.RequestPattern;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.ApplicationProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.MIDFSProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.BeanUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.QuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.SECQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ServiceProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.service.QuotesServiceRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.Message;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesLabciQuote;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesLabciResponse;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResult;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.utils.CacheDistributeHolder;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.exception.ExTraceCodeGenerator;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.ExResponseComponent;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.entity.ExResponseEntity;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

@Service("quotesHKMidfsService")
@ConditionalOnProperty(value = "service.quotes.hkEquity.injectEnabled", havingValue = "true")
public class QuotesHKMidfsServiceImpl
		extends AbstractBaseService<SECQuotesRequest, QuotesLabciResponse, CommonRequestHeader> {

	private static final Logger logger = LoggerFactory.getLogger(QuotesHKMidfsServiceImpl.class);

	private static final String ACCES_CMND_CDE_QUOTE_LIST = "QUOTE_LIST";

	private static final String BEAN_PRICE_QUOTE = "PRICE_QUOTE";

	private static final String VENDOR_API = "StockPortfolio";

	private static final String THREAD_INVISIBLE_LOCALE = "LOCALE";

	private static final String responseCode_14 = "14";

	private static final String THREAD_INVISIBLE_STOCK = "Stock";
	
	private static final String THREAD_CACHE_NODE = "node";
	
	private static final String SATFF_CHANNEL_ID = "OHB";

	@Autowired
	MarketAgreementService marketAgreementservice;

	@Autowired
	private ExResponseComponent exRespComponent;

	@Autowired
	private MIDFSProperties midfsProperties;

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private HttpClientHelper httpClientHelper;

	@Autowired()
	@Qualifier("quotesQuoteUserService")
	private QuoteUserService quoteUserService;

	@Autowired()
	@Qualifier("quotesQuoteAccessLogService")
	private QuoteAccessLogService quoteAccessLogService;
	
	@Autowired
	private QuotesHKCommonService quotesHKCommonService;

	@Override
	protected Object convertRequest(final SECQuotesRequest request, final CommonRequestHeader header) throws Exception {
		if (!QuotesHKMidfsServiceImpl.SATFF_CHANNEL_ID.equalsIgnoreCase(header.getChannelId())) {
            CacheDistributeResult result = CacheDistributeHolder.getCacheDistribute();
            this.quotesHKCommonService.validateCacheDistributeResult(result);
        }
		ArgsHolder.putArgs(QuotesHKMidfsServiceImpl.THREAD_INVISIBLE_LOCALE, header.getLocale());
		QuotesServiceRequest quotesServiceRequest = quotesHKCommonService.buildServiceRequest(request, header,
				Constant.PROD_CDE_ALT_CLASS_CODE_M);
		Map<String, String> serviceRequestMapper = new LinkedHashMap<>();
		List<ServiceProductKey> productKeys = quotesServiceRequest.getServiceProductKeys();
		List<String> items = new ArrayList<>();
		for (ServiceProductKey serviceProductKey : productKeys) {
			items.add(serviceProductKey.getProdAltNum());
		}
		String symbolList = StringUtils.join(items, SymbolConstant.SYMBOL_SEMISOLON);
		ArgsHolder.putArgs(QuotesHKMidfsServiceImpl.THREAD_INVISIBLE_STOCK, symbolList);
		ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_DELAY, isDelay(request));
		serviceRequestMapper.put("midfsReqParams", symbolList);
		return serviceRequestMapper;
	}

	private boolean isDelay(final QuotesRequest request) {
		if (request.getDelay() != null) {
			return request.getDelay();
		}
		return true;
	}

	@Override
	protected Object execute(final Object serviceRequest) throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, String> serviceRequestMapper = (Map<String, String>) serviceRequest;
		String requestParams = serviceRequestMapper.get("midfsReqParams");

		if (!StringUtils.isBlank(requestParams)) {
			String commandId = this.midfsProperties.getCommandId(QuotesHKMidfsServiceImpl.VENDOR_API,
					(String) ArgsHolder.getArgs(QuotesHKMidfsServiceImpl.THREAD_INVISIBLE_LOCALE),
					(boolean) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_DELAY));
			RequestPattern requestPattern = this.midfsProperties.getRequestPattern(QuotesHKMidfsServiceImpl.VENDOR_API);
			String parameterPattern = "";
			String requestParameter = "";
			if(ArgsHolder.getArgs(QuotesHKMidfsServiceImpl.THREAD_CACHE_NODE)!=null) {
				JsonNode node = (JsonNode) ArgsHolder.getArgs(QuotesHKMidfsServiceImpl.THREAD_CACHE_NODE);
				parameterPattern = requestPattern.getParameterPattern();
				requestParameter = parameterPattern.replace(Constant.REQUEST_PARAMETER_COMMAND_ID, commandId)
						.replace(Constant.REQUEST_PARAMETER_QUOTE_STOCK, requestParams)
						.replace(Constant.REQUEST_PARAMETER_EID, node.get("eID").asText())
						.replace(Constant.REQUEST_PARAMETER_IBUSERID, node.get("permID").asText())
						.replace(Constant.REQUEST_PARAMETER_OBO, "N")
						.replace(Constant.REQUEST_PARAMETER_PKG_ID, node.get("packageID").asText())
						.replace(Constant.REQUEST_PARAMETER_PKG_STATUS, node.get("packageStatus").asText())
						.replace(Constant.REQUEST_PARAMETER_CHARGE_AC_STATUS, node.get("chargeAcStatusHK").asText())
						.replace(Constant.REQUEST_PARAMETER_CUS_SEGMENT, node.get("cusSegment").asText())
						.replace(Constant.REQUEST_PARAMETER_BONUS, node.get("bonus").asText())
						.replace(Constant.REQUEST_PARAMETER_LAST_LOGON, node.get("lastLogonTime").asText());
			}else {
				parameterPattern = requestPattern.getDummyParameter();
				requestParameter = parameterPattern.replace(Constant.REQUEST_PARAMETER_COMMAND_ID, commandId)
						.replace(Constant.REQUEST_PARAMETER_QUOTE_STOCK, requestParams);
			}
			return this.httpClientHelper.doGet(requestPattern.getUrl(), requestParameter, null);
		} else {
			return "";
		}
	}

	@Override
	protected Object validateServiceResponse(final Object serviceResponse) throws Exception {
		JsonObject jsonNode = JsonUtil.getAsJsonObject(String.valueOf(serviceResponse));
		if (jsonNode != null) {
			String status = jsonNode.get(this.midfsProperties.getResponseStatusKey()).getAsString();
			if (!this.midfsProperties.isCorrectResponseStatus(status)) {
				String errorMessage = this.midfsProperties.getResponseMessage(status);
				logger.error(errorMessage);
				if (status.equals(QuotesHKMidfsServiceImpl.responseCode_14)) {
					throw new VendorException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
				} else {
					throw new VendorException(ExCodeConstant.EX_CODE_MIDFS_INVALID_RESPONSE);
				}
			}
		}
		return jsonNode;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	protected QuotesLabciResponse convertResponse(final Object validServiceResponse) throws Exception {
		QuotesLabciResponse response = new QuotesLabciResponse();
		List<QuotesLabciQuote> priceQuotes = new ArrayList<>();
		response.setPriceQuotes(priceQuotes);
		JsonObject responseNode = (JsonObject) validServiceResponse;
		List<PredSrchResponse> predsrchList = (List<PredSrchResponse>) ArgsHolder
				.getArgs(Constant.THREAD_INVISIBLE_PREDSRCH_RESPONSE);
		if (predsrchList == null) {
			response.setMessages(getMessages());
			return response;
		}
		for (PredSrchResponse predsrch : predsrchList) {
			JsonObject stockNode = responseNode.get(Constant.MIDFS_RESPONSE_PREFIX + predsrch.getSymbol())
					.getAsJsonObject();
			if (stockNode != null && stockNode.size() > 0) {
				QuotesLabciQuote priceQuote = new QuotesLabciQuote();
				BeanUtil.jsonToBean(priceQuote,
						this.applicationProperties.getResponseFieldMapper(
								QuotesHKMidfsServiceImpl.ACCES_CMND_CDE_QUOTE_LIST,
								QuotesHKMidfsServiceImpl.BEAN_PRICE_QUOTE),
						stockNode);
				// if (!args) {realTime
				// String priceCode = stockNode.get("mkt_stat").getAsString();
				// priceQuote.setPriceCode(this.midfsProperties.getFiledConvert(priceCode));
				// }
				String date = stockNode.get("data_date").getAsString();
				String time = stockNode.get("time_modified").getAsString();
				if (!StringUtils.isEmpty(date) && !StringUtils.isEmpty(time)) {
					priceQuote.setAsOfDateTime(DateUtil.parseDateByTimezone(
							DateUtil.parseString(date + time, Constant.DATE_FORMAT_MIDFS,
									TimeZone.getTimeZone(this.applicationProperties.getTimezone())),
							Constant.TIMEZONE, Constant.DATE_FORMAT_TRIS_ISO8601));
				}
				priceQuote.setCompanyName(stockNode
						.get(this.midfsProperties.getCompanyField(
								String.valueOf(ArgsHolder.getArgs(QuotesHKMidfsServiceImpl.THREAD_INVISIBLE_LOCALE))))
						.getAsString());
				priceQuote.setProdAltNumSegs(predsrch.getProdAltNumSegs());
				priceQuote.setSymbol(predsrch.getSymbol());
				priceQuote.setMarket(predsrch.getMarket());
				priceQuote.setExchangeCode(predsrch.getExchange());
				priceQuote.setProductType(predsrch.getProductType());
				priceQuote.setProductSubType(predsrch.getProductSubType());
				priceQuote.setRiskLvlCde(predsrch.getRiskLvlCde());
				priceQuotes.add(priceQuote);
			} else {
				//response.setMessages(messages);
				Message message = new Message();
				ExResponseEntity exResponse = this.exRespComponent
						.getExResponse(ExCodeConstant.EX_CODE_MIDFS_INVALID_RESPONSE);
				message.setReasonCode(exResponse.getReasonCode());
				message.setText(exResponse.getText());
				String traceCode = ExTraceCodeGenerator.generate();
				message.setTraceCode(traceCode);
				message.setProductType(predsrch.getProductType());
				message.setProdCdeAltClassCde(Constant.PROD_CDE_ALT_CLASS_CODE_M);
				message.setProdAltNum(predsrch.getSymbol());
				quotesHKCommonService.addMessage(message);
			}
		}
		response.setMessages(getMessages());
		return response;
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
