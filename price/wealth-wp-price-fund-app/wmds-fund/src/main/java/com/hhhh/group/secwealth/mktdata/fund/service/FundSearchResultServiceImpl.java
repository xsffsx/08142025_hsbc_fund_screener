
package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.common.convertor.RequestConverter;
import com.hhhh.group.secwealth.mktdata.common.criteria.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.common.criteria.SortCriteriaValue;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.*;
import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.predsrch.request.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.common.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.common.system.constants.*;
import com.hhhh.group.secwealth.mktdata.common.util.*;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMdsbeService;
import com.hhhh.group.secwealth.mktdata.fund.beans.helper.UtSvceHelper;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundSearchResultRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.FundSearchResultResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.common.Pagination;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundsearchresult.*;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundsearchresult.FundSearchResultProduct.*;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundsearchresult.FundSearchResultProduct.Holdings.*;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundsearchresult.FundSearchResultProduct.Performance.AnnualizedReturns;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundsearchresult.FundSearchResultProduct.Performance.CalendarReturns;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundsearchresult.FundSearchRisk.YearRisk;
import com.hhhh.group.secwealth.mktdata.fund.converter.BaseValidateConverter;
import com.hhhh.group.secwealth.mktdata.fund.criteria.Constants;
import com.hhhh.group.secwealth.mktdata.fund.criteria.util.Operator;
import com.hhhh.group.secwealth.mktdata.fund.dao.FundSearchResultDao;
import com.hhhh.group.secwealth.mktdata.fund.dao.impl.FundSearchResultDaoImpl;
import com.hhhh.group.secwealth.mktdata.fund.util.FundSearchResultUtil;
import com.hhhh.group.secwealth.mktdata.fund.webservice.prodkeylistwitheligcheck.json.ProductInfo;
import com.hhhh.group.secwealth.mktdata.fund.webservice.prodkeylistwitheligcheck.service.ProductKeyListWithEligibilityCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;

@Service("fundSearchResultService")
public class FundSearchResultServiceImpl extends AbstractMdsbeService {

    private static final String SWITCHOUT = "SWITCHOUT";
    private static final String PREDSRCH = "PREDSRCH";
    private static final String DB = "DB";
    private static final String GB_HBEU_DB = "GB_HBEU_DB";
    private static final String DEFAULT_DB = "DEFAULT_DB";
    private static final String WEBSERVICE = "WEBSERVICE";
    private static final String hhhh_BEST_SELLER = "hhhh_BEST_SELLER";
    private static final String hhhh_RETIREMENT_FUND = "hhhh_RETIREMENT_FUND";
    private static final String hhhh_NEW_FUND = "hhhh_NEW_FUND";
    private static final String hhhh_FUND_OF_QUARTER = "hhhh_FUND_OF_QUARTER";
    private static final String hhhh_TOP5_PERFORMERS = "hhhh_TOP5_PERFORMERS";
    private static final String DEFAULT = "DEFAULT";
    private static final String SITE_FEATURE_TOTALRETURNS = ".totalReturns";
    private static final String SITE_FEATURE_TOTALRETURNS_DAILY = "daily";

    @Value("#{systemConfig['holdingsReturnList']}")
    private Integer holdingsReturnList;

    @Autowired
    @Qualifier("baseValidateConverter")
    private BaseValidateConverter baseValidateConverter;

    @Resource(name = "tradableCurrencyProdUsingMap")
    private List<String> tradableCurrencyProdUsingMap;

    @Autowired
    @Qualifier("internalProductKeyUtil")
    private InternalProductKeyUtil internalProductKeyUtil;

    @Autowired
    @Qualifier("localeMappingUtil")
    private LocaleMappingUtil localeMappingUtil;

    @Resource(name = "switchOutCaseMap")
    private Map<String, String> switchOutCaseMap;

    @Resource(name = "newFundBestSellerMap")
    private Map<String, String> newFundBestSellerMap;

    @Resource(name = "newFundBestSellerCaseMap")
    private Map<String, String> newFundBestSellerCaseMap;

    @Resource(name = "holdingAllocationMap")
    private Map<String, String> holdingAllocationMap;

    @Value("#{systemConfig['fundSearch.limit.record']}")
    private String limitRecord;

//    @Autowired
//    @Qualifier("predictiveSearchService")
//    private PredictiveSearchService predictiveSearchService;

    @Autowired
    @Qualifier("fundSearchResultDao")
    private FundSearchResultDao fundSearchResultDao;

    @Autowired
    @Qualifier("requestConverter")
    private RequestConverter requestConverter;

    @Autowired
    @Qualifier("siteFeature")
    private SiteFeature siteFeature;

    @Autowired
    @Qualifier("productKeyListWithEligibilityCheckService")
    private ProductKeyListWithEligibilityCheckService productKeyListWithEligibilityCheckService;

    @Override
    public Object execute(final Object object) throws Exception {
        LogUtil.debug(FundSearchResultServiceImpl.class, "Enter into the FundSearchResultServiceImpl");
        long startTimeTotal = System.currentTimeMillis();
        // Prepare Request
        FundSearchResultRequest request = (FundSearchResultRequest) object;
        Date currentDate = new Date();
        String locale = request.getLocale();
        String countryCode = request.getCountryCode();
        String groupMember = request.getGroupMember();
        String productType = request.getProductType();
        List<SearchCriteria> detailedCriterias = request.getDetailedCriterias();
        FundSearchResultResponse response = null;

        Map<String, List<Integer>> switchOutFundMap = new HashMap<String, List<Integer>>();
        List<Integer> prodIds_switchOutFund = new ArrayList<Integer>();
        if (ListUtil.isValid(detailedCriterias)) {
            for (SearchCriteria sc : detailedCriterias) {
                if (sc.getCriteriaKey().equalsIgnoreCase("switchOutFund")) {
                    switchOutFundMap.put("switchOutFund", prodIds_switchOutFund);
                    break;
                }
            }
        }
        Integer numberOfRecords = request.getNumberOfRecords();
        Integer limitRecordCount =  Integer.valueOf(limitRecord);
        if( (numberOfRecords != null && limitRecordCount != null && numberOfRecords > limitRecordCount)
            || (null != request.getProductKeys() && request.getProductKeys().size() > limitRecordCount)
        ) {
            LogUtil.error(FundSearchResultDaoImpl.class,"numberOfRecords = " +numberOfRecords +" limitRecordCount = "+ limitRecordCount);
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
        }
        // validate
        this.baseValidateConverter.validateForSearchCriteria(detailedCriterias, countryCode, groupMember);
        this.baseValidateConverter.validateForSearchRangeCriteriaValue(request.getRangeCriterias(), countryCode, groupMember);

        List<com.hhhh.group.secwealth.mktdata.common.criteria.Holdings> holdingsList = request.getHoldings();
        Map<String, Boolean> holdingsValueMap = new HashMap<String, Boolean>();
        Map<String, Integer> holdingsTopMap = new HashMap<String, Integer>();
        Map<String, Boolean> holdingsOthersMap = new HashMap<String, Boolean>();
        if (ListUtil.isValid(holdingsList)) {
            for (com.hhhh.group.secwealth.mktdata.common.criteria.Holdings holdings : holdingsList) {
                if (StringUtil.isValid(holdings.getCriteriaKey())) {
                    String keyMap = this.holdingAllocationMap.get(holdings.getCriteriaKey().toUpperCase());
                    if (StringUtil.isValid(keyMap)) {
                        holdingsValueMap.put(keyMap, holdings.getCriteriaValue());
                        holdingsTopMap.put(keyMap, holdings.getTop());
                        holdingsOthersMap.put(keyMap, holdings.getOthers());
                    } else {
                        holdingsValueMap.put(holdings.getCriteriaKey(), holdings.getCriteriaValue());
                        holdingsTopMap.put(holdings.getCriteriaKey(), holdings.getTop());
                        holdingsOthersMap.put(holdings.getCriteriaKey(), holdings.getOthers());
                    }
                }
            }
        }

        List<SearchCriteria> searchCriteriaList = new ArrayList<SearchCriteria>();

        boolean flag = false;
        int catLevel = 0;
        List<SortCriteriaValue> sortList = request.getSortCriterias();

        String productSubType = null;
        if (ListUtil.isValid(detailedCriterias)) {
            for (SearchCriteria criteria : detailedCriterias) {
                if (FundSearchResultServiceImpl.hhhh_BEST_SELLER.equals(criteria.getCriteriaKey().toUpperCase())
                    && CommonConstants.YES.equals(criteria.getCriteriaValue().toUpperCase())) {
                    searchCriteriaList.add(criteria);
                    if (ListUtil.isInvalid(sortList)) {
                        flag = true;
                    }
                } else if (FundSearchResultServiceImpl.hhhh_NEW_FUND.equals(criteria.getCriteriaKey().toUpperCase())
                    && CommonConstants.YES.equals(criteria.getCriteriaValue().toUpperCase())) {
                    searchCriteriaList.add(criteria);
                } else if (FundSearchResultServiceImpl.hhhh_RETIREMENT_FUND.equals(criteria.getCriteriaKey().toUpperCase())
                        && CommonConstants.YES.equals(criteria.getCriteriaValue().toUpperCase())) {
                        searchCriteriaList.add(criteria);
                } else if (FundSearchResultServiceImpl.hhhh_FUND_OF_QUARTER.equals(criteria.getCriteriaKey().toUpperCase())
                    && CommonConstants.YES.equals(criteria.getCriteriaValue().toUpperCase())) {
                    searchCriteriaList.add(criteria);
                } else if (FundSearchResultServiceImpl.hhhh_TOP5_PERFORMERS.equalsIgnoreCase(criteria.getCriteriaKey())) {
                    searchCriteriaList.add(criteria);
                    if (ListUtil.isInvalid(sortList)) {
                        flag = true;
                        catLevel = 1;
                    }
                } else if (QueryExpressionHandler.PRODSUBTYPE_FUND.equalsIgnoreCase(criteria.getCriteriaKey())) {
                    productSubType = criteria.getCriteriaValue();
                }
            }
        }

        List<ProductKey> prodIds_wpcWebService = null;
        if (ListUtil.isValid(searchCriteriaList)) {
            for (SearchCriteria criteria : searchCriteriaList) {
                String criteriaKey = criteria.getCriteriaKey().substring(5);
                String criteriaValue = this.newFundBestSellerCaseMap.get(criteriaKey);
                String key = request.getSiteKey().toUpperCase() + CommonConstants.SYMBOL_UNDERLINE + criteriaKey;
                String value = this.newFundBestSellerMap.get(key);
                if (StringUtil.isInvalid(value)) {
                    value = this.newFundBestSellerMap
                        .get(FundSearchResultServiceImpl.DEFAULT + CommonConstants.SYMBOL_UNDERLINE + criteriaKey);
                }
                switch (value) {
                case DB:// get data from DB
                    prodIds_wpcWebService = this.fundSearchResultDao.getProdAltNumFromDB(request, criteriaValue);
                    break;
                case WEBSERVICE:// get data by webService
                    prodIds_wpcWebService = getProdAltNumFromWebservicce(this.productKeyListWithEligibilityCheckService
                        .getProductInfoList(locale, countryCode, groupMember, productType, productSubType, criteriaValue, null));
                    break;
                default:
                    break;
                }
            }
        }

        if (ListUtil.isValid(searchCriteriaList)) {
            detailedCriterias.removeAll(searchCriteriaList);
            request.setDetailedCriterias(detailedCriterias);
        }

        prodIds_switchOutFund = getProductIdForSwitchOutFund(request);
        if (ListUtil.isValid(prodIds_switchOutFund)) {
            for (Entry<String, List<Integer>> map : switchOutFundMap.entrySet()) {
                if (map.getKey().equalsIgnoreCase("switchOutFund")) {
                    switchOutFundMap.put(map.getKey(), prodIds_switchOutFund);
                    break;
                }
            }
        }
        // check whether hhhh Risk Level Rating criterion available
        // as a criteria like utProd.riskLvlCde in('','')
        List<String> hhhhRiskLevlList = checkhhhhRiskLevelRatingCriterionAvailable(request);

        response = new FundSearchResultResponse();

        long startTime1 = System.currentTimeMillis();
        Integer totalNumberOfRecords = this.fundSearchResultDao.searchTotalCount(request, switchOutFundMap, hhhhRiskLevlList,
            prodIds_wpcWebService, countryCode, groupMember, holdingsValueMap, flag, catLevel);
        long endTime1 = System.currentTimeMillis();
        LogUtil.debug(FundSearchResultServiceImpl.class, "[SearchFund] count Time : " + Long.toString(endTime1 - startTime1));

        if (request.getReturnOnlyNumberOfMatches()) {
            Pagination pagination = new Pagination();
            pagination.setTotalNumberOfRecords(totalNumberOfRecords);
            response.setPagination(pagination);
            return response;
        }

        // get data from the main table,if the result is null, than return null
        long startTime0 = System.currentTimeMillis();
        List<UtProdInstm> utProdInstmList = this.fundSearchResultDao.searchFund(request, switchOutFundMap, hhhhRiskLevlList,
            prodIds_wpcWebService, countryCode, groupMember, holdingsValueMap, flag, catLevel);
        long endTime0 = System.currentTimeMillis();
        LogUtil.debug(FundSearchResultServiceImpl.class, "[SearchFund] cast Time : " + Long.toString(endTime0 - startTime0));

        // get the prodIds and performanceIds as criteria for other data search
        List<Integer> prodIds_DB = new ArrayList<Integer>();
        List<String> performanceIds_DB = new ArrayList<String>();
        if (ListUtil.isValid(utProdInstmList)) {
            for (UtProdInstm utProdInstm : utProdInstmList) {
                // add prodId
                UtProdInstmId utProdInstmId = utProdInstm.getUtProdInstmId();
                if (null != utProdInstmId) {
                    Integer prodId = utProdInstmId.getProductId();
                    if (null != prodId && !prodIds_DB.contains(prodId)) {
                        prodIds_DB.add(utProdInstmId.getProductId());
                    }
                }
                // add performanceId
                String performanceId = utProdInstm.getPerformanceId();
                if (StringUtil.isValid(performanceId) && !performanceIds_DB.contains(performanceId)) {
                    performanceIds_DB.add(utProdInstm.getPerformanceId());
                }
            }
            if (ListUtil.isInvalid(prodIds_DB) || ListUtil.isInvalid(performanceIds_DB)) {
                LogUtil.error(FundSearchResultServiceImpl.class,
                    "prodIds_DB's size = " + prodIds_DB.size() + " performanceIds_DB's size = " + performanceIds_DB.size());
                throw new CommonException(PredictiveSearchConstant.ERRORMSG_NORECORDFOUND);
            }
        }
        Map<String, String> headers = request.getHeaders();
        String cmbInd = headers.get(CommonConstants.REQUEST_HEADER_GBGF);
        String channelRestrictCode = FundSearchResultUtil.buildSearchResultChannelRestrictCode(request,cmbInd);

        Map<Integer, List<String>> utProdChanl = this.fundSearchResultDao.searchChanlFund(channelRestrictCode);
        // long startTime2 = System.currentTimeMillis();
        long startTimeGic = System.currentTimeMillis();
        Map<Integer, List<UtCatAsetAlloc>> sicHldgsMap = this.fundSearchResultDao.searchHldgsMap(prodIds_DB, "SIC_ALLOC");
        long endTimeGic = System.currentTimeMillis();
        LogUtil.debug(FundSearchResultServiceImpl.class, "[hldg] Gic Time : " + Long.toString(endTimeGic - startTimeGic));

        long startTimeGeo = System.currentTimeMillis();
        Map<Integer, List<UtCatAsetAlloc>> geoHldgsMap = this.fundSearchResultDao.searchHldgsMap(prodIds_DB, "GEO_LOC");
        long endTimeGeo = System.currentTimeMillis();
        LogUtil.debug(FundSearchResultServiceImpl.class, "[hldg] Geo Time : " + Long.toString(endTimeGeo - startTimeGeo));

        long startTimeFh = System.currentTimeMillis();
        Map<String, List<UtHoldings>> fundHoldingMap =
            this.fundSearchResultDao.searchTop5HldgMap(performanceIds_DB, holdingsValueMap);
        long endTimeFh = System.currentTimeMillis();
        LogUtil.debug(FundSearchResultServiceImpl.class, "[hldg] Fh Time : " + Long.toString(endTimeFh - startTimeFh));

        long startTimeUts = System.currentTimeMillis();
        List<UtSvceHelper> utSvceHelperList = this.fundSearchResultDao.getUtSvce(performanceIds_DB);
        long endTimeUts = System.currentTimeMillis();
        LogUtil.debug(FundSearchResultServiceImpl.class, "[hldg] Uts Time : " + Long.toString(endTimeUts - startTimeUts));

        long startTimeAlloc = System.currentTimeMillis();
        Map<String, List<UTHoldingAlloc>> holdingAllocMap =
            this.fundSearchResultDao.searchHoldingAllocation(performanceIds_DB, holdingsValueMap);
        long endTimeAlloc = System.currentTimeMillis();
        LogUtil.debug(FundSearchResultServiceImpl.class, "[hldg] Uts Time : " + Long.toString(endTimeAlloc - startTimeAlloc));

        long startTime3 = System.currentTimeMillis();
        Pagination pagination = new Pagination();
        if (null != request.getStartDetail() && request.getStartDetail() > 0) {
            pagination.setStartDetail(request.getStartDetail());
        }
        if (ListUtil.isValid(utProdInstmList) && null != request.getStartDetail()) {
            pagination.setEndDetail(utProdInstmList.size() + request.getStartDetail() - 1);
        }
        if (ListUtil.isValid(utProdInstmList)) {
            pagination.setNumberOfRecords(utProdInstmList.size());
        }
        pagination.setTotalNumberOfRecords(totalNumberOfRecords);

        mergeResponseFromeDB(response, utProdInstmList, utProdChanl, request, sicHldgsMap, geoHldgsMap, fundHoldingMap,
            utSvceHelperList, holdingAllocMap, holdingsValueMap, holdingsTopMap, holdingsOthersMap, currentDate);

        String timeZoneStr = request.getEntityTimezone();
        TimeZone timeZone = null;
        if (StringUtil.isValid(timeZoneStr)) {
            timeZone = TimeZone.getTimeZone(timeZoneStr);
            if (null != timeZone) {
                String newDateStr = DateUtil.newDate2String(timeZone, DateConstants.DateFormat_yyyyMMddHHmmss);
                String iso8601Time = DateUtil.stringToISO8601DateString(newDateStr, DateConstants.DateFormat_yyyyMMddHHmmss,
                    DateConstants.DateFormat_yyyyMMddHHmmssSSSZ_ISO8601, timeZone);
                response.setEntityUpdatedTime(iso8601Time);
            }
        }
        response.setPagination(pagination);
        long endTime3 = System.currentTimeMillis();
        LogUtil.debug(FundSearchResultServiceImpl.class, "[SearchFund] code Time : " + (endTime3 - startTime3));
        long endTimeTotal = System.currentTimeMillis();
        LogUtil.debug(FundSearchResultServiceImpl.class, "[over] Total time : " + (endTimeTotal - startTimeTotal));
        return response;
    }

    // get product id For SwitchOutFund and remove Criteria which key =
    // switchOutFund
    public List<Integer> getProductIdForSwitchOutFund(final FundSearchResultRequest request) throws Exception {
        List<Integer> prodId_switchOut = new ArrayList<Integer>();
        List<SearchCriteria> detailedCriterias = request.getDetailedCriterias();
        if (ListUtil.isValid(detailedCriterias)) {
            List<SearchCriteria> removeCriterias = new ArrayList<SearchCriteria>();
            for (SearchCriteria criteria : detailedCriterias) {
                if (QueryExpressionHandler.SWITCHOUT_FUND.equalsIgnoreCase(criteria.getCriteriaKey())) {
                    removeCriterias.add(criteria);
                    LogUtil.debug(FundSearchResultServiceImpl.class,
                        "search W-Code from wpc, the criteria is: " + criteria.toString());
                    // prodAltCode|Symbol|countryTradableCode|productType
                    String[] fields = criteria.getCriteriaValue().split(CommonConstants.SYMBOL_COLON);
                    if (fields.length != 4) {
                        LogUtil.error(FundSearchResultServiceImpl.class,
                            "criteria value converter to array is falil, array lecgth is : " + fields.length);
                        throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
                    }

                    String key = request.getCountryCode().toUpperCase() + CommonConstants.SYMBOL_UNDERLINE
                        + request.getGroupMember().toUpperCase() + CommonConstants.SYMBOL_UNDERLINE
                        + FundSearchResultServiceImpl.SWITCHOUT;
                    String value = this.switchOutCaseMap.get(key);
                    if (StringUtil.isInvalid(value)) {
                        value = this.switchOutCaseMap.get(FundSearchResultServiceImpl.DEFAULT + CommonConstants.SYMBOL_UNDERLINE
                            + FundSearchResultServiceImpl.SWITCHOUT);
                    }
                    switch (value) {
                    case PREDSRCH:// get data by predSrch
                    {
                        prodId_switchOut = getSwitchOutFundProductIdFromPredSearch(prodId_switchOut, criteria, fields,
                            request.getHeaders(), request.getChannelRestrictCode());
                        break;
                    }
                    case DEFAULT_DB:// get data from DB (Core Solution)
                    {
                        prodId_switchOut = this.fundSearchResultDao.appendSqlForCoreSwitchOut(fields,
                            request.getChannelRestrictCode(), request.getRestrOnlScribInd(), request.getHeaders());
                        break;
                    }
                    case GB_HBEU_DB:// get data from DB for UK(GB_HBEU)
                    {
                        prodId_switchOut = this.fundSearchResultDao.appendSqlForUKSwitchOut(fields, request.getHeaders());
                        break;
                    }
                    case WEBSERVICE:// get data by webService
                    {

                        List<PredSrchResponse> PredSrchList = this.getProductFromPredSearch(fields[1], fields[2],
                            new String[] {fields[3]}, request.getHeaders());
                        if (ListUtil.isValid(PredSrchList)) {
                            PredSrchResponse predSrch = PredSrchList.get(0);

                            List<ProductInfo> productList = this.searchFundBySwithableGroups(predSrch.getSwithableGroups(),
                                request.getProductType(), null, request.getCountryCode(), request.getGroupMember(),
                                request.getLocale());
                            
                            
                            if (ListUtil.isValid(productList)) {
                                if (predSrch.getFundUnSwitchCode() != null && predSrch.getFundUnSwitchCode().length > 0) {
                                	 List<String> blackList = Arrays.asList(predSrch.getFundUnSwitchCode());
                                    for (ProductInfo info : productList) {
                                        info.getPrdAltNum();
                                    }
                                } else {
                                }
                            }
                        }
                        break;
                    }
                    default:
                        break;
                    }

                    if (ListUtil.isValid(prodId_switchOut)) {
                        LogUtil.debug(FundSearchResultServiceImpl.class,
                            "SearchFund Can SwitchOut product total is :" + prodId_switchOut.size());
                    } else {
                        LogUtil.debug(FundSearchResultServiceImpl.class, "SearchFund No Product Can SwitchOut");
                    }
                }
            }
            if (ListUtil.isValid(removeCriterias)) {
                detailedCriterias.removeAll(removeCriterias);
            }
        }
        return prodId_switchOut;
    }

    protected List<String> checkhhhhRiskLevelRatingCriterionAvailable(final FundSearchResultRequest request) throws Exception {
        List<String> result = null;
        List<SearchCriteria> detailedCriterias = request.getDetailedCriterias();
        if (ListUtil.isInvalid(detailedCriterias)) {
            return result;
        }
        boolean isCriterionAvailable = false;
        SearchCriteria itemForHRR = null;
        for (SearchCriteria searchCriteria : detailedCriterias) {
            if (Constants.CRITERION_REQUEST_hhhh_RISK_LEVEL_RATING.equals(searchCriteria.getCriteriaKey())) {
                isCriterionAvailable = true;
                itemForHRR = searchCriteria;
                result = parsehhhhRiskLevelRatingCriterion(searchCriteria);
                break;
            }
        }
        if (isCriterionAvailable) {
            // remove that criterion item for fund search
            detailedCriterias.remove(itemForHRR);
        }
        return result;
    }

    protected List<String> parsehhhhRiskLevelRatingCriterion(final SearchCriteria item) throws Exception {
        if (item == null) {
            LogUtil.error(FundSearchResultServiceImpl.class, "Criteria parsing error missing criteria item!!!");
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
        }
        if (!Constants.CRITERION_REQUEST_hhhh_RISK_LEVEL_RATING.equals(item.getCriteriaKey())) {
            LogUtil.error(FundSearchResultServiceImpl.class, "Criteria parsing error Parameter [" + item.getCriteriaKey()
                + "] not matching with [" + Constants.CRITERION_REQUEST_hhhh_RISK_LEVEL_RATING + "]");
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
        }
        if (!Operator.EQ.toString().equalsIgnoreCase(item.getOperator()) && !"IN".equalsIgnoreCase(item.getOperator())) {
            LogUtil.error(FundSearchResultServiceImpl.class, "Criteria parsing error Operator [" + item.getOperator()
                + "] not supported. Only '=' & 'in' can be supported for this");
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
        }
        if (StringUtil.isInvalid(item.getCriteriaValue())) {
            LogUtil.error(FundSearchResultServiceImpl.class,
                "Criteria parsing error Blank Criteria Value for " + item.getCriteriaKey() + "detected!");
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
        }
        if (Operator.EQ.toString().equalsIgnoreCase(item.getOperator())) {
            // parsing when operator is 'eq'
            return Arrays.asList(new String[] {item.getCriteriaValue()});
        } else {
            // parsing when operator is 'in'
            return Arrays.asList(item.getCriteriaValue().split(Constants.PRODUCT_KEY_SEPARATOR));
        }
    }

    public void removeValIsNullAndDistinct(final List<String> idList) {
        if (ListUtil.isValid(idList)) {
            List<String> newIdList = new ArrayList<String>();
            for (String id : idList) {
                if (null != id || StringUtil.isValid(id)) {
                    if (!newIdList.contains(id)) {
                        newIdList.add(id);
                    }
                }
            }
        }
    }

    private String getTradableCurrencyCode(final UtProdInstm utProdInstm, final String usingProductKey) {
        if (this.tradableCurrencyProdUsingMap.contains(usingProductKey)) {
            return utProdInstm.getCcyProdTradeCde();
        }
        return utProdInstm.getCcyInvstCde();
    }

    private static void sortHolding_sicHldgs(final List<UtCatAsetAlloc> utCatAsetAllocList) {
        Collections.sort(utCatAsetAllocList, new Comparator<UtCatAsetAlloc>() {
            @Override
            public int compare(final UtCatAsetAlloc utCatAsetAlloc1, final UtCatAsetAlloc utCatAsetAlloc2) {

                if (utCatAsetAlloc1 == null || utCatAsetAlloc1.getFundAllocation() == null) {
                    return 1;
                } else if (utCatAsetAlloc2 == null || utCatAsetAlloc2.getFundAllocation() == null) {
                    return -1;
                } else {
                    return utCatAsetAlloc2.getFundAllocation().compareTo(utCatAsetAlloc1.getFundAllocation());
                }
            }
        });
    }

    private static void sortHolding_geoHldgs(final List<UtCatAsetAlloc> utCatAsetAllocList) {
        Collections.sort(utCatAsetAllocList, new Comparator<UtCatAsetAlloc>() {
            @Override
            public int compare(final UtCatAsetAlloc utCatAsetAlloc1, final UtCatAsetAlloc utCatAsetAlloc2) {

                if (utCatAsetAlloc1 == null || utCatAsetAlloc1.getCategoryAllocation() == null) {
                    return 1;
                } else if (utCatAsetAlloc2 == null || utCatAsetAlloc2.getCategoryAllocation() == null) {
                    return -1;
                } else {
                    return utCatAsetAlloc2.getCategoryAllocation().compareTo(utCatAsetAlloc1.getCategoryAllocation());
                }
            }
        });
    }

    private List<TopHoldingsSearch> setTopHoldingsList(final UtProdInstm utProdInstm,
        final Map<String, List<UtHoldings>> fundHoldingMap, List<TopHoldingsSearch> topHoldings,
        final Map<String, Boolean> holdingsValueMap, final Map<String, Integer> holdingsTopMap,
        final Map<String, Boolean> holdingsOthersMap, final String classType) throws ParseException {
        if (null != fundHoldingMap) {

            int i = 0;
            int top = 0;
            boolean flag = true;
            boolean others = false;
            BigDecimal percentSum = new BigDecimal(0);
            if (!holdingsTopMap.isEmpty() && null != holdingsTopMap.get(classType)) {
                top = holdingsTopMap.get(classType);
            } else {
                top = this.holdingsReturnList;
            }
            if (!holdingsOthersMap.isEmpty() && null != holdingsOthersMap.get(classType)) {
                others = holdingsOthersMap.get(classType);
            }

            List<UtHoldings> fundHoldingList = fundHoldingMap.get(utProdInstm.getPerformanceId());
            if (ListUtil.isValid(fundHoldingList)) {
                for (UtHoldings utHoldings : fundHoldingList) {
                    TopHoldingsSearch topHoldingsItem = new TopHoldingsSearch();
                    topHoldingsItem.setHoldingName(utHoldings.getUtHoldingsId().getHoldingId().toString());
                    topHoldingsItem.setHoldingCompany(utHoldings.getUtHoldingsId().getHolderName());
                    topHoldingsItem.setHoldingPercent(utHoldings.getWeight());
                    topHoldings.add(topHoldingsItem);
                    if (flag) {
                        if (null != utHoldings.getWeight()) {
                            percentSum = percentSum.add(utHoldings.getWeight());
                        }
                        ++i;
                        if (i == top) {
                            flag = false;
                        }
                    }
                }
            }

            if (ListUtil.isValid(topHoldings)) {
                TopHoldingsSearch topHoldingsOther = new TopHoldingsSearch();
                if (top > topHoldings.size()) {
                    top = topHoldings.size();
                }
                topHoldingsOther.setHoldingName(String.valueOf(top + 1));
                topHoldingsOther.setHoldingCompany("Others");
                topHoldingsOther.setHoldingPercent(new BigDecimal(100).subtract(percentSum));
                topHoldings.add(topHoldingsOther);
                topHoldings.set(top, topHoldingsOther);
                if (others && top < 10 && top < topHoldings.size()) {
                    topHoldings = topHoldings.subList(0, top + 1 < topHoldings.size() ? top + 1 : topHoldings.size());
                } else {
                    topHoldings = topHoldings.subList(0, top < topHoldings.size() ? top : topHoldings.size());
                }
            }
        }
        return topHoldings;
    }

    private AssetAllocations setAssetAllocation(final UtProdInstm utProdInstm,
        final Map<String, List<UTHoldingAlloc>> holdingAllocMap, final AssetAllocations assetAllocations, final String classType)
        throws ParseException {
        if (null != holdingAllocMap) {
            List<UTHoldingAlloc> assetAllocList = holdingAllocMap.get(utProdInstm.getPerformanceId());
            List<HoldingAllocation> assetAllocationsList = new ArrayList<HoldingAllocation>();
            if (ListUtil.isValid(assetAllocList)) {
                for (UTHoldingAlloc assetAlloc : assetAllocList) {
                    if (assetAlloc.getUtHoldingAllocId().getHoldingAllocClassType().equals(classType)) {
                        HoldingAllocation holdingAllocation = new HoldingAllocation();
                        holdingAllocation.setName(assetAlloc.getUtHoldingAllocId().getHoldingAllocClassName());
                        holdingAllocation.setWeighting(assetAlloc.getHoldingAllocWeightNet());
                        assetAllocationsList.add(holdingAllocation);
                        assetAllocations.setPortfolioDate(DateUtil.getSimpleDateFormat(assetAlloc.getPortfolioDate(),
                            DateConstants.DateFormat_yyyyMMdd_withHyphen));
                    }
                }
            }
            assetAllocations.setAssetAllocations(assetAllocationsList);
        }
        return assetAllocations;
    }

    private GlobalStockSectors setGlobalStockSectors(final UtProdInstm utProdInstm,
        final Map<String, List<UTHoldingAlloc>> holdingAllocMap, final GlobalStockSectors globalStockSectors,
        final Map<String, Integer> holdingsTopMap, final Map<String, Boolean> holdingsOthersMap, final String classType) {
        if (null != holdingAllocMap) {

            int i = 0;
            int top = 0;
            boolean others = true;

            if (!holdingsTopMap.isEmpty() && null != holdingsTopMap.get(classType)) {
                top = holdingsTopMap.get(classType);
            } else {
                top = this.holdingsReturnList;
            }
            if (!holdingsOthersMap.isEmpty() && null != holdingsOthersMap.get(classType)) {
                others = holdingsOthersMap.get(classType);
            }

            List<UTHoldingAlloc> stockSectorsList = holdingAllocMap.get(utProdInstm.getPerformanceId());
            List<HoldingAllocation> globalStockSectorsList = new ArrayList<HoldingAllocation>();
            if (ListUtil.isValid(stockSectorsList)) {
                for (UTHoldingAlloc stockSectors : stockSectorsList) {
                    if (stockSectors.getUtHoldingAllocId().getHoldingAllocClassType().equals(classType)) {
                        HoldingAllocation holdingAllocation = new HoldingAllocation();
                        holdingAllocation.setName(stockSectors.getUtHoldingAllocId().getHoldingAllocClassName());
                        holdingAllocation.setWeighting(stockSectors.getHoldingAllocWeightNet());
                        globalStockSectorsList.add(holdingAllocation);
                        globalStockSectors.setPortfolioDate(DateUtil.getSimpleDateFormat(stockSectors.getPortfolioDate(),
                            DateConstants.DateFormat_yyyyMMdd_withHyphen));
                    }
                }
            }
            sort_topholdingAlloc(globalStockSectorsList);
            updateOthersData(globalStockSectorsList, i, top);

            if (ListUtil.isValid(globalStockSectorsList)) {
                if (others) {
                    globalStockSectors.setGlobalStockSectors(globalStockSectorsList.subList(0,
                        top + 1 < globalStockSectorsList.size() ? top + 1 : globalStockSectorsList.size()));
                } else {
                    globalStockSectors.setGlobalStockSectors(globalStockSectorsList.subList(0,
                        top < globalStockSectorsList.size() ? top : globalStockSectorsList.size()));
                }
            }
        }
        return globalStockSectors;
    }

    private RegionalExposures setRegionalExposures(final UtProdInstm utProdInstm,
        final Map<String, List<UTHoldingAlloc>> holdingAllocMap, final RegionalExposures equityRegional,
        final Map<String, Integer> holdingsTopMap, final Map<String, Boolean> holdingsOthersMap, final String classType) {
        if (null != holdingAllocMap) {

            int i = 0;
            int top = 0;
            boolean others = true;

            if (!holdingsTopMap.isEmpty() && null != holdingsTopMap.get(classType)) {
                top = holdingsTopMap.get(classType);
            } else {
                top = this.holdingsReturnList;
            }
            if (!holdingsOthersMap.isEmpty() && null != holdingsOthersMap.get(classType)) {
                others = holdingsOthersMap.get(classType);
            }
            List<UTHoldingAlloc> equityRegionalList = holdingAllocMap.get(utProdInstm.getPerformanceId());
            List<HoldingAllocation> RegionalExposuresList = new ArrayList<HoldingAllocation>();
            if (ListUtil.isValid(equityRegionalList)) {
                for (UTHoldingAlloc equityRegiona : equityRegionalList) {
                    if (equityRegiona.getUtHoldingAllocId().getHoldingAllocClassType().equals(classType)) {
                        HoldingAllocation holdingAllocation = new HoldingAllocation();
                        holdingAllocation.setName(equityRegiona.getUtHoldingAllocId().getHoldingAllocClassName());
                        holdingAllocation.setWeighting(equityRegiona.getHoldingAllocWeightNet());
                        RegionalExposuresList.add(holdingAllocation);
                        equityRegional.setPortfolioDate(DateUtil.getSimpleDateFormat(equityRegiona.getPortfolioDate(),
                            DateConstants.DateFormat_yyyyMMdd_withHyphen));
                    }
                }
            }
            sort_topholdingAlloc(RegionalExposuresList);
            updateOthersData(RegionalExposuresList, i, top);
            if (ListUtil.isValid(RegionalExposuresList)) {
                if (others) {
                    equityRegional.setRegionalExposures(RegionalExposuresList.subList(0,
                        top + 1 < RegionalExposuresList.size() ? top + 1 : RegionalExposuresList.size()));
                } else {
                    equityRegional.setRegionalExposures(
                        RegionalExposuresList.subList(0, top < RegionalExposuresList.size() ? top : RegionalExposuresList.size()));
                }
            }
        }
        return equityRegional;
    }

    private GlobalBondSectors setGlobalBondSectors(final UtProdInstm utProdInstm,
        final Map<String, List<UTHoldingAlloc>> holdingAllocMap, final GlobalBondSectors globalBondSectors,
        final Map<String, Integer> holdingsTopMap, final Map<String, Boolean> holdingsOthersMap, final String classType) {
        if (null != holdingAllocMap) {

            int i = 0;
            int top = 0;
            boolean others = true;

            if (!holdingsTopMap.isEmpty() && null != holdingsTopMap.get(classType)) {
                top = holdingsTopMap.get(classType);
            } else {
                top = this.holdingsReturnList;
            }
            if (!holdingsOthersMap.isEmpty() && null != holdingsOthersMap.get(classType)) {
                others = holdingsOthersMap.get(classType);
            }
            List<UTHoldingAlloc> bondSectorsList = holdingAllocMap.get(utProdInstm.getPerformanceId());
            List<HoldingAllocation> globalBondSectorsList = new ArrayList<HoldingAllocation>();
            if (ListUtil.isValid(bondSectorsList)) {
                for (UTHoldingAlloc bondSectors : bondSectorsList) {
                    if (bondSectors.getUtHoldingAllocId().getHoldingAllocClassType().equals(classType)) {
                        HoldingAllocation holdingAllocation = new HoldingAllocation();
                        holdingAllocation.setName(bondSectors.getUtHoldingAllocId().getHoldingAllocClassName());
                        holdingAllocation.setWeighting(bondSectors.getHoldingAllocWeightNet());
                        globalBondSectorsList.add(holdingAllocation);
                        globalBondSectors.setPortfolioDate(DateUtil.getSimpleDateFormat(bondSectors.getPortfolioDate(),
                            DateConstants.DateFormat_yyyyMMdd_withHyphen));
                    }
                }
            }

            sort_topholdingAlloc(globalBondSectorsList);
            updateOthersData(globalBondSectorsList, i, top);

            if (ListUtil.isValid(globalBondSectorsList)) {
                if (others) {
                    globalBondSectors.setGlobalBondSectors(globalBondSectorsList.subList(0,
                        top + 1 < globalBondSectorsList.size() ? top + 1 : globalBondSectorsList.size()));
                } else {
                    globalBondSectors.setGlobalBondSectors(
                        globalBondSectorsList.subList(0, top < globalBondSectorsList.size() ? top : globalBondSectorsList.size()));
                }
            }
        }
        return globalBondSectors;
    }

    private BondRegionalExposures setBondRegionalExposures(final UtProdInstm utProdInstm,
        final Map<String, List<UTHoldingAlloc>> holdingAllocMap, final BondRegionalExposures bondRegionalExposures,
        final Map<String, Integer> holdingsTopMap, final Map<String, Boolean> holdingsOthersMap, final String classType) {
        if (null != holdingAllocMap) {
            int i = 0;
            int top = 0;
            boolean others = true;


            if (!holdingsTopMap.isEmpty() && null != holdingsTopMap.get(classType)) {
                top = holdingsTopMap.get(classType);
            } else {
                top = this.holdingsReturnList;
            }
            if (!holdingsOthersMap.isEmpty() && null != holdingsOthersMap.get(classType)) {
                others = holdingsOthersMap.get(classType);
            }
            List<UTHoldingAlloc> bondRegionalList = holdingAllocMap.get(utProdInstm.getPerformanceId());
            List<HoldingAllocation> bondRegionalExposuresList = new ArrayList<HoldingAllocation>();

            if (ListUtil.isValid(bondRegionalList)) {
                for (UTHoldingAlloc bondRegional : bondRegionalList) {
                    if (bondRegional.getUtHoldingAllocId().getHoldingAllocClassType().equals(classType)) {
                        HoldingAllocation holdingAllocation = new HoldingAllocation();
                        holdingAllocation.setName(bondRegional.getUtHoldingAllocId().getHoldingAllocClassName());
                        holdingAllocation.setWeighting(bondRegional.getHoldingAllocWeightNet());
                        bondRegionalExposuresList.add(holdingAllocation);
                        bondRegionalExposures.setPortfolioDate(DateUtil.getSimpleDateFormat(bondRegional.getPortfolioDate(),
                            DateConstants.DateFormat_yyyyMMdd_withHyphen));
                    }
                }
            }
            sort_topholdingAlloc(bondRegionalExposuresList);
            updateOthersData(bondRegionalExposuresList, i, top);
            if (ListUtil.isValid(bondRegionalExposuresList)) {
                if (others) {
                    bondRegionalExposures.setBondRegionalExposures(bondRegionalExposuresList.subList(0,
                        top + 1 < bondRegionalExposuresList.size() ? top + 1 : bondRegionalExposuresList.size()));
                } else {
                    bondRegionalExposures.setBondRegionalExposures(bondRegionalExposuresList.subList(0,
                        top < bondRegionalExposuresList.size() ? top : bondRegionalExposuresList.size()));
                }
            }
        }
        return bondRegionalExposures;
    }

    private static void sort_topholdingAlloc(final List<HoldingAllocation> holdingAllocs) {
        Collections.sort(holdingAllocs, new Comparator<HoldingAllocation>() {
            @Override
            public int compare(final HoldingAllocation holding1, final HoldingAllocation holding2) {

                if (holding1 == null || holding1.getWeighting() == null) {
                    return 1;
                } else if (holding2 == null || holding2.getWeighting() == null) {
                    return -1;
                } else {
                    return holding2.getWeighting().compareTo(holding1.getWeighting());
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private List<Integer> getSwitchOutFundProductIdFromPredSearch(List<Integer> prodIds, final SearchCriteria criteria,
        final String[] fields, final Map<String, String> headerMap, final String channelRestrictCode) throws Exception {
        // temporary solution -- topNum="1000"
        PredSrchRequest request = new PredSrchRequest();
        request.setAssetClasses(new String[] {fields[3]});
        request.setSearchFields(new String[] {"productShortName", "productName", "symbol"});
        request.setSortingFields(new String[] {"symbol", "productShortName", "productName"});
        request.setTopNum("1000");
        request.setMarket(fields[2]);
        request.setChannelRestrictCode(channelRestrictCode);
        List<SearchCriteria> filterCriterias = new ArrayList<SearchCriteria>();
        filterCriterias.add(new SearchCriteria(criteria.getCriteriaKey(), criteria.getCriteriaValue(), criteria.getOperator()));
        request.setFilterCriterias(filterCriterias);
        request.putAllHeaders(headerMap);
        LogUtil.infoBeanToJson(FundSearchResultServiceImpl.class, "Get PredSrchResponse for switchOutFund request body: ", request);

        try {
            List<PredSrchResponse> predSrchResponseList = (List<PredSrchResponse>) this.internalProductKeyUtil.doService(request);
            LogUtil.infoBeanToJson(FundSearchResultServiceImpl.class, "Get PredSrchResponse for switchOutFund:",
                predSrchResponseList);

            if (ListUtil.isValid(predSrchResponseList)) {
                for (PredSrchResponse predSrchResponse : predSrchResponseList) {
                    List<ProdAltNumSeg> prodAltNumSegs = predSrchResponse.getProdAltNumSegs();
                    if (ListUtil.isValid(prodAltNumSegs)) {
                        for (ProdAltNumSeg prodAltNumSeg : prodAltNumSegs) {
                            if ("W".equals(prodAltNumSeg.getProdCdeAltClassCde())) {
                                prodIds.add(Integer.valueOf(prodAltNumSeg.getProdAltNum()));
                            }
                        }
                    }
                }
            } else {
                prodIds = null;
            }
        } catch (Exception e) {
            prodIds = null;
            LogUtil.debug(FundSearchResultServiceImpl.class, "Get PredSrchResponse for switchOutFund error:" + e.getMessage());
        }
        return prodIds;
    }

    @SuppressWarnings("unchecked")
    private List<PredSrchResponse> getProductFromPredSearch(final String keyword, final String market, final String[] assetClasses,
        final Map<String, String> headerMap) throws Exception {
        List<PredSrchResponse> predSrchResponseList = null;
        PredSrchRequest request = new PredSrchRequest();
        request.setKeyword(keyword);
        request.setAssetClasses(assetClasses);
        request.setSearchFields(new String[] {"symbol", "productName", "productShortName"});
        request.setSortingFields(new String[] {"symbol", "productName", "productShortName",});
        request.setTopNum("1");
        request.setMarket(market);
        request.putAllHeaders(headerMap);
        LogUtil.infoBeanToJson(FundSearchResultServiceImpl.class, "Get PredSrchResponse for switchOutFund request body: ", request);

        try {
          //  predSrchResponseList = (List<PredSrchResponse>) this.predictiveSearchService.doService(request);
            predSrchResponseList =  internalProductKeyUtil.doService(request);
            LogUtil.infoBeanToJson(FundSearchResultServiceImpl.class, "Get PredSrchResponse for switchOutFund:",
                predSrchResponseList);
        } catch (Exception e) {
            LogUtil.warn(FundSearchResultServiceImpl.class, "Get PredSrchResponse for switchOutFund error:" + e.getMessage());
        }
        return predSrchResponseList;
    }

    private List<ProductInfo> searchFundBySwithableGroups(final String[] swithableGroups, final String productType,
        final String productSubType, final String countryCode, final String groupMember, final String locale) throws Exception {
        return this.productKeyListWithEligibilityCheckService.getProductInfoList(locale, countryCode, groupMember, productType,
            productSubType, "fundsearch.switchablefunds", swithableGroups);
    }

    protected List<ProductKey> getProdAltNumFromWebservicce(final List<ProductInfo> productKeyObjectList) {
        List<ProductKey> productKeyList = new ArrayList<ProductKey>();
        if (ListUtil.isValid(productKeyObjectList)) {
            for (ProductInfo productInfo : productKeyObjectList) {
                if (null != productInfo) {
                    ProductKey productKey = new ProductKey();
                    productKey.setProdCdeAltClassCde(productInfo.getPrdAltClsCd());
                    productKey.setProdAltNum(productInfo.getPrdAltNum());
                    productKey.setProductType(productInfo.getPrdTpCd());
                    productKey.setMarket(productInfo.getPrdTrdCde());
                    productKeyList.add(productKey);
                }
            }
        }
        return productKeyList;
    }

    private void updateOthersData(final List<HoldingAllocation> holdingAllocationList, int i, final int top) {
        boolean flag = true;

        BigDecimal rescaledWeighting = new BigDecimal(0);
        BigDecimal weighting = new BigDecimal(0);
        if (ListUtil.isValid(holdingAllocationList)) {
            for (HoldingAllocation holdingAlloc : holdingAllocationList) {
                if (flag) {
                    if (null != holdingAlloc.getWeighting()) {
                        weighting = weighting.add(holdingAlloc.getWeighting());
                    }
                    ++i;
                    if (i == top) {
                        flag = false;
                    }
                }
            }
        }
        setOthers(holdingAllocationList, top, rescaledWeighting, weighting);
    }

    private List<HoldingAllocation> setOthers(final List<HoldingAllocation> globalStockSectorsList, final int top,
        final BigDecimal rescaledWeighting, final BigDecimal weighting) {

        if (ListUtil.isValid(globalStockSectorsList)) {
            HoldingAllocation holdingAllocOthers = new HoldingAllocation();
            holdingAllocOthers.setName("Others");
            holdingAllocOthers.setWeighting(new BigDecimal(100).subtract(weighting));
            globalStockSectorsList.add(holdingAllocOthers);
            if (top < globalStockSectorsList.size()) {
                globalStockSectorsList.set(top, holdingAllocOthers);
            }
        }
        return globalStockSectorsList;
    }

    private void mergeResponseFromeDB(final FundSearchResultResponse response, final List<UtProdInstm> utProdInstmList,
        final Map<Integer, List<String>> utProdChanl, final FundSearchResultRequest request,
        final Map<Integer, List<UtCatAsetAlloc>> sicHldgsMap, final Map<Integer, List<UtCatAsetAlloc>> geoHldgsMap,
        final Map<String, List<UtHoldings>> fundHoldingMap, final List<UtSvceHelper> utSvceHelperList,
        final Map<String, List<UTHoldingAlloc>> holdingAllocMap, final Map<String, Boolean> holdingsValueMap,
        final Map<String, Integer> holdingsTopMap, final Map<String, Boolean> holdingsOthersMap, final Date currentDate)
        throws Exception {

        if (ListUtil.isValid(utProdInstmList)) {
            int index =
                this.localeMappingUtil.getNameByIndex(request.getCountryCode() + CommonConstants.SYMBOL_DOT + request.getLocale());
            String currencyProdUsingKey =
                StringUtil.combineWithUnderline(request.getCountryCode(), request.getGroupMember(), request.getProductType());
            List<FundSearchResultProduct> products = new ArrayList<FundSearchResultProduct>();
            // Record the symbol form DB cant get the data from wpc file
            List<String> invalidWpcSymbols = new ArrayList<String>();

            for (UtProdInstm utProdInstm : utProdInstmList) {
                if (null != utProdInstm) {
                    FundSearchResultProduct fundSearchResultProduct = new FundSearchResultProduct();

                    Header header = fundSearchResultProduct.new Header();
                    Summary summary = fundSearchResultProduct.new Summary();
                    Profile profile = fundSearchResultProduct.new Profile();
                    Rating rating = fundSearchResultProduct.new Rating();
                    Performance performance = fundSearchResultProduct.new Performance();
                    AnnualizedReturns annualizedReturns = performance.new AnnualizedReturns();
                    performance.setAnnualizedReturns(annualizedReturns);
                    CalendarReturns calendarReturns = performance.new CalendarReturns();
                    performance.setCalendarReturns(calendarReturns);
                    List<FundSearchResultYear> years = new ArrayList<FundSearchResultYear>();
                    Holdings holdings = fundSearchResultProduct.new Holdings();
                    Purchase purchase = fundSearchResultProduct.new Purchase();
                    List<FundSearchRisk> risk = new ArrayList<FundSearchRisk>();

                    String totalReturnsSiteFeature = this.siteFeature.getStringDefaultFeature(request.getSiteKey(),
                        FundSearchResultServiceImpl.SITE_FEATURE_TOTALRETURNS);

                    SearchProduct searchProduct = this.internalProductKeyUtil.getProductBySearchWithAltClassCde("M",
                        request.getCountryCode(), request.getGroupMember(), utProdInstm.getSymbol(), utProdInstm.getMarket(),
                        utProdInstm.getProductType(), request.getLocale());
                    if (null != searchProduct && null != searchProduct.getSearchObject()) {
                        List<ProdAltNumSeg> prodAltNumSegs = searchProduct.getSearchObject().getProdAltNumSeg();
                        header.setProdAltNumSeg(prodAltNumSegs);
                        
                    } else {
                        invalidWpcSymbols.add(utProdInstm.getSymbol());
                        //when predSearch file not include product ,then will not return the production
                           continue;
                    }

                    // set header
                    setHeader(index, utProdInstm, header);
                    // set summary
                    setSummary(utProdInstm, summary, request.getSiteKey());
                    // set profile
                    setProfile(utProdInstm, utProdChanl, profile, currentDate);
                    // set rating
                    setRating(utProdInstm, rating);
                    // set annualized returns
                    setAnnualizedReturns(utProdInstm, annualizedReturns, totalReturnsSiteFeature);
                    // set calendar returns
                    setCalendarReturns(utProdInstm, calendarReturns, years, totalReturnsSiteFeature);
                    // set list risk
                    setRisk(utProdInstm, risk);
                    // set purchase
                    setPurchase(utProdInstm, purchase, utSvceHelperList, currencyProdUsingKey);
                    // set holdings
                    setHoldings(utProdInstm, sicHldgsMap, geoHldgsMap, fundHoldingMap, holdingAllocMap, holdingsValueMap,
                        holdingsTopMap, holdingsOthersMap, holdings);

                    

                    fundSearchResultProduct.setHeader(header);
                    fundSearchResultProduct.setSummary(summary);
                    fundSearchResultProduct.setProfile(profile);
                    fundSearchResultProduct.setRating(rating);
                    fundSearchResultProduct.setPerformance(performance);
                    fundSearchResultProduct.setRisk(risk);
                    fundSearchResultProduct.setHoldings(holdings);
                    fundSearchResultProduct.setPurchase(purchase);

                    products.add(fundSearchResultProduct);
                }
            }
            if (ListUtil.isValid(invalidWpcSymbols)) {
                LogUtil.debug(FundSearchResultServiceImpl.class, "The invalid wpc symbol from DB:" + invalidWpcSymbols.toString());
            }
            response.setProducts(products);
        }
    }

    private void setHeader(final int index, final UtProdInstm utProdInstm, final Header header) {

        if (0 == index) {
            header.setName(utProdInstm.getProdName());
            header.setCategoryName(utProdInstm.getCategoryName1());
            header.setFamilyName(utProdInstm.getFamilyName1());
            header.setCategoryLevel0Name(utProdInstm.getCategoryLevel0Name1());
            header.setCategoryLevel1Name(utProdInstm.getCategoryLevel1Name1());
            header.setInvestmentRegionName(utProdInstm.getInvestmentRegionName1());
        } else if (1 == index) {
            header.setName(utProdInstm.getProdPllName());
            header.setCategoryName(utProdInstm.getCategoryName2());
            header.setFamilyName(utProdInstm.getFamilyName2());
            header.setCategoryLevel0Name(utProdInstm.getCategoryLevel0Name2());
            header.setCategoryLevel1Name(utProdInstm.getCategoryLevel1Name2());
            header.setInvestmentRegionName(utProdInstm.getInvestmentRegionName2());
        } else if (2 == index) {
            header.setName(utProdInstm.getProdSllName());
            header.setCategoryName(utProdInstm.getCategoryName3());
            header.setFamilyName(utProdInstm.getFamilyName3());
            header.setCategoryLevel0Name(utProdInstm.getCategoryLevel0Name3());
            header.setCategoryLevel1Name(utProdInstm.getCategoryLevel1Name3());
            header.setInvestmentRegionName(utProdInstm.getInvestmentRegionName3());
        } else {
            header.setName(utProdInstm.getProdName());
            header.setCategoryName(utProdInstm.getCategoryName1());
            header.setFamilyName(utProdInstm.getFamilyName1());
            header.setCategoryLevel0Name(utProdInstm.getCategoryLevel0Name1());
            header.setCategoryLevel1Name(utProdInstm.getCategoryLevel1Name1());
            header.setInvestmentRegionName(utProdInstm.getInvestmentRegionName1());
        }

        header.setMarket(utProdInstm.getMarket());
        header.setProductType(utProdInstm.getProductType());
        header.setCurrency(utProdInstm.getCurrencyId());
        header.setCategoryCode(utProdInstm.getCategoryCode());
        header.setFamilyCode(utProdInstm.getFamilyCode());
        header.setCategoryLevel0Code(utProdInstm.getCategoryLevel0Code());
        header.setCategoryLevel1Code(utProdInstm.getCategoryLevel1Code());
        header.setInvestmentRegionCode(utProdInstm.getInvestmentRegionCode());
        header.setSiFundCatCde(utProdInstm.getSiFundCategoryCode());
    }

    private void setSummary(final UtProdInstm utProdInstm, final Summary summary, final String site) {

        summary.setRiskLvlCde(utProdInstm.getRiskLvlCde());
        summary.setLastPrice(null);// lastPrice
        summary.setDayEndNAV(utProdInstm.getDayEndNAV());
        summary.setDayEndNAVCurrencyCode(utProdInstm.getCurrencyId());
        summary.setChangeAmountNAV(utProdInstm.getChangeAmountNAV());
        summary.setChangePercentageNAV(utProdInstm.getMarketPrice());
        Date asOfDate = utProdInstm.getAsOfDate();
        if (null != asOfDate) {
            summary.setAsOfDate(DateUtil.getSimpleDateFormat(asOfDate, DateConstants.DateFormat_yyyyMMdd_withHyphen));
        }
        summary.setAverageDailyVolume(null);
        summary.setAssetsUnderManagement(utProdInstm.getAssetsUnderManagement());
        summary.setAssetsUnderManagementCurrencyCode(utProdInstm.getCcyAsofRep());
        summary.setTotalNetAsset(utProdInstm.getTotalNetAsset());
        summary.setTotalNetAssetCurrencyCode(utProdInstm.getCurrencyId());
        summary.setRatingOverall(utProdInstm.getRatingOverall());
        summary.setMer(utProdInstm.getMer());
        summary.setYield1Yr(utProdInstm.getYield1Yr());
        // switchInMinAmount
        summary.setSwitchInMinAmount(utProdInstm.getFundSwInMinAmt());
        summary.setSwitchInMinAmountCurrencyCode(utProdInstm.getCurrencyId());
        summary.setAnnualReportOngoingCharge(utProdInstm.getKiidOngoingCharge());
        summary.setActualManagementFee(utProdInstm.getActualManagementFee());

        Date endDateRiskLvlCde = utProdInstm.getEndDateRiskLvlCde();
        if (null != endDateRiskLvlCde) {
            summary.setEndDate(DateUtil.getSimpleDateFormat(endDateRiskLvlCde, DateConstants.DateFormat_yyyyMMdd_withHyphen));
        }
    }

    private void setProfile(final UtProdInstm utProdInstm, final Map<Integer, List<String>> utProdChanl, final Profile profile,
        final Date currentDate) {

        profile.setInceptionDate(null != utProdInstm.getInceptionDate()
            ? DateUtil.getSimpleDateFormat(utProdInstm.getInceptionDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen) : null);
        profile.setTurnoverRatio(utProdInstm.getTurnoverRatio());
        profile.setStdDev3Yr(utProdInstm.getStdDev3Yr());
        profile.setEquityStylebox(utProdInstm.getEquityStyle());
        profile.setExpenseRatio(utProdInstm.getExpenseRatio());
        profile.setMarginRatio(utProdInstm.getLoanProdOdMrgnPct());
        profile.setInitialCharge(utProdInstm.getChrgInitSalesPct());
        profile.setAnnualManagementFee(utProdInstm.getAnnMgmtFeePct());
        profile.setDistributionYield(utProdInstm.getYield1Yr());
        profile.setDistributionFrequency(utProdInstm.getDistributionFrequency());
        Character topPerformersInd = utProdInstm.getTopPerformersInd();
        profile.setTopPerformersIndicator(topPerformersInd == null ? null : topPerformersInd.toString());
        profile.setProdTopSellRankNum(utProdInstm.getProdTopSellRankNum());
        profile.setTopSellProdIndex(utProdInstm.getTopSellProdIndex());
        profile.setProdLaunchDate(
            DateUtil.getSimpleDateFormat(utProdInstm.getProdLaunchDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
        profile.setFundClassCde(utProdInstm.getFundClassCde());
        profile.setAmcmIndicator(utProdInstm.getAmcmAuthorizeIndicator());
        profile.setProdStatCde(utProdInstm.getProdStatCde());
        profile.setNextDealDate(
            DateUtil.getSimpleDateFormat(utProdInstm.getNextDealDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
        if (!utProdChanl.isEmpty() && utProdChanl.containsKey(utProdInstm.getUtProdInstmId().getProductId())) {
            List<String> indicate = utProdChanl.get(utProdInstm.getUtProdInstmId().getProductId());
            if (StringUtil.isValid(indicate.get(0))
                    && StringUtil.isValid(indicate.get(1))
                    && StringUtil.isValid(indicate.get(2))
                    && StringUtil.isValid(indicate.get(3))) {
                profile.setAllowBuy(indicate.get(0));
                profile.setAllowSell(indicate.get(1));
                profile.setAllowSwOutProdInd(indicate.get(2));
                profile.setAllowSwInProdInd(indicate.get(3));
            } else {
                profile.setAllowBuy(utProdInstm.getAllowBuyProdInd());
                profile.setAllowSell(utProdInstm.getAllowSellProdInd());
                profile.setAllowSwInProdInd(utProdInstm.getAllowSwInProdInd());
                profile.setAllowSwOutProdInd(utProdInstm.getAllowSwOutProdInd());
            }
        } else {
            profile.setAllowBuy(utProdInstm.getAllowBuyProdInd());
            profile.setAllowSell(utProdInstm.getAllowSellProdInd());
            profile.setAllowSwInProdInd(utProdInstm.getAllowSwInProdInd());
            profile.setAllowSwOutProdInd(utProdInstm.getAllowSwOutProdInd());
        }

        profile.setAllowSellMipProdInd(utProdInstm.getAllowSellMipProdInd());
        profile.setAnnualReportDate(
            DateUtil.getSimpleDateFormat(utProdInstm.getAnnualReportDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
        profile.setPiFundInd(utProdInstm.getPiFundInd());
        profile.setDeAuthFundInd(utProdInstm.getDeAuthFundInd());
        profile.setPayCashDivInd(utProdInstm.getPayCashDivInd());
        profile.setUndlIndexCde(utProdInstm.getUndlIndexCde());
        profile.setPopularRankNum(utProdInstm.getPopularRankNum());
        profile.setGbaAcctTrdb(utProdInstm.getGbaAcctTrdb());
        profile.setGnrAcctTrdb(utProdInstm.getGnrAcctTrdb());
        profile.setShortListRPQLvlNum(utProdInstm.getShortLstRPQLvlNum());
        profile.setEsgInd(utProdInstm.getEsgInd());
        String date = DateUtil.getDateTimeByTimeZone(DateConstants.DateFormat_yyyyMMdd,
            TimeZone.getTimeZone(DateConstants.TIMEZONE_GMT8), currentDate);
        String time =
            DateUtil.getDateTimeByTimeZone(DateConstants.FE_TIME_FORMAT_WITHOUT_SECOND,
                TimeZone.getTimeZone(DateConstants.TIMEZONE_GMT8), currentDate);

        if (null != utProdInstm.getCutOffTime() && null != utProdInstm.getInterfaceDate()) {
            String cutOffTime = DateUtil.getDateTimeByTimeZone(DateConstants.FE_TIME_FORMAT_WITHOUT_SECOND,
                TimeZone.getTimeZone(DateConstants.TIMEZONE_GMT8), utProdInstm.getCutOffTime());
            String interfaceDate = DateUtil.getDateTimeByTimeZone(DateConstants.DateFormat_yyyyMMdd,
                TimeZone.getTimeZone(DateConstants.TIMEZONE_GMT8), utProdInstm.getInterfaceDate());
            LogUtil.debug(FundSearchResultServiceImpl.class, " currentTime = " + time + " currentdate = " + date + " cutoffTime = "
                + cutOffTime + " interfaceDate = " + interfaceDate);
            if (Integer.valueOf(time) >= Integer.valueOf(cutOffTime) || Integer.valueOf(date) > Integer.valueOf(interfaceDate)) {
                profile.setInDealDate(
                    DateUtil.getSimpleDateFormat(utProdInstm.getInDealAftDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
                profile.setInRedempStlDate(DateUtil.getSimpleDateFormat(utProdInstm.getInRedempStlAftDate(),
                    DateConstants.DateFormat_yyyyMMdd_withHyphen));
                profile.setInScribStlDate(
                    DateUtil.getSimpleDateFormat(utProdInstm.getInScribStlAftDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
            } else {
                profile.setInDealDate(
                    DateUtil.getSimpleDateFormat(utProdInstm.getInDealBefDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
                profile.setInRedempStlDate(
                    DateUtil.getSimpleDateFormat(utProdInstm.getInRedempStlBefDate(),
                        DateConstants.DateFormat_yyyyMMdd_withHyphen));
                profile.setInScribStlDate(
                    DateUtil.getSimpleDateFormat(utProdInstm.getInScribStlBefDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
            }
        }

    }

    private void setRating(final UtProdInstm utProdInstm, final Rating rating) {

        Integer ratingOverall = utProdInstm.getRatingOverall();
        if (null != ratingOverall) {
            rating.setMorningstarRating(ratingOverall.toString());
        }
        rating.setTaxAdjustedRating(utProdInstm.getTaxAdjustedRating());
        rating.setAverageCreditQualityName(utProdInstm.getAverageCreditQualityName());
        rating.setAverageCreditQuality(utProdInstm.getAverageCreditQuality());
        rating.setAverageCreditQualityDate(DateUtil.getSimpleDateFormat(utProdInstm.getHoldingAllocationPortfolioDate(),
            DateConstants.DateFormat_yyyyMMdd_withHyphen));
        rating.setRank1Yr(utProdInstm.getRank1Yr());
        rating.setRank3Yr(utProdInstm.getRank3Yr());
        rating.setRank5Yr(utProdInstm.getRank5Yr());
        rating.setRank10Yr(utProdInstm.getRank10Yr());

        Date ratingDate = utProdInstm.getRatingDate();
        if (null != ratingDate) {
            rating.setRatingDate(DateUtil.getSimpleDateFormat(ratingDate, DateConstants.DateFormat_yyyyMMdd_withHyphen));
        }
    }

    private void setAnnualizedReturns(final UtProdInstm utProdInstm, final AnnualizedReturns annualizedReturns,
        final String totalReturnsSiteFeature) {

        if (StringUtil.isValid(totalReturnsSiteFeature)
            && FundSearchResultServiceImpl.SITE_FEATURE_TOTALRETURNS_DAILY.equals(totalReturnsSiteFeature)) {
            // set annualized returns (daily)
            annualizedReturns.setReturn1Mth(utProdInstm.getReturn1mthDaily());
            annualizedReturns.setReturn3Mth(utProdInstm.getReturn3mthDaily());
            annualizedReturns.setReturn6Mth(utProdInstm.getReturn6mthDaily());
            annualizedReturns.setReturn1Yr(utProdInstm.getReturn1yrDaily());
            annualizedReturns.setReturn3Yr(utProdInstm.getReturn3yrDaily());
            annualizedReturns.setReturn5Yr(utProdInstm.getReturn5yrDaily());
            annualizedReturns.setReturn10Yr(utProdInstm.getReturn10yrDaily());
            annualizedReturns.setReturnSinceInception(utProdInstm.getReturnSinceInceptionDaily());
            Date dailyEndDate = utProdInstm.getAsOfDate();
            if (null != dailyEndDate) {
                annualizedReturns
                    .setAsOfDate(DateUtil.getSimpleDateFormat(dailyEndDate, DateConstants.DateFormat_yyyyMMdd_withHyphen));
            }
        } else {
            // set annualized returns (monthly)
            annualizedReturns.setReturn1Mth(utProdInstm.getReturn1mth());
            annualizedReturns.setReturn3Mth(utProdInstm.getReturn3mth());
            annualizedReturns.setReturn6Mth(utProdInstm.getReturn6mth());
            annualizedReturns.setReturn1Yr(utProdInstm.getReturn1yr());
            annualizedReturns.setReturn3Yr(utProdInstm.getReturn3yr());
            annualizedReturns.setReturn5Yr(utProdInstm.getReturn5yr());
            annualizedReturns.setReturn10Yr(utProdInstm.getReturn10yr());
            Date monthEndDate = utProdInstm.getMonthEndDate();
            annualizedReturns.setReturnSinceInception(utProdInstm.getReturnSinceInception());
            if (null != monthEndDate) {
                annualizedReturns
                    .setAsOfDate(DateUtil.getSimpleDateFormat(monthEndDate, DateConstants.DateFormat_yyyyMMdd_withHyphen));
            }
        }
        annualizedReturns.setReturn1day(utProdInstm.getReturn1day());
        annualizedReturns.setInceptionDate(null != utProdInstm.getInceptionDate()
            ? DateUtil.getSimpleDateFormat(utProdInstm.getInceptionDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen) : null);
    }

    private void setCalendarReturns(final UtProdInstm utProdInstm, final CalendarReturns calendarReturns,
        final List<FundSearchResultYear> years, final String totalReturnsSiteFeature) {

        if (StringUtil.isValid(totalReturnsSiteFeature)
            && FundSearchResultServiceImpl.SITE_FEATURE_TOTALRETURNS_DAILY.equals(totalReturnsSiteFeature)) {
            // set calendar returns (daily)
            calendarReturns.setReturnYTD(utProdInstm.getReturnytdDaily());
        } else {
            // set calendar returns (monthly)
            calendarReturns.setReturnYTD(utProdInstm.getReturnYTD());
        }
        FundSearchResultYear fundSearchResultYear1 = new FundSearchResultYear();
        fundSearchResultYear1.setYearName(1);
        fundSearchResultYear1.setYearValue(utProdInstm.getYear1());
        years.add(fundSearchResultYear1);

        FundSearchResultYear fundSearchResultYear2 = new FundSearchResultYear();
        fundSearchResultYear2.setYearName(2);
        fundSearchResultYear2.setYearValue(utProdInstm.getYear2());
        years.add(fundSearchResultYear2);

        FundSearchResultYear fundSearchResultYear3 = new FundSearchResultYear();
        fundSearchResultYear3.setYearName(3);
        fundSearchResultYear3.setYearValue(utProdInstm.getYear3());
        years.add(fundSearchResultYear3);

        FundSearchResultYear fundSearchResultYear4 = new FundSearchResultYear();
        fundSearchResultYear4.setYearName(4);
        fundSearchResultYear4.setYearValue(utProdInstm.getYear4());
        years.add(fundSearchResultYear4);

        FundSearchResultYear fundSearchResultYear5 = new FundSearchResultYear();
        fundSearchResultYear5.setYearName(5);
        fundSearchResultYear5.setYearValue(utProdInstm.getYear5());
        years.add(fundSearchResultYear5);
        // set year
        calendarReturns.setYear(years);
    }

    private void setRisk(final UtProdInstm utProdInstm, final List<FundSearchRisk> risk) {

        FundSearchRisk fundSearchRisk1 = new FundSearchRisk();
        YearRisk yearRisk1 = fundSearchRisk1.new YearRisk();
        yearRisk1.setYear(1);
        yearRisk1.setBeta(utProdInstm.getBeta1());
        yearRisk1.setStdDev(utProdInstm.getStdDev1());
        yearRisk1.setAlpha(utProdInstm.getAlpha1());
        yearRisk1.setSharpeRatio(utProdInstm.getSharpeRatio1());
        yearRisk1.setrSquared(utProdInstm.getrSquared1());
        Date endDate = utProdInstm.getEndDateYearRisk();
        if (null != endDate) {
            yearRisk1.setEndDate(DateUtil.getSimpleDateFormat(endDate, DateConstants.DateFormat_yyyyMMdd_withHyphen));
        }
        fundSearchRisk1.setYearRisk(yearRisk1);
        risk.add(fundSearchRisk1);

        FundSearchRisk fundSearchRisk3 = new FundSearchRisk();
        YearRisk yearRisk3 = fundSearchRisk3.new YearRisk();
        yearRisk3.setYear(3);
        yearRisk3.setBeta(utProdInstm.getBeta3());
        yearRisk3.setStdDev(utProdInstm.getStdDev3Yr());
        yearRisk3.setAlpha(utProdInstm.getAlpha3());
        yearRisk3.setSharpeRatio(utProdInstm.getSharpeRatio3());
        yearRisk3.setrSquared(utProdInstm.getrSquared3());
        if (null != endDate) {
            yearRisk3.setEndDate(DateUtil.getSimpleDateFormat(endDate, DateConstants.DateFormat_yyyyMMdd_withHyphen));
        }
        fundSearchRisk3.setYearRisk(yearRisk3);
        risk.add(fundSearchRisk3);

        FundSearchRisk fundSearchRisk5 = new FundSearchRisk();
        // set year risk
        YearRisk yearRisk5 = fundSearchRisk5.new YearRisk();
        yearRisk5.setYear(5);
        yearRisk5.setBeta(utProdInstm.getBeta5());
        yearRisk5.setStdDev(utProdInstm.getStdDev5());
        yearRisk5.setAlpha(utProdInstm.getAlpha5());
        yearRisk5.setSharpeRatio(utProdInstm.getSharpeRatio5());
        yearRisk5.setrSquared(utProdInstm.getrSquared5());
        if (null != endDate) {
            yearRisk5.setEndDate(DateUtil.getSimpleDateFormat(endDate, DateConstants.DateFormat_yyyyMMdd_withHyphen));
        }
        fundSearchRisk5.setYearRisk(yearRisk5);
        risk.add(fundSearchRisk5);

        FundSearchRisk fundSearchRisk10 = new FundSearchRisk();
        // set year risk
        YearRisk yearRisk10 = fundSearchRisk10.new YearRisk();
        yearRisk10.setYear(10);
        yearRisk10.setBeta(utProdInstm.getBeta10());
        yearRisk10.setStdDev(utProdInstm.getStdDev10());
        yearRisk10.setAlpha(utProdInstm.getAlpha10());
        yearRisk10.setSharpeRatio(utProdInstm.getSharpeRatio10());
        yearRisk10.setrSquared(utProdInstm.getrSquared10());
        if (null != endDate) {
            yearRisk10.setEndDate(DateUtil.getSimpleDateFormat(endDate, DateConstants.DateFormat_yyyyMMdd_withHyphen));
        }
        fundSearchRisk10.setYearRisk(yearRisk10);
        risk.add(fundSearchRisk10);
    }

    private void setPurchase(final UtProdInstm utProdInstm, final Purchase purchase, final List<UtSvceHelper> utSvceHelperList,
        final String currencyProdUsingKey) {

        purchase.setMinInitInvst(utProdInstm.getMinInitInvst());
        // PurchaseCurrencyId - Fund Purchase Details;
        purchase.setMinInitInvstCurrencyCode(utProdInstm.getPurchaseCcy());
        purchase.setMinSubqInvst(utProdInstm.getMinSubqInvst());
        // PurchaseCurrencyId - Fund Purchase Details
        purchase.setMinSubqInvstCurrencyCode(utProdInstm.getPurchaseCcy());
        purchase.setMinInitRRSPInvst(utProdInstm.getMinimumIRA());
        // PurchaseCurrencyId - Fund Purchase Details
        purchase.setMinInitRRSPInvstCurrencyCode(utProdInstm.getPurchaseCcy());

        purchase.sethhhhMinInitInvst(utProdInstm.gethhhhMinInitInvst());
        purchase.sethhhhMinInitInvstCurrencyCode(getTradableCurrencyCode(utProdInstm, currencyProdUsingKey));
        purchase.sethhhhMinSubqInvst(utProdInstm.gethhhhMinSubqInvst());
        purchase.sethhhhMinSubqInvstCurrencyCode(getTradableCurrencyCode(utProdInstm, currencyProdUsingKey));
        if (ListUtil.isValid(utSvceHelperList)) {
            for (UtSvceHelper utSvceHelper : utSvceHelperList) {
                if (utProdInstm.getPerformanceId().equals(utSvceHelper.getPerformanceId())) {
                    purchase.setLoadType(utSvceHelper.getFundSvcClzTypeCd());
                    break;
                }
            }
        }
        purchase.setRRSPEligibility(utProdInstm.getRrspEligibility());
    }

    private void setHoldings(final UtProdInstm utProdInstm, final Map<Integer, List<UtCatAsetAlloc>> sicHldgsMap,
        final Map<Integer, List<UtCatAsetAlloc>> geoHldgsMap, final Map<String, List<UtHoldings>> fundHoldingMap,
        final Map<String, List<UTHoldingAlloc>> holdingAllocMap, final Map<String, Boolean> holdingsValueMap,
        final Map<String, Integer> holdingsTopMap, final Map<String, Boolean> holdingsOthersMap, final Holdings holdings)
        throws ParseException {

        List<TopHoldingsSearch> topHoldings = new ArrayList<TopHoldingsSearch>();
        AssetAllocations assetAlloc = holdings.new AssetAllocations();
        GlobalStockSectors stockSectors = holdings.new GlobalStockSectors();
        RegionalExposures equityRegional = holdings.new RegionalExposures();
        GlobalBondSectors bondSectors = holdings.new GlobalBondSectors();
        BondRegionalExposures bondRegional = holdings.new BondRegionalExposures();
        FundSearchSector sector = new FundSearchSector();
        FundSearchGeographicRegion geographicRegion = new FundSearchGeographicRegion();

        // set sector
        List<UtCatAsetAlloc> sicUtCatAsetAllocList = sicHldgsMap.get(utProdInstm.getUtProdInstmId().getProductId());
        if (ListUtil.isValid(sicUtCatAsetAllocList)) {
            sortHolding_sicHldgs(sicUtCatAsetAllocList);
            FundSearchResultUtil.getFundSearchSector(sector, sicUtCatAsetAllocList);
        }
        // geographicRegion
        List<UtCatAsetAlloc> geoUtCatAsetAllocList = geoHldgsMap.get(utProdInstm.getUtProdInstmId().getProductId());
        if (ListUtil.isValid(geoUtCatAsetAllocList)) {
            sortHolding_geoHldgs(geoUtCatAsetAllocList);
            FundSearchResultUtil.getFundSearchGeographicRegion(geographicRegion, geoUtCatAsetAllocList);
        }

        // set topHoldingsList
        if (null != fundHoldingMap) {
            holdings.setTopHoldingsList(setTopHoldingsList(utProdInstm, fundHoldingMap, topHoldings, holdingsValueMap,
                holdingsTopMap, holdingsOthersMap, "topHoldingsList"));
        }

        // set assetAlloc/stockSectors/equityRegional/bondSectors/bondRegional
        if (null != holdingAllocMap) {
            holdings.setAssetAlloc(setAssetAllocation(utProdInstm, holdingAllocMap, assetAlloc, "ASET_ALLOC"));
            holdings.setStockSectors(
                setGlobalStockSectors(utProdInstm, holdingAllocMap, stockSectors, holdingsTopMap, holdingsOthersMap, "STOCK_SEC"));
            holdings.setEquityRegional(
                setRegionalExposures(utProdInstm, holdingAllocMap, equityRegional, holdingsTopMap, holdingsOthersMap, "STOCK_GEO"));
            holdings.setBondSectors(
                setGlobalBondSectors(utProdInstm, holdingAllocMap, bondSectors, holdingsTopMap, holdingsOthersMap, "BOND_SEC"));
            holdings.setBondRegional(setBondRegionalExposures(utProdInstm, holdingAllocMap, bondRegional, holdingsTopMap,
                holdingsOthersMap, "BOND_GEO"));
        }
        holdings.setSector(sector);
        holdings.setGeographicRegion(geographicRegion);
    }

}