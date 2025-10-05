/*
 */
package com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.utils;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CompletableFuture;

import org.junit.After;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResponse;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResult;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResultStateEnum;

public class CacheDistributeHolderTest {

    @After
    public void after() {
        CacheDistributeHolder.clearCacheDistribute();
    }

    @Test
    public void whenGetCacheDistributeInvoked_setValidResponseIntoFuture_shouldReturnOk() {
        CacheDistributeResponse response = new CacheDistributeResponse();
        response.setCacheExistingFlag("Y");
        CompletableFuture<CacheDistributeResponse> future = new CompletableFuture<>();
        future.complete(response);
        CacheDistributeHolder.putCacheDistribute(future);

        CacheDistributeResult result = CacheDistributeHolder.getCacheDistribute();
        assertEquals(CacheDistributeResultStateEnum.OK, result.getResultState());
    }

    @Test
    public void whenGetCacheDistributeInvoked_setUncachedResponseIntoFuture_shouldReturnUncachedRecord() {
        CacheDistributeResponse response = new CacheDistributeResponse();
        response.setCacheExistingFlag("N");
        CompletableFuture<CacheDistributeResponse> future = new CompletableFuture<>();
        future.complete(response);
        CacheDistributeHolder.putCacheDistribute(future);

        CacheDistributeResult result = CacheDistributeHolder.getCacheDistribute();
        assertEquals(CacheDistributeResultStateEnum.UNCACHED_RECORD, result.getResultState());
    }

    @Test
    public void whenGetCacheDistributeInvoked_setIllegalArgumentExceptionIntoFuture_shouldReturnInvalidParameters() {
        CompletableFuture<CacheDistributeResponse> future = new CompletableFuture<>();
        future.completeExceptionally(new IllegalArgumentException(
            "X-hhhh-User-Id and X-hhhh-Session-Correlation-Id are mandatory. X-hhhh-Saml and X-hhhh-Saml3 at least one exists."));
        CacheDistributeHolder.putCacheDistribute(future);

        CacheDistributeResult result = CacheDistributeHolder.getCacheDistribute();
        assertEquals(CacheDistributeResultStateEnum.INVALID_PARAMETERS, result.getResultState());
    }

    @Test
    public void whenGetCacheDistributeInvoked_setHttpClientErrorIntoFuture_shouldReturnInvalidHttpStatus() {
        CompletableFuture<CacheDistributeResponse> future = new CompletableFuture<>();
        future.completeExceptionally(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        CacheDistributeHolder.putCacheDistribute(future);

        CacheDistributeResult result = CacheDistributeHolder.getCacheDistribute();
        assertEquals(CacheDistributeResultStateEnum.INVALID_HTTP_STATUS, result.getResultState());
    }

    @Test
    public void whenGetCacheDistributeInvoked_setHttpServerErrorIntoFuture_shouldReturnInvalidHttpStatus() {
        CompletableFuture<CacheDistributeResponse> future = new CompletableFuture<>();
        future.completeExceptionally(new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE));
        CacheDistributeHolder.putCacheDistribute(future);

        CacheDistributeResult result = CacheDistributeHolder.getCacheDistribute();
        assertEquals(CacheDistributeResultStateEnum.INVALID_HTTP_STATUS, result.getResultState());
    }

}
