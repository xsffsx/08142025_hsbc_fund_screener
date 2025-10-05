/*
 */
package com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean;

/**
 * <p>
 * <b> Result state. </b> It is useful that you can make corresponding actions
 * based on the state. Such as trigger different levels of alert.
 * </p>
 */
public enum CacheDistributeResultStateEnum {

    /** Success request */
    OK,
    /** Bad request */
    INVALID_PARAMETERS,
    /** The Cache Distribute don't contains the key you sent */
    UNCACHED_RECORD,
    /** Get response from the Cache Distribute encounter error */
    INVALID_HTTP_STATUS;

}
