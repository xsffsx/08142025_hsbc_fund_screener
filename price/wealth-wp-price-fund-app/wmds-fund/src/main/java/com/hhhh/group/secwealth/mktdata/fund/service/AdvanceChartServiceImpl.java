
package com.hhhh.group.secwealth.mktdata.fund.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
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
import com.hhhh.group.secwealth.mktdata.fund.beans.response.AdvanceChartResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.advanceChart.Data;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.advanceChart.Result;
import com.hhhh.group.secwealth.mktdata.fund.dao.AdvanceChartDao;
import com.hhhh.group.secwealth.mktdata.fund.util.EhcacheUtil;
import com.hhhh.group.secwealth.mktdata.fund.util.ThreadPoolExecutorUtil;
import net.sf.json.JSONArray;
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

@Service("advanceChartService")
public class AdvanceChartServiceImpl extends AbstractMstarService {


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

	private final static int FORWARD_WEEK = 2;

	private final static String DATATYPE_NAVPRICE = "navPrice";

	private final static String DATATYPE_CUMULATIVERETURN = "cumulativeReturn";

	private final static String DATATYPE_INDEXPRICE = "indexPrice";

	private final static String DATATYPE_INDEXRETURN = "indexReturn";

	private final static String MONTYLY = "monthly";

	private final static String DAILY = "daily";

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
	public void init() throws Exception {
		this.navDataClassJC = JAXBContext.newInstance(Class.forName(this.navDataClass));
		this.growthDataClassJC = JAXBContext.newInstance(Class.forName(this.growthDataClass));
	}


	public Object execute(final Object object) throws Exception {
		LogUtil.debug(AdvanceChartServiceImpl.class, "Enter into the AdvanceChartServiceImpl");
		AdvanceChartRequest request = (AdvanceChartRequest) object;
		sortArray(request);
		long requestKey = new SimpleKey(request).hashCode();
		AdvanceChartResponse responses = getAdvanceChartFromCache(request, String.valueOf(requestKey));
		
		if (responses == null) {
			AdvanceChartResponse response = sendAdvanceChartRequest(request, String.valueOf(requestKey));
			return response;
		}
		return responses;
	}


	public void sortArray(AdvanceChartRequest resquest) {
		Arrays.sort(resquest.getDataType());
		Collections.sort(resquest.getProductKeys(), new Comparator<ProductKey>() {
			@Override
			public int compare(ProductKey o1, ProductKey o2) {
				if ((o1.getProdAltNum()).compareToIgnoreCase(o2.getProdAltNum()) < 0) {
					return -1;
				}
				return 1;
			}
		});
	}


	public AdvanceChartResponse getAdvanceChartFromCache(final AdvanceChartRequest request, final String requestKey)
			throws Exception {
		AdvanceChartResponse res = null;
		boolean autoCacheEnable = Boolean.FALSE;
		if (StringUtil.isValid(autoCache)) {
			autoCacheEnable = Boolean.valueOf(autoCache);
		}

		if (autoCacheEnable) {
			LogUtil.info(AdvanceChartServiceImpl.class,
					"Simple Key == " + requestKey + " String == " + new ObjectMapper().writeValueAsString(request));
			String coreCacheString = (String) ehcacheUtil.get(ADVANCECHART_CORE_CACHE, String.valueOf(requestKey));
			if (StringUtil.isValid(coreCacheString)) {
				res = new ObjectMapper().readValue(coreCacheString, AdvanceChartResponse.class);
				SetIndexName(request, res);
				return res;
			} else {
				String cacheString = (String) ehcacheUtil.get(ADVANCECHART_CACHE, String.valueOf(requestKey));
				if (StringUtil.isValid(cacheString)) {
					res = new ObjectMapper().readValue(cacheString, AdvanceChartResponse.class);
					SetIndexName(request, res);
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

	public Set<String> validateProductKey(List<ProductKey> productKeys) {
		Set<String> numSet = new HashSet<>();
		for (ProductKey productKey : productKeys) {
			numSet.add(productKey.getProdCdeAltClassCde());
		}
		if (numSet.size() != 1) {
			throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
		}
		return numSet;
	}

	public void SetIndexName(final AdvanceChartRequest request, final AdvanceChartResponse response) throws Exception {
		String locale = request.getLocale();
		int index = this.localeMappingUtil
				.getNameByIndex(request.getCountryCode() + CommonConstants.SYMBOL_DOT + locale);
		List<ProductKey> productKeyMcode = new ArrayList<>();
		List<ProductKey> productKeys = request.getProductKeys();
		Set<String> numSet = validateProductKey(request.getProductKeys());
		boolean indexCode = isIndexCode(request);
		if (numSet.contains("M")) {
			productKeyMcode.addAll(productKeys);
			List<UtProdInstm> utProdInstms = this.advanceChartDao.getUtProdInstmList(productKeyMcode, request.getHeaders());
			List<Result> results = response.getResult();
			for (Result result : results) {
				for (UtProdInstm utProdInstm : utProdInstms) {
					String altNum = "";
					for (ProdAltNumSeg prodAltNumSegs : result.getProdAltNumSegs()) {
						if (utProdInstm.getSymbol().equals(prodAltNumSegs.getProdAltNum())) {
							altNum = prodAltNumSegs.getProdAltNum();
							break;
						}
					}
					if (utProdInstm.getSymbol().equals(altNum)) {
						if (indexCode) {

							if (0 == index) {
								result.setIndexName(utProdInstm.getUndlIndexName());
							} else if (1 == index) {
								result.setIndexName(utProdInstm.getUndlIndexPllName());
							} else if (2 == index) {
								result.setIndexName(utProdInstm.getUndlIndexSllName());
							} else {
								result.setIndexName(utProdInstm.getUndlIndexName());
							}
						} else {
							if (0 == index) {
								result.setProductName(utProdInstm.getProdName());
							} else if (1 == index) {
								result.setProductName(utProdInstm.getProdPllName());
							} else if (2 == index) {
								result.setProductName(utProdInstm.getProdSllName());
							} else {
								result.setProductName(utProdInstm.getProdName());
							}
						}
						break;
					}
				}
			}
		}
	}

	@SuppressWarnings("uncheck")
	protected AdvanceChartResponse sendAdvanceChartRequest(final AdvanceChartRequest request, final String requestKey)
			throws Exception {
		Map<String, Result> resultMap = null;
		Map<String, ChartProduct> currencyMap = null;
		String locale = request.getLocale();
		int index = this.localeMappingUtil
				.getNameByIndex(request.getCountryCode() + CommonConstants.SYMBOL_DOT + locale);
		//String currency = request.getCurrency();
		List<ProductKey> productKeys = request.getProductKeys();
		Set<String> numSet = validateProductKey(request.getProductKeys());
		AdvanceChartResponse advanceChartResponse = new AdvanceChartResponse();
		advanceChartResponse.setResult(new ArrayList<Result>());
		String mastrIdString = "";
		if (ListUtil.isValid(productKeys)) {
			List<ProductKey> productKeyMcode = new ArrayList<>();
			List<ProductKey> productKeyZcode = new ArrayList<>();

			// get the Z code productKeys and M code productKeys
			if (numSet.contains("M")) {
				productKeyMcode.addAll(productKeys);
			} else if (numSet.contains("Z")) {
				productKeyZcode.addAll(productKeys);
			}
			resultMap = new HashMap<>();
			currencyMap = new HashMap<>();
			if (ListUtil.isValid(productKeyMcode)) {
				boolean indexCode = isIndexCode(request);
				List<UtProdInstm> utProdInstms = this.advanceChartDao.getUtProdInstmList(productKeyMcode, request.getHeaders());
				if (ListUtil.isValid(utProdInstms)) {
					for (UtProdInstm utProdInstm : utProdInstms) {
						String mastrId = "";
						if (indexCode) {
							mastrId = utProdInstm.getUndlIndexCde();
						} else {
							mastrId = utProdInstm.getmStarID();
						}
						mastrIdString = new StringBuilder(mastrIdString).append(mastrId)
								.append(CommonConstants.SYMBOL_VERTICAL).toString();
						if (null != utProdInstm) {
							Result result = new Result();
							result.setCurrency(utProdInstm.getClosingPrcCcy());
							result.setFrequency(request.getFrequency());
							resultMap.put(mastrId, result);
							ChartProduct chartPro = new ChartProduct();
							chartPro.setCurrency(utProdInstm.getMstarCcyCde());
							chartPro.setEndDate(DateUtil.getSimpleDateFormat(utProdInstm.getAsOfDate(),
									DateConstants.DateFormat_yyyyMMdd_withHyphen));
							currencyMap.put(mastrId, chartPro);
							// resultMap.put(mastrId.concat(CommonConstants.SYMBOL_VERTICAL).concat(utProdInstm.getSymbol()),
							// result);
							SearchProduct searchProduct = this.internalProductKeyUtil.getProductBySearchWithAltClassCde(
									"M", request.getCountryCode(), request.getGroupMember(), utProdInstm.getSymbol(),
									utProdInstm.getMarket(), utProdInstm.getProductType(), request.getLocale());
							if (null != searchProduct && null != searchProduct.getSearchObject()) {
								List<ProdAltNumSeg> prodAltNumSegs = searchProduct.getSearchObject().getProdAltNumSeg();
								result.setProdAltNumSegs(prodAltNumSegs);
							} else {
								LogUtil.error(AdvanceChartServiceImpl.class, "No record found for symbol="
										+ utProdInstm.getSymbol() + " market=" + utProdInstm.getMarket());
							//	throw new CommonException(ErrTypeConstants.WARNMSG_NORECORDFOUND);
							}
							if (indexCode) {
								if (0 == index) {
									result.setIndexName(utProdInstm.getUndlIndexName());
								} else if (1 == index) {
									result.setIndexName(utProdInstm.getUndlIndexPllName());
								} else if (2 == index) {
									result.setIndexName(utProdInstm.getUndlIndexSllName());
								} else {
									result.setIndexName(utProdInstm.getUndlIndexName());
								}
							} else {
								if (0 == index) {
									result.setProductName(utProdInstm.getProdName());
								} else if (1 == index) {
									result.setProductName(utProdInstm.getProdPllName());
								} else if (2 == index) {
									result.setProductName(utProdInstm.getProdSllName());
								} else {
									result.setProductName(utProdInstm.getProdName());
								}
							}
						}
					}

				} else {
					LogUtil.error(AdvanceChartServiceImpl.class,
							"No record found in DB table v_ut_prod_instm,prodkeys ="
									+ (JSONArray.fromObject(productKeys)).toString());
				//	throw new CommonException(ErrTypeConstants.WARNMSG_NORECORDFOUND);
				}
			} else if (ListUtil.isValid(productKeyZcode)) {
				Result result = null;
				for (ProductKey productkey : productKeyZcode) {
					result = new Result();
					result.setCurrency("HKD");
					result.setFrequency(request.getFrequency());
					resultMap.put(productkey.getProdAltNum(), result);
					// resultMap.put(
					// productkey.getProdAltNum().concat(CommonConstants.SYMBOL_VERTICAL).concat(productkey.getProdAltNum()),
					// result);
					List<ProdAltNumSeg> prodAltNumSegs = new ArrayList<>();
					ProdAltNumSeg prodAltNumSeg = new ProdAltNumSeg();
					prodAltNumSeg.setProdCdeAltClassCde("Z");
					prodAltNumSeg.setProdAltNum(productkey.getProdAltNum());
					prodAltNumSegs.add(prodAltNumSeg);
					result.setProdAltNumSegs(prodAltNumSegs);
					mastrIdString = new StringBuilder(mastrIdString).append(productkey.getProdAltNum())
							.append(CommonConstants.SYMBOL_VERTICAL).toString();
				}
			}
			if (mastrIdString.length() > 0) {
				mastrIdString = mastrIdString.substring(0, mastrIdString.length() - 1);
				getMastarInfo(request, advanceChartResponse, mastrIdString, resultMap, requestKey, currencyMap);
			} else {
				LogUtil.error(AdvanceChartServiceImpl.class,
						"the productKey exist issue = " + (JSONArray.fromObject(productKeys)).toString());
			//	throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
			}
		}
		return advanceChartResponse;
	}

	private void getMastarInfo(final AdvanceChartRequest request, final AdvanceChartResponse response,
			final String mastrIdString, final Map<String, Result> resultMap, final String requestKey,
			final Map<String, ChartProduct> currencyMap) throws Exception {
		String currency = "";
		for (Entry<String, ChartProduct> map : currencyMap.entrySet()) {
			currency = map.getValue().getCurrency();
			break;
		}
		String mastrIdArray[] = mastrIdString.split(CommonConstants.SYMBOL_SEPARATOR);
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair(AdvanceChartServiceImpl.STARTDATE, request.getStartDate()));
		params.add(new BasicNameValuePair(AdvanceChartServiceImpl.ENDDATE, request.getEndDate()));
		params.add(new BasicNameValuePair(AdvanceChartServiceImpl.CURRENCYID,
				StringUtil.isValid(currency) ? currency : request.getCurrency()));
		params.add(new BasicNameValuePair(AdvanceChartServiceImpl.IDTYPE, AdvanceChartServiceImpl.IDTYPEVALUE));
		params.add(new BasicNameValuePair(AdvanceChartServiceImpl.ID, mastrIdArray[0]));
		params.add(new BasicNameValuePair(AdvanceChartServiceImpl.FREQUENCY, request.getFrequency()));
		if (request.getNavForwardFill()) {
			params.add(new BasicNameValuePair(AdvanceChartServiceImpl.FORWARDFILL, "true"));
		} else {
			params.add(new BasicNameValuePair(AdvanceChartServiceImpl.FORWARDFILL, "false"));
		}
		String reqNavUrl = new StringBuilder(this.navUrl).append(CommonConstants.SYMBOL_SLASH).append(this.mstarToken)
				.toString();
		String reqGrowthUrl = new StringBuilder(this.growthUrl).append(CommonConstants.SYMBOL_SLASH)
				.append(this.mstarToken).toString();

		// create the AdvanceChartHelp List for calling Growth
		List<AdvanceChartHelp> advanceChartHelps = new ArrayList<>();
		List<AdvanceChartHelp> advanceChartHelpsNav = new ArrayList<>();
		List<AdvanceChartHelp> advanceChartHelpsGrowth = new ArrayList<>();
		if (null != mastrIdArray) {
			for (String str : mastrIdArray) {
				 collectAdvanceChartHelpInfo(request, advanceChartHelpsGrowth, str, this.growthDataClassJC, params,
						this.growthUrl, reqGrowthUrl, currencyMap);
				 collectAdvanceChartHelpInfo(request, advanceChartHelpsNav, str, this.navDataClassJC, params, this.navUrl, reqNavUrl,
						currencyMap);
			}
		}
		Map<String, Boolean> dataTypeMap = checkDataType(request);
		List<ResponseData> responseDataList = null;
		AdvanceChartNavData advanceChartNavData = null;
		if (dataTypeMap.get(CALLGROWTHANDNAVPRICE)) {
			advanceChartHelps.addAll(advanceChartHelpsNav);
			advanceChartHelps.addAll(advanceChartHelpsGrowth);
			responseDataList = MultithreadCallMstarApi(advanceChartHelps);
		} else if (dataTypeMap.get(CALLNAVPRICE)) {
			responseDataList = MultithreadCallMstarApi(advanceChartHelpsNav);
		} else if (dataTypeMap.get(CALLGROWTH)) {
			responseDataList = MultithreadCallMstarApi(advanceChartHelpsGrowth);
		}

		AdvanceChartGrowthData advanceChartGrowthData = null;
		if (ListUtil.isValid(responseDataList)) {
			List<com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.growth.Security> securitys = new ArrayList<>();
			List<com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.Security> navDataSecuritys = new ArrayList<>();
			for (ResponseData responseData : responseDataList) {
				if (responseData instanceof AdvanceChartGrowthData) {
					AdvanceChartGrowthData futureResponse = (AdvanceChartGrowthData) responseData;
					if (null != futureResponse && ListUtil.isValid(futureResponse.getSecurity())) {
						securitys.addAll(futureResponse.getSecurity());
					}
				} else if (responseData instanceof AdvanceChartNavData) {
					AdvanceChartNavData futureResponse = (AdvanceChartNavData) responseData;
					if (null != futureResponse && ListUtil.isValid(futureResponse.getSecurity())) {
						navDataSecuritys.addAll(futureResponse.getSecurity());
					}
				}
			}
			advanceChartGrowthData = new AdvanceChartGrowthData();
			advanceChartGrowthData.setSecurity(securitys);
			advanceChartNavData = new AdvanceChartNavData();
			advanceChartNavData.setSecurity(navDataSecuritys);
		}

		Map<String, List<Data>> dataMap = mergeMstarResponse(advanceChartNavData, advanceChartGrowthData);
		mergeResponse(dataMap, response, request, resultMap, advanceChartNavData, advanceChartGrowthData, requestKey);
	}



	private List<AdvanceChartHelp> collectAdvanceChartHelpInfo(final AdvanceChartRequest reqeust,
			final List<AdvanceChartHelp> advanceChartHelps, final String id, final JAXBContext jaxbContext,
			final List<NameValuePair> params, final String mstarUrl, final String reqUrl,
			final Map<String, ChartProduct> currencyMap) throws Exception {
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

	private AdvanceChartNavData getNavPriceData(final AdvanceChartRequest request, String StartDate,
			final String mastrIdString, final List<NameValuePair> params, final String reqNavUrl) {
//		if (request.getFrequency().equals(AdvanceChartServiceImpl.MONTYLY)) {
//			try {
//			//	StartDate = calculateNavStartDate(request, StartDate);
//			} catch (ParseException e) {
//				LogUtil.error(AdvanceChartServiceImpl.class, "StartDate is invalid, newStartDate: " + StartDate);
//				throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
//			}
//		}

		params.set(AdvanceChartServiceImpl.INDEX_0,
				new BasicNameValuePair(AdvanceChartServiceImpl.STARTDATE, StartDate));
		params.set(AdvanceChartServiceImpl.INDEX_4, new BasicNameValuePair(AdvanceChartServiceImpl.ID, mastrIdString));
		String reqParams = URLEncodedUtils.format(params, CommonConstants.CODING_UTF8);
		// call nva url
		AdvanceChartNavData advanceChartNavData = (AdvanceChartNavData) callMstarRelationApi(reqNavUrl, this.navUrl,
				reqParams, this.navDataClassJC, AdaptorUtil.convertNameValuePairListToString(params));

		return advanceChartNavData;
	}

	private Map<String, Boolean> checkDataType(final AdvanceChartRequest request) {
		Map<String, Boolean> dataTypeMap = new HashMap<String, Boolean>() {
			{
				put(CALLGROWTHANDNAVPRICE, Boolean.FALSE);
				put(CALLGROWTH, Boolean.FALSE);
				put(CALLNAVPRICE, Boolean.FALSE);
			}
		};
		String dataType[] = request.getDataType();
		if (null != dataType) {
			Set<String> dataTypelist = new HashSet<>(Arrays.asList(dataType));
			if (dataTypelist.contains(AdvanceChartServiceImpl.DATATYPE_NAVPRICE)
					&& dataTypelist.contains(AdvanceChartServiceImpl.DATATYPE_CUMULATIVERETURN)) {
				dataTypeMap.put(CALLGROWTHANDNAVPRICE, Boolean.TRUE);
			} else if (dataTypelist.contains(AdvanceChartServiceImpl.DATATYPE_INDEXPRICE)
					&& dataTypelist.contains(AdvanceChartServiceImpl.DATATYPE_INDEXRETURN)) {
				dataTypeMap.put(CALLGROWTH, Boolean.TRUE);
			} else if (dataTypelist.contains(AdvanceChartServiceImpl.DATATYPE_NAVPRICE)
					|| dataTypelist.contains(AdvanceChartServiceImpl.DATATYPE_INDEXPRICE)) {
				dataTypeMap.put(CALLNAVPRICE, Boolean.TRUE);
			} else if (dataTypelist.contains(AdvanceChartServiceImpl.DATATYPE_CUMULATIVERETURN)
					|| dataTypelist.contains(AdvanceChartServiceImpl.DATATYPE_INDEXRETURN)) {
				dataTypeMap.put(CALLGROWTH, Boolean.TRUE);
			} else {
				LogUtil.error(AdvanceChartServiceImpl.class, ErrTypeConstants.SOME_INPUT_PARAMETER_INVALID);
				throw new CommonException(ErrTypeConstants.SOME_INPUT_PARAMETER_INVALID);
			}
		}
		return dataTypeMap;
	}


	private void mergeResponse(final Map<String, List<Data>> dataMap, final AdvanceChartResponse response,
			final AdvanceChartRequest request, final Map<String, Result> resultMap,
			final AdvanceChartNavData advanceChartNavData, final AdvanceChartGrowthData advanceChartGrowthData,
			final String requestKey) {
		List<Result> results = new ArrayList<>();
		if (null != dataMap && dataMap.size() > 0) {
			for (Map.Entry<String, List<Data>> entry : dataMap.entrySet()) {
				String id = entry.getKey();
				if (resultMap.containsKey(id)) {
					Result result = resultMap.get(id);
					result.setData(entry.getValue());
					results.add(result);
				}
			}
		}

		// convert the proudct order
		List<ProductKey> productKeys = request.getProductKeys();
		List<Result> resultsByOrder = new ArrayList<>();
		for(ProductKey productKey : productKeys){
			String mCode = productKey.getProdAltNum();
           for(Result result : results){
			  List<ProdAltNumSeg> prodAltNumSegs = result.getProdAltNumSegs();
			   for(ProdAltNumSeg prodAltNumSeg : prodAltNumSegs){
			   	 if(prodAltNumSeg.getProdCdeAltClassCde().equals("M") && mCode.equals(prodAltNumSeg.getProdAltNum())){
					 resultsByOrder.add(result);
					 break;
				 }
			   }
		   }
		}



		response.setResult(resultsByOrder);
		// check if cache logic
		Map<String, Boolean> dataTypeMap = checkDataType(request);
		boolean cacheEnable = Boolean.FALSE;
		boolean autoCacheEnable = Boolean.FALSE;

		if ("Z".equals(productKeys.get(0).getProdCdeAltClassCde()) && null != response.getResult()
				&& request.getProductKeys().size() == response.getResult().size()) {
			cacheEnable = true;
		} else if ("M".equals(productKeys.get(0).getProdCdeAltClassCde()) && null != response.getResult()
				&& request.getProductKeys().size() == response.getResult().size()) {
			if (dataTypeMap.get(CALLGROWTHANDNAVPRICE)) {
				if (null != advanceChartNavData && null != advanceChartGrowthData
						&& ListUtil.isValid(advanceChartNavData.getSecurity())
						&& ListUtil.isValid(advanceChartGrowthData.getSecurity())
						&& advanceChartNavData.getSecurity().size() == advanceChartGrowthData.getSecurity().size())
					if (ListUtil.isValid(advanceChartGrowthData.getSecurity().get(0).getCumulativeReturnSeries())
							&& ListUtil.isValid(advanceChartNavData.getSecurity().get(0).getHistoryDetail())) {
						cacheEnable = true;
					}
			} else if (dataTypeMap.get(CALLNAVPRICE)) {
				if (null != advanceChartNavData && ListUtil.isValid(advanceChartNavData.getSecurity())) {
					cacheEnable = true;
				}
			} else if (dataTypeMap.get(CALLGROWTH)) {
				if (null != advanceChartGrowthData && ListUtil.isValid(advanceChartGrowthData.getSecurity())) {
					cacheEnable = true;
				}
			}
		}
		if (StringUtil.isValid(autoCache)) {
			autoCacheEnable = Boolean.valueOf(autoCache);
		}

		if (cacheEnable && autoCacheEnable) {
			ObjectMapper jb = new ObjectMapper();
			try {
				ehcacheUtil.put(ADVANCECHART_CACHE, requestKey, jb.writeValueAsString(response), true, 1 * 1800);
				LogUtil.info(AdvanceChartServiceImpl.class,
						" current memroy == " + ehcacheUtil.getMemory(ADVANCECHART_CACHE));
			} catch (JsonProcessingException e) {

				throw new CommonException(ErrTypeConstants.UNDEFINED);
			}
		}
	}


	private Map<String, List<Data>> mergeMstarResponse(final AdvanceChartNavData advanceChartNavData,
			final AdvanceChartGrowthData advanceChartGrowthData) {
		Map<String, List<Data>> dataMap = new HashMap<>();
		if (null != advanceChartGrowthData) {
			if (null != advanceChartGrowthData.getSecurity()) {
				List<com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.growth.Security> securityList = advanceChartGrowthData
						.getSecurity();
				for (com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.growth.Security securityGrowth : securityList) {
					String growthId = securityGrowth.getId();
					List<Data> dataList = new ArrayList<>();
					for (CumulativeReturnSeries cumulativeReturnSeries : securityGrowth.getCumulativeReturnSeries()) {
						for (com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.growth.HistoryDetail growthHistoryDetail : cumulativeReturnSeries
								.getHistoryDetail()) {
							Data data = new Data();
							data.setDate(growthHistoryDetail.getEndDate());
							data.setCumulativeReturn(new BigDecimal(growthHistoryDetail.getValue()));
							dataList.add(data);
						}
					}
					dataMap.put(growthId, dataList);
				}
			}
		}
		if (null != advanceChartNavData) {
			if (advanceChartNavData.getSecurity() != null) {
				List<Security> securityList = advanceChartNavData.getSecurity();
				for (Security security : securityList) {
					List<Data> merageData = new ArrayList<>();
					if (dataMap.containsKey(security.getId())) {
						List<Data> growthData = dataMap.get(security.getId());
						for (int i = 0, length = security.getHistoryDetail().size(); i < length; i++) {
							HistoryDetail historyDetail = security.getHistoryDetail().get(i);
							for (Data data : growthData) {
								if (data.getDate().equals(historyDetail.getEndDate())) {
									data.setNavPrice(new BigDecimal(historyDetail.getValue()));
									break;
								}
							}
						}
						dataMap.put(security.getId(), growthData);
					} else if (ListUtil.isValid(security.getHistoryDetail())) {
						for (int i = 0, length = security.getHistoryDetail().size(); i < length; i++) {
							HistoryDetail historyDetail = security.getHistoryDetail().get(i);
							Data data = new Data();
							data.setDate(historyDetail.getEndDate());
							data.setNavPrice(new BigDecimal(historyDetail.getValue()));
							merageData.add(data);
						}
						dataMap.put(security.getId(), merageData);
					}
				}
			}
		}
		return dataMap;
	}


	private ResponseData callMstarRelationApi(final String url, final String baseUrl, final String reqParams,
			final JAXBContext jaxbContext, final String paramsWithoutEncode) {
		ResponseData responseData = null;
		LogUtil.infoOutboundMsg(AdvanceChartServiceImpl.class, VendorType.MSTAR, baseUrl, reqParams);
		try {
			String resp = this.connManager.sendRequest(url, reqParams, paramsWithoutEncode);
			LogUtil.infoInboundMsg(AdvanceChartServiceImpl.class, VendorType.MSTAR, resp);
			responseData = (ResponseData) jaxbContext.createUnmarshaller().unmarshal(new StringReader(resp));
		} catch (JAXBException e) {
			LogUtil.error(ReturnIndexChartServiceImpl.class, "Mstar MstarUnmarshalFail", e);
			throw new CommonException(MstarExceptionConstants.UNMARSHAL_FAIL, e.getMessage());
		} catch (Exception e) {
			String message = e.getMessage();
			LogUtil.info(AdvanceChartServiceImpl.class, "message info = ", message);
			if (null != message && message.toLowerCase().contains("timeout")) {
				LogUtil.error(AdvanceChartServiceImpl.class, "Mstar timeout error", e);
				throw new CommonException(MstarExceptionConstants.TIMEOUT, e.getMessage());
			} else if (null != message && message.toLowerCase().contains("nodata")) {
				LogUtil.error(AdvanceChartServiceImpl.class, "Mstar no data error", e);
				throw new CommonException(MstarExceptionConstants.NO_DATA, e.getMessage());
			} else {
				LogUtil.error(AdvanceChartServiceImpl.class, "Mstar undefined error", e);
				throw new CommonException(MstarExceptionConstants.UNDEFINED, e.getMessage());
			}
		}
		if (null == responseData) {
			LogUtil.error(AdvanceChartServiceImpl.class, "Mstar response is null");
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
					paramss.set(AdvanceChartServiceImpl.INDEX_0,
							new BasicNameValuePair(AdvanceChartServiceImpl.STARTDATE, startDate));
					paramss.set(AdvanceChartServiceImpl.INDEX_1,
							new BasicNameValuePair(AdvanceChartServiceImpl.ENDDATE, endDate));
					paramss.set(AdvanceChartServiceImpl.INDEX_2,
							new BasicNameValuePair(AdvanceChartServiceImpl.CURRENCYID, currency));
					paramss.set(AdvanceChartServiceImpl.INDEX_4,
							new BasicNameValuePair(AdvanceChartServiceImpl.ID, id));
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


	private String calculateNavStartDate(final AdvanceChartRequest request, final String startDate)
			throws ParseException {
		String timeZone = request.getTimeZone();
		String oldFormatPattern = DateConstants.DateFormat_yyyyMMdd_withHyphen;
		String newFormatPattern = DateConstants.DateFormat_yyyyMMdd_withHyphen;
		TimeZone oldTimezone = TimeZone.getDefault();
		TimeZone newTimezone = TimeZone.getTimeZone(timeZone);
		SimpleDateFormat format = new SimpleDateFormat(oldFormatPattern);
		Calendar cal = Calendar.getInstance();
		Date date = format.parse(startDate);
		cal.setTime(date);
		String newStartDate = null;
		String period = request.getPeriod();
		if (request.getFrequency().equals(DAILY)) {
			if (period.equals("YTD")) {
				cal.set(cal.get(Calendar.YEAR), 1 - 1, 1);
				cal.add(Calendar.DATE, -15);
				String dateStr = format.format(cal.getTime());
				newStartDate = DateUtil.dateByTimeZone(dateStr, oldFormatPattern, newFormatPattern, oldTimezone,
						newTimezone);
			} else if (period.equals("MAX")) {
				cal.set(1900, 0, 1);
				cal.add(Calendar.DATE, -15);
				String dateStr = format.format(cal.getTime());
				newStartDate = DateUtil.dateByTimeZone(dateStr, oldFormatPattern, newFormatPattern, oldTimezone,
						newTimezone);
			}else {
				String dateType = period.substring(period.length() - 1).toUpperCase();
				int number = Integer.parseInt(period.substring(0, period.length() - 1));
				if (CommonConstants.YEAR_PERIOD.equals(dateType)) {
					cal.add(Calendar.YEAR, -number);
					cal.add(Calendar.DATE, -15);
				} else if (CommonConstants.MONTH_PERIOD.equals(dateType)) {
					cal.add(Calendar.MONTH, -number);
					cal.add(Calendar.DATE, -15);
				} else {
					LogUtil.error(AdvanceChartServiceImpl.class, "period is invalid, period: " + period);
					throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
				}
			}
			
		} else {
			cal.add(Calendar.WEDNESDAY, -AdvanceChartServiceImpl.FORWARD_WEEK);
		}
		String dateStr = format.format(cal.getTime());
		 newStartDate = DateUtil.dateByTimeZone(dateStr, oldFormatPattern, newFormatPattern, oldTimezone,
				newTimezone);
		return newStartDate;
	}


	private String calculateTradeTime(final AdvanceChartRequest request, final String calEndDate) throws Exception {
		
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
			try {
				enddate = df.format(cal.getTime());
				if (StringUtil.isValid(calEndDate)) {
					enddate = calEndDate;
					cal.setTime(df.parse(enddate));
				}

				if (period.equals("YTD")) {
					cal.set(cal.get(Calendar.YEAR), 1 - 1, 1);
					String dateStr = df.format(cal.getTime());
					startdate = DateUtil.dateByTimeZone(dateStr, oldFormatPattern, newFormatPattern, oldTimezone,
							newTimezone);
				} else if (period.equals("MAX")) {
					cal.set(1900, 0, 1);
					String dateStr = df.format(cal.getTime());
					startdate = DateUtil.dateByTimeZone(dateStr, oldFormatPattern, newFormatPattern, oldTimezone,
							newTimezone);

				} else {
					String dateType = period.substring(period.length() - 1).toUpperCase();
					int number = Integer.parseInt(period.substring(0, period.length() - 1));
					if (CommonConstants.YEAR_PERIOD.equals(dateType)) {
						cal.add(Calendar.YEAR, -number);
						// cal.add(Calendar.MONTH);
					} else if (CommonConstants.MONTH_PERIOD.equals(dateType)) {
						cal.add(Calendar.MONTH, -number);
					} else {
						LogUtil.error(AdvanceChartServiceImpl.class, "period is invalid, period: " + period);
						throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
					}
					String dateStr = df.format(cal.getTime());
					startdate = DateUtil.dateByTimeZone(dateStr, oldFormatPattern, newFormatPattern, oldTimezone,
							newTimezone);
				}
			} catch (Exception e) {
				LogUtil.error(AdvanceChartServiceImpl.class,
						"InputParameterInvalid, period: " + period + ", timeZone: " + timeZone, e);
				throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
			}
		} else {
			timeZone = request.getTimeZone();
			cal.add(Calendar.YEAR, -3);
			String dateStr = df.format(cal.getTime());
			enddate = df.format(cal.getTime());
			startdate = DateUtil.dateByTimeZone(dateStr, oldFormatPattern, newFormatPattern, oldTimezone, newTimezone);
		}
		LogUtil.info(AdvanceChartServiceImpl.class, "startdate: " + startdate + ", enddate" + enddate);
		//request.setEndDate(enddate);
		return startdate;
	}

}
