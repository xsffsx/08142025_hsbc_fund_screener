/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.chart.controller;


import com.hhhh.group.secwealth.mktdata.api.equity.chart.constant.ChartConstant;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.request.ChartRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.ChartResponse;
import com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.header.RequestHeader;
import com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.parameter.JsonRequestParam;
import com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.controller.DispatcherController;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import com.hhhh.wmd.itt.web.bind.annotation.SamlParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController("chartController")
@RequestMapping(value = "/wealth/api/v1/market-data", method = RequestMethod.GET)
public class RESTfulController {


    @Autowired
    private DispatcherController dispatcherController;

    @RequestMapping(value = "/equity/charts/performance", method = RequestMethod.GET)
    public ChartResponse equityChart(@JsonRequestParam("param") final ChartRequest request,
                                     @RequestHeader final CommonRequestHeader header, @SamlParam(name = "nameId", required = false) final String customerId)
            throws Exception {

        String appCode = header.getAppCode();

        if(appCode!=null && appCode.equalsIgnoreCase("CMB")) {
            return this.dispatcherController.execute(ChartConstant.CHART_DATA_CMB_SERVICE_FLAG, header, request);
        } else {
            return this.dispatcherController.execute(ChartConstant.CHART_DATA_SERVICE_FLAG, header, request);
        }
    }

}


