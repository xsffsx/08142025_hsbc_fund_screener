/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.news.controller;

import com.hhhh.group.secwealth.mktdata.api.equity.common.name.id.handler.NameIdHandler;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.news.constants.NewsConstant;
import com.hhhh.group.secwealth.mktdata.api.equity.news.request.NewsDetailRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.NewsDetailResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.NewsHeadlinesResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.news.request.NewsHeadlinesRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.header.RequestHeader;
import com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.parameter.JsonRequestParam;
import com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.controller.DispatcherController;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import com.hhhh.wmd.itt.web.bind.annotation.SamlParam;


@RestController("newsController")
@RequestMapping(value = "/wealth/api/v1/market-data/news", method = RequestMethod.GET//, produces = {"application/json; charset=utf-8","application/text; charset=utf-8","application/xml; charset=utf-8"}
 )
public class RESTfulController {

    @Autowired
    private DispatcherController dispatcherController;

    @Autowired
    private NameIdHandler nameIdHandler;


    @RequestMapping(value = "/equity/headlines", method = RequestMethod.GET)
    public NewsHeadlinesResponse equityHeadlines(@JsonRequestParam("param") final NewsHeadlinesRequest request,
                                                 @RequestHeader final CommonRequestHeader header, @SamlParam(name = "nameId", required = false) final String customerId)
            throws Exception {
        this.nameIdHandler.checkAndEncrypt(header, customerId, request.getProcessingFlag());

        String appCode = header.getAppCode();

        if(appCode!=null && appCode.equalsIgnoreCase("CMB")) {
            return this.dispatcherController.execute(NewsConstant.NEWS_HEADLINES_CMB_SERVICE_FLAG, header, request);
        } else {
            return this.dispatcherController.execute(NewsConstant.NEWS_HEADLINES_SERVICE_FLAG, header, request);
        }
    }

    @RequestMapping(value = "/equity/detail", method = RequestMethod.GET)
    public NewsDetailResponse equityDetail(@JsonRequestParam("param") final NewsDetailRequest request,
                                           @RequestHeader final CommonRequestHeader header, @SamlParam(name = "nameId", required = false) final String customerId)
            throws Exception {
        this.nameIdHandler.checkAndEncrypt(header, customerId, request.getProcessingFlag());

        String appCode = header.getAppCode();

        if(appCode!=null && appCode.equalsIgnoreCase("CMB")) {
            return this.dispatcherController.execute(NewsConstant.NEWS_DETAIL_CMB_SERVICE_FLAG, header, request);
        } else {
            return this.dispatcherController.execute(NewsConstant.NEWS_DETAIL_SERVICE_FLAG, header, request);
        }
    }

}