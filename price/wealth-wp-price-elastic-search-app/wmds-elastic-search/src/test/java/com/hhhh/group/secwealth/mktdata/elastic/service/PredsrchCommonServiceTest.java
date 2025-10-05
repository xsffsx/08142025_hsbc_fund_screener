package com.hhhh.group.secwealth.mktdata.elastic.service;

import com.hhhh.group.secwealth.mktdata.elastic.bean.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.*;
import com.hhhh.group.secwealth.mktdata.elastic.component.ProductEntities;
import com.hhhh.group.secwealth.mktdata.elastic.properties.AppProperties;
import com.hhhh.group.secwealth.mktdata.elastic.properties.PredsrchSortingOrderProperties;
import com.hhhh.group.secwealth.mktdata.elastic.properties.SiteFeatureProperties;
import com.hhhh.group.secwealth.mktdata.elastic.util.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ParameterException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.assertj.core.util.Lists;
import org.elasticsearch.cluster.metadata.MetaData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.elasticsearch.action.admin.cluster.state.ClusterStateRequestBuilder;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.search.aggregations.Aggregations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PredsrchCommonServiceTest {
    @Mock
    private ElasticsearchRestTemplate elasticsearchTemplate;
    @Mock
    private Client elasticsearchClient;
    @Mock
    private SiteFeatureProperties siteFeatureProperty;
    @Mock
    private AppProperties appProperty;
    @Mock
    private ProductEntities productEntities;
    @Mock
    private PredsrchSortingOrderProperties predsrchSortingOrderProperties;
    @InjectMocks
    private PredsrchCommonService underTest;

    private Map<String, Map<String, Map<String, String>>> sortingFieldMap() {
        Map<String, Map<String, Map<String, String>>> map1 = new HashMap<>();
        Map<String, Map<String, String>> map2 = new HashMap<>();
        Map<String, String> map3 = new HashMap<>();
        Map<String, String> map4 = new HashMap<>();
        Map<String, String> map5 = new HashMap<>();
        map1.put("DEFAULT", map2);
        map2.put("first", map3);
        map2.put("second", map4);
        map2.put("third", map5);
        map2.put("fourth", map5);
        map2.put("fifth", map5);
        map2.put("sixth", map5);
        map2.put("seventh", map5);
        map2.put("eighth", map5);
        map2.put("ninth", map5);
        map2.put("tenth", map5);
        map2.put("eleventh", map5);
        map2.put("twelfth", map5);
        map3.put("operator", "contains");
        map3.put("field", "name");
        map3.put("sequence", "haha,hehe");
        map4.put("operator", "exact");
        map4.put("field", "name");
        map4.put("sequence", "guigui");
        map5.put("operator", "contains");
        map5.put("sequence", "haha,hehe");
        map5.put("field", "name");
        return map1;
    }

    private SearchHits searchHits() {
        CustomizedEsIndexForProduct content = new CustomizedEsIndexForProduct();
        content.setAllowBuy("1");
        content.setProductCode("HSCS");
        content.setProdAltNumSegs(new ArrayList<>());
        content.setProductNameAnalyzed(Arrays.asList("123"));
        content.setProductShrtNameAnalyzed(Arrays.asList("123"));
        content.setAllowSwOutProdInd("Y");
        content.setVaEtfIndicator("N");
        content.setSwitchableGroups(Lists.newArrayList("xxx"));
        content.setFundUnswithSeg(Lists.newArrayList("xxx"));
        content.setAllowSwOutProdInd("Y");
        List<ProdAltNumSeg> prodAltNumSegs = new ArrayList<>();
        ProdAltNumSeg prodAltNumSeg = new ProdAltNumSeg();
        prodAltNumSeg.setProdAltNum("0005.HK");
        prodAltNumSeg.setProdCdeAltClassCde("T");
        prodAltNumSegs.add(prodAltNumSeg);
        content.setProdAltNumSegs(prodAltNumSegs);
        SearchHit searchHit = new SearchHit("1", 1, new Object[]{1}, new HashMap<>(), content);
        SearchHitsImpl searchHits = new SearchHitsImpl(1L, TotalHitsRelation.EQUAL_TO, 1f, "1",
                Arrays.asList(searchHit), new Aggregations(new ArrayList<>()));
        return searchHits;
    }

    private SearchHits utbProduct() {
        CustomizedEsIndexForProduct content = new CustomizedEsIndexForProduct();
        content.setChanlAllowSwitchOut(new ArrayList<String>(){{add("CMB_I");add("Y");}});
        content.setChanlAllowSwitchIn(new ArrayList<String>(){{add("CMB_I");add("Y");}});
        content.setChanlAllowSell(new ArrayList<String>(){{add("CMB_I");add("Y");}});
        content.setChanlAllowBuy(new ArrayList<String>(){{add("CMB_I");add("Y");}});
        content.setAllowBuy("Y");
        content.setAllowSell("Y");
        content.setProductCode("1000047408");
        content.setProdAltNumSegs(new ArrayList<>());
        content.setProductNameAnalyzed(Arrays.asList("123"));
        content.setProductShrtNameAnalyzed(Arrays.asList("123"));
        content.setAllowSwOutProdInd("Y");
        content.setAllowSwInProdInd("Y");
        content.setVaEtfIndicator("N");
        content.setSwitchableGroups(Lists.newArrayList("xxx"));
        content.setFundUnswithSeg(Lists.newArrayList("xxx"));
        List<ProdAltNumSeg> prodAltNumSegs = new ArrayList<>();
        ProdAltNumSeg prodAltNumSeg = new ProdAltNumSeg();
        prodAltNumSeg.setProdAltNum("U50001");
        prodAltNumSeg.setProdCdeAltClassCde("M");
        prodAltNumSegs.add(prodAltNumSeg);
        content.setProdAltNumSegs(prodAltNumSegs);
        SearchHit searchHit = new SearchHit("1", 1, new Object[]{1}, new HashMap<>(), content);
        SearchHitsImpl searchHits = new SearchHitsImpl(1L, TotalHitsRelation.EQUAL_TO, 1f, "1",
                Arrays.asList(searchHit), new Aggregations(new ArrayList<>()));
        return searchHits;
    }
    public void elasticsearchClientMock() {
        AdminClient adminClient = mock(AdminClient.class);
        ClusterAdminClient clusterAdminClient = mock(ClusterAdminClient.class);
        ClusterStateRequestBuilder stateRequestBuilder = mock(ClusterStateRequestBuilder.class);
        ClusterStateResponse response = mock(ClusterStateResponse.class);
        ClusterState clusterState = mock(ClusterState.class);
        MetaData metaData = mock(MetaData.class);
        ImmutableOpenMap.Builder<String, IndexMetaData> builder = ImmutableOpenMap.builder();
        builder.put("hk_hbap_202106290110_1923_1624962445646", mock(IndexMetaData.class));
        ImmutableOpenMap<String, IndexMetaData> map = builder.build();
        when(elasticsearchClient.admin()).thenReturn(adminClient);
        when(adminClient.cluster()).thenReturn(clusterAdminClient);
        when(clusterAdminClient.prepareState()).thenReturn(stateRequestBuilder);
        when(stateRequestBuilder.get()).thenReturn(response);
        when(response.getState()).thenReturn(clusterState);
        when(clusterState.getMetaData()).thenReturn(metaData);
        when(metaData.getIndices()).thenReturn(map);
    }

    @Nested
    class WhenCheckingIfIsWildcard {

        @Test
        void test_checkingIfIsWildcard(){
            assertFalse(underTest.isWildcard(".HSI"));
        }
    }

    @Nested
    class WhenMultiPredsrch {
        @Mock
        private MultiPredSrchRequest request;
        @Mock
        private CommonRequestHeader header;

        @ParameterizedTest
        @ValueSource(strings = {"in", "ne"})
        void test_multiPredsrch() {
            when(header.getCountryCode()).thenReturn("HK");
            when(header.getGroupMember()).thenReturn("HBAP");
            when(header.getLocale()).thenReturn("en_US");
            when(request.getMarket()).thenReturn("HK");
            when(request.getKeyword()).thenReturn(new String[]{"00005"});
            when(request.getAssetClasses()).thenReturn(new String[]{"SEC"});
            when(request.getTopNum()).thenReturn("10");
            List<SearchCriteria> criteriaList = new ArrayList<>();
            SearchCriteria searchCriteria = new SearchCriteria();
            searchCriteria.setCriteriaValue("HK:CN:US");
            searchCriteria.setCriteriaKey("market");
            searchCriteria.setOperator("in");
            criteriaList.add(searchCriteria);
            when(request.getFilterCriterias()).thenReturn(criteriaList);
            ReflectionTestUtils.setField(underTest, "siteConvertRule", "hk_hbap");
            doReturn(searchHits()).when(elasticsearchTemplate).search(any(Query.class), any(), any());
            elasticsearchClientMock();
            assertNotNull(underTest.multiPredsrch(request, header));
        }

        @Test
        void test_multiPredsrch_invalidKeyword() throws Exception {
            when(header.getCountryCode()).thenReturn("HK");
            when(header.getGroupMember()).thenReturn("HBAP");
            when(header.getLocale()).thenReturn("en_US");
            when(request.getKeyword()).thenReturn(new String[]{"?"});
            when(request.getAssetClasses()).thenReturn(new String[]{"SEC"});
            assertEquals(0, underTest.multiPredsrch(request, header).size());
        }

        @Test
        void test_multiPredsrch_exceedRecords() {
            when(header.getCountryCode()).thenReturn("HK");
            when(header.getGroupMember()).thenReturn("HBAP");
            when(header.getLocale()).thenReturn("en_US");
            when(request.getKeyword()).thenReturn(new String[]{"00005"});
            when(request.getAssetClasses()).thenReturn(new String[]{"SEC"});
            when(request.getNeedRecords()).thenReturn(1000);
            when(request.getTopNum()).thenReturn("1000");
            assertThrows(ParameterException.class, () -> underTest.multiPredsrch(request, header));
        }

        @Test
        void customizedEsIndexForProductSearch() {
            when(elasticsearchTemplate.search(any(Query.class), any(Class.class), any(IndexCoordinates.class))).thenReturn(searchHits());
            elasticsearchClientMock();
            assertNotNull(underTest.customizedEsIndexForProductSearch("HK", Lists.newArrayList("00001")));
        }
    }

    @Nested
    class WhenGettingCurrentIndexName {

        @Test
        void test_gettingCurrentIndexName(){
            AdminClient adminClient = mock(AdminClient.class);
            ClusterAdminClient clusterAdminClient = mock(ClusterAdminClient.class);
            ClusterStateRequestBuilder stateRequestBuilder = mock(ClusterStateRequestBuilder.class);
            ClusterStateResponse response = mock(ClusterStateResponse.class);
            ClusterState clusterState = mock(ClusterState.class);
            MetaData metaData = mock(MetaData.class);
            ImmutableOpenMap.Builder<String, IndexMetaData> builder = ImmutableOpenMap.builder();
            builder.put("hk_hbap_202106290110_1923_1624962445646", mock(IndexMetaData.class));
            ImmutableOpenMap<String, IndexMetaData> map = builder.build();
            when(elasticsearchClient.admin()).thenReturn(adminClient);
            when(adminClient.cluster()).thenReturn(clusterAdminClient);
            when(clusterAdminClient.prepareState()).thenReturn(stateRequestBuilder);
            when(stateRequestBuilder.get()).thenReturn(response);
            when(response.getState()).thenReturn(clusterState);
            when(clusterState.getMetaData()).thenReturn(metaData);
            when(metaData.getIndices()).thenReturn(map);
            assertEquals("hk_hbap_202106290110_1923_1624962445646", underTest.getCurrentIndexName("hk_hbap"));
        }
    }

    @Nested
    class WhenPredsrching {
        @Mock
        private PredSrchRequest request;
        @Mock
        private CommonRequestHeader header;
        @Mock
        private CustomizedEsIndexForProduct customizedEsIndexForProduct;

        @Test
        void test_predsrching() {
            when(request.getKeyword()).thenReturn("00005");
            when(header.getCountryCode()).thenReturn("HK");
            when(header.getGroupMember()).thenReturn("HBAP");
            when(header.getLocale()).thenReturn("en");
            when(request.getAssetClasses()).thenReturn(new String[]{"SEC"});
            when(request.getTopNum()).thenReturn("10");
            when(request.getMarket()).thenReturn("HK");
            when(request.isHouseViewFlag()).thenReturn(true);
            when(request.getHouseViewFilter()).thenReturn("Hold");
            when(elasticsearchTemplate.search(any(Query.class), any(), any())).thenReturn(searchHits());
            when(predsrchSortingOrderProperties.getSortingField()).thenReturn(sortingFieldMap());
            elasticsearchClientMock();
            List<PredSrchResponse> responseList = underTest.predsrch(request, header);
            assertEquals(1, responseList.size());
        }

        @Test
        void test_predsrchingCMB_I() throws Exception {
            when(request.getKeyword()).thenReturn("U");
            when(header.getCountryCode()).thenReturn("HK");
            when(header.getGroupMember()).thenReturn("HBAP");
            when(header.getChannelId()).thenReturn("OHI");
            when(header.getGbgf()).thenReturn("CMB");
            when(header.getLocale()).thenReturn("en");
            when(request.getAssetClasses()).thenReturn(new String[]{"UT"});
            when(request.getTopNum()).thenReturn("10");
             when(request.getMarket()).thenReturn("HK");
             when(elasticsearchTemplate.search(any(Query.class), any(), any())).thenReturn(utbProduct());
             when(predsrchSortingOrderProperties.getSortingField()).thenReturn(sortingFieldMap());
             elasticsearchClientMock();
            List<PredSrchResponse> responseList = underTest.predsrch(request, header);
            assertNotNull(responseList);
        }

        @Test
        void test_predsrchingCMB_B() throws Exception {
            when(request.getKeyword()).thenReturn("U1");
            when(header.getCountryCode()).thenReturn("HK");
            when(header.getGroupMember()).thenReturn("HBAP");
            when(header.getChannelId()).thenReturn("OHB");
            when(header.getGbgf()).thenReturn("CMB");
            when(header.getLocale()).thenReturn("en");
            when(request.getAssetClasses()).thenReturn(new String[]{"UT"});
            when(request.getTopNum()).thenReturn("10");
            when(request.getMarket()).thenReturn("HK");
            when(elasticsearchTemplate.search(any(Query.class), any(), any())).thenReturn(utbProduct());
            when(predsrchSortingOrderProperties.getSortingField()).thenReturn(sortingFieldMap());
            elasticsearchClientMock();
            List<PredSrchResponse> responseList = underTest.predsrch(request, header);
            assertNotNull(responseList);
        }

        @Test
        void test_predsrchingWPB() {
            when(request.getKeyword()).thenReturn("U1");
            when(header.getCountryCode()).thenReturn("HK");
            when(header.getGroupMember()).thenReturn("HBAP");
            when(header.getLocale()).thenReturn("en");
            when(request.getAssetClasses()).thenReturn(new String[]{"UT"});
            when(request.getTopNum()).thenReturn("10");
            when(request.getMarket()).thenReturn("HK");
            when(elasticsearchTemplate.search(any(Query.class), any(), any())).thenReturn(utbProduct());
            when(predsrchSortingOrderProperties.getSortingField()).thenReturn(sortingFieldMap());
            elasticsearchClientMock();
            List<PredSrchResponse> responseList = underTest.predsrch(request, header);
            assertNull(responseList.get(0).getWpbProductInd());

            SearchHits utProduct = utbProduct();
            CustomizedEsIndexForProduct prodInfo = (CustomizedEsIndexForProduct) utProduct.getSearchHit(0).getContent();
            prodInfo.setWpbProductInd("N");
            when(elasticsearchTemplate.search(any(Query.class), any(), any())).thenReturn(utProduct);
            responseList = underTest.predsrch(request, header);
            assertEquals("N", responseList.get(0).getWpbProductInd());
        }

        @ParameterizedTest
        @ValueSource(strings = {"allowSwOutProdInd", "switchableGroups"})
        void test_predsrching_filterCriterias(String switchOutFund) throws Exception {
            PredSrchRequest request = mock(PredSrchRequest.class);
            CommonRequestHeader header = mock(CommonRequestHeader.class);
            when(request.getKeyword()).thenReturn("00005");
            when(header.getCountryCode()).thenReturn("HK");
            when(header.getGroupMember()).thenReturn("HBAP");
            when(header.getLocale()).thenReturn("en");
            when(request.getAssetClasses()).thenReturn(new String[]{"SEC"});
            when(request.getTopNum()).thenReturn("10");
            when(request.getMarket()).thenReturn("HK");
            when(request.isHouseViewFlag()).thenReturn(true);
            when(request.getHouseViewFilter()).thenReturn("RU");
            when(request.getChannelRestrictCode()).thenReturn("xxx");
            when(request.getRestrOnlScribInd()).thenReturn("xxx");
            List<SearchCriteria> filterCriteria = new ArrayList<>();
            SearchCriteria searchCriteria1 = new SearchCriteria();
            searchCriteria1.setOperator("eq");
            searchCriteria1.setCriteriaKey("switchOutFund");
            searchCriteria1.setCriteriaValue("xxx:00005:HK:SEC");
            filterCriteria.add(searchCriteria1);
            when(request.getFilterCriterias()).thenReturn(filterCriteria);
            if ("allowSwOutProdInd".equals(switchOutFund)) {
                when(siteFeatureProperty.getStringDefaultFeature("HK_HBAP", PredictiveSearchConstant.SWITCHOUTFUND)).thenReturn("allowSwOutProdInd");
            } else {
                when(siteFeatureProperty.getStringDefaultFeature("HK_HBAP", PredictiveSearchConstant.SWITCHOUTFUND)).thenReturn("switchableGroups");
                when(siteFeatureProperty.getStringDefaultFeature("HK_HBAP", PredictiveSearchConstant.UNSWITCHOUTFUND)).thenReturn("unSwitchableList");
            }
            when(elasticsearchTemplate.search(any(Query.class), any(), any())).thenReturn(searchHits());
            when(predsrchSortingOrderProperties.getSortingField()).thenReturn(sortingFieldMap());
            elasticsearchClientMock();
            List<PredSrchResponse> responseList = underTest.predsrch(request, header);
            assertEquals(1, responseList.size());
        }

        @Test
        void test_predsrch_exceedTopNum() {
            PredSrchRequest request = mock(PredSrchRequest.class);
            CommonRequestHeader header = mock(CommonRequestHeader.class);
            when(request.getTopNum()).thenReturn("1000");
            assertThrows(ParameterException.class, () -> underTest.predsrch(request, header));
        }

        @Test
        void test_predsrch_invalidKeyword() throws Exception {
            PredSrchRequest request = mock(PredSrchRequest.class);
            CommonRequestHeader header = mock(CommonRequestHeader.class);
            when(request.getTopNum()).thenReturn("10");
            when(request.getKeyword()).thenReturn("?");
            when(request.getAssetClasses()).thenReturn(new String[]{"SEC"});
            when(header.getCountryCode()).thenReturn("HK");
            when(header.getGroupMember()).thenReturn("HBAP");
            when(header.getLocale()).thenReturn("en");
            assertEquals(0, underTest.predsrch(request, header).size());
        }

        @Test
        void test_updateAllowBuySellInd() {
            PredSrchResponse info = new PredSrchResponse();
            String methodName = "updateAllowBuySellInd";

            ReflectionTestUtils.invokeMethod(underTest, methodName, customizedEsIndexForProduct, info, "");
            assertNull(info.getAllowBuy());
            ReflectionTestUtils.invokeMethod(underTest, methodName, customizedEsIndexForProduct, info, "OHI");
            assertNull(info.getAllowBuy());

            when(customizedEsIndexForProduct.getChanlAllowBuy()).thenReturn(Stream.of("CMB_I", "N").collect(Collectors.toList()));
            ReflectionTestUtils.invokeMethod(underTest, methodName, customizedEsIndexForProduct, info, "OHI");
            assertNull(info.getAllowBuy());
            ReflectionTestUtils.invokeMethod(underTest, methodName, customizedEsIndexForProduct, info, "CMB_I");
            assertEquals("N", info.getAllowBuy());

            when(customizedEsIndexForProduct.getChanlAllowBuy()).thenReturn(Collections.emptyList());
            when(customizedEsIndexForProduct.getChanlAllowSell()).thenReturn(Stream.of("CMB_I", "Y").collect(Collectors.toList()));
            ReflectionTestUtils.invokeMethod(underTest, methodName, customizedEsIndexForProduct, info, "OHI");
            assertNull(info.getAllowSell());
            ReflectionTestUtils.invokeMethod(underTest, methodName, customizedEsIndexForProduct, info, "CMB_I");
            assertEquals("Y", info.getAllowSell());
        }

        @Test
        void test_updateSwitchInd() {
            PredSrchResponse info = new PredSrchResponse();
            String methodName = "updateSwitchInd";
            ReflectionTestUtils.invokeMethod(underTest, methodName, customizedEsIndexForProduct, info, "");
            assertNull(info.getAllowSwInProdInd());
            ReflectionTestUtils.invokeMethod(underTest, methodName, customizedEsIndexForProduct, info, "OHI");
            assertNull(info.getAllowSwInProdInd());

            when(customizedEsIndexForProduct.getChanlAllowSwitchIn()).thenReturn(Stream.of("CMB_I", "N").collect(Collectors.toList()));
            ReflectionTestUtils.invokeMethod(underTest, methodName, customizedEsIndexForProduct, info, "OHI");
            assertNull(info.getAllowSwInProdInd());
            ReflectionTestUtils.invokeMethod(underTest, methodName, customizedEsIndexForProduct, info, "CMB_I");
            assertEquals("N", info.getAllowSwInProdInd());

            when(customizedEsIndexForProduct.getChanlAllowSwitchIn()).thenReturn(Collections.emptyList());
            when(customizedEsIndexForProduct.getChanlAllowSwitchOut()).thenReturn(Stream.of("CMB_I", "Y").collect(Collectors.toList()));
            ReflectionTestUtils.invokeMethod(underTest, methodName, customizedEsIndexForProduct, info, "OHI");
            assertNull(info.getAllowSwOutProdInd());
            ReflectionTestUtils.invokeMethod(underTest, methodName, customizedEsIndexForProduct, info, "CMB_I");
            assertEquals("Y", info.getAllowSwOutProdInd());
        }
    }
}
