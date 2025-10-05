/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.index.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hhhh.group.secwealth.mktdata.api.equity.common.name.id.handler.NameIdHandler;
import com.hhhh.group.secwealth.mktdata.api.equity.index.request.IndexQuotesRequest;
import com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.header.RequestHeader;
import com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.parameter.JsonRequestParam;
import com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.controller.DispatcherController;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import com.hhhh.wmd.itt.web.bind.annotation.SamlParam;

@RestController("indexQuotesController")
@RequestMapping(value = "/wealth/api/v1/market-data", method = RequestMethod.GET)
public class IndexQuotesController {

	@Autowired
	private DispatcherController dispatcherController;

	@Autowired
	private NameIdHandler nameIdHandler;

	private static final String INDEX_QUOTE_SERVICE_API = "Indices";

	private static final String INDEX_QUOTE_CMB_SERVICE_API = "Indices4Cmb";

	@RequestMapping(value = "/index/quotes", method = RequestMethod.GET)
	public Object equityQuotes(@JsonRequestParam("param") final IndexQuotesRequest request,
			@RequestHeader final CommonRequestHeader header,
			@SamlParam(name = "nameId", required = false) final String customerId) throws Exception {

		this.nameIdHandler.checkAndEncrypt(header, customerId, request.getProcessingFlag());

		String appCode = header.getAppCode();

		if(appCode!=null && appCode.equalsIgnoreCase("CMB")) {
			return this.dispatcherController.execute(IndexQuotesController.INDEX_QUOTE_CMB_SERVICE_API, header, request);
		} else {
			return this.dispatcherController.execute(IndexQuotesController.INDEX_QUOTE_SERVICE_API, header, request);
		}
	}
}