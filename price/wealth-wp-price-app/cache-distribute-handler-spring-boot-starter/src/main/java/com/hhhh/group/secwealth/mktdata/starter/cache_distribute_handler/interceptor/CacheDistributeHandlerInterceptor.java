/*
 */
package com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.interceptor;

import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResponse;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.service.CacheDistributeService;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.utils.CacheDistributeHolder;

import lombok.Setter;

/**
 * <p>
 * <b> 1. Retrieve cache record from the Cache Distribute and store future
 * result {@link CompletableFuture} by use ThreadLocal before request
 * processing. <br/>
 * 2. Clear cache record after completion of request processing. </br>
 * </b>
 * </p>
 */
public class CacheDistributeHandlerInterceptor implements HandlerInterceptor {

    @Setter
    private CacheDistributeService service;

    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
        throws Exception {
        CompletableFuture<CacheDistributeResponse> cacheRecord = this.service.retrieveCacheRecord(request);
        CacheDistributeHolder.putCacheDistribute(cacheRecord);
        return true;
    }

    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
        final ModelAndView modelAndView) throws Exception {}

    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
        final Exception ex) throws Exception {
        CacheDistributeHolder.clearCacheDistribute();
    }

}
