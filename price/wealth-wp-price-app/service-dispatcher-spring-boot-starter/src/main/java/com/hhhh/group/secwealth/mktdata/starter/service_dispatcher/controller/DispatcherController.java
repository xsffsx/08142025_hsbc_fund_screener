/*
 */
package com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hhhh.group.secwealth.mktdata.starter.core.service.Service;
import com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.constant.Constant;
import com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.service.DispatcherService;
import com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.service.ServiceAdapter;

import lombok.Setter;

public class DispatcherController {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherController.class);

    @Setter
    private DispatcherService dispatcherService;

    @Setter
    private ServiceAdapter serviceAdapter;

    /**
     * <p>
     * <b>Lookup service id and do service.</b>
     * </p>
     *
     * @param header
     * @param request
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <P> P execute(final String serviceApi, final Object header, final Object request) throws Exception {

        String serviceId = this.dispatcherService.getServiceId(serviceApi, header, request);
        if (serviceId == null) {
            DispatcherController.logger.error("Service id is empty when get service id with serviceApi: " + serviceApi);
            throw new Exception(Constant.EX_CODE_NO_AVAILABLE_SERVICE_MATCHED_ERROR);
        }

        Service service = this.serviceAdapter.lookupServiceById(serviceId);
        if (service == null) {
            DispatcherController.logger.error("Service object is null when lookup service by id:" + serviceId);
            throw new Exception(Constant.EX_CODE_NO_AVAILABLE_SERVICE_MATCHED_ERROR);
        }

        return (P) service.doService(request, header);
    }

}
