package com.hhhh.group.secwealth.mktdata.elastic.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import com.hhhh.group.secwealth.mktdata.elastic.bean.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.CriteriaOperator;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.CustomizedEsIndexForProduct;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.MultiPredSrchRequest;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.elastic.component.ProductEntities;
import com.hhhh.group.secwealth.mktdata.elastic.properties.AppProperties;
import com.hhhh.group.secwealth.mktdata.elastic.properties.PredsrchSortingOrderProperties;
import com.hhhh.group.secwealth.mktdata.elastic.properties.SiteFeatureProperties;
import com.hhhh.group.secwealth.mktdata.elastic.util.CommonConstants;
import com.hhhh.group.secwealth.mktdata.elastic.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.elastic.util.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.elastic.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ParameterException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import static com.hhhh.group.secwealth.mktdata.elastic.util.CommonConstants.*;

@Service
public class PredsrchCommonService {

    private static Logger logger = LoggerFactory.getLogger(PredsrchCommonService.class);

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Autowired
    private Client elasticsearchClient;

    @Autowired
    private SiteFeatureProperties siteFeatureProperty;

    @Autowired
    private AppProperties appProperty;

    @Autowired
    private ProductEntities productEntities;

    @Value("${predsrch.siteConvertRule}")
    private String siteConvertRule;

    @Autowired
    private PredsrchSortingOrderProperties predsrchSortingOrderProperties;

    private static final String WHITE_SPACE_PATTERN = "\\u0020";

    public static final int NEEDED_RECORD_NUMBER = 10;

    public static final int EXCEED_TOPNUM_NEEDRECORD = 300;

    private static final String SPECIALCHAR_REGEX = "[*]";

    public static final String UNDERSCORE = "_";

    public static final String COLON = ":";

    public static final String HK_LOCALE_EN = "HK.en";

    public static final String HK_LOCALE_ZH_HK = "HK.zh_HK";

    public boolean isWildcard(final String keyword) {
        Pattern p = Pattern.compile(CommonConstants.PATTERN_SPECIAL_CHARACTERS);
        Matcher m = p.matcher(keyword.replace(CommonConstants.SPACE, CommonConstants.EMPTY_STRING));
        return !m.find();
    }

    public boolean isSpecialChar(final String str) {
        Matcher m = Pattern.compile(SPECIALCHAR_REGEX).matcher(str);
        return m.find();
    }

    public List<PredSrchResponse> multiPredsrch(final MultiPredSrchRequest request, final CommonRequestHeader header) {
        String[] keyword = request.getKeyword();
        String countryCode = header.getCountryCode();
        String groupMember = header.getGroupMember();
        String locale = header.getLocale();
        String[] assetClasses = request.getAssetClasses();
        String site = StringUtil.combineWithUnderline(countryCode, groupMember);
        if (null != keyword && keyword.length > 0) {
            for (int i = 0; i < keyword.length; i++) {
                if (isWildcard(keyword[i]) || isSpecialChar(keyword[i])) {
                    logger.error("MultiPredictiveSearchServiceImpl keyword isWildcard, Request message: {}", request);
                    return Collections.emptyList();
                }
            }
        }

        int needRecords = PredsrchCommonService.NEEDED_RECORD_NUMBER;
        if (request.getNeedRecords() > 0) {
            needRecords = request.getNeedRecords();
        }
        String topNum = request.getTopNum();
        if (StringUtil.isValid(topNum)) {
            needRecords = Integer.valueOf(topNum);
        }

        if (needRecords > PredsrchCommonService.EXCEED_TOPNUM_NEEDRECORD) {
            logger.error("invalid params needRecords:{} or topNum:{}", request.getNeedRecords(), Integer.valueOf(topNum));
            throw new ParameterException(ExCodeConstant.EX_CODE_INPUT_PARAMETER_INVALID);
        }

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        List<SearchCriteria> filterCriterias = request.getFilterCriterias();
        if (ListUtil.isValid(filterCriterias)) {
            boolQuery = buildSearchQuery(filterCriterias);
        }
        // predSearch market
        if (StringUtil.isValid(request.getMarket())) {
            boolQuery.must(QueryBuilders.matchPhraseQuery(PredictiveSearchConstant.COUNTRY_TRADEABLE_CODE,
                    toQueryString(request.getMarket())));
        }

        BoolQueryBuilder shouldBoolQuery = QueryBuilders.boolQuery();
        shouldBoolQuery.should(QueryBuilders.termsQuery(PredictiveSearchConstant.SYMBOL, Arrays.asList(keyword)))
                .should(QueryBuilders.termsQuery(PredictiveSearchConstant.KEY, Arrays.asList(keyword)))
                .should(QueryBuilders.termsQuery(PredictiveSearchConstant.PROD_CODE, Arrays.asList(keyword)));
        boolQuery.must(shouldBoolQuery);
        if (!CommonConstants.ALL.equals(assetClasses[0])) {
            buildAssetClassFilter(assetClasses, boolQuery);
        }
        Set<String> resultString = new HashSet<>();
        List<CustomizedEsIndexForProduct> resultList = new ArrayList<>();
        List<CustomizedEsIndexForProduct> tempList;
        Sort sort = Sort.by(Sort.Order.asc(PredictiveSearchConstant.PRODUCTTYPE_WEIGHT),
                Sort.Order.desc(PredictiveSearchConstant.COUNTRYTRADABLECOD_EWEIGHT), Sort.Order.asc(PredictiveSearchConstant.SYMBOL));
        IndexCoordinates indexName = IndexCoordinates.of(getCurrentIndexName(site.toLowerCase()));
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(boolQuery)
                .withPageable(PageRequest.of(0, needRecords, sort)).build();
        SearchHits<CustomizedEsIndexForProduct> sampleEntities =
                this.elasticsearchTemplate.search(searchQuery, CustomizedEsIndexForProduct.class, indexName);
        tempList = (List) SearchHitSupport.unwrapSearchHits(sampleEntities);
        mergeResults(resultString, resultList, tempList, needRecords);

        return transformResponse(countryCode, locale, resultList, request.getChannelRestrictCode());
    }

    // get customized product by market and symbols
    public List<CustomizedEsIndexForProduct> customizedEsIndexForProductSearch(String market, List<String> symbols) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.matchPhraseQuery(PredictiveSearchConstant.COUNTRY_TRADEABLE_CODE,
                toQueryString(market)));
        BoolQueryBuilder shouldBoolQuery = QueryBuilders.boolQuery();
        shouldBoolQuery.should(QueryBuilders.termsQuery(PredictiveSearchConstant.SYMBOL, symbols));
        boolQuery.must(shouldBoolQuery);
        boolQuery.must(QueryBuilders.matchPhraseQuery(addProductType("SEC"), toQueryString("SEC")));
        Set<String> resultString = new HashSet<>();
        List<CustomizedEsIndexForProduct> resultList = new ArrayList<>();
        Sort sort = Sort.by(Sort.Order.asc(PredictiveSearchConstant.PRODUCTTYPE_WEIGHT),
                Sort.Order.desc(PredictiveSearchConstant.COUNTRYTRADABLECOD_EWEIGHT), Sort.Order.asc(PredictiveSearchConstant.SYMBOL));
        IndexCoordinates indexName = IndexCoordinates.of(this.getCurrentIndexName("HK_HBAP".toLowerCase()));
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(boolQuery)
                .withPageable(PageRequest.of(0, symbols.size(), sort)).build();
        SearchHits<CustomizedEsIndexForProduct> sampleEntities =
                this.elasticsearchTemplate.search(searchQuery, CustomizedEsIndexForProduct.class, indexName);
        List<CustomizedEsIndexForProduct> tempList = (List) SearchHitSupport.unwrapSearchHits(sampleEntities);
        mergeResults(resultString, resultList, tempList, symbols.size());
        return resultList;
    }

    public String getCurrentIndexName(String siteKey) {
        Iterator<String> keysIt = this.elasticsearchClient.admin().cluster().prepareState().get().getState()
                .getMetaData().getIndices().keysIt();
        if (StringUtils.isNotBlank(this.siteConvertRule) && this.siteConvertRule.contains(siteKey)) {
            siteKey = this.siteConvertRule.substring(this.siteConvertRule.indexOf(PredsrchCommonService.COLON) + 1);
        }
        String currentIndexName = "";
        long currentStamp = 0;
        while (keysIt.hasNext()) {
            String indexName = keysIt.next();
            if (indexName.contains(siteKey)) {
                long tmpStamp = Long.parseLong(indexName.substring(indexName.lastIndexOf(PredsrchCommonService.UNDERSCORE) + 1));
                if (currentStamp == 0) {
                    currentStamp = tmpStamp;
                    currentIndexName = indexName;
                } else {
                    if (currentStamp > tmpStamp) {
                        currentStamp = tmpStamp;
                        currentIndexName = indexName;
                    }
                }
            }
        }
        if (!StringUtil.isInvalid(currentIndexName)) {
            return currentIndexName;
        } else {
            PredsrchCommonService.logger.error("no index doc in {}", siteKey);
            throw new ParameterException(ExCodeConstant.INDEX_DATA_NOT_EXIST);
        }
    }

    public List<PredSrchResponse> predsrch(final PredSrchRequest request, final CommonRequestHeader header) {
        Integer num = PredsrchCommonService.NEEDED_RECORD_NUMBER;
        num = this.checkRequestTopNum(request, num);
        String keyword = request.getKeyword().toLowerCase();
        String countryCode = header.getCountryCode();
        String groupMember = header.getGroupMember();
        String locale = header.getLocale();
        String[] assetClasses = request.getAssetClasses();
        String site = StringUtil.combineWithUnderline(countryCode, groupMember);
        if (StringUtils.isNotEmpty(keyword)) {
            if (checkValidKeyword(keyword)) {
                PredsrchCommonService.logger.error("PredictiveSearchServiceImpl keyword isWildcard, Request message: {}", request);
                return Collections.emptyList();
            } else {
                keyword = keyword.toLowerCase();
            }
        }
        Set<String> resultString = new HashSet<>();
        List<CustomizedEsIndexForProduct> resultList = new ArrayList<>();

        List<SearchCriteria> filterCriteria;
        try {
            filterCriteria = this.updateRequestFilterCriteria(request, header, site);
        } catch (Exception e) {
            return Collections.emptyList();
        }

        BoolQueryBuilder boolFilterQuery = QueryBuilders.boolQuery();
        if (ListUtil.isValid(filterCriteria)) {
            boolFilterQuery = buildSearchQuery(filterCriteria);
        }

        // predSearch market
        if (StringUtil.isValid(request.getMarket())) {
            boolFilterQuery.must(QueryBuilders.matchPhraseQuery(PredictiveSearchConstant.COUNTRY_TRADEABLE_CODE,
                    toQueryString(request.getMarket())));
        }

        // house View indicator
        this.updateFilterQueryForHouseView(request, boolFilterQuery);
        // predSearch filter "channelRestrictCode"
        if (StringUtil.isValid(request.getChannelRestrictCode())) {
            boolFilterQuery.mustNot(QueryBuilders.matchPhraseQuery(PredictiveSearchConstant.RESCHANNELCDE,
                    toQueryString(request.getChannelRestrictCode())));
        }

        // predSearch filter "restrOnlScribInd"
        if (StringUtil.isValid(request.getRestrOnlScribInd())) {
            boolFilterQuery.mustNot(QueryBuilders.matchPhraseQuery(PredictiveSearchConstant.RESTRONLSCRIBIND,
                    toQueryString(request.getRestrOnlScribInd())));
        }

        /// predSearch CMB/WPB fund
        String channelId = getChannelId(header,request,assetClasses);
        seperateCMBandWPB(boolFilterQuery,channelId);

        if (!CommonConstants.ALL.equals(assetClasses[0])) {
            buildAssetClassFilter(assetClasses, boolFilterQuery);
        }

        this.findAndMergeResult(num, keyword, assetClasses, site, resultString, resultList, boolFilterQuery);

        return transformResponse(countryCode, locale, resultList, channelId);
    }

    private List<SearchCriteria> updateRequestFilterCriteria(PredSrchRequest request, CommonRequestHeader header, String site) {
        List<SearchCriteria> filterCriteria = request.getFilterCriterias();
        if (ListUtil.isValid(filterCriteria)) {
            List<SearchCriteria> moreCriteria = new ArrayList<>();
            String countryCode = header.getCountryCode();
            String locale = header.getLocale();
            for (SearchCriteria criteria : filterCriteria) {
                if (PredictiveSearchConstant.SWITCHOUTFUND.equalsIgnoreCase(criteria.getCriteriaKey())) {
                    // prodAltCode|Symbol|countryTradableCode|productType
                    String[] fields = criteria.getCriteriaValue().split(CommonConstants.SYMBOL_COLON);
                    CustomizedEsIndexForProduct utObject = this.getBySymbolCountryTradableCode(site, countryCode, fields[1], fields[2], fields[3], locale);
                    if (null != utObject) {
                        // DEFAULT.predictiveSearch.switchOutFund=switchableGroup
                        String switchOutFund = this.getSwitchOutFund(site);
                        // UK(GB_HBEU Feature) allowSwOutProdInd
                        this.updateCriteriaBySwitchOutFund(header, site, moreCriteria, criteria, fields, utObject, switchOutFund);
                    } else {
                        PredsrchCommonService.logger.error("No record found for theRequest {}", request);
                        throw new ParameterException("No record found");
                    }
                }
            }
            filterCriteria.addAll(moreCriteria);
        }
        return filterCriteria;
    }

    private void seperateCMBandWPB(BoolQueryBuilder boolFilterQuery,String channel) {
        if (channel.equals(CHANNEL_CMB_I)) {
            boolFilterQuery.mustNot(QueryBuilders.matchPhraseQuery(PredictiveSearchConstant.RESTRCMBONLSRCHIND, toQueryString(Y)));
            boolFilterQuery.must(QueryBuilders.matchPhraseQuery(PredictiveSearchConstant.CMBPRODIND, toQueryString(Y)));
        } else if (channel.equals(CHANNEL_CMB_B)) {
            boolFilterQuery.must(QueryBuilders.matchPhraseQuery(PredictiveSearchConstant.CMBPRODIND, toQueryString(Y)));
        } else {
            boolFilterQuery.mustNot(QueryBuilders.matchPhraseQuery(PredictiveSearchConstant.WPBPRODIND, toQueryString(NO)));
        }

    }

    private String getChannelId(CommonRequestHeader header,PredSrchRequest request, String[] assetClasses) {
        String channelId = "";
        if (assetClasses[0].equalsIgnoreCase(CommonConstants.ASSETCLASSES_UT)) {
            if (StringUtil.isValid(header.getGbgf()) && header.getGbgf().equals(CMB) && header.getChannelId().equals(CHANNEL_OHI)) {
                channelId = CHANNEL_CMB_I;
            } else if (StringUtil.isValid(header.getGbgf()) && header.getGbgf().equals(CMB) && header.getChannelId().equals(CHANNEL_OHB)) {
                channelId = CHANNEL_CMB_B;
            } else if (StringUtil.isValid(request.getChannelRestrictCode())) {
                channelId = request.getChannelRestrictCode();
            }
        }
        return channelId;
    }


    private void findAndMergeResult(Integer num, String keyword, String[] assetClasses, String site, Set<String> resultString, List<CustomizedEsIndexForProduct> resultList, BoolQueryBuilder boolFilterQuery) {
        List<CustomizedEsIndexForProduct> tempList;
        if (this.appProperty.isSedolQuery()) {
            for (int i = 0; i < 12; i++) {
                if (resultList.size() < num) {
                    tempList = findProductByStep(keyword, i, boolFilterQuery, site, num, assetClasses);
                    mergeResults(resultString, resultList, tempList, num);
                }
            }
        } else {
            for (int i = 0; i < 6; i++) {
                if (resultList.size() < num) {
                    tempList = findProductByStep(keyword, i, boolFilterQuery, site, num, assetClasses);
                    mergeResults(resultString, resultList, tempList, num);
                }
            }
        }
    }

    private boolean checkValidKeyword(String keyword) {
        return (isWildcard(keyword) || isSpecialChar(keyword));
    }

    private void updateCriteriaBySwitchOutFund(CommonRequestHeader header, String site, List<SearchCriteria> moreCriterias, SearchCriteria criteria, String[] fields, CustomizedEsIndexForProduct utObject, String switchOutFund) {
        if (PredictiveSearchConstant.SWITCHOUTFUND_ALLOWSWOUTPRODIND.equals(switchOutFund)) {
            if (null != utObject.getAllowSwOutProdInd()
                    && CommonConstants.YES.equalsIgnoreCase(utObject.getAllowSwOutProdInd())) {
                SearchCriteria productCriteria =
                        new SearchCriteria("productKey", criteria.getCriteriaValue(), CriteriaOperator.NE_OPERATOR);
                moreCriterias.add(productCriteria);
                criteria.setCriteriaKey(PredictiveSearchConstant.ALLOWSWIN_FUND);
                criteria.setCriteriaValue(utObject.getAllowSwOutProdInd());
            } else {
                PredsrchCommonService.logger.error("The product: {} not support switchOut", fields[2]);
                throw new ParameterException("Product not support switchOut");
            }
            // Core Feature
        } else if (PredictiveSearchConstant.SWITCHOUTFUND_SWITCHABLEGROUP.equals(switchOutFund) && ListUtil.isValid(utObject.getSwitchableGroups())) {
            SearchCriteria productCriteria = new SearchCriteria(PredictiveSearchConstant.PRODUCT_KEY,
                    criteria.getCriteriaValue(), CriteriaOperator.NE_OPERATOR);
            moreCriterias.add(productCriteria);
            criteria.setCriteriaKey(PredictiveSearchConstant.SWITCHOUTFUND_SWITCHABLEGROUP);
            criteria.setCriteriaValue(
                    StringUtils.join(utObject.getSwitchableGroups().toArray(), CommonConstants.SYMBOL_COLON));
            if (utObject.getSwitchableGroups().size() > 1) {
                criteria.setOperator(CriteriaOperator.IN_OPERATOR);
            }
            addCriteriasByUnSwitchOutFund(header, site, moreCriterias, utObject);
        }
    }

    private String getSwitchOutFund(String site) {
        String switchOutFund = this.siteFeatureProperty.getStringDefaultFeature(site, PredictiveSearchConstant.SWITCHOUTFUND);
        if (StringUtil.isInvalid(switchOutFund)) {
            switchOutFund = PredictiveSearchConstant.SWITCHOUTFUND_SWITCHABLEGROUP;
        }
        return switchOutFund;
    }

    private void updateFilterQueryForHouseView(PredSrchRequest request, BoolQueryBuilder boolFilterQuery) {
        if (Boolean.TRUE.equals(request.isHouseViewFlag())) {
            boolFilterQuery.must(QueryBuilders.matchPhraseQuery(PredictiveSearchConstant.HOUSE_VIEW_INDICATOR, "Y"));
            // house View rating
            if (StringUtil.isValid(request.getHouseViewFilter())) {
                if (!"RU".equalsIgnoreCase(request.getHouseViewFilter())) {
                    boolFilterQuery.must(QueryBuilders.matchPhraseQuery(PredictiveSearchConstant.HOUSE_VIEW_RATING, request.getHouseViewFilter()));
                } else {
                    boolFilterQuery.must(QueryBuilders.matchPhraseQuery(PredictiveSearchConstant.HOUSE_VIEW_RECENT_UPDATE, "Y"));
                }
            }
        }
    }

    private void addCriteriasByUnSwitchOutFund(CommonRequestHeader header, String site, List<SearchCriteria> moreCriterias, CustomizedEsIndexForProduct utObject) {
        String unSwitchOutFund = this.siteFeatureProperty.getStringDefaultFeature(site,
                PredictiveSearchConstant.UNSWITCHOUTFUND);
        if (StringUtil.isInvalid(unSwitchOutFund)) {
            unSwitchOutFund = PredictiveSearchConstant.SWITCHOUTFUND_UNSWITCHABLELIST;
        }
        if (PredictiveSearchConstant.SWITCHOUTFUND_UNSWITCHABLELIST.equals(unSwitchOutFund)) {
            List<String> fundUnswithSegs = utObject.getFundUnswithSeg();
            if (null != fundUnswithSegs) {
                for (int i = 0; i < fundUnswithSegs.size(); i++) {
                    SearchCriteria unswithProduct = new SearchCriteria(PredictiveSearchConstant.PRODUCT_KEY,
                            CommonConstants.MONTH_PERIOD + ":" + fundUnswithSegs.get(i) + ":"
                                    + header.getCountryCode() + ":" + PredictiveSearchConstant.PRODUCT_TYPE_FUND,
                            CriteriaOperator.NE_OPERATOR);
                    moreCriterias.add(unswithProduct);
                }
            }
        }
    }

    private Integer checkRequestTopNum(PredSrchRequest request, Integer num) {
        if (!StringUtil.isInvalid(request.getTopNum())) {
            num = Integer.valueOf(request.getTopNum());
        }
        if (null != num && num > PredsrchCommonService.EXCEED_TOPNUM_NEEDRECORD) {
            logger.error("invalid param topNum: {}", num);
            throw new ParameterException(ExCodeConstant.EX_CODE_INPUT_PARAMETER_INVALID);
        }
        return num;
    }

    private void updateAllowBuySellInd(final CustomizedEsIndexForProduct obj, final PredSrchResponse info,
                                       final String chanlRestCde) {
        List<String> allowBuyList = obj.getChanlAllowBuy();
        List<String> sellSellList = obj.getChanlAllowSell();

        if (StringUtil.isValid(chanlRestCde) && (ListUtil.isValid(allowBuyList) || ListUtil.isValid(sellSellList))) {
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

        if (StringUtil.isInvalid(info.getAllowBuy())) info.setAllowBuy(obj.getAllowBuy());
        if (StringUtil.isInvalid(info.getAllowSell())) info.setAllowSell(obj.getAllowSell());
    }

    private void updateSwitchInd(final CustomizedEsIndexForProduct obj, final PredSrchResponse info,
                                       final String chanlRestCde) {
        List<String> allowSwitchIn = obj.getChanlAllowSwitchIn();
        List<String> allowSwitchOut = obj.getChanlAllowSwitchOut();
        if (StringUtil.isValid(chanlRestCde) && (ListUtil.isValid(allowSwitchIn) || ListUtil.isValid(allowSwitchOut))) {
                for (int i = 0; i < allowSwitchIn.size(); i = i + 2) {
                    if (allowSwitchIn.get(i).equals(chanlRestCde)) {
                        info.setAllowSwInProdInd(allowSwitchIn.get(i + 1));
                        break;
                    }
                }
                for (int i = 0; i < allowSwitchOut.size(); i = i + 2) {
                    if (allowSwitchOut.get(i).equals(chanlRestCde)) {
                        info.setAllowSwOutProdInd(allowSwitchOut.get(i + 1));
                        break;
                    }
                }
        }
        if (StringUtil.isInvalid(info.getAllowSwInProdInd())) info.setAllowSwInProdInd(obj.getAllowSwInProdInd());
        if (StringUtil.isInvalid(info.getAllowSwOutProdInd())) info.setAllowSwOutProdInd(obj.getAllowSwOutProdInd());
    }

    /**
     *
     * @param index
     * @param objList
     * @param channelRestrictCode
     * @return
     */
    private List<PredSrchResponse> transformResponse(final String countryCode, final String locale, final List<CustomizedEsIndexForProduct> objList,
                                                     final String channelRestrictCode) {
        String key = StringUtil.combineWithDot(countryCode, locale);
        int index = this.appProperty.getNameByIndex(key);

        List<PredSrchResponse> list = new ArrayList<>();
        for (CustomizedEsIndexForProduct obj : objList) {
            PredSrchResponse info = new PredSrchResponse();

            updateAllowBuySellInd(obj, info, channelRestrictCode);
            updateSwitchInd(obj, info, channelRestrictCode);
            info.setAssetCountries(obj.getAssetCountries());//
            info.setAssetSectors(obj.getAssetSectors());//
            info.setCountryTradableCode(obj.getCountryTradableCode());
            info.setExchange(obj.getExchange());
            info.setParentAssetClasses(obj.getParentAssetClasses());//
            // Note: is it enough
            List<ProdAltNumSeg> segList = obj.getProdAltNumSegs();
            // this is added by optimism
            if (obj.getProductCode() != null) {
                ProdAltNumSeg seg = new ProdAltNumSeg();
                seg.setProdAltNum(obj.getProductCode());
                seg.setProdCdeAltClassCde(PredictiveSearchConstant.PROD_ALT_CLASS_CDE_W);
                segList.add(seg);
            }
            info.setProdAltNumSegs(segList);
            info.setSymbol(obj.getSymbol());
            for (ProdAltNumSeg seg : segList) {
                if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_T.equals(seg.getProdCdeAltClassCde())) {
                    info.setRic(seg.getProdAltNum());
                    break;
                }
            }
            info.setProductSubType(obj.getProductSubType());
            info.setProductCcy(obj.getProductCcy());
            info.setMarket(obj.getMarket());
            info.setIslmProdInd(obj.getIslmProdInd());
            info.setFundHouseCde(obj.getFundHouseCde());
            info.setBondIssuer(obj.getIsrBndNme());// bond value
            info.setProductCode(obj.getProductCode());
            info.setProdCode(obj.getProdCode()); // for P code
            info.setAllowSellMipProdInd(obj.getAllowSellMipProdInd());
            info.setInvstMipCutOffDayofMth(obj.getInvstMipCutOffDayofMth());
            info.setInvstMipMinAmt(obj.getInvstMipMinAmt());
            info.setInvstMipIncrmMinAmt(obj.getInvstMipIncrmMinAmt());
            info.setAllowTradeProdInd(obj.getAllowTradeProdInd());
            info.setProdTaxFreeWrapActStaCde(obj.getProdTaxFreeWrapActStaCde());
            info.setSwithableGroups(obj.getSwitchableGroups());
            info.setRiskLvlCde(obj.getRiskLvlCde());
            info.setFundUnSwitchCode(obj.getFundUnswithSeg());
            info.setChannelRestrictList(obj.getResChannelCde());
            info.setChanlCdeList(obj.getChanlCde());
            info.setProdStatCde(obj.getProdStatCde());
            info.setRestrOnlScribInd(obj.getRestrOnlScribInd());
            info.setPiFundInd(obj.getPiFundInd());
            info.setDeAuthFundInd(obj.getDeAuthFundInd());
            info.setGbaAcctTrdb(obj.getGbaAcctTrdb());
            info.setGnrAcctTrdb(obj.getGnrAcctTrdb());
            // cmb ut
            info.setWpbProductInd(obj.getWpbProductInd());
            info.setCmbProductInd(obj.getCmbProductInd());
            info.setRestrCmbOnlSrchInd(obj.getRestrCmbOnlSrchInd());

            info.setHouseViewIndicator(obj.getHouseViewIndicator());
            info.setVaEtfIndicator(obj.getVaEtfIndicator());
            info.setHouseViewRecentUpdate(obj.getHouseViewRecentUpdate());
            info.setHouseViewRating(obj.getHouseViewRating());
            info.setProductType(obj.getProductType());
            this.setProductName(index, obj, info);

            // add premRecomInd
            info.setPremRecomInd(obj.getPremRecomInd());
            info.setEsgInd(obj.getEsgInd());
            list.add(info);
        }
        return list;
    }

    private void setProductName(int index, CustomizedEsIndexForProduct obj, PredSrchResponse info) {
        // Choose default language of product name if null for other
        // language
        String tempProdName = obj.getProductNameAnalyzed().get(index);
        if (null != tempProdName && !CommonConstants.EMPTY_STRING.equals(tempProdName)) {
            info.setProductName(tempProdName);
        } else {
            info.setProductName(obj.getProductName());
        }

        // Choose default language of product short name if null for other
        // language
        String tempProdShrtName = obj.getProductShrtNameAnalyzed().get(index);
        if (null != tempProdShrtName && !CommonConstants.EMPTY_STRING.equals(tempProdShrtName)) {
            info.setProductShortName(tempProdShrtName);
        } else {
            info.setProductShortName(obj.getProductShortName());
        }

        //return second name with T.Chinese name
        if (null != obj.getProductNameAnalyzed().get(this.appProperty.getNameByIndex(PredsrchCommonService.HK_LOCALE_ZH_HK))) {
            info.setProductSecondName(obj.getProductNameAnalyzed().get(this.appProperty.getNameByIndex(PredsrchCommonService.HK_LOCALE_ZH_HK)));
        } else {
            info.setProductSecondName(obj.getProductNameAnalyzed().get(this.appProperty.getNameByIndex(PredsrchCommonService.HK_LOCALE_EN)));
        }

    }

    public BoolQueryBuilder buildAssetClassFilter(final String[] assetClasses, final BoolQueryBuilder boolQuery) {
        BoolQueryBuilder boolShouldQuery = QueryBuilders.boolQuery();
        if (assetClasses.length > 1) {
            for (String asset : assetClasses) {
                boolShouldQuery.should(QueryBuilders.matchPhraseQuery(addProductType(asset), toQueryString(asset)));
            }
            boolQuery.must(boolShouldQuery);
        } else {
            boolQuery.must(QueryBuilders.matchPhraseQuery(addProductType(assetClasses[0]), toQueryString(assetClasses[0])));
        }
        return boolQuery;
    }

    // productSubType need to change when save? query without type+subType
    // currently
    private String addProductType(final String productType) {
        int find = productType.indexOf(CommonConstants.SYMBOL_UNDERLINE);
        if (find != -1) {
            // The productSubType in index is productType+"_"+productSubType
            return PredictiveSearchConstant.PRODUCT_SUBTYPE_ANALYZED;
        } else {
            return PredictiveSearchConstant.PRODUCT_TYPE_ANALYZED;
        }
    }

    /**
     *
     * @param filterCriterias
     * @return
     */
    private BoolQueryBuilder buildSearchQuery(final List<SearchCriteria> filterCriterias) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        for (SearchCriteria criteria : filterCriterias) {
            if (criteria.getOperator().equalsIgnoreCase(CommonConstants.SYMBOL_EQUAL)
                    || criteria.getOperator().equalsIgnoreCase(CriteriaOperator.EQ_OPERATOR)) {
                String criteriaKey = criteria.getCriteriaKey();
                String criteriaValue = criteria.getCriteriaValue();
                if (REQUEST_PARAM_MARKET.equals(criteriaKey) && "US".equalsIgnoreCase(criteriaValue)) {
                    criteriaValue = "NA";
                }
                boolQuery.must(QueryBuilders.matchPhraseQuery(criteriaKey, toQueryString(criteriaValue)));
            } else if (criteria.getOperator().equalsIgnoreCase(CriteriaOperator.NE_OPERATOR)) {
                if (criteria.getCriteriaKey().equalsIgnoreCase(PredictiveSearchConstant.PRODUCT_KEY)) {
                    // prodCdeAltClassCde:prodAltNum:Market:productType
                    String[] fields = criteria.getCriteriaValue().split(CommonConstants.SYMBOL_COLON);
                    boolQuery.mustNot(QueryBuilders.boolQuery()
                            .must(QueryBuilders.matchPhraseQuery(PredictiveSearchConstant.SYMBOL, toQueryString(fields[1])))
                            .must(QueryBuilders.matchPhraseQuery(PredictiveSearchConstant.COUNTRY_TRADEABLE_CODE,
                                    toQueryString(fields[2])))
                            .must(QueryBuilders.matchPhraseQuery(addProductType(fields[3]), toQueryString(fields[3]))));
                } else {
                    String[] strs = this.getMarketStr(criteria);
                    boolQuery.mustNot(QueryBuilders.termsQuery(criteria.getCriteriaKey(), Arrays.asList(strs)));
                }
            } else if (criteria.getOperator().equalsIgnoreCase(CriteriaOperator.IN_OPERATOR)) {// in,lowercase
                String[] strs = this.getMarketStr(criteria);
                boolQuery.must(QueryBuilders.termsQuery(criteria.getCriteriaKey(), Arrays.asList(strs)));
            } else {
                PredsrchCommonService.logger.error("Unrecognized operator");
                throw new ParameterException(ExCodeConstant.INPUT_PARAMETER_INVALID);
            }
        }
        return boolQuery;
    }

    private String[] getMarketStr(SearchCriteria criteria) {
        String[] strs = criteria.getCriteriaValue().split(CommonConstants.SYMBOL_COLON);
        for (int i = 0; i < strs.length; i++) {
            strs[i] = strs[i].toLowerCase();
        }
        if (REQUEST_PARAM_MARKET.equals(criteria.getCriteriaKey())) {
            for (int i = 0; i < strs.length; i++) {
                if ("us".equalsIgnoreCase(strs[i])) {
                    strs[i] = "NA".toLowerCase();
                }
            }
        }
        return strs;
    }

    public List<CustomizedEsIndexForProduct> findProductByStep(final String key, final int step,
                                                               final BoolQueryBuilder boolFilterQuery, final String site, final int num, final String[] assetClasses) {
        Map<String, Map<String, String>> sortMap;
        if (assetClasses.length == 1 && assetClasses[0].equalsIgnoreCase(CommonConstants.ASSETCLASSES_UT)) {
            sortMap = this.predsrchSortingOrderProperties.getSortingField().get(CommonConstants.ASSETCLASSES_UT);
        } else {
            sortMap = this.predsrchSortingOrderProperties.getSortingField().get(site);
        }
        if (sortMap == null) {
            sortMap = this.predsrchSortingOrderProperties.getSortingField().get(PredictiveSearchConstant.SORTING_DEFAULT);
        }

        BoolQueryBuilder tempQuery = QueryBuilders.boolQuery();
        Sort sort = null;
        if (step < PredictiveSearchConstant.getSequence().length) {
            String sortMapKey = PredictiveSearchConstant.getSequence()[step];
            queryBuilder(sortMapKey, tempQuery, key, sortMap);
            sort = Sort.by(Sort.Direction.ASC,
                    sortMap.get(sortMapKey).get(PredictiveSearchConstant.SORTING_SEQUENCE).split(","));
        }

        IndexCoordinates index = IndexCoordinates.of(getCurrentIndexName(site.toLowerCase()));
        if (null != sort) {
            Query searchQuery = new NativeSearchQueryBuilder().withQuery(tempQuery.must(boolFilterQuery))
                    .withPageable(PageRequest.of(0, num, sort)).build();
            //.withIndices(getCurrentIndexName(site.toLowerCase()))
            elasticsearchTemplate.search(searchQuery, CustomizedEsIndexForProduct.class, index);
            SearchHits<CustomizedEsIndexForProduct> sampleEntities =
                    this.elasticsearchTemplate.search(searchQuery, CustomizedEsIndexForProduct.class, index);
            return (List) SearchHitSupport.unwrapSearchHits(sampleEntities);
        }
        return Lists.newArrayList();
    }

    private BoolQueryBuilder queryBuilder(final String order, final BoolQueryBuilder tempQuery, final String key,
                                          final Map<String, Map<String, String>> sortMap) {
        Map<String, String> sortingMap = sortMap.get(order);
        if (sortingMap.get(PredictiveSearchConstant.SORTING_OPERATOR).equals(PredictiveSearchConstant.SORTING_OPERATOR_EXACT)) {
            tempQuery.must(QueryBuilders.termQuery(sortingMap.get(PredictiveSearchConstant.SORTING_FIELD), key));
        } else if (sortingMap.get(PredictiveSearchConstant.SORTING_OPERATOR)
                .equals(PredictiveSearchConstant.SORTING_OPERATOR_CONTAINS)) {
            tempQuery.must(QueryBuilders.wildcardQuery(sortingMap.get(PredictiveSearchConstant.SORTING_FIELD), "*" + key + "*"));
        } else {
            tempQuery.must(QueryBuilders.prefixQuery(sortingMap.get(PredictiveSearchConstant.SORTING_FIELD), key));
        }
        return tempQuery;
    }

    private CustomizedEsIndexForProduct getBySymbolCountryTradableCode(final String site, final String countryCode,
                                                                       final String symbol, final String countryTradableCode, final String productType, final String locale) {
        CustomizedEsIndexForProduct utObj = null;
        if (StringUtil.isValid(symbol)) {
            BoolQueryBuilder tempQuery = QueryBuilders.boolQuery();
            PageRequest page = PageRequest.of(0, 1);
            tempQuery.must(QueryBuilders.termQuery(PredictiveSearchConstant.SYMBOL, toQueryString(symbol)))
                    .must(QueryBuilders.termQuery(PredictiveSearchConstant.PRODUCT_TYPE_ANALYZED, toQueryString(productType)))
                    .must(QueryBuilders.termQuery(PredictiveSearchConstant.COUNTRY_TRADEABLE_CODE, toQueryString(countryTradableCode)));
            IndexCoordinates indexName = IndexCoordinates.of(getCurrentIndexName(site.toLowerCase()));
            Query searchQuery = new NativeSearchQueryBuilder().withQuery(tempQuery)
                    .withPageable(page).build();
            SearchHits<CustomizedEsIndexForProduct> sampleEntities =
                    this.elasticsearchTemplate.search(searchQuery, CustomizedEsIndexForProduct.class, indexName);
            List<CustomizedEsIndexForProduct> resultList = (List) SearchHitSupport.unwrapSearchHits(sampleEntities);
            if (ListUtil.isValid(resultList)) {
                utObj = resultList.get(0);
                int index = 0;
                if (null != locale) {
                    String indexKey = StringUtil.combineWithDot(countryCode, locale);
                    // get product name by locale
                    // localeMapping=SG.en_US:0,CN.en_US:0,CN.zh_CN:1,CA.en_CA:0,CA.fr_CA:1,CA.zh_CA:2,GB.en_GB:0,HK.en_US:0,HK.zh_HK:1,HK.zh_CN:2
                    index = this.appProperty.getNameByIndex(indexKey);
                }
                String productName = utObj.getProductNameAnalyzed().get(index);
                String productShortName = utObj.getProductShrtNameAnalyzed().get(index);

                if (null != productName && !CommonConstants.EMPTY_STRING.equals(productName)) {
                    utObj.setProductName(productName);
                }
                if (null != productShortName && !CommonConstants.EMPTY_STRING.equals(productShortName)) {
                    utObj.setProductShortName(productShortName);
                }
            }
        }
        return utObj;
    }

    private String toQueryString(final String keyWord) {
        if (keyWord != null) {
            return StringUtils.replace(QueryParserBase.escape(keyWord), CommonConstants.SPACE, WHITE_SPACE_PATTERN)
                    .toLowerCase();
        }
        return null;
    }

    private void mergeResults(final Set<String> resultString, final List<CustomizedEsIndexForProduct> resultList,
                              final List<CustomizedEsIndexForProduct> tempList, final int needRecords) {
        for (CustomizedEsIndexForProduct ut : tempList) {
            String comString = new StringBuffer().append(ut.getSymbol()).append(ut.getProductType())
                    .append(ut.getCountryTradableCode()).toString();
            if (!resultString.contains(comString) && resultList.size() < needRecords) {
                resultString.add(comString);
                resultList.add(ut);
            }
        }
    }
}
