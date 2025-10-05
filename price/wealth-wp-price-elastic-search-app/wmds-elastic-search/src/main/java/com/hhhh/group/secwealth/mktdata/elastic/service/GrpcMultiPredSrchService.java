package com.hhhh.group.secwealth.mktdata.elastic.service;

import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.MultiPredSrchRequest;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.elastic.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ParameterException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.grpc.*;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@GrpcService
@Slf4j
public class GrpcMultiPredSrchService extends RpcMultiPredSrchServiceGrpc.RpcMultiPredSrchServiceImplBase {

    @Autowired
    private PredsrchCommonService predsrchCommonService;

    @Override
    public void multiPredSrchByRpc(RpcMultiPredSrchRequest request, StreamObserver<RpcPredSrchResponse> responseObserver) {
        try {
            RpcPredSrchResponse.Builder builder = RpcPredSrchResponse.newBuilder();
            if (null != request && ListUtil.isValid(request.getKeywordList())){
                MultiPredSrchRequest multiPredSrchRequest = new MultiPredSrchRequest();
                multiPredSrchRequest.setMarket(request.getMarket());
                multiPredSrchRequest.setAssetClasses(request.getAssetClassList().toArray(new String[request.getAssetClassCount()]));
                multiPredSrchRequest.setKeyword(request.getKeywordList().toArray(new String[request.getKeywordCount()]));
                multiPredSrchRequest.setTopNum(request.getTopNum());
                multiPredSrchRequest.setIsPreciseSrch(false);
                CommonRequestHeader header = new CommonRequestHeader();
                header.setCountryCode("HK");
                header.setGroupMember("HBAP");
                header.setLocale("en");
                List<PredSrchResponse> responses = this.predsrchCommonService.multiPredsrch(multiPredSrchRequest, header);
                if (ListUtil.isValid(responses)){
                    List<RpcResult> results = new ArrayList<>();
                    for (PredSrchResponse resp : responses) {
                        RpcResult.Builder resultBuilder = RpcResult.newBuilder();
                        resultBuilder.setSymbol(resp.getSymbol());
                        resultBuilder.setVaEtfIndicator("Y".equalsIgnoreCase(resp.getVaEtfIndicator()) ? "Y" : "N");
                        results.add(resultBuilder.build());
                    }
                    builder.addAllResult(results);
                }
            }
            RpcPredSrchResponse build = builder.build();
            responseObserver.onNext(build);
        } catch (ParameterException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Receive invalid parameter from client")
                    .withCause(e)
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.UNKNOWN
                    .withDescription("Encountered unknown exception when search")
                    .withCause(e)
                    .asRuntimeException());
        }
        responseObserver.onCompleted();
    }
}
