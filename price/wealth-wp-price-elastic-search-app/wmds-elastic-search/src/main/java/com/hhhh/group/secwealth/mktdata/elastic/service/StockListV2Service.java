package com.hhhh.group.secwealth.mktdata.elastic.service;

import com.hhhh.group.secwealth.mktdata.elastic.bean.ai.response.StockListV2Response;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.CustomizedEsIndexForProduct;
import com.hhhh.group.secwealth.mktdata.elastic.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.elastic.util.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.elastic.util.StringUtil;
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

@Service
public class StockListV2Service {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Autowired
    private PredsrchCommonService predsrchCommonService;

    public StockListV2Response findAllByMarketAndProductType(String market, String productType) {
        String[] assetClasses = new String[]{productType};
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        this.predsrchCommonService.buildAssetClassFilter(assetClasses, boolQuery);
        boolQuery.must(QueryBuilders.matchPhraseQuery(PredictiveSearchConstant.COUNTRY_TRADEABLE_CODE, market));
        IndexCoordinates indexName = IndexCoordinates.of(this.predsrchCommonService.getCurrentIndexName("HK_HBAP".toLowerCase()));
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(boolQuery)
                .withPageable(PageRequest.of(0, 5000)).build();
        SearchScrollHits<CustomizedEsIndexForProduct> searchHits = this.executeSearch("", indexName, searchQuery);
        List<CustomizedEsIndexForProduct> stockLists = (List<CustomizedEsIndexForProduct>) SearchHitSupport.unwrapSearchHits(searchHits);
        List<String> scrollIds = new ArrayList<>();
        Boolean hasMore = searchHits.hasSearchHits();
        String scrollId = searchHits.getScrollId();

        while (Boolean.TRUE.equals(hasMore)) {
            SearchScrollHits<CustomizedEsIndexForProduct> scrollSearchHits = this.executeSearch(scrollId, indexName, searchQuery);
            List<CustomizedEsIndexForProduct> orgStockLists = (List<CustomizedEsIndexForProduct>)SearchHitSupport.unwrapSearchHits(scrollSearchHits);
            if (ListUtil.isValid(orgStockLists)) {
                stockLists.addAll(orgStockLists);
            }
            scrollIds.add(scrollId);
            if(!scrollSearchHits.hasSearchHits() && ListUtil.isValid(scrollIds)) {
                this.elasticsearchTemplate.searchScrollClear(scrollIds);
            }
            scrollId = scrollSearchHits.getScrollId();
            hasMore = scrollSearchHits.hasSearchHits();
        }

        return this.convertSearchHits(stockLists);
    }

    private StockListV2Response convertSearchHits(List<CustomizedEsIndexForProduct> stockLists) {
        if (ListUtil.isValid(stockLists)) {
            StockListV2Response stockListV2Response = new StockListV2Response();
            List<String> productList = new ArrayList<>();
            for (CustomizedEsIndexForProduct stock : stockLists) {
                if (null != stock) {
                    productList.add(stock.getKey());
                }
            }
            stockListV2Response.setTotal(Long.valueOf(productList.size()));
            stockListV2Response.setData(productList);
            return stockListV2Response;
        }
        return new StockListV2Response();
    }

    private SearchScrollHits<CustomizedEsIndexForProduct> executeSearch(String scrollId, IndexCoordinates indexName, Query searchQuery) {
        SearchScrollHits<CustomizedEsIndexForProduct> scrollSearchHits;
        if (StringUtil.isValid(scrollId)) {
            scrollSearchHits = this.elasticsearchTemplate.searchScrollContinue(scrollId, 60000, CustomizedEsIndexForProduct.class, indexName);
        } else {
            scrollSearchHits = this.elasticsearchTemplate.searchScrollStart(60000, searchQuery, CustomizedEsIndexForProduct.class, indexName);
        }
        return scrollSearchHits;
    }
}
