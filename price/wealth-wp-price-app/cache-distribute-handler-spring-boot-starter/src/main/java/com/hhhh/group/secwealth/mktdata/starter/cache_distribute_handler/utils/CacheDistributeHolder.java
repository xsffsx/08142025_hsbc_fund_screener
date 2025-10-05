/*
 */
package com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResponse;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResult;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResultStateEnum;

/**
 * <p>
 * <b> When you want to get cache record from the Cache Distribute, invoke
 * {@link CacheDistributeHolder.getCacheDistribute()} method. </b>
 * </p>
 */
public class CacheDistributeHolder {

    private final static Logger LOGGER = LoggerFactory.getLogger(CacheDistributeHolder.class);

    private static final ThreadLocal<CompletableFuture<CacheDistributeResponse>> CACHE_DISTRIBUTE = new ThreadLocal<>();

    public static void putCacheDistribute(final CompletableFuture<CacheDistributeResponse> value) {
        CacheDistributeHolder.CACHE_DISTRIBUTE.set(value);
    }

    public static CacheDistributeResult getCacheDistribute() {
        CompletableFuture<CacheDistributeResponse> value = CacheDistributeHolder.CACHE_DISTRIBUTE.get();
        CacheDistributeResult result;
        try {
            CacheDistributeResponse response = value.get();
            if ("Y".equals(response.getCacheExistingFlag())) {
                result = new CacheDistributeResult(CacheDistributeResultStateEnum.OK, response);
            } else {
                result = new CacheDistributeResult(CacheDistributeResultStateEnum.UNCACHED_RECORD, response);
            }
        } catch (InterruptedException | ExecutionException e) {
            if (e.getCause().getClass() == IllegalArgumentException.class) {
                CacheDistributeHolder.LOGGER.error("Invalid parameters", e);
                result = new CacheDistributeResult(CacheDistributeResultStateEnum.INVALID_PARAMETERS);
            } else {
                CacheDistributeHolder.LOGGER.error("Get cache record from the Cache Distribute encounter error", e);
                result = new CacheDistributeResult(CacheDistributeResultStateEnum.INVALID_HTTP_STATUS);
            }
        }
        return result;
    }

    public static void clearCacheDistribute() {
        CacheDistributeHolder.CACHE_DISTRIBUTE.remove();
    }

}
