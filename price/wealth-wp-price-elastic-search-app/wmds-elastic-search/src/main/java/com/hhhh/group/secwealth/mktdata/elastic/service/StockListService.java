package com.hhhh.group.secwealth.mktdata.elastic.service;

import com.hhhh.group.secwealth.mktdata.elastic.bean.ai.response.StockListResponse;
import com.hhhh.group.secwealth.mktdata.elastic.bean.ai.stock.StockInfo;
import com.hhhh.group.secwealth.mktdata.elastic.bean.ai.stock.StockListRequest;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.CustomizedEsIndexForProduct;
import com.hhhh.group.secwealth.mktdata.elastic.properties.AppProperties;
import com.hhhh.group.secwealth.mktdata.elastic.util.CommonConstants;
import com.hhhh.group.secwealth.mktdata.elastic.util.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.elastic.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.assertj.core.util.Lists;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchScrollHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 45247946
 */
@Service
public class StockListService extends AbstractBaseService<StockListRequest, StockListResponse, CommonRequestHeader> {
    private static final String WHITE_SPACE_PATTERN = "\\u0020";
    private static final String ORG_REQUEST = "orgRequest";

    @Autowired
    PredsrchCommonService predsrchCommonService;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;
    @Autowired
    private AppProperties appProperty;
    @Override
    protected Object convertRequest(StockListRequest request, CommonRequestHeader header) throws Exception {
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER, header);
        ArgsHolder.putArgs(ORG_REQUEST, request);
        return request;
    }

    @Override
    protected Object execute(Object serviceRequest) throws Exception {
        StockListRequest request = (StockListRequest) serviceRequest;
        String[] assetClasses = request.getAssetClasses();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (assetClasses != null && assetClasses.length != 0 && !CommonConstants.ALL.equals(assetClasses[0])) {
            predsrchCommonService.buildAssetClassFilter(assetClasses, boolQuery);
        }
        /// predSearch market
        if (StringUtil.isValid(request.getMarket())) {
            boolQuery.must(QueryBuilders.matchPhraseQuery(PredictiveSearchConstant.COUNTRY_TRADEABLE_CODE,
                toQueryString(request.getMarket())));
        }
        IndexCoordinates indexName = IndexCoordinates.of(predsrchCommonService.getCurrentIndexName("HK_HBAP".toLowerCase()));
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(boolQuery)
            .withPageable(PageRequest.of(request.getPageNum(), request.getPageSize())).build();

        if(StringUtils.isNotEmpty(request.getScrollId())) {
            SearchScrollHits<CustomizedEsIndexForProduct> result
                    = this.elasticsearchTemplate.searchScrollContinue(request.getScrollId(), 60000, CustomizedEsIndexForProduct.class, indexName);
            if(!result.hasSearchHits()) {
                this.elasticsearchTemplate.searchScrollClear(Lists.newArrayList(request.getScrollId()));
            }
            return result;
        }
        return this.elasticsearchTemplate.searchScrollStart(60000, searchQuery, CustomizedEsIndexForProduct.class, indexName);
    }

    @Override
    protected Object validateServiceResponse(Object serviceResponse) throws Exception {
        return serviceResponse;
    }

    @Override
    protected StockListResponse convertResponse(Object validServiceResponse) throws Exception {
        CommonRequestHeader header = (CommonRequestHeader)ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
        StockListRequest request = (StockListRequest)ArgsHolder.getArgs(ORG_REQUEST);
        String countryCode = header.getCountryCode();
        String locale = header.getLocale();
        String key = StringUtil.combineWithDot(countryCode, locale);
        SearchScrollHits<CustomizedEsIndexForProduct> result = (SearchScrollHits<CustomizedEsIndexForProduct>)validServiceResponse;
        List<CustomizedEsIndexForProduct> orgStockLists = (List<CustomizedEsIndexForProduct>)SearchHitSupport.unwrapSearchHits(validServiceResponse);
        StockListResponse stockListResponse = new StockListResponse();
        stockListResponse.setTotal(result.getTotalHits());
        stockListResponse.setPageNumber(request.getPageNum());
        stockListResponse.setRecordsPerPage(request.getPageSize());
        stockListResponse.setScrollId(result.getScrollId());
        stockListResponse.setHasMore((request.getPageSize() == result.getSearchHits().size()) && result.hasSearchHits());
        List<StockInfo> stockInfoList = new ArrayList<>();
        int index = this.appProperty.getNameByIndex(key);
        for (CustomizedEsIndexForProduct orgStockInfo : orgStockLists) {
            StockInfo stockInfo = StockInfo.builder()
                .countryTradableCode(orgStockInfo.getCountryTradableCode())
                .prodAltNumSegs(orgStockInfo.getProdAltNumSegs())
                .productCode(orgStockInfo.getProductCode())
                .productName(orgStockInfo.getProductName())
                .symbol(orgStockInfo.getSymbol())
                .market(orgStockInfo.getMarket())
                .productCcy(orgStockInfo.getProductCcy())
                .productShortName(orgStockInfo.getProductShortName())
                .productSubType(orgStockInfo.getProductSubType())
                .productType(orgStockInfo.getProductType())
                .build();
            String tempProdName = orgStockInfo.getProductNameAnalyzed().get(index);
            if (StringUtils.isNotBlank(tempProdName)) {
                stockInfo.setProductName(tempProdName);
            } else {
                stockInfo.setProductName(orgStockInfo.getProductName());
            }
            // Choose default language of product short name if null for other
            // language
            String tempProdShrtName = orgStockInfo.getProductShrtNameAnalyzed().get(index);
            if (StringUtils.isNotBlank(tempProdShrtName)) {
                stockInfo.setProductShortName(tempProdShrtName);
            } else {
                stockInfo.setProductShortName(orgStockInfo.getProductShortName());
            }
            stockInfoList.add(stockInfo);
        }
        stockListResponse.setData(stockInfoList);
        return stockListResponse;
    }

    private String toQueryString(final String keyWord) {
        if (keyWord != null) {
            return StringUtils
                .replace(QueryParserBase.escape(keyWord), CommonConstants.SPACE, WHITE_SPACE_PATTERN)
                .toLowerCase();
        }
        return null;
    }
}
