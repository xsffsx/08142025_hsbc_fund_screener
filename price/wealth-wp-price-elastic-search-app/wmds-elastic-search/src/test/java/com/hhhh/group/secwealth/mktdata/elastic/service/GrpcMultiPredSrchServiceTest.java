package com.hhhh.group.secwealth.mktdata.elastic.service;

import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ParameterException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.grpc.RpcMultiPredSrchRequest;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.grpc.RpcPredSrchResponse;
import io.grpc.stub.StreamObserver;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GrpcMultiPredSrchServiceTest {
    @Mock
    private PredsrchCommonService predsrchCommonService;
    @InjectMocks
    private GrpcMultiPredSrchService underTest;

    @Nested
    class WhenMultiingPredSrchByRpc {
        @Mock
        private StreamObserver<RpcPredSrchResponse> responseObserver;

        @Test
        void test_multiPredSrchByRpc() {
            RpcMultiPredSrchRequest build = RpcMultiPredSrchRequest.newBuilder()
                    .addAllKeyword(Lists.newArrayList("00005"))
                    .setMarket("HK")
                    .addAllAssetClass(Lists.newArrayList("SEC"))
                    .setTopNum("10")
                    .build();
            List<PredSrchResponse> list = new ArrayList<>();
            PredSrchResponse predSrchResponse = new PredSrchResponse();
            predSrchResponse.setSymbol("00005");
            predSrchResponse.setVaEtfIndicator("N");
            list.add(predSrchResponse);
            when(predsrchCommonService.multiPredsrch(any(), any())).thenReturn(list);
            assertDoesNotThrow(() -> underTest.multiPredSrchByRpc(build, responseObserver));
        }

        @Test
        void test_multiPredSrchByRpc_invalidRequest() {
            RpcMultiPredSrchRequest build = RpcMultiPredSrchRequest.newBuilder()
                    .addAllKeyword(Lists.newArrayList("00005"))
                    .setMarket("HK")
                    .addAllAssetClass(Lists.newArrayList("SEC"))
                    .setTopNum("10")
                    .build();
            List<PredSrchResponse> list = new ArrayList<>();
            PredSrchResponse predSrchResponse = new PredSrchResponse();
            predSrchResponse.setSymbol("00005");
            predSrchResponse.setVaEtfIndicator("N");
            list.add(predSrchResponse);
            doThrow(ParameterException.class).when(predsrchCommonService).multiPredsrch(any(), any());
            assertDoesNotThrow(() -> underTest.multiPredSrchByRpc(build, responseObserver));
        }
    }
}
