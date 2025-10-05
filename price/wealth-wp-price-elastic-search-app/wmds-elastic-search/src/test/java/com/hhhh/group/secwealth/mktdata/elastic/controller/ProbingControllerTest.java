package com.hhhh.group.secwealth.mktdata.elastic.controller;

import com.google.common.collect.Lists;
import com.hhhh.group.secwealth.mktdata.elastic.properties.AppProperties;
import com.hhhh.group.secwealth.mktdata.elastic.service.PredsrchCommonService;
import com.hhhh.group.secwealth.mktdata.elastic.service.ScheduleDataLoadService;
import com.hhhh.group.secwealth.mktdata.starter.healthcheck.bean.Status;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.SystemException;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequestBuilder;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.cluster.state.ClusterStateRequestBuilder;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProbingControllerTest {
    @Mock
    private ElasticsearchRestTemplate elasticsearchTemplate;
    @Mock
    private Client elasticsearchClient;
    @Mock
    private AppProperties appProperty;
    @Mock
    private ScheduleDataLoadService scheduleDataLoadService;
    @Mock
    private PredsrchCommonService predsrchCommonService;
    @InjectMocks
    private ProbingController underTest;

    @Nested
    class WhenProbinging {

        @Test
        void test_probing() {
            assertNotNull(underTest.probing());
        }
    }

    @Nested
    class WhenCheckingPredictiveServer {

        @ParameterizedTest
        @ValueSource(strings = {"red", "green"})
        void test_checkPredictiveServer(String status) {
            AdminClient adminClient = mock(AdminClient.class);
            ClusterAdminClient clusterAdminClient = mock(ClusterAdminClient.class);
            ClusterHealthRequestBuilder healthRequestBuilder = mock(ClusterHealthRequestBuilder.class);
            ClusterHealthResponse clusterHealthResponse = mock(ClusterHealthResponse.class);
            when(elasticsearchClient.admin()).thenReturn(adminClient);
            when(adminClient.cluster()).thenReturn(clusterAdminClient);
            when(clusterAdminClient.prepareHealth()).thenReturn(healthRequestBuilder);
            when(healthRequestBuilder.get()).thenReturn(clusterHealthResponse);
            if ("red".equals(status)) {
                when(clusterHealthResponse.getStatus()).thenReturn(ClusterHealthStatus.RED);
                assertEquals(Status.FAILURE, underTest.checkPredictiveServer().getStatusCode());
            } else {
                when(clusterHealthResponse.getStatus()).thenReturn(ClusterHealthStatus.GREEN);
                assertEquals(Status.SUCCESS, underTest.checkPredictiveServer().getStatusCode());
            }
        }

        @Test
        void test_checkPredictiveServer_exception() {
            when(elasticsearchClient.admin()).thenThrow(SystemException.class);
            assertEquals(Status.FAILURE, underTest.checkPredictiveServer().getStatusCode());
        }
    }

    @Nested
    class WhenCheckingPredSrch {

        @Test
        void test_checkPredSrch() {
            List<String> supportSites = Lists.newArrayList("Default", "HK_HBAP");
            when(appProperty.getSupportSites()).thenReturn(supportSites);
            when(predsrchCommonService.getCurrentIndexName("HK_HBAP".toLowerCase())).thenReturn("hk_hbap_202106290110_1923_1624962445646");
            assertEquals(Status.FAILURE, underTest.checkPredSrch().getStatusCode());
        }
    }

    @Nested
    class WhenLoadingData {
        @Test
        void test_loadData() throws Exception {
            assertEquals("success", underTest.loadData());
        }
    }

    @Nested
    class WhenListingIndices {

        @Test
        void test_listIndices() {
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
            assertNotNull(underTest.listIndices());
        }
    }
}
