/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.topmover.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hhhh.group.secwealth.mktdata.api.equity.common.name.id.handler.NameIdHandler;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.request.TopMoverRequest;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.header.RequestHeader;
import com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.parameter.JsonRequestParam;
import com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.controller.DispatcherController;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import com.hhhh.wmd.itt.web.bind.annotation.SamlParam;


@RestController("topMoverController")
@RequestMapping(value = "/wealth/api/v1/market-data", method = RequestMethod.GET)
public class RESTfulController {

	@Autowired
    private DispatcherController dispatcherController;
	
	private static final String TOP_MOVER_SERVICE_API = "TopMover";

    private static final String TOP_MOVER_CMB_SERVICE_API = "TopMover4Cmb";

    @Autowired
    private NameIdHandler nameIdHandler;

    @RequestMapping(value = "/equity/quotes/top-mover", method = RequestMethod.GET)
    public Object equitytopmover(@JsonRequestParam("param") final TopMoverRequest request,
        @RequestHeader final CommonRequestHeader header, @SamlParam(name = "nameId", required = false) final String customerId)
        throws Exception {

        this.nameIdHandler.checkAndEncrypt(header, customerId, request.getProcessingFlag());

        String appCode = header.getAppCode();

        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_TOP_TEN_MOVERS_REQUEST, request);

        if(appCode!=null && appCode.equalsIgnoreCase("CMB")) {
            return this.dispatcherController.execute(RESTfulController.TOP_MOVER_CMB_SERVICE_API, header, request);
        } else {
            return this.dispatcherController.execute(RESTfulController.TOP_MOVER_SERVICE_API, header, request);
        }
    }
}