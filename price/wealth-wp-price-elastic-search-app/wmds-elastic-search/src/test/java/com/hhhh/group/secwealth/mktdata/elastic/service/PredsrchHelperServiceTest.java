package com.hhhh.group.secwealth.mktdata.elastic.service;

import com.hhhh.group.secwealth.mktdata.elastic.bean.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.CustomizedEsIndexForProduct;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.InternalProductKey;
import com.hhhh.group.secwealth.mktdata.elastic.properties.AppProperties;
import org.elasticsearch.search.aggregations.Aggregations;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHitsImpl;
import org.springframework.data.elasticsearch.core.TotalHitsRelation;
import org.springframework.data.elasticsearch.core.query.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PredsrchHelperServiceTest {
    @Mock
    private ElasticsearchRestTemplate elasticsearchTemplate;
    @Mock
    private AppProperties appProperty;
    @Mock
    private PredsrchCommonService predsrchCommonService;
    @InjectMocks
    private PredsrchHelperService underTest;

    @Nested
    class WhenGettingBySymbolMarket {
        private final String LOCALE = "zh_HK";
        @Mock
        private InternalProductKey ipk;

        @Test
        void test_gettingBySymbolMarketByInternalProductKey() {
            when(ipk.getCountryCode()).thenReturn("HK");
            when(ipk.getProdAltNum()).thenReturn("00005");
            when(ipk.getCountryTradableCode()).thenReturn("HK");
            when(ipk.getGroupMember()).thenReturn("HBAP");
            when(ipk.getProductType()).thenReturn("SEC");
            when(predsrchCommonService.getCurrentIndexName("HK_HBAP".toLowerCase())).thenReturn("hk_hbap_202106290110_1923_1624962445646");
            CustomizedEsIndexForProduct content = new CustomizedEsIndexForProduct();
            content.setProdAltNumSegs(new ArrayList<>());
            content.setProductNameAnalyzed(Arrays.asList("123"));
            content.setProductShrtNameAnalyzed(Arrays.asList("123"));
            content.setAllowSwOutProdInd("Y");
            content.setVaEtfIndicator("N");
            List<ProdAltNumSeg> prodAltNumSegs = new ArrayList<>();
            ProdAltNumSeg prodAltNumSeg = new ProdAltNumSeg();
            prodAltNumSeg.setProdAltNum("0005.HK");
            prodAltNumSeg.setProdCdeAltClassCde("T");
            prodAltNumSegs.add(prodAltNumSeg);
            content.setProdAltNumSegs(prodAltNumSegs);
            SearchHit searchHit = new SearchHit("1", 1, new Object[]{1}, new HashMap<>(), content);
            SearchHitsImpl searchHits = new SearchHitsImpl(1L, TotalHitsRelation.EQUAL_TO, 1f, "1",
                    Arrays.asList(searchHit), new Aggregations(new ArrayList<>()));
            when(elasticsearchTemplate.search(any(Query.class), any(), any())).thenReturn(searchHits);
            assertNotNull(underTest.getBySymbolMarket(ipk, LOCALE));
        }

        @Test
        void test_gettingBySymbolMarket() {
            assertThrows(Exception.class, () -> underTest.getBySymbolMarket("HK", "hhhh", "00005", "HK", "SEC", LOCALE));
        }
    }

    @Nested
    class WhenToingQueryString {

        @Test
        void test_toingQueryString(){
            assertNotNull(underTest.toQueryString("00005"));
        }
    }

    @Nested
    class WhenGettingByRicCode {

        @Test
        void test_gettingByRicCode() {
            assertThrows(Exception.class,() -> underTest.getByRicCode("HK", "hhhh", "0005.HK", "zh_HK"));
        }
    }

    @Nested
    class WhenGettingByWCode {

        @Test
        void test_gettingByWCode() {
            assertThrows(Exception.class, () -> underTest.getByWCode("HK", "HBAP", "0005.HK", "zh_CN"));
        }
    }

    @Nested
    class WhenGettingByPCode {

        @Test
        void test_gettingByPCode() {
            assertThrows(Exception.class,() -> underTest.getByPCode("HK", "HBAP", "600050", "zh_CN"));
        }
    }
}
