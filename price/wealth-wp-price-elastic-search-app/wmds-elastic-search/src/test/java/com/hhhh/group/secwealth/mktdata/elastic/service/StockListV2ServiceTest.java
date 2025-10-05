package com.hhhh.group.secwealth.mktdata.elastic.service;

import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.CustomizedEsIndexForProduct;
import org.apache.maven.surefire.shared.compress.utils.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchScrollHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockListV2ServiceTest {
    @Mock
    private ElasticsearchRestTemplate elasticsearchTemplate;
    @Mock
    private PredsrchCommonService predsrchCommonService;
    @InjectMocks
    private StockListV2Service underTest;

    @Nested
    class WhenFindingAllByMarketAndProductType {
        private final String MARKET = "US";
        private final String PRODUCT_TYPE = "SEC";

        @BeforeEach
        void setup() {
        }

        @Test
        void test_findAllByMarketAndProductType() {
            String indexName = "hk_hbap_202312130800_878_1721036966833";
            when(predsrchCommonService.getCurrentIndexName("HK_HBAP".toLowerCase())).thenReturn(indexName);
            SearchScrollHits<CustomizedEsIndexForProduct> normalSearchHits = mock(SearchScrollHits.class);
            when(elasticsearchTemplate.searchScrollStart(anyLong(), any(Query.class), any(Class.class), any(IndexCoordinates.class)))
                    .thenReturn(normalSearchHits);
            when(normalSearchHits.hasSearchHits()).thenReturn(true);
            when(normalSearchHits.getScrollId()).thenReturn("123-123");
            List<CustomizedEsIndexForProduct> stockLists = Lists.newArrayList();
            CustomizedEsIndexForProduct product = new CustomizedEsIndexForProduct();
            product.setMarket("US");
            product.setSymbol("AAPL");
            product.setKey("AAPL.O");
            stockLists.add(product);
            SearchScrollHits<CustomizedEsIndexForProduct> scrollSearchHits = mock(SearchScrollHits.class);
            when(scrollSearchHits.hasSearchHits()).thenReturn(false);
            when(elasticsearchTemplate.searchScrollContinue(anyString(), anyLong(), any(Class.class), any(IndexCoordinates.class)))
                    .thenReturn(scrollSearchHits);
            try (MockedStatic<SearchHitSupport> searchHitSupport = mockStatic(SearchHitSupport.class)) {
                when(SearchHitSupport.unwrapSearchHits(normalSearchHits)).thenReturn(stockLists);
                when(SearchHitSupport.unwrapSearchHits(scrollSearchHits)).thenReturn(stockLists);
                assertEquals(2, underTest.findAllByMarketAndProductType(MARKET, PRODUCT_TYPE).getTotal());
            }

        }
    }
}
