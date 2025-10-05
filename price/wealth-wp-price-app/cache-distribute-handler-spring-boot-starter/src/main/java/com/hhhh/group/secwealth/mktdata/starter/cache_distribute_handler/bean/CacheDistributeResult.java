/*
 */
package com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * <b> It's a wrapper class that adds result state
 * {@link CacheDistributeResultStateEnum} based on the native response
 * {@link CacheDistributeResponse} from the Cache Distribute. </b>
 * </p>
 */
@Getter
@Setter
public class CacheDistributeResult {

    private CacheDistributeResultStateEnum resultState;

    private CacheDistributeResponse response;

    public CacheDistributeResult(final CacheDistributeResultStateEnum resultState) {
        this.resultState = resultState;
    }

    public CacheDistributeResult(final CacheDistributeResultStateEnum resultState, final CacheDistributeResponse response) {
        this.resultState = resultState;
        this.response = response;
    }

}
