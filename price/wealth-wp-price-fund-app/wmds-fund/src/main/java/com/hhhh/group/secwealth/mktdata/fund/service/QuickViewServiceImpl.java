package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.exception.ValidatorException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.*;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.ValidatorError;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMdsbeService;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.QuickViewRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.QuickViewResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quickview.QuickView;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quickview.QuickViewRespItem;
import com.hhhh.group.secwealth.mktdata.fund.constants.QuickViewTableName;
import com.hhhh.group.secwealth.mktdata.fund.constants.TimeScale;
import com.hhhh.group.secwealth.mktdata.fund.criteria.QuickViewCriteria;
import com.hhhh.group.secwealth.mktdata.fund.dao.QuickViewDao;
import com.hhhh.group.secwealth.mktdata.fund.util.MiscUtil;
import com.hhhh.group.secwealth.mktdata.fund.util.SearchCriteriaUtil;
import com.hhhh.group.secwealth.mktdata.fund.util.WpcApiCfg;
import com.hhhh.group.secwealth.mktdata.fund.webservice.prodkeylistwitheligcheck.json.ProductInfo;
import com.hhhh.group.secwealth.mktdata.fund.webservice.prodkeylistwitheligcheck.service.ProductKeyListWithEligibilityCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service("fundQuickViewService")
public class QuickViewServiceImpl extends AbstractMdsbeService {

    private static final String DEFAULT = "DEFAULT";
    private static final String hhhh_BEST_SELLER = "BEST_SELLER";
    private static final String hhhh_RETIREMENT_FUND = "RETIREMENT_FUND";
    private static final String hhhh_NEW_FUND = "NEW_FUND";
    private static final String WEBSERVICE = "WEBSERVICE";
    private static final String DB = "DB";
    private static final String SITE_FEATURE_TOPPERFMLIST = ".topPerfmList";

    @Value("#{systemConfig['quickview.result.limit']}")
    private Integer quickviewResultLimit;

    @Value("#{systemConfig['quickview.default.timescale']}")
    private String defaultTimescale;

    @Resource(name = "newFundBestSellerMap")
    private Map<String, String> newFundBestSellerMap;

    @Resource(name = "newFundBestSellerCaseMap")
    private Map<String, String> newFundBestSellerCaseMap;

    @Autowired
    @Qualifier("productKeyListWithEligibilityCheckService")
    private ProductKeyListWithEligibilityCheckService productKeyListWithEligibilityCheckService;

    @Autowired
    @Qualifier("quickViewDao")
    private QuickViewDao quickViewDao;

    @Autowired
    @Qualifier("wpcApiCfg")
    private WpcApiCfg wpcApiCfg;

    @Autowired
    @Qualifier("localeMappingUtil")
    private LocaleMappingUtil localeMappingUtil;

    @Autowired
    @Qualifier("siteFeature")
    private SiteFeature siteFeature;

    @Override
    public Object execute(final Object object) throws Exception {
        QuickViewRequest request = (QuickViewRequest) object;
        // validate the criterias
        List<ValidatorError> errMessages = SearchCriteriaUtil.validate(request.getCriterias());
        if (ListUtil.isValid(errMessages)) {
            throw new ValidatorException(ErrTypeConstants.INPUT_PARAMETER_INVALID, errMessages);
        }

        QuickViewCriteria quickViewCriteria = QuickViewCriteria.getQuickViewCriteria(request, this.defaultTimescale);
        String locale = request.getLocale();
        String countryCode = request.getCountryCode();
        String groupMember = request.getGroupMember();
        String productType = request.getProductType();
        Map<String, String> headers = request.getHeaders();
        String currency = quickViewCriteria.getCurrency();
        String prodStatCde = quickViewCriteria.getProdStatCde();
        Integer  numberOfRecords =  quickViewCriteria.getlimitResult();

        if(null != numberOfRecords){
            this.quickviewResultLimit = numberOfRecords;
        }
        boolean returnOnlyNumberOfMatches = request.getReturnOnlyNumberOfMatches() == null ? false : request
            .getReturnOnlyNumberOfMatches().booleanValue();
        String productSubType = quickViewCriteria.getProductSubType();
        TimeScale timeScale = quickViewCriteria.getTimeScale();
        QuickViewTableName tableName = quickViewCriteria.getTableName();
        int totalNumberOfRecords = 0;
        String topPerfmList = this.siteFeature.getStringDefaultFeature(request.getSiteKey(),
            QuickViewServiceImpl.SITE_FEATURE_TOPPERFMLIST);

        int localIndex = this.localeMappingUtil.getNameByIndex(countryCode + CommonConstants.SYMBOL_DOT + locale);

        List<Object[]> resultList = new ArrayList<Object[]>();
        switch (tableName) {
        case BP: {
            totalNumberOfRecords = this.quickViewDao.getBottomPerformanceTotalCount(productType, timeScale,
                quickViewCriteria.getCategory(), productSubType,request.getRestrOnlScribInd(),currency, prodStatCde, headers);
            if (!returnOnlyNumberOfMatches) {
                resultList = this.quickViewDao.getBottomPerformanceFunds(productType, timeScale, quickViewCriteria.getCategory(),
                    productSubType, this.quickviewResultLimit,request.getRestrOnlScribInd(),currency, prodStatCde, headers);
            }
            break;
        }
        case TP: {

            if (!returnOnlyNumberOfMatches) {
                resultList = this.quickViewDao.getTopPerformanceFunds(productType, timeScale, quickViewCriteria.getCategory(),
                    productSubType, this.quickviewResultLimit,request.getRestrOnlScribInd(),currency, prodStatCde, headers);
                totalNumberOfRecords = resultList == null ? 0 : resultList.size();
            }else{
                totalNumberOfRecords = this.quickViewDao.getTopPerformanceFundsTotalCount(productType, timeScale,
                        quickViewCriteria.getCategory(), productSubType,request.getRestrOnlScribInd(),currency, prodStatCde, headers);
            }
            break;
        }
        case hhhh_ALL_FUNDS: {
            String identifier = countryCode + "-" + groupMember;
            String wpcCriteriaValue = this.wpcApiCfg.getValue(identifier, "quickview.allhhhhfund");
            if (!returnOnlyNumberOfMatches) {
                resultList = this.quickViewDao.getFundsByFundHouse(quickViewCriteria.getCategory(), productSubType, timeScale,
                    wpcCriteriaValue, headers);
                totalNumberOfRecords = resultList == null ? 0 : resultList.size();
            } else {
                totalNumberOfRecords = this.quickViewDao.getFundsByFundHouseCount(quickViewCriteria.getCategory(), productSubType,
                    timeScale, wpcCriteriaValue, headers);
            }
            break;
        }
        case HBSC_RETIREMENT_FUND: {        	
        	String tableNameValue = this.newFundBestSellerCaseMap.get(QuickViewServiceImpl.hhhh_RETIREMENT_FUND);
            String key = request.getSiteKey().toUpperCase() + CommonConstants.SYMBOL_UNDERLINE
                + QuickViewServiceImpl.hhhh_RETIREMENT_FUND;
            String value = this.newFundBestSellerMap.get(key);
            if (StringUtil.isInvalid(value)) {
                value = this.newFundBestSellerMap.get(QuickViewServiceImpl.DEFAULT + CommonConstants.SYMBOL_UNDERLINE
                    + QuickViewServiceImpl.hhhh_RETIREMENT_FUND);
            }
            resultList = getResultList(request, productSubType, timeScale, tableNameValue, value,currency,prodStatCde);
            totalNumberOfRecords = ListUtil.isValid(resultList) ? resultList.size() : 0;
            break;
        }
        case hhhh_BEST_SELLER: {
            String tableNameValue = this.newFundBestSellerCaseMap.get(QuickViewServiceImpl.hhhh_BEST_SELLER);
            String key = request.getSiteKey().toUpperCase() + CommonConstants.SYMBOL_UNDERLINE
                + QuickViewServiceImpl.hhhh_BEST_SELLER;
            String value = this.newFundBestSellerMap.get(key);
            if (StringUtil.isInvalid(value)) {
                value = this.newFundBestSellerMap.get(QuickViewServiceImpl.DEFAULT + CommonConstants.SYMBOL_UNDERLINE
                    + QuickViewServiceImpl.hhhh_BEST_SELLER);
            }
            resultList = getResultList(request, productSubType, timeScale, tableNameValue, value,currency,prodStatCde);
            totalNumberOfRecords = ListUtil.isValid(resultList) ? resultList.size() : 0;
            break;
        }
        case hhhh_FUND_OF_QUARTER: {
            totalNumberOfRecords = this.quickViewDao.getFundOfQuarterTotalCount(productType, timeScale,
                quickViewCriteria.getCategory(), productSubType, headers);
            if (!returnOnlyNumberOfMatches) {
                resultList = this.quickViewDao.getFundOfQuarter(productType, timeScale, quickViewCriteria.getCategory(),
                    productSubType, this.quickviewResultLimit, headers);
                totalNumberOfRecords = ListUtil.isValid(resultList) ? resultList.size() : 0;
            }
            break;
        }
        case hhhh_CPF_SRS: {
            List<ProductKey> productKeyList = getProdAltNumFromWebservicce(this.productKeyListWithEligibilityCheckService
                .getProductInfoList(locale, countryCode, groupMember, productType, productSubType, "quickview.cpfissrs", null));
            if (ListUtil.isValid(productKeyList)) {
                if (!returnOnlyNumberOfMatches) {
                    resultList = this.quickViewDao.searchFundByProductKeys(productKeyList, timeScale, headers);
                    totalNumberOfRecords = ListUtil.isValid(resultList) ? resultList.size() : 0;
                } else {
                    totalNumberOfRecords = this.quickViewDao.searchFundByProductKeysCount(productKeyList, timeScale, headers);
                }
            }
            break;
        }
        case hhhh_MONTH_INV_PLAN: {
            List<ProductKey> productKeyList = getProdAltNumFromWebservicce(this.productKeyListWithEligibilityCheckService
                .getProductInfoList(locale, countryCode, groupMember, productType, productSubType,
                    "quickview.monthlyinvestmentplan", null));
            if (ListUtil.isValid(productKeyList)) {
                if (!returnOnlyNumberOfMatches) {
                    resultList = this.quickViewDao.searchFundByProductKeys(productKeyList, timeScale, headers);
                    totalNumberOfRecords = ListUtil.isValid(resultList) ? resultList.size() : 0;
                } else {
                    totalNumberOfRecords = this.quickViewDao.searchFundByProductKeysCount(productKeyList, timeScale, headers);
                }
            }
            break;
        }
        case hhhh_NEW_FUND: {
            String tableNameValue = this.newFundBestSellerCaseMap.get(QuickViewServiceImpl.hhhh_NEW_FUND);
            String key = request.getSiteKey().toUpperCase() + CommonConstants.SYMBOL_UNDERLINE + QuickViewServiceImpl.hhhh_NEW_FUND;
            String value = this.newFundBestSellerMap.get(key);
            if (StringUtil.isInvalid(value)) {
                value = this.newFundBestSellerMap.get(QuickViewServiceImpl.DEFAULT + CommonConstants.SYMBOL_UNDERLINE
                    + QuickViewServiceImpl.hhhh_NEW_FUND);
            }
            resultList = getResultList(request, productSubType, timeScale, tableNameValue, value,currency,prodStatCde);
            totalNumberOfRecords = ListUtil.isValid(resultList) ? resultList.size() : 0;
            break;
        }
        case hhhh_RESTRICTED_FUND: {
            List<ProductKey> productKeyList = getProdAltNumFromWebservicce(this.productKeyListWithEligibilityCheckService
                .getProductInfoList(locale, countryCode, groupMember, productType, productSubType, "quickview.restrictedfunds",
                    null));
            if (ListUtil.isValid(productKeyList)) {
                if (!returnOnlyNumberOfMatches) {
                    resultList = this.quickViewDao.searchFundByProductKeys(productKeyList, timeScale, headers);
                    totalNumberOfRecords = ListUtil.isValid(resultList) ? resultList.size() : 0;
                } else {
                    totalNumberOfRecords = this.quickViewDao.searchFundByProductKeysCount(productKeyList, timeScale, headers);
                }
            }
            break;
        }
        case hhhh_WORLDWIDE_FUND: {
            List<ProductKey> productKeyList = getProdAltNumFromWebservicce(this.productKeyListWithEligibilityCheckService
                .getProductInfoList(locale, countryCode, groupMember, productType, productSubType,
                    "quickview.hhhhworldwidehighlightfunds", null));
            if (ListUtil.isValid(productKeyList)) {
                if (!returnOnlyNumberOfMatches) {
                    resultList = this.quickViewDao.searchFundByProductKeys(productKeyList, timeScale, headers);
                    totalNumberOfRecords = ListUtil.isValid(resultList) ? resultList.size() : 0;
                } else {
                    totalNumberOfRecords = this.quickViewDao.searchFundByProductKeysCount(productKeyList, timeScale, headers);
                }
            }
            break;
        }
        case hhhh_CIE: {
            List<ProductKey> productKeyList = getProdAltNumFromWebservicce(this.productKeyListWithEligibilityCheckService
                .getProductInfoList(locale, countryCode, groupMember, productType, productSubType,
                    "quickview.capitalinvestmententrantschemehighlightfund", null));
            if (ListUtil.isValid(productKeyList)) {
                if (!returnOnlyNumberOfMatches) {
                    resultList = this.quickViewDao.searchFundByProductKeys(productKeyList, timeScale, headers);
                    totalNumberOfRecords = ListUtil.isValid(resultList) ? resultList.size() : 0;
                } else {
                    totalNumberOfRecords = this.quickViewDao.searchFundByProductKeysCount(productKeyList, timeScale, headers);
                }
            }
            break;
        }
        case hhhh_NO_SUBSCRIPTION_FEE: {
            List<ProductKey> productKeyList = getProdAltNumFromWebservicce(this.productKeyListWithEligibilityCheckService
                .getProductInfoList(locale, countryCode, groupMember, productType, productSubType,
                    "quickview.nosubscriptionfeehighlightfund", null));
            if (ListUtil.isValid(productKeyList)) {
                if (!returnOnlyNumberOfMatches) {
                    resultList = this.quickViewDao.searchFundByProductKeys(productKeyList, timeScale, headers);
                    totalNumberOfRecords = ListUtil.isValid(resultList) ? resultList.size() : 0;
                } else {
                    totalNumberOfRecords = this.quickViewDao.searchFundByProductKeysCount(productKeyList, timeScale, headers);
                }
            }
            break;
        }
        case TOP5_PERFORMERS: {
            if (!returnOnlyNumberOfMatches) {
                resultList = this.quickViewDao.getTop5PerformersFunds(productType, timeScale, quickViewCriteria.getCategory(),
                    productSubType, request.getChannelRestrictCode(), request.getRestrOnlScribInd(), request.getProdStatCde(),
                    topPerfmList,currency,this.quickviewResultLimit, headers);
                totalNumberOfRecords = ListUtil.isValid(resultList) ? resultList.size() : 0;
            } else {
                totalNumberOfRecords = this.quickViewDao.getTop5PerformersFundsTotalCount(productType,
                    quickViewCriteria.getCategory(), productSubType, request.getChannelRestrictCode(),
                    request.getRestrOnlScribInd(), request.getProdStatCde(), topPerfmList,currency, headers);
            }
            break;
        }
            case ESG_FUND: {

                if (!returnOnlyNumberOfMatches) {
                    resultList = this.quickViewDao.getEsgFund( timeScale ,request.getRestrOnlScribInd(),currency, prodStatCde, headers, this.quickviewResultLimit);
                    totalNumberOfRecords = ListUtil.isValid(resultList) ? resultList.size() : 0;
                }else{
                    totalNumberOfRecords = this.quickViewDao.getEsgFundCount(timeScale,request.getRestrOnlScribInd(),currency,prodStatCde, headers);
                }
                break;
            }
            case GBA_FUND: {

                if (!returnOnlyNumberOfMatches) {
                    resultList = this.quickViewDao.getGBAFund( timeScale ,request.getRestrOnlScribInd(),currency, prodStatCde, headers, this.quickviewResultLimit);
                    totalNumberOfRecords = ListUtil.isValid(resultList) ? resultList.size() : 0;
                }else{
                    totalNumberOfRecords = this.quickViewDao.getGBAFundCount(timeScale,request.getRestrOnlScribInd(),currency,prodStatCde, headers);
                }
                break;
            }
            case TOP_DIVIDEND_YIELD: {

                if (!returnOnlyNumberOfMatches) {
                    resultList = this.quickViewDao.getTopDividend(productType, timeScale, quickViewCriteria.getCategory(),
                            productSubType, request.getChannelRestrictCode(), request.getRestrOnlScribInd(), prodStatCde,
                            topPerfmList,currency,this.quickviewResultLimit, headers);
                    totalNumberOfRecords = ListUtil.isValid(resultList) ? resultList.size() : 0;
                }else{
                    totalNumberOfRecords = this.quickViewDao.getTopDividendCount(productType,timeScale,
                            quickViewCriteria.getCategory(), productSubType, request.getChannelRestrictCode(),
                            request.getRestrOnlScribInd(), prodStatCde, topPerfmList,currency, headers);
                }
                break;
            }
        default:
            break;
        }
        LogUtil.debug(QuickViewServiceImpl.class, CommonConstants.QUERY_DATA_FROM_DATABASE + JacksonUtil.beanToJson(resultList));

        QuickViewResponse response = converterToResponse(totalNumberOfRecords, resultList, localIndex, returnOnlyNumberOfMatches);

        if (ListUtil.isInvalid(resultList)) {
            LogUtil.warn(QuickViewServiceImpl.class, "QuickView is no record fund: Json: {}", object.toString());
            return response;
        }

        if (!returnOnlyNumberOfMatches && quickViewCriteria.getTableName() != QuickViewTableName.BP
                && quickViewCriteria.getTableName() != QuickViewTableName.TP && quickViewCriteria.getTableName() != QuickViewTableName.TOP_DIVIDEND_YIELD
                && quickViewCriteria.getTableName() != QuickViewTableName.TOP5_PERFORMERS && quickViewCriteria.getTableName() != QuickViewTableName.hhhh_BEST_SELLER) {
            Collections.sort(response.getQuickView().getItems(), (item1, item2) -> {
                // Sort by trailingTotalReturn in descending order.
                if (item1 == null && item2 == null) {
                    return 0;
                } else if (item1 == null) {
                    return 1;
                } else if (item2 == null) {
                    return -1;
                } else if (item1.getTrailingTotalReturn() == null && item2.getTrailingTotalReturn() == null) {
                    return 0;
                } else if (item1.getTrailingTotalReturn() == null) {
                    return 1;
                } else if (item2.getTrailingTotalReturn() == null) {
                    return -1;
                } else {
                    return item2.getTrailingTotalReturn().compareTo(item1.getTrailingTotalReturn());
                }
            });
        }

        QuickView quickView = response.getQuickView();
        if (!returnOnlyNumberOfMatches && quickView != null && QuickViewTableName.hhhh_BEST_SELLER.name().equals(tableName.name())) {
            List<QuickViewRespItem> resultItems = quickView.getItems();
            List<QuickViewRespItem> limitedResultItems = resultItems.subList(0, this.quickviewResultLimit < resultItems.size()
                ? this.quickviewResultLimit : resultItems.size());

            response.getQuickView().setItems(limitedResultItems);
            response.getQuickView().setTotalNumberOfRecords(limitedResultItems.size());

        }
        LogUtil.debug(QuickViewServiceImpl.class, "Exit from getQuickView() inside FundboImpl");
        return response;
    }

    private QuickViewResponse converterToResponse(final int totalNumberOfRecords, final List<Object[]> resultList,
        final int localIndex, final boolean returnOnlyNumberOfMatches) {
        QuickViewResponse response = new QuickViewResponse();
        QuickView quickView = new QuickView();
        quickView.setTotalNumberOfRecords(totalNumberOfRecords);
        if (!returnOnlyNumberOfMatches && ListUtil.isValid(resultList)) {
            List<QuickViewRespItem> itemList = new ArrayList<>();
            for (int i = 0; i < resultList.size(); i++) {
                QuickViewRespItem item = new QuickViewRespItem();
                item.setProdAltNum(MiscUtil.safeString(resultList.get(i)[0]));
                item.setProductType(MiscUtil.safeString(resultList.get(i)[1]));
                setNameByIndex(resultList, localIndex, item, i);

                item.setMarket(MiscUtil.safeString(resultList.get(i)[5]));
                item.setTrailingTotalReturn(MiscUtil.safeBigDecimalValue(resultList.get(i)[7]));
                item.setRiskLvlCde(MiscUtil.safeString(resultList.get(i)[8]));
                item.setProdCdeAltClassCde("M");
                setExtraFields(resultList, i, item);
                itemList.add(item);
                if (i == resultList.size() - 1 && resultList.get(i)[2] != null) {
                    Date lastUpdatedDate = (Date) resultList.get(i)[2];
                    quickView.setLastUpdatedDate(DateUtil.getSimpleDateFormat(lastUpdatedDate,
                        DateConstants.DateFormat_yyyyMMdd_withHyphen));
                }
            }
            quickView.setItems(itemList);
        }
        response.setQuickView(quickView);
        return response;
    }

    private static void setNameByIndex(List<Object[]> resultList, int localIndex, QuickViewRespItem item, int i) {
        if (0 == localIndex) {
            item.setName(MiscUtil.safeString(resultList.get(i)[3]));
        } else if (1 == localIndex) {
            item.setName(MiscUtil.safeString(resultList.get(i)[4]));
        } else if (2 == localIndex) {
            item.setName(MiscUtil.safeString(resultList.get(i)[9]));
        } else {
            item.setName(MiscUtil.safeString(resultList.get(i)[3]));
        }
    }

    private static void setExtraFields(List<Object[]> resultList, int i, QuickViewRespItem item) {
        if(resultList.get(i).length > 10){
            item.setCurrency(MiscUtil.safeString(resultList.get(i)[10]));
        }
        if(resultList.get(i).length > 11){
            item.setEsgInd(MiscUtil.safeString(resultList.get(i)[11]));
        }
        if(resultList.get(i).length > 12 ){
            item.setGbaInd(MiscUtil.safeString(resultList.get(i)[12]));
        }
        if(resultList.get(i).length > 13 ){
            item.setDividendYield(MiscUtil.safeString(resultList.get(i)[13]));
        }
        if(resultList.get(i).length > 14 ){
            item.setProdStatCde(MiscUtil.safeString(resultList.get(i)[14]));
        }
    }

    private List<Object[]> getResultList(final QuickViewRequest request, final String productSubType, final TimeScale timeScale,
        final String tableNameValue, final String value,final String currency,final String proStateCde) throws Exception {
        List<Object[]> resultList = new ArrayList<>();
        switch (value) {
        case DB:// get data from DB
            resultList = this.quickViewDao.searchFundFromdb(timeScale, tableNameValue, request,currency,proStateCde);
            break;
        case WEBSERVICE:// get data by webService
            List<ProductKey> productKeyList = getProdAltNumFromWebservicce(this.productKeyListWithEligibilityCheckService
                .getProductInfoList(request.getLocale(), request.getCountryCode(), request.getGroupMember(),
                    request.getProductType(), productSubType, tableNameValue, null));
            if (ListUtil.isValid(productKeyList)) {
                resultList = this.quickViewDao.searchFundsRemoveChanlCde(productKeyList, timeScale, request);
            }
            break;
        default:
            break;
        }
        return resultList;
    }

    private List<ProductKey> getProdAltNumFromWebservicce(final List<ProductInfo> productKeyObjectList) {
        List<ProductKey> productKeyList = new ArrayList<>();
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
}
