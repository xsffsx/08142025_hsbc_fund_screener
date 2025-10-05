/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.reflect.TypeToken;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.request.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.PredSrchProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.header.Property;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

@Component
public class PredSrchService {

	private static final Logger logger = LoggerFactory.getLogger(PredSrchService.class);

	@Autowired
	private PredSrchProperties predSrchProps;

	@Autowired
	private HttpClientHelper httpClientHelper;

	/**
	 *
	 * <p>
	 * <b> Get PredSrch response from ThreadLocal. </b>
	 * </p>
	 *
	 * @param trisCode
	 * @return
	 */
	public PredSrchResponse localPredSrch(final String trisCode) {
		List<PredSrchResponse> respList = localPredSrches();
		if (respList == null) {
			return null;
		}
		for (PredSrchResponse response : respList) {
			List<ProdAltNumSeg> prodAltNumSegs = response.getProdAltNumSegs();
			for (ProdAltNumSeg prodAltNumSeg : prodAltNumSegs) {
				if ("T".equals(prodAltNumSeg.getProdCdeAltClassCde())
						&& trisCode.equals(prodAltNumSeg.getProdAltNum())) {
					return response;
				}
			}
		}
		return null;
	}

	public PredSrchResponse localPredSrch(final String prodAltNum, String convertors) {
		List<PredSrchResponse> respList = localPredSrches();
		if (respList == null) {
			return null;
		}
		for (PredSrchResponse response : respList) {
			List<ProdAltNumSeg> prodAltNumSegs = response.getProdAltNumSegs();
			for (ProdAltNumSeg prodAltNumSeg : prodAltNumSegs) {
				if (convertors.equals(prodAltNumSeg.getProdCdeAltClassCde())
						&& prodAltNum.equals(prodAltNumSeg.getProdAltNum())) {
					return response;
				}
			}
		}
		return null;
	}

	/**
	 *
	 * <p>
	 * <b> Get PredSrch responses from ThreadLocal. </b>
	 * </p>
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PredSrchResponse> localPredSrches() {
		return (List<PredSrchResponse>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_PREDSRCH_RESPONSE);
	}

	/**
	 *
	 * <p>
	 * <b> Set the request parameters by loading the configuration file. The caller
	 * only needs to pass in symbols. Please note, this method put the returned
	 * value in ThreadLocal. </b>
	 * </p>
	 *
	 * @param symbols
	 * @param header
	 * @return
	 */
	public List<PredSrchResponse> precSrch(final List<String> symbols, final CommonRequestHeader header) {
		PredSrchRequest request = new PredSrchRequest();
		request.setKeyword(symbols.toArray(new String[symbols.size()]));
		request.setAssetClasses(this.predSrchProps.getRequestParams().getAssetClasses());
		request.setSearchFields(this.predSrchProps.getRequestParams().getSearchFields());
		request.setSortingFields(this.predSrchProps.getRequestParams().getSearchFields());
		request.setTopNum(String.valueOf(symbols.size()));
		request.setPreciseSrch(this.predSrchProps.getRequestParams().isPreciseSrch());
		List<PredSrchResponse> responses = predSrch(request, getHeaders(header));
		if (responses == null || responses.isEmpty()) {
			PredSrchService.logger.warn("Responses is empty. Invalid symbols for PredSrch: " + symbols);
			throw new VendorException(ExCodeConstant.EX_CODE_PREDSRCH_EMPTY_RESPONSE);
		}
		ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_PREDSRCH_RESPONSE, responses);
		return responses;
	}

	public List<PredSrchResponse> precSrch(final PredSrchRequest request, final CommonRequestHeader header) {
		List<PredSrchResponse> responses = predSrch(request, getHeaders(header));
		if (responses == null || responses.isEmpty()) {
			PredSrchService.logger.warn("Responses is empty. Invalid symbols for PredSrch: " + Arrays.toString(request.getKeyword()));
			throw new VendorException(ExCodeConstant.EX_CODE_PREDSRCH_EMPTY_RESPONSE);
		}
		@SuppressWarnings("unchecked")
		List<PredSrchResponse> response = (List<PredSrchResponse>) ArgsHolder
				.getArgs(Constant.THREAD_INVISIBLE_PREDSRCH_RESPONSE);
		if (response != null && response.size() > 0) {
			response.addAll(responses);
			ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_PREDSRCH_RESPONSE, response);
		} else {
			ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_PREDSRCH_RESPONSE, responses);
		}
		return responses;
	}

	public List<PredSrchResponse> precSrchForNews(final PredSrchRequest request, final CommonRequestHeader header) {
		List<PredSrchResponse> responses = predSrch(request, getHeaders(header));
//		if (responses == null || responses.isEmpty()) {
//			PredSrchService.logger.warn("Responses is empty. Invalid symbols for PredSrch: " + Arrays.toString(request.getKeyword()));
//			throw new VendorException(ExCodeConstant.EX_CODE_PREDSRCH_EMPTY_RESPONSE);
//		}
		@SuppressWarnings("unchecked")
		List<PredSrchResponse> response = (List<PredSrchResponse>) ArgsHolder
				.getArgs(Constant.THREAD_INVISIBLE_PREDSRCH_RESPONSE);
		if (response != null && response.size() > 0) {
			response.addAll(responses);
			ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_PREDSRCH_RESPONSE, response);
		} else {
			ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_PREDSRCH_RESPONSE, responses);
		}
		return responses;
	}

	/*
	 * predictive search without validation, allowed predsearch response is null
	 * used by seame-sector equity,
	 */
	public List<PredSrchResponse> precSrchNoValidation(final List<String> symbols, final CommonRequestHeader header) {
		PredSrchRequest request = new PredSrchRequest();
		request.setKeyword(symbols.toArray(new String[symbols.size()]));
		request.setAssetClasses(this.predSrchProps.getRequestParams().getAssetClasses());
		request.setSearchFields(this.predSrchProps.getRequestParams().getSearchFields());
		request.setSortingFields(this.predSrchProps.getRequestParams().getSearchFields());
		request.setTopNum(String.valueOf(symbols.size()));
		request.setPreciseSrch(this.predSrchProps.getRequestParams().isPreciseSrch());
		List<PredSrchResponse> responses = predSrch(request, getHeaders(header));
		return responses;
	}

	public Map<String, String> getHeaders(final CommonRequestHeader header) {
		Map<String, String> headers = new HashMap<String, String>();
		Field[] fields = header.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Property.class)) {
				Property property = field.getAnnotation(Property.class);
				String headerName = property.value();
				field.setAccessible(true);
				try {
					headers.put(headerName, StringUtil.valueOf(field.get(header)));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					PredSrchService.logger.error("Reflection access field encounter error", e);
					throw new ApplicationException(e);
				}
			}
		}
		return headers;
	}

	public List<PredSrchResponse> predSrch(final PredSrchRequest request, final Map<String, String> headers) {
		String params = "";
		String str = JsonUtil.toJson(request);
		String charset = StandardCharsets.UTF_8.name();
		try {
			params = new StringBuilder().append(this.predSrchProps.getPredSrchBodyPrefix())
					.append(URLEncoder.encode(str, charset)).toString();
		} catch (UnsupportedEncodingException e) {
			PredSrchService.logger
					.error("Encode predsrch request encounter error, str is: " + str + ", charset is: " + charset, e);
			throw new ApplicationException(e);
		}
		String response = "";
		try {
			response = this.httpClientHelper.doGet(this.predSrchProps.getPredSrchUrl(), params, headers);
		} catch (Exception e) {
			PredSrchService.logger.error("Access predsrch encounter error", e);
			throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_PREDSRCH_ERROR, e);
		}
		return JsonUtil.fromJson(response, new TypeToken<ArrayList<PredSrchResponse>>() {
		}.getType());
	}

	/**
	 * @Title localPredSrch
	 * @Description
	 * @param inOrderStrProps
	 * @param string
	 * @param prodCdeAltClassCodeM
	 * @return
	 * @return PredSrchResponse
	 * @Author OJim
	 */
	public PredSrchResponse localPredSrch(String prodAltNum, String key, String convertors) {
		List<PredSrchResponse> respList = localPredSrches();
		if (respList == null) {
			return null;
		}
		for (PredSrchResponse response : respList) {
			if (key.equals(response.getProductType())) {
				List<ProdAltNumSeg> prodAltNumSegs = response.getProdAltNumSegs();
				for (ProdAltNumSeg prodAltNumSeg : prodAltNumSegs) {
					if (convertors.equals(prodAltNumSeg.getProdCdeAltClassCde())
							&& prodAltNum.equals(prodAltNumSeg.getProdAltNum())) {
						return response;
					}
				}
			}
		}
		return null;
	}

}
