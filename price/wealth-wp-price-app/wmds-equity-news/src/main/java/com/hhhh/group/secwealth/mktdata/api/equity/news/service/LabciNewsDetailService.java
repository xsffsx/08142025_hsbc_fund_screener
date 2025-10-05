package com.hhhh.group.secwealth.mktdata.api.equity.news.service;

import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.LabciProtalTokenService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProtalProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.token.LabciToken;
import com.hhhh.group.secwealth.mktdata.api.equity.news.constants.NewsConstant;
import com.hhhh.group.secwealth.mktdata.api.equity.news.request.NewsDetailRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.NewsDetailResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.labci.Envelop;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@ConditionalOnProperty(value = "service.newsDetail.Labci.injectEnabled")
public class LabciNewsDetailService extends AbstractBaseService<NewsDetailRequest, NewsDetailResponse, CommonRequestHeader> {

    private static final Logger logger = LoggerFactory.getLogger(TrisNewsDetailService.class);

    @Autowired
    private HttpClientHelper httpClientHelper;

    @Autowired
    @Qualifier("labciProtalProperties")
    @Getter
    @Setter
    private LabciProtalProperties labciAmhUSProperties;

    @Autowired
    private LabciProtalTokenService labciProtalAmhTokenService;
    @Autowired
    private LabciAmhNewsCommonService labciAmhNewsCommonService;

    @Override
    protected Object convertRequest(NewsDetailRequest request, CommonRequestHeader header) throws Exception {
        String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));

        String message = labciAmhNewsCommonService.assembleRequest(request, header.getLocale());
        LabciToken tokenModel = labciAmhNewsCommonService.getTokenModel(header.getChannelId(), request.getMarket());
        String token = labciProtalAmhTokenService.encryptLabciToken(site, tokenModel);
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair(NewsConstant.HTTP_PARAM_MESSAGE, message));
        params.add(new BasicNameValuePair(NewsConstant.HTTP_PARAM_TOKEN, token));
        return params;
    }

    @Override
    protected Object execute(Object obj) throws Exception {
        List<NameValuePair> params = (List<NameValuePair>) obj;
        String xmlResp;
        try {
            xmlResp = this.httpClientHelper.doPost(labciAmhUSProperties.getProxyName(), labciAmhUSProperties.getQueryUrl(), params, null);
        } catch (Exception e) {
            logger.error("Access Labci encounter error", e);
            throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_LABCI_ERROR, e);
        }
        return xmlResp;
    }

    @Override
    protected Object validateServiceResponse(Object obj) throws Exception {
        String xmlResp = (String) obj;
        Envelop responseEnvelop = labciAmhNewsCommonService.getResponseEnvelop(xmlResp);
        if (!labciAmhNewsCommonService.NORMAL_RESPONSE_CODE.equals(responseEnvelop
                .getHeader().getResponsecode())) {
            logger.error("Labci praotal return invalid response: {}", responseEnvelop.getHeader().getErrormsg());
            throw new VendorException(ExCodeConstant.EX_CODE_LABCI_INVALID_RESPONSE);
        }
        return responseEnvelop;
    }

    @Override
    protected NewsDetailResponse convertResponse(Object obj) throws Exception {
        return labciAmhNewsCommonService.convertNewsDetailResponse((Envelop) obj);
    }

}
