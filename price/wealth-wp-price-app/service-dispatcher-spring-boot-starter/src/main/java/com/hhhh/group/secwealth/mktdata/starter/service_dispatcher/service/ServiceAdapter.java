/*
 */
package com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.hhhh.group.secwealth.mktdata.starter.core.service.Service;
import com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.constant.Constant;


@SuppressWarnings("rawtypes")
public class ServiceAdapter implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(ServiceAdapter.class);

    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * <p>
     * <b> Look up service object by id. </b>
     * </p>
     *
     * @param id
     * @return Service Object
     * @throws Exception
     */
    public Service lookupServiceById(final String id) throws Exception {

        Service service = null;
        try {
            service = this.applicationContext.getBean(id, Service.class);
        } catch (Exception ex) {
            ServiceAdapter.logger.error("Error to get service object with service id: " + id, ex);
            throw new Exception(Constant.EX_CODE_NO_AVAILABLE_SERVICE_MATCHED_ERROR);
        }
        return service;
    }
}
