package com.hhhh.group.secwealth.mktdata.common.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;

public abstract class AbstractService implements Service, ServiceExecutor {

    @Override
    public Object doService(final Object object) throws Exception {
        try {
            return execute(object);
        } catch (Exception e) {
            LogUtil.error(AbstractService.class, "Exception: AbstractService, doService, doService " + object.toString(), e);
            throw e;
        }
    }

    public abstract Object execute(Object object) throws Exception;

    // Create ThreadPool
    protected ExecutorService getThreadPool(final ServiceExecutor... services) throws Exception {
        return Executors.newFixedThreadPool(services.length);
    }
}
