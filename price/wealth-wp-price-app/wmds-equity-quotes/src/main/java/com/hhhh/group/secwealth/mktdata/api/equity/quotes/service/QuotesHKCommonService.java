package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.request.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.QuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.SECQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ServiceProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.service.QuotesServiceRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.Message;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.util.ConvertorsUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResponse;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResult;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResultStateEnum;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.exception.ExTraceCodeGenerator;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.ExResponseComponent;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.entity.ExResponseEntity;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

@Service
@ConditionalOnProperty(value = "service.quotes.hkEquity.injectEnabled", havingValue = "true")
public class QuotesHKCommonService {

	private static final Logger logger = LoggerFactory.getLogger(QuotesHKCommonService.class);

	private static final String CUSTOMER_ID = "QUOTE_LABCI_CUSTOMER_ID";

	private static final String THREAD_CACHE_NODE = "node";

	@Autowired
	private PredSrchService predSrchService;

	@Autowired
	private ExResponseComponent exRespComponent;

	public void validateCacheDistributeResult(CacheDistributeResult result) {
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
					QuotesHKCommonService.logger.error("No eID found from rbp cache");
					throw new VendorException(ExCodeConstant.EX_CODE_CACHE_NO_EID_FOUND);
				}
				ArgsHolder.putArgs(QuotesHKCommonService.CUSTOMER_ID, customerId);
				ArgsHolder.putArgs(QuotesHKCommonService.THREAD_CACHE_NODE, node);
			} catch (Exception e) {
				QuotesHKCommonService.logger.error("Cache Distribute Bad Request");
				throw new VendorException(ExCodeConstant.EX_CODE_CACHE_BAD_REQUEST);
			}
		} else {
			if (resultState == CacheDistributeResultStateEnum.INVALID_PARAMETERS) {
				QuotesHKCommonService.logger.error("Cache Distribute Bad Request");
				throw new VendorException(ExCodeConstant.EX_CODE_CACHE_BAD_REQUEST);
			}
			if (resultState == CacheDistributeResultStateEnum.UNCACHED_RECORD) {
				QuotesHKCommonService.logger.error("Cache Distribute don't contains the key you sent");
				throw new VendorException(ExCodeConstant.EX_CODE_CACHE_UNCACHED_RECORD);
			}
			if (resultState == CacheDistributeResultStateEnum.INVALID_HTTP_STATUS) {
				QuotesHKCommonService.logger.error("Get response from the Cache Distribute encounter error");
				throw new VendorException(ExCodeConstant.EX_CODE_CACHE_INVALID_HTTP_STATUS);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public QuotesServiceRequest buildServiceRequest(final SECQuotesRequest request, final CommonRequestHeader header,
			String prodClsCde) {
		String requestType = Constant.CASE_10;
		if (StringUtil.isValid(request.getRequestType())) {
			requestType = request.getRequestType();
		}
		if (!Constant.CASE_10.equals(requestType) && !Constant.CASE_20.equals(requestType)) {
			QuotesHKCommonService.logger.error("Wrong requestType");
			throw new CommonException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
		}
		QuotesServiceRequest quoteServiceRequest = new QuotesServiceRequest();
		quoteServiceRequest.setDelay(isDelay(request));
		List<String> symbols = new ArrayList<>();
		Map<String, List<String>> symbolsMap = new LinkedHashMap<>();
		for (ProductKey productKey : request.getProductKeys()) {
			if (Constant.CASE_20.equals(requestType)) {//quoteDetail, only get one symbol and return
				symbols.add(productKey.getProdAltNum());
				symbolsMap.put(productKey.getProductType(), symbols);
				break;
			}
			symbols.add(productKey.getProdAltNum());//beside quoteDetail, may query symbol list
			if (symbolsMap.containsKey(productKey.getProductType())) {
				symbolsMap.get(productKey.getProductType()).add(productKey.getProdAltNum());//
			} else {
				List<String> symbol = new ArrayList<>();
				symbol.add(productKey.getProdAltNum());
				symbolsMap.put(productKey.getProductType(), symbol);
			}
		}
		List<PredSrchResponse> responses = new ArrayList<>();
		for (Map.Entry<String, List<String>> symbolMap : symbolsMap.entrySet()) {//
			PredSrchRequest predSrchRequest = new PredSrchRequest();
			String[] assetClasses = new String[1];
			assetClasses[0] = symbolMap.getKey().toString();//
			predSrchRequest.setAssetClasses(assetClasses);
			predSrchRequest.setKeyword(symbolMap.getValue().toArray(new String[symbolMap.getValue().size()]));
			predSrchRequest.setTopNum(String.valueOf(symbolMap.getValue().size()));
			try {
				responses.addAll(this.predSrchService.precSrch(predSrchRequest, header));
			} catch (Exception e) {
				QuotesHKCommonService.logger.error("precSearch error is :" + e.toString());
			}
		}

		Map<String, Object> predsrchSymbol = ConvertorsUtil.getPredsrchSymbol(request.getProductKeys(), symbolsMap,
				responses, prodClsCde);
		if (responses != null && responses.size() != symbols.size()) {
			ExResponseEntity exResponse = this.exRespComponent
					.getExResponse(ExCodeConstant.EX_CODE_PREDSRCH_INVALID_RESPONSE);
			List<ProductKey> missingProdKeys = (List<ProductKey>) predsrchSymbol.get(Constant.MISSING_PROD_KEYS);
			addMessage(missingProdKeys, exResponse);//
		}
		quoteServiceRequest
				.setServiceProductKeys((List<ServiceProductKey>) predsrchSymbol.get(Constant.SERVICE_PROD_KEYS));
		ArgsHolder.putArgs(Constant.SERVICE_PROD_KEYS, predsrchSymbol.get(Constant.SERVICE_PROD_KEYS));
		return quoteServiceRequest;

	}

	private boolean isDelay(final QuotesRequest request) {
		if (request.getDelay() != null) {
			return request.getDelay();
		}
		return true;
	}

	public void addMessage(List<ProductKey> missingProdKeys, ExResponseEntity exResponse) {
		for (ProductKey productKey : missingProdKeys) {
			Message message = new Message();
			message.setReasonCode(exResponse.getReasonCode());
			message.setText(exResponse.getText());
			String traceCode = ExTraceCodeGenerator.generate();
			message.setTraceCode(traceCode);
			message.setProductType("SEC");
			message.setProdCdeAltClassCde("M");
			message.setProductType(productKey.getProductType());
			message.setProdCdeAltClassCde(productKey.getProdCdeAltClassCde());
			message.setProdAltNum(productKey.getProdAltNum());
			addMessage(message);
		}
	}

	public void addMessage(final Message message) {
		if (ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES) == null) {
			ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES, new ArrayList<Message>());
		}
		@SuppressWarnings("unchecked")
		List<Message> messages = (List<Message>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES);
		messages.add(message);
	}
}
