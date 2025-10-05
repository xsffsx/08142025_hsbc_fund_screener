
package com.hhhh.group.secwealth.mktdata.fund.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.exception.ApplicationException;
import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.*;
import com.hhhh.group.secwealth.mktdata.common.util.*;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMstarService;
import com.hhhh.group.secwealth.mktdata.fund.beans.helper.AdvanceChartHelp;
import com.hhhh.group.secwealth.mktdata.fund.beans.helper.ChartProduct;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.ResponseData;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.AdvanceChartNavData;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.HistoryDetail;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.Security;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.growth.AdvanceChartGrowthData;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.growth.CumulativeReturnSeries;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.AdvanceChartRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.AdvanceChartV2Response;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.advanceChart.DataV2;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.advanceChart.ResultV2;
import com.hhhh.group.secwealth.mktdata.fund.dao.AdvanceChartDao;
import com.hhhh.group.secwealth.mktdata.fund.util.EhcacheUtil;
import com.hhhh.group.secwealth.mktdata.fund.util.ThreadPoolExecutorUtil;
import net.sf.json.JSONArray;
import org.apache.commons.collections.MapUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@Service("advanceChartV2Service")
public class AdvanceChartV2ServiceImpl extends AbstractMstarService {


	private final static String STARTDATE = "startDate";


	private final static String ENDDATE = "endDate";

	private final static String IDTYPE = "idType";

	private final static String CURRENCYID = "currencyId";

	private final static String FORWARDFILL = "forwardfill";

	private final static String IDTYPEVALUE = "Morningstar";

	private final static String ID = "id";

	private final static String FREQUENCY = "frequency";

	private final static int INDEX_0 = 0;
	private final static int INDEX_1 = 1;

	private final static int INDEX_2 = 2;

	private final static int INDEX_4 = 4;

	private final static String DATATYPE_NAVPRICE = "navPrice";

	private final static String DATATYPE_CUMULATIVERETURN = "cumulativeReturn";

	private final static String DATATYPE_INDEXPRICE = "indexPrice";

	private final static String DATATYPE_INDEXRETURN = "indexReturn";

	private final static String ADVANCECHART_CACHE = "advanceChartCache";

	private final static String ADVANCECHART_CORE_CACHE = "advanceChartCoreCache";

	private final static String CALLGROWTHANDNAVPRICE = "callGrowthAndNavPrice";

	private final static String CALLGROWTH = "callGrowth";

	private final static String CALLNAVPRICE = "callNavPrice";

	@Autowired
	@Qualifier("internalProductKeyUtil")
	private InternalProductKeyUtil internalProductKeyUtil;

	@Autowired
	@Qualifier("localeMappingUtil")
	private LocaleMappingUtil localeMappingUtil;

	@Autowired
	@Qualifier("advanceChartDao")
	private AdvanceChartDao advanceChartDao;

	@Value("#{systemConfig['mstar.conn.url.advanceChartNav']}")
	private String navUrl;

	@Value("#{systemConfig['mstar.conn.url.advanceChartGrowth']}")
	private String growthUrl;

	@Value("#{systemConfig['mstar.conn.url.advanceChartToken']}")
	private String mstarToken;

	@Value("#{systemConfig['advanceChart.navDataClass']}")
	private String navDataClass;

	@Value("#{systemConfig['advanceChart.growthDataClass']}")
	private String growthDataClass;

	@Value("#{systemConfig['advanceChart.auto.cache']}")
	private String autoCache;

	public EhcacheUtil ehcacheUtil = EhcacheUtil.getInstance("/spring/common/applicationContext-common-ehcache.xml");

	private JAXBContext navDataClassJC;

	private JAXBContext growthDataClassJC;
	@PostConstruct
	public void init() throws ClassNotFoundException, JAXBException {
		this.navDataClassJC = JAXBContext.newInstance(Class.forName(this.navDataClass));
		this.growthDataClassJC = JAXBContext.newInstance(Class.forName(this.growthDataClass));
	}


	public Object execute(final Object object) throws Exception {
		LogUtil.debug(AdvanceChartV2ServiceImpl.class, "Enter into the AdvanceChartServiceImpl");
		AdvanceChartRequest request = (AdvanceChartRequest) object;

		long requestKey = new SimpleKey(request).hashCode();
		AdvanceChartV2Response responses = getAdvanceChartFromCache(request, String.valueOf(requestKey));

		if (responses == null) {
			AdvanceChartV2Response response = sendAdvanceChartRequest(request, String.valueOf(requestKey));
			return response;
		}
		return responses;
	}


	public AdvanceChartV2Response getAdvanceChartFromCache(final AdvanceChartRequest request, final String requestKey)
			throws ApplicationException, IOException {
		AdvanceChartV2Response res = null;
		boolean autoCacheEnable = Boolean.FALSE;
		if (StringUtil.isValid(autoCache)) {
			autoCacheEnable = Boolean.valueOf(autoCache);
		}

		if (autoCacheEnable) {
			LogUtil.info(AdvanceChartV2ServiceImpl.class,
					"Simple Key == " + requestKey + " String == " + new ObjectMapper().writeValueAsString(request));
			String coreCacheString = (String) ehcacheUtil.get(ADVANCECHART_CORE_CACHE, String.valueOf(requestKey));
			if (StringUtil.isValid(coreCacheString)) {
				res = new ObjectMapper().readValue(coreCacheString, AdvanceChartV2Response.class);
				return res;
			} else {
				String cacheString = (String) ehcacheUtil.get(ADVANCECHART_CACHE, String.valueOf(requestKey));
				if (StringUtil.isValid(cacheString)) {
					res = new ObjectMapper().readValue(cacheString, AdvanceChartV2Response.class);
					return res;
				}
			}
		}
		return res;
	}


	public boolean isIndexCode(final AdvanceChartRequest request) {
		List<String> dataTypeList = new ArrayList<>(Arrays.asList(request.getDataType()));
		if ("M".equals(request.getProductKeys().get(0).getProdCdeAltClassCde())
				&& (dataTypeList.contains(DATATYPE_INDEXPRICE) || dataTypeList.contains(DATATYPE_INDEXRETURN))) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("uncheck")
	protected AdvanceChartV2Response sendAdvanceChartRequest(final AdvanceChartRequest request, final String requestKey)
			throws Exception {
		Map<String, ResultV2> resultMap = new HashMap<>();
		Map<String, ChartProduct> currencyMap = new HashMap<>();
		String locale = request.getLocale();
		int index = this.localeMappingUtil
				.getNameByIndex(request.getCountryCode() + CommonConstants.SYMBOL_DOT + locale);
		List<ProductKey> productKeys = request.getProductKeys();
		AdvanceChartV2Response advanceChartV2Response = new AdvanceChartV2Response();
		advanceChartV2Response.setResultV2(new ArrayList<ResultV2>());
		String mastrIdString = "";
		if (ListUtil.isValid(productKeys)) {
			boolean indexCode = isIndexCode(request);
			List<UtProdInstm> utProdInstms = this.advanceChartDao.getUtProdInstmList(productKeys, request.getHeaders());
			for (UtProdInstm utProdInstm : utProdInstms) {
				String mastrId = "";
				if (indexCode) {
					mastrId = utProdInstm.getUndlIndexCde();
				} else {
					mastrId = utProdInstm.getmStarID();
				}
				mastrIdString = new StringBuilder(mastrIdString).append(mastrId)
						.append(CommonConstants.SYMBOL_VERTICAL).toString();
				ResultV2 result = new ResultV2();
				result.setCurrency(utProdInstm.getClosingPrcCcy());
				result.setFrequency(request.getFrequency());
				resultMap.put(utProdInstm.getSymbol().concat("_").concat(mastrId), result);
				ChartProduct chartPro = new ChartProduct();
				chartPro.setCurrency(utProdInstm.getMstarCcyCde());
				chartPro.setEndDate(DateUtil.getSimpleDateFormat(utProdInstm.getAsOfDate(),
						DateConstants.DateFormat_yyyyMMdd_withHyphen));
				currencyMap.put(mastrId, chartPro);
				setProductNameByIndex(indexCode, utProdInstm, result, index);
				SearchProduct searchProduct = this.internalProductKeyUtil.getProductBySearchWithAltClassCde(
						"M", request.getCountryCode(), request.getGroupMember(), utProdInstm.getSymbol(),
						utProdInstm.getMarket(), utProdInstm.getProductType(), request.getLocale());
				if (null != searchProduct && null != searchProduct.getSearchObject()) {
					List<ProdAltNumSeg> prodAltNumSegs = searchProduct.getSearchObject().getProdAltNumSeg();
					result.setProdAltNumSegs(prodAltNumSegs);
				} else {
					LogUtil.error(AdvanceChartV2ServiceImpl.class, "No record found for symbol="
							+ utProdInstm.getSymbol() + " market=" + utProdInstm.getMarket());
				}
			}
		} else {
			LogUtil.error(AdvanceChartV2ServiceImpl.class,
					"No record found in DB table v_ut_prod_instm,prodkeys ="
							+ (JSONArray.fromObject(request.getProductKeys())).toString());
		}

		if (mastrIdString.length() > 0) {
			mastrIdString = mastrIdString.substring(0, mastrIdString.length() - 1);
			getMastarInfo(request, advanceChartV2Response, mastrIdString, resultMap, currencyMap);
		} else {
			LogUtil.error(AdvanceChartV2ServiceImpl.class,
					"the productKey exist issue = " + (JSONArray.fromObject(productKeys)).toString());
		}
		return advanceChartV2Response;
	}

	public void setProductNameByIndex(boolean indexCode, UtProdInstm utProdInstm, ResultV2 result, int index) {
		if (indexCode) {
			 if (1 == index) {
				result.setIndexName(utProdInstm.getUndlIndexPllName());
			} else if (2 == index) {
				result.setIndexName(utProdInstm.getUndlIndexSllName());
			} else {
				result.setIndexName(utProdInstm.getUndlIndexName());
			}
		} else {
			 if (1 == index) {
				result.setProductName(utProdInstm.getProdPllName());
			} else if (2 == index) {
				result.setProductName(utProdInstm.getProdSllName());
			} else {
				result.setProductName(utProdInstm.getProdName());
			}
		}
	}

	private void getMastarInfo(final AdvanceChartRequest request, final AdvanceChartV2Response response,
			final String mastrIdString, final Map<String, ResultV2> resultMap,
			final Map<String, ChartProduct> currencyMap) throws Exception {
		String currency = "";
		for (Entry<String, ChartProduct> map : currencyMap.entrySet()) {
			currency = map.getValue().getCurrency();
		}
		String mastrIdArray[] = mastrIdString.split(CommonConstants.SYMBOL_SEPARATOR);
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair(AdvanceChartV2ServiceImpl.STARTDATE, request.getStartDate()));
		params.add(new BasicNameValuePair(AdvanceChartV2ServiceImpl.ENDDATE, request.getEndDate()));
		params.add(new BasicNameValuePair(AdvanceChartV2ServiceImpl.CURRENCYID,
				StringUtil.isValid(currency) ? currency : request.getCurrency()));
		params.add(new BasicNameValuePair(AdvanceChartV2ServiceImpl.IDTYPE, AdvanceChartV2ServiceImpl.IDTYPEVALUE));
		params.add(new BasicNameValuePair(AdvanceChartV2ServiceImpl.ID, mastrIdArray[0]));
		params.add(new BasicNameValuePair(AdvanceChartV2ServiceImpl.FREQUENCY, request.getFrequency()));
		if (request.getNavForwardFill()) {
			params.add(new BasicNameValuePair(AdvanceChartV2ServiceImpl.FORWARDFILL, "true"));
		} else {
			params.add(new BasicNameValuePair(AdvanceChartV2ServiceImpl.FORWARDFILL, "false"));
		}
		String reqNavUrl = new StringBuilder(this.navUrl).append(CommonConstants.SYMBOL_SLASH).append(this.mstarToken)
				.toString();
		String reqGrowthUrl = new StringBuilder(this.growthUrl).append(CommonConstants.SYMBOL_SLASH)
				.append(this.mstarToken).toString();

		List<AdvanceChartHelp> advanceChartHelps = new ArrayList<>();
		List<AdvanceChartHelp> advanceChartHelpsNav = new ArrayList<>();
		List<AdvanceChartHelp> advanceChartHelpsGrowth = new ArrayList<>();
			for (String str : mastrIdArray) {
				 collectAdvanceChartHelpInfo(request, advanceChartHelpsGrowth, str, this.growthDataClassJC, params,
						this.growthUrl, reqGrowthUrl, currencyMap);
				 collectAdvanceChartHelpInfo(request, advanceChartHelpsNav, str, this.navDataClassJC, params, this.navUrl, reqNavUrl,
						currencyMap);
			}

		Map<String, Boolean> dataTypeMap = checkDataType(request);
		List<ResponseData> responseDataList = null;
		AdvanceChartNavData advanceChartNavData = new AdvanceChartNavData();
		AdvanceChartGrowthData advanceChartGrowthData = new AdvanceChartGrowthData();
		if (dataTypeMap.get(CALLGROWTHANDNAVPRICE)) {
			advanceChartHelps.addAll(advanceChartHelpsNav);
			advanceChartHelps.addAll(advanceChartHelpsGrowth);
			responseDataList = MultithreadCallMstarApi(advanceChartHelps);
		} else if (dataTypeMap.get(CALLNAVPRICE)) {
			responseDataList = MultithreadCallMstarApi(advanceChartHelpsNav);
		} else if (dataTypeMap.get(CALLGROWTH)) {
			responseDataList = MultithreadCallMstarApi(advanceChartHelpsGrowth);
		}
		collectAdvanceChartData(responseDataList,advanceChartNavData,advanceChartGrowthData);
		Map<String, List<DataV2>> dataMap = mergeMstarResponse(advanceChartNavData, advanceChartGrowthData);
		mergeResponse(dataMap, response, request, resultMap);
	}


	private void collectAdvanceChartData(List<ResponseData> responseDataList, AdvanceChartNavData advanceChartNavData, AdvanceChartGrowthData advanceChartGrowthData) {
		if (ListUtil.isValid(responseDataList)) {
			List<com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.growth.Security> securitys = new ArrayList<>();
			List<com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.Security> navDataSecuritys = new ArrayList<>();
			for (ResponseData responseData : responseDataList) {
				if (responseData instanceof AdvanceChartGrowthData) {
					AdvanceChartGrowthData futureResponse = (AdvanceChartGrowthData) responseData;
					if (ListUtil.isValid(futureResponse.getSecurity())) {
						securitys.addAll(futureResponse.getSecurity());
					}
				} else if (responseData instanceof AdvanceChartNavData) {
					AdvanceChartNavData futureResponse = (AdvanceChartNavData) responseData;
					if ( ListUtil.isValid(futureResponse.getSecurity())) {
						navDataSecuritys.addAll(futureResponse.getSecurity());
					}
				}
			}
			advanceChartGrowthData.setSecurity(securitys);
			advanceChartNavData.setSecurity(navDataSecuritys);
		}
	}




	private List<AdvanceChartHelp> collectAdvanceChartHelpInfo(final AdvanceChartRequest reqeust,
			final List<AdvanceChartHelp> advanceChartHelps, final String id, final JAXBContext jaxbContext,
			final List<NameValuePair> params, final String mstarUrl, final String reqUrl,
			final Map<String, ChartProduct> currencyMap) throws ApplicationException, ParseException {
		AdvanceChartHelp advanceChartHelp = new AdvanceChartHelp();
		if (null != currencyMap && currencyMap.containsKey(id) && StringUtil.isValid(currencyMap.get(id).getCurrency())) {
			advanceChartHelp.setCurrency(currencyMap.get(id).getCurrency());
			String startDate = calculateTradeTime(reqeust, currencyMap.get(id).getEndDate());
			if(reqeust.getStartDate() != null && reqeust.getEndDate() !=null){
				advanceChartHelp.setStartDate(reqeust.getStartDate());
				advanceChartHelp.setEndDate(reqeust.getEndDate());
			}else{
				advanceChartHelp.setStartDate(startDate);
				advanceChartHelp.setEndDate(currencyMap.get(id).getEndDate());
			}
		}
		advanceChartHelp.setId(id);
		advanceChartHelp.setjAXBContext(jaxbContext);
		advanceChartHelp.setParams(params);
		advanceChartHelp.setMstarUrl(mstarUrl);
		advanceChartHelp.setReqUrl(reqUrl);
		advanceChartHelps.add(advanceChartHelp);
		return advanceChartHelps;
	}


	private Map<String, Boolean> checkDataType(final AdvanceChartRequest request) {
		Map<String, Boolean> dataTypeMap = new HashMap<>();
		dataTypeMap.put(CALLGROWTHANDNAVPRICE, Boolean.FALSE);
		dataTypeMap.put(CALLGROWTH, Boolean.FALSE);
		dataTypeMap.put(CALLNAVPRICE, Boolean.FALSE);
		String dataType[] = request.getDataType();
		if (null != dataType) {
			Set<String> dataTypelist = new HashSet<>(Arrays.asList(dataType));
			if (dataTypelist.contains(AdvanceChartV2ServiceImpl.DATATYPE_NAVPRICE)
					&& dataTypelist.contains(AdvanceChartV2ServiceImpl.DATATYPE_CUMULATIVERETURN)) {
				dataTypeMap.put(CALLGROWTHANDNAVPRICE, Boolean.TRUE);
			} else if (dataTypelist.contains(AdvanceChartV2ServiceImpl.DATATYPE_INDEXPRICE)
					&& dataTypelist.contains(AdvanceChartV2ServiceImpl.DATATYPE_INDEXRETURN)) {
				dataTypeMap.put(CALLGROWTH, Boolean.TRUE);
			} else if (dataTypelist.contains(AdvanceChartV2ServiceImpl.DATATYPE_NAVPRICE)
					|| dataTypelist.contains(AdvanceChartV2ServiceImpl.DATATYPE_INDEXPRICE)) {
				dataTypeMap.put(CALLNAVPRICE, Boolean.TRUE);
			} else if (dataTypelist.contains(AdvanceChartV2ServiceImpl.DATATYPE_CUMULATIVERETURN)
					|| dataTypelist.contains(AdvanceChartV2ServiceImpl.DATATYPE_INDEXRETURN)) {
				dataTypeMap.put(CALLGROWTH, Boolean.TRUE);
			} else {
				LogUtil.error(AdvanceChartV2ServiceImpl.class, ErrTypeConstants.SOME_INPUT_PARAMETER_INVALID);
				throw new CommonException(ErrTypeConstants.SOME_INPUT_PARAMETER_INVALID);
			}
		}
		return dataTypeMap;
	}


	private List<ResultV2> orderResult(AdvanceChartRequest request, List<ResultV2> results) {
		List<ProductKey> productKeys = request.getProductKeys();
		List<ResultV2> resultsByOrder = new ArrayList<>();
		for (ProductKey productKey : productKeys) {
			String mCode = productKey.getProdAltNum();
			for (ResultV2 resultV2 : results) {
				List<ProdAltNumSeg> prodAltNumSegs = resultV2.getProdAltNumSegs();
				for (ProdAltNumSeg prodAltNumSeg : prodAltNumSegs) {
					if (prodAltNumSeg.getProdCdeAltClassCde().equals("M") && mCode.equals(prodAltNumSeg.getProdAltNum())) {
						resultsByOrder.add(resultV2);
						break;
					}
				}
			}
		}
		return resultsByOrder;
	}


	@SuppressWarnings({"java:S3776"})
	private void mergeResponse(final Map<String, List<DataV2>> dataMap, final AdvanceChartV2Response response,
			final AdvanceChartRequest request, final Map<String, ResultV2> resultMap) {
		List<ResultV2> results = new ArrayList<>();
	    for (Map.Entry<String, ResultV2> entryMap : resultMap.entrySet()) {
            if (MapUtils.isNotEmpty(dataMap)) {
                for (Map.Entry<String, List<DataV2>> entry : dataMap.entrySet()) {
                    String symbolandId = entryMap.getKey();
                    if(symbolandId.contains("_") &&symbolandId.split("_").length>1){
                        String id = symbolandId.split("_")[1];
                        String newId = entry.getKey();
                        if (newId.equals(id)) {
                            ResultV2 res = entryMap.getValue();
                            res.setData(entry.getValue());
                            results.add(res);
                        }
                    }
                }
            }
        }

		List<ResultV2> resultsByOrder = orderResult(request,results);
		response.setResultV2(resultsByOrder);
	}


	private Map<String, List<DataV2>> mergeMstarResponse(final AdvanceChartNavData advanceChartNavData,
			final AdvanceChartGrowthData advanceChartGrowthData) {
		Map<String, List<DataV2>> dataMap = new HashMap<>();
		if (null != advanceChartGrowthData && null != advanceChartGrowthData.getSecurity()) {
				List<com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.growth.Security> securityList = advanceChartGrowthData
						.getSecurity();
				for (com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.growth.Security securityGrowth : securityList) {
					String growthId = securityGrowth.getId();
					List<DataV2> dataList = new ArrayList<>();
					for (CumulativeReturnSeries cumulativeReturnSeries : securityGrowth.getCumulativeReturnSeries()) {
						for (com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.growth.HistoryDetail growthHistoryDetail : cumulativeReturnSeries
								.getHistoryDetail()) {
							DataV2 data = new DataV2();
							data.setDate(growthHistoryDetail.getEndDate());
							data.setCumulativeReturn(new BigDecimal(growthHistoryDetail.getValue()));
							dataList.add(data);
						}
					}
					dataMap.put(growthId, dataList);
				}
		}
		collectNavData(advanceChartNavData,dataMap);
		return dataMap;
	}

	private void collectDataMap(Map<String, List<DataV2>> dataMap,Security security,List<DataV2> merageData){
		if (dataMap.containsKey(security.getId())) {
			List<DataV2> growthData = dataMap.get(security.getId());
			for (int i = 0, length = security.getHistoryDetail().size(); i < length; i++) {
				HistoryDetail historyDetail = security.getHistoryDetail().get(i);
				for (DataV2 data : growthData) {
					if (data.getDate().equals(historyDetail.getEndDate())) {
						data.setNavPriceText(historyDetail.getValue());
						break;
					}
				}
			}
			dataMap.put(security.getId(), growthData);
		} else if (ListUtil.isValid(security.getHistoryDetail())) {
			for (int i = 0, length = security.getHistoryDetail().size(); i < length; i++) {
				HistoryDetail historyDetail = security.getHistoryDetail().get(i);
				DataV2 data = new DataV2();
				data.setDate(historyDetail.getEndDate());
				data.setNavPriceText(historyDetail.getValue());
				merageData.add(data);
			}
			dataMap.put(security.getId(), merageData);
		}
	}

	private void collectNavData(AdvanceChartNavData advanceChartNavData,Map<String, List<DataV2>> dataMap){
		if (null != advanceChartNavData && advanceChartNavData.getSecurity() != null) {
			List<Security> securityList = advanceChartNavData.getSecurity();
			for (Security security : securityList) {
				List<DataV2> merageData = new ArrayList<>();
				collectDataMap(dataMap,security, merageData);
			}
		}
	}


	private ResponseData callMstarRelationApi(final String url, final String baseUrl, final String reqParams,
			final JAXBContext jaxbContext, final String paramsWithoutEncode) {
		ResponseData responseData = null;
		LogUtil.infoOutboundMsg(AdvanceChartV2ServiceImpl.class, VendorType.MSTAR, baseUrl, reqParams);
		try {
			String resp = this.connManager.sendRequest(url, reqParams,paramsWithoutEncode);
			LogUtil.infoInboundMsg(AdvanceChartV2ServiceImpl.class, VendorType.MSTAR, resp);
			responseData = (ResponseData) jaxbContext.createUnmarshaller().unmarshal(new StringReader(resp));
		} catch (JAXBException e) {
			LogUtil.error(ReturnIndexChartServiceImpl.class, "Mstar MstarUnmarshalFail", e);
			throw new CommonException(MstarExceptionConstants.UNMARSHAL_FAIL, e.getMessage());
		} catch (Exception e) {
			String message = e.getMessage();
			LogUtil.info(AdvanceChartV2ServiceImpl.class, "message info = ", message);
			if (null != message && message.toLowerCase().contains("timeout")) {
				LogUtil.error(AdvanceChartV2ServiceImpl.class, "Mstar timeout error", e);
				throw new CommonException(MstarExceptionConstants.TIMEOUT, e.getMessage());
			} else if (null != message && message.toLowerCase().contains("nodata")) {
				LogUtil.error(AdvanceChartV2ServiceImpl.class, "Mstar no data error", e);
				throw new CommonException(MstarExceptionConstants.NO_DATA, e.getMessage());
			} else {
				LogUtil.error(AdvanceChartV2ServiceImpl.class, "Mstar undefined error", e);
				throw new CommonException(MstarExceptionConstants.UNDEFINED, e.getMessage());
			}
		}
		if (null == responseData) {
			LogUtil.error(AdvanceChartV2ServiceImpl.class, "Mstar response is null");
			throw new CommonException(MstarExceptionConstants.UNDEFINED, "response is null");
		}
		return responseData;

	}


	private List<ResponseData> MultithreadCallMstarApi(final List<AdvanceChartHelp> advanceChartHelps)
			throws InterruptedException, ExecutionException {
		List<Future<ResponseData>> futureList = new ArrayList<>();
		List<ResponseData> list = new ArrayList<>();

		ThreadPoolExecutor pool = ThreadPoolExecutorUtil.getInstance();

		for (AdvanceChartHelp advanceChartHelp : advanceChartHelps) {

			Future<ResponseData> future = pool.submit(new Callable<ResponseData>() {
				private AdvanceChartHelp advanceChartHelp;

				public Callable<ResponseData> accept(final AdvanceChartHelp advanceChartHelp) {
					this.advanceChartHelp = advanceChartHelp;
					return this;
				}

				@Override
				public ResponseData call() throws ExecutionException {
					List<NameValuePair> paramss = new ArrayList<>();
					String id = advanceChartHelp.getId();
					String reqUrl = advanceChartHelp.getReqUrl();
					String mstarUrl = advanceChartHelp.getMstarUrl();
					String startDate = advanceChartHelp.getStartDate();
					String endDate = advanceChartHelp.getEndDate();
					String currency = advanceChartHelp.getCurrency();
					JAXBContext jaxbContext = advanceChartHelp.getjAXBContext();
					paramss.addAll(advanceChartHelp.getParams());
					paramss.set(AdvanceChartV2ServiceImpl.INDEX_0,
							new BasicNameValuePair(AdvanceChartV2ServiceImpl.STARTDATE, startDate));
					paramss.set(AdvanceChartV2ServiceImpl.INDEX_1,
							new BasicNameValuePair(AdvanceChartV2ServiceImpl.ENDDATE, endDate));
					paramss.set(AdvanceChartV2ServiceImpl.INDEX_2,
							new BasicNameValuePair(AdvanceChartV2ServiceImpl.CURRENCYID, currency));
					paramss.set(AdvanceChartV2ServiceImpl.INDEX_4,
							new BasicNameValuePair(AdvanceChartV2ServiceImpl.ID, id));
					String reqParams = URLEncodedUtils.format(paramss, CommonConstants.CODING_UTF8);
					return callMstarRelationApi(reqUrl, mstarUrl, reqParams, jaxbContext, AdaptorUtil.convertNameValuePairListToString(paramss));
				}
			}.accept(advanceChartHelp));
			futureList.add(future);
		}

		for (Future<ResponseData> future : futureList) {
			list.add(future.get());
		}

		return list;
	}


	private String calculateTradeTime(final AdvanceChartRequest request, final String calEndDate) throws ApplicationException, ParseException {

		String period = request.getPeriod();
		// Set the date depends on the period
		String startdate = request.getStartDate();
		String enddate = request.getEndDate();
		String timeZone = request.getTimeZone();
		SimpleDateFormat df = new SimpleDateFormat(DateConstants.DateFormat_yyyyMMdd_withHyphen);
		Calendar cal = Calendar.getInstance();
		String oldFormatPattern = DateConstants.DateFormat_yyyyMMdd_withHyphen;
		String newFormatPattern = DateConstants.DateFormat_yyyyMMdd_withHyphen;
		TimeZone oldTimezone = TimeZone.getDefault();
		TimeZone newTimezone = TimeZone.getTimeZone(timeZone);
		if (StringUtil.isValid(startdate) && StringUtil.isValid(enddate)) {
			startdate = request.getStartDate();
			enddate = request.getEndDate();
		} else if (StringUtil.isValid(period) && StringUtil.isValid(timeZone)) {
			if (StringUtil.isValid(calEndDate)) {
				enddate = calEndDate;
				cal.setTime(df.parse(calEndDate));
				Calendar cale = returnTimeByPeriod(period, cal);
				String dateStr = df.format(cale.getTime());
				startdate = DateUtil.dateByTimeZone(dateStr, oldFormatPattern, newFormatPattern, oldTimezone,
						newTimezone);
			}
		} else {
			cal.add(Calendar.YEAR, -3);
			String dateStr = df.format(cal.getTime());
			enddate = df.format(cal.getTime());
			startdate = DateUtil.dateByTimeZone(dateStr, oldFormatPattern, newFormatPattern, oldTimezone, newTimezone);
		}
		LogUtil.info(AdvanceChartV2ServiceImpl.class, "startdate: " + startdate + ", enddate" + enddate);
		return startdate;
	}


	public Calendar returnTimeByPeriod(String period, Calendar cal) {
		if (period.equals("YTD")) {
			cal.set(cal.get(Calendar.YEAR), 0, 1);
		} else if (period.equals("MAX")) {
			cal.set(1900, 0, 1);
		} else {
			String dateType = period.substring(period.length() - 1).toUpperCase();
			int number = Integer.parseInt(period.substring(0, period.length() - 1));
			if (CommonConstants.YEAR_PERIOD.equals(dateType)) {
				cal.add(Calendar.YEAR, -number);
			} else if (CommonConstants.MONTH_PERIOD.equals(dateType)) {
				cal.add(Calendar.MONTH, -number);
			} else {
				LogUtil.error(AdvanceChartV2ServiceImpl.class, "period is invalid, period: " + period);
				throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
			}
		}
		return cal;
	}
}
