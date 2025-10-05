package com.hhhh.group.secwealth.mktdata.predsrch.svc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.ListUtils;
import org.compass.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.criteria.CriteriaOperator;
import com.hhhh.group.secwealth.mktdata.common.criteria.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.common.dto.InternalProductKey;
import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.SiteFeature;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.SearchableObject;
import com.hhhh.group.secwealth.mktdata.predsrch.pres.beans.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.predsrch.pres.beans.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.Product;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.ProductEntities;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.ResponseInfo;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.constants.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.handler.QueryExpressionHandler;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.manager.PredictiveSearchManager;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.service.PredictiveSearchService;

@Service("predictiveSearchService")
public class PredictiveSearchServiceImpl implements PredictiveSearchService {

	/** The manager. */
	@Autowired
	@Qualifier("predictiveSearchManager")
	private PredictiveSearchManager predictiveSearchManager;

	/** The locale mapping. */
	private Map<String, String> localeMapping;

	/** The locales. */
	@Value("#{systemConfig['localeMapping']}")
	private String[] locales;

	/** The search type by field. */
	private Map<String, String> searchTypeByField;

	/** The search types. */
	@Value("#{systemConfig['predsrch.searchType']}")
	private String[] searchTypes;

	/** The product entities. */
	@Autowired
	@Qualifier("productEntities")
	private ProductEntities productEntities;

	/** The Constant RECORD_FRIST_NUMBER. */
	public final static int RECORD_FRIST_NUMBER = 1;

	// Set default needed records is 10
	public final static int NEEDED_RECORD_NUMBER = 10;

	// Example: [_`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~]
	private final String SPECIALCHAR_REGEX = "[*]";
	private Pattern pattern;

	@Autowired
	@Qualifier("siteFeature")
	private SiteFeature siteFeature;

	private final String SWITCHOUTFUND = ".predictiveSearch.switchOutFund";
	private final String UNSWITCHOUTFUND = ".predictiveSearch.unSwitchOutFund";
	private final String SWITCHOUTFUND_SWITCHABLEGROUP = "switchableGroup";
	private final String SWITCHOUTFUND_ALLOWSWOUTPRODIND = "allowSwOutProdInd";
	private final String SWITCHOUTFUND_UNSWITCHABLELIST = "unSwitchableList";

	public static final String HK_LOCALE_ZH_HK = "HK.zh_HK";

	/**
	 * <p>
	 * <b> Initialize the locale mapping and search type for fields. </b>
	 * </p>
	 *
	 * @throws Exception
	 *             the exception
	 */
	@PostConstruct
	public void init() throws Exception {
		try {
			this.localeMapping = new ConcurrentHashMap<String, String>();
			if (null != this.locales && this.locales.length > 0) {
				for (String s : this.locales) {
					String[] mappings = s.split(CommonConstants.SYMBOL_COLON);
					this.localeMapping.put(mappings[0], mappings[1]);
				}
			}
			this.searchTypeByField = new ConcurrentHashMap<String, String>();
			if (null != this.searchTypes && this.searchTypes.length > 0) {
				for (String s : this.searchTypes) {
					String[] types = s.split(CommonConstants.SYMBOL_SEPARATOR);
					this.searchTypeByField.put(types[0], types[1]);
				}
			}
			this.pattern = Pattern.compile(this.SPECIALCHAR_REGEX);
		} catch (Exception e) {
			LogUtil.error(PredictiveSearchServiceImpl.class,
					"Can't init PredictiveSearchServiceImpl|exception=" + e.getMessage(), e);
			throw new SystemException(ErrTypeConstants.SYSTEM_INIT_ERROR, e);
		}
	}

	/**
	 * Find record by keyword.
	 *
	 * @param object
	 *            the object
	 * @return the object
	 * @throws Exception
	 *             the exception
	 */
	@Override
	public Object doService(final Object object) throws Exception {
		LogUtil.debug(PredictiveSearchServiceImpl.class, "Predictive Search start");
		PredSrchRequest request = (PredSrchRequest) object;
		List<ResponseInfo> resInfoltList = null;

		String locale = request.getLocale();
		String countryCode = request.getCountryCode();
		String groupMember = request.getGroupMember();
		String site = StringUtil.combineWithUnderline(countryCode, groupMember);
		String searchClauses = null;
		String keyword = request.getKeyword();
		// Return null if the keyword have wildcard when keyword!=null
		if (null != keyword && !keyword.equals(CommonConstants.EMPTY_STRING)) {
//			if (isWildcard(keyword) || isSpecialChar(keyword)) {
			if (isWildcard(keyword)) {
				LogUtil.errorBeanToJson(PredictiveSearchServiceImpl.class,
						"PredictiveSearch keyword isWildcard, Request message=", request);
				return ListUtils.EMPTY_LIST;
//			} else if (isSpecialChar(keyword)) {
//				// LogUtil.errorBeanToJson(PredictiveSearchServiceImpl.class,
//				// "PredictiveSearch keyword isSpecialChar, Request message=", request);
//				// throw new ValidatorException(ErrTypeConstants.INPUT_PARAMETER_INVALID,
//				// keyword);
//				LogUtil.errorBeanToJson(PredictiveSearchServiceImpl.class,
//						"PredictiveSearch keyword isWildcard, Request message=", request);
//				return ListUtils.EMPTY_LIST;
			} else {
				keyword = keyword.toLowerCase();
			}
		}

		// Processor for switchable group
		List<SearchCriteria> filterCriterias = request.getFilterCriterias();
		if (null != filterCriterias && filterCriterias.size() > 0) {
			List<SearchCriteria> moreCriterias = new ArrayList<SearchCriteria>();
			for (SearchCriteria criteria : filterCriterias) {
				if (QueryExpressionHandler.SWITCHOUT_FUND.equalsIgnoreCase(criteria.getCriteriaKey())) {
					// prodAltCode|Symbol|countryTradableCode|productType
					String[] fields = criteria.getCriteriaValue().split(CommonConstants.SYMBOL_COLON);
					SearchableObject tempObject = getBySymbolCountryTradableCode(site, countryCode, fields[1],
							fields[2], fields[3], locale);
					if (null != tempObject) {
						String switchOutFund = this.siteFeature.getStringDefaultFeature(site, this.SWITCHOUTFUND);
						if (StringUtil.isInvalid(switchOutFund)) {
							switchOutFund = this.SWITCHOUTFUND_SWITCHABLEGROUP;
						}
						// UK(GB_HBEU Feature)
						if (this.SWITCHOUTFUND_ALLOWSWOUTPRODIND.equals(switchOutFund)) {
							if (null != tempObject.getAllowSwOutProdInd_analyzed() && CommonConstants.YES
									.equalsIgnoreCase(tempObject.getAllowSwOutProdInd_analyzed())) {
								SearchCriteria productCriteria = new SearchCriteria(QueryExpressionHandler.PRODUCT_KEY,
										criteria.getCriteriaValue(), CriteriaOperator.NE_OPERATOR);
								moreCriterias.add(productCriteria);
								criteria.setCriteriaKey(QueryExpressionHandler.ALLOWSWIN_FUND);
								criteria.setCriteriaValue(tempObject.getAllowSwOutProdInd_analyzed());
							} else {
								LogUtil.error(PredictiveSearchServiceImpl.class,
										"The product={} not support switch out", fields[2].toString());
								return ListUtils.EMPTY_LIST;
							}
							// Core Feature
						} else if (this.SWITCHOUTFUND_SWITCHABLEGROUP.equals(switchOutFund)) {
							if (null != tempObject.getSwitchableGroup_analyzed()
									&& tempObject.getSwitchableGroup_analyzed().length > 0) {
								SearchCriteria productCriteria = new SearchCriteria(QueryExpressionHandler.PRODUCT_KEY,
										criteria.getCriteriaValue(), CriteriaOperator.NE_OPERATOR);
								moreCriterias.add(productCriteria);
								criteria.setCriteriaKey(QueryExpressionHandler.SWITCHABLE_GROUP);
								criteria.setCriteriaValue(StringUtils.arrayToDelimitedString(
										tempObject.getSwitchableGroup_analyzed(), CommonConstants.SYMBOL_COLON));
								if (tempObject.getSwitchableGroup_analyzed().length > 1) {
									criteria.setOperator(CriteriaOperator.IN_OPERATOR);
								}
								String unSwitchOutFund = this.siteFeature.getStringDefaultFeature(site,
										this.UNSWITCHOUTFUND);
								if (StringUtil.isInvalid(unSwitchOutFund)) {
									switchOutFund = this.SWITCHOUTFUND_UNSWITCHABLELIST;
								}
								if (this.SWITCHOUTFUND_UNSWITCHABLELIST.equals(unSwitchOutFund)) {
									String[] fundUnswithSegs = tempObject.getFundUnswithSeg();
									if (null != fundUnswithSegs) {
										for (int i = 0; i < fundUnswithSegs.length; i++) {
											SearchCriteria unswithProduct = new SearchCriteria(
													QueryExpressionHandler.PRODUCT_KEY,
													CommonConstants.MONTH_PERIOD + ":" + fundUnswithSegs[i] + ":"
															+ request.getCountryCode() + ":"
															+ QueryExpressionHandler.PRODUCT_TYPE_FUND,
													CriteriaOperator.NE_OPERATOR);
											moreCriterias.add(unswithProduct);
										}
									}
								}
							}
						}
					} else {
						LogUtil.error(PredictiveSearchServiceImpl.class, "No record found for the Request={}",
								request.toString());
						return ListUtils.EMPTY_LIST;
					}
				}
			}
			filterCriterias.addAll(moreCriterias);
		}

		// predSearch filter "channelRestrictCode"
		if (StringUtil.isValid(request.getChannelRestrictCode())) {
			SearchCriteria restrictCode = new SearchCriteria(QueryExpressionHandler.RESCHANNELCDE,
					request.getChannelRestrictCode(), CriteriaOperator.NE_OPERATOR);
			if (ListUtil.isInvalid(filterCriterias)) {
				filterCriterias = new ArrayList<SearchCriteria>();
			}
			filterCriterias.add(restrictCode);
		}

		// predSearch filter "restrOnlScribInd"
		if (StringUtil.isValid(request.getRestrOnlScribInd())) {
			SearchCriteria restrOnlScribCode = new SearchCriteria(QueryExpressionHandler.RESTRONLSCRIBIND,
					request.getRestrOnlScribInd(), CriteriaOperator.NE_OPERATOR);
			if (ListUtil.isInvalid(filterCriterias)) {
				filterCriterias = new ArrayList<SearchCriteria>();
			}
			filterCriterias.add(restrOnlScribCode);
		}

		// countryTradableCode mapping ctryProdTrade1Cde from predictive search
		// files, use ctryProdTrade1Cde to identify if the stock belong to HK
		// or CN
		if (StringUtil.isValid(request.getMarket())) {
			SearchCriteria criteria = new SearchCriteria("countryTradableCode", request.getMarket(),
					CriteriaOperator.EQ_OPERATOR);
			if (ListUtil.isInvalid(filterCriterias)) {
				filterCriterias = new ArrayList<SearchCriteria>();
			}
			filterCriterias.add(criteria);
		}

		List<SearchableObject> resultList = new ArrayList<SearchableObject>();
		List<SearchableObject> tempList = new ArrayList<SearchableObject>();
		String keyWord = request.getKeyword();

		int needRecords = PredictiveSearchServiceImpl.NEEDED_RECORD_NUMBER;
		String topNum = request.getTopNum();
		if (StringUtil.isValid(topNum)) {
			needRecords = Integer.valueOf(topNum);
		}
		String[] assetClasses = request.getAssetClasses();

		// Step1: Query by exact symbol
		// Step2: Query by exact productName
		// Step3: Query by start with symbol
		// Step4: Query by start with productName
		// Step5: Query by contains with symbol
		// Step6: Query by contains with productName
		int steps = 6;
		for (int step = 1; step <= steps; step++) {
			if (resultList.size() < needRecords) {
				// predictiveSearch request for example:
				// Default: (symbol_analyzed:5) AND
				// ((switchableGroup_analyzed:hhhh1) AND allowBuy_analyzed:y
				// AND NOT (symbol_analyzed:540002 AND
				// countryTradableCode_analyzed:CN AND
				// productType_analyzed:UT)) AND (productType_analyzed:UT)
				// =============================================================
				// For UK: (symbol_analyzed:b) AND
				// ((allowSwInProdInd_analyzed:y) AND
				// allowTradeProdInd_analyzed:y AND
				// prodTaxFreeWrapActStaCde_analyzed:1 AND NOT
				// (symbol_analyzed:b80qg38 AND countryTradableCode_analyzed:GB
				// AND productType_analyzed:UT)) AND (productType_analyzed:UT)
				searchClauses = QueryExpressionHandler.buildCommonQueryExpression(
						QueryExpressionHandler.getQueryTypeBySteps(step), assetClasses,
						QueryExpressionHandler.getSearchFieldsBySteps(step), keyWord, filterCriterias);
				tempList = this.predictiveSearchManager.findProductListByKey(searchClauses,
						QueryExpressionHandler.getSortingFieldsBySteps(step), site, needRecords);
				if (tempList != null && tempList.size() > 0) {
					mergeResults(resultList, tempList, request, needRecords);
				} else if (tempList == null) {
					// When tempList=null, it means hit the too many clauses
					// exception,
					// so break loop to return the searched results.
					break;
				}
			}
		}

		if (ListUtil.isValid(resultList)) {
			String key = StringUtil.combineWithDot(countryCode, locale);
			// get product name by locale
			int index = getNameByIndex(key);
			List<ResponseInfo> responseList = new ArrayList<ResponseInfo>();
			int size = resultList.size();
			for (int i = 0; i < size; i++) {
				SearchableObject obj = resultList.get(i);
				responseList.add(transformResponse(index, obj, request));
			}
			resInfoltList = responseList;
		} else {
			LogUtil.debug(PredictiveSearchServiceImpl.class, "No record found for the Request={}", request.toString());
			return ListUtils.EMPTY_LIST;
		}
		LogUtil.debug(PredictiveSearchServiceImpl.class, "Search end");
		return this.convertToResponse(resInfoltList);
	}

	public void mergeResults(final List<SearchableObject> resultList, final List<SearchableObject> tempList,
			final PredSrchRequest request, int needRecords) {
		List<SearchableObject> tempResultList = new ArrayList<SearchableObject>();
		int resultSize = resultList.size();
		for (int i = 0; i < tempList.size(); i++) {
			SearchableObject temp = tempList.get(i);
			boolean inResultList = false;
			for (int j = 0; j < resultSize; j++) {
				SearchableObject result = resultList.get(j);
				// Use M code + product type + market as compared key
				String comparedKey1 = new StringBuffer().append(result.getSymbol()).append(result.getProductType())
						.append(result.getCountryTradableCode()).toString();
				String comparedKey2 = new StringBuffer().append(temp.getSymbol()).append(temp.getProductType())
						.append(temp.getCountryTradableCode()).toString();
				LogUtil.debug(PredictiveSearchServiceImpl.class, "Compared key 1: {}, Compared key 2: {}", comparedKey1,
						comparedKey2);
				if (comparedKey1.equalsIgnoreCase(comparedKey2)) {
					LogUtil.debug(PredictiveSearchServiceImpl.class, "Hit the same record, comparedKey: {}",
							comparedKey1);
					inResultList = true;
					continue;
				}
			}
			if (!inResultList) {
				tempResultList.add(temp);
			}
		}
		needRecords = needRecords - resultSize;
		if (tempResultList.size() <= needRecords) {
			resultList.addAll(tempResultList);
		} else {
			resultList.addAll(tempResultList.subList(0, needRecords));
		}
	}

	/**
	 * com.hhhh.group.secwealth.mktdata.predsrch.svc.PredictiveSearchService
	 * #getBySymbolMarket
	 */
	@Override
	public SearchableObject getBySymbolMarket(final InternalProductKey ipk, final String locale) throws Exception {
		return getBySymbolCountryTradableCode(
				StringUtil.combineWithUnderline(ipk.getCountryCode(), ipk.getGroupMember()), ipk.getCountryCode(),
				ipk.getProdAltNum(), ipk.getCountryTradableCode(), ipk.getProductType(), locale);
	}

	@Override
	public SearchableObject getBySymbolMarket(final String countryCode, final String groupMember,
			final String prodAltNum, final String countryTradableCode, final String productType, final String locale)
			throws Exception {
		return getBySymbolCountryTradableCode(StringUtil.combineWithUnderline(countryCode, groupMember), countryCode,
				prodAltNum, countryTradableCode, productType, locale);
	}

	/**
	 * <p>
	 * <b> Get the product by symbol and countryTradableCode. </b>
	 * </p>
	 *
	 * @param site
	 * @param countryCode
	 * @param symbol
	 * @param countryTradableCode
	 * @param productType
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@Override
	public SearchableObject getBySymbolCountryTradableCode(final String site, final String countryCode,
			final String symbol, final String countryTradableCode, final String productType, final String locale)
			throws Exception {
		SearchableObject result = null;
		if (StringUtil.isValid(symbol)) {
			String searchClauses = QueryExpressionHandler.getProductByKey(symbol, countryTradableCode, productType);
			List<SearchableObject> resultList = this.predictiveSearchManager.findProductListByKey(searchClauses, null,
					site, PredictiveSearchServiceImpl.RECORD_FRIST_NUMBER);
			if (null != resultList && resultList.size() > 0) {
				SearchableObject obj = resultList.get(0);
				int index = 0;
				if (null != locale) {
					String indexKey = StringUtil.combineWithDot(countryCode, locale);
					// get product name by locale
					index = getNameByIndex(indexKey);
				}
				String productName = obj.getProductName_analyzed()[index];
				String productShortName = obj.getProductShortName_analyzed()[index];
				// Choose default language of product name if null for other
				// language
				if (null != productName && !CommonConstants.EMPTY_STRING.equals(productName)) {
					obj.setProductName(productName);
				}
				if (null != productShortName && !CommonConstants.EMPTY_STRING.equals(productShortName)) {
					obj.setProductShortName(productShortName);
				}
				result = obj;
			}
		}
		return result;
	}

	@Override
	public SearchableObject getByRicCode(final String countryCode, final String groupMember, final String ric,
			final String locale) throws Exception {
		if (StringUtil.isInvalid(ric)) {
			return null;
		}
		StringBuffer searchClauses = new StringBuffer();
		StringBuffer key = new StringBuffer(QueryExpressionHandler.toQueryString(ric));
		searchClauses.append(QueryExpressionHandler.KEY_ANALYZED).append(key);
		List<SearchableObject> resultList = this.predictiveSearchManager.findProductListByKey(searchClauses.toString(),
				null, StringUtil.combineWithUnderline(countryCode, groupMember),
				PredictiveSearchServiceImpl.RECORD_FRIST_NUMBER);
		if (null != resultList && resultList.size() > 0) {
			SearchableObject obj = resultList.get(0);
			int index = 0;
			if (null != locale) {
				String indexKey = StringUtil.combineWithDot(countryCode, locale);
				// get product name by locale
				index = getNameByIndex(indexKey);
			}
			String productName = obj.getProductName_analyzed()[index];
			String productShortName = obj.getProductShortName_analyzed()[index];
			// Choose default language of product name if null for other
			// language
			if (null != productName && !CommonConstants.EMPTY_STRING.equals(productName)) {
				obj.setProductName(productName);
			}
			if (null != productShortName && !CommonConstants.EMPTY_STRING.equals(productShortName)) {
				obj.setProductShortName(productShortName);
			}
			return obj;
		} else {
			return null;
		}
	}

	@Override
	public SearchableObject getByWCode(final String countryCode, final String groupMember, final String wCode,
			final String locale) throws Exception {
		if (StringUtil.isInvalid(wCode)) {
			return null;
		}
		StringBuffer searchClauses = new StringBuffer();
		StringBuffer key = new StringBuffer(QueryExpressionHandler.toQueryString(wCode));
		searchClauses.append(QueryExpressionHandler.PRODUCTCODE_ANALYZED).append(key);
		List<SearchableObject> resultList = this.predictiveSearchManager.findProductListByKey(searchClauses.toString(),
				null, StringUtil.combineWithUnderline(countryCode, groupMember),
				PredictiveSearchServiceImpl.RECORD_FRIST_NUMBER);
		if (null != resultList && resultList.size() > 0) {
			SearchableObject obj = resultList.get(0);
			int index = 0;
			if (null != locale) {
				String indexKey = StringUtil.combineWithDot(countryCode, locale);
				// get product name by locale
				index = getNameByIndex(indexKey);
			}
			String productName = obj.getProductName_analyzed()[index];
			String productShortName = obj.getProductShortName_analyzed()[index];
			// Choose default language of product name if null for other
			// language
			if (null != productName && !CommonConstants.EMPTY_STRING.equals(productName)) {
				obj.setProductName(productName);
			}
			if (null != productShortName && !CommonConstants.EMPTY_STRING.equals(productShortName)) {
				obj.setProductShortName(productShortName);
			}
			return obj;
		} else {
			return null;
		}
	}

	/**
	 * Transform response.
	 *
	 * @param index
	 *            the index
	 * @param obj
	 *            the obj
	 * @param request
	 *            the svc request
	 * @return the response info
	 * @throws Exception
	 *             the exception
	 */
	private ResponseInfo transformResponse(final int index, final SearchableObject obj, final PredSrchRequest request)
			throws Exception {
		ResponseInfo info = new ResponseInfo();

		updateAllowBuySellInd(obj, info, request.getChannelRestrictCode());

		info.setAssetCountrys(obj.getAssetCountry());
		info.setAssetSectors(obj.getAssetSector());
		info.setCountryTradableCode(obj.getCountryTradableCode());
		info.setExchange(obj.getExchange());
		info.setParentAssetClasses(obj.getParentAssetClass());
		info.setProdAltNumSegs(obj.getProdAltNumSeg());
		info.setSymbol(obj.getSymbol());
		info.setProductSubType(obj.getProductSubType());
		info.setProductCcy(obj.getProductCcy());
		info.setMarket(obj.getMarket());
		info.setFundHouseCde(obj.getFundHouseCde());
		info.setIsrBndNme(obj.getIsrBndNme());
		info.setProductCode(obj.getProductCode());
		info.setSwithableGroups(obj.getSwitchableGroup_analyzed());
		info.setFundUnswCdes(obj.getFundUnswithSeg());
		info.setAllowSellMipProdInd(obj.getAllowSellMipProdInd());
		info.setAllowTradeProdInd(obj.getAllowTradeProdInd());
		info.setProdTaxFreeWrapActStaCde(obj.getProdTaxFreeWrapActStaCde());
		info.setAllowSwInProdInd(obj.getAllowSwInProdInd());
		info.setAllowSwOutProdInd(obj.getAllowSwOutProdInd());
		info.setRiskLvlCde(obj.getRiskLvlCde());
		info.setChannelRestrictGroup(obj.getResChannelCde_analyzed());
		info.setChannelRestrictGroup(obj.getResChannelCde());
		info.setChanlCdeList(obj.getChanlCde_analyzed());
		info.setChanlCdeList(obj.getChanlCde());
		info.setProdStatCde(obj.getProdStatCde());
		info.setProdStatCde(obj.getProdStatCde_analyzed());
		info.setRestrOnlScribInd(obj.getRestrOnlScribInd());
		info.setPiFundInd(obj.getPiFundInd());
		info.setPiFundInd(obj.getPiFundInd_analyzed());
		info.setDeAuthFundInd(obj.getDeAuthFundInd());
		info.setDeAuthFundInd(obj.getDeAuthFundInd_analyzed());
		String tempProdName = obj.getProductName_analyzed()[index];
		// Choose default language of product name if null for other language
		if (null != tempProdName && !CommonConstants.EMPTY_STRING.equals(tempProdName)) {
			info.setProductName(tempProdName);
		} else {
			info.setProductName(obj.getProductName());
		}
		String tempProdShrtName = obj.getProductShortName_analyzed()[index];
		// Choose default language of product short name if null for other
		// language
		if (null != tempProdShrtName && !CommonConstants.EMPTY_STRING.equals(tempProdShrtName)) {
			info.setProductShortName(tempProdShrtName);
		} else {
			info.setProductShortName(obj.getProductShortName());
		}

		//HASE SWT, to support T.Chinese name
		retrieveProduct2ndName(info, obj);

		info.setProductType(obj.getProductType());

		String countryCode = request.getCountryCode();
		String groupMember = request.getGroupMember();
		Product product = this.productEntities.getProductEntities()
				.get(StringUtil.combineWithUnderline(countryCode, groupMember, info.getProductType()));
		// if the site product don't exist,then use default product
		if (product == null) {
			product = this.productEntities.getProductEntities().get(info.getProductType());
		}
		if (null != product) {
			String descriptor = product.getProductDescriptor();
			String text = product.getText();

			if (null != info.getSymbol()) {
				descriptor = descriptor.replace(PredictiveSearchConstant.MARKETCODE, info.getSymbol());
			} else {
				descriptor = descriptor.replace(PredictiveSearchConstant.MARKETCODE, CommonConstants.EMPTY_STRING);
			}

			String prodName = info.getProductName();
			descriptor = descriptor.replace(PredictiveSearchConstant.PRODUCTNAME, prodName);
			text = text.replace(PredictiveSearchConstant.PRODUCTNAME, prodName);
			info.setProdDecriptor(descriptor);
			text = text.replace(PredictiveSearchConstant.PRODUCTDESCRIPTOR, descriptor);
			if (null != info.getExchange()) {
				text = text.replace(PredictiveSearchConstant.EXCHANGE, info.getExchange());
			} else {
				text = text.replace(PredictiveSearchConstant.EXCHANGE, CommonConstants.EMPTY_STRING);
			}
			info.setText(text);
		}
		return info;
	}

	public void retrieveProduct2ndName(ResponseInfo info, SearchableObject obj) throws Exception {
		int tChineseNameIndex = getNameByIndex(HK_LOCALE_ZH_HK);
		String productChineseName = obj.getProductName_analyzed()[tChineseNameIndex];
		if (null != productChineseName) {
			info.setProductSecondName(productChineseName);
		} else {
			info.setProductSecondName(obj.getProductName());
		}
	}

	/**
	 * Gets the product name() by locale index. product default name or product
	 * primary name or product secondary name
	 *
	 * @param key
	 *            the key
	 * @return the name by index
	 * @throws Exception
	 *             the exception
	 */
	public int getNameByIndex(final String key) throws Exception {
		if (null != this.localeMapping.get(key)) {
			return Integer.parseInt(this.localeMapping.get(key));
		} else {
			return 0;
		}
	}

	/**
	 * Checks if is wildcard.
	 *
	 * @param keyword
	 *            the keyword
	 * @return true, if is wildcard
	 */
	public boolean isWildcard(final String keyword) {
		Pattern p = Pattern.compile(CommonConstants.PATTERN_SPECIAL_CHARACTERS);
		Matcher m = p.matcher(keyword.replace(CommonConstants.SPACE, CommonConstants.EMPTY_STRING));
		if (m.find()) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isSpecialChar(final String str) {
		Matcher m = this.pattern.matcher(str);
		return m.find();
	}

	@SuppressWarnings("unchecked")
	private List<PredSrchResponse> convertToResponse(final List<ResponseInfo> resList) throws Exception {
		if (ListUtil.isValid(resList)) {
			List<PredSrchResponse> list = new ArrayList<PredSrchResponse>();
			int size = resList.size();
			for (int i = 0; size > i; i++) {
				PredSrchResponse response = new PredSrchResponse();
				ResponseInfo info = resList.get(i);
				response.setAllowBuy(info.getAllowBuy());
				response.setAllowSell(info.getAllowSell());
				response.setAssetCountries(info.getAssetCountrys());
				response.setAssetSectors(info.getAssetSectors());
				response.setCountryTradableCode(info.getCountryTradableCode());
				response.setExchange(info.getExchange());
				response.setParentAssetClasses(info.getParentAssetClasses());
				response.setProdAltNumSegs(info.getProdAltNumSegs());
				response.setProductType(info.getProductType());
				response.setSymbol(info.getSymbol());
				response.setProductName(info.getProductName());
				response.setProductShortName(info.getProductShortName());
				response.setProductSecondName(info.getProductSecondName());
				response.setProductSubType(info.getProductSubType());
				response.setProductCcy(info.getProductCcy());
				response.setMarket(info.getMarket());
				response.setProductCode(info.getProductCode());
				response.setFundHouseCde(info.getFundHouseCde());
				response.setBondIssuer(info.getIsrBndNme());
				response.setAllowSellMipProdInd(info.getAllowSellMipProdInd());
				response.setAllowTradeProdInd(info.getAllowTradeProdInd());
				response.setProdTaxFreeWrapActStaCde(info.getProdTaxFreeWrapActStaCde());
				response.setAllowSwInProdInd(info.getAllowSwInProdInd());
				response.setAllowSwOutProdInd(info.getAllowSwOutProdInd());
				response.setSwithableGroups(info.getSwithableGroups());
				response.setRiskLvlCde(info.getRiskLvlCde());
				response.setFundUnSwitchCode(info.getFundUnswCdes());
				response.setChannelRestrictList(info.getChannelRestrictGroup());
				response.setChanlCdeList(info.getChanlCdeList());
				response.setProdStatCde(info.getProdStatCde());
				response.setRestrOnlScribInd(info.getRestrOnlScribInd());
				response.setPiFundInd(info.getPiFundInd());
				response.setDeAuthFundInd(info.getDeAuthFundInd());
				list.add(response);
			}
			return list;
		} else {
			LogUtil.error(PredictiveSearchServiceImpl.class, "No record found");
			return ListUtils.EMPTY_LIST;
		}
	}

	private void updateAllowBuySellInd(final SearchableObject obj, final ResponseInfo info, final String chanlRestCde) {
		List<String> allowBuyList = obj.getChanlAllowBuy();
		List<String> sellSellList = obj.getChanlAllowSell();

		if (StringUtil.isValid(chanlRestCde) && ListUtil.isValid(allowBuyList) && ListUtil.isValid(sellSellList)) {
			for (int i = 0; i < allowBuyList.size(); i = i + 2) {
				if (allowBuyList.get(i).equals(chanlRestCde)) {
					info.setAllowBuy(allowBuyList.get(i + 1));
					break;
				}
			}
			for (int i = 0; i < sellSellList.size(); i = i + 2) {
				if (sellSellList.get(i).equals(chanlRestCde)) {
					info.setAllowSell(sellSellList.get(i + 1));
					break;
				}
			}
		}
		if (StringUtil.isInvalid(info.getAllowBuy()) || StringUtil.isInvalid(info.getAllowSell())) {
			info.setAllowBuy(obj.getAllowBuy());
			info.setAllowSell(obj.getAllowSell());
		}
	}

}
