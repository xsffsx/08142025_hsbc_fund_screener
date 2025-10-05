package com.hhhh.group.secwealth.mktdata.elastic.service;

import com.google.common.collect.Lists;
import com.hhhh.group.secwealth.mktdata.elastic.properties.AppProperties;
import org.elasticsearch.action.admin.cluster.state.ClusterStateRequestBuilder;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PredSrchHealthCheckServiceTest {
    @Mock
    private ElasticsearchRestTemplate elasticsearchTemplate;
    @Mock
    private Client elasticsearchClient;
    @Mock
    private AppProperties appProperty;
    @InjectMocks
    private PredSrchHealthCheckService underTest;

    @Nested
    class WhenGettingStatus {

        @Test
        void test_getStatus(){
            when(appProperty.getSupportSites()).thenReturn(Lists.newArrayList("Default"));
            assertDoesNotThrow(() -> underTest.getStatus());
        }

        @Test
        void test_getStatusWithException(){
            when(appProperty.getSupportSites()).thenReturn(Lists.newArrayList("Default", "hk_hbap"));
            assertDoesNotThrow(() -> underTest.getStatus());
        }
    }

    @Nested
    class WhenCheckingElasticSrch {

        @Test
        void test_checkElasticSrch(){
            when(appProperty.getSupportSites()).thenReturn(Lists.newArrayList("Default"));
            assertNotNull(underTest.checkElasticSrch());
        }
    }

    @Nested
    class WhenGettingCurrentIndexName {

        @Test
        void test_gettingCurrentIndexName(){
            String currentIndexName = "hk_hbap_202106290110_1923_1624962445646";
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
            assertEquals(currentIndexName, underTest.getCurrentIndexName("hk_hbap"));
        }
    }
}
