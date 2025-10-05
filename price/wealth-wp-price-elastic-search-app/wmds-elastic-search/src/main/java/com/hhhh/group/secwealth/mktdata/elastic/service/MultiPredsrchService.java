package com.hhhh.group.secwealth.mktdata.elastic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.MultiPredSrchRequest;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

@Service
public class MultiPredsrchService extends AbstractBaseService<MultiPredSrchRequest, List<PredSrchResponse>, CommonRequestHeader> {

    @Autowired
    private PredsrchCommonService predsrchCommonService;

    @Override
    protected Object convertRequest(MultiPredSrchRequest request, CommonRequestHeader header) throws Exception {
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER, header);
        return request;
    }

    @Override
    protected Object execute(Object serviceRequest) throws Exception {
        return predsrchCommonService.multiPredsrch((MultiPredSrchRequest) serviceRequest, (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER));
    }

    @Override
    protected Object validateServiceResponse(Object serviceResponse) throws Exception {
        return serviceResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List<PredSrchResponse> convertResponse(Object validServiceResponse) throws Exception {
        List<PredSrchResponse> responses = (List<PredSrchResponse>) validServiceResponse;
        for (PredSrchResponse response : responses) {
            if ("NA".equals(response.getMarket())){
                response.setMarket("US");
            }
        }
        return responses;
    }

}
