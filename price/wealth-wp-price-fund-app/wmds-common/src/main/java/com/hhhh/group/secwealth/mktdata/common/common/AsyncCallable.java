
package com.hhhh.group.secwealth.mktdata.common.common;

import java.util.concurrent.Callable;

import com.hhhh.group.secwealth.mktdata.common.service.ServiceExecutor;

public class AsyncCallable implements Callable<Object> {

    private ServiceExecutor serviceExecutor;
    private Object object;

    public AsyncCallable(final ServiceExecutor serviceExecutor, final Object object) {
        this.serviceExecutor = serviceExecutor;
        this.object = object;
    }

    @Override
    public Object call() throws Exception {
        return this.serviceExecutor.execute(this.object);
    }
}