package com.hhhh.group.secwealth.mktdata.api.equity.chart.service;

import com.google.gson.JsonObject;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.constant.ChartConstant;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.request.ChartRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.ChartResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.etnet.EtnetChartResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.etnet.EtnetResult;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.pre.Result;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.EtnetProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Service
@ConditionalOnProperty(value = "service.chart.Etnet.injectEnabled")
public class EtnetChartService extends AbstractBaseService<ChartRequest, ChartResponse, CommonRequestHeader> {

	private static final Logger logger = LoggerFactory.getLogger(EtnetChartService.class);

	@Autowired
	private HttpClientHelper httpClientHelper;

	@Autowired
	private EtnetProperties etnetProperties;

	// @Autowired
	// private OatProperties oatProperties;

	/**
	 * Value Description 0 1D - Intra-day (5 mins) 2 5D - 5 days (15 mins) 5 1M - 1
	 * Month (daily) 7 3M - 3 Months (daily) 8 6M - 6 Months (daily) 11 1Y - 1 Year
	 * (daily) 13 3Y - 3 Years (daily)
	 */
	public static final Map<Number, String> PERIODS_MAPPING = new HashMap<Number, String>() {
		/**
		 *
		 */
		private static final long serialVersionUID = -8276680146926412425L;

		{
			put(0, "1D");
			put(2, "5D");
			put(5, "1M");
			put(7, "3M");
			put(8, "6M");
			put(11, "1Y");
			put(13, "3Y");
		}
	};

	@Override
	protected Object convertRequest(final ChartRequest request, final CommonRequestHeader header) throws Exception {

		String timeInterval = "";
		// String realTime = "";
		/**
		 * put request parameters into thread local
		 */
		Number period = request.getPeriod();
		String market = request.getMarket();
		String productType = request.getProductType();
		String[] symbols = request.getSymbol();
		Number intCnt = request.getIntCnt();
		String intType = request.getIntType();

		if (symbols == null || symbols.length <= 0) {
			EtnetChartService.logger.error("Invalid request: {}", request);
			throw new VendorException(ExCodeConstant.EX_CODE_REQUEST_PARAMETER_ERROR);
		}

		if (intType.equals(ChartConstant.MINUTE)) {
			timeInterval = intCnt + ChartConstant.SYMBOL_M;
		} else if (ChartConstant.DAILY.equals(intType)) {
			timeInterval = intType;
		} else {
			EtnetChartService.logger.error("Invalid request: {}", request);
			throw new VendorException(ExCodeConstant.EX_CODE_REQUEST_PARAMETER_ERROR);
		}
		String locale = header.getLocale();
		// TODO try to better
		if (ChartConstant.Locale_ZH_CN.equals(locale)) {
			locale = ChartConstant.Locale_ZH;
		}

		String token = etnetProperties.getEtnetTokenWithoutVerify();
		ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_MARKET, market);
		ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_PRODUCTTYPE, productType);

		Map<String, List<NameValuePair>> requestMap = new HashMap<String, List<NameValuePair>>();
		for (String symbol : symbols) {
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("symbol", symbol));
			params.add(new BasicNameValuePair("realTime", ChartConstant.FLAG_Y));
			params.add(new BasicNameValuePair("timePeriod", PERIODS_MAPPING.get(period)));
			params.add(new BasicNameValuePair("timeInterval", timeInterval));
			params.add(new BasicNameValuePair("indicators", ChartConstant.INDICATORS));
			params.add(new BasicNameValuePair("locale", locale));
			params.add(new BasicNameValuePair("token", token));
			requestMap.put(symbol, params);
		}
		return requestMap;
	}

	/**
	 * Get data from TRIS by HTTP call Put result into map<symbol, jsonResult>
	 */
	@Override
	protected Object execute(final Object serviceRequest) throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			@SuppressWarnings("unchecked")
			Map<String, List<NameValuePair>> requestMap = (Map<String, List<NameValuePair>>) serviceRequest;
			for (Entry<String, List<NameValuePair>> map : requestMap.entrySet()) {
				String result;
				// if (oatProperties.getEtnetFlag()) {
				// result = "{\"result\":{\"name\":\"CKH
				// HOLDINGS\",\"fields\":[\"DATE\",\"OPEN\",\"HIGH\",\"LOW\",\"CLOSE\",\"VOLUME\",\"SMA10\",\"SMA20\",\"SMA50\"],\"data\":[[\"2019-09-20T16:05:00.000+0800\",69.55,70.0,69.2,69.85,3039923,71.375,69.7775,70.593],[\"2019-09-19T16:09:00.000+0800\",70.55,71.15,69.15,69.45,4803689,71.435,69.774,70.7026],[\"2019-09-18T16:09:00.000+0800\",71.4,71.6,70.6,70.6,2804604,71.39,69.8055,70.8212],[\"2019-09-17T16:09:00.000+0800\",71.05,71.55,70.5,71.0,5913586,71.22,69.797,70.9078],[\"2019-09-16T16:08:00.000+0800\",72.9,72.9,71.4,71.5,4890368,70.78,69.7085,70.9884],[\"2019-09-13T16:10:00.000+0800\",72.0,72.95,71.6,72.75,3456002,70.325,69.5875,71.077],[\"2019-09-12T16:09:00.000+0800\",73.25,73.25,72.2,72.6,6822664,69.793,69.3015,71.1566],[\"2019-09-11T16:09:00.000+0800\",72.0,73.3,71.75,73.15,8175246,69.326,68.9555,71.2382],[\"2019-09-10T16:08:00.000+0800\",72.0,72.0,71.45,71.65,8148366,68.779,68.5095,71.3118],[\"2019-09-09T16:10:00.000+0800\",70.05,71.85,70.05,71.2,6139024,68.457,68.181,71.4034],[\"2019-09-06T16:09:00.000+0800\",70.75,71.2,70.1,70.45,11472897,68.18,67.9625,71.502],[\"2019-09-05T16:09:00.000+0800\",68.8,69.75,68.55,69.0,6570966,68.113,67.794,71.6396],[\"2019-09-04T16:09:00.000+0800\",66.9,69.65,66.9,68.9,8212577,68.221,67.703,71.7852],[\"2019-09-03T16:09:00.000+0800\",66.4,66.9,65.8,66.6,3905632,68.374,67.5995,71.9178],[\"2019-09-02T16:09:00.000+0800\",67.5,67.55,66.1,66.95,6547427,68.637,67.656,72.1024],[\"2019-08-30T16:08:00.000+0800\",68.73,68.73,67.13,67.43,7731576,68.85,67.7475,72.297],[\"2019-08-29T16:08:00.000+0800\",68.18,68.48,67.48,67.93,7902900,68.81,67.93,72.49],[\"2019-08-28T16:09:00.000+0800\",68.18,68.43,67.68,67.68,8973397,68.585,68.115,72.654],[\"2019-08-27T16:09:00.000+0800\",69.63,69.63,67.78,68.43,6286907,68.24,68.37,72.79],[\"2019-08-26T16:09:00.000+0800\",68.53,68.58,67.58,68.43,8054636,67.905,68.64,72.911],[\"2019-08-23T16:09:00.000+0800\",69.58,70.58,69.53,69.78,5449506,67.745,68.935,73.027],[\"2019-08-22T16:10:00.000+0800\",70.63,70.63,69.53,70.08,5299641,67.475,69.1825,73.124],[\"2019-08-21T16:09:00.000+0800\",69.13,70.78,69.08,70.43,7045145,67.185,69.4275,73.222],[\"2019-08-20T16:09:00.000+0800\",69.73,69.93,69.03,69.23,7844342,66.825,69.6675,73.332]]}}";
				// } else {
				result = this.httpClientHelper.doGet(etnetProperties.getProxyName(), etnetProperties.getChartDataUrl(),
						map.getValue(), null);
				JsonObject respJsonObj = JsonUtil.getAsJsonObject(result);
				String errCode = JsonUtil.getAsString(respJsonObj, "err_code");
				if (StringUtil.isValid(errCode) && EtnetProperties.EX_CODE_ETNET_INVALID_TOKEN_CODE.equals(errCode)) {
					List<NameValuePair> params = map.getValue();
					int index = 0;
					for (int i = 0; i < params.size(); i++) {
						if ("token".equals(params.get(i).getName())) {
							index = i;
						}
					}
					params.remove(index);
					params.add(new BasicNameValuePair("token", etnetProperties.getEtnetToken()));
					result = this.httpClientHelper.doGet(etnetProperties.getProxyName(),
							etnetProperties.getChartDataUrl(), params, null);
				}
				resultMap.put(map.getKey(), result);
				// }
			}
		} catch (Exception e) {
			EtnetChartService.logger.error("Access hang seng encounter error", e);
			throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_ETNET_ERROR, e);
		}
		return resultMap;
	}

	/**
	 * validate json result from tris
	 */
	protected Object validateServiceResponse(final Object serviceResponse) throws Exception {
		if (null == serviceResponse) {
			EtnetChartService.logger.error("Invalid response: {}", serviceResponse);
			throw new VendorException(ExCodeConstant.EX_CODE_ETNET_INVALID_RESPONSE);
		}

		@SuppressWarnings("unchecked")
		Map<String, String> resultMap = (Map<String, String>) serviceResponse;
		if (resultMap.isEmpty()) {
			EtnetChartService.logger.error("Invalid response from Etnet " + resultMap.toString());
			throw new VendorException(ExCodeConstant.EX_CODE_ETNET_INVALID_RESPONSE);
		}

		for (Map.Entry<String, String> map : resultMap.entrySet()) {
			if (StringUtil.isValid(map.getValue())) {
				JsonObject respJsonObj = JsonUtil.getAsJsonObject(map.getValue().toString());
				String errCode = JsonUtil.getAsString(respJsonObj, "err_code");
				if (StringUtil.isValid(errCode)) {
					etnetProperties.throwExceptionByErrorCode(errCode);
				}
			}
		}
		return serviceResponse;
	}

	@Override
	protected ChartResponse convertResponse(final Object validServiceResponse) throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, String> resultMap = (Map<String, String>) validServiceResponse;
		String market = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_MARKET).toString());
		String productType = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_PRODUCTTYPE));
		ChartResponse response = new ChartResponse();
		List<Result> preResultList = new ArrayList<>();

		try {
			for (Map.Entry<String, String> map : resultMap.entrySet()) {
				if (StringUtil.isValid(map.getValue())) {
					String[] symbols = new String[] { map.getKey() };
					EtnetChartResponse svcResponse = JsonUtil.readValue(map.getValue(), EtnetChartResponse.class);
					if (null != svcResponse && null != svcResponse.getResult()) {
						EtnetResult svcResult = svcResponse.getResult();
						Result preResult = new Result();
						preResult.setMarket(market);
						preResult.setProductType(productType);
						preResult.setSymbol(symbols);
						preResult.setData(svcResult.getData());
						preResult.setFields(svcResult.getFields());
						preResult.setDisplayName(svcResult.getName());
						preResultList.add(preResult);
					}
				}
			}
		} catch (Exception e) {
			EtnetChartService.logger.error("Error: Failed to format json string to LabciChartResponse.class", e);
			EtnetChartService.logger.error("Response from Hangseng is : {}", validServiceResponse);
			throw new VendorException(ExCodeConstant.EX_CODE_ETNET_INVALID_RESPONSE);
		}
		response.setResult(preResultList);
		return response;
	}

}
